// tests jampack produced SM files that have multiple refinements
// of exit, enter, ...

// tests if refinement of enter,etc. works across
// inheritance boundaries

State_machine r0 extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( m.msg ); }
   Otherwise_default { System.out.print( " message "); }

   States one;

   Transition e1: start -> one
   condition m.msg == M.one do { System.out.println(" e1"); }

   Transition e2 : one -> one
   condition m.msg == M.one do { System.out.println(" e2"); }

   Transition e3: one -> stop
   condition m.msg == M.eom do { System.out.println(" e3"); }

   Enter one { System.out.println( "one" ); }
   Enter one { System.out.print( "ing " ); }

   Exit one { System.out.println( "one" ); }
   Exit one { System.out.print( "ing " ); }

   Prepare one { System.out.println( "one" ); }
   Prepare one { System.out.print( "ing " ); }

   Otherwise one { System.out.println( "one" ); }
   Otherwise one { System.out.print( "wise " ); }

   Transition_action e1 { System.out.print("ition"); }
   Transition_action e2 { System.out.print("ition"); }
   Transition_action e3 { System.out.print("ition"); }
}

State_machine r1 extends r0 {
  
   Otherwise_default { System.out.print("ignoring "); }
   Enter one { System.out.print( "enter" ); }
   Exit one { System.out.print( "exit" ); }
   Prepare one { System.out.print( "prepar" ); }
   Otherwise one { System.out.print( "other" ); }
   Transition_action e1 { System.out.print("Trans"); }
   Transition_action e2 { System.out.print("Trans"); }
   Transition_action e3 { System.out.print("Trans"); }

   static int array[] = { M.one, M.one, M.four, M.one, M.eom, };

   public static void main(String args[]) {
      r1 sd = new r1();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
