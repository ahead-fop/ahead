/** JTS Javadoc
 ThrowsTagJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;
import com.sun.javadoc.*;
import com.sun.tools.javadoc.*;

public class ThrowsTagJTS extends TagJTS implements ThrowsTag {
	
	// *** Fields
	/** Initialized to empty string.
	 */
    String exceptionName = "";
	
	/** Initialized to empty string.
	 */
    String exceptionComment = "";
	
	/** Initialized to null.
	 */
    ClassDoc exceptionClass = null;
	
	// *** Constructor
	/** I assume that this constructor only calls the super )-:
	 */
	public ThrowsTagJTS (Doc d, String s1, String s2) { super (d, s1, s2); }
	
	// *** Method summary
	/** I assume this method returns the exceptionName variable.
	 * Current implementation does that.
	 */
	public String exceptionName() { return exceptionName; }
	
	/** I assume this method returns the exceptionComment variable.
	 * Current implementation does that.
	 */
	public String exceptionComment() { return exceptionComment; }
	
	/** I dont know what this represents. Probably it is the Doc reference
	 *  to the exception class. Current implementation returns null.
	 */
	public ClassDoc exception() { return exceptionClass; }
    
	// *** Auxiliary methods
	/** Sets the exception name field.
	 */
	public void setExceptionName(String _exceptionName)
	{ exceptionName = _exceptionName; }
	
	/** Sets the exception comment field.
	 */
	public void setExceptionComment(String _exceptionComment)
	{ exceptionComment = _exceptionComment; }
	
}
