// This tests if jampack rewrites calls to inherited methods correctly

layer ArefOverride;

refines class top {
   public void AsuperFunc() { 
      /* do something */
      super.AnothersuperFunc();
   }
}

