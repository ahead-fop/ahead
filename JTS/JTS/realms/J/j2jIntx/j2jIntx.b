
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
