/* ******************************************************************
   interface  : AbstractEditor
   description: an interface to a generic load/save mechanism.
*********************************************************************/
package ModelExplorer.Editor;

import java.awt.*;
import java.io.*;
import java.util.*;

public interface AbstractEditor extends FilenameFilter {

  // methods
  /**
	* Returns the hosting frame.
	*/
  public Frame getFrame();
  
  /**
   * Returns a resource bundle containing keys used by the
   * file I/O actions.
   */
  public ResourceBundle getResources();

  /**
   * Returns properties containing desktop settings.
   */
  public Properties getDesktopProperties();

  /**
   * Save the document to the spezified file.
   */
  public boolean save(File file) throws IOException;

  /**
   * Load the document from the spezified file.
   */
  public boolean load(File file) throws IOException;

  /**
   * Create a new empty document.
   *
   * @param alternate True if invoked from an alternate new command.
   */
  public boolean create();

  /**
   * Returns true, if the document has been changed.
   */
  public boolean isChanged();

  /**
   * Return a default file to display in the save file dialog or null if
   * no default file is used.
   */
  public File getDefaultFile();

} // end AbstractEditor

/* ******************************************************************
   end of file
*********************************************************************/
