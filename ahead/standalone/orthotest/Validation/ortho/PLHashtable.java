/*
 * Created on Feb 27, 2005
 */
package ProgramCube.Validation.ortho;

import java.util.*;

/**
 * This class represents a hash table of layers that is used to
 * index layers according to a single dimension of the matrix.
 * 
 * @author hamid
 * @version 1.0
 */
public class PLHashtable {
    
    /**
     * Stores the references to layers
     */
    private ArrayList[] table;
    
    /**
     * Possible dimension keys for the hash table
     */
    private ArrayList keys;

    /**
     * @param k List of possible dimension keys
     */
    public PLHashtable(ArrayList k) {
        super();
        
        keys = (ArrayList)k.clone();
        
        table = new ArrayList[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            table[i] = new ArrayList();
        }
    }

    /**
     * Add a feature element into the dimension hash table
     * @param key The dimension coordinate for addition
     * @param f The feature element to add
     * @return Result of addition, 1 if success, 0 if already present
     * and -1 in case of invalid key
     */
    public int add(String key, Layer f) {
        
        int pos = keys.indexOf(key);
        if (pos == -1) return -1; // Invalid key
        
        int fpos = table[pos].indexOf(f);
        if (fpos == -1) {
            table[pos].add(f);
            //System.out.println("ADD: " + key + " - " + f );
            return 1; // Successful add
        }
        return 0; // Duplicate add attempt, not allowed
    }
    
    /**
     * Get all the feature elements corresponding to a coordinate value
     * in a single dimension
     * @param key The dimension coordinate for retrieval
     * @return All the elements corresponding to the coordinate,
     * returns null in case of invalid key
     */
    public ArrayList getAll(String key) {
        int pos = keys.indexOf(key);
        if (pos == -1) return null; // Invalid key
        
        return (ArrayList)table[pos].clone();
    }

    /**
     * Filter out all the layers from the given list not present at 
     * the given location in the dimension
     * @param key The dimension coordinate for retrieval
     * @param flist The layers to check for presence in the given location
     * @return The layers from the given list that were found at the given 
     * location, returns empty list in case none found, or null in case of 
     * invalid key
     */
    public ArrayList filterByKey(String key, ArrayList flist) throws Exception {
        int pos = keys.indexOf(key);
        if (pos == -1) 
            throw new Exception("No such location in dimension");
        
        ArrayList temp = new ArrayList();
        Layer f;
        for (int i = 0; i < flist.size(); i++) {
            f = (Layer)flist.get(i);
            if (table[pos].contains(f)) {
                temp.add(f);
            }
        }
        
        return temp;
    }
}
