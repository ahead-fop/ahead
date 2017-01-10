//                              -*- Mode: Java -*- 
// TokenMgrDecls.java --- 
// Author          : Bernie Lofaso
// Created On      : Mon Oct 19 09:55:57 1998
// Last Modified By: 
// Last Modified On: Thu Aug 19 13:42:14 1999
// Update Count    : 10
// Status          : Under Development
// 
// $Locker:  $
// $Log: TokenMgrDecls.java,v $
// Revision 1.2  2002-02-22 18:20:12  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/08/19 19:27:00  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.2  1999/08/19 19:26:59  lofaso
// Corrected various problems with the Bali input format not accepting or not
// properly generating code relating to options, parser code, token manager
// declarations, and javacode.
//
// Revision 1.1.1.1  1999/02/18 16:15:28  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class TokenMgrDecls extends Production {
    private boolean[] order = { false };

    public TokenMgrDecls setParms(Block b) {
	arg = new Lang.AstNode[1];
	arg[0] = b;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public void populateSymbolTable() { }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print("\nTOKEN_MGR_DECLS :");
	print(props);
	pw.print("\n}\n");
    }
}
