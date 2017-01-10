//                              -*- Mode: Java -*- 
// Production.java --- 
// Author          : Bernie Lofaso
// Created On      : Mon Oct 19 09:50:50 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Wed Nov 11 10:19:51 1998
// Update Count    : 8
// Status          : Under Development
// 
// $Locker:  $
// $Log: Production.java,v $
// Revision 1.2  2002-02-22 18:20:09  sarvela
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

public abstract class Production extends Lang.AstNode {
    public boolean SyntaxCheck() { return(true); }

    public abstract void populateSymbolTable();
    public abstract void genParserSource(Lang.AstProperties props);
}
