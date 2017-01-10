layer StronglyConnected;

import  java.util.LinkedList;
import 
	java.util.Collections;
import 
        java.util.Comparator;

 // of FinishTimeWorkSpace
   
   
   // DFS Transpose traversal
   // *************************************************************************	
   public
 class WorkSpaceTranspose extends WorkSpace 
   {
	// Strongly Connected Component Counter
	int SCCCounter;  
		
	public WorkSpaceTranspose() {
           WorkSpaceTransposeConstructor();
        }

        public void WorkSpaceTransposeConstructor() {
	   SCCCounter = 0;
	}
		
	public void preVisitAction( Vertex v)
	{
	  if (v.visited!=true) 
	  { 
	   v.strongComponentNumber = SCCCounter;
	  };
	}

	public void nextRegionAction( Vertex v ) 
	{ 
	  SCCCounter++;
	}
		
   }
