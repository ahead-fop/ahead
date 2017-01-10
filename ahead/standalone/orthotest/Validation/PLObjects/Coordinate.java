package ProgramCube.Validation.PLObjects;
import DesignByContract.*;
/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 25, 2005
 *  Project: CubeServer
 *  Coordinate.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 *
 */
public class Coordinate {
    //int numDims = 0;
	String dimensions[];
	String dimensionNames[];
	boolean building=true;
	
	/**
	 * Assumes every dimension will be set. Call complete() to check
	 * @param dims
	 */
	public Coordinate(int dims){
	    dimensions = new String[dims];
	    dimensionNames = new String[dims];
	}
	
	/**
	 * Sets dimensional units
	 * @param i the ith dimension to set, starting from 1
	 * @param name the name of the dimension.
	 * @warning trims name 
	 */
	public void set(int i, String name){
	    GUARD.ensure(dimensions!=null, "Dimension array is null");
	    GUARD.ensure(building, "This coordinate has been built, now it is read only");
	    GUARD.ensure(dimensions.length>0, "Cant set dimension name in coordinate, no dimensions known!");
	    GUARD.ensure(i<=dimensions.length, "Can only set dimension less or eq to "+dimensions.length);
	    GUARD.ensure(i>0, "Can only set positive dimension");
	    GUARD.ensure(name.trim().length()>0, "Name must be a non empty string");
	            
	    dimensions[i-1]=name.trim();
	}
	
	/**
	 * Sets the name of the dimension axis
	 * @param i
	 * @param name
	 */
	public void setAxisName(int i, String name){
	    GUARD.ensure(dimensionNames!=null, "Dimension array is null");
	    GUARD.ensure(building, "This coordinate has been built, now it is read only");
	    GUARD.ensure(dimensionNames.length>0, "Cant set dimension name in coordinate, no dimensions known!");
	    GUARD.ensure(i<=dimensionNames.length, "Can only set dimension less or eq to "+dimensions.length);
	    GUARD.ensure(i>0, "Can only set positive dimension");
	    GUARD.ensure(name.trim().length()>0, "Name must be a non empty string");
	            
	    dimensionNames[i-1]=name.trim();
	}
	
	/**
	 * Finalizes a coordinate entry
	 *
	 */
	public void complete(){
	    GUARD.ensure(dimensions!=null, "Dimension array is null");
	    GUARD.ensure(dimensions.length>0, "Can't complete, No dimensions ");
	    for (int i=0;i<dimensions.length;i++){
	        GUARD.ensure(dimensions[i].length()>0, "Not all dims have been written to"); 
	    }
	    
	    building=false;
	}
	
	/**
	 * Returns name of the dimensional unit that this coordinate represents
	 * @param i the ith dimension to set, starting from 1
	 * @return name the name of the dimension.
	 */
	public String get(int dimIndex){
	    GUARD.ensure(dimensions!=null, "Dimension array is null");
	    GUARD.ensure(!building, "This coordinate has not yet completed building, rightnow it is write only");
	    GUARD.ensure(dimensions.length>0, "Cant get dimension name in coordinate, no dimensions known!");
	    GUARD.ensure(dimIndex<=dimensions.length, "Can only get dimension less or eq to "+dimensions.length);
	    GUARD.ensure(dimIndex>0, "Can only get positive dimension");
	    GUARD.ensure(dimensions[dimIndex-1].length()>0, "Name must be a non empty string");	    
	    
	    return dimensions[dimIndex-1];
	}
	
	public String getAxisName(int dimIndex){
	    GUARD.ensure(dimensionNames!=null, "Dimension array is null");
	    GUARD.ensure(!building, "This coordinate has not yet completed building, rightnow it is write only");
	    GUARD.ensure(dimensionNames.length>0, "Cant get dimension name in coordinate, no dimensions known!");
	    GUARD.ensure(dimIndex<=dimensionNames.length, "Can only get dimension less or eq to "+dimensions.length);
	    GUARD.ensure(dimIndex>0, "Can only get positive dimension");
	    GUARD.ensure(dimensionNames[dimIndex-1].length()>0, "Name must be a non empty string");	    
	    
	    return dimensionNames[dimIndex-1];
	}
	
	public int getNumDimensions(){
	    return dimensions.length;
	}
	
	/**
	 * @return coordinate formatted as conjunctoin of all dimensional units. Adds brackets ( ) but no space around them
	 *  
	 */
	public String toString(){
	
	    String temp = "(";
		for(int i=0;i<dimensions.length;i++){
			if (i!=0)
				temp+=" and ";
			temp+=dimensions[i];
		}
		temp+=")";
		
		return temp;
	}
}
