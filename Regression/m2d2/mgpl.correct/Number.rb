
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Number', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Number', :identifier => 'feature', , :ownerLayer => layer)

# Number\Graph.jak

feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
# Number\NumberWorkSpace.jak

feature_NumberWorkSpace = JavaM::Class.new(:name => 'NumberWorkSpace', :identifier => 'feature.NumberWorkSpace', :visibility => 'public', :isRefinement => false, :owner => package_feature)
# Number\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)
feature_WorkSpace = JavaM::Class.new(:name => 'WorkSpace', :identifier => 'feature.WorkSpace', :owner => package_used)

# Number\Graph.jak

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        NumberVertices();Super#.run( s );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_NumberVertices12 = JavaM::Method.new(:name => 'feature.Graph.NumberVertices()', :identifier => 'feature.Graph.NumberVertices()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        GraphSearch( new NumberWorkSpace() );
    }", :ownerClass => feature_Graph)

# Number\NumberWorkSpace.jak

feature_NumberWorkSpace.extends << feature_WorkSpace
field_feature_NumberWorkSpace_vertexCounter = JavaM::Field.new(:name => 'feature.NumberWorkSpace.vertexCounter', :identifier => 'feature.NumberWorkSpace.vertexCounter', :type => primitiveType_int, :visibility => 'package', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_NumberWorkSpace)
constructor_feature_NumberWorkSpace_NumberWorkSpace12 = JavaM::Constructor.new(:name => 'feature.NumberWorkSpace.NumberWorkSpace()', :identifier => 'feature.NumberWorkSpace.NumberWorkSpace()', :type => feature_NumberWorkSpace, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        vertexCounter = 0;}", :ownerClass => feature_NumberWorkSpace)

method_feature_NumberWorkSpace_preVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.NumberWorkSpace.preVisitAction(Vertex)', :identifier => 'feature.NumberWorkSpace.preVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
      {
        // This assigns the values on the way in
        if ( v.visited!=true )
            v.VertexNumber = vertexCounter++;
    }", :ownerClass => feature_NumberWorkSpace)
parameter_feature_NumberWorkSpace_preVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.NumberWorkSpace.preVisitAction(Vertex).v', :identifier => 'feature.NumberWorkSpace.preVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_NumberWorkSpace_preVisitAction1Vertex2)

# Number\Vertex.jak

field_feature_Vertex_VertexNumber = JavaM::Field.new(:name => 'feature.Vertex.VertexNumber', :identifier => 'feature.Vertex.VertexNumber', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.print( \" # \"+ VertexNumber + \" \" );Super#.display();
    }", :ownerClass => feature_Vertex)

