// tests refinement of constructors

layer top;

class one extends something {
   int a;
   float b;

   one(int i) { Super(int)(i); a = i; b = 0; }

   one(float j) { a = 0; b = j; }

   one() { Super(int,float)(2,3.4); }
}
