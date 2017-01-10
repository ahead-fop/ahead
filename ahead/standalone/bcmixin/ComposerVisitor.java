
package bcmixin;
import org.apache.bcel.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import java.util.*;
import java.io.*;

/**
 * visit class file(s) and merge them to a given new class file.
 *
 * @version $Id: Merger.java,v 1.0 2002/11/06
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 **/
public class ComposerVisitor extends org.apache.bcel.classfile.EmptyVisitor
	implements org.apache.bcel.classfile.Visitor {
	/** The reference to the {@link ClassGen} of the new created class.
    */
	private ClassGen cg=null;

	/** The reference to the {@link ConstantPoolGen} of the new created class.
	 */
	private ConstantPoolGen cp=null;

	/** The reference to the new class name
	 */
	private String class_name=null;

	/** The reference to the new super_name
	 */
	private String super_name=null;

	/** a list to hold the renamed class name
	 */
	private ArrayList names = null;

	/** The reference to the {@link ConstantPool} of the {@link JavaClass}
	 *  being visited.
        **/
        private ConstantPool pool = null ;

	/** The reference to the {@link ConstantPoolGen} of the {@link JavaClass}
	 *  being visited.
	 */
	private ConstantPoolGen cur_cp=null;

	/** The reference to the being visited class name.
	 */
	private String cur_class_name=null;

	/** a HashSet holds all the defined constructs so far
	 */
	private HashSet downwards;

	/** The constructors only defined in this layer
	 */
	private HashSet cur_cons;

    /** The string redirect object to redirect all the constant string which have exact same value with the
      * renamed class to the new constant pool entry.
      */
    //private StringRedirect sr;

   /**
    * Provides an <code>ComposerVisitor</code> with a ClassGen object.
    **/
   public ComposerVisitor (HashSet downwards) {
	   this.downwards = downwards;
       names = new ArrayList();
   }

	  /**
    * Visits the reference to the entire constant pool as represented by a
    * BCEL {@link ConstantPool} object.  The reference is captured for later
    * use within this class.  This is necessary because all BCEL constant
    * pool objects require that a {@link ConstantPool} reference be provided
    * when retrieving information from the pool.
    *
    * @param constantPool
    * a {@link ConstantPool} object from the {@link JavaClass} being visited.
    **/
   public void visitConstantPool (ConstantPool constantPool) {
	   pool = constantPool ;
	   //cur_cp= new ConstantPoolGen(pool); //ConstantPoolGen should get from ClassGen
       //cp = cur_cp;
       cur_cons = new HashSet();
   }

   public void visitJavaClass(JavaClass curClass) {
        cg = new ClassGen(curClass);
        cur_cp = cg.getConstantPool();
        cp = cur_cp;
        //sr = new StringRedirect(cg);
        cur_class_name=curClass.getClassName();
        //System.out.println("The visiting class: "+cur_class_name + " in layer "+ClassFileComposer.cur_layer_name);

        //Rename the class name if it's not the last one
        if (ClassFileComposer.isLast){
                class_name = cur_class_name;
                //System.out.println("the last one: "+class_name);
                class_name = cur_class_name.substring(0, cur_class_name.indexOf("$"));
                renameClass();
        }
        else{
                class_name = cur_class_name+ClassFileComposer.cur_layer_name;
                //System.out.println("new class name: "+class_name);
                names.add(class_name);
                renameClass();
        }

        //Rename the super class if it's not the first one
        if (ClassFileComposer.layer!=0){
                String s = (String)names.get(ClassFileComposer.layer-1);
                if (curClass.isInterface()){
					renameSuperInterface(s);
                }
                else{
                        super_name = s;
                        renameSuper();
                }
        }
        else{
                super_name = cg.getSuperclassName();
        }

        //downwards propagate the undefined constructors to the current class
        Iterator it = downwards.iterator();
        while (it.hasNext()){
                Method m = (Method)it.next();
                Method theMethod = cg.containsMethod(m.getName(), m.getSignature());
                if (theMethod == null){
                        //System.out.println(m.getName());
                        //System.out.println(m.getSignature());

                        //downwards propagate
                        MethodGen newmg = new MethodGen(m, class_name, cp);
                        InstructionList il = new InstructionList();
                        Type[] t = newmg.getArgumentTypes();
                        InstructionFactory factory = new InstructionFactory(cg, cp);
                        int load_idx = 0;
                        il.append(factory.createLoad(new ObjectType(class_name),load_idx++));
                        for (int i=0; i<t.length; i++)
							il.append(factory.createLoad(t[i], load_idx++));
                        il.append(factory.createInvoke(super_name, "<init>", Type.VOID, t, Constants.INVOKESPECIAL));
                        il.append(InstructionConstants.RETURN);
                        newmg.setInstructionList(il);
                        /*reset the exception range
                        Attribute[] attributes = newmg.getMethod().getAttributes();
                        for(int i=0; i < attributes.length; i++) {
                                Attribute a = attributes[i];

                                if(a instanceof Code) {
                                        Code c = (Code)a;

                                        CodeException[] ces = c.getExceptionTable();
                                        if(ces != null)
                                                for(int j=0; j < ces.length; j++) {
                                                        CodeException ce = ces[j];
                                                        ce.setEndPC(il.getByteCode().length);
                                                }
                                }
                        }*/
                        newmg.stripAttributes(true);
                        cg.addMethod(newmg.getMethod());
                }
        }

        /** find out all the Utf8 in the constantpool to replace the $$ with $$LName
         */
        Constant[] cs = cp.getConstantPool().getConstantPool();
        for (int i=0; i<cs.length; i++){
                if (cs[i] instanceof ConstantUtf8){
                        ConstantUtf8 u8 = (ConstantUtf8)cs[i];
                        String str = u8.getBytes();
                        if (str.indexOf(cur_class_name+";")!=-1 || str.indexOf(cur_class_name+".")!=-1){
                                //System.out.println("The Utf8 is "+str);
                                int idx = str.indexOf(cur_class_name);
                                str = str.substring(0, idx)+class_name+str.substring(idx+cur_class_name.length());
                                u8.setBytes(str);
                                //System.out.println("After replace, the Utf8 is "+u8.getBytes());
                        }
                }
        }

   }

   public void visitMethod(Method method) {

        if (method.getName().equals("<init>")){

                MethodGen mg1= new MethodGen(method, cur_class_name, cur_cp);
                // if the constructor is new defined, then put it into the downwards table
                Method theMethod = containsMethod(method.getName(), method.getSignature());
                if (theMethod==null){

						InstructionList il = new InstructionList();
                        mg1.setInstructionList(il);

                        mg1.stripAttributes(true);
                        /* reset the exception range
                        Attribute[] attributes = mg1.getMethod().getAttributes();
                        for(int i=0; i < attributes.length; i++) {
                                Attribute a = attributes[i];

                                if(a instanceof Code) {
                                        Code c = (Code)a;

                                        //reset the exception range
                                        CodeException[] ces = c.getExceptionTable();
                                        if(ces != null){
                                                for(int j=0; j < ces.length; j++) {
                                        CodeException ce = ces[j];
                                        ce.setEndPC(il.getByteCode().length);
                                                }
                                        }
                                }
                        }*/

                        downwards.add(mg1.getMethod());
                        cur_cons.add(method.getName()+method.getSignature());
                }

                /** find out the super constructor call in the defined constructors.If the called
                 *  super constructor is not defined in its parent class, then add it into the upwards map.
                 */
                InstructionList il = mg1.getInstructionList();
                InstructionHandle[] ihs = il.getInstructionHandles();

                int special_i;
                for(special_i=1; special_i < ihs.length; special_i++) {
                                if( ihs[special_i].getInstruction() instanceof INVOKESPECIAL){
                                                break;
                                }
                }

                //get the name and signature of the special call
                if (special_i<ihs.length){
                        INVOKESPECIAL spec = (INVOKESPECIAL)ihs[special_i].getInstruction();
                        String name = spec.getClassName(cur_cp);
                        if (name.equals(super_name)){
                                String signature = spec.getSignature(cur_cp);
                                Type[] types = spec.getArgumentTypes(cur_cp);

                                //
                                theMethod=containsMethod("<init>", signature);
                                if (theMethod==null || (theMethod!=null && cur_cons.contains("<init>"+signature))){
                                        String str;
                                        Vector v;
                                        TypeSig ts = new TypeSig(types, signature);
                                        for (int i=0; i<ClassFileComposer.layer; i++){
                                                        str = (String)names.get(i);
                                                        if (ClassFileComposer.upwards.containsKey(str)){
                                                                        v=(Vector)ClassFileComposer.upwards.get(str);
                                                                        v.add(ts);
                                                        }
                                                        else{
                                                                        v = new Vector();
                                                                        v.add(cg);
                                                                        v.add(ts);
                                                                        ClassFileComposer.upwards.put(str, v);
                                                        }
                                        }
                                }
                        }
                }

        }


   }

   /**
   public void visitField(Field field) {
        //find out the fieldref in the constantpool
        String field_name = field.getName();
        String field_signature = field.getSignature();
        int field_idx = cp.lookupFieldref(cur_class_name, field_name, field_signature);
        //set the class index of fieldref to the new class
        if (field_idx!=-1){
                ConstantFieldref cfr = (ConstantFieldref)cp.getConstant(field_idx);
                //System.out.println(cg.getClassName());
                cfr.setClassIndex(cg.getClassNameIndex());
        }
   }*/

   public void visitInnerClass(InnerClass ic) {
        //System.out.println("Visiting the innerClass....");
        //rename the inner class.
        //1. rename it inside the current class
        Constant[] constants = cp.getConstantPool().getConstantPool();
        ConstantClass clazz = (ConstantClass)cp.getConstant(ic.getInnerClassIndex());
		ConstantUtf8  u8 = (ConstantUtf8)constants[clazz.getNameIndex()];
		String cur_inner_name = u8.getBytes();
		//System.out.println("old inner name: "+ cur_inner_name);

		String cn;
		if (class_name.indexOf("$")==-1)
			cn = class_name;
		else
			cn = class_name.substring(0,class_name.indexOf("$"));

		if (cur_inner_name.substring(0, cur_inner_name.indexOf("$")).equals(cn)){
        //if (ic.getOuterClassIndex()==0){
			//ConstantClass clazz = (ConstantClass)cp.getConstant(ic.getInnerClassIndex());
			//ConstantUtf8  u8 = (ConstantUtf8)constants[clazz.getNameIndex()];
			//String cur_inner_name = u8.getBytes();
			//System.out.println("old inner name: "+ cur_inner_name);
			String new_inner_name = class_name+cur_inner_name.substring(cur_inner_name.lastIndexOf("$"));
			u8.setBytes(new_inner_name);

			//replace it in all the strings in the constantpool
			/* it doesn't work because "Gui$$$10" will replaced by "Gui$$$1"
			int length=cp.getSize();
			for (int i=0; i<length; i++){
				if (constants[i] instanceof ConstantUtf8){
					u8 = (ConstantUtf8)constants[i];
					String str = u8.getBytes();
					str = str.replace(cur_inner_name,new_inner_name);
					u8.setBytes(str);
				}
			}*/
			int length=cp.getSize();
			for (int i=0; i<length; i++){
				if (constants[i] instanceof ConstantUtf8){
					u8 = (ConstantUtf8)constants[i];
					String str = u8.getBytes();
					if (str.equals("BaliParser$$")){
						u8.setBytes("BaliParser");
					}
					else if (str.equals("BaliParser$$$1")){
						u8.setBytes("BaliParser$1");
					}
					else if (str.indexOf(";")!=-1){
						StringTokenizer st = new StringTokenizer(str, ";");
						int num = st.countTokens();
						if (!str.endsWith(";"))
							num--;
						StringBuffer sb = new StringBuffer();
						String token;
						for (int j=0; j<num; j++){
							 token = st.nextToken();
							 int idx = token.indexOf("L");
							 if (idx==-1){
								 //no object type occured
								 sb.append(token).append(";");
							 }
							 else{
								 //get object name
								 //System.out.println(token);
								 String name = token.substring(idx+1);
								 if (name.equals(cur_inner_name)){
									 sb.append(token.substring(0,idx+1));
									 sb.append(new_inner_name).append(";");

								 }
								 else if (name.equals("BaliParser$$")){
									 sb.append(token.substring(0,idx+1));
									 sb.append("BaliParser").append(";");
								 }
								 else if (name.equals("BaliParser$$$1")){
									 sb.append(token.substring(0,idx+1));
									 sb.append("BaliParser$1").append(";");
								 }
								 else{
									 sb.append(token).append(";");
								 }
							 }
						 }
						 if (st.hasMoreTokens())
							sb.append(st.nextToken());

						u8.setBytes(sb.toString());
					}
				}
			}

			//OuterClass
			int outer_idx = ic.getOuterClassIndex();
			//System.out.println("new inner name: "+new_inner_name);
			//2. rename the inner class itself and put it into composed directory
			ClassFileComposer.renameInnerClass(cur_inner_name, new_inner_name,cur_class_name, class_name);
		} //may have but when the innter class if a member of this model
   }

  //some utility functions
  /** rename the given class
   */
  public void renameClass(){
        try {
                int class_name_idx = cg.getClassNameIndex();
                ConstantClass clazz = (ConstantClass)cp.getConstant(class_name_idx);
                Constant[] constants = cp.getConstantPool().getConstantPool();
                int name_idx =clazz.getNameIndex();
                ConstantUtf8  u8 = (ConstantUtf8)constants[name_idx];
                //sr.findConflicts(u8.getBytes());
                u8.setBytes(class_name);

        }catch (Exception e){
			System.out.println("in renameclass:");
                System.err.println(e);
        }
  }

  /**
  public void renameClass(){
        cg.setClassName(class_name);
  } */

  public void renameSuper(){
        try {
                int super_name_idx = cg.getSuperclassNameIndex();
                ConstantClass clazz = (ConstantClass)cp.getConstant(super_name_idx);
                Constant[] constants = cp.getConstantPool().getConstantPool();
                int name_idx =clazz.getNameIndex();
                ConstantUtf8  u8 = (ConstantUtf8)constants[name_idx];
                //sr.findConflicts(u8.getBytes());
                //set the entry to the new class name
                u8.setBytes(super_name);

        }catch (Exception e){
			System.out.println("in renameSuper:");
                System.err.println(e);
        }
  }

  public void renameSuperInterface(String supername){

	  super_name = cg.getSuperclassName();

	  int[] inters = cg.getInterfaces();
	  Constant[] constants = cp.getConstantPool().getConstantPool();
	  for (int i=0; i<inters.length; i++){
		  ConstantClass cc = (ConstantClass)cp.getConstant(inters[i]);
          ConstantUtf8  u8 = (ConstantUtf8)constants[cc.getNameIndex()];

		  if (u8.getBytes().indexOf("stub/")!=-1){

			  try{
				  //sr.findConflicts(u8.getBytes());
                  //set the entry to the new class name
			  	u8.setBytes(supername);
			  }catch (Exception e){
  			      System.out.println("in renameSuperInterface:");
                  System.err.println(e);
          	  }
			  break;
		 }
	  }

  }

  public Method containsMethod(String name, String signature) {
      Iterator it = downwards.iterator();
      while (it.hasNext()) {
         Method m = (Method)it.next();
         if (m.getName().equals(name) && m.getSignature().equals(signature))
	        return m;
      }

      return null;
  }

  public void dump(String path){
        String fileName;
        try {
                if (path==null)
                        fileName = class_name;
                else
                        fileName = path+File.separator+class_name;

                //sr.redirect(cp);
				//rename it with given package

				ClassFileComposer.renamePackage(cg);

				//save it
                JavaClass jc =cg.getJavaClass();

                jc.dump(fileName+".class");
                //ClassFileComposer.ctable.put(jc.getClassName(),fileName);
		} catch(java.io.IOException e) {
			System.out.println("in dump:");
            System.err.println(e);
        }
  }

}
