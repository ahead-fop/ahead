
		package DocletImpl; import java.util.*; import
        jtsdoc.*; import  
        com.sun.javadoc.*; import
        java.io.*; import
        java.lang.reflect.*; import
        com.sun.tools.javadoc.*; public class layersDoclet extends javaDoclet  
{
 
 static public class DocletJTS extends javaDoclet.DocletJTS
 {
   
  // *** Pending ... 
  public static boolean start(LayerDocJTS Alayer) {
  String tagName = null; // readOptions(root.options());
  
  /*
  try {
   
  layerFile = createOutFile((Alayer.name()).trim());
  writeHTML("<HTML> <HEAD></HEAD> \n", layerFile);
  writeHTML("<TITLE> Layer " + layer.name() +" </TITLE> \n", layerFile);
  writeHTML("<H1> Layer </H1>", layerFile);
  writeHTML("<BODY> \n", layerFile);
  	  
  // writeContents(layer, layerFile);
  
  writeHTML("</BODY> </HTML> \n", layerFile);
  closeOutFile(layerFile);
  
  } catch(IOException e)
  {
    System.out.println("Error at start");
  }
  
  */
  return true;
 }


 /** Writes up a layer.
 */
 public static void writeALayer(LayerDocJTS Alayer, String _OutputDirectory) 
	throws IOException
 {
 
  // Sets up the directory to write the documentation  
  OutputDirectory = _OutputDirectory;
   
  // Header stuff	 
  Writer layerFile = createOutFile((Alayer.name()).trim());
  
  // writes out the header of the state
  writeLayerHeader(Alayer, layerFile);
  
  // writes out the body set up
  writeLayerBodySetup(Alayer, layerFile);
  
  // writes out the state name
  writeLayerName(Alayer, layerFile);
  
  // writes out the layer relationships
  writeLayerRelationships(Alayer, layerFile);
  
  // writes out the imports list
  writeLayerImports(Alayer, layerFile);
  
  // writes out the description 
  writeLayerDescription(Alayer, layerFile);
  
  // writes out the constraints
  writeLayerConstraints(Alayer, layerFile);
  
  // writes out the exports
  writeLayerExports(Alayer, layerFile);
  
  // writes out the grammar
  writeLayerGrammar(Alayer, layerFile);
  
  // Displaying the Fields
  writeFields((Alayer.classBody()).fields(), layerFile);

  // Displaying the Methods
  writeMethods((Alayer.classBody()).methods(), layerFile); 
 
  // Displaying the Constructors
  writeConstructors((Alayer.classBody()).constructors(), layerFile);

  // Displaying the Interfaces
  // writeInterfaces((Alayer.classBody()).interfaces(), layerFile);
 
   // Displaying the roles of the mixin layer
  writeRoles(Alayer.classBody(), layerFile);
         
  // Here is where the states or any other addend should go
  // In the layer this should actually be $TEqn.appendContents
  Lang.DocletJTS.appendContents(Alayer.classBody(), layerFile);
    
  // *** Footer stuff
  writeLayerFooter(Alayer, layerFile);
  
  closeOutFile(layerFile); 
  
  } // writeContents
 
 // Writes the innerclasses names only and puts the corresponding links.
 public  static void writeInnerclasses(ClassDoc[] theClasses, Writer layerFile) 
	 throws IOException
 {
	for (int i=0; i< theClasses.length ; i++)
	{
		writeHTML("<LI> InnerClass : " + theClasses[i].name() + " </LI>"
				  , layerFile);
		writeAClass(theClasses[i]);
	}
 }
 
 // ***********************************************************************
 // ***********************************************************************
 // ***********************************************************************
 // ***********************************************************************
 // Methods that are defined for a State Document
  public static void writeLayerHeader(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
 {							   
  writeHTML("<HTML> <HEAD></HEAD> \n", layerFile);
  writeHTML("<TITLE> Layer " + Alayer.name() + "  </TITLE> \n", layerFile);
  
  // Creates the table
  writeHTML("  <table border=\"1\" width=\"100%\" height=\"95\" bordercolorlight=\"#3366FF\" bordercolordark=\"#3366FF\" ",layerFile);
  writeHTML(" bordercolor=\"#0000FF\">",layerFile);
  writeHTML(" <tr>", layerFile);
  writeHTML("    <td width=\"47%\" height=\"89\" bgcolor=\"#CCCCFF\" ", layerFile);
  writeHTML(" bordercolorlight=\"#3366FF\"><font size=\"3\">", layerFile);
  
  writeHTML(computeString(Alayer,"@address"), layerFile);
  /*
     <address>
      Graph Algorithms Product Line
      (GPL)<br>
      University of Texas at Austin<br>
      Department of Computer Sciences<br>
      Product-Line Architectures Research Group<br>
      Edge-Neighbor Representation</font>
      </address>
  */
  
   writeHTML("   </td>", layerFile);
   writeHTML(" <td width=\"53%\" height=\"89\" bgcolor=\"#CCCCFF\">", layerFile);
   writeHTML(" <p align=\"left\"><font size=\"3\">", layerFile);
   writeHTML(computeString(Alayer,"@name"), layerFile);
   /* Undirected graph layer
   */
   
   writeHTML("<br> \n", layerFile);
   writeHTML(" <i> Last Update:</i> ", layerFile);
   writeHTML(computeString(Alayer,"@since"), layerFile);
  /*	  
       August 7, 2001
   */ 
   writeHTML(" <br> \n", layerFile);
   
   writeHTML("<i>Author: </i> \n", layerFile);
   writeHTML(computeString(Alayer, "@author"), layerFile);
   /*
      Roberto E. Lopez-Herrejon (rlopez@cs.utexas.edu)
   */ 
   
   writeHTML("</font> \n", layerFile);
   writeHTML("</p>  </td>  </tr> </table> ", layerFile);

 }


  
 
 // *********************************************************************
 public static void writeLayerFooter(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
 {
    writeHTML("</BODY> </HTML> \n", layerFile);
 }
 
 public static void writeLayerBodySetup(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
 {
     writeHTML("<BODY BGCOLOR=\"white\"> \n", layerFile);
 }
 
 
 public static void writeLayerName(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
 {
      writeHTML("<H2> <BR> Layer " + Alayer.name()+ " </H2>", layerFile); 
 }
 
 public static void writeLayerRelationships(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
 {	
	 writeHTML("<DL> <DT>" + Alayer.modifiers() + "layer <B> " + Alayer.name()
			   + "</B>", layerFile);
	 
	 writeHTML("<DT> Realm " + "<B> " + Alayer.realmName()+ "</B>",
			   layerFile);	
	 
	 String parameterString = " ( ";
     ParameterJTS[] arrayParameters = Alayer.parameters();
     for (int i=0; i < arrayParameters.length ; i++)
	 {
	  // writeHTML("<LI> Parameter : " +  +
	  //			 + "</LI>", layerFile);
	  parameterString = parameterString + arrayParameters[i].typeName() + " ";
	  parameterString = parameterString + arrayParameters[i].name();
	  if (i < arrayParameters.length - 1)
	  { parameterString = parameterString + ", "; }
	 } // for al lthe parameters
   
	 parameterString = parameterString + " )";
	 writeHTML("<DT> " + parameterString, layerFile);
	 
	 if (Alayer.superLayer()!= "") 
		 writeHTML("<DT> " + " extends " + Alayer.superLayer(), layerFile);

	 writeHTML("</DL>", layerFile);
 } 
 
 public static void writeLayerImports(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
 {
	String[] importsList = Alayer.importsList();
	if (importsList == null) return;      // there are no imports
	if (importsList.length == 0 ) return;
	
    writeHTML("<BR> \n", layerFile);
    writeHTML("<DL> \n", layerFile);
    writeHTML("<DT> \n", layerFile);

    writeHTML("<B> Imports </B> : ", layerFile);
  
    String importsString = "";
    for (int i=0; i< importsList.length; i++)
    {
	  importsString = importsString + importsList[i];
	  if (i < importsList.length - 1) 
		  importsString = importsString + ", ";
    }
  
    writeHTML(importsString + "</DT> \n", layerFile);
    writeHTML("</DL> \n", layerFile);
    writeHTML("<HR> \n", layerFile);
	
 } // of writeLayer Imports
 
  // writes out the layer tags
  public static void writeLayerDescription(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
  {
	  String description = computeString(Alayer, "@description");
	  writeHTML("<p><b><font size=\"4\">Description</font></b></p>", layerFile);
	  writeHTML(description, layerFile);
  }
 
  public static void writeLayerConstraints(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
  {
	  String constraints = computeString(Alayer, "@constraints");
	  writeHTML("<p><b><font size=\"4\">Constraints</font></b></p>", layerFile);
	  writeHTML(constraints, layerFile);
  }
  
  public static void writeLayerExports(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
  {
	  String exports = computeString(Alayer, "@exports");
  	  writeHTML("<p><b><font size=\"4\">Exports</font></b></p>", layerFile);
	  writeHTML(exports, layerFile);
  }
	
  public static void writeLayerGrammar(LayerDocJTS Alayer, Writer layerFile)
	 throws IOException
  {
	  String grammar = computeString(Alayer, "@grammar");
	  writeHTML("<p><b><font size=\"4\">Grammar</font></b></p>", layerFile);	  
	  writeHTML(grammar, layerFile);
	  writeHTML("<hr> \n", layerFile);
  }

 
/** Writes the inner classes and creates their corresponding file.
*/ 
public static void writeRoles(ClassDoc Aclass, Writer outputFile)
	throws IOException
{	
     ClassDoc[] innerclasses = Aclass.innerClasses();
    if (innerclasses == null) return;
	if (innerclasses.length == 0) return;

	// Displays all the roles
    for (int i=0; i< innerclasses.length ; i++)
    {	  
	  // Role row
	  /*	
	  writeHTML("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">  \n", 
				outputFile);	
	  writeHTML("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\">  \n", 
				outputFile);
	  writeHTML("<TD COLSPAN=1><FONT SIZE=\"+2\">  \n", outputFile);
	  writeHTML("<B> Role \n", outputFile);
	  writeHTML(innerclasses[i].name(), outputFile);
	  writeHTML("</B></FONT></TD>  \n", outputFile);
	  writeHTML("</TR> </TABLE> \n", outputFile);	
	  writeHTML("<BR> \n", outputFile);
	  */
	  writeBar("Role " + innerclasses[i].name(), outputFile);
	  
	  // Role tag
	  writeHTML("<p> \n", outputFile);
	  writeHTML(computeString(innerclasses[i], "@role"), outputFile);
	  writeHTML("</p> \n", outputFile);
	  
	  writeHTML("Details of this role are <B> \n", outputFile);
	  writeHTML("<A HREF=\"" + innerclasses[i].name()+".html" + "\"> " + "\n", 
				outputFile);
	  writeHTML(innerclasses[i].name() + "\n", outputFile);
	  writeHTML("</A> \n", outputFile);
	  writeHTML(" \n", outputFile);
	  writeHTML("</B> \n", outputFile);
	  writeHTML("<BR><BR> \n", outputFile);  
	  writeHTML("<BR> \n", outputFile); 
	  
	  // class the function recursively to create the file for the innerclass
	  writeAClass(innerclasses[i]);
    }

	writeHTML("</TR> \n", outputFile);
	writeHTML("</TABLE> \n", outputFile);
	writeHTML("<BR> \n", outputFile);
}
 

 // writers the appends Contents of a class
 public static void appendContents(DOCLETJTS.Lang.ClassDocJTS Aclass,
								   Writer outputFile) throws IOException 
 {
   javaDoclet.DocletJTS.appendContents(Aclass, outputFile);
 }
 
 } // class DocletJTS
  
};