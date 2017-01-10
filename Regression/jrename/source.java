// tests composition of extensions that reference methods

layer ext111;

import firstPackage.*;

SoUrCe  ext111 "C:!Java!Regression!Mixin!sm!ext111.jak";

 abstract State_machine root$$ext111 {

   void fromext11() { }
}

SoUrCe  ext114 "C:!Java!Regression!Mixin!sm!ext114.jak";

 State_machine root extends root$$ext111 {

   States h1;

   Exit h1 { Super().fromext11(); }

   void fromext14() { }
}
