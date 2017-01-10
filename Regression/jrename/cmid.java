layer Cmid;

import java.util.*;

Local_Id i, j;
Local_Id ii, jj;
Local_Id foo;

refines class top implements java.io.Serializable {

   static int i,j;
   int ii,jj;

   static { i = 4; }

   void foo(float x, float y) { /* do something */ }

   float bar( float x ) { 
       Super(float).bar(x);
       return (float) 4.0; 
   }
}
