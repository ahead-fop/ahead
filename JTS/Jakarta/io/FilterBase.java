package Jakarta.io ;

import Jakarta.collection.AbstractFilter ;

import java.io.File ;

import java.util.Arrays ;
import java.util.Collection ;
import java.util.Iterator ;

/**
 * <kbd>FilterBase</kbd> extends those methods in {@link File} that list
 * directory contents by implementing methods that interface to the
 * {@link java.util.Collection Collections API}.
 **/
abstract class FilterBase extends AbstractFilter implements FileFilter {

    abstract public boolean acceptFile (File file) ;

    abstract public boolean acceptFilename (File directory, String filename) ;

    public boolean acceptObject (Object object) {
	return accept ((File) object) ;
    }

    public boolean accept (File file) {
	return acceptFile (file) ;
    }

    public boolean accept (File directory, String filename) {
	return acceptFilename (directory, filename) ;
    }

    public boolean accept (Object object) {
	return acceptObject (object) ;
    }

    public Collection collection (File directory, Collection out) {
	out.addAll (collection (directory)) ;
	return out ;
    }

    public Collection collection (File directory) {
	return Arrays.asList (directory.listFiles ((java.io.FileFilter)this)) ;
    }

    public Iterator iterator (File directory) {
	return collection (directory) . iterator () ;
    }

}
