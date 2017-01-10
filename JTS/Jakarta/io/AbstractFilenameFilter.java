package Jakarta.io ;

import java.io.File ;

abstract public class AbstractFilenameFilter extends FilterBase {

    abstract public boolean acceptFilename (File directory, String filename) ;

    public boolean acceptFile (File file) {
	return acceptFilename (file.getParentFile(), file.getName()) ;
    }

}
