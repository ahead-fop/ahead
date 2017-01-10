package Jakarta.DRAttributes;

public abstract class RankDrc {

   String    name;	// name of attribute
   int       value;	// value of attribute
   String explanation;  // explanation for attribute
   component lastSet;	// what component last set attribute value

   public RankDrc( String name, String explanation) {
      this.name        = name;
      value            = 0;
      this.explanation = explanation;
      lastSet          = null;
   }

   protected void copy( RankDrc r ) {
      if (r.name != null)
         name = new String(r.name);
      else name = null;
      value = r.value;
      if (r.explanation != null)
         explanation = new String(r.explanation);
      else explanation = null;
      lastSet = r.lastSet;
   }

   public void set(int v) {
      value   = v;
      lastSet = component.current;
   }

   public boolean gtr(int x) {
      boolean result = (value > x);
      if (!result) errorMsg(false);
      return result;
   }

   public boolean geq(int x) {
      boolean result = (value >= x);
      if (!result) errorMsg(false);
      return result;
   }

   public boolean equ(int x) {
      boolean result = (value == x);
      if (!result) errorMsg(false);
      return result;
   }

   public boolean neq(int x) {
      boolean result = (value != x);
      if (!result) errorMsg(false);
      return result;
   }

   public boolean lss(int x) {
      boolean result = (value < x);
      if (!result) errorMsg(true);
      return result;
   }

   public boolean leq(int x) {
      boolean result = (value <= x);
      if (!result) errorMsg(true);
      return result;
   }

   public void print() {
      System.out.print("   "+name+ " " + value);
   }

   abstract public void errorMsg(boolean ascending);
} 
