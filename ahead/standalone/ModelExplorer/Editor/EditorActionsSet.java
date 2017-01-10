/* ******************************************************************
   class      : EditorActionsSet
   description: A collection of frequently used editor
                file I/O actions.

*********************************************************************/

package ModelExplorer.Editor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.TextAction;

import ModelExplorer.Editor.Utils.ResourceUtil;
import ModelExplorer.Util.Dialogs.ErrorDlg;
import ModelExplorer.Util.*;
import ModelExplorer.Main;

public class EditorActionsSet {

  // instance variable declarations
  private AbstractEditor editor;
  private File currentFile;
  /** Actions defined by the EditorActionsSet class. */
  private OpenAction openAction;
  private SaveAction saveAction;
  private CloseAction  closeAction;
  private Action[] actions;

  // constructors
  public EditorActionsSet(AbstractEditor editor) {
    this.editor   = editor;
	 openAction    = new OpenAction();
    saveAction    = new SaveAction();
	 closeAction = new CloseAction();
    actions = new Action[6];
    actions[0]=new NewAction(false);
    actions[1]=new NewAction(true);
    actions[2]=openAction;
    actions[3]=saveAction;
    actions[4]=new SaveAsAction();
	 actions[5]=closeAction;

  } // end constructor EditorActionsSet

  // methods
  /** Returns a array of file I/O editor actions. */
  public Action[] getActions() {
    return actions;
  } // end getActions

  /**
   * Should be called on closing a window containing an editor.
   *
   * @return True if window can be closed, false otherwise.
   */
  public boolean canClose() {
    return openAction.conditionalSave();
  } // end canClose

  /** Load a file. */
  public void loadFile(File file) {
    if (file == null) return;
    if (!openAction.conditionalSave()) return;
    try {
      if (!editor.load(file)) return;
      currentFile = file;
		((Main)editor.getFrame()).editorTitle.setText(file.getAbsolutePath());
    } catch (IOException excep) {
        reportError(excep,"Exception on loading file \"" + file.getName() + "\"");
    }
  } // end loadFile

  /** Save the current editor file. */
  public boolean saveFile() {
    return saveAction.save();
  } // end saveFile

  /** Returns the current editor file. */
  public File getCurrentFile() {
    return currentFile;
  } // end getCurrentFile

   /** Sets the current editor file. */
  public void setCurrentFile(File file) {
     currentFile=file;
  } // end getCurrentFile

  /** Returns the frame title without file name. */
  protected String getTitle() {
   String title = editor.getFrame().getTitle();
   int index;

   if (title != null) {
     index = title.lastIndexOf((int)'-');
     if (index >= 0) title = title.substring(index + 1);
     title = title.trim();
   } // end if
   return title;
  } // end getTitle

  /** Close the current editor.
   */
  public void close(){
	  if (!openAction.conditionalSave()) return;
      if (!editor.create()) return;
      currentFile = null;
		if (((Main)editor.getFrame()).isReadOnly)
			((Main)editor.getFrame()).editorTitle.setText("Unit Viewer");
		else
			((Main)editor.getFrame()).editorTitle.setText("Unit Editor");
  }

  protected void reportError(Exception e, String desc) {
    StringWriter sw = new StringWriter();

    e.printStackTrace(new PrintWriter(sw,true));
    new ErrorDlg(editor.getFrame(),getTitle() + " - Error",
                 true,desc + "\n" + sw);
  } // end reportError

  // inner classes
  /**
   * Create a new project.
   */
  class NewAction extends OpenAction {

    // instance variable declarations
    private boolean alternate;

    // constructors
    NewAction(boolean alternate) {
      super(alternate ? Const.ALTNEW_ACTION : Const.NEW_ACTION);
      this.alternate = alternate;
    } // end constructor NewAction

    // methods
    public void actionPerformed(ActionEvent e) {
      if (!conditionalSave()) return;
      if (!editor.create()) return;
      currentFile = null;
		((Main)editor.getFrame()).editorTitle.setText("");
    } // end actionPerformed

  } // end NewAction

  /**
   * Close an document
   */
  class CloseAction extends OpenAction {

    // instance variable declarations
    private boolean alternate;

    // constructors
    CloseAction() {
      super(Const.CLOSE_ACTION);
    } // end constructor NewAction

    // methods
    public void actionPerformed(ActionEvent e) {
      if (!conditionalSave()) return;
      if (!editor.create()) return;
      currentFile = null;
		if (((Main)editor.getFrame()).isReadOnly)
			((Main)editor.getFrame()).editorTitle.setText("Unit Viewer");
		else
			((Main)editor.getFrame()).editorTitle.setText("Unit Editor");
    } // end actionPerformed

  } // end CloseAction

  /**
   * Load a project file.
   */
  class OpenAction extends SaveAction {

    // constructors
    OpenAction() {
      super(Const.OPEN_ACTION);
    } // end constructor OpenAction

    OpenAction(String nm) {
      super(nm);
    } // end constructor OpenAction

    // methods
    public void actionPerformed(ActionEvent e) {
		 File file = getLoadFile();
		 if (file == null) return;
		 DisplayFile.file=file;
		 if (((Main)editor.getFrame()).isReadOnly){
		    //displayFile(fileInfo);
			DisplayFile.edit(((Main)editor.getFrame()));
			DisplayFile.display(((Main)editor.getFrame()));
		}
		else{
		    //displayFile(fileInfo);
			DisplayFile.display(((Main)editor.getFrame()));
			DisplayFile.edit(((Main)editor.getFrame()));
		}
      /*File file    = getLoadFile();

      if (file == null) return;
      if (!conditionalSave()) return;

      try {
        if (!editor.load(file)) return;
        currentFile = file;
		  ((Main)editor.getFrame()).editorTitle.setText(file.getName());
      } catch (IOException excep) {
        reportError(excep,"Exception on loading file \"" + file.getName() + "\"");
      }*/
    } // end actionPerformed

    /** Saves a file, if it had been changed and the user wants to. */
    protected boolean conditionalSave() {
      if (editor.isChanged()) {
        switch(saveOption()) {
          case JOptionPane.YES_OPTION: return save();
          case JOptionPane.NO_OPTION:  return true;
          default:                     return false; /* CANCEL */
        } // end switch
      } // end if
      return true;
    } // end conditionalSave

    /** Return a file name for load operations or null, if canceled. */
    protected File getLoadFile() {
		JFileChooser  chooser;
		if (currentFile==null)
			chooser = new JFileChooser(Main.modelDir);
		else
			chooser = new JFileChooser(currentFile.getParent());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int openVal = chooser.showOpenDialog(editor.getFrame());
		if (openVal == JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile();
		else
			return null;
    } // end getLoadFile

    /** Asks the user if he wants to save a file before it is discarted */
    protected int saveOption() {
      ResourceBundle res = editor.getResources();
      String text        = ResourceUtil.getResStringOrKey(res,Const.SAVECONFIRM_TEXT);
      String untitled    = ResourceUtil.getResStringOrKey(res,Const.UNTITLED_NAME);
      File file          = currentFile;

      if (file == null) text = "\"" + untitled + "\" " + text;
        else text = "\"" + file.getPath() + "\" " + text;
      return JOptionPane.showConfirmDialog(editor.getFrame(),text,
               getTitle(),JOptionPane.YES_NO_CANCEL_OPTION,
               JOptionPane.WARNING_MESSAGE);
    } // end saveOption

  } // end OpenAction

  /**
   * Save a project file.  If this is the first time, create a file dialog.
   */
  class SaveAction extends SaveAsAction {

    // constructors
    SaveAction() {
      super(Const.SAVE_ACTION);
    } // end constructor SaveAction

    SaveAction(String nm) {
      super(nm);
    } // end constructor SaveAction

    // instance methods
    /** Return a file name for save operations or null, if canceled. */
    protected File getSaveFile() {
      File file = currentFile;

      if (file == null) return super.getSaveFile();
      return file;
    } // end getSaveFile

  } // end SaveAction

  /**
   * Save a project file under a different name.
   */
  class SaveAsAction extends AbstractAction {

    // constructors
    SaveAsAction() {
      this(Const.SAVEAS_ACTION);
    } // end constructor SaveAsAction

    SaveAsAction(String nm) {
      super(nm);
    } // end constructor SaveAsAction

    // instance methods
    public void actionPerformed(ActionEvent e) {
      save();
    } // end actionPerformed

    /** Save the document.  Returns false, if the operation has been canceled. */
    protected boolean save() {
      File f         = getSaveFile();
      boolean result = (f != null);

      if (result) try {
        result = editor.save(f);
        if (result) {
          currentFile = f;
          ((Main)editor.getFrame()).editorTitle.setText(currentFile.getPath());
          ((Main)editor.getFrame()).status.setText(currentFile.getName()+" is saved");
        } // end if
      } catch (IOException excep) {
        reportError(excep,"Exception on saving file \"" + f.getName() + "\"");
        result = false;
      }
      return result;
    } // end save

    /** Return a file name for save operations or null, if canceled. */
    protected File getSaveFile() {
		 JFileChooser saveChooser;
		 File file=editor.getDefaultFile();
		 if (file!=null && file.isAbsolute())
			 saveChooser = new JFileChooser(file.getParent());
		 else
			 saveChooser = new JFileChooser(Main.modelDir);
		int returnVal = saveChooser.showSaveDialog(editor.getFrame());
		if(returnVal == JFileChooser.APPROVE_OPTION)
			return saveChooser.getSelectedFile();
		else
			return null;
    } // end getSaveFile

  } // end SaveAsAction

} // end EditorActionsSet

/* ******************************************************************
   end of file
*********************************************************************/
