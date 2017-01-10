package composer.args ;

import java.util.regex.Pattern ;

final public class PatternFilter {

    public PatternFilter (final String pattern) {

	if (pattern == null || pattern.length () < 1) {
	    includes = new boolean[] {true} ;
	    patterns = new Pattern[] {Pattern.compile (".*")} ;
	    return ;
	}

	String[] strings = SEPARATOR.split (pattern) ;

	includes = new boolean [strings.length] ;
	patterns = new Pattern [strings.length] ;
	for (int n = strings.length ; --n >= 0 ;) {
	    if (strings[n].startsWith ("+:")) {
		includes [n] = true ;
		patterns [n] = Pattern.compile (strings[n].substring (2)) ;
	    } else if (strings[n].startsWith ("-:")) {
		includes [n] = false ;
		patterns [n] = Pattern.compile (strings[n].substring (2)) ;
	    } else {
		includes [n] = true ;
		patterns [n] = Pattern.compile (strings [n]) ;
	    }
	}
    }

    public PatternFilter () {
	this (null) ;
    }

    final public boolean accept (String string) {

	int index = patterns.length - 1 ;
	while (index >= 0 && ! patterns[index].matcher(string).matches ())
	    -- index ;

	return
	    (index >= 0 && includes [index])
	    || (index < 0 && ! includes [0]) ;
    }

    final private boolean[] includes ;
    final private Pattern[] patterns ;

    final private static Pattern SEPARATOR = Pattern.compile (",(?=[-+]:)") ;

}
