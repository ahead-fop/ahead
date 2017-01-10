// transitions that are grafted onto a state machine after
// its definition causes some problems with nested states

State_machine n3 extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "n2 ignoring message " + m.msg ); }

   States three;
   Nested_state m1 : new m3sm();

   Transition end : three -> stop
      condition m.msg == M.eom do { }
}

State_machine n2 extends n3 {

   Transition init : start -> m1
   condition m.msg == M.initialize do { /*init-action*/; }

   Transition m1 : m1 -> three 
   condition m.msg == M.three do { /* m1Transition-action */; }

   public n2() { }

   static int array[] = { M.initialize, M.two, M.three, M.eom };

   public static void main(String args[]) {
      n2 sd = new n2();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

State_machine m3sm extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "m1 ignoring message " + m.msg ); }

   States two;

   Transition t11 : start->two
   condition m.msg == M.two do { outstate("M1"); }

   Transition t12 : two->stop
   condition m.msg == M.three || m.msg == M.toc do { outstate("M1"); }

}
