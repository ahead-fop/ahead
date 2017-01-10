package Jakarta.util;

import java.io.*;

// This file is created so that the Util.java file
// need not be constantly extended.  The sole reason
// for doing this is that one of the symbol table 
// regression tests uses Util.java, and every time 
// Util.java is updated with a new method or data
// member, the regression test needs to be updated.
// By placing all changes to Util into Util2.java,
// we should avoid this problem.  Eventually, when
// we figure out how to fix the regression test,
// the need for Util2.java can disappear.

public class Util2 {

    //--------------- common routines for mangling identifiers ------

    // let i be an unmangled identifier, and l be the name of a layer
    // mangleId(i,l) = i$$l
    // unmangle removes the $$ trailer string
    // Note: aspect (or layer) names can be qualified -- e.g. "A.B.C".
    // the mangled version is "A$B$C"

    public static String mangleId( String identifier, String layerName ) {
        return identifier + "$$" + ( layerName.replace( '.','$' ) );
    }

    public static String unmangleId( String identifier ) {
        int i = identifier.indexOf( '$' );
        if ( i == -1 )
            return identifier; // input identifier not mangled
        else
            return identifier.substring( 0,i );
    }

    public static boolean isMangled( String arg ) {
        return ( arg.indexOf( "$$" ) >= 0 );
    }

    //utility function to delete a file or directory
    // taken from ModelExplorer/Browser/TreeBrowser
    //
    public static boolean deleteFile( File file ) {
        boolean result=true;
        if ( file.isDirectory() ) {
            String[] filelist = file.list();
            File tmpFile = null;
            for ( int i = 0; i < filelist.length; i++ ) {
                tmpFile = new File( file.getAbsolutePath(),filelist[i] );
                if ( tmpFile.isDirectory() ) {
                    if ( !deleteFile( tmpFile ) )
                        result=false;
                }
                else
                    if ( tmpFile.isFile() ) {
                        if ( !tmpFile.delete() )
                            result=false;
                    }
            }
        }
        if ( !file.delete() )
            result=false;
        return result;
    }
}
