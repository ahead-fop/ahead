package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

class JamPackFileUnit extends FileUnit {

    public JamPackFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new JamPackFileUnitComposeFunction (factory, sources) ;
    }

}
