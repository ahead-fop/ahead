package Jakarta.symtab;

public interface Scope extends Named {
    public Scope getDeclaringEnv();
    public void addDeclaration(Declaration decl);
    public void expunge();
    public CompilationUnit getCompilationUnit();
}
