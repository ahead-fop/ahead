/* ******************************************************************
   class      : Const
   description: global available constants
*********************************************************************/
package ModelExplorer.Editor;

public abstract class Const {

  // class variable declarations

  // Resources
  /** The key to the desktop settings property file name */
  public static final String DESKTOP_NAME = "desktopFile";
  /** The key to the name to display for a untitled file */
  public static final String UNTITLED_NAME = "untitled";
  /** The key to the save confirm dialog text */
  public static final String SAVECONFIRM_TEXT = "saveConfirmDlg.Text";
 
  // Editor properties
  /** Key to the source extension resource */
  public static final String SOURCE_EXT = "sourceExtension";
  /** Key to the source code template extension resource */
  public static final String TPL_EXT = "templateExtension";
  /** Key to the source code template directory resource */
  public static final String TEMPLATE_DIR = "templateDir";
  /** Prefix to a file key */
  public static final String FILE_PREFIX = "file";
  /** Prefix to a object file key */
  public static final String OBJECT_PREFIX = "object";
  /** Key to the tab size resource */
  public static final String TAB_SIZE = "tabSize";
  /** Key to the use tabulator to store files resource */
  public static final String USE_TABS = "useTabs";
  /** Key to the file format resource */
  public static final String FILE_FORMAT = "fileFormat";
  /** Key to the file format provided to the editor on load operations. */
  public static final String LOAD_FORMAT = "editorLoadFormat";
  /** Key to the copy on load key list */
  public static final String COPY_ON_LOAD = "copyOnLoad";
  /** Key to the project file path for save operations */

  // Frequently used action identifiers */
  /** Identifier of the new action */
  public static final String NEW_ACTION = "new";
  /** Identifier of the alternative new action */
  public static final String ALTNEW_ACTION = "alt-new";
  /** Identifier of the new window action */
  public static final String NEWWINDOW_ACTION = "new-window";
  /** Identifier of the open action */
  public static final String OPEN_ACTION = "open";
  /** Identifier of the save action */
  public static final String SAVE_ACTION = "save";
  /** Identifier of the save as action */
  public static final String SAVEAS_ACTION = "save-as";
  /** Identifier of the close action */
  public static final String CLOSE_ACTION = "close";
  /** Identifier of the exit action */
  public static final String EXIT_ACTION = "exit";
  /** Identifier of the auto save action */
  public static final String AUTOSAVE_ACTION = "auto-save";

} // end Const

/* ******************************************************************
   end of file
*********************************************************************/
