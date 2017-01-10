// this tests the name mangling outside AST_FieldDeclarations

layer ext2;

import firstPackage.*;

refines State_machine root {

   States a, b, c;

   Exit a {
      Super().test1();
      // something more
   }

   Transition x : a -> b
   condition Super().test1() 
   do {
      Super(String).test3(4);
      /* test3 not overridden and referenced */
   }
}
   

