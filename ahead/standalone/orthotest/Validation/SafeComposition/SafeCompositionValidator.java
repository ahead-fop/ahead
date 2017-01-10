package ProgramCube.Validation.SafeComposition;

import guidsl.ESList;
import guidsl.PrintXML;
import guidsl.Tool;
import guidsl.findRank;
import guidsl.grammar;
import guidsl.production;
import guidsl.variable;

import java.util.Iterator;
import java.util.Vector;

import DesignByContract.*;
import ProgramCube.Feature;
import ProgramCube.PLMReader;
import ProgramCube.DataStore.XMLUtils;
import ProgramCube.Validation.FOPImplementationInfo;
import ProgramCube.Validation.OODesignInfo;
import ProgramCube.Validation.ValidationUtils;
import ProgramCube.Validation.PLObjects.*;

import org.jdom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;


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
 *
 */
public class SafeCompositionValidator {
    static boolean REPORT_STATS = true;
    static boolean OUTPUT_FAILURES = false;
    
    static boolean DEBUG_OUTPUT=false;
    static boolean FLIP = true;
    static boolean CHECKSELFREFERENCES=false;
    
    //Define flags. Random nums so wont match another int by mistake.
    static final int SAFECOMP_ERROR 	= 2341; 
    static final int SAFECOMP_WARNING 	= 9892;
    static final int SAFECOMP_IGNORE 	= 100295;
    
    //Configurable parameter
    static int INTRODUCTION_CONSTRAINT=SAFECOMP_ERROR;
    
    int m_numAbstractClassConstraint=0;
    int m_numInterfaceClassConstraint=0;
    int m_numIntroductionConstraint=0;
    int m_numReferenceConstraints=0;
    int m_numRefinementConstraints=0;
    
    int m_numAbstractClassConstraintFailures=0;
    int m_numInterfaceClassConstraintFailures=0;
    int m_numIntroductionConstraintFailures=0;
    int m_numReferenceConstraintsFailures=0;
    int m_numRefinementConstraintsFailures=0;
    
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
        
        //FOPImplementationInfo fopimpl;
        OODesignInfo oodesignInfo = new OODesignInfo();
        tester = new ConstraintTester();
        tester.setModelFromCubeElm(cubeElem);
        //System.out.println(XMLUtils.getXMLString(cubeElem));
        
        Stack fileStack = collectAllFiles(cubeElem);
        GUARD.ensure(fileStack.size()>0, "There must be atleast 1 file in the cube ");
        
        String projectRootPath = getProjectRootPath((PLFile)fileStack.peek());
        String pathToStubDirectory = projectRootPath+System.getProperty("file.separator")+"stub";
        
        System.out.println("Stub is here:"+ pathToStubDirectory);
        
        return validationOperation(oodesignInfo, fileStack, pathToStubDirectory, output);
    }
    
    /**
     * Does the actual validation.
     * @param oodesignInfo
     * @param fileStack
     * @param pathToStubDirectory
     * @param output
     * @return
     */
    public boolean validationOperation(OODesignInfo oodesignInfo, Stack fileStack, String pathToStubDirectory, StringBuffer output){
        
        long start_time = System.currentTimeMillis(); // start timing
        
        FOPImplementationInfo fopimpl = ValidationUtils.extractPLClassInfo(oodesignInfo, fileStack, pathToStubDirectory);
        
        
        RefinementConstraint(fopimpl);
        
        ReferenceFieldConstraint(fopimpl);
        ReferenceMethodConstraint(fopimpl);
        ReferenceClassConstraint(fopimpl);
        
        IntroductionConstraint(fopimpl);
        AbstractClassConstraint(fopimpl);
        InterfaceConstraint(fopimpl);
        
        long stop_time = System.currentTimeMillis(); // start timing
        if (OUTPUT_FAILURES)
            tester.reportFailures(output, DEBUG_OUTPUT);
        
        if(CHECKSELFREFERENCES)
            CheckMethodsNotInRightPlace(fopimpl);
        
        return outputStats(stop_time-start_time, output);
        

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
        out.append("--- Safe Composition Results ---"+"\n");
        out.append("--------------------------------"+"\n");
        out.append("Refinement Constraint: " + m_numRefinementConstraints +" (Failed "+m_numRefinementConstraintsFailures+")" +"\n");
        out.append("Reference Constraint: " + m_numReferenceConstraints +" (Failed "+m_numReferenceConstraintsFailures+")" +"\n");
        
        if (INTRODUCTION_CONSTRAINT==SAFECOMP_IGNORE)
            out.append("Introduction Constraint: IGNORING.." +"\n");
        else
            out.append("Introduction Constraint: " + m_numIntroductionConstraint+ " (Failed "+m_numIntroductionConstraintFailures+")" +"\n");
        
        out.append("Abstract Class Constraint: " + m_numAbstractClassConstraint+ " (Failed "+m_numAbstractClassConstraintFailures+")" +"\n");
        out.append("Interface Constraint: " + m_numInterfaceClassConstraint+ " (Failed "+m_numInterfaceClassConstraintFailures+")" +"\n");
        out.append("================================"+"\n");
        int sumOfConstraints = m_numRefinementConstraints+m_numReferenceConstraints+m_numIntroductionConstraint+m_numAbstractClassConstraint+m_numInterfaceClassConstraint;
        int sumOfFailures = m_numRefinementConstraintsFailures+m_numReferenceConstraintsFailures+m_numIntroductionConstraintFailures+m_numAbstractClassConstraintFailures+m_numInterfaceClassConstraintFailures;
        
        out.append(" Total Constraints: "+sumOfConstraints +"\n" +
        		   "    Total Failures: "+ sumOfFailures+"\n");
        out.append("  Total Duplicates: "+tester.getNumDuplicates()+"\n");
        out.append("Unique Constraints: "+(sumOfConstraints-tester.getNumDuplicates())+"\n");
        
        out.append("\n");
        out.append("    Execution Time: "+(execTime/1000)+" seconds\n" );
        
        out.append("================================\n");
        
        if (REPORT_STATS){
	        //System.out.println(out.toString());
	        output.append(out);
        }
        
        if (INTRODUCTION_CONSTRAINT==SAFECOMP_ERROR){
            return (sumOfFailures==0);//true (validated) if there are no failures
        }
        else if (INTRODUCTION_CONSTRAINT!=SAFECOMP_ERROR){ //either warning or ignore
                
            return (!(m_numRefinementConstraintsFailures>0
                    || m_numReferenceConstraintsFailures>0
                    || m_numAbstractClassConstraintFailures>0
                    || m_numInterfaceClassConstraintFailures>0));
                    
            
        }
        else {
            GUARD.ensure(false, "This statement should be unreachable");
            return false; //shd be unreachable
        }
        
            
            
    }
    
    
    /**
     * Generates constraints relating to abstract classes of the form: 
     * F and X implies sup_n(m). If X is absent, then F implies sup_n(m)
     * 
     * @param fopimpl
     */
    void AbstractClassConstraint(FOPImplementationInfo fopimpl){
        int numAbstractConstraints=0;
        
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            
            OODesignInfo designInfo = fopimpl.getOODesigninfo();
            
            //Shortcut: concrete classes cant contain abstract methods
            if (!designInfo.isAbstract(className))
                continue;
 
            //for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                
                
                //abstract method
                if (plmethod.getIsAbstract()){
                    
                    //for each definition of the abstract method
                    Iterator defIter = plmethod.definitionsIterator();
                    while(defIter.hasNext()){
                        
                        PLFile F = (PLFile)defIter.next();
                        
                        //for each concrete subclass (1st down a given chain)
                        Vector subClasses = getConcreteChildsInSubChain(className, designInfo);
                        
                        for (int i=0;i<subClasses.size();i++){
                            String concreteClass = (String)subClasses.get(i);
	                        
                            PLClass concreteplclass = fopimpl.getClass(concreteClass);
                            GUARD.ensure(concreteplclass!=null, "concrete subclass cant be null");
                            
                            Iterator subclassDefs = concreteplclass.definitionsIterator();
                            int numDefs=0;
                            while(subclassDefs.hasNext()){
                                PLFile X = (PLFile)subclassDefs.next();
                                
                                //Now test constraint
                                //F and X implies Sup0(m) or Sup1(m) or Sup2(m) or ....
                                String constraint = F.position+" and "+ X.position + " implies not ("+superDefsFindMethod(plmethod, concreteplclass, fopimpl, new Vector()) +")";
                                
                                String constType = "Abstract Class Constraint";
                                String implDetails = "Abstract Method: "+plmethod.toString()+" in Class:"+plclass;
                                
                                String layerDetails = "  Abstract Method Introduced in: " + F.layer+"  Concrete Class "+concreteplclass+" introduced in: "+X.layer;
                                
                                Vector coordinatesToSet = new Vector();// leave empty
                                coordinatesToSet.add(F.position);
                                coordinatesToSet.add(X.position);
                                
                                m_numAbstractClassConstraint++;
                                if (!tester.validateConstraint(constraint, coordinatesToSet, constType, implDetails, layerDetails))
                                    m_numAbstractClassConstraintFailures++;
                                    
                                numDefs++;
                            }
                            
                            //If no definitions
                            if (numDefs==0){
        
                                System.err.println("Zero Definitions of class: "+ concreteClass);
                                
                                //Now test constraint
                                //F implies Sup0(m) or Sup1(m) or Sup2(m) or ....
                                String constraint = F.position+" implies not ("+superDefsFindMethod(plmethod, concreteplclass, fopimpl, new Vector()) +")";
                                
                                String constType = "Abstract Class Constraint";
                                String implDetails = "Abstract Method: "+plmethod.toString()+" in Class:"+plclass;
                                
                                String layerDetails = "  Abstract Method Introduced in: " + F.layer+"  Concrete Class never introduced";
                                
                                Vector coordinatesToSet = new Vector();// leave empty
                                coordinatesToSet.add(F.position);
       
                                m_numAbstractClassConstraint++;
                                if (!tester.validateConstraint(constraint, coordinatesToSet, constType, implDetails, layerDetails))
                                    m_numAbstractClassConstraintFailures++;
                                
                                
                            }
                                
                            

                        }
                                                  
                    }
                    
                }
                    
                    
            }
        }
        

    }
    
    
    /**
     * Generates constraints relating to Interfaces of the form: 
     * F and X implies sup_n(m). If X is absent (i.e. no class implements the interface), then the constraint is not tested
     * 
     * @param fopimpl
     */
    void InterfaceConstraint(FOPImplementationInfo fopimpl){
        int numAbstractConstraints=0;
        
        //for each class
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            
            OODesignInfo designInfo = fopimpl.getOODesigninfo();
            
                
            // has to be interface? what about refinements to add interface
            if (!designInfo.isInterface(className))
                continue;
 
            //for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                //for each definition of the interface's method
                Iterator defIter = plmethod.definitionsIterator();
                while(defIter.hasNext()){
                    
                    PLFile F = (PLFile)defIter.next();
                    
                    //for each concrete subclass (1st down a given chain)
                    Vector subClasses = getConcreteInterfaceImplementor(className, designInfo);
                    
                    for (int i=0;i<subClasses.size();i++){
                        String concreteClass = (String)subClasses.get(i);
                        
                        PLClass concreteplclass = fopimpl.getClass(concreteClass);
                        GUARD.ensure(concreteplclass!=null, "concrete subclass cant be null");
                        
                        Iterator subclassDefs = concreteplclass.definitionsIterator();
                        int numDefs=0;
                        while(subclassDefs.hasNext()){
                            PLFile X = (PLFile)subclassDefs.next();
                            
                            //Now test constraint
                            //F and X implies Sup0(m) or Sup1(m) or Sup2(m) or ....
                            String constraint = F.position+" and "+ X.position + " implies not ("+superDefsFindMethod(plmethod, concreteplclass, fopimpl, new Vector()) +")";
                            
                            String constType = "Interface Class Constraint";
                            String implDetails = "Interface Method: "+plmethod.toString()+" in Class:"+plclass;
                            
                            String layerDetails = "  Interface Method Introduced in: " + F.layer+"  Concrete Class "+concreteplclass+" introduced in: "+X.layer;
                            
                            Vector coordinatesToSet = new Vector();// leave empty
                            coordinatesToSet.add(F.position);
                            coordinatesToSet.add(X.position);
                            
                            m_numInterfaceClassConstraint++;
                            
                            //System.out.println("Constraint: "+constraint);
                            //System.out.println("Impl detal: "+implDetails);
                            //System.out.println("Layer detl: "+layerDetails);
                            //System.out.println("---------------------");
                            
                            if (!tester.validateConstraint(constraint, coordinatesToSet, constType, implDetails, layerDetails))
                                m_numInterfaceClassConstraintFailures++;
                                
                            numDefs++;
                        }
                        
                        /* dont test this for interfaces
                        //If no definitions
                        if (numDefs==0){
    
                            //Now test constraint
                            //F implies Sup0(m) or Sup1(m) or Sup2(m) or ....
                            String constraint = F.position+" implies not ("+superDefsFindMethod(plmethod, concreteplclass, fopimpl) +")";
                            
                            String constType = "Abstract Class Constraint";
                            String implDetails = "Abstract Method: "+plmethod.toString()+" in Class:"+plclass;
                            
                            String layerDetails = "  Abstract Method Introduced in: " + F.layer+"  Concrete Class never introduced(always introduced?)";
                            
                            Vector coordinatesToSet = new Vector();// leave empty
                            coordinatesToSet.add(F.position);
   
                            m_numAbstractClassConstraint++;
                            if (!tester.validateConstraint(constraint, coordinatesToSet, constType, implDetails, layerDetails))
                                m_numAbstractClassConstraintFailures++;
                            
                            
                        }
                        */   
                        

                    }
                                              
                }
                
            
                    
                    
            }
        }
        

    }
    
    /**
     * Resursively gets all 1st-concrete classes downt he branches.
     * <br>
     * E.g inheritance hierarchy (where A1 is the root):<br>
     * <li> A1 --> C2 A3 C4 
     * <li> C2 --> C5
     * <li> A3 --> A6 C7
     * <li> A6 --> C8
     * <li>C4 --> C9 C10
     * <br><br>
     * Should select C2, C4, C7 and C8
     * 
     * @param className
     * @param designInfo
     * @return
     */
    Vector getConcreteChildsInSubChain(String className, OODesignInfo designInfo){
        Vector result = new Vector();
		
        //for each definition of concrete subclass (1st down a given chain)
	    if (designInfo.hasChildClass(className)){
		    String[] childClasses  = designInfo.getChildrenClass(className);
		    
		    for (int i=0;i<childClasses.length;i++){
		        if (designInfo.isAbstract(childClasses[i])){
		            result.addAll(getConcreteChildsInSubChain(childClasses[i], designInfo));
		        }
		        else
		            result.add(childClasses[i]);
		    }
        }
	    
	    return result;
    }
    
    /**
     * Resursively(intef implementing intf [is this possible?]) gets all concrete classes implementing the gigen className interface.
     * 
     * @param className
     * @param designInfo
     * @return
     */
    Vector getConcreteInterfaceImplementor(String className, OODesignInfo designInfo){
        Vector result = new Vector();
		
        //for each definition of concrete subclass (1st down a given chain)
	    if (designInfo.hasIntfImplementors(className)){
	        
		    String[] childClasses  = designInfo.getClassesImplementingIntf(className);
		    
		    for (int i=0;i<childClasses.length;i++){
		        if (designInfo.isInterface(childClasses[i])){
		            result.addAll(getConcreteChildsInSubChain(childClasses[i], designInfo));
		        }
		        else
		            result.add(childClasses[i]);
		    }
        }
	    
	    return result;
    }
    
    
    /**
     * Generates and tests multiple introduction constraints given implementation details.
     * Currently tests Methods only.
     * 
     * This can be set as ERROR, WARNING, or IGNORE.<br>
     * <li>Error: 	A failure is reported as an error.
     * <li>Warning: A failure is reported as warning, but it can still be free of composition errors.
     * <li>Ignore:  Won't test it atall!
     */
    void IntroductionConstraint(FOPImplementationInfo fopimpl){
        
        //Ignore flag set. Not test multiple introduction constraints
        if (INTRODUCTION_CONSTRAINT==SAFECOMP_IGNORE)
            return;
        
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            

            //for each method
            
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                
                //for each definition
                Vector definitions=new Vector();
                Iterator defIter = plmethod.definitionsIterator();
                while(defIter.hasNext()){
                    PLFile definitionsFileData = (PLFile)defIter.next();
                    definitions.add(definitionsFileData);
                }
                
                //To skip over larger constraints that blows up
                // I fixed this so that constraints are in CNF and wont blow up
                //if (definitions.size()>4)
                //    continue;
                
                //Only test where there are multiple introductions 
                if (definitions.size()>1){
                    
                    /* Sort vector of definitions alphabetically by coordinate.
                     * This is to ensure that, if there are same set of coordinates, they can be considered
                     * unique (string lookup in hash table) even if the vector of definitinos has the same 
                     * in different order E.g. <X, Y, Z> vs <Y, Z, X>.
                     */
                    Collections.sort(definitions, new PLFileCoordinateComparator());                    
                    
                    String constraint = no2Better(definitions);
                                       
                    String constType = "Multiple Introductions - Method overriding is possible";
                    String implDetails = "Method: "+plmethod.toString()+" in Class:"+plclass;
                    
                    String layerDetails = "  Layer: Defined in {";
                    for (int i=0;i<definitions.size();i++){
                        PLFile f = ((PLFile)definitions.get(i));
                        if (i>0)
                            layerDetails+=", ";
                        
                        layerDetails+=f.layer;
                    }
                    layerDetails+="}";
                    
                    
                    Vector coordinatesToSet = new Vector();// leave empty
                    m_numIntroductionConstraint++;
                    if (!tester.validateConstraint(constraint, coordinatesToSet, constType, implDetails, layerDetails))
                        m_numIntroductionConstraintFailures++;
                    
                
                }
                
                
                    
            }
            
 
        }    
    
    }
    
    
    String no2EvenBetter(Vector definingPLFiles){
        GUARD.ensure(definingPLFiles.size()>1, "Size must be >1");
        
        StringBuffer result=new StringBuffer("");
        
        for (int i=0;i<definingPLFiles.size();i++){
            
            if (i>0)
                result.append(" and ");
            
           // result.append("(");
            int c=0;
            Vector vectOfDisjunctions = new Vector();
            for (int j=0;j<definingPLFiles.size();j++){

                if (i!=j){
                    //if (c>0)
                     //   result.append(" or ");
                    vectOfDisjunctions.add(((PLFile)definingPLFiles.get(j)).position);
                    //result.append(((PLFile)definingPLFiles.get(j)).position.toString());
                	//c++;
                }
            }
            
            
            Coordinate firstCoordinate = (Coordinate)vectOfDisjunctions.get(0);
            int numDims = firstCoordinate.getNumDimensions();
            int nway = definingPLFiles.size()-1;
            
            GUARD.ensure(numDims>=1, "must have atleast 1 dimension");
            GUARD.ensure(nway>=1,"Must be atleast 1 way term");
            
            //initialize n set bins to 1 the lowes intex
            int[] bins = new int[nway];
            for (int b=0;b<nway;b++)
                bins[b]=1;
            
            int count=0;
            while (bins[0]<=numDims){
                
                /************************************/
                //testing: print bins
                
                //for (int b=0;b<nway;b++)
                //    System.err.print(bins[b]+" ");
                
                //System.err.println(" ");
                /************************************/
                
                
                if (count>0)
                    result.append(" and ");
                result.append("( ");
                for (int n=0;n<nway;n++){
                    if (n>0)
                        result.append(" or ");
                    
                    Coordinate coord = (Coordinate)vectOfDisjunctions.get(n);
                    result.append( (String)coord.get(bins[n]));
                }
                result.append(") ");
                
                
                //update the bins
                for(int b=(nway-1); b>=0;b--){
                    
                    bins[b]++;
                    
                    if (b!=0){ //dont reset first index it serves as the terminatino condition
	                    if (bins[b]>numDims)
	                        bins[b]=1;
	                    else
	                        break;
                    }
                }
                //bins should be set to corretct values for next iteration
                
                
                count++;
            }
            
            
            
            
           // result.append(")");
            
            
        }
        return result.toString();
    }
    
    /**
     * Generates formula for no2 constraint (think another way, negation of atmost1..)
     * If the input vector is A B C,
     * It will generate: (A or B) and (A or C) and (B or C)
     * Basically the rule of thumb is to leave out one in each disjunctive literal
     * 
     * @param definingPLFiles Vector of PLFile defining a method/field/class
     * @return String representing no2 constraint (without brackets)
     */
    String no2Better(Vector definingPLFiles){
        GUARD.ensure(definingPLFiles.size()>1, "Size must be >1");
        
        StringBuffer result=new StringBuffer("");
        
        for (int v=0;v<definingPLFiles.size();v++){
            result.append("let var"+v +" iff "+ ((PLFile)definingPLFiles.get(v)).position.toString()+" ; \n");
        }
        
        
        
        for (int i=0;i<definingPLFiles.size();i++){
            
            if (i>0)
                result.append(" and ");
            
            result.append("(");
            int c=0;
            for (int j=0;j<definingPLFiles.size();j++){

                if (i!=j){
                    if (c>0)
                        result.append(" or ");
                    result.append("var"+j);
                    
                    //result.append(((PLFile)definingPLFiles.get(j)).position.toString());
                	c++;
                }
            }
            result.append(")");
            
            
        }
        return result.toString();
    }

    /*
    String no2(Vector definingPLFiles){
        GUARD.ensure(definingPLFiles.size()>1, "Size must be >1");
        
        StringBuffer result=new StringBuffer("");
        
        for (int j=0;j<definingPLFiles.size();j++){
            if (j>0)
                result.append(" and ");
            result.append("(not "+((PLFile)definingPLFiles.get(j)).position.toString()+")");
        }
        result.append(" or ");
        
        result.append("not (choose1(");
        for (int i=0;i<definingPLFiles.size();i++){
            if (i>0)
                result.append(", ");
            result.append(((PLFile)definingPLFiles.get(i)).position.toString());
        }
        result.append("))");
        
      
        return result.toString();
        
    }
    */
    
    /**
     * Generates and tests refinement constraints given implementation details.
     */
    void RefinementConstraint(FOPImplementationInfo fopimpl){
        int numConstraints=0;
        //for each class
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            
            
            
            //for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                
                //for each refinement
                Iterator refIter = plmethod.refinementsIterator();
                while(refIter.hasNext()){
                    PLFile refinementFileData = (PLFile)refIter.next();
      
                    /*
                    System.out.println("Class:"+className+"  MethodID:"+methodID);
                    System.out.println("    Refined in:"+ refinementFileData.layer+" coordinate:"+ refinementFileData.position.toString());
                    */
                    Vector layerDefs=new Vector();
                    Vector<Coordinate> rightOrder = new Vector<Coordinate>();
                    String superConstraints = superDefs0(plmethod, plclass, fopimpl, layerDefs, rightOrder);
                    
                    // if there is one definition at the same level or above
                    if (!superConstraints.trim().equals("")){
 
                        String constraint = refinementFileData.position +" implies not("+superConstraints+")"; 
                        
                        // E.g. F=> A or B.. in the sat test F must be user-set to true
                        Vector coordinatesToSet = new Vector();
                        coordinatesToSet.add(refinementFileData.position);
                        
                        
                        String constType = "Method Refinement Dependency Unsatisfied - A refined method must be defined";
                        String implDetails = "Method: "+plmethod.toString()+" in Class:"+plclass;
                        
                        String layerDetails = "  Layer: Refined in "+refinementFileData.layer+" => {"+getStringSetFromVect(layerDefs)+"}";
                        
                        m_numRefinementConstraints++;
                        if (!tester.validateConstraint(constraint, coordinatesToSet,constType,implDetails, layerDetails ))
                            m_numRefinementConstraintsFailures++;
                           
                        
                        /**
                         * Now check Ranking
                         * left = refinementFileData.position
                         * right = 
                         */
                        Vector<Coordinate> leftOrder = new Vector<Coordinate>();
                        leftOrder.add(refinementFileData.position);
                        
                        if (!checkRank(leftOrder, rightOrder)){
                        //if (!checkRank(rightOrder, leftOrder)){

                                System.err.println(
                                        "\t Ordering Failure:  constraint:"+ constraint +"\n"+
                                                "\t"+implDetails);
                                System.err.println("--------------------------");
                                System.err.flush();
                        }

                        
                    }
                    
                }
            }
            
 
        }

        
    }
    
    
    
    void ReferenceFieldConstraint(FOPImplementationInfo fopimpl){
        int numConstraints=0;
        //for each class
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            
            
            
            //for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                
                //for each reference
                Iterator refIter = plmethod.fieldReferencesIterator();
                while(refIter.hasNext()){
                    
                    PLMethod.FileKeyPair pair = (PLMethod.FileKeyPair)refIter.next(); // key is of the form "className::type fieldName" 
                    String fieldKey = pair.key;
                        
                    String[] tmp = fieldKey.split("::");
                    String fieldsClassName = tmp[0];
                    String fieldId = tmp[1];
                    tmp = tmp[1].split(" ");
                    String fieldType = tmp[0];
                    String fieldName = tmp[1];
                    
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
                    
                    //Now generate the test.. F => D1 or D2 or D3..
                    //====== Build the constraint ======
                    String rightPart="";
                    String rightPartLayers="";
                    Iterator fieldDefsIter = field.definitionsIterator();
                    int i=0;
                    while(fieldDefsIter.hasNext()){
                        PLFile filedata = (PLFile)fieldDefsIter.next();
                        if (i>0){
                            rightPart += " or ";
                            rightPartLayers+=", ";
                        }
                        rightPart += filedata.position;
                        rightPartLayers +=filedata.layer;
                        i++;
                    }
                    String constraint = pair.file.position +" implies not("+rightPart+")"; 
                    //====== ==================== ======
                    
                    Vector coordinatesToSet = new Vector();
                    coordinatesToSet.add(pair.file.position);
                    
                    
                    
                    String constType = "Field Reference Dependency Unsatisfied - Referenced members must be defined";
                    String implDetails = "Method: '"+plmethod.toString()+"' in Class:"+plclass +" references class "+fieldsClass+"'s field '"+fieldName+"'";
                    
                    String layerDetails = "  Layer: "+pair.file.layer+" => {"+rightPartLayers+"}";
                    
                    m_numReferenceConstraints++;
                    numConstraints++;
                    if (!tester.validateConstraint(constraint, coordinatesToSet,constType,implDetails, layerDetails ))
                        m_numReferenceConstraintsFailures++;
                        
                    
                    //System.err.println(implDetails +" "+layerDetails);
                    
                } // field reference iterator
                
            }
            
 
        }

        //System.err.println("Number of field ref constraints:"+numConstraints);
        
    }
    
    void CheckMethodsNotInRightPlace(FOPImplementationInfo fopimpl){
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            
           
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
                     
                    
                    String[] tmp = fieldKey.split("::");
                    String methodsClassName = tmp[0];
                    String methodId = tmp[1];

                    
                    if (!fopimpl.getOODesigninfo().isProductLineClass(methodsClassName)){
                        continue;
                    }
                    
                    //find the class, then PLField, then layer
                    PLClass methodsClass = fopimpl.getClass(methodsClassName);
                    GUARD.ensure(methodsClass!=null, "Couldn't find method's class type");
                    
                    
                    //Referenced From
                    //pair.file.layer="";
                    
//                  Inherited fields can be referenced too.. so if they dont exist we need to look up
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
                    
                    Vector layerDefinitions=new Vector();
                    
                    //Now generate the test.. F => D1 or D2 or D3..
                    String superConstraints = superDefs0(method, currentClass, fopimpl, layerDefinitions, new Vector<Coordinate>()); //also looks at all the inherited (overridden) methods and generates superclass constraint for the method
                    
                    if (superConstraints.equals(""))
                        continue;
                    
                    
                    
                    for(int i=0;i<layerDefinitions.size();i++){
                        String def = (String)layerDefinitions.get(i);
                        if (def.equals(pair.file.layer)){
                            method.isSelfFeatureReferenced=true;
                            break;
                        }
                        
                    }
                    
                    
                } //for each references
                
                
            } //for each method
        }
        
        
        
        //loop again
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            
           
            //for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                if (! plmethod.isSelfFeatureReferenced){
                //if ( plmethod.isSelfFeatureReferenced){
                    System.out.print("Not Referenced by its Layer - Method:"+plmethod.toString() + " Class:"+plclass.toString()+" Defining Layers: ");
                    
                    Vector layerDefinitions=new Vector();
                    String superConstraints = superDefs0(plmethod, plclass, fopimpl, layerDefinitions, new Vector<Coordinate>()); //also looks at all the inherited (overridden) methods and generates superclass constraint for the method
                    if (layerDefinitions.size()>0)
                        System.out.print(layerDefinitions.get(0));
                    //for(int i=0;i<layerDefinitions.size();i++){
                    //    if(i!=0)
                    //        System.out.print(", ");
                    //    System.out.print(layerDefinitions.get(i));
                    //}
                    System.out.println("");
                    
                    
                }
            }
        }
        
        
    }
    
    
    void ReferenceMethodConstraint(FOPImplementationInfo fopimpl){
        int numConstraints=0;
        //for each class
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(className);
            
            
            
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
                     
                    
                    String[] tmp = fieldKey.split("::");
                    String methodsClassName = tmp[0];
                    String methodId = tmp[1];
                    //tmp = tmp[1].split(" ");
                    //String fieldType = tmp[0];
                    //String fieldName = tmp[1];
                    
                    
                    if (!fopimpl.getOODesigninfo().isProductLineClass(methodsClassName)){
                        //System.err.println("Skipping "+methodsClassName);
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
                    
                    Vector layerDefinitions=new Vector();
                    
                    //Now generate the test.. F => D1 or D2 or D3..
                    String superConstraints = superDefs0(method, currentClass, fopimpl, layerDefinitions, new Vector<Coordinate>()); //also looks at all the inherited (overridden) methods and generates superclass constraint for the method
                    
                    if (superConstraints.equals(""))
                        continue;
                    
                    String constraint = pair.file.position +" implies not("+superConstraints+")"; 
                    
                    
                    Vector coordinatesToSet = new Vector();
                    coordinatesToSet.add(pair.file.position);
                    
                    //System.err.println(constraint);
                    
                    String constType = "Method Reference Dependency Unsatisfied - Referenced members must be defined";
                    String implDetails = "Method: '"+plmethod.toString()+"' in Class:"+plclass +" references class "+methodsClass+"'s method '"+methodId+"'";
                    
                    String layerDetails = "  Layer: "+pair.file.layer+" => {"+getStringSetFromVect(layerDefinitions)+"}";
                    
                    m_numReferenceConstraints++;
                    numConstraints++;
                    if (!tester.validateConstraint(constraint, coordinatesToSet,constType,implDetails, layerDetails ))
                        m_numReferenceConstraintsFailures++;
                        
                    
                    //System.err.println(implDetails);
                    
                } // method reference iterator
                
            }
            
 
        }

        //System.err.println("Number of method ref constraints:"+numConstraints);
    }
    /**
     * For every method/class that references a class of the product line.
     * E.g. through parameter list (for methods) and field definitions where field is of a Type of the PL class (for classes) 
     * @param fopimpl
     */
    void ReferenceClassConstraint(FOPImplementationInfo fopimpl){
        int numConstraints=0;
        //for each class
        for(Iterator classIter = fopimpl.getListOfClasses().iterator(); classIter.hasNext(); )
        {
            String i_className = (String)classIter.next();
            PLClass plclass = fopimpl.getClass(i_className);
 
            //for each method
            for(Iterator methodIter = plclass.getListOfMethods().iterator(); methodIter.hasNext(); )
            {
                String methodID = (String)methodIter.next();
                PLMethod plmethod = plclass.getMethod(methodID);
                
                
                //for each reference to a class
                Iterator refIter = plmethod.classReferencesIterator();
                while(refIter.hasNext()){
                    
                    PLMethod.FileKeyPair pair = (PLMethod.FileKeyPair)refIter.next(); // key is of the form "className::type fieldName" 
                    String className = pair.key;              
                    
                    if (!fopimpl.getOODesigninfo().isProductLineClass(className)){
                        continue;
                    }
                    //find the class, then PLField, then layer
                    Vector layerdefs=new Vector();
                    String right = getClassDefsConstraintPart(className, fopimpl, layerdefs);
                    if (right.equals(""))
                        continue;

                    String constraint = pair.file.position +" implies not("+right+")"; 
                    
                    
                    Vector coordinatesToSet = new Vector();
                    coordinatesToSet.add(pair.file.position);
                    
                    //System.err.println(constraint);
                    
                    String constType = "Class Reference Dependency Unsatisfied - Referenced classes must be defined";
                    String implDetails = "Method: '"+plmethod.toString()+"' in Class:"+plclass +" references class "+className;
                    
                    String layerDetails = "  Layer: "+pair.file.layer+" => {"+getStringSetFromVect(layerdefs)+"}";
                    
                    m_numReferenceConstraints++;
                    numConstraints++;
                    if (!tester.validateConstraint(constraint, coordinatesToSet,constType,implDetails, layerDetails ))
                        m_numReferenceConstraintsFailures++;
                        
                    
                    
                    
                } // method reference iterator
                
            }
            
            /* Now, for each class loook at its fields' type and generate class constraint */
            for(Iterator fieldIter = plclass.getListOfFields().iterator(); fieldIter.hasNext(); )
            {
                String fieldID = (String)fieldIter.next(); //field id is combination of type and fieldName
                
                //If cant find the field.. maybe non PL fields are filtered whiel adding?
                if (!plclass.getHasField(fieldID))
                    continue;
                
                PLField plfield = plclass.getField(fieldID);
                
                /* If  type is not PL Class then dont bother
                 * E.g java.lang.String field;
                 */
                if (!fopimpl.getOODesigninfo().isProductLineClass(plfield.getType()))
                    continue;
                
                GUARD.ensure(plfield!=null, "field null");
                
                Vector layerDefs = new Vector();
                String right = getClassDefsConstraintPart(formatName(plfield.getType()), fopimpl, layerDefs);
                if (right.equals(""))
                    continue;

                Iterator defsIter = plfield.definitionsIterator();
                while (defsIter.hasNext()){
                    PLFile file = (PLFile)defsIter.next();
                    
                    String constraint = file.position +" implies not("+right+")"; 
                    
                    
                    Vector coordinatesToSet = new Vector();
                    coordinatesToSet.add(file.position);
                    
                    String constType = "Class Reference Dependency Unsatisfied - Referenced classes must be defined";
                    String implDetails = "Class:"+plclass +" references class "+plfield.getType()+" by introducing field "+ plfield.getFieldName();
                    
                    String layerDetails = "  Layer: "+file.layer+" => {"+getStringSetFromVect(layerDefs)+"}";
                    
                    m_numReferenceConstraints++;
                    numConstraints++;
                    if (!tester.validateConstraint(constraint, coordinatesToSet,constType,implDetails, layerDetails ))
                        m_numReferenceConstraintsFailures++;
                
                }
                
            } 
             
 
        }

        //System.err.println("Number of class ref constraints:"+numConstraints);
        
    }
    
    /**
     * From a class name it gets String constraint of the form "layer1 or layer2 or .. layer_n" where layers 1-n define the class.
     * @param className
     * @param fopimpl
     * @return
     */
    public String getClassDefsConstraintPart(String className, FOPImplementationInfo fopimpl, Vector layerDefs){
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
            layerDefs.add(file.layer);
            i++;
        }
        
        return rightSide;
    }
    
    //plmethod is not part of plclass but we want to fidn definitions in plclass or its superclasses
    String superDefsFindMethod(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl, Vector layerDefs){
        StringBuffer result= new StringBuffer("");
        
        //if same method exists in the given class
        if (plclass.getHasMethod(plmethod.toString())){
            PLMethod myMethod = plclass.getMethod(plmethod.toString());
            result.append(superDefsn(myMethod, plclass, fopimpl, layerDefs, new Vector<Coordinate>()));            
        } 
        else 
            result.append(goUpDefs(plmethod, plclass, fopimpl, layerDefs, new Vector<Coordinate>()));
        
        return result.toString();
     
     }
    
    /** Represents the first super0(m) call. This is differentiated 
     *  from nth call (where n>0) for obtaining statistical information only.
     **/
    String superDefs0(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl, Vector layerDefs, Vector<Coordinate> rightOrder){
        return superDefs(plmethod, plclass, fopimpl, true, layerDefs, rightOrder);
    }
    
    /** Represents the first supern(m) call, where n = 1,2,3.. **/
    String superDefsn(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl, Vector layerDefs, Vector<Coordinate> rightOrder){
        return superDefs(plmethod, plclass, fopimpl, false, layerDefs, rightOrder);
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
    String superDefs(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl, boolean first, Vector layerDefs, Vector<Coordinate> rightOrder){
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
           
           layerDefs.add(definingFileData.layer);
           rightOrder.add(definingFileData.position);
           i++;
       }

       String superConstraint = goUpDefs(plmethod, plclass, fopimpl,layerDefs, rightOrder).trim();
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
    String goUpDefs(PLMethod plmethod, PLClass plclass, FOPImplementationInfo fopimpl, Vector layerDefs, Vector<Coordinate> rightOrder){
        
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
                
                result.append(superDefsn(parentsMethod, parentPLClass, fopimpl, layerDefs, rightOrder));
                
                //Counts number of Super class constraints. If parent has it defined only then count
                //otherwise it may keep climbing up
                //if(parentsMethod.definitionsIterator().hasNext())
                   // m_numSuperRef++;
                
            } 
            else { //if plmethod does not exist in parent
                //keep going up, but pass in parent class
                //System.out.println("goUpDefs..");
                result.append(goUpDefs(plmethod, parentPLClass, fopimpl, layerDefs, rightOrder));
                
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
	    	    
	    	    if(path.getName().matches("[^\\$]++\\$++[0-9]++\\.class")){
	    	        //System.err.println("Skipping.. "+path.getName());
	    	        return;
	    	    }
	    	    
	    	    
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
					Element unit = ((Element)indexes.next()).getChild("unit");
					dimUnits.set(c, unit.getText());
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
				    
				    //if(candidate.getAttributeValue("name").matches("[^\\$]++\\$[0-9]++\\.class")){
				    if(candidate.getAttributeValue("name").matches("[^\\$]++\\$++[0-9]++\\.class")){
		    	        //System.err.println("Skipping.. "+candidate.getAttributeValue("name"));
		    	        //continue;
		    	    }
				    else {
				        //System.err.println(candidate.getAttributeValue("name"))
				        //System.err.println(candidate.getAttributeValue("location"));
				    }

				    
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
        DEBUG_OUTPUT=false;
        CHECKSELFREFERENCES=false;
        FLIP=true; //default is right to left (C B A), which I consider flipped.  -bg flag unflips it to left to right grammar (A B C)
        INTRODUCTION_CONSTRAINT=SAFECOMP_WARNING;
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

                if (args[i].equals("-f")||args[i].equals("-failures"))
                    OUTPUT_FAILURES=true; 
                else if (args[i].equals("-d") || args[i].equals("-debug"))
                    DEBUG_OUTPUT=true;
                else if (args[i].equals("-s")||args[i].equals("-stats"))
                    REPORT_STATS=true;
                else if (args[i].equals("-bg") || args[i].equals("-backgrammar"))
                    FLIP=false;
                else if (args[i].equals("-cmr") || args[i].equals("-checkmethodreferences"))
                    CHECKSELFREFERENCES=true;
                
                else if (args[i].equals("-ie"))
                    INTRODUCTION_CONSTRAINT=SAFECOMP_ERROR;
                else if (args[i].equals("-iw"))
                    INTRODUCTION_CONSTRAINT=SAFECOMP_WARNING;
                else if (args[i].equals("-ii"))
                    INTRODUCTION_CONSTRAINT=SAFECOMP_IGNORE;
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
            System.out.println("Usage: SafeCompositionValidator LINEAR_OR_DIM\n\n" +
            				   "  LINEAR_OR_DIM: OPTIONS* RootPath ModelFile  //linear model file\n" +
            				   "               | -md OPTIONS* PLMFile         //dimensional model" +
                               "\n");
            System.out.println("  OPTIONS: -f (-failures)\n" +
            				   "         | -s (-stats)\n" +
            				   "         | -ie \n" +
            				   "         | -iw \n" +
            				   "         | -ii \n" +
                               "         | -bg (-backgrammar)\n" +
                               "         | -cmr (-checkmethodreferences)\n" +
                               "         | -d (-debug)\n" +
            				   "\n" +
            				   "  Constraint: Choose1(-ie, -iw, -ii)");
            System.out.println("------------------------------------------------------------");
            System.out.println("  Options");
            System.out.println("  -f  Output Failure Details");
            System.out.println("  -s  Report Validation Statistics");
            System.out.println("  -ie Treat Introduction Constraints as Error");
            System.out.println("  -iw Treat Introduction Constraints as Warning");
            System.out.println("  -ii Ignore Introduction Constraints");
            System.out.println("  -bg Grammar elements are specified in backwards order Use -bg if grammar file is left to right (Base A B C)");
            System.out.println("  -cmr Checks for methods not being referenced by its defining Layer");
            System.out.println("  -d Prints Debuggin Info");
            System.out.println("");
            System.out.println("  RootPath: Path to dir of bc compiled project e.g. c:\\jjava\\$$");
            System.out.println("  ModelFile: Path to Model file e.g javamodel.m");
            System.out.println("  PLMFile: Path to PLM file e.g javamatrix.plm Note: path to model file and project root are provided within plm file, and can be relative path");
            System.out.println("------------------------------------------------------------");
            
            System.out.println("Example 1(Multidimensional model): java -cp 'guidsl.jar;jdom.jar;sat4j.jar;safegen.jar;ClassReader.jar;bcel-5.1.jar' \n" +
                    "    ProgramCube.Validation.SafeComposition.SafeCompositionValidator -md -s -ie -f -bg 'CubeServer\\java_no75_matrix_with_fullgrammar.plm'");
            
            System.out.println("Example 2 (Linear Model): java -cp 'guidsl.jar;jdom.jar;sat4j.jar;safegen.jar;ClassReader.jar;bcel-5.1.jar' \n" +
                    "    ProgramCube.Validation.SafeComposition.SafeCompositionValidator -s -ie -f -bg 'safegenTestcases\\$\\$' 'safegenTestcases\\$\\$\\testcase.m'");
            System.exit(0);
        }
        
        

        SafeCompositionValidator validator = new SafeCompositionValidator();
        
        if (!multidimensional)
            validator.validateLinearStandAlone(args[args.length-2], args[args.length-1]);
        else
            validator.validateDimensionalStandAlone(args[args.length-1]);
        
    }
    
    String getStringSetFromVect(Vector v){
        Iterator iter = v.iterator();
        StringBuffer sb = new StringBuffer();
        
        int i=0;
        while(iter.hasNext()){
            String str = (String)iter.next();
            
            if (i>0)
                sb.append(", ");
            
            sb.append(str);
            i++;
        }
        return sb.toString();
    }
    
    public class PLFileCoordinateComparator implements Comparator{
    	public int compare( Object o1, Object o2){
    		String pos1 = ((PLFile)o1).position.toString();
    		String pos2 = ((PLFile)o2).position.toString();
    		
    		return pos1.compareTo(pos2);
    	}
    }
    
    
    
    boolean checkRank(Vector<Coordinate> left, Vector<Coordinate> right){
        //boolean flip = false;
        boolean flip = FLIP; //just so it cant change programattically by another thread while being executed.. yeah right like thats going to happen!@
        
        int dimensions = (left.get(0)).getNumDimensions();
        
        for (int i=0;i< dimensions;i++){
            
            
            Vector<String> realLeft=new Vector<String>();
            Vector<String> realRight=new Vector<String>();
        
            for (int l=0;l<left.size();l++){
                realLeft.add(left.get(l).get(i+1));
            }
        
            for (int r=0;r< right.size();r++){
                realRight.add(right.get(r).get(i+1));
            }
            
            int leftrank = flip ? minRank(realLeft) : maxRank(realRight);
            int rightrank = flip ? maxRank(realRight) : minRank(realLeft);
            
            

            if (! (!flip && (leftrank >= rightrank)) || (flip && (leftrank <= rightrank))){
                
                System.err.println("Ranking constraint violated");
                
                System.err.println(realLeft + "<=" +realRight);
                System.err.println("Failed: "+getMaxRankLayer(realLeft)+" (" +leftrank + ") <= " +getMinRankLayer(realRight)+" ("+rightrank+")");
                return false;
            }
        
            
        }
            
        return true;    
    
    }
    //grammar.current.visit( new order() );
    int minRank(Vector tokens){
        int min = Integer.MAX_VALUE;
        
        for (int i=0;i<tokens.size();i++){
            
            findRank find = new findRank();
            find.setPrimitive((String)tokens.get(i));
            grammar.current.visit( find );
            
            int rank = find.getRank();
            
            if (rank<min)
                min=rank;
            
        }
        
        return min;
    }
    
    int maxRank(Vector tokens){
        int max = 0;
        
        for (int i=0;i<tokens.size();i++){
            findRank find = new findRank();
            find.setPrimitive((String)tokens.get(i));
            grammar.current.visit( find );
            int rank = find.getRank();
            
            if (rank>max)
                max=rank;
            
        }
        
        return max; 
    
    }
    String getMinRankLayer(Vector tokens){
        int min = Integer.MAX_VALUE;
        String layer="";
        for (int i=0;i<tokens.size();i++){
            
            findRank find = new findRank();
            find.setPrimitive((String)tokens.get(i));
            grammar.current.visit( find );
            int rank = find.getRank();
            
            if (rank<min){
                min=rank;
                layer=(String)tokens.get(i);
            }
            
        }
        
        return layer;
    }
    
    String getMaxRankLayer(Vector tokens){
        int max = 0;
        String layer="";
        for (int i=0;i<tokens.size();i++){
            findRank find = new findRank();
            find.setPrimitive((String)tokens.get(i));
            grammar.current.visit( find );
            int rank = find.getRank();
            
            if (rank>max){
                max=rank;
                layer=(String)tokens.get(i);
            }
            
        }
        
        return layer;
    }
    


}
