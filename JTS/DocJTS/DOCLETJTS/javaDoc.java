
		package DOCLETJTS; import com.sun.javadoc.*; import
        sun.tools.java.*; import
        com.sun.tools.javadoc.*; import
        jtsdoc.*; import   
        java.util.*; public class javaDoc
{

/** This class contains the description of a class.
*/
static public class ClassDocJTS extends ProgramElementDocJTS implements ClassDoc 
{
  // *************************************************************************
  // *************************************************************************
  // *** Field Summary      ---> why are these final?

  /** Originally, final java.lang.String qualifier; 
    *  I don't know its use yet, probably is the whole qualifier before the name. */
   String qualifier;                         
	
    /** Originally, final java.lang.String name; 
     * I assume is the name of the class */
    String name;                             
	
    /** Originally final com.sun.tools.javadoc.ClassDocImpl superclass; 
     * Contains the reference to the superclass. */
    Lang.ClassDocJTS superclass;
	
    /** Originally, final com.sun.tools.javadoc.PackageDocImpl containingPackage; 
     * Contains the reference to the containing package. */
    PackageDocJTS containingPackage;
	
    /** Originally, final java.util.List fields; 
     * Contains a list of FieldDocJTS objects, which are the fields of the class. */
    List fields;
	
    /** Originally, final java.util.List methods; 
     * Contains a list of MethodDocJTS objects, which are the methods of the class. */
    List methods;
	
    /** Originally, final java.util.List constructors; 
     * Contains a list of ConstructorDocJTS objects, which are the constructors of the class
     */
    List constructors;

	/** Originally, final java.util.List innerClasses; 
	 * Contains a list of $TEqn.ClassDocJTS objects, which are the innerclasses of the class. */
    List innerClasses;
   
	/** Originally, com.sun.tools.javadoc.ClassDocImpl interfaces[]; 
	 * Contains an array of ClassDocJTS objects, which are the interfaces that are 
	 implemented in the class. */
    Lang.ClassDocJTS interfaces[];

	/** Originally, final java.util.List importedClasses; 
	 * Contains a list of ClassDocJTS objects, which are the classes imported by the 
	 class. */
    List importedClasses;
	
    /** Originally, final java.util.List importedPackages; 
     * Contains a list of PackageDocJTS objects, which are the list of imported 
     packages of the class. */
    List importedPackages;
	
    /** I still dont know how it is used, and what does it mean for a class to
     be included. */
    boolean isIncluded;

    /** I still dont know how it is used ??? */
    static LinkedList constructionCompletionQueue; 

	// *************************************************************************
	// *************************************************************************	
	// *** Method summary
	
	/* No idea what this method is for )-:
	 */
	// static void secondPhaseConstruction(com.sun.tools.javadoc.Env e) { }
	
	/** Returns true if the class is Serializable or not. 
	 * That is, it implements java.io.Serializable interface. 
	 * Current implementation only checks for "Serializable" but it has to
	 check for interfaces derived from it, or java.io.Serializable .
         * Note: Externalizable extends Serializable. So this case has to be considered as well.
	 */
	public boolean isSerializable() 
	{ 
 	boolean serializable = false;
	for (int i=0; i< interfaces.length ; i++)
		if (interfaces[i].name().equals("Serializable"))
		{
			serializable = true;
			break;
		}
	return serializable; 
	}
	
	// Returns true if it implements java.io.Externalizable
	/** Returns true if the class is Externalizable or not.
	 * That is, it implements java.io.Externalizable interface.
	 * Current implementation only checks for "Externalizable" but it has to
	   consider interfaces derived from it, and java.io.Externalizable .
	 */
	public boolean isExternalizable() 
	{   
	boolean externalizable = false;
	for (int i=0; i<interfaces.length ; i++)
	{ 
		if (interfaces[i].name().equals("Externalizable"))
		{
			externalizable = true;
			break;
		}
	}
	 return externalizable; 
    }
	
	// Returns the methods that are serialized
	// All that are not transient or static
	/** I suppose this returns the methods that are "serialized", yet I dont know exactly how it works. 
	 * Current implementation returns only an empty array of MethodDocJTS objects.
	 */
	public MethodDoc serializationMethods()[] 
	{ 
	  return new MethodDocJTS[0]; 
	}
	
	/** Returns the fields that are serializable, that is, those that
	  do not have static not transient as modifiers.
	 */
	public FieldDoc serializableFields()[] 
	{  
	 LinkedList theFields = new LinkedList();
	 for(int i=0; i< fields.size(); i++)
	 {
		 FieldDocJTS theField = (FieldDocJTS)fields.get(i);
		 if (theField.isStatic() == false && theField.isTransient() == false)
			theFields.add(theField);
	 }
	 
	 FieldDoc[] arrayFields = new FieldDocJTS[theFields.size()];
	 for(int i=0; i< theFields.size(); i++)
		 arrayFields[i] = (FieldDocJTS)theFields.get(i);
	return arrayFields; 
	}
	
	/** I assume that it would return true if there are a serializable field in
	 * the class but it seems that that is not the case.
	 * Current implementation returns the value of the variable that it is 
	 * initialized to false.
	 */
	public boolean definesSerializableFields(){ return defineSerializableFields; }

	/* I dont have any idea what is this for )-:
	 */
	// static ClassDocJTS getClassDocJTS(Env e, ClassDefinition cd) { }
	
	/* I dont have any idea what is this for )-:
	 */
	// static ClassDocJTS getClassDocImpl(com.sun.tools.javadoc.Env e, sun.tools.java.ClassDeclaration cd ){}
    
	/** I dont have any idea what is this for )-:
	 * I assume that it looks somewhere for the name of a class and returns
	 * the reference to that class if any.
	 * Current implementation returns null pointer.
	 */
	static final Lang.ClassDocJTS lookup(String x) { return null;}
	
	/** I dont have any idea what is this for )-:
	 * I assume that it looks somewhere for a ClassDeclaration object and
	 * returns the reference to that class if any.
	 * Current implementation returns null pointer.
	 */
	static final Lang.ClassDocJTS lookup(ClassDeclaration cd) { return null; }
	
	/** Returns true if this is the definition of a Class.
	 */
	public boolean isClass() { return isClass; }
	
	/** Returns true if this is a definition of an Ordinary class.
	 * The question is : What is an ordinary class?
	 * Current implementation returns the value of the variable which is
	 * initialized to true.
	 */
	public boolean isOrdinaryClass() { return isOrdinaryClass; }
	
	/** Returns true if this is a definition of an interface.
	 */
	public boolean isInterface() { return isInterface; }
	
	/** I dont know what is this for. )-: 
	 * Current implementation returns the value of the variable.
	 */
	public boolean isException() { return isException; }
	
	/** I dont know what is this for. )-:
	 * Current implementation returns the value of the variable, initialized to false.
	 */
	public boolean isError() { return isError; }
	
	/** I dont know how to set it correctly.
	 * Current implementation returns the value of the variable, initialized to false.
	 */
	public boolean isAbstract() { return isAbstract; }
	
	/** I dont know what it means to be included.
	 * Current implementation returns the value of the variable, initialized to false.
	 */
	public boolean isIncluded() { return isIncluded; }

	/** Returns the reference to the package that contains this class.
	 */
	public PackageDoc containingPackage(){ return containingPackage; }
	
	/** Returns the name of the class.
	 * Current implementation does not add prefix for innerclasses.
	 */
	public String name() { return name; }
	
	/** Returns the name of the type of the Class.
	 * Seems to be the same as name() but I dont know.
	 * Current implementation just returns the name.
	 */
	public String typeName() { return name; }
	
	/** Returns the qualifiedName of the class.
	 * Seems to include the name of the package as well.
	 * Current implementation returns the name.
	 */
	public String qualifiedName() { return name; }
	
	/** Returns the qualifiedType name of the class.
	 * Seems to be the same as qualifiedName.
	 * Current implementation returns the name.
	 */
	public String qualifiedTypeName() { return name; }
	
	/** I have no idea what is this for. )-:
	 */
	static String qualifiedName(ClassDeclaration cd) { return ""; }
	
	/** Returns an string with the modifiers of the class.
	 * Current implementation prepends abstract, if present, to the
	 * modifiers method in the superclass. 
	 */
	public String modifiers()
	{ 
	String output = "";
	if (isAbstract) output =" abstract ";
	output = output + super.modifiers();
	return output; 
	}
	
	/** Returns a reference to the superclass of this class.
	 */
	public ClassDoc superclass() { return superclass; }
	
	/** I assume it returns true if the class is a subclass of this class.
	 * Current implementation returns always false.
	 * I assume that it looksup somewhere to see if it is present.
	 */
	public boolean subclassOf(ClassDoc c) { return false; }
	
	/** Returns an array of ClassDocJTS objects, that corresponds to the 
	 * interfaces this class implements.
	 */
	public ClassDoc interfaces()[] 
	{ 
		ClassDoc[] arrayInterfaces = new Lang.ClassDocJTS[theInterfaces.size()];
		for(int i=0; i< theInterfaces.size(); i++)
		{	arrayInterfaces[i] = (ClassDoc)theInterfaces.get(i);
		}
		return arrayInterfaces; 		 
	}
	
	/** I have not idea what this method is supposed to return.
	 * Not part of the interface.
	 * Current implementation returns empty array.
	 */
	public ClassDoc implementedInterfaces()[] 
	{ 
		return new Lang.ClassDocJTS[0]; 		 
	}
	
	/** Returns an array of FieldDocJTS objects, which are the fields of the class.
	 */
	public FieldDoc fields()[] 
	{ 
		if (fields == null) 
		{
			return new FieldDocJTS[0];
		}
		
		if (fields.size() < 1) return null;
		FieldDoc[] arrayFields = new FieldDocJTS[fields.size()];
		for(int i=0; i< fields.size(); i++)
		{	arrayFields[i] = (FieldDoc)fields.get(i);
		}
		return arrayFields; 		 
	}
	
	/** Returns an array of MethodDocJTS objects, which are the methods of the class.
	 */
	public MethodDoc methods()[] 
	{ 
		MethodDoc[] arrayMethods = new MethodDocJTS[methods.size()];
		for(int i=0; i< methods.size(); i++)
		{	arrayMethods[i] = (MethodDoc)methods.get(i);
		}
		return arrayMethods; 
	}
	
	/** Returns an array of ConstructorDocJTS objects, which are the constructors of the class.
	 * Note : by default it has an empty public constructor if there are not any in 
	 * the source code.
	 */
	public ConstructorDoc constructors()[] 
	{ 
		ConstructorDoc[] arrayConstructors = new ConstructorDocJTS[constructors.size()];
		for(int i=0; i< constructors.size(); i++)
		{
			arrayConstructors[i] = (ConstructorDoc)constructors.get(i);
		}
		return arrayConstructors; 
	}
	
	/** Returns an array of $TEqn.ClassDocJTS objects, which are the innerclasses and
	 * innerinterfaces of this class.
	 */
	public ClassDoc innerClasses()[] 
	{
		ClassDoc[] arrayInnerClasses = new Lang.ClassDocJTS[innerClasses.size()];
		for(int i=0; i< innerClasses.size(); i++) 
		  arrayInnerClasses[i] = (ClassDoc)innerClasses.get(i);
		return arrayInnerClasses; 
	}
	
	/** I dont know what is this for. )-:
	 * I assume that it looks somewhere for a class name and returns its
	 * reference. What is the difference with lookup?
	 * Current implementation returns null.
	 */
	public ClassDoc findClass(String classname) { return null; }
	
	/** I dont know what is this for. )-:
	 * I assume that it looks somewhere (probably the class) for a method name and its
	 * arguments and returns the correspondent reference if any.
	 * Current implementation returns null.
	 * Not part of the interface.
	 */
	public MethodDoc findMethod(String name, String[] list) { return null; }
	
	/** I dont know what is this for. )-:
	 * I assume that it looks somewhere (probably the class) for a constructor
	 * name and its parameters, and returns the correspondent reference if any.
	 * Current implementation returns null.
	 * Not part of the interface.
	 */
	public ConstructorDoc findConstructor(String name, String[] list) { return null; }
	
	/** I dont know what is this for. )-:
	 * I assume that it looks somewhere (probably the class) for a field name
	 * and returns the correspondent reference if any.
	 * Current implementation returns null.
	 * Not part of the interface.
	 */
	public FieldDoc findField(String fieldname) { return null; }
	
	/** Returns an array of imported classes.
	 * I dont know how to set it correctly. )-:
	 * Current implementation returns empty array.
	 */
	public ClassDoc importedClasses()[] 
	{ // return (ClassDoc[])(importedClasses.toArray());
		return new Lang.ClassDocJTS[0];
	}
	
	/** Returns an array of imported packages.
	 * I dont know how to set it correctly. )-:
	 * Current implementation returns empty array.
	 */
	public PackageDoc importedPackages()[] 
	{ // return (PackageDoc[])(importedPackages.toArray());
		return new PackageDocJTS[0];
	}
	
	/** I dont know what is this for. )-: 
	 * Current implementation returns "this" .
	 */
	ClassDocJTS thisClassDocJTS() { return this; }
	
	/** I dont know what is this for exactly. )-:
	 * Current implementation returns empty string.
	 */
	public String dimension() { return ""; }
	
	/** What is the difference with thisClassDocJST ???
	 * Current implementation returns this.
	 */
	public ClassDoc asClassDoc() { return this; }
	
	/** I dont know what is this for. )-:
	 * Current implementation returns empty string.
	 */
	public String toString() { return ""; }
	
	// **********************************************************************
	// **********************************************************************
	// **********************************************************************
    // **** From this point on auxiliary fields, methods and constructors
	// **** are added.

	// **** Constructors
    public ClassDocJTS()
	{
	}	
	
	/** Added: Constructor.
	 */
	public ClassDocJTS(String _name)
	{
	   super(_name);
	   name = _name;
	}
   	
	// **** Fields added to support functionality

	/** Variable that indicates if the class defines serialiable fields.
	 * Initial value : false
	 */
	boolean defineSerializableFields = false;
	
	/** Variable that indicates if the $TEqn.ClassDocJTS object is a class or not.
	 * Initial value : false
	 */
	boolean isClass = false;

	/** Variable that indicates if the class is an ordinary class.
	 * Initial value : false
	 */
	boolean isOrdinaryClass = true;
	
	/** Variable that indicates if a class is an interface or not.
	 * Initial value : false
	 */
	boolean isInterface = false;	

	/** Variable that indicates if a class is an exception or not.
	 * Initial value : false
	 */
	boolean isException = false;

	/** Variable that indicates if a class is an error or not.
	 * Initial value : false
	 */
	boolean isError = false;
	
	/** Variable that indicates if a class is abstract or not.
	 * Initial value : false
	 */
	boolean isAbstract = false;
	
    /** List that holds the interfaces implemented by the class
     */
	List theInterfaces;
	
	/** List that holds the implemented interfaces ?????
	 * Current implementation : uncertain what this means ????
	 */
	List implementedInterfaces;
	
	// **** Methods added to support functionality
	/** Sets the innerClasses list.
	 */
	public void setInnerClasses(List _innerClasses)
	{ innerClasses = _innerClasses; }
	
	/** Sets the superClass object of theclass.
	 */
	public void setSuperClass(Lang.ClassDocJTS _superclass)
	{ superclass = _superclass; }
	
	/** Sets the methods list.
	 */
	public void setMethods(List _methods)
	{ methods = _methods; }
	
	/** Sets the name of the class.
	 */
	public void setName(String _name)
	{ name = _name; }
	
	/** Sets the list of constructors.
	 */
	public void setConstructors(List _constructors)
	{ constructors = _constructors; }
	
	/** Sets the list of fields.
	 */
	public void setFields(List _fields)
	{ fields = _fields; }
	
	/** Sets the list of implemented interfaces.
	 */
	public void setInterfaces(List _interfaces)
	{ theInterfaces = _interfaces; 
	  interfaces = new Lang.ClassDocJTS[theInterfaces.size()];
	  for(int i=0; i< theInterfaces.size(); i++)
		  interfaces[i] = (Lang.ClassDocJTS) theInterfaces.get(i);
	}
	
	/** May be this method is not necessary, until we discover what
	 * are implemented interfaces then we will know
	 * Current implementation : uncertai what it means ???
	 */
	public void setImplementedInterfaces(List _implementedInterfaces)
	{ implementedInterfaces = _implementedInterfaces; }

	/** Sets the class isClass variable.
	 */
	public void setClass(boolean _isClass)
	{ isClass = _isClass; }
	
	/** Sets the class isOrdinaryClass variable.
	 */
	public void setOrdinaryClass(boolean _isOrdinaryClass)
	{ isOrdinaryClass = _isOrdinaryClass; }
	  
	/** Sets the class isInterface variable.
	 */
	public void setInterface(boolean _isInterface)
	{ isInterface = _isInterface; }
		
	/** Sets this class to be abstract.
	 */
	public void setAbstract(boolean _isAbstract)
	{ isAbstract = _isAbstract; }
	
	/** Sets the setDefineSerializableFields variable.
	 */
	public void setDefineSerializableFields(boolean _defineSerializableFields)
	{ defineSerializableFields = _defineSerializableFields; }
	
	/** Sets the containingPackage variable.
	 */
	public void setContainingPackage(PackageDocJTS _containingPackage)
	{ containingPackage = _containingPackage;  }
	
 } // end of ClassDocJTS

static public class ProgramDocJTS  
{

  /** Name that corresponds to the file name of the program
  */
  String name;
  
  /** Contains the reference to the package to where it contains, if any.
   */
  PackageDocJTS containingPackage = null;
  
  /** Contains a list with all the classes accesible from AST_Class production.
  */
  LinkedList classes ;
  
  /** Default constructor initializes the classes list.
  */
  public ProgramDocJTS()
  {
    classes = new LinkedList();
  }	
 
  /** Name constructor
   */
  public ProgramDocJTS(String _name)
  { 
	  name = _name;
	  classes = new LinkedList();
  }
  	
  /** Returns the list of classes accesible from AST_Class production.
  */
  public LinkedList classes() { return classes; }
  
  /** Returns the name of the program.
   */
  public String name() { return name; }
  
  /** Returns the containing package object reference.
   */
  public PackageDoc containingPackage() { return containingPackage; }
  
  /** Sets the list of classes accesible form AST_Class production.
  */
  public void setClasses(LinkedList _classes) { classes = _classes; }
  
  /** Sets the name of the program.
   */
  public void setName(String _name) { name = _name; }
  
  /** Sets the containing Package.
   */
  public void setContainingPackage(PackageDocJTS _containingPackage)
  { containingPackage = _containingPackage; }
    
} // end of Program ProgramDocJTS

};