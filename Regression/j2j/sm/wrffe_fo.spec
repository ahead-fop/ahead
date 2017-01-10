
// SD for wrffe_common_fo

State_machine wrffe_common_fo extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error(-1,m); }

   Otherwise_default { Proceed(m); ignore_message(current_state, m); }

   States one, mission;

   Enter stop { Proceed(m); Goto_state start(m); } 

   Transition t1 : start -> one
   condition m.msg == M.initialize
   do {  /* upstream(initialize) */; }

   Transition t2 : one -> stop
   condition m.msg == M.deny
   do { ; }

   Transition t3 : one -> mission
   condition m.msg == M.accept
   do { ; }

   public wrffe_common_fo() { }
}

// SD for mlrs extension

State_machine wrffe_mlrs_fo extends wrffe_common_fo {

   Enter mission { Proceed(m); Goto_state stop(m); } 

   public wrffe_mlrs_fo() { }

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
   condition m.msg == M.shot ||
              m.msg == M.splash
   do { ; }

   Transition t5 : mission -> three
   condition m.msg == M.roundsComplete
   do { /* set(confirmEffects) = (TimeOfFlt+VisConf)*/; }

   Transition t6 : three -> three 
   condition m.msg == M.splash
   do { /* set(confirmEffects) = greaterOf(ConfirmEffects, TimeToImpact+VisConf)*/; }

   Transition t7 : three -> stop
   condition m.msg == M.confirmEffects
   do { /* upstream(eom) */; }

   public wrffe_mortar_fo() { }

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
