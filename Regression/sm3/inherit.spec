State_machine root extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }

   Otherwise_default { stop_enter(m); }

   Exit start { /*testing*/; }
   Enter start { /* more testing; */; }
   Enter stop { start_enter(m); }

   public root() { }
}


State_machine test1 extends root {

   // test to see if Otherwise_default is inherited by state one

   States one;

   Otherwise one { ignore_message(current_state, m); }

   Transition t1 : start->one
   conditions m.msg == M.initialize
   do { /* nothing */; }

   Transition t2 : one->stop
   conditions m.msg == M.eom
   do { /* more nothing */; }
}

State_machine test2 extends test1 {


   public test2() { super(); }

   static int array[] = { M.deny, M.initialize, M.deny, M.deny, M.eom };

   public static void main(String args[]) {
      test2 sd = new test2();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
