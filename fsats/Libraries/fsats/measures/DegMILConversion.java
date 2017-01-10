package fsats.measures;

import java.lang.Math;

/**
 * For conversion of (decimal) degrees to/from MILs.
 */
public class DegMILConversion
{
   public static double degToMIL(
      double deg)
   {
      return deg*deg2mil;
   }

   public static double MILtoDeg(
      double mil)
   {
      return mil*mil2deg;
   }

   private static final double deg2mil = 6400.0/360.0;
   private static final double mil2deg = 360.0/6400.0;
}
