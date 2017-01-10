layer dunce;

refines class C {

   // this should be legal -- introducing a method that invokes super
   void foo() { super.foo(); } 

   // this should be legal -- refinement of method bar
   void bar() { int i; Super().bar(); } 

	// this is illegal, only because we can't quite implement it.
	void biff() { int i; Super().biff(); super.bar(); }

	// this is illegal, only because we can't quite implement it.
	void baz() { int i; super.baz(); Super().bar(); }
}
