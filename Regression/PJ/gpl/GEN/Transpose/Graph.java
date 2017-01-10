layer Transpose;

import  java.util.LinkedList;

 public
refines class Graph {
   
   public Graph ComputeTranspose( Graph the_graph)
   {	
     int i;
     int num_vertices = (the_graph.vertices).size();
     String theName; Graph newGraph = new Graph();
		
     // Creates and adds the vertices with the same name
     for (i=0; i<num_vertices; i++)
     {
	theName = (( Vertex)(the_graph.vertices).get(i)).name;			
        newGraph.addVertex(new Vertex().assignName(theName));
     } Neighbor newNeighbor; Vertex theVertex, newVertex; Neighbor theNeighbor; Vertex newAdjacent; Edge newEdge;
    int num_neighbors;
    int j;

    // adds the transposed edges
    for (i=0; i<num_vertices; i++)
    {        		
       // theVertex is the original source vertex
       // the newAdjacent is the reference in the newGraph to theVertex  
       theVertex = ( Vertex)(the_graph.vertices).get(i);
       newAdjacent = newGraph.findsVertex(theVertex.name); 
       num_neighbors = (theVertex.neighbors).size();
                               
       for(j=0; j<num_neighbors; j++)
       {
          // Gets the neighbor object in pos j
          theNeighbor = ( Neighbor)(theVertex.neighbors).get(j);
              
          // the new Vertex is the vertex that was adjacent to theVertex
          // but now in the new graph
          newVertex = newGraph.findsVertex(theNeighbor.end.name);
                          
          // Creates a new Edge object and adjusts the adornments 
          newEdge = new Edge(newVertex,newAdjacent);
          newEdge.adjustAdorns(theNeighbor.edg);
 
          // Adds the new Neighbor object with the newly formed edge
          // newNeighbor = new $TEqn.Neighbor(newAdjacent, newEdge);
          // (newVertex.neighbors).add(newNeighbor);
             
          newGraph.addEdge(newEdge);

         }  // all adjacentNeighbors       
      } // all the vertices
        
        return newGraph;
        
   } // of ComputeTranspose

 }
