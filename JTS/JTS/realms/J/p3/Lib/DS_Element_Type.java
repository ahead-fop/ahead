/* this is the DS interface */

package p3Lib;

import JakBasic.Lang;


public interface DS_Element_Type {

   Lang.AST_FieldDecl dataMembers();  // generate data members of transformed element class

   Lang.AST_Stmt link();              // generate link statements for insertions
  
   Lang.AST_FieldDecl relink();       // generate element relinking methods

   Lang.AST_FieldDecl unlink();       // generate element unlinking methods

   Lang.AST_Stmt upd( int fieldno );  // generate statements for updated attribute att

}
