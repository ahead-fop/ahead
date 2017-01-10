package Jakarta.DRAttributes;

public class DrcError {
   static String ErrorString;

   public static void Initialize() {
      component.current = null;
      ErrorCount.clearErrorCount();
      ErrorString = "";
   }

   public static boolean NoErrorsReported() {
      return ErrorString.equals("");
   }
 
   public static void Report(String s) {
      if (ErrorString.equals("")) 
         ErrorString = s;
      else
         ErrorString = ErrorString + "\n" + s;
   }

   public static String getErrors() {
      return ErrorString;
   }
}
