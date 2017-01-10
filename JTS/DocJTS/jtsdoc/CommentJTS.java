/** JTS Javadoc
 CommentJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 August 2001
*/
package jtsdoc;

import com.sun.tools.javadoc.*;
import java.util.*;
import java.text.*;
import com.sun.javadoc.*;

public class CommentJTS 
{
	// *** Fields
	/** I assume that this string is the whole comment.
	 */
    String commentString;
	
	/** I have no idea what this represents )-:
	 */
    List tagList;
	
	/** I assume that this string is the javadoc comment.
	 */
    String text;
	
	/** I have no idea what is this )-:
	 */
    static BreakIterator sentence;
	
	/** I have no idea what is this for )-:
	 */
    static String sentenceTerminators[];
	
	// *** Constructor
	public CommentJTS () { }
	
	/** Default constructor.
	 */
	public CommentJTS (Doc d,String s){ }
	
	// *** Methods
	/** Returns the whole comment string.
	 */
	String commentText() { return commentString; }
	
	/** Returns all the tags harvested.
	 */
	Tag tags()[] { return tags; }

	/** I assume that returns an array of the tags of a given name.
	 */
	Tag tags(String tagname)[] 
	{ 
		tagList = new LinkedList();
		
		// Adds the tags that have the name searched for.
		for(int i=0; i< tags.length; i++)
			if (tags[i].name().compareTo(tagname) == 0)
				tagList.add(tags[i]);
		
		// Creates the array to return them
		Tag[] arrayTags = new TagJTS[tagList.size()];
		
		for(int i=0; i< tagList.size(); i++)
			arrayTags[i] = (TagJTS) tagList.get(i); 
		
		return arrayTags; 
	}
	
	/** Returns all the throws tags harvested.
	 */
	public ThrowsTag throwsTags()[] { return throwsTags; }
	
	/** Returns all the param tags harvested.
	 */
	public ParamTag paramTags()[] { return paramTags; }
	
	/** Returns all the see tags harvested.
	 */
	public SeeTag seeTags()[] { return seeTags; }
	
	/** Returns all the serial field tags harvested.
	 */
	public SerialFieldTag serialFieldTags()[] { return serialFieldTags; }
	
	/** I have no idea what is this for )-:
	 * Current implementation returns empty string.
	 */
	static String localeSpecificFirstSentence(String s) { return ""; }
	
	/** I have no idea what is this for )-:
	 * Current implementation returns empty string.
	 */
	static String englishLanguageFirstSentence(String s) { return ""; }
	
	/** I have no idea what is this for )-:
	 * Current implementation returns false.
	 */
	static boolean htmlSentenceTerminatorFound(String s, int n) { return false; }
	
	/** Returns the inline Tags
	 */
	static Tag getInlineTags(Doc d, String s )[] { return new TagJTS[0]; }
	
	/** I have no idea what is this for )-:
	 * Current implementation returns 0.
	 */
	static int inlineTagImplFound(String s, int n) { return 0; }
	
	/** First sentence tags are the tags from header comment.
	 */
	static Tag firstSentenceTags(Doc d, String s)[] 
	{ return CommentParsing.CommentParser.computeFST(d,s); }
	
	/** I dont know what is this method supposed to returned.
	 * Current implementation returns empty string.
	 */
	public String toString() { return ""; }
    
	// I have no idea what is inside this static block.
	// static {}; 
	
	// ******************************************************************
	// *** Added fields and methods for additional functionality   ******
	
	// *** Tags array
	Tag[]              tags;
	ThrowsTag[]        throwsTags;
    ParamTag[]         paramTags;
	SeeTag[]           seeTags;
	SerialFieldTag[]   serialFieldTags;
	Tag[]              firstSentenceTags;
    Tag[]              inlineTags;
	
	/** Set the tags of the comment.
	 */
	public void setTags(Tag[] _tags) { tags = _tags; }
	
	/** Set the throws tags of the comment.
	 */
	public void setThrowsTag(ThrowsTag[] _throwsTags) 
	{ throwsTags = _throwsTags; }
	
	/** Set the param tags of the comment.
	 */
	public void setParamTag(ParamTag[] _paramTags)
	{ paramTags = _paramTags; }
	
	/** Set the see tags of the comment.
	 */
	public void setSeeTag(SeeTag[] _seeTags)
	{ seeTags = _seeTags; }
	
	/** Set the serial field tags of the comment.
	 */
	public void setSerialFieldTag(SerialFieldTag[] _serialFieldTags)
	{ serialFieldTags = _serialFieldTags; }
	
	/** Sets the commentString to be the whole text of a comment.
	 */
	public void setCommentString(String _commentString)
	{ commentString = _commentString; }
	
	/** Sets the text variable to be header the text of a javadoc comment.
	 */
	public void setText(String _text) { text = _text; }
	
	/** Sets the first sentence tags of the comment.
	 */
	public void setFirstSentenceTag(Tag[] _firstSentenceTags)
	{ firstSentenceTags = _firstSentenceTags; }
	
	/** Sets the inline tags of the comment.
	 */
	public void setInlineTag(Tag[] _inlineTags)
	{ inlineTags = _inlineTags; }
	
	/** Returns the text of a javadoc comment.
	 */
	public String text() { return text; }
	
	/** Returns the inline tags.
	 */
	public Tag inlineTags()[] 
	{ return inlineTags; }
	
   /** Returns all the tags */
	public Tag getTags()[]
	{ return tags; }
   
}