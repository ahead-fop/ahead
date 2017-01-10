//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Tue Mar  9 11:12:20 1999

package fsats.misc;

import fsats.misc.Plan;

/**
 * A singleton class which stores the test plan in use.  This class is
 * used in situations where there can only be one test plan, such as
 * test execution.
 */
public class CurrentPlan extends Plan
{

    private static CurrentPlan plan;
    

    /*
     * This constructor is private to enforce this class being a singleton. 
     */
    private CurrentPlan()
    {}    

    
    /**
     * Get the singleton instance of the current plan.
     */
    public synchronized static Plan getInstance()
    {
        if ( plan == null )
            plan = new CurrentPlan();        
        return plan;
    }


}
