// tests refinement of constructors

layer mid;

refines class one {
   String barf;

   refines one(int i) { barf = ""; }

   refines one(float j) { barf= "float"; }

   refines one() { barf= "biff"; }
}
