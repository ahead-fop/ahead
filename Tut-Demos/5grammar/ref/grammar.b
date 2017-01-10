// adds minus operator
// add new token

"-" MINUS

// import previously defined left-hand side

require Operator;

// add new production

Operator
    : MINUS         :: Minus
    ;

