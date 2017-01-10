public state_machine wrffe_common_fist extends wrffe implements java.io.Serializable {

   event_delivery receive_message(M m);
   no_transition { error(-1,m); }

   states  start,    one,    mission,    stop;

   edge t1 : start -> one
      conditions m.msg == M.initialize
   do {  /* upstream(initialize) */; }

   edge t2 : one -> stop
      conditions m.msg == M.deny
   do {  /* downstream(deny) */; }

   edge t3 : one -> mission
      conditions m.msg == M.accept
   do {  /* downstream(accept) */; }

   Enter stop { stop_exit(m); start_enter(m); }

   public wrffe_common_fist() { current_state = start; }

   void foo( int x ) { x = x*2; }
}
