// This program tests the prg{ ... }prg AST constructor
// and id{ ... }id constructor and the $id() escape

import java.io.FileInputStream;
import java.io.PrintWriter;
import jak2java.*;

class g8 {

   public static void main( String args[]) {

   AST_QualifiedName id =(AST_QualifiedName) AstNode.markStack(AstNode.aliasStack.size(),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","java", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","io", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
).patch()
;

   AST_Program prg =(AST_Program) AstNode.markStack(AstNode.aliasStack.size(),  (program) new program().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  new AstOptNode(
).setParms( (AST_Imports) new AST_Imports()

.add( (AST_ImportsElem) new AST_ImportsElem().setParms( (ImpQual) new ImpQual().setParms( 
new AstToken().setParms(" \n//------------------------------------------\n","import", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedName) AstNode.addComment( AstNode.safeCopy(id)," ")) 

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","File", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",";", 0)) /* ImpQual */
))/* AST_ImportsElem + add */

.add( (AST_ImportsElem) new AST_ImportsElem().setParms( (ImpQual) new ImpQual().setParms( 
new AstToken().setParms("                      //<------- fill in \"java.io\"\n","import", 0),  (AST_QualifiedName) new AST_QualifiedName()

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

.add( (AST_ClassElem) new AST_ClassElem().setParms( (ModTypeDecl) new ModTypeDecl().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (UmodClassDecl) new UmodClassDecl().setParms( 
new AstToken().setParms("\n\n// makef is a class that generates a make file for \n// dos and Unix to compile a Bali-generated compiler\n// (e.g., j2jarta).\n\n","class", 0),  (NameId) new NameId().setParms(new AstToken().setParms(" ","g0", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
,  new AstOptNode(
) /* AstOptNode */
,  (ClsBody) new ClsBody().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_FieldDecl) new AST_FieldDecl()

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (MethodDcl) new MethodDcl().setParms( 
 new AstOptNode(
).setParms( (AST_Modifiers) new AST_Modifiers()

.add( (AST_ModifiersElem) new AST_ModifiersElem().setParms( (ModPublic) new ModPublic().setParms( 
new AstToken().setParms("\n\n   // reportError just prints out to the command line how to run makef\n\n   ","public", 0)) /* ModPublic */
))/* AST_ModifiersElem + add */

.add( (AST_ModifiersElem) new AST_ModifiersElem().setParms( (ModStatic) new ModStatic().setParms( 
new AstToken().setParms(" ","static", 0)) /* ModStatic */
))/* AST_ModifiersElem + add */
) /* AstOptNode */
,  (PrimType) new PrimType().setParms( 
 (VoidTyp) new VoidTyp().setParms( 
new AstToken().setParms(" ","void", 0)) /* VoidTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (MthDector) new MthDector().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","reportError", 0)) /* NameId */
, new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0),  new AstOptNode(
) /* AstOptNode */
) /* MthDector */
,  new AstOptNode(
) /* AstOptNode */
,  (MDSBlock) new MDSBlock().setParms( 
 (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","err", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"Usage: g0 <projectName-name>\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","exit", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (IntLit) new IntLit().setParms( 
new AstToken().setParms("","40", 0)) /* IntLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n   ","}", 0)) /* BlockC */
) /* MDSBlock */
) /* MethodDcl */
))/* AST_FieldDeclElem + add */

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (MethodDcl) new MethodDcl().setParms( 
 new AstOptNode(
).setParms( (AST_Modifiers) new AST_Modifiers()

.add( (AST_ModifiersElem) new AST_ModifiersElem().setParms( (ModPublic) new ModPublic().setParms( 
new AstToken().setParms("\n\n   // main allows makef to be run from the command line\n\n   ","public", 0)) /* ModPublic */
))/* AST_ModifiersElem + add */

.add( (AST_ModifiersElem) new AST_ModifiersElem().setParms( (ModStatic) new ModStatic().setParms( 
new AstToken().setParms(" ","static", 0)) /* ModStatic */
))/* AST_ModifiersElem + add */
) /* AstOptNode */
,  (PrimType) new PrimType().setParms( 
 (VoidTyp) new VoidTyp().setParms( 
new AstToken().setParms(" ","void", 0)) /* VoidTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (MthDector) new MthDector().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","main", 0)) /* NameId */
, new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ParList) new AST_ParList()

.add( (AST_ParListElem) new AST_ParListElem().setParms( (FormParDecl) new FormParDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","String", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","argv", 0)) /* NameId */
,  new AstOptNode(
).setParms( (Dims) new Dims()

.add( (DimsElem) new DimsElem().setParms( (Dim2) new Dim2().setParms( 
new AstToken().setParms("","[", 0), new AstToken().setParms("","]", 0)) /* Dim2 */
))/* DimsElem + add */
) /* AstOptNode */
) /* DecNameDim */
) /* FormParDecl */
))/* AST_ParListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0),  new AstOptNode(
) /* AstOptNode */
) /* MthDector */
,  new AstOptNode(
) /* AstOptNode */
,  (MDSBlock) new MDSBlock().setParms( 
 (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (IfStmt) new IfStmt().setParms( 
new AstToken().setParms("\n      ","if", 0), new AstToken().setParms(" ","(", 0),  (EqExpr) new EqExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","argv", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","length", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreEqExpr) new MoreEqExpr()

.add( (MoreEqExprElem) new MoreEqExprElem().setParms( (EEBodyC) new EEBodyC().setParms( 
 (Eq) new Eq().setParms( 
new AstToken().setParms(" ","==", 0)) /* Eq */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","0", 0)) /* IntLit */
) /* EEBodyC */
))/* MoreEqExprElem + add */
) /* EqExpr */
, new AstToken().setParms("",")", 0),  (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","g0", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","reportError", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
,  new AstOptNode(
) /* AstOptNode */
) /* IfStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (IfStmt) new IfStmt().setParms( 
new AstToken().setParms("\n      ","if", 0), new AstToken().setParms(" ","(", 0),  (EqExpr) new EqExpr().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","argv", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (ExprSuf) new ExprSuf().setParms( 
new AstToken().setParms("","[", 0),  (IntLit) new IntLit().setParms( 
new AstToken().setParms("","0", 0)) /* IntLit */
, new AstToken().setParms("","]", 0)) /* ExprSuf */
))/* SuffixesElem + add */

.add( (SuffixesElem) new SuffixesElem().setParms( (QNameSuf) new QNameSuf().setParms( 
new AstToken().setParms("",".", 0),  (NameId) new NameId().setParms(new AstToken().setParms("","length", 0)) /* NameId */
) /* QNameSuf */
))/* SuffixesElem + add */

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
,  (MoreEqExpr) new MoreEqExpr()

.add( (MoreEqExprElem) new MoreEqExprElem().setParms( (EEBodyC) new EEBodyC().setParms( 
 (Eq) new Eq().setParms( 
new AstToken().setParms(" ","==", 0)) /* Eq */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","0", 0)) /* IntLit */
) /* EEBodyC */
))/* MoreEqExprElem + add */
) /* EqExpr */
, new AstToken().setParms("",")", 0),  (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","g0", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","reportError", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
,  new AstOptNode(
) /* AstOptNode */
) /* IfStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","createMakeFile", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","argv", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (ExprSuf) new ExprSuf().setParms( 
new AstToken().setParms("","[", 0),  (IntLit) new IntLit().setParms( 
new AstToken().setParms("","0", 0)) /* IntLit */
, new AstToken().setParms("","]", 0)) /* ExprSuf */
))/* SuffixesElem + add */
) /* PrimExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n   ","}", 0)) /* BlockC */
) /* MDSBlock */
) /* MethodDcl */
))/* AST_FieldDeclElem + add */

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (MethodDcl) new MethodDcl().setParms( 
 new AstOptNode(
).setParms( (AST_Modifiers) new AST_Modifiers()

.add( (AST_ModifiersElem) new AST_ModifiersElem().setParms( (ModPublic) new ModPublic().setParms( 
new AstToken().setParms("\n\n   // here's where the real work is done\n\n   ","public", 0)) /* ModPublic */
))/* AST_ModifiersElem + add */

.add( (AST_ModifiersElem) new AST_ModifiersElem().setParms( (ModStatic) new ModStatic().setParms( 
new AstToken().setParms(" ","static", 0)) /* ModStatic */
))/* AST_ModifiersElem + add */
) /* AstOptNode */
,  (PrimType) new PrimType().setParms( 
 (VoidTyp) new VoidTyp().setParms( 
new AstToken().setParms(" ","void", 0)) /* VoidTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (MthDector) new MthDector().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","createMakeFile", 0)) /* NameId */
, new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ParList) new AST_ParList()

.add( (AST_ParListElem) new AST_ParListElem().setParms( (FormParDecl) new FormParDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","String", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","projectName", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
) /* FormParDecl */
))/* AST_ParListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0),  new AstOptNode(
) /* AstOptNode */
) /* MthDector */
,  new AstOptNode(
) /* AstOptNode */
,  (MDSBlock) new MDSBlock().setParms( 
 (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","File", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","batchfile", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
).setParms( (VarAssignC) new VarAssignC().setParms( 
new AstToken().setParms(" ","=", 0),  (VarInitExpr) new VarInitExpr().setParms( 
 (Null) new Null().setParms( 
new AstToken().setParms(" ","null", 0)) /* Null */
) /* VarInitExpr */
) /* VarAssignC */
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","File", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","directory", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
).setParms( (VarAssignC) new VarAssignC().setParms( 
new AstToken().setParms(" ","=", 0),  (VarInitExpr) new VarInitExpr().setParms( 
 (Null) new Null().setParms( 
new AstToken().setParms(" ","null", 0)) /* Null */
) /* VarInitExpr */
) /* VarAssignC */
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","Process", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","proc", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","Runtime", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","rt", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","FileOutputStream", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","fos", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
).setParms( (VarAssignC) new VarAssignC().setParms( 
new AstToken().setParms(" ","=", 0),  (VarInitExpr) new VarInitExpr().setParms( 
 (Null) new Null().setParms( 
new AstToken().setParms(" ","null", 0)) /* Null */
) /* VarInitExpr */
) /* VarAssignC */
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","PrintWriter", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","ps", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n      ","String", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","filename", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
).setParms( (VarAssignC) new VarAssignC().setParms( 
new AstToken().setParms(" ","=", 0),  (VarInitExpr) new VarInitExpr().setParms( 
 (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","projectName", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (StrLit) new StrLit().setParms( 
new AstToken().setParms(" ","\"/makefile\"", 0)) /* StrLit */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
) /* VarInitExpr */
) /* VarAssignC */
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("  // used only for printing announcements\n      ","String", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","cmd", 0)) /* NameId */
,  new AstOptNode(
).setParms( (Dims) new Dims()

.add( (DimsElem) new DimsElem().setParms( (Dim2) new Dim2().setParms( 
new AstToken().setParms("","[", 0), new AstToken().setParms("","]", 0)) /* Dim2 */
))/* DimsElem + add */
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
).setParms( (VarAssignC) new VarAssignC().setParms( 
new AstToken().setParms(" ","=", 0),  (VarInitExpr) new VarInitExpr().setParms( 
 (ObjAllocExpr) new ObjAllocExpr().setParms( 
new AstToken().setParms(" ","new", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","String", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  (ArrDim1) new ArrDim1().setParms( 
 (ExprDims) new ExprDims()

.add( (ExprDimsElem) new ExprDimsElem().setParms( (ExDimBod) new ExDimBod().setParms( 
new AstToken().setParms("","[", 0),  (IntLit) new IntLit().setParms( 
new AstToken().setParms("","1", 0)) /* IntLit */
, new AstToken().setParms("","]", 0)) /* ExDimBod */
))/* ExprDimsElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* ArrDim1 */
) /* ObjAllocExpr */
) /* VarInitExpr */
) /* VarAssignC */
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n\n      // Step 1: initialize the directory\n\n      ","directory", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (ObjAllocExpr) new ObjAllocExpr().setParms( 
new AstToken().setParms(" ","new", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","File", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  (AnonClass) new AnonClass().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","projectName", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
,  new AstOptNode(
) /* AstOptNode */
) /* AnonClass */
) /* ObjAllocExpr */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (IfStmt) new IfStmt().setParms( 
new AstToken().setParms("\n      ","if", 0), new AstToken().setParms(" ","(", 0),  (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","directory", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","exists", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",")", 0),  (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (IfStmt) new IfStmt().setParms( 
new AstToken().setParms("\n         ","if", 0), new AstToken().setParms(" ","(", 0),  (NotUE) new NotUE().setParms( 
new AstToken().setParms("","!", 0),  (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","directory", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","isDirectory", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
) /* NotUE */
, new AstToken().setParms("",")", 0),  (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n            ","directory", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","renameTo", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (ObjAllocExpr) new ObjAllocExpr().setParms( 
new AstToken().setParms("","new", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","File", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  (AnonClass) new AnonClass().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","projectName", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\".old\"", 0)) /* StrLit */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
,  new AstOptNode(
) /* AstOptNode */
) /* AnonClass */
) /* ObjAllocExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
,  new AstOptNode(
) /* AstOptNode */
) /* IfStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n         ","}", 0)) /* BlockC */
,  new AstOptNode(
).setParms( (ElseClauseC) new ElseClauseC().setParms( 
new AstToken().setParms("\n      ","else", 0),  (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","directory", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","mkdir", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
) /* ElseClauseC */
) /* AstOptNode */
) /* IfStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (TryStmt) new TryStmt().setParms( 
new AstToken().setParms("\n\n\n      // Step 1: initialize the makefile\n\n      ","try", 0),  (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","batchfile", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (ObjAllocExpr) new ObjAllocExpr().setParms( 
new AstToken().setParms(" ","new", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","File", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  (AnonClass) new AnonClass().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","projectName", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
))/* AST_ArgListElem + add */

.add( (AST_ArgListElem) new AST_ArgListElem().setParms(new AstToken().setParms("",",", 0),
 (StrLit) new StrLit().setParms( 
new AstToken().setParms(" ","\"makefile\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
,  new AstOptNode(
) /* AstOptNode */
) /* AnonClass */
) /* ObjAllocExpr */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n      ","}", 0)) /* BlockC */
,  new AstOptNode(
).setParms( (AST_Catches) new AST_Catches()

.add( (AST_CatchesElem) new AST_CatchesElem().setParms( (CatchStmt) new CatchStmt().setParms( 
new AstToken().setParms("\n      ","catch", 0), new AstToken().setParms(" ","(", 0),  (FormParDecl) new FormParDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","Exception", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","e", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
) /* FormParDecl */
, new AstToken().setParms("",")", 0),  (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","err", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","e", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","err", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (AddExpr) new AddExpr().setParms( 
 (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"Can't open file \"", 0)) /* StrLit */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","filename", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (StrLit) new StrLit().setParms( 
new AstToken().setParms(" ","\" for output\"", 0)) /* StrLit */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","exit", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (IntLit) new IntLit().setParms( 
new AstToken().setParms("","40", 0)) /* IntLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n      ","}", 0)) /* BlockC */
) /* CatchStmt */
))/* AST_CatchesElem + add */
) /* AstOptNode */
,  new AstOptNode(
) /* AstOptNode */
) /* TryStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (IfStmt) new IfStmt().setParms( 
new AstToken().setParms("\n\n      // Step 2: delete the file if it already exists\n\n      ","if", 0), new AstToken().setParms(" ","(", 0),  (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","batchfile", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","exists", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",")", 0),  (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","out", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","filename", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (StrLit) new StrLit().setParms( 
new AstToken().setParms(" ","\"exists and is being deleted\"", 0)) /* StrLit */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","batchfile", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","delete", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n      ","}", 0)) /* BlockC */
,  new AstOptNode(
) /* AstOptNode */
) /* IfStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (TryStmt) new TryStmt().setParms( 
new AstToken().setParms("\n\n      // Step 3: create file output stream\n\n      ","try", 0),  (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n         ","fos", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (ObjAllocExpr) new ObjAllocExpr().setParms( 
new AstToken().setParms(" ","new", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","FileOutputStream", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  (AnonClass) new AnonClass().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","batchfile", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
,  new AstOptNode(
) /* AstOptNode */
) /* AnonClass */
) /* ObjAllocExpr */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n       ","}", 0)) /* BlockC */
,  new AstOptNode(
).setParms( (AST_Catches) new AST_Catches()

.add( (AST_CatchesElem) new AST_CatchesElem().setParms( (CatchStmt) new CatchStmt().setParms( 
new AstToken().setParms("\n       ","catch", 0), new AstToken().setParms(" ","(", 0),  (FormParDecl) new FormParDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","Exception", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","e", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
) /* FormParDecl */
, new AstToken().setParms("",")", 0),  (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n          ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","err", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","e", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n          ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","err", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (AddExpr) new AddExpr().setParms( 
 (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"Can't open stream \"", 0)) /* StrLit */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","filename", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (StrLit) new StrLit().setParms( 
new AstToken().setParms(" ","\" for output\"", 0)) /* StrLit */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n          ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","exit", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (IntLit) new IntLit().setParms( 
new AstToken().setParms("","40", 0)) /* IntLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n       ","}", 0)) /* BlockC */
) /* CatchStmt */
))/* AST_CatchesElem + add */
) /* AstOptNode */
,  new AstOptNode(
) /* AstOptNode */
) /* TryStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n\n       // Step 4: create print stream; output makefile contents and close\n\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (ObjAllocExpr) new ObjAllocExpr().setParms( 
new AstToken().setParms(" ","new", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","PrintWriter", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  (AnonClass) new AnonClass().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","fos", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
,  new AstOptNode(
) /* AstOptNode */
) /* AnonClass */
) /* ObjAllocExpr */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (AddExpr) new AddExpr().setParms( 
 (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"Prj=\"", 0)) /* StrLit */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms("","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","projectName", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"all: $(Prj)Sym.class $(Prj).class $(Prj)Scanner.class $(Prj)Parser.class\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"$(Prj)Sym.class : $(Prj)Sym.java\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\tjavac -g $(Prj)Sym.java\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"$(Prj)Scanner.class : $(Prj)Scanner.java\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\tjavac -g $(Prj)Scanner.java\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"$(Prj).class : $(Prj).java\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\tjavac -g $(Prj).java\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"$(Prj)Parser.java $(Prj)Sym.java : $(Prj).cup\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\tjava -mx32m java_cup.Main -expect 8 -parser $(Prj)Parser -symbols j2jartaSym <Jakarta.cup\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"$(Prj)Scanner.java : $(Prj).lex\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\tjava JLex.JLex $(Prj).lex\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"$(Prj).java $(Prj).lex $(Prj).cup : $(Prj).b\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\tjava BaliC jakarta.b \"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (StrLit) new StrLit().setParms( 
new AstToken().setParms("","\"\"", 0)) /* StrLit */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n       ","ps", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","close", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("\n\n       // Step 5: announce that the makefile was create\n\n       ","System", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","out", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","println", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms("","filename", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (StrLit) new StrLit().setParms( 
new AstToken().setParms(" ","\" was created\"", 0)) /* StrLit */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
))/* AST_ArgListElem + add */
) /* AstOptNode */
, new AstToken().setParms("",")", 0)) /* Args */
) /* MthCall */
))/* SuffixesElem + add */
) /* PrimExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n    ","}", 0)) /* BlockC */
) /* MDSBlock */
) /* MethodDcl */
))/* AST_FieldDeclElem + add */
) /* AstOptNode */
, new AstToken().setParms("\n","}", 0)) /* ClsBody */
) /* UmodClassDecl */
) /* ModTypeDecl */
))/* AST_ClassElem + add */
) /* AstOptNode */
) /* program */
).patch()
;
      AstProperties props = new AstProperties();
      PrintWriter pw = new PrintWriter(System.out);
      props.setProperty("output", pw);
      prg.reduce2java(props);
      pw.flush();
   }
}
