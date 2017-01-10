/* ******************************************************************
   class      : AntToolOptions
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

public class AntToolOptions extends JDialog {

	 ToolsFromAnt tools;
	
	 JTabbedPane myPanel;
	 
	 //Atomics
	 JPanel[]	panels;
	 Vector     labels;
	 Vector     fields;
	 //boolean[][] isCombos;
	
	 //bottom Panel
	 JPanel      bottomPanel;
	 JButton     okButton;
	 
	 public JLabel[] getLabels(int index){
		 return (JLabel[])labels.get(index);
	 }
	 
	 public Vector getFields(int index){
		 return (Vector)fields.get(index);
	 }
	 
	 public HashMap getNewHM(HashMap hm){
		Vector tmpVec;
		HashMap result=new HashMap();
		Iterator it=hm.keySet().iterator();
		String key;
		String newKey;
		while(it.hasNext()){
			key=(String)it.next();
			if (key.endsWith("@more")){
				newKey= key.substring(0, key.length()-5);
				if (result.containsKey(newKey)){
					tmpVec=(Vector)result.get(newKey);
				}
				else{
					tmpVec=new Vector();
					result.put(newKey,tmpVec);
				}
				tmpVec.addAll(parseValues((String)hm.get(key)));
			}
			else{
				if (result.containsKey(key)){
					tmpVec=(Vector)result.get(key);
				}
				else{
					tmpVec=new Vector();
					result.put(key,tmpVec);
				}
				tmpVec.add((String)hm.get(key));
			}
		}
		return result;
	 }
	 
	 public Vector parseValues(String str){
		 Vector result = new Vector();
       StringTokenizer st = new StringTokenizer(str, ";");
       while (st.hasMoreTokens()) {
          result.add(st.nextToken());
       }
		 return result;
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
	
	private void initPanel(){
		//init tabbed panel
		myPanel=new JTabbedPane();
		myPanel.setBorder( BorderFactory.createEtchedBorder());
		
		//init panel
		Vector args = tools.getTargetArgs();
		Vector names = tools.getNames();
		panels = new JPanel[names.size()];
		HashMap hm;
		HashMap oldHM;
		Object obj;
		JPanel hp;
		JPanel vp;
		fields = new Vector();
	   labels = new Vector();
		Vector tmpVec;
		JLabel[] tmpLabels;
		JTextField tmpField;
		JComboBox  tmpBox;
		for (int i=0; i<names.size(); i++){
			obj = args.get(i);
			if (obj!=null){
				panels[i]=new JPanel();
			   panels[i].setLayout(new BorderLayout());
			   panels[i].setBorder( BorderFactory.createEtchedBorder());
				vp = initPanel(false);
				oldHM = (HashMap)obj;
				hm = getNewHM(oldHM);
				tmpVec = new Vector();
				tmpLabels = new JLabel[hm.size()];
				Iterator it=hm.keySet().iterator();
				String key;
				int k=0;
				while(it.hasNext()){
					key=(String)it.next();
					hp = initPanel(true);
					tmpLabels[k]=new JLabel(key);
					hp.add(tmpLabels[k]);
					Vector v=(Vector)hm.get(key);
					if (v.size()==1){
						tmpField = new JTextField(20);
						tmpField.setText((String)v.get(0));
						tmpVec.add(tmpField);
						hp.add(tmpField);
					}
					else{
						tmpBox = new JComboBox(v);
						tmpBox.setSelectedItem(oldHM.get(key));
						tmpBox.setBackground(Color.white);
						tmpVec.add(tmpBox);
						hp.add(tmpBox);
					}
					vp.add(hp);
					k++;
				}
				fields.add(tmpVec);
				labels.add(tmpLabels);
				panels[i].add(vp, BorderLayout.NORTH);
			   myPanel.addTab((String)names.get(i), null, panels[i]);
			}
			else{
				panels[i]=null;
				fields.add(null);
				labels.add(null);
			}
		}
		
	}
			
	public JPanel ContentPane;
   public void initContentPane() {
		//init bottom panel
		okButton = new JButton("   Ok   ");
		okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		bottomPanel = initPanel(true);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(6,0,0,0));
		bottomPanel.add(okButton);
		bottomPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
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
		initPanel();
      initContentPane();                 // initialize content pane
      getContentPane().add(ContentPane); // set ContentPane of window
      initListeners();                   // initialize listeners
   }

   public AntToolOptions(Main me, ToolsFromAnt tools) {
      super( me, "Tool Options" );	         // set title
		this.tools = tools;
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
