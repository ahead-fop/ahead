public state_diagram wrffe_common_fo extends wrffe {

   event_delivery receive_message(M m);
   no_transition { error(-1,m); }

   otherwise_default { ignore_message(current_state, m); }

   states start, one, mission, stop;

   edge t1 : start -> one
   conditions m.msg == M.initialize
   do {  /* upstream(initialize) */; }

   edge t2 : one -> stop
   conditions m.msg == M.deny
   do { ; }

   edge t3 : one -> mission
   conditions m.msg == M.accept
   do { ; }

   public wrffe_common_fo() { current_state = start; }
}
