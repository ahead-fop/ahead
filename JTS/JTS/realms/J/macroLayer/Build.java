package JTS.realms.J.macroLayer;

import Jakarta.util.BuildUtil;
import Jakarta.util.Dependency;
import java.io.File;

public class Build {
    static public void main(String[] args) {
	String cwd = BuildUtil.workingDir("JTS.realms.J.macroLayer");
	String dir = cwd + File.separator;
	String serPath = dir + "macroLayer.ser";
	String layerPath = dir + "macroLayer.layer";
	Dependency dep;

	dep = new Dependency(serPath, new String[] { layerPath });
	if (dep.satisfied())
	    return;

	BuildUtil.deleteFile(serPath);
	BuildUtil.execute(new String[]
			  { "java", "JakBasic.Main", layerPath } );
    }
}
