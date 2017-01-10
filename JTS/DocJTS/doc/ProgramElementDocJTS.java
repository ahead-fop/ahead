/** JTS Javadoc
 ProgramElementDocJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;
import com.sun.javadoc.*;

public abstract class ProgramElementDocJTS extends DocJTS implements ProgramElementDoc
{
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary 
	
	/** Contains a reference to the class containing this class if any.
	 * Current implementation returns null.
	 */
	DOCLETJTS.Lang.ClassDocJTS containingClass;
	
	// *** Method Summary
	
	/** Returns the containing class.
	 */
	public ClassDoc containingClass() { return containingClass; }
	
	/** Abstract method. Suppose to return the containing package.
	 */
	public abstract PackageDoc containingPackage();
	
	/** Abstract method. Returns the qualified name.
	 */
	public abstract String qualifiedName();
	
	/** I dont have any idea what is this )=:
	 * Current implementation returns 0.
	 */
	public int modifierSpecifier() { return 0; }
	
	/** Returns an string with the modifiers of the ProgramElement.
	 * Current implementation display order : public, protected, private, static, final.
	 * Probably this is not standard order.
	 */
	public String modifiers() 
	{   
		String output = "";
		if (isPublic) output = output + " public ";
		if (isProtected) output = output + " protected ";
		if (isPrivate) output = output + " private ";
		if (isStatic) output = output + " static ";
		if (isFinal) output = output + " final ";	
		return output; 
	}
	
	/** Returns the variable isPublic.
	 */
	public boolean isPublic() { return isPublic; }
	
	/** Returns the variable isProtected.
	 */
	public boolean isProtected() { return isProtected; }
	
	/** Returns the variable isPrivate.
	 */
	public boolean isPrivate(){ return isPrivate; }
	
	/** Returns the variable isPackagePrivate.
	 * Current implementation : uncertain how to set it.
	 */
	public boolean isPackagePrivate() { return isPackagePrivate; }
	
	/** Returns the variable isStatic.
	 */
	public boolean isStatic() { return isStatic; }
	
	/** Returns the variable isFinal. 
	 */
	public boolean isFinal() { return isFinal; }
	
	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.	
	
	// *** Additional fields added to support functionality
	
	/** Variable that indicates if the ProgramElement is public.
	 */
	boolean isPublic = false;
	
	/** Variable that indicates if the ProgramElement is protected.
	 */
	boolean isProtected = false;
	
	/** Variable that indicates if the ProgramElement is private.
	 */
	boolean isPrivate = false;
	
	/** Variable that indicates if the ProgramElement is package private.
	 * Current implementation : uncertain what it means.
	 */
	boolean isPackagePrivate = false;
	
	/** Variable that indicates if the ProgramElement is static.
	 */
	boolean isStatic = false;
	
	/** Variable that indicates if the ProgramElement is final.
	 */
	boolean isFinal = false;

	// *** Constructor added to support functionality
	
	/** Added : Constructor.
	 */
	public ProgramElementDocJTS(String theName)
	{
		// super(theName);
	} 
	
	/** Added : Constructor.
	 */
	public ProgramElementDocJTS() { }
	
	// *** Additional methods added to support functionality
	
	/** Sets the isPublic variable of the ProgramElement.
	 */
	public void setPublic (boolean _isPublic) { isPublic = _isPublic; }
	
	/** Sets the isProtected variable of the ProgramElement.
	 */
	public void setProtected (boolean _isProtected) { isProtected = _isProtected; }
	
	/** Sets the isPrivate variable of the ProgramElement.
	 */
	public void setPrivate (boolean _isPrivate) { isPrivate = _isPrivate; }
	
	/** Sets the isPackagePrivate variable of the ProgramElement.
	 */
	public void setPrivatePackage (boolean _isPackagePrivate) { isPackagePrivate = _isPackagePrivate; }
	
	/** Sets the isStatic variable of the ProgramElement.
	 */
	public void setStatic (boolean _isStatic) { isStatic = _isStatic; }
	
	/** Sets the isFinal variable of the ProgramElement.
	 */
	public void setFinal (boolean _isFinal) { isFinal = _isFinal; }
	
}
