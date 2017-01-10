"msc"       MESSAGE_DIAGRAM
"loop"      LOOP
"exclusive" EXCLUSIVE
"axis"      AXIS
"message"   MESSAGE
// "->"        ARROW
"Base"      BASE


UnmodifiedTypeDeclaration
      : MSCDeclaration                          :: MSCDecl
      ;

UnmodifiedTypeExtension
      : MESSAGE_DIAGRAM QName [ ImplementsClause ] MSCBody 
                                                :: UmodMSCExt
      ;

MSCDeclaration 
      : MESSAGE_DIAGRAM QName [ ExtendsClause ] [ ImplementsClause ] MSCBody
                                                :: UmodMSCDecl
      ;

MSCBody
      : "{" [ AxisClause ] MSCFieldList [ AxisSmDeclList ] "}"    
                                                :: MSCBodyDecl
      ;

AxisClause
      : AXIS AxisList ";"                       :: AxisClauseDecl
      ;

AxisList
      : AxisDeclaration ( "," AxisDeclaration )*
      ;
      
AxisDeclaration
      : QName [ AxisExtends ]                   :: AxisDecl
      ;
      
AxisExtends
      : "extends" AST_QualifiedName             :: AxisExtendsDecl
      ;
      
MSCFieldList
      : ( MSCField ) +
      ;

MSCField      
      : [ AST_Modifiers ] QName "{" [ EntryList ] "}"    
                                                :: MSCFieldDecl
      ;

EntryList
      : ( Entry )+
      ;

Entry
      : MESSAGE QName ":" QName ARROW QName ";"	:: MsgDecl 
      | LOOKAHEAD(2) AST_QualifiedName ";"      :: SubMSCField  
      | LOOKAHEAD(2) BASE "." QName ";"         :: BaseSubMSCField      
      | LocalAction
      | LoopBlock					 
      | BranchBlock	
      | "{" [ EntryList ] "}"                   :: SubBlockDecl		
      ;
 
LocalAction
      : QName ":" Block	                        :: LocalActionDecl
      ;

BranchBlock
      : [BranchLabel] EXCLUSIVE "{" BranchList "}"
                                                :: BranchDecl
      ;

BranchList
      : ( ConditionalEntry )+				
      ;

ConditionalEntry
      : Condition Entry                         :: CondEntryDecl
      ;

Condition
      : "[" QName ":" Expression "]"	        :: ConditionDecl
      ;

LoopBlock
      : LOOP "{" LoopList "}"                   :: LoopDecl
      ;

LoopList
      : ( LoopEntry )+	
      ;

LoopEntry
      : LOOKAHEAD(6) Entry			
      | [BranchLabel] Condition BREAK ";"       :: BreakDecl
      ;

BranchLabel
      : "<" QName ":" QName ">"                 :: LabelDecl
      ;
      
AxisSmDeclList
      : ( AxisSmDeclaration )+
      ;
      
AxisSmDeclaration
      : AXIS QName SmClassBody                  :: AxisSmDecl
      ;