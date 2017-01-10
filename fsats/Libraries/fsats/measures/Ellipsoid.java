package fsats.measures;

import java.util.*;

/** 
 * Ellipsoid is an enumeration class and follows Bob MacDonald's
 * enumeration design pattern.
 *
 * Ellipsoid parameters from NIMA - 
 * See http://164.214.2.59/GandG/pubs.html
 */
public class Ellipsoid
{
   private static TreeMap map = new TreeMap();

   /**
    * Name identifier of the ellipsoid.  Convienience for string access.
    * String provided by toString.
    */
   public final String ellipsoidName;

   /**
    * Equatorial radius = semi-major axis = a
    */
   public final double equatorialRadius; 

   /**
    * Polar radius = semi-minor axis = b
    */
   public final double polarRadius; 

   /**
    * Flattening = f = (a-b)/a
    */
   public final double flattening; 

   /**
    * Reciprocal of flattening = 1/f
    */
   public final double reciprocalOfFlattening; 

   /**
    * First eccentricity squared = e2
    */
   public final double eccentricitySquared;  

   /**
    * Second eccentricity squared = e prime squared = ep2
    */
   public final double secondEccentricitySquared;  

   /**
    * Ellipsoids
    * Comparable to ada code at .../ulp/tso_dn_datum_services.adb 
    */
   public static final Ellipsoid Airy = 
      new Ellipsoid("Airy", 6377563.396, 299.3249646);

   public static final Ellipsoid Australian_National = 
      new Ellipsoid("Australian_National", 6378160.0, 293.25);

   public static final Ellipsoid Bessel_1841_EthiopiaIndonesiaJapanKorea = 
      new Ellipsoid("Bessel_1841", 6377397.155, 299.1528128);

   public static final Ellipsoid Bessel_1841_Namibia = 
      new Ellipsoid("Bessel_1841", 6377483.865, 299.1528128);

   public static final Ellipsoid Clarke_1866 = 
      new Ellipsoid("Clarke_1866", 6378206.4, 294.9786982);

   public static final Ellipsoid Clarke_1880 = 
      new Ellipsoid("Clarke_1880", 6378249.145, 293.465);

   public static final Ellipsoid Everest_Brunei_E_Malaysia = 
      new Ellipsoid("Everest_Brunei_E_Malaysia", 6377278.566, 300.8017);

   public static final Ellipsoid Everest_1830_India = 
      new Ellipsoid("Everest_1830_India", 6377276.345, 300.8017);

   public static final Ellipsoid Everest_1948_W_Malaysia_Singapore = 
      new Ellipsoid("Everest_1948_W_Malaysia_Singapore", 6377304.063, 300.8017);

   public static final Ellipsoid GRS_1980 = 
      new Ellipsoid("Geodetic_Reference_System_1980", 6378137.0, 298.257222101);

   public static final Ellipsoid Helmert_1906 = 
      new Ellipsoid("Helmert_1906", 6378200.0, 298.3);

   public static final Ellipsoid Hough_1906 = 
      new Ellipsoid("Hough_1906", 6378270.0, 297.0);

   public static final Ellipsoid Indonesian_1974 = 
      new Ellipsoid("Indonesian_1974", 6378160.0, 298.247);

   public static final Ellipsoid International_1924 = 
      new Ellipsoid("International_1924", 6378388.0, 297.0);

   public static final Ellipsoid Krassovsky_1940 = 
      new Ellipsoid("Krassovsky_1940", 6378245.0, 298.3);

   public static final Ellipsoid Modified_Airy = 
      new Ellipsoid("Modified_Airy", 6377340.189, 299.3249646);

   public static final Ellipsoid Modified_Fischer_1960 = 
      new Ellipsoid("Modified_Fischer_1960", 6378155.0, 298.3);

   public static final Ellipsoid South_American_1969 = 
      new Ellipsoid("South_American_1969", 6378160.0, 293.25);

   public static final Ellipsoid WGS_72 = 
      new Ellipsoid("WGS_72", 6378135.0, 298.26);

   public static final Ellipsoid WGS_84 = 
      new Ellipsoid("WGS_84", 6378137.0, 298.257223563);

   /**
    * String representation of the ellipsoid equivalent to the ellipsoids name.
    */
   public String toString()
   {
      return ellipsoidName;
   }

   /**
    * Returns the named ellipsoid such that: Ellipsoid.toString().equals(name).
    * @throws EllipsoidInvalidNameException if the name doesn't map to an Ellipsoid.
    */
   public static Ellipsoid fromString(
      String name)
      throws EllipsoidInvalidNameException
   {
      Ellipsoid found = (Ellipsoid) map.get(name);
      if ( found == null )
         throw new EllipsoidInvalidNameException(name);
      return found;
   }

   /**
    * Returns a iteration of the enumeration values.
    * The class returned is Ellipsoid.
    * Order is guaranteed to be ascending.
    */
   public static Iterator iterator()
   {
       return map.values().iterator();
   }


   private Ellipsoid(
      String ellipsoidName, 
      double equatorialRadius, 
      double reciprocalOfFlattening)
   {
      this.ellipsoidName = ellipsoidName; 
      this.equatorialRadius = equatorialRadius; 

      this.reciprocalOfFlattening = reciprocalOfFlattening;
      this.flattening = 1.0/reciprocalOfFlattening;

      this.polarRadius = equatorialRadius - flattening*equatorialRadius;

      this.eccentricitySquared = 
         flattening*(2.0-flattening);

      this.secondEccentricitySquared = 
         eccentricitySquared/(1.0 - eccentricitySquared*eccentricitySquared);
   

      map.put(ellipsoidName, this);
   }

} 
