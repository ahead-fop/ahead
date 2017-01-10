package Jakarta.DRAttributes;

public class ErrorCount {
   static int topdownErrors = 0;
   static int bottomupErrors = 0;

   public static void reportTopdownError() {
      if (topdownErrors == 0) {
         DrcError.Report("Precondition errors: ");
      }
      topdownErrors++;
   }

   public static void reportBottomupError() {
      if (bottomupErrors == 0) {
         DrcError.Report("Prerestriction errors: ");
      }
      bottomupErrors++;
   }

   public static void clearErrorCount() {
      topdownErrors = 0;
      bottomupErrors = 0;
   }
}
