layer StronglyConnected;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

// *************************************************************************
public
 class FinishTimeWorkSpace extends WorkSpace {
    int FinishCounter;
 
    public FinishTimeWorkSpace() {
        FinishTimeWorkSpaceConstructor();
    }

    public void FinishTimeWorkSpaceConstructor() {
        FinishCounter = 1;
    }

    public void preVisitAction( Vertex v )
      {
        if ( v.visited!=true )
            FinishCounter++;
    }

    public void postVisitAction( Vertex v ) {
        v.finishTime = FinishCounter++;
    } // of postVisit
    
}
