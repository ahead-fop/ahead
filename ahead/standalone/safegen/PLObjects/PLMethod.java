package ProgramCube.Validation.PLObjects;

import java.util.HashSet;
import java.util.Iterator;

import ClassReader.MethodInfo;
import DesignByContract.GUARD;

/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 26, 2005
 *  Project: CubeServer
 *  PLMethod.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 *
 */

public class PLMethod extends PLObject{

    String returnType;
    String methodname;
    String parameters;
    boolean isAbstract;
    HashSet fieldReferences;  // e.g. m() { int x = C.field; }
    HashSet methodReferences; // e.g. m() { C.method(); )
    HashSet classReferences;  //e.g when (local or global) field is defined, method parameters and return types
    
    public boolean isSelfFeatureReferenced = false;
    
    PLMethod(String returnType, String methodname, String parameters, boolean isAbstract){
        super();
        this.returnType = returnType;
        this.methodname = methodname;
        this.parameters = parameters;
        this.isAbstract = isAbstract;
        
        fieldReferences = new HashSet();
        methodReferences = new HashSet();
        classReferences = new HashSet();
                  
    }

    public int hashCode(){
        return toString().hashCode();
    }
    
    public String toString(){
        return returnType+" "+methodname+" ("+parameters+")";
    }
    
    public boolean getIsAbstract(){
        return isAbstract;
    }
    
    /**
     * Adds field reference from the current method's (layer specified by file) to another class's field.
     * It does not add PLField object but simply pushes in the string and eliminates duplicates.
     * This is because at the point where this method may be called we may not have processed all class files
     * and may lack PLField objs for some field members.
     * <br><br>
     * It is the responsibility of Reference constraint generataing method to extract PLField and the layer
     * where the field is introduced.
     * <br><br>
     * Difference between className, type and fieldName - <br>
     * C defines int field;<br>
     * A's m references C.field. Then className=C, type=int, fieldName=field
     * 
     * 
     * @param className
     * @param fieldName
     */
  
    public void addFieldReference(PLFile file, String className, String type, String fieldName){
        GUARD.ensure(className!=null && className.length()>0, "className parameter cant be empty");
        GUARD.ensure(fieldName!=null && fieldName.length()>0, "fieldName parameter cant be empty");
        
        String key = className+"::"+type+" "+fieldName;
        
        FileKeyPair pair = new FileKeyPair(file, key);
        
        if(!fieldReferences.contains(pair))
            fieldReferences.add(pair);
        
        GUARD.ensure(fieldReferences.contains(pair), "field reference not added!");
    }
    
    
    public void addMethodReference(PLFile file, String className, String returnType, String methodname, String parameters){
        GUARD.ensure(returnType!=null && returnType.length()>0, "returnType parameter cant be empty");
        GUARD.ensure(methodname!=null && methodname.length()>0, "methodname parameter cant be empty");
        GUARD.ensure(parameters!=null , "parameters parameter cant be empty");
        GUARD.ensure(file!=null , "PLFile cany be null");
        
        
        String key = className+"::"+returnType+" "+methodname+" ("+parameters+")";
        
        
        FileKeyPair pair = new FileKeyPair(file, key);
        
        if(!methodReferences.contains(pair))
            methodReferences.add(pair);
        
        GUARD.ensure(methodReferences.contains(pair), "field reference not added!");
    }
    
    public void addClassReference(PLFile file, String className){
        GUARD.ensure(className!=null && className.length()>0, "className parameter cant be empty");
        GUARD.ensure(file!=null , "PLFile cany be null");
        
        String key = className;
        
        
        FileKeyPair pair = new FileKeyPair(file, key);
        
        if(!classReferences.contains(pair))
            classReferences.add(pair);
        
        GUARD.ensure(classReferences.contains(pair), "classReferences reference not added!");
    }
    
    public Iterator fieldReferencesIterator(){
        return fieldReferences.iterator();
    }
    
    public Iterator methodReferencesIterator(){
        return methodReferences.iterator();
    }
    
    public Iterator classReferencesIterator(){
        return classReferences.iterator();
    }
    
    
    
    public class FileKeyPair{
        public PLFile file;
        public String key;
        
        
        FileKeyPair(PLFile file, String key){
            this.file = file;
            this.key = key;
        }
        
        
        public String toString(){
            return file.file +" "+file.layer+" "+file.position+" "+key; 
        }
        
    }
}

