/** JTS Javadoc
 MemberDocJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;
import com.sun.javadoc.*;
import sun.tools.java.*;

public abstract class MemberDocJTS extends ProgramElementDocJTS implements MemberDoc {
	
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary
	/** Name of the MemberDoc.
	 */
	String name ;
    
	// I dont know what is this for.
	// com.sun.tools.javadoc.MemberDocImpl(com.sun.tools.javadoc.Env,sun.tools.java.MemberDefinition);
	
	/** Originally, com.sun.tools.javadoc.ClassDocImpl thisClassDocImpl();
	 * Current implementation returns null.
	 */
	DOCLETJTS.Lang.ClassDocJTS thisClassDocImpl() { return null; }
	
	// *** Method summary
	/** Returns the isSynthetic variable.
	 */
	public boolean isSynthetic() { return isSynthetic; }
	
	// *** Overwritten methods
	/** Returns the qualified Name. I dont know how to set it correctly )-:
	 * Currrent implementation returns empty string.
	 */
	public String qualifiedName() { return ""; }
	
	/** Returns the name of the MemberDoc.
	 */
	public String name() { return name; }
	
	/** Returns the containing package.
	 * Current implementation returns null.
	 */
	public PackageDoc containingPackage(){ return null; }
	
	/** Returns the isIncluded variable.
	 */
	public boolean isIncluded(){ return isIncluded; }
    
	// *** Auxiliary methods
	/** I dont know how to set it correctly )-:
	 * Current implementation returns empty string.
	 */
	public String toString() { return ""; }
	
	/** I dont know what this method is for. )-:
	 * Current implementation returns null.
	 */
	static String lookupKey(MemberDefinition member){ return null; }
	
	/** I dont know how to set this correctly )-:
	 * Current implementation returns null.
	 */
	public String toQualifiedString(){return ""; }

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.
	
	// *** Fields
	/** Variable that indicates if a member is Synthetic or not.
	 * I have no idea what this means )-:
	 * Initial value is false.
	 */
	boolean isSynthetic = false;
	
	/** Variable that indicates if a member is Included or not.
	 * I have no idea how this is set )-:
	 * Initial value is false.
	 */
	boolean isIncluded = false;
	
	// *** Constructors
	/** Default constructor.
	 */
	public MemberDocJTS () { }
	
	/** Additional constructor.
	 */
	public MemberDocJTS (String _name) 
	{ 
		// super(_name); 
		name = _name;
	}
	
	// *** Methods
	/** Sets the name of the member doc.
	 */
	public void setName(String _name) { name = _name; }
	
	/** Sets the isSynthetic variable.
	 */
	public void setSynthetic(boolean _isSynthetic) 
	{ isSynthetic = _isSynthetic; }
	
	/** Sets the isIncluded variable.
	 */
	public void setIncluded(boolean _isIncluded)
	{ isIncluded = _isIncluded; }
	
}
