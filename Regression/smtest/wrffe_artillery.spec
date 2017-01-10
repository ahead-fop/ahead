state_diagram wrffe_artillery extends wrffe {

   event_delivery receive_message(M m);
   no_transition { error(-1,m); }

   otherwise_default { ignore_message(current_state, m); }

   states start, one, two, stop;

   Exit start { /* determine unit capability */; unit_capable = true; }

   edge t1 : start -> one
   conditions m.msg == M.initialize && unit_capable
   do { /* set loadtime; downstream(accept) */;  }

   edge t2 : start -> stop
   conditions m.msg == M.initialize && !unit_capable
   do { /* downstream(deny) */; }

   edge t3 : one -> two
   conditions m.msg == M.loadtime
   do { /* set shot, set flighttime, set completetime */ }

   Exit two { high_angle = true; }

   edge t4 : two -> two
   conditions m.msg == M.flighttime && high_angle
   do { /* downstream(splash) */; }

   edge t5 : two -> two
   conditions m.msg == M.flighttime && !high_angle
   do { ; }

   edge t6 : two -> two
   conditions m.msg == M.completetime
   do { /* downstream(roundsCompleted) */; }

   edge t7 : two -> stop
   conditions m.msg == M.eom
   do { /* downstream(mfr) */ }

   Enter stop { stop_exit(m); start_enter(m); }  // debugging only

   boolean high_angle, unit_capable;

   wrffe_artillery() { current_state = start; }

   static int array[] = { M.initialize, M.loadtime, M.flighttime, 
                          M.completetime, M.eom,
                          M.initialize, M.loadtime, M.completetime, M.eom };

   public static void main(String args[]) {
      wrffe_artillery sd = new wrffe_artillery();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
