/* this is the DS interface */

package p3Lib;

import JakBasic.Lang;


public interface DS_Container_Type {

   void  validate();             // validates nonrealm arguments 

   Lang.AST_FieldDecl dataMembers();  // generate data members of transformed container class

   Lang.AST_Stmt containerMethod();   // generate statements of container class constructor

   Lang.AST_Exp is_fullMethod();      // generate statements of is_full method

   Lang.AST_Stmt insertMethod();      //generate statements of insert
   
   Lang.AST_Stmt finishMethod();       // close method for persistent storage and 
   										// generates the profile files (counters and workload)

   Lang.AST_Stmt storageMethod();      //checkin method for persistent storage
   
   Lang.AST_Stmt openMethod();      // open method for persistent storage
}
