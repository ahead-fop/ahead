package fsats.measures;

import fsats.util.StringOps;

/**
 * A Time type - as in day:hour:min:sec expresed in seconds.
 * No "fudging" is done with seconds to always start at day 1.
 * That is the seconds are exactly equal to the days+hours+minutes+seconds.
 */
public class RelativeTime
{
   public int getDay()
   {
      int d = seconds/86400;
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
   * Elapsed Time in seconds
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
      int d = seconds/86400;
      int r = seconds - (d*86400);
      int h = r/3600;
      r = r - (h*3600);
      int m = r/60;
      int s = r - (m*60);

      return new String(""+StringOps.zeroPad(d,3)+","+
                           StringOps.zeroPad(h,2)+":"+
                           StringOps.zeroPad(m,2)+":"+
                           StringOps.zeroPad(s,2));       
   }

   public boolean isNull()
   {
      return false;
   }


   public static RelativeTime nullRelativeTime()
   {
      return nullRT;
   }
   
   public RelativeTime(
      int seconds)
   {
      this.seconds = seconds;
   }
 
   public RelativeTime(
      int days,
      int hours,
      int minutes,
      int seconds)
   {
      this.seconds = ((days)*24*3600) +
                      (hours*3600) +
                      (minutes*60) + 
                      seconds;
   }
 
   private int seconds;
   private static RelativeTime nullRT = new NullRelativeTime();
}

class NullRelativeTime
   extends RelativeTime
{
   NullRelativeTime()
   {
      super(0);
   }

   public boolean isNull()
   {
      return true;
   }
}
