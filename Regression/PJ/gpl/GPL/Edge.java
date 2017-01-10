layer GPL;

import  java.util.LinkedList;

 public
 class Edge extends Neighbor{
 	 public Vertex start;
		
	 public Edge( Vertex the_start, Vertex the_end) {
            EdgeConstructor(the_start, the_end);
         }

         public void EdgeConstructor( Vertex the_start, Vertex the_end) {
	  start = the_start;
	  end = the_end;
	 }
		
	 public void adjustAdorns( Edge the_edge)
	 {
	 }		
		
	 public void display()
	 {
	  System.out.println(" start=" + start.name + " end=" + end.name);
	 }
     }