// BaseHarvest grammar
// JEDI project
// Computer Sciences Department
// University of Texas at Austin
// Last update: November 7, 2002


TOKEN : {
    < OVERRIDES: "overrides" >
|   < REFINES:   "refines" >
}



// previously defined productions appearing on the left-hand sides

require AST_ParList ;
require AST_Stmt ;
require AST_TypeNameList ;
require Arguments ;
require QName ;

// productions that are being extended.

require CastLookaheadChoices ;
require Modifier ;
require PrimaryPrefix ;
require ExplicitConstructorInvocation ;
require UnmodifiedTypeDeclaration ;
require ClassBodyDeclaration ;

// grammar extension 

CastLookaheadChoices
	: "Super"						:: CapSuperLA
	;

Modifier :
	  NEW        :: ModNew
	| OVERRIDES  :: ModOverrides
	;

PrimaryPrefix
	: "Super" "(" [ AST_TypeNameList ] ")" "." QName :: BasePre
	;

ExplicitConstructorInvocation
	: LOOKAHEAD(3)
	  "Super" "(" [ AST_TypeNameList ] ")" Arguments ";" :: ConSSuper
	;

// other layers will define extensions to the UnmodifiedTypeExtension
// nonterminal

UnmodifiedTypeDeclaration
	: REFINES UnmodifiedTypeExtension :: Ute
	;

UnmodifiedTypeExtension 
	: REFINES         ::UTEError //added to make bali2jak happy
	;

// RefCons defines a refinement of a constructor 

ClassBodyDeclaration
	: ConstructorRefinement
	;

ConstructorRefinement
	: REFINES QName "(" [ AST_ParList ] ")" "{" [  AST_Stmt ] "}" 
		:: RefCons
	;
