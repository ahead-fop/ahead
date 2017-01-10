/** JTS Javadoc
 TypeJTS.java
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
import java.io.*;

public class TypeJTS implements Type
{
	// *** Method Summary
	/** I assume this method returns the typeName variable )-:
	 * Current implementation does that.
	 */
	public String typeName() { return typeName; }
	
	/** I assume this method returns the qualifiedTypeName variable )-:
	 * Current implementation does that.
	 */
	public String qualifiedTypeName() { return qualifiedTypeName;}
	
	/** I dont know what this method should return )-:
	 * Current implementation returns empty string.
	 */
	public String dimension() { return ""; }
	
	/** I dont know what this method should return )-:
	 * Current implementation returns empty string.
	 */
	public String toString(){ return ""; }
	
	/** I dont know what this method whould return )-:
	 * Current implementation returns null.
	 */
	public ClassDoc asClassDoc() { return null; }

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.		
	
	// Additional constructor
	/** Additional constructor.
	 */
	public TypeJTS(String _typeName, String _qualifiedTypeName)
	{
		typeName = _typeName;
		qualifiedTypeName = _qualifiedTypeName;
	}
	
	// *** Additional fields added to support functionality
	/** I assume there goes the type name )-:
	 */
	public String typeName;
	
	/** I assume there goes the qualified type name )-:
	 */
	public String qualifiedTypeName;
	
	// *** Additional methods added to support functionality
	/** Sets the typeName variable.
	 */
	public void setType (String _typeName) { typeName = _typeName; }
	
	/** Sets the qualifiedTypeName variable.
	 */
	public void setQualifiedTypeName (String _qualifiedTypeName)
	{ qualifiedTypeName = _qualifiedTypeName; }
}
