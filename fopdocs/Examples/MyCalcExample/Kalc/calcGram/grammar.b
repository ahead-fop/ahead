// grammar.b : simple calculator

// Simple tokens specified in simple format

"+" PLUS

// Regular expression tokens specified in JavaCC format

TOKEN :
{
	<INTEGER: ["1"-"9"] (["0"-"9"])* >
}

Actions : (Action)+
;

Action 	: INTEGER 	:: Push
	| PLUS 		:: Plus
	;


