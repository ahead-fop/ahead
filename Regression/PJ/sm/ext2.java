// this tests the name mangling in AST_FieldDeclarations
layer ext2;

import firstPackage.*;

refines State_machine root {

   void test1() {
      Super().test1();
      // something more
   }

   void test2(int i) {
      /* ext2 overridden and not referenced */
   }

   void test33(String i) {
      Super(String).test3(4);
      /* test3 not overridden and referenced */
   }

   void test44() {
      /* test4 not overridden and not referenced */
   }
}
   

