
/* this should test the alias contruct in multiple ways */

import java.io.PrintWriter;
import jak2java.* ;

class alias_test {
    static AstProperties props;
    static PrintWriter pw;

    public static void printcode( AST_Class code ) {
	code.reduce2java(alias_test.props);
	pw.flush();
	pw.println("\n");
    }

    public static void main(String argv[]) {

	foo f = new foo(4);
	AstProperties props;

	// Step 1:  initialize output stream

	props = new AstProperties();
	pw = new PrintWriter(System.out);
	props.setProperty("output", pw);

	// Step 2: try some simple stuff and then test augment

	f.gen_expression1().reduce2java(props); pw.print("\n");
	f.gen_expression2().reduce2java(props); pw.print("\n");

	// Step 3: now try a simple test of alias

	f.atest1().reduce2java(props); pw.print("\n");
	f.atest2().reduce2java(props); pw.print("\n");

	// Step 4: now try a more complex test

	f.atest3().reduce2java(props); pw.print("\n");
	f.atest4().reduce2java(props); pw.print("\n");
	f.atest5().reduce2java(props); pw.print("\n");

	// Step 5: lastly, try the implicit escapes

	f.atest6().reduce2java(props); pw.print("\n");
	pw.flush();
    }
}

class foo {
	public Environment _E = (Environment) new Environment().addId("i").addId("j").addId("loop").addId("klass").addId("method").addId("paramlist");
	public Environment getEnv() { return(_E); }

    int i;

    foo(int k) {
	String varname = "k";
	i = k;_E.addId( varname);

    }

    AST_Exp gen_expression1() {
	// should return i_# + j_#
	return(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","i", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","j", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
).patch()
;
    }

    AST_Exp gen_expression2() {
	// should return i_# + j_# + k_#
	return(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","i", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","j", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","k", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
).patch()
;
    }

    AST_Exp atest1() {
	// should return i_# + j_# + k_#
	AST_Exp x = gen_expression2();
	((Environment) _E).addAlias("i",  x);


	// should return i_# + j_# + k_# + j_# + k_#
	return(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","i", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","j", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","k", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
).patch()
;
    }

    AST_Exp atest2() {
	AST_Exp x =(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (AddExpr) new AddExpr().setParms( 
 (MultExpr) new MultExpr().setParms( 
 (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","4", 0)) /* IntLit */
,  (MoreMultExpr) new MoreMultExpr()

.add( (MoreMultExprElem) new MoreMultExprElem().setParms( (MEBod) new MEBod().setParms( 
 (Mult) new Mult().setParms( 
new AstToken().setParms("","*", 0)) /* Mult */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms("","5", 0)) /* IntLit */
) /* MEBod */
))/* MoreMultExprElem + add */
) /* MultExpr */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","7", 0)) /* IntLit */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
).patch()
;
	((Environment) _E).addAlias("j",  x);


	// should return i_# + j_# + k_# + 4*5 + 7 + k_#
	return(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","i", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","j", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms(" ","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","k", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
).patch()
;
    }

    AST_Stmt atest3() {
	AST_Stmt z =(AST_Stmt) AstNode.markStack(AstNode.aliasStack.size(),  (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ForStmt) new ForStmt().setParms( 
new AstToken().setParms(" ","for", 0), new AstToken().setParms(" ","(", 0),  new AstOptNode(
).setParms( (FIExprList) new FIExprList().setParms( 
 (StatementExpressionList) new StatementExpressionList()

.add( (StatementExpressionListElem) new StatementExpressionListElem().setParms( (PEStmtExpr) new PEStmtExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms("","q", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  new AstOptNode(
).setParms( (AssnExpr) new AssnExpr().setParms( 
 (Assign) new Assign().setParms( 
new AstToken().setParms("","=", 0)) /* Assign */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms("","0", 0)) /* IntLit */
) /* AssnExpr */
) /* AstOptNode */
) /* PEStmtExpr */
))/* StatementExpressionListElem + add */
) /* FIExprList */
) /* AstOptNode */
, new AstToken().setParms("",";", 0),  new AstOptNode(
).setParms( (RelExpr) new RelExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","q", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreRelExpr) new MoreRelExpr()

.add( (MoreRelExprElem) new MoreRelExprElem().setParms( (REBod) new REBod().setParms( 
 (LtOp) new LtOp().setParms( 
new AstToken().setParms("","<", 0)) /* LtOp */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms("","30", 0)) /* IntLit */
) /* REBod */
))/* MoreRelExprElem + add */
) /* RelExpr */
) /* AstOptNode */
, new AstToken().setParms("",";", 0),  new AstOptNode(
).setParms( (StmExprList) new StmExprList().setParms( 
 (StatementExpressionList) new StatementExpressionList()

.add( (StatementExpressionListElem) new StatementExpressionListElem().setParms( (PEStmtExpr) new PEStmtExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","q", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  new AstOptNode(
).setParms( (PlusPlus) new PlusPlus().setParms( 
new AstToken().setParms("","++", 0)) /* PlusPlus */
) /* AstOptNode */
) /* PEStmtExpr */
))/* StatementExpressionListElem + add */
) /* StmExprList */
) /* AstOptNode */
, new AstToken().setParms("",")", 0),  (BlockC) new BlockC().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","funcall", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (MthCall) new MthCall().setParms( 
 (Args) new Args().setParms( 
new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ArgList) new AST_ArgList()

.add( (AST_ArgListElem) new AST_ArgListElem().setParms( (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms("","q", 0)) /* NameId */
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
) /* AstOptNode */
, new AstToken().setParms(" ","}", 0)) /* BlockC */
) /* ForStmt */
))/* AST_StmtElem + add */
).patch()
;
	((Environment) _E).addAlias("loop",  z);

	return(AST_Stmt) AstNode.markStack(AstNode.aliasStack.size(),  (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","v", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","0", 0)) /* IntLit */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","loop", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","v", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","1", 0)) /* IntLit */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
).patch()
;  // returns v = 0; <loop>; v = 1
    }

    // Note: we still don't have the Alias construct quite right
    // we can only alias identifiers that were predeclared
    // not augmented... (but this will have to wait until later)

    AST_Stmt atest4() {
	bar b = new bar(this,5);
	return b.atest4();
    }

    AST_FieldDecl atest5() {
	bar b = new bar(this,5);
	return b.atest5();
    }

    AST_Class atest6() {
	((Environment) _E).addAlias("paramlist", (AST_ParList) AstNode.markStack(AstNode.aliasStack.size(),  (AST_ParList) new AST_ParList()

.add( (AST_ParListElem) new AST_ParListElem().setParms( (FormParDecl) new FormParDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (PrimType) new PrimType().setParms( 
 (IntTyp) new IntTyp().setParms( 
new AstToken().setParms(" ","int", 0)) /* IntTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","a", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
) /* FormParDecl */
))/* AST_ParListElem + add */

.add( (AST_ParListElem) new AST_ParListElem().setParms(new AstToken().setParms("",",", 0),
 (FormParDecl) new FormParDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (PrimType) new PrimType().setParms( 
 (IntTyp) new IntTyp().setParms( 
new AstToken().setParms(" ","int", 0)) /* IntTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","b", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
) /* FormParDecl */
))/* AST_ParListElem + add */
).patch()
);

	((Environment) _E).addAlias("method", (AST_FieldDecl) AstNode.markStack(AstNode.aliasStack.size(),  (AST_FieldDecl) new AST_FieldDecl()

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (ConDecl) new ConDecl().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","biff", 0)) /* NameId */
, new AstToken().setParms("","(", 0),  new AstOptNode(
).setParms( (AST_ParList) new AST_ParList()

.add( (AST_ParListElem) new AST_ParListElem().setParms( (PlstIscape) new PlstIscape().setParms(_E, new AstToken().setParms(" ","paramlist", 0)) /* PlstIscape */
))/* AST_ParListElem + add */
) /* AstOptNode */
, new AstToken().setParms(" ",")", 0),  new AstOptNode(
) /* AstOptNode */
, new AstToken().setParms(" ","{", 0),  new AstOptNode(
) /* AstOptNode */
,  new AstOptNode(
).setParms( (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","x", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","a", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","y", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","b", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms(" ","}", 0)) /* ConDecl */
))/* AST_FieldDeclElem + add */
).patch()
);

	((Environment) _E).addAlias("klass", (AST_Class) AstNode.markStack(AstNode.aliasStack.size(),  (AST_Class) new AST_Class()

.add( (AST_ClassElem) new AST_ClassElem().setParms( (ModTypeDecl) new ModTypeDecl().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (UmodClassDecl) new UmodClassDecl().setParms( 
new AstToken().setParms(" ","class", 0),  (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","biff", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
,  new AstOptNode(
) /* AstOptNode */
,  (ClsBody) new ClsBody().setParms( 
new AstToken().setParms(" ","{", 0),  new AstOptNode(
).setParms( (AST_FieldDecl) new AST_FieldDecl()

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (FldVarDec) new FldVarDec().setParms( 
 new AstOptNode(
).setParms( (AST_Modifiers) new AST_Modifiers()

.add( (AST_ModifiersElem) new AST_ModifiersElem().setParms( (ModPublic) new ModPublic().setParms( 
new AstToken().setParms(" ","public", 0)) /* ModPublic */
))/* AST_ModifiersElem + add */
) /* AstOptNode */
,  (PrimType) new PrimType().setParms( 
 (IntTyp) new IntTyp().setParms( 
new AstToken().setParms(" ","int", 0)) /* IntTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","x", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms(new AstToken().setParms("",",", 0),
 (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(_E, new AstToken().setParms("","y", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
, new AstToken().setParms("",";", 0)) /* FldVarDec */
))/* AST_FieldDeclElem + add */

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (MthIscape) new MthIscape().setParms(_E, new AstToken().setParms(" ","method", 0), new AstToken().setParms("",";", 0)) /* MthIscape */
))/* AST_FieldDeclElem + add */
) /* AstOptNode */
, new AstToken().setParms(" ","}", 0)) /* ClsBody */
) /* UmodClassDecl */
) /* ModTypeDecl */
))/* AST_ClassElem + add */
).patch()
);


	// implicit class
	return((AST_Class) AstNode.markStack(AstNode.aliasStack.size(),  (AST_Class) new AST_Class()

.add( (AST_ClassElem) new AST_ClassElem().setParms( (ClsIscape) new ClsIscape().setParms(_E, new AstToken().setParms(" ","klass", 0)) /* ClsIscape */
))/* AST_ClassElem + add */
).patch()
 );
    }
}

class bar {
	public Environment _E = (Environment) new Environment();
	public Environment getEnv() { return(_E); }

    int n;

    bar( foo f, int y ) {_E.addEnv( f.getEnv());

	n = y;
    }

    AST_Stmt atest4() {
	return(AST_Stmt) AstNode.markStack(AstNode.aliasStack.size(),  (AST_Stmt) new AST_Stmt()

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","v", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","0", 0)) /* IntLit */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","loop", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */

.add( (AST_StmtElem) new AST_StmtElem().setParms( (ExprStmt) new ExprStmt().setParms( 
 (AsgExpr) new AsgExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","v", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Assign) new Assign().setParms( 
new AstToken().setParms(" ","=", 0)) /* Assign */
,  (IntLit) new IntLit().setParms( 
new AstToken().setParms(" ","1", 0)) /* IntLit */
) /* AsgExpr */
, new AstToken().setParms("",";", 0)) /* ExprStmt */
))/* AST_StmtElem + add */
).patch()
;  // returns v = 0; <loop>; v = 1
    }

    AST_FieldDecl atest5() {
	// should generate a run-time error; i is aliased to be an expression
// 	return mth{ int i; }mth;
	return(AST_FieldDecl) AstNode.markStack(AstNode.aliasStack.size(),  (AST_FieldDecl) new AST_FieldDecl()

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (FldVarDec) new FldVarDec().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (PrimType) new PrimType().setParms( 
 (IntTyp) new IntTyp().setParms( 
new AstToken().setParms(" ","int", 0)) /* IntTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","q", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
, new AstToken().setParms("",";", 0)) /* FldVarDec */
))/* AST_FieldDeclElem + add */

.add( (AST_FieldDeclElem) new AST_FieldDeclElem().setParms( (MethodDcl) new MethodDcl().setParms( 
 new AstOptNode(
) /* AstOptNode */
,  (QNameType) new QNameType().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","AST_Exp", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
,  new AstOptNode(
) /* AstOptNode */
) /* QNameType */
,  (MthDector) new MthDector().setParms( 
 (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","x", 0)) /* NameId */
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

.add( (AST_StmtElem) new AST_StmtElem().setParms( (BlockStmt) new BlockStmt().setParms( 
 (LocalVarDecl) new LocalVarDecl().setParms( 
 new AstOptToken(
) /* AstOptToken */
,  (PrimType) new PrimType().setParms( 
 (IntTyp) new IntTyp().setParms( 
new AstToken().setParms(" ","int", 0)) /* IntTyp */
,  new AstOptNode(
) /* AstOptNode */
) /* PrimType */
,  (AST_VarDecl) new AST_VarDecl()

.add( (AST_VarDeclElem) new AST_VarDeclElem().setParms( (VarDecl) new VarDecl().setParms( 
 (DecNameDim) new DecNameDim().setParms( 
 (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","r", 0)) /* NameId */
,  new AstOptNode(
) /* AstOptNode */
) /* DecNameDim */
,  new AstOptNode(
).setParms( (VarAssignC) new VarAssignC().setParms( 
new AstToken().setParms(" ","=", 0),  (VarInitExpr) new VarInitExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","i", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* VarInitExpr */
) /* VarAssignC */
) /* AstOptNode */
) /* VarDecl */
))/* AST_VarDeclElem + add */
) /* LocalVarDecl */
, new AstToken().setParms("",";", 0)) /* BlockStmt */
))/* AST_StmtElem + add */
) /* AstOptNode */
, new AstToken().setParms(" ","}", 0)) /* BlockC */
) /* MDSBlock */
) /* MethodDcl */
))/* AST_FieldDeclElem + add */
).patch()
;
    }

    // note; replace return mth{ ... }mth with return mth{ int q; AST_Exp x()
    // { int r = i; } }mth
    // should return "int q; AST_Exp x() { int r = i_# + j_# + k_#; }"
}
