State_machine refinedParent extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "nested ignoring message " + m.msg ); }

   States one, two, three;

   Transition e1: start -> one
   condition m.msg == M.one do { /*init-action*/; }

   Transition e2 : one -> two
   condition m.msg == M.two do { /* m2Transition-action */; }

   Transition e22: two -> two
   condition m.msg == M.two do { /* m2Transition-action */; }

   Transition e3 : two -> three
   condition m.msg == M.three do { /* t3Transition-action */; }

   Transition t4 : three -> stop
   condition m.msg == M.eom do { /* t4Transition-action */; }
}


State_machine refined extends refinedParent {

   Prepare start { System.out.println("preparing start"); }
   Enter start { System.out.println("entering start"); }
   Exit start  { System.out.println("exiting start"); }

   Prepare one { System.out.println("preparing one"); }
   Enter one { System.out.println("entering one"); }
   Exit one  { System.out.println("exiting one"); }

   Prepare two { System.out.println("preparing two"); }
   Enter two { System.out.println("entering two"); }
   Exit two { System.out.println("exiting two"); }

   Prepare three { System.out.println("preparing three"); }
   Enter three { System.out.println("entering three"); }
   Exit three { System.out.println("exiting three"); }

   Prepare stop { System.out.println("preparing stop"); }
   Enter stop { System.out.println("entering stop"); }
   Exit stop  { System.out.println("exiting stop"); }

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

