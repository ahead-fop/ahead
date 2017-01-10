/**
 * class      : Main
 * description: The main GUI of the model explorer
 * @version 1.0
 * @author Yancong Zhou
 */

package ModelExplorer;

import ModelExplorer.SwingUtils.*;
import ModelExplorer.Browser.*;
import mmatrix.*;
import ModelExplorer.MMatrixBrowser.*;
import ModelExplorer.Editor.*;
import ModelExplorer.Composer.*;
import ModelExplorer.Guidsl.*;
import ModelExplorer.Util.*;

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
import javax.swing.text.html.*;
import java.net.*;

public class Main extends SwingApp {

    final static String newLine  = "\n";
    final static String openGif  = "images/Open.gif";
    final static String resetGif = "images/Reset.gif";
    final static String saveGif  = "images/Save.gif";
    final static String cutGif   = "images/Cut.gif";
    final static String copyGif  = "images/Copy.gif";
    final static String pasteGif = "images/Paste.gif";
    final static String backGif  = "images/Back.gif";
    final static String forwardGif = "images/Forward.gif";
    final static String upGif         = "images/Up.gif";
    final static String checkGif   = "images/Checker.gif";
    final static String reduceGif  = "images/Reducer.gif";
    final static String chooseGif  = "images/Choose.gif";
    final static String emptyGif   = "images/Empty.gif";
    final static String lineGif     = "images/Line.gif";
    final static String findGif     = "images/Find.gif";
    final static String undoGif     = "images/Undo.gif";
    final static String redoGif     = "images/Redo.gif";
    final static String printGif     = "images/Print.gif";
    final static String editorGif     = "images/Editor.gif";
    final static String viewerGif  = "images/Viewer.gif";

    public static String modelDir=".";
    public static String activeEquation=null;
    public static int    matrixIndex;
    public static int    highestIndex;
    public static boolean isLoaded=false;
    public static boolean isReduced=false;
    public static boolean isSub=false;
    public static Matrix rmatrix=null;
    public static String compositionDir=null;
    public static HashSet      fileExtens;
    public static HashSet      appliedFileExtens;
    public static Hashtable        menuItemsForEditor;
    public static Hashtable    buttonsForEditor;
    public static Font            font = null;
    public static Vector            tmpFileFilter=null;
    public static Vector            tmpDirFilter=null;
    //public static Vector matrixVector=new Vector();

    public QueryBrowserGui qbrowserGui;
    public TreeBrowser      treeBrowser;
    public MMatrixGui      mmatrixGui; //matrixIndex=0
    public MMatrixGui      rmatrixGui; //matrixIndex=-1
    public MMatrixGui      jakMatrixGui1; //matrixIndex=1
    public MMatrixGui      jakMatrixGui2; //matrixIndex=2
    public MMatrixGui          subMatrixGui; //matrixIndex=0;
    public ToolsPanel            toolsPanel;
    public GuidslPanel            guidslPanel;
    public EquationEditor  equationPanel;
    public JList           output;
    public CustomDialog    cDialog;
    public OptionDialog    oDialog;
    public HelpDialog          helpDialog;

    public ImageIcon lineIcon;
    public ImageIcon editorIcon;
    public ImageIcon viewerIcon;
    public ImageIcon backIcon;
    public ImageIcon forwardIcon;

    // Atomic Component Declarations

    public JMenu         model;
    public JMenu         fileMenu;
    public JMenu         editMenu;
    public JMenu        options;
    public JMenu          helpMenu;
    public JToolBar      toolBar;

    public JLabel         editorTitle;
    public JLabel         editorTitle2;
    public JCodePanel        codeEditor;
    public JEditorPane   editor;

    //public JLabel        outputTitle;
    public JLabel             status;

    public JComboBox     activeEq;
    public JButton       backButton;
    public JButton       forwardButton;
    public JButton            upButton;
    public JButton       editorButton;
    public MRUList           openedModel;
    public boolean           isReadOnly;
    public String          ahead_home;

    Properties      modelProp;
    JMenuItem     menuItem;
    JButton       button;
    JLabel        label;
    JTextArea     navigatorArea;

    Action        openAction;
    Action          resetAction;
    Action          backAction;
    Action          forwardAction;
    Action          upAction;
    Action        openFileAction;
    Action        newFileAction;
    Action        saveAction;
    Action        saveAsAction;
    Action        checkAction;
    Action        reduceAction;

    boolean       isEventEnable=true;

    //utility function to update the active equation
    public void updateActiveEq() {
        isEventEnable = false;
        File file=new File( modelDir );
        if ( file.isDirectory() ) {
            activeEq.removeAllItems();
            activeEq.addItem( "Current Directory" );
            String[] list=file.list();
            for ( int i=0; i<list.length; i++ ) {
                if ( list[i].substring( list[i].indexOf( '.' )+1 ).equals( "equation" ) ) {
                    activeEq.addItem( list[i] );
                }
            }
        }
        isEventEnable = true;
    }

    //utility function to reload the matrix
    public void reloadMatrix() {
        status.setText( "Status: Loading the navigator matrix .........1" );
        status.setForeground( Color.magenta );
        isLoaded=true;
        browserTabbedPane.setEnabledAt( 2,false );
        matrixIndex=0;
        highestIndex=0;
        backButton.setEnabled( false );
        forwardButton.setEnabled( false );

        MMatrixGuiThread thread=new MMatrixGuiThread( Main.this );

    }

    /** Utility function to transfer String words to HashSet
     */
    public HashSet toHashSet( String words ) {
        StringTokenizer t = new StringTokenizer( words, ", " );
        HashSet hs=new HashSet();
        while ( t.hasMoreTokens() ) {
            hs.add( t.nextToken() );
        } // end while
        return hs;
    } // end toHashSet

    /** Utility function to add special separator to toolbar
     */
    public void addSepa( int num ) {
        JButton button=new JButton( lineIcon );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        for ( int i=0; i<num; i++ )
            toolBar.add( button );
    } // end addSepa

    /** Utility function to set the MRU number
     */
    public void setMRUMax( int max ) {
        MRUList.MRU_MAX=max;
    }

    public void initConstants() {
        matrixIndex=0;
        highestIndex=0;
    }

    // initialize the atomic component
    public void initAtoms() {

        super.initAtoms();

        String tempStr;
        if ( modelDir.equals( "." ) ) {
            tempStr= ( new File( modelDir ) ).getAbsolutePath();
            tempStr=tempStr.substring( 0,tempStr.lastIndexOf( File.separatorChar ) );
            modelDir=tempStr;
        }

        ahead_home = System.getProperty( "jts.home" ) ;
        while( ahead_home==null ) {
            ahead_home = JOptionPane.showInputDialog( "Property \"jts.home\" is not defined."
                          + "  Please enter a value for it:" ) ;
        }
        ahead_home=ahead_home.replace( '\\', File.separatorChar );
        ahead_home=ahead_home.replace( '/', File.separatorChar );
        File aheadDir = new File( ahead_home ) ;

        while ( ahead_home.equals( " " )||! aheadDir.isDirectory() ) {
            ahead_home = JOptionPane.showInputDialog( "Property \"jts.home\" is not a valid directory."
                          + "  Please enter a new value:", ahead_home ) ;
            ahead_home=ahead_home.replace( '\\', File.separatorChar );
            ahead_home=ahead_home.replace( '/', File.separatorChar );
            aheadDir = new File( ahead_home ) ;
        }

        lineIcon = new ImageIcon( this.getClass().getResource( lineGif ) );
        editorIcon = new ImageIcon( this.getClass().getResource( editorGif ) );
        viewerIcon = new ImageIcon( this.getClass().getResource( viewerGif ) );
        backIcon = new ImageIcon( this.getClass().getResource( backGif ) );
        forwardIcon = new ImageIcon( this.getClass().getResource( forwardGif ) );
        isReadOnly=true;
        menuItemsForEditor = new Hashtable();
        buttonsForEditor   = new Hashtable();

        modelProp = new Properties();
        try {
            modelProp.load( new FileInputStream( "_MEState.properties" ) );
        }
        catch( IOException ioe ) {}

        String str=modelProp.getProperty( "matrixFilter" );
        if ( str!=null ) {
            appliedFileExtens = toHashSet( str );
            modelProp.remove( "matrixFilter" );
        }
        else {
            appliedFileExtens = new HashSet();
            appliedFileExtens.add( "jak" );
            appliedFileExtens.add( "jsm" );
        }

        openedModel = new MRUList( "_MRUModel.properties" );

        str=modelProp.getProperty( "model" );
        if ( str!=null ) {
            File file=new File( modelDir );
            if ( file.isDirectory() ) {
                modelDir=str;
                modelProp.remove( "model" );
            }
            else {
                JOptionPane.showMessageDialog( null,
                                                                                                                                                                                                                "The lastest opened model is invalid now! Using current direcory.",
                                                                                                                                                                                                                "Error",
                                                                                                                                                                                                                JOptionPane.ERROR_MESSAGE );
            }
        }

        toolsPanel = createToolsPanel();
        equationPanel = new EquationEditor( this );
        guidslPanel = new GuidslPanel( this );

        navigatorArea = new JTextArea( 60,30 );

        model = new JMenu( "Model" );
        fileMenu = new JMenu( "File" );
        editMenu = new JMenu( "Edit" );
        options = new JMenu( "Options" );
        helpMenu = new JMenu( "Help" );

        toolBar = new JToolBar();
        toolBar.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.setBorder( new EtchedBorder() );

        // Add special separator to toolbar.
        addSepa( 2 );

        label = new JLabel( " Model:" );
        label.setForeground( Color.gray );
        toolBar.add( label );

        openAction = new OpenAction();
        resetAction = new ResetAction();
        backAction = new BackAction();
        upAction = new UpAction();
        forwardAction = new ForwardAction();
        checkAction = new CheckAction();
        reduceAction = new ReduceAction();

        button = toolBar.add( openAction );
        button.setIcon( new ImageIcon( this.getClass().getResource( openGif ) ) );
        button.setText( "" ); //an icon-only button
        button.setToolTipText( "Open" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        menuItem = model.add( openAction );
        menuItem.setIcon( null );

        button = toolBar.add( resetAction );
        button.setIcon( new ImageIcon( this.getClass().getResource( resetGif ) ) );
        button.setText( "" ); //an icon-only button
        button.setToolTipText( "Reset" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        menuItem = model.add( resetAction );
        menuItem.setIcon( null );

        model.addSeparator();
        menuItem = new JMenuItem( "Exit" );
        menuItem.addActionListener( new ExitActionListener() );
        model.add( menuItem );

        //toolBar.addSeparator();
        addSepa( 2 );
        //toolBar.addSeparator();

        label = new JLabel( " Navigator:" );
        label.setForeground( Color.gray );
        toolBar.add( label );

        backButton = toolBar.add( backAction );
        backButton.setIcon( backIcon );
        backButton.setText( "" ); //an icon-only button
        backButton.setToolTipText( "Back" );
        backButton.setBorderPainted( false );
        backButton.setMargin( new Insets( -4,0,-4,0 ) );
        backButton.setEnabled( false );

        upButton = toolBar.add( upAction );
        upButton.setIcon( new ImageIcon( this.getClass().getResource( upGif ) ) );
        upButton.setText( "" ); //an icon-only button
        upButton.setToolTipText( "Up" );
        upButton.setBorderPainted( false );
        upButton.setMargin( new Insets( -4,0,-4,0 ) );
        upButton.setEnabled( false );

        forwardButton = toolBar.add( forwardAction );
        forwardButton.setIcon( forwardIcon );
        forwardButton.setText( "" ); //an icon-only button
        forwardButton.setToolTipText( "Forward" );
        forwardButton.setBorderPainted( false );
        forwardButton.setMargin( new Insets( -4,0,-4,0 ) );
        forwardButton.setEnabled( false );

        activeEq = new JComboBox();
        updateActiveEq();
        str=modelProp.getProperty( "equation" );
        if ( str!=null ) {
            isEventEnable=false;
            activeEq.setSelectedItem( str );
            activeEquation=str;
            isEventEnable=true;
            modelProp.remove( "equation" );
        }
        str=modelProp.getProperty( "composition" );
        if ( str!=null ) {
            compositionDir=str;
            modelProp.remove( "composition" );
        }

        activeEq.setBackground( Color.white );
        activeEq.setPreferredSize( new Dimension( 10,5 ) );
        toolBar.add( activeEq );

        button = toolBar.add( checkAction );
        button.setIcon( new ImageIcon( this.getClass().getResource( checkGif ) ) );
        button.setText( "" ); //an icon-only button
        button.setToolTipText( "Error Checker" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );

        button = toolBar.add( reduceAction );
        button.setIcon( new ImageIcon( this.getClass().getResource( reduceGif ) ) );
        button.setText( "" ); //an icon-only button
        button.setToolTipText( "Size Reducing" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );

        //toolBar.addSeparator();
        addSepa( 2 );
        //toolBar.addSeparator();

        label = new JLabel( " Editor:" );
        label.setForeground( Color.gray );
        toolBar.add( label );

        // fileMenu and editMenu part
        menuItem = new JMenuItem( "New" );
        fileMenu.add( menuItem );
        menuItemsForEditor.put( "new", menuItem );

        menuItem = new JMenuItem( "Open" );
        fileMenu.add( menuItem );
        menuItemsForEditor.put( "open", menuItem );

        menuItem = new JMenuItem( "Close" );
        fileMenu.add( menuItem );
        menuItemsForEditor.put( "close", menuItem );

        fileMenu.addSeparator();

        menuItem = new JMenuItem( "Save" );
        fileMenu.add( menuItem );
        menuItemsForEditor.put( "save", menuItem );

        menuItem = new JMenuItem( "Save As" );
        fileMenu.add( menuItem );
        menuItemsForEditor.put( "saveas", menuItem );

        fileMenu.addSeparator();

        menuItem = new JMenuItem( "Print" );
        fileMenu.add( menuItem );
        menuItemsForEditor.put( "print", menuItem );

        menuItem = new JMenuItem( "Cut" );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "cut", menuItem );

        menuItem = new JMenuItem( "Copy" );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "copy", menuItem );

        menuItem = new JMenuItem( "Paste" );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "paste", menuItem );

        editMenu.addSeparator();

        menuItem = new JMenuItem( "Undo" );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "undo", menuItem );

        menuItem = new JMenuItem( "Redo" );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "redo", menuItem );

        editMenu.addSeparator();

        menuItem = new JMenuItem( "Find..." );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "find", menuItem );

        menuItem = new JMenuItem( "FindAgain" );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "findagain", menuItem );

        menuItem = new JMenuItem( "Replace..." );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "replace", menuItem );

        editMenu.addSeparator();

        menuItem = new JMenuItem( "Go To Line..." );
        editMenu.add( menuItem );
        menuItemsForEditor.put( "goToLine", menuItem ); // end fileMenu and editMenu part

        // Toolbar editor part
        editorButton = new JButton( editorIcon );
        editorButton.setToolTipText( "Editor" );
        editorButton.setBorderPainted( false );
        editorButton.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( editorButton );
        editorButton.addActionListener( new editorActionListener() );

        button = new JButton( new ImageIcon( this.getClass().getResource( saveGif ) ) );
        button.setToolTipText( "Save" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( button );
        buttonsForEditor.put( "save", button );
        addSepa( 1 );

        button=new JButton( new ImageIcon( this.getClass().getResource( cutGif ) ) );
        button.setToolTipText( "Cut" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( button );
        buttonsForEditor.put( "cut-to-clipboard",button );

        button=new JButton( new ImageIcon( this.getClass().getResource( copyGif ) ) );
        button.setToolTipText( "Copy" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( button );
        buttonsForEditor.put( "copy-to-clipboard",button );

        button=new JButton( new ImageIcon( this.getClass().getResource( pasteGif ) ) );
        button.setToolTipText( "Paste" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( button );
        buttonsForEditor.put( "paste-from-clipboard",button );

        addSepa( 1 );
        button=new JButton( new ImageIcon( this.getClass().getResource( undoGif ) ) );
        button.setToolTipText( "Undo" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( button );
        buttonsForEditor.put( "Undo",button );

        button=new JButton( new ImageIcon( this.getClass().getResource( redoGif ) ) );
        button.setToolTipText( "Redo" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( button );
        buttonsForEditor.put( "Redo",button );

        button=new JButton( new ImageIcon( this.getClass().getResource( findGif ) ) );
        button.setToolTipText( "Find" );
        button.setBorderPainted( false );
        button.setMargin( new Insets( -4,0,-4,0 ) );
        toolBar.add( button );
        buttonsForEditor.put( "find",button ); // end toolbar editor part

        // Options Menu part
        menuItem = new JMenuItem( "Read Only", new ImageIcon( this.getClass().getResource( chooseGif ) ) );
        menuItem.addActionListener( new ReadOnlyActionListener() );
        options.add( menuItem );
        options.addSeparator();
        menuItem = new JMenuItem( "Preferences..." );
        menuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                JMenuItem mi= ( JMenuItem )e.getSource();
                if ( mi.getText().equals( "Preferences..." ) ) {
                    if ( oDialog==null )
                        oDialog=new OptionDialog( Main.this );
                    else
                        oDialog.setVisible( true );
                }
            }
        } );
        options.add( menuItem ); // end Options Menu part

        //help menu part
        menuItem = new JMenuItem( "User Guide" );
        menuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                JMenuItem mi= ( JMenuItem )e.getSource();
                if ( mi.getText().equals( "User Guide" ) ) {
                    if ( helpDialog==null )
                        helpDialog=new HelpDialog( Main.this );
                    else
                        helpDialog.reset();
                    helpDialog.setVisible( true );
                }
            }
        } );
        helpMenu.add( menuItem );
        helpMenu.addSeparator();
        menuItem = new JMenuItem( "About..." );
        menuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                JMenuItem mi= ( JMenuItem )e.getSource();
                if ( mi.getText().equals( "About..." ) ) {
                    JOptionPane.showMessageDialog( Main.this,
                                                                                                                                                                                                                                                                                                            "  Version: 1.3\n  Date:       04/18/2003\n  Author:    Yancong Zhou\n\n"+
                                                                                                                                                                                                                                                                                                            "Copyright: Software Systems Generator Research Group. All rights reserved.\n\n\n"+
                                                                                                                                                                                                                                                                                                            "                            The University of Texas at Austin",
                                                                                                                                                                                                                                                                                                            "About Model Explorer",
                                                                                                                                                                                                                                                                                                            JOptionPane.INFORMATION_MESSAGE );

                }
            }
        } );
        helpMenu.add( menuItem ); //end help menu part

        label = new JLabel( "                   " );
        toolBar.add( label );

        // Other atomics.
        str=modelProp.getProperty( "tabSize" );
        if ( str!=null )
            codeEditor.TAB_SIZE=str;
        str=modelProp.getProperty( "MRUMax" );
        if ( str!=null )
            MRUList.MRU_MAX = Integer.parseInt( str );

        editor = new JEditorPane();
        editor.setEditable( false );
        editor.setFont( new Font( "Courier",Font.PLAIN,12 ) );

        codeEditor = new JCodePanel( this );

        editorTitle = new JLabel( "Unit Viewer" );
        editorTitle.setAlignmentX( JLabel.CENTER_ALIGNMENT );
        status = new JLabel( "Status: " );

        editorTitle2 = new JLabel( "Unit Viewer" );
        editorTitle2.setAlignmentX( JLabel.CENTER_ALIGNMENT );

        //outputTitle = new JLabel("Output Window");
        //outputTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        output = new JList();

    }

    // Layout Component Declarations
    JSplitPane            modelExplorerPane;

    public MyJTabbedPane        browserTabbedPane;
    public JMenuBar            menuBar;
    public JScrollPane        editorScrollPane;
    public JTabbedPane        editorTabbedPane;
    public JPanel           editorPanel;
    public JPanel           editorPanel2;
    public JScrollPane        outputScrollPane;
    //public JPanel           outputPanel;

    //Utility function to initilize the JPanel
    JPanel initPanel( boolean horizontal ) {
        JPanel p = new JPanel();
        if ( horizontal )
            p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
        else
            p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
        p.setBorder( BorderFactory.createEmptyBorder( 1,2,1,2 ) );
        return p;
    }

    public void initLayout() {
        menuBar = new JMenuBar();
        menuBar.add( model );
        menuBar.add( fileMenu );
        menuBar.add( editMenu );
        menuBar.add( options );
        menuBar.add( helpMenu );

        editorTabbedPane = new JTabbedPane( JTabbedPane.BOTTOM );
        editorTabbedPane.setName( "editorTabbedPane" );

        editorPanel = initPanel( false );
        editorPanel.add( editorTitle );
        editorPanel.add( codeEditor.ContentPane );

        editorScrollPane = new JScrollPane( editor );
        editorScrollPane.setPreferredSize( new Dimension( 200,150 ) );
        editorScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        editorPanel2 = initPanel( false );
        editorPanel2.add( editorTitle2 );
        editorPanel2.add( editorScrollPane );

        outputScrollPane = new JScrollPane( output );
        outputScrollPane.setPreferredSize( new Dimension( 200,150 ) );
        outputScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        //outputPanel = initPanel(false);
        //outputPanel.add(outputTitle);
        //outputPanel.add(outputScrollPane);

        if ( isReadOnly )
            editorTabbedPane.addTab( "Viewer", editorPanel );
        else
            editorTabbedPane.addTab( "Editor", editorPanel );
        editorTabbedPane.addTab( "Equation", equationPanel.ContentPane );
        editorTabbedPane.addTab( "Tools", toolsPanel.ContentPane );
        editorTabbedPane.addTab( "GuiDsl", guidslPanel.ContentPane );
        //editorTabbedPane.addTab("Output", outputPanel);
        //editorTabbedPane.setFont(new Font("Courier", Font.BOLD, 12));
        editorTabbedPane.setSelectedIndex( 0 );

        treeBrowser = new TreeBrowser( this );
        qbrowserGui = new QueryBrowserGui( this );
        //mmatrixGui = new MMatrixGui(this);

        browserTabbedPane = new MyJTabbedPane();
        browserTabbedPane.setName( "browserTabbedPane" );
        //ImageIcon  nullIcon = null;
        browserTabbedPane.addTab( "Tree", null, treeBrowser.ContentPane );
        browserTabbedPane.addTab( "Query", null, qbrowserGui.ContentPane );
        //browserTabbedPane.addTab("Navigator", null, mmatrixGui.ContentPane);
        browserTabbedPane.addTab( "Navigator", null, navigatorArea );
        //browserTabbedPane.setEnabledAt(2,false);

        modelExplorerPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
                           browserTabbedPane, editorTabbedPane );
        modelExplorerPane.setOneTouchExpandable( true );
        modelExplorerPane.setDividerLocation( 320 );

    }

    // the ContentPane

    public void initContentPane() {
        ContentPane = new JPanel();
        ContentPane.setLayout( new BorderLayout() );
        ContentPane.setBorder( BorderFactory.createEmptyBorder( 2,//top
                                                               2,//left
                                                               2,//bottom
                                                               2 ) );
        ContentPane.add( toolBar, BorderLayout.NORTH );
        ContentPane.add( modelExplorerPane, BorderLayout.CENTER );
        ContentPane.add( status, BorderLayout.SOUTH );
        setJMenuBar( menuBar );
        StringBuffer strBuf=new StringBuffer();
        strBuf.append( "Model Explorer - [" );

        String tempStr=modelDir;
        tempStr=tempStr.substring( tempStr.lastIndexOf( File.separatorChar )+1 );

        strBuf.append( tempStr );
        strBuf.append( " [Model]]" );
        setTitle( strBuf.toString() );
    }

    public void openModel( String model ) {
        File file=new File( model );
        if ( file.isDirectory() ) {
            status.setText( "Status: It's openning the selected model...." );

            modelDir = model;
            activeEquation = null;

            treeBrowser = new TreeBrowser( Main.this );
            browserTabbedPane.setComponentAt( 0,treeBrowser.ContentPane );

            qbrowserGui =new QueryBrowserGui( Main.this );
            browserTabbedPane.setComponentAt( 1, qbrowserGui.ContentPane );

            browserTabbedPane.setComponentAt( 2,navigatorArea );

            browserTabbedPane.setSelectedIndex( 0 );

            toolsPanel = createToolsPanel();
            editorTabbedPane.setComponentAt( 2,toolsPanel.ContentPane );
            guidslPanel = new GuidslPanel( Main.this );
            editorTabbedPane.setComponentAt( 3,guidslPanel.ContentPane );

            StringBuffer strBuf=new StringBuffer();
            strBuf.append( "Model Explorer - [" );
            strBuf.append( file.getName() );
            strBuf.append( " [Model]]" );
            setTitle( strBuf.toString() );

            updateActiveEq();
            matrixIndex=0;
            highestIndex=0;
            isLoaded=false;
            isSub=false;
            upButton.setEnabled( false );
            backButton.setEnabled( false );
            forwardButton.setEnabled( false );
            openedModel.add( modelDir );
            codeEditor.close();
            toolsPanel.resetEq();
            guidslPanel.resetModel();
            equationPanel.resetEq();

            status.setText( "Status: The new model was loaded successfully!" );
        }
        else {
            JOptionPane.showMessageDialog( null,
                                                                                                                                                            "Invalid model directory!",
                                                                                                                                                            "Error",
                                                                                                                                                            JOptionPane.ERROR_MESSAGE );
        }
    }

    public void initListeners() {
        model.addMenuListener( new MenuListener() {
            public void menuSelected( MenuEvent e ) {
                JMenu menu= ( JMenu )e.getSource();
                if ( openedModel.myList[0]!=null ) {
                    menu.removeAll();
                    menuItem = menu.add( openAction );
                    menuItem.setIcon( null );
                    menuItem = menu.add( resetAction );
                    menuItem.setIcon( null );
                    menu.addSeparator();
                    JMenuItem mitem;
                    for( int i=0; i<MRUList.MRU_MAX;i++ ) {
                        if ( openedModel.myList[i]!=null ) {
                            final String str=openedModel.myList[i];
                            mitem=new JMenuItem( ( i+1 )+". "+str );
                            mitem.addActionListener( new ActionListener() {
                                public void actionPerformed( ActionEvent e ) {
                                    openModel( str );
                                }
                            } );
                            menu.add( mitem );
                        }
                        else
                            break;
                    }
                    menu.addSeparator();
                    menuItem = new JMenuItem( "Exit" );
                    menuItem.addActionListener( new ExitActionListener() );
                    menu.add( menuItem );
                }
            }
            public void menuDeselected( MenuEvent e ) {}
            public void menuCanceled( MenuEvent e ) {}
        } );

        activeEq.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                if ( isEventEnable ) {
                    JComboBox cb = ( JComboBox )e.getSource();
                    String equationName = ( String )cb.getSelectedItem();
                    if ( equationName.substring( equationName.indexOf( '.' )+1 ).equals( "equation" ) ) {
                        activeEquation = equationName;
                        compositionDir = modelDir+File.separatorChar+equationName.substring( 0,equationName.indexOf( '.' ) );
                    }
                    else {
                        activeEquation = null;
                        compositionDir = null;
                    }

                    qbrowserGui =new QueryBrowserGui( Main.this );
                    browserTabbedPane.setComponentAt( 1, qbrowserGui.ContentPane );

                    status.setText( "Status: Loading the navigator matrix .........2" );
                    status.setForeground( Color.magenta );
                    browserTabbedPane.setEnabledAt( 1,false );
                    browserTabbedPane.setEnabledAt( 2,false );

                    //mmatrixGui =new MMatrixGui(Main.this);
                    browserTabbedPane.setSelectedIndex( 0 );
                    MMatrixGuiThread thread=new MMatrixGuiThread( Main.this );

                    isLoaded=true;
                    isSub=false;
                    matrixIndex=0;
                    highestIndex=0;
                    upButton.setEnabled( false );
                    backButton.setEnabled( false );
                    forwardButton.setEnabled( false );
                }
            }
        } );

        browserTabbedPane.addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                JTabbedPane tp= ( JTabbedPane )e.getSource();
                int index=tp.getSelectedIndex();
                if ( index==2 && !isLoaded ) {
                    status.setText( "Status: Loading the navigator matrix..........3" );
                    status.setForeground( Color.magenta );
                    isLoaded = true;
                    browserTabbedPane.setEnabledAt( 2,false );
                    MMatrixGuiThread thread=new MMatrixGuiThread( Main.this );
                    browserTabbedPane.setSelectedIndex( 2 );
                }
                else {
                    switch ( index )
                    {
                        case 0:
                        tp.getComponentAt( 1 ).setVisible( false );
                        tp.getComponentAt( 2 ).setVisible( false );
                        tp.getComponentAt( 0 ).setVisible( true );
                        break;
                        case 1:
                        tp.getComponentAt( 0 ).setVisible( false );
                        tp.getComponentAt( 1 ).setVisible( true );
                        tp.getComponentAt( 2 ).setVisible( false );
                        break;
                        case 2:
                        tp.getComponentAt( 0 ).setVisible( false );
                        tp.getComponentAt( 1 ).setVisible( false );
                        tp.getComponentAt( 2 ).setVisible( true );
					}
                }
            }
        } );

        editor.addHyperlinkListener( new HyperlinkListener() {
            public void hyperlinkUpdate( HyperlinkEvent e ) {
                if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
                    JEditorPane pane = ( JEditorPane ) e.getSource();
                    if ( e instanceof HTMLFrameHyperlinkEvent ) {
                        HTMLFrameHyperlinkEvent  evt = ( HTMLFrameHyperlinkEvent )e;
                        HTMLDocument doc = ( HTMLDocument )pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent( evt );
                    }
                    else {
                        try {
                            pane.setPage( e.getURL() );
                        }
                        catch ( Throwable t ) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        } );

    }

    public void saveState() {
        try {
            if ( modelDir!=null )
                modelProp.setProperty( "model", modelDir );
            if ( activeEquation!=null )
                modelProp.setProperty( "equation", activeEquation );
            if ( compositionDir!=null && activeEquation!=null )
                modelProp.setProperty( "composition",compositionDir );
            String str=appliedFileExtens.toString();
            modelProp.setProperty( "matrixFilter", str.substring( 1,str.length()-1 ) );
            modelProp.setProperty( "tabSize", codeEditor.TAB_SIZE );
            modelProp.setProperty( "MRUMax", ( new Integer( MRUList.MRU_MAX ) ).toString() );
            modelProp.store( new FileOutputStream( "_MEState.properties" ),null );
        }
        catch( IOException e ) {
            System.out.println( e );
        }
    }

    public Main() { super();
    }

    public Main( String AppTitle ) {
                super( AppTitle );
        addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent e ) {
                //saveState();
                //openedModel.save();
                codeEditor.close();
                System.exit( 0 );
            }
        } );
    }

    public static void main( String[] args ) {
        new Main( "Model Explorer" );
    }

    //inner classes
    class ReadOnlyActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            JMenuItem mi= ( JMenuItem )e.getSource();
            if ( mi.getText().equals( "Read Only" ) ) {
                if ( isReadOnly ) {
                    isReadOnly=false;
                    editorButton.setIcon( viewerIcon );
                    editorButton.setToolTipText( "Viewer" );
                    mi.setIcon( new ImageIcon( this.getClass().getResource( emptyGif ) ) );
                    codeEditor.setEditable( true );
                    editorTabbedPane.setTitleAt( 0,"Editor" );
                    status.setText( "Status: Editing the selected artifact." );
                    String str=editorTitle2.getText();
                    if ( !str.equals( "Unit Viewer" ) ) {
                        str=str.substring( str.lastIndexOf( '.' )+1 );
                        str=str.toLowerCase();
                        if ( str.equals( "html" )||str.equals( "htm" ) ) {
                            editorTabbedPane.setComponentAt( 0,editorPanel );
                        }
                        else
                            if ( str.equals( "gif" )||str.equals( "jpg" ) ) {
                                status.setText( "Status: this type file can't be edited here." );
                            }
                    }
                }
                else {
                    isReadOnly=true;
                    editorButton.setIcon( editorIcon );
                    editorButton.setToolTipText( "Editor" );
                    mi.setIcon( new ImageIcon( this.getClass().getResource( chooseGif ) ) );
                    codeEditor.setEditable( false );
                    editorTabbedPane.setTitleAt( 0,"Viewer" );
                    status.setText( "Status: Displaying the selected artifact." );
                    String str=editorTitle2.getText();
                    if ( !str.equals( "Unit Viewer" ) ) {
                        str=str.substring( str.lastIndexOf( '.' )+1 );
                        str=str.toLowerCase();
                        if ( str.equals( "html" )||str.equals( "htm" ) ) {
                            editorTabbedPane.setComponentAt( 0,editorPanel2 );
                        }
                    }
                }
            }
        }
    }

    //inner classes
    class editorActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            JMenuItem mi=options.getItem( 0 );
            if ( mi.getText().equals( "Read Only" ) ) {
                if ( isReadOnly ) {
                    isReadOnly=false;
                    editorButton.setIcon( viewerIcon );
                    editorButton.setToolTipText( "Viewer" );
                    mi.setIcon( new ImageIcon( this.getClass().getResource( emptyGif ) ) );
                    codeEditor.setEditable( true );
                    editorTabbedPane.setTitleAt( 0,"Editor" );
                    status.setText( "Status: Editing the selected artifact." );
                    String str=editorTitle2.getText();
                    if ( !str.equals( "Unit Viewer" ) ) {
                        str=str.substring( str.lastIndexOf( '.' )+1 );
                        str=str.toLowerCase();
                        if ( str.equals( "html" )||str.equals( "htm" ) ) {
                            editorTabbedPane.setComponentAt( 0,editorPanel );
                        }
                        else
                            if ( str.equals( "gif" )||str.equals( "jpg" ) ) {
                                status.setText( "Status: this type file can't be edited here." );
                            }
                    }
                }
                else {
                    isReadOnly=true;
                    editorButton.setIcon( editorIcon );
                    editorButton.setToolTipText( "Editor" );
                    mi.setIcon( new ImageIcon( this.getClass().getResource( chooseGif ) ) );
                    codeEditor.setEditable( false );
                    editorTabbedPane.setTitleAt( 0,"Viewer" );
                    status.setText( "Status: Displaying the selected artifact." );
                    String str=editorTitle2.getText();
                    if ( !str.equals( "Unit Viewer" ) ) {
                        str=str.substring( str.lastIndexOf( '.' )+1 );
                        str=str.toLowerCase();
                        if ( str.equals( "html" )||str.equals( "htm" ) ) {
                            editorTabbedPane.setComponentAt( 0,editorPanel2 );
                        }
                    }
                }
            }
        }
    }

    /** This class is an action for open model.
     */
    class OpenAction extends AbstractAction {
        // constructors
        public OpenAction() {
                                    super( "Open" );
            setEnabled( true );
        } // end constructor OpenAction

        // instance methods
        public void actionPerformed( ActionEvent e ) {
            JFileChooser  chooser;
            //File file=new File(modelDir);
            //if (file.getParent()!=null)
            //   chooser = new JFileChooser(file.getParent()); // end if
            //else
            //   chooser = new JFileChooser(modelDir); // end else
	   // now have modelDir point to its parent directory
	   // a/b/c --> a/b; a\b\c -->a\b  c: --> c:
	   
           int i = modelDir.lastIndexOf('/');
	   int j = modelDir.lastIndexOf('\\');
	   if (i >= 0) modelDir = modelDir.substring(0,i); 
	   else 
           if (j >= 0) modelDir= modelDir.substring(0,j); 

            chooser = new JFileChooser( modelDir );
            chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
            int openVal = chooser.showOpenDialog( Main.this );
            if ( openVal == JFileChooser.APPROVE_OPTION ) {
                File file = chooser.getSelectedFile();
                status.setText( "Status: It's openning the selected model...." );
                modelDir = file.getAbsolutePath();
                activeEquation = null;

                treeBrowser = new TreeBrowser( Main.this );
                browserTabbedPane.setComponentAt( 0,treeBrowser.ContentPane );

                qbrowserGui =new QueryBrowserGui( Main.this );
                browserTabbedPane.setComponentAt( 1, qbrowserGui.ContentPane );

                browserTabbedPane.setComponentAt( 2,navigatorArea );

                browserTabbedPane.setSelectedIndex( 0 );

                toolsPanel = createToolsPanel();
                editorTabbedPane.setComponentAt( 2,toolsPanel.ContentPane );
                guidslPanel = new GuidslPanel( Main.this );
                editorTabbedPane.setComponentAt( 3,guidslPanel.ContentPane );

                StringBuffer strBuf=new StringBuffer();
                strBuf.append( "Model Explorer - [" );
                strBuf.append( file.getName() );
                strBuf.append( " [Model]]" );
                setTitle( strBuf.toString() );

                updateActiveEq();
                matrixIndex=0;
                highestIndex=0;
                isLoaded=false;
                backButton.setEnabled( false );
                forwardButton.setEnabled( false );
                upButton.setEnabled( false );
                isSub=false;
                openedModel.add( modelDir );
                codeEditor.close();
                toolsPanel.resetEq();
                guidslPanel.resetModel();
                equationPanel.resetEq();

                status.setText( "Status: The new model was loaded successfully!" );
            } // end if
        } // end actionPerformed
    } // end OpenAction

    class ResetAction extends AbstractAction {
        // constructors
        public ResetAction() {
                                    super( "Reset" );
            setEnabled( true );
        } // end constructor ResetAction

        // instance methods
        public void actionPerformed( ActionEvent e ) {
            reset();
        } // end actionPerformed
    } // end ResetAction

    public void reset() {
        activeEquation = null;
        File file = new File( modelDir );
        treeBrowser = new TreeBrowser( Main.this );
        browserTabbedPane.setComponentAt( 0,treeBrowser.ContentPane );

        qbrowserGui =new QueryBrowserGui( Main.this );
        browserTabbedPane.setComponentAt( 1, qbrowserGui.ContentPane );

        browserTabbedPane.setComponentAt( 2,navigatorArea );

        browserTabbedPane.setSelectedIndex( 0 );

        toolsPanel = createToolsPanel();
        editorTabbedPane.setComponentAt( 2,toolsPanel.ContentPane );
        guidslPanel = new GuidslPanel( Main.this );
        editorTabbedPane.setComponentAt( 3,guidslPanel.ContentPane );

        StringBuffer strBuf=new StringBuffer();
        strBuf.append( "Model Explorer - [" );
        strBuf.append( file.getName() );
        strBuf.append( " [Model]]" );
        setTitle( strBuf.toString() );

        updateActiveEq();

        matrixIndex=0;
        highestIndex=0;
        isLoaded=false;
        isSub=false;
        upButton.setEnabled( false );
        backButton.setEnabled( false );
        forwardButton.setEnabled( false );

        status.setText( "Status: The model has been reset successfully!" );
    }

    class BackAction extends AbstractAction {
        // constructors
        public BackAction() {
                                    super( "Back" );
            setEnabled( true );
        } // end constructor BackAction

        // instance methods
        public void actionPerformed( ActionEvent e ) {
            if ( ( matrixIndex-1 )>-2 ) {
                /* DSB -- replaced remove, add with setComponentAt *****/
                //browserTabbedPane.remove(2);
                switch ( matrixIndex-1 )
                {
                    case -1:
                    if ( isSub )
                        //browserTabbedPane.add(subMatrixGui.ContentPane,2);
                        browserTabbedPane.setComponentAt( 2,subMatrixGui.ContentPane );
                    else
                        //browserTabbedPane.add(rmatrixGui.ContentPane,2);
                        browserTabbedPane.setComponentAt( 2,rmatrixGui.ContentPane );
                    break;
                    case 0:
                    if ( isSub )
                        //browserTabbedPane.add(subMatrixGui.ContentPane,2);
                        browserTabbedPane.setComponentAt( 2,subMatrixGui.ContentPane );
                    else
                        //browserTabbedPane.add(mmatrixGui.ContentPane,2);
                        browserTabbedPane.setComponentAt( 2,mmatrixGui.ContentPane );
                    break;
                    case 1:
                    //browserTabbedPane.add(jakMatrixGui1.ContentPane,2);
                    browserTabbedPane.setComponentAt( 2,jakMatrixGui1.ContentPane );
                    break;
                    case 2:
                    //browserTabbedPane.add(jakMatrixGui2.ContentPane,2);
                    browserTabbedPane.setComponentAt( 2,jakMatrixGui2.ContentPane );
			   } // end if
                browserTabbedPane.setTitleAt( 2,"Navigator" );
                browserTabbedPane.setSelectedIndex( 2 );
                browserTabbedPane.fire(); /* DSB added */

                matrixIndex--;
                if ( ( matrixIndex==-1 )|| ( matrixIndex==0 && rmatrixGui==null ) )
                    backButton.setEnabled( false );
                forwardButton.setEnabled( true );
            } // end if
        } // end actionPerformed
    } // end BackAction

    class UpAction extends AbstractAction {
        // constructors
        public UpAction() {
                                    super( "Up" );
            setEnabled( true );
        } // end constructor UpAction

        // instance methods
        public void actionPerformed( ActionEvent e ) {
            /* DSB -- replaced remove(2) add...2 with setComponentAt(2,...) */

            //browserTabbedPane.remove(2);
            if ( subMatrixGui.subMatrix.getParent()!=null ) {
                subMatrixGui.subMatrix=subMatrixGui.subMatrix.getParent();
                subMatrixGui.isSub=true;
                subMatrixGui=new MMatrixGui( Main.this );
                subMatrixGui.isSub=false;
                //browserTabbedPane.add(subMatrixGui.ContentPane,2);
                browserTabbedPane.setComponentAt( 2,subMatrixGui.ContentPane );
                browserTabbedPane.setTitleAt( 2,"Navigator" );
                browserTabbedPane.setSelectedIndex( 2 );
            }
            else {
                if ( matrixIndex==-1 )
                    browserTabbedPane.add( rmatrixGui.ContentPane,2 );
                else {
                    browserTabbedPane.add( mmatrixGui.ContentPane,2 );
                    matrixIndex=0;
                }
                browserTabbedPane.setTitleAt( 2,"Navigator" );
                browserTabbedPane.setSelectedIndex( 2 );
                upButton.setEnabled( false );
                isSub=false;
            }
            browserTabbedPane.fire(); /* DSB */

            if ( ( matrixIndex==-1 )|| ( matrixIndex==0 && rmatrixGui==null ) )
                backButton.setEnabled( false );
            if ( highestIndex>matrixIndex )
                forwardButton.setEnabled( true );
        } // end actionPerformed
    } // end BackAction

    class ForwardAction extends AbstractAction {
        // constructors
        public ForwardAction() {
                                    super( "Forward" );
            setEnabled( true );
        } // end constructor ForwardAction

        // instance methods
        public void actionPerformed( ActionEvent e ) {
            if ( ( matrixIndex+1 )<3 ) {
                /* DSB -- replaced remove then add by setComponentAt */
                             // browserTabbedPane.remove(2);
                switch ( matrixIndex+1 )
                {
                    case 0:
                    //browserTabbedPane.add(mmatrixGui.ContentPane,2);
                    browserTabbedPane.setComponentAt( 2,mmatrixGui.ContentPane );
                    break;
                    case 1:
                    //browserTabbedPane.add(jakMatrixGui1.ContentPane,2);
                    browserTabbedPane.setComponentAt( 2,jakMatrixGui1.ContentPane );
                    break;
                    case 2:
                    //browserTabbedPane.add(jakMatrixGui2.ContentPane,2);
                    browserTabbedPane.setComponentAt( 2,jakMatrixGui2.ContentPane );
		 	   }
                browserTabbedPane.setTitleAt( 2,"Navigator" );
                browserTabbedPane.setSelectedIndex( 2 );
                browserTabbedPane.fire(); /* DSB */

                matrixIndex++;
                if ( matrixIndex==highestIndex )
                    forwardButton.setEnabled( false );
                backButton.setEnabled( true );
            }
        }
    } // end ForwardAction

    class CheckAction extends AbstractAction {
        // constructors
        public CheckAction() {
                                    super( "Check" );
            setEnabled( true );
        } // end constructor CheckAction

        // instance methods
        public void actionPerformed( ActionEvent e ) {
            if ( mmatrixGui!=null ) {
                status.setText( "Status: Running the error checker........" );
                status.setForeground( Color.magenta );
                switch ( matrixIndex )
                {
                    case -1:
                    rmatrixGui.checker();
                    break;
                    case 0:
                    mmatrixGui.checker();
                    break;
                    case 1:
                    jakMatrixGui1.checker();
                    break;
                    case 2:
                    jakMatrixGui2.checker();
				}
                status.setText( "Status: Finished running the error checker." );
                status.setForeground( editorTitle.getForeground() );

            }
            else {
                JOptionPane.showMessageDialog( null,
                                                                                                                                                                                                "Empty Matrix! Please loading matrix first!",
                                                                                                                                                                                                "Error",
                                                                                                                                                                                                JOptionPane.ERROR_MESSAGE );
            }
        }
    } // end CheckAction

    //checker are called by the tool panel
    public void checker() {

        if ( matrixIndex!=0 )
            setMatrixIndexToZero();
        mmatrixGui.checker();

    }

    public void setMatrixIndexToZero() {
        /* DSB -- remove, add replaced by setComponentAt */
        //browserTabbedPane.remove(2);
        //browserTabbedPane.add(mmatrixGui.ContentPane,2);
        browserTabbedPane.setComponentAt( 2,mmatrixGui.ContentPane );

        matrixIndex=0;
        browserTabbedPane.setTitleAt( 2,"Navigator" );
        browserTabbedPane.setSelectedIndex( 2 );
        browserTabbedPane.fire(); /* DSB */

        upButton.setEnabled( false );
        isSub=false;
        if ( rmatrixGui==null )
            backButton.setEnabled( false );
        else
            backButton.setEnabled( true );
        if ( highestIndex>matrixIndex )
            forwardButton.setEnabled( true );
    }

    class ReduceAction extends AbstractAction {
        // constructors
        public ReduceAction() {
                                    super( "Reduce" );
            setEnabled( true );
        } // end constructor ReduceAction

        // instance methods
        public void actionPerformed( ActionEvent e ) {
            if( mmatrixGui!=null ) {
                if ( cDialog==null ) {
                    cDialog = new CustomDialog( Main.this,Main.this );
                    cDialog.setLocationRelativeTo( Main.this );
                    cDialog.setSize( 250,170 );
                    cDialog.setVisible( true ); //show();
                }
                else {
                    cDialog.setVisible( true ); // show();
                }
            }
            else {
                JOptionPane.showMessageDialog( null,
                                                                                                                                                                                 "Empty Matrix! Please loading matrix first!",
                                                                                                                                                                                 "Error",
                                                                                                                                                                                 JOptionPane.ERROR_MESSAGE );

            }
        }
    } // end ReduceAction

    /** Close window listener
     */
    class ExitActionListener implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            //saveState();
            //openedModel.save();
            codeEditor.close();
            System.exit( 0 );
        }
    } // end ExitActionListener

    //function to set all the fonts in the gui
    public void setFont( String fontName, int size ) {
        font = new Font( fontName,Font.PLAIN,size );
        activeEq.setFont( font );
        editorTitle.setFont( font );
        editorTitle2.setFont( font );
        editor.setFont( font );
        codeEditor.setFont( font );
        treeBrowser.setFont( font );
        browserTabbedPane.setFont( font );
        editorTabbedPane.setFont( font );
        status.setFont( font );
        //outputTitle.setFont(font);
        output.setFont( font );
        qbrowserGui.setFont( font );
        toolsPanel.setFont( font );
        guidslPanel.setFont( font );
        equationPanel.setFont( font );
        if ( mmatrixGui!=null ) {
            mmatrixGui.setFont( font );
        }
        if ( rmatrixGui!=null ) {
            rmatrixGui.setFont( font );
        }
        if ( jakMatrixGui1!=null ) {
            jakMatrixGui1.setFont( font );
        }
        if ( jakMatrixGui2!=null ) {
            jakMatrixGui2.setFont( font );
        }
        if ( subMatrixGui!=null ) {
            subMatrixGui.setFont( font );
        }
    }

    //set the temporary global filter
    public static void setFilter( ModelFilter filter ) {
        if ( tmpFileFilter!=null ) {
            for ( int i=0; i<tmpFileFilter.size(); i++ ) {
                filter.files.add( tmpFileFilter.get( i ) );
            }
        }
        if ( tmpDirFilter!=null ) {
            for ( int i=0; i<tmpDirFilter.size(); i++ ) {
                filter.dirs.add( tmpDirFilter.get( i ) );
            }
        }
    }

    //create the tools panel
    public ToolsPanel createToolsPanel() {

        File file = tryToolsFile( new File( modelDir ) ) ;

        if ( file == null ) {
            File homeDir = new File( System.getProperty( "user.home" ) ) ;
            file = tryToolsFile( homeDir ) ;
        }

        if ( file == null )
            file = tryToolsFile( new File( ahead_home, "lib" ) ) ;

        if ( file == null )
            return new DefaultToolsPanel( this ) ;

        AntToolsPanel.buildFile = file;
        return new AntToolsPanel( this ) ;
    }

    private static File tryToolsFile( File directory ) {
        File file = new File( directory, TOOLS_XML ) ;
        return file.isFile() ? file : null ;
    }

    final public static String TOOLS_XML = "ModelExplorer.xml" ;

}
