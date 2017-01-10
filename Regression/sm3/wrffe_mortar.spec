State_machine wrffe_mortar extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error(-1,m); }

   Otherwise_default { ignore_message(current_state, m); }

   States one, two, three, four;

   Transition t1 : start -> one
   conditions m.msg == M.initialize
   do { /* set loadtime */; }

   Transition t2 : one -> two
   conditions m.msg == M.loadtime
   do { /* set flighttime, set completetime, downstream(shot) */; }

   Exit two { high_angle = false; }

   Transition t3 : two -> three
   conditions m.msg == M.flighttime && !high_angle
   do { ; }

   Transition t4 : two -> three
   conditions m.msg == M.flighttime && high_angle
   do { /* downstream(splash) */; }

   Transition t5 : two -> four
   conditions m.msg == M.completetime
   do { /* downstream(roundsCompleted) */; }

   Transition t6 : three -> four
   conditions m.msg == M.completetime
   do { /* downstream(roundsCompleted) */; }

   Transition t7 : four -> stop
   conditions m.msg == M.eom
   do { ; }

   Enter stop { Goto_state start(m); }

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
