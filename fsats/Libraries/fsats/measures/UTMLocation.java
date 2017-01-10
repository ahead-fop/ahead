package fsats.measures;

import fsats.util.DataContainer;

/**
 * A UTM Location type - Easting, Northing, Gridzone and Altitude in meters..
 *
 * UTM coordinate system convention is that:
 *  1. Grid zone numbers are positive for the northern 
 *     and negative for the southern hemisphere.
 *  2. The default value for an "unset" Altitude is 0 meters.
 *
 * (UTMLocation no longer contains a gridzone letter.  It was never used
 *  and so has been removed.  The static UTMZoneLetter method remains just
 *  in case.)
 */
public class UTMLocation
{
//=====================
//Public Static methods
//=====================
   /**
    * The longitude of the central meridian of the gridzone in degrees.
    *
    * @param zoneNumber the UTM gridzone number.
    * @return the central meridian of the gridzone in degrees.
    */
   public static double longitudeOfCentralMeridian(
      int zoneNumber)
   {
      double cm = 0.0;
      // longitude of the origin 
      // (the central meridian of the projection)
      // +3 puts origin in middle of zone
      if (zoneNumber != Integer.MIN_VALUE)
      {
         cm = ((double)((Math.abs(zoneNumber) - 1)*6 - 180 + 3));
      }
      return cm;
   }

   /**
    * Calculates UTM zone number of the passed point.  Note that the
    * zone number is negative if the latitude is in the southern
    * hemisphere and postive if in the northern hemisphere.
    *
    * @param loc the Location of the point
    * @return zoneNumber the UTM Zone number
    */
   public static int UTMZoneNumber(
      Location loc)
   {
      return UTMLocation.UTMZoneNumber(loc.longitude(), loc.latitude());
   }
 
   /**
    * The UTM zone number of the passed point.  Note that the
    * zone number is negative if the latitude is in the southern
    * hemisphere and postive if in the northern hemisphere.
    *
    * @param longitude the longitude of the point
    * @param latitude the latitude of the point
    * @return zoneNumber the UTM Zone number
    */
   public static int UTMZoneNumber(
      double longitude,
      double latitude)
   {
      int zoneNumber = (int)((longitude + 180)/6) + 1;

      if( latitude >= 56.0 && latitude < 64.0 && longitude >= 3.0 && longitude < 12.0 )
         zoneNumber = 32;

      // Special zones for Svalbard
      if ( latitude >= 72.0 && latitude < 84.0 )
      {
         if (      longitude >= 0.0  && longitude <  9.0 ) zoneNumber = 31;
         else if ( longitude >= 9.0  && longitude < 21.0 ) zoneNumber = 33;
         else if ( longitude >= 21.0 && longitude < 33.0 ) zoneNumber = 35;
         else if ( longitude >= 33.0 && longitude < 42.0 ) zoneNumber = 37;
      }
      return (latitude < 0.0) ? (- zoneNumber) : zoneNumber;
   }

   /**
    * Determines the correct UTM letter designator for the given latitude.
    * Returns 'Z' if latitude is outside the UTM limits of 84N to 80S.
    *
    * @param latitude the latitude at which to determine the UTM zone letter
    * @return the UTM zone letter
    */
   public static char UTMZoneLetter(
      double latitude)
   {
      //
      //Written by Chuck Gantz- chuck.gantz@globalstar.com
      //source code downloaded from http://www.gpsy.com/gpsinfo/geotoutm/
      //
      char LetterDesignator;

      if((84 >= latitude) && (latitude>= 72)) LetterDesignator = 'X';
      else if((72 > latitude) && (latitude>= 64)) LetterDesignator = 'W';
      else if((64 > latitude) && (latitude>= 56)) LetterDesignator = 'V';
      else if((56 > latitude) && (latitude>= 48)) LetterDesignator = 'U';
      else if((48 > latitude) && (latitude>= 40)) LetterDesignator = 'T';
      else if((40 > latitude) && (latitude>= 32)) LetterDesignator = 'S';
      else if((32 > latitude) && (latitude>= 24)) LetterDesignator = 'R';
      else if((24 > latitude) && (latitude>= 16)) LetterDesignator = 'Q';
      else if((16 > latitude) && (latitude>= 8)) LetterDesignator = 'P';
      else if(( 8 > latitude) && (latitude>= 0)) LetterDesignator = 'N';
      else if(( 0 > latitude) && (latitude>= -8)) LetterDesignator = 'M';
      else if((-8> latitude) && (latitude>= -16)) LetterDesignator = 'L';
      else if((-16 > latitude) && (latitude>= -24)) LetterDesignator = 'K';
      else if((-24 > latitude) && (latitude>= -32)) LetterDesignator = 'J';
      else if((-32 > latitude) && (latitude>= -40)) LetterDesignator = 'H';
      else if((-40 > latitude) && (latitude>= -48)) LetterDesignator = 'G';
      else if((-48 > latitude) && (latitude>= -56)) LetterDesignator = 'F';
      else if((-56 > latitude) && (latitude>= -64)) LetterDesignator = 'E';
      else if((-64 > latitude) && (latitude>= -72)) LetterDesignator = 'D';
      else if((-72 > latitude) && (latitude>= -80)) LetterDesignator = 'C';
      //This is here as an error flag to show that the
      //Latitude is outside the UTM limits
      else LetterDesignator = 'Z';

      return LetterDesignator;
   }

   /**
    * Return a null Location;
    */
   public static UTMLocation nullUTMLocation()
   {
      return nullUTMLocation;
   }

//=================
//Public methods
//=================
   /**
    * Test for null.
    * 
    * @return true if "null" UTMLocation, false otherwise
    */
   public boolean isNull()
   {
      return false;
   }
   
   
   /**
    * The northing.
    *
    * @return the UTMLocation's northing
    */
   public double northing()
   {
      return n;
   }
   
   /**
    * The easting
    *
    * @return the UTMLocation's easting
    */
   public double easting()
   {
      return e;
   }

   /**
    * Test for null altitude.
    *
    * @return true if the UTMLocation was constructed without an altitude,
    *         and false if constructed with an altitude.
    */
   public boolean isNullAltitude()
   {
      return nullAlt;
   }

   /**
    * The altitude. If the UTMLocation was constructed without an
    * altitude the value will default to 0.
    *
    * @return the UTMLocation's altitude
    *
    */
   public double altitude()
   {
      return a;
   }

   /**
    * The zone number. Zone number is positive if in the northern hemisphere
    * and negitive if in the southern hemisphere.
    *
    * @return the UTMLocation zoneNumber
    */
   public int zoneNumber()
   {
      return zn;
   }

   /* OBE
    * The zone letter
    *
    * @return the UTMLocation's zoneLetter
   public char zoneLetter()
   {
      return zl;
   }
    */

   /**
    * The datum. Default datum is WGS_84.
    *
    * @return the UTMLocation's datum
    */
   public Datum datum()
   {
      return d;
   }
   /**
    * Returns this utmlocatin as a data container.
    * The datum is not currently written out.
    */
   public DataContainer toContainer()
   {
       DataContainer cont = new DataContainer( "UTM_LOCATION" );

       Double d = new Double( e );
       cont.addField( new DataContainer( "EASTING", d.toString() ) );

       d = new Double( n );
       cont.addField( new DataContainer( "NORTHING", d.toString() ) );

       if (!isNullAltitude())
       {
          d = new Double( a );
          cont.addField( new DataContainer( "ALTITUDE", d.toString() ) );
       }

       cont.addField( new DataContainer( "GRID_ZONE", "" + zn ) );

       /* OBE
       StringBuffer buf = new StringBuffer();
       buf.append( zl );
       cont.addField( new DataContainer( "GRID_ZONE_LETTER", buf.toString()) );
       */

       return cont;
   }



   /**
    * Creates a UTMLocation from a container representation.
    * On error return NullUTMLocation.
    * @see toContainer
    *
    * @param a DataContainer to parse
    * @return the UTMLocation
    */    
   public static UTMLocation create( 
      DataContainer container )
   {
       UTMLocation utm = UTMLocation.nullUTMLocation();

       String easting = container.getField( "EASTING" ).getValue();
       String northing = container.getField( "NORTHING" ).getValue();
       String altitude = container.getField( "ALTITUDE" ).getValue();
       String gridzone = container.getMultilevelField( "GRID_ZONE" ).getValue();

       /* OBE
       String gridzoneLetter = container.getField( "GRID_ZONE_LETTER" ).getValue();
       */

       try 
       {
           double e = Double.parseDouble( easting );
           double n = Double.parseDouble( northing );

           int gz = Integer.parseInt( gridzone );

           /* OBE
           char gzl = gridzoneLetter.charAt( 0 );
           */
        
           if (altitude==null || altitude.equals(""))
           {
              /* OBE
              utm = new UTMLocation( e, n, gz, gzl );
              */
              utm = new UTMLocation( e, n, gz );
           }
           else
           {
              double a = Double.parseDouble( altitude );

              /* OBE
              utm = new UTMLocation( e, n, a, gz, gzl );
              */
              utm = new UTMLocation( e, n, a, gz );
           }
       } 
       catch( NumberFormatException e ) 
       {
           e.printStackTrace();
           return nullUTMLocation;
       }

       return utm;
   }


   /**
    * Computes the geographic Location of this UTMLocation.
    *
    * @return Location the geographic Location
    */
   public Location inGeo()
   {
      return GeoUTMConversion.UTMtoLL(this);
   }


   /**
    * Returns the UTM location relative to the passed datum.
    *
    * @param datum the datum to shift the location to
    *
    * @return UTMLocation the datum shifted UTMLocation
    */
   public UTMLocation shiftToDatum(
      Datum datum)
   {
      if (d.equals(datum))
      {
         return this;
      }
      else
      {
         //return this.d.shiftTo(datum,this);
         return this.inGeo().shiftToDatum(datum).inUTM();
      }
   }



   /** Returns a string representation of the location as the ordered tuple
    *  (northing,easting,altitude,gridzone,datum).
    *
    *  @return String the UTMLocation in the above format.
    */
   public String toString()
   {
      /* OBE
      return "(E,N,A,GZ,Datum) (" +e+","+n+","+a+","+zn+zl+","+d.datumName+")";
      */
      return "(E,N,A,GZ,Datum) (" +e+","+n+","+a+","+zn+","+d.datumName+")";
   }


   
   /**
    * Constructs a newly allocated UTMLocation object with the given
    * easting, northing and zoneNumber.
    *
    * The UTMLocation's altitude value defaults to 0.0 and isNullAlt returns true.
    * The UTMLocations's datum defaults to WGS_84.
    */
   public UTMLocation(
      double easting,
      double northing,
      int zoneNumber)
   {
      this(easting,northing,nullAltValue,zoneNumber,Datum.WGS_84);
      nullAlt = true;
   }

   /**
    * Constructs a newly allocated UTMLocation object with the given
    * easting, northing, altitude and zoneNumber.
    *
    * The UTMLocations's datum defaults to WGS_84.
    */
   public UTMLocation(
      double easting,
      double northing,
      double altitude,
      int zoneNumber)
   {
      this(easting,northing,altitude,zoneNumber,Datum.WGS_84);
   }

   /**
    * Constructs a newly allocated UTMLocation object with the given
    * easting, northing and zoneNumber and datum.
    *
    * The UTMLocation's altitude value defaults to 0.0 and isNullAlt returns true.
    */
   public UTMLocation(
      double easting,
      double northing,
      int zoneNumber,
      Datum datum)
   {
      this(easting,northing,nullAltValue,zoneNumber,datum);
      nullAlt = true;
   }


   public UTMLocation(
      double easting,
      double northing,
      double altitude,
      int zoneNumber,
      Datum datum)
   {
      this.e = easting;
      this.n = northing;
      this.a = altitude;
      this.zn = zoneNumber;
      this.d = datum;
   }

   /* OBE
    * The datum default is WGS_84.
   public UTMLocation(
      double easting,
      double northing,
      int zoneNumber,
      char zoneLetter)
   {
      this(easting,northing,zoneNumber,zoneLetter,Datum.WGS_84);
   }

   public UTMLocation(
      double easting,
      double northing,
      int zoneNumber,
      char zoneLetter,
      Datum datum)
   {
      this.e = easting;
      this.n = northing;
      this.zn = zoneNumber;
      this.zl = zoneLetter;
      this.d = datum;
   }
    */

    
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj==null || !(obj instanceof UTMLocation) )
            return false;
        UTMLocation loc2 = (UTMLocation) obj;
        return ( e == loc2.e
                 && n == loc2.n
                 && a == loc2.a
                 && zn == loc2.zn
                 && (d==null? loc2.d==null: d.equals( loc2.d ) ) );
    }


   //0.0 is the "hasn't been set" value for altitudes.
   private static final double nullAltValue = 0.0;
   
   //Double.MIN_VALUE is the "hasn't been set" value for easting and northing.
   private double e = Double.MIN_VALUE;
   private double n = Double.MIN_VALUE;
   private double a = nullAltValue;

   private boolean nullAlt = false;

   //Integer.MAX_VALUE is the "hasn't been set" value for zone number.
   //Thus the hemisphere will default to northern.
   private int zn = Integer.MAX_VALUE;

   /* OBE
   // 'Z' is the "hasn't been set" value for the zone letter.
   //Thus the hemisphere will default to northern.
   private char zl = 'Z';
   */

   private Datum d = Datum.WGS_84;
   
   private static UTMLocation nullUTMLocation = new NullUTMLocation();

   public static void main(String[] args)
   {
      UTMLocation u = new UTMLocation( 1, 2, 3, 4 );
      System.out.println( u );
      DataContainer cont = u.toContainer();
      UTMLocation u1 = UTMLocation.create( cont );
      System.out.println( u1 );
   }
}

class NullUTMLocation
   extends UTMLocation
{
   NullUTMLocation()
   {
      //... was super(-1.0, -1.0, -1, 'Z');
      super(Double.MIN_VALUE, Double.MIN_VALUE, Integer.MIN_VALUE);
   }
   public boolean isNull()
   {
      return true;
   }
}
  
