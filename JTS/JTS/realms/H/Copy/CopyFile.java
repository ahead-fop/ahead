package Copy;

import java.io.*;

public class CopyFile {
   public static void move( String origFile, String fileCopy ) {
      FileInputStream in;
      FileOutputStream out;
      BufferedInputStream bin;
      BufferedOutputStream bout;
      byte bArray[] = new byte[256];
      int bytesRead;

      try {
         in   = new FileInputStream( origFile );
         bin  = new BufferedInputStream( in );
         out  = new FileOutputStream( fileCopy );
         bout = new BufferedOutputStream( out );

         while( bin.available() > 0 ) {
            bytesRead = bin.read( bArray );
            bout.write( bArray, 0, bytesRead  );
         }

         bin.close();
         bout.close();
      } catch (Exception e) { 
          Error.RuntimeStop( e.getMessage() );
      }
   }
}
