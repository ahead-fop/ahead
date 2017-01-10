package fsats.plan;

import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Fowards ChangeEvents to registered listeners.
 */
class ChangeEventMulticaster
{

    List list = new ArrayList();



  /**
   * Register listener.
   */
  public void addListener( ChangeListener listener )
  {
      if ( !list.contains( listener ) )
          list.add( listener );
  }

  /**
   * Deregister listener.
   */
  public void removeListener( ChangeListener listener )
  {
      list.remove( listener );
  }

  /**
   * Send event to registered listeners.
   */
  public void send( ChangeEvent event )
  {
      ListIterator iter = list.listIterator();
      while( iter.hasNext() ) 
          {
              ChangeListener listener = (ChangeListener) iter.next();
              listener.stateChanged( event );
          }
  }

}
