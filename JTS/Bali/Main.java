package Bali;

import JakBasic.Lang;

import Jakarta.loader.Loader ;
import Jakarta.loader.PrefixClassLoader ;

import Jakarta.util.BuildUtil;
import Jakarta.util.Dependency;
import Jakarta.util.Debug ;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.LineWriter ;
import Jakarta.util.Util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream ;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.URL ;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;

public class Main {
    private static LineWriter
        debugBali = Debug.global.getWriter ("debug.bali") ;

    private static Loader baliLoader ;	// Finds & loads Bali resources.

    private static boolean use_composer    = true;
    private static boolean yydebug = false;
    private static String languageName;	// stores LHS generator name
    public static File genLangDir;	// dir of target language

    // Parser
    private static BaliParser parser = null;

    // All code pertaining to the incrementality logic of Bali is marked with an
    // (INCR) comment.
    static boolean incremental = true;  // incremental behavior?

    // current system properties
    private static Properties curProps = new Properties();
    // system properties of last invocation
    private static Properties oldProps = new Properties();  

    // Type Equation library is stored as a set of named strings.
    private static Properties TELib;

    // This is the merged AST produced from the AST of the components.
    private static BaliInput mergedRoot;

    // Temporaries used during merge process.
    private static String options = "";
    private static String parserCode = "";
    private static Hashtable regexProds = new Hashtable();	// RegexType
    private static Rules tokenMgrDecls = new Rules();
    private static Rules JCProds = new Rules();		// JavacodeProductions
    private static Rules BaliProds = new Rules();	// & BaliProductions

    //**************************************************
    // This private class is used to store info about lexical declarations.
    //**************************************************
    private static class RegexType {
	private String id;	// StateList + type : used as Hashtable key
	private RegexSpecList _constants = new RegexSpecList();
	private RegexSpecList _nonConstants = new RegexSpecList();

	public RegexType(String id) {
	    this.id = id;
	}

	public void addConstant(RegexSpecListElem rspec) {
	    rspec.tok = new Lang.AstToken[1];
	    rspec.tok[0] = new Lang.AstToken().setParms("\n", "|", 0);
	    _constants.add(rspec);
	}

	public void addNonConstant(RegexSpecListElem rspec) {
	    rspec.tok = new Lang.AstToken[1];
	    rspec.tok[0] = new Lang.AstToken().setParms("\n", "|", 0);
	    _nonConstants.add(rspec);
	}

	public String id() { return(id); }
	public RegexSpecList constants() { return(_constants); }
	public RegexSpecList nonConstants() { return(_nonConstants); }
    }


    //************************************************** 
    // Utility method for reading a macro file and adding the (name, value)
    // pairs to a Properties table.
    //************************************************** 
    public static void loadMacros(Properties plist, String mfile_name)
        throws java.io.FileNotFoundException {
	FileInputStream fis;
	BufferedReader input;
	String line;
	String name, value;
	int start, i;
	int len;

	fis = null;
	try {
	    fis = new FileInputStream(new File(genLangDir, mfile_name));

	    input = new BufferedReader(new InputStreamReader(fis));

	    line = input.readLine();
	    while (line != null) {
		// search for #macro
		if (line.startsWith("#macro")) {
		    len = line.length();
		    for (i=6; i < len; i++)
			if (! Character.isWhitespace(line.charAt(i)))
			    break;
		    if (i == len) {
			Util.fatalError("Format error in macro file '" +
					mfile_name + "'");
		    }
		    start = i;
		    for (; i < len; i++)
			if (Character.isWhitespace(line.charAt(i)))
			    break;
		    name = line.substring(start, i);
		    if (i != len) {
			value = line.substring(i).trim();

			if (value.length() > 0) {
			    // Add to table
			    plist.put(name, value);
			    line = input.readLine();
			    continue;
			}
		    }

		    // Multi-line value
		    value = "";
		    line = input.readLine();
		    if (line == null)
			return;
		    while (! line.startsWith("#mend")) {
			value += line + "\n";
			line = input.readLine();
		    }

		    // Add to table
		    plist.put(name, value);
		    continue;
		}	// end of if

		line = input.readLine();
	    }	// end of while
	}
	catch (FileNotFoundException e) {
	    throw e;
	}
	catch (Exception e) {
	    Util.fatalError("Error processing macro file: "+mfile_name, e);
	}
    }



    // Creates a .java file that will build the new language when executed
    private static void do_buildJava() {
	CodeTemplate make_tmpl;
	PrintWriter mout;

	String compLibList = curProps.getProperty("LibList").trim();
	// "trim()" required because of an API bug. Stored properties are
	// automatically trimmed so the comparison will fail otherwise.
	String oldLibList = oldProps.getProperty("LibList");

	try {
	    String resource = "Bali/Build.java.tmpl" ;
	    make_tmpl = new CodeTemplate (baliLoader.getResource (resource)) ;
	    mout = Util.backedOutput (new File (genLangDir, "Build.java")) ;
	    make_tmpl.genCode (mout, curProps) ;
	    mout.close () ;
	} catch (IOException e) {
	    Util.fatalError ("error creating Build.java", e);
	    make_tmpl = null;
	}

	if (!compLibList.equals(oldLibList))
	    writeProperties(genLangDir);
    }

    private static void readOldProperties(File dir) {
	File propFile = new File(dir, "props.ser");
	if (!propFile.exists())
	    return;
	try {
	    FileInputStream propStream = new FileInputStream(propFile);
	    oldProps.load(propStream);
	}
	catch (IOException e) {
	    Util.warning("Cannot read properties file");
	    // Unreadable properties file: error?  Nah, just return!
	    return;
	}
    }
  
    private static void writeProperties(File dir) {
	File propFile = new File(dir, "props.ser");
	try {
	    FileOutputStream propStream = new FileOutputStream(propFile);
	    curProps.store (propStream, "Type equation properties") ;
	}
	catch (IOException e) {
	    Util.warning("Cannot create properties file");
	    // Can't write properties file: error?  Nah, just return!
	    return;
	}
    }
  
    // If a string contains backslashes (\), they must be escaped before the
    // string is written out to a .java file
    private static String quoteString(String s) {
	return BuildUtil.subst(s, "\\", "\\\\");
    }

    // Strings to be output in a bash script need to be in a different form
    // than Java strings. For instance, regardless of the platform, the path
    // separator character should always be a "/" (forward slash)
    private static String quoteStringScript(String s) {
	return BuildUtil.subst(s, File.separator, "/");
    }


    //**************************************************
    // Parse type equation and create components.
    //**************************************************
    private static Stack makeComponents(String eqn) {
	int p1, p2;	// indicies into equation string
	Stack comp;	// stores RHS components
	Component c;
	String compName;
	String compLibList;	// list of components having "libraries"
	String compLibSrcList;	// list of paths to component "libraries"
	File compLibDir;	// 'Lib' dir inside component dir

	comp = new Stack();
	p1 = 0;
	p2 = eqn.indexOf('[', p1);
	compLibList = "";
	compLibSrcList = "";
	while (p2 > 0) {
	    compName = eqn.substring(p1, p2);
	    try {
		c = new Component(compName, yydebug);
		comp.push(c);

		// Check for existence of component library
		compLibDir = new File(c.getCompDir(), "Lib");
		if (compLibDir.exists()) {
		    if (compLibList.length() == 0) {
			compLibList = "\""+ c.getName() + "\"";
			compLibSrcList = "\"" + 
			    compLibDir.getAbsolutePath() + "\"";
		    }
		    else {
			compLibList = compLibList + ", \"" + c.getName()+ "\"";
			compLibSrcList = compLibSrcList + ", \"" +
			    compLibDir.getAbsolutePath() + "\"";
		    }
		}
	    }
	    catch (InvalidComponentException e) {
		Util.error("Bad component: "+compName);
		e.printStackTrace();
		c = null;
	    }

	    // advance indicies
	    p1 = p2 + 1;
	    p2 = eqn.indexOf('[', p1);
	}

	// Last component is terminated by ']' or end of string
	p2 = eqn.indexOf(']', p1);
	compName = (p2 > 0) ? eqn.substring(p1, p2) : eqn.substring(p1);
	try {
	    comp.push(new Component(compName, yydebug));
	}
	catch (InvalidComponentException e) {
	    Util.error("Bad component: "+compName);
	    e.printStackTrace();
	}

	// "Return" library information in 'curProps' Properties. Return
	// component stack 'comp'.
	curProps.put("LibList", compLibList);
	curProps.put("LibSrcDirList", quoteString(compLibSrcList));
	return(comp);
    }


    //**************************************************
    // This method locates the start symbol (i.e. the name of the first
    // rule) for a given BaliInput hierarchy.
    //**************************************************
    private static String getStart(BaliInput root) {
	Lang.AstNode rules;	// an AstList class
	Lang.AstNode lnode;
	Production prod;
	Lang.AstToken token;

	rules = root.arg[2].arg[0];
	if (rules == null)
	    return(null);

	lnode = rules.arg[0];
	while (lnode != null) {
	    prod = (Production) lnode.arg[0];
	    if (prod instanceof BaliProduction) {
		token = (Lang.AstToken) prod.tok[0];
		return(token.tokenName());
	    }

	    lnode = lnode.right;
	}
	return(null);
    }


    //**************************************************
    // This method takes a RegExprProd and either creates a RegexType object
    // for it, or merges the "clauses" (RegexprSpecListElem's) into an
    // existing RegexType object definition. A RegexType object represents
    // a (<state list>, <token type>) tuple. Within a given RegexType,
    // we classify "clauses" by whether they're constants or not.
    //**************************************************
    private static void mergeLex(RulesElem rule) {
	RegExprProd prod;
	String id;
	RegexType rt;
	RegexSpecListElem clause;
	RegexSpecListElem next;
	RegExpr re;

	prod = (RegExprProd) rule.arg[0];
	id = prod.states + Integer.toString(prod.kind);
	rt = (RegexType) regexProds.get(id);
	if (rt == null) {
	    // Create a new RegexType and store it in the table
	    rt = new RegexType(id);
	    regexProds.put(id, rt);
	}

	// Now classify the "clauses" by constant or non-constant
	clause = (RegexSpecListElem) prod.arg[0].arg[0];
	while (clause != null) {
	    next = (RegexSpecListElem) clause.right;
	    re = (RegExpr) clause.arg[0].arg[0];
	    if (re instanceof LTokenDef) {
		if (re.arg[1] instanceof StringChoice)
		    rt.addConstant(clause);
		else
		    rt.addNonConstant(clause);
	    }
	    else
		rt.addNonConstant(clause);

	    clause = next;
	}
    }


    //**************************************************
    // This method takes a BaliProduction and either adds the production to
    // a temporary list (BaliProds) or if the rule name has already been
    // merged, then the clauses of this rule are added to the clauses of the
    // already seen BaliProduction.
    //**************************************************
    private static void mergeBaliRule(RulesElem rule) {
	String ruleName;
	RulesElem listProd;
	String listRuleName;
	TailList existingTL;
	TailList newTL;

	ruleName = rule.arg[0].tok[0].tokenName();
	listProd = (RulesElem) BaliProds.arg[0];
	while (listProd != null) {
	    if (listProd.arg[0] instanceof BaliProduction) {
		listRuleName = listProd.arg[0].tok[0].tokenName();
		if (ruleName.compareTo(listRuleName) == 0) {
		    // Found rule name match. Merge clauses.
		    existingTL = (TailList) listProd.arg[0].arg[0];
		    newTL = (TailList) rule.arg[0].arg[0];

		    // Get clause separators right
		    newTL.arg[0].tok[0] =
			new Lang.AstToken().setParms("\n\t","|",0);

// 		    newTL.arg[0].tok[0] =
// 			new Lang.AstToken().setParms("\n\t",":",0);
// 		    existingTL.arg[0].tok[0] =
// 			new Lang.AstToken().setParms("\n\t", "|", 0);

		    // Add new TL to tail of existing one.
		    existingTL.add((Lang.AstList) newTL);

		    return;
		}
	    }
	    listProd = (RulesElem) listProd.right;
	}

	// No class found. Add to list of known rules.
	BaliProds.add(rule);
    }


    //**************************************************
    // This method takes the AST passed to it and breaks it into several
    // parts (lexical vs. rules, etc.) and merges these parts into
    // temporary lists. completeMerge() must be called to concatenate the
    // temporary lists and produce a proper hierarchy in 'mergedRoot'.
    //**************************************************
    private static void mergeComponent(BaliInput comp) {
	Options opts;
	ParserCode pcode;
	RulesElem rule, nextRule;
	Production prod;

	// Merge options if necessary
	opts = (Options) comp.arg[0].arg[0];
	if (opts != null)
	    options += opts.getOptions();

	// Merge parser code if necessary
	pcode = (ParserCode) comp.arg[1].arg[0];
	if (pcode != null)
	    parserCode += pcode.getParserCode();

	rule = (RulesElem) comp.arg[2].arg[0].arg[0];
	while (rule != null) {
	    nextRule = (RulesElem) rule.right;
	    prod = (Production) rule.arg[0];
	    if (prod instanceof RegExprProd) {
		// lexical spec
		mergeLex(rule);
	    }
	    else if (prod instanceof TokenMgrDecls) {
		tokenMgrDecls.add(rule);
	    }
	    else if (prod instanceof JavacodeProduction) {
		JCProds.add(rule);
	    }
	    else if (prod instanceof RequireProduction) {
		/* Ignore these; only used by new Bali. */
	    }
	    else {
		// must be a BaliProduction
		mergeBaliRule(rule);
	    }

	    rule = nextRule;
	}
    }


    //**************************************************
    // This method creates a RegExprProd for the "catch-all" token.
    //**************************************************
    private static RegExprProd createCatchAll() {
	String caStr = "TOKEN : {\n" +
	    "<IDENTIFIER: <LETTER> (<LETTER>|<DIGIT>)*>\n|\n <OTHER: ~[]> }";
	ByteArrayInputStream is = new ByteArrayInputStream(caStr.getBytes());
	RegExprProd prod;

	parser = Component.parser;
	parser.ReInit(is);
	try {
	    prod = parser.RegularExprProduction();
	}
	catch (ParseException pe) {
	    Util.fatalError(pe);
	    prod = null;
	}
	return(prod);
    }


    //**************************************************
    // This method converts a RegexType object to a RegExprProd object.
    //**************************************************
    private static RegExprProd rt2rep(RegexType regex, RegexSpecList rslist) {
	String states;
	int kind;
	int len;

	len = regex.id().length();
	if (len == 1) {
	    states = null;
	    try {
		kind = Integer.parseInt(regex.id());
	    }
	    catch (Exception e) {
		Util.fatalError("Bad regular expression kind");
		kind = 0;
	    }
	}
	else {
	    states = regex.id().substring(0, len-2);
	    try {
		kind = Integer.parseInt(regex.id().substring(len-1));
	    }
	    catch (Exception e) {
		Util.fatalError("Bad regular expression kind");
		kind = 0;
	    }
	}

	return(new RegExprProd().setParms(states, kind, false, rslist));
    }

 
    //**************************************************
    // This method merges multiple TokenMgrDecls into one. The objects to
    // merge come from the tokenMgrDecls list.
    //**************************************************
    private static TokenMgrDecls mergeTMDs() {
	TokenMgrDecls result;
	Lang.AstNode node;	// RulesElem node
	String content;

	result = (TokenMgrDecls) tokenMgrDecls.arg[0].arg[0];
	node = tokenMgrDecls.arg[0].right;
	if (node != null) {
	    content = ((Block) result.arg[0]).block;
	    while (node != null) {
		content += ((Block) node.arg[0].arg[0]).block;
		node = node.right;
	    }
	    ((Block) result.arg[0]).block = content;
	}
	return(result);
    }


    //**************************************************
    // Takes the temporary lists created by mergeComponent() and builds
    // a proper BaliInput hierarchy in 'mergedRoot'.
    //**************************************************
    private static void completeMerge() {
	Lang.AstOptNode opts = new Lang.AstOptNode();
	Lang.AstOptNode pcode = new Lang.AstOptNode();
	Lang.AstOptNode r = new Lang.AstOptNode();
	Rules rules = new Rules();
	Enumeration scanPtr;
	RegexType regex;
	RegexSpecList rslist;
	RegExprProd prod;
	TokenMgrDecls tmd;

	mergedRoot = new BaliInput().setParms(opts, pcode, r);

	if (options.length() != 0)
	    opts.setParms(new Options().setParms(options));
	else
	    opts.setParms(null);

	if (parserCode.length() != 0)
	    pcode.setParms(new ParserCode().setParms(parserCode));
	else
	    pcode.setParms(null);

	//**************************************************
	// Build an ordered list of rules.
	//**************************************************

	// Constant token definitions come first
	scanPtr = regexProds.elements();
	while (scanPtr.hasMoreElements()) {
	    regex = (RegexType) scanPtr.nextElement();
	    rslist = regex.constants();
	    if (rslist.arg[0] == null)
		continue;

	    // Need to create an RegExprProd object from the RegexType object
	    prod = rt2rep(regex, rslist);

	    // Add to rules
	    rules.add(new RulesElem().setParms(prod));
	}

	// Non-constant token definitions come next
	scanPtr = regexProds.elements();
	while (scanPtr.hasMoreElements()) {
	    regex = (RegexType) scanPtr.nextElement();
	    rslist = regex.nonConstants();
	    if (rslist.arg[0] == null)
		continue;

	    // Need to create an RegExprProd object from the RegexType object
	    prod = rt2rep(regex, rslist);

	    // Add to rules
	    rules.add(new RulesElem().setParms(prod));
	}

	// Now add a single TokenMgrDecls, if necessary
	if (tokenMgrDecls.arg[0] != null) {
	    tmd = mergeTMDs();
	    rules.add(new RulesElem().setParms(tmd));
	}

	// Add JavacodeProductions and BaliProductions
	rules.add(JCProds);
	rules.add(BaliProds);

	// Add "catch-all" token definition
	rules.add(new RulesElem().setParms(createCatchAll()));

	if (rules.arg[0] != null)
	    r.setParms(rules);
	else
	    r.setParms(null);
    }


    //**************************************************
    // This method takes a Stack of the Components associated with a given type
    // equation and merges them into the 'mergedRoot' structure. Each
    // component's BaliInput AST is processed by the mergeComponent() method
    // which sorts the AST's constructs and accumulates them in temporary
    // structures. Only after completeMerge() is called will 'mergedRoot'
    // point to a complete valid BaliInput hierarchy.
    //**************************************************
    private static void mergeComponents(Stack comp) {
	Component c;

	c = (Component) comp.firstElement();
	if (c.getAST() != null) {
	    mergeComponent(c.getAST());
	    if (comp.size() == 1) {
		completeMerge();
		SymbolTable.setStartSymbol(getStart(mergedRoot));
		return;
	    }
	}

	for (int i=1; i < comp.size(); i++) {
	    c = (Component) comp.elementAt(i);
	    if (Util.errorCount() > 0)
		return;

	    // AST can be null if component has no .b file (which is allowed)
	    if (c.getAST() == null)
		continue;

	    if (i == (comp.size()-1))
		SymbolTable.setStartSymbol(getStart(c.getAST()));

	    mergeComponent(c.getAST());
	}

	// Complete merge. At this point the mergedRoot member has a valid
	// BaliInput hierarchy.
	completeMerge();
    }


    //**************************************************
    // Check if the type equation is the same. Sets value 'TypeEquation' in
    // curProps (a class static member).
    //**************************************************
    private static void TEsame(Stack comp) {
	boolean exists = true;
	String typeEq = "#";
	String oldEq = null;
	Component c;

	for (int i=0; i < comp.size(); i++) {
	    c = (Component) comp.elementAt(i);
	    typeEq = typeEq + c.getName() + "#";
	}
	curProps.put("TypeEquation", typeEq);
	if (genLangDir.exists()) {
	    // retrieve properties of previous invocation
	    readOldProperties(genLangDir);
	    oldEq = oldProps.getProperty("TypeEquation");
	}
	else 
	    exists = false;

	if (!exists || !typeEq.equals(oldEq)) {
	    // Something is wrong. Start afresh
	    debugBali.println ("oldEq == " + oldEq) ;
	    debugBali.println ("typeEq == " + typeEq) ;
	    incremental = false;
	    if (genLangDir.exists() && ! genLangDir.delete()) {
		genLangDir.renameTo(new File(genLangDir.getAbsolutePath()+
					     System.currentTimeMillis()));
	    }
	    if (! Util.makeHierarchy (genLangDir)) {
		Util.error("Can't create directory for '"+languageName+"'");
		return;
	    }
	    writeProperties(genLangDir);
	}
    }


    //**************************************************
    // Output merged grammar file.
    //**************************************************
    private static void outputGrammar(Stack comp) {
	String [] dotbList;
	Dependency dotbDep;
	PrintWriter tmpPW;
	Component c;
	Lang.AstProperties props;
	String fname;

	dotbList = new String[comp.size()]; 
	for (int i=0; i < comp.size(); i++) {
	    c = (Component) comp.elementAt(i);
	    dotbList[i] = c.getCompDir().getAbsolutePath() + 
		File.separator + c.getName() + ".b";
	}
	// String array representation of the component list. Used for 
	// dependency checking.
	fname = genLangDir.getAbsolutePath() + File.separator + 
	    languageName + ".b";
	dotbDep = new Dependency(fname, dotbList);
	  
	// Only create a ".b" file if one of the component ".b" files has
	// changed (INCR)
	if (!dotbDep.satisfied() || !incremental) {
	    props = new Lang.AstProperties();
	    tmpPW = Util.backedOutput(new File(genLangDir, languageName+".b"));
	    props.setProperty("output", tmpPW);
	    mergedRoot.print(props);
	    tmpPW.println();
	    tmpPW.close();
	}
    }


    //**************************************************
    // Performs component composition.
    //**************************************************
    private static void compose(Stack comp) {

	// List generated .java files (except "Lang") in "compList".  Base and
	// any components with code template files will be on this list.
	//
	String compList = "kernel";

	URL sourceURL = Util.getResource ("JTS/realms/K/kernel/CodeTemplate") ;
	CodeTemplate source ;

	File targetFile = new File (genLangDir, "kernel.java") ;
	PrintWriter target = Util.backedOutput (targetFile) ;

	try {
	    source = new CodeTemplate (sourceURL) ;
	    source.genCode (target, curProps) ;
	    target.close () ;
	} catch (IOException e) {
	    Util.fatalError("error generating kernel.java", e);
	    System.exit(583);
	}
	
	//**************************************************
	// Instantiate component template files in language directory.
	// NON-COMPOSER
	//**************************************************

	compList = compList + " Base";
	curProps.put("ParentComponent", "Base");

	while (! comp.empty()) {
	    Component c = (Component) comp.pop ();

	    System.err.println ("component: " + c.getCompPath()) ;
	    sourceURL = Util.getResource (c.getCompPath() + "/CodeTemplate") ;

	    try {
		source = new CodeTemplate (sourceURL) ;
	    } catch (IOException except) {
		source = null ;		
	    }

	    if (source != null) {

		// Add to compList only if there is a CodeTemplate resource:
		//
		compList = compList + " " + c.getName();

		targetFile = new File (genLangDir, c.getName () + ".java") ;
		target = Util.backedOutput (targetFile) ;

		source.genCode (target, curProps);
		target.close () ;

		curProps.put("ParentComponent", c.getName());
	    }
	}

	curProps.put("ComponentList", compList);

	// Create language "component", if it doesn't already exist:
	//
	targetFile = new File (genLangDir, "Lang.java") ;
	if (! targetFile.exists() || ! incremental) {
	    target = Util.backedOutput (targetFile) ;

	    target.println ("package " + languageName + " ;") ;
	    target.println () ;

	    String parent = curProps.getProperty ("ParentComponent") ;
	    target.println ("public class Lang extends " + parent + " {}") ;
	    target.close();
	}
    }


    //**************************************************
    // This method parses a type equation and produces a parser for the
    // generator.
    //**************************************************
    private static void handleTypeEquation(String lname, String eqn) {
	Stack comp;		// stores RHS components
	String compLibList;	// list of components having "libraries"
	String compLibSrcList;	// list of paths to component "libraries"
	String dir;
	File compInput = null;	// input file for composer
	Lang.AstProperties props = new Lang.AstProperties();
	PrintWriter src;

	//**************************************************
	// Parse type equation and create components.
	//**************************************************
	languageName = lname;
	comp = makeComponents(eqn);

	if (Util.errorCount() != 0) {
	    mergedRoot = null;
	    return;
	}

	//**************************************************
	// Now we have all components on the stack. Merge them.
	//**************************************************
	mergeComponents(comp);

	//**************************************************
	// Populate symbol table
	//**************************************************
	mergedRoot.populateSymbolTable();

	if (Util.errorCount() > 0)
	    return;

	//**************************************************
	// Typecheck the AST for validity
	//**************************************************
	mergedRoot.typecheck();

	if (Util.errorCount() > 0)
	    Util.fatalError("Type Check Errors... " +
			    "further processing aborted\n\n");

	//**************************************************
	// Create the project directory in 'languages'.
	//**************************************************
	dir = System.getProperty("LANG.dir");

	if (dir != null)
	    genLangDir = new File (dir, languageName) ;
	else {
	    String resource = "JTS/languages/" + languageName ;
	    genLangDir = Util.getResourceDirectory (resource, true) ;
	}

	if (! genLangDir.exists ())
		Util.makeHierarchy (genLangDir) ;

	if (genLangDir == null || ! genLangDir.isDirectory ()) {
	    Util.error ("no language directory found for " + languageName) ;
	    genLangDir = null ;
	    return ;
	}

	//**************************************************
	// Check if the type equation is the same
	//**************************************************
	TEsame(comp);

	//**************************************************
	// Output merged grammar file
	//**************************************************
	outputGrammar(comp);

	curProps.put("StartSymbolClass", SymbolTable.getStartSymbol());
	curProps.put("LanguageName", languageName);
	curProps.put("ParentComponent", "kernel");

	//**************************************************
	// Instantiate Kernel - NON-COMPOSER
	//**************************************************
	if (! use_composer)
	    compose(comp);
	else {
	    // Set up to call composer - don't call just yet though.
	    String genLangParent;
	    PrintWriter input;
	    int i, count;
	    String realmName;
	    File tmplSrc;
	    Component c;

	    genLangParent = Util.getParentDir(genLangDir);
	    if (genLangParent.length() == 0)
		Util.fatalError("Can't find parent directory of " +
				genLangDir.getAbsolutePath());
	    compInput = new File(genLangParent, "compInput");
	    input = Util.backedOutput(compInput);
	    if (comp.size() > 0)
		realmName = ((Component) comp.elementAt(0)).getRealmName();
	    else
		realmName = "J";
	    input.print("typeEquation " + realmName + " " + languageName +
			".Lang = ");
	    count = 0;
	    for (i=0; i < comp.size(); i++) {
		c = (Component) comp.elementAt(i);
// 		tmplSrc = new File(c.getCompDir(), c.getName() + ".ser");
		tmplSrc = new File(c.getCompDir(), c.getName() + ".layer");
		if (! tmplSrc.exists())
		    continue;
		count++;
		input.print(c.getName() + "(");
	    }
	    input.print("Base(kernel())");
	    for (i=0; i < count; i++)
		input.print(")");
	    input.println(";");
	    input.close();
	}

	//**************************************************
	// Create build script (even if they didn't ask for you to).
	//**************************************************
	do_buildJava();

	//**************************************************
	// Generate source and run tools and compile if able.
	//**************************************************

	props.setProperty("LanguageName", languageName);

	// Generate Base.java
	File base = new File(genLangDir, "Base.java");
	if (! (base.exists() && incremental)) {
	    src = Util.backedOutput(new File(genLangDir, "Base.java"));
	    props.setProperty("output", src);

	    src.println("package " + languageName + ";");
	    src.println("public class Base extends kernel {");
	    SymbolTable.getInstance().genBase(props);
	    src.println("}");

	    props.removeProperty("output");
	    src.close();
	}

	// Generate .jj file
	//
	props.setProperty ("LanguageDirectory", genLangDir) ;
	props.setProperty ("ResourceLoader", baliLoader) ;
	mergedRoot.genParserSource (props) ;

	// Call JavaCC

	//**************************************************
	// If using composer, call it here, after Base.java has been
	// generated from the GENERATE_SOURCE callback above.
	//**************************************************
	if (use_composer) {
	    String[] args = new String[1];
	    args[0] = compInput.getAbsolutePath();
	    JakBasic.Main.main(args);
	}
    }



    //**************************************************
    // Print program usage
    //**************************************************
    public static void usage() {
	System.err.print("Usage: java Bali.Main [options] files | type_equation\n");
// 	System.err.print("      -d  debug parse\n");
	System.err.println("	-c  have Bali perform component composition");
	System.exit(1);
    };


    //**************************************************
    // Main
    //**************************************************
    public static void main(String[] args) {
	int argc = args.length;
	int non_switch_args;
	int i, j;
	int len;
	char ch;
	File pdir;
	Component component;
	String eqn;

	baliLoader = new PrefixClassLoader ("Bali/") ;

	if (argc < 1)
	    usage();

	non_switch_args = 0;
	for (i=0; i<argc; i++) {
	    if (args[i].charAt(0) == '-') {
		// switch or specifying stdin
		len = args[i].length();
		if (len == 1)
		    usage();
		for (j=1; j < len; j++) {
		    ch = args[i].charAt(j);
		    switch (ch) {
// 		    case 'd':
// 			yydebug = true;
// 			break;
		    case 'c':
			use_composer = false;
			break;
		    default:
			usage();
		    }
		}
	    }
	    else {
		// non-switch arg
		args[non_switch_args] = args[i];
		non_switch_args++;
	    }
	}

	if (non_switch_args != 1)
	    usage();

	i = args[0].indexOf("=");
	if (i > 0)
	    handleTypeEquation(args[0].substring(0, i),
			       args[0].substring(i+1));
	else {
	    eqn = null;
	    if (TELib == null)
		initTELib();
	    if (TELib != null)
		eqn = TELib.getProperty(args[0]);
	    if (eqn == null) {
		Util.fatalError("Invalid type equation: "+args[0]);
	    }
	    handleTypeEquation(args[0], eqn);
	}

	if (Util.errorCount () > 0)
	    Util.fatalError ("compilation aborted due to errors") ;
    }


    /**
     * Initialize the type equation properties.  The type equations are stored
     * in a text resource named "Bali/TELib".
     **/
    private static void initTELib() {
	Properties lib ;

	try {
	    URL resource = baliLoader.getResource ("Bali/TELib") ;
	    InputStream input = resource.openStream () ;
	    lib = TELib != null ? new Properties(TELib) : new Properties() ;
	    lib.load (input) ;
	    input.close () ;
	    TELib = lib ;
	} catch (IOException e) {
	    Util.warning ("error reading resource Bali/TELib") ;
	}
    }
}
