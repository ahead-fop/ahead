//                              -*- Mode: Java -*- 
// Pattern.java --- 
// Author          : 
// Created On      : Thu Oct 22 12:54:36 1998
// Last Modified By: 
// Last Modified On: Mon Nov 09 12:43:53 1998
// Update Count    : 3
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;
import java.util.BitSet;

public class Pattern extends Lang.AstList {
    public boolean CheckElemType(Lang.AstNode l) { return(true); }
    public String ElemType() { return(null); }

    public boolean[] terminalList() {
	BitSet terms = new BitSet();
	boolean[] result;
	int count = 0;
	Lang.AstNode ptr;
	Primitive prim;

	ptr = arg[0];
	while (ptr != null) {
	    prim = (Primitive) ptr.arg[0];	// pt to Primitive
	    if (prim.isTerminal())
		terms.set(count);

	    ptr = ptr.right;
	    count++;
	}

	result = new boolean[count];
	count--;
	while (count >= 0) {
	    result[count] = terms.get(count);
	    count--;
	}
	return(result);
    }
}
