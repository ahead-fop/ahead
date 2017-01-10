
// SD for wrffe_common_fo

State_machine wrffe_common_fo extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error(-1,m); }

   Otherwise_default { ignore_message(current_state, m); }

   States one, mission;

   Enter stop { Goto_state start(m); } 

   Transition t1 : start -> one
   conditions m.msg == M.initialize
   do {  /* upstream(initialize) */; }

   Transition t2 : one -> stop
   conditions m.msg == M.deny
   do { ; }

   Transition t3 : one -> mission
   conditions m.msg == M.accept
   do { ; }

   public wrffe_common_fo() { }
}

// SD for mlrs extension

State_machine wrffe_mlrs_fo extends wrffe_common_fo {

   Enter mission { Goto_state stop(m); } 

   public wrffe_mlrs_fo() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_fo sd = new wrffe_mlrs_fo();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

// extension for mortar and artillery 

State_machine wrffe_mortar_fo extends wrffe_common_fo {

   States three;

   Transition t4 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash
   do { ; }

   Transition t5 : mission -> three
   conditions m.msg == M.roundsComplete
   do { /* set(confirmEffects) = (TimeOfFlt+VisConf)*/; }

   Transition t6 : three -> three 
   conditions m.msg == M.splash
   do { /* set(confirmEffects) = greaterOf(ConfirmEffects, TimeToImpact+VisConf)*/; }

   Transition t7 : three -> stop
   conditions m.msg == M.confirmEffects
   do { /* upstream(eom) */; }

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
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
