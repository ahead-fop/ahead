//
// Assert
//
// $Revision: 1.1.1.1 $
//
// $Date: 2002-03-14 22:51:31 $
//
package fsats.util.debug;

/**
 * Assert adds some rudimentary pre and post condition checking abilities.
 * This is a modified version of the Assert class proposed by
 * <A HREF="http://www.holub.com/">Alan Holub</A>. To use this Assert
 * class, import fsats.util.debug.Assert and when through testing,
 * import fsats.util.Assert. The latter class contains empty methods and
 * will be inline optimized away using the HotSpot VM.
 * An example for usage would be:
 *<PRE>
 *  // ...
 *  public void someMethod(int someInt, Object someObject)
 *  {
 *      // Pre-condition asserts
 *      Assert.true(someInt > 0);
 *      Assert.true(someObject != null);
 *      // body of method code here
 *
 *      // Post-condition asserts
 *      Assert.true(someOtherCondition == true);
 *  }
 *  // ...
 *</PRE>
 *
 * @see fsats.util.Assert
 */
public class Assert
{
    /**
     * Asserts that a condition is true.
     *
     * @param condition condition to check
     */
    public final static void isTrue(boolean condition)
    {
        if (!condition)
        {
            throw new Assert.Failed();
        }
    }
    
    /**
     * Asserts that a condition is false.
     *
     * @param condition condition to check
     */
    public final static void isFalse(boolean condition)
    {
        if (condition)
        {
            throw new Assert.Failed();
        }
    }
    
    /**
     * Asserts that a condition is true, and allows a string to be
     * passed to the exception that results from a failed assertion.
     *
     * @param condition condition to check
     * @param message message about the failed assertion
     */
    public final static void isTrue(boolean condition, String message)
    {
        if (!condition)
        {
            throw new Assert.Failed(message);
        }
    }
    
    /**
     * Asserts that a condition is false, and allows a string to be
     * passed to the exception that results from a failed assertion.
     *
     * @param condition condition to check
     * @param message message about the failed assertion
     */
    public final static void isFalse(boolean condition, String message)
    {
        if (condition)
        {
            throw new Assert.Failed(message);
        }
    }
    
    /**
     * Class used to signal an assertion failure. It is a RuntimeException
     * so that the developer does not have to catch any exceptions.
     */
    public static class Failed extends RuntimeException
    {
        /**
         * Creates a new Assert.Failed object with a default string.
         */
        public Failed()
        {
            super("Assert Failed");
        }
        
        /**
         * Creates a new Assert.Failed object with the given string.
         */
        public Failed(String message)
        {
            super(message);
        }
    }
}
     
