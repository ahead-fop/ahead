/** JTS Javadoc
 ExecutableMemberDocJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;
import com.sun.javadoc.*;
import java.text.*;
import sun.tools.java.*;
import com.sun.tools.javadoc.*;

public class ExecutableMemberDocJTS extends MemberDocJTS implements ExecutableMemberDoc {

	// *** Fields, why are these finals?
	/** I assume this is the signature.
	 * Initialized to empty string.
	 */
	String signature = "";
	
	/** I dont know what is this.
	 * Initialized to empty string.
	 */
    String flatSignature = "";
	
	/** Array of ParameterJTS objects.
	 * Initialized to null.
	 */
    ParameterJTS parameters[] = null;
	
	/** Array of ClassDocJTS that I think corresponds to the exception throwns.
	 */
    DOCLETJTS.Lang.ClassDocJTS thrownExceptions[] = null;
	
	// *** Constructor
	// I dont know what is this.
	// ExecutableMemberDocJTS(Env e, MemberDefinition m) { return; }
		
	// public ExecutableMemberDocJTS() { return; }
		
	// *** Auxiliary methods
	/** I dont know what is the function of this method.
	 * Current implementation returns false.
	 */
	boolean hasNameAndParameters(String name, String[] params) {return false; }
	
	/** Compares the parameter types with the ones in a String.
	 */
	public boolean equalParameterTypes(String[] parmtypes) 
	{ 
		if (parameters == null) return false;
		
		for(int i=0; i< parameters.length; i++)
		{
			if (!(parameters[i].typeName()).equals(parmtypes[i]))
				return false;
		}
		
		return true; 
	}
	
	/** I dont know what is the function of this method.
	 * Current implementation returns null.
	 */
	CollationKey generateKey(){ return null; }
	
	// *** Method Summary
	
	/** Returns an array of ClassDocJTS objects that corresponds to the 
	 * thrown exceptions.
	 */
	public ClassDoc thrownExceptions()[] 
	{ return thrownExceptions; }
	
	/** Returns the isNative variable.
	 */
	public boolean isNative() { return isNative; }
	
	/** Returns the isSynchronized variable.
	 */
	public boolean isSynchronized() { return isSynchronized; }
	
	/** Returns an array of ParameterJTS objects, which correspond to the
	 * parameters.
	 */
	public Parameter parameters()[] { return parameters; }
	
	/** Returns an array of ThrowsTagJTS objects, which correspond to the
	 * thrown tags of the executable member.
	 * Current implementation returns null.
	 */
	public ThrowsTag throwsTags()[] { return null; }
	
	/** Returns an array of ParamTagJTS objects, which correspond to the
	 * param tags of the executable member.
	 */
	public ParamTag paramTags()[] { return null; }
	
	/** Returns the signature variable.
	 */
	public String signature() { return signature; }
	
	/** Returns the flatSignature variable.
	 */
	public String flatSignature() { return flatSignature; }
	
	/** I dont know what this one returns.
	 * Currrent implementation return empty string.
	 */
	public String toQualifiedString() { return ""; }
		
	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.
	
	// *** Additional fields added to support functionality
	/** Variable that indicates if the executable member is native.
	 * Initialized to false.
	 */
	boolean isNative = false;
	
	/** Variable that indicates if the executable member is synchronized.
	 * Initialized to false.
	 */
	boolean isSynchronized = false;
	
	// *** Additional methods added to support functionality
	/** Sets the parameters array.
	 */
	public void setParameters (ParameterJTS[] _parameters)
	{ parameters = _parameters; }
	
	/** Sets the variable isNative.
	 */
	public void setNative(boolean _isNative) { isNative = _isNative; }
	
	/** Sets the variable isSynchronized.
	 */
	public void setSynchronized(boolean _isSynchronized)
	{ isSynchronized = _isSynchronized; }
	
	/** Sets the thrown exceptions.
	 */
	public void setThrownExceptions(DOCLETJTS.Lang.ClassDocJTS[] _thrownExceptions)
	{
		thrownExceptions = _thrownExceptions;
	}
	
   // *** Additional constructor added to support functionality
	/** Default constructor
	 */
	public ExecutableMemberDocJTS ()  { }
	
	/** Added constructor.
	 */
	public ExecutableMemberDocJTS(String _name) {  super(_name); }	
}
