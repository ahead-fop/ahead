layer StronglyConnected;

import  java.util.LinkedList;
import 
	java.util.Collections;
import 
        java.util.Comparator;

 public
refines class Vertex {
      public int finishTime;
	  public int strongComponentNumber;
	  
      public void display() {
         System.out.print(" FinishTime -> " + finishTime + " SCCNo -> " 
			+ strongComponentNumber);
         Super().display();
      }
   }
