layer Undirected;

import  java.util.LinkedList;

 public
 class Graph {
      public Graph() { GraphConstructor(); }
      public LinkedList vertices;
      public LinkedList edges;
      public final boolean isDirected = false;
	  
      public void GraphConstructor() {
         vertices = new LinkedList();
         edges = new LinkedList();
      }
   
      public void addVertex( Vertex v ) { 
         vertices.add( v );
      }
   
      public void addEdge( Edge the_edge) { Vertex start = the_edge.start; Vertex end = the_edge.end;
	edges.add(the_edge);		 
        start.addNeighbor(new Neighbor(end,the_edge));  
        end.addNeighbor(new Neighbor(start,the_edge));         
      }

      // This method adds only the edge and not the neighbor.
      // Used in Transpose layer.
      public void addOnlyEdge( Edge the_edge) {
	 edges.add(the_edge);		 
      }
      
      // Finds a vertex given its name in the vertices list
      public Vertex  findsVertex(String theName)
      {
	int i=0; Vertex theVertex;
	
	// if we are dealing with the root
	if (theName==null) return null;
	    
	for(i=0; i<vertices.size(); i++)
	{
	 theVertex = ( Vertex)vertices.get(i);
	 if (theName.equals(theVertex.name))
		return theVertex;
	}
	return null;
       }
	  
       // Finds an Edge given both of its vertices
       public Edge findsEdge( Vertex theSource, Vertex theTarget)
       {
	 int i=0; Edge theEdge;
	
	 for(i=0; i<edges.size(); i++)
	 {
	  theEdge = ( Edge)edges.get(i);
	  if ((theEdge.start.name.equals(theSource.name) && 
	      theEdge.end.name.equals(theTarget.name)) ||
	     (theEdge.start.name.equals(theTarget.name) && 
	      theEdge.end.name.equals(theSource.name)) )
		return theEdge;
	 }   
	 return null;	   
	}
	       
      public void display() {
         int i;
		
	 System.out.println("******************************************");
	 System.out.println("Vertices ");
         for (i=0; i<vertices.size(); i++) 
            (( Vertex) vertices.get(i)).display();
         
         System.out.println("******************************************");
         System.out.println("Edges ");
         for (i=0; i<edges.size(); i++)
			(( Edge) edges.get(i)).display();
		
	 System.out.println("******************************************");
     
      }
   }
