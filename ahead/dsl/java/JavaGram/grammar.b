// -*- Mode: Java -*-

options {
    CACHE_TOKENS = true ;
    JAVA_UNICODE_ESCAPE = true ;
    //OPTIMIZE_TOKEN_MANAGER = true ;
    STATIC = false ;

    // Section -- options for debugging:
    //
    // DEBUG_PARSER = true ;
    // DEBUG_LOOKAHEAD = true ;
    // DEBUG_TOKEN_MANAGER = true ;
    //
    // FORCE_LA_CHECK = true ;
    // CHOICE_AMBIGUITY_CHECK=5 ;
    // OTHER_AMBIGUITY_CHECK=5 ;
} options

/* RESERVED WORDS AND LITERALS */

TOKEN :
{
  < ABSTRACT: "abstract" >
| < BOOLEAN: "boolean" >
| < BREAK: "break" >
| < BYTE: "byte" >
| < CASE: "case" >
| < CATCH: "catch" >
| < CHAR: "char" >
| < CLASS: "class" >
| < CONST: "const" >
| < CONTINUE: "continue" >
| < _DEFAULT: "default" >
| < DO: "do" >
| < DOUBLE: "double" >
| < ELSE: "else" >
| < EXTENDS: "extends" >
| < FALSE: "false" >
| < FINAL: "final" >
| < FINALLY: "finally" >
| < FLOAT: "float" >
| < FOR: "for" >
| < GOTO: "goto" >
| < IF: "if" >
| < IMPLEMENTS: "implements" >
| < IMPORT: "import" >
| < INSTANCEOF: "instanceof" >
| < INT: "int" >
| < INTERFACE: "interface" >
| < LONG: "long" >
| < NATIVE: "native" >
| < NEW: "new" >
| < NULL: "null" >
| < PACKAGE: "package">
| < PRIVATE: "private" >
| < PROTECTED: "protected" >
| < PUBLIC: "public" >
| < RETURN: "return" >
| < SHORT: "short" >
| < STATIC: "static" >
| < SUPER: "super" >
| < SWITCH: "switch" >
| < SYNCHRONIZED: "synchronized" >
| < THIS: "this" >
| < THROW: "throw" >
| < THROWS: "throws" >
| < TRANSIENT: "transient" >
| < TRUE: "true" >
| < TRY: "try" >
| < VOID: "void" >
| < VOLATILE: "volatile" >
| < WHILE: "while" >
}

/* LITERALS */

TOKEN :
{
  < INTEGER_LITERAL:
        <DECIMAL_LITERAL> (["l","L"])?
      | <HEX_LITERAL> (["l","L"])?
      | <OCTAL_LITERAL> (["l","L"])?
  >
|
  < #DECIMAL_LITERAL: ["1"-"9"] (["0"-"9"])* >
|
  < #HEX_LITERAL: "0" ["x","X"] (["0"-"9","a"-"f","A"-"F"])+ >
|
  < #OCTAL_LITERAL: "0" (["0"-"7"])* >
|
  < FLOATING_POINT_LITERAL:
        (["0"-"9"])+ "." (["0"-"9"])* (<EXPONENT>)? (["f","F","d","D"])?
      | "." (["0"-"9"])+ (<EXPONENT>)? (["f","F","d","D"])?
      | (["0"-"9"])+ <EXPONENT> (["f","F","d","D"])?
      | (["0"-"9"])+ (<EXPONENT>)? ["f","F","d","D"]
  >
|
  < #EXPONENT: ["e","E"] (["+","-"])? (["0"-"9"])+ >
|
  < CHARACTER_LITERAL:
      "'"
      (   (~["'","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )
      "'"
  >
|
  < STRING_LITERAL:
      "\""
      (   (~["\"","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )*
      "\""
  >
}

/* SEPARATORS */

TOKEN :
{
  < LBRACE: "{" >
| < RBRACE: "}" >
| < LBRACKET: "[" >
| < RBRACKET: "]" >
| < SEMICOLON: ";" >
| < COMMA: "," >
| < DOT: "." >
}


/* OPERATORS */

TOKEN :
{
  < ASSIGN: "=" >
| < GT: ">" >
| < LT: "<" >
| < BANG: "!" >
| < TILDE: "~" >
| < HOOK: "?" >
| < COLON: ":" >
| < EQ: "==" >
| < LE: "<=" >
| < GE: ">=" >
| < NE: "!=" >
| < SC_OR: "||" >
| < SC_AND: "&&" >
| < INCR: "++" >
| < DECR: "--" >
| < PLUS: "+" >
| < MINUS: "-" >
| < STAR: "*" >
| < SLASH: "/" >
| < BIT_AND: "&" >
| < BIT_OR: "|" >
| < XOR: "^" >
| < REM: "%" >
| < LSHIFT: "<<" >
| < RSIGNEDSHIFT: ">>" >
| < RUNSIGNEDSHIFT: ">>>" >
| < PLUSASSIGN: "+=" >
| < MINUSASSIGN: "-=" >
| < STARASSIGN: "*=" >
| < SLASHASSIGN: "/=" >
| < ANDASSIGN: "&=" >
| < ORASSIGN: "|=" >
| < XORASSIGN: "^=" >
| < REMASSIGN: "%=" >
| < LSHIFTASSIGN: "<<=" >
| < RSIGNEDSHIFTASSIGN: ">>=" >
| < RUNSIGNEDSHIFTASSIGN: ">>>=" >
}

"("		LPAREN
")"		RPAREN


/*****************************************
 * THE JAVA LANGUAGE GRAMMAR STARTS HERE *
 *****************************************/

/*
 * Program structuring syntax follows.
 */

AST_Program
	: [ PackageDeclaration ] [ AST_Imports ] [ AST_Class ]
								::program
	;

AST_Imports
	: ( ImportDeclaration )+
	;

AST_Class
	: ( TypeDeclaration )+
	;

PackageDeclaration
	: "package" AST_QualifiedName ";"			::PackStm
	;

ImportDeclaration
	: "import" AST_QualifiedName [ DotTimes ] ";"		::ImpQual
	;

DotTimes
	: "." "*"						::DotTimesC
	;

TypeDeclaration
	: [ AST_Modifiers ] UnmodifiedTypeDeclaration		::ModTypeDecl
	| ";"							::EmptyTDecl
	;

UnmodifiedTypeDeclaration
	: UnmodifiedClassDeclaration
	| UnmodifiedInterfaceDeclaration
	;

/*
 * Declaration syntax follows.
 */

AST_Modifiers
	: ( Modifier )+
	;

Modifier
	: ABSTRACT						::ModAbstract
	| FINAL							::ModFinal
	| PUBLIC						::ModPublic
	| PROTECTED						::ModProtected
	| PRIVATE						::ModPrivate
	| STATIC						::ModStatic
	| TRANSIENT						::ModTransient
	| VOLATILE						::ModVolatile
	| NATIVE						::ModNative
	| SYNCHRONIZED					::ModSynchronized
	;

UnmodifiedClassDeclaration
	: "class" QName [ ExtendsClause ] [ ImplementsClause ] ClassBody
								::UmodClassDecl
	;

ExtendsClause
	: "extends" AST_QualifiedName				::ExtClause
	;

ImplementsClause
	: "implements" AST_TypeNameList				::ImplClause
	;

ClassBody
	: "{" [ AST_FieldDecl ] "}"				::ClsBody
	;

// NOTE: It looks like FieldDeclaration in old grammar is equivalent to
// ClassBodyDeclaration
AST_FieldDecl
	: ( ClassBodyDeclaration )+
	;

ClassBodyDeclaration
	: LOOKAHEAD(2)
	  Initializer
	|
	  LOOKAHEAD( [ AST_Modifiers() ] "class" )
	  NestedClassDeclaration
	| LOOKAHEAD( [ AST_Modifiers() ] "interface" )
	  NestedInterfaceDeclaration
	| LOOKAHEAD( [ "public" | "protected" | "private" ] AST_QualifiedName() "(" )
	  ConstructorDeclaration
	| LOOKAHEAD( MethodDeclarationLookahead() )
	  MethodDeclaration
	| FieldDeclaration
	;

NestedClassDeclaration
	: [ AST_Modifiers ] UnmodifiedClassDeclaration		::NClassDecl
	;

// This production is to determine lookahead only.
MethodDeclarationLookahead
	: [ AST_Modifiers ] AST_TypeName QName "("		::MDeclLA
	;

// never used -- will leave it as a comment for now
//InterfaceDeclaration
//	: [ AST_Modifiers ] UnmodifiedInterfaceDeclaration	::InterDecl
//	;

NestedInterfaceDeclaration
	: [ AST_Modifiers ] UnmodifiedInterfaceDeclaration	::NInterDecl
	;

UnmodifiedInterfaceDeclaration
	: "interface" QName [ IntExtClause ]
		"{" [ InterfaceMemberDeclarations ] "}"		::UmInterDecl
	;

IntExtClause
	: "extends" AST_TypeNameList				::IntExtClauseC
	;

InterfaceMemberDeclarations
	: (InterfaceMemberDeclaration)+
	;

InterfaceMemberDeclaration
	: LOOKAHEAD( [ AST_Modifiers() ] "class" )
	  NestedClassDeclaration				::NCDecl
	| LOOKAHEAD( [ AST_Modifiers() ] "interface" )
	  NestedInterfaceDeclaration				::NIDecl
	| LOOKAHEAD( MethodDeclarationLookahead() )
	  MethodDeclaration					::MDecl
	| FieldDeclaration					::FDecl
	;

// This matches FieldVariableDeclaration in the old grammar.
FieldDeclaration
	: [ AST_Modifiers ] AST_TypeName AST_VarDecl ";"	::FldVarDec
	;

AST_VarDecl
	: VariableDeclarator ( "," VariableDeclarator )*
	;

VariableDeclarator
	: VariableDeclaratorId [ VarAssign ]			::VarDecl
	;

VarAssign
	: "=" AST_VarInit					::VarAssignC
	;

VariableDeclaratorId
	: QName [ Dims ]					::DecNameDim
	;

Dims
	: ( LOOKAHEAD(2) Dim )+
	;

Dim
	: "[" "]"						::Dim2
	;

AST_VarInit
	: "{" [ AST_ArrayInit ] [ "," ] "}"			::ArrInit
	| Expression						::VarInitExpr
	;

// This used to be called ArrayInitializer.
AST_ArrayInit
	: ArrayInitializer  ::ArrayInit
	;

ArrayInitializer
	: AST_VarInit ( LOOKAHEAD(2) "," AST_VarInit )*
	;

MethodDeclaration
	: [ AST_Modifiers ] AST_TypeName MethodDeclarator
	  [ ThrowsClause] MethodDeclSuffix			::MethodDcl
	;

ThrowsClause
	: "throws" AST_TypeNameList				::ThrowsClauseC
	;

MethodDeclSuffix
	: Block							::MDSBlock
	| ";"							::MDSEmpty
	;

MethodDeclarator
	: QName "(" [ AST_ParList ] ")" [ Dims ]		::MthDector
	;

AST_ParList
	: FormalParameter ( "," FormalParameter )*
	;

FormalParameter
	: [ "final" ] AST_TypeName VariableDeclaratorId		::FormParDecl
	;

ConstructorDeclaration
	: [ AST_Modifiers ] QName "(" [ AST_ParList ] ")"
		[ ThrowsClause ] "{"
		[ LOOKAHEAD(ExplicitConstructorInvocation())
			ExplicitConstructorInvocation ]
		[ AST_Stmt ] "}"				::ConDecl
	;

ExplicitConstructorInvocation
	: LOOKAHEAD("this" Arguments() ";")
	  "this" Arguments ";"					::ConThis
	| [ LOOKAHEAD(2) PrimDot ] "super" Arguments ";"
								::ConSuper
	;

PrimDot
	: PrimaryExpression "."					::PrimDotC
	;

Initializer
	: [ "static" ] Block					::Init
	;


/*
 * Type, name and expression syntax follows.
 */

AST_TypeName
	: PrimitiveType [ LOOKAHEAD(2) Dims ]			::PrimType
	| AST_QualifiedName [ LOOKAHEAD(2) Dims ]		::QNameType
	;

PrimitiveType
	: "boolean"						::BoolTyp
	| "char"						::CharTyp
	| "byte"						::ByteTyp
	| "short"						::ShortTyp
	| "int"							::IntTyp
	| "long"						::LongTyp
	| "float"						::FloatTyp
	| "double"						::DoubleTyp
	| "void"						::VoidTyp
	;

// ResultType
// 	: "void"						::VoidResult
// 	| AST_TypeName
// 	;

// A lookahead of 2 is required below since "AST_QualifiedName" can be followed
// by a ".*" when used in the context of an "ImportDeclaration".
AST_QualifiedName 
	: QName ( LOOKAHEAD(2) "." QName )*
	;

QName
	: IDENTIFIER						::NameId
	;

// New grammar called this 'NameList' which is probabaly a better name.
AST_TypeNameList
	: TName ( "," TName )*
	;

// This was added, rather than having AST_QualifiedName appear directly
// in AST_TypeNameList, so ast.b can extend TName.
TName
	: AST_TypeName						::TNClass
	;

/*
 * Expression syntax follows.
 */

// This expansion has been written this way instead of:
//   Assignment() | ConditionalExpression()
// for performance reasons.
// However, it is a weakening of the grammar for it allows the LHS of
// assignments to be any conditional expression whereas it can only be
// a primary expression.  Consider adding a semantic predicate to work
// around this.
Expression
	: ConditionalExpression
	| ConditionalExpression AssignmentOperator Expression	::AsgExpr
	;

AST_Exp
	: Expression
	;

AssignmentOperator
	: "="							::Assign
	| "*="							::AssnTimes
	| "/="							::AssnDiv
	| "%="							::AssnMod
	| "+="							::AssnPlus
	| "-="							::AssnMinus
	| "<<="							::AssnShL
	| ">>="							::AssnShR
	| ">>>="						::AssnShRR
	| "&="							::AssnAnd
	| "^="							::AssnXor
	| "|="							::AssnOr
	;

ConditionalExpression
	: ConditionalOrExpression
	| ConditionalOrExpression "?" Expression ":" ConditionalExpression
								::QuestExpr
	;

ConditionalOrExpression
	: ConditionalAndExpression
	| ConditionalAndExpression MoreCondOrExpr		::CondOrExpr
	;

MoreCondOrExpr
	: ( LOOKAHEAD(2) COEBody )+
	;

COEBody
	: "||" ConditionalAndExpression				::COEBod
	;

ConditionalAndExpression
	: InclusiveOrExpression
	| InclusiveOrExpression MoreCondAndExpr			::CondAndExpr
	;

MoreCondAndExpr
	: ( LOOKAHEAD(2) CAEBody )+
	;

CAEBody
	: "&&" InclusiveOrExpression				::CAEBod
	;

InclusiveOrExpression
	: ExclusiveOrExpression
	| ExclusiveOrExpression MoreInclOrExpr			::InclOrExpr
	;

MoreInclOrExpr
	: ( LOOKAHEAD(2) IOEBody )+
	;

IOEBody
	: "|" ExclusiveOrExpression				::IOEBod
	;

ExclusiveOrExpression
	: AndExpression
	| AndExpression MoreExclOrExpr				::ExclOrExpr
	;

MoreExclOrExpr
	: ( LOOKAHEAD(2) EOEBody )+
	;

EOEBody
	: "^" AndExpression					::EOEBod
	;

AndExpression
	: EqualityExpression
	| EqualityExpression MoreAndExpr			::AndExpr
	;

MoreAndExpr
	: ( LOOKAHEAD(2) AEBody )+
	;

AEBody
	: "&" EqualityExpression				::AEBod
	;

EqualityExpression
	: InstanceOfExpression
	| InstanceOfExpression MoreEqExpr			::EqExpr
	;

MoreEqExpr
	: ( LOOKAHEAD(2) EEBody )+
	;

EEBody
	: EqExprChoices InstanceOfExpression			::EEBodyC
	;

EqExprChoices
	: "=="							::Eq
	| "!="							::Neq
	;

InstanceOfExpression
	: RelationalExpression
	| RelationalExpression "instanceof" AST_TypeName	::IoExpr
	;

RelationalExpression
	: ShiftExpression
	| ShiftExpression MoreRelExpr				::RelExpr
	;

MoreRelExpr
	: ( LOOKAHEAD(2) REBody )+
	;

REBody
	: RelExprChoices ShiftExpression			::REBod
	;

RelExprChoices
	: "<"							::LtOp
	| ">"							::GtOp
	| "<="							::LeOp
	| ">="							::GeOp
	;

ShiftExpression
	: AdditiveExpression
	| AdditiveExpression MoreShiftExpr			::ShiftExpr
	;

MoreShiftExpr
	: ( LOOKAHEAD(2) SEBody )+
	;

SEBody
	: ShiftExprChoices AdditiveExpression			::SEBodyC
	;

ShiftExprChoices
	: "<<"							::LShift
	| ">>"							::RShift
	| ">>>"							::RRShift
	;

AdditiveExpression
	: MultiplicativeExpression
	| MultiplicativeExpression MoreAddExpr			::AddExpr
	;

MoreAddExpr
	: ( LOOKAHEAD(2) AdEBody )+
	;

AdEBody
	: AddExprChoices MultiplicativeExpression		::AdEBod
	;

AddExprChoices
	: "+"							::Plus
	| "-"							::Minus
	;

MultiplicativeExpression
	: UnaryExpression
	| UnaryExpression MoreMultExpr				::MultExpr
	;

MoreMultExpr
	: ( LOOKAHEAD(2) MEBody )+
	;

MEBody
	: MultExprChoices UnaryExpression			::MEBod
	;

MultExprChoices
	: "*"							::Mult
	| "/"							::Div
	| "%"							::Mod
	;

UnaryExpression
	: "+" UnaryExpression					::PlusUE
	| "-" UnaryExpression					::MinusUE
	| PreIncrementExpression
	| PreDecrementExpression
	| UnaryExpressionNotPlusMinus
	;

PreIncrementExpression
	: "++" PrimaryExpression				::PIncExpr
	;

PreDecrementExpression
	: "--" PrimaryExpression				::PDecExpr
	;

UnaryExpressionNotPlusMinus
	: "~" UnaryExpression  					::TildeUE
	| "!" UnaryExpression					::NotUE
	| LOOKAHEAD( CastLookahead() )
	  CastExpression
	| PostfixExpression
	;

// This production is to determine lookahead only. The LOOKAHEAD specifications
// below are not used, but they are there just to indicate that we know about
// this.
CastLookahead
	:  LOOKAHEAD(2)
	  "(" PrimitiveType					::Cla1
	| LOOKAHEAD("(" AST_QualifiedName() "[")
	  "(" AST_QualifiedName "[" "]"				::Cla2
	|  "(" AST_QualifiedName ")" CastLookaheadChoices	::Cla3
	;

CastLookaheadChoices
	: "~"							::TildeLA
	| "!"							::BangLA
	| "("							::OpParenLA
	| IDENTIFIER						::IdLA
	| "this"						::ThisLA
	| "super"						::SuperLA
	| "new"							::NewLA
	| Literal						::LitLA
	;

PostfixExpression
	: PrimaryExpression
	| PrimaryExpression PEPostIncDec			::PEIncDec
	;

PEPostIncDec
	: "++"							::PlusPlus2
	| "--"							::MinusMinus2
	;

CastExpression
	:  LOOKAHEAD("(" PrimitiveType())
	  "(" AST_TypeName ")" UnaryExpression			::CastExpr1
	| "(" AST_TypeName ")" UnaryExpressionNotPlusMinus	::CastExpr2
	;

PrimaryExpression
	: PrimaryPrefix
	| PrimaryPrefix Suffixes				::PrimExpr
	;

Suffixes
	: ( LOOKAHEAD(2) PrimarySuffix )+
	;

PrimaryPrefix
	: Literal
	| "this"						::ThisPre
	| "super" "." QName					::SuperPre
	| "(" Expression ")"					::ExprPre
	| AllocationExpression
	| LOOKAHEAD( AST_TypeName() "." "class" )
	  AST_TypeName "." "class"				::RTPre
	| AST_QualifiedName					::PPQualName
	;

PrimarySuffix
	: LOOKAHEAD(2)
	  "." "this"						::ThisSuf
	| LOOKAHEAD(2)
	  "." AllocationExpression				::AllocSuf
	| "[" Expression "]"					::ExprSuf
	| "." QName						::QNameSuf
	| Arguments						::MthCall
	;

Literal
	: INTEGER_LITERAL					::IntLit
	| FLOATING_POINT_LITERAL				::FPLit
	| CHARACTER_LITERAL					::CharLit
	| STRING_LITERAL					::StrLit
	| BooleanLiteral
	| NullLiteral
	;

BooleanLiteral
	: "true"						::True
	| "false"						::False
	;

NullLiteral
	: "null"						::Null
	;

Arguments
	:  "(" [ AST_ArgList ] ")"				::Args
	;

AST_ArgList
	:  Expression ( "," Expression )*
	;

AllocationExpression
	: LOOKAHEAD(2)
	  "new" PrimitiveType ArrayDimsAndInits			::PrimAllocExpr
	| "new" AST_QualifiedName AllocExprChoices		::ObjAllocExpr
	;

AllocExprChoices
	: ArrayDimsAndInits
	| Arguments [ ClassBody ]				::AnonClass
	;

/*
 * The third LOOKAHEAD specification below is to parse to PrimarySuffix
 * if there is an expression between the "[...]".
 */
/*****
ArrayDimsAndInits
	: LOOKAHEAD(2)
	  ( LOOKAHEAD(2) "[" Expression "]" )+ ( LOOKAHEAD(2) "[" "]" )*
	| Dims AST_ArrayInit
	;
*****/
ArrayDimsAndInits
	: LOOKAHEAD(2)
	  ExprDims [ LOOKAHEAD(2) Dims ]			::ArrDim1
	| Dims AST_ArrayInit					::ArrDim2
	;

ExprDims
	: ( LOOKAHEAD(2) ExDimBody )+
	;

ExDimBody
	:"[" Expression "]"					::ExDimBod
	;

/*
 * Statement syntax follows.
 */

Statement
	: LOOKAHEAD(QName() ":")
	  LabeledStatement
	| Block
	| EmptyStatement
	| SwitchStatement
	| IfStatement
	| WhileStatement
	| DoStatement
	| ForStatement
	| BreakStatement
	| ContinueStatement
	| ReturnStatement
	| ThrowStatement
	| SynchronizedStatement
	| TryStatement
	| AST_Exp ";"						::ExprStmt
	;

LabeledStatement
	: QName ":" Statement					::LabeledStmt
	;

Block
	:  "{" [ AST_Stmt ] "}"					::BlockC
	;

AST_Stmt
	: ( BlockStatement )+
	;

BlockStatement
	: LOOKAHEAD([ "final" ] AST_TypeName() <IDENTIFIER>)
	  LocalVariableDeclaration ";"				::BlockStmt
	| Statement
	| UnmodifiedClassDeclaration				::BlkClassDcl
	| UnmodifiedInterfaceDeclaration			::BlkInterDcl
	;

LocalVariableDeclaration
	:  [ "final" ] AST_TypeName AST_VarDecl			::LocalVarDecl
	;

EmptyStatement
	:  ";"							::Empty
	;

// The last expansion of this production accepts more than the legal
// Java expansions for StatementExpression.  This expansion does not
// use PostfixExpression for performance reasons.
StatementExpression
	: PreIncrementExpression				::PIExpr
	| PreDecrementExpression				::PDExpr
	| PrimaryExpression [ StmtExprChoices ]			::PEStmtExpr
	;

StmtExprChoices
	: "++"							::PlusPlus
	| "--"							::MinusMinus
	| AssignmentOperator Expression				::AssnExpr
	;

SwitchStatement
	:  "switch" "(" Expression ")" "{" [ AST_SwitchEntry ] "}"
								::SwitchStmt
	;

AST_SwitchEntry
	: ( SwitchEntryBody )+
	;

SwitchEntryBody
	: SwitchLabel [ AST_Stmt ]				::SEBod
	;

SwitchLabel
	: "case" Expression ":"					::CaseLabel
	| "default" ":"						::DefLabel
	;

// The disambiguating algorithm of JavaCC automatically binds dangling
// else's to the innermost if statement.  The LOOKAHEAD specification
// is to tell JavaCC that we know what we are doing.
IfStatement
	: "if" "(" Expression ")" Statement [ LOOKAHEAD(1) ElseClause ]
								::IfStmt
	;

ElseClause
	: "else" Statement					::ElseClauseC
	;

WhileStatement
	:  "while" "(" Expression ")" Statement			::WhileStm
	;

DoStatement
	:  "do" Statement "while" "(" Expression ")" ";"	::DoWhileStm
	;

ForStatement
	:  "for" "(" [ ForInit ] ";" [ Expression ] ";" [ ForUpdate ] ")"
		Statement					::ForStmt
	;

ForInit
	: LOOKAHEAD( [ "final" ] AST_TypeName() <IDENTIFIER> )
	  LocalVariableDeclaration
	| StatementExpressionList				::FIExprList
	;

StatementExpressionList
	:  StatementExpression ( "," StatementExpression )*
	;

ForUpdate
	: StatementExpressionList				::StmExprList
	;

BreakStatement
	:  "break" [ QName ] ";"				::BreakStm
	;

ContinueStatement
	:  "continue" [ QName ] ";"				::ContinueStm
	;

ReturnStatement
	:  "return" [ Expression ] ";"				::ReturnStm
	;

ThrowStatement
	:  "throw" Expression ";"				::ThrowStm
	;

SynchronizedStatement
	:  "synchronized" "(" Expression ")" Block		::SyncStmt
	;

// Semantic check required here to make sure that at least one
// finally/catch is present.
TryStatement
	: "try" Block [ AST_Catches ] [ Finally ]		::TryStmt
	;

Finally
	: "finally" Block					::FinallyC
	;

AST_Catches
	: ( Catch )+
	;

Catch
	: "catch" "(" FormalParameter ")" Block			::CatchStmt
	;
