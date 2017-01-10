SoUrCe RooT foo "xxx";

State_machine rp extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { Proceed(m); System.out.println( "nested ignoring message " + m.msg ); }

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


SoUrCe foo "xxx";

State_machine r extends rp {

   Prepare start { Proceed(m); System.out.println("preparing start"); }
   Enter start { Proceed(m); System.out.println("entering start"); }
   Exit start  { Proceed(m); System.out.println("exiting start"); }

   Prepare one { Proceed(m); System.out.println("preparing one"); }
   Enter one { Proceed(m); System.out.println("entering one"); }
   Exit one  { Proceed(m); System.out.println("exiting one"); }

   Prepare two { Proceed(m); System.out.println("preparing two"); }
   Enter two { Proceed(m); System.out.println("entering two"); }
   Exit two { Proceed(m); System.out.println("exiting two"); }

   Prepare three { Proceed(m); System.out.println("preparing three"); }
   Enter three { Proceed(m); System.out.println("entering three"); }
   Exit three { Proceed(m); System.out.println("exiting three"); }

   Prepare stop { Proceed(m); System.out.println("preparing stop"); }
   Enter stop { Proceed(m); System.out.println("entering stop"); }
   Exit stop  { Proceed(m); System.out.println("exiting stop"); }

   static int array[] = { M.one, M.two, M.two, M.two, M.three, M.eom };

   public static void main(String args[]) {
      r sd = new r();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

