State_machine starroot extends class wrffe {

   Delivery_parameters(M m);
   Unrecognizable_state { error((-1),m); }
   Otherwise_default { System.out.println( "ignoring message " + m.msg ); }

   Transition t0 : * -> stop
   condition m.msg != M.initialize && m.msg != M.eom
   do { System.out.println("going to stop on " + m.msg); }

   Enter stop { Goto_state start(m); }

   public starroot() { }
}


State_machine startest1 extends starroot {

   States one;

   Otherwise one { ignore_message(current_state, m); if (false) Proceed(m); }

   Transition t1 : start->one
   condition m.msg == M.initialize
   do { /* nothing */; }

   Transition t2 : one->stop
   condition m.msg == M.eom
   do { /* more nothing */; }
}

State_machine star extends startest1 {

   Otherwise_default { System.out.println("inheritance works"); }

   Transition t100 : * -> stop
   condition false
   do { /* nothing */ }

   public star() { }

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
