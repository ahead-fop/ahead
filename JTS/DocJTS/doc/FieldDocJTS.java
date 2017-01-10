/** JTS Javadoc
 FieldDocJTS.java
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
import sun.tools.java.*;

public class FieldDocJTS extends MemberDocJTS implements FieldDoc {
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary 
	
	/** Type of the Field.
	 */
    com.sun.javadoc.Type type ;
	
	// *** Constructor
	// I have no idea what is this for.
	// static FieldDocJTS getFieldDocJTS(Env e, MemberDefinition md) { return null; }
		
	// *** Methods overwritten
	/** Overrides the method in DocJTS and returns always true since this is a
	 * field.
	 */
	public boolean isField() { return true; }
	
	// *** Method summary
	/** Returns the type variable.
	 */
	public com.sun.javadoc.Type type() { return type; }
	
	/** Returns the isTransient variable.
	 */
	public boolean isTransient() { return isTransient; }
	
	/** Returns the isVolatile variable.
	 */
	public boolean isVolatile() { return isVolatile; }
	
	/** Returns an array of SerialFieldTagJTS objects.
	 * I dont know how to set it correctly.
	 * Current implementation returns empty array.
	 */
	public SerialFieldTag serialFieldTags()[] 
	{ return new SerialFieldTagJTS[0]; }
	
	// I dont know what is inside this.
    // static {};

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.	
	
	// *** Fields added to support functionality
	/** Variable that indicates if the field is transient or not.
	 * Initialized to false.
	 */
	boolean isTransient = false;
	
	/** Variable that indicates if the field is volatile or not.
	 * Initialized to false.
	 */
	boolean isVolatile = false;
	
	// *** Methods added to support functionality
	/** Added constructor. 
	 */
	public FieldDocJTS(String _name) { super(_name); }
	
	/** Sets the isTransient variable.
	 */
	public void setTransient(boolean _isTransient) { isTransient = _isTransient; }

	/** Sets the isVolatile variable.
	 */
	public void setVolatile(boolean _isVolatile) { isVolatile = _isVolatile; }

	/** Sets the type of the field.
	 */
	public void setType(TypeJTS _type) { type = _type; }
}
