/** xc.java
 * Performs the composition of two XML files
 * It is based entirely on JDOM
 * AHEAD Project
 * @author Roberto E. Lopez-Herrejon
 * Last Update: June 24, 2005 3:00 pm
 */

package XC;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;
import org.jdom.transform.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.util.*;
import java.io.*;

import java.net.*;

public class Main {

    /** Final constants */
    private final  String APPEND_FILE ="append.xsl";
    private final  String XSL_COMPOSER = "XSLComposer.xsl";
    private final  String STYLESHEET_TAG = "stylesheet";
    private final  String STYLESHEET_NAMESPACE = "metaxsl";
    private final  String FUNCTION_TAG = "function";
    private final  String METAXSL = "metaxsl"; 
    private final  String XSLT_URI = "http://www.w3.org/1999/XSL/Transform";
    
    // Constant for command options handling
    private final int DEFAULT = 0;
    private final int OUTPUT = 1;
    private final int FILES = 2;
    private final int COMPOSE = 3;

    // ---------- Append Document Creation  --------------------
    private Document appendDocument;
    
    /** Initializes the append document object */
    public void initAppendDocument() {
	// @pending, original did not look to resources
	// appendDocument = parseXML(APPEND_FILE);
	
	// @test of using resources for APPEND FILE
	Loader loader = new Loader();
	appendDocument = parseXML(loader.getResource(APPEND_FILE));

    } // of initAppendDocument

    // ---------- File     manipulation --------------------
    /** Validates an XML file against its schema. It does not return a 
     * document just validates it. 
     * @param String fileName of the file to parse.
     * @return boolean true if the XML file is valid otherwise false.
     */
    public boolean validateXML(String fileName) {
	// starting it is assumed to be valid
	boolean result = true;
	
	// Builds a validating parser
	SAXBuilder builder = 
		 new SAXBuilder();
	
	// sets for validation
	builder.setValidation(true); 
	
	// turns on schema validation
	
	builder.setFeature("http://apache.org/xml/features/validation/schema",
				true);
	

	try {
		 // parses the file
		 builder.build(fileName);
		 
	} catch (JDOMException e) {
		 result = false;
		 System.out.println("Invalid file " + fileName);
		 System.out.println(e.getMessage());
	} catch (IOException e2) {
		 result = false;
		 System.out.println("Invalid file " + fileName);
		 System.out.println(e2.getMessage());
	}
	
	// Returns the validation outcome
	return result;
	
    } // of validateXML

    /** Validates an XML file against its schema. It does not return a 
     * document just validates it. As opposed to the previous method
     * it validates a given document
     * @param Document document to validate with its schema. 
     * @return boolean true if the document is valid, false if not.
     */
    public boolean validateXML(Document document) {
	// starting it is assumed to be valid
	boolean result = true;
	
	// Builds a validating parser
	SAXBuilder builder = 
		 new SAXBuilder();
	
	// sets for validation
	builder.setValidation(true); 
	
	// turns on schema validation
	
	builder.setFeature("http://apache.org/xml/features/validation/schema",
				true);

	try {
		 // Gets a bytestream from the document to use for validation
		 // Defines the XMLOutputter to generate the outputstream
		 XMLOutputter outp = new XMLOutputter();
		 
		 outp.setFormat(Format.getPrettyFormat());
		 //outp.setIndent(" ");
		 //outp.setNewlines(true);
		 
		 // Defines the byte array to dump the stream
		 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		 // Outputs the document into the stream
		 outp.output(document, outputStream);  

		 // With the resulting stream create the input stream 
		 ByteArrayInputStream inputResult = 
		new ByteArrayInputStream(outputStream.toByteArray());
		 
		 // parses the file
		 builder.build(inputResult);
		 
	} catch (JDOMException e) {
		 result = false;
		 System.out.println("Invalid document");
		 System.out.println(e.getMessage());
	} catch (IOException e2) {
		 result = false;
		 System.out.println("Invalid document");
		 System.out.println(e2.getMessage());
	}
	
	// Returns the validation outcome
	return result;
	
    } // of validateXML

    
    /** Parses an XML file with optional validation. Returns a Document with
     * with it.
     * @param String fileName of the file to parse. 
     * @return Document object of the parsed file.
     */
    public Document parseXML(String fileName) {
	
	Document XMLDocument = null;
	
	try {
		 // Builds a Document 
		 SAXBuilder builder = new SAXBuilder();
		 XMLDocument = builder.build(fileName);
	} catch (JDOMException e) {
		 e.printStackTrace();
	} catch (IOException e2) {
		 e2.printStackTrace();
	}
	
	// Returns the parsed document
	return XMLDocument;
	
    } // of parseXML

    /** Parses an XML file from a Reader object. Returns a Document with
     * with it. This is mainly used for append and XSLComposer stylesheets.
     * @param String fileName of the file to parse. 
     * @return Document object of the parsed file.
     */
    public Document parseXML(Reader file) {
	
	Document XMLDocument = null;
	
	try {
		 // Builds a Document 
		 SAXBuilder builder = new SAXBuilder();
		 XMLDocument = builder.build(file);
	} catch (JDOMException e) {
		 e.printStackTrace();
	} catch (IOException e2) {
		 e2.printStackTrace();
	}
	
	// Returns the parsed document
	return XMLDocument;
	
    } // of parseXML
    
    /** Dumps a document into a file. Uses a XMLOutputter
     * @param Document document to dump.
     * @param String outputName of the file where to dump.
     */
    public void dumpDocument(Document document, 
				  String outputName) {
	try {
		 XMLOutputter outp = new XMLOutputter();
		 
		 outp.setFormat(Format.getPrettyFormat());
		 //outp.setIndent(" ");
		 //outp.setNewlines(true);
		 outp.output(document, new FileOutputStream(outputName));

	}  catch (IOException ioe) {
		 System.out.println("IOError " + ioe.getMessage());
		 ioe.printStackTrace();
	}
	
    } // of dumpDocument
    
    /* Displays the contents of a Document. */
    public void displayDocument(Document document) {
	try{
		 XMLOutputter outp = new XMLOutputter();
		 outp.setFormat(Format.getPrettyFormat());
		 //outp.setIndent(" ");
		 //outp.setNewlines(true);

		 outp.output(document, System.out);
	} catch(java.io.IOException e) {
		 e.printStackTrace();
	}
    } // of displayDocument
    
    // ---------- XML tree manipulation --------------------
    /** Traverses recursively all the element nodes to change their URI for
     * a particular prefix namespace.
     * @param Element current correspond to the current element in the tree
     * that is modified.
     * @param String prefix that corresponds to the new URI
     * @param String URI for the new prefix.
     */
    public void changeNamespace(Element current, String prefix,
				String URI) {
	
	// Checks to see if it is of the desired prefix, if so modify
	String currentPrefix = current.getNamespacePrefix();
	if (currentPrefix.equals(prefix)) 
		 current.setNamespace(Namespace.getNamespace(currentPrefix, URI));
	
	// Recurses on the children
	List children = current.getChildren();
	Iterator iterator = children.iterator();
	while(iterator.hasNext()) {
		 Element child = (Element) iterator.next();
		 changeNamespace(child, prefix, URI);
	}
    } // of changeNamespace
    
    /** Returns whether or not a file is a function */
    public boolean isFunction(Document document) {
	
	// Gets the root of the document
	Element root = document.getRootElement();
	
	// Gets the name of the root and compares it with function
	String rootName = root.getName();
	
	// Returns the final result
	return rootName.equals(FUNCTION_TAG);
    }
    
    /** Function that transforms a given JDOM document based on a 
     * stylesheet, putting its output on another JDOM document
     * @param Document document is the JDOM source document file
     * @param Document stylesheet is the JDOM document used as stylesheet
     * @param boolean metatransformation indicates whether or not the 
     * outcome of the transformation is another XSLT file 
     * @return Document is the JDOM document result from the transformation
     */
    public Document transformXML(Document document, 
				 Document stylesheet,
				 boolean metaTransformation){
	
	// Creates the result document
	Document result = null;
	
	try {
		 // If the XSLT produces another XSLT
		 if (metaTransformation) {
		// Gets the root element
		Element root = stylesheet.getRootElement();
		
		// All the meta name spaces have to be changed
		changeNamespace(root, METAXSL,  XSLT_URI);
		 }
		 
		 // Creates a JDOM Source to be used in the transformer
		 JDOMSource stylesheetSource = new JDOMSource(stylesheet);
		 
		 // Creates the XSLT transformer
		 Transformer transformer = TransformerFactory.newInstance()
		.newTransformer(stylesheetSource);
		 
		 // Sets up the source and result of the transformation
		 JDOMSource sourceDocument = new JDOMSource(document);
		 
		 // Transforms the result into a JDOM Result object
		 JDOMResult jdomResult = new JDOMResult();
		 transformer.transform(sourceDocument, jdomResult);
		 result = jdomResult.getDocument();
		 
	} catch (TransformerConfigurationException tce) {
		 // Error generated by the parser
		 System.out.println ("\n** Transformer Factory error");
		 System.out.println("   " + tce.getMessage() );
		 
		 // Use the contained exception, if any
		 Throwable x = tce;
		 if (tce.getException() != null)
		x = tce.getException();
		 x.printStackTrace();
		 
        } catch (TransformerException te) {
		 // Error generated by the parser
		 System.out.println ("\n** Transformation error");
		 System.out.println("   " + te.getMessage() );
		 
		 // Use the contained exception, if any
		 Throwable x = te;
		 if (te.getException() != null)
		x = te.getException();
		 x.printStackTrace();
	} 

	// Returns
	return  result;
	
    } //of transformXML
    
    /** Receives a function document and returns a list of stylesheet
     * document objects.
     * @param Document 
     * @return List of Document objects that represent stylesheets
     */
    public List breakFunction(Document document) {
	// List that is returned
	LinkedList result = new LinkedList();
	
	// Gets the childen tag elements of the document
	List children = document.getRootElement().getChildren();
	Iterator iterator = children.iterator();
	Element child = null;
	
	// Name and name space of the children read
	String name, nameSpace = "";
	
	// New document to be created
	Document newDoc = null;
	
	// Reads all the stylesheets nodes from the file
	while (iterator.hasNext()) {
		 child = (Element) iterator.next();
		 name = child.getName();
		 nameSpace = (child.getNamespace()).getPrefix();
		 
		 // If a stylesheet tag has been found
		 if (name.equals(STYLESHEET_TAG) && 
		nameSpace.equals(STYLESHEET_NAMESPACE)) {
		
		// Create a new document with this tag object
		newDoc = new Document((Element)child.clone());
		
		// Adds the document to the accumulation list
		result.add(newDoc);
		
		 } // If it is a stylesheet tag
		 
	} // for all the children tags
	
	
	// Returns the accumulated list
	return result;
	
    } // of breakFunction
    
    
    /** Breaks a function into a list of stylesheets to be applied to
     * the constant.
     * @param Document function JDOM document
     * @return List of stylesheet Document objects.
     */
    public List stylesheetBreaker(Document function) {
	Document stylesheetsDocument = null;
	Document XSLComposerDocument = null;
	try {
		 // Builds a Document from the XSL composer stylesheet file
		 SAXBuilder builder = new SAXBuilder();
		 
		 // @original, uses file name 
		 // XSLComposerDocument = builder.build(XSL_COMPOSER);
		 
		 // @test of use of resources
		 Loader loader = new Loader();
		 XSLComposerDocument = 
		builder.build(loader.getResource(XSL_COMPOSER));

		 // Applies the XSLComposer transformation Document to the function
		 // Outcome: tempFunctionFile xsl file contains the transformations
		 //          to be applied to the constant
		 stylesheetsDocument = transformXML(function, XSLComposerDocument,
							 false);
		 
	} catch (JDOMException e) {
		 e.printStackTrace();
	} catch (IOException e2) {
		 e2.printStackTrace();
	}
	
	// Gets the list of stylesheet document objects based on the root
	return breakFunction(stylesheetsDocument);
	
    } // of stylesheetBreak
    
    // ---------- Composition Functions ---------------------
    /** Composes two function documents document2 (document1).
     * Deletes the temporary file used for the stylesheet generation of
     * document2.
     * @param Document document1 the inner function
     * @param Document document2 the outer function
     * @param boolean validate validates or not the outcome of the composition
     * @return Document object that contains the composition of the two 
     * functions.
     * @throws CompositionException
     */
    public Document composeFunctions(Document document1,
					  Document document2,
					  boolean validate) 
	throws CompositionException
    {
	// Transforms document2 to append it to document1, then applies it
	Document result = transformXML(document1, 
						 transformXML(document2,
							 appendDocument, 
							 false),
						 true);
	// Validates or not the outcome
	if (!validateXML(result)) {
		 throw new CompositionException("Invalid Function Function " +
						"Composition");
	}

	// returns the outcome
	return result;

    } // of composeFunctions
    
    /** Applies a function to a constant document.
     * @param Document constant
     * @param Document function
     * @param boolean validate whether the resulting constant meets 
     * the constants schema
     * @throws CompositionException to signal a problem with composition
     */
    public Document composeFunctionConstant(Document constant,
						 Document function,
						 boolean validate) 
	throws CompositionException
    {

	// Partitions the function document into several stylesheets
	// objects
	List stylesheetDocuments = stylesheetBreaker(function);
	
	// Applies the transformations one at a time to the constant file
	Iterator iterator = stylesheetDocuments.iterator();
	Element child = null;
	
	// New refinement to be applied
	Document currentRefinement = null;
	
	// Reads all the stylesheets nodes from the file
	while (iterator.hasNext()) {
		 currentRefinement = (Document) iterator.next();
		 
		 // Applies the current refinement to the constant
		 constant = transformXML(constant,currentRefinement, true);
	}
	
	// Tests to see if the result meets the constant schema
	if (validate) {
		 boolean result = validateXML(constant);
		 if (!result) 
		throw new CompositionException("Invalid Function Constant " +
							 "Composition");
	}

	// Returns the composed file
	return constant;
	
    } // of composeFunctionConstant
    
    
    /** Composes two files given their names.
     * Checks what types the files are. 
     * If they are functions then file2 is appened to file1.
     * If file1 is function and file2 constant then override by constant
     * If file1 is constant and file2 is function then compose file2(file1)
     * @param String fileName1 is the file name of the first file
     * @param String fileName2 is the file name of the second file
     * @param boolean validate to schema validate files or not by default. 
     * @return Document result of the composition in a JDOM document object.
     * Notice that all the AHEAD functions are validated by default.
     */
    public Document composeTwoFiles(String fileName1, String fileName2,
					 boolean validate) 
	throws CompositionException
    {
	
	// Parses the two files by default without validating
	Document document1 = parseXML(fileName1);
	Document document2 = parseXML(fileName2);
	
	// If validation is set and any of the two files fails then
	// signal an error and return
	if (validate && (!validateXML(fileName1) || 
					!validateXML(fileName2))) {
		 throw new CompositionException("Error: " + 
		"Composition cannot be performed between " + 
		 fileName1 + " and " + fileName2);
	}
	
	// Checks what type of files they are: constants or functions
	boolean isFunction1 = isFunction(document1);
	boolean isFunction2 = isFunction(document2);
	
	// If validation is set, then it does not matter if it is function or
	// not since they have already being validated
	// If validation is not set then we have to check that if they are
	// functions then they are validated
	if (!validate &&
		 ((isFunction1 && !validateXML(fileName1)) ||
		  (isFunction2 && !validateXML(fileName2))) ) {
		 throw new CompositionException("Error: " + 
			"Composition cannot be performed between " + 
			 fileName1 + " and " + fileName2);
	}
	
	// Result Document
	Document result = null;
	
	// Applies the corresponding composition rules
	if (!isFunction2) {
		 // If document2 is constant then it overrides either another
		 // constant or a function of the first document
		 result = document2;
	}
	else {
		 if (isFunction1) {
		// Both are functions so append f2 to f1
		result = composeFunctions(document1, document2, validate);
		
		 }else
		 { // document2 is function and document1 is constant
		// therefore apply the function
		result = composeFunctionConstant(document1, document2, 
						 validate); 
		 }
	}
	
	// Returns the document result of the composition
	return result;

    } // of composeTwoFiles

    /** Method that composes a Document object and a File, validates
     * or not depending on the boolean entry validate.
     * @param Document document1 document to compose.
     * @param File file object to compose.
     * @param boolean validate or not the composition.
     * @return Document object with the resulting composition.
     * @throws CompositionException if problems with composition.
     */
    public Document composeDocumentAndFile(Document document1, 
						File file,
						boolean validate) 
	throws CompositionException
    {
	// Parses the file
	String fileName2 = file.getPath();
	Document document2 = parseXML(fileName2);
	
	// If validation is set validate the file
	// Notice that if validation has been set, document1 has already
	// been validated.
	if (validate && !validateXML(fileName2)) {
		 throw new CompositionException("Error: " + 
		"Composition with file " + fileName2 + 
		" cannot be performed between ");
	}
	
	// Checks what type of files they are: constants or functions
	boolean isFunction1 = isFunction(document1);
	boolean isFunction2 = isFunction(document2);
	
	// If validation is set, then it does not matter if it is function or
	// not since they have already being validated
	// If validation is not set then we have to check that if they are
	// functions then they are validated
	if (!validate &&
		 ((isFunction1 && !validateXML(document1)) ||
		  (isFunction2 && !validateXML(fileName2))) ) {
		 throw new CompositionException("Error: " + 
			"Composition with file " + fileName2 + 
			"cannot be performed ");
	}

	// Result Document
	Document result = null;
	
	// Applies the corresponding composition rules
	if (!isFunction2) {
		 // If document2 is constant then it overrides either another
		 // constant or a function of the first document
		 result = document2;
	}
	else {
		 if (isFunction1) {
		// Both are functions so append f2 to f1
		result = composeFunctions(document1, document2, validate);
		
		 }else
		 { // document2 is function and document1 is constant
		// therefore apply the function
		result = composeFunctionConstant(document1, document2, 
						 validate); 
		 }
	}
	
	// Returns the document result of the composition
	return result;

    } // of composeDocumentAndFile
					

    /** Method that composes a list of File objects and returns the 
     * composed file. This method is going to be called by AHEAD composer.
     * @param List filesList is a list of File objects that are going to be 
     * composed.
     * @param boolean validate or not the composed pieces.
     * @return Document with the result of the composition.
     */
    public Document composeListFiles(List filesList, boolean validate) 
	throws CompositionException
    {
	// Starts the composition process
	// Creates the Document used for appending two functions
	initAppendDocument();

	// Traverses the list of files and gets the final composition
	Iterator iterator = filesList.iterator();
	Document currentDocument = null;
	File currentFile = null;
	File secondFile = null;
	
	// Gets the first file
	if (iterator.hasNext()) {
		 currentFile = (File)iterator.next();


		 if (iterator.hasNext()) {
		secondFile = (File)iterator.next();
		// There is first and second file so they can be composed
		currentDocument = composeTwoFiles(currentFile.getPath(),
						  secondFile.getPath(),
						  validate);
		 }else {
		// Parse the only file object in the list
		Document onlyDocument = parseXML(currentFile.getPath());
		
		// If validation is set validate it 
		if (validate) {
			 if (validateXML(currentFile.getPath())) {
			return onlyDocument;
			 } 
			 else throw 
				new CompositionException("First File validation failed");
		} else
			 return onlyDocument;
		 } // if there is also second file
	} else   // there is nothing in the list, return composition exception
		throw new CompositionException("No files selected for composition");


	
	// While there are files to compose keep composing
	while(iterator.hasNext()) {
		 currentDocument = composeDocumentAndFile(currentDocument, 
							  (File)iterator.next(),
							  validate); 
	}

	// Returns the final result
	return currentDocument;

    } // of composeListFiles

    // ---------- Command Options       ---------------------       
    /** Usage Error message */
    public void usageError() {
	System.out.println("Wrong command line usage");
	System.out.println("Usage xc [options] -o outputFile");
	System.out.println(" -c file1 file2 file3 ... ");
	System.out.println("    Composes xml files in order ...file3(file2(file1))");
	System.out.println("    Extension .xml is required");
	System.out.println(" -f file1 file2 file3 ... ");
	System.out.print  ("    Composes lists files, text files whose lines are");
        System.out.println(" xml files ");
	System.out.println("    in order ...file3(file2(file1))");
	System.out.print("    List files can have any extension. Xml files must");
        System.out.println(" have extension .xml");
    }

    /** Reads each of the files and adds their file names specified in them
     * and returns a lists with all the accumulated names.
     * @param List files
     * @return List the names of the files to compose
     */
    public List readFileNames(List fileNames) {
	// Result list with the names of the files
	LinkedList result = new LinkedList();

	// For all the file names in the list of names open the file
	// read the rows, each one of them is a file name
	// add them to the result list
	Iterator iterator = fileNames.iterator();
	try {
		 while(iterator.hasNext()) {
		// Opens the file to read it
		BufferedReader reader = 
			new BufferedReader(new FileReader((String)iterator.next()));
		
		String afile = reader.readLine();
		while(afile != null) {
			 result.add(afile);  // adds the file name read
			 afile = reader.readLine(); // gets the next name
		}
		
		// Closes the reader
		reader.close();

		 } // for all the file names
	} catch(IOException e)
	{   // The file name given was not valid, it will print the message
		 // and come back
		 System.out.println ("\n** Error while reading files ");
		 System.out.println("   " + e.getMessage() );
	}

	// Returns the accumulated list
	return result;
	
    } // of readFileNames
 
    /** Receives a list of String names of files to be composed and
     * converts them into File objects.
     * @param List files list with String file names.
     * @param boolean validate or not the composition.
     * @param String outputFile name of the file where composition is put.
     */
    public void filesOption(List files, boolean validate, String outputFile) 
    {
	// File List is a list of File Objects 
	LinkedList filesList = new LinkedList();
	Iterator iterator = files.iterator();

	// Creates a list of File objects from a list of String names
	while(iterator.hasNext()) {
		 filesList.add(new File((String)iterator.next()));
	}

	// Here the transformation has been done so proceed to dump
	try {
		 dumpDocument(composeListFiles(filesList, validate), outputFile);
	}catch (CompositionException e) {
		 System.out.println ("\n** Composition error");
		 System.out.println("   " + e.getMessage() );
	}

    } // of filesOption

    /** Processes the command line options.
     */
    public void processCommand(String[] args) {
	// Checks to see if the command line is empty
	if (args.length == 0) {
		 usageError();
		 return;
        }
	
	// Auxiliary variables
	boolean validate = false;       // validate or not the composition
	int option = DEFAULT;           // command option chosen
	String outputFile = "";
	LinkedList fileList = new LinkedList();
	LinkedList fileNames = new LinkedList();

	// Traverses the command line and get the files to be composed
	for(int i=0; i< args.length; i++) {
		 if (args[i].equals("-v")) { validate = true; continue; }
		 if (args[i].equals("-o")) { option = OUTPUT; continue; }
		 if (args[i].equals("-c")) { option = COMPOSE; continue; }
		 if (args[i].equals("-f")) { option = FILES; continue; }

		 switch(option) {
		 case OUTPUT: outputFile = args[i];
		break;
		 case COMPOSE: fileList.add(args[i]);
		break;
		 case FILES: fileNames.add(args[i]);
		break;
		 default: // Wrong usage of the command line
		usageError();
		return;
		 } // for the switch
	} // for all the arguments

	// If -t option was selected, read the file names contained in those
	// files and add them to the fileList
	if (fileNames.size()> 0)
		 fileList.addAll(readFileNames(fileNames));
	
	// Output file name must be set
	if (outputFile.equals("")) { 
		 usageError();
		 return;
	}

	// Calls for the composition of files
	filesOption(fileList, validate, outputFile);
	
    } // processCommand

    // ---------- Composition Functions ---------------------    
    /** Main method */
    public static void main(String[] args) throws Exception {

	/* Flags:
		-v validation set
		-f text filea with multiple lines
		-o output file
		-c file1 file2 file3 file4 ... // composes xml files
	 */

	Main composer = new Main();
	composer.processCommand(args);

    } // of main method
}
