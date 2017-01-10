//                              -*- Mode: Java -*- 
// TailListElem.java --- 
// Author          : Bernie Lofaso
// Created On      : Mon Oct 19 09:26:44 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Fri Nov 13 10:11:08 1998
// Update Count    : 6
// Status          : Under Development
// 
// $Locker:  $
// $Log: TailListElem.java,v $
// Revision 1.2  2002-02-22 18:20:12  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:28  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:28  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import JakBasic.Lang;

public class TailListElem extends Lang.AstListNode {
    public TailListElem setParms(Lang.AstToken t, Lang.AstNode cls) {
	tok = new Lang.AstToken[1];
	tok[0] = t;
	return((TailListElem) setParms(cls));
    }

    public boolean CheckArgType() { return(true); }
    public String ArgType() { return(null); }
}
