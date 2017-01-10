public state_machine wrffe_mortar_repeater refines wrffe_common_repeater {

   edge t20 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   edge t21 : mission -> stop 
   conditions m.msg == M.eom
   do { /* upstream(eom) */; }

   public wrffe_mortar_repeater() { super(); }

   static int array[] = { M.initialize, M.deny, M.accept,
                          M.shot, M.splash, M.roundsComplete, M.eom };

   public static void main(String args[]) {
      wrffe_mortar_repeater sd = new wrffe_mortar_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
