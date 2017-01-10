/** DOCGUI.java
 * JDoc project 
 * University of Texas at Austin
 * Computer Science Department
 * Product-Line Architecture Research Group
 * @author Roberto E. Lopez-Herrejon (rlopez@cs.utexas.edu)
 * @since October 15, 2001
 * @version 1.0
*/

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Integer;

public class DOCGUI extends JFrame {	
 
  // File variables
  public Writer outFile;       // File handler for writing
  public Reader inputFile1, inputFile2;
  public int ch;  
  public String currentFileName, outputFileName;
  public static final String DefaultFileName = "None"; 
     
   // Graphic variables 
   int numberLayers = 0;
   String baliEquation =" JDoc.Java";        // parser
   String docletEquation = " javadoc ( )";      // docletJTS class
   String docletImplEquation = " javadoclet ( )";  // docletImpl
   
   // Validation variables
   public static int JAVA = 0;
   public static int LAYERS = 1;
   public static int STATES = 2;
   
   public static int TOTALLAYERS = 3;
   String layersNames[] = { "JDoc.Java", "JDoc.layers","JDoc.sm"};
   String docletImplNames[] = { "javaDoclet", "layersDoclet", "smDoclet"};
   String docletNames[] = { "javaDoc", "layersDoc", "smDoc"};
   String stringsArray[]  = {"JDoc.Java","",""};
   int currentSpec[] = new int[TOTALLAYERS];
   LinkedList listOfFiles = new LinkedList();
						  
   // Initial values sets java layer to true always
   boolean selectedButtons[] = {true, false, false};
   
   
 // *************************************************************
 // GUI Implementation
 // *************************************************************
   
   // Atomic Component declarations
   JTextField   typeEquationField, outputDirectoryField;
   JLabel       typeEquationLabel, outputDirectoryLabel;
   JLabel       layersLabel, selectedFilesLabel;
   JCheckBox    javaCheckBox, layersCheckBox, statesCheckBox;
   JButton      addFileButton, removeFileButton, exitButton, createButton;
   JButton      generateButton, clearButton;
   JFileChooser chooserOutput;
   JList        fileList;
   DefaultListModel fileModel;
   
   // Initializes the values of the atomic components
   public void initAtoms() {
	   	  
      // Creates the TextFields
      typeEquationField = new JTextField(20);
	  typeEquationField.setEditable(false);
	  typeEquationField.setText("JDoc.Java");
	  
	  outputDirectoryField   = new JTextField(30);
	  outputDirectoryField.setText(".");
	  
	  // TextFields ToolTips
	  typeEquationField.setToolTipText("Current Type Equation");
	  typeEquationField.setBackground(Color.white);
	  typeEquationField.setBackground(Color.white);

	  outputDirectoryField.setToolTipText("Output directory for documentation");
	  
      // Creates the Labels
	  typeEquationLabel = new JLabel("Type Equation");
	  outputDirectoryLabel = new JLabel("Output Directory ");
	  layersLabel = new JLabel("Layers Selected");
	  selectedFilesLabel = new JLabel ("Selected Files");
	  
	  // Labels ToolTips
	  typeEquationLabel.setToolTipText("Current Type Equation");
	  outputDirectoryLabel.setToolTipText("Output directory for documentation");
	  selectedFilesLabel.setToolTipText("List of selected files for documentation");
		  
      // Creates the Buttons
      addFileButton = new JButton("Add File");
      addFileButton.setToolTipText("Adds a file from the generation list");
	  removeFileButton = new JButton("Remove File");
	  removeFileButton.setToolTipText("Removes selected file from the generation list");
	  exitButton = new JButton("Exit");
	  exitButton.setToolTipText("Finishes execution");
	  createButton = new JButton("Compose");
	  createButton.setToolTipText("Composes selected layers");
	  generateButton = new JButton("Generate");
	  generateButton.setToolTipText("Generate documentation for selected files");
	  clearButton = new JButton("Clear Files");
	  clearButton.setToolTipText("Clears the list of selcted files");
	  
	  // Layers
	  javaCheckBox = new JCheckBox("Java");
	  javaCheckBox.setSelected(true);
	  javaCheckBox.setToolTipText("Default Java layer");
	  javaCheckBox.setEnabled(false);
	  
	  layersCheckBox = new JCheckBox("Layers");
	  layersCheckBox.setSelected(false);
	  layersCheckBox.setToolTipText("Adds layers");
	  
	  statesCheckBox = new JCheckBox("States");
	  statesCheckBox.setSelected(false);
	  statesCheckBox.setToolTipText("Adds state diagrams");
	  	  
	  // File choosers
	  chooserOutput = new JFileChooser("");
	  chooserOutput.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	  
	  // File Model
	  fileModel = new DefaultListModel();
	  
	  // List of files
	  fileList = new JList(fileModel);
	  fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  fileList.setVisibleRowCount(5);
   }

   // Layout Component Definitions
   JPanel layersPanel, typeEquationPanel, firstLinePanel;
   JPanel fileButtonsPanel;   
   JScrollPane fileListScrollPane;
   JPanel wholePanel;
   
   // Initialize component hierarchy
   public void initLayout() {
	   
   // FirstLine Panel elements
   // layers Panel
   layersPanel = new JPanel();
   layersPanel.setLayout(new BoxLayout(layersPanel,BoxLayout.Y_AXIS));
   layersPanel.setBorder(BorderFactory.createEmptyBorder());
   layersPanel.add(layersLabel);
   layersPanel.add(javaCheckBox);
   layersPanel.add(layersCheckBox);
   layersPanel.add(statesCheckBox);
   
   // type equation panel
   typeEquationPanel = new JPanel();
   typeEquationPanel.setLayout(new BoxLayout(typeEquationPanel,BoxLayout.Y_AXIS));
   typeEquationPanel.setBorder(BorderFactory.createEmptyBorder());
   typeEquationPanel.add(typeEquationLabel);
   typeEquationPanel.add(typeEquationField);
   typeEquationPanel.add(outputDirectoryLabel);
   typeEquationPanel.add(outputDirectoryField);
  
   // JPanel fileButtonsPanel; 
   fileButtonsPanel = new JPanel();
   fileButtonsPanel.setLayout(new GridLayout(3,2)); // originally 0,1
   fileButtonsPanel.setBorder(BorderFactory.createEmptyBorder());
   fileButtonsPanel.add(createButton);
   fileButtonsPanel.add(generateButton);
   fileButtonsPanel.add(addFileButton);
   fileButtonsPanel.add(removeFileButton);
   fileButtonsPanel.add(clearButton);
   fileButtonsPanel.add(exitButton);
   
   // First Line Panel
   firstLinePanel = new JPanel();
   firstLinePanel.setLayout(new BoxLayout(firstLinePanel,BoxLayout.X_AXIS));
   firstLinePanel.setBorder(BorderFactory.createEmptyBorder());
   firstLinePanel.add(layersPanel);
   firstLinePanel.add(typeEquationPanel);
   firstLinePanel.add(fileButtonsPanel); 
   
   // Second Line Panel  
   // JScrollPane fileListScrollPane;
   fileListScrollPane = new JScrollPane(fileList);
   
   // Second Line Panel
   wholePanel = new JPanel();
   wholePanel.setLayout(new BoxLayout(wholePanel,BoxLayout.Y_AXIS));
   wholePanel.setBorder(BorderFactory.createEmptyBorder());
   wholePanel.add(firstLinePanel);
   wholePanel.add(selectedFilesLabel);
   wholePanel.add(fileListScrollPane);
    
     
   } // end of init layout
   
   public JPanel ContentPane;

   public void initContentPane() {
     ContentPane = new JPanel();
	 ContentPane.setLayout(new BoxLayout(ContentPane,BoxLayout.X_AXIS));
     // ContentPane.setLayout(new GridLayout(0,1));
	 ContentPane.add(wholePanel);
   }

   public void initListeners() {
	
       addFileButton.addActionListener( new ActionListener() 
       {
			public void actionPerformed(ActionEvent e) {
			 int returnVal=0;
             returnVal = chooserOutput.showOpenDialog(DOCGUI.this);

		     if (returnVal == JFileChooser.APPROVE_OPTION) 
		       {
		 	      File Inputfile = chooserOutput.getSelectedFile();
			      String Name = Inputfile.getName();
			
			      if (Name.equals(""))
			      { // if the chosen name is the default you have to change it
				    JOptionPane.showMessageDialog(null, "Wrong File Name ","Error!", 
											  JOptionPane.ERROR_MESSAGE);
					return;
			      };
		         
			      currentFileName = Inputfile.getPath();
				  
				  // if it is already selected then it cannot be duplicated
				  if (fileModel.lastIndexOf(currentFileName) != -1)
				  {
				    JOptionPane.showMessageDialog(null, "File Name already selected","Error!", 
					  JOptionPane.ERROR_MESSAGE);  
					return;
				  }
				  
				  fileModel.addElement(currentFileName);
				  listOfFiles.add(Name);
		      };					
			   
			} // of actionPerformed
       });   
	   
 
	  removeFileButton.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
	         int[] itemsSelected = fileList.getSelectedIndices();
			 for(int i=0; i< itemsSelected.length; i++)
			 { 
				 fileModel.removeElementAt(itemsSelected[i]);
				 listOfFiles.remove(itemsSelected[i]);
			 } // for all the items selected
			}
	    });
	
      exitButton.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
             System.exit(0);
			}
	    });
		   
     clearButton.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
			 fileModel.removeAllElements();
			 listOfFiles.clear();
			}
	    });
		   
      createButton.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
			 updateSpecs();	
			 executeCreation();
			}
	    }); 
	  
	   generateButton.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
            Object[] files = fileModel.toArray();
			
			String direct = outputDirectoryField.getText();
			
			File src = new File(direct);
			
			// if directory does not exits then create it
			if (src.isDirectory() == false ) 
			{	
			  int answer = JOptionPane.CLOSED_OPTION;
			  do
			  {
				 answer = confirmationMessages("Do you want to create the directory?",
										  "Output Directory");
			  }while (answer != JOptionPane.YES_OPTION && 
					  answer != JOptionPane.NO_OPTION);
			
			  if (answer == JOptionPane.NO_OPTION) 
			  {
				informationMessages("No documentation generated",
									"Output Directory");  			 
			    return;
			  }
			  
			  // tries to create the directory
			  boolean result = src.mkdirs();
		      if (result == false) 
			  { 
				  errorMessages("Directory " + direct + " cannot be created !");
				  return;
			  }
			} // if such directory already does not exits
						
			for (int i=0; i< files.length ;  i++)
			{
				String fileDoc = (String) files[i];
				generateDoc(fileDoc, direct);
			}
			informationMessages("Generation Done", "Execution");

			} // of actionPerformed
	    }); 
	  
	  
		// layers buttons
		javaCheckBox.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
			}
	    });
		
		layersCheckBox.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
			 selectedButtons[LAYERS] = ! selectedButtons[LAYERS];
			 updateSpecs();
			}
	    }); 
		
		statesCheckBox.addActionListener( new ActionListener() 
	    {
			public void actionPerformed(ActionEvent e) {
			 selectedButtons[STATES] = ! selectedButtons[STATES];
		     updateSpecs();
			}
	    }); 
			
	    
	   
   } // of initListeners	
   
   // Updates the graphical interface, the typeEquation, and signals
   // any composition error
   public void updateSpecs()
   {
	   int i=0;
	   
	   // Based on the status starts the compositon 
	   numberLayers = 0;
	   String newEquation = "";
	   baliEquation = "";
	   docletEquation = "";
	   docletImplEquation = "";
	   
	   for(i=0; i< TOTALLAYERS; i++)
	    if (selectedButtons[i])
		{
			currentSpec[numberLayers] = i;
			stringsArray[numberLayers] = layersNames[i];
			if (i==JAVA)
			{
				newEquation = layersNames[i];
				baliEquation = layersNames[i];
				
			}
			else
			{
			  newEquation = layersNames[i] + "[ " + newEquation + " ]";
			  baliEquation = layersNames[i] + "[" + baliEquation + "]";
			}
			
			docletImplEquation = docletImplNames[i] + " ( " + 
								   docletImplEquation + " ) ";
  			docletEquation = docletNames[i] + " ( " + 
								   docletEquation + " ) ";			
			numberLayers++;
		} // for all the selected layers
	   
	   // Displays the new Equation
	   typeEquationField.setText(newEquation);
	   
   } // end of updateSpecs
   
   // Does the composition
   public void executeCreation()	   
   {       
	   // Writes type equation for doclet
       // typeEquation DOCLET DOCLETJTS.Lang = layersDoc ( javaDoc ( ) ) ;	   
	   try {
		writeTypeEquation("JDoc","DOCLETJTS",docletEquation, "buildDoclet.jak");        
	   } catch(IOException e1)
	   {
		   errorMessages("Writing Doclet JTS Equation aborted" + e1.getMessage());
		   return;
	   }
	   
	   // Writes type equation for docletImpl
	   // typeEquation DOCLETImpl DocletImpl.Lang = layersDoclet ( javaDoclet ( ) );	
	   try {
		writeTypeEquation("JDoc","DocletImpl",docletImplEquation, "buildDocletImpl.jak");        
	   } catch(IOException e1)
	   {
		   errorMessages("Writing Doclet Impl Equation aborted" + e1.getMessage());
		   return;
	   }
	   
	   // Writes the equation to generate the bali parser
	   try {
	   writeBaliEquation();
	   } catch(IOException e2)
	   {
		   errorMessages("Writing Bali Equation aborted" + e2.getMessage());
		   return;
	   }
	   
	   // Executes the composition based on the generated files
	   executeComposition();
	   
   } // end of executeCreation
   
   
   /** Displays the error Messages in a dialog box
    */
   public void errorMessages (String theMessage)
   {
	   JOptionPane.showMessageDialog(this,theMessage, "Error!",
										  JOptionPane.ERROR_MESSAGE);
   }

   /** Displays an information message in a dialog box
    */
   public void informationMessages(String theMessage, String typeMessage)
   {
	   JOptionPane.showMessageDialog(this, theMessage, typeMessage, 
						  JOptionPane.INFORMATION_MESSAGE);	
   }
   
   /** Displays a confirmation message and returns the response.
    */
   public int confirmationMessages(String theMessage, String typeMessage)
   {
	  return JOptionPane.showConfirmDialog(this, theMessage, typeMessage,
										  JOptionPane.YES_NO_OPTION);
   }
   
   /** Executes the composition based on the jak and parser files.
    */
   public void executeComposition()
   {
	   // Creates a process to compose the equation and compile them
       try {
           
		   String[] cmdArray0 = {"doclet.bat"};
		   String[] cmdArray1 = {"compileme.bat" }; // compiles jtsdoc
		   // String[] cmdArray1 = {"java","JakBasic.Main","buildFile.jak"};
           String[] cmdArray2 = {"docletImpl.bat"};
		   String[] cmdArray3 = {"runApp.bat"};
		   
		   // Creates DOCLETJTS
		   System.out.println("DOCLETJTS started !!!");
   		   Process child = Runtime.getRuntime().exec(cmdArray0); 
		 
   		   InputStream lsOut0 = child.getInputStream();
		   InputStreamReader r0 = new InputStreamReader(lsOut0);
		   BufferedReader in0 = new BufferedReader(r0);
		   String line0;
		   while((line0 = in0.readLine()) != null) 
			   System.out.println(line0);
           if (child.waitFor() != 0)
		   {
	         errorMessages("Error while creating DOCLETJTS");
			 return;
		   }           
			 
		   // Compiles jtsdoc -> temporary
		   child = Runtime.getRuntime().exec(cmdArray1); 

  		   InputStream lsOut1 = child.getInputStream();
		   InputStreamReader r1 = new InputStreamReader(lsOut1);
		   BufferedReader in1 = new BufferedReader(r1);
		   String line1;
		   while((line1 = in1.readLine()) != null) 
			   System.out.println(line1);
           if (child.waitFor() != 0)
		   {
	         errorMessages("Error while compiling jtsdoc");
			 return;
		   }
		   
		  // Compiles the doclet implementation
		  child = Runtime.getRuntime().exec(cmdArray2); 

  		  InputStream lsOut2 = child.getInputStream();
		  InputStreamReader r2 = new InputStreamReader(lsOut2);
		  BufferedReader in2 = new BufferedReader(r2);		  
		  String line2;
		  while((line2 = in2.readLine()) != null) 
			   System.out.println(line2);
          if (child.waitFor() != 0)
		   {
	         errorMessages("Error occurred in doclet implementation");
			 return;
		   }
		  System.out.println("Doclet Implementation compiled");
		  
		  // Creates the new composed bali parser
		  child = Runtime.getRuntime().exec(cmdArray3); 
  
 		  InputStream lsOut3 = child.getInputStream();
		  InputStreamReader r3 = new InputStreamReader(lsOut3);
		  BufferedReader in3 = new BufferedReader(r3);
		  String line3;
		  while((line3 = in3.readLine()) != null) 
			   System.out.println(line3);
          if (child.waitFor() != 0)
		   {
	         errorMessages("Error occurred in application composition");
			 return;
		   }		
		  System.out.println("Bali composition done");
		  
      }catch(Exception e)
      {
         errorMessages("Error ocurred in Composition");
	   }
	 
   } // of writeComposition
   
   
   // Generates the documentation for a file
   public void generateDoc(String fileDoc, String directory)
   {
   // Creates a process to compose the equation and compile them
       try {
           
		   String[] cmdArray = {"java","theParser.Main", fileDoc, directory};
		   
		   // Creates DOCLETJTS
		   System.out.println("Generating " + fileDoc + " !!!");
   		   Process child = Runtime.getRuntime().exec(cmdArray); 
		 
   		   InputStream lsOut0 = child.getInputStream();
		   InputStreamReader r0 = new InputStreamReader(lsOut0);
		   BufferedReader in0 = new BufferedReader(r0);
		   String line0;
		   while((line0 = in0.readLine()) != null) 
			   System.out.println(line0);
           if (child.waitFor() != 0)
		   {
	         errorMessages("Error while generating doc");
			 return;
		   }           
		  
      }catch(Exception e)
      {
         errorMessages("Error in generation of file " + fileDoc);
	   }	   
   }
   
   // Write the file with the type Equation 
   public void writeTypeEquation(String theRealm, String theLanguage,
								  String theEquation, String fileName) 
	   throws IOException
   {
	try {
		outFile = new FileWriter(fileName);
	} catch (IOException e)
	{
		JOptionPane.showMessageDialog(this,
   	       "File Cannot be written" + e.getMessage(),
		   "Error!",
		   JOptionPane.ERROR_MESSAGE);
		return;
	}

    outFile.write("typeEquation " + theRealm + " " + theLanguage 
				  + ".Lang = " + theEquation + " ; \n");
	
	// closes the file
	outFile.close();
				
   } // of writeTypeEquation

   
  // Write the file with the type Equation 
   public void writeBaliEquation() 
	   throws IOException
   {
	try {
		outFile = new FileWriter("runApp.bat");
	} catch (IOException e)
	{
		JOptionPane.showMessageDialog(this,
   	       "File Cannot be written" + e.getMessage(),
		   "Error!",
		   JOptionPane.ERROR_MESSAGE);
		return;
	}
	
	// delete the contents of theParser to prevent any side effect
	// del .\theParser\*.* in a quite mode
	outFile.write("del .\\theParser\\*.* /Q\n");
	
    // java Bali.Main "theParser=JDoc.layers[JDoc.Java]"
    outFile.write("java Bali.Main \"theParser=" + baliEquation + "\"" + "\n");
	
	// cd theParser
	outFile.write("cd theParser \n");
	
	// javac Build.java
	outFile.write("javac Build.java \n");
	
	// java theParser.Build
	outFile.write("java theParser.Build \n");
	
	// cd ..
	outFile.write("cd ..\n");
	
	// closes the file
	outFile.close();
				
   } // of writeTypeEquation    
   
   // ***********************************************************
   // GUI initialization
   // ***********************************************************
   
   // initialize entire containment hierarchy
   public void init() {
      initAtoms();                       // initialize atoms
      initLayout();                      // initialize layout
      initContentPane();                 // initialize content pane
      getContentPane().add(ContentPane); // set ContentPane of window
      initListeners();                   // initialize listeners
   }

   public DOCGUI() { super(); }

   public DOCGUI(String AppTitle ) 
   { 
      super( AppTitle );	         // set title
      init();                        // initialize hierarchy
      addWindowListener(	         // standard code to kill window 
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
      pack();                            // start window by packing
      setVisible(true);                  // and making visible

   }

   public static void main(String[] args) { 
      new DOCGUI("JDoc"); 
   }
} // of DOCGUI




