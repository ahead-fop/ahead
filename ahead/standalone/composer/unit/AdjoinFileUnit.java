package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

class AdjoinFileUnit extends FileUnit {

    public AdjoinFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new AdjoinFileUnitComposeFunction (factory, sources) ;
    }

}
