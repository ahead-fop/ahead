package Jakarta.DRAttributes;

public class Annotation {
   String name;
   String explanation;
   String notations;

   public Annotation copy() {
      Annotation cpy = new Annotation(name,explanation);
      cpy.notations = new String(notations);
      return cpy;
   }
      
   public Annotation( String name, String explanation ) {
      this.name = name;
      this.explanation = explanation;
      notations = "";
   }

   public void merge( Annotation a ) { } // do nothing

   public void set( String layer, String note ) {
      String annote = ":" + layer + "#" + note + ":";
      notations = notations + annote;
   }

   public void set( String layer, int note ) {
      set(layer,""+note);
   }

   public boolean test( String layer, String note ) {
      String annote = ":" + layer + "#" + note + ":";

      // check to see if the annotation is already present.

      if (notations.indexOf(annote) == -1) {
         return true;
      }
      else {
         DrcError(layer, note);
         return false;
      }
   }

   public boolean test( String layer, int note ) {
      return test(layer,""+note);
   }

   void DrcError(String layer, String note) {
      ErrorCount.reportTopdownError();
      DrcError.Report("   multiple " + layer + 
                         " layers have annotation " + note );
   }

   public void print() {
      System.out.println( name + " " + explanation + " " + notations);
   }
}
