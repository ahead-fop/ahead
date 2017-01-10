package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

class IgnoreFileUnit extends FileUnit {

    public IgnoreFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new IgnoreFileUnitComposeFunction (factory, sources) ;
    }

}
