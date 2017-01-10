import jampack.*;

class demo2 { 
   static String directory = "GEN";

   static String fn( String realm, String filename ) {
      return directory + "/" + realm + "/" + filename + ".java";
   }

   static JTSParseTree base;

   static void next( String realm, String filename ) throws Exception {
      JTSParseTree t = new JTSParseTree( fn( realm, filename ) );
      base.compose(t);
   }

   public static void main( String args[] ) {
      try {
      JTSParseTree.setFlags( true, true );

      base = new JTSParseTree( fn( "Undirected", "Graph" ) );
      next( "BFS", "Graph" );
      next( "Number", "Graph");
      next( "Benchmark", "Graph");
      base.setAspectName("GPL");
      base.print2file( "GPL/Graph.java");

      base = new JTSParseTree( fn( "Undirected", "Vertex" ) );
      next( "BFS", "Vertex" );
      next( "Number", "Vertex" );
      base.setAspectName("GPL");
      base.print2file( "GPL/Vertex.java" );

      base = new JTSParseTree( fn( "Undirected", "Edge" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/Edge.java" );

      base = new JTSParseTree( fn( "Undirected", "Neighbor" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/Neighbor.java" );

      base = new JTSParseTree( fn( "BFS", "WorkSpace" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/WorkSpace.java" );

      base = new JTSParseTree( fn( "Number", "NumberWorkSpace" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/NumberWorkSpace.java" );

      base = new JTSParseTree( fn( "Prog", "Main" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/Main.java" );
      }
      catch (Exception e) {
         System.out.println( e.getMessage() );
         System.exit(1);
      }
   }
}
