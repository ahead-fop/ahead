// tests replication of States, Transitions

layer ext4;

refines State_machine root {

   // in root
   States a, b;
   States c;

   // in root
   Transition e1 : a -> b 
   condition m!=null
   do { /* action 1 */ }

   Exit a { /* action a */ }
}
   

