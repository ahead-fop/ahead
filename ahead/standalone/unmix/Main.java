package unmix;

import java.net.*;
import java.io.*;
import java.util.*;
import Jakarta.util.*;

public class Main {
   static String toolname = "unmix";

	static boolean verbose = false;

    private static void usage( String err ) {
        System.err.println( "Error: " + err );
        System.err.print( "Usage: java " + toolname + ".Main [options]" );
        System.err.println( " file1 file2 ..." );
        System.err.println( "         -verbose" );
        System.exit( 1 );
    }
      
    public static void main( String args[] ) {
        int                 i;
        int                 argc = args.length;
        int                 non_switch_args;
   
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
            if ( arg.equals( "-verbose" ) ) {
					verbose = true;
					continue;
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
					 */

                usage( "unrecognized option: " + cur );
            }
        }
     
        // Step 2: there must be at least one real input argument, 
        //         otherwise error

        if ( non_switch_args == 0 )
            usage( "must specify at least one input file" );

        // Step 3: unmix each file
		  
		  for (int k=0; k<non_switch_args; k++)
				unmix( args[k] );

    } //end main()


	 public static void unmix( String fileName ) {

		if (verbose) 
			System.err.println( "unmix " + fileName );

		// Step 1: open the file and get its iterator
		//
		StringFile edit = new StringFile(fileName).readFile();
		ListIterator editit = edit.listIterator(0);

		// Step 2: foreach SoUrCe line, determine if propagation
		//         is necessary
		while (editit.hasNext()) {
			 String line = (String) editit.next();
			 if (line.startsWith("//SoUrCe "))
					propagate( edit, line.substring(8).trim(), editit.nextIndex() );
		}
	}

	public static void propagate( StringFile editedFile, String URL, int index ) {
		String editedLine, origLine;

		if (verbose)
			System.err.println( "  comparing " + URL + " inside " + editedFile.fileName +
									  " starting from line " + index );

		// Step 1: get list iterators for both the original file and
		//         the edited file
		//
      StringFile origFile = new StringFile(URL).readFile();
		ListIterator orig = origFile.listIterator(0);
		ListIterator edit = editedFile.listIterator(index);

		// Step 2: as long as there is a line to match on both sides
		//         (and we know that the has been agreement so far)...
		//
		while (orig.hasNext() && edit.hasNext()) {

			// Step 2.1: get corresponding lines in both files
			//           and convert the edited line if it dealt with
			//           import
			//
			editedLine = (String) edit.next();
			origLine   = (String) orig.next();
			if (editedLine.startsWith("//import_ "))
				editedLine = "import_ " + editedLine.substring(10);

			// Step 2.2: compare both lines.  If they are equal,
			//           proceed onto next line (if it exists)
			if (editedLine.equals(origLine))
				 continue;

			// Step 2.3: if we get this far, this means that
			//           the files are not equal.  So we need to
			//           back-propagate the changes of the edited
			//           file
			backpropagate( editedFile, URL, index );
			return;
		}
      
		// Step 3: we fell-out of the loop, which means that
		//         one of the files has been exhausted. we now go
		//         through a case analysis of what to do
		//

		// Step 3a: if the original file is exhausted, then the
		//         next line of the edited file should be an end-marker.
		//         if not, the edited file has been lengthened, and
		//         we need to backpropagate

		if (!orig.hasNext()) {
         editedLine = (String) edit.next();
			if (!editedLine.equals("//EnDSoUrCe ")) 
				backpropagate( editedFile, URL, index );
			return;
		}

		// Step 3b: original file hasn't been exhausted, but the
		//          edited file has.  Something is definitely wrong
		//          as the edited file would surely have an end-marker
		//          and this marker would have been detected as being
		//          different from what is in the source.  At this point,
		//          we throw our hands up not knowing what to do.

		bailout( editedFile, URL );
   }

	static void bailout( StringFile editedFile, String URL ) {
		Jakarta.util.Util.fatalError( "no EnDSoUrCe marker found for file " + URL
												+ " in mix-produced file " + editedFile.fileName );
	}

   public static void backpropagate( StringFile editedFile, String URL, int index ) {
		ListIterator li;
		boolean      done = false;
		String       editedLine;

		if (verbose) 
			System.err.println( "  propagating changes to " + URL );

		// Step 1: see if there is an end-marker in the edited file before we back propagate
		//
		li = editedFile.listIterator(index);

		while (li.hasNext() && !done) {
			editedLine = (String) li.next();
			done = editedLine.equals("//EnDSoUrCe ");
		}
		if (!done)
			bailout( editedFile, URL );

		// Step 2: now backpropagate changes
		try {
			PrintStream ps = new PrintStream( new FileOutputStream( URL ) );
         li = editedFile.listIterator(index);
			while (li.hasNext()) {
				editedLine = (String) li.next();
				if (editedLine.startsWith("//import_ "))
					editedLine = "import_ " + editedLine.substring(10);
				else
				if (editedLine.equals("//EnDSoUrCe "))
					break;
				ps.println( editedLine );
			}
			ps.close();
		}
		catch (Exception e) { Jakarta.util.Util.fatalError( e.getMessage() ); }
   }	
}
