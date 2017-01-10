/** JTS Javadoc
 TagJTS.java
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

public class TagJTS extends Object implements Tag {
	
	// *** Fields, why are this final??
	/** I have no idea what this variable holds.
	 * I assume it is the text of the tag )-:
	 */
    String text;
	
	/** I have no idea what this variable should hold )-:
	 */
    String name;

	/** I have no idea what this variable should hold )-:
	 * Initialized to null
	 */
	Doc holder = null ;
	
	// *** Constructor
	/** I am not sure what this constructor should do )-:
	 * I assume it assigns the parameters to the fields in that order.
	 * Current implementation does that.
	 */
	public TagJTS (Doc _holder, String _text, String _name)
	{ holder = _holder; text = _text; name = _name; }

	// *** Method summary
	/** I suppose it returns variable name )-:
	 * Current implementation does that.
	 */
	public String name() { return name; }
	
	/** Current implementation returns kind field.
	 */
	public String kind() { return kind; }
	
	/** I suppose it returns the text variable )-:
	 * Current implementation does that.
	 */
	public String text() { return text; }
	
	/** I dont know what this method should return )-:
	 * Current implementation returns an empty array.
	 */
	public Tag firstSentenceTags()[] { return firstSentenceTags; }
	
	/** I have no idea what this method should return )-:
	 * Current implementation returns an empty array.
	 */
	public Tag inlineTags()[] {return inlineTags; }
  
	// *** Auxiliary methods
	/** I have no idea what this method should return )-:
	 * Current implementation returns null.
	 */
	String divideAtWhite()[]{ return null; }
	
	// *** Overwritten methods
	/** I dont know how to set it correctly )-:
	 * Current implementation Kind:Text
	 */
	public String toString() { return kind() + ":"+ text() ; }
	
	/** I dont know how to set it correctly )-:
	 * Current implementation returns an empty string.
	 */
	public String qualifiedDocName() { return  ""; }
	
	// ****************************************************************
	// **** Additional fields and methods to support functionality  ***
	// ****************************************************************
	/** Contains the kind of tag.
	 */
	public String kind;
	
	/** Contains an array with the firstSentence Tags.
	 */
	public Tag[] firstSentenceTags = null;
	
	/** Contains an array with the inlineTags.
	 */
	public Tag[] inlineTags = null;
	
	// ********************* Additional methods ************************
	/** Sets the value of kind variable
	 */
	public void setKind(String _kind) { kind = _kind; }
	
	/** Sets the first sentence Tags.
	 */
	public void setFirstSentenceTags(Tag[] _firstSentenceTags)
	{ firstSentenceTags = _firstSentenceTags; }
	
	/** Sets the inline tags.
	 */
	public void setInlineTags(Tag[] _inlineTags)
	{ inlineTags = _inlineTags; }
}

