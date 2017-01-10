// tests refinement of static methods

layer yyh;

refines class one {
   static int foo( int b ) { Super(int).foo(b); Super().bar(); }
   static int bar() { System.out.println("yyy"); Super().bar(); }
}
