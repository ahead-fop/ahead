// package ckJavaLib;

package J.ckJava.library;

import java.io.*;

public class Utils {
      // truncate("a.b.c") = "c"
      // truncate("abc")   = "abc"

      public static String truncate(String abc) {
         int position = abc.lastIndexOf(".");
         if (position == -1)
            return abc;
         else
            return abc.substring(position+1);
      }

      public static void WriteObject( Object obj, String fileName ) {
         ObjectOutputStream os = null;
         try {
            os = new ObjectOutputStream( new FileOutputStream(fileName));
         }
         catch( Exception e ) {
            System.err.println("Cannot open " + fileName + e);
            System.exit(1);
         }

         try {
            os.writeObject(obj);
         }
         catch( Exception e ) {
            System.err.println("Cannot write " + fileName + e);
            System.exit(1);
         }

         try {
            os.close();
         }
         catch( Exception e ) {
            System.err.println("Cannot close" + fileName + e);
            System.exit(1);
         }
     }

      public static Object readObject( String fileName ) {
         ObjectInputStream is = null;
         Object o = null;
         try {
            is = new ObjectInputStream( new FileInputStream(fileName));
         }
         catch( Exception e ) {
            System.err.println("Cannot open " + fileName);
            return null;
         }

         try {
            o = is.readObject();
         }
         catch( Exception e ) {
            System.err.println("Cannot read " + fileName + e);
            System.exit(1);
         }

         try {
            is.close();
         }
         catch( Exception e ) {
            System.err.println("Cannot close " + fileName + e);
            System.exit(1);
         }
         return o;
     }
}
