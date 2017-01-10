package Jakarta.io ;

import java.io.File ;

public class DirectoryFileFilter extends AbstractFileFilter {

    public boolean acceptFile (File file) {
	return file . isDirectory () ;
    }

}
