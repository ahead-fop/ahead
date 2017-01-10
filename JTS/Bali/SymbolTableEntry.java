//                              -*- Mode: Java -*- 
// SymbolTableEntry.java --- 
// Author          : Bernie Lofaso
// Created On      : Thu Oct 29 15:40:37 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 14:38:51 1998
// Update Count    : 5
// Status          : Under Development
// 
// $Locker:  $
// $Log: SymbolTableEntry.java,v $
// Revision 1.2  2002-02-22 18:20:11  sarvela
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

public class SymbolTableEntry {
    private String name;
    private Lang.AstNode node;
    private String _superclass;
    private boolean listClass = false;
    private int first_subclass, class_code;

    public SymbolTableEntry(String name, Lang.AstNode node) {
	this.name = name;
	this.node = node;
    }

    // name and node members have only 'get' accessors.
    public String getName() { return(name); }
    public Lang.AstNode getNode() { return(node); }

    public void superclass(String name) { _superclass = name; }
    public String superclass() { return(_superclass); }

    public void markListClass() { listClass = true; _superclass = "AstList"; }
    public boolean isListClass() { return(listClass); }

    public void firstSubclass(int fsc) { first_subclass = fsc; }
    public int firstSubclass() { return(first_subclass); }

    public void classCode(int cc) { class_code = cc; }
    public int classCode() { return(class_code); }
}
