/* ******************************************************************
   class      : ToolOptions
*********************************************************************/

package ModelExplorer.Composer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import ModelExplorer.Main;

public class ToolOptions extends JDialog {

	 JTabbedPane myPanel;
	 //First Panel: composer tool options
	 JPanel		 composerPanel;  //First tab panel
	 JLabel		 toolLabel;
	 public JComboBox	 toolBox;
	
	 //Second Panel: reform tool options	 
	 JPanel      reformPanel; //Second tab panel
	
	 //Third Panel: unmixin tool options
	 JPanel      unmixinPanel;//third tab panel
	 
	 //Fourth Panel: J2J tool options
	 JPanel		j2jPanel;
	 
	 //Fifth Panel: build tool options
	 JPanel		buildPanel;
	
	 //bottom Panel
	 JPanel      bottomPanel;
	 JButton     okButton;
	 
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
	
	private void initComposerPanel(){
		 //init atomics
		toolLabel = new JLabel("Value for unit.file.jak: ");
		String[] value={"JamPackFileUnit", "MixinFileUnit"};
		toolBox = new JComboBox(value);
		toolBox.setSelectedIndex(0);
		toolBox.setBackground(Color.white);
		 //init layout
		JPanel p=initPanel(true);
		p.add(toolLabel);
		p.add(toolBox);
		
		composerPanel=new JPanel();
		composerPanel.setLayout( new BorderLayout() );
      composerPanel.setBorder( BorderFactory.createEtchedBorder());
      composerPanel.add(p, BorderLayout.NORTH);
	 }
	
	 private void initReformPanel(){
		 reformPanel=initPanel(false);
	 }
	 
	 private void initUnmixinPanel(){
		 unmixinPanel=initPanel(false);
	 }
	 
	 private void initJ2JPanel(){
		 j2jPanel=initPanel(false);
	 }
	 
	 private void initBuildPanel(){
		 buildPanel=initPanel(false);
	 }
	 
	 public void initTabbedPane(){
		 initComposerPanel();
		 //initReformPanel();
		 //initUnmixinPanel();
		 myPanel=new JTabbedPane();
		 myPanel.setBorder( BorderFactory.createEtchedBorder());
		 myPanel.addTab("Composer Options", null, composerPanel);
	 }
	
	public JPanel ContentPane;
   public void initContentPane() {
		okButton = new JButton("   Ok   ");
		okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		bottomPanel = initPanel(true);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(6,0,0,0));
		bottomPanel.add(okButton);
		bottomPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		/*ContentPane = new JPanel();
      ContentPane.setLayout( new BorderLayout() );
      ContentPane.setBorder( BorderFactory.createEmptyBorder(2,//top
                                                             2,//left
                                                             2,//bottom
                                                             2 // right
                                                             ) );
      ContentPane.add(myPanel, BorderLayout.CENTER);
		ContentPane.add(bottomPanel, BorderLayout.SOUTH);*/
		ContentPane = initPanel(false);
		ContentPane.add(myPanel);
		ContentPane.add(bottomPanel);
		
	};
	
	public void initListeners() {
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
	}
	 
	public void init() {
		initTabbedPane();
      initContentPane();                 // initialize content pane
      getContentPane().add(ContentPane); // set ContentPane of window
      initListeners();                   // initialize listeners
   }

   public ToolOptions(Main me) {
      super( me, "Tool Options" );	         // set title
      init();                            // initialize hierarchy
      addWindowListener(	         // standard code to kill window
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });
      //setLocationRelativeTo(me);
		setLocation(200,200);
		setSize(400,250);
		setVisible(true);
   }
}
