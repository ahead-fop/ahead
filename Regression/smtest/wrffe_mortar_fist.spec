public state_diagram wrffe_mortar_fist refines wrffe_common_fist {

   edge t4 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   edge t5 : mission -> stop 
   conditions m.msg == M.eom
   do { /* upstream(eom) */; }

   public wrffe_mortar_fist() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept, M.shot, M.splash,
                          M.roundsComplete, M.eom };

   public static void main(String args[]) {
      wrffe_mortar_fist sd = new wrffe_mortar_fist();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
