package Bali;

import Jakarta.util.Util;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A component class represents a Bali component. It's primary function to
 * take a name representing the component and translate it into a Bali class
 * object. The name accepted can be of one of two forms. The first form is R.C
 * where R is the realm name and C is the component name.  The second form is
 * C where just the component name is specified. In either case we look for a
 * file named C.b. If the first form is used then we search for the component
 * directory to locate this file. If the second form is used then this file is
 * expected to be in the current directory.
 **/
public class Component {
    private BaliInput root;		// obtained from parsing BaliInput NT
    private File compDir;
    private String compPath ;		// Path to component resource.
    private String[] comp;
    private String compName;
    public static BaliParser parser;

    public Component(String name, boolean yydebug)
	throws InvalidComponentException {
	File dotb;
	InputStream is;
	FileOutputStream fos;

	// Save name
	compName = name;

	// Break name into parts. NOTE: the findComponents() method
	// deals with name components (of a qualified name) not with
	// Jakarta components.
	comp = Util.findComponents(name);
	switch (comp.length) {
	case 1:
	    compDir = new File(System.getProperty("user.dir"));
	    compPath = compDir.getPath().replace (File.separatorChar, '/') ;
	    break;
	case 2:
	    // Locate the directory containing the component package. In
	    // this case the name of the component is the package name.
	    compDir = Util.findPackageDir("JTS.realms." + name);
	    compPath = "JTS/realms/" + name.replace ('.', '/') ;
	    break;
	default:
	    throw new InvalidComponentException();
	}

	if (compDir == null)
	    throw new
		InvalidComponentException("Can't find component directory");

	dotb = new File(compDir, comp[comp.length-1] + ".b");

	if (! dotb.exists()) {
	    root = null;
	    return;
	}

	// Parse .b file

	try {
	    is = new FileInputStream(dotb);
	}
	catch (IOException e) {
	    is = null;
	    throw new InvalidComponentException("Can't open "+
						dotb.getName());
	}

	if (parser == null)
	    parser = new BaliParser(is);
	else
	    // Re-initialize the parser
	    parser.ReInit(is);

	// parse the dotb file
	try {
	    if (yydebug) {
		Util.warning("Warning: can't use debug yet!");
		root = parser.BaliInput();
	    }
	    else
		root = parser.BaliInput();
	}
	catch (ParseException pe) {
	    Util.fatalError(pe);
	}
    }

    //**************************************************
    // Accessor methods
    //**************************************************

    public String getFullName() { return(compName); }

    public String getName() { return(comp[comp.length-1]); }

    public String getRealmName() {
	if (comp.length == 1)
	    return(null);
	return(comp[0]);
    }

    public BaliInput getAST() { return(root); }

    public File getCompDir() { return(compDir); }

    public String getCompPath() { return compPath ;}
}
