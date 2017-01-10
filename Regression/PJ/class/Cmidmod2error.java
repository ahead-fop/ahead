layer miderror2;

refines class c {
   overrides void e() { }
   new void b() {
      Super().b();
      /* new code2 */ 
   }
}
