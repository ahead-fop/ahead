layer Iext0;

refines interface MyInt extends java.io.Serializable {
    overrides void foo() throws AnotherException;
    overrides void barrr();
    new int biff( int i );
    overrides float biff();
}
