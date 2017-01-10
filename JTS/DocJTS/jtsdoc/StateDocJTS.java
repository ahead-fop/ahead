/** JTS Javadoc
 StateDocJTS.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;
import com.sun.javadoc.*;
import java.util.*;

public class StateDocJTS 
{
	// *************************************************************************
	// *************************************************************************
	// *** Field Summary 
	/** Contains the name of the state.
	 */
	String name;
	
	/** List that contains the classes/interfaces that are in the block
	 * of Exit, Enter or Otherwise state declarations.
	 */
	LinkedList classes;
	
	/** Additional constructor.
	 */
	public StateDocJTS(String _name, LinkedList _classes) 
	{ name = _name;  classes = _classes; }
	
	// *** Method summary.
	/** Gets the name of the state.
	 */
	public String name() { return name; }

	/** Gets the list of the classes/interfaces.
	 */
	public LinkedList classes() { return classes; }
	
	/** Sets the name of the state.
	 */
	public void setName(String _name) { name = _name; }
	
	/** Sets the list of classes/interfaces.
	 */
	public void setClasses(LinkedList _classes) { classes = _classes; }
	
} // of StateDocJTS
