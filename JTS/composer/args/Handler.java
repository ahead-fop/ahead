package composer.args ;

/**
 * An argument <code>Handler</code> interacts with an argument {@link Parser}
 * to process individual argument tokens from a command line.
 **/
public interface Handler {

    /**
     * Returns the current value of this <code>Handler</code> or
     * <code>null</code> if there is no current value.
     **/
    public Object getValue () throws ArgumentException ;

    /**
     * Converts a {@link java.lang.String} (as would be specified on a command
     * line) to an internal representation for arguments of this
     * <code>Handler</code>'s kind.  For example, if the <code>argument</code>
     * represented an input file name, this method might return an object of
     * type {@link java.io.File}.
     *
     * @param argument the {@link java.lang.String} to convert or
     * <code>null</code> if no value was specified on the command line
     * (<code>null</code> is only possible if {@link #requiresValue()} returns
     * <code>false</code>).
     *
     * @return an internal object representing <code>argument</code>.
     **/
    public Object handle (String argument) throws ArgumentException ;

    public boolean requiresValue () ;

}
