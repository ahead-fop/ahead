//                              -*- Mode: Java -*- 
// Primitive.java --- 
// Author          : 
// Created On      : Thu Oct 22 13:00:05 1998
// Last Modified By: 
// Last Modified On: Fri Nov 20 22:25:08 1998
// Update Count    : 8
// Status          : Under Development
// 

package Bali;

import JakBasic.Lang;

public abstract class Primitive extends Lang.AstNode {
    public boolean SyntaxCheck() { return(true); }

    public abstract boolean isTerminal();
    public abstract void setPrimitiveType(PrimitiveInfo pInfo);
    public abstract void genParserSource(Lang.AstProperties props);
}
