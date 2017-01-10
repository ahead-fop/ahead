// tests composition of extensions that have state and transition declarations
// and method declarations

layer ext11;

import firstPackage.*;

refines State_machine root {

   // from ext 11
   States g;
   States h;

   // from ext11
   Transition e5 : d -> e 
   condition m!=null
   do { /* action 2 */ }

   Transition e6 : c -> d
   condition true
   do { /* action 3 */ }

   Exit h { /* action a */ }

   // from ext11
   void fromext11() { as = 5; }
}
   
