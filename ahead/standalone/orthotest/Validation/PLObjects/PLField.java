package ProgramCube.Validation.PLObjects;


/* 
 * ***********************************
 *   Author: Sahil
 *  Created: Dec 26, 2005
 *  Project: CubeServer
 *  PLField.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 *
 */

public class PLField extends PLObject{
    String fieldname;
    String type;
    
    PLField(String type, String name){
        super();
        
        this.type = type;
        this.fieldname = name;
                   
    }
    
    public int hashCode(){
        return toString().hashCode();
    }
    
    public String toString(){
        return type+" "+fieldname;
    }
    
    public String getType(){
        return type;
    }
    
    public String getFieldName(){
        return fieldname;
    }
}
