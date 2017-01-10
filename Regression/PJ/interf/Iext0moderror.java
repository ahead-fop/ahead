layer Iext0;

refines interface MyInt extends java.io.Serializable {
    new void foo() throws SomeException;
    overrides void barrr();
}
