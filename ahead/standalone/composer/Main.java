package composer ;

import composer.algebra.AlgebraException ;
import composer.algebra.AlgebraFactory ;
import composer.algebra.Function ;
import composer.algebra.Unit ;

import composer.args.ArgumentException ;
import composer.args.Arguments ;
import composer.args.Handler ;
import composer.args.Handlers ;
import composer.args.Parser ;

import composer.unit.EquationFileUnit ;
import composer.unit.EquationFileUnitComposeFunction ;
import composer.unit.EquationsFileUnit ;
import composer.unit.FileUnitComposeFunction ;

import Jakarta.util.ExitError ;

import java.io.File ;
import java.io.FileOutputStream ;
import java.io.InputStream ;
import java.io.IOException ;

import java.lang.reflect.Constructor ;
import java.lang.reflect.InvocationTargetException ;

import java.net.URI ;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Map ;
import java.util.Properties ;

import java.util.logging.Level ;
import java.util.logging.Logger ;

public final class Main {

    public static final String COPYRIGHT =
        "(C) 2002-2003 The University of Texas at Austin" ;

    public static final ClassLoader LOADER = new ClassLoader () {
	/* Empty body; will use only inherited implementation. */
    } ;

    public static final String NAME = "composer" ;
    public static final Logger LOGGER = Logger.getLogger (NAME) ;
    public static final LogHandler LOG_HANDLER = new LogHandler (NAME) ;
    public static final String RESOURCE = NAME + ".properties" ;
    public static final String UNIT_FACTORY = NAME + ".unit.Factory" ;
    public static final String VERSION = "v2003.04.15" ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private static void abort (String message, Throwable thrown) {
	thrown.printStackTrace (System.err) ;
	LOGGER.log (Level.SEVERE, message, thrown) ;
	exit (1) ;
    }

    final private static void abort (String message) {
	LOGGER.log (Level.SEVERE, message) ;
	exit (1) ;
    }

    public static final void main (String[] argsMain) {

	LOGGER.setLevel (Level.WARNING) ;
	LOGGER.setUseParentHandlers (false) ;
	LOGGER.addHandler (LOG_HANDLER) ;

	Properties appProperties =
	    initProperties (System.getProperty(RESOURCE,RESOURCE).trim ()) ;

	Arguments args = argsParse (argsMain) ;

	modelProperties (appProperties) ;

	File equation = equationFile ((String) args.getOption ("equation")) ;
	LOGGER.fine ("equation file " + equation) ;

	File target = targetFile ((String)args.getOption("target"), equation) ;
	LOGGER.fine ("target " + target) ;

	// Set the layer name, handling deprecated argument "-aspect":
	// (option "-layer" now serves that role)
	//
	String aspectName = (String) args.getOption ("aspect") ;
	if (aspectName.length () > 0)
	    LOGGER.warning (
		"option \"-aspect\" is deprecated; use \"-layer\" instead"
	    ) ;

	String layerName = (String) args.getOption ("layer") ;
	if (layerName.length () < 1)
	    layerName = aspectName ;

	setBaseLayer (layerName, equation, target) ;

	// Extract the operands list.  If the operands come from an equations
	// file, the "this" argument may be used:
	//
	String thisName = (String) args.getOption ("this") ;

	List operands = operandsList (args.operands (), equation, thisName) ;
	if (operands.size () < 1)
	    usage ("at least one operand must be specified") ;

	// Severe errors up until now cause an early exit:
	// (e.g., an operand couldn't be found)
	//
	if (LOG_HANDLER.getSevere () > 0)
	    exit (1) ;	// Assumes error messages already provided.

	// Indicates that no real file-level compositions are done:
	//
	boolean noInvoke = argToggle (args, "no-invoke") ;

	// For debugging:  Should stack traces be provided on errors?
	//
	boolean stackTrace = argToggle (args, "stack-trace") ;

	// Save the equation, if requested:
	//
	String saveFileName = (String) args.getOption ("save-equation") ;
	if (saveFileName.length() > 0) {
	    try {
		EquationFileUnitComposeFunction.writeEquation (
		    operands,
		    new FileOutputStream (saveFileName),
		    null
		) ;
		LOGGER.info ("saved equation on \"" + saveFileName + '"') ;
	    } catch (Exception e) {
		if (stackTrace)
		    e.printStackTrace (System.err) ;
		LOGGER.warning ("can't save equation; " + e.getMessage()) ;
	    }
	}

	try {
	    // Create a factory to handle sources and to compose target:
	    //
	    AlgebraFactory factory = createFactory (UNIT_FACTORY) ;

	    if (factory instanceof composer.unit.Factory) {
		File model = Path.getInstance().getMatchedDirectory () ;
		if (model == null)
		    model = new File (".") ;
		((composer.unit.Factory) factory) . setNoInvoke (noInvoke) ;
		((composer.unit.Factory) factory) . setModel (model) ;
		((composer.unit.Factory) factory) . setTarget (target) ;
	    }

	    // Is composer writing a trace of compositions?
	    //
	    String equationsFile = (String) args.getOption("write-equations") ;
	    FileUnitComposeFunction.setOutput (equationsFile) ;

	    List sources = new ArrayList (operands.size ()) ;
	    for (Iterator p = operands.iterator () ; p.hasNext () ; ) {
		File sourceFile = (File) p.next () ;
		Checks.inputFile (sourceFile) ;
		sources.add (factory.getUnit (sourceFile)) ;
	    }

	    try {
		Function function = factory.getFunction ("compose", sources) ;
		function.invoke (factory.getUnit (target)) ;
	    } catch (ExitError error) {
		if (stackTrace)
		    error.printStackTrace (System.err) ;
		LOGGER.severe (error.getMessage ()) ;
	    }

	    FileUnitComposeFunction.endOutput () ;

	} catch (Exception exception) {
	    if (stackTrace)
		exception.printStackTrace (System.err) ;
	    FileUnitComposeFunction.endOutput () ;
	    abort ("failed; " + exception) ;

	} catch (Throwable thrown) {
	    if (stackTrace)
		thrown.printStackTrace (System.err) ;
	    LOGGER.severe ("internal error; is composer.properties correct?") ;
	    FileUnitComposeFunction.endOutput () ;
	    abort ("internal error; " + thrown) ;
	}

	if (! noInvoke)
	    antBuild (target) ;

	exit (0) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private static material below this line:                              */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private static final void antBuild (File target) {

	// Running "ant" only makes sense for directory compositions:
	//
	if (! target.isDirectory ())
	    return ;

	// If errors occurred, there's probably not enough for a useful build:
	//
	if (LOG_HANDLER.getSevere () > 0)
	    return ;

	// Can't run "ant" if there's no build file!
	//
	File antFile = new File (target, "build.xml") ;
	if (! antFile.exists ())
	    return ;

	// Get a path to the "Ant" program (null means not to use Ant):
	//
	String antPath = System.getProperty ("tool.ant") ;
	if (antPath == null || antPath.length () < 1)
	    return ;
	antPath = antPath.trim () ;

	String[] cmd =
	    new String[]
	    {"env", "--", antPath, "-buildfile", antFile.toString()} ;

	LOGGER.info (antPath + " -buildfile " + antFile.toString ()) ;

	try {
	    Command runner = new Command (cmd) ;
	    int status = runner.waitFor () ;
	    if (status != 0)
		abort ("ant build exited with errors") ;
	} catch (IOException exception) {
	    abort ("exception occurred during ant build", exception) ;
	} catch (InterruptedException exception) {
	    abort ("exception occurred during ant build", exception) ;
	}

	return ;
    }

    private static final Arguments argsParse (String[] argsArray) {

	Parser parser = new Parser (Handlers.stringHandler ()) ;
	parser.addHandler ("ant", Handlers.stringHandler ("")) ;
	parser.addHandler ("aspect", Handlers.stringHandler ("")) ;
	parser.addHandler ("base", Handlers.stringHandler ("")) ;
	parser.addHandler ("equation", Handlers.stringHandler ("")) ;
	parser.addHandler ("help", USAGE_HANDLER) ;
	parser.addHandler ("ignore", Handlers.stringHandler ("")) ;
	parser.addHandler ("layer", Handlers.stringHandler ("")) ;
	parser.addHandler ("logging", Handlers.stringHandler ("WARNING")) ;
	parser.addHandler ("model", Handlers.stringHandler (".")) ;
	parser.addHandler ("no-invoke", Handlers.booleanHandler (false)) ;
	parser.addHandler ("save-equation", Handlers.stringHandler ("")) ;
	parser.addHandler ("stack-trace", Handlers.booleanHandler (false)) ;
	parser.addHandler ("target", Handlers.stringHandler ("")) ;
	parser.addHandler ("this", Handlers.stringHandler ("")) ;
	parser.addHandler ("write-equations", Handlers.stringHandler ("")) ;

	Arguments args ;
	try {
	    args = parser.parse (argsArray) ;

	} catch (ArgumentException except) {
	    usage (except.toString ()) ;
	    return null ;
	}

	// Set the logging level from optional --logging argument:
	//
	String level = (String) args.getOption ("logging") ;
	try {
	    LOGGER.setLevel (Level.parse (level.toUpperCase ())) ;
	} catch (IllegalArgumentException exception) {
	    usage ("\"" + level + "\" is an invalid logging level") ;
	}

	propertyArgument ("tool.ant",             args.getOption ("ant")) ;
	propertyArgument ("composer.file.ignore", args.getOption ("ignore"));
	propertyArgument ("composer.model.path",  args.getOption ("model")) ;

	// Handle deprecated argument "-base":
	//
	String base = (String) args.getOption ("base") ;
	if (base != null && base.length () > 0) {

	    LOGGER.warning (
		"option \"-base\" is deprecated; use \"-model\" instead"
	    ) ;

	    String path = System.getProperty ("composer.model.path") ;

	    propertyArgument (
		"composer.model.path",
		IO.getCanonicalFile (new File (base)) . getPath ()
		+ ((path != null) ? File.pathSeparator + path : "")
	    ) ;

	}

	return args ;
    }

    final private static boolean argToggle (Arguments args, String option) {
	Boolean object = (Boolean) args.getOption (option) ;
	boolean value = object.booleanValue () ;
	LOGGER.fine (option + " option is " + value) ;
	return value ;
    }

    final static AlgebraFactory createFactory (String className)
    throws ClassNotFoundException, IllegalAccessException,
	   InstantiationException, InvocationTargetException {

	String name = System.getProperty(UNIT_FACTORY,UNIT_FACTORY).trim () ;
	Class factoryClass = LOADER.loadClass (name) ;
    
	try {
	    Constructor constructor = factoryClass.getConstructor (
		new Class[] {ClassLoader.class}
	    ) ;

	    return (AlgebraFactory) constructor.newInstance (
		new Object[] {LOADER}
	    ) ;

	} catch (NoSuchMethodException except) {
	    /* Ignored -- will try using newInstance below. */
	}

	return (AlgebraFactory) factoryClass.newInstance () ;
    }

    private static final File equationFile (String base) {

	if (base == null || base.length () < 1)
	    return null ;

	try {
	    File file = new File (base + ".equation") ;
	    if (file.exists ())
		return Checks.inputNonDirectory (file) ;

	    file = new File (base + ".equations") ;
	    if (file.exists ())
		return Checks.inputNonDirectory (file) ;

	    file = new File (base) ;
	    if (file.exists ())
		return Checks.inputNonDirectory (file) ;

	    usage ("file \"" + base + "\" is not an equation file") ;

	} catch (FileException exception) {
	    usage (exception.toString ()) ;
	}

	return null ;
    }

    final private static void exit (int status) {
	exit (status, LOG_HANDLER) ;
    }

    final static void exit (int status, LogHandler handler) {

	int severe = handler.getSevere () ;
	int warning = handler.getWarning () ;
	StringBuffer buffer = new StringBuffer () ;

	if (warning > 0) {
	    buffer . append (warning) . append (" warning") ;
	    if (warning > 1)
		buffer.append ('s') ;
	}

	if (warning > 0 && severe > 0)
	    buffer.append (" and ") ;

	if (severe > 0) {
	    buffer . append (severe) . append (" error") ;
	    if (severe > 1)
		buffer.append ('s') ;
	}

	if (buffer.length () > 0) {
	    buffer.append (" occurred") ;
	    LOGGER.severe (buffer.toString ()) ;
	}

	if (status != 0)
	    System.exit (status) ;

	System.exit (severe == 0 ? 0 : 1) ;
    }

    private static final List fill (int width, String message) {

	List list = new ArrayList () ;
	String rest = message.trim () ;
	while (rest.length () > width) {

	    int mark = width ;
	    while (mark > 40 && ! Character.isWhitespace (rest.charAt (mark)))
		-- mark ;

	    list.add (rest . substring (0, mark) . trim ()) ;
	    rest = rest . substring (1 + mark) . trim () ;
	}

	if (rest.length () > 0)
	    list.add (rest) ;

	return list ;
    }

    private static final boolean hasSuper (List strings) {

	for (Iterator p = strings.iterator () ; p.hasNext () ; )
	    if ("super".equals (p.next ()))
		return true ;

	return false ;
    }

    /**
     * Updates system properties to include defaults for new properties defined
     * in sources named <code>name</code>.  Any properties already defined in
     * the system properties will <em>not</em> be overridden.  After this
     * method returns, the system Properties object will have been changed to a
     * new object with all the properties copied over, but with defaults taken
     * from a Properties object defined in this method.
     *
     * @return new default Properties object.
     **/
    final static Properties initProperties (String name) {

	// Create an application Properties object to fill with definitions
	// in a specific order:
	//
	Properties appProperties = new Properties () ;

	try {
	    InputStream stream = Main.class.getResourceAsStream (name) ;
	    if (stream == null) {
		ClassLoader loader = Main.class.getClassLoader () ;
		stream = loader.getResourceAsStream (name) ;
	    }
	    IO.loadProperties (stream, appProperties) ;
	    LOGGER.fine ("properties loaded from resource \"" + name + '"') ;
	    stream.close () ;
	} catch (NullPointerException except) {
	    LOGGER.warning ("resource \"" + name + "\" not found") ;
	} catch (IOException except) {
	    abort ("error loading resource \"" + name + '"', except) ;
	}

	loadProperties (
	    new File (System.getProperty("user.home",".").trim(), "." + name) ,
	    appProperties
	) ;

	// Create a new system Properties object containing the current system
	// properties, but using the application Properties as defaults.  This
	// way, properties defined on the command line (which show up as system
	// properties) override application properties defined via relatively
	// static sources such as resources and files.
	//
	Properties newSystem = new Properties (appProperties) ;
	newSystem.putAll (System.getProperties ()) ;
	System.setProperties (newSystem) ;

	return appProperties ;
    }

    final private static void loadProperties (File file, Properties props) {

	try {
	    IO.loadProperties (file, props) ;
	    LOGGER.fine ("properties loaded from file \"" + file + '"') ;
	} catch (FileException except) {
	    LOGGER.fine (except.toString ()) ;
	} catch (IOException except) {
	    abort ("error while loading file \"" + file + '"', except) ;
	}
    }

    /**
     * Updates a {@link Properties} object with additional properties loaded
     * from a properties file found via the model search path.
     **/
    final private static void modelProperties (Properties properties) {

	File file = Path.getInstance().resolve (RESOURCE) ;
	if (file != null && file.isFile ())
	    loadProperties (file, properties) ;
    }

    private static final List operandsList
    (List operands, File equation, String thisName) {

	if (equation == null)
	    return operandsResolve (operands) ;

	List contents = null ;
	try {
	    if (equation.getName().endsWith (".equations"))
		contents = EquationsFileUnit.readEquation (equation,thisName) ;
	    else
		contents = EquationFileUnit.readEquation (equation) ;
	} catch (Exception exception) {
	    abort (exception.getMessage ()) ;
	}

	if (operands == null || operands.size () < 1)
	    return operandsResolve (contents) ;

	boolean contentsSuper = hasSuper (contents) ;
	boolean operandsSuper = hasSuper (operands) ;

	if (contentsSuper && operandsSuper)
	    usage ("both equation and operands specify \"super\"") ;

	if (contentsSuper)
	    return operandsResolve (
		EquationFileUnit.superSubstitute (operands, contents)
	    ) ;

	if (operandsSuper)
	    return operandsResolve (
		EquationFileUnit.superSubstitute (contents, operands)
	    ) ;

	LOGGER.warning ("neither equation nor operands specify \"super\"") ;
	return operandsResolve (contents) ;
    }

    final static List operandsResolve (final List operands) {
	
	Path path = Path.getInstance () ;

	List files = new ArrayList () ;
	for (Iterator p = operands.iterator () ; p.hasNext () ; ) {

	    String name = (String) p.next () ;
	    File operand = Path.getInstance().resolve (name) ;
	    if (operand != null)
		files.add (operand) ;
	    else
		LOGGER.severe ("operand \"" + name + "\" not found") ;
	}

	return files ;
    }

    private static final File parentFile (File file) {
	return
	    (file.getParentFile () != null)
	    ? file.getParentFile ()
	    : IO.getCanonicalFile(file).getParentFile () ;
    }

    private static final void print (String message) {
	System.out.print (message) ;
    }

    private static final void println (String message) {
	System.out.println (message) ;
    }

    private static final void println () {
	System.out.println () ;
    }

    private static final void propertyArgument (String key, Object value) {

	if (value == null)
	    return ;

	String stringValue = (String) value ;
	if (stringValue.length () > 0) {
	    System.setProperty (key, stringValue) ;
	    LOGGER.fine ("property " + key + " <- " + value) ;
	}

	return ;
    }

    private static final void setBaseLayer
    (String layer, final File equation, final File target) {

	final String aspectProperty = "composer.aspect.base" ;
	final String layerProperty = "composer.layer.base" ;

	// Set the layer base name from the first of the following:
	// (*) "layer" argument;
	// (*) "composer.layer.base" property;
	// (*) "composer.aspect.base" property (deprecated);
	// (*) "equation" argument;
	// (*) "target" argument.
	//
	if (layer == null || layer.length () < 1)
	    layer = System.getProperty (layerProperty) ;

	if (layer == null || layer.length () < 1) {
	    layer = System.getProperty (aspectProperty) ;
	    if (layer != null)
		LOGGER.warning (
		    "property \""
		    + aspectProperty
		    + "\" is deprecated, "
		    + "use \""
		    + layerProperty
		    + "\" instead"
		) ;
	}

	if (layer == null || layer.length () < 1) {
	    layer = target.getName () ;
	    int period = layer.indexOf ('.') ;
	    if (period > 0)
		layer = layer.substring (0, period) ;
	}

	if (layer == null || layer.length () < 1) {
	    layer = equation.getName () ;
	    int period = layer.indexOf ('.') ;
	    if (period > 0)
		layer = layer.substring (0, period) ;
	}

	if (layer != null && layer.length () > 0)
	    layer = layer.trim () ;

	if (layer != null && layer.length () > 0)
	    propertyArgument (layerProperty, layer) ;
    }

    private static final File targetFile (String targetName, File equation) {

	if (targetName != null && targetName.length () > 0)
	    try {
		return Checks.outputFile (new File (targetName)) ;
	    } catch (FileException exception) {
		usage (exception.toString ()) ;
	    }

	if (equation == null)
	    usage ("option \"equation\" or \"target\" must be specified") ;

	String name = equation.getName () ;
	int period = name.lastIndexOf ('.') ;

	if (period == 0) {
	    File parent = parentFile (equation) ;
	    if (parent != null)
		return parent ;
	}

	if (period > 0)
	    try {
		String target = name.substring (0, period) ;
		return Checks.outputFile (new File (target)) ;
	    } catch (FileException exception) {
		abort (exception.toString (), exception) ;
	    }

	usage ("option \"target\" can't be derived from equation") ;
	return null ;
    }

    final private static void usage (String message) {
	usage_print () ;
	println () ;
	LOGGER.log (Level.SEVERE, message) ;
	exit (0) ;
    }

    final private static void usage () {
	usage_print () ;
	exit (0) ;
    }

    final static void usage_option (String option, String message) {

	final int INDENT = 4 ;
	final String PREFIX = "  " + option ;
	final String SPACES = "                                          " ;

	Iterator lines = fill (USAGE_FILL - INDENT, message) . iterator () ;

	println () ;
	print (PREFIX) ;
	if (lines.hasNext () && PREFIX.length () < INDENT) {
	    print (SPACES.substring (0, INDENT - PREFIX.length ())) ;
	    print (lines.next().toString ()) ;
	}
	println () ;

	while (lines.hasNext ())
	    println (SPACES.substring (0, INDENT) + lines.next ()) ;
    }

    final static void usage_paragraph (String message) {
	println () ;
	Iterator lines = fill (USAGE_FILL, message) . iterator () ;
	while (lines.hasNext ())
	    println (lines.next().toString ()) ;
    }

    final private static void usage_print () {

	println ("JTS " + NAME + ' ' + VERSION + ' ' + COPYRIGHT) ;
	println () ;
	println ("Usage: " + NAME + " [<option> ...] <source> ...") ;

	usage_paragraph (
	    "where a <source> is any existing file or directory and an"
	    + " <option> is one of the following:"
	) ;
	
	usage_option (
	    "--ant=<ant-executable> (default is \"ant\")",
	    "Specifies the name for the Ant program.  When Ant is executed,"
	    + " it is invoked by using this name in the standard search for"
	    + " system executables.  How this is done is system-dependent."
	    + " This option may also be specified as the value of the"
	    + " \"tool.ant\" property"
	    + " in a \"" + RESOURCE + "\" file."
	) ;

	usage_option (
	    "--equation=<equation-file> (default is to use no equation file)",
	    "Specifies an equation file from which the target and <source>"
	    + " operands can be derived.  The base name of the file is all"
	    + " that's needed.  An extension, either \".equation\" or"
	    + " \".equations\", will be tried in that order.  If no --target"
	    + " option is provided, the base name of the equation file is also"
	    + " used as the target."
	) ;

	usage_option (
	    "--help",
	    "Prints this helpful message, then exits."
	) ;

	usage_option (
	    "--ignore=<ignore-pattern> (default in composer.properties)",
	    "Specifies which files are to be ignored.  The argument value is"
	    + " a pattern as described in the documentation for"
	    + " \"java.util.regex.Pattern\" in the Java API.  Any filename"
	    + " that matches this pattern will be ignored.  Instead of using"
	    + " this command-line option, it's more general and probably more"
	    + " convenient to define \"composer.file.ignore\""
	    + " and \"composer.directory.ignore\""
	    + " in a \"" + RESOURCE + "\" file."
	) ;

	usage_option (
	    "--layer=<base-layer-name> (default is to use no base name)",
	    "Specifies the initial prefix of generated package names"
	    + " when composing Jak files.  If the composition is part of a"
	    + " directory composition, the complete package names will be"
	    + " formed by appending the relative path to each target Jak file."
	) ;

	usage_option (
	    "--logging=<level> (default level is \"warning\")",
	    "Selects how much detail to report during execution.  As per"
	    + " java.util.logging (see method \"Level.parse\"), <level> is"
	    + " one of \"off\", \"severe\", \"warning\", \"info\", \"fine\""
	    + " or \"all\".  Other values are specified in java.util.logging,"
	    + " but are not meaningful in this program."
	) ;

	usage_option (
	    "--model=<directory-path> (default is the current directory)",
	    "Specifies a search path for locating model files such as"
	    + " \"" + RESOURCE + "\", <source> operands and Velocity template"
	    + " files.  If multiple directories are specified, they must be"
	    + " separated by \"" + File.pathSeparatorChar + "\" characters."
	    + " For a smidgen of portability, an operand located via the model"
	    + " path may be specified either as a file URI or as a"
	    + " system-dependent file name."
	    + " This option may also be specified as the value of the"
	    + " \"composer.model.path\" property"
	    + " in a \"" + RESOURCE + "\" file."
	) ;

	usage_option (
	    "--no-invoke (default is false)",
	    "Disables file compositions, but not directory compositions.  If"
	    + " a directory composition is being done, then the directories"
	    + " will still be composed to find all nested file compositions."
	    + " However, the file compositions themselves will not be"
	    + " performed.  This option is provided for use in"
	    + " combination with option \"--write-equations\" so that an"
	    + " equations file can be generated without also generating"
	    + " a target."
	) ;

	usage_option (
	    "--target=<file-or-directory> (required if --equation not given)",
	    "Designates a destination for the composition.  If the <source>"
	    + " operands are files this will be a file and, if the operands"
	    + " are directories, this will be a directory.  If it already"
	    + " exists, its contents will be overwritten.  If it doesn't yet"
	    + " exist, it will be created."
	) ;

	usage_option (
	    "--this=<equation-name> (default is \"this\")",
	    "Specifies an equation in a \".equations\" file.  This option is"
	    + " only useful if the \"--equation\" option specifies an"
	    + " \".equations\" file.  When specified, the equation named by"
	    + " this option is composed.  Otherwise, the default equation"
	    + " is used."
	) ;

	usage_option (
	    "--write-equations=<file> (default is to not write equations)",
	    "If this option is provided, all file composition equations"
	    + " (but not directory composition equations)"
	    + " will be written to the specified file."
	) ;
    }

    final private static int USAGE_FILL = 76 ;

    final private static Handler USAGE_HANDLER = new Handler () {

	final public Object getValue () {
	    return this ;
	}

	final public Object handle (String argument) {
	    usage () ;
	    return argument ;
	}

	final public boolean requiresValue () {
	    return false ;
	}

    } ;

}
