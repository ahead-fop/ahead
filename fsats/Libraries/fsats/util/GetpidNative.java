//                              -*- Mode: Java -*- 
// File            : $Workfile:    $
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Thu Nov 12 10:46:50 1998
// History

package fsats.util;

/**
 * Java native interface class to return the process ID.
 */
public class GetpidNative
{
    public native static int getpid();
    static { System.loadLibrary("GetpidNative"); }

}
