package fsats.measures;

import java.lang.Math;

/**
 * Converts Location (latitude/longitude) to and from UTMLocation (easting/northing) coordinates.
 * Equations are derived from NIMA publication DMATM 8358.2.  The document claims conversion
 * accuracy to the nearest .001 arc second for geographic coordinates and to the nearest
 * .01 meter for grid coordinates.
 *
 * NIMA's website on Geodesy and Geophysics is at http://164.214.2.59/GandG/pubs.html
 *
 * East longitudes are positive. West longitudes are negative. 
 * North latitudes are positive. South latitudes are negative.
 * Latitude and longitude are in decimal degrees.
 */
public class GeoUTMConversion
{
   /**
    * Converts Location (latitude/longitude) to UTMLocation (easting/northing) coordinates.
    * Converts into "native" gridzone.
    */
   public static UTMLocation LLtoUTM(
      Location loc)
   {
      return GeoUTMConversion.LLtoUTM(loc,
                                      UTMLocation.UTMZoneNumber(loc.longitude(),loc.latitude()));
   }

   /**
    * Converts Location (latitude/longitude) to UTMLocation (easting/northing) coordinates.
    * Forces conversion into passed gridzone.
    */
   public static UTMLocation LLtoUTM(
      Location loc,
      int zoneNumber)
   {
      UTMLocation result = UTMLocation.nullUTMLocation();

      if (!loc.isNull())
      {
         // NB: since these equations are long an attempt is made to use
         // "short" notation similiar to the referenced document.

         //*********************** ellipsoid parameters.
         // a = simi-major axis of the ellipsoid
         double a = loc.datum().ellipsoid.equatorialRadius;

         // e2 = first eccentricity squared
         double e2  = loc.datum().ellipsoid.eccentricitySquared;

         // ep2 = e prime squared = second eccentricity squared
         double ep2  = e2/(1.0 - e2);
         double ep4  = ep2*ep2;

         // f = flattening
         double f = loc.datum().ellipsoid.flattening;

         double n = f / (2.0 - f);
         double n2 = n*n;
         double n3 = n2*n;
         double n4 = n3*n;
         double n5 = n4*n;

         // A prime = Ap
         double Ap = a*(1.0 - n + (5.0/4.0)*(n2 - n3) + (81.0/64.0)*(n4 - n5));

         // B prime = Bp
         double Bp = (3.0/2.0)*a*(n - n2 + (7.0/8.0)*(n3 - n4) + (55.0/64.0)*n5);

         // C prime = Cp
         double Cp = (15.0/16.0)*a*(n2 - n3 + (3.0/4.0)*(n4 - n5));

         // D prime = Dp
         double Dp = (35.0/48.0)*a*(n3 - n4 + (11.0/16.0)*n5);

         // E prime = Ep
         double Ep = (315.0/512.0)*a*(n4 - n5);


         //***********************
         // latitude = phi (in radians)
         double phi = loc.latitude()*deg2rad;
         double sinPhi = Math.sin(phi);
         double sinPhi2 = sinPhi*sinPhi;
         double sin2Phi = Math.sin(2.0*phi);
         double sin4Phi = Math.sin(4.0*phi);
         double sin6Phi = Math.sin(6.0*phi);
         double sin8Phi = Math.sin(8.0*phi);
         double cosPhi = Math.cos(phi);
         double cosPhi2 = cosPhi*cosPhi;
         double cosPhi4 = cosPhi2*cosPhi2;
         double cosPhi6 = cosPhi4*cosPhi2;
         double tanPhi = Math.tan(phi);
         double tanPhi2 = tanPhi*tanPhi;

         //***********************
         // nu = v = radius of curvature in the prime vertical; 
         // also defined as the normal to the elliposid terminating at the minor axis.
         double v = a / Math.sqrt(1 - e2*sinPhi2);

         //False Northing = FN
         //0.0 for the northern hemisphere
         //10,000,000 meter offset for southern hemisphere
         double FN = (phi < 0) ? 10000000.0 : 0.0; 

         //***********************
         // longitude = lamda = L(in radians; where: -180.0 < lonRad < 179.9)
         //double L = ((loc.longitude()+180) - (int)((loc.longitude()+180)/360)*360-180) * deg2rad;
         double L = loc.longitude()*deg2rad;

         //False Easting = FE
         //500000 meters
         double FE = 500000.0;

         // lambda sub-zero = lamda-naught = L0
         double L0 = UTMLocation.longitudeOfCentralMeridian(zoneNumber)*deg2rad;
         
         // delta lambda = dL = difference of longitude from the central meridian
         // (value is sign dependent)
         double dL = L - L0;

         // delta lambda squared <=> dLSq
         double dLSq = dL*dL;


         //***********************
         // S = meridional arc, the true meridoional distance on the ellipsoid from the equator.
         double S=Ap*phi - Bp*sin2Phi + Cp*sin4Phi - Dp*sin6Phi + Ep*sin8Phi;

         double T1 = S*k0;

         double T2 = (v*sinPhi*cosPhi*k0)/2.0;

         double T3 = (T2*cosPhi2/12)*(5.0 - tanPhi2 + 9.0*ep2*cosPhi2 + 4.0*ep4*cosPhi4);

         double T4 = (T2*cosPhi4/360.0)*(
                      61.0 + tanPhi2*(
                              tanPhi2 - 58.0 - ep2*cosPhi2*(
                                                330.0 + ep2*cosPhi2*(
                                                        680.0 +  ep2*cosPhi2*(
                                                                  600.0 + 192.0*ep2*cosPhi2))))
                           + ep2*cosPhi2*(
                             270.0 + ep2*cosPhi2*(
                                     445.0 + ep2*cosPhi2*(
                                             324.0 + 88.0*ep2*cosPhi2))));

         double T5 = (T2*cosPhi6/20160.0)*(1385.0 - tanPhi2*(3111.0 + tanPhi2*(543 - tanPhi2)));

         // northing <=> N
         double N = FN + T1 + dLSq*(T2 + dLSq*(T3 + dLSq*(T4 + dLSq*T5)));



         //***********************
         double T6 = v*cosPhi*k0;

         double T7 = (T6*cosPhi2/6.0)*(1.0 - tanPhi2 + ep2*cosPhi2);

         double T8 = (T6*cosPhi4/120.0)*(
                      5.0 + tanPhi2*(
                              tanPhi2 - 18.0 - ep2*cosPhi2*(
                                                58.0 + ep2*cosPhi2*(
                                                        64.0 +  24.0*ep2*cosPhi2)))
                           + ep2*cosPhi2*(
                             14.0 + ep2*cosPhi2*(
                                     13.0 + 4.0*ep2*cosPhi2)));

         double T9 = (T6*cosPhi6/5040.0)*(61.0 - tanPhi2*(479.0 - tanPhi2*(179.0 - tanPhi2)));

         // easting <=> E
         double E = FE + dL*(T6 + dLSq*(T7 + dLSq*(T8 + dLSq*T9)));


         if (loc.isNullAltitude())
         {
            result = new UTMLocation(E, N, zoneNumber, loc.datum());
            //result = new UTMLocation(E, N, zoneNumber, UTMLocation.UTMZoneLetter(loc.latitude()), loc.datum());
         }
         else
         {
            result = new UTMLocation(E, N, loc.altitude(), zoneNumber, loc.datum());
         }
      }

      return result;
   }

   /**
    * Converts UTM coords to lat/long.  
    */
   public static Location UTMtoLL(
      UTMLocation utmLocation)
   {
      Location result = Location.nullLocation();

      if (!utmLocation.isNull())
      {
         // NB: since these equations are long an attempt is made to use
         // "short" notation similiar to the referenced document.

         //*********************** ellipsoid parameters.
         // a = simi-major axis of the ellipsoid
         double a = utmLocation.datum().ellipsoid.equatorialRadius;

         // e2 = first eccentricity squared
         double e2  = utmLocation.datum().ellipsoid.eccentricitySquared;

         // ep2 = e prime squared = second eccentricity squared
         double ep2  = e2/(1.0 - e2);
         double ep4  = ep2*ep2;

         //***********************
         // easting = E
         double E = utmLocation.easting(); 

         // delta E - value is sign dependent.
         double dE = E - 500000.0; 
         double dE2 = dE*dE;
         //System.out.println("UTMToGeo dE2="+dE2);

         //***********************
         //successive approximation to compute phiP based on term T1
         double phiP = GeoUTMConversion.getPhiP(utmLocation.datum().ellipsoid, utmLocation);

         //try computing T-terms with phiP
         double sinPhiP = Math.sin(phiP);
         double sinPhiP2 = sinPhiP*sinPhiP;
         double cosPhiP = Math.cos(phiP);
         double cosPhiP2 = cosPhiP*cosPhiP;
         double tanPhiP = Math.tan(phiP);
         double tanPhiP2 = tanPhiP*tanPhiP;

         // p = radius of curvature in the meridian
         double p = a*(1-e2)/((1-e2*sinPhiP2)*Math.sqrt(1.0 - e2*sinPhiP2));

         // nu = v = radius of curvature in the prime vertical; 
         // also defined as the normal to the elliposid terminating at the minor axis.
         double v = a / Math.sqrt(1.0 - e2*sinPhiP2);
         double v2 = v*v;
         double v4 = v2*v2;
         double v6 = v4*v2;

         double k02 = k0*k0;
         double k04 = k02*k02;
         double k06 = k04*k02;

         double T10 = tanPhiP/(2.0*p*v*k02);

         double T11 = (T10/(12.0*v2*k02))*(
                         5.0 + 
                         3.0*tanPhiP2 + 
                         cosPhiP2*ep2*(
                            1.0 - 
                            4.0*ep2*cosPhiP2 - 
                            9.0*tanPhiP2));

         double T12 = (T10/(360.0*v4*k04))*(
                         61.0 +
                         tanPhiP2*(90.0 + 45.0*tanPhiP2) +
                         ep2*cosPhiP2*(46.0 - tanPhiP2*(252.0 + 90.0*tanPhiP2) + 
                            ep2*cosPhiP2*(-3.0 + tanPhiP2*(-66.0 + 225.0*tanPhiP2) +
                               ep2*cosPhiP2*(100.0 + 84.0*tanPhiP2 +
                                  ep2*cosPhiP2*(88.0 - 192.0*tanPhiP2)))));


         double T13 = (T10/(20160.0*v6*k06))*
                      (1385.0 + tanPhiP2*(3633.0 + tanPhiP2*(4095.0 + 1575.0*tanPhiP2)));

         double T14 = 1.0/(v*cosPhiP*k0);

         double T15 = (T14/(6.0*v2*k02))*(1.0 + 2.0*tanPhiP2 + ep2*cosPhiP2);

         double T16 = (T14/(120.0*v4*k04))*
                      (5.0 + 4.0*tanPhiP2*(
                             7.0 + 6.0*tanPhiP2 + ep2*cosPhiP2*(2.0 + ep2*cosPhiP2*(1.0 + 6.0*ep2*cosPhiP2)))
                           + ep2*cosPhiP2*(6.0 - ep2*cosPhiP2*(3.0 + 4.0*ep2*cosPhiP2)));

         double T17 = (T14/(5040.0*v6*k06))*(61.0 + tanPhiP2*(662.0 + tanPhiP2*(1320.0 + tanPhiP2*720.0)));



         //***********************
         // latitude = phi (in radians)
         double phi = phiP - dE2*(T10 - dE2*(T11 - dE2*(T12 - dE2*T13)));

         
         //***********************
         // lambda sub-zero = lamda-naught = L0
         double L0 = UTMLocation.longitudeOfCentralMeridian(utmLocation.zoneNumber())*deg2rad;
      
         // longitude = lamda = L(in radians; where: -180.0 < lonRad < 179.9)
         double L = L0 + dE*(T14 - dE2*(T15 - dE2*(T16 - dE2*T17)));



         if (utmLocation.isNullAltitude())
         {
            result =  new Location(phi*rad2deg,L*rad2deg,utmLocation.datum());
         }
         else
         {
            result =  new Location(phi*rad2deg,L*rad2deg,utmLocation.altitude(),utmLocation.datum());
         }
      }

      return result;

   }


   public static void main(String args[])
   {
      Location testGeoToUTM[] = 
         {
            new Location(73.0, 45.0, Datum.European_1950_Europe_Mean_Solution),
            new Location(30.0, 102.0, Datum.European_1950_Europe_Mean_Solution),
            new Location((72.0+(4.0/60.0)+(32.110/3600.0)), 
                        -(113.0+(54.0/60.0)+(43.321/3600.0)), 
                         Datum.European_1950_Europe_Mean_Solution)
      
         };

      UTMLocation testUTMToGeo[] = 
         {
            new UTMLocation(210577.93, 3322824.35, 48, Datum.European_1950_Europe_Mean_Solution),
            new UTMLocation(789411.59, 3322824.08, 47, Datum.European_1950_Europe_Mean_Solution),
            new UTMLocation(200000.00, 1000000.00, 31, Datum.European_1950_Europe_Mean_Solution),
            new UTMLocation(859739.88, 1000491.75, 30, Datum.European_1950_Europe_Mean_Solution),
            new UTMLocation(500000.00, 9000000.00, 43, Datum.European_1950_Europe_Mean_Solution),
            new UTMLocation(700000.00, 4000000.00, -30, Datum.European_1950_Europe_Mean_Solution),
            new UTMLocation(307758.89, 4000329.42, -31, Datum.European_1950_Europe_Mean_Solution)
         };

      System.out.println("TESTING GEO TO UTM");
      for (int i=0; i<testGeoToUTM.length; i++)
      {
         System.out.println("Ith location: " + i);
         System.out.println("Start : " + testGeoToUTM[i].toString());

         UTMLocation utm = GeoUTMConversion.LLtoUTM(testGeoToUTM[i]);
         System.out.println(" Calc : " + utm.toString());

         Location loc = GeoUTMConversion.UTMtoLL(utm);
         System.out.println(" Calc : "+loc.toString());

         System.out.println("");
      }

      System.out.println("TESTING UTM TO GEO");
      for (int i=0; i<testUTMToGeo.length; i++)
      {
         System.out.println("Ith location: " + i);
         System.out.println("Start : " + testUTMToGeo[i].toString());

         Location ll = GeoUTMConversion.UTMtoLL(testUTMToGeo[i]);
         System.out.println(" Calc : " + ll.toString());
         System.out.println(" Calc : " + ll.toDMS());

         UTMLocation utm = GeoUTMConversion.LLtoUTM(ll);
         System.out.println(" Calc : "+utm.toString());

         System.out.println("");
      }
   }

   private static double getPhiP(
      fsats.measures.Ellipsoid ellipsoid,
      UTMLocation utmLoc)
   {
      double phiPresult = 0.0;

      double epsilon = 0.000000001;

      //Need to fix this to handle negative GZ numbers as
      //indicators of southern hemisphere.
      double fN = 0.0;
      //if ( ((int)(utmLoc.zoneLetter()) - (int)'N') < 0 )
      if ( utmLoc.zoneNumber() < 0 )
      {
         //point is in southern hemisphere
         //remove 10,000,000 meter offset used for southern hemisphere
         fN = 10000000.0;
      }
      double dN = utmLoc.northing() - fN;
      //System.out.println("getPhiP dN="+dN+" meters");
      //delta N corrected for scale...
      dN = dN/k0;
      //System.out.println("getPhiP scaled dN="+dN+" meters");


      // f = flattening
      double f = ellipsoid.flattening;

      // a = simi-major axis of the ellipsoid
      double a = ellipsoid.equatorialRadius;

      double n = f / (2.0 - f);
      double n2 = n*n;
      double n3 = n2*n;
      double n4 = n3*n;
      double n5 = n4*n;

      // A prime = Ap
      double Ap = a*(1.0 - n + (5.0/4.0)*(n2 - n3) + (81.0/64.0)*(n4 - n5));

      // B prime = Bp
      double Bp = (3.0/2.0)*a*(n - n2 + (7.0/8.0)*(n3 - n4) + (55.0/64.0)*n5);

      // C prime = Cp
      double Cp = (15.0/16.0)*a*(n2 - n3 + (3.0/4.0)*(n4 - n5));

      // D prime = Dp
      double Dp = (35.0/48.0)*a*(n3 - n4 + (11.0/16.0)*n5);

      // E prime = Ep
      double Ep = (315.0/512.0)*a*(n4 - n5);


      //initial estimate for phi prime
      double phiPest = dN/Ap;
      //System.out.println("getPhiP initialEstimate="+phiPest*rad2deg);

      while ( phiPresult == 0.0 )
      {
         double sin2PhiPest = Math.sin(2.0*phiPest);
         double sin4PhiPest = Math.sin(4.0*phiPest);
         double sin6PhiPest = Math.sin(6.0*phiPest);
         double sin8PhiPest = Math.sin(8.0*phiPest);

         // S = meridional arc, the true meridoional distance on the ellipsoid from the equator.
         double Sp = Ap*phiPest - Bp*sin2PhiPest + Cp*sin4PhiPest - Dp*sin6PhiPest + Ep*sin8PhiPest;
         //System.out.println("Sp ="+Sp);

         double dNS = dN - Sp;
      
         if (Math.abs(dNS) > epsilon)
         {
            phiPest = phiPest + (dNS/Ap);
            //System.out.println("phiPest="+phiPest*rad2deg);
         }
         else
         {
            phiPresult = phiPest;
         }
      }

      //System.out.println("getPhiP phiPresult="+phiPresult*rad2deg);
      return phiPresult;
   }

   private static final double pi = Math.PI;
   private static final double deg2rad = pi/180.0;
   private static final double rad2deg = 180.0/pi;

   // cnetral scale factor, an arbitrary reduction applied to all geodetic lengths to
   // to reduce the maximum scale distortion of the projection.
   // for the U.T.M., k0 = 0.9996
   private static final double k0 = 0.9996;
}
