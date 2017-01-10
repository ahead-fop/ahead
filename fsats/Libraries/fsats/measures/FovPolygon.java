package fsats.measures;

/**
 * A FOV Polygon - 
 * Contains the xPoints, yPoints and nPoints of a FOV polygon.
 */
public class FovPolygon
{
   /**
    * Construct a FovPolygon with nPoints.
    */
   public FovPolygon(
      Location[] locs,
      int nPoints)
   {
      this.nPoints = nPoints;
      this.locs = new Location[nPoints];

      for (int i=0; i<nPoints; i++)
      {
         this.locs[i] = locs[i];
      }
   }

   /**
    * Construct an empty FovPolygon.
    */
   public FovPolygon() { }
 
   public int nPoints = 0;
   public Location[] locs = new Location[0];
}
