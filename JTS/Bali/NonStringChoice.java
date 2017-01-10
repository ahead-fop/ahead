//                              -*- Mode: Java -*- 
// NonStringChoice.java --- 
// Author          : Bernie Lofaso
// Created On      : Mon Oct 26 09:11:00 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 10 10:59:51 1998
// Update Count    : 3
// Status          : Under Development
// 
// $Locker:  $
// $Log: NonStringChoice.java,v $
// Revision 1.2  2002-02-22 18:20:08  sarvela
// Removed carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:26  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:26  lofaso
// Snapshot 2-18-99
//
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class NonStringChoice extends CmpREChoice {
    private boolean order[] = new boolean[0];
    private String choice;	// text up to matching ">"

    public NonStringChoice setParms(String nsc) {
	choice = nsc;
	return(this);
    }

    public boolean[] printorder() { return(order); }

    public void print() {
	System.out.print(choice + ">");
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(choice + ">");
    }
}
