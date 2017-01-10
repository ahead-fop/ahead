package Jakarta.collection ;

import java.util.Collection ;
import java.util.Iterator ;

public interface Filter {

    public boolean acceptObject (Object object) ;

    public boolean accept (Object object) ;

    public Collection collection (Collection inp, Collection out) ;

    public Collection collection (Collection inp)
    throws IllegalAccessException, InstantiationException ;

    public Iterator iterator (Iterator baseIterator) ;

    public Iterator iterator (Collection baseCollection) ;

}
