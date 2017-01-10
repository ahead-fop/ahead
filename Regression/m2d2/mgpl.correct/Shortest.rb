
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Shortest', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Shortest', :identifier => 'feature', , :ownerLayer => layer)

# Shortest\Graph.jak

java_lang_Integer = JavaM::Class.new(:name => 'java.lang.Integer', :identifier => 'java.lang.Integer', :owner => package_used)
java_util_LinkedList = JavaM::Class.new(:name => 'java.util.LinkedList', :identifier => 'java.util.LinkedList', :owner => package_used)
java_util_Collections = JavaM::Class.new(:name => 'java.util.Collections', :identifier => 'java.util.Collections', :owner => package_used)
java_util_Comparator = JavaM::Class.new(:name => 'java.util.Comparator', :identifier => 'java.util.Comparator', :owner => package_used)
feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Graph.importsType << java_lang_Integer
feature_Graph.importsType << java_util_LinkedList
feature_Graph.importsType << java_util_Collections
feature_Graph.importsType << java_util_Comparator
# Shortest\Vertex.jak

feature_Vertex = JavaM::Class.new(:name => 'Vertex', :identifier => 'feature.Vertex', :visibility => 'public', :isRefinement => true, :owner => package_feature)
java_lang_String = JavaM::Class.new(:name => 'String', :identifier => 'java.lang.String', :owner => package_used)
feature_Vertex.importsType << java_lang_Integer
feature_Vertex.importsType << java_util_LinkedList
feature_Vertex.importsType << java_util_Collections
feature_Vertex.importsType << java_util_Comparator

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)
primitiveType_int = JavaM::PrimitiveType.new(:name =>'int', :identifier =>'int', :owner => package_used)

# Shortest\Graph.jak

method_feature_Graph_run1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.run(Vertex)', :identifier => 'feature.Graph.run(Vertex)', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => "
     {
        Graph gaux = ShortestPath( s );
        Graph.stopProfile();
        gaux.display();
        Graph.resumeProfile();Super#.run( s );
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_run1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.run(Vertex).s', :identifier => 'feature.Graph.run(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_run1Vertex2)

method_feature_Graph_ShortestPath1Vertex2 = JavaM::Method.new(:name => 'feature.Graph.ShortestPath(Vertex)', :identifier => 'feature.Graph.ShortestPath(Vertex)', :type => feature_Graph, :nesting => 0, :visibility => 'public', :body => " {
        Vertex source;
                
        source = s;
        int numvertices = vertices.size();
        int i;
        Vertex x,n;
        Neighbor vn;
        int wuvn;
        int k1;
                                        
        // 1. Initializes the single source
        for ( i=0; i < numvertices; i++ )
        {
            x = ( Vertex )vertices.get( i );
            x.predecessor = null;
            x.dweight = Integer.MAX_VALUE;
        }
                        
        source.dweight = 0;
        source.predecessor = null;
                                
        // 2. S <- empty set
        LinkedList S = new LinkedList();
                
        // 3. Queue <- V[G], copy the vertex in the graph in the priority queue
        LinkedList Queue = new LinkedList();
        for( i=0; i < numvertices; i++ )
        {
            x = ( Vertex )vertices.get( i );
            if ( x.dweight != 0 ) // this means, if this is not the source
                Queue.add( x );
        }
                
        // Inserts the source at the head of the queue
        Queue.addFirst( source );
                
        // 4. while Q!=0
        Vertex ucurrent;
        int j,k,l;
        int pos;
        LinkedList Uneighbors;
        Vertex u,v;
        Edge en;
        int wuv;
                
        while ( Queue.size()!=0 )
        {
            // 5. u <- Extract-Min(Q);
            u = ( Vertex )Queue.removeFirst();
                                    
            // 6. S <- S U {u} 
            S.add( u );
                                                                        
            // 7. for each vertex v adjacent to u
            Uneighbors = u.neighbors;

            // For all the neighbors
            for( k=0; k < Uneighbors.size(); k++ )
              {
                vn = ( Neighbor )Uneighbors.get( k );
                v = vn.end;
                en = vn.edge;
                wuv = en.weight;
  
                // 8. Relax (u,v w)
                if ( v.dweight > ( u.dweight +  wuv ) )
                     {
                    v.dweight = u.dweight +  wuv;
                    v.predecessor = u.name;
                    Uneighbors.set( k,vn ); // adjust values in the neighbors
                                                                                                    
                    // update the values of v in the queue
                    int indexNeighbor = Queue.indexOf( v );
                    if ( indexNeighbor>=0 ) 
                                        {
                        Object residue = Queue.remove( indexNeighbor );
                                                                        
                        // Get the new position for v
                        int position = Collections.binarySearch( Queue,v, 
                                                   new Comparator() {
                            public int compare( Object o1, Object o2 )
                                                                                    {
                                Vertex v1 = ( Vertex )o1;
                                Vertex v2 = ( Vertex )o2;
                                
                                if ( v1.dweight < v2.dweight )
                                    return -1;

                                if ( v1.dweight == v2.dweight )
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
                                                                                                                                            
                    } // if it is in the Queue
                                                                
                } // if 8.
            } // for
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
        Neighbor theNeighbor,newNeighbor;
        boolean flag = false;

        // For each vertex in vertices list we find its predecessor
        // make an edge for the new graph from predecessor->vertex
        for( i=0; i<numvertices; i++ )
       {
            // theVertex and its Predecessor
            theVertex = ( Vertex )vertices.get( i );
            thePred = findsVertex( theVertex.predecessor );
         
            // if theVertex is the source then continue we dont need
            // to create a new edge at all
            if ( thePred==null )
                continue;
         
            // Find the references in the new Graph
            theNewVertex = newGraph.findsVertex( theVertex.name );
            theNewPred = newGraph.findsVertex( thePred.name );
 
            // theNeighbor corresponds to the neighbor formed with
            // theVertex -> thePred
            // find the corresponding neighbor of the Vertex, that is,
            // predecessor
            j=0;
            flag=false;
            do
          {
                theNeighbor = ( Neighbor ) ( thePred.neighbors ).get( j );
                if ( theNeighbor.end.name.equals( theVertex.name ) )
                    flag = true;
                else
                    j++;
            }
            while( flag==false && j< thePred.neighbors.size() );
        
            // Creates the new edge from predecessor -> vertex in the newGraph
            // and ajusts the adorns based on the old edge
            Edge theNewEdge = new  Edge();
            theNewEdge.EdgeConstructor( theNewPred, theNewVertex );
            e = theNeighbor.edge;
            theNewEdge.adjustAdorns( e );
         
            // Adds the new edge to the newGraph
            newGraph.addEdge( theNewEdge );
        }
          
        return newGraph;
         
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_ShortestPath1Vertex2_s = JavaM::Parameter.new(:name => 'feature.Graph.ShortestPath(Vertex).s', :identifier => 'feature.Graph.ShortestPath(Vertex).s', :type => feature_Vertex, :nesting => 0, :ownerMethod => method_feature_Graph_ShortestPath1Vertex2)

# Shortest\Vertex.jak

field_feature_Vertex_predecessor = JavaM::Field.new(:name => 'feature.Vertex.predecessor', :identifier => 'feature.Vertex.predecessor', :type => java_lang_String, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
field_feature_Vertex_dweight = JavaM::Field.new(:name => 'feature.Vertex.dweight', :identifier => 'feature.Vertex.dweight', :type => primitiveType_int, :visibility => 'public', :nesting => 0, :isStatic => false, :isFinal => false, :ownerClass => feature_Vertex)
method_feature_Vertex_display12 = JavaM::Method.new(:name => 'feature.Vertex.display()', :identifier => 'feature.Vertex.display()', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        System.out.print( \"Pred \" + predecessor + \" DWeight \" + dweight + \" \" );Super#.display();
    }", :ownerClass => feature_Vertex)

