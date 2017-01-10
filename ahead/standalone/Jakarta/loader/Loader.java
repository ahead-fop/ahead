package Jakarta.loader ;

import java.io.File ;
import java.io.InputStream ;
import java.io.IOException ;

import java.net.URL ;

import java.util.Enumeration ;
import java.util.List ;

/**
 * Interface for a generic resource loader.  This is a non-optimal version
 * built to include all the public methods from {@link ClassLoader} and
 * {@link AbstractClassLoader}.  The interface should be reduced in size and
 * methods for explicitly reading and writing to resource repositories should
 * be implemented.
 **/
public interface Loader {

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    // Methods from ClassLoader:

    public ClassLoader getParent () ;

    public URL getResource (String resourceName) ;

    public InputStream getResourceAsStream (String resourceName) ;

    public Enumeration getResources (String resourceName)
    throws IOException ;

    public Class loadClass (String className)
    throws ClassNotFoundException ;

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    // Methods from AbstractClassLoader:

    public File getPackageDirectory (String packageName)
    throws IOException ;

    public File getResourceDirectory (String resourceName)
    throws IOException ;

    public List listResources (String name, List result, int limit)
    throws IOException ;

    public List listResources (String name, List result)
    throws IOException ;

    public List listResources (String name)
    throws IOException ;

}
