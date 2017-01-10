package composer.evaluator ;

import composer.Checks ;
import composer.Path ;

import java.io.File ;
import java.io.IOException ;

final class SourceFunction extends Function {

    SourceFunction (final String sourceURL) throws IOException {
	super (sourceURL) ;
	url = sourceURL ;
    }

    final public String toString () {
	return "SourceFunction(" + url + ')' ;
    }

    final private String url ;

}
