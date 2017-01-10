
// SD for wrffe_common_fo

state_machine wrffe_common_fo extends class wrffe {

   event_delivery receive_message(M m);
   no_transition { error(-1,m); }

   otherwise_default { ignore_message(current_state, m); }

   states start, one, mission, stop;

   edge t1 : start -> one
   conditions m.msg == M.initialize
   do {  /* upstream(initialize) */; }

   edge t2 : one -> stop
   conditions m.msg == M.deny
   do { ; }

   edge t3 : one -> mission
   conditions m.msg == M.accept
   do { ; }

   public wrffe_common_fo() { current_state = start; }
}

// SD for mlrs extension

state_machine wrffe_mlrs_fo extends wrffe_common_fo {

   Enter stop { stop_exit(m); start_enter(m); }   // for debugging only

   Enter mission { mission_exit(m); stop_enter(m); } 

   public wrffe_mlrs_fo() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_fo sd = new wrffe_mlrs_fo();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}

// extension for mortar and artillery 

state_machine wrffe_mortar_fo extends wrffe_common_fo {

   states three;

   edge t4 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash
   do { ; }

   edge t5 : mission -> three
   conditions m.msg == M.roundsComplete
   do { /* set(confirmEffects) = (TimeOfFlt+VisConf)*/; }

   edge t6 : three -> three 
   conditions m.msg == M.splash
   do { /* set(confirmEffects) = greaterOf(ConfirmEffects, TimeToImpact+VisConf)*/; }

   edge t7 : three -> stop
   conditions m.msg == M.confirmEffects
   do { /* upstream(eom) */; }

   Enter stop { stop_exit(m); start_enter(m); }   // for debugging only

   public wrffe_mortar_fo() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept, M.shot, M.splash,
                          M.roundsComplete, M.confirmEffects };

   public static void main(String args[]) {
      wrffe_mortar_fo sd = new wrffe_mortar_fo();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
