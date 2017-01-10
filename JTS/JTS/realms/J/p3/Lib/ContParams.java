// Objects of the ContParams contain information on 
// a specific type equation and container

package p3Lib;

import JakBasic.Lang;


public class ContParams {

   static int       counter = 1;

   public String    ConcreteContainerName;
   public String    ConcreteElementName;
   public String    CommonCursorName;
   public Lang.AST_QualifiedName AST_ConcreteContainerName;
   public Lang.AST_QualifiedName AST_ConcreteElementName;
   public Lang.AST_QualifiedName AST_CommonCursorName;
   

   public ContParams( TopParams t, String Cname ) {
       ConcreteContainerName = Cname;
       ConcreteElementName   = t.ElementName + "_" + ContParams.counter;
       CommonCursorName      = t.AbstractCursorName + "_" + ContParams.counter++;

       AST_ConcreteContainerName = Lang.AST_QualifiedName.Make(Cname);
       AST_ConcreteElementName   = Lang.AST_QualifiedName.Make(ConcreteElementName);
       AST_CommonCursorName      = Lang.AST_QualifiedName.Make(CommonCursorName);
   }
}

