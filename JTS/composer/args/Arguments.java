package composer.args ;

import java.util.List ;

/**
 * Represents the result of parsing command-line arguments.  Command-line
 * arguments are either <em>operands</em> or <em>options</em>.  Operands are
 * positional arguments while option are named arguments specified via a
 * keyword argument of the form <code>-key</code> or <code>--key</code>
 * (<em>key</em> can be any valid identifier).  Options may also take a value
 * which can be specified with either the form <code>-key value</code> or
 * <code>--key=value</code> (<em>value</em> may be any sequence of characters
 * allowed by the command-line shell).
 **/
public interface Arguments {

    /**
     * Returns the value of a command-line option.  This value is usually an
     * internal representation after processing by a {@link Handler}.  For
     * example, a filename argument may be represented by a
     * {@link java.io.File}.
     *
     * @param optionName the keyword of the option without leading dashes
     * (for example, <code>key</code> would be used for options specified with
     * <code>-key</code> or <code>--key</code>.
     **/
    public Object getOption (String optionName) ;

    /**
     * Returns a {@link List} of positional arguments in the order they
     * occurred on the command line.  The list elements are usually an internal
     * representation after processing by a {@link Handler}.  For example, a
     * filename argument may be represented by a {@link java.io.File}.
     **/
    public List operands () ;

}
