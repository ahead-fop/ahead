
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'MSTPrim', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'MSTPrim', :identifier => 'feature', , :ownerLayer => layer)

# MSTPrim\Graph.jak

java_lang_Integer = JavaM::Class.new(:name => 'java.lang.Integer', :identifier => 'java.lang.Integer', :owner => package_used)
java_util_LinkedList = JavaM::Class.new(:name => 'java.util.LinkedList', :identifier => 'java.util.LinkedList', :owner => package_used)
java_util_Collections = JavaM::Class.new(:name => 'java.util.Collections', :identifier => 'java.util.Collections', :owner => package_used)
java_util_Comparator = JavaM::Class.new(:name => 'java.util.Comparator', :identifier => 'java.util.Comparator', :owner => package_used)
feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Graph.importsType << java_lang_Integer
feature_Graph.importsType << java_util_LinkedList
feature_Graph.importsType << java_util_Collections
feature_Graph.importsType << java_util_Comparator
# MSTPrim\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)
java_lang_String = JavaM::Class.new(:name => 'String', :identifier => 'java.lang.String', :owner => package_used)
feature_Vertex.importsType << java_lang_Integer
feature_Vertex.importsType << java_util_LinkedList
feature_Vertex.importsType << java_util_Collections
feature_Vertex.importsType << java_util_Comparator

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)

# MSTPrim\Graph.jak

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        Graph gaux = Prim( s );
        Graph.stopProfile();
        gaux.display();
        Graph.resumeProfile();Super#.run( s );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_Prim1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.Prim(Vertex)', :identifier => 'feature.Graph.Prim(Vertex)', :type => feature_Graph, :nesting => 0, :visibility => 'public', :body => " {
        Vertex root;
                
        root = r;
        int numvertices = vertices.size();
        int i;
        Vertex x;
                
        // 2. and 3. Initializes the vertices
        for ( i=0; i < numvertices; i++ )
        {
            x = ( Vertex )vertices.get( i );
            x.pred = null;
            x.key = Integer.MAX_VALUE;
        }
                
        // 4. and 5.        
        root.key = 0;
        root.pred = null;
                                
        // 2. S <- empty set
                
        // 1. Queue <- V[G], copy the vertex in the graph in the priority queue
        LinkedList Queue = new LinkedList();

        for( i=0; i < numvertices; i++ )
        {
            x = ( Vertex )vertices.get( i );
            if ( x.key != 0 ) // this means, if this is not the root
                Queue.add( x );
        }
                
        // Inserts the root at the head of the queue
        Queue.addFirst( root );
                
        // 6. while Q!=0
        Vertex ucurrent;
        int j,k,l;
        int pos;
        LinkedList Uneighbors;
        Vertex u,v;
        Edge en;
        Neighbor vn;
                
        int wuv;
        boolean isNeighborInQueue = false;
                
        // Queue is a list ordered by key values.
        // At the beginning all key values are INFINITUM except
        // for the root whose value is 0.
        while ( Queue.size()!=0 )
        {
            // 7. u <- Extract-Min(Q);                                                  
            // Since this is an ordered queue the first element is the min
            u = ( Vertex )Queue.removeFirst();
                                                                        
            // 8. for each vertex v adjacent to u
            Uneighbors = u.neighbors;

            for( k=0; k < Uneighbors.size(); k++ )
              {
                vn = ( Neighbor )Uneighbors.get( k );
                v = vn.end;
                en = vn.edge;

                // Check to see if the neighbor is in the queue
                isNeighborInQueue = false;
                                                                
                // if the Neighor is in the queue
                int indexNeighbor = Queue.indexOf( v );
                if ( indexNeighbor>=0 )
                    isNeighborInQueue=true;
                                                                                                                                                
                wuv = en.weight;
                                                                
                // 9. Relax (u,v w)
                if ( isNeighborInQueue && ( wuv < v.key ) )
                    {
                    v.key = wuv;
                    v.pred = u.name;
                    Uneighbors.set( k,vn ); // adjust values in the neighbors
                                                                                                    
                    // update the values of v in the queue                    
                    // Remove v from the Queue so that we can reinsert it
                    // in a new place according to its new value to keep
                    // the Linked List ordered
                    Object residue = Queue.remove( indexNeighbor );
                                                                                                    
                    // Get the new position for v
                    int position = Collections.binarySearch( Queue,v, 
                                              new Comparator() {
                        public int compare( Object o1, Object o2 )
                                                 {
                            Vertex v1 = ( Vertex )o1;
                            Vertex v2 = ( Vertex )o2;
                                
                            if ( v1.key < v2.key )
                                return -1;
                            if ( v1.key == v2.key )
                                return 0;
                            return 1;
                        }
                    } );
                                                                                                            
                    // Adds v in its new position in Queue                                                                                  
                    if ( position < 0 )  // means it is not there
                                         {
                        Queue.add( - ( position+1 ),v );
                    }
                    else      // means it is there
                                         {
                        Queue.add( position,v );
                    }
                                                                                                  
                } // if 8-9.
            } // for all neighbors
        } // of while

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
        Vertex theVertex, thePred;
        Vertex theNewVertex, theNewPred;
        Edge   e;
       
        // For each vertex in vertices list we find its predecessor
        // make an edge for the new graph from predecessor->vertex
        for( i=0; i<numvertices; i++ )
       {
            // theVertex and its Predecessor
            theVertex = ( Vertex )vertices.get( i );
            thePred = findsVertex( theVertex.pred );
         
            // if theVertex is the source then continue we dont need
            // to create a new edge at all
            if ( thePred==null )
                continue;
         
            // Find the references in the new Graph
            theNewVertex = newGraph.findsVertex( theVertex.name );
            theNewPred = newGraph.findsVertex( thePred.name );
         
            // Creates the new edge from predecessor -> vertex in the newGraph
            // and ajusts the adorns based on the old edge
            Edge theNewEdge = new  Edge();
            theNewEdge.EdgeConstructor( theNewPred, theNewVertex );
            e = findsEdge( thePred,theVertex );
            theNewEdge.adjustAdorns( e );
         
            // Adds the new edge to the newGraph
            newGraph.addEdge( theNewEdge );
        }
        return newGraph;
        
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_Prim1Vertex2_r = JavaM::Parameter.new(:name => 'feature.Graph.Prim(Vertex).r', :identifier => 'feature.Graph.Prim(Vertex).r', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_Prim1Vertex2)

# MSTPrim\Vertex.jak

field_feature_Vertex_pred = JavaM::Field.new(:name => 'feature.Vertex.pred', :identifier => 'feature.Vertex.pred', :type => java_lang_String, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
field_feature_Vertex_key = JavaM::Field.new(:name => 'feature.Vertex.key', :identifier => 'feature.Vertex.key', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.print( \" Pred \" + pred + \" Key \" + key + \" \" );Super#.display();
    }", :ownerClass => feature_Vertex)

