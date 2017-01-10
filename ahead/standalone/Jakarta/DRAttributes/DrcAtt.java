package Jakarta.DRAttributes;

public abstract class DrcAtt {
   static final int any          = 0;
   static final int affirm       = 1;
   static final int negate       = 2;
   static final int inconsistent = 3;

   String           name;	  // name of attribute
   int              value;	  // value of attribute
   String           explanation;  // explanation for attribute
   component        lastSet;      // what component last set attribute value

   protected void copy(DrcAtt x) {
      name = new String(x.name);
      value = x.value;
      if (x.explanation == null)
         explanation = null;
      else
         explanation = new String(x.explanation);
      lastSet = x.lastSet;
   }
      
   public DrcAtt( String name, String explanation) {
      this.name        = name;
      value            = any;
      this.explanation = explanation;
      lastSet          = null;
   }

   public void any() { 
      value   = any; 
      lastSet = component.current;
   }

   public void affirm() { 
      value   = affirm;
      lastSet = component.current;
   }

   public void negate() { 
      value   = negate;
      lastSet = component.current;
   }

   public void inconsistent() { 
      value   = inconsistent;
      lastSet = component.current;
   }

   public boolean Pany() { 
      boolean result = (value == any);
      if (!result) errorMsg(any);
      return result;
   }

   public boolean Paffirm() {
      boolean result = (value == affirm);
      if (!result) errorMsg(affirm);
      return result;
   }

   public boolean Pnegate() { 
      boolean result = (value == negate);
      if (!result) errorMsg(negate);
      return result;
   }

   public boolean Pinconsistent() { 
      boolean result = (value == inconsistent);
      if (!result) errorMsg(inconsistent);
      return result;
   }

   public void print() {
      System.out.print("   "+name+ " ");
      switch(value) {
      case any:    System.out.println("any"); break;
      case affirm: System.out.println("affirm"); break;
      case negate: System.out.println("negate"); break;
      case inconsistent: System.out.println("inconsistent"); break;
      default:     System.err.println("unrecognized value " +value);
                   System.exit(1);
      }
   }

   abstract public void errorMsg(int x);
} 
