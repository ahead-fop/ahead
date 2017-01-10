// base grammar for mini-calculator
// INTEGER is predefined
// Tokens here

"+"  PLUS

// first production is start production

Expr
    : INTEGER
    | INTEGER Operator Expr :: Opr
    ;

Operator
    : PLUS   :: Plus
    ;
