package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

// same as mixin file unit -- no different, except calling CMixin methods
//
class CMixinFileUnit extends FileUnit {

    public CMixinFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new CMixinFileUnitComposeFunction (factory, sources) ;
    }

}
