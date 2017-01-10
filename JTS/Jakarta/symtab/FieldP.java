//                              -*- Mode: Java -*- 
// FieldP.java --- 
// Author          : Bernie Lofaso
// Created On      : Wed May 14 14:23:32 1997
// Last Modified By: 
// Last Modified On: Fri May 07 10:18:28 1999
// Update Count    : 7
// Status          : Under Development
// 
// $Locker:  $
// $Log: FieldP.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:55  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/05/19 21:25:00  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/05/19 21:25:00  lofaso
// Revised symbol table implementation.
//
// Revision 1.1.1.1  1999/02/18 16:15:44  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:27  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:32  lofaso
// Imported Java 1.1 sources
//
// Revision 1.2  1997/07/07 13:54:06  lofaso
// Modifications to add symbol table support.
//
// Revision 1.1.1.1  1997/05/22 21:30:13  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

public class FieldP extends FieldInfo {
    String name;
    int access_flags;
    Type type;
    String sig;

    public FieldP(String n, int af, Type t, String s) {
	name = n.trim();
	access_flags = af;
	type = t;
	sig = s;
    }

    public String getName() { return(name); }

    public int getModifiers() { return(access_flags); }

    public Type getType() { return(type); }

    // Mainly used to determine if an array and perhaps dimensions
    public String getFieldSignature() { return(sig); }
}
