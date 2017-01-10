package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

class SingletonFileUnit extends LastFileUnit {

    public SingletonFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new SingletonFileUnitComposeFunction (factory, sources) ;
    }

}
