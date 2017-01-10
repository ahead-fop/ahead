/** this source come from jdk2/jfc
 *  Class Name: CustomFileFilter
 */
package ModelExplorer.Composer;
 
import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

public class CustomFileFilter extends FileFilter {

    private static String TYPE_UNKNOWN = "Type Unknown";
    private static String HIDDEN_FILE = "Hidden File";

    private Hashtable filters = null;
    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    /**
     * Creates a file filter. If no filters are added, then all
     * files are accepted.
     */
    public CustomFileFilter() {
		 this.filters = new Hashtable();
    }

    /**
     * Creates a file filter that accepts files with the given extension.
     */
    public CustomFileFilter(String extension) {
		 this(extension,null);
    }

    /**
     * Creates a file filter that accepts the given file type.
     */
    public CustomFileFilter(String extension, String description) {
		 this();
		 if(extension!=null) addExtension(extension);
 		 if(description!=null) setDescription(description);
    }

    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     *
     * Files that begin with "." are ignored.
     */
    public boolean accept(File f) {
			if(f != null) {
			    if(f.isDirectory()) {
					return true;
			    }
			    String extension = getExtension(f);
			    if(extension != null && filters.get(getExtension(f)) != null) {
					return true;
			    };
			}
			return false;
    }

    /**
     * Return the extension portion of the file's name .
     */
     public String getExtension(File f) {
			if(f != null) {
			    String filename = f.getName();
			    int i = filename.lastIndexOf('.');
			    if(i>0 && i<filename.length()-1) {
					return filename.substring(i+1).toLowerCase();
			    };
			}
			return null;
    }

    /**
     * Adds a filetype "dot" extension to filter against.
     */
    public void addExtension(String extension) {
			if(filters == null) {
			    filters = new Hashtable(5);
			}
			filters.put(extension.toLowerCase(), this);
			fullDescription = null;
    }


    /**
     * Returns the human readable description of this filter. For
     * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
     */
    public String getDescription() {
			if(fullDescription == null) {
			    if(description == null || isExtensionListInDescription()) {
 					fullDescription = description==null ? "(" : description + " (";
					// build the description from the extension list
					Enumeration extensions = filters.keys();
					if(extensions != null) {
					    fullDescription += "*." + (String) extensions.nextElement();
					    while (extensions.hasMoreElements()) {
							fullDescription += ", " + (String) extensions.nextElement();
					    }
					}
					fullDescription += ")";
				 } else {
					fullDescription = description;
			    }
			}
			return fullDescription;
    }

    /**
     * Sets the human readable description of this filter. For
     * example: filter.setDescription("Gif and JPG Images");
     */
    public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
    }

    /**
     * Determines whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     */
    public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
    }

    /**
     * Returns whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     */
    public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
    }
}
