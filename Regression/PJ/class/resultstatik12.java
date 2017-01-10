// tests refinement of static methods

package top;

class one extends two {
   static final int bar$$top() { System.out.println("xxx"); }
   static int bar() { System.out.println("yyy"); bar$$top(); }
   static final int foo$$top( int b ) { two.foo(b); }
   static int foo( int b ) { foo$$top(b); bar$$top(); }
}
