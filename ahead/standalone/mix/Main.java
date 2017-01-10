package mix;

import java.net.*;
import java.io.*;
import java.util.*;
import Jakarta.util.*;


public class Main {
   static String toolname = "mix";

    // methods taken from Main in AHEAD tools
	 // would be nice to define these in one place
	 
    public static String file2uri( String fileName ) {

        URI fileURI = new File( fileName ) . toURI() . normalize() ;

        String base = baseURI.getPath() ;
        String path = fileURI.getPath() ;
        int minSize = Math.min( base.length(), path.length() ) ;
            
        // Find first position after a slash at which base and path differ:
        //
        int diff = 0 ;
        while ( diff < minSize && base.charAt( diff ) == path.charAt( diff ) )
            ++ diff ;
        diff = 1 + base.lastIndexOf( '/', diff ) ;

        // Start a relative URI by first prefixing as many ".." segments
        // as needed to move from base to the common parent prefix:
        //
        StringBuffer uri = new StringBuffer() ;
        for ( int n = diff ; ( n = 1 + base.indexOf( '/', n ) ) > 0 ; )
            uri.append( "../" ) ;

        // Append the remaining (relative) path that leads to the file:
        //
        uri.append( path.substring( diff ) ) ;

        return uri.toString() ;
    }

    public static void setBaseURI( String fileName ) {
        if ( fileName == null )
            fileName = "." ;
        baseURI = new File( fileName ) . toURI() . normalize() ;
    }

    public static String uri2file( String uriName ) {
        File file = new File( baseURI.resolve( uriName ) ) ;
        return file.toString() ;
    }

    private static URI baseURI ;

    private static void usage( String err ) {
        System.err.println( "Error: " + err );
        System.err.print( "Usage: java " + toolname + ".Main [options]" );
        System.err.println( " baseFile extensionFile1 extensionFile2 ..." );
        System.err.println( "         -a <layerName> name of layer to generate " );
        System.err.println( "         -f <fileName> name file to generate" );
        System.err.println( "         -output same as -f" );
        System.exit( 1 );
    }
      
    public static void main( String args[] ) {
        int                 i;
        int                 argc = args.length;
        int                 non_switch_args;
        String              aspectName = null;
        String              fileName = null;
   
        // Step 1: a general routine to pick off command line options
        //         options are removed from command line and
        //         args array is adjusted accordingly.
        //         right now, there are no command-line options
        //         but this code is here for future expansion
      
        non_switch_args = 0;

        for ( i=0; i < argc; i++ ) {

            if ( args[i].charAt( 0 ) != '-' ) {
                args[non_switch_args] = args[i];
                non_switch_args++;
                continue ;
            }

            String arg = args [i] ;

            // Switches of form -xxx where xxx is a word:
            //
            if ( arg.equals( "-output" ) && i + 1 < args.length ) {
                fileName = args [++i] ;
                continue ;
            }

            // Switches of form -xxx where each x is a switch character:
            //
            for ( int j=1; j < arg.length(); j++ ) {

                char cur = arg.charAt( j );
                /*
                // Simple toggle switches:
                //
                if ( cur == 'k' ) {
                    keySort = true;
                    continue;
                }
                if ( cur == 't' ) {
                    typeSort = true;
                    continue;
                }
					 */

                // Switches with an argument:
                //
                if ( cur != 'a' && cur != 'f' )
                    usage( "unrecognized option: " + cur );

                if ( i + 1 >= args.length )
                    usage( "option requires an argument: " + cur ) ;

                if ( cur == 'a' ) {
                    aspectName = args[++i];
                    continue;
                }
                if ( cur == 'f' ) {
                    fileName = args[++i];
                    continue;
                }

                usage( "unrecognized option: " + cur );
            }
        }
     
        // Step 2: there must be at least one real input argument, 
        //         otherwise error

        if ( non_switch_args == 0 )
            usage( "must specify at least one input file" );

        // Step 3: set the static variables for composition
		  //

		  setBaseURI( fileName );
        String firstFile = args[0];
		  boolean dotH = firstFile.endsWith(".h");
		  int pos = firstFile.lastIndexOf('.');
		  if (pos != -1)
			  firstFile = firstFile.substring(0, pos);
		  pos = firstFile.lastIndexOf('/');
		  if (pos == -1)
			  pos = firstFile.lastIndexOf('\\');
		  if (pos != -1)
			  firstFile = firstFile.substring(pos+1);
		  StringFile.init( firstFile, aspectName, dotH );

		  // Step 4: read each file in
		  //
		  LinkedList l = new LinkedList();

		  for (int k=0; k<non_switch_args; k++) {
			  StringFile sf = new StringFile( args[k] );
			  sf.readFile();
			  sf.harvestImports();
			  l.add(sf);
		  }

		  try {
		  // Step 5: determine output

			  PrintStream ps = System.out;
			  if (fileName != null) 
					ps = new PrintStream( new FileOutputStream( fileName ) );

			  // Step 6: now output composed document
			  //
		 
			  StringFile.printImportStatements(ps);
			  ListIterator li = l.listIterator();
			  int m = 0;
			  while (li.hasNext()) {
				  StringFile s = (StringFile) li.next();
				  s.header(ps, ((++m)==non_switch_args), file2uri(s.fileName) );
				  s.printImports(ps);
				  s.footer(ps);
			  }
			  ps.close();
		  }
		  catch (Exception e) { Jakarta.util.Util.fatalError( e.getMessage() ); }

    } //end main()
}
