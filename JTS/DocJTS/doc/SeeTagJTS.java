/** JTS Javadoc
 SeeTagJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 August 2001
 Note: current implementation creates new ClassDoc, MemberDoc, and PackageDoc
 objects, but later on these will be substitute for an actual search of this
 objects in a collection.
*/

package jtsdoc;
import com.sun.javadoc.*;
import com.sun.tools.javadoc.*;
import sun.tools.java.*;
import java.util.*;

public class SeeTagJTS extends TagJTS implements SeeTag {
	
	// *** Fields
	/** I have no idea what is the contents of this variable )-:
	 */
    String where;
	
	/** I have no idea what is the contents of this variable )-:
	 */
    String what;
	
	/** I dont know where this variable points to )-:
	 * Initialized to null.
	 */
    PackageDoc referencedPackage = null;
	
	/** I dont know where this variable points to )-:
	 * Initialized to null.
	 */
    ClassDoc referencedClass = null;
	
	/** I dont know where this variable points to )-:
	 * Initialized to null.
	 */
    MemberDoc referencedMember =null;
	
	/** I dont know what is the contents of this variable )-:
	 * Initialized to empty string.
	 */
    String label = "";
    
	// *** Constructor
	/** I assume this constructor simple calls the super.
	 */
	public SeeTagJTS(Doc d, String s1, String s2) { 
		super(d, s1, s2);
	}
	
	// *** Method summary
	/** Current implementation returns name of referencedClass.
	 */
	public String referencedClassName() { return referencedClass.name(); }
	
	/** Current implementation returns the variable referencedPackage.
	 */
	public PackageDoc referencedPackage() { return referencedPackage; }
	
	/** Current implementation returns the variable referencedClass.
	 */
	public ClassDoc referencedClass() { return referencedClass; }
	
	/** Current implementation returns name of referencedMember.
	 */
	public String referencedMemberName(){ return referencedMember.name(); }
	
	/** Current implementation returns the variable referencedMember.
	 */
	public MemberDoc referencedMember() { return referencedMember; }
	
	/** Current implementation returns label string.
	 */
	public String label() { return label; }
	
	// ******** Auxiliary set methods.
	/** Sets the label value.
	 */
	public void setLabel(String _label) { label = _label;}
	
	/** Sets the referencedClass
	 */
	public void setReferencedClass(ClassDoc _referenceClass, String className)
	{
		referencedClass = new DOCLETJTS.Lang.ClassDocJTS(className);
	}
	
	/** Sets the referencedMember.
	 */
	public void setReferencedMember(MemberDoc _referencedMember)
	{
	   referencedMember = _referencedMember;
	}
	
	/** Sets the referencedPackage.
	 */
	public void setReferencedPackage(PackageDoc _referencedPackage, String packageName)
	{
		referencedPackage = new PackageDocJTS(packageName);
	}
	
	// *** Innerclass
	/** I have no idea what this inner class is for and how
	 * it is used. It seans that has to do with the parsing of the tags.
	 */
    public class ParameterParseMachine extends java.lang.Object 
    {
        final int START;
        final int TYPE;
        final int NAME;
        final int TNSPACE;
        final int ARRAYDECORATION;
        final int ARRAYSPACE;
        String parameters;
        StringBuffer typeId;
        List paramList;
		ParameterParseMachine(SeeTagJTS st,String s) { return; }
		public String parseParameters()[]{ return null; }
		void addTypeToParamList() { return ; } 
    } // of Parameter Parse Machine
}
