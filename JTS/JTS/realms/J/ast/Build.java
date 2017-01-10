package JTS.realms.J.ast;

import Jakarta.util.BuildUtil;
import Jakarta.util.Dependency;
import java.io.File;

public class Build {
    static public void main(String[] args) {
	String cwd = BuildUtil.workingDir("JTS.realms.J.ast");
	String dir = cwd + File.separator;
	String serPath = dir + "ast.ser";
	String layerPath = dir + "ast.layer";
	Dependency dep;

	dep = new Dependency(serPath, new String[] { layerPath });
	if (dep.satisfied())
	    return;

	BuildUtil.deleteFile(serPath);
	BuildUtil.execute(new String[]
			  { "java", "JakBasic.Main", layerPath } );
    }
}
