
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Weighted', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Weighted', :identifier => 'feature', , :ownerLayer => layer)

# Weighted\Edge.jak

feature_Edge = JavaM::Class.new(:name => 'Edge', :identifier => 'feature.Edge', :visibility => 'public', :isRefinement => true, :owner => package_feature)
# Weighted\Graph.jak

feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)
feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :owner => package_used)

# Weighted\Edge.jak

field_feature_Edge_weight = JavaM::Field.new(:name => 'feature.Edge.weight', :identifier => 'feature.Edge.weight', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Edge)
method_feature_Edge_EdgeConstructor1Vertex_Vertex_int2 = JavaM::Method.new(:name => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int)', :identifier => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {Super#.EdgeConstructor( the_start,the_end );
        weight = the_weight;
    }", :ownerClass => feature_Edge)
parameter_feature_Edge_EdgeConstructor1Vertex_Vertex_int2_the_start = JavaM::Parameter.new(:name => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int).the_start', :identifier => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int).the_start', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Edge_EdgeConstructor1Vertex_Vertex_int2)
parameter_feature_Edge_EdgeConstructor1Vertex_Vertex_int2_the_end = JavaM::Parameter.new(:name => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int).the_end', :identifier => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int).the_end', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Edge_EdgeConstructor1Vertex_Vertex_int2)
parameter_feature_Edge_EdgeConstructor1Vertex_Vertex_int2_the_weight = JavaM::Parameter.new(:name => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int).the_weight', :identifier => 'feature.Edge.EdgeConstructor(Vertex,Vertex,int).the_weight', :type => primitiveType_int, :nesting => 0, :ownerMethod => method_feature_Edge_EdgeConstructor1Vertex_Vertex_int2)

method_feature_Edge_adjustAdorns1Edge2 = JavaM::Method.new(:name => 'feature.Edge.adjustAdorns(Edge)', :identifier => 'feature.Edge.adjustAdorns(Edge)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        weight = the_edge.weight;Super#.adjustAdorns( the_edge );
    }", :ownerClass => feature_Edge)
parameter_feature_Edge_adjustAdorns1Edge2_the_edge = JavaM::Parameter.new(:name => 'feature.Edge.adjustAdorns(Edge).the_edge', :identifier => 'feature.Edge.adjustAdorns(Edge).the_edge', :type => feature_Edge, :nesting => 0, :ownerMethod => method_feature_Edge_adjustAdorns1Edge2)

method_feature_Edge_display12 = JavaM::Method.new(:name => 'feature.Edge.display()', :identifier => 'feature.Edge.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.print( \" Weight=\" + weight );Super#.display();
    }", :ownerClass => feature_Edge)

# Weighted\Graph.jak

method_feature_Graph_addAnEdge1Vertex_Vertex_int2 = JavaM::Method.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int)', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
    {
        Edge theEdge = new  Edge();
        theEdge.EdgeConstructor( start, end, weight );
        addEdge( theEdge );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_addAnEdge1Vertex_Vertex_int2_start = JavaM::Parameter.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int).start', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int).start', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_addAnEdge1Vertex_Vertex_int2)
parameter_feature_Graph_addAnEdge1Vertex_Vertex_int2_end = JavaM::Parameter.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int).end', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int).end', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_addAnEdge1Vertex_Vertex_int2)
parameter_feature_Graph_addAnEdge1Vertex_Vertex_int2_weight = JavaM::Parameter.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int).weight', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int).weight', :type => primitiveType_int, :nesting => 0, :ownerMethod => method_feature_Graph_addAnEdge1Vertex_Vertex_int2)

