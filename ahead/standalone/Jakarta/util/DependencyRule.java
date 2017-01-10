package Jakarta.util;

import java.io.PrintWriter;
import java.io.BufferedReader;

// Combine a dependency with an action to enforce it
public class DependencyRule {
  Depends dep;
  Callback cb;
  
  public DependencyRule(Depends depends, Callback cback) {
    dep = depends;
    cb = cback;
  }

  // A convenient form of constructor: creates a "Dependency" and an "Action"
  public DependencyRule(String name, String[] fnames, String[] cmd) {
    dep = new Dependency(name, fnames);
    cb = new Action(cmd);
  }

  // A convenient form of constructor: creates a "Dependency" and an "Action"
  public DependencyRule(String name, String[] fnames, String[] cmd, 
			BufferedReader in, PrintWriter out, PrintWriter err) {
    dep = new Dependency(name, fnames);
    cb = new Action(cmd, in, out, err);
  }

  public int enforce() {
    if (!dep.satisfied()) {
      try {
	return cb.executeCallback(null);
      }
      catch (Exception e) {
	Util.fatalError("Error executing callback", e);
      }
    }
    return 0;
  }
}
