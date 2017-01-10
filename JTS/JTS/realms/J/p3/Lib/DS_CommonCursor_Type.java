/* this is the DS interface */

package p3Lib;

import JakBasic.Lang;


public interface DS_CommonCursor_Type {

     Lang.AST_FieldDecl dataMembers();       // generate data members to add to cursor type
	 
	 Lang.AST_Stmt insertMethod();          //added to be able to increase counters for insertion

     Lang.AST_Stmt removeMethod();           // generate removal method

     Lang.AST_FieldDecl movementMethods();   // generate primitive advance, reverse methods
}
