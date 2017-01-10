TOKEN : {
	< SOURCE: "SoUrCe" >
	| < ROOT: "RooT"   >
}


// previously-defined productions that are right-side referenced

// require AST_QualifiedName ;

// productions that are being extended

// require TypeDeclaration ;

// grammar extension

TypeDeclaration
	: SOURCE [ROOT] AST_QualifiedName STRING_LITERAL ";"   :: SourceDecl
	;
