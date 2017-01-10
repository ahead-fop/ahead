//                              -*- Mode: Java -*- 
// Dependency.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Jan 17 10:10:21 1997
// Last Modified By: 
// Last Modified On: Mon Nov 09 12:39:48 1998
// Update Count    : 19
// Status          : Under Development
// 
// $Locker:  $
// $Log: Dependency.java,v $
// Revision 1.2  2002-02-22 18:20:06  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:26  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:26  lofaso
// Snapshot 2-18-99
//
// Revision 1.2  1997/12/31 11:59:50  smaragd
// Added java files for performing builds incrementally
//
// Revision 1.1.1.1  1997/12/15 21:00:12  lofaso
// Imported sources
//
// Revision 1.2  1997/09/08 15:28:46  lofaso
// Modified code to use Util.fatalError() to report errors. Fixed whitespace
// problem.
//
// Revision 1.1.1.1  1997/08/25 20:06:16  lofaso
// Imported Java 1.1 sources
//
// Revision 1.2  1997/08/19 17:50:28  lofaso
// Modification to port to Java 1.1.
//
// Revision 1.1.1.1  1997/02/28 16:46:44  lofaso
// Imported Sources
//
// 

package Bali;

import java.util.Enumeration;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.Util;
import Jakarta.util.Callback;

public class Dependency {
    static Hashtable deps = new Hashtable();
    String name;
    Callback callback;
    Object parms;
    Vector precursors;
    File file;

    public Dependency(String n, Callback cb, Object p) {
	this(null, n, cb, p);
    }

    public Dependency(String path, String n, Callback cb, Object p) {
	name = n;
	callback = cb;
	parms = p;
	if (name == null)
	    Util.fatalError("Name required for creating a Dependency");

	if (callback == null)
	    Util.fatalError("Callback required for creating a Dependency");

	precursors = new Vector();
	if (path == null)
	    file = new File(name);
	else
	    file = new File(path, name);
	try {
	    deps.put(name, this);
	}
	catch (Exception e) {}
    }

    public void addRequirement(String name) {
	precursors.addElement(name);
    }

    public long update() throws CantMakeException {
	int i;
	String pcName;
	Dependency pcDep;
	long myModTime;
	boolean remake;
	CantMakeException excp;

	remake = false;
	if (! file.exists()) {
	    myModTime = Long.MIN_VALUE;
	    remake = true;
	}
	else
	    myModTime = file.lastModified();

	for (i=0; i < precursors.size(); i++) {
	    pcName = (String) precursors.elementAt(i);
	    pcDep = (Dependency) deps.get(pcName);
	    if (pcDep == null) {
		excp = new CantMakeException("Error! Don't know how to make '"+
					     pcName + "'");
		throw excp;
	    }

	    if (pcDep.update() > myModTime)
		remake = true;
	}
	if (remake) {
	    try {
		callback.executeCallback(parms);
	    }
	    catch (Exception e) {
		System.err.println("executeCallback threw :");
		System.err.println(e);
		e.printStackTrace();
		excp = new CantMakeException(e.getMessage());
		throw excp;
	    }
	}

	return(file.lastModified());
    }

    public static Dependency find(String name) {
	return((Dependency) deps.get(name));
    }
}
