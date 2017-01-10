"{"			LBRACE
"}"			RBRACE
"<"			LANGBRACK
">"			RANGBRACK
"("			OPENPAREN
")"			CLOSEPAREN
"code"			CODE
"options"		_OPTIONS
"LOOKAHEAD"		_LOOKAHEAD
"IGNORE_CASE"		_IGNORE_CASE
"PARSER_BEGIN"		_PARSER_BEGIN
"PARSER_END"		_PARSER_END
"JAVACODE"		_JAVACODE
"TOKEN"			_TOKEN
"SPECIAL_TOKEN"		_SPECIAL_TOKEN
"MORE"			_MORE
"SKIP"			_SKIP
"TOKEN_MGR_DECLS"	_TOKEN_MGR_DECLS
"EOF"			_EOF

TOKEN :
{
	<BALI_TOKEN: <UPPERCASE> (<UPPERCASE> | <DIGIT>)*>
|
	<#UPPERCASE: ["A"-"Z", "_", "$"]>
|
	<STRING:
		"\""
		( (~["\"","\\","\n","\r"])
		| ("\\"
		    ( ["n","t","b","r","f","\\","'","\""]
		    | ["0"-"7"] ( ["0"-"7"] )?
		    | ["0"-"3"] ["0"-"7"] ["0"-"7"]
		    )
		  )
		)*
		"\""
	>
|
	<IDENTIFIER: <LETTER> (<LETTER>|<DIGIT>)*>
|
	<INTEGER: (<DIGIT>)+>
}

//**************************************************
// Productions start here
//**************************************************

//**************************************************
BaliInput
	: [ Options ] [ ParserCode ] [ Rules ]			::BaliInputC
	;


//**************************************************
Rules
	: (Production)+
	;


//**************************************************
Options
	: _OPTIONS "{" findBlockEnd "}" _OPTIONS		::OptionsC
	;


//**************************************************
ParserCode
	: CODE "{" findBlockEnd "}" CODE			::ParserCodeC
	;


//**************************************************
Production
	: LOOKAHEAD(1)
	  //
	  // Since JAVACODE is both a JavaCC reserved word and a Java
	  // identifier, we need to give preference to
	  // "JavacodeProduction" over "BaliProduction".
	  //
	  JavacodeProduction
	| LOOKAHEAD(1)
	  //
	  // Since SKIP, TOKEN, etc. are both JavaCC reserved words and
	  // Java identifiers, we need to give preference to
	  // "RegularExprProduction" over "BaliProduction".
	  //
	  RegularExprProduction
	| LOOKAHEAD(1)
	  // This handles the old-style Bali lexical spec consisting
	  // of simply a string followed by a token name.
	  SimpleProduction
	| LOOKAHEAD(1)
	  //
	  // Since TOKEN_MGR_DECLS is both a JavaCC reserved word and a
	  // Java identifier, we need to give preference to
	  // "TokenMgrDecls" over "BaliProduction".
	  //
	  TokenMgrDecls
	| BaliProduction
	;


//**************************************************
BaliProduction
	: IDENTIFIER ":" TailList ";"				::BaliProd
	;


//**************************************************
TailList
	: Clause ( "|" Clause )*
	;


//**************************************************
Clause
	: [ Lookahead ] Tail					::ClauseC
	;


//**************************************************
Lookahead
	: _LOOKAHEAD "(" findCloseParen ")"			::LookaheadC
	;


//**************************************************
Tail
	: // SimpleList
	  "(" [ Lookahead ] Primitive ")" "+"			::SimpleList
	| // NamedRule, or ComplexList
	  LOOKAHEAD(2)
	  Primitive CLorNR					::CLNR
	| // UnNamedRule
	  RuleName						::UnNamedRule
	;

//**************************************************
CLorNR
	: "(" [ Lookahead ] Primitive Primitive ")" "*"		::ComplexList
	| [ Pattern ] "::" IDENTIFIER				::NamedRule
	;


//**************************************************
Pattern
	: (Primitive)+
	;


//**************************************************
Primitive
	: RuleName
	| BALI_TOKEN						::PrimToken
	| STRING						::PrimString
	| LOOKAHEAD( "[" <_LOOKAHEAD> )
	  "[" Lookahead Primitive "]"				::LAPrim
	| "[" Primitive "]"					::OptPrim
	;


//**************************************************
RuleName
	: IDENTIFIER						::RuleName
	;


//**************************************************
// From JavaCC.jj
//**************************************************


JavacodeProduction
	: "JAVACODE" Block					::JCode
	;


Block
	: findBlockStart findBlockEnd				::Block
	;


RegularExprProduction
	: [ States ]
	  RegexprKind [ CaseFlag ] ":"
	  "{" RegexprSpecList "}"				::RegExprProd
	;

States
	: LOOKAHEAD(2) "<" "*" ">"				::AllStates
	| "<" StateList ">"					::StateListC
	;

StateList
	: BALI_TOKEN ("," BALI_TOKEN)*
	;

RegexprKind
	: _TOKEN						::TokenKind
	| _SPECIAL_TOKEN					::SpecialKind
	| _SKIP							::SkipKind
	| _MORE							::MoreKind
	;

CaseFlag
	: "[" _IGNORE_CASE "]"					::CFlag
	;

RegexprSpecList
	: RegexprSpec ("|" RegexprSpec)*
	;

TokenMgrDecls
	: _TOKEN_MGR_DECLS ":" Block				::TokenMgrDcls
	;

RegexprSpec
	: RegularExpression [ LOOKAHEAD(<LBRACE>) Block ]
		[ OptState ]					::RegexSpecC
	;

OptState
	: ":" BALI_TOKEN					::OptStateC
	;

RegularExpression
	: STRING						::REString
	| LOOKAHEAD(3)
	  "<" [ OptLabel ] ComplexRegExprChoices ">"
								::LTokenDef
	| LOOKAHEAD(2)
	  "<" BALI_TOKEN ">"					::TokExpansion
	| "<" <EOF> ">"						::Eof
	;

ComplexRegExprChoices
	: STRING						::StringChoice
	| findRAngleBracket					::NonStrChoice
	;

OptLabel
	: [ "#" ] BALI_TOKEN ":"				::OptLabel
	;

SimpleProduction
	: STRING BALI_TOKEN					::SimpleProd
	;
