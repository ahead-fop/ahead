package ProgramCube.Validation;

import java.io.File;
import ClassReader.ClassInfo;
import ClassReader.ClassReader;
import DesignByContract.*;

import java.util.HashMap;
import java.util.HashSet;

/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 25, 2005
 *  Project: CubeServer
 *  OODesignInfo.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * This class represents the Object Oriented design information of a product line.
 * It is stateful and only one should be created per product line.
 * 
 * @author Sahil
 *
 */
public class OODesignInfo {
    /**  
     * Serves 2 purpose:
     *   1. Set of classes existing in this product line
     *   2. Tells whether it is an abstract class or interface or not.
     */
    HashMap PLClasses; 
    
    /** Forward indexe for climbing up and down the inheritance tree */
    HashMap inheritanceChildToParent;
    
    /** Forward indexe for climbing up and down the interface definition/implementation tree */
    HashMap interfaceChildToParents; //maps to HashSet of parents
    
    /** Backward indexe for climbing up and down the inheritance tree */
    HashMap inheritanceParentToChildren;  //computed on second pass. Maps to HashSet of children
    
    /** Backward indexe for climbing up and down the interface definition/implementation tree */
    HashMap interfaceParentToChildren;    //computed on second pass. Maps to HashSet of Children
    
    boolean hasExtracted=false;
    
    /** Constructor. Just initialize the hashmap data structures */
    public OODesignInfo(){
        PLClasses = new HashMap();
        inheritanceChildToParent 	= new HashMap();
        interfaceChildToParents 	= new HashMap();
        inheritanceParentToChildren = new HashMap();
        interfaceParentToChildren 	= new HashMap();
        
    }
    
    
    /**
     * Reads all <b>.class</b> files in the stub directory and extracts information
     * about OO design such as inheritance hierarchy, abtract classes, interfaces, etc.
     * This information can later by used by SafeComposition Validator to generate theorems.
     * <br><br>
     * <b>Appends '$$' after every class name if it does not end with $$. 
     * Also, 'stub.' part of the class name is removed.</b>
     * <br><br>
     * Classes outside the product line classes are pointed to by the data structures. 
     * E.g Foo extends java.lang.Object. It is the
     * responsibility of the user of this class to check (if necessary) that references to other classes are 
     * pointing to classes in the product line.
     * <br><br>
     * It assumes that stub directory contains all classes in the product line and that it correctly
     * represents the OO design of the product line.
     * <br><br>
     * Depends on functionality of the ClassReader. 
     * 
     * @param pathToStubDir readable path to the stub directory
     */
    public void extract(String pathToStubDir){
        File f = new File(pathToStubDir);
        
        /** Preconditions **/
        GUARD.ensure(f.exists(), "Stub directory does not exist");
        GUARD.ensure(f.canRead(), "Can't read stub directory");
        GUARD.ensure(f.isDirectory(), "Stub directory path is not directory");
        GUARD.ensure(!hasExtracted, "OODesignInfo can only extract once");
        /*****************/
                
        
        //START: Loop over each file in the stub directory
	    File[] files = f.listFiles();
	    for(int i=0; i< files.length; i++){
	        
	        //If a class file
	    	if (files[i].isFile() && files[i].getAbsolutePath().endsWith(".class")){
	    		
	    		ClassInfo classdata = ClassReader.eval(files[i].getAbsolutePath());
	    		
	    		if (classdata.getName().startsWith("AstTokenInterface")){
	    		    int stophere=1;
	    		}
	    		
	    		//Work out if this class is abstract or an interface
	    		//And ad the class to PLCLasses set. 
	    		String[] modifiers = classdata.getModifiers();
	    		
	    		/*Interfaces seem to be abstract also
	    		 * But we dont need to test their constraint, so interfaces are checked before
	    		 * abstract modifier
	    		 */
	    		if (ValidationUtils.ArrayContainsStringIgnoreCase(modifiers, "interface"))
	    		    PLClasses.put(formatName(classdata.getName()), "interface");
	    		else if (ValidationUtils.ArrayContainsStringIgnoreCase(modifiers, "abstract"))
	    		    PLClasses.put(formatName(classdata.getName()), "abstract");
	    		else
	    		    PLClasses.put(formatName(classdata.getName()), "neither");
	    		
	    		/******* START: Build both forward and back inheritance map *******/
	    		//In cases where stub dir is artificially populated, such as bali 
	    		//a mixin child inherits from its paretn with same name (after formatting)
	    		if (formatName(classdata.getName()).equals(formatName(classdata.getSuperClass())))
	    		        continue;
	    		
	    		//Add to forward inheritance map: child->parent
	    		inheritanceChildToParent.put(formatName(classdata.getName()), formatName(classdata.getSuperClass()));	    		
	    		
	    		//Add to backward inheritance map: child->parent(s)
	    		/*****************************************************/
	    		String childClassName = formatName(classdata.getName());
	    		String parentClassName = formatName(classdata.getSuperClass());
    			
	    		if (inheritanceParentToChildren.containsKey(parentClassName)){
    			    //Since this class is already in the map
    			    //Add my  to the set of children in the map
    			    HashSet setOfChildren = (HashSet)(inheritanceParentToChildren.get(parentClassName));
    			    setOfChildren.add(childClassName);

    			}
    			else {
    			    //Create new "Set of children", add my parent and 
    			    // add it to the interface map
    				HashSet setOfChildren = new HashSet();
    				setOfChildren.add(childClassName);
    				inheritanceParentToChildren.put(parentClassName, setOfChildren);
    			}
    			/******* END: Build both forward and back inheritance map *******/
	    		
    			/******* START: Build both forward and back interface map *******/    			
    			//Add to forward  interface map: child->parent(s)
	    		String[] interfaces = classdata.getSuperInterfaces();
	    		//Loop over each interface implemented by this class
	    		for (int j=0;j<interfaces.length;j++){
	    			String parentInterfName = formatName(interfaces[j]);
	    			String childImplementorName = formatName(classdata.getName());
	    			
	    			/*****************************************************/
	    			//Add to backward Interface Map: Parent -> Children
	    			if (interfaceParentToChildren.containsKey(parentInterfName)){
	    			    //Since this class is already in the map
	    			    //Add my  to the set of children in the map
	    			    HashSet setOfChildren = (HashSet)(interfaceParentToChildren.get(parentInterfName));
	    			    setOfChildren.add(childImplementorName);
	
	    			}
	    			else {
	    			    //Create new "Set of children", add my parent and 
	    			    // add it to the interface map
	    				HashSet setOfChildren = new HashSet();
	    				setOfChildren.add(childImplementorName);
	    				interfaceParentToChildren.put(parentInterfName, setOfChildren);
	    			}
	    			
	    			/*****************************************************/
	    			//Add to forward Interface Map: Children -> Parent
	    			if (interfaceChildToParents.containsKey(childImplementorName)){
	    			    //Since this class is already in the map
	    			    //Add my  to the set of parent in the map
	    			    HashSet setOfParent = (HashSet)(interfaceChildToParents.get(childImplementorName));
	    			    setOfParent.add(parentInterfName);
	
	    			}
	    			else {
	    			    //Create new "Set of children", add my parent and 
	    			    // add it to the interface map
	    				HashSet setOfParent = new HashSet();
	    				setOfParent.add(parentInterfName);
	    				interfaceChildToParents.put(childImplementorName, setOfParent);
	    			}
	    			
	    		}
	    		/******* END: Build both forward and back interface map *******/
	    		
	    		
	    	} //End: if a class file
	    	
	    } //END: Loop over each file in the stub directory

	
	    hasExtracted=true;
	    //Post conditions are too complex to ensure easily.
    } //end method
    
    public boolean getHasExtracted(){
        return hasExtracted;
    }
    
    public String[] getPLClasses(){
        Object[] objs = (Object[]) PLClasses.keySet().toArray();
        String[] strs = new String[objs.length];
        
        for (int i=0; i< objs.length;i++)
            strs[i]= (String)objs[i];
        
        return strs;
    }
    
    /**
     * Basically this formatting ensures class names are in a format jak compiler produces so
     * that it can be directly matched to classes retrieved form layers.
     * 
     * @param className the name of the class to be formatted
     * @return Name formatted according to the following convention:<br> 
     * 1) Must end with $$ because jak compilers adds $$ to the compiled classes present in the layers, and names need to be matched for processing<br>
     * 2) Class name should not start with 'stub.'
     *  
     */
    String formatName(String className){
        
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
        else {
            //could come from outside stub
            //GUARD.ensure(false, "When looking at stub no class file name should end with $$");
        }
            
        return className.replaceFirst("stub.", "");
    
    }

   

    /* Query methods */
    
    public boolean isProductLineClass(String name){
        GUARD.ensure(PLClasses!=null && PLClasses.size()>0, "PL classes structure hasnt been populated");
        
		return PLClasses.containsKey(formatName(name));
    }
    
    public void addProductLineClass(String name){
        GUARD.ensure(!isProductLineClass(name), "cant add a class that exists");
        
        PLClasses.put(formatName(formatName(name)), "neither");
        
    }
    public String getParentClass(String childname){
        childname=formatName(childname);
        GUARD.ensure(inheritanceChildToParent.containsKey(childname), "Cant get parent class because cant find child class: "+childname);
        
        return (String)inheritanceChildToParent.get(childname);
    }
    
    
    /**
     * Ensure that parent class is part of hte product line
     * @param childname
     * @return true if childname class has a parent class that is part of the product line classes
     */
    public boolean hasParentClass(String childname){
       if (inheritanceChildToParent.containsKey(childname))
           return isProductLineClass((String)inheritanceChildToParent.get(childname));
       else
           return false;
        
    }
    
    public boolean hasChildClass(String parentname){
        parentname=formatName(parentname);
        return inheritanceParentToChildren.containsKey(parentname);
    }
    
    public boolean hasIntfImplementors(String parentname){
        parentname=formatName(parentname);
        return interfaceParentToChildren.containsKey(parentname); 
    }
    
    public String[] getChildrenClass(String parentname){
        parentname=formatName(parentname);
        GUARD.ensure(inheritanceParentToChildren.containsKey(parentname), "Cant get child class because cant find parent class:"+parentname);
        
        HashSet setofChildren = (HashSet)inheritanceParentToChildren.get(parentname);
        
        Object[] objArr = setofChildren.toArray();
        
        String[] strArr = new String[objArr.length];
        for (int i=0;i<objArr.length;i++){
            strArr[i]= (String)objArr[i];
        }
        return strArr;
    }
    
    public String[] getIntfsImplementedByClass(String childname){
        childname=formatName(childname);
        GUARD.ensure(interfaceChildToParents.containsKey(childname), "Cant get interfaces implemented by this class because cant find the given class");
        
        HashSet setofParents = (HashSet)interfaceChildToParents.get(childname);
        
        return ((String[])setofParents.toArray());         
    }
    
    public String[] getClassesImplementingIntf(String parentname){
        parentname=formatName(parentname);
        GUARD.ensure(interfaceParentToChildren.containsKey(parentname), "Cant get interface implementor class because cant find interface class");
        
        HashSet setofChildren = (HashSet)interfaceParentToChildren.get(parentname);
        
        Object[] objArr = setofChildren.toArray();
        
        String[] strArr = new String[objArr.length];
        for (int i=0;i<objArr.length;i++){
            strArr[i]= (String)objArr[i];
        }
        return strArr;        
    }
    
    
    public boolean isAbstract(String name){
        GUARD.ensure(isProductLineClass(name), "Class not part of the product line");
        
        String modifier = (String)PLClasses.get(name);
        
        if (modifier!=null && modifier.equals("abstract"))
            return true;
        else 
            return false;
            
    }
    
    public boolean isInterface(String name){
        GUARD.ensure(isProductLineClass(name), "Class not part of the product line");
        
        String modifier = (String)PLClasses.get(name);
        
        if (modifier!=null && modifier.equals("interface"))
            return true;
        else 
            return false;
        
    }
}
