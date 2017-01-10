layer MSTPrim;

import  java.lang.Integer;
import 
	java.util.LinkedList;
import 
	java.util.Collections;
import 
        java.util.Comparator;


   
   // *************************************************************************
   public
refines class Graph {

      public Graph Prim( Vertex r) { Vertex root;
		
	root = r;
	int numvertices = vertices.size();
	int i; Vertex x;
		
	// 2. and 3. Initializes the vertices
	for (i=0; i < numvertices; i++)
	{
	 x = ( Vertex)vertices.get(i);		
	 x.pred = null;
	 x.key = Integer.MAX_VALUE;
	}
		
	// 4. and 5.	
	root.key = 0;
	root.pred = null;
				
	// 2. S <- empty set
		
	// 1. Queue <- V[G], copy the vertex in the graph in the priority queue
	LinkedList Queue = new LinkedList();

	for(i=0; i < numvertices; i++)
	{
 	  x = ( Vertex)vertices.get(i);
	  if (x.key != 0) // this means, if this is not the root
		Queue.add(x);						
	}
		
	// Inserts the root at the head of the queue
	Queue.addFirst(root); Vertex ucurrent;
	int j,k,l;
	int pos;
	LinkedList Uneighbors; Vertex u,v; Edge en; Neighbor vn;
		
	int wuv;
	boolean isNeighborInQueue = false;
		
	// Queue is a list ordered by key values.
	// At the beginning all key values are INFINITUM except
	// for the root whose value is 0.
	while (Queue.size()!=0)
	{
	  // 7. u <- Extract-Min(Q);	  			
	  // Since this is an ordered queue the first element is the min
	  u = ( Vertex)Queue.removeFirst();
						
	  // 8. for each vertex v adjacent to u
	  Uneighbors = u.neighbors;

  	  for(k=0; k < Uneighbors.size(); k++)
	  {
	    vn = ( Neighbor)Uneighbors.get(k);
	    v = vn.end;
	    en = vn.edg;

	    // Check to see if the neighbor is in the queue
	    isNeighborInQueue = false;
				
	    // if the Neighor is in the queue
	    int indexNeighbor = Queue.indexOf(v);
	    if (indexNeighbor>=0) isNeighborInQueue=true;
									
	    wuv = en.weight;
				
	    // 9. Relax (u,v w)
	    if (isNeighborInQueue && (wuv < v.key))
	    {
	      v.key = wuv;
	      v.pred = u.name;
	      Uneighbors.set(k,vn); // adjust values in the neighbors
					
	      // update the values of v in the queue	
	      // Remove v from the Queue so that we can reinsert it
	      // in a new place according to its new value to keep
	      // the Linked List ordered
	      Object residue = Queue.remove(indexNeighbor);
					
	      // Get the new position for v
	      int position = Collections.binarySearch(Queue,v, 
	      	new Comparator()
		{
		 public int compare (Object o1, Object o2)
		 { Vertex v1 = ( Vertex)o1; Vertex v2 = ( Vertex)o2; 
                                
		  if (v1.key < v2.key)
		  	return -1;
 		  if (v1.key == v2.key)
			return 0;
		  return 1;	   		 
 		 }
		});	
					        
	        // Adds v in its new position in Queue					  
		if (position < 0)  // means it is not there
		 { Queue.add(-(position+1),v); }
		else      // means it is there
		 { Queue.add(position,v); } 	
					  	
		} // if 8-9.
	  } // for all neighbors			
	} // of while

    // Creates the new Graph that contains the SSSP
    String theName; Graph newGraph = new Graph();
		
    // Creates and adds the vertices with the same name
    for (i=0; i<numvertices; i++)
    {
        theName = (( Vertex)vertices.get(i)).name;			
         newGraph.addVertex(new Vertex().assignName(theName));
    } Vertex theVertex, thePred; Vertex theNewVertex, theNewPred; Edge   e;
       
       // For each vertex in vertices list we find its predecessor
       // make an edge for the new graph from predecessor->vertex
       for(i=0; i<numvertices; i++)
       {
         // theVertex and its Predecessor
         theVertex = ( Vertex)vertices.get(i);
         thePred = findsVertex(theVertex.pred);
         
         // if theVertex is the source then continue we dont need
         // to create a new edge at all
         if (thePred==null) continue;
         
         // Find the references in the new Graph
         theNewVertex = newGraph.findsVertex(theVertex.name);
         theNewPred = newGraph.findsVertex(thePred.name); Edge theNewEdge = new Edge(theNewPred, theNewVertex);
         e = findsEdge(thePred,theVertex);
         theNewEdge.adjustAdorns(e);
         
         // Adds the new edge to the newGraph
         newGraph.addEdge(theNewEdge);
       }  
        return newGraph;
        
      } // MST
      
   }
