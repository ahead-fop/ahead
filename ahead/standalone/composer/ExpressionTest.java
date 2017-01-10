package composer ;

import java.io.File ;

import junit.framework.TestCase ;

final public class ExpressionTest extends TestCase {

    public ExpressionTest (String testName) {
	super (testName) ;
    }

    final public void testGetInstanceFile () {

	File file = new File ("file1.txt") ;
	Expression one = Expression.getInstance (file) ;
	Expression two = Expression.getInstance (file) ;
	assertSame ("same File doesn't yield same Expression!", one, two) ;

	one = Expression.getInstance (new File ("file2.txt")) ;
	two = Expression.getInstance (new File ("file2.txt")) ;
	assertSame ("equal Files don't yield same Expression!", one, two) ;
    }

}
