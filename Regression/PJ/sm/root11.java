// tests composition of extensions that have state and transition declarations
// and method declarations

layer root11;

State_machine root {

   Delivery_parameters( M m );
   Unrecognizable_state { ignore(m); }

   // from root11
   States g;
   States h;

   // from root11
   Transition e5 : d -> e 
   condition m!=null
   do { /* action 2 */ }

   Transition e6 : c -> d
   condition true
   do { /* action 3 */ }

   Exit h { /* action a */ }

   // from root11
   void fromext11() { as = 5; }
}
   

