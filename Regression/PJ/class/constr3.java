// tests refinement of constructor refinements

layer bot;

refines class one {
   String biff;

   refines one(int i) { biff= ""; }

   refines one(float j) { biff= "float"; }

   refines one() { biff= "biff"; }
}
