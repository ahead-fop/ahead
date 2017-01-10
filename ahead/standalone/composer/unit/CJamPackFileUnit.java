package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

// same as JamPackFileUnit, except will call CJamPackFileUnit
//
class CJamPackFileUnit extends FileUnit {

    public CJamPackFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new CJamPackFileUnitComposeFunction (factory, sources) ;
    }

}
