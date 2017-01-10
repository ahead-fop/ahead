// PLEASE NOTE ORDER matters in this file
// common must come first, mortar_fist error last

// common SD for wrffe_fist

State_machine wrffe_common_fist extends class wrffe 
   implements java.io.Serializable {

   Delivery_parameters(M m);
   Unrecognizable_state { error(-1,m); }
   Otherwise_default { ignore_message(current_state, m); }

   States  one, mission;

   Transition t1 : start -> one
      conditions m.msg == M.initialize
   do {  /* upstream(initialize) */; }

   Transition t2 : one -> stop
      conditions m.msg == M.deny
   do {  /* downstream(deny) */; }

   Transition t3 : one -> mission
      conditions m.msg == M.accept
   do {  /* downstream(accept) */; }

   Enter stop { Goto_state start(m); } 

   public wrffe_common_fist() { }

   void foo( int x ) { x = x*2; }
}

// MLRS-specific extension to common

State_machine wrffe_mlrs_fist extends wrffe_common_fist {

   Enter mission { Goto_state stop(m); }

   public wrffe_mlrs_fist() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_fist sd = new wrffe_mlrs_fist();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

// Mortar-specific extension to common

State_machine wrffe_mortar_fist extends wrffe_common_fist {

   Transition t4 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   Transition t5 : mission -> stop 
   conditions m.msg == M.eom
   do { /* upstream(eom) */; }

   public wrffe_mortar_fist() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept, M.shot, M.splash,
                          M.roundsComplete, M.eom };

   public static void main(String args[]) {
      wrffe_mortar_fist sd = new wrffe_mortar_fist();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

// Mortar-specific-error extension to mortar-fist

State_machine wrffe_mortar_fist_error extends wrffe_mortar_fist {

   Transition t6 : one -> mission
   conditions m.msg == M.shot
           || m.msg == M.splash
           || m.msg == M.roundsComplete
   do {  System.out.println("dropped accept message"); }

   Transition t7 : one -> stop
   conditions m.msg == M.eom
   do {  System.out.println("dropped accept message"); }

   public wrffe_mortar_fist_error() { super(); }

   static int array[] = { M.initialize, /*ignore*/ M.mfr, M.deny, 
                          M.initialize, M.accept, M.shot,/*ignore*/ M.mfr, M.splash,
                          M.roundsComplete, M.eom,
                          M.initialize, /*ignore*/ M.mfr, M.eom,
                          M.initialize, M.splash, M.eom };

   public static void main(String args[]) {
      wrffe_mortar_fist_error sd = new wrffe_mortar_fist_error();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
