layer Weighted;


 public
refines class Edge{
	public int weight;
		
        public void Edge( Vertex the_start, Vertex the_end, int the_weight) {
           EdgeConstructor(the_start,the_end,the_weight);
        }

        public void EdgeConstructor( Vertex the_start, Vertex the_end, int the_weight) {
          base( Vertex, Vertex).EdgeConstructor(the_start,the_end);
	  weight = the_weight;
	}
		
	public void adjustAdorns( Edge the_edge)
	{
	  weight = the_edge.weight;
	  Super(Edge).adjustAdorns(the_edge);
	}
	    
	public void display()
	{
 	 System.out.print(" Weight=" + weight);
	 Super().display();
	}
		
  }
