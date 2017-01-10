/** JTS Javadoc
 StateDiagramDocJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;
import com.sun.javadoc.*;
import java.util.*;

public class StateDiagramDocJTS extends DocJTS implements StateDiagramDoc
{
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary 
	/** Contains the name of the state.
	 */
	String name;
	
	
	/** Variable that contains the diagram the state refines.
	 * Initialized to empty string.
	 */
	String refines = "";
	
	/** Variable that contains the name of the class it extends.
	 * Initialized to empty string.
	 */
	String extendsName = "";
	
	/** Classbody of the state.
	 * Pending
	 */
	DOCLETJTS.Lang.ClassDocJTS classBody;
	
	/** Imports List.
	 * List that contains ClassDocJTS objects that are interfaces of
	 * implements clause.
	 */
	List implementsList;
	
	/**	Variable that contains the name of the root clause if any.
	 * Initialized to empty string.
	 */
	String rootClauseName="";
	
	/** Array that contains the parameters of the delivery class if any.
	 * Initialized to empty array.
	 */
    ParameterJTS[] parameters = new ParameterJTS[0];
	
	/** List that contains the classes/interfaces of NoTransitionClause.
	 * Initialized to empty list.
	 */
    LinkedList noTransitionClauseClasses = new LinkedList();
   
   /** List that contains the classes/interfaces of OtherwiseClause.
    * Initialized to empty list.
    */
   LinkedList otherwiseClauseClasses = new LinkedList();

   /** List that contains the state string of StatesClause.
    * Initialized to empty list.
    */
   LinkedList statesClauseStates = new LinkedList();
   
   // These lists are for the ESList part of SDClassBody
   /** List that contains the Exit states.
    */
   LinkedList  ExitStates = new LinkedList();
   
   /** List that contains the Enter states.
    */
   LinkedList  EnterStates = new LinkedList();
   
   /** List that contains the Edge states.
    */
   LinkedList EdgeStates = new LinkedList();
   
   /** List that contains the Otherwise states.
    */
   LinkedList OtherwiseStates = new LinkedList();
   
   // These lists are for the AST_FieldDecl part of SDClassBody

   /** List that contains the methods of SDClassBody.
    * Initialized to empty list.
    */
   LinkedList methods = new LinkedList();
   
   /** List that contains the innerclasses of SDClassBody.
    * Initialized to empty list.
    */
   LinkedList innerClasses = new LinkedList();
   
   /** List that contains the constructors of SDClassBody.
    * Initialized to empty list.
    */
   LinkedList constructors = new LinkedList();
   
   /** List that contains the fields of SDClassBody.
    * Initialized to empty list.
    */
   LinkedList fields = new LinkedList();
   
   /** List that contains the states of SDClassBody.
    * Initialized to empty list.
    */
   LinkedList states = new LinkedList(); 
   
   /** Class that represents the classbody parts of the State.
    */
   DOCLETJTS.Lang.ClassDocJTS theClassBody;
		
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
	
	/** Variable that indicates if the state is public.
	 */
	boolean isPublic = false;
	
	/** Variable that indicates if the state is protected.
	 */
	boolean isProtected = false;
	
	/** Variable that indicates if the state is private.
	 */
	boolean isPrivate = false;
	
	/** Variable that indicates if the state is static.
	 */
	boolean isStatic = false;
	
	/** Variable that indicates if the state is final.
	 */
	boolean isFinal = false;
	
	/** Variable that indicates if the state is relative
	 */
    boolean isRelative = false;

	/** Variable that indicates if the state is abstract.
	 */
	boolean isAbstract = false;
	
	/** Variable that indicates if the state is transient.
	 */
	boolean isTransient = false;
	
	/** Variable that indicates if the state is volatile.
	 */
	boolean isVolatile = false;
	
	/** Variable that indicates if the state is native.
	 */
	boolean isNative = false;
	
	/** Variable that indicates if the state is synchronized.
	 */
	boolean isSynchronized = false;	
	
	// *** Constructor added to support functionality

	/** Added : Constructor.
	 */
	public StateDiagramDocJTS(String _name)
	{ name = _name;	} 
	
	/** Added : Constructor.
	 */
	public StateDiagramDocJTS() { }
	
	// *** Additional methods added to support functionality
	/** Gets the name of the state.
	 */
	public String name() { return name; }
	
	/** Gets the name of the state it refines.
	 */
	public String refines() { return refines; }
	
	/** Gets the name of the class it extends.
	 */
	public String extendsName() { return extendsName; }
	
	/** Gets the classBody object of the state.
	 * Pending
	 */
	public DOCLETJTS.Lang.ClassDocJTS classBody() { return classBody; }
	
	/** Gets the list of the implements strings.
	 */
	public List implementsList() { return implementsList; }
	
	/** Returns the name of the root clause.
	 */
	public String rootClauseName() { return rootClauseName; }
	
	/** Returns the array of parameters.
	 */
	public ParameterJTS parameters()[] { return parameters; }
	
	/** Returns the list of classes in noTransitionClause.
	 */
	public LinkedList noTransitionClauseClasses()
	{ return noTransitionClauseClasses; }
	
	/** Returns the list of classes in otherwiseClause.
	 */
	public LinkedList otherwiseClauseClasses()
	{ return otherwiseClauseClasses; }
	
	/** Returns the list of string states of the States clause.
	 */
	public LinkedList statesClauseStates()
	{ return statesClauseStates; }
	
	/** Returns the list of Exit states.
	 */
	public LinkedList  ExitStates() { return ExitStates; }
	
	/** Returns the list of Enter states.
	 */
	public LinkedList  EnterStates() { return EnterStates; }
	
	/** Returns the list of Edge states.
	 */
	public LinkedList EdgeStates() { return EdgeStates; }
	
	/** Returns the list of Otherwise states.
	 */
	public LinkedList OtherwiseStates() { return OtherwiseStates; }	
	
	/** Returns the methods of the SDClassBody.
	 */
	public LinkedList methods()
	{ return methods; }
	
	/** Returns the innerclasses of SDClassBody.
	 */
	public LinkedList innerClasses()
	{ return innerClasses; }
	
	/** Returns the constructors of SDClassBody.
	 */
	public LinkedList constructors()
	{ return constructors; }
	
	/** Returns the fields of SDClassBody.
	 */
	public LinkedList fields()
	{ return fields; }
	
	/** Returns the states of the SDClassBody.
	 */
	public LinkedList states()
	{ return states; }	
		
	/** Sets the name of the state it refines.
	 */
	public void setRefines(String _refines) { refines = _refines; }
	
	/** Sets ot the class it extends.
	 */
	public void setExtendsName(String _extendsName) { extendsName = _extendsName; }
	
	/** Sets the class body object of the state.
	 * Pending
	 */
	public void setClassBody(DOCLETJTS.Lang.ClassDocJTS _classBody) 
	{ classBody = _classBody; }
	
	/** Sets the importsList of the state.
	 */
	public void setImplementsList(List _implementsList) 
	{ implementsList = _implementsList; }
	
	/** Sets the isPublic variable of the state.
	 */
	public void setPublic (boolean _isPublic) { isPublic = _isPublic; }
	
	/** Sets the isProtected variable of the state.
	 */
	public void setProtected (boolean _isProtected) { isProtected = _isProtected; }
	
	/** Sets the isPrivate variable of the state.
	 */
	public void setPrivate (boolean _isPrivate) { isPrivate = _isPrivate; }
	
	/** Sets the isStatic variable of the state.
	 */
	public void setStatic (boolean _isStatic) { isStatic = _isStatic; }
	
	/** Sets the isFinal variable of the state.
	 */
	public void setFinal (boolean _isFinal) { isFinal = _isFinal; }
	
	/** Sets the isRelative variable of the state.
	 */
	public void setRelative(boolean _isRelative) { isRelative = _isRelative; }

	/** Sets the isAbstract variable of the state.
	 */
	public void setAbstract(boolean _isAbstract) { isAbstract = _isAbstract; }
	
	/** Sets the isTransient variable of the state.
	 */
	public void setTransient(boolean _isTransient) { isTransient = _isTransient; }
	
	/** Sets the isVolatile variable of the state.
	 */
	public void setVolatile(boolean _isVolatile) { isVolatile = _isVolatile; }
	
	/** Sets the isNative variable of the state.
	 */
	public void setNative(boolean _isNative) { isNative = _isNative; }
	
	/** Sets the isSynchronized variable of the state.
	 */
	public void setSynchronized(boolean _isSynchronized) 
	{ isSynchronized = _isSynchronized; }
	
	/** Sets the RootClause name.
	 */
	public void setRootClauseName(String _rootClauseName)
	{ rootClauseName = _rootClauseName; }
	
	/** Sets the parameters array.
	 */
   public void setParameters(ParameterJTS[] _parameters)
   { parameters = _parameters; }
   
   /** Sets the noTransition Clause classes.
    */
   public void setNoTransitionClauseClasses(LinkedList _noTransitionClauseClasses)
   { noTransitionClauseClasses = _noTransitionClauseClasses; }

   /** Sets the otherwise Clause classes.
    */
   public void setOtherwiseClauseClasses(LinkedList _otherwiseClauseClasses)
   { otherwiseClauseClasses = _otherwiseClauseClasses; }

   /** Sets the states strings from States clause.
    */
   public void setStatesClauseStates(LinkedList _statesClauseStates)
   { statesClauseStates = _statesClauseStates; }
  
   /** Sets the Exit states list.
    */
   public void setExitStates(LinkedList _ExitStates) 
   { ExitStates =_ExitStates; }
   
   /** Sets the Enter states list.
    */
   public void setEnterStates(LinkedList _EnterStates)
   { EnterStates = _EnterStates; }
	
   
   /** Sets the Edge states list.
    */
   public void setEdgeStates(LinkedList _EdgeStates)
   { EdgeStates = _EdgeStates; }
	

   /** Sets the Otherwise states list.
    */
   public void setOtherwiseStates(LinkedList _OtherwiseStates)
   { OtherwiseStates = _OtherwiseStates; }
   
   /** Sets the methods of SDClassBody.
    */
  public void setMethods(LinkedList _methods)
  { methods = _methods; }
  
  /** Sets the innerclasses of SDClassBody.
   */
  public void setInnerClasses(LinkedList _innerClasses)
  { innerClasses = _innerClasses; }
  
  /** Sets the constructors of SDClassBody.
   */
  public void setConstructors(LinkedList _constructors)
  { constructors = _constructors; }

  /** Sets the fields of SDClassBody
   */
  public void setFields(LinkedList _fields)
  { fields = _fields; }

  /** Sets the states of the SDClassBody
   */
  public void setStates(LinkedList _states)
  { states = _states; }	
  
 
  
} // of StateDiagramDocJTS
