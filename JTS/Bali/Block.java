//                              -*- Mode: Java -*- 
// Block.java --- 
// Author          : 
// Created On      : Thu Oct 22 14:15:33 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Tue Nov 10 09:38:37 1998
// Update Count    : 5
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class Block extends Lang.AstNode {
    private boolean order[] = new boolean[0];
    public String block;

    public Block setParms(String s) {
	block = s;
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public void print() {
	System.out.print(block);
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(block);
    }
}
