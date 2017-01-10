// this extension references every method
// of the base class -- this will require
// each base class method to be mangled
// and "reborn" by calling it's mangled counterpart

layer cmid1;

refines class top {

   void mm() {
       Super().ma();
   }

   void mn(top c) {
       Super(top).mb();
   }

   float mo(top c, top c1) {
       Super(top,top).mc(c, c1);
   }
}

