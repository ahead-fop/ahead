layer StronglyConnected;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

public
refines class Graph {

    public Graph StrongComponents() {
                
        FinishTimeWorkSpace FTWS = new FinishTimeWorkSpace();
        
        // 1. Computes the finishing times for each vertex
        GraphSearch( FTWS );
                  
        // 2. Order in decreasing  & call DFS Transposal
        Collections.sort( vertices, 
         new Comparator() {
            public int compare( Object o1, Object o2 )
                {
                Vertex v1 = ( Vertex )o1;
                Vertex v2 = ( Vertex )o2;

                if ( v1.finishTime > v2.finishTime )
                    return -1;

                if ( v1.finishTime == v2.finishTime )
                    return 0;
                return 1;
            }
        } );
        Graph gaux = ComputeTranspose( ( Graph )this );
            
        // 4. Traverse the transpose G
        WorkSpaceTranspose WST = new WorkSpaceTranspose();
        gaux.GraphSearch( WST );
 
        return gaux;
        
    } // of Strong Components
      
}
