package fsats.mathFunctions;

import java.util.Random;
import java.lang.Math;

/**
 * A set of static functions which use a random number seed and
 * compute a random number with varying constraints.
 * See java.util.Random.
 */

public class RandomFunctions
{
   public static int getRandomIn(
      int min, 
      int max,
      Random seed)
   {
      /*nextDouble returns a value between 0.0 and 1.0*/
      //double r = (double)min + ((double)max - (double)min)*(seed.nextDouble());
      //return (int)(Math.floor(r));

      return (int)
             (RandomFunctions.getRandomIn((long)min,(long)max,seed));
   }

   public static long getRandomIn(
      long min, 
      long max,
      Random seed)
   {
      /*nextDouble returns a value between 0.0 and 1.0*/
      //double r = (double)min + ((double)max - (double)min)*(seed.nextDouble());
      //return (long)(Math.floor(r));

      return (long)
             (Math.floor(RandomFunctions.getRandomIn((double)min,(double)max,seed)));
   }

   public static double getRandomIn(
      double min, 
      double max,
      Random seed)
   {
      /*nextDouble returns a value between 0.0 and 1.0*/
      return min + (max - min + 1.0)*(seed.nextDouble());
   }

   public static double getInterArrivalTime(
      double lamda,
      Random seed)
   {
      double R = seed.nextDouble();
      double t = (-Math.log(1.0 - R))/lamda;
      return t;
   }
}
