/* this is the DS interface */

package p3Lib;

import JakBasic.Lang;


public interface DS_Cursor_Type {

     Qopt    optimize( Qopt q );        // optimize query

     Lang.AST_FieldDecl dataMembers();       // generate data members to add to cursor type

     Lang.AST_Stmt cursorMethod();           // generate statements for constructor

     Lang.AST_Stmt firstMethod();            // generate statements for first method

     Lang.AST_Exp moreMethod();              // generate expression for more method

     Lang.AST_Stmt nextMethod();             // generate statements for next method

     Lang.AST_Exp lowerBound( boolean pos ); // return lower bound predicate

     Lang.AST_Exp upperBound( boolean pos);  // return upper bound predicate

     Lang.AST_Exp residual( );               // return residual predicate

     Lang.AST_Stmt noMoreMethod();           // generate code for end-of-data structure

     Lang.AST_Stmt removeMethod();           // generate cursor-specific code for element removal
}
