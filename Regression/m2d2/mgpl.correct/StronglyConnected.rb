
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'StronglyConnected', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'StronglyConnected', :identifier => 'feature', , :ownerLayer => layer)

# StronglyConnected\FinishTimeWorkSpace.jak

java_util_LinkedList = JavaM::Class.new(:name => 'java.util.LinkedList', :identifier => 'java.util.LinkedList', :owner => package_used)
java_util_Collections = JavaM::Class.new(:name => 'java.util.Collections', :identifier => 'java.util.Collections', :owner => package_used)
java_util_Comparator = JavaM::Class.new(:name => 'java.util.Comparator', :identifier => 'java.util.Comparator', :owner => package_used)
feature_FinishTimeWorkSpace = JavaM::Class.new(:name => 'FinishTimeWorkSpace', :identifier => 'feature.FinishTimeWorkSpace', :visibility => 'public', :isRefinement => false, :owner => package_feature)
feature_FinishTimeWorkSpace.importsType << java_util_LinkedList
feature_FinishTimeWorkSpace.importsType << java_util_Collections
feature_FinishTimeWorkSpace.importsType << java_util_Comparator
# StronglyConnected\Graph.jak

feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Graph.importsType << java_util_LinkedList
feature_Graph.importsType << java_util_Collections
feature_Graph.importsType << java_util_Comparator
# StronglyConnected\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Vertex.importsType << java_util_LinkedList
feature_Vertex.importsType << java_util_Collections
feature_Vertex.importsType << java_util_Comparator
# StronglyConnected\WorkSpaceTranspose.jak

feature_WorkSpaceTranspose = JavaM::Class.new(:name => 'WorkSpaceTranspose', :identifier => 'feature.WorkSpaceTranspose', :visibility => 'public', :isRefinement => false, :owner => package_feature)
feature_WorkSpaceTranspose.importsType << java_util_LinkedList
feature_WorkSpaceTranspose.importsType << java_util_Collections
feature_WorkSpaceTranspose.importsType << java_util_Comparator

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)
feature_WorkSpace = JavaM::Class.new(:name => 'WorkSpace', :identifier => 'feature.WorkSpace', :owner => package_used)

# StronglyConnected\FinishTimeWorkSpace.jak

feature_FinishTimeWorkSpace.extends << feature_WorkSpace
field_feature_FinishTimeWorkSpace_FinishCounter = JavaM::Field.new(:name => 'feature.FinishTimeWorkSpace.FinishCounter', :identifier => 'feature.FinishTimeWorkSpace.FinishCounter', :type => primitiveType_int, :visibility => 'package', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_FinishTimeWorkSpace)
constructor_feature_FinishTimeWorkSpace_FinishTimeWorkSpace12 = JavaM::Constructor.new(:name => 'feature.FinishTimeWorkSpace.FinishTimeWorkSpace()', :identifier => 'feature.FinishTimeWorkSpace.FinishTimeWorkSpace()', :type => feature_FinishTimeWorkSpace, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        FinishCounter = 1;}", :ownerClass => feature_FinishTimeWorkSpace)

method_feature_FinishTimeWorkSpace_preVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.FinishTimeWorkSpace.preVisitAction(Vertex)', :identifier => 'feature.FinishTimeWorkSpace.preVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
      {
        if ( v.visited!=true )
            FinishCounter++;
    }", :ownerClass => feature_FinishTimeWorkSpace)
parameter_feature_FinishTimeWorkSpace_preVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.FinishTimeWorkSpace.preVisitAction(Vertex).v', :identifier => 'feature.FinishTimeWorkSpace.preVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_FinishTimeWorkSpace_preVisitAction1Vertex2)

method_feature_FinishTimeWorkSpace_postVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.FinishTimeWorkSpace.postVisitAction(Vertex)', :identifier => 'feature.FinishTimeWorkSpace.postVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        v.finishTime = FinishCounter++;
    }", :ownerClass => feature_FinishTimeWorkSpace)
parameter_feature_FinishTimeWorkSpace_postVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.FinishTimeWorkSpace.postVisitAction(Vertex).v', :identifier => 'feature.FinishTimeWorkSpace.postVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_FinishTimeWorkSpace_postVisitAction1Vertex2)

# StronglyConnected\Graph.jak

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        Graph gaux = StrongComponents();
        Graph.stopProfile();
        gaux.display();
        Graph.resumeProfile();Super#.run( s );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_StrongComponents12 = JavaM::Method.new(:name => 'feature.Graph.StrongComponents()', :identifier => 'feature.Graph.StrongComponents()', :type => feature_Graph, :nesting => 0, :visibility => 'public', :body => " {
                
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
  
        // 3. Compute the transpose of G
        // Done at layer transpose                     
        Graph gaux = ComputeTranspose( ( Graph )this );
            
        // 4. Traverse the transpose G
        WorkSpaceTranspose WST = new WorkSpaceTranspose();
        gaux.GraphSearch( WST );
 
        return gaux;
        
    }", :ownerClass => feature_Graph)

# StronglyConnected\Vertex.jak

field_feature_Vertex_finishTime = JavaM::Field.new(:name => 'feature.Vertex.finishTime', :identifier => 'feature.Vertex.finishTime', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
field_feature_Vertex_strongComponentNumber = JavaM::Field.new(:name => 'feature.Vertex.strongComponentNumber', :identifier => 'feature.Vertex.strongComponentNumber', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.print( \" FinishTime -> \" + finishTime + \" SCCNo -> \" 
                        + strongComponentNumber );Super#.display();
    }", :ownerClass => feature_Vertex)

# StronglyConnected\WorkSpaceTranspose.jak

feature_WorkSpaceTranspose.extends << feature_WorkSpace
field_feature_WorkSpaceTranspose_SCCCounter = JavaM::Field.new(:name => 'feature.WorkSpaceTranspose.SCCCounter', :identifier => 'feature.WorkSpaceTranspose.SCCCounter', :type => primitiveType_int, :visibility => 'package', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_WorkSpaceTranspose)
constructor_feature_WorkSpaceTranspose_WorkSpaceTranspose12 = JavaM::Constructor.new(:name => 'feature.WorkSpaceTranspose.WorkSpaceTranspose()', :identifier => 'feature.WorkSpaceTranspose.WorkSpaceTranspose()', :type => feature_WorkSpaceTranspose, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        SCCCounter = 0;}", :ownerClass => feature_WorkSpaceTranspose)

method_feature_WorkSpaceTranspose_preVisitAction1Vertex2 = JavaM::Method.new(:name => 'feature.WorkSpaceTranspose.preVisitAction(Vertex)', :identifier => 'feature.WorkSpaceTranspose.preVisitAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
    {
        if ( v.visited!=true ) 
          {
            v.strongComponentNumber = SCCCounter;
        }
        ;
    }", :ownerClass => feature_WorkSpaceTranspose)
parameter_feature_WorkSpaceTranspose_preVisitAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.WorkSpaceTranspose.preVisitAction(Vertex).v', :identifier => 'feature.WorkSpaceTranspose.preVisitAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpaceTranspose_preVisitAction1Vertex2)

method_feature_WorkSpaceTranspose_nextRegionAction1Vertex2 = JavaM::Method.new(:name => 'feature.WorkSpaceTranspose.nextRegionAction(Vertex)', :identifier => 'feature.WorkSpaceTranspose.nextRegionAction(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " 
    {
        SCCCounter++;
    }", :ownerClass => feature_WorkSpaceTranspose)
parameter_feature_WorkSpaceTranspose_nextRegionAction1Vertex2_v = JavaM::Parameter.new(:name => 'feature.WorkSpaceTranspose.nextRegionAction(Vertex).v', :identifier => 'feature.WorkSpaceTranspose.nextRegionAction(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_WorkSpaceTranspose_nextRegionAction1Vertex2)

