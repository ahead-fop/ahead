package fsats.measures;

import java.lang.Math;
import java.util.Vector;
import java.util.Random;

import fsats.mathFunctions.RandomFunctions;
import fsats.mathFunctions.GreatCircleFunctions;

/**
 * A Fan FOV type -
 * azimuth : >=0 in degrees (aka width).
 * (visible arc of space to the left and right of look direction vector)
 */
public class FanFov
   extends Fov
{

   private double leftIdeg(
      double i)
   {
      return (lookDirection - i + 360.0 ) % 360.0;
   }

   private double rightIdeg(
      double i)
   {
      return (lookDirection + i) % 360.0;
   }

   /**
    * Compute and return a fov polygon for the fov given the 
    * passed initial location.  The fovPolygon passed in may be overwritten.
    */
   public FovPolygon getPolygon(
      Location startLoc)
   {  
      //int npoints = 8;
      //Location[] locs = new Location[8];
      
      Vector vLocs = new Vector();
      
      double left = (lookDirection - azimuth/2.0 + 360.0 ) % 360.0;
      double right = (lookDirection + azimuth/2.0) % 360.0;

      //opfac position
      vLocs.addElement(startLoc);

      //min range away in the look direction.
      vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, lookDirection, minRange));

      //min range away every 10 degrees to the left from the look direction.
      for (double i=10; i<azimuth/2.0; i=i+10)
      {
         vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
            startLoc, leftIdeg(i), minRange));
      }
         
      //min range away in the left most direction.
      vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, left, minRange));

      //max range away in the left most direction.
      vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, left, maxRange));

      //max range away every 10 degrees from the look direction to the right.
      for (double i=azimuth/2.0; i>0; i=i-10)
      {
         vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
            startLoc, leftIdeg(i), maxRange));
      }

      //max range away in the look direction.
      vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, lookDirection, maxRange));

      //max range away every 10 degrees to the right of the look direction.
      for (double i=10; i<azimuth/2.0; i=i+10)
      {
         vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
            startLoc, rightIdeg(i), maxRange));
      }

      //max range away in the right most direction.
      vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, right, maxRange)); 

      //min range away in the right most direction.
      vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, right, minRange)); 

      //min range away every 10 degrees from the right of the look direction.
      for (double i=azimuth/2.0; i>0; i=i-10)
      {
         vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
            startLoc, rightIdeg(i), minRange));
      }

      //min range away in the look direction.
      vLocs.addElement(GreatCircleFunctions.locationGivenRadialAndDistance(
         startLoc, lookDirection, minRange));

      
      int npoints = vLocs.size();
      Location[] locs = new Location[npoints];
      vLocs.copyInto(locs);

      return new FovPolygon(locs, npoints);
   }


   /**
    * The angular width of perception in degrees. aka azimuth.
    */
   public double getWidth()
   {
      return azimuth;
   }
  

  
   /**
    * Test for equality
    */
   public boolean equals(Fov f)  
   {
      return (f instanceof FanFov) ? super.equals(f) : false;
   }


   /**
    * Get FAN fov shape string.
    */
   public static String fanFovShape()
   {
      return shapeString;
   }


 
   /**
    * FOV Shape string => FAN
    */
   public String fovShape()
   {
      return shapeString;
   }



   /**
    * return a copy of the fov.
    */
   public Fov copy()
   {
      return new FanFov(minRange, maxRange, azimuth, lookDirection);
   }


  /**
   * Return a "random" relative location in this FOV.
   */
   public RelativeLocation getRandomRelativeLocation(
      Random seed)
   {
      /* d == distance to visible location */
      double d = RandomFunctions.getRandomIn(minRange, maxRange, seed);

      /* a == where in the azimuth */
      double a = RandomFunctions.getRandomIn(0.0, azimuth, seed);

      /* angle east of north to visible location*/
      double theta = (lookDirection + (a - azimuth/2.0)) % 360.0;
      
      return new RelativeLocation(d, theta);
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
      return "fan fov with min range " + minRange + " (m), max range " + maxRange +
             " (m), look direction " + lookDirection + " (degrees East of North), " +
             " and azimuth of " + azimuth + " (degrees)";
   }
   


   public FanFov(
      double minRange,
      double maxRange,
      double azimuth,
      double lookDirection)
   {
      super(minRange, maxRange, lookDirection);
      this.azimuth = (azimuth >= 0.0) ? azimuth : 0.0;
   }
 
   private double azimuth;
   private static final String shapeString = "FAN";
}
