package Jakarta.io ;

import Jakarta.collection.Filter ;

import java.io.File ;
import java.io.FilenameFilter ;

import java.util.Collection ;
import java.util.Iterator ;

public interface FileFilter extends Filter, java.io.FileFilter, FilenameFilter
{
    public boolean acceptFile (File file) ;

    public boolean acceptFilename (File directory, String filename) ;

    public boolean accept (File file) ;

    public boolean accept (File directory, String filename) ;

    public Collection collection (File directory, Collection out) ;
    public Collection collection (File directory) ;

    public Iterator iterator (File directory) ;

}
