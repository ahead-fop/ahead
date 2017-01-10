// tests jampack produced SM files that have multiple refinements
// of exit, enter, ...

State_machine ref extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( m.msg ); Proceed(m); }
   Otherwise_default { System.out.print( "ignoring message "); Proceed(m); }

   States one;

   Transition e1: start -> one
   condition m.msg == M.one do { /*init-action*/; }

   Transition e2 : one -> one
   condition false do { /* m2Transition-action */; }

   Transition e3: one -> stop
   condition m.msg == M.eom do { /* m2Transition-action */; }

   Exit start  { System.out.println(" start"); }
   Exit start  { System.out.print("ing"); }
   Exit start  { System.out.print("exit"); }

   Otherwise one { System.out.println( "one " ); }
   Otherwise one { System.out.print("in state " ); }

   Prepare one { System.out.println(" one"); }
   Prepare one { System.out.print("ing"); }
   Prepare one { System.out.print("prepar"); }

   Enter one { System.out.println(" one"); }
   Enter one { System.out.print("ing"); }
   Enter one { System.out.print("enter"); }

   Exit one { System.out.println(" one"); }
   Exit one { System.out.print("ing"); }
   Exit one { System.out.print("exit"); }

   Exit stop  { System.out.println(" stop"); }
   Exit stop  { System.out.print("ing"); }
   Exit stop  { System.out.print("exit"); }

   Transition_condition e2 Proceed(m) || m.msg == M.two;
   Transition_condition e2 Proceed(m) || m.msg == M.three;
   Transition_condition e2 Proceed(m) || m.msg == M.one;

   static int array[] = { M.eom, M.one, M.four, M.two, M.two, M.one, M.three, 
                          M.four, M.eom };

   public static void main(String args[]) {
      ref sd = new ref();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

