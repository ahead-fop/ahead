/** JTS Javadoc
 SerialFieldTagJTS.java 
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
import java.lang.*;

public class SerialFieldTagJTS extends TagJTS implements SerialFieldTag, Comparable 
{
	// *** Fields
	/** I dont know what are the contents of this variable )-:
	 * Initialized to empty string.
	 */
    String fieldName = "";
	
	/** I dont know what are the contents of this variable )-:
	 * Initialized to empty string.
	 */
    String fieldType = "";
	
	/** I dont know what are the contents of this variable )-:
	 * Initialized to empty string.
	 */
    String description = "";
	
	/** I dont know what are the contents of this variable )-:
	 * Initialized to null.
	 */
    ClassDoc containingClass = null;
	
	/** I dont know what are the contents of this variable )-:
	 * Initialized to null.
	 */
    ClassDoc fieldTypeDoc = null;
	
	/** I dont know what are the contents of this variable )-:
	 * Initialized to null.
	 */
    FieldDocJTS matchingField = null;

	// *** Constructor
	/** I assume the constructor calls the super only )-:
	 */
	public SerialFieldTagJTS(Doc d, String s1, String s2) { super(d, s1, s2);	}
	
	// *** Auxiliary methods
	/** I have no idea what this method should return )-:
	 * Current implementation returns empty string.
	 */
	String key() { return ""; }
	
	/** I dont know what this method should do )-:
	 * Current implementation does nothing.
	 */
	void mapToFieldDocJTS(FieldDocJTS field) {  }
	
	/** I dont know what this method should do )-:
	 * Current implementation returns null.
	 */
	FieldDocJTS getMatchingField() { return null; }
	
	// *** Method summary
	/** I assume this method returns the variable fieldName.
	 * Current implementation does that.
	 */
	public String fieldName() { return fieldName; }
	
	/** I assume this method returns the variable fieldType.
	 * Current implementation does that.
	 */
	public String fieldType() { return fieldType; }
	
	/** I assume this method returns the variable fieldTypeDoc.
	 * Current implementation does that.
	 */
	public ClassDoc fieldTypeDoc() { return fieldTypeDoc; }
	
	/** I have no idea what this method should return )-:
	 * Current implementation returns empty string.
	 */
	public String description() { return ""; }
	
	/** I have no idea how this comparison should be set )-:
	 * Current implementation returns always 0 meaning everything is equal.
	 */
	public int compareTo(Object obj) { return 0; }
	
	// *** Overwritten methods
	/** I have no idea what this method should return )-:
	 * Current implementation returns empty string.
	 */
	public String kind() { return ""; }
	
	/** I have no idea what this method should return )-:
	 * Current implementation returns empty string. 
	 */
	public String toString() { return ""; }
}
