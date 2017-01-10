package p3Lib;

import java.lang.Math;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Number;

/*

	For each struture, we have evaluated the cost to answer a particular type of query. 

First, we have to distinguish among 5 types of queries : 

	SCAN_QUERY : that spans all the elements.

	POINT_QUERY : We look for a particular element in [k,k].

	UPPER_SCAN_QUERY : spans elements in [k,+inf]

	LOWER_SCAN_QUERY : spans elements in [-inf,k]

	RANGE_QUERY : spans elements in [k,p] with k<p


The structures we use are : rbtree   implements a redblack tree.

                            hash     implements a hash list.

                            odlist   implemetns an ordered list.

                            dlist    implements a double linked list.

                            predindx implements a predicate index :  list which maintains elements on this 

                                     list that satisfies the predicate.

                            slist    implements a single linked list.

                            bstree   implements a binary search tree.


  

We access the costs of these structures using a set of n elements and a cost c to read 

each node, we can use equations that account for the number of nodes read during a special 

operation :


	A list scan will cost = c * n

	A tree location       = c * log_2(n+1)

	A tree scan           = 3 * c * n

	A hash scan           = c * n + b


	Starting from these costs we can define the average cost for each type of queries :




             Point               Lower          Upper            Range            Scan


RBTREE	     c*log_2(n+1)    fwd 3 * c * n/2    3 * c * n        3 * c * n/2      3 * c * n

                             bwd 3 * c * n      3 * c * n/2

BSTREE       c*log_2(n+1)+b  fwd 3 * c * n/2    3 * c * n        3 * c * n/2      3 * c * n 

                             bwd 3 * c * n      3 * c * n/2 

HASH     key c * 4 

      no key c * n + b

DLIST        c * n              c * n           c * n           c * n            c * n


SLIST        c * n              c * n           c * n           c * n            c * n


ODLIST       c * n/2        fwd c * n/2         c * n           c * n/3          c * n

                            bwd c * n           c * n/2

PREDINDX min(c*n/20,4*c)    min(c*n/20,4*c) min(c*n/20,4*c) min(c*n/20,4*c)  min(c*n/20,4*c)





If we choose n = 500 and c = 1 and b = 1, we have


             Point          Lower              Upper            Range           Scan


RBTREE	     8.96           fwd 750            1500             750             1500

                            bwd 1500           750

BSTREE       9.96           fwd 750            1500             750             1500

                            bwd 1500           750   

HASH     key 4                  

      no key 501                501             501             501             501

DLIST        500                500             500             500             500


SLIST        500                500             500             500             500

        

ODLIST       250            fwd 250             500             166             500

                            bwd 500             250

PREDINDX     25	                25              25              25              25





	We know the hash table to be the best when it has the right key in a point query,

so we'll give it a cost so that it will be the cheapest. Let's say 4 * c. If the key is not right

the cost is slightly more than a list scan, we so added a constant b to the cost of a list scan. So did we 

with the binary search tree compared to a red black tree.

	The use of the predicate indexed list when it is possible will be the cheapest 

in all cases except for  point query where the hash list remains with the smallest cost.

We can give it a cost of c * n/20 but for a point query a hash list should always be cheaper so we 

bounded the inferior limit by 4 * c.





*/





final public class Qopt {

   //constants used in computation of costs
   static int c = 1;   // cost of a visit of element
   static int b = 1;   //cost for a hash scan over a list scan
   static int n = 500; // number of elements
   static double two = 2; 
   static double n_1 = n+1;
   static int log2_n_1 =(int)(Math.log(n_1)/Math.log(two));

   public static int INFINITY        = 1000000;
   public static int LISTSCAN        = c * n/*90*/;
   public static int HALF_LISTSCAN   = c * n/2 /*45*/;
   public static int HASH_LISTSCAN  = (c * n) + b /*95*/;
   public static int HASH_LOCATE     = 4 * c /*4*/;
   public static int BINTREE_LOCATE  = b + ( c * log2_n_1)/*8*/;
   public static int RBTREE_LOCATE   = c * log2_n_1;
   public static int TREESCAN        = 3 * c * n/*50*/;
   public static int HALF_TREESCAN   = 3 * c * n/2 /*25*/;
   public static int PREDINDX_LOCATE = c * n /20 /*20*/;
   public static int THIRD_LISTSCAN   = c * n / 3;
   
   public static int BUFFERING_COST  = 30;
   public static int SORTNG_COST     = 60;

   public DS  fastest_layer;          // layer that provided the lowest cost
   public int cost;                   // lowest cost estimate for processing query

   public boolean requires_skip;      // elements out of keyrange must be examined
   public boolean requires_buffering;
   public boolean requires_sorting;
   public boolean requires_inbetween; // does retrieval layer need services of
                                      // the inbetween layer?

   public Qopt() { 
     fastest_layer      = null; 
     cost               = Qopt.INFINITY; 

     requires_skip      = false;
     requires_buffering = false;
     requires_sorting   = false;
     requires_inbetween = false;
   }
}
