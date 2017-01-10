// common wrffe repeater protocol

State_machine wrffe_common_repeater extends class wrffe {
 
   Delivery_parameters(M m);
   Unrecognizable_state { error(-1,m); }  

   Otherwise_default { ignore_message(current_state, m); }

   States one, three, mission;

   Prepare start {
     assets_available = true; 
     OK_to_elevate = true;
     assets_in_my_command = false;
   }

   Transition t1 : start -> mission
   condition m.msg == M.initialize && assets_in_my_command
   do { System.out.println("t1"); }

   Transition t2 : start -> one
   condition m.msg == M.initialize && !assets_in_my_command 
              && assets_available
   do { System.out.println("t2"); }

   Transition t3 : start -> three
   condition m.msg == M.initialize && !assets_in_my_command 
              && !assets_available && OK_to_elevate
   do { System.out.println("t3"); }

   Transition t4 : start -> stop
   condition m.msg == M.initialize && !assets_in_my_command
              && !assets_available && !OK_to_elevate
   do { System.out.println("t4"); }

   Prepare one { more_assets_available = true; }

   Transition t5 : one -> one
   condition m.msg == M.deny && more_assets_available
   do { System.out.println("t5"); }

   Transition t6 : one -> mission
   condition m.msg == M.accept
   do { System.out.println("t6"); }

   Transition t7 : one -> three
   condition m.msg == M.deny && !more_assets_available && OK_to_elevate
   do { System.out.println("t7"); }

   Transition t8 : three -> mission
   condition m.msg == M.accept
   do { System.out.println("t8"); }

   Transition t9 : three -> stop
   condition m.msg == M.deny
   do { System.out.println("t9"); }

   Transition t10 : one -> stop
   condition m.msg == M.deny && !more_assets_available && !OK_to_elevate
   do { System.out.println("t10"); }

   boolean more_assets_available = false, OK_to_elevate = false;
   boolean assets_in_my_command = false;
   boolean assets_available = false;

   public wrffe_common_repeater() { }
}

// MLRS-specific extension

State_machine wrffe_mlrs_repeater extends wrffe_common_repeater {

   Enter mission { Goto_state stop(m); } 

   public wrffe_mlrs_repeater() { }

   static int array[] = { M.initialize, M.deny, M.deny, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_repeater sd = new wrffe_mlrs_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

// mortar specific extension

State_machine wrffe_mortar_repeater extends wrffe_common_repeater {

   Transition t20 : mission -> mission
   condition m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { System.out.println("t20"); }

   Transition t21 : mission -> stop 
   condition m.msg == M.eom
   do { System.out.println("t21"); }

   public wrffe_mortar_repeater() { }

   static int array[] = { M.initialize, M.deny, M.accept,
                          M.shot, M.splash, M.roundsComplete, M.eom };

   public static void main(String args[]) {
      wrffe_mortar_repeater sd = new wrffe_mortar_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

// artillery-specific extension

State_machine wrffe_artillery_repeater extends wrffe_common_repeater {

   States four;

   Transition t20 : mission -> mission
   condition m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   Transition t21 : mission -> four
   condition m.msg == M.eom
   do { /* upstream(eom) */; }

   Transition t22 : four -> stop
   condition m.msg == M.mfr
   do { /* downstream(mfr) */; }

   public wrffe_artillery_repeater() { }

   static int array[] = { M.initialize, M.deny, M.accept,
                          M.shot, M.splash, M.roundsComplete, M.eom,
                          M.mfr };

   public static void main(String args[]) {
      wrffe_artillery_repeater sd = new wrffe_artillery_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}


