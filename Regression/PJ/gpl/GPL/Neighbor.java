layer GPL;

import  java.util.LinkedList;

 public
 class Neighbor {
 	 public Vertex end;		
	 public Edge   edg;
		
	 public Neighbor() { NeighborConstructor(); }

         public void NeighborConstructor() {
	  end = null;
	  edg = null;
	 }
		
	 public Neighbor( Vertex v, Edge e) {
            NeighborConstructor(v,e);
         }

         public void NeighborConstructor( Vertex v, Edge e) {
	  end = v;
	  edg = e;
	 }
		
	}