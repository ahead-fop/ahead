//                              -*- Mode: Java -*- 
// PrimitiveInfo.java --- 
// Author          : Bernie Lofaso
// Created On      : Thu Nov 12 12:36:41 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 23 10:31:12 1998
// Update Count    : 9
// Status          : Under Development
// 
// $Locker:  $
// $Log: PrimitiveInfo.java,v $
// Revision 1.2  2002-02-22 18:20:09  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:26  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:25  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import java.util.Vector;

//**************************************************
// The purpose of this class is to allow the Primitive classes to
// state what type they are (terminal or non-terminal) and recieve
// a variable name for that Primitive to use in generating an assignment
// to a class-static variable. Additionally, once all Primitive's in
// a pattern have "registered" their type, a Tail class can scan for
// the variable names used for each Primitive. It uses this information to
// generate a parameter list for the setParms() call in the action at
// the end of each rule clause.
//**************************************************

public class PrimitiveInfo {
    static private class PType {
	boolean optional;
	boolean terminal;
	int num;
	String name;

	public PType(int n, boolean term, boolean opt) {
	    num = n;
	    terminal = term;
	    optional = opt;
	    name = (term ? "" : "n") + "t" + Integer.toString(n);
	}
    }

    Vector ptInfo = new Vector();	// Stores PType objects

    // Counts of terminals and non-terminals
    int t_cnt = 0;
    int nt_cnt = 0;

    // Flag to indicate next "registration" is for an optional Primitive
    boolean optional = false;

    //**************************************************
    // This method clears the information stored on the Primitives.
    //**************************************************
    public void reset() {
	ptInfo.removeAllElements();
	t_cnt = 0;
	nt_cnt = 0;
	optional = false;
    }

    //**************************************************
    // When this method is called, the next Primitive "registered" will
    // be treated as an optional Primitive.
    //**************************************************
    public void nextIsOptional() { optional = true; }

    //**************************************************
    // These are used by Primitive's to "register" their type. Each
    // Primitive must call one (and only one) of these methods exactly once.
    // The return value is the index of the Primitive, not the number
    // that follows the variable name.
    //**************************************************
    public int terminal() {
	PType ptype;

	t_cnt++;
	ptype = new PType(t_cnt, true, optional);
	optional = false;
	ptInfo.addElement(ptype);
	return(ptInfo.size()-1);
    }

    public int nonTerminal() {
	PType ptype;

	nt_cnt++;
	ptype = new PType(nt_cnt, false, optional);
	optional = false;
	ptInfo.addElement(ptype);
	return(ptInfo.size()-1);
    }


    //**************************************************
    // The methods below are used by the Primitives.
    //**************************************************

    public int variableCount() { return(ptInfo.size()); }
    public int terminalCount() { return(t_cnt); }
    public int nonTerminalCount() { return(nt_cnt); }

    public String variableName(int index) {
	PType ptype;

	try {
	    ptype = (PType) ptInfo.elementAt(index);
	}
	catch (Exception e) {
	    return("");
	}
	return(ptype.name);
    }

    public boolean isOptional(int index) { 
	PType ptype;

	try {
	    ptype = (PType) ptInfo.elementAt(index);
	}
	catch (Exception e) {
	    return(false);
	}
	return(ptype.optional);
    }

    public boolean isTerminal(int index) { 
	PType ptype;

	try {
	    ptype = (PType) ptInfo.elementAt(index);
	}
	catch (Exception e) {
	    return(false);
	}
	return(ptype.terminal);
    }

    public int number(int index) { 
	PType ptype;

	try {
	    ptype = (PType) ptInfo.elementAt(index);
	}
	catch (Exception e) {
	    return(0);
	}
	return(ptype.num);
    }
}
