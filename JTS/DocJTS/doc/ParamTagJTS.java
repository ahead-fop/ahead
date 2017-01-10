/** JTS Javadoc
 ParamTagJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 August 2001
*/

package jtsdoc;
import com.sun.javadoc.*;
import com.sun.tools.javadoc.*;

public class ParamTagJTS extends TagJTS implements ParamTag {
	
	// *** Fields
	/** I dont know what this variable means )-:
	 */
    String parameterName;
	
	/** I dont know what this variable means )-:
	 */
    String parameterComment;
	
	// *** Constructor
	/** I assume that this constructor only calls its super.
	 */
	public ParamTagJTS(Doc d, String s1, String s2) { super (d, s1, s2); }
	
	// *** Method Summary
	/** I assume this returns the parameterName variable )-:
	 * Current implementation does that.
	 */
	public String parameterName() { return parameterName; }
	
	/** I assume this returns the parameterComment variable )-:
	 * Current implementation does that.
	 */
	public String parameterComment() { return parameterComment; }
  
	// *********** Auxiliary 
	/** Sets the parameter name.
	 */
    public void setParameterName(String _parameterName)
	{ parameterName = _parameterName; }
	
	/** Sets the parameter comment.
	 */
	public void setParameterComment(String _parameterComment)
	{ parameterComment = _parameterComment; }
}
