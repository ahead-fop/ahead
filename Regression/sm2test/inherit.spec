state_machine root extends class wrffe {

   event_delivery receive_message(M m);
   no_transition { error((-1),m); }

   otherwise_default { stop_enter(m); }

   states start, stop;

   Exit start { /*testing*/; }
   Enter start { /* more testing; */; }

   public root() { current_state = start; }
}


state_machine test1 extends root {

   // test to see if otherwise_default is inherited by state one

   states one;

   edge t1 : start->one
   conditions m.msg == M.initialize
   do { /* nothing */; }

   edge t2 : one->stop
   conditions m.msg == M.eom
   do { /* more nothing */; }
}

state_machine test2 extends test1 {

   otherwise_default { stop_enter(m); }

   otherwise one { ignore_message(current_state, m); }

   Enter stop { start_enter(m); }

   public test2() { super(); }

   static int array[] = { M.deny, M.initialize, M.deny, M.deny, M.eom };

   public static void main(String args[]) {
      test2 sd = new test2();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
