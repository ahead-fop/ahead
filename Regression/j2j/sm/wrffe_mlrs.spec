State_machine wrffe_mlrs extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error(-1,m); }

   Otherwise_default { Proceed(m); ignore_message(current_state, m); }

   States one;

   Prepare start { Proceed(m); /* determine unit capability */; unit_capable = true; }

   Transition t1 : start -> one
   condition m.msg == M.initialize && unit_capable
   do { /* set totaltime; downstream(accept) */;  }

   Transition t2 : start -> stop
   condition m.msg == M.initialize && !unit_capable
   do { /* downstream(deny) */; }

   Transition t3 : one -> stop
   condition m.msg == M.totaltime
   do { /* downstream(mfr) */ }

   boolean unit_capable;

   wrffe_mlrs() { }

   static int array[] = { M.initialize, M.totaltime };

   public static void main(String args[]) {
      wrffe_mlrs sd = new wrffe_mlrs();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
