//                              -*- Mode: Java -*- 
// ArrayEnumeration.java --- 
// Author          : Bernie Lofaso
// Created On      : Tue May 13 16:40:11 1997
// Last Modified By: Bernie Lofaso
// Last Modified On: Thu May 22 15:45:06 1997
// Update Count    : 3
// Status          : Under Development
// 
// $Locker:  $
// $Log: ArrayEnumeration.java,v $
// Revision 1.4  2007-08-13 19:39:26  dsb
// reinstalling deleted files (sigh).
//
// Revision 1.2  2001/12/04 15:53:20  sarvela
// Removed redundant and error-prone carriage returns.
//
// Revision 1.1.1.1  1999/02/18 14:15:44  sarvela
// Imported original v3.0beta4 sources from Webpage
//
// Revision 1.1.1.1  1999/02/18 16:15:43  lofaso
// Snapshot 2-18-99
//
// Revision 1.1.1.1  1997/12/15 21:00:25  lofaso
// Imported sources
//
// Revision 1.1.1.1  1997/08/25 20:06:30  lofaso
// Imported Java 1.1 sources
//
// Revision 1.1.1.1  1997/05/22 21:30:11  lofaso
// baseline sources
//
// 

package Jakarta.symtab;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ArrayEnumeration implements Enumeration {
    Object[] array;
    int index;

    public ArrayEnumeration(Object[] a) {
	array = a;
	index = 0;
    }

    public boolean hasMoreElements() {
	return(index < array.length);
    }

    public Object nextElement() {
	if (index == array.length)
	    throw new NoSuchElementException();
	return(array[index++]);
    }
}
