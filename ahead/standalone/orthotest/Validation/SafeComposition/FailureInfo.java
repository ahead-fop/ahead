package ProgramCube.Validation.SafeComposition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import DesignByContract.*;
/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 29, 2005
 *  Project: CubeServer
 *  FailureInfo.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * Tracks failures. Multiple failure reasons if there are duplicate constraints  
 * Formats the output into groups.
 * @author Sahil
 *
 */
public class FailureInfo {
    String constraint;
    String assignment;
    HashMap constTypeMap = new HashMap();
    
    FailureInfo(String constraint, String assignment, String constraintType, String implDetail, String layerDetail){
        this.constraint = constraint;
        this.assignment = assignment;
        add(constraintType, implDetail, layerDetail);
    }
    
    public void addFailure(String constraint, String constraintType, String implDetail, String layerDetail ){
        GUARD.ensure(constraint.equals(this.constraint), "Why is different constraint being added to the same FailureInfo Object??");
        add(constraintType, implDetail, layerDetail);
    }
    
    
    void add(String constraintType, String implDetail, String layerDetail){
        
        HashMap layerDetailMap;
        if (constTypeMap.containsKey(constraintType))
            layerDetailMap = (HashMap)constTypeMap.get(constraintType);
        else {
            layerDetailMap = new HashMap();
            constTypeMap.put(constraintType, layerDetailMap);
        }

        if (layerDetailMap.containsKey(layerDetail)){
            Vector implDetailVect = (Vector)layerDetailMap.get(layerDetail);
            implDetailVect.add(implDetail);
        }
        else{
            Vector implDetailVect = new Vector();
            implDetailVect.add(implDetail);
            layerDetailMap.put(layerDetail, implDetailVect);
        }
        
        
    }
    
    public String toString(){
        return toString(true);
    }
    public String toString(boolean debug){
        int num=0;
        
        StringBuffer out = new StringBuffer();
        out.append("----------| Failure Info |---------\n");
        
        if (debug){
            out.append("Constraint: "+ constraint + "\n");
        	out.append("Found Assignment: "+ assignment + "\n\n");
        }
        
        Iterator constTypesIter = constTypeMap.keySet().iterator();
        while(constTypesIter.hasNext()){
            String constType = (String)constTypesIter.next();
            out.append(""+ constType + "\n");
            //out.append("   Type: --: "+ constType + " :--\n");
            
            HashMap layerDetailMap = (HashMap)constTypeMap.get(constType);
            
            Iterator layerDetailIter = layerDetailMap.keySet().iterator();
            while(layerDetailIter.hasNext()){
                String layerDetail = (String)layerDetailIter.next();
                out.append("      "+ layerDetail + "\n");
                
                Vector implVector = (Vector)layerDetailMap.get(layerDetail);
                for (int i=0;i<implVector.size();i++){
                    out.append("         * "+ implVector.get(i) + "\n");
                    num++;
                }
            }
        }
        if (num>1)
            out.append("[Aggregated "+ num + " failures]\n");
        
        out.append("-----------------------------------\n");
        
        return out.toString();
    }
    
    public String getAssignment(){
        return assignment;
    }
    
}
