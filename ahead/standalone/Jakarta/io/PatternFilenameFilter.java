package Jakarta.io ;

import Jakarta.string.Pattern ;

import java.io.File ;

public class PatternFilenameFilter extends AbstractFilenameFilter {

    public PatternFilenameFilter (String pattern) {
	this.pattern = new Pattern (pattern) ;
    }

    public boolean acceptFilename (File directory, String filename) {
	return pattern.matches (filename) ;
    }

    protected Pattern pattern ;

}
