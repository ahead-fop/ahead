//                              -*- Mode: Java -*- 
// OptPrim.java --- 
// Author          : 
// Created On      : Thu Oct 22 14:08:34 1998
// Last Modified By: Bernie Lofaso
// Last Modified On: Mon Nov 30 15:49:51 1998
// Update Count    : 11
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class OptPrim extends Primitive {
    private boolean order[] = { false };
    private String varName;

    public OptPrim setParms(Primitive prim) {
	arg = new Lang.AstNode[1];
	arg[0] = prim;
	InitChildren();
	return(this);
    }

    public boolean[] printorder() { return(order); }
    public boolean SyntaxCheck() { return(true); }

    public boolean isTerminal() { return(((Primitive) arg[0]).isTerminal()); }

    public void setPrimitiveType(PrimitiveInfo pInfo) {
	int index;

	pInfo.nextIsOptional();
	((Primitive) arg[0]).setPrimitiveType(pInfo);
	index = pInfo.variableCount() - 1;
	varName = pInfo.variableName(index);
    }

    public void genParserSource(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(" [ ");
	((Primitive) arg[0]).genParserSource(props);
	pw.print(" ]");
    }

    public void print() {
	System.out.print(" [");
	arg[0].print();		// print primitive
	System.out.print(" ]");
    }

    public void print(Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");

	pw.print(" [");
	arg[0].print(props);	// print primitive
	pw.print(" ]");
    }
}
