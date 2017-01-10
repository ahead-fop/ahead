// tests if root clauses are errors in extensions -- they are

layer ext5;

refines State_machine root {
   Delivery_parameters( M m );
   Unrecognizable_state { Yourignore(m); }
}

