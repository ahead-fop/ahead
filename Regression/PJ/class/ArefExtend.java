// This tests if jampack rewrites calls to inherited methods correctly

layer ArefExtend;

refines class top {
   public void AsuperFunc() { 
      /* do something */
      Super().AnothersuperFunc();
   }
}

