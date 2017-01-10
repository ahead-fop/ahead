package fsats.measures;

import fsats.util.StringOps;

/**
 * A Time type - as in day:hour:min:sec expresed in seconds.
 *  When seconds are entered it is assumed that they are 
 *   relative to day 1.  As such when converting to or from
 *   seconds to day:hour:min:secs one days worth of seconds
 *   is added or subtracted respectively.
 */
public class Time {

    final public static
        int SECOND = Integer.getInteger ("fsats.second", 1000) . intValue () ;

    public int getDay()
    {
	int d = seconds/86400;
	d++;
	return d;
    }
    public int getHour()
    {
	int d = seconds/86400;
	int r = seconds - (d*86400);
	int h = r/3600;
	return h;
    }
    public int getMinute()
    {
	int d = seconds/86400;
	int r = seconds - (d*86400);
	int h = r/3600;
	r = r - (h*3600);
	int m = r/60;
	return m;
    }
    public int getSecond()
    {
	int d = seconds/86400;
	int r = seconds - (d*86400);
	int h = r/3600;
	r = r - (h*3600);
	int m = r/60;
	int s = r - (m*60);
	return s;
    }
   
    /**
     * Time in seconds
     */
    public int getTime()
    {
	return seconds;
    }

    /**
     * Returns format ddd:hh:mm:ss
     */ 
    public String toString()
    {
	//same as DHMS.toDHMS(this.seconds);

	int d = seconds/86400;
	int r = seconds - (d*86400);
	int h = r/3600;
	r = r - (h*3600);
	int m = r/60;
	int s = r - (m*60);

	d++;

	return new String(""+StringOps.zeroPad(d,3)+","+
			  StringOps.zeroPad(h,2)+":"+
			  StringOps.zeroPad(m,2)+":"+
			  StringOps.zeroPad(s,2));       
    }
   
    public Time(
	int seconds)
    {
	this.seconds = seconds;
    }
 
    public Time(
	int days,
	int hours,
	int minutes,
	int seconds)
    {
	this.seconds = ((days-1)*24*3600) +
	    (hours*3600) +
	    (minutes*60) + 
	    seconds;
    }
 

    public boolean isNull()
    {
	return false;
    }

    public static Time nullTime()
    {
	return nullTime;
    }
   
    private static Time nullTime = new NullTime();

    private int seconds;
}



class NullTime
extends Time
{
    NullTime()
    {
	super(0);
    }

    public boolean isNull()
    {
	return true;
    }
}
