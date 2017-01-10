TOKEN : {
	< REFINES: "refines" >
}

UnmodifiedTypeDeclaration
	: REFINES UnmodifiedTypeExtension :: Ute
	;

// other layers will define extensions to the UnmodifiedTypeExtension
// nonterminal
