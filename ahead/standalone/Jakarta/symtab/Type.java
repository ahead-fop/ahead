//                              -*- Mode: Java -*- 
// Type.java --- 
// Author          : 
// Created On      : Thu May 06 15:24:19 1999
// Last Modified By: 
// Last Modified On: Thu May 13 10:33:14 1999
// Update Count    : 2
// Status          : Under Development
// 

package Jakarta.symtab;

public interface Type extends ClassInfo {
    public int getDim();
    public ClassInfo baseType();
}
