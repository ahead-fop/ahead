package Begin;

import java.io.*;
import java.util.*;
import java.lang.*;
import Jakarta.util.*;

public class MLObject {

   HTMLfile html;

   public MLObject( String directory, String filename ) {
      Parse p = new Parse( directory, filename );
      html = p.parse();
      html.setExtension( p.extension );
   }

   public String getFileName() {
      return html.getFileName();
   }

   public void compose( MLObject ext ) {
      if (ext.isExtension())
         html.compose( ext.html );
      else
         Util.stop( ext.getFileName() + " is not an extension" );
   }

   public void write( String directory, String filename ) {
      html.write( directory, filename );
   }

   public static void setReportStream( PrintWriter w ) {
      Error.setReportStream(w);
   }

   public boolean isExtension() {
      return html.getExtension();
   }
}
