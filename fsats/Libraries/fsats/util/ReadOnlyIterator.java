package fsats.util;



public class ReadOnlyIterator implements java.util.Iterator
{
    java.util.Iterator iterator;

    public ReadOnlyIterator( java.util.Iterator iterator )
    {
        this.iterator = iterator;
    }

    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    public Object next()
    {
        return iterator.next();
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
