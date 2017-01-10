//                              -*- Mode: Java -*- 
// CONSTANT_InterfaceMethodref.java --- 
// Author          : Bernie Lofaso
// Created On      : Wed May  7 16:45:51 1997
// Last Modified By: Bernie Lofaso
// Last Modified On: Thu May 22 15:48:39 1997
// Update Count    : 8
// Status          : Under Development
// 
// $Locker:  $
// $Log: CONSTANT_InterfaceMethodref.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:38  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:44  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:44  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:25  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:30  lofaso
// Imported Java 1.1 sources
//
// Revision 1.1.1.1  1997/05/22 21:30:12  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

public class CONSTANT_InterfaceMethodref {
    public int name_index;
    public int name_and_type_index;

    public CONSTANT_InterfaceMethodref(int ni, int nti) {
	name_index = ni;
	name_and_type_index = nti;
    }
}
