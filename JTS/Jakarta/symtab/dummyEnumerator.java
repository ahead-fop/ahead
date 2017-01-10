//                              -*- Mode: Java -*- 
// dummyEnumerator.java --- 
// Author          : 
// Created On      : Mon May 17 08:41:52 1999
// Last Modified By: 
// Last Modified On: Mon May 17 08:42:50 1999
// Update Count    : 1
// Status          : Under Development
// 

package Jakarta.symtab;

import java.util.Enumeration;
import java.util.NoSuchElementException;

class dummyEnumerator implements Enumeration {
    public boolean hasMoreElements() {
	return(false);
    }

    public Object nextElement() throws NoSuchElementException {
	throw new NoSuchElementException();
    }
}
