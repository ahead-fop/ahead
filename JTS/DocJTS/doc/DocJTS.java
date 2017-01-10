/** JTS Javadoc
 DocJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 August 24, 2001
*/

package jtsdoc;
import java.io.*;
import sun.tools.java.*;
import com.sun.javadoc.*;
import java.text.*;
import com.sun.tools.javadoc.*;

public abstract class DocJTS  implements Doc, Comparable 
{
	// *************************************************************************
	// *************************************************************************
 
	// *** Field summary
	/** I have no idea what is this for )=:
	 */
	static sun.tools.util.ModifierFilter showAccess;
	
	/** I have no idea what is this for )=:
	 */
	java.text.CollationKey collationkey;
	
	/** I have no idea what is this for )-:
	 */
	String documentation;
	
	/** Contains the comment associated to the document
	 */
	CommentJTS theComment;
	
	// *** Method summary
    /** I have no idea what is this for. )=:
    * Current implementation returns the empy string.
    */
	String documentation() { return documentation; }

	/** Current implementation returns a comment.
	 */
	public CommentJTS comment() { return theComment; }
	
	/** I have no idea how this should be set )-:
	 * Current implementation returns null.
	 */
	boolean checkAccess(){ return false; }
	
	/** Returns the header text comment from the Comment.
	 */
    public String commentText() { return theComment.text();}
	
	/** Returns the tags of the comment.
	 */
    public Tag tags()[] { return theComment.tags(); };
	
	/** Returns an array of tags that corresponds to a particular tag name.
	 */
    public Tag tags(String tagname)[] { return theComment.tags(tagname); };
	
	/** Returns the see tags of the comment.
	 */
    public SeeTag seeTags()[] { return theComment.seeTags(); }
	
	/** Returns the inline tags of the comment.
	 */
    public Tag inlineTags()[] 
	{ return theComment.inlineTags(); }
	
	/** Returns the first sentence tags from the header of a comment.
	 */
    public Tag firstSentenceTags()[] 
	{ return theComment.firstSentenceTags(this, theComment.text()); }
	
	/** Returns the whole comment string
	 */
    public String getRawCommentText() { return theComment.commentText();}
    
	/** I have no idea how this should be set )-:
	 * Current implementation returns an empty string.
	 */
	String readHTMLDocumentation(InputStream ios, String x) throws IOException 
	{ return ""; }

	/** I have no idea what this method does )-:
	 * Current implementation is empty.
	 */
    public void setRawCommentText(String rawDocumentation) 
    {
    };
    
	/** I have no idea what this method does )-:
	 * Current implementation returns false.
	 */
	boolean shouldDocument(MemberDefinition md){ return false; }
	
	/** I have no idea what this method does )-:
	 * Curremt implementation returns false.
	 */
	boolean shouldDocument(ClassDefinition cd) { return false; }
	
	/** I have no idea what this method does )-:
	 * Current implementation returns false.
	 */
	boolean isVisible(ClassDefinition cd) { return false; }
	
	/** I have no idea what this method does )-:
	 * Current implementation returns null.
	 */
	DOCLETJTS.Lang.ClassDocJTS thisClassDocJTS() { return null; }
	
	/** I have no idea what this method does )-:
	 * Current implementation returns null.
	 */
	CollationKey key() { return null; }
	
	/** I have no idea what this method does )-:
	 * Current implementation returns null.
	 */
	CollationKey generateKey() { return null; }
	
	/** I have no idea how this method is correctly set.
	 * Current implementation returns empty string.
	 */
	public String toString() { return ""; }
	
	/** I have no idea how this method is correctly set.
	 * Current implementation returns empty string.
	 */
	public String toQualifiedString() { return ""; }
	
	/** Suppose to return the name.
	 */
    public abstract String name();
    
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns 0 meaning that objects are all equal!
	 */
    public int compareTo(Object obj)
    { return 0; }

	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isField() { return false; }
	
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isMethod() { return false; }
	
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isConstructor() { return false; }
	
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isInterface() { return false; }
	
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isException() { return false; }
	
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isError() { return false; }
	
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isOrdinaryClass() { return false; }
	
	/** I have no idea how this method should be correctly set )-:
	 * Current implementation returns false.
	 */
    public boolean isClass() { return false; }
	
	/** I have no idea where this method should be set and how )-:
	 */
    public abstract boolean isIncluded();	

	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.		

	/** Sets the comment associated to this doc.
	 */
	public void setComment(CommentJTS _theComment) { theComment = _theComment; }
	
	/* Since now the name() is abstract then we dont need this field,
	   and the constructors and methods to handle it.
	 */
	
	// *** Field Summary 
	// public String name;	

	// *** Constructors added to support the functionality
	// public DocJTS() {  }
	// public DocJTS(String _name) { name = _name; }
	// I have no idea how to use this constructor ??
	// com.sun.tools.javadoc.DocImpl(com.sun.tools.javadoc.Env,java.lang.String)
	
	// *** Methods added to support the functionality
	// public void setName(String theName) { name = theName; }	
	
}
