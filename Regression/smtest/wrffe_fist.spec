// PLEASE NOTE ORDER matters in this file
// common must come first, mortar_fist error last

// common SD for wrffe_fist

state_diagram wrffe_common_fist extends wrffe implements java.io.Serializable {

   event_delivery receive_message(M m);
   no_transition { error(-1,m); }

   states  start,    one,    mission,    stop;

   edge t1 : start -> one
      conditions m.msg == M.initialize
   do {  /* upstream(initialize) */; }

   edge t2 : one -> stop
      conditions m.msg == M.deny
   do {  /* downstream(deny) */; }

   edge t3 : one -> mission
      conditions m.msg == M.accept
   do {  /* downstream(accept) */; }

   Enter stop { stop_exit(m); start_enter(m); }

   public wrffe_common_fist() { current_state = start; }

   void foo( int x ) { x = x*2; }
}

// MLRS-specific extension to common

state_diagram wrffe_mlrs_fist refines wrffe_common_fist {

   Enter stop { stop_exit(m); start_enter(m); }   // for debugging only

   Enter mission { mission_exit(m); stop_enter(m); } 

   public wrffe_mlrs_fist() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_fist sd = new wrffe_mlrs_fist();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}

// Mortar-specific extension to common

state_diagram wrffe_mortar_fist refines wrffe_common_fist {

   edge t4 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   edge t5 : mission -> stop 
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
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}

// Mortar-specific-error extension to mortar-fist

state_diagram wrffe_mortar_fist_error refines wrffe_mortar_fist {

   otherwise_default { ignore_message(current_state, m); }

   edge t6 : one -> mission
   conditions m.msg == M.shot
           || m.msg == M.splash
           || m.msg == M.roundsComplete
   do {  System.out.println("dropped accept message"); }

   edge t7 : one -> stop
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
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
