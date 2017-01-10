State_machine wrffe_mortar extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error(-1,m); }

   Otherwise_default { Proceed(m); ignore_message(current_state, m); }

   States one, two, three, four;

   Transition t1 : start -> one
   condition m.msg == M.initialize
   do { /* set loadtime */; }

   Transition t2 : one -> two
   condition m.msg == M.loadtime
   do { /* set flighttime, set completetime, downstream(shot) */; }

   Exit two { Proceed(m); high_angle = false; }

   Transition t3 : two -> three
   condition m.msg == M.flighttime && !high_angle
   do { ; }

   Transition t4 : two -> three
   condition m.msg == M.flighttime && high_angle
   do { /* downstream(splash) */; }

   Transition t5 : two -> four
   condition m.msg == M.completetime
   do { /* downstream(roundsCompleted) */; }

   Transition t6 : three -> four
   condition m.msg == M.completetime
   do { /* downstream(roundsCompleted) */; }

   Transition t7 : four -> stop
   condition m.msg == M.eom
   do { ; }

   Enter stop { Proceed(m); Goto_state start(m); }

   boolean high_angle;

   wrffe_mortar() { }

   static int array[] = { M.initialize, M.loadtime, M.flighttime, 
                          M.completetime, M.eom, 
                          M.initialize, M.loadtime, M.completetime, M.eom };

   public static void main(String args[]) {
      wrffe_mortar sd = new wrffe_mortar();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
