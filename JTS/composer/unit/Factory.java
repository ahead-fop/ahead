package composer.unit ;

import composer.algebra.AlgebraException ;
import composer.algebra.AlgebraFactory ;
import composer.algebra.Collective ;
import composer.algebra.Function ;
import composer.algebra.Type ;
import composer.algebra.Unit ;

import composer.Checks ;
import composer.FileException ;

import java.io.File ;

import java.lang.reflect.Constructor ;
import java.lang.reflect.Method ;
import java.lang.reflect.InvocationTargetException ;

import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.WeakHashMap ;

import java.util.logging.Logger ;

final public class Factory implements AlgebraFactory {

    final public static Logger LOGGER = Logger.getLogger ("composer") ;

    public Factory (ClassLoader loader) {

	Checks.nonNullArgument (loader, "loader") ;

	this.loader = loader ;
    }

    final public Function getFunction (String name, List sources)
    throws AlgebraException {

	if (! name.equals ("compose"))
	    throw new AlgebraException ("task \"" + name + "\" is undefined") ;

	Iterator p = sources.iterator () ;
	if (! p.hasNext ())
	    throw new AlgebraException ("no sources provided") ;

	Type type = ((Unit) p.next ()) . getType () ;
	while (p.hasNext ()) {

	    Type newType = ((Unit) p.next ()) . getType () ;

	    if (type.isSubtype (newType))
		type = newType ;
	    else if (! newType.isSubtype (type))
		throw new AlgebraException ("source types are incompatible") ;

	}

	if (sources.size () > 1)
	    LOGGER.fine ("common type is \"" + type + '"') ;

	try {
	    Function function = composeFunction ((ClassType) type, sources) ;
	    LOGGER.finer (
		"composition function is \""
		+ localize (function.getClass().getName ())
		+ '"'
	    ) ;

	    return function ;

	} catch (Exception exception) {
	    throw new AlgebraException (
		"type \"" + type + "\" has no composition operator",
		exception
	    ) ;
	}
    }

    final public String getProperty (String name, String defaultValue) {
	String value = System.getProperty (name, defaultValue) ;
	return (value != null || value.length () > 0) ? value.trim () : value ;
    }

    final public String getProperty (String name) {
	return System.getProperty (name, null) ;
    }

    final public Unit getUnit (Object object) throws AlgebraException {

	File file = (File) object ;

	ClassType type = null ;
	try {
	    type = findTypeFromFile (file) ;

	    Constructor creator = type.klass.getConstructor (
		new Class[] {getClass (), File.class, Type.class}
	    ) ;

	    Unit unit = (Unit) creator.newInstance (
		new Object[] {this, file, type}
	    ) ;

	    LOGGER.fine (
		"file \""
		+ file
		+ '"'
		+ " is type \""
		+ localize (unit.getClass().getName ())
		+ '"'
	    ) ;

	    return unit ;

	} catch (Exception exception) {
	    LOGGER.severe (badFileType (file, type)) ;
	}

	if (file.isDirectory ()) {
	    type = classType (DirectoryCollective.class) ;
	    return new DirectoryCollective (this, file, type) ;
	}

	return new FileUnit (this, file, classType (FileUnit.class)) ;
    }

    final public String getModelPath () {
	if (this.model == null)
	    return null ;
	return model.getAbsolutePath () ;
    }

    final public void setModel (File model) {
	this.model = model ;
    }

    final public void setNoInvoke (boolean noInvoke) {
	this.noInvoke = noInvoke ;
    }

    final public File getTarget () {
	return this.target ;
    }

    final public void setTarget (File target) {
	this.target = target ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private String badFileType (File file, ClassType type) {

	StringBuffer buffer = new StringBuffer () ;
	buffer.append (file.isDirectory () ? "directory" : "file") ;
	buffer.append (" \"") ;
	buffer.append (file) ;
	buffer.append ("\" has bad type") ;

	if (type != null) {
	    buffer.append (" \"") ;
	    buffer.append (type.klass.getName ()) ;
	    buffer.append ("\" ") ;
	}

	return buffer.toString () ;
    }

    final ClassType classType (Class klass) {

	Object type = class2type.get (klass) ;
	if (type == null) {
	    type = new ClassType (klass) ;
	    class2type.put (klass, type) ;
	}

	return (ClassType) type ;
    }

    final private Function composeFunction (ClassType type, List sources)
    throws Exception {

	if (noInvoke && ! type.isAlwaysInvoked ())
	    return new TraceOnlyComposeFunction (this, sources) ;

	Method method = type.klass.getMethod (
	    "composeFunction",
	    new Class[] {getClass (), List.class}
	) ;

	return (Function) method.invoke (null, new Object[] {this, sources}) ;
    }

    final private ClassType findTypeFromFile (File file)
    throws ClassNotFoundException {

	String base = "unit." + (file.isDirectory () ? "directory" : "file") ;
	String name = file.getName () ;
	int dot = name.lastIndexOf ('.') ;

	String typeName = null ;
	if (dot >= 0)
	    typeName = getProperty (base + name.substring (dot)) ;

	if (typeName == null)
	    typeName = getProperty (base) ;

	if (typeName == null)
	    typeName = UNIT_PREFIX + "FileUnit" ;
	else if (typeName.indexOf ('.') < 0)
	    typeName = UNIT_PREFIX + typeName ;

	return classType (loadClass (typeName)) ;
    }

    final private Class loadClass (String className)
    throws ClassNotFoundException {

	String name = getProperty (className, className) ;
	return loader.loadClass (name) ;
    }

    final private static String localize (String string) {
	return
	    string.startsWith (UNIT_PREFIX)
	    ? string.substring (UNIT_PREFIX.length ())
	    : string ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private class ClassType implements Type {

	ClassType (Class klass) {
	    this.klass = klass ;
	    this.name = localize (klass.getName ()) ;
	}

	final public AlgebraFactory getFactory () {
	    return Factory.this ;
	}

	final public Type getType () {
	    return classType (ClassType.class) ;
	}

	final public boolean isAlwaysInvoked () {
	    return
		Collective.class.isAssignableFrom (this.klass)
		|| IgnoreFileUnit.class.isAssignableFrom (this.klass) ;
	}

	final public boolean isSubtype (Type type) {

	    if (! (type instanceof ClassType))
		return false ;

	    ClassType that = (ClassType) type ;

	    if (that.getFactory () != this.getFactory ())
		return false ;

	    return that.klass.isAssignableFrom (this.klass) ;
	}

	final public String toString () {
	    return name ;
	}

	final private Class klass ;
	final private String name ;

    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private ClassLoader loader ;

    private File model = null ;
    private boolean noInvoke = false ;
    private File target = null ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private Map class2type = new WeakHashMap () ;
    final static String UNIT_PREFIX = "composer.unit." ;

}
