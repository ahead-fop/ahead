layer Cycle;

import java.lang.Integer;

// Cycle checking, Edge-Neighbor implementation
  
// *************************************************************************
public
refines class Graph {
              
    public boolean CycleCheck() {
        CycleWorkSpace c = new CycleWorkSpace( isDirected );
        GraphSearch( c );
        return c.AnyCycles;
    }
}
