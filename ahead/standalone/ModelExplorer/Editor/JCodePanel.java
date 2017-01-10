/* ******************************************************************
   class		  : JCodePanel
   description: The editor Gui
******************************************************************* */

package ModelExplorer.Editor;

import ModelExplorer.Editor.Utils.*;
import ModelExplorer.SwingUtils.*;
import ModelExplorer.Editor.Streams.*;
import ModelExplorer.Util.*;
import ModelExplorer.Util.Dialogs.*;
import ModelExplorer.Main;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import javax.swing.undo.*;
import java.net.URL;
import java.beans.*;

public class JCodePanel extends SwingApp implements UndoableEditListener,
	CaretListener, AbstractEditor{

  // class variable declarations
  public static String TAB_SIZE="4";
  protected static ResourceBundle resources;
  private static Properties desktop;
  private static Properties prop;
  protected Hashtable coloringMap;

   /** UndoManager that we add edits to. */
  protected UndoManager undo;

  /** The history list maximum length. */
  private static final int HISTLIST_LENGTH = 10;

   /** Identifier of the print action */
  public static final String PRINT_ACTION = "print";
  /** Identifier of the undo action for toolbar buttons. */
  public static final String UNDO_BTN_ACTION = "undo-btn";
  /** Identifier of the find action */
  public static final String FIND_ACTION = "find";
  /** Identifier of the find again action */
  public static final String FINDAGAIN_ACTION = "findagain";
  /** Identifier of the replace action */
  public static final String REPLACE_ACTION = "replace";
  /** Identifier of the font action */
  public static final String FONT_ACTION = "font";
  /** Identifier of the go to line action */
  public static final String GOTOLINE_ACTION = "go-to-line";
  /** Identifier of the file format action */
  public static final String FORMAT_ACTION = "format";
  /** Identifier of the file history action */
  public static final String FILEHIST_ACTION = "file-history";

  private static final String COLORING_PREFIX = "coloringMap";
  /** Key which spezifies if the AltGr bug should be fixed. */
  private static final String ALTGR_BUGFIX_ON = "fixAltGrBug";
  /** Key which spezifies if the mnemonic bug should be fixed. */
  private static final String MNEMONIC_BUGFIX_ON = "fixMnemonicBug";
  /** Key to the font settings resource. */
  private static final String FONT_SETTINGS = "fontSettings";

  private static final String GOTOLINEDLG_TITLE = "GoToLineDlg.title";
  private static final String GOTOLINEDLG_TEXT  = "GoToLineDlg.text";
  private static final String SEARCHDLG_PREFIX  = "searchDialog.";

  /** Key to the search not found message label. */
  private static final String NOTFOUNDMSG_LABEL = "searchNotFoundDlg.label";
  /** Key to the replace confirm dialog label. */
  private static final String REPCONFDLG_BUTTONS = "replaceConfDlg.buttons";
  /** Key to the replace confirm dialog label. */
  private static final String REPCONFDLG_LABEL = "replaceConfDlg.label";
  /** Key to the default document name. */
  public final static String DEFNAME_PROPERTY = "propertiesDefName";

   // Atomic Component Declarations
    JCodePane	editor;
	 private Main frame;
	 private WindowStateTracker wndState;
	 private Hashtable commands;
	 private String[] sourceExt;
	 private Action[] externalActions;
	 private EditorActionsSet editorActions;
	 /** The file history list data. */
	 protected Vector histListData;
	 /** The search options. */
	 protected class SearchOpt {
		  public String text      = null;
		  public String replace   = null;
		  public boolean caseSens = false;
		  public boolean wordOnly = false;
		  public boolean quick    = false;
		  public boolean focus    = false;
	 }
	protected SearchOpt searchOpt;
	// action implementations
	private UndoAction    undoAction;
	private UndoBtnAction undoBtnAction;
	private RedoAction    redoAction;
	/** Actions defined by the JCodePanel class. */
	private Action[] defaultActions;
	public JButton status;

   // initialize the atomic component
   public void initAtoms() {

	  super.initAtoms();

	  status = new JButton("");
	  status.setToolTipText("GoToLine...");
	  status.setBorderPainted(false);
	  status.setMargin(new Insets(-2,2,-2,2));
	  status.setAlignmentX(JButton.RIGHT_ALIGNMENT);

	  undo = new UndoManager();
	  searchOpt = new SearchOpt();
	  prop=new Properties();
	  desktop = new Properties();
	  wndState      = new WindowStateTracker();

	  try {
		  resources = ResourceBundle.getBundle("ModelExplorer/Editor/_Main",Locale.getDefault());
		} catch (MissingResourceException mre) {
		  System.err.println("Main: _Main.properties not found");
		  System.exit(1);
		  resources = null; // to avoid not initialized error
		}

	   // initialize the undo manager
		undo.setLimit(30);
		// create the embedded JTextComponent
		String str = desktop.getProperty(FONT_SETTINGS);
		Font font  = null;
		if (str != null)
			try {
			  String[] aStr = ResourceUtil.splitToWords(str);

			  if (aStr.length == 3) {
			    font = new Font(aStr[0],Integer.parseInt(aStr[1]),
			                    Integer.parseInt(aStr[2]));
			  } // end if
			} catch (NumberFormatException excep) {
			  excep.printStackTrace();
			}
		if (font == null) {
		  font = new Font("Courier",Font.PLAIN,12);
		} // end if

		editor = createEditor();
		editor.setFont(font);
		editor.setEditable(false);
		// Add this as a listener for undoable edits
		editor.getDocument().addUndoableEditListener(this);
		// Add this as a listener for caret movement
		editor.addCaretListener(this);
		// The editor should request the focus on activationg the window
      wndState.setFocusRequester(editor);

		 // initialize the file history list
		Object[] aObj = new Object[2];
		histListData = new Vector(HISTLIST_LENGTH);
		aObj[0] = "";
		aObj[1] = new Integer(0);
		histListData.addElement(aObj);

		// initalize the filename filter
		str=ResourceUtil.getResourceString(resources,Const.SOURCE_EXT);
		prop.setProperty(Const.SOURCE_EXT, str);
		if (str != null) sourceExt = ResourceUtil.splitToWords(str);
		  else System.err.println("JCodePanel: " + Const.SOURCE_EXT +
		                          " not in resource file");

		// Build up syntax coloring map
		if (coloringMap == null) {
		  int i = 1;

		  coloringMap = new Hashtable();
		  str = ResourceUtil.getResourceString(resources,COLORING_PREFIX + (i++));
		  while (str != null) {
		    String[] aStr = ResourceUtil.splitToWords(str);

		    if (aStr.length < 2)
		      System.err.println("JCodePanel: Illegal resource: " + str);
		    else
		      coloringMap.put(aStr[0],aStr[1]);
		    // end if
		    str = ResourceUtil.getResourceString(resources,COLORING_PREFIX + (i++));
		  } // end while
		} // end if

		editorActions = new EditorActionsSet(this);
		undoAction      = new UndoAction();
		undoBtnAction   = new UndoBtnAction();
		redoAction      = new RedoAction();
		/** Actions defined by the JCodePanel class. */
		defaultActions =new Action[12];
		defaultActions[0]=new PrintAction();
		defaultActions[1]=undoAction;
		defaultActions[2]=undoBtnAction;
		defaultActions[3]=redoAction;
		defaultActions[4]=new FindAction();
		defaultActions[5]=new FindAgainAction();
		defaultActions[6]=new ReplaceAction();
		//defaultActions[7]=new FontAction();
		defaultActions[7]=new GoToLineAction();
		//defaultActions[9]=new FormatAction();
		defaultActions[8]=new FileHistAction();
		defaultActions[9]=new CutAction();
		defaultActions[10]=new CopyAction();
		defaultActions[11]=new PasteAction();

		commands      = new Hashtable();
		Action[] actions = getActions();
		for (int i = 0; i < actions.length; i++) {
		   Action a = actions[i];
			//System.out.println(a.getValue(Action.NAME));
		   commands.put(a.getValue(Action.NAME), a);
		} // end for

	}

		//Utility function to initilize the JPanel
   JPanel initPanel(boolean horizontal) {
       JPanel p = new JPanel();
       if (horizontal)
          p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
       else
          p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
       p.setBorder( BorderFactory.createEmptyBorder(1,2,1,2));
       return p;
   }

    // Layout Component Declarations
   JScrollPane			editorScrollPane;

   public void initLayout() {

      editorScrollPane = new JScrollPane(editor);
		editorScrollPane.getViewport().setBackground(Color.white);
	   editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
  }

    // the ContentPane

    public void initContentPane() {
        //ContentPane = initPanel(false);
       // ContentPane.add(editorPanel);
		 // ContentPane.add(status);
		ContentPane = new JPanel();
      ContentPane.setLayout( new BorderLayout() );
      ContentPane.setBorder( BorderFactory.createEmptyBorder(2,//top
                                                             2,//left
                                                             2,//bottom
                                                             2 // right
                                                             ) );

      ContentPane.add(editorScrollPane, BorderLayout.CENTER);
		ContentPane.add(status, BorderLayout.SOUTH);

    }

   public void initListeners() {
		JToolBarLoader toolBarLoader=new JToolBarLoader(commands);
		toolBarLoader.loader();
		JMenuLoader menuLoader = new JMenuLoader(resources,commands);
		menuLoader.loader();
		status.addActionListener(new GoToLineAction());
   }

   public JCodePanel(Main me) {
		super();
		frame=me;
		frame.addWindowListener(wndState);
	}

   public JCodePanel(String AppTitle) { super(AppTitle); }

	/** The main to test.
	 */
   public static void main(String[] args) {
      JCodePanel cp=new JCodePanel("Test the JCodePane" );
		try{
			cp.load(new File("ModelExplorer/Editor/JCodePanel.java"));
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
   } // end main

	//instance methods
   /** Set editor to read only or not
    */
	public void setEditable(boolean b){
	   editor.setEditable(b);
   } // end setEditable

	/** Get the editor in this panel
	 */
	public JCodePane getEditor(){
		return editor;
	} // end getEditor

	public void setFont(Font font){
		editor.setFont(font);
	}
	/** Close the current document
	 */
	public void close(){
		editorActions.close();
	} // end close

   /** Create an empty document. */
   protected Document createDocument() {
		String str;

		str = ResourceUtil.getResourceString(resources,CodeEditorKit.COLORING_PROPERTY);
		return createDocument(str);
	} // end createDocument

	/** Create an empty document. */
	protected Document createDocument(String coloringClass) {
		  Document doc = new PlainDocument();
		  String str;

		  str = prop.getProperty(Const.TAB_SIZE,TAB_SIZE);
		  try {
		    doc.putProperty(CodeEditorKit.TABSIZE_PROPERTY,new Integer(str));
		  } catch (NumberFormatException excep) {
		    excep.printStackTrace();
		  }
		  if (coloringClass != null)
		    doc.putProperty(CodeEditorKit.COLORING_PROPERTY,coloringClass);
		  // end if
		  return doc;
	} // end createDocument

	/** Get the coloring class name for a file name. */
	private final String getColoringName(String name) {
		String key;
		Enumeration en = coloringMap.keys();

		while (en.hasMoreElements()) {
		    key = en.nextElement().toString();
		    if (name.endsWith(key)) return coloringMap.get(key).toString();
		} // end while
		return null;
	} // end getColoringName

	/**
	 * Create an editor to represent the given document.
	 */
	protected JCodePane createEditor() {
		JCodePane cp;
		String s     = ResourceUtil.getResourceString(resources,ALTGR_BUGFIX_ON);
		boolean b    = (s != null) && s.toLowerCase().equals("true");

		cp = new JCodePane(createDocument());
		cp.fixAltGrBug(b);
		s = ResourceUtil.getResourceString(resources,MNEMONIC_BUGFIX_ON);
		b = (s != null) && s.toLowerCase().equals("true");
		cp.fixMnemonicBug(b);
		return cp;
	} // end createEditor

	 /**
   * Fetch the list of actions supported by this
   * editor.
   */
	public Action[] getActions() {
		if (externalActions == null){
         return TextAction.augmentList(editor.getActions(),
               TextAction.augmentList(editorActions.getActions(),
                                      defaultActions));
		}
      else
			return TextAction.augmentList(editor.getActions(),
			         TextAction.augmentList(editorActions.getActions(),
			           TextAction.augmentList(defaultActions,
			                                  externalActions)));
	}

	/**
   * Return the word around the caret.
   */
	public String getWordAtCaretPos() {
	  /*if (searchOpt.focus && (tfQuickSearch.getText().length() > 0))
	    return tfQuickSearch.getText().trim();
	  else*/
	    return editor.getWordAtCaretPos();
	  // end if
	} // end getWordAtCaretPos

	/** Move the caret to the specified editor line. */
	public void goToLine(int lineNr) {
	  editor.goToLine(lineNr);
	} // end goToLine

	/** Return the current editor file. */
	public File getCurrentFile() {
	  return editorActions.getCurrentFile();
	} // end getCurrentFile

	/** Save the current editor file. */
	public boolean saveCurrentFile() {
	  // conditional save
	  if ((getCurrentFile() == null) ||
	      isChanged()) return editorActions.saveFile();
	  return true;
	} // end saveCurrentFile

	/** Returns the project directory. */
	protected String getProjectDirectory() {
	  return Main.modelDir;
	} // end getProjectDirectory

	/** Check if editor can be dismissed without loss of data. */
	public boolean canClose() {
	  return editorActions.canClose();
	} // end canClose


	protected Action getAction(String cmd) {
	   return (Action)commands.get(cmd);
	} // end getAction

	/**
   * Messaged when the Caret position has changed.
   */
	public void caretUpdate(CaretEvent e) {
	  setCaretLabel(editor.getCaretCharPos());
	} // end caretUpdate

	static String[] space={"", " ", "  ", "   ", "    ",
								  "                                                                                 "};
	protected void setCaretLabel(Point point) {
		/*String str;
		if (editor.hasChanged())

			str="*  [Ln "+(point.y+1)+"   Col "+(point.x+1)+"]";
		else
			str="  [Ln "+(point.y+1)+"   Col "+(point.x+1)+"]";
		if(editorActions.getCurrentFile()!=null)
			((Main)frame).editorTitle.setText(editorActions.getCurrentFile().getName()+str);*/
		StringBuffer sb=new StringBuffer();
		sb.append(space[5]);
		String str="Ln "+(point.y+1);
		sb.append(str);
		sb.append(space[7-str.length()]);
		sb.append(space[2]);
		str="Col "+(point.x+1);
		sb.append(str);
		sb.append(space[8-str.length()]);
		//status.setText("Ln "+(point.y+1)+"    Col "+(point.x+1));
		status.setText(sb.toString());

		if (editor.hasChanged()){
			//System.out.println(frame.editorTitle.getText().endsWith("*"));
			if (!frame.editorTitle.getText().endsWith("*") && editorActions.getCurrentFile()!=null)
			{
				((Main)frame).editorTitle.setText(editorActions.getCurrentFile().getPath()+"*");
			}
		}

	} // end setCaretLabel

	/**
	 * Messaged when the Document has created an edit, the edit is
	 * added to <code>undo</code>, an instance of UndoManager.
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
	  undo.addEdit(e.getEdit());
	  undoAction.update();
	  redoAction.update();
	} // end undoableEditHappened


	//--------- AbstractEditor ----------------------------------------
	 /** Returns the hosting frame. */
	public Frame getFrame() {
	  return frame;
	} // end getFrame

	/** Returns the resource bundle. */
	public ResourceBundle getResources() {
	  return resources;
	} // end getResources

	/** Returns properties containing desktop settings. */
	public Properties getDesktopProperties() {
	  return desktop;
	} // end getDesktopProperties

	/** Save the document to the specified file. */
   public boolean save(File file) throws IOException {
	  Writer out          = new FileWriter(file);
	  BufferedWriter bout = new BufferedWriter(out);
	  Document doc        = editor.getDocument();
	  int line            = editor.getCaretCharPos().y;
	  TabWriter tab;
	  int ts = 8;
	  String str;
	  Boolean useTabs;
	  int format;

	  try {
	    ts = Integer.parseInt(doc.getProperty(CodeEditorKit.TABSIZE_PROPERTY).toString());
	  } catch (NumberFormatException excep) {
	    excep.printStackTrace();
	  }
	  str     = prop.getProperty(Const.USE_TABS,"false");
	  useTabs = new Boolean(str);
	  str     = prop.getProperty(Const.FILE_FORMAT,"" + TabWriter.PLATFORM);
	  try {
	    format = Integer.parseInt(str);
	  } catch (NumberFormatException excep) {
	    excep.printStackTrace();
	    format = TabWriter.PLATFORM;
	  }
	  tab = new TabWriter(bout,ts,useTabs.booleanValue(),format);
	  editor.write(tab);
	  tab.close();
	  bout.close();
	  out.close();
	  setCaretLabel(editor.getCaretCharPos());
	  // update file history list
	  addHistListItem(file,line,true);
	  return true;
	} // end save

	 /** Load the document from the specified file. */
	public boolean load(File file) throws IOException {
	  Document doc       = editor.getDocument();
	  Reader in          = new FileReader(file);
	  BufferedReader bin = new BufferedReader(in);
	  int fmt            = ResourceUtil.getResourceInt(resources,Const.LOAD_FORMAT,
	                                                   TabReader.NO_CHANGE);
	  int line           = editor.getCaretCharPos().y;
	  String cClass      = getColoringName(file.getName());
	  TabReader tab;
	  int ts = 8;
	  Object[] aObj;

	  if (doc != null) {
	    doc.removeUndoableEditListener(this);
	    undo.discardAllEdits();
		 undoAction.update();
	  } // end if
	  editor.setDocument(createDocument(cClass));
	  doc = editor.getDocument();
	  try {
	    ts = Integer.parseInt(doc.getProperty(CodeEditorKit.TABSIZE_PROPERTY).toString());
	  } catch (NumberFormatException excep) {
	    excep.printStackTrace();
	  }
	  tab = new TabReader(bin,ts,fmt);
	  editor.read(tab,"");
	  tab.close();
	  bin.close();
	  in.close();
	  doc.addUndoableEditListener(JCodePanel.this);
	  editor.setCaretPosition(0);
	  // update file history list
	  //addHistListItem(file,line,false);
	  return true;
	} // end load

	public void loadFile(File file){
		editorActions.loadFile(file);
	}

	 /** Create a new empty document. */
	public boolean create(){
	  Document oldDoc = editor.getDocument();
	  int line        = editor.getCaretCharPos().y;

	  if(oldDoc != null) {
	    oldDoc.removeUndoableEditListener(JCodePanel.this);
	    undo.discardAllEdits();
		 undoAction.update();
	  } // end if
	  editor.setDocument(createDocument());
	  editor.getDocument().addUndoableEditListener(JCodePanel.this);
	  // update file history list
	  addHistListItem(null,line,false);
	  return true;
	} // end create

	/* it will be added later
	 private final boolean createFromTemplate(Reader in,
                                           AbstractMacroprocessor preproc) {
		Document doc       = editor.getDocument();
		BufferedReader bin = new BufferedReader(in);
		int fmt            = ResourceUtil.getResourceInt(resources,Const.LOAD_FORMAT,
		                       TabReader.NO_CHANGE);
		int line           = editor.getCaretCharPos().y;
		TabReader tab;
		int ts = 8;
		String str;

		if (doc != null) {
		  doc.removeUndoableEditListener(this);
		  undo.discardAllEdits();
		  undoAction.update();
		} // end if
		doc = createDocument(getColoringName(preproc.getMacro("__EXT__")));
		// check if new document is an assertion class
		str = preproc.getMacro("__ASSERT__");
		if (str != null) {
		  str = preproc.getMacro("__RELPATH__");
		  if (str == null) str = "";
		  doc.putProperty(Const.CUR_ASSERT,str);
		} // end if
		// build the default name of the new document
		str = preproc.getMacro("__NAME__");
		if ((str != null) && (preproc.getMacro("__EXT__") != null)) {
		  str += preproc.getMacro("__EXT__");
		  if (preproc.getMacro("__RELPATH__") != null)
		    str = preproc.getMacro("__RELPATH__") + File.separator + str;
		  // end if
		  doc.putProperty(DEFNAME_PROPERTY,str);
		} // end if
		editor.setDocument(doc);
		try {
		  ts = Integer.parseInt(doc.getProperty(CodeEditorKit.TABSIZE_PROPERTY).toString());
		} catch (NumberFormatException excep) {
		  excep.printStackTrace();
		}
		tab = new TabReader(bin,ts,fmt);
		try {
		  editor.read(tab,"");
		} catch (IOException excep) {
		  excep.printStackTrace();
		  editor.setDocument(createDocument());
		  doc = editor.getDocument();
		} finally {
		  try { tab.close(); } catch (IOException e) {}
		  try { bin.close(); } catch (IOException e) {}
		  try { in.close();  } catch (IOException e) {}
		}
		doc.addUndoableEditListener(JCodePanel.this);
		editor.setCaretPosition(0);
		// update file history list
		addHistListItem(null,line,false);
		return true;
	} // end createFromTemplate*/

	 /** Write an item to the file history list. */
	private void addHistListItem(File file, int oldLine, boolean replace) {
	  String path   = file == null ? ""  : file.getAbsolutePath();
	  int line      = editor.getCaretPosition();
	  Object[] aObj = (Object[])histListData.elementAt(0);
	  Object[] tmp;

	  if (aObj[0].toString().equals(path) || aObj[0].toString().equals(""))
	    replace = true;
	  // end if
	  if (replace) histListData.removeElementAt(0);
	    else aObj[1] = new Integer(oldLine);
	  aObj    = new Object[2];
	  aObj[0] = path;
	  aObj[1] = new Integer(line);
	  histListData.insertElementAt(aObj,0);
	  // Remove duplicate elems.
	  for (int i=1; i<histListData.size(); i++) {
	    tmp = (Object[])histListData.elementAt(i);
	    if (tmp[0].toString().equals(path)) {
	      histListData.removeElementAt(i);
	      break;
	    } // end if
	  } // end for
	  // Trim history list to the max. length.
	  if (histListData.size() > HISTLIST_LENGTH)
	    histListData.setSize(HISTLIST_LENGTH);
	  // end if
	} // end addHistListItem

	 /** Returns true, if the document has been changed. */
	public boolean isChanged() {
	  return editor.hasChanged();
	} // end isChanged

	/** Return a default file to display in the save file dialog. */
	public File getDefaultFile() {
	  File file = editorActions.getCurrentFile();
	  Document doc;
	  String str;

	  if (file != null) return file;
	  doc = getEditor().getDocument();
	  str = (String)doc.getProperty(DEFNAME_PROPERTY);
	  if (str != null)
	    return new File(getProjectDirectory(),str);
	  // end if
	  if ((sourceExt == null) || (sourceExt.length < 1)) return null;
	  return new File("*" + sourceExt[0]);
	} // end getDefaultFile

	public boolean accept(File dir, String name) {
	  if (sourceExt == null) return true;
	  for (int i=0; i<sourceExt.length; i++)
	    if (name.endsWith(sourceExt[i])) return true;
	  return false;
	} // end accept

	/** Method for the tab size changed */
	public void setTab(int size){
		Integer tabSize = new Integer(size);
		Document doc = editor.getDocument();
	   doc.putProperty(CodeEditorKit.TABSIZE_PROPERTY,tabSize);
	   prop.put(Const.TAB_SIZE,tabSize.toString());
	   desktop.put(Const.TAB_SIZE,tabSize.toString());
		TAB_SIZE=tabSize.toString();
	}

	/**Get the tab size

	public Integer getTabSize(){
		Integer tabSize;
		Object obj=editor.getDocument().getProperty(CodeEditorKit.TABSIZE_PROPERTY);
	    if (obj == null) obj = TAB_SIZE;
	    try {
	      tabSize = new Integer(obj.toString());
	    } catch (NumberFormatException excep) {
	      excep.printStackTrace();
	      tabSize = new Integer(TAB_SIZE);
	    }
		return tabSize;
	} // end getTabSize */

	 // inner classes
	class UndoAction extends AbstractAction {

	  // constructors
	  public UndoAction() {
	    super("Undo");
	    setEnabled(false);
	  } // end constructor UndoAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    try {
	      undo.undo();
	    } catch (CannotUndoException excep) {
	      System.err.println("Unable to undo: "+excep);
	      excep.printStackTrace();
	    }
	    update();
	    redoAction.update();
		 editor.requestFocus();
	  } // end actionPerformed

	  public void update() {
	    if(undo.canUndo()) {
	      setEnabled(true);
	      putValue(Action.NAME,undo.getUndoPresentationName());
	    }
	    else {
	      setEnabled(false);
	      putValue(Action.NAME,"Undo");
	    } // end if
	    undoBtnAction.update();
	  } // end update

	} // end UndoAction

	/**
	 * This class redirects the actionPerformed call to UndoAction.
	 * It was implemented to avoid changing the text of the toolbar
	 * button.
	 */
	class UndoBtnAction extends AbstractAction {

	  // constructors
	  public UndoBtnAction() {
	    super(UNDO_BTN_ACTION);
	    setEnabled(false);
	  } // end constructor UndoBtnAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    undoAction.actionPerformed(e);
	  } // end actionPerformed

	  protected void update() {
	    if(undo.canUndo()) {
	      setEnabled(true);
	    }
	    else {
	      setEnabled(false);
	    } // end if
	  } // end update

	} // end UndoBtnAction

	class RedoAction extends AbstractAction {

	  // constructors
	  public RedoAction() {
	    super("Redo");
	    setEnabled(false);
	  } // end constructor RedoAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    try {
	      undo.redo();
	    } catch (CannotRedoException excep) {
	      System.err.println("Unable to redo: "+excep);
	      excep.printStackTrace();
	    }
	    update();
	    undoAction.update();
		 editor.requestFocus();
	  } // end actionPerformed

	  protected void update() {
	    if(undo.canRedo()) {
	      setEnabled(true);
	      putValue(Action.NAME, undo.getRedoPresentationName());
	    }
	    else {
	      setEnabled(false);
	      putValue(Action.NAME, "Redo");
	    } // end if
	  } // end update

	} // end RedoAction

	class PrintAction extends AbstractAction {

	  // constructors
	  PrintAction() {
	    super(PRINT_ACTION);
	  } // end constructor PrintAction

	  PrintAction(String nm) {
	    super(nm);
	  } // end constructor PrintAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    Writer prn = new PrintDeviceWriter(editor.getFont());

	    try {
	      editor.write(prn);
	    } catch (IOException excep) {
	      excep.printStackTrace();
	    } finally {
	      try { prn.close(); } catch (IOException e1) {}
	    }
	  } // end actionPerformed

	} // end PrintAction

	/** Search a string in the current editor document. */
	class FindAction extends AbstractAction {

	  // instance variable declarations
	  protected String notFoundTitle;

	  // constructors
	  FindAction() {
	    super(FIND_ACTION);
	  } // end constructor FindAction

	  FindAction(String nm) {
	    super(nm);
	  } // end constructor FindAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    SearchDlg dlg = getDialog();

	    dlg.setSearchOptions(searchOpt.text,searchOpt.replace,
	                         searchOpt.caseSens,searchOpt.wordOnly);
	    dlg.setVisible(true);
	    if (dlg.isCanceled()) return;
	    notFoundTitle      = dlg.getTitle();
	    searchOpt.text     = dlg.getSearchString();
	    searchOpt.replace  = dlg.getReplaceString();
	    searchOpt.caseSens = dlg.isCaseSensitive();
	    searchOpt.wordOnly = dlg.isWordsOnlyMode();
	    searchOpt.quick    = false;
	    //tfQuickSearch.setText(searchOpt.text);
	    search(dlg.isToConfirm());
	  } // end actionPerformed

	  /** Perform a search specified by searchOpt. */
	  protected void search(boolean confirm) {
	    boolean found = editor.search(searchOpt.text,
	                                    searchOpt.wordOnly,
	                                    searchOpt.caseSens);
	    if (found) {
	      replaceString(searchOpt.replace,confirm);
	    }
	    else {
	      if (searchOpt.quick) {
	        String str;

	        str = ResourceUtil.getResStringOrKey(resources,NOTFOUNDMSG_LABEL);
			  frame.status.setText("Status: "+str);
	        getToolkit().beep();
	      }
	      else {
	        JOptionPane.showMessageDialog(frame,
	          ResourceUtil.getResStringOrKey(resources,NOTFOUNDMSG_LABEL),
	          notFoundTitle,JOptionPane.INFORMATION_MESSAGE);
	      } // end if
	    } // end if
	  } // end search

	  /** May be overwritten to create a replace operation. */
	  protected SearchDlg getDialog() {
	      return new SearchDlg(frame,SearchDlg.SEARCH,
	                             resources,SEARCHDLG_PREFIX);
	  } // end getDialog

	  /** May be overwritten to create a replace operation. */
	  protected void replaceString(String replace, boolean confirm) {
	    // do nothing
	  } // end replaceString

	} // end FindAction

	/** Repeat a previous performed find action. */
	class FindAgainAction extends FindAction {

	  // constructors
	  FindAgainAction() {
	    super(FINDAGAIN_ACTION);
	  } // end constructor FindAgainAction

	  // methods
	  public void actionPerformed(ActionEvent e) {
	    if (searchOpt.focus) editor.requestFocus();
	    if (searchOpt.text == null) super.actionPerformed(e);
	      else search(false);
	  } // end actionPerformed

	} // end FindAgainAction

	/** Search and replace a string in the current editor document. */
	class ReplaceAction extends FindAction {

	  // constructors
	  ReplaceAction() {
	    super(REPLACE_ACTION);
	  } // end constructor ReplaceAction

	  // methods
	  protected SearchDlg getDialog() {
	    return new SearchDlg(frame,SearchDlg.REPLACE,
	                           resources,SEARCHDLG_PREFIX);
	  } // end getDialog

	  protected void replaceString(String replace, boolean confirm) {
	    int optNr = 0; // default: replace it
	    boolean found;

	    do {
	      if (confirm) {
	        String str;
	        Object[] options;

	        str = ResourceUtil.getResStringOrKey(resources,REPCONFDLG_BUTTONS);
	        options = ResourceUtil.splitToWords(str);
	        str = ResourceUtil.getResStringOrKey(resources,REPCONFDLG_LABEL);
	        optNr = JOptionPane.showOptionDialog(frame,str,
	                  notFoundTitle,JOptionPane.DEFAULT_OPTION,
	                  JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
	      } // end if
	      switch (optNr) {
	        case 0:  // replace it
	          editor.replaceSelection(replace);
	        case 1:  // continue operation
	          found = editor.search(searchOpt.text,
	                                  searchOpt.wordOnly,
	                                  searchOpt.caseSens);
	        break;
	        default: // suspend operation
	          found = false;
	      } // end switch
	    } while (found);
	  } // end replaceString

	} // end ReplaceAction

	/*class FontAction extends AbstractAction {

	  // constructors
	  FontAction() {
	    super(FONT_ACTION);
	  } // end constructor FontAction

	  // methods
	  public void actionPerformed(ActionEvent e) {
	    FontDlg fontDlg = new FontDlg(frame,editor.getFont(),
	                                  resources,"fontDialog.");

	    fontDlg.setVisible(true);
	    if (!fontDlg.isCanceled()) {
	      Font font  = fontDlg.getSelectedFont();
	      String str = font.getName() + " " + font.getStyle() + " " +
	                   font.getSize();

	      editor.setFont(font);
	      desktop.put(FONT_SETTINGS,str);
	    } // end if
	  } // end actionPerformed

	} // end FontAction*/

	 /** Move the caret to an editor line keyed in in a small dialog box. */
	class GoToLineAction extends AbstractAction {

	  // constructors
	  GoToLineAction() {
	    super(GOTOLINE_ACTION);
	  } // end constructor GoToLineAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    int i        = 0;
	    boolean done = false;
	    String str;

	    do {
	      str = JOptionPane.showInputDialog(frame,
	              ResourceUtil.getResStringOrKey(resources,GOTOLINEDLG_TEXT),
	              ResourceUtil.getResStringOrKey(resources,GOTOLINEDLG_TITLE),
	              JOptionPane.PLAIN_MESSAGE);
	      if (str != null) try {
	        i    = Integer.valueOf(str).intValue();
	        done = true;
	      } catch (NumberFormatException excep) {}
	    } while ((str != null) && !done);
	    if (done) editor.goToLine(i - 1);
	  } // end actionPerformed

	} // end GoToLineAction

	/*/** Select the file format and the tabulator size.
	class FormatAction extends AbstractAction {

	  // constructors
	  FormatAction() {
	    super(FORMAT_ACTION);
	  } // end constructor FormatAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    Document doc = editor.getDocument();
	    Object obj;
	    String str;
	    Integer tabSize;
	    Boolean useTabs;
	    Integer format;
	    FileFormatDlg dlg;

	    obj = doc.getProperty(CodeEditorKit.TABSIZE_PROPERTY);
	    if (obj == null) obj = TAB_SIZE;
	    try {
	      tabSize = new Integer(obj.toString());
	    } catch (NumberFormatException excep) {
	      excep.printStackTrace();
	      tabSize = new Integer(TAB_SIZE);
	    }
	    str     = prop.getProperty(Const.USE_TABS,"false");
	    useTabs = new Boolean(str);
	    str     = prop.getProperty(Const.FILE_FORMAT,"" + TabWriter.PLATFORM);
	    try {
	      format = new Integer(str);
	    } catch (NumberFormatException excep) {
	      excep.printStackTrace();
	      format = new Integer(TabWriter.PLATFORM);
	    }
	    dlg = new FileFormatDlg(frame,resources,FORMATDLG_PREFIX,
	                            tabSize.intValue(),useTabs.booleanValue(),
	                            format.intValue());
	    dlg.show();
	    if (!dlg.isCanceled()) {
	      tabSize = new Integer(dlg.getTabSize());
	      doc.putProperty(CodeEditorKit.TABSIZE_PROPERTY,tabSize);
	      prop.put(Const.TAB_SIZE,tabSize.toString());
	      desktop.put(Const.TAB_SIZE,tabSize.toString());
	      useTabs = new Boolean(dlg.getUseTabs());
	      prop.put(Const.USE_TABS,useTabs.toString());
	      desktop.put(Const.USE_TABS,useTabs.toString());
	      format = new Integer(dlg.getFileFormat());
	      prop.put(Const.FILE_FORMAT,format.toString());
	      desktop.put(Const.FILE_FORMAT,format.toString());
	    } // end if
	  } // end actionPerformed

	} // end FormatAction*/

	 /** Display a file history popup menu. */
	class FileHistAction extends AbstractAction {

	  // constructors
	  FileHistAction() {
	    super(FILEHIST_ACTION);
	  } // end constructor FileHistAction

	  // instance methods
	  public void actionPerformed(ActionEvent e) {
	    String cmd = e.getActionCommand();

	    if (cmd.startsWith("_")) {
	      int index = Integer.parseInt(cmd.substring(1));

	      if (index < 1) return;

	      Object[] aObj = (Object[])histListData.elementAt(index);
	      File file     = new File(aObj[0].toString());
	      int line      = ((Integer)aObj[1]).intValue();
	      File cur;

	      editorActions.loadFile(file);
	      cur = editorActions.getCurrentFile();
	      if ((cur != null) &&
	          file.getAbsolutePath().equals(cur.getAbsolutePath())) {
	        goToLine(line);
	      } // end if
	      return;
	    } // end if

	    Object obj = e.getSource();
	    Component comp;
	    Enumeration en;
	    JPopupMenu popup;
	    int i;
	    if (!(obj instanceof Component)) {
	      Toolkit.getDefaultToolkit().beep();
	      return;
	    } // end if
	    comp  = (Component)obj;
	    en  = histListData.elements();
	    popup = new JPopupMenu();
	    i     = 0;
	    while (en.hasMoreElements()) {
	      Object[] aObj = (Object[])en.nextElement();
	      String path   = aObj[0].toString();
	      String name   = path.equals("") ? " " : (new File(path)).getName();
	      JMenuItem mi  = new JMenuItem(name);

	      mi.setActionCommand("_" + (i++));
	      mi.addActionListener(this);
	      popup.add(mi);
	    } // end while
	    popup.show(comp,0,comp.getSize().height - 1);
	  } // end actionPerformed

	} // end FileHistAction

   /** Extend the CutAction so that the focus can be set back to editor from main
   */
	class CutAction extends DefaultEditorKit.CutAction{
		// Constructors
		CutAction(){
			super();
		} // end constructor

		// instance methods
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			editor.requestFocus();
		} // end actionPerformed
	} // end CutAction

	/** Extend the CopyAction so that the focus can be set back to editor from main
   */
	class CopyAction extends DefaultEditorKit.CopyAction{
		// Constructors
		CopyAction(){
			super();
		} // end constructor

		// instance methods
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			editor.requestFocus();
		} // end actionPerformed
	} // end CopyAction

	/** Extend the PasteAction so that the focus can be set back to editor from main
   */
	class PasteAction extends DefaultEditorKit.PasteAction{
		// Constructors
		PasteAction(){
			super();
		} // end constructor

		// instance methods
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			editor.requestFocus();
		} // end actionPerformed
	} // end PasteAction

}// end JCodePanel

/* ******************************************************************
   end of file
*********************************************************************/


