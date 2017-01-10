
public state_machine wrffe_mlrs_repeater refines wrffe_common_repeater {

   Enter mission { mission_exit(m); stop_enter(m); } 

   public wrffe_mlrs_repeater() { super(); }

   static int array[] = { M.initialize, M.deny, M.deny, M.accept };

   public static void main(String args[]) {
      wrffe_mlrs_repeater sd = new wrffe_mlrs_repeater();
      int i;
      M m = new M();

      for (i=0; i<array.length; i++) {
         m.msg = array[i];
         sd.receive_message(m);
         System.out.println(sd.getState());
      }
   }
}
