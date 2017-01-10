layer Directed;

import  java.util.LinkedList;

 public
 class Graph {
      public LinkedList vertices;
      public LinkedList edges;
      public final boolean isDirected = true;
   
      public Graph() { 
         GraphConstructor();
      }

      public void GraphConstructor() {
         Super().GraphConstructor();
         vertices = new LinkedList();
         edges = new LinkedList();
      }
   
      public void addVertex( Vertex v ) { 
         vertices.add( v );
      }
   
      public void addEdge( Edge the_edge) { Vertex start = the_edge.start; Vertex end = the_edge.end;
	 edges.add(the_edge);		 
         start.addNeighbor(new Neighbor(end,the_edge));           
      }
	  
      // This method adds only the edge and not the neighbor
      // used in Transpose
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
