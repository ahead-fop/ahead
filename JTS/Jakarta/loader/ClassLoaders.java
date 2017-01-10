package Jakarta.loader ;

public class ClassLoaders {

    /**
     * Private constructor to prevent external instantiation.
     **/
    private ClassLoaders () {
	// Private constructor to prevent external instantiation.
    }

    static public String toString (ClassLoader loader) {
	if (null == loader)
	    return "BootstrapClassLoader" ;

	if (ClassLoader.getSystemClassLoader() == loader)
	    return "SystemClassLoader" ;

	return loader.toString() ;
    }

}
