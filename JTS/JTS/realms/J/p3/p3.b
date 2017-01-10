TOKEN :
{
	<CONTAINER: "container">
|
	<CURSOR: "cursor">
|
	<DELETION: "deletion">
|
	<UPDATE: "update">
|
	<USING: "using">
|
	<WHERE: "where">
|
	<ORDERBY: "orderby">
|
	<P3DEBUG: "P3debug">
|
	<DEM: "Dem">
|
	<JBEANS: "JBeans">
|
	<PROFILE: "Profile">
|
	<HTML: "Html">
|
	<P3ATTR: "P3Attributes">
}


TypeDeclaration
        : LOOKAHEAD( [ AST_Modifiers() ] (<CONTAINER> | <CURSOR>))
	  P3ClassDeclarations
        ;

P3ClassDeclarations
        : LOOKAHEAD( [ AST_Modifiers() ] <CONTAINER> "<")
	  [ AST_Modifiers ] CONTAINER "<" QName ">" QName ";"	:: P3AContDecl
        | LOOKAHEAD( [ AST_Modifiers() ] <CURSOR> "<")
	  [ AST_Modifiers ] CURSOR "<" QName ">" QName ";"	:: P3ACursDecl
        | LOOKAHEAD( [ AST_Modifiers() ] <CONTAINER>)
	  [ AST_Modifiers ] CONTAINER QName ExtendsClause [ImplementsClause]
             USING TypeEquation ";"				:: P3CContDecl
        | [ AST_Modifiers ] CURSOR QName "(" AST_ParList ")" [ImplementsClause]
             [DELETION] [Upd] [Where] [Orderby] ";"		:: P3CCursDecl
        ;

TypeEquation
        : AST_Exp						:: Teqn
        ;

Upd
        : LOOKAHEAD(2)
	  UPDATE "*"						:: UpdateAll
        | UPDATE QNameList					:: UpdateSome
        ;

QNameList
        : QName ( "," QName )*
        ;
Where
        : WHERE AST_Exp						:: WhereClause
        ;

Orderby
        : ORDERBY [ MINUS ] [ QName ]				:: OrderbyClause
        ;

Modifier
        : P3DEBUG						:: ModP3debug
	| DEM							:: ModDem
	| JBEANS						:: ModJBeans
	| PROFILE						:: ModProfile
	| HTML							:: ModHtml
	| P3ATTR						:: ModP3Attributes
        ;
