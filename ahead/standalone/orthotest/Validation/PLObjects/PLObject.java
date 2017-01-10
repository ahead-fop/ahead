package ProgramCube.Validation.PLObjects;

import java.util.HashSet;

import DesignByContract.GUARD;
import java.util.Iterator;
/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 26, 2005
 *  Project: CubeServer
 *  PLObject.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 *
 */
public abstract class PLObject{
    
    HashSet definitions;
    HashSet refinements;
    
    PLObject(){
        definitions = new HashSet();
    	refinements = new HashSet();
    }
    
    /**
     * In reality adds a PLFIle object, which contains Layer, Filepath and coordinate information
     * @param file the PLFile object where this class is being defined
     */
    public void addDefiningLayer(PLFile file){
        GUARD.ensure(!definitions.contains(file), "Cant add, PLFile already exists");
        definitions.add(file);
        GUARD.ensure(definitions.contains(file), "PLFile not added!");
    }
    
    /**
     * In reality adds a PLFIle object, which contains Layer, Filepath and coordinate information
     * @param file the PLFile object where this class is being refined
     */       
    public void addRefiningLayer(PLFile file){
        GUARD.ensure(!refinements.contains(file), "Cant add, PLFile already exists");
        refinements.add(file);
        GUARD.ensure(refinements.contains(file), "PLFile not added!");            
    }
    
    public Iterator refinementsIterator(){
        return refinements.iterator();
    }
    
    public PLFile[] getRefinementsAsArray(){
        
         PLFile arr[] =new PLFile[refinements.size()];
         
         Iterator refinesIter = refinementsIterator();
         int i=0;
         while(refinesIter.hasNext()){
             PLFile f = (PLFile)refinesIter.next();
             arr[i]=f;
             i++;
         }
         
         return arr;
     }
    
    public Iterator definitionsIterator(){
        return definitions.iterator();
    }
    
    public PLFile[] getDefinitionsAsArray(){
       
        PLFile arr[] =new PLFile[definitions.size()];
        
        Iterator definesIter = definitionsIterator();
        int i=0;
        while(definesIter.hasNext()){
            PLFile f = (PLFile)definesIter.next();
            arr[i]=f;
            i++;
        }
        
        return arr;
    }
    
}

