package ProgramCube.Validation.SafeComposition;

import guidsl.CubeXMLHandler;
import guidsl.ESList;
import guidsl.SATtest;
import guidsl.Tool;
import guidsl.cnfClause;
import guidsl.grammar;
import guidsl.order;
import guidsl.pattern;
import guidsl.production;
import guidsl.variable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;
import DesignByContract.*;
import ProgramCube.Validation.PLObjects.Coordinate;


/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 28, 2005
 *  Project: CubeServer
 *  ConstraintTester.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * Currently this only works with dimensional cube.
 * WHat about 0d cubes? Set a stringified model content instead of cubeElm
 * @author Sahil
 *
 */
public class ConstraintTester {

    //static Logger logger = Logger.getLogger(ConstraintTester.class);
    String modelString;
    HashMap duplicates = new HashMap();
    HashMap failureInfos = new HashMap(); //Maps constraint -> Failure info
    int numDuplicates =0;
    
    /**
     * Saves the model (Grammar + constraint) for future constraints validation. 
     * @param model String input representing the content of the model
     */
    public void setModelFromString(String model){
        GUARD.ensure(model!=null && model.trim().length()>0, "model cant be null");
        
        this.modelString = model;
    }
    
    public void setModelFromCubeElm(Element cubeElm){
        GUARD.ensure(cubeElm!=null, "Cube element cant be null");
        
        Element modelElm = cubeElm.getChild("model");
        this.modelString = CubeXMLHandler.generateModelFileContent(modelElm);
    }
    
    public boolean bothCanExistInProduct(Coordinate a, Coordinate b){
        
        String modelWithConstraints = modelString + "\n\n"+ a.toString()+" ;\n";
        modelWithConstraints += b.toString()+" ;\n";
        
		String filepath = writeTempModelFile(modelWithConstraints);
		
		//System.err.println(filepath);
		
		//------- reset GUIDSL --------
		production.resetModel();
		variable.Vtable= new HashMap();
		variable.vtsize=0;
		try { 
		    grammar.reset();
		} catch (Exception e){}
		cnfClause.clist=new ArrayList();
	     pattern.Ttable = new HashMap();
	     ESList.CTable = new ArrayList();
	    //-----------------------------
        
	     Tool tool=null ; 
		    SATtest t=null;
		    try {
		        tool = new Tool(filepath); 
		        t = new SATtest( filepath, true, true);
		    }
		    catch (Exception e){
		        System.out.println("Fiel: "+filepath);
		        System.exit(0);
		    }
		    
			
			StringBuffer outbuf = new StringBuffer();
			
			return tool.modelDebug(t, true, outbuf);
    }
    
    public boolean validateConstraint(String constraint, Vector coordinatesToSet, String constraintType, String implDetail, String layerDetail){
        GUARD.ensure(modelString!=null, "Model content must be set");
        GUARD.ensure(coordinatesToSet!=null, "coordinatesToSet must not be null");
        GUARD.ensure(constraint!=null && constraint.trim().length()>0, "constraint must not be null");
        
        if (duplicates.containsKey(constraint)){
            
            numDuplicates++;
            //Failed
            if (!((Boolean)duplicates.get(constraint)).booleanValue()){
	            if (!duplicates.containsKey(constraint)){
		            duplicates.put(constraint, new Boolean(false));
		            
		            String assignment = ((FailureInfo)failureInfos.get(constraint)).getAssignment();
		            GUARD.ensure(assignment!=null, "Couldnt find assignment of a duplicate");
		            
		            FailureInfo finfo = new FailureInfo(constraint, assignment,constraintType, implDetail, layerDetail);
		            failureInfos.put(constraint, finfo);
				}
				else {
				    FailureInfo finfo = (FailureInfo)failureInfos.get(constraint);
				    finfo.addFailure(constraint, constraintType, implDetail, layerDetail);
				}
            }
            
            return ((Boolean)duplicates.get(constraint)).booleanValue();
        }
        
        String modelWithConstraints = modelString + "\n\n"+ constraint+" ;\n";
		String filepath = writeTempModelFile(modelWithConstraints);
		
		//System.out.print("temp file= "+filepath+"\n");
		
		//------- reset GUIDSL --------
		production.resetModel();
		variable.Vtable= new HashMap();
		variable.vtsize=0;
		try { 
		    grammar.reset();
		} catch (Exception e){}
		cnfClause.clist=new ArrayList();
	     pattern.Ttable = new HashMap();
	     ESList.CTable = new ArrayList();
	    //-----------------------------
	   
	    Tool tool=null ; 
	    SATtest t=null;
	    try {
	        tool = new Tool(filepath); 
	        t = new SATtest( filepath, true, true);
	    }
	    catch (Exception e){
	        System.out.println("Fiel: "+filepath);
	        System.exit(0);
	    }
	    grammar.current.visit( new order() );
	    
	    //For each coordinate, need to individually set
	    //each ordinate
	    for (int i=0;i<coordinatesToSet.size();i++){
	        Coordinate point = (Coordinate)coordinatesToSet.get(i);
	        int dims = point.getNumDimensions();
	        
	        //System.out.print("Assignmetn Set: ");
	        for (int j=0;j<dims;j++){
	            //System.out.print(" "+point.get(j+1));
	            t.add(point.get(j+1));
	        }
	        //System.out.println("");
	    }
		
		
		StringBuffer outbuf = new StringBuffer();
		
		if ( tool.modelDebug(t, true, outbuf)){
			
			//System.out.println("***** FAILED *****" );
			//System.out.println("Found invalid assignment: "+outbuf);
			
			if (!duplicates.containsKey(constraint)){
	            duplicates.put(constraint, new Boolean(false));
	            
	            FailureInfo finfo = new FailureInfo(constraint, outbuf.toString(), constraintType, implDetail, layerDetail);
	            failureInfos.put(constraint, finfo);
			}
			else {
			    FailureInfo finfo = (FailureInfo)failureInfos.get(constraint);
			    finfo.addFailure(constraint, constraintType, implDetail, layerDetail);
			}
	        
			return false;

		}
		else{
		    
			//System.out.println("*** PASSED");
			//System.out.println("-----------------------");
		    
		    if (!duplicates.containsKey(constraint))
	            duplicates.put(constraint, new Boolean(true));
	    
		    return true;
		}
		
    }
    
    String writeTempModelFile(String modelWithConstraint){
        String filepath=null;
		try {
			File f = File.createTempFile("tempModel",".m");
			
			if(!f.canWrite())throw new IOException("cant write in file");
			
			FileWriter fw = new FileWriter(f);
			fw.write(modelWithConstraint);
			fw.close();
			filepath= f.getAbsolutePath();
		}
		catch(IOException ioe){
			//logger.debug("there was an IOE");
			System.err.println(ioe);
			System.exit(0);
		}
		
		return filepath;
    
    }
    
    public void reportFailures(StringBuffer output, boolean debug){
        Iterator finfoIter = failureInfos.values().iterator();
        
        while(finfoIter.hasNext()){
            FailureInfo finfo = (FailureInfo)finfoIter.next();
            
            output.append(finfo.toString(debug)+"\n");
            
        }
        
        
    }
    
    public int getNumDuplicates(){
        return numDuplicates;
    }
}
