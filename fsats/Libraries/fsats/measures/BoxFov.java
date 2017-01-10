package fsats.measures;

import java.lang.Math;
import java.util.Random;

import fsats.mathFunctions.RandomFunctions;
import fsats.mathFunctions.GreatCircleFunctions;

/**
 * A Square FOV type - 
 * Actually a square slice.
 * width : >=0 in m.
 */
public class BoxFov
   extends Fov
{
   /**
    * Compute and return a fov polygon for the fov given the
    * passed initial location.  The fovPolygon passed in may be overwritten.
    */
   public FovPolygon getPolygon(
      Location startLoc)
   {
      int npoints = 8;
      Location[] locs = new Location[8];

      double left = (lookDirection - 90.0 + 360.0) % 360.0;
      double right = (lookDirection + 90.0) % 360.0;

      locs[0] = startLoc;

      locs[1] = GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, lookDirection, minRange);
      locs[7] = locs[1];

      locs[2] = GreatCircleFunctions.locationGivenRadialAndDistance(
         locs[1], left, width/2.0);

      locs[6] = GreatCircleFunctions.locationGivenRadialAndDistance(
         locs[1], right, width/2.0);

      locs[4]= GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, lookDirection, maxRange);
      
      locs[3] = GreatCircleFunctions.locationGivenRadialAndDistance(
         locs[4], left, width/2.0);
    
      locs[5] = GreatCircleFunctions.locationGivenRadialAndDistance(
         locs[4], right, width/2.0);
      
      return new FovPolygon(locs, npoints);
   }


   /**
    * The width of perception in meters.
    */
   public double getWidth()
   {
      return width;
   }



   /**
    * Test for equality
    */
   public boolean equals(Fov f)
   {
      return (f instanceof BoxFov) ? super.equals(f) : false;
   }  


   
   /**
    * return a copy of the fov.
    */
   public Fov copy()
   {
      return new BoxFov(minRange, maxRange, width, lookDirection);
   }


   
   /**
    * Get BOX fov shape string.
    */
   public static String boxFovShape()
   {
      return shapeString;
   }



   /**
    * FOV Shape string => BOX
    */
   public String fovShape()
   {
      return shapeString;
   }


  /**
   * Return a "random" relative location in this FOV.
   */
   public RelativeLocation getRandomRelativeLocation(
      Random seed)
   {
      /* a == distance within the valid range to visible location */
      double a = RandomFunctions.getRandomIn(minRange, maxRange, seed);

      /* w == where in the width */
      double w = RandomFunctions.getRandomIn(0.0, width, seed);
      
      /* b == offset from the center of the width */
      double b = w - (width/2.0);

      /* d == distance to point */
      double d = Math.sqrt(a*a + b*b);

      //atan2 returns values from -pi to pi
      //of the angle measured counter-clockwise 
      //from the x axis to the line running from (0,0) thru (b,a)
      double aa = Math.atan2(a,b);
      double alpha = Math.toDegrees(aa);
      //System.out.println("radians aa="+aa+"      degrees alpha="+alpha);
 
      //need phi to be the angle
      //measured clockwise from the y axis 
      //to the line running from (0,0) thru (b,a)
      double phi = 0.0;
      //System.out.println("alpha="+alpha);
      if (0.0<=alpha && alpha<=180.0)     //if the point (b,a) is in the 1st or 2nd quadrant
      {  
         phi = 90.0 - alpha;
         //System.out.println("     if phi="+phi);
      }
      else                                //else it's in the 3rd or 4th quadrant
      {  
         //This is an error - it should never happen since distance is always positive!!
         System.out.println("[BoxFov] Error getting Random Visible Location\n" +
                            "Returning point in lookDirection at the generated distance");
      }

      //now need to correct for look direction.
      phi = (lookDirection + phi) % 360.0;
      
      return new RelativeLocation(d, phi);
   }



  /**
   * Given an origin return a "random" location in this FOV.
   */
   public Location getRandomVisibleLocation(
      Location origin,
      Random seed)
   {
      RelativeLocation rl = getRandomRelativeLocation(seed);
      return origin.locationOf(rl.distance(), rl.theta());
   }



   /**
    * String representation of the FOV
    */
   public String toString()
   {
      return "box fov with min range " + minRange + " (m), max range " + maxRange +
             " (m), look direction " + lookDirection + " (degrees East of North), " +
             " and width of " + width + " (m)";
   }


   
   public BoxFov(
      double minRange,
      double maxRange,
      double width,
      double lookDirection)
   {
      super(minRange, maxRange, lookDirection);
      this.width = (width >= 0.0) ? width : 0.0;
   }
 
   private double width;
   private static final String shapeString = "BOX";
}
