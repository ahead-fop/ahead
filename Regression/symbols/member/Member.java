
package TestPak;

import java.util.Enumeration;

public class Member {
    private Member head = null;

    public Enumeration enum() { return(new Enumerator()); }

    private class Enumerator implements Enumeration {
	Member current;

	private class Enumerator2 {
	    int dummy;
	}

	public Enumerator() { current = head; }

	public boolean hasMoreElements() { return (current != null); }
	public Object nextElement() {
	    return(current);
	}
    }
}
