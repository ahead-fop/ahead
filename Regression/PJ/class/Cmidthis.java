// this tests this(..) and super(..) 
// These constructs are now permitted

layer Cmid;

import AnotherPackage;

refines class top implements java.io.Serializable, xxx {

   top(float x) {  this(); }

   top(String x) { super(x); }
}

