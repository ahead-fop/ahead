package fsats.util;

/** Contains static methods which return strings.
 */
public class StringOps
{
   /** Prepends blanks on to the passed string to
    *  make a string with total length len.
    */
   static public String pad(String str, int len)
   {
      int curlen = str.length();
      StringBuffer sb = new StringBuffer(len);
      
      for (int i=0; i<(len-curlen); i++)
      {
         sb.append(" ");
      }
      sb.append(str);
      
      //System.out.println("Str len= " + curlen + " new len= " + len);
      return sb.toString();
   }

   /** Prepends zeros on to an integer string to
    *  make a string with total length len.
    */
   static public String zeroPad(int intIn, int len)
   {
      String str = String.valueOf(intIn);

      int curlen = str.length();
      StringBuffer sb = new StringBuffer(len);
      
      for (int i=0; i<(len-curlen); i++)
      {
         sb.append("0");
      }
      sb.append(str);
      
      //System.out.println("Str len= " + curlen + " new len= " + len);
      return sb.toString();
   }

   /** Prepends blanks on to an integer string to
    *  make a string with total length len.
    */
   static public String pad(int intIn, int len)
   {
      String str = String.valueOf(intIn);

      int curlen = str.length();
      StringBuffer sb = new StringBuffer(len);
      
      for (int i=0; i<(len-curlen); i++)
      {
         sb.append(" ");
      }
      sb.append(str);
      
      //System.out.println("Str len= " + curlen + " new len= " + len);
      return sb.toString();
   }

   /** Centers the passed string in the given space.
    */
   static public String center(String str, int len)
   {
      int halfPad = (len-str.length())/2;
      
      StringBuffer sb = new StringBuffer(len);
      for (int i=0; i<halfPad; i++)
      {
         sb.append(" ");
      }
      sb.append(str);

      //setLength appears to not work on SCO
      //sb.setLength(len);
      int remainingPad = len - sb.length();
      for (int i=0; i<remainingPad; i++)
      {
         sb.append(" ");
      }

      return sb.toString();
   }

  /** Centers the passed integer in the passed space. 
   */
   static public String center(int intIn, int len)
   {
      String str = String.valueOf(intIn);
      return center(str, len);
   }
}
    
