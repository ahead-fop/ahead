state_machine starroot extends class wrffe {

   event_delivery receive_message(M m);

   no_transition { error((-1),m); }
   otherwise_default { System.out.println( "ignoring message " + m.msg ); }

   states start, stop;

   edge t0 : * -> stop
   conditions m.msg != M.initialize && m.msg != M.eom
   /* do { } */
   do { System.out.println("going to stop on " + m.msg); }

   Exit start { /*testing*/; }
   Enter start { /* more testing; */; }

   public starroot() { current_state = start; }
}


state_machine startest1 extends starroot {

   states one;

   edge t1 : start->one
   conditions m.msg == M.initialize
   do { /* nothing */; }

   edge t2 : one->stop
   conditions m.msg == M.eom
   do { /* more nothing */; }
}

state_machine star extends startest1 {

   otherwise_default { super.otherwise_Default(m); 
                       System.out.println("inheritance works"); }

   otherwise one { ignore_message(current_state, m); }

/*
   refines edge t0 : * -> stop
   conditions super.t0_test(m)
   do { System.out.println("going to stop on " + m.msg); }
*/

   edge t100 : * -> stop
   conditions false
   do { /* nothing */ }

   Enter stop { start_enter(m); }

   public star() { super(); }

   static int array[] = { M.deny, M.initialize, M.deny, 
                          M.initialize, M.eom, M.eom };

   public static void main(String args[]) {
      star sd = new star();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
