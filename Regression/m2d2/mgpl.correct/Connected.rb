
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Connected', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Connected', :identifier => 'feature', , :ownerLayer => layer)

# Connected\Graph.jak

feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
# Connected\RegionWorkSpace.jak

feature_RegionWorkSpace = JavaM::Class.new(:name => 'RegionWorkSpace', :identifier => 'feature.RegionWorkSpace', :visibility => 'public', :isRefinement => false, :owner => package_feature)
# Connected\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)
feature_WorkSpace = JavaM::Class.new(:name => 'WorkSpace', :identifier => 'feature.WorkSpace', :owner => package_used)

# Connected\Graph.jak

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        ConnectedComponents();Super#.run( s );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_ConnectedComponents12 = JavaM::Method.new(:name => 'feature.Graph.ConnectedComponents()', :identifier => 'feature.Graph.ConnectedComponents()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        GraphSearch( new RegionWorkSpace() );
    }", :ownerClass => feature_Graph)

# Connected\RegionWorkSpace.jak

feature_RegionWorkSpace.extends << feature_WorkSpace
field_feature_RegionWorkSpace_counter = JavaM::Field.new(:name => 'feature.RegionWorkSpace.counter', :identifier => 'feature.RegionWorkSpace.counter', :type => primitiveType_int, :visibility => 'package', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_RegionWorkSpace)
constructor_feature_RegionWorkSpace_RegionWorkSpace12 = JavaM::Constructor.new(:name => 'feature.RegionWorkSpace.RegionWorkSpace()', :identifier => 'feature.RegionWorkSpace.RegionWorkSpace()', :type => feature_RegionWorkSpace, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        counter = 0;}", :ownerClass => feature_RegionWorkSpace)

method_feature_RegionWorkSpace_init_vertex1Vertex2 = JavaM::Method.new(:name => 'feature.RegionWorkSpace.init_vertex(Vertex)', :identifier => 'feature.RegionWorkSpace.init_vertex(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        v.componentNumber = -1;
    }", :ownerClass => feature_RegionWorkSpace)
parameter_feature_RegionWorkSpace_init_vertex1Vertex2_v = JavaM::Parameter.new(:name => 'feature.RegionWorkSpace.init_vertex(Vertex).v', :identifier => 'feature.RegionWorkSpace.init_vertex(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_RegionWorkSpace_init_vertex1Vertex2)

method_feature_RegionWorkSpace_postVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.RegionWorkSpace.postVisitAction(Vertex)', :identifier => 'feature.RegionWorkSpace.postVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        v.componentNumber = counter;
    }", :ownerClass => feature_RegionWorkSpace)
parameter_feature_RegionWorkSpace_postVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.RegionWorkSpace.postVisitAction(Vertex).v', :identifier => 'feature.RegionWorkSpace.postVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_RegionWorkSpace_postVisitAction1Vertex2)

method_feature_RegionWorkSpace_nextRegionAction1Vertex2 = JavaM::Method.new(:name => 'feature.RegionWorkSpace.nextRegionAction(Vertex)', :identifier => 'feature.RegionWorkSpace.nextRegionAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        counter ++;
    }", :ownerClass => feature_RegionWorkSpace)
parameter_feature_RegionWorkSpace_nextRegionAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.RegionWorkSpace.nextRegionAction(Vertex).v', :identifier => 'feature.RegionWorkSpace.nextRegionAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_RegionWorkSpace_nextRegionAction1Vertex2)

# Connected\Vertex.jak

field_feature_Vertex_componentNumber = JavaM::Field.new(:name => 'feature.Vertex.componentNumber', :identifier => 'feature.Vertex.componentNumber', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.print( \" comp# \"+ componentNumber + \" \" );Super#.display();
    }", :ownerClass => feature_Vertex)

