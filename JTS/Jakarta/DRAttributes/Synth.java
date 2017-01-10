package Jakarta.DRAttributes;

public class Synth extends DrcAtt {
   static final int array[][] = 
      { { any, affirm, negate, inconsistent },
        { affirm, affirm, inconsistent, inconsistent },
        { negate, inconsistent, negate, inconsistent },
        { inconsistent, inconsistent, inconsistent, inconsistent }};

   public Synth copy() {
      Synth s = new Synth("","");
      s.copy(this);
      return s;
   }

   public int get() {
      return value;
   }

   public void merge( Synth s ) {
      value = array[value][s.get()];
   }

   public Synth( String name, String explanation ) {
      super(name, explanation);
   }

   public void errorMsg(int value) {
      ErrorCount.reportBottomupError();
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
               lastSet.name + " and " + component.current.name +".");
         else
            DrcError.Report("   " + explanation + " is expected below " +
               component.current.name +".");
         break;
      case negate:
         if (lastSet != null)
            DrcError.Report("   " + component.current.name + " is not expecting " +
               explanation + " (" + lastSet.name +") below it.");
         else
            DrcError.Report(component.current.name + " is not expecting " +
               explanation + " below it.");
         break;
      }
   }
} 
