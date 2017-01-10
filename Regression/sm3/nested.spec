State_machine nested extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "nested ignoring message " + m.msg ); }

   States common, three;
   Nested_state m1 : new m1sm();
   Nested_state m2 : new m2sm();

   Transition init : start -> common
   conditions m.msg == M.initialize do { /*init-action*/; }

   Transition m1 : common -> m1
   conditions m.msg == M.one do { /* m1Transition-action */; }

   Transition m2 : common -> m2
   conditions m.msg == M.four do { /* m2Transition-action */; }

   Transition t3 : m1 -> three
   conditions m.msg == M.three do { /* t3Transition-action */; }

   Transition t4 : three -> common
   conditions m.msg == M.toc do { /* t4Transition-action */; }

   Transition tc : m1 -> common
   conditions m.msg == M.toc  do { /* tcTransition-action */; }

   Transition tc1 : m2 -> common
   conditions m.msg == M.toc  do { /* tc1Transition-action */; }

   Transition end : common -> stop
   conditions m.msg == M.eom do { /* endTransition-action */; }

   public nested() { }

   static int array[] = { M.initialize, M.one, M.two, M.three, M.toc,
                                  M.one, M.two, M.toc,
                                  M.four, M.four, M.four, M.toc,
                                  M.eom };

   public static void main(String args[]) {
      nested sd = new nested();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}


State_machine m1sm extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "m1 ignoring message " + m.msg ); }

   States two;

   Transition t11 : start->two
   conditions m.msg == M.two do { outstate("M1"); }

   Transition t12 : two->stop
   conditions m.msg == M.three || m.msg == M.toc do { outstate("M1"); }

}

State_machine m2sm extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "m2 ignoring message " + m.msg ); }

   Transition t21 : start -> start conditions m.msg == M.four do { outstate("M2"); }

   Transition t22 : start -> stop conditions m.msg == M.toc do { outstate("M2"); }
}
