package composer.macro ;

import composer.Main ;

import java.io.File ;
import java.io.IOException ;

import java.util.Collections ;
import java.util.List ;

import org.apache.velocity.VelocityContext ;

/**
 * Extends {@link VelocityContext} to provide preloaded context objects.
 **/
public final class MacroContext extends VelocityContext {

    public MacroContext () {
        super () ;
        put ("system", Collections.unmodifiableMap (System.getProperties ())) ;
    }

    public MacroContext (List sources, File target) {
        this () ;
        put ("sources", Collections.unmodifiableList (sources)) ;
	put ("target", target) ;
    }

}
