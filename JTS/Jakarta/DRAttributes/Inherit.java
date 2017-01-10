package Jakarta.DRAttributes;

public class Inherit extends DrcAtt {

   public Inherit( String name, String explanation ) {
      super(name, explanation);
   }

   public Inherit copy() {
      Inherit i = new Inherit("","");
      i.copy(this);
      return i;
   }

   public void merge( Inherit i ) { } // do nothing

   public void errorMsg(int value) {
      ErrorCount.reportTopdownError();
      switch(value) {
      case any:
         if (lastSet != null) 
            DrcError.Report("   "+component.current.name+ 
              "is not expecting " +explanation+ " to be affirmed or negated" +
              "above it (" + lastSet.name + ").");
         else
            DrcError.Report("   "+component.current.name+ 
              "is not expecting " +explanation+ " to be affirmed or negated" +
              "above it.");
         break;
      case affirm:
         if (lastSet != null) 
            DrcError.Report("   " + explanation + " is expected between " + 
               lastSet.name + " and " + component.current.name + ".");
         else
            DrcError.Report("   " + explanation + " is expected above " +
               component.current.name + ".");
         break;
      case negate:
         if (lastSet != null)
            DrcError.Report("   " + component.current.name + " is not expecting " +
               explanation + " (" + lastSet.name +") above it.");
         else
            DrcError.Report(component.current.name + " is not expecting " +
               explanation + " above it.");
         break;
      }
   }
} 
