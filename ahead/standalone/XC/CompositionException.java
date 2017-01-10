/** CompositionException.java
 * Exception thrown when the XMLComposer cannot proceed.
 * Reasons: Schema violation, files missing
 * AHEAD Project
 * @author Roberto E. Lopez-Herrejon
 * Last Update: July 2, 2003 4:20 pm
 */

package XC;

public class CompositionException extends Exception {
    public String message;
    public CompositionException(String message) {
	super(message);
	this.message = message;
    }
}
