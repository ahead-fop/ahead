// Meta-file: used to compile other files!

package JTS.realms.J.p3.Lib;

import Jakarta.util.BuildUtil;
import java.io.File;

public class Build {
  public static void main(String[] args) {
    String [] jakFiles = {"ABSTOP", "atop", "delflag", "dlist", "DS", "hashcmp",
			  "malloc", "odlist", "qualify", "range", "text", "TOP",
			  "top2ds", "hash", "predindx", "inbetween", "rbtree",
			  "bstree", "sort", "debug", "persistent", "profile", "dshtml", "BaseDS" };

    String cwd = BuildUtil.workingDir("JTS.realms.J.p3.Lib");
    for (int i = 0; i< jakFiles.length; i++)
      BuildUtil.buildJak(cwd + File.separator + jakFiles[i] + ".jak");
  }
}

