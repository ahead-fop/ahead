package fsats.measures;

import java.lang.Math;
import java.util.Random;

import fsats.mathFunctions.RandomFunctions;

/**
 * A FOV type - as in Field of View.
 *   Actually a square slice.
 *    minRange     : >=0 in m (minRange < maxRange)
 *    maxRange     : >=0 in m
 *    lookDirection: >=0 in degrees east of north.
 */
public abstract class Fov
{
   static Fov nullfov = new NullFov();


  /**
   *  Test for a null Fov
   */
   public boolean isNull()
   {
      return false;
   }



   /**
    * Return a null Fov
    */
   public static Fov nullFov()
   {
      return nullfov;
   }



  /**
   * Test for equality
   */
   public boolean equals(Fov f)
   {
      boolean yes = true;
      if (!f.isNull())
      {
         if (minRange != f.minRange) yes = false;
         if (maxRange != f.maxRange) yes = false;
         if (lookDirection != f.lookDirection) yes = false;
         if (getWidth() !=f.getWidth() ) yes = false;
      }
      else
      {
         yes = false;
      }

      return yes;
   }



   /**
    * Compute and return a fov polygon for the fov given the
    * passed initial location.  The fovPolygon passed in may be overwritten.
    */
   public abstract FovPolygon getPolygon(Location startLoc);


   /**
    * return a copy of the fov.
    */
   public abstract Fov copy();



   /**
    * FOV Shape string
    */
   public abstract String fovShape();
  

  /**
   * Given an origin return a "random" location in this FOV.
   */
   public abstract Location getRandomVisibleLocation(Location origin, Random seed);



  /**
   * Return a "random" relative location in this FOV.
   */
   public abstract RelativeLocation getRandomRelativeLocation(Random seed);



   /**
    * The width of perception in meters.
    */
   public abstract double getWidth();



   /**
    * String representation of the FOV
    */
   public abstract String toString();



   /**
    * Test for equality
    */

   /**
    * The minimum visible range in meters.
    */
   public double getMinRange()
   {
      return minRange;
   }


  
   /**
    * The maximum visible range in meters.
    */
   public double getMaxRange()
   {
      return maxRange;
   }


  
   /**
    * The look direction
    */
   public double getLookDirection()
   {
      return lookDirection;
   }


   
   protected Fov(
      double minRange,
      double maxRange,
      double lookDirection)
   {
      this.minRange = (minRange >= 0.0) ? minRange : 0.0;
      this.maxRange = (maxRange >= 0.0) ? maxRange : 0.0;
      this.lookDirection = (lookDirection >= 0.0) ? lookDirection : 0.0;

      maxRange = (maxRange<minRange) ? minRange : maxRange;
   }
 
   protected double minRange;
   protected double maxRange;
   protected double lookDirection;
}



class NullFov
   extends Fov
{
  NullFov()
  {
     super(0.0, 0.0, 0.0);
  }



  /**
   * Return an empty FovPolygon.
   */
   public FovPolygon getPolygon(
      Location loc)
   {
      return new FovPolygon();
   }



  /**
   *  Test for a null Fov
   */
   public boolean isNull()
   {
      return true;
   }



  /**
   * Test for equality
   */
   public boolean equals(Fov f)
   {
      return f.isNull() ? true : false;
   }


   public RelativeLocation getRandomRelativeLocation(Random seed)
   {
      return RelativeLocation.nullRelativeLocation();
   }

  /**
   * Given an origin return a "random" location in this FOV.
   */
   public Location getRandomVisibleLocation(Location origin, Random seed)
   {
      //return new nullLocation();
      return Location.nullLocation();
   }



   /**
    * The width of perception in meters.
    */
   public double getWidth()
   {
      return 0.0;
   }



   /**
    * String representation of the FOV
    */
   public String toString()
   {
      return "null fov";
   }



   /**
    * FOV Shape string
    */
   public String fovShape()
   {
      return shapeString;
   }



   /**
    * return a null fov copy
    */
   public Fov copy()
   {
      return nullFov();
   }


   private static final String shapeString = "NULL";
}
