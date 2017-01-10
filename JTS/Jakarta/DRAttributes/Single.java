package Jakarta.DRAttributes;

public class Single extends DrcAtt {
   static boolean seenIt;    // used to make sure we don't replicate error
                             // reports

   public Single() { super("",""); }
      
   public Single copy() {
      Single s = new Single();
      s.copy(this);
      return s;
   }

   public void merge( Single s ) { }  // do nothing

   public Single( String name, String explanation ) {
      super(name, explanation);
      value = negate;
      seenIt = false;
   }

   public void errorMsg(int x) {
      ErrorCount.reportTopdownError();
      if (!seenIt) {
         DrcError.Report("   duplicate copies of a "+ name + " component.");
         seenIt = true;
      }
   }
} 
