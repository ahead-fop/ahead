package DocletImpl; 
import com.sun.javadoc.*; 
import sun.tools.java.*; 
import com.sun.tools.javadoc.*; 
import jtsdoc.*; 
import   java.util.*; 
import java.io.*; 

// Updated version August 28, 2001
public class javaDoclet
{

 static public class DocletJTS {

 /** Strings that contains the output directory to write the files.
 */
 public static String OutputDirectory;
 
 public static boolean start(RootDoc root) {
  String tagName = null; // readOptions(root.options());
  try {
    writeContents(root.classes(), tagName);
  } catch(IOException e)
  {
    System.out.println("Error at start");
  }
  return true;
 }

 
 // This is the part I have to work on for the AST Classes part probably ???
 public static void writeContents(ClassDoc[] classes, String tagName) throws IOException
 {
   Writer outFile;

   for (int i=0; i < classes.length; i++) 
   {	  
     // opens the corresponding file
     outFile = createOutFile(classes[i].name());
		
     // Inserts the header and 
     writeHTML("<HTML> <HEAD></HEAD> \n", outFile);
     writeHTML("<TITLE> Class Hierarchy </TITLE> \n" , outFile);
     writeHTML("<H1> Layer Layout </H1>", outFile);
     writeHTML("<BODY> \n", outFile);
     writeHTML("<UL> \n", outFile);
    
     /// stateDoclet sd = new stateDoclet();
     // sd.writeAClass(classes[i], outFile);
	  
     // Here goes the class hierarchy layout
     writeAClass(classes[i]);
	  
     writeHTML("</UL> \n", outFile);  // for the class
     writeHTML("</BODY> </HTML> \n", outFile);

     // closes the output file
     closeOutFile(outFile);
	
    } // for i all the classes		
   
  } // writeContents
 
 
 /*
 // WriteContentsAClass
  public static void writeContentsAClass(ClassDoc Aclass) 
	  throws IOException
 {
   System.out.println("writeContentsAClass " + Aclass.name());
  
  } // writeContents
 */
 
 // Write a simple class, and calls itself recursively in nested interfaces 
 // and classes.
 // If using the javadoc ClassDocImpl, if using JTS DOCLETJTS.Lang.ClassDocJTS
 public static void writeAClass(ClassDoc Aclass) 
	 throws IOException
 {
    System.out.println("Producing Documentation for class/interface " 
					   + Aclass.name());  
 	Writer outFile = createOutFile((Aclass.name()).trim());
		
	// Inserts the header stuff
	writeClassHeader(Aclass,outFile);
	
	// Inserts the body setup of the class
	writeClassBodySetup(Aclass, outFile);
	 
    // Inserts the class name
 	writeClassName(Aclass, outFile);
	
	// Inserts the class relationships & modifiers
	writeClassRelationships(Aclass, outFile);
	
	// Inserts the class description
	writeClassDescription(Aclass, outFile);
	
	// writes out the fields
	writeFields(Aclass.fields(), outFile);
	
	// writes out the constructors
	writeConstructors(Aclass.constructors(), outFile);
	
	// writes out the methods
	writeMethods(Aclass.methods(), outFile);
		  
	// writes out the Innerclasses
	writeInnerclasses(Aclass, outFile);

	// *** appends stuff
	Lang.DocletJTS.appendContents((DOCLETJTS.Lang.ClassDocJTS)Aclass, outFile);
	
	// *************************************************************************
	// *************************** DETAILS *************************************
	
	// writes out the fields
	writeDetailFields(Aclass.fields(), outFile);
	
	// writes out the constructors
	writeDetailConstructors(Aclass.constructors(), outFile);
	
	// writes out the methods
	writeDetailMethods(Aclass.methods(), outFile);
		  
	// writes out the Innerclasses
	writeDetailInnerclasses(Aclass, outFile);

	// append stuff for states pending ... )-:
	
	// **** footer stuff
    writeClassFooter(Aclass, outFile);
		
	// closes the output file
	closeOutFile(outFile);
	
 } // of writeAClass

 // writers the appends Contents of a class
 public static void appendContents(DOCLETJTS.Lang.ClassDocJTS Aclass,
		   Writer outputFile) throws IOException 
 {
 }
  
  // ********************************************************
 // ********************************************************
 // ********************************************************
 // Methods that are defined for a Class Document specification
 public static void writeClassHeader(ClassDoc Aclass, Writer outFile)
	 throws IOException
 {							  
 	writeHTML("<HTML> <HEAD></HEAD> \n", outFile);
    writeHTML("<TITLE> Class "+ Aclass.name() + "  </TITLE> \n", outFile);
	writeHTML("<HR> ", outFile);
 }

 public static void writeClassFooter(ClassDoc Aclass, Writer outFile)
	 throws IOException
 {
    writeHTML("</BODY> </HTML> \n", outFile);
 }
 
 public static void writeClassBodySetup(ClassDoc Aclass, Writer outFile)
	 throws IOException
 {
     writeHTML("<BODY BGCOLOR=\"white\"> \n", outFile);
 }
 
 
 public static void writeClassName(ClassDoc Aclass, Writer outFile)
	 throws IOException
 {
	if (Aclass.isClass())
	{ 
      writeHTML("<H2> <BR> Class " + Aclass.name()+ " </H2>", outFile); 
	}
	
	if (Aclass.isInterface())
	{ writeHTML("<H2> <BR> Interface  " + Aclass.name() + "</H2>", outFile); }
    // Class header
   writeText(Aclass.commentText(), outFile);
 }
 
 /** Class description displays the header comment of a class if any. Also
  * displays the see, since, deprecated, author, version, and link tags that
  * the class comment contains.
  */
 public static void writeClassDescription(ClassDoc Aclass, Writer outFile)
	 throws IOException
 {
   // Class header
   writeText(computeDescriptionComment(Aclass.inlineTags()), outFile);
   
   // Write start of tags
   writeTagsHeader(outFile);
   
   // Writes deprecated tags
   writeDeprecatedTags(Aclass, outFile);
   
   // Writes author tags
   writeAuthorTags(Aclass, outFile);
   
   // Writes version tags
   writeVersionTags(Aclass, outFile);
   
   // Writes since tags
   writeSinceTags(Aclass, outFile);
   
   // Writes see tags
   writeSeeTags(Aclass, outFile);
   
   // Writes tags footer
   writeTagsFooter(outFile);
 }
 
 
 /** Description displays the Class name with the names of the classe it
  * extends the interfaces it implements.
  */
 public static void writeClassRelationships(ClassDoc Aclass, Writer outFile)
	 throws IOException
 {
	 writeHTML("<DL> <DT>" + Aclass.modifiers() + "<B> " + Aclass.name()+ "</B>",
			   outFile);
	 if (Aclass.superclass() != null) 
		 writeHTML("<DT> extends " + (Aclass.superclass()).name(), outFile);
	 ClassDoc[] theInterfaces = (ClassDoc[])Aclass.interfaces();
	 
	 if (theInterfaces != null)
	 { 
	   if (theInterfaces.length > 0) 
		   writeHTML("<DT>implements ", outFile);
	   for(int i=0; i< theInterfaces.length; i++)
	   {
		 if (i < theInterfaces.length - 1)   
		 { writeHTML(theInterfaces[i].name() + ",", outFile); }
		 else
			writeHTML(theInterfaces[i].name() + "", outFile); 
	   }
	 };
	 writeHTML("</DL>", outFile);
	 writeHTML("<HR>", outFile);
 }
 
 
 /** Writes out the constructors.
  */
 public static void writeConstructors(ConstructorDoc[] constructors , Writer outputFile) 
	 throws IOException
 {
	// if constructors is null or there are not constructors dont do anything
	 if (constructors == null) return;
	 if (constructors.length == 0) return;
		 
	// Constructs the fancy constructor summary table, the header row
	writeHTML("<!-- ======== CONSTRUCTOR SUMMARY ======== --> \n", outputFile);
	writeHTML("<A NAME=\"constructor_summary\"><!-- --></A> \n",outputFile);
	writeHTML("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\"> \n",
			  outputFile);
	writeHTML("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\"> \n",outputFile);
	writeHTML(" <TD COLSPAN=2><FONT SIZE=\"+2\"> \n",outputFile);
	writeHTML(" <B>Constructor Summary</B></FONT></TD> \n",outputFile);
	writeHTML(" </TR> \n",outputFile);	
	
	// constructors

         // for all the constructors of a class
         for (int l=0; l< constructors.length; l++) 
		 {
		   Parameter[] parameters = constructors[l].parameters();
		   String theSignature = " ( "; 
           // shows all the arguments of the method
	       if (parameters != null) 
		   {
            for (int k=0; k< parameters.length; k++)
            { 
              //writeHTML(" " + parameters[k].typeName() + " " + parameters[k].name()
              //			, outputFile);
		theSignature = theSignature + parameters[k].typeName() + " " 
					 + parameters[k].name();
		if (k < parameters.length - 1) 
		  theSignature = theSignature + ", ";
            } // for k all the parameters
		   }
		    theSignature = theSignature + " )";
            // writeHTML(" ) </LI> \n", outputFile);
 
	   // Adding exceptions	
	   String theExceptions = "";
		   
	   ClassDoc[] thrownExceptions = constructors[l].thrownExceptions();
           if (thrownExceptions != null)
	   { 
	      if (thrownExceptions.length > 0) theExceptions = "throws ";   
		for (int m=0; m < thrownExceptions.length; m++)
		{
		   theExceptions = theExceptions + thrownExceptions[m].name() + " ";
		   if (thrownExceptions.length != thrownExceptions.length)
				theExceptions = theExceptions + ", ";
		} // for all the thrown exceptions
			
	       } // if there are thrown exceptions
	
		   // writes out the description of a method
		   writeHTML("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\"> \n",
					 outputFile);
		   writeHTML("<TD><CODE><B>  \n",outputFile);
                   writeHTML("<A>" + constructors[l].name() + "\n", outputFile);
		   writeHTML("</A> \n",outputFile);
		   writeHTML("</B>" + theSignature + theExceptions + " </CODE> \n", 
					 outputFile);
		   writeHTML("<BR> \n", outputFile);
		   
		   // this is the comment of the first sentence.
		   Tag[] fst = constructors[l].firstSentenceTags();
		   String fstText = "";
		   for(int m = 0; m < fst.length; m++)
		     fstText = fstText + fst[m].text();
		   writeHTML(fstText + "\n", outputFile);
		   
		   writeHTML("</TD>  \n", outputFile);
		   writeHTML("<BR> \n", outputFile);
		   
		   writeHTML("</TR>  \n", outputFile);
			   
          } // for l the contents of the methods in the class
	     		  
	
	 writeHTML("</TABLE> \n", outputFile);
	 writeHTML("<BR> \n",outputFile);
 }

 /** Constructor Description displays the header comment if any. Also displays
  * the see, since, deprecated, author, version, and link tags.
  */
public static void writeConstructorDescription(ConstructorDocJTS Aconstructor, 
         Writer  outFile)
	throws IOException
{  
   // Inserts the string for the header comment
   writeText(computeDescriptionComment(Aconstructor.inlineTags()), outFile);
 
   // Write start of tags
   writeTagsHeader(outFile);
   
   // Writes deprecated tags
   writeDeprecatedTags(Aconstructor, outFile);

   // Writes params tags
   writeParamTags(Aconstructor, outFile);
   
   // Writes throws tags
   writeThrowsTags(Aconstructor, outFile);
   
   // Writes author tags
   writeAuthorTags(Aconstructor, outFile);

   // Writes version tags
   writeVersionTags(Aconstructor, outFile);
   
   // Writes since tags
   writeSinceTags(Aconstructor, outFile);
   
   // Writes see tags
   writeSeeTags(Aconstructor, outFile);
   
   // Writes return tags
   writeReturnTags(Aconstructor, outFile);
   
   // Writes tags footer
   writeTagsFooter(outFile);
}
 
  /** Method description displays the header comment of a method if any. Also
  * displays the see, since, deprecated, param, throws, author, version, and 
  * link tags that the class comment contains.
  */
 public static void writeMethodDescription(MethodDocJTS Amethod, Writer outFile)
	 throws IOException
 {
   // Inserts the string for the header comment
   writeText(computeDescriptionComment(Amethod.inlineTags()), outFile);

   // Write start of tags
   writeTagsHeader(outFile);
   
   // Writes deprecated tags
   writeDeprecatedTags(Amethod, outFile);

   // Writes params tags
   writeParamTags(Amethod, outFile);
   
   // Writes throws tags
   writeThrowsTags(Amethod, outFile);
   
   // Writes author tags
   writeAuthorTags(Amethod, outFile);
   
   // Writes version tags
   writeVersionTags(Amethod, outFile);
   
   // Writes since tags
   writeSinceTags(Amethod, outFile);
   
   // Writes see tags
   writeSeeTags(Amethod, outFile);
   
   // Writes return tags
   writeReturnTags(Amethod, outFile);
   
   // Writes tags footer
   writeTagsFooter(outFile);
 }
 
 /** Writes the fields.
  */
 public static void writeFields(FieldDoc[] fields, Writer outputFile) throws IOException
 {	 
    // if constructors is null or there are not constructors dont do anything
    if (fields == null) return;
    if (fields.length == 0) return;
		 
    // Constructs the fancy field summary table, the header row
    writeHTML("<!-- ======== FIELD SUMMARY ======== --> \n", outputFile);
    writeHTML("<A NAME=\"field_summary\"><!-- --></A> \n",outputFile);
    writeHTML("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\"> \n",
			  outputFile);
    writeHTML("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\"> \n",outputFile);
    writeHTML(" <TD COLSPAN=2><FONT SIZE=\"+2\"> \n",outputFile);
    writeHTML(" <B>Field Summary</B></FONT></TD> \n",outputFile);
    writeHTML(" </TR> \n",outputFile);		 
	 
    for (int l=0; l < fields.length ; l++)
    {	    
      writeHTML("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\"> \n",outputFile);
      writeHTML("<TD ALIGN=\"right\" VALIGN=\"top\" WIDTH=\"1%\"><FONT SIZE=\"-1\"> \n",
			  outputFile);
      writeHTML("<CODE>" + fields[l].modifiers() + " " + (fields[l].type()).typeName() 
			  + "</CODE> \n", outputFile);
      writeHTML("</FONT></TD> \n",outputFile);
      writeHTML("<TD><CODE><B>" + fields[l].name()+ "</B></CODE> \n",outputFile);
      writeHTML(" \n",outputFile);
      writeHTML("<BR> \n",outputFile); 
		
      // this is the comment of the first sentence.
      Tag[] fst = fields[l].firstSentenceTags();
      String fstText = "";
      for(int m = 0; m < fst.length; m++)
	fstText = fstText + fst[m].text();
   
      writeHTML(fstText + "\n", outputFile);
		
      writeHTML(" </TD> \n",outputFile);
      writeHTML("</TR> \n",outputFile);
    }

    writeHTML("</TABLE> \n", outputFile);
    writeHTML("<BR> \n",outputFile);
 }
 
 /** Constructor Description displays the header comment if any. Also displays
  * the see, since, deprecated, author, version, and link tags.
  */
public static void writeFieldDescription(FieldDocJTS Afield, Writer  outFile)
	throws IOException
{  
   // Inserts the string for the header comment
   // System.out.println("Field Detail ===> " +	(Afield.inlineTags()).length + " " + 
   //				computeDescriptionComment(Afield.inlineTags()));
  
   writeText(computeDescriptionComment(Afield.inlineTags()), outFile);
 
   // Write start of tags
   writeTagsHeader(outFile);
   
   // Writes deprecated tags
   writeDeprecatedTags(Afield, outFile);

   // Writes params tags
   writeParamTags(Afield, outFile);
   
   // Writes throws tags
   writeThrowsTags(Afield, outFile);
   
   // Writes author tags
   writeAuthorTags(Afield, outFile);

   // Writes version tags
   writeVersionTags(Afield, outFile);
   
   // Writes since tags
   writeSinceTags(Afield, outFile);
   
   // Writes see tags
   writeSeeTags(Afield, outFile);
   
   // Writes return tags
   writeReturnTags(Afield, outFile);
   
   // Writes tags footer
   writeTagsFooter(outFile);
}

 /** Writes the interfaces. 
  */
 public static void writeInterfaces(ClassDoc[] interfaces, Writer outputFile) 
	 throws IOException
 { 
	writeHTML("<LI> Interfaces (in implements): </LI>", outputFile);
	if (interfaces != null)
	{
	  writeHTML("<UL> \n", outputFile);
	  for (int l=0; l< interfaces.length ; l++)
		  writeHTML("<LI> Interface : " + interfaces[l].name() + " </LI>"
					, outputFile);
	  writeHTML("</UL> \n", outputFile);		
	} // if there are interfaces		 
	
 }
 
 /** Writes the methods.
  */
 public static void writeMethods(MethodDoc[] methods, Writer outputFile) 
	 throws IOException
 {
	// if there are no methods return
	if (methods == null) return;
	if (methods.length == 0) return;

	writeHTML("<!-- ========== METHOD SUMMARY =========== --> \n", outputFile);
	writeHTML("<A NAME=\"method_summary\"><!-- --></A> \n", outputFile);
	writeHTML("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\"> \n",
			  outputFile);
	writeHTML("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\"> \n", outputFile);
	writeHTML("<TD COLSPAN=2><FONT SIZE=\"+2\"> \n", outputFile);
	writeHTML("<B>Method Summary</B></FONT></TD> \n", outputFile);
	writeHTML("</TR> \n", outputFile);
	
	// for all the methods 
    for (int l=0; l< methods.length; l++) 
	{
	  writeHTML("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\"> \n", outputFile);
	  writeHTML("<TD ALIGN=\"right\" VALIGN=\"top\" WIDTH=\"1%\"><FONT SIZE=\"-1\"> \n", 
				  outputFile);
	  writeHTML("<CODE>" + methods[l].modifiers() + " " + (methods[l].returnType()).typeName() 
				  + "</CODE></FONT></TD> \n", outputFile);

      Parameter[] parameters = methods[l].parameters();
	  String theSignature = " ( ";
	   
      // shows all the arguments of the method
    if (parameters != null) 
	{
         for (int k=0; k< parameters.length; k++)
         {
	  theSignature = theSignature + parameters[k].typeName() + " " 
			 + parameters[k].name();
	  if (k < parameters.length - 1) 
	   theSignature = theSignature + ", ";			 
          } // for k all the parameters
	   } // if there are parameters
	   
    theSignature = theSignature + " ) ";
   
    ClassDoc[] thrownExceptions = methods[l].thrownExceptions();
    String theExceptions = ""; 	
    if (thrownExceptions != null)
    { 
	 if (thrownExceptions.length > 0) theExceptions = "throws ";   
	 for (int m=0; m < thrownExceptions.length; m++)
	 {
	   theExceptions = theExceptions + thrownExceptions[m].name() + " ";
	   if (thrownExceptions.length != thrownExceptions.length)
			theExceptions = theExceptions + ", ";
	  } // for all the thrown exceptions
			
     } // if there are thrown exceptions

     writeHTML("<TD><CODE><B>"+ methods[l].name() + theSignature + " " + theExceptions 
				+ " \n", outputFile);
     writeHTML("</B></CODE> \n", outputFile);
     writeHTML("<BR> \n", outputFile); // &nbsp;&nbsp; here goes the message
	  
      // this is the comment of the first sentence.
      Tag[] fst = methods[l].firstSentenceTags();
      String fstText = "";
      for(int m = 0; m < fst.length; m++)
	     fstText = fstText + fst[m].text();
      
      writeHTML(fstText + "\n", outputFile);	  
      writeHTML("</TD> \n", outputFile);
      writeHTML("</TR> \n", outputFile);
    } // for l the contents of the methods in the class
		 
    writeHTML(" </TABLE> \n", outputFile);
    writeHTML("<BR> \n", outputFile);
 }
 
/** Writes the inner classes and creates their corresponding file.
*/ 
public static void writeInnerclasses(ClassDoc Aclass, Writer outputFile)
	throws IOException
{	
     ClassDoc[] innerclasses = Aclass.innerClasses();
    if (innerclasses == null) return;
	if (innerclasses.length == 0) return;
	
 	writeHTML("<!-- ======== INNER CLASS SUMMARY ======== --> \n", outputFile);
	writeHTML("<A NAME=\"inner_class_summary\"><!-- --></A> \n", outputFile);
	writeHTML("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\"> \n",
			  outputFile);
	writeHTML("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\"> \n", outputFile);
	writeHTML("<TD COLSPAN=2><FONT SIZE=\"+2\"> \n", outputFile);
	writeHTML("<B>Inner Class Summary</B></FONT></TD> \n", outputFile);
	writeHTML("</TR> \n", outputFile);
	
    // innerclasses detail 
    for (int i=0; i< innerclasses.length ; i++)
    {	  
	  writeHTML("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\"> \n", outputFile);
	  writeHTML("<TD ALIGN=\"right\" VALIGN=\"top\" WIDTH=\"1%\"> \n",
				outputFile);
	  writeHTML("<FONT SIZE=\"-1\"> \n", outputFile);
	  writeHTML("<CODE>" + innerclasses[i].modifiers() + "   "+ "</CODE> \n", 
				outputFile);
	  writeHTML("</FONT> \n", outputFile);
	  writeHTML("</TD> \n", outputFile);
	  writeHTML("<TD> \n", outputFile);
	  writeHTML("<CODE> \n", outputFile);
	  writeHTML("<B> \n", outputFile);
	  writeHTML("<A HREF=\"" + innerclasses[i].name()+".html" + "\"> " + "\n", 
				outputFile);
	  writeHTML(innerclasses[i].name() + "\n", outputFile);
	  writeHTML("</A> \n", outputFile);
	  writeHTML(" \n", outputFile);
	  writeHTML("</B> \n", outputFile);
	  writeHTML("</CODE> \n", outputFile);
	  writeHTML("<BR> \n", outputFile);  // here goes the message 
	  writeHTML("</TD> \n", outputFile);
	  writeHTML("</TR> \n", outputFile);
	  
	  // class the function recursively to create the file for the innerclass
	  writeAClass(innerclasses[i]);
    }

	writeHTML("</TR> \n", outputFile);
	writeHTML("</TABLE> \n", outputFile);
	writeHTML("<BR> \n", outputFile);
}
 
 // ********************************************************
 // ********************* Details methods   ****************
 // ********************************************************
 
 /** Writes out the fields details.
  */
 public static void writeDetailFields(FieldDoc[] fields, Writer outFile) 
	 throws IOException
 {	 
	// if fields is null or there are not constructors dont do anything
    if (fields == null) return;
    if (fields.length == 0) return;	 
	 
	 String fieldString ="";
 
	 // Writes section bar.
	 writeBar("Fields Detail", outFile);
	   	 
    // Constructs the field details field by field
    writeHTML("<!-- ======== FIELD DETAILS ======== --> \n", outFile);

	// For all the fields
    for (int l=0; l < fields.length ; l++)
    {	   
	  // Header with class name 
	  writeHTML("<H3> \n", outFile);
	  writeHTML(fields[l].name(), outFile);
	  writeHTML("</H3> \n", outFile);	 
	  
	  fieldString =  fields[l].modifiers() + " " + (fields[l].type()).typeName();
	  
	  // Display modifiers, type,  name, arguments, exceptions	  
	  writeHTML(fieldString + "\n", outFile);
	  writeHTML("<B> \n", outFile);
	  writeHTML(fields[l].name(), outFile);
	  writeHTML("</B> \n", outFile);
	  writeHTML("<BR> &nbsp;&nbsp; ", outFile);
	  
	  // Displays the tags
      writeFieldDescription((FieldDocJTS)fields[l], outFile);	  
	  
	  /*
	  		   Tag[] fst = constructors[l].firstSentenceTags();
		   String fstText = "";
		   for(int m = 0; m < fst.length; m++)
		     fstText = fstText + fst[m].text();
		   writeHTML(fstText + "\n", outputFile);
	  */
	  
    } // for all the fields

    writeHTML("<BR> \n",outFile);	 	
 }
	
 /** Writes out the constructors details.
  */
 public static void writeDetailConstructors(ConstructorDoc[] constructors, Writer outFile)
	 throws IOException
 {
	 String constructorString ="";
	 
	 // If there are no methods then dont do details.
	 if (constructors.length == 0) return;
	 
	 // Writes section bar.
	 writeBar("Constructor Detail", outFile);
	 
	// for all the constructors 
        for (int l=0; l< constructors.length; l++) 
	{
	   constructorString = constructors[l].modifiers();
	  
       Parameter[] parameters = constructors[l].parameters();
	   String theSignature = " ( ";
	   
           // shows all the arguments of the method
	   if (parameters != null) 
	   {
             for (int k=0; k< parameters.length; k++)
              {
		  theSignature = theSignature + parameters[k].typeName() + " " 
						 + parameters[k].name();
		  if (k < parameters.length - 1) 
		   theSignature = theSignature + ", ";			 
              } // for k all the parameters
	   } // if there are parameters
	   
	   theSignature = theSignature + " ) ";
   
           // The exceptions of the constructors
	   ClassDoc[] thrownExceptions = constructors[l].thrownExceptions();
	   String theExceptions = ""; 	
           if (thrownExceptions != null)
	   { 
		 if (thrownExceptions.length > 0) theExceptions = "throws ";   
		 for (int m=0; m < thrownExceptions.length; m++)
		 {
			theExceptions = theExceptions + thrownExceptions[m].name() + " ";
			if (thrownExceptions.length != thrownExceptions.length)
				theExceptions = theExceptions + ", ";
		} // for all the thrown exceptions
			
	   } // if there are thrown exceptions

	  // Header with class name 
	  writeHTML("<H3> \n", outFile);
	  writeHTML(constructors[l].name(), outFile);
	  writeHTML("</H3> \n", outFile);

	  // Display modifiers, type,  name, arguments, exceptions	  
	  writeHTML(constructorString + "\n", outFile);
	  writeHTML("<B> \n", outFile);
	  writeHTML(constructors[l].name(), outFile);
	  writeHTML("</B> \n", outFile);
	  writeHTML(" " + theSignature, outFile);
	  writeHTML("<BR> &nbsp;&nbsp; ", outFile);
	  writeHTML(theExceptions + " \n", outFile);
	  
	  // Displays the tags
      writeConstructorDescription((ConstructorDocJTS)constructors[l], outFile);

    } // for l the contents of the methods in the class	
		
 } // of writeDetailConstructors
	
 /** Writes out the methods details.
  */
 public static void writeDetailMethods(MethodDoc[] methods, Writer outFile)
	 throws IOException
 {
	 String methodString ="";
	 
	 // If there are no methods then dont do details.
	 if (methods.length == 0) return;
	 
	 // Writes section bar.
	 writeBar("Method Detail", outFile);
	 
	// for all the methods 
        for (int l=0; l< methods.length; l++) 
	{
	   methodString = methods[l].modifiers() + " " + 
                          (methods[l].returnType()).typeName();
	  
           Parameter[] parameters = methods[l].parameters();
	   String theSignature = " ( ";
	   
           // shows all the arguments of the method
	   if (parameters != null) 
	   {
             for (int k=0; k< parameters.length; k++)
              {
		  theSignature = theSignature + parameters[k].typeName() + " " 
						 + parameters[k].name();
		  if (k < parameters.length - 1) 
		   theSignature = theSignature + ", ";			 
              } // for k all the parameters
	   } // if there are parameters
	   
	   theSignature = theSignature + " ) ";
   
	   ClassDoc[] thrownExceptions = methods[l].thrownExceptions();
	   String theExceptions = ""; 	
           if (thrownExceptions != null)
	   { 
		 if (thrownExceptions.length > 0) theExceptions = "throws ";   
		 for (int m=0; m < thrownExceptions.length; m++)
		 {
			theExceptions = theExceptions + thrownExceptions[m].name() + " ";
			if (thrownExceptions.length != thrownExceptions.length)
				theExceptions = theExceptions + ", ";
		} // for all the thrown exceptions
			
	   } // if there are thrown exceptions

	  // Header with class name 
	  writeHTML("<H3> \n", outFile);
	  writeHTML(methods[l].name(), outFile);
	  writeHTML("</H3> \n", outFile);

	  // Display modifiers, type,  name, arguments, exceptions	  
	  writeHTML(methodString + "\n", outFile);
	  writeHTML("<B> \n", outFile);
	  writeHTML(methods[l].name(), outFile);
	  writeHTML("</B> \n", outFile);
	  writeHTML(" " + theSignature, outFile);
	  writeHTML("<BR> &nbsp;&nbsp; ", outFile);
	  writeHTML(theExceptions + " \n", outFile);
	  
	  // Displays the tags
          writeMethodDescription((MethodDocJTS)methods[l], outFile);

    } // for l the contents of the methods in the class	
 }
		  
 /** Writes out the Innerclasses.
  */
 public static void writeDetailInnerclasses(ClassDoc Aclass, Writer outFile)
	 throws IOException
 {
 }
 
 // ********************************************************
 // ************************* Tag management  **************
 // ********************************************************
 
 /** Valid tag operations.
  */
 private static String readOptions(String[][] options) {
  String tagName = null;
  for (int i=0; i<options.length ; i++)
  {
    String [] opt = options[i];
    if (opt[0].equals("-tag")) { 
        tagName= opt[1];
    }
  } // for i
  return tagName;
 
 } // of readOptions

 /** Lengths of the tags ??
  */
 public static int optionLength(String option) {
  if (option.equals("-tag")) { 
    return 2;
   }
  return 0;
 }

 public static boolean validOptions(String options[][], DocErrorReporter reporter) {
  boolean foundTagOption = false;
  for (int i=0; i < options.length; i++) {
   String[] opt = options[i];
   if (opt[0].equals("-tag")) {
      if (foundTagOption) {
        reporter.printError("Only one -tag option allowed.");
        return false;
      } else {
        foundTagOption = true;
      }     
   }   
  } // for i

  if (!foundTagOption) {
    reporter.printError("Usage: javadoc -tag mytag -doclet listTags ...");
   }
  return foundTagOption;
 }// of validOptions

 
 // ********************************************************
 // ********************************************************
 // ********************************************************
 // Low level file handling
 
 /** Creates the output file.
  */
 public static Writer createOutFile(String FileName) throws IOException
 {
     Writer outFile = null;       
	 System.out.println("Create file " + OutputDirectory + FileName + ".html");
      // Creates the prog.layer file 
     try {
       outFile = new FileWriter(OutputDirectory + FileName+".html");
     } catch (IOException e)
     {
        System.out.println("Error while creating the output file");
	    System.exit(1);       
     } 
	 
	 return outFile;
  }  // of writeProgLayer

 /** Closes the output file.
  */
 public static void closeOutFile(Writer outFile) throws IOException
 {
    try {
     outFile.close();
    } catch(IOException e) 
    {
       System.out.println("Error while closing the output file");
	System.exit(1);    
    }
 } // of closeOutFile
 

 /** Writes a string to the output file.
  */
 public static void writeHTML(String theLine, Writer theFile) throws IOException
 {
    try {
     theFile.write(theLine);
    } catch(IOException e) 
    {
       System.out.println("Error while writing the output file");
	System.exit(1);    
    }
 }

/** Writes out the content of a program object. Created in the AST_Class rule
 * of the grammar.
 */
 public static void writeAProgram(DOCLETJTS.Lang.ProgramDocJTS Aprogram,
                                  String _OutputDirectory)
	throws IOException
 {
     OutputDirectory = _OutputDirectory;
     
	 Writer outFile = createOutFile((Aprogram.name()).trim());
	 
	// Inserts the header stuff
	writeProgramHeader(Aprogram,outFile);
	
	// Inserts the body setup of the program
	writeProgramBodySetup(Aprogram, outFile);
	 
    // Inserts the program name
 	writeProgramName(Aprogram, outFile);
	
	// Insterts the program relationships & modifiers
	writeProgramRelationships(Aprogram, outFile);
			  
    // writes out the classes
	writeProgramClasses(Aprogram, outFile);

	// *** appends stuff
	Lang.DocletJTS.appendsProgramContents((DOCLETJTS.Lang.ProgramDocJTS)Aprogram, 
										  outFile);
	
	// **** footer stuff
    writeProgramFooter(Aprogram, outFile);
		
	// closes the output file
	closeOutFile(outFile); 
 }
 
 public static void writeProgramHeader(DOCLETJTS.Lang.ProgramDocJTS Aprogram, Writer outFile)
	 throws IOException
 {							  
 	writeHTML("<HTML> <HEAD></HEAD> \n", outFile);
    writeHTML("<TITLE> Program "+ Aprogram.name() + "  </TITLE> \n", outFile);
	writeHTML("<HR> ", outFile);
 }

 public static void writeProgramFooter(DOCLETJTS.Lang.ProgramDocJTS Aprogram, Writer outFile)
	 throws IOException
 {
    writeHTML("</BODY> </HTML> \n", outFile);
 }
 
 public static void writeProgramBodySetup(DOCLETJTS.Lang.ProgramDocJTS Aprogram, Writer outFile)
	 throws IOException
 {
     writeHTML("<BODY BGCOLOR=\"white\"> \n", outFile);
 }
 
 
 public static void writeProgramName(DOCLETJTS.Lang.ProgramDocJTS Aprogram, 
									 Writer outFile)
	 throws IOException
 {
     writeHTML("<H2> <BR> Program " + Aprogram.name()+ " </H2>", outFile); 
 
 }
 
 public static void writeProgramRelationships(DOCLETJTS.Lang.ProgramDocJTS Aprogram,
											  Writer outFile)
	 throws IOException
 {
	 // Here it is where it writes the package name and the imports part
	 writeHTML("<DL> <DT> <B> " + Aprogram.name()+ "</B>",
			   outFile);
	 if (Aprogram.containingPackage() != null) 
		 writeHTML("<DT> Package " + (Aprogram.containingPackage()).name(), outFile);
	 
	 // Pending the imports packages part
	 writeHTML("</DL>", outFile);
	 writeHTML("<HR>", outFile);
 }
 
 
 public static void writeProgramClasses(DOCLETJTS.Lang.ProgramDocJTS Aprogram,
											  Writer outFile)
	 throws IOException
 {
	 // Calls the writeInnerclasses by creating first fake class and array
	 DOCLETJTS.Lang.ClassDocJTS tempClass = new DOCLETJTS.Lang.ClassDocJTS("");
     tempClass.setInnerClasses((List)Aprogram.classes());
     writeInnerclasses(tempClass, outFile);
 }

 public static void appendsProgramContents(DOCLETJTS.Lang.ProgramDocJTS Aprogram,
											  Writer outFile)
	 throws IOException
 {
   
 }

 // *****************************************************************
 // *****************************************************************
 // **** Auxiliary HTML generation function *************************
 
 /** Writes the fancy bar used for cons/methods/fields/etc details
  */ 
 public static void writeBar(String barMessage, Writer outFile)
	 throws IOException
 {
    writeHTML("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\"> \n", 
				outFile);	
	writeHTML("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\">  \n", 
				outFile);
	writeHTML("<TD COLSPAN=1><FONT SIZE=\"+2\">  \n", outFile);
	writeHTML("<B> ", outFile);
	
	// Here goes the message
	writeHTML(barMessage, outFile);
	writeHTML("</B></FONT></TD>  \n", outFile);
	writeHTML("</TR> </TABLE> \n", outFile);	
	writeHTML("<BR> \n", outFile);	 
 }
 
 /** Writes the text of a comment a.k.a the header. 
  */
 public static void writeText(String text, Writer outFile)
	 throws IOException
 {
	 writeHTML("<p> \n", outFile);
	 writeHTML(text, outFile);
	 writeHTML("</p> \n", outFile);
 }
 
 /** Computes an string for a given tag name.
   */
 public static String computeString(Doc ADoc, String tagName)
 {
	 Tag[] tags = ADoc.tags(tagName);
	 String resultString = "";
	 
	 for (int i=0; i< tags.length; i++)
		 resultString = resultString + tags[i].text();
	 
	 return resultString;
 }
 
 /** Computes the full text from a comment, given its tag name.
  * Includes also the inline tags.
  */
 public static String computeFullString(Doc ADoc, String tagName)
 {
	 String fullString = "";
	 Tag[] tags = ADoc.tags(tagName);
	 
	 /*
	  For all the tags whose name is tagName, read the inlinetags that they
	  have and add their text.
	  If there are inlink, you have to cast them to see Tags and get the
	  referenced class, and member name plus the label.
	 */
	 for(int i=0; i < tags.length ; i++)
	 {
		 Tag[] inlineTags = tags[i].inlineTags();
		 for(int j=0; j< inlineTags.length; j++)
		 {
			 // if we are dealing with a text tag then just add it
			 if (inlineTags[j].name().compareTo("Text") == 0)
			 { fullString = fullString + " " + inlineTags[j].text(); }
			 else // it is an inlink tag so make the reference call
			 {  
			 SeeTag seeTag = (SeeTag) inlineTags[j];
			 String seeString  = seeTag.referencedClassName() 
				+ "#" + seeTag.referencedMemberName();
			 fullString = fullString + "<A HREF = " + seeString  + ">" + 
					        seeTag.label() + "</A> "; 
			 }
		 }
	 }
	 
	 return fullString;
 }
 
 /** Writes tag description comment. Extracts the info from the inline tags.
  */
 public static String computeDescriptionComment(Tag[] inlineTags)
 {
	 String descriptionString = "";

	 // If there are inlink, you have to cast them to see Tags and get the
	 //  referenced class, and member name plus the label.

	 for(int j=0; j< inlineTags.length; j++)
	 {
	   // if we are dealing with a text tag then just add it
	   if (inlineTags[j].name().compareTo("Text") == 0)
	   { 
	      descriptionString = descriptionString + " " + inlineTags[j].text(); 
	   }
	    else // it is an inlink tag so make the reference call
	   {  
		 SeeTag seeTag = (SeeTag) inlineTags[j];
		 String seeString  = seeTag.referencedClassName() 
				+ "#" + seeTag.referencedMemberName();
			          descriptionString = descriptionString + 
				 "<A HREF = " + seeString  + ">" + 
				 seeTag.label() + "</A> "; 
	    }
	 }

	 System.out.println("#Tags, Description Text => " + inlineTags.length + 
						" "+ descriptionString);
	 return descriptionString;	 
 }
 
  /** Writes the header of the tags section.
   */
  public static void writeTagsHeader(Writer outFile) throws IOException
  {
	writeHTML("<P> \n", outFile);
	writeHTML("<DL> \n", outFile);
  }
 
  /** Writes the footer of tags section.
   */
  public static void writeTagsFooter(Writer outFile) throws IOException
  {
	  writeHTML("</DL> \n", outFile);
	  writeHTML("<HR> \n", outFile);
	  writeHTML("<BR> \n", outFile);
  }
  
 // *********************** Write Tags *********************************
 
 /** Writes a general tag.
  */
 public static void writeGeneralTag(Doc ADoc,String label, String tagName,
									Writer outFile)
	 throws IOException
 {
 	 // Computes the deprecated text including the InlineTags.
	 String text = computeFullString(ADoc, tagName);
	 
	 // If there is nothing to show dont do it
	 if (text == "") return;
	 
	 writeHTML("<DT> \n", outFile);
	 writeHTML("<B> \n", outFile);
	 writeHTML(label + "\n", outFile);
	 writeHTML("</B>&nbsp;  \n", outFile);
	 writeHTML("<DD> \n", outFile);
	 
	 // Here goes the comment
	 writeHTML(text, outFile);
	 
	 writeHTML("</DD> \n", outFile);
	 writeHTML("<BR>", outFile);
	 writeHTML("\n", outFile);	 
 }
 
 /** Writes deprecated tags.
  */
 public static void  writeDeprecatedTags(Doc ADoc, Writer outFile)
	 throws IOException
 {
   writeGeneralTag(ADoc, "Deprecated", "@deprecated", outFile);
 }
   
 /** Writes author tags.   // Class header
   writeText(Aclass.commentText(), outFile);
  */
 public static void writeAuthorTags(Doc ADoc, Writer outFile)
	 throws IOException
 {
	 writeGeneralTag(ADoc, "Author", "@author", outFile);
 }
   
 /** Writes version tags.
  */
 public static void writeVersionTags(Doc ADoc, Writer outFile)
	 throws IOException
 {
     writeGeneralTag(ADoc, "Version", "@version", outFile);
 }
   
 /** Writes since tags.
  */
 public static void writeSinceTags(Doc ADoc, Writer outFile)
	 throws IOException
 {
	 writeGeneralTag(ADoc, "Since", "@since", outFile);
 }
  
 /** Writes return tags.
  */
 public static void writeReturnTags(Doc ADoc, Writer outFile)
	 throws IOException
 {
	 writeGeneralTag(ADoc, "Return", "@return", outFile);
 }
 
 /** Writes see tags.
  */
 public static void writeSeeTags(Doc ADoc, Writer outFile)
	 throws IOException
 {
	 SeeTag[] seeTags = ADoc.seeTags();
 }
 
 /** Writes param tags.
  */
 public static void writeParamTags(DocJTS ADoc, Writer outFile)
	 throws IOException
 {
	 String parameterComment = "";
	 ParamTag[] paramTags = (ADoc.comment()).paramTags();
	 
	 // If there are no parameters dont show anything.
	 if (paramTags.length == 0) return;
	 
	 writeHTML("<DT> <B>Parameters:</B> \n", outFile);
	 for(int i=0; i< paramTags.length; i++)
	 {
	  	 writeHTML("<DD> \n",outFile);
		 writeHTML(paramTags[i].parameterName(), outFile);
		 parameterComment = computeDescriptionComment(paramTags[i].inlineTags());
		 writeHTML(" - " + parameterComment, outFile);
		 writeHTML(" \n", outFile);
	 }	 
 }

 /** Writes the throws tags.
  */
 public static void writeThrowsTags(DocJTS ADoc, Writer outFile)
	 throws IOException
 {
	 String throwsComment = "";
	 ThrowsTag[] throwsTags = (ADoc.comment()).throwsTags();
	 
	 // If there are no parameters dont show anything.
	 if (throwsTags.length == 0) return;
	 
	 writeHTML("<DT> <B>Throws:</B> \n", outFile);
	 for(int i=0; i< throwsTags.length; i++)
	 {
	  	 writeHTML("<DD> \n", outFile);
		 writeHTML(throwsTags[i].name(), outFile);
		 throwsComment = computeDescriptionComment(throwsTags[i].inlineTags());
		 writeHTML(" - " + throwsComment, outFile);
		 writeHTML(" \n", outFile);
	 }	 
 }
 
 } // of class DocletJTS
 
 
};
