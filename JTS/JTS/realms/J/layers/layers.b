TOKEN :
{
	<TYPEEQN: "typeEquation">
|
	<LAYER: "layer">
|
	<REALM: "realm">
|
	<IMPORTS: "imports">
|
	<RELATIVE: "relative">
}

//**********************************************************************
// The next two rules specify the type equation grammar.
//**********************************************************************

AST_Program
	: TE_Generator
	;

TE_Generator
	: TYPEEQN AST_QualifiedName AST_QualifiedName "=" AST_Exp ";"
								:: TEGen
	;



//**********************************************************************
// The composer must be able to read layer files, so the grammar below
// represents that used in layer files.
//**********************************************************************

AST_Program
	: LOOKAHEAD( [ LayerModifiers() ] <LAYER> )
	  Layer_Decl						::LDecl
	;

LayerHeader
	: [ LayerModifiers ] LAYER QName "(" [ AST_ParList ] ")"
								::LyrHeader
	;

Layer_Decl
	: [ LayerModifiers ] LAYER QName "(" [ AST_ParList ] ")"
	  REALM QName [ SuperLayer ] [ LayerImports ]
	  ClassBody						::LayerDecl
	;

LayerModifiers
	: ( LayerModifier )+
	;

LayerModifier
	: Modifier						::NonRelMod
	| RELATIVE						::ModRelative
	;

SuperLayer
	: EXTENDS AST_QualifiedName				::SupLayer
	;

LayerImports
	: IMPORTS ImportList					::LyrImports
	;

ImportList
	: ImportPack ( "," ImportPack )*
	;

ImportPack
	: AST_QualifiedName [ DotTimes ]			::ImportPak
	;
