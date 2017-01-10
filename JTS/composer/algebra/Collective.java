package composer.algebra ;

import java.util.Set ;

public interface Collective extends Unit {

    public Set getSignatures () ;

    public Unit getUnit (Signature signature) throws AlgebraException ;

}
