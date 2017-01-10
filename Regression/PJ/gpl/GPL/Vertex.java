layer GPL;

import  java.util.LinkedList;

 public
 class Vertex {
      public LinkedList neighbors;
      public String name;
      public boolean visited;
      public LinkedList Queue =  new LinkedList();
      public int VertexNumber;
	  
      public Vertex() { VertexConstructor(); }

      public void VertexConstructor() {
         visited = false;
      }
   
      public void addNeighbor( Neighbor n ) {
         neighbors.add(n);
      }

      public Vertex assignName( String name ) {
         this.name = name;
         return ( Vertex) this;
      } 
   
      public void bftNodeSearch( WorkSpace w) {
         int           s, c; Vertex  v; Vertex  header; Neighbor n;

         // Step 1: if preVisitAction is true or if we've already
         //         visited this node

	w.preVisitAction(( Vertex) this);
		
	if (visited) return;

         // Step 2: Mark as visited, put the unvisited neighbors in the queue 
         //         and make the recursive call on the first element of the queue
         //         if there is such if not you are done

         visited = true;
         
         // Step 3: do postVisitAction now, you are no longer going through the
         // node again, mark it as black
         w.postVisitAction(( Vertex) this);
        
	 s = neighbors.size();
        
	 // enqueues the vertices not visited
	 for (c = 0; c < s; c++) 
	 {
		n = ( Neighbor) neighbors.get(c);  
		v = n.end;

		// if your neighbor has not been visited then enqueue 
		if (!v.visited)
		{
			Queue.add(v);
		}
			
  	 } // end of for           
         
      
	// while there is something in the queue
	while(Queue.size()!=0)
	{
 	  header = ( Vertex) Queue.get(0);
	  Queue.remove(0);
	  header.bftNodeSearch(w);
	}                           
                  
      }

      public final void display$$Undirected() {
         int s = neighbors.size();
         int i;

         System.out.print(" Node " + name + " connected to: ");

         for (i=0; i<s; i++) 
         { Neighbor theNeighbor = ( Neighbor) neighbors.get(i);
            System.out.print( theNeighbor.end.name + ", ");
         }     
         System.out.println();
      } // of bfsNodeSearch

      public final void display$$BFS() {
         if (visited)
            System.out.print("  visited ");
         else
            System.out.println(" !visited ");
         display$$Undirected();
      }

      public void display() {
         System.out.print(" # "+ VertexNumber + " ");
         display$$BFS();
      }
      
      public void init_vertex( WorkSpace w ) {
         visited = false;
         w.init_vertex( ( Vertex) this);
      }    
   }