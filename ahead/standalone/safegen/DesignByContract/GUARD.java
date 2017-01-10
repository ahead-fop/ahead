package DesignByContract;

/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 21, 2005
 *  Project: CubeServer
 *  GUARD.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * Makes sure a condition is true. Precondition and Postconditions are
 * logically enforced at the method being looked at. This is similar to
 * java assertions, however doesnt require compiler flag and can be
 * turned off without having to remove all "assert" statements.
 * 
 * @author Sahil
 * @depends on precondition and postcondition method names to be _pre_xyz or _post_xyz
 */
public class GUARD {
    
    //controls whether Guards will be asserted or not
    public static boolean NDEBUG = true;

    
    private static void printStack(String why) {
        Throwable t = new Throwable(why);
        StackTraceElement[] stackElems = t.getStackTrace();
        System.err.flush();
        System.err.println("---------------------------------");
        System.err.println("Assertion Failure:");
        System.err.println("    \""+why+"\"");
        System.err.println("");
        
        String methodName = stackElems[2].getMethodName();
        methodName += "( .. )";
        
        if (methodName.startsWith("_pre_"))
                System.err.println("Precondition : "+ methodName.replace("_pre_",""));
        else if (methodName.startsWith("_post_"))
            System.err.println("Postcondition : "+ methodName.replace("_post_",""));
        else
            System.err.println("Method : "+ stackElems[2].getMethodName());
        
        System.err.println(" Class : "+ stackElems[2].getClassName());

        System.err.println("---------------------------------");
        System.err.flush();
        
       t.printStackTrace();
       System.exit(1);
    }

    public static void ensure(boolean expression, String why) {
       if (NDEBUG && !expression) {
          printStack(why);
       }
    }

}
