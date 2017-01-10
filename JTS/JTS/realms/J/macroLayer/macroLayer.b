TOKEN :
{
	<MACRO: "macro">
|
	<LOCAL: "local">
|
	<MPARM_ESCAPE: "$mparm">
}

TypeDeclaration
	: MacroInst					::MacroCls
	;

UnmodifiedTypeDeclaration
	: MacroDecl
	;

MacroDecl
	: MACRO QName "(" [ AST_ParList ] ")" 
	    [ LocalIdents ] JakartaSST			::MacroDcl
	;

LocalIdents
	: LOCAL DeclNameList2				::LocalIds
	;

DeclNameList2
	: QName ( "," QName )*
	;

MacroInst
	: "#" AST_QualifiedName "(" [ AST_ArgList] ")"	::MacroCall
	;

ClassBodyDeclaration
	: MacroInst					::MacroMth
	;

Expression
	: MacroInst					::MacroExp
	;

PrimaryExpression
	: MPARM_ESCAPE "(" AST_Exp ")"			::MParm
	;
