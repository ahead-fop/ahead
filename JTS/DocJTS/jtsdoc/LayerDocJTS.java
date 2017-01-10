/** JTS Javadoc
 LayerDocJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;
import com.sun.javadoc.*;

public class LayerDocJTS extends DocJTS implements LayerDoc
{
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary 
	/** Contains the name of the layer.
	 */
	String name;
	
	
	/** Variable that contains the name of the realm of the layer.
	 */
	String realmName;
	
	/** Variable that contains the name of the super layer.
	 * Initialized to empty string.
	 */
	String superLayer = "";
	
	/** Classbody of the layer.
	 */
	DOCLETJTS.Lang.ClassDocJTS classBody;
	
	/** Imports List.
	 * Current implementation only keeps the string.
	 * Future versions will have to deal with something like 
	 * imported classes or packages.
	 */
	String[] importsList;
	
	/** Array that contains the parameters of the layer.
	 */
	ParameterJTS parameters[];
	
	// *** Method Summary
	
	/** Returns the qualified name.
	 * Legacy. Review
	 */
	public String qualifiedName() { return ""; }
	
	/** Returns always false.
	 * Legacy. Review
	 */
	public boolean isIncluded() { return false; }
	
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
		if (isRelative) output = output + " relative " ;
		if (isAbstract) output = output + " abstract " ;
		if (isTransient) output = output + " transient " ;
		if (isVolatile) output = output + " volatile " ;
		if (isNative) output = output + " native " ;
		if (isSynchronized) output = output + " synchronized " ;
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
	
	/** Returns the variable isStatic.
	 */
	public boolean isStatic() { return isStatic; }
	
	/** Returns the variable isFinal. 
	 */
	public boolean isFinal() { return isFinal; }
	
	/** Returns the variable isRelative.
	 */
	public boolean isRelative() { return isRelative; }
	
	/** Returns the variable isAbstract.
	 */
	public boolean isAbstract() { return isAbstract; }
			
	/** Returns the variable isTransient.
	 */
	public boolean isTransient() { return isTransient; }
	
	/** Returns the variable isVolatile.
	 */
	public boolean isVolatile() { return isVolatile; }
	
	/** Returns the variable isNative.
	 */
	public boolean isNative() { return isNative; }
	
	/** Returns the variable isSyncrhonized.
	 */
	public boolean isSynchronized() { return isSynchronized; }
	
	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.	
	
	// *** Additional fields added to support functionality
	
	/** Variable that indicates if the layer is public.
	 */
	boolean isPublic = false;
	
	/** Variable that indicates if the layer is protected.
	 */
	boolean isProtected = false;
	
	/** Variable that indicates if the layer is private.
	 */
	boolean isPrivate = false;
	
	/** Variable that indicates if the layer is static.
	 */
	boolean isStatic = false;
	
	/** Variable that indicates if the layer is final.
	 */
	boolean isFinal = false;
	
	/** Variable that indicates if the layer is relative
	 */
    boolean isRelative = false;

	/** Variable that indicates if the layer is abstract.
	 */
	boolean isAbstract = false;
	
	/** Variable that indicates if the layer is transient.
	 */
	boolean isTransient = false;
	
	/** Variable that indicates if the layer is volatile.
	 */
	boolean isVolatile = false;
	
	/** Variable that indicates if the layer is native.
	 */
	boolean isNative = false;
	
	/** Variable that indicates if the layer is synchronized.
	 */
	boolean isSynchronized = false;	
	
	// *** Constructor added to support functionality

	/** Added : Constructor.
	 */
	public LayerDocJTS(String _name)
	{ name = _name;	} 
	
	/** Added : Constructor.
	 */
	public LayerDocJTS() { }
	
	// *** Additional methods added to support functionality
	/** Gets the name of the layer.
	 */
	public String name() { return name; }
	
	/** Gets the name of the realm.
	 */
	public String realmName() { return realmName; }
	
	/** Gets the name of the super layer.
	 */
	public String superLayer() { return superLayer; }
	
	/** Gets the classBody object of the layer.
	 */
	public DOCLETJTS.Lang.ClassDocJTS classBody() { return classBody; }
	
	/** Gets the list of the imports strings.
	 */
	public String importsList()[] { return importsList; }
	
	/** Returns the array of the parameters.
	 */
	public ParameterJTS parameters()[] { return parameters; }
	
	/** Sets the name of the realm.
	 */
	public void setRealmName(String _realmName) { realmName = _realmName; }
	
	/** Sets the superlayer of the layer.
	 */
	public void setSuperLayer(String _superLayer) { superLayer = _superLayer; }
	
	/** Sets the class body object of the layer.
	 */
	public void setClassBody(DOCLETJTS.Lang.ClassDocJTS _classBody) 
	{ classBody = _classBody; }
	
	/** Sets the importsList of the layer.
	 */
	public void setImportsList(String[] _importsList) 
	{ importsList = _importsList; }
	
	/** Sets the isPublic variable of the layer.
	 */
	public void setPublic (boolean _isPublic) { isPublic = _isPublic; }
	
	/** Sets the isProtected variable of the layer.
	 */
	public void setProtected (boolean _isProtected) { isProtected = _isProtected; }
	
	/** Sets the isPrivate variable of the layer.
	 */
	public void setPrivate (boolean _isPrivate) { isPrivate = _isPrivate; }
	
	/** Sets the isStatic variable of the layer.
	 */
	public void setStatic (boolean _isStatic) { isStatic = _isStatic; }
	
	/** Sets the isFinal variable of the layer.
	 */
	public void setFinal (boolean _isFinal) { isFinal = _isFinal; }
	
	/** Sets the isRelative variable of the layer.
	 */
	public void setRelative(boolean _isRelative) { isRelative = _isRelative; }

	/** Sets the isAbstract variable of the layer.
	 */
	public void setAbstract(boolean _isAbstract) { isAbstract = _isAbstract; }
	
	/** Sets the isTransient variable of the layer.
	 */
	public void setTransient(boolean _isTransient) { isTransient = _isTransient; }
	
	/** Sets the parameters array of the layer.
	 */
	public void setParameters(ParameterJTS[] _parameters)
	{ parameters = _parameters; }
	
	/** Sets the isVolatile variable of the layer.
	 */
	public void setVolatile(boolean _isVolatile) { isVolatile = _isVolatile; }
	
	/** Sets the isNative variable of the layer.
	 */
	public void setNative(boolean _isNative) { isNative = _isNative; }
	
	/** Sets the isSynchronized variable of the layer.
	 */
	public void setSynchronized(boolean _isSynchronized) 
	{ isSynchronized = _isSynchronized; }
	
} // of LayerDocJTS
