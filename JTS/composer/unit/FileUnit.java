package composer.unit ;

import composer.Checks ;

import composer.algebra.AlgebraFactory ;
import composer.algebra.Function ;
import composer.algebra.Type ;
import composer.algebra.Unit ;

import java.io.File ;

import java.util.List ;

public class FileUnit implements Unit {

    public FileUnit (Factory factory, File file, Type type) {

	Checks.nonNullArgument (factory, "factory") ;
	Checks.nonNullArgument (file, "file") ;
	Checks.nonNullArgument (type, "type") ;

	this.factory = factory ;
	this.file = file ;
	this.type = type ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new FileUnitComposeFunction (factory, sources) ;
    }

    final public AlgebraFactory getFactory () {
	return factory ;
    }

    final public File getFile () {
	return file ;
    }

    final public Type getType () {
	return type ;
    }

    final public String toString () {
	return file.toString () ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private Factory factory ;
    final private File file ;
    final private Type type ;

}
