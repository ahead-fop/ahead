
public state_machine wrffe_mlrs_fo refines wrffe_common_fo {

   Enter stop { stop_exit(m); start_enter(m); }   // for debugging only

   Enter mission { mission_exit(m); stop_enter(m); } 

   public wrffe_mlrs_fo() { super(); }

   static int array[] = { M.initialize, M.deny, 
                          M.initialize, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_fo sd = new wrffe_mlrs_fo();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
