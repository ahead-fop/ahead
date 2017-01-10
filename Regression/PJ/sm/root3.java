// this tests the name mangling outside AST_FieldDeclarations

layer root3;

State_machine root extends class common  {
   Delivery_parameters( M m );
   Unrecognizable_state { ignore(m); }

   void test1() {
      /* overridden and referenced */
   }

   void test2(int i) {
      /* overridden and not referenced */
   }

   void test3(String i) {
      /* not overridden and referenced */
   }

   void test4() { 
      /* not overridden and not referenced */
   }
}
