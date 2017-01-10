package ProgramCube.Validation.Orthogonality;

import guidsl.ESList;
import guidsl.PrintXML;
import guidsl.SATtest;
import guidsl.Tool;
import guidsl.grammar;
import guidsl.production;
import guidsl.variable;

import java.util.Iterator;
import java.util.Vector;

import ClassReader.*;
import DesignByContract.*;
import ProgramCube.Feature;
import ProgramCube.PLMReader;
import ProgramCube.DataStore.XMLUtils;
import ProgramCube.Exceptions.CubeEncodingException;
import ProgramCube.Validation.PLObjects.*;

import org.jdom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import ProgramCube.Validation.OODesignInfo;
import ProgramCube.Validation.FOPImplementationInfo;
import ProgramCube.Validation.PLObjects.*;
import ProgramCube.Validation.SafeComposition.ConstraintTester;
import ProgramCube.Validation.ValidationUtils;

/* 
 * ***********************************
 *   Author: Sahil
 *  Created:  2005
 *  Project: CubeServer
 *  SafeCompositionValidator.java
 * 
 *  Copyright 2005+ Sahil
 * ***********************************
 */

/**
 * @author Sahil
 * testing argument  "java ProgramCube.Validation.Orthogonality.OrthogonalityValidator -md -s "C:\cygwin\home\Sahil\projects\orthogonalityTestCase$$\testcase.plm"
 */

public class OrthogonalityValidator {
    static boolean REPORT_STATS = true;
    static boolean OUTPUT_FAILURES = false;
    
    //Define flags. Random nums so wont match another int by mistake.
    static final int SAFECOMP_ERROR 	= 2341; 
    static final int SAFECOMP_WARNING 	= 9892;
    static final int SAFECOMP_IGNORE 	= 100295;
    

    Element cubeElement=null;//state

    ConstraintTester tester;
    
    void validateDimensionalStandAlone(String plmFile){
        
        GUARD.ensure(plmFile.length()>0,"No input plm file provided");
        
        
        File f = new File(plmFile);        
        GUARD.ensure(f.exists()  ,"File does not exist: "+plmFile);
        GUARD.ensure(f.isFile()  ,"Path not a file: "+plmFile);
        GUARD.ensure(f.canRead()  ,"Cant read file: "+plmFile);
        
        
		String cubexml=null;
		try {
		    Element cubeElm = readPLM(plmFile);
		    
			//Now do the validation
			StringBuffer output=new StringBuffer();
	    	if(validate(cubeElm, output)){
	    	    System.out.println(output.toString());
	    	    System.out.println("Product Line Model Validated for Safe Composition");
	    	}
	    	else {
	    	    System.out.println(output.toString());
	    	    System.out.println("Product Line Model Not Validated for Safe Composition");
	    	}
			
			
		}		
		catch(Exception e){
		    e.printStackTrace();
			System.exit(0);
		}
		

    }
    
    
    /**
     * Validates a product line for safe composition. This method is intended to be used as a
     * stand alone application. The major difference between a validate(..) used through a Cube
     * and this is the input - both functions obtain needed input for validation differently.
     * <br><br>
     * I plan to support both linear model (single dimensional) as well as multidimensional models, although
     * they both will require different user inputs:<br>
     * Linear 
     * <li> Path to root directory storing layers of compiled classes
     * <li> Path to the model file (where primitives are names of layer directries)
     * <br>
     * Multidimensional
     * <li> Path to root directory storing layers of compiled classes
     * <li> Path to the Product Line Matrix (PLM) file defining dimensions and layers within each cell
     * 
     * @param rootPath
     * @param modelFile File path to the model file
     */
    void validateLinearStandAlone(String rootPath, String modelFile){
        
        GUARD.ensure(modelFile.length()>0,"No input model file provided");
        File f = new File(modelFile);        
        GUARD.ensure(f.exists()  ,"File does not exist: "+modelFile);
        GUARD.ensure(f.isFile()  ,"Path not a file: "+modelFile);
        GUARD.ensure(f.canRead()  ,"Cant read file: "+modelFile);
        
        String modelString = ValidationUtils.readFile(modelFile);
        
        if (modelString.contains("##")){
            modelString = modelString.substring(0, modelString.indexOf("##"));
        }
        
        
        FOPImplementationInfo fopimpl;
        OODesignInfo oodesignInfo = new OODesignInfo();
        
        tester = new ConstraintTester();
        tester.setModelFromString(modelString);
        
        
        /*
         * This line reads all fiels in all layers within the root dir. If a grammar includes only a subset
         * of the layers in the rootPath, there will be run-time errors when guidsl because a constraint will
         * reference Tokens not defined in the model grammar.
         * 
         * The solution is to read the grammar and process only those layers referenced in the grammar.
         */
        
        Stack fileStack = collectAllFiles(rootPath, modelFile);
        
        GUARD.ensure(fileStack.size()>0, "There must be atleast 1 file in the cube ");
        
        
        String pathToStubDirectory = rootPath+System.getProperty("file.separator")+"stub";
        
        //System.out.println("Stub is here:"+ pathToStubDirectory);
        
        StringBuffer output=new StringBuffer();
        if (validationOperation(oodesignInfo, fileStack, pathToStubDirectory, output)){
            System.out.println(output.toString());
            System.out.println("Product Line Model Validated for Safe Composition");
        }
        else {
            System.out.println(output.toString());
            System.out.println("Product Line Model Not Validated for Safe Composition");
        }

        //System.out.println(output.toString());

    }
    
    /**
     * 
     * @param cubeElem
     * @param modelString Content of the model as String (not the file path)
     * @param output	string buffer where output results are append
     * @return			bool whether the cube was validated
     */
    public boolean validate(Element cubeElem, StringBuffer output){
        
        this.cubeElement = cubeElem;
        //FOPImplementationInfo fopimpl;
        OODesignInfo oodesignInfo = new OODesignInfo();
        tester = new ConstraintTester();
        tester.setModelFromCubeElm(cubeElem);
        
        //System.out.println(XMLUtils.getXMLString(cubeElem));
        Stack fileStack = collectAllFiles(cubeElem);
        GUARD.ensure(fileStack.size()>0, "There must be atleast 1 file in the cube \n" +
                "Ensure that the second line of the PLM file points to the location of bytecode compiled directory" +
                "");
        
        String projectRootPath = getProjectRootPath((PLFile)fileStack.peek());
        String pathToStubDirectory = projectRootPath+System.getProperty("file.separator")+"stub";
        
        System.out.println("Stub is here:"+ pathToStubDirectory);
        
        return validationOperation(oodesignInfo, fileStack, pathToStubDirectory, output);
    }
    
    
    
    public boolean validationOperation(OODesignInfo oodesignInfo, Stack fileStack, String pathToStubDirectory, StringBuffer output){
        
        long start_time = System.currentTimeMillis(); // start timing
        
        FOPImplementationInfo fopimpl = ValidationUtils.extractPLClassInfo(oodesignInfo, fileStack, pathToStubDirectory);
        
        
        TestReferences(fopimpl, oodesignInfo);
        TestCommonDefsOrRefinements(fopimpl, oodesignInfo);
        
        long stop_time = System.currentTimeMillis(); // start timing
        
        return outputStats(stop_time-start_time, output);
        
    }
    
    void TestCommonDefsOrRefinements(FOPImplementationInfo fopimpl, OODesignInfo oodesignInfo){
        
        //for each class
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String i_className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(i_className);
	 
            if(plclass.toString().matches("[^\\$]++\\$++[0-9]++\\.class"))
                continue;
            
            //for each method
	        for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
	        {
	            String methodID = (String)methodIter.next();
	            PLMethod plmethod = plclass.getMethod(methodID);
	         
	            PLFile[] defs = plmethod.getDefinitionsAsArray();
	            
	            if(defs.length==1) //no need to test if only one layer defines
	                continue;
	            
	            for(int i=0; i<(defs.length-1);i++){
	                for(int j=i+1; j<defs.length;j++){
	                    if (isRightDiagonal(defs[i], defs[j])){
	                        if (tester.bothCanExistInProduct(defs[i].position, defs[j].position)){
	                            System.err.println("Orthogonality failure (Same method defined by layers at diagonal): " + defs[i].position.toString()+" with "+defs[j].position.toString());
	                            System.err.println(" Method: "+methodID);
	                            System.err.println("");
	                        }
	                        //else 
	                        //    System.err.println("blocked");
	                    }
	                }
	            }
	            
	            
	            PLFile[] refins = plmethod.getRefinementsAsArray();
	            
	            if(refins.length==1) //no need to test if only one layer refins
	                continue;
	            
	            for(int i=0; i<(refins.length-1);i++){
	                for(int j=i+1; j<refins.length;j++){
	                    if (isRightDiagonal(refins[i], refins[j])){
	                        if (tester.bothCanExistInProduct(refins[i].position, refins[j].position))
	                            System.err.println("Orthogonality failure (Same method refined by layers at diagonal): " + refins[i].position.toString()+" with "+refins[j].position.toString());
	                        //else 
	                        //    System.err.println("blocked");
	                    }
                        
                        
                        if (isLeftDiagonal(refins[i], refins[j])){
                            if (tester.bothCanExistInProduct(refins[i].position, refins[j].position))
                                System.err.println("LEFT DIAGONAL Warning (Same method refined by layers at diagonal): " + refins[i].position.toString()+" with "+refins[j].position.toString());
                            //else 
                            //    System.err.println("blocked");
                        }
                        
                        
	                }
	            }
	            
	        }
	        
	        //For each field
	        for(Iterator fieldIter = plclass.getListOfFields().iterator(); fieldIter.hasNext(); )
	        {
	            String fieldID = (String)fieldIter.next(); //field id is combination of type and fieldName
	            
	            //If cant find the field.. maybe non PL fields are filtered whiel adding?
	            if (!plclass.getHasField(fieldID))
	                continue;
	            
	            PLField plfield = plclass.getField(fieldID);
	            
	            PLFile[] defs = plfield.getDefinitionsAsArray();
	            
	            if(defs.length==1) //no need to test if only one layer defines
	                continue;
	            
	            for(int i=0; i<(defs.length-1);i++){
	                for(int j=i+1; j<defs.length;j++){
	                    if (isRightDiagonal(defs[i], defs[j])){
	                        if (tester.bothCanExistInProduct(defs[i].position, defs[j].position))
	                            System.err.println("Orthogonality failure (Same Field defined by layers at diagonal): " + defs[i].position.toString()+" with "+defs[j].position.toString());
	                        //else 
	                        //    System.err.println("blocked");
	                    }
	                }
	            }
	        }
	        
	        
	        
	        
	        
	        
	        
        }
    }
    
    void TestReferences(FOPImplementationInfo fopimpl, OODesignInfo oodesignInfo){
        
        //for each class
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String i_className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(i_className);
	 
            if(plclass.toString().matches("[^\\$]++\\$++[0-9]++\\.class"))
                continue;
            
	        for(Iterator fieldIter = plclass.getListOfFields().iterator(); fieldIter.hasNext(); )
	        {
	            String fieldID = (String)fieldIter.next(); //field id is combination of type and fieldName
	            
	            //If cant find the field.. maybe non PL fields are filtered whiel adding?
	            if (!plclass.getHasField(fieldID))
	                continue;
	            
	            PLField plfield = plclass.getField(fieldID);
	            PLFile P = (PLFile)plfield.definitionsIterator().next();
	            
	            /* If  type is not PL Class then dont bother
	             * E.g java.lang.String field;
	             */
	            if (!fopimpl.getOODesigninfo().isProductLineClass(plfield.getType()))
	                continue;
	            
	            GUARD.ensure(plfield!=null, "field null");
	            
	            String right = getClassDefsConstraintPart(formatName(plfield.getType()), fopimpl);
	            if (right.equals(""))
	                continue;
	
	            //For every definition of the Class (Type of the field)
	            Iterator defsIter = plfield.definitionsIterator();
	            while (defsIter.hasNext()){
	                PLFile file = (PLFile)defsIter.next(); //this is where it is defined
	                
	                if (isRightDiagonal(P, file)){
	                    if (tester.bothCanExistInProduct(P.position, file.position))
	                        System.err.println("Orthogonality failure class refs: " + P.position.toString()+" with "+file.position.toString());
	                    //else 
                        //    System.err.println("blocked");
	                }
	            
	            }
	            
	        } 
	        
	        
	        
	        //for each method
	        for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
	        {
	            String methodID = (String)methodIter.next();
	            PLMethod plmethod = plclass.getMethod(methodID);
	            
	            
	            //for each reference to a method
	            Iterator refIter = plmethod.methodReferencesIterator();
	            while(refIter.hasNext()){
	                
	                PLMethod.FileKeyPair pair = (PLMethod.FileKeyPair)refIter.next(); // key is of the form "className::type fieldName" 
	                String fieldKey = pair.key;
	                 
	                PLFile P = pair.file; //place from where m references X
	                
	                String[] tmp = fieldKey.split("::");
	                String methodsClassName = tmp[0];
	                String methodId = tmp[1];
	                
	                if(methodsClassName.matches("[^\\$]++\\$++[0-9]++\\$++"))
	                    continue;
	                
	                if (!fopimpl.getOODesigninfo().isProductLineClass(methodsClassName)){                   
	                    continue;
	                }
	                //find the class, then PLField, then layer
	                PLClass methodsClass = fopimpl.getClass(methodsClassName);
	                GUARD.ensure(methodsClass!=null, "Couldn't find method's class type");
	                
	                //Inherited fields can be referenced too.. so if they dont exist we need to look up
	                PLMethod method=null;
	                
	                OODesignInfo oodesign = fopimpl.getOODesigninfo();
	                boolean found=false;
	                PLClass currentClass=methodsClass;//start with the lowest level
	                
	                while(!found){
	                    if (currentClass.getHasMethod(methodId)){
	                        method = currentClass.getMethod(methodId);
	                        found=true;
	                    }
	                    else {
	                        
	                        if (oodesign.isProductLineClass(currentClass.toString()) && oodesign.hasParentClass(currentClass.toString()))
	                            currentClass = fopimpl.getClass(oodesign.getParentClass(currentClass.toString()));
	                        else 
	                            found=true;
	                    }
	                }
	                if (method==null) //if cant find the method skip this test
	                    continue;
	                
	                //find definitions D
	                Iterator defsIter = method.definitionsIterator();
		            while (defsIter.hasNext()){
		                PLFile file = (PLFile)defsIter.next(); //this is where it is defined
			            
                        if (isRightDiagonal(P, file)){
                            if (isRightDiagonal(P, file)){}
                            
			                if (tester.bothCanExistInProduct(P.position, file.position)){
			                    System.err.println("Orthogonality failure (method referencing method): " + P.position.toString()+" with "+file.position.toString());
			                    System.err.println(" "+plmethod.toString() +" references "+pair.key);
			                    System.err.println(" "+ P.layer +" references "+ file.layer);
			                    System.err.println("");
			                }
			                    //else 
			                //   System.err.println("blocked");
			            }
		            }
	                
		            
	            } // method reference iterator

	        } //for each method
	        
	        
//	      for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                
                //for each reference
                Iterator refIter = plmethod.fieldReferencesIterator();
                while(refIter.hasNext()){
                    
                    PLMethod.FileKeyPair pair = (PLMethod.FileKeyPair)refIter.next(); // key is of the form "className::type fieldName" 
                    PLFile P = pair.file;
                    
                    String fieldKey = pair.key;
                    
                    String[] tmp = fieldKey.split("::");
                    String fieldsClassName = tmp[0];
                    String fieldId = tmp[1];
                    tmp = tmp[1].split(" ");
                    String fieldType = tmp[0];
                    String fieldName = tmp[1];
                    
                    if (fieldName.matches("this\\$[0-9]++"))
                        continue;
                    
                    if (!fopimpl.getOODesigninfo().isProductLineClass(fieldsClassName)){
                        System.err.println("Skipping "+fieldsClassName);
                        continue;
                    }
                    //find the class, then PLField, then layer
                    PLClass fieldsClass = fopimpl.getClass(fieldsClassName);
                    GUARD.ensure(fieldsClass!=null, "Couldn't find field's class type");
                    //System.err.println("looking for "+fieldId + " in class "+fieldsClassName);
                    
                    
                    //Inherited fields can be referenced too.. so if they dont exist we need to look up
                    PLField field=null;
                    
                    OODesignInfo oodesign = fopimpl.getOODesigninfo();
                    boolean found=false;
                    PLClass currentClass=fieldsClass;//start with the lowest level
                    
                    while(!found){
                        if (currentClass.getHasField(fieldId)){
                            field = currentClass.getField(fieldId);
                            found=true;
                        }
                        else {
                            
                            if (oodesign.isProductLineClass(currentClass.toString()) && oodesign.hasParentClass(currentClass.toString()))
                                currentClass = fopimpl.getClass(oodesign.getParentClass(currentClass.toString()));
                            else 
                                found=true;
                        }
                    }
                    if (field==null) //if cant find the field  skip this test
                        continue;
                    
                    //find definitions D
	                Iterator defsIter = field.definitionsIterator();
		            while (defsIter.hasNext()){
		                PLFile file = (PLFile)defsIter.next(); //this is where it is defined
			            if (isRightDiagonal(P, file)){
			                if (tester.bothCanExistInProduct(P.position, file.position)){
			                    System.err.println("Orthogonality failure (method referencing field): " + P.position.toString()+" with "+file.position.toString());
			                    System.err.println("Class:"+plclass.toString());
			                    System.err.println(" From "+pair.file.layer+" method " + plmethod.toString());
			                    System.err.println("  references:"+pair.key + ", defined in "+file.layer);
			                    
			                    System.err.println("");
			                }
			                //else 
			                //    System.err.println("blocked");
			            }
		            }
	                
                    
                } // field reference iterator
                
            } //for each method
	        
            
            //for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                
                //for each reference to a class
                Iterator refIter = plmethod.classReferencesIterator();
                while(refIter.hasNext()){
                    
                    PLMethod.FileKeyPair pair = (PLMethod.FileKeyPair)refIter.next(); // key is of the form "className::type fieldName" 
                    PLFile P = pair.file;
                    String className = pair.key;              
                    
                    if (!fopimpl.getOODesigninfo().isProductLineClass(className)){
                        continue;
                    }
                    //find the class, then PLField, then layer
                    
                    PLClass referencedClass = fopimpl.getClass(className);
                    //find definitions D
	                Iterator defsIter = referencedClass.definitionsIterator();
		            while (defsIter.hasNext()){
		                PLFile file = (PLFile)defsIter.next(); //this is where it is defined
			            if (isRightDiagonal(P, file)){
			                if (tester.bothCanExistInProduct(P.position, file.position))
			                    System.err.println("Orthogonality failure (method referencing class): " + P.position.toString()+" with "+file.position.toString());
			                //else
			                //    System.err.println("blocked");
			            }
		                    
		                
		            }
	                
                    
                    
                } // method reference iterator
                
            }
	        
        }//for each class
               
        
        
    }
    
    
    
    /**
     * Returns true if any two dimension of the two coordinates are at right diagonal
     * @param P
     * @param D
     * @return
     */
    boolean isRightDiagonal(PLFile P, PLFile D){
        
        GUARD.ensure(P.position.getNumDimensions()==D.position.getNumDimensions(), "To check for diagonal both coordinates must have equal dimensions");
        GUARD.ensure(P.position.getNumDimensions()>1, "Matrix must have >1 dimensions to check for diagonal");
        
        int dims = P.position.getNumDimensions();
        
        
        for (int i=1;i<dims;i++){
            for (int j=i+1; j<=dims; j++){
                
                int dimA = i;
                int dimB = j;
                
                String P_Aunit = P.position.get(dimA);
                String P_Bunit = P.position.get(dimB);
                
                String D_Aunit = D.position.get(dimA);
                String D_Bunit = D.position.get(dimB);
                
                GUARD.ensure(P.position.getAxisName(dimA).equals(D.position.getAxisName(dimA)), "Both coordinates should have same axis for the same index");
                GUARD.ensure(P.position.getAxisName(dimB).equals(D.position.getAxisName(dimB)), "Both coordinates should have same axis for the same index");

                String axisA = P.position.getAxisName(dimA);
                String axisB = P.position.getAxisName(dimB);
                
                String[] axisAunits = getDimUnitsInOrder(axisA);
                String[] axisBunits =getDimUnitsInOrder(axisB);
                
                int indx_P_Aunit = ValidationUtils.getIndexOfStringInArray(axisAunits, P_Aunit);
                int indx_P_Bunit = ValidationUtils.getIndexOfStringInArray(axisBunits, P_Bunit);
                
                int indx_D_Aunit = ValidationUtils.getIndexOfStringInArray(axisAunits, D_Aunit);
                int indx_D_Bunit = ValidationUtils.getIndexOfStringInArray(axisBunits, D_Bunit);
                
                if (indx_P_Aunit==indx_D_Aunit ||indx_P_Bunit==indx_D_Bunit)
                    return false;
                else {
                    if (indx_P_Aunit > indx_D_Aunit){
                        if (indx_P_Bunit < indx_D_Bunit)
                            return true;
                    }
                    else { //has to be less than 
                        if (indx_P_Bunit > indx_D_Bunit)
                            return true;
                    }
                         
                    return false;   
                }
                
            }   
            
        }
        
        return false;
    }
    
    
    
    boolean isLeftDiagonal(PLFile P, PLFile D){
        
        GUARD.ensure(P.position.getNumDimensions()==D.position.getNumDimensions(), "To check for diagonal both coordinates must have equal dimensions");
        GUARD.ensure(P.position.getNumDimensions()>1, "Matrix must have >1 dimensions to check for diagonal");
        
        int dims = P.position.getNumDimensions();
        
        
        for (int i=1;i<dims;i++){
            for (int j=i+1; j<=dims; j++){
                
                int dimA = i;
                int dimB = j;
                
                String P_Aunit = P.position.get(dimA);
                String P_Bunit = P.position.get(dimB);
                
                String D_Aunit = D.position.get(dimA);
                String D_Bunit = D.position.get(dimB);
                
                GUARD.ensure(P.position.getAxisName(dimA).equals(D.position.getAxisName(dimA)), "Both coordinates should have same axis for the same index");
                GUARD.ensure(P.position.getAxisName(dimB).equals(D.position.getAxisName(dimB)), "Both coordinates should have same axis for the same index");

                String axisA = P.position.getAxisName(dimA);
                String axisB = P.position.getAxisName(dimB);
                
                String[] axisAunits = getDimUnitsInOrder(axisA);
                String[] axisBunits =getDimUnitsInOrder(axisB);
                
                int indx_P_Aunit = ValidationUtils.getIndexOfStringInArray(axisAunits, P_Aunit);
                int indx_P_Bunit = ValidationUtils.getIndexOfStringInArray(axisBunits, P_Bunit);
                
                int indx_D_Aunit = ValidationUtils.getIndexOfStringInArray(axisAunits, D_Aunit);
                int indx_D_Bunit = ValidationUtils.getIndexOfStringInArray(axisBunits, D_Bunit);
                
                if (indx_P_Aunit==indx_D_Aunit ||indx_P_Bunit==indx_D_Bunit)
                    return false;
                else {
                    if (indx_P_Aunit < indx_D_Aunit){
                        if (indx_P_Bunit < indx_D_Bunit)
                            return true;
                    }
                    else { //has to be less than 
                        if (indx_P_Bunit > indx_D_Bunit)
                            return true;
                    }
                         
                    return false;   
                }
                
            }   
            
        }
        
        return false;
    }

    /**
     * 
     * @param execTime
     * @param output
     * @return Returns bool whether this cube has been validated for safegen. Note: over
     * riding constraint can be set to error or warning
     */
    boolean outputStats(long execTime, StringBuffer output){
        StringBuffer out=new StringBuffer();
        
        out.append("================================"+"\n");
        out.append("--- Orthogonality Test Results ---"+"\n");
        out.append("--------------------------------"+"\n");
        
        out.append("\n");
        out.append("    Execution Time: "+(execTime/1000)+" seconds\n" );
        
        out.append("================================\n");
        
        if (REPORT_STATS){
	        //System.out.println(out.toString());
	        output.append(out);
        }
        
        
        return false;
            
            
    }
    

    
    /**
     * From a class name it gets String constraint of the form "layer1 or layer2 or .. layer_n" where layers 1-n define the class.
     * @param className
     * @param fopimpl
     * @return
     */
    public String getClassDefsConstraintPart(String className, FOPImplementationInfo fopimpl){
        PLClass classObj = fopimpl.getClass(className);
        GUARD.ensure(classObj!=null, "Couldn't find class type");
        
        Iterator classDefsIter = classObj.definitionsIterator();
        String rightSide = "";
        int i=0;
        while(classDefsIter.hasNext()){
            PLFile file = (PLFile)classDefsIter.next();
        
            if (i>0)
                rightSide+=" or ";
            
            rightSide+=file.position;
            i++;
        }
        
        return rightSide;
    }
    
    //plmethod is not part of plclass but we want to fidn definitions in plclass or its superclasses
    String superDefsFindMethod(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl){
        StringBuffer result= new StringBuffer("");
        
        //if same method exists in the given class
        if (plclass.getHasMethod(plmethod.toString())){
            PLMethod myMethod = plclass.getMethod(plmethod.toString());
            result.append(superDefsn(myMethod, plclass, fopimpl));            
        } 
        else 
            result.append(goUpDefs(plmethod, plclass, fopimpl));
        
        return result.toString();
     
     }
    
    /** Represents the first super0(m) call. This is differentiated 
     *  from nth call (where n>0) for obtaining statistical information only.
     **/
    String superDefs0(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl){
        return superDefs(plmethod, plclass, fopimpl, true);
    }
    
    /** Represents the first supern(m) call, where n = 1,2,3.. **/
    String superDefsn(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl){
        return superDefs(plmethod, plclass, fopimpl, false);
    }
    /**
     * Recursively generates sup0(m) or sup1(m) or sup2(m)...
     * NO brackets needed. But "or" must be placed appropriately
     * 
     * @param plmethod the method being searched. Be careful, this method HAS to belong to plclass. Wont work simply with same name. PLMethod represents {method, class}
     * 
     * @param plclass  
     * @param fopimpl 
     * @return Constraint for definitions of the given method and any definitions in parent classes, recursively generated
     */
    String superDefs(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl, boolean first){
       StringBuffer result= new StringBuffer("");
       
       Iterator defIter = plmethod.definitionsIterator();
       int i=0;
       while(defIter.hasNext()){
           
           //Counts num of constraint part generated because of refinement of a method definition
           //if (first && i==0)
           //    m_numRefConstraints++;
           
           PLFile definingFileData = (PLFile) defIter.next();
           
           if (i>0)
               result.append(" or ");
           result.append(definingFileData.position);
           i++;
       }

       String superConstraint = goUpDefs(plmethod, plclass, fopimpl).trim();
       if (!superConstraint.equals("") && !result.toString().trim().equals(""))
           result.append(" or "+superConstraint);
       
       return result.toString();
    
    }
    /**
     * Generates constraints of any superclasses if super class exists. Climbs up the tree till
     * no parent (from this product-line) exists and anywhere that it finds method definitions
     * it calls superDefs to generate constraints.
     * 
     * @param plmethod
     * @param plclass
     * @param fopimpl
     * @return
     */
    String goUpDefs(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl){
        
        StringBuffer result=new StringBuffer("");
        
        OODesignInfo designInfo = fopimpl.getOODesigninfo();
        //if method's class has a parent
        if (designInfo.hasParentClass(plclass.toString())){
            String parentClassName = designInfo.getParentClass(plclass.toString());
            PLClass parentPLClass = fopimpl.getClass(parentClassName);
            
            //if same method exists in parent class
            if (parentPLClass.getHasMethod(plmethod.toString())){
                PLMethod parentsMethod = parentPLClass.getMethod(plmethod.toString());
                
                //System.out.println("superDefsn..");
                
                result.append(superDefsn(parentsMethod, parentPLClass, fopimpl));
                
                //Counts number of Super class constraints. If parent has it defined only then count
                //otherwise it may keep climbing up
                //if(parentsMethod.definitionsIterator().hasNext())
                   // m_numSuperRef++;
                
            } 
            else { //if plmethod does not exist in parent
                //keep going up, but pass in parent class
                //System.out.println("goUpDefs..");
                result.append(goUpDefs(plmethod, parentPLClass, fopimpl));
                
            }
            
        }
        return result.toString();
    }
    
    
    String getProjectRootPath(PLFile file){
        
        String filepath = file.file;
        String feature = file.layer;
        
        /* set the projectRoot path */
		String projectRoot=null;
		if (System.getProperty("file.separator").equals("/"))
			projectRoot= ((filepath.split("/"+feature+"/"))[0]);
		else if (System.getProperty("file.separator").equals("\\"))
			projectRoot= ((filepath.split("\\\\"+feature+"\\\\"))[0]);
		
		return projectRoot;
    }
    /**
     * Puts path names of all files in a project directory into a file stack to be processed later..
     * @param rootPath
     * @return
     */
    Stack collectAllFiles(String rootPath, String modelFilePath ){
        Stack fileStack=new Stack();
        
        Tool tool=null ; 
	    try {
	        tool = new Tool(modelFilePath); 
	    }
	    catch (Exception e){
	        e.printStackTrace();
	        System.out.println("Fiel: "+modelFilePath);
	        System.exit(0);
	    }

        visitAllDirsAndFiles(new File(rootPath), null, fileStack);
        
        //-- Resetting guidsl tool --
        grammar.reset();
	    production.resetModel();
	    variable.Vtable=new HashMap();
	    variable.vtsize=0;
	    //---------------------------
	    
        return fileStack;
    }
    
    /**
     * Visits a given director recursively and places class file path names into fileStack
     * A file is only visited if its layer is part of the input model grammar's primitive (terminal) token.
     * Assumes single level of directory (singel level layers)
     * 
     * @param path the path name to a file or dir
     * @param parentDir the parent directory name - representing the layer name. If null then dir is the root directory
     * @param fileStack reference to the stack that will be filled by files
     */
    public void visitAllDirsAndFiles(File path, String parentDir, Stack fileStack) {
        
        //If parentDir is null then dir must be dir not file
        GUARD.ensure(parentDir!=null||path.isDirectory() , "Root path name must be a directory");
        
        if (path.isFile()) {
   
	        //If a class file
	    	if (path.getAbsolutePath().endsWith(".class")){
	    	    
	    	    PLFile file = new PLFile();
				file.file = path.getAbsolutePath();
				file.layer = parentDir;
				
				Coordinate dimUnits = new Coordinate(1);
				dimUnits.set(1, parentDir);
				dimUnits.complete();
				
				file.position = dimUnits;
				fileStack.push(file);	
	    	    
	    	}
	    
        }
        else if (path.isDirectory()) {
            if (!path.getName().equals("stub")){
	            String[] children = path.list();
	            for (int i=0; i<children.length; i++) {
	                //if parent is null then it is the root directory. root will never be part of the model grammar
	                //otehrwise check that it is a primitive in the grammar, only then process the layer
	                if (parentDir==null||(variable.Vtable.containsKey(path.getName()) && !grammar.productions.containsKey(path.getName())))
	                    visitAllDirsAndFiles(new File(path, children[i]), path.getName(), fileStack);
	            }
            }
        }
    }

    /**
     * Puts path names of all files located under a cube's XML Element structure into a file stack 
     * @param cubeElm
     * @return
     */
    Stack collectAllFiles(Element cubeElm ){
        
        Stack nodeStack=new Stack();
        Stack fileStack=new Stack();
      
        
		/*   
		 *  Traverse the tree depth-first (using nodeStack) and push all file
		 *  nodes into fileStack
		 *  
		 */
		nodeStack.push(cubeElm);
		
		String feature="no feature";
		Coordinate dimUnits=null; //stores units of dimensions for a set of files
		while(!nodeStack.empty()){
			Element elm = (Element)nodeStack.pop();
			
			/*
			 * If at feature node, find units of every dimension
			 * and store it in dimUnits so that we can add this information to a file we find during
			 * depth traversal 
			 */
			if (elm.getName().equals("feature")){
				feature = elm.getAttributeValue("name");
				
				List units = elm.getChildren("index");
				dimUnits = new Coordinate(units.size());
				
				Iterator indexes = units.iterator();
				int c=1;
				while(indexes.hasNext()){
				    Element index = (Element)indexes.next();
					Element unit = index.getChild("unit");
					Element axis = index.getChild("axis");
					
					dimUnits.set(c, unit.getText());
					dimUnits.setAxisName(c, axis.getText());
					c++;
				}
				dimUnits.complete();
			}
			
			//loop through all children
			Iterator it = elm.getChildren().iterator();
			while(it.hasNext()){
				Element candidate = (Element)it.next();
				
				//child is a file node and class file
				//dont add this to stack
				if (candidate.getName().equals("file") && candidate.getAttributeValue("type").equalsIgnoreCase("class")){
				    
					//candidate.setAttribute("dimUnits", temp);
					PLFile file = new PLFile();
					file.file = candidate.getAttributeValue("location");
					file.layer = feature;
					file.position = dimUnits;
					fileStack.push(file);	
				}
				else { //if child is not a file node
					nodeStack.push(candidate);
				}
			}
			
		}
		// Should have all files in file stack
		
		return fileStack;
    
    }
    
 
    String[] getDimUnitsInOrder(String axisName){
        GUARD.ensure(cubeElement!=null, "CubeElem not set");
        
        Element dims = (Element)cubeElement.getChild("dimensions");
        GUARD.ensure(dims!=null, "dims null");
        
        List axes = dims.getChildren("axis");
        
        
        Iterator axesIter = axes.iterator();
        while(axesIter.hasNext()){
            Element axis = (Element)axesIter.next();
            if (axis.getAttributeValue("name").equals(axisName)){
                List units = axis.getChildren("unit");
                
                String[] arr = new String[units.size()];
                Iterator unitsIter = units.iterator();
                int i=0;
                //System.out.println("Printing Units in order");
                while(unitsIter.hasNext()){
                    String unit = ((Element)unitsIter.next()).getAttributeValue("name");
                    arr[i]=unit;
                    //System.out.println("  "+unit);
                    i++;
                }
                
                
                return arr;
            }
            
        }
        
        
        GUARD.ensure(false, "Could not find axis");
    
        return null;
    }
    /**
     * Basically this formatting ensures class names are in a format
     * that can be directly matched to classes retrieved form layers.
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
            
        return className.replaceFirst("stub.", "");
    
    }

    
    
    Element readPLM(String plmfilePath){
        try {
	        PLMReader plm = new PLMReader();
			plm.read(plmfilePath);
			
			if (plm.errorMessage.length()>0){
				System.err.println("There was an error reading plm file");
				throw new Exception( plm.errorMessage);
			}
			
			Document doc = new Document();
			Element elmCube = new Element("cube");
			Element elmDim = new Element("dimensions");
			Element elmFeatures = new Element("features");
			elmCube.setAttribute("name", "cubeName");
			doc.addContent(elmCube);
			elmCube.addContent(elmDim);
			elmCube.addContent(elmFeatures); 
			
			
			String modelfileString = plm.getModelFileString();
            try {
                File temp = new File(modelfileString);
                
                //Absolute path does not exist, maybe it is a relative path
                if (!temp.exists()){
                   File f = new File(plmfilePath);
                  temp = new File(f.getParent(), modelfileString);
                    
                }
                if (!temp.isFile())
                    throw new Exception("File "+modelfileString+" musts exist");
                
                modelfileString=temp.getAbsolutePath();
            } catch(Exception e){
                throw e;
            }
            
			Tool tool = new Tool(modelfileString);
			
			PrintXML printer = new PrintXML();
	        String grammar = printer.getXMLString();
	        String constraints = ESList.getCTableXML();
	        
	        //Element elmModel = (XMLUtils.getDocFromString("<model>"+grammar+constraints+"</model>")).getRootElement().detach();
	        elmCube.addContent((XMLUtils.getDocFromString("<model>"+grammar+constraints+"</model>")).getRootElement().detach());
	        //------------------------------------------------
	        		
			
			//
			//-----------------------------------
			
			// POSTreq dim
			/*---------- build dimensions ---------*/
			ArrayList dim = plm.getDimensions();
			ArrayList dimNames = plm.getDimensionNames();
			
			for(int i=0;i< dim.size();i++){
				Element elmAxis = new Element("axis");
				elmAxis.setAttribute("name", (String)dimNames.get(i));
				//elmAxis.setAttribute("name", Integer.toString(i+1));
				
				ArrayList units = (ArrayList)dim.get(i);
				for(int j=0;j< units.size();j++){
					Element elmUnit = new Element("unit");
					elmUnit.setAttribute("name", (String)units.get(j));
					elmAxis.addContent(elmUnit);
					
				}
				
				elmDim.addContent(elmAxis);
			}
			/*-------- end build dimensions -------*/
			// POSTreq dim built
			
			
			/*---------- build features ---------*/
			ArrayList features = plm.getFeatures();
			
			for(int i=0;i<features.size();i++){
				Feature feature = (Feature)features.get(i);
				Element f = new Element("feature");
				f.setAttribute("name", feature.name);
				
				
				for(int j=0;j< feature.indexes.length;j++){
					
					Element index = new Element("index");
					Element axis = new Element("axis");			
					axis.setText((String)dimNames.get(j));
					//axis.setText(Integer.toString(j+1));
					
					Element unit = new Element("unit");
					unit.setText(feature.indexes[j]);
					index.addContent(axis);
					index.addContent(unit);
					f.addContent(index);
				}
				
				elmFeatures.addContent(f);
				
				Element layers = new Element("layers");
				f.addContent(layers);
			
				File rootlayer=null;
				File[] subfiles=null;
				try {
					//traverse directories
					rootlayer =new File(plm.getRootPath()+"\\"+feature.name);
				
					subfiles = rootlayer.listFiles();
					if (subfiles !=null){
						for (int j=0;j< subfiles.length; j++)
							if(! subfiles[j].getName().equalsIgnoreCase("CVS"))
								layers.addContent(encodeSubFiles(subfiles[j]));
					}
		
				} catch(Exception e){
				    System.err.println("-----------");
				    System.err.println("File: "+plm.getRootPath()+"\\"+feature.name);
				    System.err.println("subfiles:"+subfiles);
					e.printStackTrace();
				}
				
				//layers.setText(plm.getRootPath()+"\\"+feature.name);
				
			}
			
			
			
			
		
			return elmCube;
		        
        } catch (Exception e){
            e.printStackTrace();
        }
    
        return null;
    }
    
    /*
	 * @param File f could be file or directory
	 * @return Element representint either the file or 
	 * 		   the directory with subdirectories encoded
	 * 
	 * Recurse directories, of if file return file element
	 */
	private Element encodeSubFiles(File f){
		if (f.isFile()){
			Element file = new Element("file");
			try {
				file.setAttribute("name", f.getName());
				file.setAttribute("type", getFileType(f));

				file.setAttribute("location", f.getCanonicalPath());
				
			} catch (Exception e){}
			file.setAttribute("modified", Long.toString(f.lastModified()));
			
			return file;
		}
		else if (f.isDirectory()){
			Element dir = new Element("directory");
			dir.setAttribute("name", f.getName());
			File[] subfiles = f.listFiles();
			
			for (int i=0;i< subfiles.length;i++)
				if (! subfiles[i].getName().equalsIgnoreCase("CVS"))
					dir.addContent(encodeSubFiles(subfiles[i]));
			
			
			return dir;
		}
		
		else return null;
	}
	
	private String getFileType(File f){
		String filetype="unknown";
		if (f.getName().endsWith(".jak")||f.getName().endsWith(".JAK"))
			filetype="jak";
		else if (f.getName().endsWith(".java")||f.getName().endsWith(".JAVA"))
			filetype="java";
		else if (f.getName().endsWith(".class")||f.getName().endsWith(".CLASS"))
			filetype="class";
		else if (f.getName().endsWith(".equation")||f.getName().endsWith(".EQUATION"))
			filetype="equation";
		else if (f.getName().endsWith(".b")||f.getName().endsWith(".B"))
			filetype="grammar";
		else if (f.getName().endsWith(".drc")||f.getName().endsWith(".DRC"))
					filetype="drc";	
		return filetype;
	}
    

    /*
     * SafeCompositionValidator [-o -r -ie -iw -ii -md] rootPath modelFile
     */
    public static void main(String[] args) {
        //check input options etc..
//      Defaults
        REPORT_STATS = false;
        OUTPUT_FAILURES = false;        
        
        boolean multidimensional = false;
        
        try {
            if(args.length < 2)
                throw new Exception("Must provide atleast two arguments");

            boolean encounteredIntro=false;
            int endParams=2;
            if (args[0].equals("-md"))
                endParams=1;
            
            for (int i=0;i<args.length-endParams;i++){
                
                if (args[i].equals("-ie")
                        ||args[i].equals("-iw")
                        ||args[i].equals("-ii")){
                    
                    if (encounteredIntro){
                        throw new Exception("Constraint: Choose1(-ie, -iw, -ii)");
                    }
                    else 
                        encounteredIntro=true;
                }

                if (args[i].equals("-f"))
                    OUTPUT_FAILURES=true;    
                else if (args[i].equals("-s"))
                    REPORT_STATS=true;
                    
                else if (args[i].equals("-md"))
                    multidimensional=true;
                else {
                    throw new Exception("Invalid argument token:"+args[i]);
                }
            }
            
        } catch (Exception e){
            System.out.println(e.getMessage());
            //Help Message
            System.out.println("------------------------------------------------------------");
            System.out.println("Usage: OrthogonalityValidator LINEAR_OR_DIM\n\n" +
            				   "  LINEAR_OR_DIM: OPTIONS* rootPath modelFile  //linear\n" +
            				   "               | -md OPTIONS* plmFile         //dimensional");
            System.out.println("  OPTIONS: -f \n" +
            				   "         | -s \n" +
            				   "         | -ie \n" +
            				   "         | -iw \n" +
            				   "         | -ii \n" +
            				   "\n" +
            				   "  Constraint: Choose1(-ie, -iw, -ii)");
            System.out.println("------------------------------------------------------------");
            System.out.println("  Options");
            System.out.println("  -f  Output Failure Details");
            System.out.println("  -s  Report Validation Statistics");
            System.out.println("  -ie Treat Introduction Constraints as Error");
            System.out.println("  -iw Treat Introduction Constraints as Warning");
            System.out.println("  -ii Ignore Introduction Constraints");
            System.out.println("------------------------------------------------------------");
            System.out.println("Example: java -cp \"guidsl.jar;ClassReader.jar;jdom.jar;sat4j.jar\" ProgramCube.Validation.SafeComposition.SafeCompositionValidator -f -s -ii \"path\" \"path\"");
            System.exit(0);
        }
        
        

        OrthogonalityValidator validator = new OrthogonalityValidator();
        
        if (!multidimensional)
            System.err.println("Must be a multidimensional matrix");
        else
            validator.validateDimensionalStandAlone(args[args.length-1]);
        
    }
    
    
    public class PLFileCoordinateComparator implements Comparator{
    	public int compare( Object o1, Object o2){
    		String pos1 = ((PLFile)o1).position.toString();
    		String pos2 = ((PLFile)o2).position.toString();
    		
    		return pos1.compareTo(pos2);
    	}
    }

}
