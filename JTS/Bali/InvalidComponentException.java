//                              -*- Mode: Java -*- 
// InvalidComponentException.java --- 
// Author          : Bernie Lofaso
// Created On      : Thu Sep 18 10:17:17 1997
// Last Modified By: 
// Last Modified On: Mon Nov 09 12:40:13 1998
// Update Count    : 3
// Status          : Under Development
// 
// $Locker:  $
// $Log: InvalidComponentException.java,v $
// Revision 1.2  2002-02-22 18:20:06  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:26  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:26  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:14  lofaso
// Imported sources
//
// 

package Bali;

public class InvalidComponentException extends Exception {
    public InvalidComponentException() { super(); }
    public InvalidComponentException(String s) { super(s); }
}
