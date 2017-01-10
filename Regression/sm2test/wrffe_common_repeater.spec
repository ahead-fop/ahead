state_machine wrffe_common_repeater extends wrffe {

 
   event_delivery receive_message(M m);
   no_transition { error(-1,m); }  

   otherwise_default { ignore_message(current_state, m); }

   states start, one, three, mission, stop;

   Exit start { 
     assets_available = true; 
     OK_to_elevate = true;
     assets_in_my_command = false;
   }

   edge t1 : start -> mission
   conditions m.msg == M.initialize && assets_in_my_command
   do { /* upstream(initialize), downstream(accept) */; }

   edge t2 : start -> one
   conditions m.msg == M.initialize && !assets_in_my_command 
              && assets_available
   do { /* upstream(initialize) */; }

   edge t3 : start -> three
   conditions m.msg == M.initialize && !assets_in_my_command 
              && !assets_available && OK_to_elevate
   do { /* superior(initialize) */; }

   edge t4 : start -> stop
   conditions m.msg == M.initialize && !assets_in_my_command
              && !assets_available && !OK_to_elevate
   do { /* downstream(deny) */; }

   Exit one { more_assets_available = true; }

   edge t5 : one -> one
   conditions m.msg == M.deny && more_assets_available
   do { /* upstream(initialize) */; }

   edge t6 : one -> mission
   conditions m.msg == M.accept
   do { /* downstream(accept) */; }

   edge t7 : one -> three
   conditions m.msg == M.deny && !more_assets_available && OK_to_elevate
   do { /* superior(initialize) */; }

   edge t8 : three -> mission
   conditions m.msg == M.accept
   do { /* downstream(accept) */; }

   edge t9 : three -> stop
   conditions m.msg == M.deny
   do { /* downstream(deny) */; }

   edge t10 : one -> stop
   conditions m.msg == M.deny && !more_assets_available && !OK_to_elevate
   do { /* downstream(deny) */; }

   boolean more_assets_available, OK_to_elevate, assets_in_my_command;
   boolean assets_available;

   public wrffe_common_repeater() { current_state = start; }

}
