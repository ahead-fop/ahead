package p3Lib;


import JakBasic.Lang;
import Jakarta.util.Util;

public class ParseTE {
  
   public static Object Finish( DS layer, Lang.AstNode x ) {
      return layer.ParseArgs(x.arg[1].arg[0].arg[0].arg[0].arg[0].arg[0]);
   }

   public static Object parse( Lang.AstNode x ) {
      String componentName;
      DS     layer;

      if (x instanceof Lang.PrimExpr &&
          x.arg[0] instanceof Lang.PPQualName) {
	  componentName = ((Lang.AST_QualifiedName)
			   x.arg[0].arg[0]).GetName();

	  if (componentName.compareTo("predindx")==0) 
	      return ParseTE.Finish( new predindx(), x );

	  if (componentName.compareTo("dlist")==0) 
	      return ParseTE.Finish( new dlist(), x );

	  if (componentName.compareTo("odlist")==0) 
	      return ParseTE.Finish( new odlist(), x );
            
	  if (componentName.compareTo("malloc")==0) 
	      return ParseTE.Finish( new malloc(), x );

          if (componentName.compareTo("persistent")==0) 
	      return ParseTE.Finish( new persistent(), x );

	  if (componentName.compareTo("delflag")==0) 
	      return ParseTE.Finish( new delflag(), x );
            
	  if (componentName.compareTo("text")==0) 
	      return ParseTE.Finish( new text(), x );

	  if (componentName.compareTo("hashcmp")==0) 
	      return ParseTE.Finish( new hashcmp(), x );

	  if (componentName.compareTo("hash")==0) 
	      return ParseTE.Finish( new hash(), x );
	
	  if (componentName.compareTo("profile")==0) 
	      return ParseTE.Finish( new profile(), x );

	  if (componentName.compareTo("rbtree")==0) 
	      return ParseTE.Finish( new rbtree(), x );

	  if (componentName.compareTo("bstree")==0) 
	      return ParseTE.Finish( new bstree(), x );
/*DELETE		  
	  if (componentName.compareTo("dshtml")==0)
	      return ParseTE.Finish( new dshtml(), x );	  
*** DELETE */

	  System.out.println("Unknown component " + componentName + "\nParsing terminated");
      }
      else
	  Util.fatalError("Syntax Error: Unrecognizable type equation");

      return null;
   }

   public static void parseError( String msg ) {
      Util.fatalError(msg);
   }
}
