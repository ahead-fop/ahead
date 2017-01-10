package Jakarta.io ;

import java.io.File ;

public class ExtensionFilenameFilter extends AbstractFilenameFilter {

    public ExtensionFilenameFilter (String extension) {
	if (extension == null)
	    throw new NullPointerException ("ExtensionFileFilter(String)") ;
	this.extension = extension ;
    }

    public boolean acceptFilename (File directory, String filename) {
	return filename . endsWith (extension) ;
    }

    protected String extension ;

}
