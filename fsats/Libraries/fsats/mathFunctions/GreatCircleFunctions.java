package fsats.mathFunctions;

import java.lang.Math;
import java.lang.Double;
import fsats.measures.Location;

/**
 * Static functions which perform
 *  great circle calculations and
 *  relevent unit conversions
 *  (degres<->radians, NMI<->meters etc).
 */

public class GreatCircleFunctions
{
   /** 
    * Converts angles in degrees to radians.
    */
   public static double angleDegreesToRadians(
      double angleDegrees)
   {
       return (pi/180.0)*angleDegrees;
   }
   
   /** 
    * Converts angles in radians to degrees.
    */
   public static double angleRadiansToDegrees(
      double angleRadians)
   {
      return (180.0/pi)*angleRadians;
   }

   /** 
    * Converts meters to nautical miles.
    * 1 nmi = 1852 meters.
    */
   public static double metersToNMi(
      double meters)
   {
      return meters/1852.0;
   }

   /** 
    * Converts nautical miles to meters.
    * 1 nmi = 1852 meters.
    */
   public static double NMiToMeters(
      double nmi)
   {
      return nmi*1852.0;
   }

   /** 
    * Converts distance in meters to radians.
    */
   public static double distanceMToRadians(
      double distM)
   {
      return distanceNMiToRadians(metersToNMi(distM));
   }
   
   /** 
    * Converts distance in radians to meters.
    */
   public static double distanceRadiansToM(
      double distRadians)
   {
      return NMiToMeters(distanceRadiansToNMi(distRadians));
   }
 
   /** 
    * Converts distance in nautical miles to radians.
    */
   public static double distanceNMiToRadians(
      double distNMi)
   {
      return (pi/(180.0*60.0) ) * distNMi;
   }
   
   /** 
    * Converts distance in radians to nautical miles.
    */
   public static double distanceRadiansToNMi(
      double distRadians)
   {
      return ((180.0*60.0)/pi) * distRadians;
   }
 
   /**
    * Finds a location (lat,lon) relative to the origin given
    * a true course radial in degrees and a distance in meters.
    *
    * True course (tc) is defined as the angle between the course line 
    * and the local meridian measured clockwise (ie. east of north).
    *
    * Method assumes limited distance such that dlon<p/2.
    * i.e. those that extend around less than one quarter of the
    * circumference of the earth in longitude.
    */

   /* A point {lat,lon} is a distance d out on the tc radial from point 1 if: 
    *
    * (comments assume postive west longitude - 
    *  code written for postive east longitude)
    *     lat=asin(sin(lat1)*cos(d)+cos(lat1)*sin(d)*cos(tc))
    *     IF (cos(lat)=0)
    *        lon=lon1      // endpoint a pole
    *     ELSE
    *        lon=mod(lon1-asin(sin(tc)*sin(d)/cos(lat))+pi,2*pi)-pi
    *     ENDIF
    *
    *FYI - but not implemented!
    * A completely general, but more complicated algorithm is necessary
    * if greater distances are allowed: 
    *
    *     lat =asin(sin(lat1)*cos(d)+cos(lat1)*sin(d)*cos(tc))
    *     dlon=atan2(sin(tc)*sin(d)*cos(lat1),cos(d)-sin(lat1)*sin(lat))
    *     lon=mod( lon1-dlon +pi,2*pi )-pi
    */
   public static Location locationGivenRadialAndDistance(
      Location origin,
      double tcRadial,    //degrees
      double distance)    //meters
   {
      double lat1 = angleDegreesToRadians(origin.latitude());
      double lon1 = angleDegreesToRadians(origin.longitude());
      double d = distanceMToRadians(distance);
      double tc = angleDegreesToRadians(tcRadial);

      double lat = Math.asin( (Math.sin(lat1)*Math.cos(d)) + 
                              (Math.cos(lat1)*Math.sin(d)*Math.cos(tc)) );
      //pwlon - pos. west lon
      //switch sign on input lon 
      double pwlon = -lon1;    //endpoint a pole if (Math.cos(lat)==0)

/*was...
      if (Math.cos(lat)!=0)
      {
         pwlon = ( (lon1 - 
                  Math.asin(Math.sin(tc)*Math.sin(d)/Math.cos(lat)) + 
                  pi) 
                      % (2*pi) ) - pi;
... need to switch sign on input lon 
    since location now in right hand coordinate system too!
        pwlon = ( ( (-lon1) - 
                  Math.asin(Math.sin(tc)*Math.sin(d)/Math.cos(lat)) + 
                  pi) 
                      % (2*pi) ) - pi;
      }
*/

      //pwlon - pos. west lon
      //to change formula need to "multiply thru by -1"
      double pelon = lon1;    //endpoint a pole if (Math.cos(lat)==0)
      if (Math.cos(lat)!=0)
         pelon = ( ( lon1 + 
                  Math.asin(Math.sin(tc)*Math.sin(d)/Math.cos(lat)) - 
                  pi) 
                      % (2*pi) ) + pi;

/*
      System.out.println("pwlon="+pwlon + " pelon="+pelon ) ;
      System.out.println("-lon1-pwlon="+(-lon1-pwlon)+
                         " lon1-pelon="+(lon1-pelon) );
*/
      return new Location(angleRadiansToDegrees(lat), 
                          angleRadiansToDegrees(pelon));
   }

   /**Computes the I-th out of N equally spaced locations along the line
    * from loc1 to loc2. 
    */
   public static Location locationOnLine(
      Location loc1,
      Location loc2,
      int I,
      int N)
   {
      double h = heading(loc1, loc2);
      double dist = distance(loc1, loc2);

      double dI = 0;       // ith distance
      Location lI = null;  // ith location

      // I must be strictly greater than 0 
      // and N must be greater than or equal to I
      if ((N<I) || (I<=0)) //I must be strictly greater than 0 
      {
         //throw an exception...
      }
      if (N==1)
      {
         //then the only possiblity is that I is also equal to 1
         //if I is not equal to 1 the above should have caught it!
         //so divide the dist in half ...  dI = dist/2.0;
         lI = locationGivenRadialAndDistance(loc1, h, dist/2.0);
      }
      else if (I==1)
      {
         //dI = 0.0;
         lI = loc1;
      }
      else if (I==N)
      {
         //dI = dist;
         lI = loc2;
      }
      else 
      {
         //no problem with divide by zero here - see above if (N==1)
         dI = (dist/(N-1))*(I-1);
         lI = locationGivenRadialAndDistance(loc1, h, dI);
      }

      return lI;
   }

   /** Compute the heading from loc1 to loc2 in meters.
    * ie. the true course from loc1 to loc2.
    */
   public static double heading(
      Location loc1, 
      Location loc2) 
   {
      /**
       * (comments assume postive west longitude - 
       *  code written for postive east longitude)
       *  See: http://www.best.com/~williams/avform.htm
       *
       * Starting at at the poles...
       * IF (cos(lat1) < EPS)   // EPS a small number ~ machine precision
       *   IF (lat1 > 0)
       *      tc1= pi           //  starting from N pole
       *   ELSE
       *      tc1= 0            //  starting from S pole
       *   ENDIF
       * ENDIF
       *
       * For starting points other than the poles: 
       * 
       * IF sin(lon2-lon1)<0       
       *    tc1=acos((sin(lat2)-sin(lat1)*cos(d))/(sin(d)*cos(lat1)))    
       * ELSE       
       *    tc1=2*pi-acos((sin(lat2)-sin(lat1)*cos(d))/(sin(d)*cos(lat1)))    
       * ENDIF 
       */

      double lat1 = angleDegreesToRadians(loc1.latitude());
      double lat2 = angleDegreesToRadians(loc2.latitude());

      double tc1 = 0.0;

      if (Math.cos(lat1) < Double.MIN_VALUE)
      {
         if (lat1 > 0)
            tc1 = pi;
         else
            tc1 = 0.0;
      }
      else
      {
         double lon1 = angleDegreesToRadians(loc1.longitude()); 
         double lon2 = angleDegreesToRadians(loc2.longitude());
         double d = distanceMToRadians(distance(loc1, loc2));

         tc1 = Math.acos( (Math.sin(lat2) - Math.sin(lat1)*Math.cos(d)) /
                          (Math.sin(d)*Math.cos(lat1)) );

         //just switch inequality
         if ( Math.sin(lon2-lon1) < 0 )
            tc1 = 2*pi - tc1;
      }
      return angleRadiansToDegrees(tc1);
   }

   /** computes the distance from location loc1 to location loc2.
    */
   public static double distance(
      Location loc1,
      Location loc2)
   {
      /* Signs don't matter when computing distance!
       *
       *See: http://www.best.com/~williams/avform.htm
       * this page assumes postive west longitude - 
       *Haversine Formula ... less subject to rounding error ...
       *     d=2*asin(sqrt((sin((lat1-lat2)/2))^2 + 
       *                    cos(lat1)*cos(lat2)*(sin((lon1-lon2)/2))^2))
       *
       * or...
       * See: http://www.gxtc.edu.cn/gisfaq51.html
       *   this page assumes positive east longitude -
       *      the "normal" mathematical convention
       *   dlon = lon2 - lon1 
       *   dlat = lat2 - lat1 
       *   a = sin^2(dlat/2) + cos(lat1) * cos(lat2) * sin^2(dlon/2) 
       *   c = 2 * arcsin(sqrt(a)) 
       *   d = R * c 
       */

      double lat1 = angleDegreesToRadians(loc1.latitude());
      double lat2 = angleDegreesToRadians(loc2.latitude());
      double dlat = lat1 - lat2;

      double dlon = angleDegreesToRadians(loc1.longitude()) - 
                    angleDegreesToRadians(loc2.longitude());
    
      double sinSqLat = Math.pow(Math.sin(dlat/2.0), 2);
      double sinSqLon = Math.pow(Math.sin(dlon/2.0), 2);

      double a = sinSqLat + Math.cos(lat1)*Math.cos(lat2)*sinSqLon;

      return distanceRadiansToM(2.0 * Math.asin(Math.sqrt(a)));
   }

   private static final double pi = Math.PI;

   public static void main(
      String args[])
   {
      //Location origin = new Location(33.95, 118.4);
      Location origin = new Location(33.95, -118.4);
      double radial = 66.0;
      double dist = GreatCircleFunctions.NMiToMeters(100.0);

      Location l = GreatCircleFunctions.locationGivenRadialAndDistance(
         origin, radial, dist);
      System.out.println("test1 input: latitude="+origin.latitude()+
                         " longitude="+origin.longitude() +
                         " radial="+radial +
                         " distance="+dist);
      System.out.println("test1 output: latitude="+l.latitude()+
                         " longitude="+l.longitude());


      System.out.println("test distance formula");
      double d1 = GreatCircleFunctions.distance(origin, l);
      Location origin2 = new Location(33.95, 118.4);
      Location l2 = new Location(l.latitude(), -l.longitude());
      double d2 = GreatCircleFunctions.distance(origin2, l2);
      System.out.println("d1="+d1+" d2="+d2);

      System.out.println("test heading formula");
      double h1_2 = GreatCircleFunctions.heading(origin,l);
      double h2_1 = GreatCircleFunctions.heading(l,origin);
      System.out.println("h1_2="+h1_2+" h2_1="+h2_1);
      
   }
}
