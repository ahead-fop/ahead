layer Connected;


 public
refines class Vertex {
      public int componentNumber;

      public void display() {
         System.out.print(" comp# "+ componentNumber + " ");
         Super().display();
      }
   }
