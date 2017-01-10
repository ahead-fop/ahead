// SmHarvest grammar
// JEDI project
// Computer Sciences Department
// University of Texas at Austin
// Last update: November 7, 2002

// Note: This grammar is the concatenation of smempty and mixinSm
//       as defined for the mixin and unmixin tool

// --- smempty

// grammar defines extensions to state machines


// previously-defined productions appearing on right-hand side

require ImplementsClause ;
require QName ;
require SmClassBody ;

// productions that are being extended with new rules

require UnmodifiedTypeExtension ;

// grammar extension

UnmodifiedTypeExtension
        : STATE_MACHINE QName [ ImplementsClause ] SmClassBody :: UmodSmExt
	;

