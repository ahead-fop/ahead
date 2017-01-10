/** JTS Javadoc
 RootDocJTS.java
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

public class RootDocJTS extends DocJTS implements RootDoc , Constants  
{
	// *** Methods Summary
	/** I dont know exactly how to set this correctly )-:
	 * Current implementation returns null.
	 */
	public String options()[][] { return null; }
	
	/** I dont know exactly how to set this correctly )-:
	 * Current implementation returns an empty array.
	 */
	public PackageDoc specifiedPackages()[] { return new PackageDocJTS[0]; }
	
	/** I dont know how to set it correctly )-:
	 * Current implementation returns an empty array.
	 */
	public ClassDoc specifiedClasses()[]{ return new DOCLETJTS.Lang.ClassDocJTS[0]; }
	
	/** Returns the array of classes that are at the root of the Document.
	 */
	public ClassDoc classes()[]{ return theClasses; }
	
	/** I dont know what this method is doing )-:
	 * I assume that it looks somewhere for a package by its name.
	 * Current implementation returns null.
	 */
	public PackageDoc packageNamed(String name) { return null; }
	
	/** I dont know what this method is doing )-:
	 * I assume that it looks somewhere for a class by name.
	 * Current implementation returns null.
	 */
	public ClassDoc classNamed(String qualifiedName) { return null;  }
	
	// *** DocErrorReporter
	/** I have no idea what is this for )-:
	 * Current implementation does nothing.
	 */
	public void printError(String msg){	}
	
	/** I have no idea what his method is for )-:
	 * Current implementation does nothing.
	 */
	public void printWarning(String msg){ }
	
	/** I have no idea what this method is for )-:
	 * Current implementation does nothing.
	 */
	public void printNotice(String msg){ }

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.
	
	// *** Fields added to support the functionality
	/** Array that contains the classes of the root.
	 */
	ClassDoc[] theClasses;
	
	/** Variable that indicates if the root is included or not.
	 * I have no idea what it means )-:
	 * Initialized to false.
	 */
	boolean isIncluded = false;
	
	// *** Methods added to suppor the functionality
	/** Sets the classes of the root.
	 */
	public void setClasses(ClassDoc[] _theClasses)
	{
		theClasses = _theClasses;
	}
	
	/** Sets the isIncluded variable.
	 */
	public boolean isIncluded() { return isIncluded; }
	
	/** I dont know how to implement this correctly )-:
	 * Current implementation returns empty string.
	 */
	public String name() { return ""; }
	
}
