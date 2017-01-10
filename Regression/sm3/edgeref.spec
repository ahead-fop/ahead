State_machine refinedTransitionParent extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "parent ignoring message " + m.msg ); }

   States one, two;

   Transition e1: start -> one
   conditions false do { System.out.println("1"); }

   Transition e2 : one -> two
   conditions false do { System.out.println("2"); }

   Transition e22: two -> two
   conditions m.msg == M.two do { System.out.println("22"); }

   Transition e3 : two -> stop 
   conditions false do { System.out.println("3"); }
}


State_machine edgeref extends refinedTransitionParent {

   Transition_test e1 m.msg == M.one ;
   Transition_action e1 { System.out.print("following "); super.e1_action(m); }

   Transition_test e2 m.msg == M.two ;
   Transition_action e2 { System.out.print("following ");  super.e2_action(m); }

   Transition_test e22 super.e22_test(m) || m.msg == M.four ;
   Transition_action e22 { System.out.print("following "); super.e22_action(m); }

   Transition_test e3 m.msg == M.eom ;
   Transition_action e3 { System.out.print("following "); super.e3_action(m); }

   static int array[] = { M.one, M.two, M.two, M.four, M.eom };

   public static void main(String args[]) {
      edgeref sd = new edgeref();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}

