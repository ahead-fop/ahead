package composer.algebra ;

import java.util.List ;

public interface AlgebraFactory {

    /**
     * Returns a {@link Function} object to acting on a <code>sources</code>
     * {@link List}.
     *
     * <p>
     * <em>Note:</em> It should be possible for this method to be implemented
     * by defining a {@link Collective} of {@link Function} objects and
     * constructing a composite {@link Signature} from the <code>sources</code>
     * and, optionally, the <code>target</code>.
     **/
    public Function getFunction (String name, List sources)
    throws AlgebraException ;

    public Unit getUnit (Object object) throws AlgebraException ;

}
