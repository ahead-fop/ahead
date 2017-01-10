// this extension should generate an error
// because it tries to extend (replace) a
// previously defined constructor

layer conserror;

refines class top {

   top(int q) { topinit(x+y); }
}

