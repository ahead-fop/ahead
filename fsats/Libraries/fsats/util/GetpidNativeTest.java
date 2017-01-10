//                              -*- Mode: Java -*- 
// File            : $Workfile:    $
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Thu Nov 12 10:48:33 1998
// History

package fsats.util;

/**
 * Test driver for class GetpidNative
 */
public class GetpidNativeTest
{
    public static void main( String[] args )
    {
        int i = GetpidNative.getpid();
        System.out.println("pid = " + i);
        // leave running so process id can be checkedx
        for(;;)  
            ;
    }
}
