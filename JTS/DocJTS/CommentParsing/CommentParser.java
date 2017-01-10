/* JDoc project
 * CommentParser.java
 * University of Texas at Austin
 * Department of Computer Sciences
 * Product-line Architecture Research Group
 * @author Roberto E. Lopez-Herrejon
 * @version 1.0
 * @since October 15, 2001
*/

 package CommentParsing;

 import com.sun.tools.javadoc.*;
 import com.sun.javadoc.*;
 import jtsdoc.*;
 import java.util.*;

/** This class contains all comment parsing a la javadoc.
 * Recieves the different comments from.
 */
public class CommentParser
{
	
   // Contains the number of Different "types" of Tags
   public static final int NUM_TAG_TYPES = 5;
	   
   // Constants to access the indexes of outputTags 
   public static final int TagIndex = 0;
   public static final int SeeTagIndex = 1;
   public static final int ParamTagIndex = 2;
   public static final int ThrowsTagIndex = 3;
   public static final int SerialFieldTagIndex = 4;
	   
   /** Parsers a comment from a Class.
    */
	public static CommentJTS ParserCommentClass(Doc Document, String comment)
	{
		CommentJTS theComment = parseComment(Document, comment);			
		return theComment;
	}
	
   /** Parsers a comment from a Method.
    */
	public static CommentJTS ParserCommentMethod(Doc Document, String comment)
	{
		CommentJTS theComment = parseComment(Document, comment);
		return theComment;
	}	

	/** Parsers a comment from a Constructor
	 */
	public static CommentJTS ParserCommentConstructor(Doc Document, String comment)
	{
		CommentJTS theComment = parseComment(Document, comment);
		return theComment;
	}	
	
	/** Parsers a comment from a Field
     */
	public static CommentJTS ParserCommentField(Doc Document, String comment)
	{
		CommentJTS theComment = parseComment(Document, comment);
		return theComment;
	}	
	
	/** Parsers a comment from a Layer
     */
	public static CommentJTS ParserCommentLayer(Doc Document, String comment)
	{
		CommentJTS theComment = parseComment(Document, comment);
		return theComment;
	}	
	
	/** Parsers a comment from a State
     */
	public static CommentJTS ParserCommentState(Doc Document, String comment)
	{
		CommentJTS theComment = parseComment(Document, comment);
		return theComment;
	}		
	
/**************************************************************************/
/********************* Auxiliary Parsing Functions ************************/
/**************************************************************************/

 public static CommentJTS parseComment(Doc Document, String comment)
	{
	    // Auxiliary variables
	    CommentJTS theComment = null;
		String rawComment = comment;
		String text = "";
		
		// 1) Look for the start of the comment /*
		comment = findBegin(comment);
		
		// System.out.println("--- Remainder --- ");
        // System.out.println(comment);
		// System.out.println("----------------- ");
		
		
		// 2) Look for the header of the comment.
		String[] header = new String[2];
		header = findHeader(comment);
		
		/*
		System.out.println("--- Header ----");
		System.out.println(header[0]);
		System.out.println("----------------- ");
		*/
		
		comment = header[1];
		text = header[0];
		
		// 3) Parse each tag line one by one.
		// System.out.println("---- Tags -----");
		// System.out.println(comment);
		
		LinkedList tagList = findTags(comment);
		
		
		// for(int i=0; i< tagList.size(); i++)
		//  System.out.println("Tag -> " + (String) tagList.get(i));
		
		// System.out.println("---------------");
		
		
		// 4) Creates the tag objects for the comment
		LinkedList[] tagObjectsLists = createTags(Document, tagList);
		
		// 5) Creates the Comment Object
		theComment = new CommentJTS(Document, comment);
		
		// Sets the header comments and so forth
		theComment.setCommentString(rawComment);
		theComment.setText(text);
		
		// Sets the fields of the comment with the tags
		theComment.setTags(convertTags(tagObjectsLists[TagIndex]));
		theComment.setSeeTag(convertSeeTag(tagObjectsLists[SeeTagIndex]));
		theComment.setParamTag(convertParamTag(tagObjectsLists[ParamTagIndex]));
		theComment.setThrowsTag(convertThrowsTag(tagObjectsLists[ThrowsTagIndex]));
		theComment.setSerialFieldTag(convertSerialFieldTag(tagObjectsLists[SerialFieldTagIndex]));
		theComment.setFirstSentenceTag(computeFST(Document, text));
		theComment.setInlineTag(computeIT(Document, text));
		
		// Temporarly prints the tagobjects
		// System.out.println("\n Display General Tags");
		// displayTags(tagObjectsLists[TagIndex]);
		
		// System.out.println("\n Display See Tags");
		// displayTags(tagObjectsLists[SeeTagIndex]);
		
		// System.out.println("\n Display Param Tags");
		// displayTags(tagObjectsLists[ParamTagIndex]);
		
		// System.out.println("\n Display Throws Tags");
		// displayTags(tagObjectsLists[ThrowsTagIndex]);
		
		// System.out.println("\n Display Serial Field Tags");
		// displayTags(tagObjectsLists[SerialFieldTagIndex]);
		
		return theComment;
	}
 
 /** Computes the first setence tags for the comment header.
  */
 public static Tag[] computeFST(Doc Document, String stringTag)
 {
	StringTokenizer tokens = new StringTokenizer(stringTag," \t\n\r\f.@{}",true);
	LinkedList FirstSentenceTags = FirstSentence(tokens, Document);	  
    return convertTags(FirstSentenceTags);
 }
 
 /** Computes the inline tags for the comment header.
  */
 public static Tag[] computeIT(Doc Document, String stringTag)
 {
	StringTokenizer tokens = new StringTokenizer(stringTag," \t\n\r\f.@{}",true);
	LinkedList inlineTags = RestComment(tokens, Document);
	
	/*
    System.out.println("\n Display Inline Tags of " + stringTag);
	displayTags(inlineTags);
	System.out.println("????????????????????????");
	*/
	
    return convertTags(inlineTags);	 
 }
 
 // ***** List to array conversion rutines
 public static Tag[] convertTags(LinkedList tags)
 {
	 Tag[] arrayTags = new TagJTS[tags.size()];
	 
	 for(int i=0; i< tags.size(); i++)
		 arrayTags[i] = (TagJTS)tags.get(i);
	 
	 return arrayTags;
 }
 
 public static SeeTag[] convertSeeTag(LinkedList tags)
 {
	 SeeTag[] arrayTags = new SeeTagJTS[tags.size()];
	 
	 for(int i=0; i< tags.size(); i++)
		 arrayTags[i] = (SeeTagJTS)tags.get(i);
	 
	 return arrayTags;	 
 }
 
 public static ParamTag[] convertParamTag(LinkedList tags)
 {
	 ParamTag[] arrayTags = new ParamTagJTS[tags.size()];
	 
	 for(int i=0; i< tags.size(); i++)
		 arrayTags[i] = (ParamTagJTS)tags.get(i);
	 
	 return arrayTags;	 	 
 }
 
 public static ThrowsTag[] convertThrowsTag(LinkedList tags)
 {
	 ThrowsTag[] arrayTags = new ThrowsTagJTS[tags.size()];
	 
	 for(int i=0; i< tags.size(); i++)
		 arrayTags[i] = (ThrowsTagJTS)tags.get(i);
	 
	 return arrayTags;	 	 
 }
 
 public static SerialFieldTag[] convertSerialFieldTag(LinkedList tags)
 {
	 SerialFieldTag[] arrayTags = new SerialFieldTagJTS[tags.size()];
	 
	 for(int i=0; i< tags.size(); i++)
		 arrayTags[i] = (SerialFieldTagJTS)tags.get(i);
	 
	 return arrayTags;	 	 
 }
 
 /** Temporary method. Displays the contents of a Tag.
  */
 public static void displayTags(LinkedList tagsList)
 {
	 for (int i=0; i< tagsList.size(); i++)
	 {
		 TagJTS tag = (TagJTS) tagsList.get(i);
		 System.out.println("-----------------------------");
		 System.out.println("Name " + tag.name());
		 System.out.println("Kind " + tag.kind());
		 System.out.println("Text " + tag.text());
		 
		 System.out.println("First Sentence Tags");
		 Tag[] fsTags = tag.firstSentenceTags();
		 if (fsTags != null)
		  for(int j=0; i< fsTags.length ; j++)
		  {	 
		    System.out.println("Name " + fsTags[j].name());
		    System.out.println("Kind " + fsTags[j].kind());
		    System.out.println("Text " + fsTags[j].text());
		  };
		 
		 System.out.println("Inline Tags");
		 Tag[] inTags = tag.inlineTags();
		 if (inTags != null)
		  for(int j=0; j< inTags.length ; j++)
		  {	 
		    System.out.println("Name " + inTags[j].name());
		    System.out.println("Kind " + inTags[j].kind());
		    System.out.println("Text " + inTags[j].text());
		 };
		  
		 System.out.println(" ");
	 }
 }
 
 public static void displayATag(Tag tag)
 {
		 System.out.println("-----------------------------");
		 System.out.println("Name " + tag.name());
		 System.out.println("Kind " + tag.kind());
		 System.out.println("Text " + tag.text());
		 System.out.println("First Sentence Tags");

		 Tag[] fsTags = tag.firstSentenceTags();
		 for(int i=0; i< fsTags.length; i++)
		 {	 
		   System.out.println("Name " + fsTags[i].name());
		   System.out.println("Kind " + fsTags[i].kind());
		   System.out.println("Text " + fsTags[i].text());
		 }
		 System.out.println("Inline Tags");
		 Tag[] inTags = tag.inlineTags();
		 for(int i=0; i< inTags.length; i++)
		 {	 
		   System.out.println("Name " + inTags[i].name());
		   System.out.println("Kind " + inTags[i].kind());
		   System.out.println("Text " + inTags[i].text());
		 }
		 
 }
 
 // *****************************************************************************
   /** Finds the /** in the comment string and returns the remainder of the 
    * comment
    */
   public static String findBegin(String comment)
   {
	   	String[] line;    // line[0] is the line to parse
        String aLine ="";     // is the remaining part of the comment 
		String commentStart;
		
		// *******************
		boolean found = false;
		String remainder = "";
		String token ="";
		StringTokenizer parser = new StringTokenizer(comment,"\n",false);
		
		while(parser.hasMoreTokens()==true && found == false)
		{
			aLine = parser.nextToken();
			
		   // Trims the line of white spaces at beginning or end
		   aLine = aLine.trim();
		   
		   // Searches the /** in the line just read
		   found = searchStart(aLine);
		   // System.out.println("Line -> " + aLine + " found " + found);
		}
		
		// if the begin was found then obtain the rest of the comment
		if (found == true)
		{
			int position = aLine.indexOf("/**");
			remainder = aLine.substring(position+3);
			// System.out.println("First remainder " + remainder);
			
			while(parser.hasMoreTokens())
			{
				token = parser.nextToken();
				// System.out.println("Token -> " + token);
				remainder = remainder + "\n" + token;
			}
			// System.out.println("Last remainder " + remainder);
		}
		
		return remainder;
		
		// ****************************
		
		/*
		// Parse line by line while we have not reach the end of the 
		// comment or we have found the /** characters
		do
		{
		   line = nextLine(comment);
		   System.out.println("Line -> " + line[0]);
		   aLine = line[0];
		   comment = line[1]; // the rest of the comment
		   
		   // Trims the line of white spaces at beginning or end
		   aLine = aLine.trim();
		   
		   // Searches the /** in the line just read
		   commentStart = searchStart(aLine);
		   // System.out.println("Aline " + aLine + " start? " + commentStart);
		   
		}while(line[1]!=null && commentStart==null);
	
	   if (commentStart !=null) 
		   comment = commentStart + "\n" + comment;	
	   */
		
	   // return comment;	
   } // findBegin
   
   /** Searches the /** in the comment line and returns true if it is the
    * beginning otherwhise returns false.
    */
   static boolean searchStart(String line)
   {
	   int position = line.indexOf("/**");
	   
	   // System.out.print("Line " + line + " position " + position);
	   
	   // if it is not on the line return null
	   if (position == -1) return false;
	   
	   // Check from position 0 to where the /** was found
	   // See if there are no // at the beginning of the line
	   int slash_slash = line.indexOf("//");
	   
	   // System.out.println(" slash " + slash_slash);
	   
	   // The /** is part of a comment so ignore it
	   if (slash_slash < position && slash_slash!=-1 ) 
		   return false;
	   
	   return true;
	   /*
	   // for all the characters in the line
	   for (int i=0; i< position - 1; i++)
	   { 
		   char theCharacter = line.charAt(i);
		   if (theCharacter!=' ' && theCharacter!='\n')
		     return null;
	   }
	   
	   return line.substring(position+3);
	   */
   }
   
   /** Returns the header string and the rest of the comment.
    */
   public static String[] findHeader(String comment)
   {
     String[] result = new String[2];
	 String[] line = new String[2];
	 String aLine, commentStart;
	 String header = "";      // contains the header of the comment
	 String rest = "";        // contains the rest of the comment
	 
	 // Checks to see if we have something in the comment
	 if (comment == null)
	 {
		 result[0]=null; result[1]=null;
		 return result;
	 }
	
	 do
		{
		   line = nextLine(comment);
		   // System.out.println("Line -> " + line[0]);
		   aLine = line[0];
		   comment = line[1]; // the rest of the comment
		   
		   // If there is nothing to parse exit
		   if (aLine == null) break;
		   
		   // Trims the line of white spaces at beginning or end
		   aLine = aLine.trim();
		   // System.out.println("Trimmed line " + aLine);
		     
		   // Removes leading asteriscs * 
		   commentStart = removeAsterisc(aLine);
	       // System.out.println("Print comment -> " + commentStart);
		   
		   // The rest of the comment is comment
		   rest = comment;
		   
		   // if the line is a tag line that's the end of the header
		   // and return
		   if (isTag(commentStart)) 
		   {
			   rest = commentStart + "\n" + rest;
			   break; 
		   }

		   // Checks to see if the line has the */ if so that's the
		   // end of the comment, ignore the rest.
		   if (containsEnd(aLine))
		   {
			   line[1] = null;
			   // System.out.println("Contains end");
		   }
		   else
		   {  // add the trim line to the header
		      header = header + " " + commentStart;
		   }
		   
		}while(line[1]!=null);
		
	 // Sets the values of the result	
	 result[0] = header;
	 result[1] = rest;
	 
	 return result;
   }
   
   
   /** Checks to see if we have the * and /
    */
   public static boolean containsEnd(String line)
   {
	   return (line.lastIndexOf("*/")!=-1);
   }
   
   /** Removes the leading asterics * from the header.
    */
   public static String removeAsterisc(String comment)
   {
	   // for all the characters in the line
	   int i=0;
	   
	   // Finds the last leading asterisk
	   for(i=0; i< comment.length() && comment.charAt(i) =='*'; i++);
	   
	   // Checks that after the leading asterisks there is no  /
	   // to end the comment. If so then remove all but last *
	   if (i < comment.length() && comment.charAt(i)=='/')
		   i = i-1;
	   
	   // Removes the *
	   comment = comment.substring(i);
	   
	   // Removes any trailing spaces
	   comment = comment.trim();
	   
	   return comment;
	   
   }
   
   /** Checks that the line is a tag line @tag
    */
   public static boolean isTag(String comment)
   {
	   boolean result = false;
	   String word = "";
	   
	   int i=0;
	   if (comment==null || comment.length()==0) 
		   return false;
	   do
	   {
		 char theCharacter = comment.charAt(i);
		 if (Character.isWhitespace(theCharacter)==true)
			 break;
		 
		 // Gets the word that may include the tag
		 word = word + theCharacter;
		 i++;   
	   }while(i< comment.length());	   
	   
	   // Checks to see if the first word is a Tag
	   result = word.startsWith("@");
	   
	   return result;
   }
   
 /** Returns a lists of strings each one of them corresponds to a tag line.
   * Watches out for the ending of the comment as well.
   */

   public static LinkedList findTags(String comment)
   {
     LinkedList tagsList = new LinkedList();
	 
     // String[] result = new String[2];
	 // String[] line = new String[2];
	 String aLine, tagLine;
	 // String header = "";      // contains the header of the comment
	 // String rest = "";        // contains the rest of the comment
	 String currentTag = "";  // contains a string of possibly many lines
     boolean endFound = false;
	 
	 // Checks to see if there are remaining comments
	 if (comment==null)
		 return tagsList;
	 
	 // Tokenizes line by line the Tags section of the comment
	 StringTokenizer tagParser = new StringTokenizer(comment, "\n",false);
	 
	 // While there are more tokens to parse
	 while (tagParser.hasMoreTokens() && endFound == false)
	 {
	   // Read the line	 
       aLine = tagParser.nextToken();
		 
	   // Trims the line of white spaces at beginning or end
	   aLine = aLine.trim();	
	   
   	   // Removes leading asteriscs * 
	   tagLine = removeAsterisc(aLine);
	   
   	   // Checks to see if the tagLine contains the * and /
	   if (containsEnd(tagLine)) 
		   endFound = true;
	   
	   // if the line is a tag line then insert currentTag if not empty
	   // make the currentTag the tagLine
	   if (isTag(tagLine)) 
	   {
		   if (currentTag.length()>0) 
			   tagsList.add(currentTag);
		   currentTag = tagLine;	       
		}
		else // it is not a new tag line it is part of a previous tag line
		{
			// If we have not found the end of the comment
			if (endFound == false)
			  currentTag = currentTag + "\n" + tagLine;
		}
	 } // while there is something to parse and have not found * and /

	 // If there is a remainder tag add it to the list
     if (currentTag.length() > 0) tagsList.add(currentTag);
	   
	 return tagsList;
   }
     
/**************************************************************************/
/**************************************************************************/
// *********************** TAG PARSING   **********************************/   

   /** This method receives as input a Linked List where each element
    * is a String that corresponds to a tag Line.
    * The output of the method is a Linked List with a tag object for each
    * of the input Strings.
    */
   public static LinkedList[] createTags(Doc Document, LinkedList tagList)
   {
	   /* List that will contain the Tag objects.
		outputTags[0] => is the list of all Tags.
	    outputTags[1] => is the list of SeeTags.
	    outputTags[2] => is the list of ParamTags.
	    outputTags[3] => is the list of ThrowsTags.
	    outputTags[4] => is the list of SerialFieldTags.
	   */
	   LinkedList[] outputTags = new LinkedList[NUM_TAG_TYPES];
	   outputTags[0] = new LinkedList();
	   outputTags[1] = new LinkedList();
	   outputTags[2] = new LinkedList();
	   outputTags[3] = new LinkedList();
	   outputTags[4] = new LinkedList();

	   // For all the tags in the input list
	   for (int i=0; i< tagList.size(); i++)
	   {
		   // Tag that is going to be added to the returned list
		   Tag currentTag;
		   
		   // Tag string of current list element
		   String stringTag = (String) tagList.get(i);
		   
		   /*
		     TagDeclaration ->  SeeTag		| DocRootTag		| SinceTag		|
								ParamTag	| DeprecatedTag		| SerialTag		|
								ThrowsTag	| ExceptionTag		| SerialDataTag |
								SerialFieldTag | LinkTag		| VersionTag	|
								AuthorTag	| ReturnTag			| OtherTag
		   */

		   // SeeTag
		   if (stringTag.startsWith("@see "))
		   {
			   // Parses a @param tag and adds it to general tags and param tags
			   // lists
			   SeeTagJTS see = parseSeeTag(Document, stringTag);
			   outputTags[TagIndex].add(see);
			   outputTags[SeeTagIndex].add(see);
			   continue;
		   }
		   
		   // ParamTag
		   if (stringTag.startsWith("@param "))
		   {
			   // Parses a @param tag and adds it to general tags and param tags
			   // lists
			   ParamTagJTS param = parseParamTag(Document, stringTag);
			   outputTags[TagIndex].add(param);
			   outputTags[ParamTagIndex].add(param);
			   continue;
		   }
		   
		   // ThrowsTag
		   if (stringTag.startsWith("@throws "))
		   {
			   // Parses a @throws tag and adds it to general tags and throws tags
			   // lists
			   ThrowsTagJTS throwsTag = parseThrowsTag(Document, stringTag);
			   outputTags[TagIndex].add(throwsTag);
			   outputTags[ThrowsTagIndex].add(throwsTag);
			   continue;
		   }
		   
		   // SerialFieldTag
		   if (stringTag.startsWith("@serialField "))
		   {
			   // Parses a @serialField tag and adds it to general tags and 
			   // throws tags lists
			   SerialFieldTagJTS serialFieldTag = parseSerialFieldTag(Document, stringTag);
			   outputTags[TagIndex].add(serialFieldTag);
			   outputTags[SerialFieldTagIndex].add(serialFieldTag);
			   continue;
		   }		   
		   
		   // AuthorTag
		   if (stringTag.startsWith("@author "))
		   {
			   // Parses a @author tag and adds it to general tags
			   TagJTS authorTag = parseAuthorTag(Document, stringTag);
			   outputTags[TagIndex].add(authorTag);
			   continue;
		   }		   

		   // DocRootTag   -> Pending
		   // it is an special case since it is not located at the beginning of
		   // a line.
		   
		   // Deprecated
		   if (stringTag.startsWith("@deprecated "))
		   {
			   // Parses a @deprecated tag and adds it to general tags
			   TagJTS deprecatedTag = parseDeprecatedTag(Document, stringTag);
			   outputTags[TagIndex].add(deprecatedTag);
			   continue;
		   }		
		   
		   // ExceptionTag equivalent to @throws
		   if (stringTag.startsWith("@exception "))
		   {
			   // Parses an @exception tag and adds it to general tags list
			   // and to the throws list
  			   ThrowsTagJTS throwsTag = parseThrowsTag(Document, stringTag);
			   outputTags[TagIndex].add(throwsTag);
			   outputTags[ThrowsTagIndex].add(throwsTag);
			   continue;
		   }		
		   
		   // LinkTag is an special case and it cannot be at the beginning of a 
		   // tag line
		   
		   // ReturnTag
		   if (stringTag.startsWith("@return "))
		   {
			   // Parses a @return tag and adds it to general tags
			   TagJTS returnTag = parseReturnTag(Document, stringTag);
			   outputTags[TagIndex].add(returnTag);
			   continue;
		   }			   
		   
		   // SinceTag
		   if (stringTag.startsWith("@since "))
		   {
			   // Parses a @since tag and adds it to general tags
			   TagJTS sinceTag = parseSinceTag(Document, stringTag);
			   outputTags[TagIndex].add(sinceTag);
			   continue;
		   }			   
		   
		   // SerialTag -> Pending review to see if it also is included in 
		   // SerialFieldTags list
		   if (stringTag.startsWith("@serial "))
		   {
			   // Parses a @serial tag and adds it to general tags
			   TagJTS serialTag = parseSerialTag(Document, stringTag);
			   outputTags[TagIndex].add(serialTag);
			   continue;
		   }			   
		   
		   // SerialDataTag -> Pending review to see if it also is included in 
		   // SerialFieldTags list
		   if (stringTag.startsWith("@serialData "))
		   {
			   // Parses a @serialData tag and adds it to general tags
			   TagJTS serialDataTag = parseSerialDataTag(Document, stringTag);
			   outputTags[TagIndex].add(serialDataTag);
			   continue;
		   }
		   
		   // VersionTag
		   if (stringTag.startsWith("@version "))
		   {
			   // Parses a @version tag and adds it to general tags
			   TagJTS versionTag = parseVersionTag(Document, stringTag);
			   outputTags[TagIndex].add(versionTag);
			   continue;
		   }			   		   
		   
		   // At this point any non-standard tag is captured
		   // OtherTag
		   TagJTS otherTag = parseOtherTag(Document, stringTag);
		   outputTags[TagIndex].add(otherTag);
		   
	   } // for all the tags in the list of Strings
	   
	   // Returns the tag objects found
	   return outputTags;
   }

  // *******************************************************************************   
   /** Method that parses and builds a SeeTag.
    *  SeeTag -> "@see" Reference
    *  Reference -> \" String \"
    *            -> "<a href=" \" URL[#Member] ">" label "</a>"
    *            -> [package][class]#Member[ArgumentList]
    */
   public static SeeTagJTS parseSeeTag(Doc Document, String stringTag)
   {
	   SeeTagJTS seeTag = new SeeTagJTS(Document,"","");
	   
	   String token = "";
	   StringTokenizer tokens = new StringTokenizer(stringTag," \"</>", true);

	   // Eats @param and following white space.
	   if (tokens.hasMoreTokens()) 
		  token = tokens.nextToken();
	   if (tokens.hasMoreTokens())
		  token = tokens.nextToken();
	   
	   // Finds out what type reference we are dealing with
	   if (tokens.hasMoreTokens())
	   {
		   token = tokens.nextToken();
		   
		   // If the see tag is an string reference.
		   if (token.compareTo("\"")==0) 
		   {  seeTag = ParseString(Document, tokens); }
		   else
		   { // If the see tag is a ULR reference.
		    if (token.compareTo("<")==0)
			{ seeTag = ParseURL(Document, tokens);}
			else
				seeTag = ParseMember(Document, stringTag);
		   }
	   }
	   return seeTag;
   }
   
   /** Parses a String for a see tag, and returns the corresponding object.
    */
   public static SeeTagJTS ParseString(Doc Document, StringTokenizer tokens)
   {
	   // Auxiliary variables
       String token = "";
	   String text = "";
	   SeeTagJTS seeTag;
	   
	   // Reads all the string until it finds the closing "
	   while(tokens.hasMoreTokens())
	   {
          token = tokens.nextToken();
		  
		  // if the " is found that's the end of the string.
		  if (token.compareTo("\"")==0)
			  break;
		  
		  // Captures the string.
		  text = text + token;
	   }
	   
	   text = text.trim();
	   
	   // Creates the SeeTag object
	   seeTag = new SeeTagJTS(Document, "\"" + text + "\"", "@see");
	   seeTag.setKind("@see");
	   
	   // Sets the First Sentence tags array to be the text of the string.
	   TagJTS[] arrayTags = new TagJTS[1];
	   arrayTags[0] = new TagJTS(Document, "\"" + text + "\"","Text");
	   arrayTags[0].setFirstSentenceTags(new TagJTS[0]);
	   arrayTags[0].setInlineTags(new TagJTS[0]);
	   seeTag.setFirstSentenceTags(arrayTags);   
	   
	   // Sets the Inline tags array to be the text of the string
	   TagJTS[] inTags = new TagJTS[1];
	   inTags[0] = new TagJTS(Document, "\"" + text + "\"", "Text");
	   inTags[0].setFirstSentenceTags(new TagJTS[0]);
	   inTags[0].setInlineTags(new TagJTS[0]);
       seeTag.setInlineTags(inTags);    
	   
	   // Temporary, for test purposes
	   // System.out.println("See Text = " + text);
	   
	   // Returns the see tag.
	   return seeTag;
   }
 
   /** Parses a URL for a see tag, and returns the corresponding object.
    */
   public static SeeTagJTS ParseURL(Doc Document, StringTokenizer tokens)
   {
	   // Auxiliary variables
       String token = "";
	   String anchor = "<";
	   SeeTagJTS seeTag;
	   
	   // Reads all the string
	   while(tokens.hasMoreTokens())
	   {
          token = tokens.nextToken();
		  
		  // Captures the string.
		  anchor = anchor + token;	
	   }
	   
	   anchor = anchor.trim();
	   
	   // Creates the SeeTag object
	   seeTag = new SeeTagJTS(Document, anchor, "@see");
	   seeTag.setKind("@see");
	   
	   // Sets the First sentence array to be the text of the string.
	   TagJTS[] arrayTags = new TagJTS[1];
	   arrayTags[0] = new TagJTS(Document, anchor, "Text");
	   arrayTags[0].setFirstSentenceTags(new TagJTS[0]);
	   arrayTags[0].setInlineTags(new TagJTS[0]);
	   seeTag.setFirstSentenceTags(arrayTags);   	   
	  
	   // Sets the inline tags to be an empty string
	   TagJTS[] inTags = new TagJTS[1];
	   inTags[0] = new TagJTS(Document, anchor, "Text");
	   inTags[0].setFirstSentenceTags(new TagJTS[0]);
	   inTags[0].setInlineTags(new TagJTS[0]);
       seeTag.setInlineTags(inTags);    	   
	   
	   // Temporary for testing only
	   // System.out.println("Anchor " + anchor);
	   
	   // Returns the see tag.
	   return seeTag;	   
   }
   
   
   /** Parses a reference member of a see tag.
    *   -> package.class#member label
    *  Current Class Member
    *   #field
    *   #method(Type, Type, ...)
    *   #method(Type argname, Type argname, ...)
    * 
    *  Another Class in the current or imported packages
    *   Class#field
    *   Class#method(Type, Type, ...)
    *   Class#method(Type argname, Type argname, ...)
    *   Class
    *  
    *  Referencing another package (fully qualified)
    *   package.Class#field 
    *   package.Class#method(Type, Type, ...)
    *   package.Class#method(Type argname, Type argname, ...)
    *   package.Class
    *   package
    */
   public static SeeTagJTS ParseMember(Doc Document, String stringTag)
   {   // Auxiliary variables
       String token = "";
	   SeeTagJTS seeTag;
	   String packageClass = "";             // Everything before #
	   String[] memberName = new String[3];  // member &signature & label
	   String signature = "";                // signature method/const
	   String label ="";
	   String name = "";
	   
       // Variable initialization
	   memberName[0] = ""; memberName[1] = ""; memberName[2] = "";
	   
	   // Trims of leading white spaces
	   stringTag = stringTag.trim();
	   
	   // Creates the StringTokenizer
	   String delims = " #(),";
	   StringTokenizer tokens = new StringTokenizer(stringTag,delims, true);
	   
	   // Eats the @see tag and the following white space
	   if (tokens.hasMoreTokens()) token = tokens.nextToken();
	   if (tokens.hasMoreTokens()) token = tokens.nextToken();
	   
	   // Reads the packageClass name
	   if (tokens.hasMoreTokens()) packageClass = tokens.nextToken();
	   if (packageClass.equals("#")) packageClass = "";
	   	   
	   // Reads the member name, signature, label
	   memberName = ParseMemberDescription(tokens);
	   
	   /*
       while(tokens.hasMoreTokens())
	   {
		   token = tokens.nextToken();
		   
		   // If the # is found now parse the rest
		   if (token.compareTo("#")==0)
		   {
			   memberName = ParseMemberDescription(tokens);
			   break;
		   }
		   
		   // This is part of the Class/Package name
		   packageClass = packageClass + token;
	   }
	   */
	   
	   // If this is a method/constructor then obtains the signature
	   // and trims the member name.
	   /*
	   if (!memberName.equals("")) 
	   { 
		   int pos1 = memberName.indexOf("(");
		   int pos2 = memberName.indexOf(")");
		   if (pos1>=0 && pos2>=0 && pos2>pos1)  
		   {
			   signature = memberName.substring(pos1+1,pos2);
			   memberName = memberName.substring(0,pos1);
		   }
	   }
	   */
	   
	   packageClass = packageClass.trim();
	   name = memberName[0];
	   signature = memberName[1];
	   label = memberName[2];
	   
	   // Temporary display for output
	   // System.out.println("PClass -> " + packageClass + " memberName-> " 
	   //					  + name + " Signature -> " + signature
	   //					  + " label -> " + label);
	   
   	   // Creates the SeeTag object
	   seeTag = new SeeTagJTS(Document, packageClass, "@see");
	   seeTag.setKind("@see");
	   
	   // Computes the tag's text
	   String wholeLine = "";
	   StringTokenizer tagTokens = new StringTokenizer(stringTag,delims, true);
	   
	   // Eats the @see tag and the following white space
	   if (tagTokens.hasMoreTokens()) wholeLine = tagTokens.nextToken();
	   if (tagTokens.hasMoreTokens()) wholeLine = tagTokens.nextToken();
	   
	   wholeLine ="";
	   while(tagTokens.hasMoreTokens()) 
		   wholeLine = wholeLine + tagTokens.nextToken();
	   
	   // Sets the First sentence array to be the text of the string.
	   TagJTS[] arrayTags = new TagJTS[1];
	   arrayTags[0] = new TagJTS(Document, wholeLine, "Text");
	   arrayTags[0].setFirstSentenceTags(new TagJTS[0]);
	   arrayTags[0].setInlineTags(new TagJTS[0]);
	   seeTag.setFirstSentenceTags(arrayTags);   	   
	  	  
	   // Sets the inline tags to be an empty string
	   TagJTS[] inTags = new TagJTS[1];
	   inTags[0] = new TagJTS(Document, wholeLine, "Text");
	   inTags[0].setFirstSentenceTags(new TagJTS[0]);
	   inTags[0].setInlineTags(new TagJTS[0]);
       seeTag.setInlineTags(inTags);    	   	   
	   
	   // Sets the label
	   seeTag.setLabel(label);
	   
	   // Sets the class name
	   seeTag.setReferencedClassName(packageClass);
	   
	   // Sets the member name
	   seeTag.setReferencedMemberName(name + signature);
	   
	   // MemberDoc, Package, ClassDoc are going to be set
	   // on the second pass of the document generation
	   
	   return seeTag;
   }
   
   /** Parses the Member description, that is, returns the member name     
    *  , signature, and the label.
    */
  public static String[] ParseMemberDescription(StringTokenizer tokens)
  {
	  String label = "";
	  String signature = "";
	  String[] member = new String[3];
	  String token = "";
	  boolean parenFound = false;
	  
	  // Variable initialization
	  member[0] = "";       // member name
	  member[1] = "";       // signature
	  member[2] = "";       // label
 	  
	  // Parser the member name if any which is the first word
	  if (tokens.hasMoreTokens())
	  {
		token = tokens.nextToken();
		if (token.equals("#"))
		{
			if (tokens.hasMoreTokens()) 
				token = tokens.nextToken();
		}
		
		member[0] = token;
	  }
	  
	  // Parsers the signature and possibly the label
	  while(tokens.hasMoreTokens())
	  {
	      token = tokens.nextToken();
		  
		  // if the open parenthesis is found read until close
		  // parenthesis is found
  		  if (token.equals("("))
		  {
			parenFound = true;
			while(tokens.hasMoreTokens())
			{
				token = tokens.nextToken();
				if (token.equals(")")) break;
				signature = signature + token;
			}
		  }
		  
		  if (!token.equals(")"))
		    label = label + token;
	  };
	   
	  // Sets the parenthesis in the signature if method/constructor/layer 
	   if (parenFound) 
		   signature = "(" + signature + ")";
	  
	   // Sets the signature and label string
	  member[1] = signature;
	  member[2] = label;
	  
	  return member;
  }
  
   /** Method that creates a new Inline Tag of the type TagType.
   */
  public static SeeTagJTS newSeeTag(String TagType, String inlineTagText,
									Doc Document)
  {
	  SeeTagJTS seeTag = new SeeTagJTS(Document, inlineTagText,TagType);
	  seeTag.setKind("@see");
	  return seeTag;
  }
  // *******************************************************************************
 
   /** Method that parses and builds a ParamTag.
    * ParamTag -> @param parameter_name parameter_description
    */
   public static ParamTagJTS parseParamTag(Doc Document, String stringTag)
   {
   	   String parameterName = "";
   	   String token ="";
	   StringTokenizer tokens = new StringTokenizer(stringTag," ",true);

	   // Eats @param and following white space.
	   if (tokens.hasMoreTokens()) 
		  token = tokens.nextToken();
	   if (tokens.hasMoreTokens())
		  token = tokens.nextToken();
	   
	   // Reads the parameter name and the following white space
	   if (tokens.hasMoreTokens())
			parameterName = tokens.nextToken();		
       if (tokens.hasMoreTokens())
		   token = tokens.nextToken();
	   
	   // Separates the parameter description part.
	   token = "";
	   while(tokens.hasMoreTokens())
	   { token = token + tokens.nextToken(); }
	   
	   // Parses the description part of the tag.
   	   TagJTS tempTag =  ParseDescription(Document, "@dummy " + parameterName + " " + token, "@param");
	   
	   // Creates the ParamTag from the parts of tempTag
       ParamTagJTS paramTag = new ParamTagJTS(Document, tempTag.text(), tempTag.name());
	   paramTag.setKind(tempTag.kind());
	   paramTag.setParameterName(parameterName);
	   paramTag.setParameterComment(token);
	   paramTag.setFirstSentenceTags(tempTag.firstSentenceTags());
	   paramTag.setInlineTags(tempTag.inlineTags());
	   
	   return paramTag;
   }
   
   /** Method that parses and builds a ThrowsTag.
    * ThrowsTag -> @throws class_name class_description
    */
   public static ThrowsTagJTS parseThrowsTag(Doc Document, String stringTag)
   {
	   String className = "";
   	   String token ="";
	   StringTokenizer tokens = new StringTokenizer(stringTag," ",true);

	   // Eats @throws and following white space.
	   if (tokens.hasMoreTokens()) 
		  token = tokens.nextToken();
	   if (tokens.hasMoreTokens())
		  token = tokens.nextToken();
	   
	   // Reads the parameter name and the following white space
	   if (tokens.hasMoreTokens())
			className = tokens.nextToken();		
       if (tokens.hasMoreTokens())
		   token = tokens.nextToken();
	   
	   // Separates the parameter description part.
	   token = "";
	   while(tokens.hasMoreTokens())
	   { token = token + tokens.nextToken(); }
	   
	   // Parses the description part of the tag.
   	   TagJTS tempTag =  ParseDescription(Document, "@dummy " + className + " " + token, "@throws");
	   
	   // Creates the ParamTag from the parts of tempTag
       ThrowsTagJTS throwsTag = new ThrowsTagJTS(Document, tempTag.text(), tempTag.name());
	   throwsTag.setKind(tempTag.kind());
	   throwsTag.setExceptionName(className);
	   throwsTag.setExceptionComment(token);
	   throwsTag.setFirstSentenceTags(tempTag.firstSentenceTags());
	   throwsTag.setInlineTags(tempTag.inlineTags());
	   	   
	   return throwsTag;
   }   
   
    /** Method that parses and builds a SerialFieldTag.
    * SerialFieldTag -> @serialField field_name field_type field_description
    */
   public static SerialFieldTagJTS parseSerialFieldTag(Doc Document, String stringTag)
   {
		SerialFieldTagJTS serialFieldTag = new SerialFieldTagJTS(Document,"","");
		return serialFieldTag;
   }     

  /** Method that parses and builds an AuthorTag.
    * AuthorTag -> @author FirstSentence [RestComment]
    */
   public static TagJTS parseAuthorTag(Doc Document, String stringTag)
   {
	   	TagJTS authorTag =  ParseDescription(Document, stringTag, "@author");
		return authorTag;
   }     
  
  // *******************************************************************************   
  /** Method that parses and builds a DeprecatedTag.
   * DeprecatedTag -> @deprecated deprecated_text
   */
   public static TagJTS parseDeprecatedTag(Doc Document, String stringTag)
   {
	  TagJTS deprecatedTag =  ParseDescription(Document, stringTag, "@deprecated");
	  return deprecatedTag;
   }  	   
   
   /** Method that parses and builds an ExceptionTag, equivalent to ThrowsTag
   * ExceptionTag -> @exception class_name description 
   */
   public static ThrowsTagJTS parseExceptionTag(Doc Document, String stringTag)
   {
      return parseThrowsTag(Document, stringTag);
   }  	   
   
   /** Method that parses and builds a ReturnTag.
   * ReturnTag -> @return description
   */
   public static TagJTS parseReturnTag(Doc Document, String stringTag)
   {
	   TagJTS returnTag = ParseDescription(Document, stringTag, "@return");
	   return returnTag;	
   }  	   
   
   /** Method that parses and builds a SinceTag.
   * SinceTag -> @since since-text
   */
   public static TagJTS parseSinceTag(Doc Document, String stringTag)
   {
		TagJTS sinceTag =  ParseDescription(Document, stringTag, "@since");
		return sinceTag;
   }  	    
 
   /** Method that parses and builds a SerialTag. -> pending review
   * SerialTag -> @serial field_description
   */
   public static TagJTS parseSerialTag(Doc Document, String stringTag)
   {
		TagJTS serialTag = new TagJTS(Document,"","");
		return serialTag;
   }  	       

   /** Method that parses and builds a SerialDataTag. -> pending review
   * SerialDataTag -> @serialData data_description
   */
   public static TagJTS parseSerialDataTag(Doc Document, String stringTag)
   {
		TagJTS serialDataTag = new TagJTS(Document,"","");
		return serialDataTag;
   }  	       
   
   /** Method that parses and builds a VersionTag.
   * VersionTag -> @version version-text
   */
   public static TagJTS parseVersionTag(Doc Document, String stringTag)
   {
		TagJTS versionTag =  ParseDescription(Document, stringTag, "@version");
		return versionTag;
   }  
   
   /** Method that parses and builds a OtherTag.
   * OtherTag -> @tagname description
   */
   public static TagJTS parseOtherTag(Doc Document, String stringTag)
   {
	   String tagName = "";
	   StringTokenizer tokens = new StringTokenizer(stringTag," \n\t",true);

	   // Eats @throws and following white space.
	   if (tokens.hasMoreTokens()) 
		  tagName = tokens.nextToken();
	   
	   TagJTS otherTag = ParseDescription(Document, stringTag, tagName);
	   return otherTag;
   }     

/**************************************************************************/
/**************************************************************************/
/********************* GENERAL PARSING ROUTINES  **************************/   
   
 /** Parse a description tag of the 
  */  
 public static TagJTS ParseDescription(Doc Document, String stringTag, 
									   String TagType)
 {   
		String token ="";
		StringTokenizer tokens = new StringTokenizer(stringTag," \t\n\r\f.@{}",true);

		// Eats @ and TagName
		if (tokens.hasMoreTokens()) 
			token = tokens.nextToken();
		if (tokens.hasMoreTokens())
			token = tokens.nextToken();		
		
		// ********* Tag full Text
		StringTokenizer fullTokens = new StringTokenizer(stringTag," \t\n\r\f.@{}",true);
		String fullText = "";
		
		// Eats @ and TagName
		if (tokens.hasMoreTokens()) 
			token = fullTokens.nextToken();
		if (tokens.hasMoreTokens())
			token = fullTokens.nextToken();
		
		while (fullTokens.hasMoreTokens())
			fullText = fullText + fullTokens.nextToken();
				
		// FirstSentence -> Element* ["."]
		LinkedList FirstSentenceTags = FirstSentence(tokens, Document);
        
		/*
		System.out.println("First Sentence Tags ");
		// displayTags(FirstSentenceTags);
		System.out.println("End of First Sentence Tags \n");
		*/
		
		// RestComment -> RestElement*
		StringTokenizer AllTokens = new StringTokenizer(stringTag," \t\n\r\f.@{}",true);
		
		// Eats @ and TagName
		if (AllTokens.hasMoreTokens()) 
			token = AllTokens.nextToken();
		if (AllTokens.hasMoreTokens())
			token = AllTokens.nextToken();		
		LinkedList InlineTags = RestComment(AllTokens, Document);
        
		/*
		System.out.println("Inline Tags ");
		displayTags(InlineTags);
		System.out.println("End of Inline Tags \n");
		*/
		
		// Here is where the Tag is created
		TagJTS newTag = new TagJTS(Document,fullText,TagType);
		newTag.setKind(TagType);

		// Adds the inlineTags and the First Sentence Tags to authorTag
		Tag[] fsTags = new TagJTS[FirstSentenceTags.size()];
		for(int i=0; i< FirstSentenceTags.size(); i++)
		{ fsTags[i] = (TagJTS) FirstSentenceTags.get(i); }

		Tag[] inTags = new TagJTS[InlineTags.size()];
		for(int i=0; i< InlineTags.size(); i++)
		{ inTags[i] = (TagJTS) InlineTags.get(i); }
		
		newTag.setFirstSentenceTags(fsTags);
		newTag.setInlineTags(inTags);
		
		/*
		System.out.println(" --- Display " + TagType + " Tag ---");
		displayATag(newTag);
		System.out.println(" --------------------------");
		*/
		
		return newTag;	   	 
  }
 
   /** FirstSentence -> Element* ["."]
    */
  public static LinkedList FirstSentence(StringTokenizer tokens,
											   Doc Document)
  {
	  // Parsing variables
	  String token ="";
	  String Element ="";
	  String LookAhead1, LookAhead2, LookAhead3;
	  String firstSentenceText = "";   // The text of the whole first sentence
	  String currentTagText = "";      // The text of current Tag
	  String inlineTagText = "";       // The text of an inlineTag
	  boolean  dotFound = false;
	  
	  // List with the tag objects obtained from the first sentence
	  LinkedList tagsList = new LinkedList();
	  TagJTS currentTag, inlineTag;
	  
      // Parsers all the comment while it does not reach a dot.
	  while (tokens.hasMoreTokens() && dotFound == false)
	  {  
		// Element is the next token
		Element = tokens.nextToken();
		
		// If Element is the dot and had not been found before then
		// that's the end of the first sentence
		if (Element.compareTo(".")==0 && dotFound == false) 
		{
			currentTagText = currentTagText + ".";
			dotFound = true;
			break;
		}
		
		// If Element is the { then read ahead two characters 
		// @ and an id, if it holds then that's and inline tag
		if (Element.compareTo("{")==0)
		{
			// Reads the inline tag name
			if (tokens.hasMoreTokens())
			{
			  LookAhead1 = tokens.nextToken();
			  
			  if (LookAhead1.compareTo("@")==0)
			  {
				  // If there are still more tokens
				  if (tokens.hasMoreTokens())
				  {
					  LookAhead2 = tokens.nextToken();
					  
					  if (LookAhead2.length() > 0 
						  && LookAhead2.compareTo(" ")!=0)
					  { // Voila we found an InlineTag
						// Whatever is in currentTagText is a Tag
                        currentTag = newTextTag(currentTagText, Document);
						if (currentTag != null) tagsList.add(currentTag);
						
						// Parse until you find the } to end InlineTag
						// If there are not } consider it as another Tag
						inlineTagText = ParseInlineTag(tokens);
						if (inlineTagText.endsWith("}"))
						{  // it is a proper inlineTag
							inlineTag = newInlineTag(LookAhead2, inlineTagText, Document);
							tagsList.add(inlineTag);
						}
						else
						{  // it is not a proper inlineTag
							currentTag = newTextTag("{@"+LookAhead2+ inlineTagText, Document);
							tagsList.add(currentTag);
						}
						// From } there is a new CurrentTag
						currentTagText = "";
					  }
					  else // There was a {@ but no tag name
					  {
						  currentTagText = currentTagText + Element 
										   + LookAhead1 + LookAhead2;
					  }
				  }
				  else
				  { // There was no more tokens, so it is the end of the
					// first sentence and add Element and LookAhead1 to the
					// currentTagText
                    currentTagText = currentTagText + Element + LookAhead1;
					dotFound = true;
				  }
			  }
			  else
			  { // there was another token but not an @, so it is Anyword
				currentTagText = currentTagText + Element + LookAhead1;  
			  } 
			}
			else 
			{ // There was a { but no more characters, so consider it AnyWord
			  // And since there are no more characters it is the end of the 
			  // first sentence
				currentTagText = currentTagText + Element;
				dotFound = true;
			}
		}
		else // Element is AnyWord, therefore add its text to currentTagText
		{
			currentTagText = currentTagText + Element; 
		}
			
	} // while there are tokens && not found end of first sentence 
	  
	  // If there is something in the current tag when we found .
	  // Then create a new Tag for it.
	  if (currentTagText.length()>0) 
	  {
		  currentTag = newTextTag(currentTagText, Document);
		  if (currentTag !=null) tagsList.add(currentTag);
	  }
	  
	  
	  return tagsList;
	  
  } // of FirstSentence
  
/** RestComment -> RestElement*
  */
  public static LinkedList RestComment(StringTokenizer tokens,
											 Doc Document)
  {

    // Parsing variables
	  String token ="";
	  String Element ="";
	  String LookAhead1, LookAhead2, LookAhead3;
	  String firstSentenceText = "";   // The text of the whole first sentence
	  String currentTagText = "";      // The text of current Tag
	  String inlineTagText = "";       // The text of an inlineTag
	  
	  // List with the tag objects obtained from the first sentence
	  LinkedList tagsList = new LinkedList();
	  TagJTS currentTag, inlineTag;
	  
      // Parsers all the comment while it does not reach a dot.
	  while (tokens.hasMoreTokens())
	  {  
		// Element is the next token
		Element = tokens.nextToken();
				
		// If Element is the { then read ahead two characters 
		// @ and an id, if it holds then that's and inline tag
		if (Element.compareTo("{")==0)
		{
			// Reads the inline tag name
			if (tokens.hasMoreTokens())
			{
			  LookAhead1 = tokens.nextToken();
			  
			  if (LookAhead1.compareTo("@")==0)
			  {
				  // If there are still more tokens
				  if (tokens.hasMoreTokens())
				  {
					  LookAhead2 = tokens.nextToken();
					  
					  if (LookAhead2.length() > 0 
						  && LookAhead2.compareTo(" ")!=0)
					  { // Voila we found an InlineTag
						// Whatever is in currentTagText is a Tag
                        currentTag = newTextTag(currentTagText, Document);
						if (currentTag != null) tagsList.add(currentTag);
						
						// Parse until you find the } to end InlineTag
						// If there are not } consider it as another Tag
						inlineTagText = ParseInlineTag(tokens);
						if (inlineTagText.endsWith("}"))
						{  // it is a proper inlineTag
							inlineTag = newInlineTag(LookAhead2, inlineTagText, Document);
							tagsList.add(inlineTag);
						}
						else
						{  // it is not a proper inlineTag
							currentTag = newTextTag("{@"+LookAhead2+ inlineTagText, Document);
							tagsList.add(currentTag);
						}
						// From } there is a new CurrentTag
						currentTagText = "";
					  }
					  else // There was a {@ but no tag name
					  {
						  currentTagText = currentTagText + Element 
										   + LookAhead1 + LookAhead2;
					  }
				  }
				  else
				  { // There was no more tokens, so it is the end of the
					// first sentence and add Element and LookAhead1 to the
					// currentTagText
                    currentTagText = currentTagText + Element + LookAhead1;
				  }
			  }
			  else
			  { // there was another token but not an @, so it is Anyword
				currentTagText = currentTagText + Element + LookAhead1;  
			  } 
			}
			else 
			{ // There was a { but no more characters, so consider it AnyWord
			  // And since there are no more characters it is the end of the 
			  // first sentence
				currentTagText = currentTagText + Element;
			}
		}
		else // Element is AnyWord, therefore add its text to currentTagText
		{
			currentTagText = currentTagText + Element; 
		}
			
	 // System.out.println("CurrentTag Text =  " + currentTagText);
	
	} // while there are tokens
	  
	// If there is something in the current tag when we found .
	// Then create a new Tag for it.
	if (currentTagText.length()>0) 
	{
	  currentTag = newTextTag(currentTagText, Document);
	  if (currentTag !=null) 
	  { tagsList.add(currentTag);
		// System.out.println("Added Tag " + currentTagText);
	  }
	}
	  
	return tagsList;
  } // of RestComment

  /** Parses an InlineTag until it reaches the } character.
   */
  public static String ParseInlineTag(StringTokenizer tokens)
  {
	  String linkBody = "";
	  String token;
	  
	  while (tokens.hasMoreTokens())
	  {
		  token = tokens.nextToken();
		  linkBody =linkBody + token;
		  if (token.compareTo("}") == 0)
			  return linkBody;
	  }
	  
	  return linkBody;
  }

  /** Method that creates a new Tag of type Text.
   */
  public static TagJTS newTextTag(String currentTagText, Doc Document)
  {
	  String trimmedText = currentTagText.trim();
	  if (trimmedText.length() > 0)
	  {
		  TagJTS newTag = new TagJTS(Document,trimmedText,"Text");
		  newTag.setKind("Text");
		  return newTag;
	  }
	  else 
		  return null;
  }
  
  /** Method that creates a new Inline Tag of the type TagType.
   */
  public static TagJTS newInlineTag(String TagType, String inlineTagText,
									Doc Document)
  {
	  // Trims the tag text.
	  String trimmedText = inlineTagText.trim();
	  
	  // Removes the last } .
	  String inlineTag = trimmedText.substring(0,trimmedText.length()-1);
	  

	  // Checks to see if TagType is link, if so it is converted to @see
	  // This is how javadoc does it, exactly why? I dont know.
	  if (TagType.compareTo("link")==0)
	  { 
          SeeTagJTS seeTag = newSeeTag("@" + TagType ,inlineTag,Document);
		  return seeTag;
	  }
	  
	  // Else it is a normal tag
	  TagJTS newTag = new TagJTS(Document, inlineTag, "@" + TagType); 
	  newTag.setKind("@"+TagType);
	  return newTag;
		  
  }
  
  
  
/**************************************************************************/
/**************************************************************************/
/************************* STRING PARSING   *******************************/
   
   
	/** Reads line by line a comment.
	 * The first element of the array is the line
	 * The second element of the array is the rest of the comment
	 */
	static String[] nextLine(String comment)
	{
		String[] result = new String[2];
        String token = "";
		StringTokenizer parser = new StringTokenizer(comment,"\n",false);
		
		// If there is another line left
		if (parser.hasMoreTokens())
		{
			result[0] = parser.nextToken();
			
			// Parsers the rest of the comment
			String result1 = "";
			while(parser.hasMoreTokens())
			{
				token = parser.nextToken();
				// System.out.println("Token -: " + token);
				result1 = result1 + "\n" + token;
			}
			
            // System.out.println("*************************************");
            // System.out.println("Result 1 ->" + result1);
			
			if (result1 =="")
			   { result[1] = null;}
			else 
				result[1] = result1;
		}
		else  // there is nothing else to parse
		{
			result[0] = null;
			result[1] = null;
		}
		
		/*
		// If there is no comment return both results as null
		if (comment == null)
		{
			result[0]=null;
			result[1]=null;
			return result;
		}
		
		int newLinePos = comment.indexOf("\n");
		
		// If there is not new line then the line is all that's left
		if (newLinePos == -1) 
		{
			result[0] = comment;
			result[1] = null;
			return result;
		}
		
		// Checks boundaries of the String
		if (newLinePos == 0)
		  { result[0] = "";}
		else
		  result[0] = comment.substring(0, newLinePos -1);
		
		// If \n is the last character then the rest is null
		if (newLinePos + 1 >= comment.length())
		{ result[1] = "";}
		else  // if not, the rest is from next char after \n
		 result[1] = comment.substring(newLinePos+1);
		*/
		
		// System.out.println("Result 0 = " + result[0] + "\n");
		// System.out.println("Result 1 = " + result[1] + "\n");
		
		
		return result;
	}
	
/**************************************************************************/
	
} // end of CommentParser
