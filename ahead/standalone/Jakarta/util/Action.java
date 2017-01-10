package Jakarta.util;

import java.io.PrintWriter;
import java.io.BufferedReader;

// The simplest kind of an action performed to satisfy a dependency:
// executes a single command as a separate process
public class Action implements Callback {
  String[] cmd;
  PrintWriter out;
  PrintWriter err;
  BufferedReader in;
  public Action(String[] command) {
    this(command, null, new PrintWriter(System.out), 
	 new PrintWriter(System.err));
  }

  public Action(String[] command, BufferedReader inStream, 
		PrintWriter outStream, PrintWriter errStream) {
    cmd = command;
    in = inStream;
    out = outStream;
    err = errStream;
  }

  public int executeCallback(Object parms) throws Exception {
    return BuildUtil.execute(cmd, in, out, err);
  }
}
