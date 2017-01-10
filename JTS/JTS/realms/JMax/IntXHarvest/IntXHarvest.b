// IntHarvest grammar
// JEDI project
// Computer Sciences Department
// University of Texas at Austin
// Last update: November 7, 2002


// previously-defined productions appearing on RHS

require IntExtClause ;
require InterfaceMemberDeclarations ;
require QName ;

// productions that are being extended

require UnmodifiedTypeExtension ;

// grammar extension

UnmodifiedTypeExtension
	: "interface" QName [ IntExtClause ]
		"{" [ InterfaceMemberDeclarations ] "}" ::UmodIntExt
	;
