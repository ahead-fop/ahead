layer MSTKruskal;

import java.lang.Integer;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

// of Graph

  // *************************************************************************
public
refines class Vertex {
    public Vertex representative;
    public LinkedList members;
      
    public void display() {
        if ( representative == null )
            System.out.print( "Rep null " );
        else
            System.out.print( " Rep " + representative.name + " " );
        Super().display();
    }
}
