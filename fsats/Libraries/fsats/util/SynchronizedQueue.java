package fsats.util;

public class SynchronizedQueue
{
    private java.util.Vector data = new java.util.Vector();

    public synchronized void put(Object o)
    {
        data.addElement(o);
        notify();
    }

    public synchronized Object get()
    {
        while (data.isEmpty ()) 
            try {
		wait () ;
	    } catch (InterruptedException e) {
		// Repeat loop.
	    }

	return data.remove (0) ;
    }
    
    public synchronized int size() { return data.size(); }

    public synchronized boolean isEmpty() { return data.size() < 1 ; }

}
