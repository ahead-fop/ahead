/** JTS Javadoc
 ConstructorJTS.java
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

public class ConstructorDocJTS extends ExecutableMemberDocJTS implements ConstructorDoc
{
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary
	
	// *** Method summary
	/** Returns the qualified name of the Constructor.
	 * I dont know how to set it correctly )-:
	 * Current implementation returns empty string.
	 */
	public String qualifiedName(){ return ""; }

	// *** Constructor
	// I have no idea what to do with this method.
	// static ConstructorDocJTS getConstructorDocJTS(Env e, MemberDefinition m){ return null; }
	// static ConstructorDocJTS getConstructorDocJTS(){ return null; }

	// *** Overwritten methods from Doc
	/** Returns the name of the constructor.
	 */
	public String name() { return super.name(); }
	
	/** Overrrides the isConstructor method of Doc. Always returns true
	 * because this is a constructor.
	 */
	public boolean isConstructor() { return true; }
    
	// I dont have any idea what is inside this block.
	// static {};

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.

	// *** Constructors.
	/** Additional constructor.
	 */
	public ConstructorDocJTS (String _name) { super(_name); }

	// *** Methods added to support functionality
	/** Sets the name of the Constructor.
	 */
	public void setName(String _name) { super.setName(_name); } 
}
