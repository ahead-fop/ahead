
public state_machine wrffe_mlrs_fist refines wrffe_common_fist {

   Enter stop { stop_exit(m); start_enter(m); }   // for debugging only

   Enter mission { mission_exit(m); stop_enter(m); } 

   public wrffe_mlrs_fist() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_fist sd = new wrffe_mlrs_fist();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
