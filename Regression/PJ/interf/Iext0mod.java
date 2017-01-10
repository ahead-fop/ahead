layer Iext0;

refines interface MyInt extends java.io.Serializable {
    overrides void foo() throws SomeException;
    new void barrr();
}
