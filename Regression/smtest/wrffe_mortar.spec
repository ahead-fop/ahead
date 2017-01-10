state_diagram wrffe_mortar extends wrffe {

   event_delivery receive_message(M m);
   no_transition { error(-1,m); }

   otherwise_default { ignore_message(current_state, m); }

   states start, one, two, three, four, stop;

   edge t1 : start -> one
   conditions m.msg == M.initialize
   do { /* set loadtime */; }

   edge t2 : one -> two
   conditions m.msg == M.loadtime
   do { /* set flighttime, set completetime, downstream(shot) */; }

   Exit two { high_angle = false; }

   edge t3 : two -> three
   conditions m.msg == M.flighttime && !high_angle
   do { ; }

   edge t4 : two -> three
   conditions m.msg == M.flighttime && high_angle
   do { /* downstream(splash) */; }

   edge t5 : two -> four
   conditions m.msg == M.completetime
   do { /* downstream(roundsCompleted) */; }

   edge t6 : three -> four
   conditions m.msg == M.completetime
   do { /* downstream(roundsCompleted) */; }

   edge t7 : four -> stop
   conditions m.msg == M.eom
   do { ; }

   Enter stop { stop_exit(m); start_enter(m); }

   boolean high_angle;

   wrffe_mortar() { current_state = start; }

   static int array[] = { M.initialize, M.loadtime, M.flighttime, 
                          M.completetime, M.eom, 
                          M.initialize, M.loadtime, M.completetime, M.eom };

   public static void main(String args[]) {
      wrffe_mortar sd = new wrffe_mortar();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
