
/***********************************************/
/* FixDosOutputStream fixes Java printstreams  */
/* in Dos that don't output a CR before a NL   */
/***********************************************/

package Jakarta.util;

import java.io.*;

public class FixDosWriter extends FilterWriter {
   private Writer out;
   private int LastByte;

   public FixDosWriter(Writer out) {  
     super(out);
     this.out = out; 
     LastByte = 0; 
   }

   public void close() throws IOException { out.close(); }

   public void flush() throws IOException { out.flush(); }

   public void write( int b ) throws IOException {  
      if (b == '\n' && LastByte != '\r') {
         out.write('\r');
      } 
      out.write(b);
      LastByte = b;
   }

   public void write( char b[], int start, int len ) throws IOException {
      int i;
      int end = start+len-1;

      for (i=start; i<=end; i++)
         write(b[i]);
   }

   public void write( String str, int off, int len ) throws IOException {
      int i;

      for (i=off; i<(off+len); i++)
         write(str.charAt(i));
   }
}
