package Jakarta.util;

import Jakarta.loader.Loader ;
import Jakarta.loader.PrefixClassLoader ;

import Jakarta.string.Strings ;

import java.io.*;

import java.net.URL ;

import java.util.ArrayList ;
import java.util.Collection ;
import java.util.Collections ;
import java.util.Iterator ;
import java.util.List ;

public class Util {
    private static int errors = 0;
    private static int warnings = 0;
    private static PrintWriter reportStream = new PrintWriter(System.out);

    private static String classpath;

    // Used by system()
    private static String bashExec;

    public static void resetCounters() {
       errors = 0;
       warnings = 0;
    }

    public static void report( String msg ) {
        reportStream.println(msg);
        reportStream.flush();
    }

    public static void report( String tool, String msg ) {
	report(tool + msg );
    }
 
    public static void warning(String msg) {
	reportStream.println(msg);
	reportStream.flush();
	warnings++;
    }

    public static void warning(String tool, String file, String msg ) {
	report(tool + " Warning: " + file + ": " + msg );
        warnings++;
    }

    public static void error(String msg) {
	reportStream.println(msg);
	reportStream.flush();
	errors++;
    }

    public static void error(String tool, String file, String msg ) {
	report(tool + " Error: " + file + ": " + msg );
	errors++;
    }

    public static void fatalError(String msg) {
	/*
	reportStream.println(msg);
	(new Exception("")).printStackTrace(reportStream);
	reportStream.flush();
	System.exit(1);
	*/
	throw new ExitError(msg);
    }

    public static void fatalError(String tool, String file, String msg ) {
	fatalError(tool + " Fatal Error: " + file + ": " + msg );
    }

    public static void fatalError(Exception e) {
	/*
	errors++;
	e.printStackTrace(reportStream);
	reportStream.flush();
	System.exit(1);
	*/
	throw new ExitError(e);
    }

    public static void fatalError(String msg, Exception e) {
	throw new ExitError( "\n" + msg, e );
    }

    // used to exit compilation upon detection of 
    // a user-input error

    public static void stop(String msg) {
         RuntimeException e = new RuntimeException( 
                                 "Compilation stopped: " + msg );
         e.printStackTrace();
         throw e;
    }

    public static void stopError( String msg ) {
       errors++;
       stop( msg );
    }

    public static void override( String msg, Object o ) {
       fatalError( msg + " should be overridden in class " 
                   + o.getClass().getName() );
    }

    public static void setReportStream(PrintWriter pw) { reportStream = pw; }

    public static int warningCount() { return(warnings); }

    public static int errorCount() { return(errors); }

    /**
     * Converts a qualified name consisting of components separated by dots to
     * an array of the component names.
     **/
    public static String[] findComponents (String packageName) {
	Collection components = Strings.split (packageName, ".") ;
	String[] result = new String [components.size()] ;
	return (String[]) components.toArray (result) ;
    }

    /**
     * Find a directory in the search path corresponding to a package.
     **/
    public static File findPackageDir (String packageName) {
	return getPackageDirectory (packageName, false) ;
    }

    /**
     * Find a directory in the search path corresponding to a package,
     * optionally creating it.
     **/
    public static File getPackageDirectory (String name, boolean create) {
	String path = name.replace ('.', '/') ;
	return getResourceDirectory (path, create) ;
    }

    /**
     * Convenience method to return resource via default loader.  The default
     * loader is a {@link PrefixClassLoader} constructed with prefix
     * "<code>JTS/</code>".
     **/
    public static URL getResource (String resourceName) {
	return loader.getResource (resourceName) ;
    }

    /**
     * Returns a directory corresponding to the given resource name.
     *
     * If the directory can be found via the search path, returns that
     * directory.  Otherwise, examines the filesystem for a matching
     * subdirectory, creating it if it doesn't already exist.
     **/
    public static File getResourceDirectory (String resource, boolean create) {
	if (! resource.endsWith ("/"))
		 resource = resource + '/' ;

	int slash = resource.lastIndexOf ('/') ;
	while (slash >= 0) {

		 String prefix = resource.substring (0, slash) ;
		 String suffix = resource.substring (1 + slash) ;

		 try {
		File directory = loader.getResourceDirectory (prefix) ;
		if (directory != null) {
			 directory = resourceDir (directory, suffix, create) ;
			 if (directory != null)
			return directory ;
		}
		 } catch (IOException except) {
		// Ignore these exceptions and try next possible directory.
		 }

		 slash = resource.lastIndexOf ('/', slash - 1) ;
	}

	return resourceDir (new File ("."), resource, create) ;
    }

    /**
     * Appends all {@link File} ancestors of a given {@link File} to a
     * {@link Collection}.  The ancestors are added starting from the
     * highest in the hierarchy.
     **/
    public static Collection listHierarchy (File file, Collection result) {
	if (file != null) {
		 listHierarchy (file.getParentFile(), result) ;
		 result.add (file) ;
	}
	return result ;
    }

    /**
     * Computes a {@link List} of all {@link File} ancestors of a given
     * {@link File}.  The ancestors are added starting from the highest in the
     * hierarchy.
     **/
    public static List listHierarchy (File file) {
	return (List) listHierarchy (file, new ArrayList ()) ;
    }

    /**
     * Creates a directory, creating ancestor directories as appropriate.
     * This method differs from {@link File#mkdirs()} by cleaning up after
     * itself if the creation fails.
     *
     * @return true if directory present, false otherwise.
     **/
    public static boolean makeHierarchy (File directory) {
	if (! directory.exists ()) {

		 // Get the list of ancestor directories:
		 //
		 List hierarchy = listHierarchy (directory) ;

		 if (debugDir.isEnabled ())
		for (Iterator p = hierarchy.iterator() ; p.hasNext() ; )
			 debugDir.println ("all " + ((File)p.next()).getPath()) ;

		 // Remove existing ancestor directories from list:
		 //
		 for (Iterator p = hierarchy.iterator() ; p.hasNext() ; )
		if ( ((File) p.next ()) . exists () )
			 p.remove () ;

		 if (debugDir.isEnabled ())
		for (Iterator p = hierarchy.iterator() ; p.hasNext() ; )
			 debugDir.println ("mkdir " + ((File)p.next()).getPath()) ;

		 // Create the remaining elements in list, if possible.
		 // If creation fails, delete the intermediate directories.
		 //
		 if (! directory.mkdirs ()) {
		Collections.reverse (hierarchy) ;
		for (Iterator p = hierarchy.iterator() ; p.hasNext() ; ) {
			 File intermediate = (File) p.next () ;
			 if (intermediate.isDirectory ()) {
			intermediate.delete () ;
			debugDir.println ("rm " + ((File)p.next()).getPath()) ;
			 }
		}
		 }
	}

	return directory.isDirectory () ;
    }

    /**
     * Checks for and, optionally, creates a subdirectory corresponding to
     * resource.
     **/
    private static File resourceDir (File base, String sub, boolean create) {
	String subdirPath = sub.replace ('/', File.separatorChar) ;
	File subdir = new File (base, subdirPath) ;

	if (! subdir.exists () && create)
		 return makeHierarchy (subdir) ? subdir : null ;

	return subdir.isDirectory() ? subdir : null ;
    }

    //************************************************** 
    // A utility method for invoking external programs (synchronously)
    //**************************************************
    public static int system(String ext_cmd) {
	return(system(ext_cmd, "."));
    }

    public static int system(String ext_cmd, String cwd) {
	Process proc;
	Runtime rt;
	int rc;
	String cmd[];

	if (bashExec == null)
		 bashExec = System.getProperty("bash.executable");
	cmd = new String[3];
	cmd[0] = bashExec;
	cmd[1] = "-c";
	cmd[2] = "cd " + cwd + ";" + ext_cmd;
	rc = -1;
	try {
		 rt = Runtime.getRuntime();
		 proc = rt.exec(cmd);
		 rc = proc.waitFor();
	}
	catch (Exception e) {
		 fatalError("Can't execute '"+ext_cmd+"'", e);
	}
	return(rc);
    }


    //**************************************************
    // A method for extracting the base type of an object. For example,
    // given a pointer to an object of type foo.bar.baz, this method
    // will return "baz".
    //
    // NOTE: In order to handle nested classes properly, we assume that
    // '$' is not part of the class name and treat '$' as a dot.
    //**************************************************
    public static String baseType(Object obj) {
	return(baseType(obj.getClass().getName()));
    }


    public static String baseType(Class cls) {
	return(baseType(cls.getName()));
    }

    public static String baseType(String className) {
	int lastDot;
	int lastDollar;
	int last;

	lastDot = className.lastIndexOf('.');
	lastDollar = className.lastIndexOf('$');
	last = (lastDot > lastDollar) ? lastDot : lastDollar;
	if (last == -1)
		 return(className);
	return(className.substring(last+1));
    }


    //************************************************** 
    // A utility method for creating files while retaining a backup copy.
    // Files are created in the generated language directory so they should
    // not be paths.
    //************************************************** 
    public static PrintWriter backedOutput(File file) {
	File backup;
	FileOutputStream fos;
	PrintWriter pw;
	String lineSeparator;

	fos = null;
	pw = null;
	try {
		 backup = new File(Util.getFullPath(file)+".bak");
		 if (file.exists()) {
		backup.delete();
		file.renameTo(backup);
		 }
		 fos = new FileOutputStream(file);
		 lineSeparator =
		System.getProperties().getProperty("line.separator");
		 if (lineSeparator.compareTo("\n") != 0)
		pw = new PrintWriter(new FixDosOutputStream(fos));
		 else
		pw = new PrintWriter(fos);
	}
	catch (Exception e) {
		 Util.fatalError("Can't open file " + Util.getFullPath(file) +
				 " for output", e);
	}
	return(pw);
    }

    /**
     * Convenience method to return full absolute pathname of a File
     * in preferred form, if possible, else as absolute form.
     **/
    public static String getFullPath (File file) {
	try {
		 return file.getCanonicalPath() ;
	} catch (IOException except) {
		 return file.getAbsolutePath() ;
	}
    }

    public static String getParentDir(File file) {
	try {
		 return file.getCanonicalFile().getParent() ;
	} catch (IOException except) {
		 return file.getAbsoluteFile().getParent() ;
	}
    }

    public static File getParentFile (File file) {
	try {
		 return file.getCanonicalFile().getParentFile () ;
	} catch (IOException except) {
		 return file.getAbsoluteFile().getParentFile () ;
	}
    }

    protected static LineWriter
        debugDir = Debug.global.getWriter ("debug.mkdirs") ,
	debugPath = Debug.global.getWriter ("debug.path") ;

    protected static Loader loader = new PrefixClassLoader ("JTS/") ;

    /**
     * This method copies source file to destination
     * @param fIn
     * @param fOut
     * @return
     * @throws Exception
     */
    public static boolean copyFile(File fIn, File fOut) throws Exception
    {
        FileInputStream fis  = new FileInputStream(fIn);
        FileOutputStream fos = new FileOutputStream(fOut);
        byte[] buf = new byte[1024];
        int i = 0;
        while((i=fis.read(buf))!=-1) {
          fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
        return true ;
    }
}
