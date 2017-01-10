package composer.unit ;

import composer.algebra.Function ;
import composer.algebra.Type ;

import java.io.File ;

import java.util.List ;

class PropertiesFileUnit extends FileUnit {

    public PropertiesFileUnit (Factory factory, File file, Type type) {
	super (factory, file, type) ;
    }

    public static Function composeFunction (Factory factory, List sources) {
	return new PropertiesFileUnitComposeFunction (factory, sources) ;
    }

}
