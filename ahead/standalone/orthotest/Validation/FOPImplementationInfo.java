package ProgramCube.Validation;

import java.io.File;
import java.util.Vector;
import ClassReader.ClassInfo;
import ClassReader.ClassReader;
import DesignByContract.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

import ProgramCube.Validation.PLObjects.*;

import java.util.Set;
/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 26, 2005
 *  Project: CubeServer
 *  FOPImplementationInfo.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * Responsibility:
 * 
 * @author Sahil
 *
 */
public class FOPImplementationInfo {


    
    /**
     * Maps String class names to PLClass object
     */
    HashMap PLClasses;
    
    /**
     * Ref to oodesign info object. Used for extracting class information
     */
    OODesignInfo oodesigninfo;
    
    /**
     * Constructor. Also generates all product line classes objects based on whats in OODesignInfo.
     * So there is strong dependeny between classes that get processed and whtas in the stub directyory
     * 
     * @param obj the OODesignInfo object that must have been populated prior to constructing FOPImplementationInfo
     */
    public FOPImplementationInfo(OODesignInfo obj){
        GUARD.ensure(obj!=null, "OODesignInfo must not be null");
        GUARD.ensure(obj.getHasExtracted(), "OODesignInfo must have been processed");
        
        PLClasses=new HashMap();
        String[] classes = obj.getPLClasses();
     
        //Loop over classes and Populate this object's PLClasses with PLClass Object
        for (int i=0;i<classes.length;i++){
            GUARD.ensure(!PLClasses.containsKey(classes[i]), "Duplicate class name encountered. Assuming be a unique key (maybe problem with same class name in different packge. I'm not handling that case.)");
             
            PLClass plclass = new PLClass(classes[i]); 
            PLClasses.put(classes[i],plclass);
            
        }
        
        oodesigninfo = obj;
    }
    
    public PLClass getClass(String className){
        if (!className.contains("$$$1$$"))
            GUARD.ensure(PLClasses.containsKey(className), "Cant find class:"+className);
        
        return (PLClass)PLClasses.get(className);
    }

    public Set getListOfClasses(){
        return PLClasses.keySet();
    }
    
    public OODesignInfo getOODesigninfo(){
        return oodesigninfo;
    }

    public PLClass addClass(String name){
        //oodesigninfo.isProductLineClass(name) this will fail in addPLClass
        
        oodesigninfo.addProductLineClass(name);
        PLClass plclass = new PLClass(ValidationUtils.formatName(name)); 
        PLClasses.put(ValidationUtils.formatName(name),plclass);
        return plclass;
    }
}
