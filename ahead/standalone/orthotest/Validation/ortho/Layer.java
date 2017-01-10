/*
 * Created on Feb 27, 2005
 */
package ProgramCube.Validation.ortho;

import java.io.*;
import java.util.*;
import ClassReader.*;

/**
 * This class represents a feature in a product line.
 * 
 * @author hamid
 * @version 1.0
 */
public class Layer {
    
    /**
     * Name of the feature, currently same as the path
     */
    private String name;
    
    /**
     * Path(s) of the files containing the feature
     */
    private String paths;
    
    /**
     * Location in the n-dimensional matrix of product line model
     */
    private ArrayList point;

    /**
     * List of members (PLItems) defined and/or extended by this feature.
     */
    public ArrayList items;
    
    /**
     * 
     */
    public ArrayList refs;
    
    /**
     * @param n Name of the feature
     * @param p List of dimension coordinate names
     */
    public Layer(String n, ArrayList p) {
        super();
        name = n;
        paths = n;
        point = (ArrayList)p.clone();
        
        items = new ArrayList();
        refs = new ArrayList();
    }

    /**
     * @param n Name of the feature
     * @param dim Contains names of dimension coordinates 
     */
    public Layer(String n, String dim) {
        super();
        
        //System.out.println("Layer("+n+" ==> "+dim+")");
        
        name = n;
        paths = n;
        
        point = new ArrayList();
        
        items = new ArrayList();
        refs = new ArrayList();
        
        dim = dim.trim();
        while (!dim.equals("")) { // Reading coordinate
            int index = dim.indexOf(" ");
            if (index == -1) {
                point.add(dim);
                break;
            }
            else {
	            String token = dim.substring(0, index);
	            point.add(token);
	            dim = dim.substring(index);
            }
            dim = dim.trim();
        }
    }
    
    /**
     * Returns string representation of the feature
     * @return Feature name followed by matrix location
     */
    public String toString() {
        return point.toString(); 
    }
    
    public String toShortInfoString() {
        return point.toString() + " " + paths.toString(); 
    }
    
    public String toLongInfoString() {
        return point.toString() + "\n"
        	+ "  Path(s): " + paths.toString() + "\n"
        	+ "  Defines Items: \t" + items.size() + "\n"
        	+ "  Refines:      \t" + refs.size() + "\n";
    }
    
    /**
     * Gets the feature location in the matrix
     * @return The coordinates for the feature 
     */
    public ArrayList getLocation() {
        return point;
    }
    
    /**
     * Gets the feature location in a particular dimension
     * @param d The dimension number
     * @return The location name in the given dimension
     */
    public String getLocationInDimension(int d) {
        return (String)point.get(d);
    }

    /**
     * Checks two Layer objects for equality
     * @param o Object to check for equality
     * @return Whether the two objects are the same
     */
    public boolean equals(Object o) {
        if (o instanceof Layer) {
            Layer f = (Layer)o; 
            if (this.name.equals(f.name)
                    && this.point.size() == f.point.size()) {
                return this.point.toString().equals(f.point.toString());
            }
        }
        
        return false;
    }
    
    /**
     * 
     * @param rpath Root path for the matrix layers 
     * @throws Exception
     */
    public void readLayer(String rpath) throws Exception {
        String rname = "";
        
        String p = paths.trim();
        int spcIndex = 0;
        
        while (!p.equals("")) {
            spcIndex = p.indexOf(' ');
            
            if (spcIndex == -1) { // Last file/folder for the feature
                rname = p;
                p = "";
            }
            else { // One of the file(s)/folder(s) picked
                rname = p.substring(0, spcIndex);
                p = p.substring(spcIndex).trim();
            }

            //System.out.println(rpath + rname);
            File root = new File(rpath + rname);
    		// Traversing the entire directory tree or the file
            // based on the path retrieved
    		if (root.isDirectory())
    			readDirectory(root);
    		else if (root.getName().endsWith(".class"))
    			readFile(root);
    		else
    			throw new Exception("Unreadable feature file in the model: "+root.getAbsolutePath());
    		
    		//System.out.println(point.toString() + ": " + rpath + rname);
        }
		System.out.println(point.toString() + " done");
    }

    /**
     * Reads all files in a directory to retrieve feature items
     * @param root Root directory containing files with feature data
     */
	public void readDirectory(File root) {

		File[] files = root.listFiles();

		// Recursively reading classes in the directory structure
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
			    readDirectory(files[i]);
			else  if (files[i].getName().endsWith(".class"))
				readFile(files[i]);
		}
	}

	/**
	 * Reads a file to retrieve layers items
	 * @param data File containing layers data
	 */
	public void readFile(File data) {
		ClassInfo classdata = ClassReader.eval(data.getPath());
		
		
		PLItem pli, plr;
		
		FieldInfo[] fields = classdata.getFields();
		FieldInfo f;
		for (int i = 0; i < fields.length; i++) {
		    f = fields[i];
		    
		    pli = new PLItem(this, classdata.getName(), f.getName(),
		            f.getType(), "", false, false);
		    items.add(pli);
		}
		
		MethodInfo[] methods = classdata.getMethods();
		MethodInfo m;
		MethodInfo rm;
		FieldInfo rf;
		Object[] ref;
		for (int j = 0; j < methods.length; j++) {
		    m = methods[j];
		    
		    if (m.getName().equals("<init>".trim())){
		    	continue;
		    }
		    //System.err.println(m.getName());
		    	
		    	
		    String sign = "("; 
		    String[] args = m.getArgTypes();
		    for (int k = 0; k < args.length; k++) {
		        if (k == args.length - 1) sign += args[k];
		        else sign += args[k] + ", ";
		    }
		    sign += ")";
		    
		    pli = new PLItem(this, classdata.getName(), m.getName(), 
		            m.getReturnType(), sign, m.getIsRefined(), false);
		    items.add(pli);
		    
		    ref = m.getReferenceTable();
		    for (int l = 0; l < ref.length; l++) {
				if (ref[l] instanceof MethodInfo) { // Reference is a method
					rm = (MethodInfo)ref[l];
					
					if (rm.getName().equals("<init>"))
						continue;
					
					String sg = "(";
					String rarg[] = rm.getArgTypes();
				    for (int g = 0; g < rarg.length; g++) {
				        if (g == rarg.length - 1) sg += rarg[g];
				        else sg += rarg[g] + ", ";
				    }
					sg += ")";
					
					plr = new PLItem(this, rm.getClassName(), rm.getName(),
					        rm.getReturnType(), sg, rm.getIsRefined(), true);
					refs.add(plr);
				}
				else if (ref[l] instanceof FieldInfo) { // Reference is a field
					rf = (FieldInfo)ref[l];
					plr = new PLItem(this, rf.getClassName(), rf.getName(),
					        rf.getType(), "", false, true);
					refs.add(plr);
				}
		    }
		}
	}
	
	public ArrayList getCommonItems(ArrayList ilist) {
	    ArrayList common = new ArrayList();
	    
	    PLItem item1, item2;
	    for (int i = 0; i < items.size(); i++) {
	        item1 = (PLItem)items.get(i);
	        
	        for (int j = 0; j < ilist.size(); j++) {
	            item2 = (PLItem)ilist.get(j);
	            
	            if (item1.keyString().equals(item2.keyString())) {
	                common.add(item1);
	            }
	        }
	    }
	    
	    return common;
	}
	
	public String getPaths(){
		return paths;
	}
}
