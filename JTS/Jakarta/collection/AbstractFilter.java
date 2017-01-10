package Jakarta.collection ;

import java.util.Collection ;
import java.util.Iterator ;
import java.util.NoSuchElementException ;

abstract public class AbstractFilter implements Filter {

    abstract public boolean acceptObject (Object object) ;

    public boolean accept (Object object) {
	return acceptObject (object) ;
    }

    public Collection collection (Collection inp, Collection out) {
	for (Iterator p = iterator (inp.iterator()) ; p.hasNext() ; )
	    out.add (p.next()) ;
	return out ;
    }

    public Collection collection (Collection inp)
    throws IllegalAccessException, InstantiationException {
	return collection (inp, (Collection) inp.getClass().newInstance()) ;
    }

    public Iterator iterator (Iterator baseIterator) {
	return new FilteredIterator (baseIterator) ;
    }

    public Iterator iterator (Collection baseCollection) {
	return iterator (baseCollection.iterator()) ;
    }

    /**
     * A <kbd>FilteredIterator</kbd> wraps another {@link Iterator},
     * the <em>base</em> iterator, enhancing it by restricting the elements
     * returned through the base iterator to those that match the containing
     * {@link Filter}.
     *
     * To do this consistently with the definition of {@link Iterator} while
     * still being efficient, each <kbd>FilteredIterator</kbd> keeps
     * <em>state</em> information about the base iterator.  There are three
     * possible states:
     * <dl compact>
     * <dt>END_MATCH</dt>
     *    <dd>
     *    All elements have been returned by the base iterator.
     *    Its {@link Iterator#hasNext()} method returns <kbd>false</kbd>.
     *    </dd>
     * <dt>HAS_MATCH</dt>
     *    <dd>
     *    The previous element from the base iterator (returned by its
     *    {@link Iterator#next()} method) matched the filter condition.
     *    A reference to the matching element has been saved in an auxiliary
     *    variable. 
     *    </dd>
     * <dt>PRE_MATCH</dt>
     *    <dd>
     *    The base iterator has not been exhausted, but it has not yet been
     *    positioned after a matching element.  In this state, it's usually
     *    necessary to call the base iterator's {@link Iterator#next()} method
     *    until either a match has been found or the iterator is exhausted.
     *    </dd>
     **/
     protected class FilteredIterator implements Iterator {

	FilteredIterator (Iterator baseIterator) {
	    iterator = baseIterator ;
	    state = PRE_MATCH ;
	}

	public boolean hasNext () {
	    switch (state) {
	    case END_MATCH: return false ;
	    case HAS_MATCH: return true ;
	    case PRE_MATCH: return (advance() == HAS_MATCH) ;
	    }
	    throw new IllegalStateException ("invalid state") ;
	}

	public Object next () {
	    switch (state) {
	    case END_MATCH: throw new NoSuchElementException ("exhausted") ;
	    case HAS_MATCH: state = PRE_MATCH ; return element ;
	    case PRE_MATCH: advance() ; return next() ;
	    }
	    throw new IllegalStateException ("invalid state") ;
	}

	public void remove () {
	    switch (state) {
	    case END_MATCH: throw new IllegalStateException ("exhausted") ;
	    case HAS_MATCH: iterator.remove() ; state = PRE_MATCH ; return ;
	    case PRE_MATCH: throw new IllegalStateException ("reference") ;
	    }
	    throw new IllegalStateException ("invalid state") ;
	}

	protected int advance () {
	    state = END_MATCH ;

	    while (iterator.hasNext()) {
		element = iterator.next () ;
		if (accept (element)) {
		    state = HAS_MATCH ;
		    break ;
		}
	    }

	    return state ;
	}

	protected Iterator iterator ;
	protected Object element = null ;
	protected int state ;

	protected final int END_MATCH = 0 ;
	protected final int HAS_MATCH = 1 ;
	protected final int PRE_MATCH = 2 ;
    }

}
