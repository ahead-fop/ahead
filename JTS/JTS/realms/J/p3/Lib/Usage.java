/* Usage - collects information from cursor declaration about how
           cursor will be used - i.e., will it be used to delete
           elements, which fields are to be updated, what order are
           elements to be returned, etc. */

package p3Lib;

// import P3.ParseTE;
// import P3.TopParams;
import JakBasic.Lang;

public class Usage implements Cloneable {
   TopParams      tp;
   public Lang.AST_Exp predicate; // cursor selection predicate
   public boolean del;            // will elements be deleted thru cursor?
   public boolean upd[];          // is field[i] to be updated?
   public boolean forward;        // will elements be retrieved in "forward"
                                  // order?
   public int     order;          // order field, -1 if order not specified
   public boolean support_index;  // will this cursor support position operator?
   public String  cursName;       //name of cursor 

   public void print( ) {
      int i;

      System.out.println("\ncursor usage");
      if (del) 
        System.out.println("   deletion");
      else
        System.out.println("no deletion");
      for (i=0; i<tp.NFields; i++)
        if (upd[i])
           System.out.println("upd " + tp.FieldName[i]);
        else
           System.out.println("    " + tp.FieldName[i]);
      if (forward)
         if (order == -1)
            System.out.println("retrieve in normal order");
         else
            System.out.println("retrieve in " + tp.FieldName[order] +
                               " order");
      else
         if (order == -1)
            System.out.println("retrieve in reverse order");
         else 
            System.out.println("retrieve in reverse " + tp.FieldName[order] +
                               " order");
   }

   public Usage(TopParams t, Lang.AST_Exp pre, String curs) {
     int i;

     tp  = t;
     predicate = pre;
     del = false;
     upd = new boolean[ tp.NFields ];
     for (i=0; i<tp.NFields; i++) 
        upd[i] = false;
     forward = true;
     order   = -1;
     support_index = true;
	 cursName = curs;
   }

   public void SetUpd(String fld) {
     int i;

     i = tp.FieldIndex( fld );
     if (i != -1)
       {
	   upd[i] = true;
	   }
     else
        ParseTE.parseError("Field " + fld + "not a member of " 
                           + tp.ElementName);
   }

   public void SetAll() {
     int i;

     for (i=0; i<tp.NFields; i++) 
        upd[i] = true;
   }

   public Object clone() {
/*
      Usage u = new Usage( tp, predicate );
      u.del = del;  
      u.upd = upd;  // shouldn't this be array copy? -- gang
      u.forward = forward;
      u.order = order;
      return u;
*/
      System.err.println( "Usage.clone() has been called." );

      Usage u = new Usage( tp, (Lang.AST_Exp) predicate.clone(), cursName );
      u.del = del;

      u.upd = new boolean[ upd.length ];
      for ( int i = 0; i < upd.length; i++ ) 
        u.upd[i] = upd[i];

      u.forward = forward;
      u.order = order;
      u.support_index = support_index;

      return u;

   }
}
