// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett


package fsats.util;

import fsats.util.DateUtil;

import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;


/**
 * This class implements an observable GMT clock.
 * Observers are notified of the current GMT every second.
 * A single global instance if provided.
 */
public class GMTObservableClock extends ObservableClock
{

    private static GMTObservableClock instance;

    
    private GMTObservableClock()
    {}

    
    
    public synchronized static GMTObservableClock getInstance()
    {
        if ( instance == null )
            instance = new GMTObservableClock();
        return instance;
    }



    public Date getDate()
    {
        return DateUtil.currentGMT();
    }
    
    
    
}
