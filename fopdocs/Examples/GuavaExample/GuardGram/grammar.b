// grammar.b

"->" ARROW
require Expression;	// import previously defined production
require Statement;	// import previously defined production
Statement    :  LOOKAHEAD( Expression() "->" )
                Expression ARROW Statement :: GuardCmd
             ;

