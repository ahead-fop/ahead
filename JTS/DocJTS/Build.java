// Meta-file: used to compile other files!

package DocJTS;

import Jakarta.util.BuildUtil;

public class Build {
  public static void main(String[] args) {
    String cwd = BuildUtil.workingDir("DocJTS");
    BuildUtil.buildDirectory( cwd );
  }
}

