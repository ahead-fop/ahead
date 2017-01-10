// this program computes the answer of aliastest
// without the use of aliases.  It does so by 
// using explicit escapes.

import java.io.PrintWriter;
import jak2java.*;

class altst {
    public static void main(String[] args) {
	AA aa;
	AstProperties props;
	AST_Class cls;

	// Step 1: do the standard stuff

	props = new AstProperties();
	PrintWriter pw = new PrintWriter(System.out);
	props.setProperty("output",pw);

	// Step 2: now create instance of AA

	aa = new AA();
	cls = aa.Acls();
 	cls.reduce2java(props);
	pw.println();
	pw.flush();
    }
}

      
class AA {
    AST_QualifiedName A, A1, A2;
    AST_QualifiedName B;
    AST_TypeName      B1;
    AST_Exp           C;

    AA() {
	A =(AST_QualifiedName) AstNode.markStack(AstNode.aliasStack.size(),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","x", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
).patch()
;
	A1 = (AST_QualifiedName) A.clone();
	A2 = (AST_QualifiedName) A.clone();
	B =(AST_QualifiedName) AstNode.markStack(AstNode.aliasStack.size(),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","z", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","q", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","r", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
).patch()
;
	B1 =(AST_TypeName) AstNode.markStack(AstNode.aliasStack.size(),  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","z", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","q", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","r", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
).patch()
;
	C =(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","r", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","s", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms(new AstToken().setParms("",".", 0),
 (NameId) new NameId().setParms(new AstToken().setParms("","t", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
).patch()
;
    }

    AST_Class Acls() {
	return(AST_Class) AstNode.markStack(AstNode.aliasStack.size(),  (AST_Class) new AST_Class()

.add( (AST_ClassElem) new AST_ClassElem().setParms( (ModTypeDecl) new ModTypeDecl().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (UmodClassDecl) new UmodClassDecl().setParms( 
new AstToken().setParms("\n\t    ","class", 0), ( (AST_QualifiedName) AstNode.addComment( AstNode.safeCopy(A)," ") ).makeQName(),  new AstOptNode(
).setParms( (ExtClause) new ExtClause().setParms( 
new AstToken().setParms(" ","extends", 0),  (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedName) AstNode.addComment( AstNode.safeCopy(B)," ")) 
) /* ExtClause */
) /* AstOptNode */
,  new AstOptNode(
) /* AstOptNode */
,  (ClsBody) new ClsBody().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_FieldDecl) new AST_FieldDecl()

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (ConDecl) new ConDecl().setParms( 
 new AstOptNode(
) /* AstOptNode */
, ( (AST_QualifiedName) AstNode.addComment( AstNode.safeCopy(A1)," ") ).makeQName(), new AstToken().setParms("","(", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms("",")", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms(" ","{", 0),  new AstOptNode(
) /* AstOptNode */
,  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
, (AST_TypeName) AstNode.addComment( AstNode.safeCopy(B1)," "),  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(new AstToken().setParms(" ","b", 0)) /* NameId */
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

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedName) AstNode.addComment( AstNode.safeCopy(A2)," ")) 
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (MultExpr) new MultExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(new AstToken().setParms(" ","b", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreMultExpr) new MoreMultExpr()

.add( (MoreMultExprElem) new MoreMultExprElem().setParms( (MEBod) new MEBod().setParms( 
 (Mult) new Mult().setParms( 
new AstToken().setParms(" ","*", 0)) /* Mult */
, (ExprPre) new ExprPre().setParms( new AstToken().setParms(" ","(", 0),
(Expression) AstNode.addComment( AstNode.safeCopy(C)," "), new AstToken().setParms("",")", 0))
) /* MEBod */
))/* MoreMultExprElem + add */
) /* MultExpr */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms(" ","}", 0)) /* ConDecl */
))/* AST_FieldDeclElem + add */
) /* AstOptNode */
, new AstToken().setParms(" ","}", 0)) /* ClsBody */
) /* UmodClassDecl */
) /* ModTypeDecl */
))/* AST_ClassElem + add */
).patch()
;
    }
    // should return:
    // class x extends z.q.r { x() { z.q.r b; x = b * r.s.t; } }
}
