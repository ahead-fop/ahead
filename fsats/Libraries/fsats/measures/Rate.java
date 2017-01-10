package fsats.measures;

/**
 * A Rate type - as in number of targets per second.
 * The implied unit of time is ALWAYS 1 second.
 */
public class Rate
{
   
  /**
   * Rate in number of items per second.
   */
   public double getRate()
   {
      return rate;
   }

  /**
   * Returns items per second
   */ 
   public String toString()
   {
      return "" + rate + " per second";
   }
   
   public Rate(
      double rate)
   {
      this.rate = rate;
   }
 
   private double rate;
}
