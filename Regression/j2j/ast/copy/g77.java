// tests propagation of constructors AND the presence of constructors
// inside quoted text

import java.io.FileInputStream;
import java.io.PrintWriter;
import jak2java.*;



 abstract class g77$$allesuber extends g0 {
      // inherited constructors

      g77$$allesuber( int v0) { super(  v0); }  }



 class g77 extends g77$$allesuber {

   // propagate this constructor
   g77() {
      super(5);
   }

   public static void main( String args[]) {

   AST_Class cls =(AST_Class) AstNode.markStack(AstNode.aliasStack.size(),  (AST_Class) new AST_Class()

.add( (AST_ClassElem) new AST_ClassElem().setParms( (ModTypeDecl) new ModTypeDecl().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (UmodClassDecl) new UmodClassDecl().setParms( 
new AstToken().setParms(" \n//--------------------\n","class", 0),  (NameId) new NameId().setParms(new AstToken().setParms(" ","gg0", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
,  new AstOptNode(
) /* AstOptNode */
,  (ClsBody) new ClsBody().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_FieldDecl) new AST_FieldDecl()

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (ConDecl) new ConDecl().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (NameId) new NameId().setParms(new AstToken().setParms("\n\n   // don't propagate this constructor\n   ","gg0", 0)) /* NameId */
, new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (ConSSuper) new ConSSuper().setParms( 
new AstToken().setParms("\n      ","Super", 0), new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_TypeNameList) new AST_TypeNameList()

.add( (AST_TypeNameListElem) new AST_TypeNameListElem().setParms( (TNClass) new TNClass().setParms( 
 (PrimType) new PrimType().setParms( 
 (IntTyp) new IntTyp().setParms( 
new AstToken().setParms("","int", 0)) /* IntTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
) /* TNClass */
))/* AST_TypeNameListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0),  (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (IntLit) new IntLit().setParms( 
new AstToken().setParms("","5", 0)) /* IntLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
, new AstToken().setParms("",";", 0)) /* ConSSuper */
) /* AstOptNode */
,  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("\n   ","}", 0)) /* ConDecl */
))/* AST_FieldDeclElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n\n   ","}", 0)) /* ClsBody */
) /* UmodClassDecl */
) /* ModTypeDecl */
))/* AST_ClassElem + add */
).patch()
;


   AST_Program prg =(AST_Program) AstNode.markStack(AstNode.aliasStack.size(),  (program) new program().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  new AstOptNode(
).setParms( (AST_Imports) new AST_Imports()

.add( (AST_ImportsElem) new AST_ImportsElem().setParms( (ImpQual) new ImpQual().setParms( 
new AstToken().setParms(" \n//------------------------------------------\n","import", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","java", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","io", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","File", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",";", 0)) /* ImpQual */
))/* AST_ImportsElem + add */

.add( (AST_ImportsElem) new AST_ImportsElem().setParms( (ImpQual) new ImpQual().setParms( 
new AstToken().setParms("\n","import", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","java", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","io", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","FileOutputStream", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",";", 0)) /* ImpQual */
))/* AST_ImportsElem + add */

.add( (AST_ImportsElem) new AST_ImportsElem().setParms( (ImpQual) new ImpQual().setParms( 
new AstToken().setParms("\n","import", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","java", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","io", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","PrintWriter", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",";", 0)) /* ImpQual */
))/* AST_ImportsElem + add */
) /* AstOptNode */
,  new AstOptNode(
).setParms( (AST_Class) new AST_Class()

.add( (AST_Class) AstNode.addComment( AstNode.safeCopy(cls),"\n\n// makef is a class that generates a make file for \n// dos and Unix to compile a Bali-generated compiler\n// (e.g., j2jarta).\n\n")) 
) /* AstOptNode */
) /* program */
).patch()
;
      AstProperties props = new AstProperties();
      PrintWriter pw = new PrintWriter(System.out);
      props.setProperty("output", pw);
      prg.print(props);
      pw.flush();
   }
}
