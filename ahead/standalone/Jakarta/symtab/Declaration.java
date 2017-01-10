package Jakarta.symtab;

public interface Declaration extends Named {
    public Scope getDeclaringEnv();
    public void setDeclaringEnv(Scope declEnv);
    public Object getUserData();
    public void setUserData(Object data);
    public CompilationUnit getCompilationUnit();
}
