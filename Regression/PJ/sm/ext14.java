// tests composition of extensions that have state and Transition declarations
// and method declarations

layer ext14;

refines State_machine root {

   // Otherwise_default { Super().fromext11(); }

   // from ext 14
   States g1;
   States h1;

   // from ext14
   Transition e55 : d -> e 
   condition m!=null
   do { /* action 2 */ Super().fromext11(); }

   Transition e66 : c -> d
   condition true
   do { /* action 3 */ Super().fromext11(); }

   Exit h1 { Super().fromext11(); }

   // from ext14
   void fromext14() { Super().fromext11(); }
}
   

