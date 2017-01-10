package fsats.util;

import java.util.Vector;

/**
 * 
 * Instances of classes that implement this interface are used to filter
 * processes. These instances are used to filter the list of process ids
 * that are returned by the list method of class PosixPS. 
 *
 * @author Sean Hill
 * @version 1.0, 04/14/98
 * @see fsats.util.PosixPS
 * @see fsats.util.PosixPS#list(fsats.util.PosixProcessFilter)
 */
public interface PosixProcessFilter
{
  /**
   * Return true to have the PosixPS object accept the process, else
   * false. Index into the Vector using the variable defined in the
   * PosixPS class.
   */
   public boolean accept(Vector ps_fields);
}
