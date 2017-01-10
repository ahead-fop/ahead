layer Ctop; 

import Jakarta.util.*;

Local_Id i, j;
Local_Id ii, jj;
Local_Id foo;

class top {
   static int i,j;
   int ii,jj;

   static { i = 4; }

   top() {  ii = 5; }
   top(int rj) { jj = rj; }

   void foo(float x, float y) { /* do something */ }

   float bar( float x ) { return (float) 5.0; }
}

