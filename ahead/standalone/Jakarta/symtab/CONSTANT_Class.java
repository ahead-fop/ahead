//                              -*- Mode: Java -*- 
// CONSTANT_Class.java --- 
// Author          : Bernie Lofaso
// Created On      : Wed May  7 16:37:16 1997
// Last Modified By: Bernie Lofaso
// Last Modified On: Thu May 22 15:46:00 1997
// Update Count    : 6
// Status          : Under Development
// 
// $Locker:  $
// $Log: CONSTANT_Class.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:28  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:44  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:43  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:25  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:30  lofaso
// Imported Java 1.1 sources
//
// Revision 1.1.1.1  1997/05/22 21:30:11  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

public class CONSTANT_Class {
    public int name_index;

    public CONSTANT_Class(int index) { name_index = index; }
}
