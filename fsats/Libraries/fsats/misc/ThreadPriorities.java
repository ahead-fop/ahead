//                              -*- Mode: Java -*- 
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Ted Beckett

package fsats.misc;


public interface ThreadPriorities
{
    
    /**
     * The DOE threads which send the Heartbeat, receive the Heartbeat
     * and check for expired DOEs.
     */
    public final static int DOE_HEARTBEAT_PRIORITY = Thread.MAX_PRIORITY;

    /**
     * The DOE threads which send and receive messages from remote DOEs.
     */
    public final static int DOE_MESSAGE_PASSING_PRIORITY = 
    ( DOE_HEARTBEAT_PRIORITY - 1 );
    
    /**
     * The thread which implements a DOE object.
     */
    public final static int DOE_OBJECT_PRIORITY = Thread.NORM_PRIORITY;
    
}    
