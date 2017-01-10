state_diagram wrffe_mlrs extends wrffe {

   event_delivery receive_message(M m);
   no_transition { error(-1,m); }

   otherwise_default { ignore_message(current_state, m); }

   states start, one, stop;

   Exit start { /* determine unit capability */; unit_capable = true; }

   edge t1 : start -> one
   conditions m.msg == M.initialize && unit_capable
   do { /* set totaltime; downstream(accept) */;  }

   edge t2 : start -> stop
   conditions m.msg == M.initialize && !unit_capable
   do { /* downstream(deny) */; }

   edge t3 : one -> stop
   conditions m.msg == M.totaltime
   do { /* downstream(mfr) */ }

   boolean unit_capable;

   wrffe_mlrs() { current_state = start; }

   static int array[] = { M.initialize, M.totaltime };

   public static void main(String args[]) {
      wrffe_mlrs sd = new wrffe_mlrs();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
