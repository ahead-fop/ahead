//                              -*- Mode: Java -*- 
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett

package fsats.util;

import java.util.*;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.text.SimpleDateFormat;


/**
 * This is a utility class for converting dates to and from string
 * representations.
 *
 * Two formats are defined.  One format follows that of the Ada package
 * time-ascii.  This is the format used to send date strings in Doe
 * messages.  The other format is for displaying a date in a UI.
 */
public class DateUtil
{
    /**
     * standard format used by fsats for passing Dates via Doe messages
     */
    public static final String DOE_FORMAT = "dd-MMM-yyyy HH:mm:ss.SSS";

    /**
     * standard long format for displaying dates
     */
    public static final String LONG_FORMAT = "MMM dd, yyyy  HH:mm:ss";
    
    /**
     * standard format for displaying a date where the important part is
     * the time and the date is secondary.
     */
    public static final String SHORT_FORMAT = "MMM dd, HH:mm:ss";

    
    public static final SimpleTimeZone gmtTimeZone;


    private static final SimpleDateFormat doeFormatter =
    new SimpleDateFormat( DOE_FORMAT );

    private static final SimpleDateFormat longFormatter =
    new SimpleDateFormat( LONG_FORMAT );

    private static final SimpleDateFormat shortFormatter =
    new SimpleDateFormat( SHORT_FORMAT );

    private static final Calendar calendar = new GregorianCalendar();

    // # millisecs between gmt and the local time
    private static final int utcOffset;


    
    // initialize those static vars which can't be initialized at declaration
    static
    {
        // using TimeZone.getTimeZone( "GMT" ) when converting a date to a
        // string and back resulted in the ending date off by one hour from
        // what i started with.  there is something i don't understand about
        // daylight savings time and the default "GMT" time zone.  
        // as a work-around, i explicitly set the timezone offset using the
        // fsats env var FSATS_UTC_OFFSET.
        // hopefully, this can be cleaned up later.        

        int localOffset = TimeZone.getDefault().getRawOffset();
        
        try {
            String strOffset =
                FsatsProperties.get( FsatsProperties.FSATS_UTC_OFFSET,
                                     "" + localOffset );
            localOffset = Integer.parseInt( strOffset );
            
            // convert to milliseconds
            localOffset *= 3600000;
        } catch( Throwable t ) {
            t.printStackTrace();
        }
        
        utcOffset = localOffset;

        gmtTimeZone = new SimpleTimeZone( utcOffset, "GMT" );
    }

    


    private DateUtil()
    {}

    


    
    /**
     * Return a string representation of a Date.
     * The format is that used by the Ada package time_ascii.
     * This is the format used to pass dates in Doe messages.
     * The format is dd-mmm-yyyy hh:mm:ss.xxx
     * The TimeZone is normally either gmtTimeZone or the default timezone.
     *
     * @param date - the Date to be formatted
     * @param tz - the timezone used in the format operation
     */
    public synchronized static String toDoeFormat( Date d, TimeZone tz )
    {
        doeFormatter.setTimeZone( tz );
        String s = doeFormatter.format( d );
        return s;
    }




    /**
     * Return a string representation of a Date.
     * The format is for display of dates in a UI.
     * The format is mmm dd, yyyy hh:mm:ss
     * The TimeZone is normally either gmtTimeZone or the default timezone.
     *
     * @param date - the Date to be formatted
     * @param tz - the timezone used in the format operation
     */
    public synchronized static String toLongFormat( Date d, TimeZone tz )
    {
        longFormatter.setTimeZone( tz );
        String s = longFormatter.format( d );
        return s;
    }


    
    
    /**
     * Return a string representation of a Date.
     * The format is for display of dates where the time is the important
     * part and the date is secondary.
     * The format is hh:mm:ss, MMM dd.
     * The TimeZone is normally either gmtTimeZone or the default timezone.
     *
     * @param date - the Date to be formatted
     * @param tz - the timezone used in the format operation
     */
    public synchronized static String toShortFormat(Date d, TimeZone tz)
    {
        shortFormatter.setTimeZone( tz );
        String s = shortFormatter.format( d );
        return s;
    }

    
    /**
     * Returns a string representation of a time zone.
     */
    public synchronized static String toString( TimeZone tz )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "z" );
        formatter.setTimeZone( tz );
        String s = formatter.format( new Date() );
        return s;
    }

    
    
    /**
     * Parse a string representation of a Date.
     * The format is that used by the Ada package time_ascii and is
     * the format of dates passed in Doe messages.
     * The format is dd-mmm-yyyy hh:mm:ss.xxx
     * The TimeZone is normally either gmtTimeZone or the default timezone.
     *
     * @param str - a string representation of a date
     * @param tz - the timezone used in the parse operation
     */
    public synchronized static Date parseFsatsDate( String str, TimeZone tz )
         throws java.text.ParseException
    {
        doeFormatter.setTimeZone( tz );
        Date d = doeFormatter.parse( str );
        return d;
    }



    
    /**
     * Return the current time in the GMT timezone.
     */
    public synchronized static Date currentGMT()
    {
        Date d = new Date();
        calendar.setTime( d );
        
        // gmt time = local time - offset
        calendar.add( Calendar.MILLISECOND, -utcOffset );
        
        return calendar.getTime();
    }

    

    /**
     * test driver
     */
    public static void main( String[] args )
    {
        /********
        Date d = new Date();
        String s = toFsatsFormat( d, gmtTimeZone );
        String s1 = toDisplayFormat( d, gmtTimeZone );
        System.out.println( d + "    " + s + "   " + s1 );

        s = "30-APR-1999 09:00:06.860";
        try {
            // the string literal was produced by time_ascii.ascii(time.utc)
            d = parseFsatsDate( s, TimeZone.getDefault() );            
            System.out.println( s + " default   " + d );
            d = parseFsatsDate( s, gmtTimeZone );            
            System.out.println( s + "  gmt  " + d );
            s = toFsatsFormat( d, gmtTimeZone );
            System.out.println( d + "  gmt  " + s );
        } catch( Throwable t ) {
              System.out.println( t );
        }
        ********/
    }
    


}
