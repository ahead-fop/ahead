
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'MSTKruskal', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'MSTKruskal', :identifier => 'feature', , :ownerLayer => layer)

# MSTKruskal\Graph.jak

java_lang_Integer = JavaM::Class.new(:name => 'java.lang.Integer', :identifier => 'java.lang.Integer', :owner => package_used)
java_util_LinkedList = JavaM::Class.new(:name => 'java.util.LinkedList', :identifier => 'java.util.LinkedList', :owner => package_used)
java_util_Collections = JavaM::Class.new(:name => 'java.util.Collections', :identifier => 'java.util.Collections', :owner => package_used)
java_util_Comparator = JavaM::Class.new(:name => 'java.util.Comparator', :identifier => 'java.util.Comparator', :owner => package_used)
feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Graph.importsType << java_lang_Integer
feature_Graph.importsType << java_util_LinkedList
feature_Graph.importsType << java_util_Collections
feature_Graph.importsType << java_util_Comparator
# MSTKruskal\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Vertex.importsType << java_lang_Integer
feature_Vertex.importsType << java_util_LinkedList
feature_Vertex.importsType << java_util_Collections
feature_Vertex.importsType << java_util_Comparator

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)

# MSTKruskal\Graph.jak

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        Graph gaux = Kruskal();
        Graph.stopProfile();
        gaux.display();
        Graph.resumeProfile();Super#.run( s );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_Kruskal12 = JavaM::Method.new(:name => 'feature.Graph.Kruskal()', :identifier => 'feature.Graph.Kruskal()', :type => feature_Graph, :nesting => 0, :visibility => 'public', :body => " {
      
        // 1. A <- Empty set
        LinkedList A = new LinkedList();
                
        // 2. for each vertex v E V[G]
        // 3.    do Make-Set(v)
        int numvertices = vertices.size();
        int i;
        Vertex v;
                
        for ( i=0; i < numvertices; i++ )
        {
            v = ( Vertex )vertices.get( i );
            v.representative = v; // I am in my set
            v.members = new LinkedList(); // I have no members in my set
        }
                
        // 4. sort the edges of E by nondecreasing weight w        
        // Creates the edges objects
        int j;
        LinkedList Vneighbors = new LinkedList();
        Vertex u;
                
        // Sort the Edges in non decreasing order
        Collections.sort( edges, 
          new Comparator() {
            public int compare( Object o1, Object o2 )
                 {
                Edge e1 = ( Edge )o1;
                Edge e2 = ( Edge )o2;
                if ( e1.weight < e2.weight )
                    return -1;
                if ( e1.weight == e2.weight )
                    return 0;
                return 1;
            }
        } );
        
        // 5. for each edge in the nondecresing order
        int numedges = edges.size();
        Edge e1;
        Vertex vaux, urep, vrep;
                
        for( i=0; i<numedges; i++ )
        {
            // 6. if Find-Set(u)!=Find-Set(v)
            e1 = ( Edge )edges.get( i );
            u = e1.start;
            v = e1.end;

            if ( ! ( v.representative.name ).equals( u.representative.name ) )
              {
                // 7. A <- A U {(u,v)}
                A.add( e1 );
                                                                
                // 8. Union(u,v)
                urep = u.representative;
                vrep = v.representative;
                                                                 
                if ( ( urep.members ).size() > ( vrep.members ).size() )
                    { // we add elements of v to u
                    for( j=0; j< ( vrep.members ).size(); j++ )
                          {
                        vaux = ( Vertex ) ( vrep.members ).get( j );
                        vaux.representative = urep;
                        ( urep.members ).add( vaux );
                    }
                    v.representative = urep;
                    vrep.representative = urep;
                    ( urep.members ).add( v );
                    if ( !v.equals( vrep ) )
                        ( urep.members ).add( vrep );
                    ( vrep.members ).clear();
                }
                else
                     { // we add elements of u to v
                    for( j=0; j< ( urep.members ).size(); j++ )
                           {
                        vaux = ( Vertex ) ( urep.members ).get( j );
                        vaux.representative = vrep;
                        ( vrep.members ).add( vaux );
                    }
                    u.representative = vrep;
                    urep.representative = vrep;
                    ( vrep.members ).add( u );
                    if ( !u.equals( urep ) )
                        ( vrep.members ).add( urep );
                    ( urep.members ).clear();
                                                                                
                } // else
                                                
            } // of if
                        
        } // of for numedges
                
        // 9. return A
        // Creates the new Graph that contains the SSSP
        String theName;
        Graph newGraph = new  Graph();
                
        // Creates and adds the vertices with the same name
        for ( i=0; i<numvertices; i++ )
      {
            theName = ( ( Vertex )vertices.get( i ) ).name;
            newGraph.addVertex( new  Vertex().assignName( theName ) );
        }
        
        // Creates the edges from the NewGraph
        Vertex theStart, theEnd;
        Vertex theNewStart, theNewEnd;
        Edge   theEdge;
       
        // For each edge in A we find its two vertices
        // make an edge for the new graph from with the correspoding
        // new two vertices
        for( i=0; i<A.size(); i++ )
       {
            // theEdge with its two vertices
            theEdge = ( Edge )A.get( i );
            theStart = theEdge.start;
            theEnd = theEdge.end;
         
            // Find the references in the new Graph
            theNewStart = newGraph.findsVertex( theStart.name );
            theNewEnd = newGraph.findsVertex( theEnd.name );
         
            // Creates the new edge with new start and end vertices 
                // in the newGraph
            // and ajusts the adorns based on the old edge
            Edge theNewEdge = new  Edge();
            theNewEdge.EdgeConstructor( theNewStart, theNewEnd );
            theNewEdge.adjustAdorns( theEdge );
         
            // Adds the new edge to the newGraph
            newGraph.addEdge( theNewEdge );
        }
        return newGraph;
        
    }", :ownerClass => feature_Graph)

# MSTKruskal\Vertex.jak

field_feature_Vertex_representative = JavaM::Field.new(:name => 'feature.Vertex.representative', :identifier => 'feature.Vertex.representative', :type => feature_Vertex, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
field_feature_Vertex_members = JavaM::Field.new(:name => 'feature.Vertex.members', :identifier => 'feature.Vertex.members', :type => java_util_LinkedList, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        if ( representative == null )
            System.out.print( \"Rep null \" );
        else
            System.out.print( \" Rep \" + representative.name + \" \" );Super#.display();
    }", :ownerClass => feature_Vertex)

