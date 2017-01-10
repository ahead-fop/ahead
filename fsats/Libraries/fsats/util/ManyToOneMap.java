//                              -*- Mode: Java -*-
// Author          : Ted Beckett


package fsats.util;

import java.util.*;


/**
 * A HashMap with additional methods for when a Map is used to store
 * a collection of many-to-one relations (many keys map to the same value).
 */
public class ManyToOneMap extends HashMap
{

  /**
   * Returns all keys which map to the specified value.
   * The keys are returned in ascending order according to the 
   * natural order of the key.
   */
  public SortedSet keysFor( Object value )
  {
    SortedSet set = new TreeSet();
    Iterator iterator = entrySet().iterator();
    while( iterator.hasNext() )
    {
      Map.Entry entry = (Map.Entry) iterator.next();
      if ( entry.getValue().equals( value ) ) {
         set.add( entry.getKey() );
      }
    }
    return set;
  }

  /**
   * Removes all entries which have a value equal to the specified value.
   */
  public void removeAll( Object value )
  {
    Iterator iterator = entrySet().iterator();
    while( iterator.hasNext() )
    {
      Map.Entry entry = (Map.Entry) iterator.next();
      if ( entry.getValue().equals( value ) )
         remove( entry.getKey() );
    }
  }

}
