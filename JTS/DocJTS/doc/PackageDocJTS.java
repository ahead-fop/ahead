/** JTS Javadoc
 PackageDocJTS.java
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
import java.util.*;

public class PackageDocJTS extends DocJTS implements PackageDoc {
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary 	
	/** Variable that indicates if the package is included or not.
	 * I dont know what it  means )-:
	 * Initial value is false.
	 */
    boolean isIncluded = false;

	// *** Constructors
	
	// I  have no idea what are these for !!!
	// static PackageDocJTS getPackageDocJTS(Env e, String s) { };
    // static PackageDocJTS getPackageDocJTS() { return null; }
	
	// *** Auxiliary methods
	
	/** I dont know what this method is doing )-:
	 * I assume that it looks in some list for something.
	 * Current implementation returns null.
	 */
	static PackageDocJTS lookup(String name) { return null; }
	
	/** I dont know what this method is doing )-:
	 * I assume that it adds a class reference to an structure.
	 * Current implementation does nothing.
	 */
	void addClass(DOCLETJTS.Lang.ClassDocJTS c){ return ; }
	
	/** I dont know what this method returns )-:
	 * Current implementation returns empty string.
	 */
	String documentation() { return ""; }
    
	// *** Methods overwritten
	/** Returns the name of the package.
	 */
	public String name() { return name; }
	
	/** I dont know what it does. I assume it calls addClass for
	 * each element of on the list. )-:
	 * Current implementation does nothing.
	 */
	void addAllClassesTo(List L) { return ; }
	
	/** Returns the isIncluded variable
	 */
	public boolean isIncluded() { return isIncluded; }
	
	/** I dont know what this method should return )-:
	 * Current implementation returns the empty string.
	 */
	public String toString() { return ""; }
	
	// *** Method summary
	/** I assume this method returns an array with all the classes in the
	 * package )-:
	 * Current implementation returns an empty array.
	 */
	public ClassDoc allClasses()[] { return new DOCLETJTS.Lang.ClassDocJTS[0]; }
	
	/** I assume this method returns an array of ordinary classes.
	 * I dont know how this is correctly set )-:
	 * Current implementation returns an empty array.
	 */
	public ClassDoc ordinaryClasses()[] { return new DOCLETJTS.Lang.ClassDocJTS[0]; }
	
	/** I assume this method returns an array of classes that are exceptions.
	 * I dont know how this is correctly set )-:
	 * Current implementation returns an empty array.
	 */
	public ClassDoc exceptions()[] { return new DOCLETJTS.Lang.ClassDocJTS[0]; }
	
	/** I assume this method returns an array of classes that are errors.
	 * I dont know how this is correctly set )-:
	 * Current implementation returns an empty array.
	 */
	public ClassDoc errors()[] { return new DOCLETJTS.Lang.ClassDocJTS[0]; }
	
	/** I assume this method returns an array of classes that are the 
	 * interfaces of the package.
	 * I dont know how this is correctly set )-:
	 * Current implementation returns an empty array.
	 */
	public ClassDoc interfaces()[] { return new DOCLETJTS.Lang.ClassDocJTS[0]; }
	
	/** I assume that this method looks for a class in the structure.
	 * What is the difference with lookup ?
	 * I dont know how to set it correctly )-:
	 * Current implementation returns null.
	 */
	public ClassDoc findClass(String className) { return null; }
    
	
	// I have no idea what is inside this block.
	// Probably the actual data structure.
    // static {};

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.	
	
    // *** Additional fields
	/** Variable that contains the name of the package.
	 */
	String name;
	
	// *** Additional methods
	/** Sets the variable isIncluded.
	 */
	public void setIncluded(boolean _isIncluded)
	{ isIncluded = _isIncluded; }
	
	// *** Additional constructors added to support functionality
	/** Additional constructor.
	 */
	public PackageDocJTS(String _name) { name = _name; }
}
