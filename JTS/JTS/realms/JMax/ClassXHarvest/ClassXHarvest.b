// ClassHarvest grammar
// JEDI project
// Computer Sciences Department
// University of Texas at Austin
// Last update: November 7, 2002


// previously defined productions that are RHS referenced

require ClassBody ;
require ImplementsClause ;
require QName ;

// productions that are being extended

require UnmodifiedTypeExtension ;

// the grammar extension

UnmodifiedTypeExtension
        : "class" QName [ ImplementsClause ] ClassBody :: UmodClassExt
	;
