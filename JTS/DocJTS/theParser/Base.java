package theParser;
public class Base extends kernel {

    // NamedRule
    static public class ShiftExpr extends Lang.ShiftExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ShiftExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.ShiftExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class AST_ParList extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class AST_ParListElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AST_ParListElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.AST_ParListElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class IOEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class StrLit extends Lang.Literal {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.StrLit setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.StrLit) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ReturnStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnShR extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnShR setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnShR) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class AST_FieldDecl extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class AST_FieldDeclElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class EOEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnShL extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnShL setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnShL) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class IfStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Neq extends Lang.EqExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Neq setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Neq) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModPrivate extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModPrivate setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModPrivate) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Initializer extends Lang.ClassBodyDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class InterfaceDeclaration extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ExplicitConstructorInvocation extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class VariableDeclaratorId extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Eq extends Lang.EqExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Eq setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Eq) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ConditionalOrExpression extends Lang.ConditionalExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class QNameSuf extends Lang.PrimarySuffix {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.QNameSuf setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.QNameSuf) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Statement extends Lang.BlockStatement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class InterfaceMemberDeclaration extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnTimes extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnTimes setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnTimes) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AndExpression extends Lang.ExclusiveOrExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PreDecrementExpression extends Lang.UnaryExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MDeclLA extends Lang.MethodDeclarationLookahead {
        
        boolean order[] = { false, false, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MDeclLA setParms(Lang.AstNode _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.Token _arg3) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[1];
		arg[0] = _arg0;
		arg[1] = _arg1;
		arg[2] = _arg2;
		tok[0] = _arg3;;
            InitChildren();
            return((Lang.MDeclLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AST_TypeName extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Literal extends Lang.PrimaryPrefix {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PlusPlus2 extends Lang.PEPostIncDec {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PlusPlus2 setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.PlusPlus2) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class AST_Catches extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class AST_CatchesElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AddExprChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class MultiplicativeExpression extends Lang.AdditiveExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class REBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class AST_Modifiers extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class AST_ModifiersElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class RelationalExpression extends Lang.InstanceOfExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class SuperLayer extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PEStmtExpr extends Lang.StatementExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PEStmtExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.PEStmtExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ThisPre extends Lang.PrimaryPrefix {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ThisPre setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ThisPre) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class IOEBod extends Lang.IOEBody {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.IOEBod setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.IOEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Mult extends Lang.MultExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Mult setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Mult) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AllocExprChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AST_VarInit extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MinusMinus extends Lang.StmtExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MinusMinus setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.MinusMinus) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreExclOrExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreExclOrExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class SynchronizedStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class BoolTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.BoolTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.BoolTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class IntLit extends Lang.Literal {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.IntLit setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.IntLit) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class IdLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.IdLA setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.IdLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class DoStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class IntExtClauseC extends Lang.IntExtClause {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.IntExtClauseC setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.IntExtClauseC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MDSEmpty extends Lang.MethodDeclSuffix {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MDSEmpty setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.MDSEmpty) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AEBod extends Lang.AEBody {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AEBod setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.AEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AST_Exp extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class FormalParameter extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MDSBlock extends Lang.MethodDeclSuffix {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MDSBlock setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.MDSBlock) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ConditionalExpression extends Lang.Expression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LocalVarDecl extends Lang.LocalVariableDeclaration {
        
        boolean order[] = { true, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LocalVarDecl setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;;
            InitChildren();
            return((Lang.LocalVarDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PDExpr extends Lang.StatementExpression {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PDExpr setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.PDExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class IfStmt extends Lang.IfStatement {
        
        boolean order[] = { true, true, false, true, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.IfStmt setParms(Lang.Token _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4, Lang.AstNode _arg5) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		tok[1] = _arg1;
		arg[0] = _arg2;
		tok[2] = _arg3;
		arg[1] = _arg4;
		arg[2] = _arg5;;
            InitChildren();
            return((Lang.IfStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class BooleanLiteral extends Lang.Literal {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LyrImports extends Lang.LayerImports {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LyrImports setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.LyrImports) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class AST_Class extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class AST_ClassElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class SuperPre extends Lang.PrimaryPrefix {
        
        boolean order[] = { true, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.SuperPre setParms(Lang.Token _arg0, Lang.Token _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		tok[1] = _arg1;
		arg[0] = _arg2;;
            InitChildren();
            return((Lang.SuperPre) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class DotTimesC extends Lang.DotTimes {
        
        boolean order[] = { true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.DotTimesC setParms(Lang.Token _arg0, Lang.Token _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		tok[1] = _arg1;;
            InitChildren();
            return((Lang.DotTimesC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class AST_ArrayInit extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class AST_ArrayInitElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AST_ArrayInitElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.AST_ArrayInitElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class UmodClassDecl extends Lang.UnmodifiedClassDeclaration {
        
        boolean order[] = { true, false, false, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.UmodClassDecl setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.AstNode _arg3, Lang.AstNode _arg4) {
            arg = new Lang.AstNode[4];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;
		arg[2] = _arg3;
		arg[3] = _arg4;;
            InitChildren();
            return((Lang.UmodClassDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Args extends Lang.Arguments {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Args setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.Args) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ShiftExprChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LeOp extends Lang.RelExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LeOp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.LeOp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class BangLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.BangLA setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.BangLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class IoExpr extends Lang.InstanceOfExpression {
        
        boolean order[] = { false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.IoExpr setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[1];
		arg[0] = _arg0;
		tok[0] = _arg1;
		arg[1] = _arg2;;
            InitChildren();
            return((Lang.IoExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class UnmodifiedTypeDeclaration extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ArrayDimsAndInits extends Lang.AllocExprChoices {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ClassBodyDeclaration extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class InclusiveOrExpression extends Lang.ConditionalAndExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NameId extends Lang.QName {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NameId setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.NameId) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class BlockStatement extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnMod extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnMod setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnMod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ThisSuf extends Lang.PrimarySuffix {
        
        boolean order[] = { true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ThisSuf setParms(Lang.Token _arg0, Lang.Token _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		tok[1] = _arg1;;
            InitChildren();
            return((Lang.ThisSuf) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MinusUE extends Lang.UnaryExpression {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MinusUE setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.MinusUE) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreRelExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreRelExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ConSuper extends Lang.ExplicitConstructorInvocation {
        
        boolean order[] = { false, true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ConSuper setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[2];
		arg[0] = _arg0;
		tok[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;;
            InitChildren();
            return((Lang.ConSuper) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Assign extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Assign setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Assign) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PrimExpr extends Lang.PrimaryExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PrimExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.PrimExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PrimDotC extends Lang.PrimDot {
        
        boolean order[] = { false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PrimDotC setParms(Lang.AstNode _arg0, Lang.Token _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		arg[0] = _arg0;
		tok[0] = _arg1;;
            InitChildren();
            return((Lang.PrimDotC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class InterfaceMemberDeclarations extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class InterfaceMemberDeclarationsElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class FPLit extends Lang.Literal {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.FPLit setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.FPLit) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ReturnStm extends Lang.ReturnStatement {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ReturnStm setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ReturnStm) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class TildeUE extends Lang.UnaryExpressionNotPlusMinus {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.TildeUE setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.TildeUE) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ByteTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ByteTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ByteTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LabeledStmt extends Lang.LabeledStatement {
        
        boolean order[] = { false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LabeledStmt setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[1];
		arg[0] = _arg0;
		tok[0] = _arg1;
		arg[1] = _arg2;;
            InitChildren();
            return((Lang.LabeledStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class CastLookahead extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class EqualityExpression extends Lang.AndExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class FDecl extends Lang.InterfaceMemberDeclaration {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.FDecl setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.FDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class MultExprChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreAndExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreAndExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class ImportList extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class ImportListElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ImportListElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.ImportListElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModNative extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModNative setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModNative) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModVolatile extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModVolatile setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModVolatile) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PrimitiveType extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NIDecl extends Lang.InterfaceMemberDeclaration {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NIDecl setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.NIDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnAnd extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnAnd setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnAnd) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Null extends Lang.NullLiteral {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Null setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Null) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PostfixExpression extends Lang.UnaryExpressionNotPlusMinus {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class FieldDeclaration extends Lang.ClassBodyDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PEPostIncDec extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Catch extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class DoWhileStm extends Lang.DoStatement {
        
        boolean order[] = { true, false, true, true, false, true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.DoWhileStm setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2, Lang.Token _arg3, Lang.AstNode _arg4, Lang.Token _arg5, Lang.Token _arg6) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[5];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;
		tok[2] = _arg3;
		arg[1] = _arg4;
		tok[3] = _arg5;
		tok[4] = _arg6;;
            InitChildren();
            return((Lang.DoWhileStm) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PrimarySuffix extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class MethodDeclaration extends Lang.ClassBodyDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class CastExpression extends Lang.UnaryExpressionNotPlusMinus {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class EqExpr extends Lang.EqualityExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.EqExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.EqExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ClassBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class LocalVariableDeclaration extends Lang.ForInit {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ForStmt extends Lang.ForStatement {
        
        boolean order[] = { true, true, false, true, false, true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ForStmt setParms(Lang.Token _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4, Lang.Token _arg5, Lang.AstNode _arg6, Lang.Token _arg7, Lang.AstNode _arg8) {
            arg = new Lang.AstNode[4];
		tok = new Lang.Token[5];
		tok[0] = _arg0;
		tok[1] = _arg1;
		arg[0] = _arg2;
		tok[2] = _arg3;
		arg[1] = _arg4;
		tok[3] = _arg5;
		arg[2] = _arg6;
		tok[4] = _arg7;
		arg[3] = _arg8;;
            InitChildren();
            return((Lang.ForStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MEBod extends Lang.MEBody {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MEBod setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.MEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModFinal extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModFinal setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModFinal) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class NullLiteral extends Lang.Literal {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnShRR extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnShRR setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnShRR) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class SupLayer extends Lang.SuperLayer {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.SupLayer setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.SupLayer) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Cla3 extends Lang.CastLookahead {
        
        boolean order[] = { true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Cla3 setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2, Lang.AstNode _arg3) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;
		arg[1] = _arg3;;
            InitChildren();
            return((Lang.Cla3) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Cla2 extends Lang.CastLookahead {
        
        boolean order[] = { true, false, true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Cla2 setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2, Lang.Token _arg3) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;
		tok[2] = _arg3;;
            InitChildren();
            return((Lang.Cla2) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Cla1 extends Lang.CastLookahead {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Cla1 setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.Cla1) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class SwitchStmt extends Lang.SwitchStatement {
        
        boolean order[] = { true, true, false, true, true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.SwitchStmt setParms(Lang.Token _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.Token _arg4, Lang.AstNode _arg5, Lang.Token _arg6) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[5];
		tok[0] = _arg0;
		tok[1] = _arg1;
		arg[0] = _arg2;
		tok[2] = _arg3;
		tok[3] = _arg4;
		arg[1] = _arg5;
		tok[4] = _arg6;;
            InitChildren();
            return((Lang.SwitchStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ExprStmt extends Lang.Statement {
        
        boolean order[] = { false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ExprStmt setParms(Lang.AstNode _arg0, Lang.Token _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		arg[0] = _arg0;
		tok[0] = _arg1;;
            InitChildren();
            return((Lang.ExprStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AdEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ConDecl extends Lang.ConstructorDeclaration {
        
        boolean order[] = { false, false, true, false, true, false, true, false, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ConDecl setParms(Lang.AstNode _arg0, Lang.AstNode _arg1, Lang.Token _arg2, Lang.AstNode _arg3, Lang.Token _arg4, Lang.AstNode _arg5, Lang.Token _arg6, Lang.AstNode _arg7, Lang.AstNode _arg8, Lang.Token _arg9) {
            arg = new Lang.AstNode[6];
		tok = new Lang.Token[4];
		arg[0] = _arg0;
		arg[1] = _arg1;
		tok[0] = _arg2;
		arg[2] = _arg3;
		tok[1] = _arg4;
		arg[3] = _arg5;
		tok[2] = _arg6;
		arg[4] = _arg7;
		arg[5] = _arg8;
		tok[3] = _arg9;;
            InitChildren();
            return((Lang.ConDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class BlockC extends Lang.Block {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.BlockC setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.BlockC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NClassDecl extends Lang.NestedClassDeclaration {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NClassDecl setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.NClassDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class UnmodifiedInterfaceDeclaration extends Lang.UnmodifiedTypeDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class LayerModifiers extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class LayerModifiersElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Init extends Lang.Initializer {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Init setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.Init) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Empty extends Lang.EmptyStatement {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Empty setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Empty) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModAbstract extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModAbstract setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModAbstract) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PrimType extends Lang.AST_TypeName {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PrimType setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.PrimType) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ExclOrExpr extends Lang.ExclusiveOrExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ExclOrExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.ExclOrExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NotUE extends Lang.UnaryExpressionNotPlusMinus {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NotUE setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.NotUE) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class VarAssign extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class EOEBod extends Lang.EOEBody {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.EOEBod setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.EOEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Block extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Div extends Lang.MultExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Div setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Div) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ThrowsClauseC extends Lang.ThrowsClause {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ThrowsClauseC setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.ThrowsClauseC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnPlus extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnPlus setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnPlus) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ContinueStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class GeOp extends Lang.RelExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.GeOp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.GeOp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class BlkInterDcl extends Lang.BlockStatement {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.BlkInterDcl setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.BlkInterDcl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LtOp extends Lang.RelExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LtOp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.LtOp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class OpParenLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.OpParenLA setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.OpParenLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class UnaryExpression extends Lang.MultiplicativeExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PDecExpr extends Lang.PreDecrementExpression {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PDecExpr setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.PDecExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PrimAllocExpr extends Lang.AllocationExpression {
        
        boolean order[] = { true, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PrimAllocExpr setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;;
            InitChildren();
            return((Lang.PrimAllocExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Dim extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PackStm extends Lang.PackageDeclaration {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PackStm setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.PackStm) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModProtected extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModProtected setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModProtected) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class program extends Lang.AST_Program {
        
        boolean order[] = { false, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.program setParms(Lang.AstNode _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[3];
		arg[0] = _arg0;
		arg[1] = _arg1;
		arg[2] = _arg2;;
            InitChildren();
            return((Lang.program) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ExtendsClause extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NewLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NewLA setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.NewLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NonRelMod extends Lang.LayerModifier {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NonRelMod setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.NonRelMod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnXor extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnXor setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnXor) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ArrInit extends Lang.AST_VarInit {
        
        boolean order[] = { true, false, true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ArrInit setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2, Lang.Token _arg3) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;
		tok[2] = _arg3;;
            InitChildren();
            return((Lang.ArrInit) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ForInit extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class AST_TypeNameList extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class AST_TypeNameListElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AST_TypeNameListElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.AST_TypeNameListElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class QuestExpr extends Lang.ConditionalExpression {
        
        boolean order[] = { false, true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.QuestExpr setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[2];
		arg[0] = _arg0;
		tok[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;
		arg[2] = _arg4;;
            InitChildren();
            return((Lang.QuestExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class SyncStmt extends Lang.SynchronizedStatement {
        
        boolean order[] = { true, true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.SyncStmt setParms(Lang.Token _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		tok[1] = _arg1;
		arg[0] = _arg2;
		tok[2] = _arg3;
		arg[1] = _arg4;;
            InitChildren();
            return((Lang.SyncStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class DefLabel extends Lang.SwitchLabel {
        
        boolean order[] = { true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.DefLabel setParms(Lang.Token _arg0, Lang.Token _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		tok[1] = _arg1;;
            InitChildren();
            return((Lang.DefLabel) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MinusMinus2 extends Lang.PEPostIncDec {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MinusMinus2 setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.MinusMinus2) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class WhileStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ExprPre extends Lang.PrimaryPrefix {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ExprPre setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ExprPre) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class VariableDeclarator extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PlusUE extends Lang.UnaryExpression {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PlusUE setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.PlusUE) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class RelExpr extends Lang.RelationalExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.RelExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.RelExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class StmExprList extends Lang.ForUpdate {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.StmExprList setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.StmExprList) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class QNameType extends Lang.AST_TypeName {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.QNameType setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.QNameType) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LShift extends Lang.ShiftExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LShift setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.LShift) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class VarDecl extends Lang.VariableDeclarator {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.VarDecl setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.VarDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class AST_QualifiedName extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class AST_QualifiedNameElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AST_QualifiedNameElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.AST_QualifiedNameElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreEqExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreEqExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AllocationExpression extends Lang.PrimaryPrefix {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class False extends Lang.BooleanLiteral {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.False setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.False) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class IntTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.IntTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.IntTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class Suffixes extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class SuffixesElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class NestedClassDeclaration extends Lang.ClassBodyDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModTransient extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModTransient setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModTransient) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AdEBod extends Lang.AdEBody {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AdEBod setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.AdEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class EmptyTDecl extends Lang.TypeDeclaration {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.EmptyTDecl setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.EmptyTDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AndExpr extends Lang.AndExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AndExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.AndExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class CAEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LyrHeader extends Lang.LayerHeader {
        
        boolean order[] = { false, true, false, true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LyrHeader setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4, Lang.Token _arg5) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[3];
		arg[0] = _arg0;
		tok[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;
		arg[2] = _arg4;
		tok[2] = _arg5;;
            InitChildren();
            return((Lang.LyrHeader) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class SwitchStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class MethodDeclarationLookahead extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Plus extends Lang.AddExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Plus setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Plus) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class COEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class TName extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ThrowStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class DotTimes extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class TypeDeclaration extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ClsBody extends Lang.ClassBody {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ClsBody setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ClsBody) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PlusPlus extends Lang.StmtExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PlusPlus setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.PlusPlus) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModPublic extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModPublic setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModPublic) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreMultExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreMultExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Modifier extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class EqExprChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class TryStmt extends Lang.TryStatement {
        
        boolean order[] = { true, false, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.TryStmt setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.AstNode _arg3) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;
		arg[2] = _arg3;;
            InitChildren();
            return((Lang.TryStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class InterDecl extends Lang.InterfaceDeclaration {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.InterDecl setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.InterDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ExprSuf extends Lang.PrimarySuffix {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ExprSuf setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ExprSuf) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ConditionalAndExpression extends Lang.ConditionalOrExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LongTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LongTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.LongTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModRelative extends Lang.LayerModifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModRelative setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModRelative) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AsgExpr extends Lang.Expression {
        
        boolean order[] = { false, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AsgExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[3];
		arg[0] = _arg0;
		arg[1] = _arg1;
		arg[2] = _arg2;;
            InitChildren();
            return((Lang.AsgExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreInclOrExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreInclOrExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LayerDecl extends Lang.Layer_Decl {
        
        boolean order[] = { false, true, false, true, false, true, true, false, false, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LayerDecl setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4, Lang.Token _arg5, Lang.Token _arg6, Lang.AstNode _arg7, Lang.AstNode _arg8, Lang.AstNode _arg9, Lang.AstNode _arg10) {
            arg = new Lang.AstNode[7];
		tok = new Lang.Token[4];
		arg[0] = _arg0;
		tok[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;
		arg[2] = _arg4;
		tok[2] = _arg5;
		tok[3] = _arg6;
		arg[3] = _arg7;
		arg[4] = _arg8;
		arg[5] = _arg9;
		arg[6] = _arg10;;
            InitChildren();
            return((Lang.LayerDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PreIncrementExpression extends Lang.UnaryExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ImportPack extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PEIncDec extends Lang.PostfixExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PEIncDec setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.PEIncDec) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreShiftExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreShiftExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class GtOp extends Lang.RelExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.GtOp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.GtOp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModTypeDecl extends Lang.TypeDeclaration {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModTypeDecl setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.ModTypeDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class AST_ArgList extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class AST_ArgListElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AST_ArgListElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.AST_ArgListElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class LayerImports extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class DoubleTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.DoubleTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.DoubleTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AllocSuf extends Lang.PrimarySuffix {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AllocSuf setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.AllocSuf) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class RShift extends Lang.ShiftExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.RShift setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.RShift) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PrimaryExpression extends Lang.PostfixExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ThisLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ThisLA setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ThisLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CharLit extends Lang.Literal {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CharLit setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.CharLit) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MultExpr extends Lang.MultiplicativeExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MultExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.MultExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class SuperLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.SuperLA setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.SuperLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreCondOrExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreCondOrExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class TildeLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.TildeLA setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.TildeLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class VarAssignC extends Lang.VarAssign {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.VarAssignC setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.VarAssignC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class EEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ExDimBod extends Lang.ExDimBody {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ExDimBod setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ExDimBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CAEBod extends Lang.CAEBody {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CAEBod setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.CAEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AssignmentOperator extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class TNClass extends Lang.TName {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.TNClass setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.TNClass) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class RRShift extends Lang.ShiftExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.RRShift setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.RRShift) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class FormParDecl extends Lang.FormalParameter {
        
        boolean order[] = { true, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.FormParDecl setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;;
            InitChildren();
            return((Lang.FormParDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnMinus extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnMinus setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnMinus) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class LayerModifier extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class AST_Stmt extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class AST_StmtElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ConstructorDeclaration extends Lang.ClassBodyDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class True extends Lang.BooleanLiteral {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.True setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.True) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Minus extends Lang.AddExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Minus setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Minus) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class COEBod extends Lang.COEBody {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.COEBod setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.COEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Layer_Decl extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ObjAllocExpr extends Lang.AllocationExpression {
        
        boolean order[] = { true, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ObjAllocExpr setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;;
            InitChildren();
            return((Lang.ObjAllocExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class WhileStm extends Lang.WhileStatement {
        
        boolean order[] = { true, true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.WhileStm setParms(Lang.Token _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		tok[1] = _arg1;
		arg[0] = _arg2;
		tok[2] = _arg3;
		arg[1] = _arg4;;
            InitChildren();
            return((Lang.WhileStm) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NInterDecl extends Lang.NestedInterfaceDeclaration {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NInterDecl setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.NInterDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class AST_ExpStmt extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class AST_ExpStmtElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AST_ExpStmtElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.AST_ExpStmtElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class LayerHeader extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class Dims extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class DimsElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ElseClauseC extends Lang.ElseClause {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ElseClauseC setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.ElseClauseC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MthCall extends Lang.PrimarySuffix {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MthCall setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.MthCall) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AnonClass extends Lang.AllocExprChoices {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AnonClass setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.AnonClass) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class TE_Generator extends Lang.AST_Program {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PIncExpr extends Lang.PreIncrementExpression {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PIncExpr setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.PIncExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ForUpdate extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreAddExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreAddExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ShiftExpression extends Lang.RelationalExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MthDector extends Lang.MethodDeclarator {
        
        boolean order[] = { false, true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MthDector setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[2];
		arg[0] = _arg0;
		tok[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;
		arg[2] = _arg4;;
            InitChildren();
            return((Lang.MthDector) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class NCDecl extends Lang.InterfaceMemberDeclaration {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.NCDecl setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.NCDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class RTPre extends Lang.PrimaryPrefix {
        
        boolean order[] = { false, true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.RTPre setParms(Lang.AstNode _arg0, Lang.Token _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		arg[0] = _arg0;
		tok[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.RTPre) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class FinallyC extends Lang.Finally {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.FinallyC setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.FinallyC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnDiv extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnDiv setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnDiv) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class CastLookaheadChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class BreakStm extends Lang.BreakStatement {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.BreakStm setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.BreakStm) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class BreakStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class EmptyStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class FIExprList extends Lang.ForInit {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.FIExprList setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.FIExprList) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class QName extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class SwitchLabel extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ForStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class TryStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class ExprDims extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class ExprDimsElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Finally extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class NestedInterfaceDeclaration extends Lang.ClassBodyDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ImplClause extends Lang.ImplementsClause {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ImplClause setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.ImplClause) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ImpQual extends Lang.ImportDeclaration {
        
        boolean order[] = { true, false, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ImpQual setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.Token _arg3) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;;
            InitChildren();
            return((Lang.ImpQual) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class DecNameDim extends Lang.VariableDeclaratorId {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.DecNameDim setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.DecNameDim) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ExtClause extends Lang.ExtendsClause {
        
        boolean order[] = { true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ExtClause setParms(Lang.Token _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;
		arg[0] = _arg1;;
            InitChildren();
            return((Lang.ExtClause) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModSynchronized extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModSynchronized setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModSynchronized) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class UnmodifiedClassDeclaration extends Lang.UnmodifiedTypeDeclaration {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Dim2 extends Lang.Dim {
        
        boolean order[] = { true, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Dim2 setParms(Lang.Token _arg0, Lang.Token _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		tok[1] = _arg1;;
            InitChildren();
            return((Lang.Dim2) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CatchStmt extends Lang.Catch {
        
        boolean order[] = { true, true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CatchStmt setParms(Lang.Token _arg0, Lang.Token _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		tok[1] = _arg1;
		arg[0] = _arg2;
		tok[2] = _arg3;
		arg[1] = _arg4;;
            InitChildren();
            return((Lang.CatchStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class InstanceOfExpression extends Lang.EqualityExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ElseClause extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class VarInitExpr extends Lang.AST_VarInit {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.VarInitExpr setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.VarInitExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class SwitchEntryBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PrimaryPrefix extends Lang.PrimaryExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MDecl extends Lang.InterfaceMemberDeclaration {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MDecl setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.MDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class StatementExpressionList extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class StatementExpressionListElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.StatementExpressionListElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.StatementExpressionListElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class EEBodyC extends Lang.EEBody {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.EEBodyC setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.EEBodyC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class MEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class RelExprChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class MethodDcl extends Lang.MethodDeclaration {
        
        boolean order[] = { false, false, false, false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.MethodDcl setParms(Lang.AstNode _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.AstNode _arg3, Lang.AstNode _arg4) {
            arg = new Lang.AstNode[5];
		arg[0] = _arg0;
		arg[1] = _arg1;
		arg[2] = _arg2;
		arg[3] = _arg3;
		arg[4] = _arg4;;
            InitChildren();
            return((Lang.MethodDcl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnExpr extends Lang.StmtExprChoices {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.AssnExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ArrDim2 extends Lang.ArrayDimsAndInits {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ArrDim2 setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.ArrDim2) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ArrDim1 extends Lang.ArrayDimsAndInits {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ArrDim1 setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.ArrDim1) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PackageDeclaration extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class IntExtClause extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ContinueStm extends Lang.ContinueStatement {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ContinueStm setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ContinueStm) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ImportPak extends Lang.ImportPack {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ImportPak setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.ImportPak) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PIExpr extends Lang.StatementExpression {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PIExpr setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.PIExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class UnaryExpressionNotPlusMinus extends Lang.UnaryExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class InclOrExpr extends Lang.InclusiveOrExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.InclOrExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.InclOrExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Arguments extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AST_Program extends Lang.AstNode {
                public static AST_Program root;
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class FldVarDec extends Lang.FieldDeclaration {
        
        boolean order[] = { false, false, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.FldVarDec setParms(Lang.AstNode _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.Token _arg3) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[1];
		arg[0] = _arg0;
		arg[1] = _arg1;
		arg[2] = _arg2;
		tok[0] = _arg3;;
            InitChildren();
            return((Lang.FldVarDec) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class PPQualName extends Lang.PrimaryPrefix {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.PPQualName setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.PPQualName) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList
    static public class AST_VarDecl extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // ComplexList (Elem)
    static public class AST_VarDeclElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AST_VarDeclElem setParms(Lang.AstToken _arg1,
                                          Lang.AstNode _arg2) {
            if (_arg1 == null)
                _arg1 = new Lang.AstToken().setParms("", "", 0);
            setParms(_arg2);
            tok = new Lang.AstToken[1];
            tok[0] = _arg1;
            return((Lang.AST_VarDeclElem) this);
        }

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LDecl extends Lang.AST_Program {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LDecl setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.LDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ImplementsClause extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class MethodDeclarator extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ExDimBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class PrimDot extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CondOrExpr extends Lang.ConditionalOrExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CondOrExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.CondOrExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ConThis extends Lang.ExplicitConstructorInvocation {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ConThis setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ConThis) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class UmInterDecl extends Lang.UnmodifiedInterfaceDeclaration {
        
        boolean order[] = { true, false, false, true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.UmInterDecl setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4, Lang.Token _arg5) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;
		arg[2] = _arg4;
		tok[2] = _arg5;;
            InitChildren();
            return((Lang.UmInterDecl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ShortTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ShortTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ShortTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CondAndExpr extends Lang.ConditionalAndExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CondAndExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.CondAndExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class MoreCondAndExpr extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class MoreCondAndExprElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class Mod extends Lang.MultExprChoices {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.Mod setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.Mod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class VoidTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.VoidTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.VoidTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class LabeledStatement extends Lang.Statement {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ImportDeclaration extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class AST_Imports extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class AST_ImportsElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class SEBod extends Lang.SwitchEntryBody {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.SEBod setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.SEBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ExclusiveOrExpression extends Lang.InclusiveOrExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class MethodDeclSuffix extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class StmtExprChoices extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class BlkClassDcl extends Lang.BlockStatement {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.BlkClassDcl setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.BlkClassDcl) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AddExpr extends Lang.AdditiveExpression {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AddExpr setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.AddExpr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class Expression extends Lang.AST_Exp {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class AssnOr extends Lang.AssignmentOperator {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.AssnOr setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.AssnOr) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class SEBodyC extends Lang.SEBody {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.SEBodyC setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.SEBodyC) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class FloatTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.FloatTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.FloatTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class LitLA extends Lang.CastLookaheadChoices {
        
        boolean order[] = { false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.LitLA setParms(Lang.AstNode _arg0) {
            arg = new Lang.AstNode[1];
		arg[0] = _arg0;;
            InitChildren();
            return((Lang.LitLA) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class AdditiveExpression extends Lang.ShiftExpression {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CaseLabel extends Lang.SwitchLabel {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CaseLabel setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.CaseLabel) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class SEBody extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class BlockStmt extends Lang.BlockStatement {
        
        boolean order[] = { false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.BlockStmt setParms(Lang.AstNode _arg0, Lang.Token _arg1) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		arg[0] = _arg0;
		tok[0] = _arg1;;
            InitChildren();
            return((Lang.BlockStmt) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CharTyp extends Lang.PrimitiveType {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CharTyp setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.CharTyp) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class StatementExpression extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CastExpr2 extends Lang.CastExpression {
        
        boolean order[] = { true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CastExpr2 setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2, Lang.AstNode _arg3) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;
		arg[1] = _arg3;;
            InitChildren();
            return((Lang.CastExpr2) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ThrowStm extends Lang.ThrowStatement {
        
        boolean order[] = { true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ThrowStm setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;;
            InitChildren();
            return((Lang.ThrowStm) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class CastExpr1 extends Lang.CastExpression {
        
        boolean order[] = { true, false, true, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.CastExpr1 setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.Token _arg2, Lang.AstNode _arg3) {
            arg = new Lang.AstNode[2];
		tok = new Lang.Token[2];
		tok[0] = _arg0;
		arg[0] = _arg1;
		tok[1] = _arg2;
		arg[1] = _arg3;;
            InitChildren();
            return((Lang.CastExpr1) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList
    static public class AST_SwitchEntry extends Lang.AstList {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckElemType(Lang.AstNode l)
            { return(true); }
        public String ElemType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // SimpleList (Elem)
    static public class AST_SwitchEntryElem extends Lang.AstListNode {
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean CheckArgType() { return(true); }
        public String ArgType() { return(""); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class TEGen extends Lang.TE_Generator {
        
        boolean order[] = { true, false, false, true, false, true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.TEGen setParms(Lang.Token _arg0, Lang.AstNode _arg1, Lang.AstNode _arg2, Lang.Token _arg3, Lang.AstNode _arg4, Lang.Token _arg5) {
            arg = new Lang.AstNode[3];
		tok = new Lang.Token[3];
		tok[0] = _arg0;
		arg[0] = _arg1;
		arg[1] = _arg2;
		tok[1] = _arg3;
		arg[2] = _arg4;
		tok[2] = _arg5;;
            InitChildren();
            return((Lang.TEGen) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // Non-leaf virtual class (gen'd by BaliProduction)
    static public class ThrowsClause extends Lang.AstNode {
        
        static private int first_subclass = 0;
        static private int class_code = 0;

        public boolean SyntaxCheck() { return(false); }
        public boolean[] printorder() { return(null); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class ModStatic extends Lang.Modifier {
        
        boolean order[] = { true };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.ModStatic setParms(Lang.Token _arg0) {
            arg = new Lang.AstNode[1];
		tok = new Lang.Token[1];
		tok[0] = _arg0;;
            InitChildren();
            return((Lang.ModStatic) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }

    // NamedRule
    static public class REBod extends Lang.REBody {
        
        boolean order[] = { false, false };
        static private int first_subclass = 0;
        static private int class_code = 0;

        public Lang.REBod setParms(Lang.AstNode _arg0, Lang.AstNode _arg1) {
            arg = new Lang.AstNode[2];
		arg[0] = _arg0;
		arg[1] = _arg1;;
            InitChildren();
            return((Lang.REBod) this);
        }

        public boolean[] printorder() { return(order); }
        public boolean SyntaxCheck() { return(true); }
        public int firstSubclass() { return(first_subclass); }
        public int classCode() { return(class_code); }
    }


	static Lang.AstNode getStartRoot() {
		return(AST_Program.root);
	}

	static Lang.AstNode getStartRoot(BaliParser parser)
	    throws ParseException {
		AST_Program.root = parser.AST_Program();
		return(AST_Program.root);
	}
}
