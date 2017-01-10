State_machine nestederror extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { 
      System.out.println( "nested error handler " + m.msg );
      switch( m.msg ) {
      case M.initialize: Goto_state start(m); delivery(m); break;
      case M.one:        Goto_state m1(m); break;
      case M.two:        Goto_state m1(m); delivery(m);  break;
      case M.three:      Goto_state three(m); break;
      case M.four:       Goto_state m2(m); break;
      case M.eom:        Goto_state stop(m); break;
      case M.toc:        Goto_state common(m); break;
      }
   }

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

   public nestederror() { }

   static int array[] = { M.initialize, M.one, M.four, M.four, M.toc,
                          M.three, M.toc,
                          M.four, M.two, M.three, M.toc, M.eom };

   public static void main(String args[]) {
      nestederror sd = new nestederror();
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
   Otherwise_default { 
      System.out.println( "M1 error handler " + m.msg );
      switch( m.msg ) {
      case M.initialize: Goto_state stop(m); break;
      case M.one:        Goto_state start(m); break;
      case M.two:        Goto_state two(m); break;
      case M.three:      Goto_state stop(m); break;
      case M.four:       Goto_state stop(m); break;
      case M.eom:        Goto_state stop(m); break;
      case M.toc:        Goto_state stop(m); break;
      }
   }

   States two;

   Transition t11 : start->two
   conditions m.msg == M.two do { outstate("M1"); }

   Transition t12 : two->stop
   conditions m.msg == M.three || m.msg == M.toc do { outstate("M1"); }

}

State_machine m2sm extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { 
      System.out.println( "M2 error handler " + m.msg );
      switch( m.msg ) {
      case M.initialize: Goto_state stop(m); break;
      case M.one:        Goto_state stop(m); break;
      case M.two:        Goto_state stop(m); break;
      case M.three:      Goto_state stop(m); break;
      case M.four:       System.err.println("should never occur"); break;
      case M.eom:        Goto_state stop(m); break;
      case M.toc:        Goto_state stop(m); break;
      }
   }

   Transition t21 : start -> start conditions m.msg == M.four do { outstate("M2"); }

   Transition t22 : start -> stop conditions m.msg == M.toc do { outstate("M2"); }
}
