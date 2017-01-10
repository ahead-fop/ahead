
/***********************************************/
/* FixDosOutputStream fixes Java printstreams  */
/* in Dos that don't output a CR before a NL   */
/***********************************************/

package Jakarta.util;

import java.io.*;

public class FixDosOutputStream extends FilterOutputStream {
   private OutputStream out;
   private int LastByte;

   public FixDosOutputStream(OutputStream out) {  
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

   public void write( byte b[], int start, int len ) throws IOException {
      int i;
      int end = start+len-1;

      for (i=start; i<=end; i++)
         write(b[i]);
   }

   public void write( byte b[] ) throws IOException {
      int i;
      int end = b.length;

      for (i=0; i<end; i++)
         write(b[i]);
   }
}
