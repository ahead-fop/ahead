state_machine wrffe_artillery_repeater refines wrffe_common_repeater {

   states four;

   edge t20 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash ||
              m.msg == M.roundsComplete
   do { /* downstream(m) */; }

   edge t21 : mission -> four
   conditions m.msg == M.eom
   do { /* upstream(eom) */; }

   edge t22 : four -> stop
   conditions m.msg == M.mfr
   do { /* downstream(mfr) */; }

   public wrffe_artillery_repeater() { super(); }

   static int array[] = { M.initialize, M.deny, M.accept,
                          M.shot, M.splash, M.roundsComplete, M.eom,
                          M.mfr };

   public static void main(String args[]) {
      wrffe_artillery_repeater sd = new wrffe_artillery_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}





