State_machine starroot extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "ignoring message " + m.msg ); }

   Transition t0 : * -> stop
   conditions m.msg != M.initialize && m.msg != M.eom
   /* do { } */
   do { System.out.println("going to stop on " + m.msg); }

   Exit start { /*testing*/; }
   Enter start { /* more testing; */; }
   Enter stop { start_enter(m); }

   public starroot() { }
}


State_machine startest1 extends starroot {

   States one;

   Otherwise one { ignore_message(current_state, m); }

   Transition t1 : start->one
   conditions m.msg == M.initialize
   do { /* nothing */; }

   Transition t2 : one->stop
   conditions m.msg == M.eom
   do { /* more nothing */; }
}

State_machine star extends startest1 {

   Otherwise_default {
      super.otherwise_Default(m);
      System.out.println("inheritance works");
   }

   Transition t100 : * -> stop
   conditions false
   do { /* nothing */ }

   public star() { super(); }

   static int array[] = { M.deny, M.initialize, M.deny, 
                          M.initialize, M.eom, M.eom };

   public static void main(String args[]) {
      star sd = new star();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
