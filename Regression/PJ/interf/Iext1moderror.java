layer Iext0;

refines interface MyInt extends java.io.Serializable {
    new void foo() throws AnotherException;
    new void barrr();
    overrides int biff( int i );
}
