package composer.args ;

import junit.framework.TestCase ;

final public class PatternFilterTest extends TestCase {

    public PatternFilterTest (String testName) {
	super (testName) ;
    }

    /**
     * Tests the default pattern which is to include everything.
     **/
    final public void testDefaultPattern () {
	PatternFilter pattern = new PatternFilter () ;
	assertTrue (pattern.accept ("Hello.java")) ;
	assertTrue (pattern.accept ("Hello.class")) ;
	assertTrue (pattern.accept ("HelloTest.class")) ;
	assertTrue (pattern.accept ("MainTest.class")) ;
    }

    /**
     * Tests a mixture of patterns.
     **/
    final public void testIncludeExclude () {
	PatternFilter pattern = new PatternFilter (
	    ".*[.]class,-:.*Test.*,+:MainTest[.]class"
	) ;
	assertTrue (! pattern.accept ("Hello.java")) ;
	assertTrue (pattern.accept ("Hello.class")) ;
	assertTrue (! pattern.accept ("HelloTest.class")) ;
	assertTrue (pattern.accept ("MainTest.class")) ;
    }

    /**
     * Tests a single exclude pattern to ensure that only what's excluded
     * is actually excluded.
     **/
    final public void testSingleExclude () {
	PatternFilter pattern = new PatternFilter ("-:.*[.]class") ;
	assertTrue (! pattern.accept ("Hello.class")) ;
	assertTrue (pattern.accept ("Hello.java")) ;
    }

    /**
     * Tests a single include pattern to ensure that only what's included
     * is actually included.
     **/
    final public void testSingleInclude () {
	PatternFilter pattern = new PatternFilter (".*[.]class") ;
	assertTrue (pattern.accept ("Hello.class")) ;
	assertTrue (! pattern.accept ("Hello.java")) ;
    }

}
