package Jakarta.symtab ;

import java.io.PrintWriter ;

public interface Named extends Comparable {
    public String getFullName () ;
    public String getName () ;
    public void dump (PrintWriter writer, int indentLevel) ;
}
