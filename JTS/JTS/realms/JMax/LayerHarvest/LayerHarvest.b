// LayerDeclHarvest grammar
// JEDI project
// Department of Computer Sciences
// University of Texas at Austin
// Last update: November 7, 2202

// adds "layer" as another kind of package

TOKEN : {
	< LAYER: "layer" >
}



// Previously known as aspects instead of layer

require AST_QualifiedName ;

// productions that are being extended

require PackageDeclaration ;

// grammar extension, notice that AspectStm is probably outdated

PackageDeclaration
	: LAYER AST_QualifiedName ";"	::AspectStm
	;
