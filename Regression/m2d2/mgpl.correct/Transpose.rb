
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Transpose', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Transpose', :identifier => 'feature', , :ownerLayer => layer)

# Transpose\Graph.jak

java_util_LinkedList = JavaM::Class.new(:name => 'java.util.LinkedList', :identifier => 'java.util.LinkedList', :owner => package_used)
feature_Graph = JavaM::Class.new(:name => 'Graph', :identifier => 'feature.Graph', :visibility => 'public', :isRefinement => true, :owner => package_feature)
feature_Graph.importsType << java_util_LinkedList

#  implicitly imported files


# Transpose\Graph.jak

method_feature_Graph_ComputeTranspose1Graph2 = JavaM::Method.new(:name => 'feature.Graph.ComputeTranspose(Graph)', :identifier => 'feature.Graph.ComputeTranspose(Graph)', :type => feature_Graph, :nesting => 0, :visibility => 'public', :body => "
   {
        int i;
        int num_vertices = ( the_graph.vertices ).size();
        String theName;
                
        // Creating the new Graph
        Graph newGraph = new  Graph();
                
        // Creates and adds the vertices with the same name
        for ( i=0; i<num_vertices; i++ )
     {
            theName = ( ( Vertex ) ( the_graph.vertices ).get( i ) ).name;
            newGraph.addVertex( new  Vertex().assignName( theName ) );
        }

        Neighbor newNeighbor;
        Vertex theVertex, newVertex;
        Neighbor theNeighbor;
        Vertex newAdjacent;
        Edge newEdge;
        int num_neighbors;
        int j;

        // adds the transposed edges
        for ( i=0; i<num_vertices; i++ )
    {
            // theVertex is the original source vertex
            // the newAdjacent is the reference in the newGraph to theVertex  
            theVertex = ( Vertex ) ( the_graph.vertices ).get( i );
            newAdjacent = newGraph.findsVertex( theVertex.name );
            num_neighbors = ( theVertex.neighbors ).size();
                               
            for( j=0; j<num_neighbors; j++ )
       {
                // Gets the neighbor object in pos j
                theNeighbor = ( Neighbor ) ( theVertex.neighbors ).get( j );
              
                // the new Vertex is the vertex that was adjacent to theVertex
                // but now in the new graph
                newVertex = newGraph.findsVertex( theNeighbor.end.name );
                          
                // Creates a new Edge object and adjusts the adornments 
                newEdge = new  Edge();
                newEdge.EdgeConstructor( newVertex,newAdjacent );
                newEdge.adjustAdorns( theNeighbor.edge );
 
                // Adds the new Neighbor object with the newly formed edge
                // newNeighbor = new $TEqn.Neighbor(newAdjacent, newEdge);
                // (newVertex.neighbors).add(newNeighbor);
             
                newGraph.addEdge( newEdge );

            } // all adjacentNeighbors
        } // all the vertices
        
        return newGraph;
        
    }", :ownerClass => feature_Graph)
parameter_feature_Graph_ComputeTranspose1Graph2_the_graph = JavaM::Parameter.new(:name => 'feature.Graph.ComputeTranspose(Graph).the_graph', :identifier => 'feature.Graph.ComputeTranspose(Graph).the_graph', :type => feature_Graph, :nesting => 0, :ownerMethod => method_feature_Graph_ComputeTranspose1Graph2)

