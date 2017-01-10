// Meta-file: used to compile other files!

package theParser;

import Jakarta.util.BuildUtil;
import Jakarta.util.Util;
import Jakarta.util.Dependency;
import java.io.*;

public class Build {
  public static void main(String[] args) {
    String cwd = BuildUtil.workingDir("JTS.languages.theParser");
    String dir = cwd + File.separator;
    System.out.println("--- ---");
    System.out.println("--- Building theParser");
    System.out.println("--- ---");

    System.out.println("--- Compiling theParser Executable");

    System.out.println("Invoking JavaCC ...");
    String source = dir + "theParser.jj";
    Dependency dep2 = new Dependency(dir + "BaliParser.java",
				    new String[] { source });
    if (! dep2.satisfied()) {
	String outdirSwitch = "-OUTPUT_DIRECTORY=" + cwd;
	String[] argsJavaCC = new String[] { outdirSwitch, source };

	// output errors to a file
	try {
	    PrintWriter err =
		new PrintWriter(new FileOutputStream(new File(dir +
							      "JavaCC.out")));
	    BuildUtil.buildJavaCC (argsJavaCC, err) ;
	    err.close();
	}
	catch (IOException e) {
	    Util.fatalError("Cannot create file", e);
	}
    }

    String[] dirList= {};
    String[] libList= {};

    for (int i=0; i<libList.length; i++) {
      String libDirPath = dir+libList[i]+"Lib";
      File libDir = new File(libDirPath);
      if (!libDir.exists())
	libDir.mkdirs();
      File srcDir = new File(dirList[i]);
      {
	System.out.println("--- --- Copying and compiling component "+
			     libList[i] + " library");
	String[] fileList = srcDir.list(new BuildUtil.javaFileFilter());
	if (fileList != null) {
	  for (int j=0; j<fileList.length; j++) {
	    System.out.println("--- --- \t Copying " + fileList[j]);
	    String fnameTo = libDirPath + File.separator + fileList[j];
	    String fnameFrom = dirList[i] + File.separator + fileList[j];
	    Dependency dep = new Dependency (fnameTo,new String[]{fnameFrom});
	    if (!dep.satisfied()) {
	      File fout = new File(fnameTo);
	      if (fout.exists())
		fout.delete();
	      BuildUtil.appendSubst(fnameFrom, fnameTo, 
				    new String[] { "JakBasic", "package " },
				    new String[] { "theParser", "package theParser." });
	    }
	  }
	}
      }
    }

    /* skip as all of this will be compiled by buildDirectory
    for (int i=0; i<libList.length; i++) {
	String libDirPath = dir+libList[i]+"Lib";

	System.out.println("--- --- Compiling component " + libList[i]);
	BuildUtil.buildAllJava(libDirPath,
			       new String[] {"javac", "-g", "-J-mx64m"});
    }
    */

    System.out.println("Compiling Main ...");
    BuildUtil.buildDirectory(cwd);
  }
}

