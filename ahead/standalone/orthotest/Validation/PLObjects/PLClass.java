package ProgramCube.Validation.PLObjects;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import DesignByContract.*;
import ProgramCube.Validation.PLObjects.PLMethod.FileKeyPair;
/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 26, 2005
 *  Project: CubeServer
 *  PLClass.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 *
 */
public class PLClass extends PLObject{
    String classname;
    
    Hashtable methods;
    Hashtable fields;
    HashSet classReferences;  //e.g when (local or global) field is defined
    
    public PLClass(String name){
        super();
        this.classname = name;
        
        methods=new Hashtable();
        fields=new Hashtable();
        classReferences = new HashSet();
    }
    
    /**
     * Identify each file by its name
     */
    public int hashCode(){
        return classname.hashCode();
    }
    
    /**
     * Returns PLMethod object from the class. If it doesnt exsit it is created.
     * @return
     */
    public PLMethod addAndGetMethod(String returnType, String methodname, String parameters, boolean isAbstract){
        
        //Class name can be ignored because we know which class this method is in
        String methodID = returnType+" "+methodname+" ("+parameters+")";
        
        if (methods.containsKey(methodID)){
            return (PLMethod)methods.get(methodID);
        }
        else {
            PLMethod method = new PLMethod(returnType, methodname, parameters, isAbstract);
            
            methods.put(methodID, method);
            return method;
        }

    }
    
    public PLMethod getMethod(String methodID){
        GUARD.ensure(methods.containsKey(methodID), "cant find method");
        return (PLMethod)methods.get(methodID);
    }
    public boolean getHasMethod(String methodID){
        return methods.containsKey(methodID);
        
    }
    
    public boolean getHasField(String fieldID){
        return fields.containsKey(fieldID);
        
    }
    
    public PLField addAndGetField(String type, String fieldname){
        
        //Class name can be ignored because we know which class this method is in
        String fieldID = type+" "+fieldname;
        
        if (fields.containsKey(fieldID)){
            return (PLField)fields.get(fieldID);
        }
        else {
            PLField field = new PLField(type, fieldname);
            
            fields.put(fieldID, field);
            return field;
        }

    }

    public PLField getField(String fieldID){
        GUARD.ensure(fields.containsKey(fieldID), "Cant find field: "+fieldID);
        
        return (PLField)fields.get(fieldID);
    }
    
    public Set getListOfMethods(){
        return methods.keySet();
    }
    
    public Set getListOfFields(){
        return fields.keySet();
    }
    
    public String toString(){
        return classname;
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

