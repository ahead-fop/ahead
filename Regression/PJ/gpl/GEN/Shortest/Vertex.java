layer Shortest;

import  java.lang.Integer;
import 
	java.util.LinkedList;
import 
	java.util.Collections;
import 
        java.util.Comparator;

 // of Graph

// *********************************************************************************
   public
refines class Vertex {      
	  public String predecessor;  // the name of the predecessor if any
	  public int dweight;         // weight so far from s to it
	  
      public void display() {
         System.out.print(" Pred " + predecessor + " DWeight " + dweight + " ");
         Super().display();
      }
      
   }
