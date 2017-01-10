TOKEN : { 
   < STRING_LITERAL:
      "\""
      (   (~["\"","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )*
      "\""
  >
}

Model	: Prods [Cons] [Vars] :: MainModel
	;

// variable (term) declarations

Vars	: "##" AvarList :: VarStmt
        ;

AvarList : (Avar)+ ;

Avar	: IDENTIFIER "{" [Opts] "}" :: Var
	;

Opts	: (Opt)+ ;

Opt	: LOOKAHEAD(2) IDENTIFIER "=" STRING_LITERAL ::Strlit
        | IDENTIFIER :: Optid
	;

// production declaration

Prods	: (GProd)+ ;

GProd	: IDENTIFIER ":" Pats ";" :: GProduction
	;

Pats	: Pat ( "|" Pat)* ;

Pat	: LOOKAHEAD(2) TermList "::" IDENTIFIER :: GPattern
	| IDENTIFIER  :: SimplePattern
	;

TermList : (GTerm)+ ;

GTerm   : LOOKAHEAD(2) IDENTIFIER "+"	:: PlusTerm  // production name
        | LOOKAHEAD(2) IDENTIFIER "*"   :: StarTerm  // production name
        | IDENTIFIER     	        :: TermName  // token or production name
	| "[" IDENTIFIER "]"            :: OptTerm  // opt tok or prod
	;

// constraints

// The following rules introduce precendence in parsing
// the bottom-most term of an expression is an identifier or (term)
// not binds with terms next
// then and, then or, then implies, then iff, then choose1

// so (a or not b or not c and d) is parsed as
// (((a or (not b)) or (not c)) and d)  -- i think

BExpr	: IDENTIFIER   :: Bvar
	| "(" Expr ")" :: Paren
	;

NExpr	: BExpr
	| "not" NExpr	:: BNot
	;

AExpr	: NExpr 
	| NExpr "and" AExpr :: BAnd
	;

OExpr	: AExpr 
	| AExpr "or" OExpr :: BOr
	;

IExpr	: OExpr
	| OExpr "implies" IExpr :: BImplies
	;

EExpr	: IExpr
	| IExpr "iff" EExpr :: BIff
	;

Expr	: "choose1" "(" ExprList ")" ::BChoose1
	| EExpr
	;

ExprList : EExpr ("," EExpr)* ;

// a list of constraints is an ESList, where
// an ESList is a sequence of one or more ExprStmts

Cons	:  "%%" ESList :: ConsStmt
	;

ESList	: (ExprStmt)+ ;

ExprStmt : Expr ";" :: EStmt
	| "let" IDENTIFIER "iff" Expr ";" :: VarDef
	;
