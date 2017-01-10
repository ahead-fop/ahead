// same for wrffe_artillery_fo 

public state_machine wrffe_mortar_fo refines wrffe_common_fo {

   states three;

   edge t4 : mission -> mission
   conditions m.msg == M.shot ||
              m.msg == M.splash
   do { ; }

   edge t5 : mission -> three
   conditions m.msg == M.roundsComplete
   do { /* set(confirmEffects) = (TimeOfFlt+VisConf)*/; }

   edge t6 : three -> three 
   conditions m.msg == M.splash
   do { /* set(confirmEffects) = greaterOf(ConfirmEffects, TimeToImpact+VisConf)*/; }

   edge t7 : three -> stop
   conditions m.msg == M.confirmEffects
   do { /* upstream(eom) */; }

   Enter stop { stop_exit(m); start_enter(m); }   // for debugging only

   public wrffe_mortar_fo() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept, M.shot, M.splash,
                          M.roundsComplete, M.confirmEffects };

   public static void main(String args[]) {
      wrffe_mortar_fo sd = new wrffe_mortar_fo();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
