// tests refinement of static methods

layer top;

class one extends two {
   static int foo( int b ) { two.foo(b); }
   static int bar() { System.out.println("xxx"); }
}
