package ProgramCube.Validation;

import ClassReader.ClassInfo;
import ClassReader.ClassReader;
import ClassReader.FieldInfo;
import ClassReader.MethodInfo;
import DesignByContract.GUARD;
import ProgramCube.Validation.PLObjects.PLClass;
import ProgramCube.Validation.PLObjects.PLField;
import ProgramCube.Validation.PLObjects.PLFile;
import ProgramCube.Validation.PLObjects.PLMethod;

import java.io.*;
import java.util.Stack;
/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 29, 2005
 *  Project: CubeServer
 *  ValidationUtils.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 *
 */
public class ValidationUtils {

    /**
     * Searches the array of string for a string being seeked. Matches whole string and ignores cases.
     * @param arr Array of string to be searched
     * @param value Key being searched
     * @return Tells whether the key is found or not
     */
    public static boolean  ArrayContainsStringIgnoreCase(String[] arr, String value){
		if (arr==null)return false;
		
		for (int i=0;i<arr.length;i++)
			if (arr[i].equalsIgnoreCase(value))
				return true;
		
		return false;
	}
    
    
    public static int getIndexOfStringInArray(String[] arr, String value){
		GUARD.ensure(arr!=null, "Array cant be null");
		
		for (int i=0;i<arr.length;i++)
			if (arr[i].equals(value))
				return i;
		
		GUARD.ensure(false, "Array does not contain input sting:"+value);
		return -1;
	}
    
    
    /**
     * Gets params that form part of the signature, exclusing brackets ( )
     * @return
     */
    public static String getParametersOfMethod(MethodInfo m){

		String params = ""; 
	    String[] args = m.getArgTypes();
	    for (int k = 0; k < args.length; k++) {
	        if (k == args.length - 1) params += args[k];
	        else params += args[k] + ", ";
	    }
	    
		return params;
    }
    
    public static String readFile(String filePath){
        
        try 
        {
	        BufferedReader br = new BufferedReader(new FileReader(filePath)); 
	        String text = null; 
	        StringBuffer allText = new StringBuffer(); 
	       
	        while((text = br.readLine()) != null) { 
		        allText.append(text);
		        allText.append("\n"); 
	        }
	        
	        br.close(); 
	        return allText.toString();
	     } 
	     catch (IOException e) {
	            e.printStackTrace();
	            System.exit(0);
	     }
	        	      
    
	     return "";
    }
    
    public static FOPImplementationInfo extractPLClassInfo(OODesignInfo oodesignInfo, Stack fileStack, String pathToStubDirectory){
        oodesignInfo.extract(pathToStubDirectory);
        FOPImplementationInfo fopimpl = new FOPImplementationInfo(oodesignInfo);
        
        //For each file in all Layers in the PL
        while(!fileStack.empty()){
        	
			PLFile file = (PLFile)fileStack.pop();
	
			
			ClassInfo classdata = ClassReader.eval(file.file);
			FieldInfo[] fields = classdata.getFields();
			MethodInfo[] methods = classdata.getMethods();
    		
			
			PLClass plclass=null;
			if (oodesignInfo.isProductLineClass(formatName(classdata.getName()))){
			    plclass = fopimpl.getClass(formatName(classdata.getName()));
			}
			else { //add
			    plclass = fopimpl.addClass(classdata.getName());
			    
			}
		
            
			if(plclass==null){
			    GUARD.ensure(false, "now this shouldnt occur");
			    continue;
			}
			
			//Add class definitions or refinements
			if (classdata.getSuperClass()!=null && classdata.getSuperClass().startsWith("stub.")){
			    plclass.addRefiningLayer(file);
			}
			else {
			    plclass.addDefiningLayer(file);
			}
			
			
			// Iterate through fields
			// Only definitions (and references), no refinements
			for (int i=0;i<fields.length;i++){
				FieldInfo finfo = fields[i];
				String fieldName = finfo.getName();
				String fieldType = finfo.getType();
				
				PLField field = plclass.addAndGetField(fieldType, fieldName);
				field.addDefiningLayer(file);
				
				//Add PL references 
			}
			
			//Iterate through methods
			for (int i=0;i<methods.length;i++){
				MethodInfo minfo = methods[i];
				
				//Ignore <clinit> and main methods
				if ( minfo.getName().equals("<clinit>") || minfo.getName().equals("main")){
			    	continue;
			    }
				
				String methodName = minfo.getName();
				String returnType = minfo.getReturnType();
				
				
				//---------work out signature ------------------
	    		String sign = ""; 
			    String[] args = minfo.getArgTypes();
			    for (int k = 0; k < args.length; k++) {
			        if (k == args.length - 1) sign += args[k];
			        else sign += args[k] + ", ";
			    }
		    	//-----------------------------------------------
			    
			    //TODO: remove this after verifying that it works
			    GUARD.ensure(ValidationUtils.getParametersOfMethod(minfo).equals(sign), "Hmm why is this not the same");
			    
			    boolean isAbstract=false;
			    //See if the method is abstract
			    if (ValidationUtils.ArrayContainsStringIgnoreCase(minfo.getModifiers(), "abstract"))
			        isAbstract=true;
			    
			    
				PLMethod method = plclass.addAndGetMethod(returnType, methodName, sign, isAbstract);
				
				if (isMethodInfoRefined(minfo)){
  				    GUARD.ensure(!isAbstract, "A refinement method cant be abstract");
				    //System.err.println(file.layer + " refined method: "+methodName);
				    method.addRefiningLayer(file);
				}
				else {
				    //System.err.println(file.layer + " defined method: "+methodName);
				    
				    method.addDefiningLayer(file);
				}
				
				//Add PL references
				Object[] obj = minfo.getReferenceTable();
			    for(int p=0;p<obj.length;p++){
			    	
			    	if (obj[p] instanceof FieldInfo){
			    		FieldInfo finfo = (FieldInfo)obj[p];
			    		
			    		String className = formatName(finfo.getClassName());
			    		String type = finfo.getType();
			    		
			    		if (!oodesignInfo.isProductLineClass(className))
			    		    continue;
			    		
			    		//String key = type+" "+finfo.getName();
			    		method.addFieldReference(file, className, type, finfo.getName());
			    		
			    	}
			    	
			    	else if (obj[p] instanceof MethodInfo){
			    		
			    	    MethodInfo refed_minfo = (MethodInfo)obj[p];

			    		String className = formatName(refed_minfo.getClassName());
			    		
			    		
			    		if (!oodesignInfo.isProductLineClass(className))
			    		    continue;
			    		
			    		method.addMethodReference(file, className, refed_minfo.getReturnType(), refed_minfo.getName(), ValidationUtils.getParametersOfMethod(refed_minfo));
			    		
			    	}
                    else if (obj[p] instanceof ClassInfo){
                        ClassInfo refed_cinfo = (ClassInfo)obj[p];
                        System.out.println("Ref to class"+ refed_cinfo.getName() );
                    }
			    } //for each item in the reference table
			    
			    //Now add references to classes
			    String[] paramTypes = minfo.getArgTypes();
			    for (int p=0;p<paramTypes.length;p++)
			        method.addClassReference(file, formatName(paramTypes[p]));
			    
			    //Also add return type of the method as a reference to Class
			    if (!minfo.getReturnType().equalsIgnoreCase("void"))
			        method.addClassReference(file, formatName(minfo.getReturnType()));
			    
			} //foreach method

    	}
    
        return fopimpl;
    }
    
    /**
     * Basically this formatting ensures class names are in a format
     * that can be directly matched to classes retrieved form layers.
     * 
     * @param className the name of the class to be formatted
     * @return Name formatted according to the following convention:<br> 
     * 1) Must end with $$ because jak compilers adds $$ to the compiled classes present in the layers, and names need to be matched for processing<br>
     * 2) Class name should not start with 'stub.'
     *  
     */
    public static String formatName(String className){
        if (!className.endsWith("$$")){
            
            /*
             * Some classess have inner classes. Java compiler names them as outerclass$innerclass.class
             * Jak compiler adds $$ after each class name in the layer. So it turns into outerclass$$$innerclass$$.class
             * 
             * If class name does not end with $$ it is "pure" (not modified by jak compiler) and any $s present represent 
             * separator of inner class and they should be converted to the format that jak compiler will produce.
             */
            
            //in bali there is no Stub dir so I had to place all classes in the layers into stub dir
            //there is a file Variables$$$Typedata.class and we dont want to replace $$$.
            if(!className.contains("$$$"))
                className = className.replaceAll("\\$", "\\$\\$\\$");
            
            className+="$$";
        }
            
        return className.replaceFirst("stub.", "");
    
    }
    
    
    /**
     * Tells whether a given method is being refined or introduced. 
     * 
     * @param m
     * @return
     */
    public static boolean isMethodInfoRefined(MethodInfo m){
        boolean isRefined = false;
    	Object[] reference_table = m.getReferenceTable();
        for(int i = 0; i < reference_table.length; i++)
        {
            if(!(reference_table[i] instanceof MethodInfo))
                continue;
            MethodInfo methodinfo = (MethodInfo)reference_table[i];
            if(!methodinfo.getClassName().startsWith("stub") || !methodinfo.getName().equals(m.getName()))
                continue;
            isRefined = true;
            break;
        }
        
        return isRefined;
    
    }
    
}
