/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Melanie Kail <p>
 * Company:      <p>
 * @author Melanie Kail
 * @version 1.0
 */

package fsats.measures;

/**
 * Indicates an invalid ellipsoid name.
 */
public class EllipsoidInvalidNameException extends Exception
{

  public EllipsoidInvalidNameException( String msg )
  {
    super( msg );
  }

}

