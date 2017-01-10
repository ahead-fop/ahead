package Jakarta.util;

import java.io.File;

// Simple dependency class: the dependency is satisfied if the dependant file 
// is newer than all the files it depends on.
public class Dependency implements Depends {
  String[] filenames;
  String target;
    
  public Dependency(String name, String[] fnames) {
    filenames = fnames;
    target = name;
    // Should ideally be an assert
    if (filenames == null || name == null)
      Util.fatalError("Incomplete dependency specification");
  }

  public Dependency (String name, String a) {
      this (name, new String[] {a}) ;
  }

  public Dependency (String name, String a, String b) {
      this (name, new String[] {a, b}) ;
  }

  public Dependency (String name, String a, String b, String c) {
      this (name, new String[] {a, b, c}) ;
  }

  public Dependency (String name, String a, String b, String c, String d) {
      this (name, new String[] {a, b, c, d}) ;
  }

  // Dependency is satisfied iff the target is newer than all files it depends
  // on. A non-existant file is considered infinitely old.
  public boolean satisfied() {
    File targetfile = new File(target);
    long targetModTime;
    
    if (!targetfile.exists())
      targetModTime = Long.MIN_VALUE;
    else
      targetModTime = targetfile.lastModified();

    for (int i=0; i<filenames.length; i++) {
      File dependfile = new File(filenames[i]);
      if (!dependfile.exists())
	continue;
      if (dependfile.lastModified() > targetModTime)
	return false;
    }
    return true;
  }
}
