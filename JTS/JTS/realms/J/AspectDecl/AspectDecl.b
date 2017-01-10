// adds "layer" as another kind of package

TOKEN : {
	< LAYER: "layer" >
}

// previously-defined productions appearing on RHS

require AST_QualifiedName ;

// productions that are being extended

require PackageDeclaration ;

// grammar extension

PackageDeclaration
	: LAYER AST_QualifiedName ";"	::AspectStm
	;
