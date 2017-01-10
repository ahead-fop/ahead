// previously-defined productions appearing on RHS

require QName ;

// productions that are being extended

require TypeDeclaration ;

// grammar extension

TypeDeclaration
	: "Local_Id" QNameList ";"	:: LocalIdProd
	;

QNameList
	: QName ( "," QName)*
	;
