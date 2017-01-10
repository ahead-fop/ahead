package Jakarta.util;

import java.io.*;
import java.lang.Thread;

public class BuildUtil {

    public static class javaFileFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
	    return name.endsWith(".java");
	}
    }

    public static class noBuildFileFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
	    return (name.endsWith(".java") && !name.equals("Build.java"));
	}
    }

    public static class jakFileFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
	    return name.endsWith(".jak");
	}
    }

    /**
     * Builds a specified package using a build file matching a specified
     * basename.  If the basename has an extension, then the basename is used
     * as the final build file.  Otherwise, a search is made for a build file
     * matching the basename as extended by an extension such as ".xml" or
     * ".java".  The first matching build file, if any, is used as the final
     * build file.
     *
     * Build files with ".xml" extensions are first sought, then those with
     * ".java" extensions.  The search is case insensitive.
     **/
    public static boolean executeBuild (String pkgName, String fileBase) {
	File directory = Util.getPackageDirectory (pkgName, false) ;

	File buildFile = new File (directory, fileBase) ;
	if (fileBase.endsWith (".xml"))
	    return executeAntBuild (buildFile) ;

	if (fileBase.endsWith (".java"))
	    return executeJavaBuild (pkgName, buildFile) ;

	buildFile = new File (directory, fileBase + ".xml") ;
	if (buildFile.exists())
	    return executeAntBuild (buildFile) ;

	buildFile = new File (directory, fileBase.toLowerCase() + ".xml") ;
	if (buildFile.exists())
	    return executeAntBuild (buildFile) ;

	buildFile = new File (directory, fileBase + ".java") ;
	if (buildFile.exists())
	    return executeJavaBuild (pkgName, buildFile) ;

	return false ;
    }	

    /**
     * Build a package using a default external command.  The preferred
     * command, if a file named "build.xml" exists in the package, is "ant".
     * Otherwise, if a file named "Build.java" exists there, it will compile
     * and run that.
     **/
    public static boolean executeBuild (String packageName) {
	    return executeBuild (packageName, "Build") ;
    }

    private static boolean executeAntBuild (File buildFile) {
	String[] command = new String [5] ;
	command [0] = "env" ;
	command [1] = "--" ;
	command [2] = "ant" ;
	command [3] = "-buildfile" ;
	command [4] = Util.getFullPath (buildFile) ;
	return execute (command) == 0 ;
    }

    private static boolean executeJavaBuild (String pkgName, File buildFile) {
	if (! buildJava (Util.getFullPath (buildFile)))
	    return false ;

	String basename = buildFile.getName () ;
	if (basename.endsWith (".java"))
	    basename = basename.substring (0, basename.length() - 5) ;

	return execute ("java", "-mx64m", pkgName + "." + basename) == 0 ;
    }

    // Change the suffix of a string
    public static String changeSuffix(String original, String fromSuffix,
				      String toSuffix) {
	// Should be an "assert"
	if (!original.endsWith(fromSuffix))
	    Util.fatalError("String doesn't end in " + fromSuffix);
	int index = original.length() - fromSuffix.length();
	return original.substring(0, index) + toSuffix;
    }

    // Take a string with suffix ".java" and change it into ".class"
    public static String DotClassify(String dotjavaname) {
	return changeSuffix(dotjavaname, ".java", ".class");
    }

    // Take a string with suffix ".jak" and change it into ".java"
    public static String DotJavify(String dotjakname) {
	return changeSuffix(dotjakname, ".jak", ".java");
    }

    public static String workingDir (String packageName) {
	File directory = Util.getPackageDirectory (packageName, false) ;
	return Util.getFullPath (directory) ;
    }

    // Run JavaCC to compile a grammer
    public static boolean buildJavaCC (String[] args, PrintWriter errOutput) {
	String[] command ;

	try {
	    Class sunJavaCC = Class.forName ("COM.sun.labs.javacc.Main") ;
	    command = new String [args.length + 2] ;
	    command[0] = "java" ;
	    command[1] = "COM.sun.labs.javacc.Main" ;
	    System.arraycopy (args, 0, command, 2, args.length) ;
	} catch (ClassNotFoundException except) {
	    command = new String [args.length + 3] ;
	    command[0] = "env" ;
	    command[1] = "--" ;
	    command[2] = "javacc" ;
	    System.arraycopy (args, 0, command, 3, args.length) ;
	}

	PrintWriter out = new PrintWriter (System.err) ;
	return execute (command, null, out, errOutput) == 0 ;
    }

    // Run JavaCC using short-cut calls that package arguments in array:
    public static boolean buildJavaCC (String a) {
	PrintWriter writer = new PrintWriter (System.err) ;
	return buildJavaCC (new String[] {a}, writer) ;
    }

    public static boolean buildJavaCC (String a, String b) {
	PrintWriter writer = new PrintWriter (System.err) ;
	return buildJavaCC (new String[] {a, b}, writer) ;
    }

    public static boolean buildJavaCC (String a, String b, String c) {
	PrintWriter writer = new PrintWriter (System.err) ;
	return buildJavaCC (new String[] {a, b, c}, writer) ;
    }

    public static boolean buildJavaCC(String a, String b, String c, String d) {
	PrintWriter writer = new PrintWriter (System.err) ;
	return buildJavaCC (new String[] {a, b, c, d}, writer) ;
    }

    // Build a given .java file
    public static boolean buildJava (String fullPath, String[] command) {
	String[] cmd = new String [command.length + 1] ;
	System.arraycopy (command, 0, cmd, 0, command.length) ;
	cmd [command.length] = fullPath ;

	String dotClass = DotClassify(fullPath);
	DependencyRule dr = new DependencyRule(dotClass, 
					       new String[] { fullPath }, cmd);
	if (dr.enforce() != 0) {
	    System.err.println(fullPath + " build failed");
	    System.err.flush();
	    return false ;
	}
	return true ;
    }

    // The usual command to invoke javac is "javac -g"
    public static boolean buildJava (String fullPath) {
	return buildJava (fullPath, new String[] {"javac", "-g"}) ;
    }

    // Build all Java files in a directory
    public static boolean buildAllJava(String cwd, String[] command) {
	File parent ;
	String[] fileList;
	String[] newCommand;

	try {
	    parent = new File(cwd);
	    if (! parent.isDirectory())
		Util.fatalError(cwd + " is not a directory!");
	    fileList = parent.list(new noBuildFileFilter());
	}
	catch (SecurityException e) {
	    Util.fatalError("Cannot access directory " + cwd);
	    return false ;
	}

	newCommand = new String[command.length + 1];
	System.arraycopy(command, 0, newCommand, 0, command.length);
	for (int i = 0 ; i < fileList.length ; i++) {
	    newCommand[command.length] =
		Util.getFullPath (new File (parent, fileList[i])) ;
	    if (execute(newCommand) != 0)
		return false ;
	}

	return true ;
    }

    // The usual build command for Java files is "javac -g"
    public static boolean  buildAllJava (String cwd) {
	String[] command = new String[] {"javac", "-g", "-J-mx32m"} ;
	return buildAllJava (cwd, command) ;
    }


    // Build a given .jak file
    public static boolean buildJak (String fullPath, String[] command) {
	String[] cmd = new String[command.length + 1];
	System.arraycopy (command, 0, cmd, 0, command.length) ;
	cmd [command.length] = fullPath ;

	String dotJava = DotJavify(fullPath);

	// Local class for compiling Jak files when needed
	class JakAction implements Callback {

	    public JakAction (String[] cmd, String dotJava) {
		this.cmd = cmd ;
		this.dotJava = dotJava ;
	    }

	    public int executeCallback(Object parms) throws Exception {
		PrintWriter outputStream;
		try {
		    outputStream = new PrintWriter(new FileOutputStream
						   (new File(dotJava)), true);
		}
		catch (IOException e) {
		    Util.fatalError("Cannot create file "+ dotJava, e);
		    return 1 ;
		}
		// Redirect Jak output to the corresponding .java file
		int result =  execute(cmd, null, outputStream, 
				      new PrintWriter(System.err, true));
		outputStream.close();
		return result ;
	    }

	    private String[] cmd ;
	    private String dotJava ;
	}

	DependencyRule dr = new DependencyRule
	    (new Dependency(dotJava, new String[] { fullPath }),
	     new JakAction (cmd, dotJava));
	if (dr.enforce() != 0) {
	    System.err.println(fullPath + " translation failed");
	    System.err.flush();
	    return false ;
	}
	return true ;
    }

    // The usual command to invoke Jak is "java JakBasic.Main"
    public static boolean buildJak (String fullPath) {
	String[] command = new String[] {"java", "-mx64m", "JakBasic.Main"} ;
	return buildJak (fullPath, command) ;
    }

    // Build all Jak files in a directory.
    public static boolean buildAllJak (String cwd, String[] command) {
	File file;
	String[] filelist;
	try {
	    file = new File(cwd);
	    filelist = file.list(new jakFileFilter());
	} catch (SecurityException e) {
	    Util.fatalError("Cannot access directory " + cwd);
	    return false ;
	}

        if (filelist.length == 0)
	    return buildDirectory (cwd) ;

	for (int i=0; i<filelist.length; i++) {
	    String fullPath = cwd + File.separator + filelist[i];
	    if (! buildJak (fullPath, command))
		return false ;
	}

	return true ;
    }

    public static boolean buildAllJak (String cwd) {
	String[] command = new String[] {"java", "-mx64m", "JakBasic.Main"} ;
	return buildAllJak (cwd, command) ;
    }

    // Build all Jak files in a directory.
    public static boolean copyAll (String src, String dst, FilenameFilter ff) {
	File file;
	String[] filelist;
	try {
	    file = new File(src);
	    filelist = file.list(ff);
	}
	catch (SecurityException e) {
	    Util.fatalError("Cannot access directory " + src);
	    return false ;
	}

	for (int i=0; i<filelist.length; i++) {
	    File srcFile = new File (src, filelist[i]) ;
	    File dstFile = new File (src, filelist[i]) ;
	    if (! copyFile (srcFile, dstFile))
		return false ;
	}

	return true ;
    }

    // Copy a file (if needed)
    public static boolean copyFile (File fromFile, File toFile) {
	/**
	 * Hack for equality dependency rule: the dependency is satisfied if
	 * the dependant file is newer than the file it depends on and has the
	 * same length. In other words, the dependant is assumed to be a copy
	 * of the other file and the file size is used as a heuristic to
	 * ensure that this is true. If the dependency is not satisfied, we
	 * just copy the file.
	 **/
	class EQDependency implements Depends {

	    public EQDependency (File fromFile, File toFile) {
		this.fromFile = fromFile ;
		this.toFile = toFile ;
	    }

	    public boolean satisfied() {
		long fromModTime, toModTime;
		if (fromFile.length() != toFile.length())
		    return false;
		if (!fromFile.exists())
		    return true;
		if (!toFile.exists())
		    return false;
		fromModTime = fromFile.lastModified();
		toModTime = toFile.lastModified();
		return toModTime >= fromModTime;
	    }

	    private File fromFile ;
	    private File toFile ;
	}
	
	String from = Util.getFullPath (fromFile) ;
	String to = Util.getFullPath (toFile) ;

	// copy the file by using the more general concatenation action class
	DependencyRule dr =
	    new DependencyRule 
		(new EQDependency (fromFile, toFile),
		 new CatAction(new String[] {from}, to)) ;
	return dr.enforce() == 0 ;
    }

    public static boolean copyFile (String fromName, String toName) {
	return copyFile (new File (fromName), new File (toName)) ;
    }

    /**
     * Replace all occurrences of substring "patFrom" by "patTo".
     **/
    public static String subst(String line, String patFrom, String patTo) {
	int patFromLen = patFrom.length();
	int lineLen = line.length();
	int start = 0;
	int pos;
	// The following "if" is really just an optimization so that the 
	// usual case will abort very quickly.
	if ((pos = line.indexOf(patFrom, start)) != -1) {
	    // Allocate a large enough string buffer for speed
	    StringBuffer temp = new StringBuffer(100); // initial size guess
	    while (pos != -1) {
		temp.append(line.substring(start,pos));
		temp.append(patTo);
		start = pos+patFromLen;
		pos = line.indexOf(patFrom, start);
	    }
	    temp.append(line.substring(start, lineLen));
	    line = temp.toString();
	}
	return line;
    }


    private static void appendSubst(File fFrom, BufferedWriter toStream, 
				    String[] patFrom, String[] patTo) 
	throws IOException 
    {
	if (fFrom.exists()) {
	    BufferedReader fromStream = new BufferedReader(new FileReader(fFrom));
	    String lineSeparator = System.getProperties().
		getProperty("line.separator");
	    for (String line = fromStream.readLine(); line != null;
		 line = fromStream.readLine()) {
		for (int i=0; i < patFrom.length; i++) 
		    line = subst(line, patFrom[i], patTo[i]);
		toStream.write(line);
		toStream.write(lineSeparator);
	    }
	    fromStream.close();
	}
    }

    // Append the contents of one text file to another while substituting
    // all occurences of given patterns
    public static void appendSubst(String fnameFrom, String fnameTo,
				   String[] patFrom, String[] patTo) {
	File source = new File(fnameFrom);
	File dest = new File(fnameTo);
	String cwd = dest.getParent();
	String fullPath = cwd + File.separator + System.currentTimeMillis() +
	    ".tmp";
	File tempFile = new File(fullPath);
	try {
	    BufferedWriter toStream = new BufferedWriter
		(new OutputStreamWriter(new FileOutputStream(tempFile)));
	    appendSubst(dest, toStream, new String[] { }, new String [] { });
	    appendSubst(source, toStream, patFrom, patTo);
	    toStream.flush();
	    toStream.close();
	    if (dest.exists())
		dest.delete();
	    if (!tempFile.renameTo(dest))
		throw new IOException();
	}
	catch (IOException e) {
	    Util.fatalError("Could not append to file "+fnameTo, e);
	}
    }
      
    // Concatenate two arrays of strings
    public static String[] catStringArrays(String[] arr1, String[] arr2) {
	String[] result = new String[ arr1.length + arr2.length ];
	int i;
	for (i = 0; i < arr1.length; i++)
	    result[i] = arr1[i];
	for (int j = 0; j < arr2.length; j++)
	    result[i++] = arr2[j];
	return result;
    }
  
    public static void moveFile(String fnameFrom, String fnameTo) 
	throws IOException 
    {
	File fFrom = new File(fnameFrom);
	File fTo = new File(fnameTo);

	if (fTo.exists() &&
	    fFrom.getCanonicalPath().equals(fTo.getCanonicalPath()))
	    return;
	if (fTo.exists())
	    fTo.delete();
	fFrom.renameTo(fTo);
    }


    public static void deleteFile(String fname)
    {
	File f = new File(fname);
	if (f.exists())
	    f.delete();
    }


    public static void deleteAll(String cwd, FilenameFilter ff) {
	String[] filelist;
	File file;

	file = new File(cwd);
	if (! file.isDirectory())
	    Util.fatalError("BuiltUtil.deleteAll: " + cwd +
			    " is not a directory");
	filelist = file.list(ff);
	for (int i=0; i < filelist.length; i++) {
	    file = new File(cwd, filelist[i]);
	    file.delete();
	}
    }


    // This is the main dispatcher method. It executes an arbitrary command 
    // (synchronously) and redirects the output and error channels of the new 
    // process. No shell is involved! 
    // The input channel cannot be redirected correctly on NT (due to a bug).
    public static int execute(String[] cmd, BufferedReader in, 
			      PrintWriter out, PrintWriter err) {
	Process proc;
	int rc = -1;
    
	if (cmd[0].equals ("java") || cmd[0].equals ("javac")) {
	    String[] newCmd = new String [cmd.length + 2] ;

	    newCmd [0] = cmd [0] ;
	    newCmd [1] = "-classpath" ;
	    newCmd [2] = System.getProperty ("java.class.path", ".") ;
	    System.arraycopy (cmd, 1, newCmd, 3, cmd.length - 1) ;

	    cmd = newCmd ;
	}

	try {
	    // Local class! Used to represent a separate thread that
	    // channels an input stream to an output stream. If it hinders
	    // readability or if we want to use this class elsewhere, we
	    // better make it global.
	    class Channel extends Thread {
		BufferedReader input;
		PrintWriter output;
		boolean closeOnEnd = false;
	
		public void SetChannel(BufferedReader is,
				       PrintWriter os,
				       boolean close) {
		    input = is;
		    output = os;
		    closeOnEnd = close;
		}
	
		public void run() {
		    try {
			String line;
			while ((line = input.readLine()) != null) {
			    if (line.trim().length() > 0) {
				output.println(line);
				output.flush();
			    }
			    if (closeOnEnd) 
				output.close();
			}
		    }
		    catch (IOException e) {
			Util.fatalError("Error while channeling output ", e);
		    }
		}
	    }  // End of local class
	    Channel ch1 = new Channel();
	    Channel ch2 = new Channel();
	    Channel ch3 = new Channel();
      
            debugCommand (cmd);

	    proc = Runtime.getRuntime().exec(cmd);

	    // This may not be the right way to execute a new process and
	    // take control of its input, output, and error streams. I
	    // couldn't find a way to rebind these streams using the Java
	    // API (I guess it's because this is platform specific). The
	    // channeling thread technique I'm using was suggested in a
	    // newsgroup posting I read. (YS)

	    // Take control of the output stream of the new process
	    if (out != null) {
		ch1.SetChannel(new BufferedReader
			       (new InputStreamReader(proc.getInputStream())),
			       out, false);
		ch1.start();
	    }
	    // Take control of the error stream of the new process
	    if (err != null) {
		ch2.SetChannel(new BufferedReader
			       (new InputStreamReader(proc.getErrorStream())), 
			       err, false);
		ch2.start();
	    }
	    // Feed data to the input stream of the new process. 
	    /* // This works fine on Solaris but, due to an NT bug, it is currently
	       // not used.
	       if (in != null) {
	       ch3.SetChannel(in, new PrintWriter(proc.getOutputStream()), true);
	       ch3.start();
	       }
	       */
	    ch1.join();
	    ch2.join();
	    ch3.join();
	    rc = proc.waitFor();
	}
	catch (Exception e) {
	    Util.fatalError("Can't execute '"+cmd[0]+"'", e);
	}
	return rc;
    }

  
    public static int execute (String[] cmd) {
	return execute(cmd, null, new PrintWriter(System.out, true), 
		       new PrintWriter(System.err, true));
    }

    public static int execute (String a) {
	return execute (new String[] {a}) ;
    }

    public static int execute (String a, String b) {
	return execute (new String[] {a, b}) ;
    }

    public static int execute (String a, String b, String c) {
	return execute (new String[] {a, b, c}) ;
    }

    public static int execute (String a, String b, String c, String d) {
	return execute (new String[] {a, b, c, d}) ;
    }

   public static void debugCommand( String[] cmd ) {
       if (cmd.length > 0 && debugCmd.isEnabled()) {
	   debugCmd.print (cmd[0]) ;
	   for (int n = 1 ; n < cmd.length ; ++n)
	       debugCmd.print (" " + cmd[n]) ;
	   debugCmd.println() ;
       }
   }

    public static boolean buildDirectory( String cwd ) {
	File     file;
	String[] filelist;
	String[] newCommand;

	try {
	    file = new File(cwd);
	    if ( !file.isDirectory() )
		Util.fatalError(cwd + " is not a directory!");
	    filelist = file.list(new noBuildFileFilter());
	} catch (SecurityException e) {
	    Util.fatalError("Cannot access directory " + cwd);
	    return false ;
	}

	int i;
	newCommand = new String[filelist.length + 1];
	newCommand[0] = "javac";
	for (i=0; i<filelist.length; i++)
	    newCommand[i+1] = Util.getFullPath (new File (cwd, filelist[i])) ;

	return execute(newCommand) == 0 ;
    }

    protected static LineWriter
        debugBuild = Debug.global.getWriter("debug.build") ,
        debugCmd = Debug.global.getWriter("debug.command") ;
}
