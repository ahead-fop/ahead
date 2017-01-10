package ClassReader;

import java.util.*;
import org.apache.bcel.classfile.*;

/**
 * Represent field informations from a class file.
 *
 * @version $Id: FieldInfo.java,v 1.0 2005/01/12
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 */

public final class FieldInfo {
	private String class_name;
	private String name;
	private String[] modifiers;
	private String type;

	/** This constructor is for the field references in the reference table, Therefore no modifiers
	   are needed.
	 */
	public FieldInfo(String class_name,String name, String[] modifiers, String type){
		this.class_name = class_name;
		this.name = name;
		this.modifiers = modifiers;
		this.type = type;
	}

    /** Construct a FieldInfo from the byte code */
	public FieldInfo(Field f){
		this.class_name = null;
		this.name = f.getName();
		this.type = f.getType().toString();
		this.modifiers = getModifiers(f);
	}

	public static String[] getModifiers(Field f){
		  ArrayList ll = new ArrayList();
		  if (f.isAbstract()){
			  ll.add ("Abstract");
		  }
		   if (f.isPrivate()){
				  ll.add("Private");
			  }
			  if (f.isProtected()){
				  ll.add("Protected");
			  }
			  if (f.isPublic()){
				  ll.add("Public");
		  }
		  if (f.isFinal()){
			  ll.add("Final");
		  }

		  if (f.isStatic()){
			  ll.add("Static");
		  }


		  if (f.isSynchronized()){
			  ll.add("Synchronized");
		  }
		  if (f.isTransient()){
			  ll.add("Transient");
		  }
		  if (f.isVolatile()){
			  ll.add("Volatile");
		  }

		  String[] flags = new String[ll.size()];
		  for (int i=0; i<ll.size(); i++){
			  flags[i]=(String)ll.get(i);
		  }
		  return flags;
	  }

	public String getClassName(){
		return class_name;
	}

	public String getName(){
		return name;
	}

	public String[] getModifiers(){
		return modifiers;
	}

	public String getType(){
		return type;
	}

	public void print(){
		if (class_name == null){
			System.out.println("	name:");
			System.out.println("		"+name);
			System.out.println("	type:");
			System.out.println("		"+type);
			System.out.println("	modifiers:");
			for (int i=0; i<modifiers.length; i++)
				System.out.println("		"+modifiers[i]);
			System.out.println("");
		}
		else{
			System.out.print("		FieldRefer:  "+class_name+": "+type+" "+name);
		}
	}


}



