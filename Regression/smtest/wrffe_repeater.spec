// common wrffe repeater protocol

state_diagram wrffe_common_repeater extends wrffe {
 
   event_delivery receive_message(M m);
   no_transition { error(-1,m); }  

   otherwise_default { ignore_message(current_state, m); }

   states start, one, three, mission, stop;

   Exit start { 
     assets_available = true; 
     OK_to_elevate = true;
     assets_in_my_command = false;
   }

   edge t1 : start -> mission
   conditions m.msg == M.initialize && assets_in_my_command
   do { /* upstream(initialize), downstream(accept) */; }

   edge t2 : start -> one
   conditions m.msg == M.initialize && !assets_in_my_command 
              && assets_available
   do { /* upstream(initialize) */; }

   edge t3 : start -> three
   conditions m.msg == M.initialize && !assets_in_my_command 
              && !assets_available && OK_to_elevate
   do { /* superior(initialize) */; }

   edge t4 : start -> stop
   conditions m.msg == M.initialize && !assets_in_my_command
              && !assets_available && !OK_to_elevate
   do { /* downstream(deny) */; }

   Exit one { more_assets_available = true; }

   edge t5 : one -> one
   conditions m.msg == M.deny && more_assets_available
   do { /* upstream(initialize) */; }

   edge t6 : one -> mission
   conditions m.msg == M.accept
   do { /* downstream(accept) */; }

   edge t7 : one -> three
   conditions m.msg == M.deny && !more_assets_available && OK_to_elevate
   do { /* superior(initialize) */; }

   edge t8 : three -> mission
   conditions m.msg == M.accept
   do { /* downstream(accept) */; }

   edge t9 : three -> stop
   conditions m.msg == M.deny
   do { /* downstream(deny) */; }

   edge t10 : one -> stop
   conditions m.msg == M.deny && !more_assets_available && !OK_to_elevate
   do { /* downstream(deny) */; }

   boolean more_assets_available, OK_to_elevate, assets_in_my_command;
   boolean assets_available;

   public wrffe_common_repeater() { current_state = start; }

}

// MLRS-specific extension

state_diagram wrffe_mlrs_repeater refines wrffe_common_repeater {

   Enter mission { mission_exit(m); stop_enter(m); } 

   // check out edge refinement - here we simply replace original
   // definition with a copy...

   refines edge t1 : start -> mission
   conditions m.msg == M.initialize && assets_in_my_command
   do { /* upstream(initialize), downstream(accept) */; }

   // here we simply call the original definition...
   // the net effect is that nothing should change...

   refines edge t2 : start -> one
   conditions super.t2_test(m)
   do { super.t2_action(m); }

   public wrffe_mlrs_repeater() { super(); }

   static int array[] = { M.initialize, M.deny, M.deny, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_repeater sd = new wrffe_mlrs_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}

// mortar specific extension

state_diagram wrffe_mortar_repeater refines wrffe_common_repeater {

   edge t20 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   edge t21 : mission -> stop 
   conditions m.msg == M.eom
   do { /* upstream(eom) */; }

   public wrffe_mortar_repeater() { super(); }

   static int array[] = { M.initialize, M.deny, M.accept,
                          M.shot, M.splash, M.roundsComplete, M.eom };

   public static void main(String args[]) {
      wrffe_mortar_repeater sd = new wrffe_mortar_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}

// artillery-specific extension

state_diagram wrffe_artillery_repeater refines wrffe_common_repeater {

   states four;

   edge t20 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   edge t21 : mission -> four
   conditions m.msg == M.eom
   do { /* upstream(eom) */; }

   edge t22 : four -> stop
   conditions m.msg == M.mfr
   do { /* downstream(mfr) */; }

   public wrffe_artillery_repeater() { super(); }

   static int array[] = { M.initialize, M.deny, M.accept,
                          M.shot, M.splash, M.roundsComplete, M.eom,
                          M.mfr };

   public static void main(String args[]) {
      wrffe_artillery_repeater sd = new wrffe_artillery_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}


