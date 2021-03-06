layer Undirected;

import java.util.LinkedList;

public
 class Vertex {
    public LinkedList neighbors;
    public String name;
      
    public Vertex() {
        VertexConstructor();
    }

    public void VertexConstructor() {
        name      = null;
        neighbors = new LinkedList();
    }

    public Vertex assignName( String name ) {
        this.name = name;
        return ( Vertex ) this;
    }
   
    public void addNeighbor( Neighbor n ) {
        neighbors.add( n );
    }

    public void display() {
        int s = neighbors.size();
        int i;

        System.out.print( " Node " + name + " connected to: " );

        for ( i=0; i<s; i++ ) 
         {
            Neighbor theNeighbor = ( Neighbor ) neighbors.get( i );
            System.out.print( theNeighbor.end.name + ", " );
        }
        System.out.println();
    }
}
