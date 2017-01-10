package composer.unit ;

import composer.Checks ;

import composer.algebra.Signature ;

final class StringSignature implements Comparable, Signature {

    StringSignature (String string) {
	Checks.nonNullArgument (string, "string") ;
	this.string = string ;
    }

    final public int compareTo (Object object) {
	return string.compareTo (((StringSignature) object) . string) ;
    }

    final public boolean equals (Object object) {
	return string.equals (((StringSignature) object) . string) ;
    }

    final public int hashCode () {
	return string.hashCode () ;
    }

    final public String toString () {
	return string ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    final private String string ;

}
