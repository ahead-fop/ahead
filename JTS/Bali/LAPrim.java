//                              -*- Mode: Java -*- 
// LAPrim.java --- 
// Author          : 
// Created On      : Thu Oct 22 14:04:52 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 30 15:50:24 1998
// Update Count    : 11
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class LAPrim extends Primitive {
    private boolean order[] = { false, false };
    private String varName;

    public LAPrim setParms(Lookahead la, Primitive prim) {
	arg = new Lang.AstNode[2];
	arg[0] = la;
	arg[1] = prim;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public boolean isTerminal() { return(((Primitive) arg[1]).isTerminal()); }

    public void print() {
	System.out.print(" [");
	arg[0].print();		// print lookahead
	arg[1].print();		// print primitive
	System.out.print(" ]");
    }


    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(" [");
	arg[0].print(props);	// print lookahead
	arg[1].print(props);	// print primitive
	pw.print(" ]");
    }

    public void setPrimitiveType(PrimitiveInfo pInfo) {
	int index;

	pInfo.nextIsOptional();
	((Primitive) arg[1]).setPrimitiveType(pInfo);
	index = pInfo.variableCount() - 1;
	varName = pInfo.variableName(index);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(" [ ");
	((Lookahead) arg[0]).print(props);
	((Primitive) arg[1]).genParserSource(props);
	pw.print(" ]");
    }
}
