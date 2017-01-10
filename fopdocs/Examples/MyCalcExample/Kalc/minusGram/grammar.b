// grammar.b : minus grammar

"-" MINUS

require Action;               //imports grammar rule defined elsewhere

Action: MINUS :: Minus
      ;

