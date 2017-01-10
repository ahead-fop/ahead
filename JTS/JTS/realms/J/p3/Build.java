package JTS.realms.J.p3;

import Jakarta.util.BuildUtil;
import Jakarta.util.Dependency;
import java.io.File;

public class Build {
    static public void main(String[] args) {
	String cwd = BuildUtil.workingDir("JTS.realms.J.p3");
	String dir = cwd + File.separator;
	String serPath = dir + "p3.ser";
	String layerPath = dir + "p3.layer";
	Dependency dep;

	dep = new Dependency(serPath, new String[] { layerPath });
	if (dep.satisfied())
	    return;

	BuildUtil.deleteFile(serPath);
	BuildUtil.execute(new String[]
			  { "java", "JakBasic.Main", layerPath } );

	// Build library
	BuildUtil.executeBuild("JTS.realms.J.p3.Lib");
    }
}
