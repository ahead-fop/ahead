package composer.macro ;

import composer.Main ;

import org.apache.velocity.app.VelocityEngine ;

public final class MacroEngine extends VelocityEngine {

    public static MacroEngine getInstance () throws Exception {

        if (instance == null) {
            instance = new MacroEngine () ;
            instance.init (System.getProperties ()) ;
        }

        return instance ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Constructor is private to prevent external instantiation.
     **/
    private MacroEngine () {
        super () ;
    }

    private static MacroEngine instance = null ;

}
