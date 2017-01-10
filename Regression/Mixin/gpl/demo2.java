// -*- Java -*-

import mixin.*;

import java.io.File ;

class demo2 { 
   static File directory = new File ("GEN") ;

   static String fn( String realm, String filename ) {
       File file = new File (directory, realm) ;
       file = new File (file, filename + ".java") ;
       return file.toString () ;
   }

   static JTSParseTree base;

   static void next( String realm, String filename ) throws Exception {
      JTSParseTree t = new JTSParseTree( fn( realm, filename ) );
      base.compose(t);
   }

   public static void main( String args[] ) {
      try {
      Main.setBaseURI ("GPL") ;
      JTSParseTree.setFlags( true, true );

      base = new JTSParseTree( fn( "Undirected", "Graph" ) );
      next( "BFS", "Graph" );
      next( "Number", "Graph");
      next( "Benchmark", "Graph");
      base.setAspectName("GPL");
      base.print2file( "GPL/Graph.jak");

      base = new JTSParseTree( fn( "Undirected", "Vertex" ) );
      next( "BFS", "Vertex" );
      next( "Number", "Vertex" );
      base.setAspectName("GPL");
      base.print2file( "GPL/Vertex.jak" );

      base = new JTSParseTree( fn( "Undirected", "Edge" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/Edge.jak" );

      base = new JTSParseTree( fn( "Undirected", "Neighbor" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/Neighbor.jak" );

      base = new JTSParseTree( fn( "BFS", "WorkSpace" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/WorkSpace.jak" );

      base = new JTSParseTree( fn( "Number", "NumberWorkSpace" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/NumberWorkSpace.jak" );

      base = new JTSParseTree( fn( "Prog", "Main" ) );
      base.setAspectName("GPL");
      base.print2file( "GPL/Main.jak" );
      }
      catch (Exception e) {
         System.out.println( e.getMessage() );
         System.exit(1);
      }
   }
}
