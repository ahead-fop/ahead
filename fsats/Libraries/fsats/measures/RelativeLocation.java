package fsats.measures;

import fsats.mathFunctions.GreatCircleFunctions;

/**
 * A RelativeLocation type - distance and theta to a point (from an unknown origin).
 */
public class RelativeLocation
{
   /**
    * Test for null.
    */
   public boolean isNull()
   {
      return false;
   }
   
   /**
    * Return a null RelativeLocation;
    */
   public static RelativeLocation nullRelativeLocation()
   {
      return nullLoc;
   }

   /**
    * The distance
    */
   public double distance()
   {
      return distance;
   }
  
   /**
    * The theta
    */
   public double theta()
   {
      return theta;
   }

  
   public String toString()
   {
      return "RelativeLocation of " + distance + "(m) at " +
                             theta + "(degrees)";
   }
   

   public RelativeLocation(
      double distance,
      double theta)
   {
      this.theta = theta;
      this.distance = distance;
   }
   
   
   private double distance = 0.0;
   private double theta = 0.0;
   
   private static RelativeLocation nullLoc = new NullRelativeLocation();
}

class NullRelativeLocation
   extends RelativeLocation
{
   NullRelativeLocation()
   {
      super(0.0, 0.0);
   }
   public boolean isNull()
   {
      return true;
   }
}
  
