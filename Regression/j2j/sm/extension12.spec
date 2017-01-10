State_machine extension12 extends extension11 {

   States one;

   Otherwise one { ignore_message(current_state, m); if (false) Proceed(m); }

   Transition t1 : start->one
   condition m.msg == M.initialize
   do { /* nothing */; }

   Transition t2 : one->stop
   condition m.msg == M.eom
   do { /* more nothing */; }
}
