State_machine refinedParent extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "nested ignoring message " + m.msg ); }

   States one, two, three;

   Transition e1: start -> one
   conditions m.msg == M.one do { /*init-action*/; }

   Transition e2 : one -> two
   conditions m.msg == M.two do { /* m2Transition-action */; }

   Transition e22: two -> two
   conditions m.msg == M.two do { /* m2Transition-action */; }

   Transition e3 : two -> three
   conditions m.msg == M.three do { /* t3Transition-action */; }

   Transition t4 : three -> stop
   conditions m.msg == M.eom do { /* t4Transition-action */; }
}


State_machine refined extends refinedParent {

   Prepare start { super.start_prepare(m); System.out.println("preparing start"); }
   Enter start { super.start_enter(m); System.out.println("entering start"); }
   Exit start  { super.start_exit(m); System.out.println("exiting start"); }

   Prepare one { super.one_prepare(m); System.out.println("preparing one"); }
   Enter one { super.one_enter(m); System.out.println("entering one"); }
   Exit one  { super.one_exit(m); System.out.println("exiting one"); }

   Prepare two { super.two_prepare(m); System.out.println("preparing two"); }
   Enter two { super.two_enter(m); System.out.println("entering two"); }
   Exit two { super.two_exit(m); System.out.println("exiting two"); }

   Prepare three { super.three_exit(m); System.out.println("preparing three"); }
   Enter three { super.three_enter(m); System.out.println("entering three"); }
   Exit three { super.three_exit(m); System.out.println("exiting three"); }

   Prepare stop { super.stop_prepare(m); System.out.println("preparing stop"); }
   Enter stop { super.stop_enter(m); System.out.println("entering stop"); }
   Exit stop  { super.stop_exit(m); System.out.println("exiting stop"); }

   static int array[] = { M.one, M.two, M.two, M.two, M.three, M.eom };

   public static void main(String args[]) {
      refined sd = new refined();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

