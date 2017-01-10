// sm or "state diagram" adds constructs to define state machines


"state_diagram" STATE_DIAGRAM
"states"	STATES
"refines"       REFINES
"event_delivery" EVENT_DELIVERY
"edge"		EDGE
"conditions"	CONDITIONS
"->" 		ARROW
"Exit"		EXIT
"Enter"		ENTER
"otherwise"     OTHERWISE
"otherwise_default" OTHERWISE_DEFAULT 
"no_transition" NO_TRANSITION

// the following hooks SDDeclaration with class, interface declarations

UnmodifiedTypeDeclaration
	: SDDeclaration				:: SDDecl
	;

SDDeclaration
	: STATE_DIAGRAM QName [ RefinesClause ] [ ExtendsClause ] 
          [ ImplementsClause ] SDClassBody 	:: UmodSdDecl
	;

RefinesClause
	: REFINES AST_QualifiedName		:: RefinesDecl
	;

SDClassBody
	: "{" [ RootClause ] [ OtherwiseClause ] [ StatesClause ] [ ESList ] 
  	  [ AST_FieldDecl ] "}" 		:: SdClassDecl
	;

StatesClause
	: STATES AST_TypeNameList ";"		:: StatesDecl
	;

RootClause
	: DelivClause NoTransitionClause	:: RootDecl
	;

NoTransitionClause
	: NO_TRANSITION Block			:: NoTransDecl
	;

DelivClause
	: EVENT_DELIVERY QName "(" AST_ParList ")" ";"
						:: DelivDecl
	;

OtherwiseClause
        : OTHERWISE_DEFAULT Block               :: ODefaultDecl
        ;

ESList	: ( Es )+
	;

Es	
	: EXIT QName Block			:: ExitDecl
	| ENTER QName Block			:: EnterDecl
	| [Refines] EDGE QName ":" StartName ARROW QName
	  CONDITIONS AST_Exp DO Block 		:: EdgeDecl
        | OTHERWISE QName Block                 :: OtherDecl
	;

Refines : REFINES				:: RefinesMod
        ;

StartName 
	: QName					:: SdSName
	| "*"					:: StarName
	;


// the following is used to allow SD declarations to be nested within
// class declarations; this is useful if we want to include SD specifications
// within layers

NestedSDDeclaration
	: [ AST_Modifiers ] SDDeclaration	:: NSDDecl
	;

ClassBodyDeclaration
	: LOOKAHEAD( [ AST_Modifiers() ] "state_diagram" )
            NestedSDDeclaration	
        ;
