package bcmixin;

import java.util.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import java.io.*;
/**
 * Read class file(s) and union all of its methods.
 *
 * @version $Id: ClassFileComposer.java,v 1.0 2002/12/08
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 */
public final class ClassFileComposer {
  final public static String COPYRIGHT = "(C) 2002 Yancong Zhou" ;
  final public static String NAME = "ClassFileComposer" ;
  final public static String VERSION = "v2002.12.12alpha" ;
  private static String class_name;
  //private static ClassGen        cg;
  private static HashSet       downwards;
  public static  HashMap       upwards;
  public static int layer=0;
  public static int innerIdx=1;
  public static String cur_layer_path;
  public static String cur_layer_name;
  public static String composedPath=null;
  public static boolean isLast = false;
  public static String pname;
  /** Keep a Map from signatures to definitions. Initially empty.
   */
  //private static HashMap exportsMap;
  public static void main(String[] argv) {
	  // Parse command-line arguments:
	 Vector operands = new Vector();	// The input class names.

	 List args = Arrays.asList (argv) ;
     for (Iterator p = args.iterator () ; p.hasNext () ; ) {
	    String arg = (String) p.next () ;
	    if (arg.equals ("-help"))
			 usage () ;
	    else if (arg.equals ("-output") && p.hasNext ())
			class_name = (String) p.next () ;
	    else if (arg.startsWith ("-"))
			usage ("invalid option " + arg) ;
		 else if (arg.endsWith(".class"))
			 operands.add(arg);
		 else
			usage ("invalid class name for operands " + arg) ;

	 }

	 //composing process
	 //cg = new ClassGen(class_name, "java.lang.Object", class_name+".java", 0, null);
     downwards = new HashSet();
	 ComposerVisitor visitor=new ComposerVisitor(downwards);
     composeProcess(operands,visitor,null);

  }

   public static void Main(File composedDir, String name, Vector operands) {
  	  layer =0;
      class_name = name;
  	  composedPath = composedDir.getAbsolutePath();
	  //composing process
	  //cg = new ClassGen(class_name, "java.lang.Object", class_name+".java", 0, null);
      downwards = new HashSet();
	  ComposerVisitor visitor=new ComposerVisitor(downwards);
	  composeProcess(operands,visitor,composedPath);
	  doUpwards(composedPath);
  }


  public static void composeProcess(Vector operands, ComposerVisitor visitor, String path){
     try {
		/** Union the given classes.
		 */
		JavaClass curClass;
		isLast = false;
		upwards = new HashMap();
		//System.out.println("the size: "+operands.size());
		for(int i=0; i < operands.size(); i++) {
			//creating the class object by giving class name
			curClass = new ClassParser (((File)operands.get(i)).getAbsolutePath()) . parse () ;
			cur_layer_path = ((File)operands.get(i)).getParent();
			cur_layer_name = ((File)operands.get(i)).getParentFile().getName();
			//System.out.println("the i: "+i);
			if (i==operands.size()-1){
					isLast = true;
					//System.out.println("it's last one!");
			}
			new TraversalVisitor(curClass, visitor, path).visit();
			layer++;
		}
    } catch(Exception e) {
		System.out.println("in composeProcess:");
      e.printStackTrace();
    }
  }

  public static void doUpwards(String path){
	Iterator it = upwards.keySet().iterator();
	while (it.hasNext()){
		String class_name = (String)it.next();
		Vector methods = (Vector)upwards.get(class_name);

	    ClassGen cgen = (ClassGen)methods.get(0);
	    ConstantPoolGen cp = cgen.getConstantPool();
        String supername = cgen.getSuperclassName();
	    //String classname = fileName.substring(fileName.lastIndexOf(File.separator)+1);
	    String newcname = cgen.getClassName();
        String fileName = path+File.separator+class_name;

		for (int i=1; i<methods.size(); i++){
			TypeSig ts = (TypeSig)methods.get(i);
			String sig = ts.getSig();
			Type[] t = ts.getTypes();

			Method theMethod = cgen.containsMethod("<init>", sig);
			if (theMethod==null){
				String[] argnames = new String[t.length];
				for (int j=0; j<argnames.length; j++){
					argnames[j] = "p"+j;
				}
				InstructionList il = new InstructionList();
				InstructionFactory factory = new InstructionFactory(cgen, cp);
				il.append(factory.createInvoke(supername, "<init>", Type.VOID, t, Constants.INVOKESPECIAL));
										il.append(InstructionConstants.RETURN);

				MethodGen mg= new MethodGen(Constants.ACC_PUBLIC, Type.VOID, t,argnames,
									"<init>", newcname, il, cp);

				cgen.addMethod(mg.getMethod());
			}
		}

		try{
			System.out.println(fileName);
			cgen.getJavaClass().dump(fileName+".class");
			//jc.dump(fileName+".class");
		}catch(java.io.IOException e) {
			System.out.println("in doupwards:");
			System.err.println(e);
		}

	}

  }

  public static void renamePackage(ClassGen cgen){
    StringRedirect sr;
	//a hashset to hold all the class names which are "class$***".
	HashSet hs = new HashSet();


	ConstantPoolGen cp = cgen.getConstantPool();
	int length = cp.getSize();
	Constant[] cs = cp.getConstantPool().getConstantPool();
	sr = new StringRedirect(cgen);

	//get all the types from the constant pool
	/**1. rename*/
	for (int i=0; i<length; i++){
		if (cs[i] instanceof ConstantUtf8){
			ConstantUtf8 u8 = (ConstantUtf8)cs[i];
			String str = u8.getBytes();

			//Analyze the str to get the types & rename it by generating a new string
			//case 1: exactly class name

			if (Main.ctable.contains(str)){
				if (str.equals("print")||str.equals("node")){
					if (cgen.getClassName().equals("Main$$dmain") || cgen.getClassName().equals("guid.Main$$dmain")){
					}
				}
				else{
					sr.findConflicts(str);
					u8.setBytes(pname+"/"+str);
				}
			}
			//case 2: start with class$
			else if (str.startsWith("class$") && str.length()>6){
				if (Main.ctable.contains(str.substring(6))){
					str="class$"+pname+"$"+str.substring(6);
					u8.setBytes(str);
					hs.add(str);
				}
			}
			//case 3: need to parse out the types in the string
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
						 //if (name.equals("AstTokenInterface$$kernel")){
							// System.out.println(str);
						 //}
						 if (Main.ctable.contains(name)){
							 sb.append(token.substring(0,idx+1));
							 sb.append(pname).append("/").append(name).append(";");

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


		//have to regenerate the cp since setBytes doesn't change the lookuptable
		//cp = cgen.getConstantPool(); //doesn't work
		sr.redirect(cp);
		Iterator it = hs.iterator();
		while (it.hasNext()){
				String cn = (String)it.next();
				int idx = cp.lookupUtf8(cn);
				if (idx!=-1){
						ConstantUtf8 u8 = (ConstantUtf8)cp.getConstant(idx);
						u8.setBytes(pname+"."+u8.getBytes());
				}
		}

  }

  public static void renameInnerClass(String old_name, String new_name, String curcname, String newcname){
        try{
                JavaClass jc = new ClassParser (cur_layer_path+File.separator+old_name+".class") . parse () ;
                ClassGen cgen = new ClassGen(jc);
                ConstantPoolGen cp = cgen.getConstantPool();
                int class_name_idx = cgen.getClassNameIndex();
                ConstantClass clazz = (ConstantClass)cp.getConstant(class_name_idx);
                Constant[] constants = cp.getConstantPool().getConstantPool();
                //StringRedirect sr = new StringRedirect(cgen);
                int name_idx = clazz.getNameIndex();
                ConstantUtf8  u8 = (ConstantUtf8)constants[name_idx];
                //sr.findConflicts(u8.getBytes());
                u8.setBytes(new_name);
                /** find out all the Utf8 in the constantpool to replace the $$ with $$LName
                 */
                Constant[] cs = cp.getConstantPool().getConstantPool();
                for (int i=0; i<cp.getSize(); i++){
                        if (cs[i] instanceof ConstantUtf8){
                                u8 = (ConstantUtf8)cs[i];
                                String str = u8.getBytes();
                                if (str.equals("BaliParser$$")){
									u8.setBytes("BaliParser");
								}
								else if (str.equals("BaliParser$$$1")){
									u8.setBytes("BaliParser$1");
								}
								else if (str.equals(curcname)){
									u8.setBytes(newcname);
								}
								else if (str.equals(old_name)){
									u8.setBytes(new_name);
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
											 if (name.equals(curcname)){
												 sb.append(token.substring(0,idx+1));
												 sb.append(newcname).append(";");

											 }
											 if (name.equals(old_name)){
												 sb.append(token.substring(0,idx+1));
												 sb.append(new_name).append(";");

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
                String fileName = composedPath+File.separator+new_name;

                //sr.redirect(cp);

                //rename it with given package before saving it
                renamePackage(cgen);
                jc = cgen.getJavaClass();
                jc.dump(fileName+".class");
                //System.out.println("inner class: "+fileName);
                //ctable.put(jc.getClassName(), fileName);
        }catch(java.io.IOException e) {
			System.out.println("in renameInnerclass:");
                System.err.println(e);
        }
  }

  /**
  public static void copyInnerClass(String name){
        try{
                JavaClass jc = new ClassParser(cur_layer_path+File.separator+name+".class").parse();
                jc.dump(composedPath+File.separator+name+".class");
        }catch (java.io.IOException e){
                System.err.println(e);
        }
  }*/

   /**.
     * Print a usage message in response to a <code>-help</code> command-line
     * option, then exit successfully.
     **/
    final private static void usage () {
		 usage_print () ;
		 System.exit (0) ;
    }

    /**
     * Print a usage message in response to an argument-parsing error,
     * then abort with a message about the argument error.
     *
     * @param message a {@link String} describing the argument-parsing error.
     **/
    final private static void usage (String message) {
		 usage_print () ;
		 System.err.println () ;
		 abort (message) ;
    }

    /**
     * Utility method to print a usage message onto standard output.
     **/
   final private static void usage_print () {
		System.out.println (NAME + ' ' + VERSION + ' ' + COPYRIGHT) ;
		System.out.println () ;

		System.out.println (
		    "Usage: java ClassFileComposer [<options>] <class+>"
		) ;

		System.out.println () ;
		System.out.println ("where <options> include any of the following:") ;
		System.out.println () ;
		System.out.println (" -help") ;
		System.out.println ("  Prints this helpful message, then exits.") ;
		System.out.println () ;
		System.out.println (" -output <file> (default file name is \"result\")") ;
		System.out.println ("  Specifies a class file name to store the result.") ;
		System.out.println ("  Don't need file extension \".class\". It will be added automatically") ;
		System.out.println () ;
		System.out.println ("where <class+> means multiple class can be specified.") ;
		System.out.println ("The class name must end with \".class\"" ) ;
		System.out.println () ;
   }

	//When things go really wrong, this method is invoked.
	final private static void abort (final String message) {
		System.out.println(message) ;
		System.exit (1) ;
   }
}
