//                              -*- Mode: Java -*- 
// File            : $Workfile:    $
// Version         : @(#) $Revision: 1.4 $
// Author          : Ted Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Tue Dec  8 17:13:45 1998
// History         :

package fsats.util;

import fsats.measures.Time ;

import java.io.*;
import java.util.*;
import java.awt.event.*;
//import com.sun.java.swing.Timer;

/**
 * This class implements a thread safe queue with a blocking get method.
 */
public class BlockingQueue
{

    // the queue
    private Vector vector;

    
    public BlockingQueue()
    {
        vector = new Vector();
    }

    
    /**
     * Put Object at the back of the queue.
     */
    public synchronized void put( Object obj )
    {
        vector.addElement( obj );
        notify();  
    }

    
    /**
     * Return the object at the front of the queue, blocking until available
     */
    public synchronized Object get() throws InterruptedException {

        while ( vector.isEmpty () )
            wait();

        return vector.remove (0) ;
    }
    
        
    /**
     * Return the object at the front of the queue, blocking until available
     * or the specified timeout occurs, in which case null is returned.
     * For timeout <= 0, return right away.  Timeout is in milliseconds.
     */
    public synchronized Object get (int timeout) throws InterruptedException {

	long whenDone = System.currentTimeMillis () ;
	whenDone += (timeout > 0 ? timeout : 0) ;
	
        while (vector.isEmpty ()) {
	    long now = System.currentTimeMillis () ;
	    if (now >= whenDone)
		return null ;
	    wait (whenDone - now) ;
	}
	    
        return vector.remove (0) ;
    }


    public synchronized boolean isEmpty()
    {
        return vector.isEmpty();
    }
    
    
    /**
     * Test driver
     */
    /** comment references to swing components to keep size down
    public static void main( String[] args )
    {
        class TimedPut implements ActionListener
        {
            Timer localTimer;
            BlockingQueue queue;
            Object object;
            
            public TimedPut( BlockingQueue queue )
            {
                this.queue = queue;
                localTimer = new Timer(0, this);
                localTimer.setRepeats( false );
            }

            public void put( int delay, Object object )
            {
                if ( delay <= 0 )
                    queue.put( object );
                else
                    {
                        this.object = object;
                        localTimer.setInitialDelay( delay );
                        localTimer.start();
                    }
            }

            public void actionPerformed( ActionEvent e )
            {
                localTimer.stop();
                queue.put( object );
                object = null;
            }
        }

        BlockingQueue q = new BlockingQueue();
        TimedPut timedPut = new TimedPut( q );
        Integer i = null;

        try {
            
            q.put( new Integer(1) );
            i = (Integer)q.get();
            System.out.println( i + " " + new Date() );

            timedPut.put( 10 * Time.SECOND, new Integer(2) );
            i = (Integer)q.get();
            System.out.println( i + " " + new Date() );

            timedPut.put( 10 * Time.SECOND, new Integer(3) );
            i = (Integer)q.get( 11000 );
            System.out.println( i + " " + new Date() );
 
            timedPut.put( 10 * Time.SECOND, new Integer(4) );
            i = (Integer)q.get( 2000 );
            System.out.println( i + " " + new Date() );

            i = (Integer)q.get();
            System.out.println( i + " " + new Date() );

            // zero timeout with nothing in queue
            System.out.println("zero/neg one timeout gets");
            i = (Integer)q.get( 0 );
            System.out.println( i + " " + new Date() );
            i = (Integer)q.get( -1 );
            System.out.println( i + " " + new Date() );

            // zero timeout with something in queue
            timedPut.put( 0 , new Integer(6) );
            timedPut.put( 0 , new Integer(7) );
            i = (Integer)q.get( 0 );
            System.out.println( i + " " + new Date() );
            i = (Integer)q.get( -1 );
            System.out.println( i + " " + new Date() );


        
            System.out.println("blocking indefinitely");
            i = (Integer)q.get();
            System.out.println( i + " " + new Date() );
            
        } catch( InterruptedException e ) {}
    }
    ***/
}
