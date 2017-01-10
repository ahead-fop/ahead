layer DFS;


 public
refines class Graph {
   
   // Graph search receives a Working Space, and 
   public void GraphSearch( WorkSpace w) {
   int           s, c; Vertex  v;
  
   // Step 1: initialize visited member of all nodes

   s = vertices.size();
   if (s == 0) return;               // if there are no vertices return
         
   // Initializig the vertices
   for (c = 0; c < s; c++) {
      v = ( Vertex) vertices.get(c);  
      v.init_vertex( w );
   }

   // Step 2: traverse neighbors of each node
   for (c = 0; c < s; c++) {
      v = ( Vertex) vertices.get(c);  
      if (!v.visited)  {
          w.nextRegionAction(v);
          v.dftNodeSearch( w);
      }
    } //end for
   }
 }