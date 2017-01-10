package fsats.measures;

import java.util.*;

/** 
 * Datum is an enumeration class and follows Bob MacDonald's
 * enumeration design pattern.
 *
 * Datum parameters from NIMA -
 * See http://164.214.2.59/GandG/pubs.html
 */
public class Datum
{
   /** 
    * DatumName is the key for the TreeMap.
    */
   private static TreeMap map = new TreeMap();

   /**
    * Name identifier of the datum.  Convienience for string access.
    * String provided by toString.
    */
   public final String datumName;

   /**
    * Ellipsoid associated with the datum.
    */
   public final Ellipsoid ellipsoid;

   /**
    *  Datum transformation parameters.  Local geodetic datum to WGS 84.
    */
   public double deltaX; 
   public double deltaY;  
   public double deltaZ;  

   /**
    * Datums
    */
   public static final Datum WGS_84 =
      new Datum("WGS_84", Ellipsoid.WGS_84, 0.0, 0.0, 0.0);

   public static final Datum North_America_1927_CONUS = 
      new Datum("North_America_1927", Ellipsoid.Clarke_1866, -8.0, 160.0, 176.0);

   public static final Datum European_1950_Iran = 
      new Datum("European_1950_Iran", Ellipsoid.International_1924, -117.0, -132.0, -164.0);

   public static final Datum European_1950_Europe_Mean_Solution = 
      new Datum("European_1950_Europe_Mean_Solution", Ellipsoid.International_1924, -87.0, -98.0, -121.0);


   /**
    * String representation of the datum equivalent to the datum's name.
    */
   public String toString()
   {
      return datumName;
   }

   /**
    * Returns a Datum such that: Datum.toString().equals(name).
    * @throws DatumInvalidNameException if the name doesn't map to a datum.
    */
   public static Datum fromString(
      String name) 
      throws DatumInvalidNameException
   {
      Datum found = (Datum) map.get(name);
      if ( found == null )
         throw new DatumInvalidNameException(name);
      return found;
   }

   /**
    * Returns a iteration of the enumeration values.
    * The class returned is Datum.  
    * Order is guaranteed to be ascending.
    */
   public static Iterator iterator()
   {
       return map.values().iterator();
   }

   /**
    **????? Can I do this and still have the map.get stuff work????
    */
   public boolean equals(
      Object o)
   {
      boolean yes = ( (o instanceof Datum) && (datumName.equals(((Datum)o).datumName)) ) ? true : false;
      return yes;
   }


   /**
    * Changes the reference datum for the passed location.
    *
    * @param l the location
    * @return the datum shifted location
    */
   public Location shiftTo(
      Datum d,
      Location l)
   {
      Location newLoc = Location.nullLocation();

      Datum fromDatum = l.datum();
      Datum toDatum = d;

      //If datums are equal
      if (fromDatum.equals(toDatum))
      {
         //then no need to shift...
         newLoc = l;
      }
      //shift from WGS_84 to toDatum OR
      //      from from_datum to WGS_84
      else if (fromDatum.equals(Datum.WGS_84) || toDatum.equals(Datum.WGS_84))
      {
         newLoc = shiftDatum(l,fromDatum,toDatum);
      }
      //shift from fromDatum to WGS_84 and then to toDatum
      else 
      {
         newLoc = shiftDatum(shiftDatum(l,fromDatum,Datum.WGS_84), Datum.WGS_84, toDatum);
      }

      return newLoc;
   }

   private Location shiftDatum(
      Location l,
      Datum from,
      Datum to)
   {
      throw new UnsupportedOperationException(
         "Datum.shiftDatum Exception: Datum-Datum conversion not fully implemented");
/*
      Location newL = Location.nullLocation();
      
      //input latitude
      double fromPhi = l.latitude();

      //input longitude
      double fromLamda = l.longitude();

      //input altitude
      double fromH = l.altitude();

      //equatorial radius
      double fromA = from.ellipsoid.equatorialRadius;

      //flatteninng
      double fromF = from.ellipsoid.flattening;

      //second eccentricity squared
      double fromES = from.ellipsoid.secondEccentricitySquared;

      //equatorial radius
      double toA = to.ellipsoid.equatorialRadius;

      //flatteninng
      double toF = to.ellipsoid.flattening;

      //second eccentricity squared
      double toES = to.ellipsoid.secondEccentricitySquared;

      //datum shift parameters
      double dX = 
*/
      
   }


   private Datum(
      String datumName, 
      Ellipsoid ellipsoid,
      double deltaX,
      double deltaY,
      double deltaZ)
   {
      this.datumName = datumName; 
      this.ellipsoid = ellipsoid;
      this.deltaX = deltaX;
      this.deltaY = deltaY;
      this.deltaZ = deltaZ;

      map.put(datumName, this);
   }

}
