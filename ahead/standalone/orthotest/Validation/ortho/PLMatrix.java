/*
 * Created on Feb 27, 2005
 */
package ProgramCube.Validation.ortho;

import java.util.*;
import java.io.*;
import org.jdom.*;

/**
 * This class represents an entire product line model.
 * It currently supports building of the matrix from a given
 * <a href="http://www.cs.utexas.edu/users/hamid/work/matrix.plm">.plm</a>
 * file. It also provides function for retrieving a feature given its location
 * in the n-dimensional matrix.
 * Soon, the functions for matrix partitioning will be added that will allow
 * restrictions on various dimensions etc.
 * 
 * @author hamid
 * @version 1.0
 */

/*
 * dims
 * [ [entries]  <-- dimension, each entry is a feature of that dimensional model
 * 	 [entries]	<-- dimension
 *   [entries]	<-- dimension
 * 		]
 * 
 * dims.size() == number of dimensions
 * 
 * @author Sahil
 *
 */
public class PLMatrix {
    
    /**
     * Contains root path for the layers in use
     */
     public String rootpath;
    
    /**
     * Contains information about the dimensional model of the product line
     */
     private ArrayList dims;
    
    /**
     * Actual list of layers in the entire product line, not used for
     * accessing, only for storage
     */
     private ArrayList layers;
    
    /**
     * n-dimensional matrix using n hash tables, all the feature access
     * is done through this matrix
     */
     private PLHashtable[] matrix;

    /**
     * Hash table that is used to determine the conflicts in the product
     * line represented by the matrix.
     *  
     */
     private ConflictHashtable chash;

    /**
     * 
     */
     private ArrayList memConflicts;
    /**
     * 
     */
      private ArrayList refConflicts;

    /**
     * Constructs only the dimensional model and an empty matrix
     * @param ds Dimension model for the matrix
     */
    
    public PLMatrix(ArrayList ds) {
        super();
        
        dims = (ArrayList)ds.clone();
        layers = new ArrayList();
        
        matrix = new PLHashtable[dims.size()];
        for (int x = 0; x < dims.size(); x++) {
            matrix[x] = new PLHashtable((ArrayList)dims.get(x));
        }
        
        memConflicts = new ArrayList();
        refConflicts = new ArrayList();
    }
    

    
    public PLMatrix(Element cube) throws Exception {
    	//create a new pl matrix by parsing xml tree instead of plain file
        dims = new ArrayList();
        layers = new ArrayList();

        memConflicts = new ArrayList();
        refConflicts = new ArrayList();
        
        //first add all dimensions and units
        Element dimensions = cube.getChild("dimensions");
        List axes = dimensions.getChildren("axis");
        Iterator axIt = axes.iterator();
        
        while (axIt.hasNext()){
        	ArrayList unitsArray = new ArrayList();
        	
        	Element axis = (Element)axIt.next();
        	List units = axis.getChildren("unit");
        	Iterator unitIt = units.iterator();
        	while(unitIt.hasNext()){
        		Element unit = (Element)unitIt.next();
        		unitsArray.add(unit.getAttributeValue("name"));
        	}
        	
        	dims.add(unitsArray);
        }
        
        //now add the layers
        Element features = cube.getChild("features");
        List featureList = features.getChildren("feature");
        Iterator fetIt = featureList.iterator();
        
        while (fetIt.hasNext()){
        	Element feature = (Element)fetIt.next();
        	String coordinate="";
        	
        	List indexes = feature.getChildren("index");
        	Iterator indexIt = indexes.iterator();
        	int i=0;
        	while(indexIt.hasNext()){
        		Element index = (Element)indexIt.next();
        		
        		if(i==0)
        			coordinate += index.getChild("unit").getText().trim();
        		else
        			coordinate += " " + index.getChild("unit").getText().trim();
        		
				++i;
        	}
        	
        	

        	if (rootpath==null || rootpath.equals("")){
        		Element file = feature.getChild("layers").getChild("file");
            	String fileLoc = file.getAttributeValue("location");
        		rootpath = fileLoc.substring(0, fileLoc.indexOf(feature.getAttributeValue("name")));
        	}
			
        	//System.out.println("rootpath="+rootpath);
        	//System.out.println(feature.getAttributeValue("name")+", "+coordinate);
        	//System.exit(0);
            Layer layer = new Layer(feature.getAttributeValue("name"), coordinate);
            layer.readLayer(rootpath);
            layers.add(layer);
        }
        

        
        // Adding the layers to the actual matrix of d-dimensions
        // represented as a set of d hash tables
        matrix = new PLHashtable[dims.size()];
        for (int d = 0; d < dims.size(); d++) {
            matrix[d] = new PLHashtable((ArrayList)dims.get(d));
            for (int i = 0; i < layers.size(); i++) {
            	Layer layer = (Layer)layers.get(i);
                
                String loc = layer.getLocationInDimension(d);
                matrix[d].add(loc, layer);
            }
        }   
        
    }
    
    /**
     * @param filename Name/path of the file to be read
     * @exception In case the file is not found.
     * 
     * uses members:
     * 	dims, matrix, layers
     * 
     * initializes:
     * 	dims
     * 	layers
     *  matrix
     * 	memConflicts
     * 	refConflicts
     *  rootpath
     */
    public PLMatrix(String filename) throws Exception {
        super();
        
        
        dims = new ArrayList();
        layers = new ArrayList();

        memConflicts = new ArrayList();
        refConflicts = new ArrayList();

        File f = new File(filename);
        FileReader fr = new FileReader(f);
        
        BufferedReader buf = new BufferedReader(fr);
        String lineOfData, token;
        int section = 0, index = 0;
        ArrayList entry;
        
        Layer layer;
        
        while ((lineOfData = buf.readLine()) != null) { // Reading the model
            
        	lineOfData = lineOfData.trim(); // Removing redundant whitespace
            
            if (lineOfData.startsWith("'")) { ; } // Comment
	        else if (lineOfData.startsWith("%%")) { // New section started
	            section++;
	        }
	        else if (lineOfData.equals("")) { ; } // Empty line
			else {
			    switch (section) {
		        	case 0: // Reading the root path
		        	    rootpath = lineOfData;
		        	    break;
		            
				    case 1: // Reading all the dimensions
				        entry = new ArrayList();
				        
				        while (!lineOfData.equals("")) { // Reading dimension
				            index = lineOfData.indexOf(" ");
				            if (index == -1) {
				                entry.add(lineOfData);
				                break;
				            }
				            else {
					            token = lineOfData.substring(0, index);
					            entry.add(token);
					            lineOfData = lineOfData.substring(index);
				            }
				            lineOfData = lineOfData.trim();
				        }
				        dims.add(entry);
				        break;
			        case 2:
			            
			            index = lineOfData.indexOf(":");
			            if (index != -1) {
			                token = lineOfData.substring(0, index).trim();
			                lineOfData = lineOfData.substring(index + 1).trim();
			                layer = new Layer(lineOfData, token);
			                layer.readLayer(rootpath);
			                layers.add(layer);
			            }
			            break;
			            
			        default:
			            // trouble
			    }
			}
        }
        buf.close();
        fr.close();
        
        String loc;

        // Adding the layers to the actual matrix of d-dimensions
        // represented as a set of d hash tables
        matrix = new PLHashtable[dims.size()];
        for (int d = 0; d < dims.size(); d++) {
            matrix[d] = new PLHashtable((ArrayList)dims.get(d));
            for (int i = 0; i < layers.size(); i++) {
            	layer = (Layer)layers.get(i);
                
                loc = layer.getLocationInDimension(d);
                matrix[d].add(loc, layer);
            }
        }
    }
    
    /**
     * Gets the feature present at the given location in the n-D matrix.
     * @param loc The coordinates in n-dimensions for the feature required
     * @return The feature present at the given location, null in case there
     * are duplicates or none present, or if the location provided is invalid
     * @exception If any location in any dimension is invalid
     */
    public Layer getFeature(String loc) throws Exception {
        
        ArrayList lst = new ArrayList();
        int index, dim = 0;
        String locd;
        while (!loc.equals("")) { // Reading dimension
            index = loc.indexOf(" ");
            if (index == -1) {
	            if (dim == 0) lst = matrix[dim].getAll(loc);
	            else lst = matrix[dim].filterByKey(loc, lst);
                break;
            }
            else {
	            locd = loc.substring(0, index);
	            loc = loc.substring(index);
	            
	            if (dim == 0) lst = matrix[dim].getAll(locd);
	            else lst = matrix[dim].filterByKey(locd, lst);
            }
            loc = loc.trim();
            dim++;
        }
        
        if (lst != null && lst.size() == 1) return (Layer)lst.get(0);
        return null; // If none found, or more than one found
    }
    
    /**
     * 
     * @param filename
     * @return x*x*x*x*xx*x*x*x*x*x*x*x*x*x*x
     * @throws Exception
     */
    public PLMatrix getSubMatrixForFile(String filename) throws Exception {

        ArrayList subdims = new ArrayList();
        
        File f = new File(filename);
        FileReader fr = new FileReader(f);
        BufferedReader buf = new BufferedReader(fr);

        String data, token;
        int section = 0, index = 0;
        ArrayList elist;
        
        while ((data = buf.readLine()) != null) { // Reading the model
            
            data = data.trim(); // Removing redundant whitespace
            
            if (data.startsWith("'")) { ; } // Comment
	        else if (data.startsWith("%%")) { // New section started
	            section++;
	        }
	        else if (data.equals("")) { ; } // Empty line
			else {
			    switch (section) {
	        	case 0: // Reading the root path
	        	    rootpath = data;
	        	    break;
	            
			    case 1: // Reading all the dimensions
			        elist = new ArrayList();
			        
			        while (!data.equals("")) { // Reading dimension
			            index = data.indexOf(" ");
			            if (index == -1) {
			                elist.add(data);
			                break;
			            }
			            else {
				            token = data.substring(0, index);
				            elist.add(token);
				            data = data.substring(index);
			            }
			            data = data.trim();
			        }
			        subdims.add(elist);
			        break;
			    }
			}
        }
        buf.close();
        fr.close();
        
        return getSubMatrixWithDims(subdims);
    }
    
    /**
     * Constructs and gets a projected/filtered part of the matrix by
     * the help of given dimensional model
     * @param subdims The dimensional model to project
     * @return The submatrix with only the given dimensional model
     * @exception In case of dimensional model mismatch between the actual 
     * matrix and the provided model
     */
    public PLMatrix getSubMatrixWithDims(ArrayList subdims) throws Exception {
        // Checking the dimensions to see if the submatrix can be created
        if (subdims.size() != dims.size()) return null;
        for (int ds = 0; ds < subdims.size(); ds++) {
            ArrayList subdim = (ArrayList)subdims.get(ds);
            ArrayList dim = (ArrayList)dims.get(ds);
            for (int d = 0; d < subdim.size(); d++) {
                if (!dim.contains(subdim.get(d))) {
                    // Dimensions mismatch
                    throw new Exception("Dimension mismatch"); 
                }
            }
        }
        
        PLMatrix sub = new PLMatrix(subdims);
        String tdim;
        
        for (int i = 0; i < layers.size(); i++) {
            Layer f = (Layer)layers.get(i);
            boolean present = true;
        
            for (int d = 0; d < sub.dims.size(); d++) {
                tdim = f.getLocationInDimension(d);
            
                // Filtering out the unneeded matrix elements
                if (!((ArrayList)sub.dims.get(d)).contains(tdim)) {
                    present = false;
                    break;
                }
            }
            if (present) {
                sub.layers.add(f);

                // Adding the feature to the new submatrix
                for (int j = 0; j < sub.matrix.length; j++) {
                    tdim = f.getLocationInDimension(j);
                    sub.matrix[j].add(tdim, f);
                }
            }
        }
        
        return sub; // Returning the required submatrix
    }
    
    /**
     * Checks if the two layers are in conflicting quadrants with respect
     * to any pair of dimensions. 
     * @param f1 First feature for test
     * @param f2 Second feature for test
     * @return String containing the identifier of first dimension pair 
     * found in case the layers conflict in one or more pairs of dimensions:
     * e.g "0,1" if conflict is in dim0-dim1 pair, returns null in case there
     * is no such conflict
     * @exception Non-existent feature passed to the function as f1 or f2
     */
    public String locConflict(Layer f1, Layer f2) throws Exception {
    
        // Check if layers are valid
        if (!(layers.contains(f1) && layers.contains(f2))) {
            // One or both layers do not exist
            throw new Exception("Layer does not exist");
        }
        
        int f1locd1, f1locd2, f2locd1, f2locd2;
        ArrayList dim1l, dim2l;
        
        // Checking each pair of dimensions for conflicts
        for (int d1 = 0; d1 < dims.size(); d1++) {
            dim1l = (ArrayList)dims.get(d1);
            // Getting the locations of layers in dimension d1
            f1locd1 = dim1l.indexOf(f1.getLocationInDimension(d1));
            f2locd1 = dim1l.indexOf(f2.getLocationInDimension(d1));
            
            for (int d2 = d1 + 1; d2 < dims.size(); d2++) {
                dim2l = (ArrayList)dims.get(d2);
                // Getting the locations of layers in dimension d2
                f1locd2 = dim2l.indexOf(f1.getLocationInDimension(d2));
                f2locd2 = dim2l.indexOf(f2.getLocationInDimension(d2));
                
                // Checking for the quadrant of each in the pair of dimensions
                if (f1locd1 > f2locd1 && f1locd2 < f2locd2) {
                    return (d1 + "," + d2); 
                }
                else if (f1locd1 < f2locd1 && f1locd2 > f2locd2) {
                    return (d1 + "," + d2); 
                }
            }
        }
        return null;
    }

    /**
     * 
     * @throws Exception
     */
    public void calcMemConflicts() throws Exception {

        memConflicts.clear();
        chash = new ConflictHashtable(this);
        
        Layer plf;
        PLItem pli;
        ArrayList plcs;
        Layer[] pair;
        ArrayList inpair = new ArrayList();
        
        for (int i = 0; i < layers.size(); i++) {
            plf = (Layer)layers.get(i);
            inpair.clear();
            
            for (int j = 0; j < plf.items.size(); j++) {
                pli = (PLItem)plf.items.get(j);
                plcs = chash.addItemAndGetConflicts(pli);
                
                for (int k = 0; k < plcs.size(); k++) {
                    pair = new Layer[]{plf, (Layer)plcs.get(k)};
                    if (!inpair.contains(pair[1])) {
                        memConflicts.add(pair);
                        inpair.add(pair[1]);
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @return xyz
     */
    public ArrayList getMemConflictList() {
        return memConflicts;
    }
    
    /**
     * 
     * @throws Exception
     */
    public void calcRefConflicts() throws Exception {
        if (chash == null) throw new Exception("Member hashtable not present");
        
        refConflicts.clear();
        
        Layer plf;
        PLItem pli;
        ArrayList plcs;
        Layer[] pair;
        ArrayList inpair = new ArrayList();
        
        for (int i = 0; i < layers.size(); i++) {
            plf = (Layer)layers.get(i);
            inpair.clear();
            for (int j = 0; j < plf.refs.size(); j++) {
                pli = (PLItem)plf.refs.get(j);
                plcs = chash.getConflicts(pli);
                
                for (int k = 0; k < plcs.size(); k++) {
                    pair = new Layer[]{plf, (Layer)plcs.get(k)};
                    if (!inpair.contains(pair[1])) {
                        refConflicts.add(pair);
                        inpair.add(pair[1]);
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @return xyz
     */
    public ArrayList getRefConflictList() {
        return refConflicts;
    }
    
    /**
     * 
     *
     */
    public void printLayers() {
        for (int i = 0; i < layers.size(); i++)
            System.out.println(
                    ((Layer)layers.get(i)).toLongInfoString());
    }
    
    /**
     * 
     * @param filename
     */
    public void logLayers(String filename) {

        try {
            FileWriter fout = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fout);

            for (int i = 0; i < layers.size(); i++) {
                out.write(((Layer)layers.get(i)).toLongInfoString()
                        + "\n");
            }

            out.close();
        }
        catch (IOException e) {
        	e.printStackTrace();
            System.out.println("I Cannot create file for logLayers");
        }    
    }

    public void logFeatureDetails(String filename) {

        try {
            FileWriter fout = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fout);

            Layer plf;
            for (int i = 0; i < layers.size(); i++) {
                plf = (Layer)layers.get(i);
                out.write(plf.toShortInfoString() + "\n");
                
                for (int j = 0; j < plf.items.size(); j++)
                    out.write("DEFINES \t" + plf.items.get(j) + "\n");

                for (int k = 0; k < plf.refs.size(); k++)
                    out.write("REFERENCES \t" + plf.refs.get(k) + "\n");
                
                out.write("---\n");
            }

            out.close();
        }
        catch (IOException e) {
            System.out.println("Cannot create file for logFeatureDetails");
        }    
    }

    public void logConflicts(String filename) {
        
        try {
            FileWriter fout = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fout);

            Layer[] pair;
            
            for (int i = 0; i < memConflicts.size(); i++) {
                pair = (Layer[])memConflicts.get(i);
                out.write("Matrix Cell: \t");
                out.write(pair[0].toString() + " <CONFLICTS WITH> "
                        + pair[1].toString() + "\n");
                out.write("   Layer: \t\t");out.write(pair[0].getPaths() + " <CONFLICTS WITH> "
                        + pair[1].getPaths() + "\n");
                
                
                out.write("---------------------------------------\n");
            }

            out.close();
        }
        catch (IOException e) {
            System.out.println("Cannot create file for logConflicts");
        }    
    }

    

    public void logConflictDetails(String filename) {
        
        try {
            FileWriter fout = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fout);

            Layer[] pair;
            ArrayList confs;
            
            for (int i = 0; i < memConflicts.size(); i++) {
                pair = (Layer[])memConflicts.get(i);
                
                out.write("Point: \t"+pair[0].toString() + " <CONFLICTS WITH> " + pair[1].toString() + "\n");
                out.write(" =layer \t"+pair[0].getPaths() + " <CONFLICTS WITH> "+ pair[1].getPaths() + "\n");
                out.write("\n");
                
                confs = pair[0].getCommonItems(pair[1].items);
                for (int j = 0; j < confs.size(); j++) {
                    out.write("DEFINES " + ((PLItem)confs.get(j)).container.getPaths() + ((PLItem)confs.get(j)).keyString() + "\n");
                }
                
                confs = pair[0].getCommonItems(pair[1].refs);
                for (int k = 0; k < confs.size(); k++) {
                    out.write("REFERENCES " + ((PLItem)confs.get(k)).keyString() + "\n");
                }
                
                out.write("---------------------------------------\n");
            }

            out.close();
        }
        catch (IOException e) {
            System.out.println("Cannot create file for logConflictDetails");
        }    
    }
}
