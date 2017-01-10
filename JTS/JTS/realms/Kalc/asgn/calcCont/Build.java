
package Kalc.asgn.calcCont;

import Jakarta.util.BuildUtil;
import java.io.*;

public class Build {
    public static void main(String[] args) {
	String cwd = BuildUtil.workingDir("Kalc.asgn.calcCont");
	String dir = cwd + File.separator;

	// Remove all java files except the Build.java file
	BuildUtil.deleteAll(cwd, new BuildUtil.noBuildFileFilter());

	// Restore pairs.java from a saved backup copy and compile it
	BuildUtil.copyFile(dir + "pairs.java.save", dir + "pairs.java");
	BuildUtil.buildJava(dir + "pairs.java");

	// Translate paircont.jak to java files
	BuildUtil.buildJak(dir + "paircont.jak",
			   new String[] { "java", "Jak.Main"});

	// Build all Java files in the package
	BuildUtil.buildDirectory(cwd);
    }
}
