package ModelExplorer.Util;

import java.io.*;
import java.util.*;

public class Environment
{
   public static String CMD = "cmd /c set";

   private Hashtable env;

   /**
   * Creates a new Environment object
   */
   public Environment() throws IOException
   {
	   String osName = System.getProperty("os.name" );
		if( osName.equals( "Windows NT" )|| osName.equals("Windows 2000")|| osName.equals("Windows XP"))
			CMD = "cmd /c set";
		else if( osName.equals( "Windows 95" ) )
			CMD = "command.com /c set";
		else
			CMD = "env";

      env = new Hashtable();
      Runtime runtime = Runtime.getRuntime();
      Process pid = runtime.exec(CMD);
      BufferedReader in =
         new BufferedReader(
         new InputStreamReader(
         pid.getInputStream()));
      for (;;) {
         String line = in.readLine();
         if (line == null)
            break;
         int p = line.indexOf("=");
         String key = line.substring(0, p);
         String value = line.substring(p+1);
         env.put(key, value);
      }
      try {
         pid.waitFor();
      }
      catch (InterruptedException e) {
         throw new IOException(e.getMessage());
      }
      in.close();
   }

   /**
   * Returns an environment variable
   */
   public String getEnv(String key)
   {
      return (String) env.get(key);
   }
}
