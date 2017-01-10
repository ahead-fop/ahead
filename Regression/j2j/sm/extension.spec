// this is exactly the same as the star-test,
// except that the inheritance chain is distributed
// over several files.  this forces jak2java to 
// track down the file names

State_machine extension extends extension12 {

   Otherwise_default { System.out.println("inheritance works"); }

   Transition t100 : * -> stop
   condition false
   do { /* nothing */ }

   public extension() { }

   static int array[] = { M.deny, M.initialize, M.deny, 
                          M.initialize, M.eom, M.eom };

   public static void main(String args[]) {
      extension sd = new extension();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.delivery(m);
         System.out.println(sd.getState());
      }
   }
}
