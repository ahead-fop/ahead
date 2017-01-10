package Jakarta.DRAttributes;

public class RankInherit extends RankDrc {

   public void merge( RankInherit r ) { }   // do nothing

   public RankInherit( String name, String explanation) {
      super(name, explanation);
   }

   public RankInherit copy() {
      RankInherit c = new RankInherit("", "");
      c.copy(this);
      return c;
   }

   public void errorMsg(boolean ascending) {
      ErrorCount.reportTopdownError();
      if (ascending) {
         if (lastSet != null)
            DrcError.Report("   "  + explanation + " requires " 
               + component.current.name + 
               " to appear below " + lastSet.name + ".");
         else
            DrcError.Report("   " + component.current.name +
               " doesn't exceed default " + explanation + " rank");
       } else {
         if (lastSet != null)
            DrcError.Report("   " + explanation + " requires " +
               component.current.name + 
               " not to appear below " + lastSet.name + ".");
         else
            DrcError.Report("   " + component.current.name +
               " exceeds default " + explanation + " rank");
       }
   }
} 
