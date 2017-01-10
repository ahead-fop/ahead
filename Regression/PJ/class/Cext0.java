layer myaspect;

public static refines class MyInt implements java.io.Serializable {
    void foo() throws SomeException { Super().foo(); }
    SomeType bar( int i ) { Super(int).bar(i); }
}
