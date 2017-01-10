// calc.b : simple calculator

// Simple tokens specified in simple format
"print"			PRINT
"+"			PLUS
"-"			MINUS
";"			END
","			COMMA
"("			LPAREN
")"			RPAREN

// Regular expression tokens specified in JavaCC format
TOKEN :
{
	<INTEGER: ["1"-"9"] (["0"-"9"])* >
}

Actions	: ( Action )+
	;
Action	: PRINT [ ExprLst ] END					:: Print
	;

ExprLst	: Sum ( COMMA Sum )*
	;

Sum	: Term [ OpAndSum ]					:: Sums
	;

OpAndSum
	: PLUS Sum						:: Plus
	| MINUS	Sum						:: Minus
	;

Term	: MINUS Element						:: UnaryMinus
	| Element
	;

Element	: INTEGER						:: Integ
	| LPAREN Sum RPAREN					:: Paren
	;
