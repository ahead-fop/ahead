package p3Lib;

public class DrcAttributes {

   public boolean qualify_present;             // is the qualify layer present?

   public boolean unordered_layer_present;     // is an unordered DS layer
                                               // present in TE?
   public String  unordered_layer_set;         // layer that set the u_l_p value

   public boolean below_topmost_layers;        // layers that set this value
                                               // are retrieval etc. that sit
                                               // at the bottom of type eqn.
   public String below_topmost_set;            // layer that set b_t_l value

   public boolean retrieval_layer_present;     // is there a retrieval layer?

// Note: retrieval layers always set the below_topmost_layers property
// and retrieval_layer present property.  However, not all layers set both
// values (e.g. check out predindx).  So if retrieval_layer is true, then
// we know below_topmost_layers is true, but not vice versa.

   public boolean logical_del_layer_present;   // is a logical deletion layer present?
   public String  logical_del_set;             // layer that set l_d_l value

   public static int error_count;
 
   public String annotations;                  // used for checking annotations
 
   public static int drc_errors() { 
      int tmp = DrcAttributes.error_count;
      DrcAttributes.error_count = 0;
      return tmp;
   }

   public DrcAttributes() {
      qualify_present           = false;
      below_topmost_layers      = false;
      retrieval_layer_present   = false;
      logical_del_layer_present = false;
      unordered_layer_present   = false;
      annotations               = "";
   }

   void printProperty(String name, boolean value) {
      if (value)
         System.err.println("True  " + name);
      else
         System.err.println("False " + name);
   }

   public void print() {
      printProperty( "below_topmost_layers", below_topmost_layers );
      printProperty( "retrieval_layer_present", retrieval_layer_present );
      printProperty( "logical_del_layer_present", logical_del_layer_present );
      printProperty( "unordered_layer_present", unordered_layer_present );
   }

   public static void DrcError( String msg ) {
      System.err.println("Design Rule Error: " + msg );
      DrcAttributes.error_count++;
   }
   
 
  public void check_annotation( String layer, String note ) {
      String annote = ":" + layer + "**" + note + ":";

      // check to see if the annotation is already present.
      // if so, flag an error, otherwise remember it

      if (annotations.indexOf(annote) == -1)
         annotations = annotations + annote;
      else
        { 
		  if ((note == " ") && (layer == "profile"))
		   DrcError("multiple " + layer + " layers ");
		  else DrcError("multiple " + layer + " layers have annotation " + note);
		}
   }

   public boolean test_and_set_annotation( String layer, String note ) {
      String annote = ":" + layer + "**" + note + ":";

      // check to see if the annotation is already present.
      // if so, flag an error, otherwise remember it

      if (annotations.indexOf(annote) == -1) {
         annotations = annotations + annote;
         return false;
      }
      return true;
   }
 
   
   
   
   
}
   
