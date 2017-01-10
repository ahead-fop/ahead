package Jakarta.util;

import java.io.*;

// Performs concatenation of files
public class CatAction implements Callback {
  String[] fromFilename = null;
  String toFilename = null;
  
  public CatAction(String[] from, String to) {
    fromFilename = from;
    toFilename = to;
  }

  public int executeCallback(Object parms) throws Exception {
    BufferedWriter toStream = new BufferedWriter
      (new OutputStreamWriter(new FileOutputStream(new File(toFilename))));
    for (int i = 0; i < fromFilename.length; i++) {
      BufferedReader fromStream = new BufferedReader
	(new InputStreamReader(new FileInputStream(new File(fromFilename[i]))));
      try {
	String lineSeparator = System.getProperties().
	  getProperty("line.separator");
	// Is this a safe way to concatenate text files on all platforms?
	// What happens to non-text files?
	for (String line = fromStream.readLine(); line != null; 
	     line = fromStream.readLine()) {
	  toStream.write(line);
	  toStream.write(lineSeparator);
	}
      }	
      catch (IOException e) {
	Util.fatalError("Cannot copy file " + fromFilename[i] + "to" +
			toFilename, e);
      }
      toStream.flush();
      toStream.close();
    }
    return 0;
  }
}
