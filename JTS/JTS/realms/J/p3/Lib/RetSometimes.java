// RetSometimes is a class that factors all the testing of
// the retrieval_layer boolean so that actual specifications
// of the first, next, etc methods are simpler

package p3Lib;

import JakBasic.Lang;


abstract class RetSometimes {
   boolean         retrieval_layer;
   DS_Cursor_Type  lower;

   public abstract DS thisLayer();
   public abstract void optimizeAction( Qopt q );

   public Qopt optimize( Qopt q ) {

      optimizeAction( q );  
      q = lower.optimize(q);

      // now determine if this is the retrieval layer

      retrieval_layer = (q.fastest_layer == thisLayer());
      return q;
   }

   public abstract Lang.AST_Stmt firstMth();

   public Lang.AST_Stmt firstMethod() {
       Lang.AST_Stmt result;

       if (retrieval_layer) 
          return firstMth();
       else return lower.firstMethod();
    }

    public abstract Lang.AST_Exp moreMth();

    public Lang.AST_Exp moreMethod() {
       Lang.AST_Exp result;

       if (retrieval_layer) {
          result = moreMth();
       }
       else result = lower.moreMethod();
       return result;
    }

    abstract Lang.AST_Stmt noMoreMth();

    public Lang.AST_Stmt noMoreMethod() {
       Lang.AST_Stmt result;
       if (retrieval_layer) 
          result = noMoreMth();
       else 
          result = lower.noMoreMethod();
       return result;
    }

    abstract Lang.AST_Stmt nextMth();

    public Lang.AST_Stmt nextMethod()  {
       Lang.AST_Stmt result;

       if (retrieval_layer) {
          result = nextMth();
       }
       else result = lower.nextMethod();
       return result;
    }

    abstract Lang.AST_Exp lowerBoundExp( boolean pos );

    public Lang.AST_Exp lowerBound( boolean pos ) {
       Lang.AST_Exp result;
       if (retrieval_layer) 
          result = lowerBoundExp(pos);
       else 
          result = lower.lowerBound(pos);
       return result;
    }

    abstract Lang.AST_Exp upperBoundExp( boolean pos );

    public Lang.AST_Exp upperBound( boolean pos ) {
       Lang.AST_Exp result;
       if (retrieval_layer) 
          result = upperBoundExp(pos);
       else 
          result = lower.upperBound(pos);
       return result;
    }

    abstract Lang.AST_Exp residualExp();

    public Lang.AST_Exp residual() {
       Lang.AST_Exp result;
       if (retrieval_layer) 
          result = residualExp();
       else 
          result = lower.residual();
       return result;
    }
}
