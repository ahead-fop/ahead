TOKEN : {
	<AI_BEGIN: "ai{">
|	<AI_END: "}ai">
|	<AI_ESCAPE: "$ai">
|	<CASE_BEGIN: "case{">
|	<CASE_END: "}case">
|	<CASE_ESCAPE: "$case">
|	<CAT_BEGIN: "cat{">
|	<CAT_END: "}cat">
|	<CAT_ESCAPE: "$cat">
|	<CLS_BEGIN: "cls{">
|	<CLS_END: "}cls">
|	<CLS_ESCAPE: "$cls">
|	<ESTM_BEGIN: "estm{">
|	<ESTM_END: "}estm">
|	<ESTM_ESCAPE: "$estm">
|	<EXP_BEGIN: "exp{">
|	<EXP_END: "}exp">
|	<EXP_ESCAPE: "$exp">
|	<ID_BEGIN: "id{">
|	<ID_END: "}id">
|	<ID_ESCAPE: "$id">
|       <NAMEID_ESCAPE: "$name">
|	<IMP_BEGIN: "imp{">
|	<IMP_END: "}imp">
|	<IMP_ESCAPE: "$imp">
|	<MOD_BEGIN: "mod{">
|	<MOD_END: "}mod">
|	<MOD_ESCAPE: "$mod">
|	<MTH_BEGIN: "mth{">
|	<MTH_END: "}mth">
|	<MTH_ESCAPE: "$mth">
|	<PLST_BEGIN: "plst{">
|	<PLST_END: "}plst">
|	<PLST_ESCAPE: "$plst">
|	<PRG_BEGIN: "prg{">
|	<PRG_END: "}prg">
|	<STM_BEGIN: "stm{">
|	<STM_END: "}stm">
|	<STM_ESCAPE: "$stm">
|	<STR_ESCAPE: "$str">
|	<TLST_BEGIN: "tlst{">
|	<TLST_END: "}tlst">
|	<TLST_ESCAPE: "$tlst">
|	<TYP_BEGIN: "typ{">
|	<TYP_END: "}typ">
|	<TYP_ESCAPE: "$typ">
|	<VI_BEGIN: "vi{">
|	<VI_END: "}vi">
|	<VI_ESCAPE: "$vi">
|	<VLST_BEGIN: "vlst{">
|	<VLST_END: "}vlst">
|	<VLST_ESCAPE: "$vlst">
|	<XLST_BEGIN: "xlst{">
|	<XLST_END: "}xlst">
|	<XLST_ESCAPE: "$xlst">
}


// previously defined productions appearing on the
// left-hand side of productions in this file

// require AST_ArgList ;
// require AST_ArrayInit ;
// require AST_Catches ;
// require AST_Class ;
// require AST_Exp ;
// require AST_ExpStmt ;
// require AST_FieldDecl ;
// require AST_Imports ;
// require AST_Modifiers ;
// require AST_ParList ;
// require AST_Program ;
// require AST_QualifiedName ;
// require AST_Stmt ;
// require AST_SwitchEntry ;
// require AST_TypeNameList ;
// require AST_VarDecl ;
// require LocalVariableDeclaration ;

// productions that are extended

// require AST_TypeName ;
// require AST_VarInit ;
// require BlockStatement ;
// require Catch ;
// require ClassBodyDeclaration ;
// require ConditionalExpression ;
// require FormalParameter ;
// require ImportDeclaration ;
// require Modifier ;
// require PrimaryPrefix ;
// require QName ;
// require Statement ;
// require StatementExpression ;
// require SwitchEntryBody ;
// require TName ;
// require TypeDeclaration ;
// require VariableDeclarator ;

// grammar extension

AST_TypeName
	: TYP_ESCAPE "(" AST_Exp ")"			::TypEscape
	;

TName
	: TLST_ESCAPE "(" AST_Exp ")"			::TlstEscape
	;

JakartaSST
	: EXP_BEGIN  AST_Exp EXP_END			::AST_ExpC
	| STM_BEGIN [ AST_Stmt ] STM_END		::AST_StmtC
	| CASE_BEGIN AST_SwitchEntry CASE_END		::AST_SwitchEntryC
	| MTH_BEGIN [ AST_FieldDecl ] MTH_END		::AST_FieldDeclC
	| CLS_BEGIN [ AST_Class ] CLS_END		::AST_ClassC
	| PRG_BEGIN  AST_Program PRG_END		::AST_ProgramC
	| TYP_BEGIN  AST_TypeName TYP_END		::AST_TypeNameC
	| ID_BEGIN   AST_QualifiedName ID_END		::AST_QualifiedNameC
	| PLST_BEGIN [ AST_ParList ] PLST_END		::AST_ParListC
	| XLST_BEGIN [ AST_ArgList ] XLST_END		::AST_ArgListC
	| TLST_BEGIN AST_TypeNameList TLST_END		::AST_TypeNameListC
	| IMP_BEGIN [ AST_Imports ] IMP_END		::AST_ImportsC
	| MOD_BEGIN [ AST_Modifiers ] MOD_END		::AST_ModifiersC
	| VLST_BEGIN AST_VarDecl VLST_END		::AST_VarDeclC
	| VI_BEGIN   AST_VarInit VI_END			::AST_VarInitC
	| AI_BEGIN   AST_ArrayInit AI_END		::AST_ArrayInitC
	| ESTM_BEGIN AST_ExpStmt ESTM_END		::AST_ExpStmtC
	| CAT_BEGIN [ AST_Catches ] CAT_END		::AST_CatchesC
	;

SwitchEntryBody
	: CASE_ESCAPE "(" AST_Exp ")"			::SwEscape
	;

TypeDeclaration
	: CLS_ESCAPE "(" AST_Exp ")"			::ClsEscape
	| IDENTIFIER					::ClsIscape
	;

ImportDeclaration
	: IMP_ESCAPE "(" AST_Exp ")" ";"		::ImpEscape
	;

QName
	: ID_ESCAPE "(" AST_Exp ")"			::IdEscape
	| NAMEID_ESCAPE "(" AST_Exp ")"			::NameIdEscape
	;
 
Modifier
	: MOD_ESCAPE "(" AST_Exp ")"			::ModEscape
	;

ClassBodyDeclaration
	: LOOKAHEAD(2) IDENTIFIER ";"			::MthIscape
	| MTH_ESCAPE "(" AST_Exp ")"			::MthEscape
	;

VariableDeclarator
	: VLST_ESCAPE "(" AST_Exp ")"			::VlstEscape
	; 

BlockStatement
	: LOOKAHEAD([ "final" ] AST_TypeName() <VLST_ESCAPE>)
	  LocalVariableDeclaration ";"			::VEStmt
	;

AST_VarInit
	: VI_ESCAPE "(" AST_Exp ")"			::ViEscape
	;

AST_ArrayInit
	: AI_ESCAPE "(" AST_Exp ")"			::AiEscape
	;

FormalParameter
	: LOOKAHEAD(<IDENTIFIER> ( "," | ")" ) )
	  IDENTIFIER					::PlstIscape
	| PLST_ESCAPE "(" AST_Exp ")"			::PlstEscape
	;

Statement
	:STM_ESCAPE "(" AST_Exp ")" ";"			::StmEscape
	;

StatementExpression
	: ESTM_ESCAPE "(" AST_Exp ")"			::EstmEscape
	;

Catch
	: CAT_ESCAPE "(" AST_Exp ")"			::CatEscape
	;

// Used to be PrimaryExpression but I think PrimaryPrefix is
// more correct.
PrimaryPrefix
	: StringEscape
	| EXP_ESCAPE "(" AST_Exp ")"			::ExpEscape
	;

StringEscape
	: STR_ESCAPE "(" AST_Exp ")"			::StrEscape
	;

ConditionalExpression
	: XLST_ESCAPE "(" AST_Exp ")"			::XlstEscape
	| JakartaSST
	;

