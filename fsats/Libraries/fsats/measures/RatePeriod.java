package fsats.measures;

/**
 * A Rate type - as in number of targets in period hours.
 */
public class RatePeriod
{
   static RatePeriod nullRatePeriod = new NullRatePeriod();



  /**
   *  Test for a null RatePeriod
   */
   public boolean isNull()
   {
      return false;
   }



   /**
    * Return a null RatePeriod
    */
   public static RatePeriod nullRatePeriod()
   {
      return nullRatePeriod;
   }



   /**
    * Return a copy of this RatePeriod.
    */
   public RatePeriod copy()
   {
      return new RatePeriod(number, period);
   }



  /**
   * Test for equality
   */
   public boolean equals(RatePeriod rp)
   {
      boolean yes = true;
      if (!rp.isNull())
      {
         if (number != rp.number) yes = false;
         if (period != rp.period) yes = false;
      }
      else
      {
         yes = false;
      }

      return yes;
   }

   
   
  /**
   * Rate normalized to 1 hour
   */
   public double getRate()
   {
      return number/period;
   }



   public double getNumber()
   {
      return number;
   }



   /**
    * The period of time in hours.
    */
   public double getPeriod()
   {
      return period;
   }



  /**
   * Returns the string "<number> in <period> hours"
   */ 
   public String toString()
   {
      return "" + number + " in " + period + " hours";
   }


   
   public RatePeriod(
      double number,
      double period)
   {
      this.number = number;
      this.period = period;
   }
 


   private double number;
   private double period;
}


class NullRatePeriod
   extends RatePeriod
{  
   NullRatePeriod()
   {
      super(0.0, 0.0);
   }
    
 
 
  /**
   *  Test for a null RatePeriod
   */
   public boolean isNull()
   {
      return true;
   }
 
 
 
  /**
   * Test for equality
   */
   public boolean equals(RatePeriod f)
   {
      return f.isNull() ? true : false;
   }


   /**
    * Return a null RatePeriod
    */
   public RatePeriod copy()
   {
      return nullRatePeriod();
   }
}
   
