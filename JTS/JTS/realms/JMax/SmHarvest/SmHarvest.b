// SmHarvest grammar
// JEDI project
// Computer Sciences Department
// University of Texas at Austin
// Last update: November 7, 2002

// Note: This grammar is the concatenation of smempty and mixinSm
//       as defined for the mixin and unmixin tool
// --- mixinSm

// state machine DSL grammar

"State_machine" STATE_MACHINE
"States"	STATES
"Delivery_parameters" DELIVERY
"Transition"		EDGE
"condition"	CONDITION
"->" 		ARROW
"Exit"		EXIT
"Enter"		ENTER
"Prepare"       PREPARE
"Otherwise"     OTHERWISE
"Otherwise_default" OTHERWISE_DEFAULT 
"Unrecognizable_state" UNRECOGNIZABLE_STATE
"Nested_state"  NESTED_STATE
"Goto_state"    GOTO_STATE
"Proceed"       PROCEED
"Transition_condition"     EDGETEST
"Transition_action"   EDGEACTION

// previously defined productions appearing on the right-hand side

require AST_Exp ;
require AST_FieldDecl ;
require AST_Modifiers ;
require AST_ParList ;
require AST_QualifiedName ;
require AST_TypeNameList ;
require AllocationExpression ;
require Arguments ;
require Block ;
require ExtendsClause ;
require ImplementsClause ;
require QName ;

// productions that are being extended

require ClassBodyDeclaration ;
require PrimaryPrefix ;
require UnmodifiedTypeDeclaration ;
require Statement ;


// grammar extension

UnmodifiedTypeDeclaration
	: SmDeclaration
	;

SmDeclaration
	: STATE_MACHINE QName [ SmExtendsClause ] 
          [ ImplementsClause ] SmClassBody 	:: UmodSmDecl
	;

SmExtendsClause
	: LOOKAHEAD(2) ExtendsClause                           :: SmExtends
	| LOOKAHEAD(2) "extends" "class" AST_QualifiedName     :: SmClsExtends
	;

SmClassBody
	: "{" [ RootClause ] [ OtherwiseClauses ] [ StatesList ] [ ESList ] 
  	  [ AST_FieldDecl ] "}" 		:: SmClassDecl
	;

StatesList
	: (StatesClause)+
	;

StatesClause
	: STATES AST_TypeNameList ";"		:: StatesDecl
	| NESTED_STATE QName ":" AllocationExpression ";"  :: NStateDecl
	;

RootClause
	: DelivClause [ NoTransitionClause ] 	:: RootDecl
	;

NoTransitionClause
	: UNRECOGNIZABLE_STATE Block		:: NoTransDecl
	;

DelivClause
	: DELIVERY "(" AST_ParList ")" ";" 	:: DelivDecl
	;

OtherwiseClauses
	: ( OtherwiseClause )+
        ;

OtherwiseClause
        : OTHERWISE_DEFAULT Block               :: ODefaultDecl
        ;

ESList	: ( Es )+
	;

Es	
	: EXIT QName Block			:: ExitDecl
	| ENTER QName Block			:: EnterDecl
	| PREPARE QName Block			:: PrepareDecl
        | EDGETEST QName AST_Exp ";"            :: TestDecl
        | EDGEACTION QName Block                :: ActionDecl
	| EDGE QName ":" StartName ARROW QName
	  CONDITION AST_Exp DO Block 		:: TransitionDecl
        | OTHERWISE QName Block                 :: OtherDecl
	;

StartName 
	: QName					:: SmSName
	| "*"					:: StarName
	;

Statement
	: GOTO_STATE QName Arguments		:: GotoState
	;

PrimaryPrefix
	: PROCEED Arguments			:: ProceedDecl
	;

// the following is used to allow Sm declarations to be nested within
// class declarations; this is useful if we want to include Sm specifications
// within layers

NestedSmDeclaration
	: [ AST_Modifiers ] SmDeclaration	:: NSmDecl
	;

ClassBodyDeclaration
	: LOOKAHEAD( [ AST_Modifiers() ] "state_machine" )
            NestedSmDeclaration	
        ;
