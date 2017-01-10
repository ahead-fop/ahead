package composer.algebra ;

import composer.Checks ;

import java.util.Collections ;
import java.util.Map ;
import java.util.Set ;

abstract public class AbstractCollective implements Collective {

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Constructors and methods for the sub-class author.                    */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    protected AbstractCollective (Map unitMap) {
	Checks.nonNullArgument (unitMap, "unitMap") ;
	this.unitMap = Collections.unmodifiableMap (unitMap) ;
    }

    abstract public Type getType () ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Implementations of methods from {@link Collective}.                   */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    public Set getSignatures () {
	return unitMap.keySet () ;
    }

    public Unit getUnit (Signature signature) {
	return (Unit) unitMap.get (signature) ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Private material below this line.  Don't look!                        */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    private final Map unitMap ;
}
