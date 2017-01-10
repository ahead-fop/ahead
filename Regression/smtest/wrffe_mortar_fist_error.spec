public state_diagram wrffe_mortar_fist_error refines wrffe_mortar_fist {

   otherwise_default { ignore_message(current_state, m); }

   edge t6 : one -> mission
   conditions m.msg == M.shot
           || m.msg == M.splash
           || m.msg == M.roundsComplete
   do {  System.out.println("dropped accept message"); }

   edge t7 : one -> stop
   conditions m.msg == M.eom
   do {  System.out.println("dropped accept message"); }

   public wrffe_mortar_fist_error() { super(); }

   static int array[] = { M.initialize, /*ignore*/ M.mfr, M.deny, 
                          M.initialize, M.accept, M.shot,/*ignore*/ M.mfr, M.splash,
                          M.roundsComplete, M.eom,
                          M.initialize, /*ignore*/ M.mfr, M.eom,
                          M.initialize, M.splash, M.eom };

   public static void main(String args[]) {
      wrffe_mortar_fist_error sd = new wrffe_mortar_fist_error();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
