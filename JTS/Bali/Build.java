// Meta-file: used to compile other files!

package Bali;

import Jakarta.util.BuildUtil;
import Jakarta.util.Dependency;
import Jakarta.util.Util ;

import java.io.*;

public class Build {
  public static void main(String[] args) {
    String cwd = BuildUtil.workingDir("Bali");
    String dir = cwd + File.separator;

    Dependency dep = new Dependency(dir + "BaliParser.java",
				    new String[] { "Bali.jj" });
    if (! dep.satisfied())
	if (! BuildUtil.buildJavaCC ("Bali.jj"))
	    Util.fatalError ("JavaCC compilation failed") ;

    String[] javac = new String[] {"javac", "-g", "-J-mx32m", "-nowarn"} ;

    if (! BuildUtil.buildJava (cwd + File.separator + "Main.java", javac))
	Util.fatalError ("Java compilation failed") ;
  }
}

