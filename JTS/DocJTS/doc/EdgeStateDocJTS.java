/** JTS Javadoc
 EdgeStateDocJTS.java
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

public class EdgeStateDocJTS 
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
	
	/** Start name.
	 */
	String startName;
	
	/** End name.
	 */
	String endName;
	
	/** Conditions string.
	 */
	String conditions;
	
	/** Additional constructor.
	 */
	public EdgeStateDocJTS(String _name, LinkedList _classes) 
	{ name = _name;  classes = _classes; }
	
	// *** Method summary.
	/** Gets the name of the state.
	 */
	public String name() { return name; }

	/** Gets the list of the classes/interfaces.
	 */
	public LinkedList classes() { return classes; }
	
	/** Gets start name.
	 */
	public String startName() { return startName; }
	
	/** Gets end name.
	 */
	public String endName() { return endName; }
	
	/** Conditions string.
	 */
	public String conditions() { return conditions; }
	
	/** Sets the name of the state.
	 */
	public void setName(String _name) { name = _name; }
	
	/** Sets the list of classes/interfaces.
	 */
	public void setClasses(LinkedList _classes) { classes = _classes; }
	
	/** Sets the start name.
	 */
	public void setStartName(String _startName) { startName = _startName; }
	
	/** Sets the end name.
	 */
	public void setEndName(String _endName) { endName = _endName; }
	
	/** Sets the conditions string.
	 */
	public void setConditions(String _conditions) { conditions = _conditions; }
	
} // of EdgeStateDocJTS
