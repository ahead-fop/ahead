package Bali;

import JakBasic.Lang;
import java.io.PrintWriter;

public class RequireRule extends Lang.AstNode {

    private boolean order[] = { true, false };

    public RequireRule setParms (Lang.AstToken ruleName, Lang.AstOptNode type){
	arg = new Lang.AstNode [1] ;
	arg[0] = type ;
	tok = new Lang.AstToken [1] ;
	tok[0] = ruleName ;
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
	pw.print(" ");
	tok[0].print(props);
    }

}
