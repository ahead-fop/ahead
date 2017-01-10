layer cm2;

refines class top {

   float foobar() { Super().foobar(); }

   public void foo( float x, float y ) { Super(float, float).foo(0,0); }
}

