/** JTS Javadoc
 StateDocDiagram.java
 University of Texas at Austin
 Department of Computer Sciences
 Product-line Architecture Research Group
 @author Roberto E. Lopez-Herrejon
 @version 0.3
 June 2001
*/

package jtsdoc;

import com.sun.javadoc.*;

public interface StateDiagramDoc extends com.sun.javadoc.Doc 
{
//    public abstract com.sun.javadoc.ClassDoc containingClass();
//    public abstract com.sun.javadoc.PackageDoc containingPackage();
    public abstract java.lang.String qualifiedName();
//    public abstract int modifierSpecifier();
    public abstract java.lang.String modifiers();
    public abstract boolean isPublic();
    public abstract boolean isProtected();
    public abstract boolean isPrivate();
//  public abstract boolean isPackagePrivate();
    public abstract boolean isStatic();
    public abstract boolean isFinal();
	
	// added
	public abstract boolean isRelative();
	public abstract boolean isAbstract();
	public abstract boolean isTransient();
	public abstract boolean isVolatile();
	public abstract boolean isNative();
	public abstract boolean isSynchronized();
	
	
}
