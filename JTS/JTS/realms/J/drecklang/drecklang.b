"and_expr"		AND_EXPR
"conditions"		CONDITIONS
"explain_expr"		EXPLAIN_EXPR
"example_teqn"          EXAMPLE_TEQN
"domain"		DOMAIN 
"requires"		REQUIRES
"exports"		EXPORTS
"above"			ABOVE
"below"			BELOW
"merge"			MERGE
"realm"			REALM

AST_Program
	: Domain [ AST_Imports ] Realms AttributeList [ Cond ] 
          ComponentList	 [ Exampl ]            :: DRC_prog
	;


Domain	
	: DOMAIN AST_QualifiedName ";" 		:: DomainDef
	;

Attribute
	: AST_TypeName QName [ AttName ] ";"	:: AttrDef
	;

AttName
	: STRING_LITERAL			:: AttrName
	;

AttributeList
	: ( LOOKAHEAD(2) Attribute )+
	;

Cond
	: CONDITIONS "(" ")"  "{" [ AST_Stmt ] "}"	:: CondStmt
	;

Realms
	: REALM AST_TypeNameList ";"		:: RealmsDef
	;

Component
	: QName "[" [ ParameterList ] "]" ":" 
          QName "{" [ PropertyList ] [ AST_FieldDecl ] "}" 
                                                :: CompDef
	;

ComponentList
	: ( Component )+
	;

Parameter
	: QName ":" AST_TypeName :: ParamDef
	;

ParameterList
	: Parameter ( "," Parameter )*
	;
	
PropertyList
	: ( Property )+	
	;

Property
	: REQUIRES Requir				:: RequirDecl
	| EXPORTS Expor					:: ExporDecl
	| MERGE "(" ")" "{" [ AST_Stmt ] "}"		:: Merge
	| AND_EXPR Expression 
          EXPLAIN_EXPR Expression ";"			:: Explain
	;

Expor
	: BELOW QName "{" [ AST_Stmt ] "}"		:: Postcondition
	| ABOVE "{" [ AST_Stmt ] "}"			:: Postrestriction
        ;

Requir
	: ABOVE Expression ";"				:: Precondition
	| BELOW QName Expression ";"			:: Prerestriction
        ;

Exampl
	: EXAMPLE_TEQN STRING_LITERAL ";"	        :: ExampleTeqn
	;
