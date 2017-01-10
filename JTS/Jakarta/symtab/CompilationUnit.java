//                              -*- Mode: Java -*- 
// CompilationUnit.java --- 
// Author          : Bernie Lofaso
// Created On      : Wed Mar 31 14:21:19 1999
// Last Modified By: 
// Last Modified On: Tue May 04 16:07:10 1999
// Update Count    : 18
// Status          : Under Development
// 

package Jakarta.symtab;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

public class CompilationUnit {
    private Vector importOnDemand;	// store PackageInfo
    private Vector importSingleType;	// store ClassInfo
    private File inputFile;
    private Object ast;
    private PackageInfo pkgOfThisCU;

    // Maintain a cache of Compilation Units
    static private Vector CU_Cache = new Vector();
    static public Enumeration getCompilationUnits() {
	return(CU_Cache.elements());
    }

    protected CompilationUnit(File input, Object astRoot) {
	PackageInfo javaLang;
	Symtab st = Symtab.instance();

	inputFile = input;
	ast = astRoot;
	importOnDemand = new Vector();
	importSingleType = new Vector();

	// The package associated with this CU is the default until/unless
	// it is set to something else by the setPackage() method.
	pkgOfThisCU = st.defaultPackage();

	// Add 'java.lang' as an implicit import-on-demand package.
	javaLang = (PackageInfo) st.lookup("java.lang");
	importOnDemand.addElement(javaLang);

	// Add to the cache
	CU_Cache.addElement(this);
    }

    //**************************************************
    // This method is used to create new CompilationUnit's. It guarentee's
    // that only one CompilationUnit is created for a given AST.
    //**************************************************
    static public CompilationUnit internCompilationUnit(File input,
							Object astRoot) {
	Enumeration scanPtr;
	CompilationUnit cu;

	scanPtr = CU_Cache.elements();
	while (scanPtr.hasMoreElements()) {
	    cu = (CompilationUnit) scanPtr.nextElement();
	    if (cu.ast == astRoot)
		return(cu);
	}

	cu = new CompilationUnit(input, astRoot);
	return(cu);
    }

    // Accessor for input file
    public File getInputFile() { return(inputFile); }

    // Accessor for AST root
    public Object getAST() { return(ast); }

    // Used to set the package associated with this CU to something
    // other than the default package.
    public void setPackage(PackageInfo pkg) {
	if (pkg != null)
	    pkgOfThisCU = pkg;
    }

    // Get the package associated with this CU.
    public PackageInfo getPackage() {
	return(pkgOfThisCU);
    }

    public Declaration addImport(String importSpec) {
	PackageInfo pi;
	ClassInfo ci;

	if (importSpec.compareTo("*") == 0) {
	    // This imports everything in the default package.
	    pi = Symtab.instance().defaultPackage();
	    if (pi != null)
		importOnDemand.addElement(pi);
	    return(pi);
	}
	if (importSpec.endsWith(".*")) {
	    // import on demand
	    importSpec = importSpec.substring(0, importSpec.length() - 2);
	    pi = (PackageInfo) Symtab.instance().lookup(importSpec);
	    if (pi != null)
		importOnDemand.addElement(pi);
	    return(pi);
	}
	if (importSpec.indexOf('.') == -1) {
	    // import is a class in the default package
	    pi = Symtab.instance().defaultPackage();
	    ci = pi.findClass(importSpec);
	}
	else
	    ci = (ClassInfo) Symtab.instance().lookup(importSpec);

	if (ci != null)
	    importSingleType.addElement(ci);
	return(ci);
    }

    // Scan through import-on-demand packages
    public Enumeration getIODPackages() { return(importOnDemand.elements()); }

    // Retrieve an imported type. Uses simple name.
    public ClassInfo getImportType(String name) {
	Enumeration scanPtr;
	ClassInfo ci;
	String typeName;

	scanPtr = importSingleType.elements();
	while (scanPtr.hasMoreElements()) {
	    ci = (ClassInfo) scanPtr.nextElement();
	    typeName = ci.getName();
	    if (typeName.compareTo(name) == 0)
		return(ci);
	}
	return(null);
    }
}
