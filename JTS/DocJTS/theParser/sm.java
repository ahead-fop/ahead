
		package theParser; import
	java.util.Vector; import
	Jakarta.util.FixDosOutputStream; import
        Jakarta.util.Util; import
        java.io.*; import
        java.util.*; import
        jtsdoc.*; 
        import CommentParsing.*;

public class sm extends layers
{

  static public class UnmodifiedTypeDeclaration extends layers.UnmodifiedTypeDeclaration {
   }

   static public class SDDecl extends layers.SDDecl {
	   public void execute() 
	   {
		   super.execute(); 
	   }
   }

   /** SDDeclaration
    * STATE_DIAGRAM QName [ RefinesClause ] [ ExtendsClause ] 
    * [ ImplementsClause ] SDClassBody	:: UmodSdDecl
    */
   static public class UmodSdDecl extends layers.UmodSdDecl {
	   StateDiagramDocJTS _harvest_state;
	   
  	   public void execute() 
	   {
		   // Modifiers come before this
		   // Up node is  Lang.NSDDecl
		   // $TEqn.AstNode upperNode = up;
		   // if (upperNode != null) System.out.println("Up Name " + up.className());
		   
		   // State name
		   _harvest_state = new StateDiagramDocJTS(arg[0].toString());
		   
		   // System.out.println("State Name : " + arg[0].toString());
		   super.execute(); 
		   
		   // [RefinesClause]
		   Lang.AstNode e1 = arg[1].arg[0];
		   if (e1 != null) 
		   { 
			   String refinesName = getAST_QualifiedName((Lang.AST_QualifiedName)e1.arg[0]);
			   _harvest_state.setRefines(refinesName);
		   }
		   
		   // [ExtendsClause]	  
		   // Sets the extends arg2 is OptNode so we get the first child
		   Lang.AstNode e2 = arg[2].arg[0];
		   if (e2 != null)
		   {
   			   String extendsName = getAST_QualifiedName((Lang.AST_QualifiedName)e2.arg[0]);
			  _harvest_state.setExtendsName(extendsName);
		   }		   
		   
		   
		   // [ImplementsClase]
		   Lang.AstNode   e3 = arg[3].arg[0];
		   
		   // If there are implemented interfaces then added them
		   if (e3!=null) 
		   {
			   List implementsInterfaces = ((ImplClause)e3).implementedInterfaces;
			   _harvest_state.setImplementsList(implementsInterfaces);
		   }
		   else // otherwise it is empty (same as above here)
		   {  _harvest_state.setImplementsList(new LinkedList());	
		   }
		   
		   // SDClassBody	   
		   
		   // Sets the methods for the class
		   _harvest_state.setRootClauseName(((SdClassDecl)arg[4]).rootClauseName);
		   _harvest_state.setParameters(((SdClassDecl)arg[4]).parameters);
	       _harvest_state.setNoTransitionClauseClasses(((SdClassDecl)arg[4]).noTransitionClauseClasses);
	       _harvest_state.setOtherwiseClauseClasses(((SdClassDecl)arg[4]).otherwiseClauseClasses);
	       _harvest_state.setStatesClauseStates(((SdClassDecl)arg[4]).statesClauseStates);	   

		   // ESList information
	       _harvest_state.setExitStates(((SdClassDecl)arg[4]).ExitStates);
	       _harvest_state.setEnterStates(((SdClassDecl)arg[4]).EnterStates);
	       _harvest_state.setEdgeStates(((SdClassDecl)arg[4]).EdgeStates);
	       _harvest_state.setOtherwiseStates(((SdClassDecl)arg[4]).OtherwiseStates);		   
	   
		   // AST_FieldDecl information
		 
		   _harvest_state.setMethods(((SdClassDecl)arg[4]).listOfMethods);
		   _harvest_state.setInnerClasses(((SdClassDecl)arg[4]).listOfInnerClasses);
		   _harvest_state.setConstructors(((SdClassDecl)arg[4]).listOfConstructors);
		   _harvest_state.setFields(((SdClassDecl)arg[4]).listOfFields);
		   _harvest_state.setStates(((SdClassDecl)arg[4]).listOfStates);
         
		   DOCLETJTS.Lang.ClassDocJTS theClass = ((SdClassDecl)arg[4])._sd_class;
		   theClass.setName("state" +(_harvest_state.name()).trim());
		   _harvest_state.setClassBody(theClass);
		   
		   // pending outer class handling
		   // outerClasses.add(theClass);
		   
		   // ************ Comment Processing ************************************
		   String comment = ((Lang.AstToken) tok[0]).white_space;
		   CommentJTS stateComment = 
				CommentParser.ParserCommentState(_harvest_state, comment);
           _harvest_state.setComment(stateComment);		   
       
	   } // execute
   }
   
   // Takes care of updating the modifiers of the class
   static public void setModifiers(NSDDecl element)
   {
	    //System.out.println("In set modifiers ");
		
        Lang.AstCursor c = new AstCursor();  // c to traverse list
        Lang.AstNode   e = element.arg[0].arg[0];  // e instanceOf Lang.AstList		
	    
		StateDiagramDocJTS state = ((UmodSdDecl)element.arg[1])._harvest_state;
		
		// System.out.println("Checking modifiers Name" + state.name());
	    if (e != null && state !=null ) 
		{ // if there are modifiers and it is a class
            for (c.FirstElement(e); c.MoreElement(); c.NextElement()) {

				// abstract method
			    if (c.node instanceof ModAbstract) { state.setAbstract(true);}
			
			    // final method
			    if (c.node instanceof ModFinal) { state.setFinal(true); }
			
			    // public method
			    if (c.node instanceof ModPublic) { state.setPublic(true); }
			
			    // protected method
			    if (c.node instanceof ModProtected) { state.setProtected(true); }
			
			    // private method
			    if (c.node instanceof ModPrivate) { state.setPrivate(true); }
			
			    // static method
			    if (c.node instanceof ModStatic) { state.setStatic(true); }
			
			    // these 4 I dont know if have to be accesed 
				// transient method
			    if (c.node instanceof ModTransient)	{ state.setTransient(true); }
			
			    // volatile method
			    if (c.node instanceof ModVolatile) { state.setVolatile(true); }
			
			    // native method
			    if (c.node instanceof ModNative) { state.setNative(true);	}
			
			    // synchronized method
			    if (c.node instanceof ModSynchronized) { state.setSynchronized(true);	}
		      } // for all modifiers
			
		    } // if there are modifiers				   
	
   } // of setModifiers
     
   // Unchanged
   static public class RefinesClause extends layers.RefinesClause {
   }

   // Unchanged
   static public class RefinesDecl extends layers.RefinesDecl {
   }

   static public class SDClassBody extends layers.SDClassBody {
   }

/** SDClassBody
	"{" [ RootClause ] [ OtherwiseClause ] [ StatesClause ] 
        [ ESList ] [ AST_FieldDecl ] "}"	:: SdClassDecl
 */   
   static public class SdClassDecl extends layers.SdClassDecl 
   {
	   public String rootClauseName="";
	   public ParameterJTS[] parameters = new ParameterJTS[0];
	   public LinkedList noTransitionClauseClasses = new LinkedList();
	   public LinkedList otherwiseClauseClasses = new LinkedList();
	   public LinkedList statesClauseStates = new LinkedList();
	   
	   // information from [ESList] 
	   public LinkedList  ExitStates = new LinkedList();
	   public LinkedList  EnterStates = new LinkedList();
	   public LinkedList EdgeStates = new LinkedList();
	   public LinkedList OtherwiseStates = new LinkedList();	   

	  // These lists are for the AST_FieldDecl part
       public   LinkedList listOfMethods = new LinkedList();
	   public   LinkedList listOfInnerClasses = new LinkedList();
	   public   LinkedList listOfConstructors = new LinkedList();
	   public   LinkedList listOfFields = new LinkedList();
	   public   LinkedList listOfStates = new LinkedList(); 
	   
	   // Class that contains the AST_FieldDecl part
	   public DOCLETJTS.Lang.ClassDocJTS _sd_class;
	   
	   public void execute() 
	   {    
	
		    
		   super.execute(); 
		   _sd_class = new DOCLETJTS.Lang.ClassDocJTS();
		   
		   // **** [RootClause]
		   Lang.AstNode e0 = arg[0].arg[0];
		   if (e0 != null) // there is a root clause
		   {
			   // e0.arg[0] is DelivClause, e0.arg[0].arg[0] is Qname
			   rootClauseName = e0.arg[0].arg[0].toString();
			   // e0.arg[0].arg[1] is AST_ParList
			   parameters = ((AST_ParList)(e0.arg[0].arg[1])).parametersArray;
			   
			   // e0.arg[1] is NoTransDecl, e0.arg[1].arg[0] is BlockC
			   // /System.out.println("Block " + e0.arg[1].arg[0].className());
		       Lang.AstNode e00 = e0.arg[1].arg[0].arg[0];
			   if (e00 != null)
			   {				   
				   noTransitionClauseClasses = parseBlock(e00.arg[0]);
			   } // if there are statements in the block	   
		   
		   } // if there is a root clause
		   
		   // **** [OtherwiseClause]
		   Lang.AstNode e1 = arg[1].arg[0];
		   if (e1 != null) // there is an otherwise clause
		   {
			   // e1 is ODefaultDecl
			   // e1.arg[0] Block
			   Lang.AstNode e10 = e1.arg[0].arg[0];
			   if (e10 != null)  // there are elements in the block
			   {
				   otherwiseClauseClasses = parseBlock(e10.arg[0]);				   
			   } // if there are elements in the block
			   
		   } // if there is an otherwise clause
		   
		   // **** [StatesClause]
		  Lang.AstCursor c2= new AstCursor();  // c to traverse list
          Lang.AstNode   e2= arg[2].arg[0];    
		  
		  // arg[2] is AST_OptNode
		  // arg[2].arg[0] is StatesDecl
		  // arg[2].arg[0].arg[0] is AST_TypeNameList <- pass to the cursor
		  // arg[2].arg[0].arg[0].arg[0] is Lang.AST_TypeNameListElem
		  
		  if (e2!= null) // if there are modifiers
		  {
           for (c2.FirstElement(e2.arg[0]); c2.MoreElement(); c2.NextElement()) {	
			 // TNClass -> QNameType -> AST_QualifiedName
			 // adds to the statesClause states list
			 statesClauseStates.add(c2.node.arg[0].arg[0].toString());
		   } // for
		  } // if there are implements interfaces
		  
		   // **** [ESList]
		   // arg[3] => Lang.AstOptNode
           // arg[3].arg[0] => Lang.ESList
           // arg[3].arg[0].arg[0] => Lang.ESListElem
		   
		   Lang.AstCursor c3 = new AstCursor();       	   
		   Lang.AstNode   e3 = arg[3].arg[0];  // e instanceOf Lang.AstList		 
		   if (e3 != null) 
		   { 		   	   
	         for (c3.FirstElement(e3); c3.MoreElement(); c3.NextElement()) 
	         { 
				 // System.out.println("State type " + c3.node.className());
				 if (c3.node instanceof ExitDecl)
					 ExitStates.add(((ExitDecl)c3.node).exit_state);
				 if (c3.node instanceof EnterDecl)
					 EnterStates.add(((EnterDecl)c3.node).enter_state);
				 if (c3.node instanceof EdgeDecl)
					 EdgeStates.add(((EdgeDecl)c3.node).edge_state);
				 if (c3.node instanceof OtherDecl)
					 OtherwiseStates.add(((OtherDecl)c3.node).other_state);
		     } // for all the states
		   } // if there are states
		   
		   // Sets the list of the states in the 
		   
		   // **** [AST_FieldDecl]
		   // AST_FieldDecl -> (ClassBodyDeclaration)+
		   // We can use the same approach here to collect the data
		   // in the same fashion as in UmodClassDecl
		   
		  // Traverses the tree of AST_FieldDecl to get the methods, nestedclasses,
		  // interfaces, etc
		   Lang.AstCursor c4 = new AstCursor();       // c to traverse list
		   // arg[4] is AST_FieldDecl, arg[0] is the OptNode, arg[0] is the first element

		   Lang.AstNode   e4 = arg[4].arg[0];  // e instanceOf Lang.AstList		 
		   if (e4 != null) 
		   { 		   	   
	       for (c4.FirstElement(e4); c4.MoreElement(); c4.NextElement()) 
	       { 
			   
			  if (c4.node instanceof MethodDcl)
			  {
				  MethodDocJTS met = ((MthDector)c4.node.arg[2])._harvest_method;
				  listOfMethods.add(met);
			  }
			  
			  if (c4.node instanceof NestedClassDeclaration)
			  {
				 DOCLETJTS.Lang.ClassDocJTS cls = ((UmodClassDecl)c4.node.arg[1])._harvest_class;				 
				 listOfInnerClasses.add(cls);
			  }
			  
			  if (c4.node instanceof NestedInterfaceDeclaration)
			  {
				 DOCLETJTS.Lang.ClassDocJTS cls = ((UmInterDecl)c4.node.arg[1])._harvest_class;
				 listOfInnerClasses.add(cls);
			  }
			  
			  if (c4.node instanceof ConDecl)
			  {
				  ConstructorDocJTS cons = ((ConDecl)c4.node)._harvest_constructor;
				  listOfConstructors.add(cons);
			  }
			  
			  if (c4.node instanceof FldVarDec)
			  { 
				  LinkedList tempList = ((FldVarDec)c4.node).fieldsList;
				  for(int i=0; i< tempList.size(); i++)
				  {
					  listOfFields.add((FieldDocJTS)tempList.get(i));
				  }
				  // fieldsList
			  }

			  if (c4.node instanceof NSDDecl)
			  {
				  StateDiagramDocJTS theState = ((NSDDecl)c4.node)._harvest_state;
				  listOfStates.add(theState);
			  }
			  
		   } // end of for
		  } // if there members of the class
		   
        // what about default constructors? probably not needed		   
		   _sd_class.setMethods(listOfMethods);
		   _sd_class.setInnerClasses(listOfInnerClasses);
		   _sd_class.setInterfaces(new LinkedList());
		   _sd_class.setConstructors(listOfConstructors);
		   _sd_class.setFields(listOfFields);		  
		   
	   }  // of execute
	   
   } // of SdClassDecl

   static public class StatesClause extends layers.StatesClause {
	    public void execute() { super.execute(); }
   }

   static public class StatesDecl extends layers.StatesDecl {
  public void execute() { super.execute(); }
   }

   static public class RootClause extends layers.RootClause {
	    public void execute() { super.execute(); }
   }

   static public class RootDecl extends layers.RootDecl {
	    public void execute() { super.execute(); }
   }

   static public class NoTransitionClause extends layers.NoTransitionClause {
	    public void execute() { super.execute(); }
   }

   static public class NoTransDecl extends layers.NoTransDecl {
	    public void execute() { super.execute(); }
   }

   static public class DelivClause extends layers.DelivClause {
	    public void execute() { super.execute(); }
   }

   static public class DelivDecl extends layers.DelivDecl {
	    public void execute() { super.execute(); }
   }

   static public class OtherwiseClause extends layers.OtherwiseClause {
   }

   static public class ODefaultDecl extends layers.ODefaultDecl {
   }

   static public class ESList extends layers.ESList {
   }

   static public class Es extends layers.Es {
   }

   /** Saves exit states.
    * EXIT QName Block			
    */
   static public class ExitDecl extends layers.ExitDecl 
   {
	   public StateDocJTS exit_state;
	   
	   public void execute() 
	   {
		   
		   
		  // QName
		   String _harvest_state_name = arg[0].toString();
		  
		   // System.out.println("Exit " + _harvest_state_name);
		   
		   
		   super.execute(); 
		   LinkedList listOfClasses = new LinkedList();
		   
		  // Block is arg[1]  
		  Lang.AstNode e0 = arg[1].arg[0];
    	  if (e0 != null)
	      {
			 listOfClasses = parseBlock(e0.arg[0]);			
		  } // if there are statements in the block	   		   	   
		   
		  exit_state = new StateDocJTS(_harvest_state_name, listOfClasses);
	   } // of execute
	   
   }

   static public class EnterDecl extends layers.EnterDecl 
   {
	   public StateDocJTS enter_state;
	   
	   public void execute() 
	   { 
		  // QName
		   String _harvest_state_name = arg[0].toString();
		   // System.out.println("Enter " + _harvest_state_name);
		   
		   super.execute(); 
		   LinkedList listOfClasses = new LinkedList();
		   
		  // Block is arg[1]  
		  Lang.AstNode e0 = arg[1].arg[0];		   
		  if (e0 != null)  // if there is something in the block
		  {  listOfClasses = parseBlock(e0.arg[0]); }
		  
		  enter_state = new StateDocJTS(_harvest_state_name, listOfClasses);
	   } // of execute

   }

  /**
   * [Refines] EDGE QName ":" StartName ARROW QName
	  CONDITIONS AST_Exp DO Block 		
   */
   static public class EdgeDecl extends layers.EdgeDecl 
   {
	   public EdgeStateDocJTS edge_state;
	   
	   public void execute() 
	   { 
  		   super.execute(); 

		  // [Refines] is arg[0]
		  boolean refines = false;
		  if (arg[0].arg[0] != null) refines = true;
		  
		  // QName is arg[1]
		   String _harvest_state_name = arg[1].toString();

   		   // System.out.println("Edge " + _harvest_state_name);

		   
		  // StartName is arg[2]
		   String _harvest_start_name = arg[2].toString();
		   
		  // EndName is arg[3]
		   String _harvest_end_name = arg[3].toString();
		  
		  // Conditions is arg[4]
		   String _harvest_conditions = arg[4].toString();
		   
		   //Block is arg[5]
		   LinkedList listOfClasses = new LinkedList();
		   Lang.AstNode e0 = arg[5].arg[0];		   
		   
		   if (e0 != null)  // if there is something in the block
		   {  listOfClasses = parseBlock(e0.arg[0]);  }
		   
		   // sets the edge state object appropiately
		   edge_state = new EdgeStateDocJTS(_harvest_state_name, listOfClasses);
		   edge_state.setStartName(_harvest_start_name);
		   edge_state.setEndName(_harvest_end_name);
		   edge_state.setConditions(_harvest_conditions);
		   
	   } // of execute	   			
   }

   static public class OtherDecl extends layers.OtherDecl 
   {
	   public StateDocJTS other_state;
	   
	   public void execute() 
	   { 
		  // QName
		   String _harvest_state_name = arg[0].toString();
		   // System.out.println("Other " + _harvest_state_name);
		   
		   super.execute(); 
		   LinkedList listOfClasses = new LinkedList();
		   
		  // Block is arg[1]  
		  Lang.AstNode e0 = arg[1].arg[0];		   
		  if (e0 != null)  // if there is something in the block
		  {
			  listOfClasses = parseBlock(e0.arg[0]);
		  }
		   
		  other_state = new StateDocJTS(_harvest_state_name, listOfClasses);
	   } // of execute	   
   }

   
   /** This methods parses the information in a block and
    * exports a list with the classes/interfaces in the block.
    */
   static public LinkedList parseBlock(AstNode theBlock)
   {
	   LinkedList listOfClasses = new LinkedList();
	   
	   // if the block passed is null {} then return the empty list.
	   if (theBlock == null) return listOfClasses;
	   
	   Lang.AstCursor c = new AstCursor();
       for (c.FirstElement(theBlock); c.MoreElement(); c.NextElement()) 
	   {
		   // System.out.println("Block Stmt " + c.node.className());
		   if (c.node instanceof BlkClassDcl)
		   {
		      DOCLETJTS.Lang.ClassDocJTS innerclass = ((UmodClassDecl)c.node.arg[0])._harvest_class;
			  listOfClasses.add(innerclass);
			  // System.out.println("Inner name : " + innerclass.name());
		   }
				   
		   if (c.node instanceof BlkInterDcl)
		   {
		      DOCLETJTS.Lang.ClassDocJTS innerclass = ((UmInterDecl)c.node.arg[0])._harvest_class;
			  listOfClasses.add(innerclass);	
			  /// System.out.println("Inner name : " + innerclass.name());
		   }					   
       } // for all nodes	   
	   return listOfClasses;
   }
   
   static public class Refines extends layers.Refines {
	   
   }

   static public class RefinesMod extends layers.RefinesMod {
   }

   static public class StartName extends layers.StartName {
   }

   static public class SdSName extends layers.SdSName {
   }

   static public class StarName extends layers.StarName {
   }

   static public class NestedSDDeclaration extends layers.NestedSDDeclaration {
   }

   static public class NSDDecl extends layers.NSDDecl {
	 public StateDiagramDocJTS _harvest_state = null; 
	 public void execute() 
	 { 
	   super.execute();
	   // setModifiers(this);
	   
       Lang.AstCursor c = new AstCursor();  // c to traverse list
       Lang.AstNode   e = arg[0].arg[0];    // e instanceOf Lang.AstList		
	    
	   _harvest_state = ((UmodSdDecl)arg[1])._harvest_state;
		
	    if (e != null && _harvest_state != null) 
		{ // if there are modifiers and it is a class
			
		  // Gets the comment from the first modifier
		  c.FirstElement(e);
		  Lang.AstNode first = c.node;
				
          for (c.FirstElement(e); c.MoreElement(); c.NextElement()) {

				// abstract method
			    if (c.node instanceof ModAbstract) { _harvest_state.setAbstract(true);}
			
			    // final method
			    if (c.node instanceof ModFinal) { _harvest_state.setFinal(true); }
			
			    // public method
			    if (c.node instanceof ModPublic) { _harvest_state.setPublic(true); }
			
			    // protected method
			    if (c.node instanceof ModProtected) { _harvest_state.setProtected(true); }
			
			    // private method
			    if (c.node instanceof ModPrivate) { _harvest_state.setPrivate(true); }
			
			    // static method
			    if (c.node instanceof ModStatic) { _harvest_state.setStatic(true); }
			
			    // these 4 I dont know if have to be accesed 
				// transient method
			    if (c.node instanceof ModTransient)	{ _harvest_state.setTransient(true); }
			
			    // volatile method
			    if (c.node instanceof ModVolatile) { _harvest_state.setVolatile(true); }
			
			    // native method
			    if (c.node instanceof ModNative) { _harvest_state.setNative(true);	}
			
			    // synchronized method
			    if (c.node instanceof ModSynchronized) { _harvest_state.setSynchronized(true);	}
		      } // for all modifiers
		  
	    // ************ Comment Processing ************************************
	    // if there are comments at Unmodified type those are overwritten	
		String comment = ((Lang.AstToken) first.tok[0]).white_space;
		CommentJTS stateComment = 
			CommentParser.ParserCommentState(_harvest_state, comment);
        _harvest_state.setComment(stateComment);		  
		  
		} // if there are modifiers		   
	   	   
	 } // of execute
   }

   static public class ClassBodyDeclaration extends layers.ClassBodyDeclaration 
   {
	   public void execute() 
	   {
		   super.execute(); 
 
	   }   
   }

   static public class ClassBody extends layers.ClassBody 
   {	   
	 LinkedList listOfStates = new LinkedList();
	   
      public void execute() 
	  { 
		  super.execute();
		  // arg[0] OptNode, arg[0].arg[0] AST_FieldDecl
		  Lang.AstNode e = arg[0].arg[0];
		  if (e!=null)
		  {
  		   Lang.AstCursor c = new AstCursor();       // c to traverse list
	       for (c.FirstElement(e); c.MoreElement(); c.NextElement()) 
	        {
			   if (c.node instanceof Lang.NSDDecl)
			   {
				   listOfStates.add(((Lang.NSDDecl)c.node)._harvest_state);
			   }
			 
		    } // for all the elements in classbody
		   
		  } // if there are elements in the ClassBody
		  
 		   _harvest_class.setStates(listOfStates);
		  
	  } // execute
	  
   } // classBody
  
   
 /** This method captures the states defined as part of program.
   * This is used for the combination of sm and java lower alone
   * and any other layer that adds something to the Unmodified type declaration
   */ 
 static public class AST_Class extends layers.AST_Class {
	  
	  // programClasses contains the classes declared at 
	  public LinkedList programStates = new LinkedList();
														
      public void execute()  
	  { 
		  super.execute();
		  
	     Lang.AstCursor c = new AstCursor();     // c to traverse list
         Lang.AstNode   e = this;                // e instanceOf $TEqn.AstList		  
	
         for (c.FirstElement(e); c.MoreElement(); c.NextElement()) 
		 {
		  if (c.node instanceof ModTypeDecl)
	      {  
			if (c.node.arg[1] instanceof Lang.SDDecl)
			{	  
			  // SDDecl -> UmodSdDecl	
			  UmodSdDecl theStateDecl = (UmodSdDecl)c.node.arg[1].arg[0];
			  StateDiagramDocJTS theState = theStateDecl._harvest_state;
			  programStates.add(theState);
			}
         } // if this is a type declaration
		  
	    } // for all the states
	  
		 // Adds the states to the program
		 _harvest_program.setStates(programStates);
		 
		 
		 // Calls the doclet to write the contents of the Program
		 Lang.startWriteAProgram(_harvest_program);

		   
	 } // execute
	 
   } // of class  AST_Class

// *****************************************************************************
// Required for comment management
   static public class ModTypeDecl extends layers.ModTypeDecl {
      public void execute() 
	  { 
		  System.out.println("In ModTypeDecl in SM");
		  super.execute(); 
		  
		  // Insert the corresponding modifiers to the class
		  // Gets the _harvest_class from its UnmodifiedClassDeclaration
	      StateDiagramDocJTS st = null;
		  
	      if (arg[1] instanceof UmodSdDecl)
			  st = ((UmodSdDecl)arg[1])._harvest_state;
		  
          Lang.AstCursor c = new AstCursor();  // c to traverse list
          Lang.AstNode   e = arg[0].arg[0];    // e instanceOf Lang.AstList		
	      if (e != null && st !=null ) 
		      { // if there are modifiers and it is a class
				  
				// Gets the comment from the first modifier
				c.FirstElement(e);
				Lang.AstNode first = c.node;
				
                for (; c.MoreElement(); c.NextElement()) {
			    // abstract method
			    if (c.node instanceof ModAbstract) { st.setAbstract(true);}
			
			    // final method
			    if (c.node instanceof ModFinal) { st.setFinal(true); }
			
			    // public method
			    if (c.node instanceof ModPublic) { st.setPublic(true); }
			
			    // protected method
			    if (c.node instanceof ModProtected) { st.setProtected(true); }
			
			    // private method
			    if (c.node instanceof ModPrivate) { st.setPrivate(true); }
			
			    // static method
			    if (c.node instanceof ModStatic) { st.setStatic(true); }
			
			    // these 4 somehow are not part of ProgramElementDocJTS
			    // transient method
			    // volatile method
			    // native method
			    // synchronized method

		      } // for all modifiers
				
		     // ************ Comment Processing ************************************
			 // if there are comments at Unmodified type those are overwritten	
		     String comment = ((Lang.AstToken) first.tok[0]).white_space;
		     CommentJTS stateComment = 
				CommentParser.ParserCommentState(st, comment);
             st.setComment(stateComment);
			 
			 System.out.println("Comment in State is -> " + comment);
		    } // if there are modifiers			  
			  
  } // execute
 }	   
 
// *****************************************************************************
 
 public static void startWriteAProgram(DOCLETJTS.Lang.ProgramDocJTS _harvest_program ) 
 {
      try {
	     DocletImpl.Lang.DocletJTS.writeAProgram(_harvest_program, OutputDirectory);
	   } catch (IOException ex)
	   {
		  System.out.println("Error while starting a program ");
	   }	 
 } // of startWriteAProgram
   
 
    
};