package ClassReader;

import java.util.*;
import org.apache.bcel.classfile.*;

/**
 * Represent informations from a class file.
 *
 * @version $Id: ClassInfo.java,v 1.0 2005/01/12
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 */

public final class ClassInfo {
	private String package_name;
	private String class_name;
	private String[] modifiers;
	private String super_class;
	private String[] super_interfaces;
	private MethodInfo[] methods;
	private FieldInfo[] fields;
	private boolean isRefined;


	public ClassInfo(String package_name, String class_name, String[] modifiers, String super_class,
						String[] super_interfaces, MethodInfo[] methods, FieldInfo[] fields){
		this.package_name = package_name;
		this.class_name = class_name;
		this.modifiers = modifiers;
		this.super_class = super_class;
		this.super_interfaces = super_interfaces;
		this.methods = methods;
		this.fields = fields;
		if (Main.refineinfo){
			if (super_class.startsWith("stub."))
				isRefined = true;
			else
				isRefined = false;
		}
		else
			isRefined = false;
	}

	public String getPackage(){
		return package_name;
	}

	public String getName(){
		return class_name;
	}

	public String[] getModifiers(){
		return modifiers;
	}

	public String getSuperClass(){
		return super_class;
	}

	public String[] getSuperInterfaces(){
		return super_interfaces;
	}

	public MethodInfo[] getMethods(){
		return methods;
	}

	public FieldInfo[] getFields(){
		return fields;
	}

	public boolean getIsRefined(){
		return isRefined;
	}

	public void print(){
		System.out.println("package:");
		System.out.println("	"+package_name);
		System.out.println("class name:");
		System.out.println("	"+class_name);
		System.out.println("modifiers: ");
		if (Main.refineinfo && isRefined)
			System.out.println("	refined");
		for (int i=0; i<modifiers.length; i++)
			System.out.println("	"+modifiers[i]);
		System.out.println("super class: ");
		System.out.println("	"+super_class);
		System.out.println("super interfaces:");
		if (super_interfaces.length==0)
			System.out.println("		null");
		else{
			for (int j=0; j<super_interfaces.length; j++)
				System.out.println("	"+super_interfaces[j]);
		}
		System.out.println("methods:");
		for (int i=0; i<methods.length; i++)
			methods[i].print();
		System.out.println("fields:");
		for (int i=0; i<fields.length; i++)
			fields[i].print();
	}

}



