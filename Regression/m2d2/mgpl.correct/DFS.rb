
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'DFS', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'DFS', :identifier => 'feature', , :ownerLayer => layer)

# DFS\Graph.jak

feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
# DFS\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)
# DFS\WorkSpace.jak

feature_WorkSpace = JavaM::Class.new(:name => 'WorkSpace', :identifier => 'feature.WorkSpace', :visibility => 'public', :isRefinement => false, :owner => package_feature)

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_boolean = JavaM::PrimitiveType.new(:name =>'boolean', :identifier =>'boolean', :owner => package_used)

# DFS\Graph.jak

method_feature_Graph_GraphSearch1WorkSpace2 = JavaM::Method.new(:name => 'feature.Graph.GraphSearch(WorkSpace)', :identifier => 'feature.Graph.GraphSearch(WorkSpace)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        int           s, c;
        Vertex  v;
  
        // Step 1: initialize visited member of all nodes

        s = vertices.size();
        if ( s == 0 )
            return; // if there are no vertices return
         
        // Initializig the vertices
        for ( c = 0; c < s; c++ ) {
            v = ( Vertex ) vertices.get( c );
            v.init_vertex( w );
        }

        // Step 2: traverse neighbors of each node
        for ( c = 0; c < s; c++ ) {
            v = ( Vertex ) vertices.get( c );
            if ( !v.visited ) {
                w.nextRegionAction( v );
                v.dftNodeSearch( w );
            }
        } //end for
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_GraphSearch1WorkSpace2_w = JavaM::Parameter.new(:name => 'feature.Graph.GraphSearch(WorkSpace).w', :identifier => 'feature.Graph.GraphSearch(WorkSpace).w', :type => feature_WorkSpace, :nesting => 0, :ownerMethod => method_feature_Graph_GraphSearch1WorkSpace2)

# DFS\Vertex.jak

field_feature_Vertex_visited = JavaM::Field.new(:name => 'feature.Vertex.visited', :identifier => 'feature.Vertex.visited', :type => primitiveType_boolean, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_VertexConstructor12 = JavaM::Method.new(:name => 'feature.Vertex.VertexConstructor()', :identifier => 'feature.Vertex.VertexConstructor()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {Super#.VertexConstructor();
        visited = false;
    }", :ownerClass => feature_Vertex)

method_feature_Vertex_init_vertex1WorkSpace2 = JavaM::Method.new(:name => 'feature.Vertex.init_vertex(WorkSpace)', :identifier => 'feature.Vertex.init_vertex(WorkSpace)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        visited = false;
        w.init_vertex( ( Vertex ) this );
    }", :ownerClass => feature_Vertex)
parameter_feature_Vertex_init_vertex1WorkSpace2_w = JavaM::Parameter.new(:name => 'feature.Vertex.init_vertex(WorkSpace).w', :identifier => 'feature.Vertex.init_vertex(WorkSpace).w', :type => feature_WorkSpace, :nesting => 0, :ownerMethod => method_feature_Vertex_init_vertex1WorkSpace2)

method_feature_Vertex_dftNodeSearch1WorkSpace2 = JavaM::Method.new(:name => 'feature.Vertex.dftNodeSearch(WorkSpace)', :identifier => 'feature.Vertex.dftNodeSearch(WorkSpace)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        int           s, c;
        Vertex v;
        Neighbor n;

        // Step 1: Do preVisitAction. 
        //            If we\'ve already visited this node return

        w.preVisitAction( ( Vertex ) this );
         
        if ( visited )
            return;

        // Step 2: else remember that we\'ve visited and 
        //         visit all neighbors

        visited = true;
         
        s = neighbors.size();
        for ( c = 0; c < s; c++ ) 
                {
            n = ( Neighbor ) neighbors.get( c );
            v = n.end;
            w.checkNeighborAction( ( Vertex ) this, v );
            v.dftNodeSearch( w );
        }
        ;
     
        // Step 3: do postVisitAction now
        w.postVisitAction( ( Vertex ) this );
    }", :ownerClass => feature_Vertex)
parameter_feature_Vertex_dftNodeSearch1WorkSpace2_w = JavaM::Parameter.new(:name => 'feature.Vertex.dftNodeSearch(WorkSpace).w', :identifier => 'feature.Vertex.dftNodeSearch(WorkSpace).w', :type => feature_WorkSpace, :nesting => 0, :ownerMethod => method_feature_Vertex_dftNodeSearch1WorkSpace2)

method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        if ( visited )
            System.out.print( \"  visited\" );
        else
            System.out.println( \" !visited\" );Super#.display();
    }", :ownerClass => feature_Vertex)

# DFS\WorkSpace.jak

method_feature_WorkSpace_init_vertex1Vertex2 = JavaM::Method.new(:name => 'feature.WorkSpace.init_vertex(Vertex)', :identifier => 'feature.WorkSpace.init_vertex(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {}", :ownerClass => feature_WorkSpace)
parameter_feature_WorkSpace_init_vertex1Vertex2_v = JavaM::Parameter.new(:name => 'feature.WorkSpace.init_vertex(Vertex).v', :identifier => 'feature.WorkSpace.init_vertex(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpace_init_vertex1Vertex2)

method_feature_WorkSpace_preVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.WorkSpace.preVisitAction(Vertex)', :identifier => 'feature.WorkSpace.preVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {}", :ownerClass => feature_WorkSpace)
parameter_feature_WorkSpace_preVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.WorkSpace.preVisitAction(Vertex).v', :identifier => 'feature.WorkSpace.preVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpace_preVisitAction1Vertex2)

method_feature_WorkSpace_postVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.WorkSpace.postVisitAction(Vertex)', :identifier => 'feature.WorkSpace.postVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {}", :ownerClass => feature_WorkSpace)
parameter_feature_WorkSpace_postVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.WorkSpace.postVisitAction(Vertex).v', :identifier => 'feature.WorkSpace.postVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpace_postVisitAction1Vertex2)

method_feature_WorkSpace_nextRegionAction1Vertex2 = JavaM::Method.new(:name => 'feature.WorkSpace.nextRegionAction(Vertex)', :identifier => 'feature.WorkSpace.nextRegionAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {}", :ownerClass => feature_WorkSpace)
parameter_feature_WorkSpace_nextRegionAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.WorkSpace.nextRegionAction(Vertex).v', :identifier => 'feature.WorkSpace.nextRegionAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpace_nextRegionAction1Vertex2)

method_feature_WorkSpace_checkNeighborAction1Vertex_Vertex2 = JavaM::Method.new(:name => 'feature.WorkSpace.checkNeighborAction(Vertex,Vertex)', :identifier => 'feature.WorkSpace.checkNeighborAction(Vertex,Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {}", :ownerClass => feature_WorkSpace)
parameter_feature_WorkSpace_checkNeighborAction1Vertex_Vertex2_vsource = JavaM::Parameter.new(:name => 'feature.WorkSpace.checkNeighborAction(Vertex,Vertex).vsource', :identifier => 'feature.WorkSpace.checkNeighborAction(Vertex,Vertex).vsource', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpace_checkNeighborAction1Vertex_Vertex2)
parameter_feature_WorkSpace_checkNeighborAction1Vertex_Vertex2_vtarget = JavaM::Parameter.new(:name => 'feature.WorkSpace.checkNeighborAction(Vertex,Vertex).vtarget', :identifier => 'feature.WorkSpace.checkNeighborAction(Vertex,Vertex).vtarget', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpace_checkNeighborAction1Vertex_Vertex2)

