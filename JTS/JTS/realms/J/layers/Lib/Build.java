
package JTS.realms.J.layers.Lib;

import Jakarta.util.BuildUtil;

public class Build {
    static public void main(String[] args) {
	String cwd = BuildUtil.workingDir("JTS.realms.J.layers.Lib");

	BuildUtil.buildAllJak(cwd);
    }
}
