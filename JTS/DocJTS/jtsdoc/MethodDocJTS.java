/** JTS Javadoc
 MethodDocJTS.java
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

public class MethodDocJTS extends ExecutableMemberDocJTS implements MethodDoc {
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary
	
	// *** Fields why are these final ??
	/** The return type of the method.
	 * Initialized to null.
	 */
    com.sun.javadoc.Type returnType = null;
	
	/** I have no idea how is this set correctly.
	 * Initialized to null.
	 */
    DOCLETJTS.Lang.ClassDocJTS overriddenClass = null;

	// *** Get constructor ???
	/** Originally, static MethodDocJTS getMethodDocJTS(Env e, MemberDefinition m ) 
	 * Current implementation returns null.
	 */
	static MethodDocJTS getMethodDocJTS() { return null; }

	// *** Method summary	

	// *** Methods overriden
	// I have no idea how this is used.
	// ClassDocJTS overriddenClass(Env e, MemberDefinition m) { return null; }
	
	/** I am not sure how it is correctly implemented.
	 *  Current implementation always returns true since this is a method.
	 */
	public boolean isMethod() { return true; }

	/** Returns the variable isAbstract.
	 */
	public boolean isAbstract() { return isAbstract; }
	
	/** Returns the variable returnType.
	 */
	public com.sun.javadoc.Type returnType() { return returnType; }
	
	/** Returns the variable overridenClass.
	 */
	public ClassDoc overriddenClass(){ return overriddenClass; }
    
	// I dont know what it is in here!!!
	// static {};

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.
	
	// *** Additional constructor added to support functionality
	/** Default constructor.
	 */
	public MethodDocJTS () { }
	
	/** Added constructor.
	 */
	public MethodDocJTS (String _name) { super(_name); }
	
	// *** Additional fields added to support functionality
	/** Variable that indicates if a method is abstract or not.
	 * Initial value : false
	 */		
	boolean isAbstract = false;
	
	// *** Additional methods added to support functionality
	/** Sets the value of the isAbstract variable.
	 */
	public void setAbstract(boolean _isAbstract) { isAbstract = _isAbstract; }
	
	/** Sets the value of the returnType variable.
	 */
	public void setReturnType(TypeJTS _returnType) { returnType = _returnType; }
	
	/** Returns the variable returnType.
	 */
	public com.sun.javadoc.Type getReturnType() { return returnType; }
	
}
