// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett


package fsats.util;

import fsats.util.DateUtil;

import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;


/**
 * This class provides an observable interface to a time source.
 * Observers are notified of the time source's current time every second.
 * Observers are given a Date object provided by the time source.
 * The time source is provided by the subclass.
 */
public abstract class ObservableClock
extends Observable implements ActionListener
{

    // use the singleton one second timer for efficiency
    private OneSecondTimer timer = OneSecondTimer.getInstance();

    

    public synchronized void addObserver( Observer o )
    {
        int numObservers = countObservers();
        
        if ( numObservers == 0 )
            timer.addActionListener( this );

        super.addObserver( o );
    }

    


    public synchronized void deleteObserver( Observer o )
    {
        super.deleteObserver( o );

        int numObservers = countObservers();
        
        if ( numObservers == 0 )
            timer.removeActionListener( this );
    }



        
    public void actionPerformed( ActionEvent e )
    {
        Date date = getDate();
        setChanged();
        notifyObservers( date );
    }



    
    public abstract Date getDate();

    

}
