/* ******************************************************************
   class      : HelpDialog
*********************************************************************/

package ModelExplorer.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import javax.swing.text.html.*;
import java.net.*;
import ModelExplorer.Main;

public class HelpDialog extends JDialog {
	
    final private static int WIDTH  = 850 ;
    final private static int HEIGHT = 500 ;

    // final private static LOGGER = Logger.getLogger ("ModelExplorer") ;

    private Main	me;
    JToolBar			toolBar;
    JButton			backButton;
    JButton			forwardButton;
    JLabel			backLabel;
    JLabel			forwardLabel;
    JLabel			addressLabel;
    JLabel			status;
    JComboBox		addressBox;
    JEditorPane		editor;
    JScrollPane		scroll; 
    URL					topURL;
    ArrayList			urlList;
    int					curIdx;
    boolean				isEventEnable=false;

    public void initAtomic(){
		
	urlList = new ArrayList();
	curIdx = -1;
		
	status = new JLabel("Status: ");
		
	toolBar = new JToolBar();
	toolBar.setMargin(new Insets(-4,0,-4,0));
	toolBar.setBorder(new EtchedBorder());
		
	backButton = new JButton("");
	backButton.setIcon(me.backIcon);
	//backButton.setToolTipText("Back");
	backButton.setBorderPainted(false);
	backButton.setMargin(new Insets(-4,0,-4,0));
	backButton.setEnabled(false);
	toolBar.add(backButton);
		
	backLabel = new JLabel(" Back   ");
	toolBar.add(backLabel);
		
	forwardButton = new JButton("");
	forwardButton.setIcon(me.forwardIcon);
	//forwardButton.setToolTipText("Forward");
	forwardButton.setBorderPainted(false);
	forwardButton.setMargin(new Insets(-4,0,-4,0));
	forwardButton.setEnabled(false);
	toolBar.add(forwardButton);
		
	forwardLabel = new JLabel(" Forward     ");
	toolBar.add(forwardLabel);
		
	addressBox = new JComboBox();
	addressBox.setPreferredSize (new Dimension (250,25)) ;
	addressBox.setBackground(Color.white);
	//addressBox.setModel(new DefaultComboBoxModel(urlList));
	addressBox.setEditable(false);
	toolBar.add(addressBox);
	addressLabel = new JLabel(" Address  ");
	toolBar.add(addressLabel);
		
	editor = new JEditorPane();
	editor.setEditable(false);
	editor.setFont(new Font("courier", Font.PLAIN,12));
	String filePath;
	topURL = null;
		
	filePath=me.ahead_home+File.separator+"docs"+File.separator+"index.html";
	File file = new File(filePath);
	while (! file.isFile ()) {
	    filePath = JOptionPane.showInputDialog (
		"Document "+filePath+" is not a valid file."
		+ "  Please enter a new value:", filePath
		) ;
	    filePath=filePath.replace('\\', File.separatorChar);
	    filePath=filePath.replace('/', File.separatorChar);
	    file = new File (filePath) ;
	}	
			
	try{ topURL = file.getAbsoluteFile().toURI().toURL () ; }
	catch(Exception e1){
	    me.status.setText("Status: Couldn't create the User Guide URL");
	    return;
	}
	urlList.add(topURL);
	try{
	    //System.out.println ("topURL=\"" + topURL + '"') ;
	    editor.setPage(topURL);
	    curIdx = 0 ;
	    updateAddressBox () ;
	}catch (Exception e2){
	    me.status.setText("Status: Couldn't open the User Guide document.");
	}
	
	scroll = new JScrollPane(editor);
	scroll.setPreferredSize(new Dimension(WIDTH,HEIGHT));
	scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public JPanel ContentPane;
    public void initContentPane() {
	ContentPane = new JPanel();
	ContentPane.setLayout( new BorderLayout() );
	ContentPane.setBorder( BorderFactory.createEmptyBorder(2,//top
							       2,//left
							       2,//bottom
							       2 //right
				   ) );
	ContentPane.add(toolBar, BorderLayout.NORTH);
	ContentPane.add(scroll, BorderLayout.CENTER);
	ContentPane.add(status, BorderLayout.SOUTH);

    };

    public void initListeners() {
	editor.addHyperlinkListener(new HyperlinkListener() {
		public void hyperlinkUpdate(HyperlinkEvent e) {
		    if (e.getEventType() == HyperlinkEvent.EventType.ENTERED){
			status.setText("Status: "+e.getURL());
		    }
		    else if (e.getEventType() == HyperlinkEvent.EventType.EXITED){
			status.setText("Status: ");
		    }
		    else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			JEditorPane pane = (JEditorPane) e.getSource();
			if (e instanceof HTMLFrameHyperlinkEvent) {
			    HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
			    HTMLDocument doc = (HTMLDocument)pane.getDocument();
			    doc.processHTMLFrameHyperlinkEvent(evt);
			} else {
			    try {
				isEventEnable = false;
				pane.setPage(e.getURL());
				int idx = urlList.indexOf (e.getURL());
				if (idx >= 0)
				    urlList.subList(idx,urlList.size()).clear () ;
				urlList.add (e.getURL ()) ;
				curIdx = urlList.size () - 1 ;
				backButton.setEnabled (curIdx > 0) ;
				forwardButton.setEnabled (false) ;
				updateAddressBox();
			    } catch (Throwable t) {
				t.printStackTrace();
			    }
			}
		    }
		}
	    });
		 
	backButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    try{
			//System.out.println("backbutton are clicked.");
			URL url;
			try{
			    //System.out.println(curIdx-1);
			    //System.out.println(urlList.size());
			    //System.out.println(urlList.get(curIdx-1));
			    url = (URL) urlList.get(curIdx-1) ; 
			}catch(Exception e1){
			    status.setText("Status: Invalid URL in back");
			    return;
			}
			isEventEnable = false;
			editor.setPage(url);
			curIdx--;
			addressBox.setSelectedIndex(curIdx);
			isEventEnable = true;
			forwardButton.setEnabled(true);
			backButton.setEnabled (curIdx > 0) ;
		    }catch (Throwable t) {
			t.printStackTrace();
		    }
		}
	    });
		
	forwardButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    try{
			URL url;
			try{
			    url = (URL) urlList.get(curIdx+1) ; 
			}catch(Exception e1){
			    status.setText("Status: Invalid URL in forward");
			    return;
			}
			editor.setPage(url);
			isEventEnable = false;
			curIdx++;
			addressBox.setSelectedIndex(curIdx);
			isEventEnable = true;
			backButton.setEnabled(true);
			forwardButton.setEnabled (curIdx < urlList.size() - 1) ;
		    }catch (Throwable t) {
			t.printStackTrace();
		    }
		}
	    });
		
	addressBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (isEventEnable){
			JComboBox cb = (JComboBox)e.getSource();
			int idx = cb.getSelectedIndex();
			try{
			    URL url;
			    try{
				//System.out.println("selected item:"+cb.getSelectedItem());
				url = (URL) cb.getSelectedItem () ; 
			    }catch(Exception e1){
				status.setText("Status: Invalid URL in address");
				return;
			    }
			    editor.setPage(url);
			    curIdx = idx;
			    backButton.setEnabled (curIdx > 0) ;
			    forwardButton.setEnabled (curIdx < urlList.size() - 1) ;
			}catch (Throwable t) {
			    t.printStackTrace();
			}
		    }
		}
	    });	
		
    }

    public void init() {
	initAtomic();
	initContentPane();                 // initialize content pane
	getContentPane().add(ContentPane); // set ContentPane of window
	initListeners();                   // initialize listeners
    }

    public HelpDialog(Main me) {
	super( me, "User Guide" );	         // set title
	this.me =me;
	init();                            // initialize hierarchy
	addWindowListener(	         // standard code to kill window
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });
	//setLocationRelativeTo(me);
	setLocation(80,80);
	setSize(WIDTH,HEIGHT);
	setVisible(true);
    }
	
    //following is the utility function
    /** reset the url of the navigator to the top url when reopen the help
     * dialog
     */
    public void reset(){
	if (topURL!=null){
	    try{
		editor.setPage(topURL);
	    }catch (Exception e2){
		me.status.setText("Status: Couldn't open the Users' Guide");
	    }
	}
	else
	    me.status.setText("Status: Couldn't create the User Guide URL");
    }

    //Update the address combo box
    public void updateAddressBox(){
	isEventEnable = false;
	addressBox.removeAllItems();
	for (int i=0; i<urlList.size(); i++){
	    addressBox.addItem(urlList.get(i));
	}
	addressBox.setSelectedIndex(curIdx);
	isEventEnable = true;
    }	 
	 
}
