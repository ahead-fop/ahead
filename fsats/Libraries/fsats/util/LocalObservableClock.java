// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett


package fsats.util;

import fsats.util.DateUtil;

import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;


/**
 * This class implements an observable Local Time clock.
 * Observers are notified of the  current local time every second.
 * A single global instance if provided.
 */
public class LocalObservableClock extends ObservableClock
{

    private static LocalObservableClock instance;

    
    private LocalObservableClock()
    {}

    
    
    public synchronized static LocalObservableClock getInstance()
    {
        if ( instance == null )
            instance = new LocalObservableClock();
        return instance;
    }



    public Date getDate()
    {
        return new Date();
    }
    
    
}
