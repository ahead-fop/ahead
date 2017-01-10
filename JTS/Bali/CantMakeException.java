//                              -*- Mode: Java -*- 
// CantMakeException.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Jan 17 14:28:39 1997
// Last Modified By: 
// Last Modified On: Mon Nov 09 12:36:08 1998
// Update Count    : 4
// Status          : Under Development
// 
// $Locker:  $
// $Log: CantMakeException.java,v $
// Revision 1.2  2002-02-22 18:20:05  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:26  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:25  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:11  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:16  lofaso
// Imported Java 1.1 sources
//
// Revision 1.1.1.1  1997/02/28 16:46:44  lofaso
// Imported Sources
//
// 

package Bali;

public class CantMakeException extends Exception {
    public CantMakeException() { super(); }
    public CantMakeException(String s) { super(s); }
}
