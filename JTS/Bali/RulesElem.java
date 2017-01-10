//                              -*- Mode: Java -*- 
// RulesElem.java --- 
// Author          : Bernie Lofaso
// Created On      : Fri Oct 16 14:41:54 1998
// Last Modified By: 
// Last Modified On: Mon Nov 09 12:46:34 1998
// Update Count    : 2
// Status          : Under Development
// 
// $Locker:  $
// $Log: RulesElem.java,v $
// Revision 1.2  2002-02-22 18:20:11  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:28  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:27  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import JakBasic.Lang;

public class RulesElem extends Lang.AstListNode {
    public boolean CheckArgType() { return(true); }
    public String ArgType() { return(null); }
}
