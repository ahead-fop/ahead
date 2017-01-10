state_machine root extends wrffe {

   event_delivery receive_message(M m);
   no_transition { error((-1),m); }

   otherwise_default { stop_enter(m); }

   states start, stop;

   Exit start { /*testing*/; }
   Enter start { /* more testing; */; }

   public root() { current_state = start; }
}
