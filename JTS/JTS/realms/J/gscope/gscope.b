TOKEN : {
	<ALIAS: "alias">
|	<AUGMENT: "augment">
|	<ENVIRONMENT: "environment">
|	<PARENT: "parent">
}

// previously-defined productions referenced on the RHS

require AST_Exp ;
require QName ;

// productions that are being extended

require FieldDeclaration ;
require Statement ;

// the grammar extension

FieldDeclaration
        : ENVIRONMENT [ DeclNameList ] ";"			::Env
        ;

Statement
        : ALIAS "(" QName COMMA AST_Exp ")" ";"			::Alias
        | LOOKAHEAD(2) ENVIRONMENT AUGMENT AST_Exp ";"		::Augment
        | ENVIRONMENT PARENT AST_Exp ";"			::SetParent
        ;

DeclNameList
        : QName ( COMMA QName )*
        ;
