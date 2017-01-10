/*
 * Last Modified: Oct 23rd
 */
package ProgramCube.Validation.ortho;

import java.util.*;

/**
 * Main class for orthogonality testing.
 * 
 */

public class OrthoMain {
    
    //leave blank for current directory or 
	//specify relative or absolute path
	private final static String LOGPATH = "";
	
	//name prefix for each log
	private final static String LOGNAME = "output-log";
	
	
    public static void main(String[] argv) throws Exception {
        
        if (argv.length < 1) {
            System.out.println("No matrix file specified.");
            return;
        }
        
        PLMatrix x = new PLMatrix(argv[0]);
        
        System.exit(0);
        	
        System.out.println("Checking membership conflicts...\n");
        x.calcMemConflicts();
        ArrayList c = x.getMemConflictList();
        Layer[] pair = new Layer[2];
        for (int i = 0; i < c.size(); i++) {
            pair = (Layer[])c.get(i);
            System.out.println(pair[0].toString() + " - " + pair[1].toString());
        }
        System.out.println("\n...done checking membership conflicts\n");

        System.out.println("Checking reference conflicts...\n");
        x.calcRefConflicts();
        ArrayList rc = x.getRefConflictList();
        for (int i = 0; i < rc.size(); i++)
            System.out.println(rc.get(i).toString());
        System.out.println("\n...done checking reference conflicts\n");
        
        
        /*
        System.out.println("Logging...\n");
        x.logLayers(LOGPATH+LOGNAME+"Features.log");
        x.logFeatureDetails(LOGPATH+LOGNAME+"FeatureDetails.log");
        x.logConflicts(LOGPATH+LOGNAME+"Conflicts.log");
        x.logConflictDetails(LOGPATH+LOGNAME+"ConflictDetails.log");
        System.out.println("\n...done logging\n");
        */
        
        
        
    }

}
