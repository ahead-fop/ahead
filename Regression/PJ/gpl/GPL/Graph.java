layer GPL;

import  java.util.LinkedList;

import  java.io.*;

 public
 class Graph {
      public LinkedList vertices;
      public LinkedList edges;
      public final boolean isDirected = false;
    
    public Reader inFile;               // File handler for reading
    public int ch;               // Character to read/write
	
    // timmings
    long last=0, current=0, accum=0;
      public Graph() { GraphConstructor(); }
	  
      public void GraphConstructor() {
         vertices = new LinkedList();
         edges = new LinkedList();
      }
   
    // Graph search receives a Working Space, and 
      public void GraphSearch( WorkSpace w ) {
         int           s, c; Vertex  v;
  
         // Step 1: initialize visited member of all nodes

         s = vertices.size();
         if (s == 0) return;
         
         // Showing the initialization process
         for (c = 0; c < s; c++) {
            v = ( Vertex) vertices.get(c);  
            v.init_vertex( w );
         }

         // Step 2: traverse neighbors of each node
         
         for (c = 0; c < s; c++) {
            v = ( Vertex) vertices.get(c);  
            if (!v.visited)  {
               w.nextRegionAction(v);
               v.bftNodeSearch( w);
            }
         } //end for
         
      }

      public void NumberVertices() {
         GraphSearch( new NumberWorkSpace());
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
   
      public void addVertex( Vertex v ) { 
         vertices.add( v );
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
	  
     public void endProfile()
     {
	 current = System.currentTimeMillis();
	 accum = accum + (current-last);
	 System.out.println("Time elapsed: " + accum + " milliseconds");
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
	  
    public int readNumber() throws IOException
    {
  	int index =0;
	char[] word = new char[80];
	int ch=0;
		
	ch = inFile.read();
	while(ch==32) ch = inFile.read();  // skips extra whitespaces
		
	while(ch!=-1 && ch!=32 && ch!=10) // while it is not EOF, WS, NL
	{
	  word[index++] = (char)ch;
	  ch = inFile.read();
	}
	word[index]=0;
		
	String theString = new String(word);
		
	theString = new String(theString.substring(0,index));
	return Integer.parseInt(theString,10);
     }
	  
     public void resumeProfile()
     {
      current = System.currentTimeMillis();
      last = current;
     }
      
    public void runBenchmark(String FileName) throws IOException
    {
	try {
		inFile = new FileReader(FileName);
	} catch (IOException e)
	{
		System.out.println("Your file " + FileName + " cannot be read");
	}		  
    }
	  
     public void startProfile()
     {
       accum = 0;
       current = System.currentTimeMillis();
       last = current;
     }
	  
    public void stopBenchmark() throws IOException
    {
	inFile.close();
    }
	  
     public void stopProfile()
     {
       current = System.currentTimeMillis();
       accum = accum + (current - last);
     }
   }