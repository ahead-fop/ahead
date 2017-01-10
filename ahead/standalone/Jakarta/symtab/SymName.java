package Jakarta.symtab;

import Jakarta.util.Util ;

public class SymName {

    // Classification constants

    static public final int UNCLASSIFIED = 0;
    static public final int PACKAGE_NAME = 1;
    static public final int TYPE_NAME = 2;
    static public final int EXPRESSION_NAME = 3;
    static public final int METHOD_NAME = 4;
    static public final int AMBIGUOUS_NAME = 5;

    protected String fullName;
    protected String comp[];
    protected int type[];	// uses classification constants above
    protected int num_comp;
    protected int dim_count;	// used if SymName is really a TypeName
    				// (holds array dimension count)

    public SymName(String n) {
	n = n.trim();
	fullName = n;
	comp = Util.findComponents(n);
	num_comp = comp.length;
	type = new int[num_comp];
    }

    //**************************************************
    // Get/set dim_count.
    //**************************************************
    public void setDimCount(int n) { dim_count = n; }

    public int getDimCount() { return(dim_count); }

    // Used by Get Signature (below).
    private static final String dims = "[[[[[";

    //**************************************************
    // Returns a signature code based on the name and dimensions.
    // This only has meaning if the SymName is a type name.
    //**************************************************
    public String getSignature() {
	String sig;

	sig = dims.substring(0, dim_count);
	if (num_comp == 1) {
	    if (comp[0].compareTo("byte") == 0)
		sig += "B";
	    else if (comp[0].compareTo("char") == 0)
		sig += "C";
	    else if (comp[0].compareTo("double") == 0)
		sig += "D";
	    else if (comp[0].compareTo("float") == 0)
		sig += "F";
	    else if (comp[0].compareTo("int") == 0)
		sig += "I";
	    else if (comp[0].compareTo("long") == 0)
		sig += "J";
	    else if (comp[0].compareTo("short") == 0)
		sig += "S";
	    else if (comp[0].compareTo("boolean") == 0)
		sig += "Z";
	    else if (comp[0].compareTo("void") == 0)
		sig += "V";
	    else {
		// Component is an object but might have implied context
		sig += "L" + fullName  + ";";
	    }
	}
	else
	    sig += "L" + fullName  + ";";
	return(sig);
    }

    //**************************************************
    // Get component count
    //**************************************************
    public int getComponentCount() { return(num_comp); }

    //**************************************************
    // Get name of last component.
    //**************************************************
    public String getName() {
	if (num_comp == 0)
	    return("");
	return(comp[num_comp-1]);
    }

    //**************************************************
    // Get fully qualified name (all components)
    //**************************************************
    public String getFullName() {
	return(fullName);
    }

    //**************************************************
    // Returns the type object associated with the last component.
    //**************************************************
    public int getType() {
	if (num_comp == 0)
	    return(UNCLASSIFIED);
	return(type[num_comp-1]);
    }

    //**************************************************
    // Returns the object type associated with a particular component.
    //**************************************************
    public int getType(int index) {
	return(type[index]);
    }

    //**************************************************
    // Sets the object type associated with the last component.
    //**************************************************
    public void setType(int t) {
	type[num_comp-1] = t;
    }

    //**************************************************
    // Sets the object type associated with a particular component.
    //**************************************************
    public void setType(int index, int t) {
	type[index] = t;
    }

    //**************************************************
    // Returns a particular component (name)
    //**************************************************
    public String componentName(int index) {
	return(comp[index]);
    }
}
