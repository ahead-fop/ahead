// sm2 or "state machine" adds constructs to define state machines


"state_machine" STATE_MACHINE
"states"	STATES
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
	: SDDeclaration
	;

SDDeclaration
	: STATE_MACHINE QName [ RefinesClause ] 
          [ ImplementsClause ] SDClassBody 	:: UmodSdDecl
	;

RefinesClause
	: LOOKAHEAD(2)
	  "extends" AST_QualifiedName		:: RefinesDecl
	| LOOKAHEAD(2) 
          "extends" "class" AST_QualifiedName	:: ExtendsClassDecl
	;

SDClassBody
	: "{" [ RootClause ] [ OtherwiseClause ] [ StatesList ] [ ESList ] 
  	  [ AST_FieldDecl ] "}" 		:: SdClassDecl
	;

StatesList
	: (StatesClause)+
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
	| EDGE QName ":" StartName ARROW QName
	  CONDITIONS AST_Exp DO Block 		:: EdgeDecl
        | OTHERWISE QName Block                 :: OtherDecl
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
	: LOOKAHEAD( [ AST_Modifiers() ] "state_machine" )
            NestedSDDeclaration	
        ;
