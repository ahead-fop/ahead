/** JTS Javadoc
 ParameterJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 August 2001
*/

package jtsdoc;
import com.sun.javadoc.*;

public class ParameterJTS extends Object implements Parameter {

	// *** Fields
	/** Variable that indicates the type of the Parameter.
	 */
	Type type;
	
	/** Variable that contains the name of the Parameter.
	 */
    String name;
    
	// *** Constructors
	/** Additional constructor.
	 */
	public ParameterJTS (Type t, String n)
	{ 
	  type = t; 
	  name = n;
	  return ;
	}
	
	// *** Method Summary
	/** Returns the type variable of the parameter.
	 */
	public Type type() { return type; }
	
	/** Returns the name variable of the parameter. 
	 */
	public String name() { return name; }
	
	/** I assume it returns the typeName() of type variable.
	 * I dont know how is this set correctly.
	 * Current implementation returns type.typeName()
	 */
	public String typeName() { return type.typeName(); }
	
	/** I dont know what this thing should return )-:
	 * Current implementation return empty string.
	 */
	public String toString() { return ""; }
}
