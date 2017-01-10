
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Undirected', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Undirected', :identifier => 'feature', , :ownerLayer => layer)

# Undirected\Edge.jak

java_util_LinkedList = JavaM::Class.new(:name => 'java.util.LinkedList', :identifier => 'java.util.LinkedList', :owner => package_used)
feature_Edge = JavaM::Class.new(:name => 'Edge', :identifier => 'feature.Edge', :visibility => 'public', :isRefinement => false, :owner => package_feature)
feature_Edge.importsType << java_util_LinkedList
# Undirected\Graph.jak

feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => false, :owner => package_feature)
java_lang_String = JavaM::Class.new(:name => 'String', :identifier => 'java.lang.String', :owner => package_used)
feature_Graph.importsType << java_util_LinkedList
# Undirected\Neighbor.jak

feature_Neighbor = JavaM::Class.new(:name => 'Neighbor', :identifier => 'feature.Neighbor', :visibility => 'public', :isRefinement => false, :owner => package_feature)
feature_Neighbor.importsType << java_util_LinkedList
# Undirected\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => false, :owner => package_feature)
feature_Vertex.importsType << java_util_LinkedList

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)
primitiveType_boolean = JavaM::PrimitiveType.new(:name =>'boolean', :identifier =>'boolean', :owner => package_used)

# Undirected\Edge.jak

feature_Edge.extends << feature_Neighbor
field_feature_Edge_start = JavaM::Field.new(:name => 'feature.Edge.start', :identifier => 'feature.Edge.start', :type => feature_Vertex, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Edge)
method_feature_Edge_EdgeConstructor1Vertex_Vertex2 = JavaM::Method.new(:name => 'feature.Edge.EdgeConstructor(Vertex,Vertex)', :identifier => 'feature.Edge.EdgeConstructor(Vertex,Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        start = the_start;
        end = the_end;
    }", :ownerClass => feature_Edge)
parameter_feature_Edge_EdgeConstructor1Vertex_Vertex2_the_start = JavaM::Parameter.new(:name => 'feature.Edge.EdgeConstructor(Vertex,Vertex).the_start', :identifier => 'feature.Edge.EdgeConstructor(Vertex,Vertex).the_start', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Edge_EdgeConstructor1Vertex_Vertex2)
parameter_feature_Edge_EdgeConstructor1Vertex_Vertex2_the_end = JavaM::Parameter.new(:name => 'feature.Edge.EdgeConstructor(Vertex,Vertex).the_end', :identifier => 'feature.Edge.EdgeConstructor(Vertex,Vertex).the_end', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Edge_EdgeConstructor1Vertex_Vertex2)

method_feature_Edge_adjustAdorns1Edge2 = JavaM::Method.new(:name => 'feature.Edge.adjustAdorns(Edge)', :identifier => 'feature.Edge.adjustAdorns(Edge)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {}", :ownerClass => feature_Edge)
parameter_feature_Edge_adjustAdorns1Edge2_the_edge = JavaM::Parameter.new(:name => 'feature.Edge.adjustAdorns(Edge).the_edge', :identifier => 'feature.Edge.adjustAdorns(Edge).the_edge', :type => feature_Edge, :nesting => 0, :ownerMethod => method_feature_Edge_adjustAdorns1Edge2)

method_feature_Edge_display12 = JavaM::Method.new(:name => 'feature.Edge.display()', :identifier => 'feature.Edge.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.println( \" start=\" + start.name + \" end=\" + end.name );
    }", :ownerClass => feature_Edge)

# Undirected\Graph.jak

field_feature_Graph_vertices = JavaM::Field.new(:name => 'feature.Graph.vertices', :identifier => 'feature.Graph.vertices', :type => java_util_LinkedList, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Graph)
field_feature_Graph_edges = JavaM::Field.new(:name => 'feature.Graph.edges', :identifier => 'feature.Graph.edges', :type => java_util_LinkedList, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Graph)
field_feature_Graph_isDirected = JavaM::Field.new(:name => 'feature.Graph.isDirected', :identifier => 'feature.Graph.isDirected', :type => primitiveType_boolean, :visibility => 'public', :nesting => 0, :isStatic => true, :isFinal => true, :ownerClass => feature_Graph, :value => ' = false')
constructor_feature_Graph_Graph12 = JavaM::Constructor.new(:name => 'feature.Graph.Graph()', :identifier => 'feature.Graph.Graph()', :type => feature_Graph, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        vertices = new LinkedList();
        edges = new LinkedList();}", :ownerClass => feature_Graph)

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {}", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_addAnEdge1Vertex_Vertex_int2 = JavaM::Method.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int)', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
      {
        Edge theEdge = new  Edge();
        theEdge.EdgeConstructor( start, end );
        addEdge( theEdge );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_addAnEdge1Vertex_Vertex_int2_start = JavaM::Parameter.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int).start', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int).start', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_addAnEdge1Vertex_Vertex_int2)
parameter_feature_Graph_addAnEdge1Vertex_Vertex_int2_end = JavaM::Parameter.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int).end', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int).end', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_addAnEdge1Vertex_Vertex_int2)
parameter_feature_Graph_addAnEdge1Vertex_Vertex_int2_weight = JavaM::Parameter.new(:name => 'feature.Graph.addAnEdge(Vertex,Vertex,int).weight', :identifier => 'feature.Graph.addAnEdge(Vertex,Vertex,int).weight', :type => primitiveType_int, :nesting => 0, :ownerMethod => method_feature_Graph_addAnEdge1Vertex_Vertex_int2)

method_feature_Graph_addVertex1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.addVertex(Vertex)', :identifier => 'feature.Graph.addVertex(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        vertices.add( v );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_addVertex1Vertex2_v = JavaM::Parameter.new(:name => 'feature.Graph.addVertex(Vertex).v', :identifier => 'feature.Graph.addVertex(Vertex).v', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_addVertex1Vertex2)

method_feature_Graph_addEdge1Edge2 = JavaM::Method.new(:name => 'feature.Graph.addEdge(Edge)', :identifier => 'feature.Graph.addEdge(Edge)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        Vertex start = the_edge.start;
        Vertex end = the_edge.end;
        edges.add( the_edge );
        start.addNeighbor( new  Neighbor( end,the_edge ) );
        end.addNeighbor( new  Neighbor( start,the_edge ) );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_addEdge1Edge2_the_edge = JavaM::Parameter.new(:name => 'feature.Graph.addEdge(Edge).the_edge', :identifier => 'feature.Graph.addEdge(Edge).the_edge', :type => feature_Edge, :nesting => 0, :ownerMethod => method_feature_Graph_addEdge1Edge2)

method_feature_Graph_addOnlyEdge1Edge2 = JavaM::Method.new(:name => 'feature.Graph.addOnlyEdge(Edge)', :identifier => 'feature.Graph.addOnlyEdge(Edge)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        edges.add( the_edge );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_addOnlyEdge1Edge2_the_edge = JavaM::Parameter.new(:name => 'feature.Graph.addOnlyEdge(Edge).the_edge', :identifier => 'feature.Graph.addOnlyEdge(Edge).the_edge', :type => feature_Edge, :nesting => 0, :ownerMethod => method_feature_Graph_addOnlyEdge1Edge2)

method_feature_Graph_findsVertex1String2 = JavaM::Method.new(:name => 'feature.Graph.findsVertex(String)', :identifier => 'feature.Graph.findsVertex(String)', :type => feature_Vertex, :nesting => 0, :visibility => 'public', :body => "
      {
        int i=0;
        Vertex theVertex;
        
        // if we are dealing with the root
        if ( theName==null )
            return null;
            
        for( i=0; i<vertices.size(); i++ )
        {
            theVertex = ( Vertex )vertices.get( i );
            if ( theName.equals( theVertex.name ) )
                return theVertex;
        }
        return null;
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_findsVertex1String2_theName = JavaM::Parameter.new(:name => 'feature.Graph.findsVertex(String).theName', :identifier => 'feature.Graph.findsVertex(String).theName', :type => java_lang_String, :nesting => 0, :ownerMethod => method_feature_Graph_findsVertex1String2)

method_feature_Graph_findsEdge1Vertex_Vertex2 = JavaM::Method.new(:name => 'feature.Graph.findsEdge(Vertex,Vertex)', :identifier => 'feature.Graph.findsEdge(Vertex,Vertex)', :type => feature_Edge, :nesting => 0, :visibility => 'public', :body => "
       {
        int i=0;
        Edge theEdge;
        
        for( i=0; i<edges.size(); i++ )
         {
            theEdge = ( Edge )edges.get( i );
            if ( ( theEdge.start.name.equals( theSource.name ) && 
                  theEdge.end.name.equals( theTarget.name ) ) ||
                 ( theEdge.start.name.equals( theTarget.name ) && 
                  theEdge.end.name.equals( theSource.name ) ) )
                return theEdge;
        }
        return null;
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_findsEdge1Vertex_Vertex2_theSource = JavaM::Parameter.new(:name => 'feature.Graph.findsEdge(Vertex,Vertex).theSource', :identifier => 'feature.Graph.findsEdge(Vertex,Vertex).theSource', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_findsEdge1Vertex_Vertex2)
parameter_feature_Graph_findsEdge1Vertex_Vertex2_theTarget = JavaM::Parameter.new(:name => 'feature.Graph.findsEdge(Vertex,Vertex).theTarget', :identifier => 'feature.Graph.findsEdge(Vertex,Vertex).theTarget', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_findsEdge1Vertex_Vertex2)

method_feature_Graph_display12 = JavaM::Method.new(:name => 'feature.Graph.display()', :identifier => 'feature.Graph.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        int i;
                
        System.out.println( \"******************************************\" );
        System.out.println( \"Vertices \" );
        for ( i=0; i<vertices.size(); i++ )
            ( ( Vertex ) vertices.get( i ) ).display();
         
        System.out.println( \"******************************************\" );
        System.out.println( \"Edges \" );
        for ( i=0; i<edges.size(); i++ )
            ( ( Edge ) edges.get( i ) ).display();
                
        System.out.println( \"******************************************\" );
     
    }", :ownerClass => feature_Graph)

# Undirected\Neighbor.jak

field_feature_Neighbor_end = JavaM::Field.new(:name => 'feature.Neighbor.end', :identifier => 'feature.Neighbor.end', :type => feature_Vertex, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Neighbor)
field_feature_Neighbor_edge = JavaM::Field.new(:name => 'feature.Neighbor.edge', :identifier => 'feature.Neighbor.edge', :type => feature_Edge, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Neighbor)
constructor_feature_Neighbor_Neighbor12 = JavaM::Constructor.new(:name => 'feature.Neighbor.Neighbor()', :identifier => 'feature.Neighbor.Neighbor()', :type => feature_Neighbor, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        end = null;
        edge = null;}", :ownerClass => feature_Neighbor)

constructor_feature_Neighbor_Neighbor1Vertex_Edge2 = JavaM::Constructor.new(:name => 'feature.Neighbor.Neighbor(Vertex,Edge)', :identifier => 'feature.Neighbor.Neighbor(Vertex,Edge)', :type => feature_Neighbor, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        end = v;
        edge = e;}", :ownerClass => feature_Neighbor)
parameter_feature_Neighbor_Neighbor1Vertex_Edge2_v = JavaM::Parameter.new(:name => 'feature.Neighbor.Neighbor(Vertex,Edge).v', :identifier => 'feature.Neighbor.Neighbor(Vertex,Edge).v', :type => feature_Vertex, :nesting => 0, :ownerConstructor => constructor_feature_Neighbor_Neighbor1Vertex_Edge2)
parameter_feature_Neighbor_Neighbor1Vertex_Edge2_e = JavaM::Parameter.new(:name => 'feature.Neighbor.Neighbor(Vertex,Edge).e', :identifier => 'feature.Neighbor.Neighbor(Vertex,Edge).e', :type => feature_Edge, :nesting => 0, :ownerConstructor => constructor_feature_Neighbor_Neighbor1Vertex_Edge2)

# Undirected\Vertex.jak

field_feature_Vertex_neighbors = JavaM::Field.new(:name => 'feature.Vertex.neighbors', :identifier => 'feature.Vertex.neighbors', :type => java_util_LinkedList, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
field_feature_Vertex_name = JavaM::Field.new(:name => 'feature.Vertex.name', :identifier => 'feature.Vertex.name', :type => java_lang_String, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
constructor_feature_Vertex_Vertex12 = JavaM::Constructor.new(:name => 'feature.Vertex.Vertex()', :identifier => 'feature.Vertex.Vertex()', :type => feature_Vertex, :nesting => 0, :visibility => 'public', :isRefinement => 'false', :body => "{
        VertexConstructor();}", :ownerClass => feature_Vertex)

method_feature_Vertex_VertexConstructor12 = JavaM::Method.new(:name => 'feature.Vertex.VertexConstructor()', :identifier => 'feature.Vertex.VertexConstructor()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        name      = null;
        neighbors = new LinkedList();
    }", :ownerClass => feature_Vertex)

method_feature_Vertex_assignName1String2 = JavaM::Method.new(:name => 'feature.Vertex.assignName(String)', :identifier => 'feature.Vertex.assignName(String)', :type => feature_Vertex, :nesting => 0, :visibility => 'public', :body => " {
        this.name = name;
        return ( Vertex ) this;
    }", :ownerClass => feature_Vertex)
parameter_feature_Vertex_assignName1String2_name = JavaM::Parameter.new(:name => 'feature.Vertex.assignName(String).name', :identifier => 'feature.Vertex.assignName(String).name', :type => java_lang_String, :nesting => 0, :ownerMethod => method_feature_Vertex_assignName1String2)

method_feature_Vertex_addNeighbor1Neighbor2 = JavaM::Method.new(:name => 'feature.Vertex.addNeighbor(Neighbor)', :identifier => 'feature.Vertex.addNeighbor(Neighbor)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        neighbors.add( n );
    }", :ownerClass => feature_Vertex)
parameter_feature_Vertex_addNeighbor1Neighbor2_n = JavaM::Parameter.new(:name => 'feature.Vertex.addNeighbor(Neighbor).n', :identifier => 'feature.Vertex.addNeighbor(Neighbor).n', :type => feature_Neighbor, :nesting => 0, :ownerMethod => method_feature_Vertex_addNeighbor1Neighbor2)

method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        int s = neighbors.size();
        int i;

        System.out.print( \" Node \" + name + \" connected to: \" );

        for ( i=0; i<s; i++ ) 
         {
            Neighbor theNeighbor = ( Neighbor ) neighbors.get( i );
            System.out.print( theNeighbor.end.name + \", \" );
        }
        System.out.println();
    }", :ownerClass => feature_Vertex)

