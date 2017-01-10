package ClassReader;

import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import java.util.*;
import java.io.*;
/**
 * Extract informations from a class file.
 *
 * @version $Id: Main.java,v 1.0 2005/01/12
 * @author  <A HREF="http://www.cs.utexas.edu/users/yczhou">Y. Zhou</A>
 */
public final class Main {
  final public static String COPYRIGHT = "(C) 2005 Yancong Zhou" ;
  final public static String NAME = "ClassReader.Main" ;
  final public static String VERSION = "v2005.01.12alpha" ;
  public static boolean refineinfo;

  public static void main(String[] argv) {
	  if (argv.length == 0){
		  usage();
	  }
	  else{
		  if (argv[0].equals("-help")){
		  	usage();
		  }
		  else{
			  ClassInfo ci;
			  if (argv[0].equals("-r")){
				  if (argv.length<2)
					  usage();
				  refineinfo = true;
				  ci = Main.eval(argv[1]);
			  }
			  else{
				  refineinfo = false;
				  ci = Main.eval(argv[0]);
			  }
			  ci.print();
		  }
	  }
  }

  /** This method can be invoked by Main.eval(filename). it will return an ClassInfo object to you.
      The ClassInfo object include all the atomic information that you need to extract from a class file.
    */
  public static ClassInfo eval (String filename){
	  JavaClass jc = null;
	  try{
		  jc = new ClassParser (filename).parse();

		  String class_package;
		  String class_name;
		  String[] modifiers;
		  String super_class;
		  String[] super_interfaces;
		  MethodInfo[] methods;
		  FieldInfo[] fields;

		  class_package = jc.getPackageName();
		  class_name = jc.getClassName();
		  modifiers = getModifiers(jc);
		  super_class = jc.getSuperclassName();
		  super_interfaces = jc.getInterfaceNames();
		  methods = getMethods(jc);
		  fields = getFields(jc);

		  return new ClassInfo(class_package, class_name, modifiers, super_class, super_interfaces, methods, fields);

	  }catch(Exception e){
	  			e.printStackTrace();
	  			return null;
	  }

  }

  public static MethodInfo[] getMethods(JavaClass jc){
	  Method[] ms = jc.getMethods();
	  MethodInfo[] methods = new MethodInfo[ms.length];
	  for (int i=0; i<ms.length; i++){
		  //if ((!(ms[i].getName().equals("<init>"))) && (!(ms[i].getName().equals("<clinit>"))))
		  methods[i] = new MethodInfo(new ConstantPoolGen(jc.getConstantPool()), ms[i]);
	  }
	  return methods;
  }

  public static FieldInfo[] getFields(JavaClass jc){
	  Field[] fs = jc.getFields();
	  FieldInfo[] fields = new FieldInfo[fs.length];
	  for (int i=0; i<fs.length; i++){
		  fields[i] = new FieldInfo(fs[i]);
	  }
	  return fields;
  }

  public static String[] getModifiers(JavaClass jc){
	   //get the modifiers
	  ArrayList ll = new ArrayList();
	  if (jc.isAbstract()){
		  ll.add ("Abstract");
	  }
	   if (jc.isPrivate()){
	  		  ll.add("Private");
	  	  }
	  	  if (jc.isProtected()){
	  		  ll.add("Protected");
	  	  }
	  	  if (jc.isPublic()){
	  		  ll.add("Public");
	  }
	  if (jc.isFinal()){
		  ll.add("Final");
	  }

	  if (jc.isStatic()){
	  	  ll.add("Static");
	  }

	  if (jc.isNative()){
		  ll.add("Native");
	  }

	  if (jc.isStrictfp()){
		  ll.add("Strictfp");
	  }
	  //if (jc.isSynchronized()){
	  //	  ll.add("Synchronized");
	  //}
	  if (jc.isTransient()){
		  ll.add("Transient");
	  }
	  if (jc.isVolatile()){
		  ll.add("Volatile");
	  }
	   if (jc.isInterface()){
	  		  ll.add("Interface");
	  	  }
	  	  else{
	  		  ll.add("class");
	  }

	  String[] flags = new String[ll.size()];
	  for (int i=0; i<ll.size(); i++){
		  flags[i]=(String)ll.get(i);
	  }
	  return flags;

  }


   /**
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
		    "Usage: java ClassReader.Main [<options>] <classname>"
		) ;

		System.out.println () ;
		System.out.println ("where <options> include any of the following:") ;
		System.out.println () ;
		System.out.println (" -help") ;
		System.out.println ("  Prints this helpful message, then exits.") ;
		System.out.println () ;
		System.out.println (" -r ");
		System.out.println("   will extract the refinement information from the given class.");
		System.out.println () ;
		System.out.println ("where <classname> means the class can be specified.") ;
		System.out.println ("The class name must end with \".class\"" ) ;
		System.out.println () ;
   }

	//When things go really wrong, this method is invoked.
	final private static void abort (final String message) {
		System.out.println(message) ;
		System.exit (1) ;
   }
}
