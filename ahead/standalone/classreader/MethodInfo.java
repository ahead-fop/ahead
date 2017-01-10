package ClassReader;

import java.util.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

/**
 * Represent method informations from a class file.
 *
 * @version $Id: MethodInfo.java,v 1.0 2005/01/12
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 */

public final class MethodInfo {
	private String class_name;
	private String name;
	private String[] modifiers;
	private String return_type;
	private String[] arg_types;
	private int start_line;
	private int end_line;
	private Object[] reference_table;
	private boolean isRefined;

	/** This constructor is for the method references in the reference table. therefore no modifiers,
	   lines and reference table are needed.
     */
	public MethodInfo(String class_name, String name, String return_type, String[] arg_types){
		this.class_name = class_name;
		this.name = name;
		this.modifiers = null;
		this.return_type = return_type;
		this.arg_types = arg_types;
		this.start_line = 0;
		this.end_line = 0;
		this.reference_table = null;
		this.isRefined = false;

	}

	/** This constructor is for get a MethodInfo from the byte code */
	public MethodInfo(ConstantPoolGen cp, Method m){
		this.class_name = null;
		this.name = m.getName();
		this.modifiers = getModifiers(m);
		this.return_type = m.getReturnType().toString();
		this.arg_types =getArgTypes(m.getArgumentTypes());
		int[] ls = new int[2];
		ls = getLines(m);
		this.start_line = ls[0];
		this.end_line = ls[1];
		this.reference_table = getReferenceTable(cp,m);
		isRefined = false;
		if (Main.refineinfo){
			for (int i=0; i<reference_table.length; i++){
				if (reference_table[i] instanceof MethodInfo){
					MethodInfo mi = (MethodInfo)reference_table[i];
					if (mi.getClassName().startsWith("stub") && mi.getName().equals(name)){
						isRefined = true;
						break;
					}
				}
			}
		}

	}

 	/** Print out the infomation for a method: if the MethodInfo object is an instance in the reference
 	   table, then only one line is print out. Otherwise, the whole information in that method will show.
 	 */
	public void print(){
		if (class_name == null){
			System.out.println("	name:");
			System.out.println("		"+name);
			System.out.println("	modifiers:");
			if (Main.refineinfo && isRefined)
				System.out.println("		refined");
			for (int i=0; i<modifiers.length; i++)
				System.out.println("		"+modifiers[i]);
			System.out.println("	return type:");
			System.out.println("		"+return_type);
			System.out.println("	argument types:");
			if (arg_types.length==0)
				System.out.println("		null");
			else{
				for (int i=0; i<arg_types.length; i++)
					System.out.println("		"+arg_types[i]);
			 }
			System.out.println("	start line:");
			System.out.println("		"+start_line);
			System.out.println("	end line:");
			System.out.println("		"+end_line);
			System.out.println("	reference table:");
			if (reference_table.length==0)
				System.out.println("		null");
			else{
				for (int i=0; i<reference_table.length; i++){
					if (reference_table[i] instanceof MethodInfo)
						((MethodInfo)reference_table[i]).print();
					else
						((FieldInfo)reference_table[i]).print();
				}
			}

		 }
		 else{
			 StringBuffer sb = new StringBuffer();
			 sb.append("		MethodRefer: ");
			 sb.append(class_name).append(": ");
			 sb.append(return_type).append(" ");
			 sb.append(name).append("(");
			 for (int i=0; i<arg_types.length; i++){
				 sb.append(arg_types[i]);
				 if (i<arg_types.length-1)
				 	sb.append(", ");
			 }
			 sb.append(")");
			 System.out.println(sb.toString());

		 }
	}

	/** The reference table includes all the references for method or field. therefore objects in the returned Object[]
	   may be MethodInfo or FieldInfo. For a method reference, the information includes: class name, method name,
	   return types and arguments types. For a field reference, the information includes: class name, type and field name.
	 */
	public static Object[] getReferenceTable(ConstantPoolGen cp, Method m){
		ArrayList refs = new ArrayList();
		MethodGen mg = new MethodGen(m, null, cp);
		InstructionList il = mg.getInstructionList();
		if (il!=null){
			for(InstructionHandle ih=il.getStart(); ih != null; ih = ih.getNext()) {
				Instruction i = ih.getInstruction();
				if(i instanceof FieldInstruction) {
					FieldInstruction fc = (FieldInstruction)i;
					//Constant      c  = cp.getConstant(ci.getIndex());
					//refs.add("FieldRefer:  "+fc.getClassName(cp)+": "+fc.getFieldType(cp).toString()+" "+fc.getFieldName(cp));
					refs.add(new FieldInfo(fc.getClassName(cp),fc.getFieldName(cp), null, fc.getFieldType(cp).toString()));
				}
				else if(i instanceof InvokeInstruction){
					InvokeInstruction ic = (InvokeInstruction)i;
					//refs.add("MethodRefer: "+ic.getClassName(cp)+": "+getMethodRefer(ic, cp));
					refs.add(getMethodRefer(ic,cp));
				}
			}
		}
		//System.out.println("debug:"+refs.get(0));
		Object[] rs = new Object[refs.size()];
		for (int i=0; i<rs.length; i++){
			rs[i] = refs.get(i);
		}
		return rs;
	}

	/** get a MethodInfo object from the give instruction which will be put into the reference table.
	 */
	public static MethodInfo getMethodRefer(InvokeInstruction ic, ConstantPoolGen cp){
		String cname = ic.getClassName(cp);
		String mname = ic.getMethodName(cp);
		String rtype = ic.getReturnType(cp).toString();
		return new MethodInfo(cname, mname, rtype, getArgTypes(ic.getArgumentTypes(cp)));
		/*
		StringBuffer sb = new StringBuffer();
		sb.append(ic.getReturnType(cp).toString());
		sb.append(" ");
		sb.append(ic.getMethodName(cp));
		sb.append("(");
		String[] ss = getArgTypes(ic.getArgumentTypes(cp));
		for (int i=0; i<ss.length; i++){
			sb.append(ss[i]);
			if (i<ss.length-1)
				sb.append(", ");
		}
		sb.append(")");
		return sb.toString();*/
	}


	public static int[] getLines(Method m){
		LineNumberTable lntable = m.getLineNumberTable();
		int[] lines = new int[2];
		if (lntable==null){
			lines[0] = 0;
			lines[1] = 1;
		}
		else{
			LineNumber[] ln = lntable.getLineNumberTable();
			lines[0] = ln[0].getLineNumber();
			lines[1] = ln[ln.length-1].getLineNumber();
		}
		return lines;
	}

	public static String[] getArgTypes(Type[] ts){
		String[] ss = new String[ts.length];
		for (int i=0; i<ts.length; i++){
			ss[i] = ts[i].toString();
		}
		return ss;
	}

	public static String[] getModifiers(Method m){
		   //get the modifiers
		  ArrayList ll = new ArrayList();
		  if (m.isAbstract()){
			  ll.add ("Abstract");
		  }
		   if (m.isPrivate()){
				  ll.add("Private");
			  }
			  if (m.isProtected()){
				  ll.add("Protected");
			  }
			  if (m.isPublic()){
				  ll.add("Public");
		  }
		  if (m.isFinal()){
			  ll.add("Final");
		  }

		  if (m.isStatic()){
			  ll.add("Static");
		  }


		  if (m.isSynchronized()){
			  ll.add("Synchronized");
		  }
		  if (m.isTransient()){
			  ll.add("Transient");
		  }
		  if (m.isVolatile()){
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

	public String getReturnType(){
		return return_type;
	}

	public String[] getArgTypes(){
		return arg_types;
	}

	public int getStartLine(){
		return start_line;
	}

	public int getEndLine(){
		return end_line;
	}

	public Object[] getReferenceTable(){
		return reference_table;
	}

	public boolean getIsRefined(){
		return isRefined;
	}

}



