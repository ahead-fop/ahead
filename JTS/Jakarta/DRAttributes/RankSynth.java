package Jakarta.DRAttributes;

public class RankSynth extends RankDrc {

   public int get() {
      return value;
   }

   public void merge( RankSynth r ) {  // return greater of two numbers
      value = value > r.get() ? value : r.get();
   }

   public RankSynth( String name, String explanation) {
      super(name, explanation);
   }


   public RankSynth copy() {
      RankSynth c = new RankSynth("", "");
      c.copy(this);
      return c;
   }

   public void errorMsg(boolean ascending) {
      ErrorCount.reportBottomupError();
      if (ascending) {
         if (lastSet != null)
            DrcError.Report("   "  + explanation + " requires " 
               + component.current.name + 
               " to appear above " + lastSet.name + ".");
         else
            DrcError.Report("   " + component.current.name +
               " doesn't exceed default " + explanation + " rank");
       } else {
         if (lastSet != null)
            DrcError.Report("   " + explanation + " requires " +
               component.current.name + 
               " not to appear above " + lastSet.name + ".");
         else
            DrcError.Report("   " + component.current.name +
               " exceeds default " + explanation + " rank");
       }
   }
} 
