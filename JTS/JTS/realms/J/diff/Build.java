package JTS.realms.J.diff;

import Jakarta.util.BuildUtil;
import Jakarta.util.Dependency;
import java.io.File;

public class Build {

    static public void main(String[] args) {
	String cwd = BuildUtil.workingDir("JTS.realms.J.diff");
	String dir = cwd + File.separator;
	String serPath = dir + "diff.ser";
	String layerPath = dir + "diff.layer";
	Dependency depends;

	depends = new Dependency(serPath, layerPath);

	if (! depends.satisfied()) {
	    BuildUtil.deleteFile (serPath) ;
	    BuildUtil.execute ("java", "JakBasic.Main", layerPath) ;
	}
    }

}
