/*
 * Created on Apr 8, 2005
 */
package ProgramCube.Validation.ortho;

import java.util.*;

/**
 * @author hamid
 */
public class ConflictHashtable {

    /**
     * 
     */
    private ArrayList[] table;
    /**
     * 
     */
    private PLMatrix matrix;
    
    /**
     * 
     * @param m
     */
    public ConflictHashtable(PLMatrix m) {
        super();
        
        matrix = m;
        
        int size = 67;
        
        table = new ArrayList[size];
        for (int i = 0; i < table.length; i++) {
            table[i] = new ArrayList();
        }
    }

    /**
     * 
     * @param size
     * @param m
     */
    public ConflictHashtable(int size, PLMatrix m) {
        super();

        matrix = m;
        
        table = new ArrayList[size];
        for (int i = 0; i < table.length; i++) {
            table[i] = new ArrayList();
        }
    }
    
    /**
     * 
     * @param pli
     * @return x**xxxxxxxxxxxxxxx*x*x****
     * @throws Exception
     */
    public ArrayList addItemAndGetConflicts(PLItem pli) throws Exception {
        
        String key = pli.keyString();
        int loc = Math.abs(key.hashCode()) % table.length;
        ArrayList conflicts = new ArrayList();
        
        for (int i = 0; i < table[loc].size(); i++) {
            PLItem p = (PLItem)table[loc].get(i);
            if (key.equals(p.keyString())) {
                if (matrix.locConflict(pli.container, p.container) != null) {
                    if(!conflicts.contains(p.container))
                        conflicts.add(p.container);
                }
            }
        }
        table[loc].add(pli);
        return conflicts;
    }
    
    public ArrayList getConflicts(PLItem pli) throws Exception {

        String key = pli.keyString();
        int loc = Math.abs(key.hashCode()) % table.length;
        ArrayList conflicts = new ArrayList();
        
        for (int i = 0; i < table[loc].size(); i++) {
            PLItem p = (PLItem)table[loc].get(i);
            if (key.equals(p.keyString())) {
                if (matrix.locConflict(pli.container, p.container) != null) {
                    if(!conflicts.contains(p.container))
                        conflicts.add(p.container);
                }
            }
        }
        return conflicts;
    }
}
