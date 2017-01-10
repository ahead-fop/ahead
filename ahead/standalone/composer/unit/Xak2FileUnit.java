package composer.unit;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

class Xak2FileUnit extends FileUnit {
	
    public Xak2FileUnit (Factory factory, File file, Type type) {
    	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
    	return new Xak2FileUnitComposeFunction (factory, sources) ;
    }

}
