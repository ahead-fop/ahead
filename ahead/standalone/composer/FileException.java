package composer ;

import java.io.File ;
import java.io.IOException ;

public final class FileException extends IOException {

    public FileException (File file, String message) {
	super (type (file) + " \"" + file + "\" " + message) ;
	this.file = file ;
    }

    public final File getFile () {
	return file ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private material below this point.                                    */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private static final String type (File file) {
	StringBuffer buffer = new StringBuffer () ;

	// if (! file.exists ())
	//     buffer.append ("non-existent ") ;

	if (file.isHidden ())
	    buffer.append ("hidden ") ;

	buffer.append (file.isDirectory () ? "directory" : "file") ;

	return buffer.toString () ;
    }

    private final File file ;

}
