"msc"       MESSAGE_DIAGRAM
"loop"      LOOP
"exclusive" EXCLUSIVE
"axis"      AXIS
"message"   MESSAGE
"->"        ARROW
"Base"      BASE


UnmodifiedTypeDeclaration
      : MSCDeclaration                          :: MSCDecl
      ;

MSCDeclaration 
      : MESSAGE_DIAGRAM QName [ ExtendsClause ] [ ImplementsClause ] MSCBody
                                                :: UmodMSCDecl
      ;

MSCBody
      : "{" [ AxisClause ] MSCFieldList "}"     :: MSCBodyDecl
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

/*      
MSCField      
      : ABSTRACT QName ";"                      :: AbstFieldDecl
      | LOOKAHEAD(2) QName "{" EntryList "}"    :: NrmFieldDecl
      ;
*/

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
      : QName ":" Block	                        :: ActionDecl
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
      | [BranchLabel] Condition BREAK ";"	:: BreakDecl
      ;

BranchLabel
      : "<" QName ":" QName ">"
                                                :: LabelDecl
      ;