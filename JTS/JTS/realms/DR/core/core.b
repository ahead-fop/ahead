TOKEN :
{
< INSIDE: ("inside" | "left2right" | "flowright") >
| < OUTSIDE: ("outside" | "right2left" | "flowleft" ) >

| < IDENTIFIERDOT : <IDENTIFIER> ("." <IDENTIFIER>)* >

| < INTEGER_LITERAL: 
        <DECIMAL_LITERAL> (["l","L"])?
      | <HEX_LITERAL> (["l","L"])?
      | <OCTAL_LITERAL> (["l","L"])?>
|	<#DECIMAL_LITERAL:  ["1"-"9"] (["0"-"9"])*>
|	<#HEX_LITERAL:  "0" ["x","X"] (["0"-"9","a"-"f","A"-"F"])+>
|	<#OCTAL_LITERAL:  "0" (["0"-"7"])*>
|	< FLOATING_POINT_LITERAL: 
        (["0"-"9"])+ "." (["0"-"9"])* (<EXPONENT>)? (["f","F","d","D"])?
      | "." (["0"-"9"])+ (<EXPONENT>)? (["f","F","d","D"])?
      | (["0"-"9"])+ <EXPONENT> (["f","F","d","D"])?
      | (["0"-"9"])+ (<EXPONENT>)? ["f","F","d","D"]>
|	<#EXPONENT:  ["e","E"] (["+","-"])? (["0"-"9"])+>
|	< CHARACTER_LITERAL: 
      "'"
      (   (~["'","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )
      "'">
|	< STRING_LITERAL: 
      "\""
      (   (~["\"","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )*
      "\"">
}


"and"		AND
"!"		BANG
"<"		LSS
"<="		LEQ
"=="		EQ
">"		GTR
">="		GEQ
"?"		QUESTION
";"		SEMI
"("		LEFTB
")"		RIGHTB
"constant"      CONSTANT
"layer"		LAYER
"provides"	PROVIDES
"requires"	REQUIRES
"single"	SINGLE
"extern"        EXTERN
"left"	LEFT
"right"	RIGHT

AST_Program
	: AspDecl [ AttDecl ] [ RulesDecl ]    :: Program
	;

AspDecl
	: [ Singleton ] [ Const ] LAYER  [ IDENTIFIERDOT ] SEMI :: AspectDecl
	;

Const
	: CONSTANT	:: ConstantInstance
	;

Singleton
	: SINGLE	:: SingleInstance
	;

AttDecl
	: ( Attr ) +
	;

Attr
	: [Import] Modifier IDENTIFIERDOT IDENTIFIERDOT [ InitVal ] SEMI :: Attribute
	;

Import
	: EXTERN	:: ImportTok
	;

Modifier
	: OUTSIDE	:: Synthetic
	| INSIDE	:: Inherit
	;

InitVal
	: LEFTB Predikate RIGHTB		:: Init
	;

RulesDecl
	: ( Rule )+
	;

Rule
	: Condition Direction Predikate  SEMI :: RuleDecl
	;

Condition
	: REQUIRES	:: Reqr
	| PROVIDES	:: Prov
	;

Direction
	: OUTSIDE	:: Up
	| INSIDE	   :: Down
	| LEFT      :: Left
	| RIGHT     :: Right
	;

Predikate
	: Pred ( AND Pred )*
	;

Pred
	: BANG IDENTIFIERDOT 		:: BoolNegate
	| QUESTION IDENTIFIERDOT		:: BoolUndef
	| INTEGER_LITERAL Rel IDENTIFIERDOT  :: IntLeft
	| LOOKAHEAD(2) IDENTIFIERDOT Rel INTEGER_LITERAL :: IntRight
	| LOOKAHEAD(2) IDENTIFIERDOT	:: BoolAssert
	;

Rel
	: LSS		:: Lss
	| LEQ		:: Leq
	| EQ		:: Equ
	| GTR		:: Gtr
	| GEQ		:: Geq
	;
