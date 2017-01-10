layer conse;

refines class top {

   top(int q, int x) { topinit(x+y); }

   void topinit(int x) {
      Super(int).topinit(x*2);
   }
}

