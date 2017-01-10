package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class RequireType extends Lang.AstNode {

    private boolean order[] = { true };

    public RequireType setParms (Lang.AstToken ruleName) {
	tok = new Lang.AstToken[] {ruleName} ;
	return this ;
    }

    public boolean[] printorder() { return(order); }

    public boolean SyntaxCheck() { return(true); }

    public void populateSymbolTable () { }

    public void print() {
	System.out.print(" ");
	tok[0].print();
    }

    public void print (Lang.AstProperties props) {
	PrintWriter pw = (PrintWriter) props.getProperty("output");
	pw.print(" -> ");
	tok[0].print(props);
    }

}
