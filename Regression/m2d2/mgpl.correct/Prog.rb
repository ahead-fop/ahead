
#! JavaM => metamodels/JavaM.ecore 


layer = JavaM::Layer.new(:name => 'Prog', :identifier => 'layer') 
package_used = JavaM::Package.new(:name => 'used', :identifier => 'used', :ownerLayer => layer)
package_feature = JavaM::Package.new(:name => 'Prog', :identifier => 'feature', , :ownerLayer => layer)

# Prog\Main.jak

package_java_io = JavaM::Package.new(:name => 'java.io', :identifier => 'java.io', :isImported => true, :ownerLayer => layer)
feature_Main = JavaM::Class.new(:name => 'Main', :identifier => 'feature.Main', :visibility => 'public', :isRefinement => false, :owner => package_feature)
java_lang_String = JavaM::Class.new(:name => 'String', :identifier => 'java.lang.String', :owner => package_used)
feature_Main.importsPackage << package_java_io

#  implicitly imported files

primitiveType_void = JavaM::PrimitiveType.new(:name =>'void', :identifier =>'void', :owner => package_used)

# Prog\Main.jak

method_feature_Main_main1String342 = JavaM::Method.new(:name => 'feature.Main.main(String[])', :identifier => 'feature.Main.main(String[])', :type => primitiveType_void, :nesting => 0, :visibility => 'public', :body => " {
        
        // Step 1: create graph object

        Graph g = new  Graph();
        Graph gaux = new  Graph();
        
        // Step 2: sets up the benchmark file to read
        try {
            g.runBenchmark( args[0] );
        }
        catch( IOException e ) {}
         
        // Step 3: reads number of vertices, number of edges
        // and weights
        int num_vertices = 0;
        int num_edges = 0;
        int dummy = 0;
        try {
            num_vertices = g.readNumber();
            num_edges = g.readNumber();
            dummy = g.readNumber();
            dummy = g.readNumber();
            dummy = g.readNumber();
        }
        catch( IOException e ) {}
                 
        // Step 4: reserves space for vertices, edges and weights
        Vertex V[] = new  Vertex[num_vertices];
        // $TEqn.Edge E[] = new $TEqn.Edge[num_edges];
        int weights[] = new int[num_edges];
        int startVertices[] = new int[num_edges];
        int endVertices[] = new int[num_edges];
         
        // Step 5: creates the vertices objects 
        int i=0;
        for ( i=0; i<num_vertices; i++ )
    {
            V[i] = new Vertex().assignName( \"v\"+i );
            g.addVertex( V[i] );
        }
                  
        // Step 6:        reads the edges
        try {
            for( i=0; i<num_edges; i++ )
             {
                startVertices[i] = g.readNumber();
                endVertices[i] = g.readNumber();
            }
        }
        catch( IOException e ) {}
         
        // Step 7: reads the weights
        try {
            for( i=0; i<num_edges; i++ )
              {
                weights[i] = g.readNumber();
            }
        }
        catch ( IOException e ) {}

        // Stops the benchmark reading
        try {
            g.stopBenchmark();
        }
        catch( IOException e ) {}
         
        // Step 8: Adds the edges
        for ( i=0; i<num_edges; i++ )
     {
            g.addAnEdge( V[startVertices[i]], V[endVertices[i]],weights[i] );
        }
     
        // Executes the selected features
        g.startProfile();
        g.run( g.findsVertex( args[1] ) );
           
        g.stopProfile();
        g.display();
        g.resumeProfile();
            
        // End profiling
            
        g.endProfile();

    }", :ownerClass => feature_Main)
parameter_feature_Main_main1String342_args = JavaM::Parameter.new(:name => 'feature.Main.main(String[]).args', :identifier => 'feature.Main.main(String[]).args', :type => java_lang_String, :nesting => 0, :ownerMethod => method_feature_Main_main1String342)

