package Jakarta.symtab ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Enumeration ;
import java.util.Iterator ;
import java.util.List ;

public abstract class AbstractNamed implements Named {

    /**
     * Comparison method uses classname as major key, object name next.
     **/
    public int compareTo (Object that) {
	int comparison = 0 ;

	if (comparison == 0 && that != null && that.getClass() != getClass()) {
	    String thatName = that.getClass().getName() ;
	    String thisName = this.getClass().getName() ;
	    comparison = thisName.compareTo (thatName) ;
	}

	if (comparison == 0 && that instanceof Named) {
	    String thatName = ((Named) that) . toString () ;
	    String thisName = toString () ;
	    comparison = thisName.compareTo (thatName) ;
	}

	return
	    (comparison != 0)
	    ? comparison
	    : System.identityHashCode(this) - System.identityHashCode(that) ;
    }

    /**
     * Utility method to build an ordered Enumeration over an Enumeration.
     * This method exists only to aid refactoring of "dump" methods.
     * <em>Please don't use it for other purposes!</em>
     * When refactor is completed, this method will be removed.
     **/
    static protected Enumeration sortCursor (Enumeration cursor) {
	List list = new ArrayList () ;

	while (cursor.hasMoreElements())
	    list.add (cursor.nextElement()) ;
	Collections.sort (list) ;

	return Collections.enumeration (list) ;
    }

    /**
     * Utility method to build an ordered Iterator over an Iterator.
     * This method exists only to aid refactoring of "dump" methods.
     * <em>Please don't use it for other purposes!</em>
     * When refactor is completed, this method will be removed.
     **/
    static protected Iterator sortCursor (Iterator cursor) {
	List list = new ArrayList () ;

	while (cursor.hasNext())
	    list.add (cursor.next()) ;
	Collections.sort (list) ;

	return list.iterator () ;
    }

    /**
     * This version of "toString" is only here for refactoring.
     **/
    public String toString () {
	return getFullName () ;
    }
}
