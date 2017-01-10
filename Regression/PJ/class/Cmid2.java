// this extension references every method
// of the base class but only the most refined
// version
layer cmid2;

refines class top {

   void mm() {
       ma();
   }

   void mn(top c) {
       mb(c);
   }

   float mo(top c, top c1) {
       mc(c, c1);
   }
}

