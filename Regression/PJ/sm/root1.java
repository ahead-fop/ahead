layer root1;

import firstPackage.*;

State_machine root extends class common  {
   Delivery_parameters( M m );
   Unrecognizable_state { ignore(m); }

   // in root
   States a, b;
   States c;

   // in root
   Transition e1 : a -> b 
   condition m!=null
   do { /* action 1 */ }

   Exit a { /* action a */ }
}
   

