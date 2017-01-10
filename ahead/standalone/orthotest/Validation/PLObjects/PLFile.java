package ProgramCube.Validation.PLObjects;



/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 26, 2005
 *  Project: CubeServer
 *  PLFile.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 *
 */
public class PLFile {   
    
    public String file;
    public String layer;
    public Coordinate position;

    public int hashCode(){
        return (file+":"+layer+":"+position).hashCode();
    }
    

}
