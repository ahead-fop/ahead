package composer.algebra ;

/**
 * A <code>Signature</code> is a key used to access a {@link Unit} within a
 * {@link Collective}.  For example, if a given {@link Collective} represents a
 * Java class containing methods as {@link Unit} objects, then the
 * <code>Signature</code> objects should combine the method name <em>and</em>
 * the parameter types in sequence.
 *
 * <p>
 * <code>Signature</code> objects are analogous to keys used in a hash table
 * and will often be used in this way by long-lived {@link Collective} objects.
 * This suggests that <code>Signature</code> implementations be immutable and
 * that they satisfy the recommendations for {@link Object#equals(Object)} and
 * {@link Object#hashCode()}.
 *
 * <p>
 * For {@link Collective} objects that provide a sorted view of their contents,
 * <code>Signature</code> implementations should also implement
 * {@link Comparable}.
 **/
public interface Signature {

    public boolean equals (Object object) ;

    public int hashCode () ;

}
