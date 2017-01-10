//
// SMCommand
//
// $Date: 2002-03-14 22:51:29 $
//
// $Revision: 1.1.1.1 $
//
package fsats.util;

/**
 * Extends the fsats.util.Command object and provides a means to spawn a
 * process by looking up the command to be spawned through the
 * StringManager.
 */
public class SMCommand extends Command
{

    /**
     * Spawn the given command with the given arguments. The command reference
     * will be processed by StringManager by passing in any args.
     *
     * @param commandRef StringManager string reference for the command
     * @param args object array of arguments that will be processed by the
     *             StringManager before executing the command
     */
    public SMCommand(String commandRef, Object[] args)
    {
        String cmd = null;
        if (args == null)
        {
            cmd = StringManager.instance().format(commandRef);
        }
        else
        {
            cmd = StringManager.instance().format(commandRef, args);
        }

        if (cmd == null)
        {
            cmd = "";
        }

        // NOTE obviously this is somewhat unixy...
        cmdArray = new String[] {"/bin/sh", "-c", cmd};
    }

    /**
     * Spawn the command referenced by the given command reference string.
     * StringManager will be used to look up the command.
     *
     * @param commandRef StringManager string reference for the command
     */
    public SMCommand(String commandRef)
    {
        this(commandRef, null);
    }
}
