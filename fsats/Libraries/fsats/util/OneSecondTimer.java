// Version         : @(#) $Revision: 1.2 $
// Author          : Ted Beckett


package fsats.util;

import fsats.measures.Time ;
import fsats.util.DateUtil;

import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;


/**
 * This class provides a global one-second timer.
 * Applications like digital clocks and timer widgets which
 * need a one second timer can use this single thread and
 * avoid the overhead of multiple instances each having their
 * own private timer thread.
 */
public class OneSecondTimer
{

    private CustomTimer timer;

    
    // the single global instance of this class
    private static OneSecondTimer instance = new OneSecondTimer();




    
    // this class is a singleton
    private OneSecondTimer()
    {}


    

    public synchronized static OneSecondTimer getInstance()
    {
        return instance;
    }


    

    public synchronized void addActionListener( ActionListener listener )
    {
        if ( timer == null )
            {
                timer = new CustomTimer( Time.SECOND, listener );
                timer.setRepeats( true );
            }
        else
            {
                timer.addActionListener( listener );
            }
        
        if ( timer.getListenerCount() == 1 )
            timer.start();
    }

    


    
    public synchronized void removeActionListener( ActionListener listener )
    {
       timer.removeActionListener( listener );
       
       if ( timer.getListenerCount() == 0 )
           timer.stop();       
    }



    
    public int getListenerCount()
    {
        int numListeners = 0;
        
        if ( timer != null )
           numListeners = timer.getListenerCount();

        return numListeners;
    }


    

    /**
     * the standard swing timer with public access to the number
     * of listeners
     */
    private class CustomTimer extends Timer
    {
        
        public CustomTimer( int delay,
                            java.awt.event.ActionListener listener )
        {
            super( delay, listener );
        }

        
        public int getListenerCount()
        {
            return listenerList.getListenerCount();
        }
        
    }


    
    /* begin test driver ***************************************
    private class Test implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            System.out.println( new Date() );
        }
    }
    public static void main( String[] args )
    {
        OneSecondTimer timer = OneSecondTimer.getInstance();
        T t = new T();
        System.out.println("add listener");
        timer.addActionListener( t );
        try {
            Thread.sleep( 5 * Time.SECOND );
        } catch ( Throwable th ) {}
        System.out.println("add listener");
        timer.addActionListener( t );
        try {
            Thread.sleep( 5 * Time.SECOND );
        } catch ( Throwable th ) {}
        System.out.println("remove listener");
        timer.removeActionListener( t );
        try {
            Thread.sleep( 5 * Time.SECOND );
        } catch ( Throwable th ) {}
        System.out.println("remove listener");
        timer.removeActionListener( t );
        try {
            Thread.sleep( 5 * Time.SECOND );
        } catch ( Throwable th ) {}    
    }
    ** end test driver **************************************  */
        
}
