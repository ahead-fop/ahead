//                              -*- Mode: Java -*- 
// BadSymbolNameException.java --- 
// Author          : Bernie Lofaso
// Created On      : Thu Apr 01 11:28:57 1999
// Last Modified By: 
// Last Modified On: Thu Apr 01 11:30:12 1999
// Update Count    : 1
// Status          : Under Development
// 

package Jakarta.symtab;

public class BadSymbolNameException extends Exception {
    public BadSymbolNameException() { super(); }
    public BadSymbolNameException(String s) { super(s); }
}
