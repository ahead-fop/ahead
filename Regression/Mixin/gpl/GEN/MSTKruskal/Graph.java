layer MSTKruskal;

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
		  	
      public Graph Kruskal() {
      
	// 1. A <- Empty set
	LinkedList A = new LinkedList();
		
	// 2. for each vertex v E V[G]
	// 3.    do Make-Set(v)
	int numvertices = vertices.size();
	int i; Vertex v;
		
	for (i=0; i < numvertices; i++)
	{
	  v = ( Vertex)vertices.get(i);
	  v.representative = v;		// I am in my set
	  v.members = new LinkedList();	// I have no members in my set
	}
		
	// 4. sort the edges of E by nondecreasing weight w	
	// Creates the edges objects
	int j;
	LinkedList Vneighbors = new LinkedList(); Vertex u;
		
	// Sort the Edges in non decreasing order
        Collections.sort(edges, 
	  new Comparator()
	   {
	     public int compare (Object o1, Object o2)
	     { Edge e1 = ( Edge)o1; Edge e2 = ( Edge)o2;
	        if (e1.weight < e2.weight)
		  return -1;
		if (e1.weight == e2.weight)
  		  return 0;
		return 1;	   		 
	      }
 	 });		
        
	// 5. for each edge in the nondecresing order
	int numedges = edges.size(); Edge e1; Vertex vaux, urep, vrep;
		
	for(i=0; i<numedges; i++)
	{
	  // 6. if Find-Set(u)!=Find-Set(v)
	  e1 = ( Edge)edges.get(i);
	  u = e1.start;
	  v = e1.end;

	  if (!(v.representative.name).equals(u.representative.name))
	  {
	    // 7. A <- A U {(u,v)}
	    A.add(e1);
				
	    // 8. Union(u,v)
  	    urep = u.representative;
	    vrep = v.representative;
	 			
	    if ((urep.members).size() > (vrep.members).size())
	    { // we add elements of v to u
	      for(j=0; j<(vrep.members).size(); j++)
	      {
		vaux = ( Vertex)(vrep.members).get(j);
		vaux.representative = urep;
		(urep.members).add(vaux);
	      }
	      v.representative = urep;
	      vrep.representative = urep;
	      (urep.members).add(v);
	      if (!v.equals(vrep)) (urep.members).add(vrep);
			(vrep.members).clear();
	     }
	     else
	     { // we add elements of u to v
	       for(j=0; j<(urep.members).size(); j++)
	       {
		 vaux = ( Vertex)(urep.members).get(j);
		 vaux.representative = vrep;
		 (vrep.members).add(vaux);
	       }
	       u.representative = vrep;
	       urep.representative = vrep;
	       (vrep.members).add(u);
	       if (!u.equals(urep)) (vrep.members).add(urep);
		 (urep.members).clear();
					
	      } // else
				
  	  } // of if
			
       } // of for numedges
		
      // 9. return A
      // Creates the new Graph that contains the SSSP
      String theName; Graph newGraph = new Graph();
		
      // Creates and adds the vertices with the same name
      for (i=0; i<numvertices; i++)
      {
         theName = (( Vertex)vertices.get(i)).name;			
         newGraph.addVertex(new Vertex().assignName(theName));
      } Vertex theStart, theEnd; Vertex theNewStart, theNewEnd; Edge   theEdge;
       
       // For each edge in A we find its two vertices
       // make an edge for the new graph from with the correspoding
       // new two vertices
       for(i=0; i<A.size(); i++)
       {   
         // theEdge with its two vertices
         theEdge = ( Edge)A.get(i);
         theStart = theEdge.start;
         theEnd = theEdge.end;
         
         // Find the references in the new Graph
         theNewStart = newGraph.findsVertex(theStart.name);
         theNewEnd = newGraph.findsVertex(theEnd.name); Edge theNewEdge = new Edge(theNewStart, theNewEnd);
         theNewEdge.adjustAdorns(theEdge);
         
         // Adds the new edge to the newGraph
         newGraph.addEdge(theNewEdge);
       }          
        return newGraph;	
        
      } // kruskal 
           
   }