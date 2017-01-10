// this extension overrides every method
// of the base class 

refines class top {

   static void ma() {
       Super().ma();
   }

   Util mb(top c) {
       Super(top).mb();
   }

   Bar mc(top c, top c1) {
       Super(top,top).mc(c, c1);
   }
}

