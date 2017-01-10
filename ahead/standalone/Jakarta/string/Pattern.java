package Jakarta.string ;

public class Pattern {

    public Pattern (String pattern) {
	this.pattern = pattern ;
    }

    public boolean matches (String target) {
	if (pattern == null || target == null)
	    return pattern == target ;
	return matchesAt (target, 0) ;
    }

    public boolean matchesAt (String target, int index) {
	if (target == null)
	    throw new NullPointerException ("target String is null") ;
	if (pattern == null)
	    return false ;
	return (new Comparison (target)) . matchesAt (0, index) ;
    }

    protected String pattern ;

    /**
     * <kbd>Comparison</kbd> is a helper class that encapsulates the pattern
     * matching algorithm efficiently.  The core of the algorithm is
     * implemented in method {@link Pattern.Comparison#matchesAt(int,int)}
     * which steps through the characters of the pattern string, matching them
     * to corresponding characters in the target string.
     *
     * This class assumes that both the pattern and the target are not null.
     **/
    protected class Comparison {

	public Comparison (String target) {
	    this.target = target ;
	}

	public boolean matchesAt (int patternIndex, int targetIndex) {
	    if (patternIndex >= pattern.length())
		return targetIndex >= target.length() ;

	    char patternChar = pattern.charAt (patternIndex) ;

	    switch (patternChar) {

	    default :
		return
		    targetIndex < target.length()
		    && patternChar == target.charAt (targetIndex)
		    && matchesAt (patternIndex + 1, targetIndex + 1) ;

	    case '?' :
		return
		    targetIndex < target.length()
		    && matchesAt (patternIndex + 1, targetIndex + 1) ;

	    case '*' :
		for (int n = targetIndex ; n <= target.length() ; ++n)
		    if (matchesAt (patternIndex + 1, n))
			return true ;
		return false ;
	    }
	}

	protected String target ;
    }

}
