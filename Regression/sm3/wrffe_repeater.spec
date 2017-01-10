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
   conditions m.msg == M.initialize && assets_in_my_command
   do { /* upstream(initialize), downstream(accept) */; }

   Transition t2 : start -> one
   conditions m.msg == M.initialize && !assets_in_my_command 
              && assets_available
   do { /* upstream(initialize) */; }

   Transition t3 : start -> three
   conditions m.msg == M.initialize && !assets_in_my_command 
              && !assets_available && OK_to_elevate
   do { /* superior(initialize) */; }

   Transition t4 : start -> stop
   conditions m.msg == M.initialize && !assets_in_my_command
              && !assets_available && !OK_to_elevate
   do { /* downstream(deny) */; }

   Prepare one { more_assets_available = true; }

   Transition t5 : one -> one
   conditions m.msg == M.deny && more_assets_available
   do { /* upstream(initialize) */; }

   Transition t6 : one -> mission
   conditions m.msg == M.accept
   do { /* downstream(accept) */; }

   Transition t7 : one -> three
   conditions m.msg == M.deny && !more_assets_available && OK_to_elevate
   do { /* superior(initialize) */; }

   Transition t8 : three -> mission
   conditions m.msg == M.accept
   do { /* downstream(accept) */; }

   Transition t9 : three -> stop
   conditions m.msg == M.deny
   do { /* downstream(deny) */; }

   Transition t10 : one -> stop
   conditions m.msg == M.deny && !more_assets_available && !OK_to_elevate
   do { /* downstream(deny) */; }

   boolean more_assets_available, OK_to_elevate, assets_in_my_command;
   boolean assets_available;

   public wrffe_common_repeater() { }
}

// MLRS-specific extension

State_machine wrffe_mlrs_repeater extends wrffe_common_repeater {

   Enter mission { Goto_state stop(m); } 

   public wrffe_mlrs_repeater() { super(); }

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
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   Transition t21 : mission -> stop 
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
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

// artillery-specific extension

State_machine wrffe_artillery_repeater extends wrffe_common_repeater {

   States four;

   Transition t20 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   Transition t21 : mission -> four
   conditions m.msg == M.eom
   do { /* upstream(eom) */; }

   Transition t22 : four -> stop
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
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}


