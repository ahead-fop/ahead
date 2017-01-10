//                              -*- Mode: Java -*- 
// NoSuchFieldException.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri May  9 15:18:53 1997
// Last Modified By: Bernie Lofaso
// Last Modified On: Thu May 22 15:52:42 1997
// Update Count    : 2
// Status          : Under Development
// 
// $Locker:  $
// $Log: NoSuchFieldException.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:56  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:46  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:45  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:27  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:32  lofaso
// Imported Java 1.1 sources
//
// Revision 1.1.1.1  1997/05/22 21:30:14  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

public class NoSuchFieldException extends Exception {
    public NoSuchFieldException() { super(); }
    public NoSuchFieldException(String s) { super(s); }
}
