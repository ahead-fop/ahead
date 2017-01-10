
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Cycle', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Cycle', :identifier => 'feature', , :ownerLayer => layer)

# Cycle\CycleWorkSpace.jak

java_lang_Integer = JavaM::Class.new(:name => 'java.lang.Integer', :identifier => 'java.lang.Integer', :owner => package_used)
feature_CycleWorkSpace = JavaM::Class.new(:name => 'CycleWorkSpace', :identifier => 'feature.CycleWorkSpace', :visibility => 'public', :isRefinement => false, :owner => package_feature)
feature_CycleWorkSpace.importsType << java_lang_Integer
# Cycle\Graph.jak

feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Graph.importsType << java_lang_Integer
# Cycle\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Vertex.importsType << java_lang_Integer

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)
primitiveType_boolean = JavaM::PrimitiveType.new(:name =>'boolean', :identifier =>'boolean', :owner => package_used)
feature_WorkSpace = JavaM::Class.new(:name => 'WorkSpace', :identifier => 'feature.WorkSpace', :owner => package_used)

# Cycle\CycleWorkSpace.jak

feature_CycleWorkSpace.extends << feature_WorkSpace
field_feature_CycleWorkSpace_AnyCycles = JavaM::Field.new(:name => 'feature.CycleWorkSpace.AnyCycles', :identifier => 'feature.CycleWorkSpace.AnyCycles', :type => primitiveType_boolean, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_CycleWorkSpace)
field_feature_CycleWorkSpace_counter = JavaM::Field.new(:name => 'feature.CycleWorkSpace.counter', :identifier => 'feature.CycleWorkSpace.counter', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_CycleWorkSpace)
field_feature_CycleWorkSpace_isDirected = JavaM::Field.new(:name => 'feature.CycleWorkSpace.isDirected', :identifier => 'feature.CycleWorkSpace.isDirected', :type => primitiveType_boolean, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_CycleWorkSpace)
field_feature_CycleWorkSpace_WHITE = JavaM::Field.new(:name => 'feature.CycleWorkSpace.WHITE', :identifier => 'feature.CycleWorkSpace.WHITE', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => true, :isFinal => true, :ownerClass => feature_CycleWorkSpace, :value => ' = 0')
field_feature_CycleWorkSpace_GRAY = JavaM::Field.new(:name => 'feature.CycleWorkSpace.GRAY', :identifier => 'feature.CycleWorkSpace.GRAY', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => true, :isFinal => true, :ownerClass => feature_CycleWorkSpace, :value => '  = 1')
field_feature_CycleWorkSpace_BLACK = JavaM::Field.new(:name => 'feature.CycleWorkSpace.BLACK', :identifier => 'feature.CycleWorkSpace.BLACK', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => true, :isFinal => true, :ownerClass => feature_CycleWorkSpace, :value => ' = 2')
constructor_feature_CycleWorkSpace_CycleWorkSpace1boolean2 = JavaM::Constructor.new(:name => 'feature.CycleWorkSpace.CycleWorkSpace(boolean)', :identifier => 'feature.CycleWorkSpace.CycleWorkSpace(boolean)', :type => feature_CycleWorkSpace, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        AnyCycles = false;
        counter   = 0;
        isDirected = UnDir;}", :ownerClass => feature_CycleWorkSpace)
parameter_feature_CycleWorkSpace_CycleWorkSpace1boolean2_UnDir = JavaM::Parameter.new(:name => 'feature.CycleWorkSpace.CycleWorkSpace(boolean).UnDir', :identifier => 'feature.CycleWorkSpace.CycleWorkSpace(boolean).UnDir', :type => primitiveType_boolean, :nesting => 0, :ownerConstructor => constructor_feature_CycleWorkSpace_CycleWorkSpace1boolean2)

method_feature_CycleWorkSpace_init_vertex1Vertex2 = JavaM::Method.new(:name => 'feature.CycleWorkSpace.init_vertex(Vertex)', :identifier => 'feature.CycleWorkSpace.init_vertex(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " 
      {
        v.VertexCycle = Integer.MAX_VALUE;
        v.VertexColor = WHITE; // initialize to white color
    }", :ownerClass => feature_CycleWorkSpace)
parameter_feature_CycleWorkSpace_init_vertex1Vertex2_v = JavaM::Parameter.new(:name => 'feature.CycleWorkSpace.init_vertex(Vertex).v', :identifier => 'feature.CycleWorkSpace.init_vertex(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_CycleWorkSpace_init_vertex1Vertex2)

method_feature_CycleWorkSpace_preVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.CycleWorkSpace.preVisitAction(Vertex)', :identifier => 'feature.CycleWorkSpace.preVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        
        // This assigns the values on the way in
        if ( v.visited!=true ) 
        { // if it has not been visited then set the
            // VertexCycle accordingly
            v.VertexCycle = counter++;
            v.VertexColor = GRAY; // we make the vertex gray
        }
    }", :ownerClass => feature_CycleWorkSpace)
parameter_feature_CycleWorkSpace_preVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.CycleWorkSpace.preVisitAction(Vertex).v', :identifier => 'feature.CycleWorkSpace.preVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_CycleWorkSpace_preVisitAction1Vertex2)

method_feature_CycleWorkSpace_postVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.CycleWorkSpace.postVisitAction(Vertex)', :identifier => 'feature.CycleWorkSpace.postVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " 
      {
        v.VertexColor = BLACK; // we are done visiting so make it black
        counter--;
    }", :ownerClass => feature_CycleWorkSpace)
parameter_feature_CycleWorkSpace_postVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.CycleWorkSpace.postVisitAction(Vertex).v', :identifier => 'feature.CycleWorkSpace.postVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_CycleWorkSpace_postVisitAction1Vertex2)

method_feature_CycleWorkSpace_checkNeighborAction1Vertex_Vertex2 = JavaM::Method.new(:name => 'feature.CycleWorkSpace.checkNeighborAction(Vertex,Vertex)', :identifier => 'feature.CycleWorkSpace.checkNeighborAction(Vertex,Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " 
      {
        // if the graph is directed is enough to check that the source node
        // is gray and the adyacent is gray also to find a cycle
        // if the graph is undirected we need to check that the adyacent is not
        // the father, if it is the father the difference in the VertexCount is
        // only one.                                   
        if ( isDirected )
        {
            if ( ( vsource.VertexColor == GRAY ) && ( vtarget.VertexColor == GRAY ) ) 
              {
                AnyCycles = true;
            }
        }
        else
        { // undirected case
            if ( ( vsource.VertexColor == GRAY ) && ( vtarget.VertexColor == GRAY ) 
                 && vsource.VertexCycle != vtarget.VertexCycle+1 ) 
              {
                AnyCycles = true;
            }
        }
        
    }", :ownerClass => feature_CycleWorkSpace)
parameter_feature_CycleWorkSpace_checkNeighborAction1Vertex_Vertex2_vsource = JavaM::Parameter.new(:name => 'feature.CycleWorkSpace.checkNeighborAction(Vertex,Vertex).vsource', :identifier => 'feature.CycleWorkSpace.checkNeighborAction(Vertex,Vertex).vsource', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_CycleWorkSpace_checkNeighborAction1Vertex_Vertex2)
parameter_feature_CycleWorkSpace_checkNeighborAction1Vertex_Vertex2_vtarget = JavaM::Parameter.new(:name => 'feature.CycleWorkSpace.checkNeighborAction(Vertex,Vertex).vtarget', :identifier => 'feature.CycleWorkSpace.checkNeighborAction(Vertex,Vertex).vtarget', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_CycleWorkSpace_checkNeighborAction1Vertex_Vertex2)

# Cycle\Graph.jak

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        System.out.println( \" Cycle? \" + CycleCheck() );Super#.run( s );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_CycleCheck12 = JavaM::Method.new(:name => 'feature.Graph.CycleCheck()', :identifier => 'feature.Graph.CycleCheck()', :type => primitiveType_boolean, :nesting => 0, :visibility => 'public', :body => " {
        CycleWorkSpace c = new CycleWorkSpace( isDirected );
        GraphSearch( c );
        return c.AnyCycles;
    }", :ownerClass => feature_Graph)

# Cycle\Vertex.jak

field_feature_Vertex_VertexCycle = JavaM::Field.new(:name => 'feature.Vertex.VertexCycle', :identifier => 'feature.Vertex.VertexCycle', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
field_feature_Vertex_VertexColor = JavaM::Field.new(:name => 'feature.Vertex.VertexColor', :identifier => 'feature.Vertex.VertexColor', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.print( \" VertexCycle# \" + VertexCycle + \" \" );Super#.display();
    }", :ownerClass => feature_Vertex)

