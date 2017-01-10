package Jakarta.collection ;

/**
 * An <kbd>InstanceFilter</kbd> accepts only those objects that are instances
 * of a specified <kbd>Class</kbd> given in the <kbd>InstanceFilter</kbd>
 * constructor.
 **/
public class InstanceFilter extends AbstractFilter {

    public InstanceFilter (Class type) {
	if (type == null)
	    throw new NullPointerException ("InstanceFilter(Class)") ;
	this.type = type ;
    }

    public boolean acceptObject (Object trialObject) {
	return type.isInstance (trialObject) ;
    }

    protected Class type ;

}
