// asgn.b  - assignment statements

"="			EQUALS

Action	: IDENTIFIER EQUALS Sum END	:: Assign
	;

element	: IDENTIFIER			:: Ident
	;
