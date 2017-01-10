/*
 * Created on Mar 24, 2005
 */
package ProgramCube.Validation.ortho;

/**
 * This class represents an item (data member / method) within a feature
 * 
 * @author hamid
 * @version 1.0
 */
public class PLItem {
    
    /**
     * Name of the class that contains the member
     */
    public String className;
    
    /**
     * Actual name of the member
     */
    public String memberName;
    
    /**
     * Type of the member
     */
    public String typeName;
    
    /**
     * Signature of the member (empty for variables)
     */
    public String signature;
    
    /**
     * Feature containing the member (item)
     */
    public Layer container;
    
    /**
     * Is the member (method) an extension?
     */
    public boolean isRefined;
    
    //true=ref, false=item
    public boolean isReference;
    
    
    /**
     * Constructs an item with no information
     */
    public PLItem() {
        super();
    }

    /**
     * Constructs an item for a feature
     * 
     * @param c Feature containing this item
     * @param cls Name of the item's class
     * @param mem Name of the member
     * @param typ Type of the member
     * @param sign Signature of the member
     * @param ref The member extends (refines) a member
     */
    public PLItem(Layer c, String cls, String mem, 
            String typ, String sign, boolean ref, boolean isReference) {
        super();
        
        container = c;
        className = cls;
        memberName = mem;
        typeName = typ;
        signature = sign;
        isRefined = ref;
        
        this.isReference = isReference;
    }

    /**
     * Returns information about PLItem in the form of a string
     * @return String representation
     */
    public String toString() {
    	if (isReference)
    		return "[" + className + "] references "+ typeName + " " + memberName + signature;
    	else
    		return "[" + className + "] defines "+ typeName + " " + memberName + signature;
    	
        //return "[" + className + "] "+ typeName + " " + memberName + signature;
    }

    /**
     * Returns a key string for the PLItem. This key is used for hashing.
     * @return Key to be hashed
     */
    public String keyString() {
    	if (isReference)
    		return "[" + className + "] REFERENCES " + memberName + signature;
    	else
    		return "[" + className + "] DEFINES " + memberName + signature;
    }
    
}
