// grammar defines extensions to state machines


// previously-defined productions appearing on right-hand side

// require ImplementsClause ;
// require QName ;
// require SmClassBody ;

// productions that are being extended with new rules

// require UnmodifiedTypeExtension ;

// grammar extension

UnmodifiedTypeExtension
        : STATE_MACHINE QName [ ImplementsClause ] SmClassBody :: UmodSmExt
	;
