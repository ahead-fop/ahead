
public class test1 {
    // nested top-level class
    static public class nested {
	int i;
    }

    // static initializer
    int i; 	{ i = 5; }

    // final with declaration
    final int x;

    // nested member class
    public class nestedMember {

	// final with parameter
	public nestedMember(final int y) {
	    // new 'this' syntax
	    this.i = test1.this.i;
	}
    }

    // nested interface
    public interface nestedInterface {
	public void goAway();
    }

    // anonymous classes
    public Enumeration getEnum() {
	return new Enumeration() {
	    int x = 5;
	};
    }

    public void main(String[] args) {
	// new 'new' syntax
	Object obj = test1.new nestedMember();

	// anonymous arrays
	int[] a;
	a = new int[] {1, 2, 3};

	// local class
	class localClass {
	    int i;
	}
    }
}
