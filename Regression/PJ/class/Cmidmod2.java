layer mid2;

refines class c {
   new void e() { }
   overrides void b() {
      Super().b();
      /* new code2 */ 
   }
}
