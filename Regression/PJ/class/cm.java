layer cm;

refines class top {

   float foobar() { Super(float,float).foo(0, 0); }

   public void foo( float x, float y ) { /* something more */ }
}

