package Jakarta.io ;

import Jakarta.collection.AbstractFilter ;

import java.io.File ;

import java.util.Arrays ;
import java.util.Collection ;
import java.util.Iterator ;

/**
 * <kbd>AbstractFileFilter</kbd> extends those methods in {@link File} that
 * list directory contents by adding methods that interface to the
 * {@link java.util.Collection Collections API}.
 **/
abstract public class AbstractFileFilter extends FilterBase {

    abstract public boolean acceptFile (File file) ;

    public boolean acceptFilename (File directory, String filename) {
	return accept (new File (directory, filename)) ;
    }

}
