// Package and name scoping
package Jakarta.util;

/** Container class for file specification information.
    @author R. Cardone (1/8/98)
  */
public class CleanFileSpec
{
 // Instance variables.
 public String filter          = null;
 public String subcommand      = null;
 public Object subcommandParms = null;

 /* ------------------------------------------------------------------- */
 /* toString:                                                           */
 /* ------------------------------------------------------------------- */
 /** Return a string representation of this object.
     @return String representation. */
 public String toString()
 {
  return '(' + subcommand + ": " + filter + ", " + subcommandParms + ')';
 }
}
