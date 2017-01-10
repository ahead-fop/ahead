
// Objects of the TopParams contain information on 
// the kind of elements to be stored and the names of
// the generic cursor and container types

package p3Lib;


import JakBasic.Lang;

import Jakarta.symtab.*;
import Jakarta.util.Util;
import java.util.Enumeration;
import java.util.Vector;

public class TopParams {
    public static final int P3Convention     = 0;
    public static final int DemConvention    = 1;
    public static final int JBeansConvention = 2;

    public int                 NFields;
    public String[]            FieldName;
    public String[]            FieldType;
    public Lang.AST_QualifiedName[] AST_FieldName;
    public Lang.AST_QualifiedName[] AST_FieldType;
    public Lang.AST_QualifiedName[] get_AST_FieldName;
    public Lang.AST_QualifiedName[] set_AST_FieldName;
    public boolean             Dem    = false;
    public boolean             JBeans = false;
    public boolean             Unique = false;
    public boolean             Sync   = false;

	//intent of adding a new param for the type equation
	public String              TypeEquat;

    public String     ElementName;
    public ClassInfo  ElementClass;
    public String     AbstractCursorName;
    public String     AbstractContainerName;
    public Lang.AST_QualifiedName AST_ElementName;
    public Lang.AST_QualifiedName AST_AbstractContainerName;
    public Lang.AST_QualifiedName AST_AbstractCursorName;

    Lang.AST_ParList  ParamLst;  // element constructor param list
    Lang.AST_ArgList  ArgLst;    // element constructor argument list

    public TopParams( int size, String Ename, boolean singleton ) {
	init(size, Ename, singleton);
    }

    public TopParams(String name, int Convention) {
	Lang.Symtab   st = (Lang.Symtab) Lang.Symtab.instance();
	ClassInfo     ReturnType;
	Vector        attributes;	// contains FieldInfo objects
	FieldInfo     field;
	MethodInfo    methods[];
	ClassInfo     parmTypes[];
	Enumeration   fcursor;
	int 		  i, j;
	boolean 	  getFound;
	boolean 	  setFound;
	String 		  fieldName, setName, getName;
	TopParams 	  tp;

        // Step 1: determine set/get naming convention

        switch (Convention) {
        case P3Convention: 
           Dem = false;
           JBeans = false;
           break;
        case DemConvention:
           Dem = true;
           JBeans = false;
           break;
        case JBeansConvention:
           Dem = false;
           JBeans = true;
           break;
        default:
           Util.fatalError("Unrecognized get/set naming convention");
           return;
        }

	attributes = new Vector();
	ElementClass = (ClassInfo) st.lookup(name);
	if (ElementClass == null)
	    return;
	methods = ElementClass.getMethods();

	fcursor = ElementClass.getFieldCursor();
	while (fcursor.hasMoreElements()) {
	    field = (FieldInfo) fcursor.nextElement();

	    // Test if attribute is protected
	    if ((field.getModifiers() & ClassInfo.ACC_PROTECTED) == 0)
		continue;

	    // Scan through the methods list to locate get and set methods.
	    fieldName = field.getName();
            switch (Convention) {
            case DemConvention:
                setName = "set_" + fieldName;
                getName = "get_" + fieldName;
                break;
            case JBeansConvention:
                setName = "set" + fieldName;
                getName = "get" + fieldName;
                break;
            default:
                setName = fieldName;
                getName = fieldName;
                break;
            }
	    getFound = false;
	    setFound = false;

            for (i=0; i < methods.length; i++) {
		if (getName.compareTo(methods[i].getName()) == 0) {

                   // Get method has no params
                   // parmTypes used to check # of parameters

                   parmTypes = methods[i].getParameterTypes(); 

		   if (field.getType() == methods[i].getReturnType()
                       && parmTypes.length == 0) {
                      getFound = true;
                      if (setFound)
                         break;
                   }
                }

		if (setName.compareTo(methods[i].getName()) == 0) {

                   // set method has one param
                   // parmTypes used to check # of parameters

                   parmTypes = methods[i].getParameterTypes(); 

		   // NOTE: to test equality of types with '==' we MUST
		   // assure that both objects are of type 'Type' and neither
		   // is of type 'ClassInfo'. This is why getType() is called
		   // below.
                   if (Dem || JBeans) 
                      ReturnType = Symtab.Void.getType(0);
                   else
                      ReturnType = field.getType();

		   if (parmTypes.length == 1 
                       && methods[i].getReturnType() == ReturnType
                       && parmTypes[0] == field.getType()) {
                      setFound = true;
                      if (setFound)
                         break;
                   }
                }

            }	// end of for loop

	    if (setFound && getFound)
		attributes.addElement(field);
	}	// end of while loop

	// All attributes have been placed in the attributes vector.
	// Initialize this TopParams object.

        init(attributes.size(), name, false);
	for (i=0; i < attributes.size(); i++) {
	    field = (FieldInfo) attributes.elementAt(i);
	    AddField(field.getName(), field.getType().getFullName());
	}
    }

    private void init( int size, String Ename, boolean singleton ) {
	FieldName = new String[ size ];
	FieldType = new String[ size ];
	AST_FieldName = new Lang.AST_QualifiedName[ size ];
	AST_FieldType = new Lang.AST_QualifiedName[ size ];
        get_AST_FieldName = new Lang.AST_QualifiedName[ size ];
        set_AST_FieldName = new Lang.AST_QualifiedName[ size ];

	ElementName               = Ename;
	AbstractContainerName     = null;
	AbstractCursorName        = null;
	AST_ElementName           = Lang.AST_QualifiedName.Make(Ename);
	AST_AbstractContainerName = null;
	AST_AbstractCursorName    = null;
	NFields                   = 0;
	//init new param
	TypeEquat                 =	null;//to be initialized somewhere else
	Unique                    = singleton;
        Sync                      = false;
    }

    public void AddField( String Name, String Type ) {
	FieldName[NFields] = Name;
	FieldType[NFields] = Type;
	AST_FieldName[NFields] = Lang.AST_QualifiedName.Make(Name);
	AST_FieldType[NFields] = Lang.AST_QualifiedName.Make(Type);
        if (Dem) {
           get_AST_FieldName[NFields] = Lang.AST_QualifiedName.Make("get_"+Name);
           set_AST_FieldName[NFields] = Lang.AST_QualifiedName.Make("set_"+Name);
        }
        if (JBeans) {
           get_AST_FieldName[NFields] = Lang.AST_QualifiedName.Make("get"+Name);
           set_AST_FieldName[NFields] = Lang.AST_QualifiedName.Make("set"+Name);
        }
	NFields++;
    }

    public void AddContainerName( String Cname ) {
	AbstractContainerName     = Cname;
	AST_AbstractContainerName = Lang.AST_QualifiedName.Make(Cname);
    }

    public void AddCursorName( String Kname ) {
	AbstractCursorName        = Kname;
	AST_AbstractCursorName    = Lang.AST_QualifiedName.Make(Kname);
    }

    public void setUnique( boolean val ) {
	Unique = val;
    }

    // FieldIndex(f) returns the index of the attribute with name f
    // -1 is returned if there no such attribute exists

    public int FieldIndex( String fld ) {
	int i;

	for (i=0; i<NFields; i++) 
	    if (FieldName[i].compareTo(fld) == 0)
		return i;
	return -1;
    }


    public void print() {
       int i;

       System.out.println("class " + ElementName + " {");
       for (i=0; i<NFields; i++) {
         System.out.println("   " + FieldType[i] + "   " + FieldName[i] + ";");
       }
       System.out.println("}\n");
    }
}



