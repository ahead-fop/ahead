
import java.io.PrintWriter;
import jak2java.* ;

public class AliasTest3 {
    public static void main(String args[]) {
	dummy d = new dummy();

	d.test();
    }
}

	
class dummy {
	public Environment _E = (Environment) new Environment().addId("x");
	public Environment getEnv() { return(_E); }


    public void test() {
	AST_Exp expression;
	AstProperties props = new AstProperties();
	PrintWriter pw = new PrintWriter(System.out);

	props.setProperty("output", pw);
	expression =(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (PrimExpr) new PrimExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","xArr", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (Suffixes) new Suffixes()

.add( (SuffixesElem) new SuffixesElem().setParms( (ExprSuf) new ExprSuf().setParms( 
new AstToken().setParms("","[", 0),  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms("","ipos", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
, new AstToken().setParms("","]", 0)) /* ExprSuf */
))/* SuffixesElem + add */
) /* PrimExpr */
).patch()
;
	((Environment) _E).addAlias("x",  expression);

	expression =(AST_Exp) AstNode.markStack(AstNode.aliasStack.size(),  (AddExpr) new AddExpr().setParms( 
 (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms(" ","x", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
,  (MoreAddExpr) new MoreAddExpr()

.add( (MoreAddExprElem) new MoreAddExprElem().setParms( (AdEBod) new AdEBod().setParms( 
 (Plus) new Plus().setParms( 
new AstToken().setParms("","+", 0)) /* Plus */
,  (PPQualName) new PPQualName().setParms( 
 (AST_QualifiedName) new AST_QualifiedName()

.add( (AST_QualifiedNameElem) new AST_QualifiedNameElem().setParms( (NameId) new NameId().setParms(_E, new AstToken().setParms("","y", 0)) /* NameId */
))/* AST_QualifiedNameElem + add */
) /* PPQualName */
) /* AdEBod */
))/* MoreAddExprElem + add */
) /* AddExpr */
).patch()
;
	expression.reduce2java(props);
	pw.println();
	pw.flush();
    }
}
