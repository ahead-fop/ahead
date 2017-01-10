package applybali2jak;

import Jakarta.util.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import bali2jak.*;

public class Main {

    static boolean debug = false;
	 static String  buildDir = "";

    private static ArrayList equations;

    public static void main( String[] args ) {

        if ( args.length == 0 ) {
            System.err.println( "applybali2jak <arguments>" );
            System.err.println( "   -debug" );
            System.err.println( "   -dprefix <path>            (build directory prefix)" );
            System.err.println( "   -equation  <equation-file> (an equation file)" );
            System.err.println( "   layer ...                  (a sequence of layer paths)" );
            System.exit( 1 );
        }

        parseArguments( Arrays.asList( args ) ) ;
        apply( equations );
         
    }

    private static void parseArguments( List args ) {

        equations = new ArrayList();
        for ( ListIterator p = args.listIterator() ; p.hasNext() ; ) {
            String arg = ( String ) p.next() ;
            if ( arg.equals( "-debug" ) ) {
                debug = true;
                continue;
            }
				if ( arg.equals( "-dprefix" ) ) {
					 buildDir = ((String) p.next() ) + "/";
					 continue;
				}
            if ( arg.equals( "-equation" ) ) {
                String fileName = ( String ) p.next();
                try {
                    //System.out.println("fileName = " + fileName);
                    BufferedReader inpFile = new BufferedReader( new FileReader( fileName ) );
                    String line;
                    while( ( line = inpFile.readLine() ) != null ) {
                        int j = 0;
                        line = line.trim();
                        if ( ( line.length()!=0 ) && ( line.charAt( 0 )=='#' ) )
                            continue; // ignore comments
                        while( j < line.length() && line.charAt( j ) != ' ' )
                            j++;
                        if( j != line.length() )
                            line = line.substring( 0, j-1 );
                        if( line.trim().length() > 0 )
                            equations.add( line );
                    }
                }
                catch( IOException e ) {
                    System.err.println( e.getMessage() );
                }
            }
            else {
                equations.add( arg );
            }
        }
    }

    private static void apply( ArrayList equations ) {
        for ( Iterator p = equations.iterator() ; p.hasNext() ; ) {
            String dir = ( String ) p.next();
            //apply bali2jak to a directory
            b2j( dir );
        }
    }

    private static void b2j( String dir ) {
        
        File directory = new File( dir );
        String[] fileList = directory.list();
       
		  if (fileList == null) {
				Util.fatalError("'" + dir + "' is not a directory or " +
					 "it does not exist");
		  }

        for( int i = 0; i < fileList.length; i++ ) {
            if ( fileList[i].endsWith( ".b" ) ) {
                try {
                    fileList[i] = dir + "/" + fileList[i];
                    bali2jak.Main instance = new bali2jak.Main() ;
                    String[] args = new String[3];
                    args[0] = fileList[i];
                    args[1] = "-directory";
                    args[2] = buildDir + dir;
                    if (debug)
							 System.out.println( "bali2jak " + args[0] + " " + args[1] + " " + args[2] );
						  // now, if a build directory was specified on the
						  // applybali2jak command-line, then delete this directory
						  // otherwise build the .jak files in the same directory
						  // as the .b file
						  
						  if (!buildDir.equals("")) {
							  if (debug)
									System.out.println( "deleting " + args[2] );
							  File f = new File(args[2]);
							  Util2.deleteFile(f);
						  }
                    instance.driver( args ) ;
                }
                catch ( Throwable thrown ) {
                    thrown.printStackTrace() ;
                    System.exit( 1 ) ;
                }
            }
            else {
                if ( fileList[i].endsWith( "CVS" ) )
                    continue;
                File f = new File( dir + "/" + fileList[i] );
                if ( debug )
                    System.out.println( ">>> " + dir + "/" + fileList[i] );
                if( f.exists() && f.isDirectory() )
                    b2j( fileList[i] );
            }
        }
    }

    private static boolean contains( String[] fileList, String extension ) {
        for ( int i = 0; i < fileList.length; i++ ) {
            if( fileList[i].endsWith( extension ) )
                return true;
        }
        return false;
    }
}
