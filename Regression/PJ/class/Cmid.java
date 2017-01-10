layer Cmid;

import AnotherPackage;

refines class top implements java.io.Serializable, xxx {
   static int k;
   static { j = 5; }

   top(float x) { /* do something */ }

   float foobar() { Super(float).bar(4.0);
                    Super(float,float).foo(0, 0); }

   public void foo( float x, float y ) { /* something more */ }
}

