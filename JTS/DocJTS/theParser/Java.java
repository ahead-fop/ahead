
		package theParser; import
	java.util.Vector; import
	Jakarta.util.FixDosOutputStream; import
    Jakarta.util.Util; import
    java.io.*; import
    jtsdoc.*; import   // imports added
    com.sun.javadoc.*; import
    java.util.*; import
    CommentParsing.*; public class Java extends Base
{
 // ************************************************************************
 // ************************************************************************
 // ************************************************************************
  //*** global variables
  public static LinkedList outerClasses = new LinkedList();
  public static LinkedList harvestedClasses = new LinkedList();
  public static PackageDocJTS containingPackage;
  public static String _program_name ="";
  public static String OutputDirectory; 
  
   // ** definition of Classes	
  /** UnmodifiedClassDeclaration
	: "class" QName [ ExtendsClause ] [ ImplementsClause ] ClassBody	::UmodClassDecl
	;
   */
   static public class UmodClassDecl extends Base.UmodClassDecl {
   	  public String _harvest_name;
      public DOCLETJTS.Lang.ClassDocJTS _harvest_class;
	  
      public void execute() 
	  {
		  
  		  super.execute();
		  
		  // arg[0] class name
		  _harvest_name = ((Lang.AstToken)arg[0].tok[0]).tokenName();
		  		  
		  // arg[3] ClassBody, we get the class harvested in ClassBody
		  _harvest_class = ((Lang.ClassBody)arg[3])._harvest_class;
		  _harvest_class.setName(_harvest_name);
		  
		  // arg[1] OptNode extends
		  // Sets the extends arg1 is OptNode so we get the first child
		   Lang.AstNode e1 = arg[1].arg[0];
		   if (e1 != null)
		   {
			   // Here again I assume that I have to look for the reference
			   // of that class somewhere bot for now just create it with
			   // the name
			  _harvest_class.setSuperClass(new DOCLETJTS.Lang.ClassDocJTS(e1.arg[0].toString())); 
		   }
		   else
			 _harvest_class.setSuperClass(new DOCLETJTS.Lang.ClassDocJTS("Object")); 
		   
		  // arg[2] OptNode implements
		  // Sets the implemented interfaces
		   Lang.AstNode   e2 = arg[2].arg[0];
		   
		   // If there are implemented interfaces then added them
		   if (e2!=null) 
		   {
			   _harvest_class.setInterfaces(((ImplClause)e2).implementedInterfaces);
		   }
		   else // otherwise it is empty (same as above here)
			   _harvest_class.setInterfaces(new LinkedList());
		   
		   
		   // Adds the class parsed to the harvested classes
		   harvestedClasses.add(_harvest_class); 

		   // ************ Comment Processing ************************************
		   String comment = ((Lang.AstToken) tok[0]).white_space;
		   CommentJTS classComment = 
				CommentParser.ParserCommentClass(_harvest_class, comment);
           _harvest_class.setComment(classComment);
           		  
	  } // execute
	  
   }

   // *** definition of interfaces
   /** Grammar rule.
    * UnmodifiedInterfaceDeclaration
	: "interface" QName [ IntExtClause ]
		"{" [ InterfaceMemberDeclarations ] "}"	::UmInterDecl
	;

    */
   static public class UmInterDecl extends Base.UmInterDecl {
	  public String _harvest_name;
      public DOCLETJTS.Lang.ClassDocJTS _harvest_class;
	  
	  public LinkedList listOfMethods = new LinkedList();
	  public LinkedList listOfInnerClasses = new LinkedList();
	  public LinkedList listOfFields = new LinkedList();
	  
	  
      public void execute() 
	  { 
		  super.execute();
		  
		  _harvest_name = ((Lang.AstToken)arg[0].tok[0]).tokenName();
		  _harvest_class = new DOCLETJTS.Lang.ClassDocJTS (_harvest_name);	
		  
		  // Traverses the tree of AST_FieldDecl to get the methods, nestedclasses,
		  // interfaces, etc
		   Lang.AstCursor c = new AstCursor();       // c to traverse list
  
		   // Interface member declarations
		   Lang.AstNode   e = arg[2].arg[0];  // e instanceOf Lang.AstList
		   
		   if (e != null) 
		   {
		   
	       for (c.FirstElement(e); c.MoreElement(); c.NextElement()) 
	       { 
			  // System.out.println("Interface member " + c.node.className());
			  // Method part of an interface 
			  if (c.node instanceof MDecl)
			  {
				  MethodDocJTS met = ((MthDector)((MethodDcl)c.node.arg[0]).arg[2])._harvest_method;
				  listOfMethods.add(met);
			  }
			  
			  // Nested class inside an interface
			  if (c.node instanceof NCDecl)
			  {
				 DOCLETJTS.Lang.ClassDocJTS cls = ((UmodClassDecl)((NClassDecl)c.node.arg[0]).arg[1])._harvest_class;
				 listOfInnerClasses.add(cls);
			  }
			  
			  // Nested interface
			  if (c.node instanceof NIDecl)
			  {
				 DOCLETJTS.Lang.ClassDocJTS cls = ((UmInterDecl)((NInterDecl)c.node.arg[0]).arg[1])._harvest_class; 
				 listOfInnerClasses.add(cls);				 
			  }
			  
			  // Field inside an interface
			  if (c.node instanceof FDecl)
			  { 
				  LinkedList tempList = ((FldVarDec)c.node.arg[0]).fieldsList;
				  

				  for(int i=0; i< tempList.size(); i++)
				   {
					 // By default all the fields of an interface are public
				     // static and final
					  FieldDocJTS theField = (FieldDocJTS)tempList.get(i);
					  theField.setPublic(true);
					  theField.setFinal(true);
					  theField.setStatic(true);
					  
				    listOfFields.add(theField);
				   }			  
			  } // of FDecl
			  
		   } // end of for
		   } // if there are not members
		   
		   // Sets the methods for the class
		   _harvest_class.setMethods(listOfMethods);
		   _harvest_class.setInnerClasses(listOfInnerClasses);
		   _harvest_class.setInterfaces(new LinkedList());
		   _harvest_class.setConstructors(new LinkedList());
		   _harvest_class.setFields(listOfFields);		  
		   _harvest_class.setInterface(true);
		   _harvest_class.setContainingPackage(containingPackage);
		   
		   // adds the Extend Class at the Interface
		   // Arg[1] IntExtClause is optional that's why we get the child
		   Lang.AstNode   e1 = arg[1].arg[0];
		   
		   if (e1 != null)
		   { 
			_harvest_class.setSuperClass(new DOCLETJTS.Lang.ClassDocJTS(e1.arg[0].toString()));
		   }

		   // ************ Comment Processing ************************************
		   String comment = ((Lang.AstToken) tok[0]).white_space;
		   CommentJTS classComment = 
				CommentParser.ParserCommentClass(_harvest_class, comment);
           _harvest_class.setComment(classComment);		
           		   
	  } //execute
   }   
   
 /** Harvests the data structures for a constructor.
    */   
   static public class ConDecl extends Base.ConDecl {
	  public ConstructorDocJTS _harvest_constructor; 
	  public LinkedList thrownExceptions;
	  
      public void execute() 
	  {   
	      String constructorName = ((Lang.AstToken)arg[1].tok[0]).tokenName();
		  _harvest_constructor = new ConstructorDocJTS(constructorName);
		  
		  super.execute(); 
		  
		  // Assign the parameters array to the method
		  // arg1 is an AstOptNode an optional node, we have to get the first
		  // kid if any, which is AST_ParList type, if not null set the 
		  // parameters otherwise just make them null
		  Lang.AST_ParList kid = (AST_ParList)arg[2].arg[0];
		  if (kid == null) 
		   { _harvest_constructor.setParameters(new ParameterJTS[0]); }
		  else
		  _harvest_constructor.setParameters(kid.parametersArray);
		  
		  // Adjusts the Modifiers of the constructor
	      Lang.AstCursor c1 = new AstCursor();  // c to traverse list
          Lang.AstNode   e1 = arg[0].arg[0];    // e instanceOf Lang.AstList		  

		  // Contains the comment
		  String comment ="";
		  		 
		  if (e1 != null) // if there are modifiers
		  {
		   // Gets the comments from the first modifier
		   c1.FirstElement(e1);
		   Lang.AstNode first = c1.node;
		   
           for (; c1.MoreElement(); c1.NextElement()) {	
			// final method
			if (c1.node instanceof ModFinal) { _harvest_constructor.setFinal(true); }
			
			// public method
			if (c1.node instanceof ModPublic) { _harvest_constructor.setPublic(true); }
			
			// protected method
			if (c1.node instanceof ModProtected) { _harvest_constructor.setProtected(true); }
			
			// private method
			if (c1.node instanceof ModPrivate) { _harvest_constructor.setPrivate(true); }
			
			// static method
			if (c1.node instanceof ModStatic) { _harvest_constructor.setStatic(true); }
			
			// transient and volatile are part of FieldDocJTs
			// transient method

			// native method
			if (c1.node instanceof ModNative) { _harvest_constructor.setNative(true);	}
			
			// synchronized method
			if (c1.node instanceof ModSynchronized) { _harvest_constructor.setSynchronized(true);	}

		   } // for all modifiers
		   
  		   // ************ Comment Processing ************************************
		   comment = ((Lang.AstToken) first.tok[0]).white_space;
	   		   
		  } // if there are modifiers
		  else  // if there are no modifiers then read the comments from QName 
  		  {	
			  comment = ((Lang.AstToken)arg[1].tok[0]).white_space;
		  } // if there are no modifiers
		  
		   CommentJTS constructorComment = 
				CommentParser.ParserCommentConstructor(_harvest_constructor, comment);
           _harvest_constructor.setComment(constructorComment);	
		   		   
		  // ****************************************************************		   	  
   
		  // Throws class is optional that's why we have to check its kid
		   Lang.AstNode tn = arg[3].arg[0];
		   
		  if (tn != null) 
		  {
			  // AST_TypeNameList is tn.arg[0]
		      thrownExceptions  = new LinkedList();
		  
		      Lang.AstCursor c2 = new AstCursor();  // c to traverse list
              Lang.AstNode   e2 = tn.arg[0];    // e instanceOf Lang.AstList		  
		 			  
               for (c2.FirstElement(e2); c2.MoreElement(); c2.NextElement()) {	
			    // TNClass -> QNameType -> AST_QualifiedName
			    // I suppose that here I have to either look at some table with
			    // all the interfaces and got the references, BUT in the mean time
			    // I just create an interface object and I added to a list
		         DOCLETJTS.Lang.ClassDocJTS cls = new DOCLETJTS.Lang.ClassDocJTS(c2.node.arg[0].arg[0].toString());
			     cls.setClass(true);
			
			    // adds to the implemented interfaces list
			     thrownExceptions.add(cls);
		        } // for
			   
			   DOCLETJTS.Lang.ClassDocJTS[] arrayExceptions = new DOCLETJTS.Lang.ClassDocJTS[thrownExceptions.size()];
			   for (int i=0; i < thrownExceptions.size(); i++)
				   arrayExceptions[i] = (DOCLETJTS.Lang.ClassDocJTS) thrownExceptions.get(i);
			   
			   // sets the method's thrown exception field
			   _harvest_constructor.setThrownExceptions(arrayExceptions);
			   
		  } // if there are throw clauses		  
		 else // if there are no throw clauses
		  _harvest_constructor.setThrownExceptions(new DOCLETJTS.Lang.ClassDocJTS[0]);
	  } // of execute
   }				   
	
   /** This method adds the Modifiers to the classes at the upper level.
   */
   static public class ModTypeDecl extends Base.ModTypeDecl {
      public void execute() 
	  { 							 
		  super.execute(); 

		  // Insert the corresponding modifiers to the class
		  // Gets the _harvest_class from its UnmodifiedClassDeclaration
	      DOCLETJTS.Lang.ClassDocJTS cls =null;
	      if (arg[1] instanceof UmodClassDecl)
			  cls = ((UmodClassDecl)arg[1])._harvest_class;
	      if (arg[1] instanceof UmInterDecl)
			  cls = ((UmInterDecl)arg[1])._harvest_class;
			  
          Lang.AstCursor c = new AstCursor();  // c to traverse list
          Lang.AstNode   e = arg[0].arg[0];    // e instanceOf Lang.AstList		
	      if (e != null && cls !=null ) 
	      { // if there are modifiers and it is a class
				
			// Gets the comment from the first modifier
			c.FirstElement(e);
			Lang.AstNode first = c.node;		      
		      
            for (; c.MoreElement(); c.NextElement()) {
	    
	        // abstract method
			if (c.node instanceof ModAbstract) { cls.setAbstract(true);}
			
			// final method
			if (c.node instanceof ModFinal) { cls.setFinal(true); }
			
			// public method
			if (c.node instanceof ModPublic) { cls.setPublic(true); }
			
			// protected method
			if (c.node instanceof ModProtected) { cls.setProtected(true); }
			
			// private method
			if (c.node instanceof ModPrivate) { cls.setPrivate(true); }
			
			// static method
			if (c.node instanceof ModStatic) { cls.setStatic(true); }
			
			// these 4 somehow are not part of ProgramElementDocJTS
			// transient, volatile, native, synchronized method

		    } // for all modifiers
		      
		     // ************ Comment Processing ************************************
			 // if there are comments at Unmodified type those are overwritten	
		     String comment = ((Lang.AstToken) first.tok[0]).white_space;
		     CommentJTS classComment = 
				CommentParser.ParserCommentClass(cls, comment);
             cls.setComment(classComment);		            
		      
		    } // if there are modifiers			 		  
	  } // execute
   } 
   
   
   /** NestedClassDeclaration contains the modifiers, and the 
     * UnmodifiedclassDeclaration
     * NestedClassDeclaration
	 *   : [ AST_Modifiers ] UnmodifiedClassDeclaration		::NClassDecl
	 *   ;
     */  
  static public class NestedClassDeclaration extends Base.NestedClassDeclaration {
      public void execute() 
	  { 
		  super.execute(); 
		  
		  // Gets the _harvest_class from its UnmodifiedClassDeclaration
		  DOCLETJTS.Lang.ClassDocJTS cls = ((UmodClassDecl)arg[1])._harvest_class;
		  
	      Lang.AstCursor c = new AstCursor();  // c to traverse list
          Lang.AstNode   e = arg[0].arg[0];    // e instanceOf Lang.AstList		
		  if (e != null) // if there are modifiers
		  {
  		   // Gets the first of the modifiers to get the comment from it	  
		   c.FirstElement(e);
		   Lang.AstNode first = c.node;
		  
           for (; c.MoreElement(); c.NextElement()) {
			// abstract method
			if (c.node instanceof ModAbstract) { cls.setAbstract(true);}
			
			// final method
			if (c.node instanceof ModFinal) { cls.setFinal(true); }
			
			// public method
			if (c.node instanceof ModPublic) { cls.setPublic(true); }
			
			// protected method
			if (c.node instanceof ModProtected) { cls.setProtected(true); }
			
			// private method
			if (c.node instanceof ModPrivate) { cls.setPrivate(true); }
			
			// static method
			if (c.node instanceof ModStatic) { cls.setStatic(true); }
			
			// these 4 somehow are not part of ProgramElementDocJTS
			// transient, volatile, native, synchronized method
		   } // for all modifiers
		   
  		   // ************ Comment Processing ************************************
		   String comment = ((Lang.AstToken) first.tok[0]).white_space;
		   CommentJTS classComment = 
				CommentParser.ParserCommentClass(cls, comment);
           cls.setComment(classComment);		   
		   
		  } // if there are modifiers
		  
	  } // execute 
   }
 
  /** Contains the modifiers of an interface 
    * InterfaceDeclaration
	*    : [ AST_Modifiers ] UnmodifiedInterfaceDeclaration	::InterDecl
	*    ;
    */
   static public class InterfaceDeclaration extends Base.InterfaceDeclaration {
      public void execute() 
	  { 
		  super.execute();
		  
		  // Gets the _harvest_class from its UnmodifiedClassDeclaration
		  DOCLETJTS.Lang.ClassDocJTS cls = ((UmInterDecl)arg[1])._harvest_class;
		  
	      Lang.AstCursor c = new AstCursor();  // c to traverse list
          Lang.AstNode   e = arg[0].arg[0];    // e instanceOf Lang.AstList		
		  if (e != null) // if there are modifiers
		  {
		   // Gets the interface comment from the first modifier
		   c.FirstElement(e);
  		   Lang.AstNode first = c.node; 
  		   		  
           for (; c.MoreElement(); c.NextElement()) {
			// abstract method
			if (c.node instanceof ModAbstract) { cls.setAbstract(true);}
			
			// final method
			if (c.node instanceof ModFinal) { cls.setFinal(true); }
			
			// public method
			if (c.node instanceof ModPublic) { cls.setPublic(true); }
			
			// protected method
			if (c.node instanceof ModProtected) { cls.setProtected(true); }
			
			// private method
			if (c.node instanceof ModPrivate) { cls.setPrivate(true); }
			
			// static method
			if (c.node instanceof ModStatic) { cls.setStatic(true); }
			
			// these 4 somehow are not part of ProgramElementDocJTS
			// transient, volatile, native, synchronized method
		   } // for all modifiers
		   
  		   // ************ Comment Processing ************************************
		   String comment = ((Lang.AstToken) first.tok[0]).white_space;
		   CommentJTS classComment = 
				CommentParser.ParserCommentClass(cls, comment);
           cls.setComment(classComment);			   
		   
		  } // if there are modifiers		  
		  
	  } // execute
   } 
  
  
 /** NestedInterfaceDeclaration
  *	: [ AST_Modifiers ] UnmodifiedInterfaceDeclaration	::NInterDecl
  *	;
 */    
 static public class NestedInterfaceDeclaration extends Base.NestedInterfaceDeclaration {
      public void execute() 
	  { 
		  super.execute(); 
		  
		  // Gets the _harvest_class from its UnmodifiedClassDeclaration
		  DOCLETJTS.Lang.ClassDocJTS cls = ((UmInterDecl)arg[1])._harvest_class;
		  
	      Lang.AstCursor c = new AstCursor();  // c to traverse list
          Lang.AstNode   e = arg[0].arg[0];    // e instanceOf Lang.AstList		
		  if (e != null) // if there are modifiers
		  {
		   // Gets the comment from the first modifier
		   c.FirstElement(e);
		   Lang.AstNode first = c.node;
		   		  
           for (; c.MoreElement(); c.NextElement()) {
			// abstract method
			if (c.node instanceof ModAbstract) { cls.setAbstract(true);}
			
			// final method
			if (c.node instanceof ModFinal) { cls.setFinal(true); }
			
			// public method
			if (c.node instanceof ModPublic) { cls.setPublic(true); }
			
			// protected method
			if (c.node instanceof ModProtected) { cls.setProtected(true); }
			
			// private method
			if (c.node instanceof ModPrivate) { cls.setPrivate(true); }
			
			// static method
			if (c.node instanceof ModStatic) { cls.setStatic(true); }
			
			// these 4 somehow are not part of ProgramElementDocJTS
			// transient, volatile, native, synchronized method

		   } // for all modifiers
		   
  		   // ************ Comment Processing ************************************
		   String comment = ((Lang.AstToken) first.tok[0]).white_space;
		   CommentJTS classComment = 
				CommentParser.ParserCommentClass(cls, comment);
           cls.setComment(classComment);			   
		   
		  } // if there are modifiers		 
		  
	  } // execute	  
   }

  /** MethodDeclaration
   *	: [ AST_Modifiers ] AST_TypeName MethodDeclarator
   *      [ ThrowsClause] MethodDeclSuffix			::MethodDcl
   *	;
   */
   static public class MethodDeclaration extends Base.MethodDeclaration {
	  LinkedList thrownExceptions;
	  
      public void execute() 
	  { 
		  super.execute();		  
		  
		  // Gets the _harvest_method from its MethodDeclarator
		  MethodDocJTS met = ((MethodDeclarator)arg[2])._harvest_method;
		  		  
	      Lang.AstCursor c = new AstCursor();  // c to traverse list
          Lang.AstNode   e = arg[0].arg[0];    // e instanceOf Lang.AstList		  

		  // Contains the comment
		  String comment ="";
		  String returnTypeString = "";
		  		 
		  if (e != null) // if there are modifiers
		  {
		   // Gets the comments from the first modifier
		   c.FirstElement(e);
		   Lang.AstNode first = c.node;
		   		  
           for (; c.MoreElement(); c.NextElement()) {			  
			// abstract method
			if (c.node instanceof ModAbstract) { met.setAbstract(true);}
			
			// final method
			if (c.node instanceof ModFinal) { met.setFinal(true); }
			
			// public method
			if (c.node instanceof ModPublic) { met.setPublic(true); }
			
			// protected method
			if (c.node instanceof ModProtected) { met.setProtected(true); }
			
			// private method
			if (c.node instanceof ModPrivate) { met.setPrivate(true); }
			
			// static method
			if (c.node instanceof ModStatic) { met.setStatic(true); }
			
			// these 4 somehow are not part of ProgramElementDocJTS
			// transient, volatile, native, synchronized
		   } // for all modifiers
		   
  		   // ************ Comment Processing ************************************
		   comment = ((Lang.AstToken) first.tok[0]).white_space;
		   
		  } // if there are modifiers
		  else  // if there are no modifiers then read the comments from ???? 
  		  {

			  // Gets the comment from the primitive type
			  if (arg[1] instanceof Lang.PrimType)
			  {
				  comment = ((Lang.AstToken)arg[1].arg[0].tok[0]).white_space;
				  // returnTypeString = ((Lang.AstToken)arg[1].arg[0].tok[0]).tokenName();
			  }
			  
			  // Gets the comment from a Qualified type name
			  if (arg[1] instanceof Lang.QNameType)
			  {
				 // arg[1] = QNameType, arg[1].arg[0] = AST_QualifiedName
			     // arg[1].arg[0].arg[0] = AST_QualifiedNameElem;
			     // arg[1].arg[0].arg[0].arg[0] = Lang.NameId 
	  			  Lang.AstNode commentNode =  arg[1].arg[0].arg[0].arg[0];
                  comment = ((Lang.AstToken)commentNode.tok[0]).white_space;
				  // returnTypeString = arg[1].toString();
				  
				  // In case there are comments get only the type
				  // returnTypeString = returnTypeString.substring(comment.length(), 
				  //								returnTypeString.length());
			  }
			  
		  } // if there are no modifiers
		  
		   CommentJTS methodComment = 
				CommentParser.ParserCommentMethod(met, comment);
           met.setComment(methodComment);	
		   		   
		  // Obtains the returnTypeString 
		  // Gets the comment from the primitive type
		  if (arg[1] instanceof Lang.PrimType)
		  {
			  returnTypeString = ((Lang.AstToken)arg[1].arg[0].tok[0]).tokenName();
		  }
			  
		  // Gets the comment from a Qualified type name
		  if (arg[1] instanceof Lang.QNameType)
		  {
		      returnTypeString = arg[1].toString();
			 
			  System.out.println("Comment " + comment + " " + comment.length());
			  System.out.println("Return " + returnTypeString + " " + 
							       returnTypeString.length());
			  
			  // If there are no modifiers then returnType should remove the
			  // comment
			  if (e == null) 
			  returnTypeString = returnTypeString.substring(comment.length(), 
											returnTypeString.length());
		   }
			  		   
		   
		  // Sets the corresponding AST_TypeName
		   met.setReturnType(new TypeJTS(returnTypeString,returnTypeString));		  

		  // ********************************************************************** 
		  		  		  
		  // Throws class is optional that's why we have to check its kid
		  Lang.AstNode tn = arg[3].arg[0];
		   
		  if (tn != null) 
		  {
			  // AST_TypeNameList is tn.arg[0]

		      thrownExceptions  = new LinkedList();
		  
		      Lang.AstCursor c1 = new AstCursor();  // c to traverse list
              Lang.AstNode   e1 = tn.arg[0];    // e instanceOf Lang.AstList		  
		 			  
               for (c1.FirstElement(e1); c1.MoreElement(); c1.NextElement()) {	
			    // TNClass -> QNameType -> AST_QualifiedName
			    // I suppose that here I have to either look at some table with
			    // all the interfaces and got the references, BUT in the mean time
			    // I just create an interface object and I added to a list
		         DOCLETJTS.Lang.ClassDocJTS cls = new DOCLETJTS.Lang.ClassDocJTS(c1.node.arg[0].arg[0].toString());
			     cls.setClass(true);
			
			    // adds to the implemented interfaces list
			     thrownExceptions.add(cls);
		        } // for
			   
			   DOCLETJTS.Lang.ClassDocJTS[] arrayExceptions = new DOCLETJTS.Lang.ClassDocJTS[thrownExceptions.size()];
			   for (int i=0; i < thrownExceptions.size(); i++)
				   arrayExceptions[i] = (DOCLETJTS.Lang.ClassDocJTS) thrownExceptions.get(i);
			   
			   // sets the method's thrown exception field
			   met.setThrownExceptions(arrayExceptions);
			   
		  } // if there are throw clauses
		  else // there are no throw clauses
			  met.setThrownExceptions(new DOCLETJTS.Lang.ClassDocJTS[0]);
		  
	 } // execute
   }

   /** This static method just returns an string that represents a
    * AST_QualifiedName
    */
   public static String getAST_QualifiedName(Lang.AST_QualifiedName theNode)
   {
      Lang.AstCursor c = new AstCursor();  // c to traverse list
      Lang.AstNode   e = theNode;          // e instanceOf Lang.AstList		  
	
	  String qualifiedName = "";
	  String token;
	  
      for (c.FirstElement(e); c.MoreElement(); c.NextElement()) 
  	  {
		  token = ((Lang.AstToken) c.node.tok[0]).tokenName();
		  qualifiedName = qualifiedName + "." + token;
	  }
	  
	  // Eliminates the first dot
	  qualifiedName = qualifiedName.substring(1);
	  
	  return qualifiedName;
   }

  /** The variable  _harvest_method is declared here because MethodDeclaration's 
    * arg[2] is an object of this type.
    */
   static public class MethodDeclarator extends Base.MethodDeclarator {
    public MethodDocJTS _harvest_method;
	  public void execute() { super.execute(); }
   }   
   
   /** MethodDeclarator
	 *      : QName "(" [ AST_ParList ] ")" [ Dims ]		::MthDector
	 *      ;
	*/
    static public class MthDector extends Base.MthDector {
	  
      public void execute() 
	  {   // Sets the method name
	      String method_name = ((Lang.AstToken)arg[0].tok[0]).tokenName();
		  _harvest_method = new MethodDocJTS(method_name);
		  super.execute(); 
	      	  		   
		  // Assign the parameters array to the method
		  // arg1 is an AstOptNode an optional node, we have to get the first
		  // kid if any, which is AST_ParList type, if not null set the 
		  // parameters otherwise just make them null
		  Lang.AST_ParList kid = (AST_ParList)arg[1].arg[0];
		  if (kid == null) 
		  {
			  _harvest_method.setParameters(new ParameterJTS[0]);
			  return;
		  }
		  _harvest_method.setParameters(kid.parametersArray);
						  
	  } // of execute
   }

/**
  * AST_ParList
  *		: FormalParameter ( "," FormalParameter )*
  *		;
 */
  static public class AST_ParList extends Base.AST_ParList {
	  public LinkedList parametersList;
	  public ParameterJTS[] parametersArray;
	  	  
      public void execute() 
	  { 
		  super.execute(); 
		  	  
		  // Adds the parameters to the 
		   parametersList = new LinkedList();
	  
		   Lang.AstCursor c = new AstCursor(); // c to traverse list
		   Lang.AstNode   e = this;          // e instanceOf Lang.AstList
	  
	       for (c.FirstElement(e); c.MoreElement(); c.NextElement()) 
	       { 
			 parametersList.add(((FormParDecl)c.node)._harvest_parameter);
	       }		   
		   
		   // now make it an array so that you can pass it to the method
		   parametersArray = new ParameterJTS[parametersList.size()];
		   for(int i=0; i< parametersList.size(); i++)
			   parametersArray[i] = (ParameterJTS)parametersList.get(i);		  
		  		  
	  } // execute
  }	  

/**  FormalParameter
  *	   : [ "final" ] AST_TypeName VariableDeclaratorId		::FormParDecl
  *	   ;
 */  
   static public class FormParDecl extends Base.FormParDecl {
	  public ParameterJTS _harvest_parameter;
	  
      public void execute() 
	  {   
		  _harvest_parameter = 
			  new ParameterJTS (new TypeJTS(arg[0].toString(),arg[0].toString()), 
							 arg[1].toString());		  
		  super.execute(); 
	  }
   }

   
   static public class NClassDecl extends Base.NClassDecl {
      public void execute() { super.execute(); }
   }

  /** AST_Program
	*    : [ PackageDeclaration ] [ AST_Imports ] [ AST_Class ]
	*							::program
	*    ;
    */
	static public class program extends Base.program {
      public void execute() { super.execute(); } 
   }
   
  static public class ClassBodyDeclaration extends Base.ClassBodyDeclaration {
      public void execute() { super.execute(); }
   }

   static public class ClsBody extends Base.ClsBody {
      public void execute() { super.execute(); }
   }
	 
  static public class AST_FieldDecl extends Base.AST_FieldDecl {
      public void execute() { super.execute(); }
   }   
  
/** AST_Class
  *	   : ( TypeDeclaration )+
  *	   ;
 */  
  static public class AST_Class extends Base.AST_Class {
	  
	  // program class 
	  public DOCLETJTS.Lang.ProgramDocJTS _harvest_program =
			 new  DOCLETJTS.Lang.ProgramDocJTS(_program_name);
	  
	  // programClasses contains the classes declared at 
	  public LinkedList programClasses = new LinkedList();
														
      public void execute() 
	  { 
		  super.execute(); 
       
		// Sets the containing package
		_harvest_program.setContainingPackage(containingPackage);
		
	    Lang.AstCursor c = new AstCursor();   // c to traverse list
        Lang.AstNode   e = this;              // e instanceOf Lang.AstList		  
	
       for (c.FirstElement(e); c.MoreElement(); c.NextElement()) { 
		   
		if (c.node instanceof ModTypeDecl)
	    {  
			DOCLETJTS.Lang.ClassDocJTS theClass;
			
			// if we found a class
			if (c.node.arg[1] instanceof UmodClassDecl)
			{ 
			  UmodClassDecl theClassDecl = (UmodClassDecl)c.node.arg[1];
			  theClass = theClassDecl._harvest_class;
			  programClasses.add(theClass);
			}
			
			// if we found an interface
			if (c.node.arg[1] instanceof UmInterDecl)
			{ 
			  UmInterDecl theInterfaceDecl = (UmInterDecl)c.node.arg[1];
			  theClass =  theInterfaceDecl._harvest_class;
			  programClasses.add(theClass);
			}
	    } // if this is a type declaration
		
	  } // for all interfaces and classes defined in the AST_Class
	  
        // Adds to theProgram the program Classes found
	      _harvest_program.setClasses(programClasses);
		
		// Calls the method in charge of running the doclet  
        Lang.startWriteAProgram(_harvest_program);
		
	 } // execute
   }

 public static void startWriteAProgram(DOCLETJTS.Lang.ProgramDocJTS _harvest_program ) 
 {
      try {
	     DocletImpl.Lang.DocletJTS.writeAProgram(_harvest_program, OutputDirectory);
	   } catch (IOException ex)
	   {
		  System.out.println("Error while starting a program ");
	   }	 
 }

  static public class UnmodifiedTypeDeclaration extends Base.UnmodifiedTypeDeclaration {
      public void execute()  { super.execute(); }
   }

  /** FieldDeclaration
	* [ AST_Modifiers ] AST_TypeName AST_VarDecl ";"	::FldVarDec
	* ;
   */
    static public class FldVarDec extends Base.FldVarDec {
     LinkedList fieldsList = new LinkedList();
	
	// A list had to be added beacuse several fields can be defined at once
      public void execute() 
	  { 
		  super.execute(); 	
 
		  // arg[2] is AST_VarDecl is a AstList so that we have to traverse
		  // it to get the list of Fields
	      Lang.AstCursor c = new AstCursor();     // c to traverse list
          Lang.AstNode   e = arg[2];      // e instanceOf Lang.AstList		  
		  
	      for (c.FirstElement(e); c.MoreElement(); c.NextElement()) {
	       if (c.node instanceof VarDecl) 
		   { 
			 fieldsList.add(((VarDecl)c.node)._harvest_field);
		   }
	      } // for
	      
	      // For all the fields of the list.
		  for(int i=0; i< fieldsList.size(); i++)
		  {	  
		  // Gets the _harvest_method from its MethodDeclarator
		  FieldDocJTS field = (FieldDocJTS)fieldsList.get(i);
		  
	      Lang.AstCursor c1 = new AstCursor();  // c to traverse list
          Lang.AstNode   e1 = arg[0].arg[0];    // e instanceOf Lang.AstList		  

 		  // Contains the comment
		  String comment ="";
		  String returnTypeString = "";
		  		  
		  // Sets the modifiers
		  if (e1 != null) // if there are modifiers
		  {
		   // Gets the comments from the first modifier
		   c1.FirstElement(e1);
		   Lang.AstNode first = c1.node;
		   
           for (; c1.MoreElement(); c1.NextElement()) {	

			// final method
			if (c1.node instanceof ModFinal) { field.setFinal(true); }
			
			// public method
			if (c1.node instanceof ModPublic) { field.setPublic(true);  }
			
			// protected method
			if (c1.node instanceof ModProtected) { field.setProtected(true); }
			
			// private method
			if (c1.node instanceof ModPrivate) { field.setPrivate(true); }
			
			// static method
			if (c1.node instanceof ModStatic) { field.setStatic(true); }
			
			// transient and volatile are part of FieldDocJTs
			// transient method
			if (c1.node instanceof ModTransient)	{ field.setTransient(true); }
			
			// volatile method
			if (c1.node instanceof ModVolatile) { field.setVolatile(true); }
		   } // for all modifiers
		   
		   // ************ Comment Processing ************************************
		   comment = ((Lang.AstToken) first.tok[0]).white_space;		   
		   
		  } // if there are modifiers
		  else  // if there are no modifiers then read the comments from ???? 
  		  {

			  // Gets the comment from the primitive type
			  if (arg[1] instanceof Lang.PrimType)
			  {
				  comment = ((Lang.AstToken)arg[1].arg[0].tok[0]).white_space;
			  }
			  
			  // Gets the comment from a Qualified type name
			  if (arg[1] instanceof Lang.QNameType)
			  {
				 // arg[1] = QNameType, arg[1].arg[0] = AST_QualifiedName
			     // arg[1].arg[0].arg[0] = AST_QualifiedNameElem;
			     // arg[1].arg[0].arg[0].arg[0] = Lang.NameId 
	  			  Lang.AstNode commentNode =  arg[1].arg[0].arg[0].arg[0];
                  comment = ((Lang.AstToken)commentNode.tok[0]).white_space;
			  }
			  
		  } // if there are no modifiers
		  
		   CommentJTS fieldComment = 
				CommentParser.ParserCommentField(field, comment);
           field.setComment(fieldComment);	
		   		   
		  // Obtains the returnTypeString 
		  // Gets the comment from the primitive type
		  if (arg[1] instanceof Lang.PrimType)
		  {
			  returnTypeString = ((Lang.AstToken)arg[1].arg[0].tok[0]).tokenName();
		  }
			  
		  // Gets the comment from a Qualified type name
		  if (arg[1] instanceof Lang.QNameType)
		  {
		      returnTypeString = arg[1].toString();
			  
			  // If there are no modifiers then returnType should remove the
			  // comment
			  if (e1 == null) 
			  returnTypeString = returnTypeString.substring(comment.length(), 
											returnTypeString.length());
		   }
		   			  		   		   
		  // sets the type of the field
		  field.setType(new TypeJTS(returnTypeString, returnTypeString));
		  
		 } // for all the fields of the list		   
		 	  	  		  
	 } // execute
   }
   
  static public class AST_VarDecl extends Base.AST_VarDecl {
      public void execute()   {  super.execute();   }
   }	
  
  // *** Field of a class
  /** VariableDeclarator
	*  : VariableDeclaratorId [ VarAssign ]			::VarDecl
	*  ;
   */
   static public class VarDecl extends Base.VarDecl {
	  public FieldDocJTS _harvest_field;
	   
      public void execute() 
	  { 
		  super.execute();
		  String field_name = ((Lang.AstToken)arg[0].arg[0].tok[0]).tokenName();
		  _harvest_field = new FieldDocJTS(field_name);
	  }
   }
  
  /**
   * ImplementsClause
   *	: "implements" AST_TypeNameList				::ImplClause
   * 	;
  */    
  static public class ImplClause extends Base.ImplClause {
	  LinkedList implementedInterfaces;
	  
      public void execute() 
	  { 
		  // arg[0] is a AstList so we can traverse it
		  super.execute();
	
		  implementedInterfaces = new LinkedList();
		  
		  Lang.AstCursor c1 = new AstCursor();  // c to traverse list
          Lang.AstNode   e1 = arg[0];    // e instanceOf Lang.AstList		  
		 
		  if (e1 != null) // if there are modifiers
		  {
           for (c1.FirstElement(e1); c1.MoreElement(); c1.NextElement()) {	
			 // TNClass -> QNameType -> AST_QualifiedName

			 // I suppose that here I have to either look at some table with
			 // all the interfaces and got the references, BUT in the mean time
			 // I just create an interface object and I added to a list
		     DOCLETJTS.Lang.ClassDocJTS cls = new DOCLETJTS.Lang.ClassDocJTS(c1.node.arg[0].arg[0].toString());
			 cls.setInterface(true);
			
			 // adds to the implemented interfaces list
			 implementedInterfaces.add(cls);

		   } // for
		   
		  } // if there are implements interfaces
		  		   
	  } // execute
   }

 /**
   ClassBody
	: "{" [ AST_FieldDecl ] "}"				::ClsBody
	;

   AST_FieldDecl
	: ( ClassBodyDeclaration )+
	;

   ClassBodyDeclaration
	: LOOKAHEAD(2)
	  Initializer
	|
	  LOOKAHEAD( [ AST_Modifiers() ] "class" )
	  NestedClassDeclaration
	| LOOKAHEAD( [ AST_Modifiers() ] "interface" )
	  NestedInterfaceDeclaration
	| LOOKAHEAD( [ "public" | "protected" | "private" ] AST_QualifiedName() "(" )
	  ConstructorDeclaration
	| LOOKAHEAD( MethodDeclarationLookahead() )
	  MethodDeclaration
	| FieldDeclaration
	;
 */  
 static public class ClassBody extends Base.ClassBody 
 {
      public DOCLETJTS.Lang.ClassDocJTS _harvest_class;
      public LinkedList listOfMethods = new LinkedList();
	  public LinkedList listOfInnerClasses = new LinkedList();
	  public LinkedList listOfConstructors = new LinkedList();
	  public LinkedList listOfFields = new LinkedList();
	  public LinkedList listOfInterfaces = new LinkedList();
	  
      public void execute() 
	  {
		  super.execute(); 		 
		  _harvest_class = new DOCLETJTS.Lang.ClassDocJTS("bodyclass");	
		  
		  // Traverses the tree of AST_FieldDecl to get the methods, nestedclasses,
		  // interfaces, etc
		   Lang.AstCursor c = new AstCursor();       // c to traverse list
		   
		   // arg[3] is ClassBody, arg[0] is the OptNode, arg[0] is the first element
		   Lang.AstNode   e = arg[0].arg[0];  // e instanceOf Lang.AstList
		   
		   if (e != null) 
		   { 		   	   
	       for (c.FirstElement(e); c.MoreElement(); c.NextElement()) 
	       {
			  if (c.node instanceof MethodDcl)
			  {
				  MethodDocJTS met = ((MthDector)c.node.arg[2])._harvest_method;
				  listOfMethods.add(met);
			  }
			  
			  if (c.node instanceof NestedClassDeclaration)
			  {
				 DOCLETJTS.Lang.ClassDocJTS cls = ((UmodClassDecl)c.node.arg[1])._harvest_class;
				 
				 listOfInnerClasses.add(cls);

			  }
			  
			  if (c.node instanceof NestedInterfaceDeclaration)
			  {
				 DOCLETJTS.Lang.ClassDocJTS cls = ((UmInterDecl)c.node.arg[1])._harvest_class;				 
				 listOfInnerClasses.add(cls);
			  }
			  
			  if (c.node instanceof ConDecl)
			  {
				  ConstructorDocJTS cons = ((ConDecl)c.node)._harvest_constructor;
				  listOfConstructors.add(cons);
			  }
			  
			  if (c.node instanceof FldVarDec)
			  { 
				  LinkedList tempList = ((FldVarDec)c.node).fieldsList;
				  for(int i=0; i< tempList.size(); i++)
				  {
					  listOfFields.add((FieldDocJTS)tempList.get(i));
				  }
			  }
			  
		   } // end of for
		  } // if there members of the class
		   
		   // Sets the methods for the class
		   _harvest_class.setMethods(listOfMethods);
		   _harvest_class.setInnerClasses(listOfInnerClasses);
		   _harvest_class.setInterfaces(listOfInterfaces);
		   _harvest_class.setSuperClass(new DOCLETJTS.Lang.ClassDocJTS("Object"));
		   _harvest_class.setConstructors(listOfConstructors);
		   _harvest_class.setFields(listOfFields);
		   _harvest_class.setClass(true);
		   _harvest_class.setContainingPackage(containingPackage);		   
		   _harvest_class.setInterfaces(new LinkedList());
		   _harvest_class.setSuperClass(new DOCLETJTS.Lang.ClassDocJTS("")); 
		  
	  } // execute	   
	  
   } // of classBody

/** Handles the comments for the method calls.
  */
static public class AST_Stmt extends Base.AST_Stmt {
	  
/** List of comments from a Block or AST_Stmt.
  */ 
public LinkedList commentsList = new LinkedList();
	  
   public void execute() 
   { 
     super.execute();
	/*	  
	 $TEqn.AstNode e = this;
	 if (e!= null)
	 {
	      $TEqn.AstCursor c = new AstCursor();  // c to traverse list
   	 			  
          for (c.FirstElement(e); c.MoreElement(); c.NextElement()) 
	      {	
			   if (c.node instanceof Lang.ExprStmt)
			   {   // arg[0] => Lang.PrimExpr
				   // arg[0].arg[0] => Lang.PPQualName
				   // arg[0].arg[0].arg[0] => Lang.AST_QualifiedName
				   // arg[0].arg[0].arg[0].arg[0] => Lang.AST_QualifiedNameElem
			       // arg[0].arg[0].arg[0].arg[0].arg[0] => Lang.NameId
				   
				   $TEqn.AstNode e1 = c.node.arg[0].arg[0].arg[0].arg[0].arg[0];
				   String comment = (($TEqn.AstToken)e1.tok[0]).white_space;
				   comment = comment.trim();
				   if (comment.length() != 0) commentsList.add(comment);
			   }
		   }
		} // if		  
	  */
	  } // of execute
   }   
   
  //*************************************************************************					   
  //*************************************************************************					   
  //*************************************************************************					   

   static public class AST_Program extends Base.AST_Program {
      public void execute() { super.execute(); }
   }

   static public class AST_Imports extends Base.AST_Imports {
      public void execute() { super.execute(); }
   }

   
   static public class PackageDeclaration extends Base.PackageDeclaration {
      public void execute() { super.execute(); }
   }

   static public class PackStm extends Base.PackStm {
      public void execute() { super.execute(); }
   }

   static public class ImportDeclaration extends Base.ImportDeclaration {
      public void execute() { super.execute(); }
   }

   static public class ImpQual extends Base.ImpQual {
      public void execute() { super.execute(); }
   }

   static public class DotTimes extends Base.DotTimes {
      public void execute() { super.execute(); }
   }

   static public class DotTimesC extends Base.DotTimesC {
      public void execute() { super.execute(); }
   }

   static public class TypeDeclaration extends Base.TypeDeclaration {
      public void execute() { super.execute(); }
   }
   
  static public class InterDecl extends Base.InterDecl {
      public void execute() { super.execute(); }
   } 
  
  
 static public class FieldDeclaration extends Base.FieldDeclaration {	 
      public void execute()  { super.execute();  }
 }


   static public class EmptyTDecl extends Base.EmptyTDecl {
      public void execute() { super.execute(); }
   }



   static public class AST_Modifiers extends Base.AST_Modifiers {
      public void execute() { super.execute(); }
   }

   static public class Modifier extends Base.Modifier {
      public void execute() { super.execute(); }
   }

   static public class ModAbstract extends Base.ModAbstract {
      public void execute() { super.execute(); }
   }

   static public class ModFinal extends Base.ModFinal {
      public void execute() { super.execute(); }
   }

   static public class ModPublic extends Base.ModPublic {
      public void execute() { super.execute(); }
   }

   static public class ModProtected extends Base.ModProtected {
      public void execute() { super.execute(); }
   }

   static public class ModPrivate extends Base.ModPrivate {
      public void execute() { super.execute(); }
   }

   static public class ModStatic extends Base.ModStatic {
      public void execute() { super.execute(); }
   }

   static public class ModTransient extends Base.ModTransient {
      public void execute() { super.execute(); }
   }

   static public class ModVolatile extends Base.ModVolatile {
      public void execute() { super.execute(); }
   }

   static public class ModNative extends Base.ModNative {
      public void execute() { super.execute(); }
   }

   static public class ModSynchronized extends Base.ModSynchronized {
      public void execute() { super.execute(); }
   }

   static public class UnmodifiedClassDeclaration extends Base.UnmodifiedClassDeclaration {
      public void execute() 
	  { super.execute(); 
	  }
   }



   static public class ExtendsClause extends Base.ExtendsClause {
      public void execute() { super.execute(); }
   }

   static public class ExtClause extends Base.ExtClause {
      public void execute() { super.execute(); }
   }

   static public class ImplementsClause extends Base.ImplementsClause {
      public void execute() { super.execute(); }
   }





 



 


   static public class MethodDeclarationLookahead extends Base.MethodDeclarationLookahead {
      public void execute() { super.execute(); }
   }

   static public class MDeclLA extends Base.MDeclLA {
      public void execute() { super.execute(); }
   }



 


   static public class NInterDecl extends Base.NInterDecl {
      public void execute() { super.execute(); }
   }

   static public class UnmodifiedInterfaceDeclaration extends Base.UnmodifiedInterfaceDeclaration {
      public void execute() { super.execute(); }
   }



   static public class IntExtClause extends Base.IntExtClause {
      public void execute() { super.execute(); }
   }

   static public class IntExtClauseC extends Base.IntExtClauseC {
      public void execute() { super.execute(); }
   }

   static public class InterfaceMemberDeclarations extends Base.InterfaceMemberDeclarations {
      public void execute() { super.execute(); }
   }

   static public class InterfaceMemberDeclaration extends Base.InterfaceMemberDeclaration {
      public void execute() { super.execute(); }
   }

   static public class NCDecl extends Base.NCDecl {
      public void execute() { super.execute(); }
   }

   static public class NIDecl extends Base.NIDecl {
      public void execute() { super.execute(); }
   }

   static public class MDecl extends Base.MDecl {
      public void execute() { super.execute(); }
   }

   static public class FDecl extends Base.FDecl {
      public void execute() { super.execute(); }
   }




 

   static public class VariableDeclarator extends Base.VariableDeclarator {
      public void execute() { super.execute(); }
   }



   static public class VarAssign extends Base.VarAssign {
      public void execute() { super.execute(); }
   }

   static public class VarAssignC extends Base.VarAssignC {
      public void execute() { super.execute(); }
   }

   static public class VariableDeclaratorId extends Base.VariableDeclaratorId {
      public void execute() { super.execute(); }
   }

   static public class DecNameDim extends Base.DecNameDim {
      public void execute() { super.execute(); }
   }

   static public class Dims extends Base.Dims {
      public void execute() { super.execute(); }
   }

   static public class Dim extends Base.Dim {
      public void execute() { super.execute(); }
   }

   static public class Dim2 extends Base.Dim2 {
      public void execute() { super.execute(); }
   }

   static public class AST_VarInit extends Base.AST_VarInit {
      public void execute() { super.execute(); }
   }

   static public class ArrInit extends Base.ArrInit {
      public void execute() { super.execute(); }
   }

   static public class VarInitExpr extends Base.VarInitExpr {
      public void execute() { super.execute(); }
   }

   static public class AST_ArrayInit extends Base.AST_ArrayInit {
      public void execute() { super.execute(); }
   }


   static public class MethodDcl extends Base.MethodDcl {
      public void execute() { super.execute(); }
   }

   static public class ThrowsClause extends Base.ThrowsClause {
      public void execute() { super.execute(); }
   }

   static public class ThrowsClauseC extends Base.ThrowsClauseC {
      public void execute() { super.execute(); }
   }

   static public class MethodDeclSuffix extends Base.MethodDeclSuffix {
      public void execute() { super.execute(); }
   }

   static public class MDSBlock extends Base.MDSBlock {
      public void execute() { super.execute(); }
   }

   static public class MDSEmpty extends Base.MDSEmpty {
      public void execute() { super.execute(); }
   }





   static public class FormalParameter extends Base.FormalParameter {
      public void execute() { super.execute(); }
   }



   static public class ConstructorDeclaration extends Base.ConstructorDeclaration {
      public void execute() { super.execute(); }
   }



   static public class ExplicitConstructorInvocation extends Base.ExplicitConstructorInvocation {
      public void execute() 
	  {   
		  super.execute(); 
	  }
   }

   static public class ConThis extends Base.ConThis {
      public void execute() { super.execute(); }
   }

   static public class ConSuper extends Base.ConSuper {
      public void execute() { super.execute(); }
   }

   static public class PrimDot extends Base.PrimDot {
      public void execute() { super.execute(); }
   }

   static public class PrimDotC extends Base.PrimDotC {
      public void execute() { super.execute(); }
   }

   static public class Initializer extends Base.Initializer {
      public void execute() { super.execute(); }
   }

   static public class Init extends Base.Init {
      public void execute() { super.execute(); }
   }

   static public class AST_TypeName extends Base.AST_TypeName {
      public void execute() { super.execute(); }
   }

   static public class PrimType extends Base.PrimType {
      public void execute() { super.execute(); }
   }

   static public class QNameType extends Base.QNameType {
      public void execute() { super.execute(); }
   }

   static public class PrimitiveType extends Base.PrimitiveType {
      public void execute() { super.execute(); }
   }

   static public class BoolTyp extends Base.BoolTyp {
      public void execute() { super.execute(); }
   }

   static public class CharTyp extends Base.CharTyp {
      public void execute() { super.execute(); }
   }

   static public class ByteTyp extends Base.ByteTyp {
      public void execute() { super.execute(); }
   }

   static public class ShortTyp extends Base.ShortTyp {
      public void execute() { super.execute(); }
   }

   static public class IntTyp extends Base.IntTyp {
      public void execute() { super.execute(); }
   }

   static public class LongTyp extends Base.LongTyp {
      public void execute() { super.execute(); }
   }

   static public class FloatTyp extends Base.FloatTyp {
      public void execute() { super.execute(); }
   }

   static public class DoubleTyp extends Base.DoubleTyp {
      public void execute() { super.execute(); }
   }

   static public class VoidTyp extends Base.VoidTyp {
      public void execute() { super.execute(); }
   }

   static public class AST_QualifiedName extends Base.AST_QualifiedName {
      public void execute() { super.execute(); }
   }

   static public class QName extends Base.QName {
      public void execute() { super.execute(); }
   }

   static public class NameId extends Base.NameId {
      public void execute() 
	  { 
		  super.execute(); 
		  // System.out.println("Name ID " + tok[0].tokenName());
	  }
   }

   static public class AST_TypeNameList extends Base.AST_TypeNameList {
      public void execute() { super.execute(); }
   }

   static public class TName extends Base.TName {
      public void execute() { super.execute(); }
   }

   static public class TNClass extends Base.TNClass {
      public void execute() { super.execute(); }
   }

   static public class Expression extends Base.Expression {
      public void execute() { super.execute(); }
   }

   static public class AsgExpr extends Base.AsgExpr {
      public void execute() { super.execute(); }
   }

   static public class AST_Exp extends Base.AST_Exp {
      public void execute() { super.execute(); }
   }

   static public class AssignmentOperator extends Base.AssignmentOperator {
      public void execute() { super.execute(); }
   }

   static public class Assign extends Base.Assign {
      public void execute() { super.execute(); }
   }

   static public class AssnTimes extends Base.AssnTimes {
      public void execute() { super.execute(); }
   }

   static public class AssnDiv extends Base.AssnDiv {
      public void execute() { super.execute(); }
   }

   static public class AssnMod extends Base.AssnMod {
      public void execute() { super.execute(); }
   }

   static public class AssnPlus extends Base.AssnPlus {
      public void execute() { super.execute(); }
   }

   static public class AssnMinus extends Base.AssnMinus {
      public void execute() { super.execute(); }
   }

   static public class AssnShL extends Base.AssnShL {
      public void execute() { super.execute(); }
   }

   static public class AssnShR extends Base.AssnShR {
      public void execute() { super.execute(); }
   }

   static public class AssnShRR extends Base.AssnShRR {
      public void execute() { super.execute(); }
   }

   static public class AssnAnd extends Base.AssnAnd {
      public void execute() { super.execute(); }
   }

   static public class AssnXor extends Base.AssnXor {
      public void execute() { super.execute(); }
   }

   static public class AssnOr extends Base.AssnOr {
      public void execute() { super.execute(); }
   }

   static public class ConditionalExpression extends Base.ConditionalExpression {
      public void execute() { super.execute(); }
   }

   static public class QuestExpr extends Base.QuestExpr {
      public void execute() { super.execute(); }
   }

   static public class ConditionalOrExpression extends Base.ConditionalOrExpression {
      public void execute() { super.execute(); }
   }

   static public class CondOrExpr extends Base.CondOrExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreCondOrExpr extends Base.MoreCondOrExpr {
      public void execute() { super.execute(); }
   }

   static public class COEBody extends Base.COEBody {
      public void execute() { super.execute(); }
   }

   static public class COEBod extends Base.COEBod {
      public void execute() { super.execute(); }
   }

   static public class ConditionalAndExpression extends Base.ConditionalAndExpression {
      public void execute() { super.execute(); }
   }

   static public class CondAndExpr extends Base.CondAndExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreCondAndExpr extends Base.MoreCondAndExpr {
      public void execute() { super.execute(); }
   }

   static public class CAEBody extends Base.CAEBody {
      public void execute() { super.execute(); }
   }

   static public class CAEBod extends Base.CAEBod {
      public void execute() { super.execute(); }
   }

   static public class InclusiveOrExpression extends Base.InclusiveOrExpression {
      public void execute() { super.execute(); }
   }

   static public class InclOrExpr extends Base.InclOrExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreInclOrExpr extends Base.MoreInclOrExpr {
      public void execute() { super.execute(); }
   }

   static public class IOEBody extends Base.IOEBody {
      public void execute() { super.execute(); }
   }

   static public class IOEBod extends Base.IOEBod {
      public void execute() { super.execute(); }
   }

   static public class ExclusiveOrExpression extends Base.ExclusiveOrExpression {
      public void execute() { super.execute(); }
   }

   static public class ExclOrExpr extends Base.ExclOrExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreExclOrExpr extends Base.MoreExclOrExpr {
      public void execute() { super.execute(); }
   }

   static public class EOEBody extends Base.EOEBody {
      public void execute() { super.execute(); }
   }

   static public class EOEBod extends Base.EOEBod {
      public void execute() { super.execute(); }
   }

   static public class AndExpression extends Base.AndExpression {
      public void execute() { super.execute(); }
   }

   static public class AndExpr extends Base.AndExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreAndExpr extends Base.MoreAndExpr {
      public void execute() { super.execute(); }
   }

   static public class AEBody extends Base.AEBody {
      public void execute() { super.execute(); }
   }

   static public class AEBod extends Base.AEBod {
      public void execute() { super.execute(); }
   }

   static public class EqualityExpression extends Base.EqualityExpression {
      public void execute() { super.execute(); }
   }

   static public class EqExpr extends Base.EqExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreEqExpr extends Base.MoreEqExpr {
      public void execute() { super.execute(); }
   }

   static public class EEBody extends Base.EEBody {
      public void execute() { super.execute(); }
   }

   static public class EEBodyC extends Base.EEBodyC {
      public void execute() { super.execute(); }
   }

   static public class EqExprChoices extends Base.EqExprChoices {
      public void execute() { super.execute(); }
   }

   static public class Eq extends Base.Eq {
      public void execute() { super.execute(); }
   }

   static public class Neq extends Base.Neq {
      public void execute() { super.execute(); }
   }

   static public class InstanceOfExpression extends Base.InstanceOfExpression {
      public void execute() { super.execute(); }
   }

   static public class IoExpr extends Base.IoExpr {
      public void execute() { super.execute(); }
   }

   static public class RelationalExpression extends Base.RelationalExpression {
      public void execute() { super.execute(); }
   }

   static public class RelExpr extends Base.RelExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreRelExpr extends Base.MoreRelExpr {
      public void execute() { super.execute(); }
   }

   static public class REBody extends Base.REBody {
      public void execute() { super.execute(); }
   }

   static public class REBod extends Base.REBod {
      public void execute() { super.execute(); }
   }

   static public class RelExprChoices extends Base.RelExprChoices {
      public void execute() { super.execute(); }
   }

   static public class LtOp extends Base.LtOp {
      public void execute() { super.execute(); }
   }

   static public class GtOp extends Base.GtOp {
      public void execute() { super.execute(); }
   }

   static public class LeOp extends Base.LeOp {
      public void execute() { super.execute(); }
   }

   static public class GeOp extends Base.GeOp {
      public void execute() { super.execute(); }
   }

   static public class ShiftExpression extends Base.ShiftExpression {
      public void execute() { super.execute(); }
   }

   static public class ShiftExpr extends Base.ShiftExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreShiftExpr extends Base.MoreShiftExpr {
      public void execute() { super.execute(); }
   }

   static public class SEBody extends Base.SEBody {
      public void execute() { super.execute(); }
   }

   static public class SEBodyC extends Base.SEBodyC {
      public void execute() { super.execute(); }
   }

   static public class ShiftExprChoices extends Base.ShiftExprChoices {
      public void execute() { super.execute(); }
   }

   static public class LShift extends Base.LShift {
      public void execute() { super.execute(); }
   }

   static public class RShift extends Base.RShift {
      public void execute() { super.execute(); }
   }

   static public class RRShift extends Base.RRShift {
      public void execute() { super.execute(); }
   }

   static public class AdditiveExpression extends Base.AdditiveExpression {
      public void execute() { super.execute(); }
   }

   static public class AddExpr extends Base.AddExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreAddExpr extends Base.MoreAddExpr {
      public void execute() { super.execute(); }
   }

   static public class AdEBody extends Base.AdEBody {
      public void execute() { super.execute(); }
   }

   static public class AdEBod extends Base.AdEBod {
      public void execute() { super.execute(); }
   }

   static public class AddExprChoices extends Base.AddExprChoices {
      public void execute() { super.execute(); }
   }

   static public class Plus extends Base.Plus {
      public void execute() { super.execute(); }
   }

   static public class Minus extends Base.Minus {
      public void execute() { super.execute(); }
   }

   static public class MultiplicativeExpression extends Base.MultiplicativeExpression {
      public void execute() { super.execute(); }
   }

   static public class MultExpr extends Base.MultExpr {
      public void execute() { super.execute(); }
   }

   static public class MoreMultExpr extends Base.MoreMultExpr {
      public void execute() { super.execute(); }
   }

   static public class MEBody extends Base.MEBody {
      public void execute() { super.execute(); }
   }

   static public class MEBod extends Base.MEBod {
      public void execute() { super.execute(); }
   }

   static public class MultExprChoices extends Base.MultExprChoices {
      public void execute() { super.execute(); }
   }

   static public class Mult extends Base.Mult {
      public void execute() { super.execute(); }
   }

   static public class Div extends Base.Div {
      public void execute() { super.execute(); }
   }

   static public class Mod extends Base.Mod {
      public void execute() { super.execute(); }
   }

   static public class UnaryExpression extends Base.UnaryExpression {
      public void execute() { super.execute(); }
   }

   static public class PlusUE extends Base.PlusUE {
      public void execute() { super.execute(); }
   }

   static public class MinusUE extends Base.MinusUE {
      public void execute() { super.execute(); }
   }

   static public class PreIncrementExpression extends Base.PreIncrementExpression {
      public void execute() { super.execute(); }
   }

   static public class PIncExpr extends Base.PIncExpr {
      public void execute() { super.execute(); }
   }

   static public class PreDecrementExpression extends Base.PreDecrementExpression {
      public void execute() { super.execute(); }
   }

   static public class PDecExpr extends Base.PDecExpr {
      public void execute() { super.execute(); }
   }

   static public class UnaryExpressionNotPlusMinus extends Base.UnaryExpressionNotPlusMinus {
      public void execute() { super.execute(); }
   }

   static public class TildeUE extends Base.TildeUE {
      public void execute() { super.execute(); }
   }

   static public class NotUE extends Base.NotUE {
      public void execute() { super.execute(); }
   }

   static public class CastLookahead extends Base.CastLookahead {
      public void execute() { super.execute(); }
   }

   static public class Cla1 extends Base.Cla1 {
      public void execute() { super.execute(); }
   }

   static public class Cla2 extends Base.Cla2 {
      public void execute() { super.execute(); }
   }

   static public class Cla3 extends Base.Cla3 {
      public void execute() { super.execute(); }
   }

   static public class CastLookaheadChoices extends Base.CastLookaheadChoices {
      public void execute() { super.execute(); }
   }

   static public class TildeLA extends Base.TildeLA {
      public void execute() { super.execute(); }
   }

   static public class BangLA extends Base.BangLA {
      public void execute() { super.execute(); }
   }

   static public class OpParenLA extends Base.OpParenLA {
      public void execute() { super.execute(); }
   }

   static public class IdLA extends Base.IdLA {
      public void execute() { super.execute(); }
   }

   static public class ThisLA extends Base.ThisLA {
      public void execute() { super.execute(); }
   }

   static public class SuperLA extends Base.SuperLA {
      public void execute() { super.execute(); }
   }

   static public class NewLA extends Base.NewLA {
      public void execute() { super.execute(); }
   }

   static public class LitLA extends Base.LitLA {
      public void execute() { super.execute(); }
   }

   static public class PostfixExpression extends Base.PostfixExpression {
      public void execute() { super.execute(); }
   }

   static public class PEIncDec extends Base.PEIncDec {
      public void execute() { super.execute(); }
   }

   static public class PEPostIncDec extends Base.PEPostIncDec {
      public void execute() { super.execute(); }
   }

   static public class PlusPlus2 extends Base.PlusPlus2 {
      public void execute() { super.execute(); }
   }

   static public class MinusMinus2 extends Base.MinusMinus2 {
      public void execute() { super.execute(); }
   }

   static public class CastExpression extends Base.CastExpression {
      public void execute() { super.execute(); }
   }

   static public class CastExpr1 extends Base.CastExpr1 {
      public void execute() { super.execute(); }
   }

   static public class CastExpr2 extends Base.CastExpr2 {
      public void execute() { super.execute(); }
   }

   static public class PrimaryExpression extends Base.PrimaryExpression {
      public void execute() { super.execute(); }
   }

   static public class PrimExpr extends Base.PrimExpr {
      public void execute() { super.execute(); }
   }

   static public class Suffixes extends Base.Suffixes {
      public void execute() { super.execute(); }
   }

   static public class PrimaryPrefix extends Base.PrimaryPrefix {
      public void execute() { super.execute(); }
   }

   static public class ThisPre extends Base.ThisPre {
      public void execute() { super.execute(); }
   }

   static public class SuperPre extends Base.SuperPre {
      public void execute() { super.execute(); }
   }

   static public class ExprPre extends Base.ExprPre {
      public void execute() { super.execute(); }
   }

   static public class RTPre extends Base.RTPre {
      public void execute() { super.execute(); }
   }

   static public class PPQualName extends Base.PPQualName {
      public void execute() { super.execute(); }
   }

   static public class PrimarySuffix extends Base.PrimarySuffix {
      public void execute() { super.execute(); }
   }

   static public class ThisSuf extends Base.ThisSuf {
      public void execute() { super.execute(); }
   }

   static public class AllocSuf extends Base.AllocSuf {
      public void execute() { super.execute(); }
   }

   static public class ExprSuf extends Base.ExprSuf {
      public void execute() { super.execute(); }
   }

   static public class QNameSuf extends Base.QNameSuf {
      public void execute() { super.execute(); }
   }

   static public class MthCall extends Base.MthCall {
      public void execute() { super.execute(); }
   }

   static public class Literal extends Base.Literal {
      public void execute() { super.execute(); }
   }

   static public class IntLit extends Base.IntLit {
      public void execute() { super.execute(); }
   }

   static public class FPLit extends Base.FPLit {
      public void execute() { super.execute(); }
   }

   static public class CharLit extends Base.CharLit {
      public void execute() { super.execute(); }
   }

   static public class StrLit extends Base.StrLit {
      public void execute() { super.execute(); }
   }

   static public class BooleanLiteral extends Base.BooleanLiteral {
      public void execute() { super.execute(); }
   }

   static public class True extends Base.True {
      public void execute() { super.execute(); }
   }

   static public class False extends Base.False {
      public void execute() { super.execute(); }
   }

   static public class NullLiteral extends Base.NullLiteral {
      public void execute() { super.execute(); }
   }

   static public class Null extends Base.Null {
      public void execute() { super.execute(); }
   }

   static public class Arguments extends Base.Arguments {
      public void execute() { super.execute(); }
   }

   static public class Args extends Base.Args {
      public void execute() { super.execute(); }
   }

   static public class AST_ArgList extends Base.AST_ArgList {
      public void execute() { super.execute(); }
   }

   static public class AllocationExpression extends Base.AllocationExpression {
      public void execute() { super.execute(); }
   }

   static public class PrimAllocExpr extends Base.PrimAllocExpr {
      public void execute() { super.execute(); }
   }

   static public class ObjAllocExpr extends Base.ObjAllocExpr {
      public void execute() { super.execute(); }
   }

   static public class AllocExprChoices extends Base.AllocExprChoices {
      public void execute() { super.execute(); }
   }

   static public class AnonClass extends Base.AnonClass {
      public void execute() { super.execute(); }
   }

   static public class ArrayDimsAndInits extends Base.ArrayDimsAndInits {
      public void execute() { super.execute(); }
   }

   static public class ArrDim1 extends Base.ArrDim1 {
      public void execute() { super.execute(); }
   }

   static public class ArrDim2 extends Base.ArrDim2 {
      public void execute() { super.execute(); }
   }

   static public class ExprDims extends Base.ExprDims {
      public void execute() { super.execute(); }
   }

   static public class ExDimBody extends Base.ExDimBody {
      public void execute() { super.execute(); }
   }

   static public class ExDimBod extends Base.ExDimBod {
      public void execute() { super.execute(); }
   }

   static public class Statement extends Base.Statement {
      public void execute() { super.execute(); }
   }

   static public class ExprStmt extends Base.ExprStmt {
      public void execute() { super.execute(); }
   }

   static public class LabeledStatement extends Base.LabeledStatement {
      public void execute() { super.execute(); }
   }

   static public class LabeledStmt extends Base.LabeledStmt {
      public void execute() { super.execute(); }
   }

   static public class Block extends Base.Block {
      public void execute() { super.execute(); }
   }

   static public class BlockC extends Base.BlockC {
      public void execute() { super.execute(); }
   }

   static public class BlockStatement extends Base.BlockStatement {
      public void execute() { super.execute(); }
   }

   static public class BlockStmt extends Base.BlockStmt {
      public void execute() { super.execute(); }
   }

   static public class BlkClassDcl extends Base.BlkClassDcl {
      public void execute() { super.execute(); }
   }

   static public class BlkInterDcl extends Base.BlkInterDcl {
      public void execute() { super.execute(); }
   }

   static public class LocalVariableDeclaration extends Base.LocalVariableDeclaration {
      public void execute() { super.execute(); }
   }

   static public class LocalVarDecl extends Base.LocalVarDecl {
      public void execute() { super.execute(); }
   }

   static public class EmptyStatement extends Base.EmptyStatement {
      public void execute() { super.execute(); }
   }

   static public class Empty extends Base.Empty {
      public void execute() { super.execute(); }
   }

   static public class AST_ExpStmt extends Base.AST_ExpStmt {
      public void execute() { super.execute(); }
   }

   static public class StatementExpression extends Base.StatementExpression {
      public void execute() { super.execute(); }
   }

   static public class PIExpr extends Base.PIExpr {
      public void execute() { super.execute(); }
   }

   static public class PDExpr extends Base.PDExpr {
      public void execute() { super.execute(); }
   }

   static public class PEStmtExpr extends Base.PEStmtExpr {
      public void execute() { super.execute(); }
   }

   static public class StmtExprChoices extends Base.StmtExprChoices {
      public void execute() { super.execute(); }
   }

   static public class PlusPlus extends Base.PlusPlus {
      public void execute() { super.execute(); }
   }

   static public class MinusMinus extends Base.MinusMinus {
      public void execute() { super.execute(); }
   }

   static public class AssnExpr extends Base.AssnExpr {
      public void execute() { super.execute(); }
   }

   static public class SwitchStatement extends Base.SwitchStatement {
      public void execute() { super.execute(); }
   }

   static public class SwitchStmt extends Base.SwitchStmt {
      public void execute() { super.execute(); }
   }

   static public class AST_SwitchEntry extends Base.AST_SwitchEntry {
      public void execute() { super.execute(); }
   }

   static public class SwitchEntryBody extends Base.SwitchEntryBody {
      public void execute() { super.execute(); }
   }

   static public class SEBod extends Base.SEBod {
      public void execute() { super.execute(); }
   }

   static public class SwitchLabel extends Base.SwitchLabel {
      public void execute() { super.execute(); }
   }

   static public class CaseLabel extends Base.CaseLabel {
      public void execute() { super.execute(); }
   }

   static public class DefLabel extends Base.DefLabel {
      public void execute() { super.execute(); }
   }

   static public class IfStatement extends Base.IfStatement {
      public void execute() { super.execute(); }
   }

   static public class IfStmt extends Base.IfStmt {
      public void execute() { super.execute(); }
   }

   static public class ElseClause extends Base.ElseClause {
      public void execute() { super.execute(); }
   }

   static public class ElseClauseC extends Base.ElseClauseC {
      public void execute() { super.execute(); }
   }

   static public class WhileStatement extends Base.WhileStatement {
      public void execute() { super.execute(); }
   }

   static public class WhileStm extends Base.WhileStm {
      public void execute() { super.execute(); }
   }

   static public class DoStatement extends Base.DoStatement {
      public void execute() { super.execute(); }
   }

   static public class DoWhileStm extends Base.DoWhileStm {
      public void execute() { super.execute(); }
   }

   static public class ForStatement extends Base.ForStatement {
      public void execute() { super.execute(); }
   }

   static public class ForStmt extends Base.ForStmt {
      public void execute() { super.execute(); }
   }

   static public class ForInit extends Base.ForInit {
      public void execute() { super.execute(); }
   }

   static public class FIExprList extends Base.FIExprList {
      public void execute() { super.execute(); }
   }

   static public class StatementExpressionList extends Base.StatementExpressionList {
      public void execute() { super.execute(); }
   }

   static public class ForUpdate extends Base.ForUpdate {
      public void execute() { super.execute(); }
   }

   static public class StmExprList extends Base.StmExprList {
      public void execute() { super.execute(); }
   }

   static public class BreakStatement extends Base.BreakStatement {
      public void execute() { super.execute(); }
   }

   static public class BreakStm extends Base.BreakStm {
      public void execute() { super.execute(); }
   }

   static public class ContinueStatement extends Base.ContinueStatement {
      public void execute() { super.execute(); }
   }

   static public class ContinueStm extends Base.ContinueStm {
      public void execute() { super.execute(); }
   }

   static public class ReturnStatement extends Base.ReturnStatement {
      public void execute() { super.execute(); }
   }

   static public class ReturnStm extends Base.ReturnStm {
      public void execute() { super.execute(); }
   }

   static public class ThrowStatement extends Base.ThrowStatement {
      public void execute() { super.execute(); }
   }

   static public class ThrowStm extends Base.ThrowStm {
      public void execute() { super.execute(); }
   }

   static public class SynchronizedStatement extends Base.SynchronizedStatement {
      public void execute() { super.execute(); }
   }

   static public class SyncStmt extends Base.SyncStmt {
      public void execute() { super.execute(); }
   }

   static public class TryStatement extends Base.TryStatement {
      public void execute() { super.execute(); }
   }

   static public class TryStmt extends Base.TryStmt {
      public void execute() { super.execute(); }
   }

   static public class Finally extends Base.Finally {
      public void execute() { super.execute(); }
   }

   static public class FinallyC extends Base.FinallyC {
      public void execute() { super.execute(); }
   }

   static public class AST_Catches extends Base.AST_Catches {
      public void execute() { super.execute(); }
   }

   static public class Catch extends Base.Catch {
      public void execute() { super.execute(); }
   }

   static public class CatchStmt extends Base.CatchStmt {
      public void execute() { super.execute(); }
   }

	
 


 // ************************************************************************
 // ************************************************************************
 // ************************************************************************


   static public  abstract class AstNode extends Base.AstNode {
      public void execute() { 
         int i;
         if (arg == null)
            return;
         for (i=0; i<arg.length; i++)
            if (arg[i]!=null)
               arg[i].execute();
      }
   }

   static public  abstract class AstList extends Base.AstList {
      public void execute() {
         Lang.AstNode l;
         if (arg[0]==null) return;
         for (l = arg[0]; l!=null; l = l.right) {
            if (l.arg[0] == null) 
                continue;
            l.arg[0].execute();
         }
      }
   }

   static public  abstract class AstListNode extends Base.AstListNode {
      public void execute() { 
          Util.fatalError("AstListNode.execute() method should not be called");
        }
   }

   static public class AstOptNode extends Base.AstOptNode {
      public void execute() {
         if (arg[0]!=null)
            arg[0].execute(); 
      }
      public AstOptNode( ) { super(); }
   }


    static public class Main extends Base.Main {

	private static void usage() {
	    System.err.println("Usage: java " + "Lang" +
			       ".Main [-f file]");
	    System.err.println("       -f for input from file");
	    System.exit(-10);
	}
   
	public static void main(String args[]) {
	    int     argc    = args.length;
	    int                non_switch_args;
	    int                i, j;
	    char               ch;
	    Lang.AstProperties props;
	    BaliParser         myParser = null;
	    Lang.AstNode       root;
	    PrintWriter        pw;
	    String             line;     // one line from the user
	    String             input;    // one Language command (terminated with line ".")

	    ByteArrayInputStream is;     // is and dis are used together 
	    DataInputStream      dis;    // to "feed" the scanner.
	    BufferedReader       userInput = null;

	    // Step 1: print the Marquee...

	    // Step 2: a general routine to pick off command line options
	    //         options are removed from command line and
	    //         args array is adjusted accordingly.
        /*
	    non_switch_args = 0;
	    for (i=0; i < argc; i++) {
		if (args[i].charAt(0) == '-') {

		    // switches of form -xxxxx (where xxx is a sequence of 1
		    // or more characters

		    for (j=1; j < args[i].length(); j++) {
			if (args[i].charAt(j) == 'f') {
			    try {
				userInput =
				    new BufferedReader(new FileReader(args[i+1]));
				_program_name = args[i+1];    
			    }
			    catch (Exception e) {
				System.err.println("File " + args[i+1] + " not found:" 
				                  + e.getMessage());
			    }
			    i++;
			    break;
			}
			else
			    usage();
		    }
		}
		else {
		    // non-switch arg
		    args[non_switch_args] = args[i];
		    non_switch_args++;
		}
	    }
        */
	    // Step 3: there must be at least one real argument, otherwise error
        /* 
	    if (non_switch_args != 0)
		usage();
        */

		// *******************************************************************
		// *******************************************************************
		// *******************************************************************

		try {
			userInput =
			    new BufferedReader(new FileReader(args[0]));
			_program_name = args[0];    
		    }
		    catch (Exception e) {
			System.err.println("File " + args[0] + " not found:" 
			                  + e.getMessage());
		    }
		OutputDirectory = args[1];
		// *******************************************************************
		// *******************************************************************
		// *******************************************************************
				        
	    // Step 4: Initialize output stream
	    //         Standard initialization stuff that should be
	    //         platform independent.

	    props = new Lang.AstProperties();
	    String lineSeparator =
		System.getProperties().getProperty("line.separator");
	    if (lineSeparator.compareTo("\n") != 0)
		pw = new PrintWriter(new FixDosOutputStream(System.out));
	    else
		pw = new PrintWriter(System.out);
	    props.setProperty("output", pw);
    
	    // Step 5: Get input and parse until an empty line is entered.
	    //         An empty line is something with "." only.

	    if (userInput == null)
		userInput = new BufferedReader(new InputStreamReader(System.in));

	    do {                  // LanguageName statement loop
		input = "";       // initialize input string

		// Step 6.1: print prompt
                 
                // Modification !!! 
		// System.out.print("\n" + $str($PackName) + "> ");
		System.out.flush();

		// Step 5.2: collect in variable input over multiple line reads
		do {
		    line = "";
		    try {
			line = userInput.readLine();
		    }
		    catch (Exception e) {
			System.exit(10);
		    }
                    if (line == null) break; 
		    if (line.compareTo("") == 0)
			continue; 
		    if (line.compareTo(".") == 0)
			break;
		    input += "\n" + line;
		    // Modification !!!
                    // System.out.print(" > ");
		    System.out.flush();
		} while (true);    

		if (input == "")
		    break;

		// Step 5.3: parse input string

		is  = new ByteArrayInputStream(input.getBytes());
		dis = new DataInputStream(is);
		if (myParser == null)
		    myParser = new BaliParser(dis);
		else
		    myParser.ReInit(dis);

		try {
		    root = getStartRoot(myParser);
		}
		catch (Throwable e) {
		    System.out.println("Parsing Exception Thrown: " +
				       e.getMessage());
		    e.printStackTrace();
		    continue;     // go to next $(LanguageName) statement
		}
    
		// Step 5.4: Parse of input command succeeded!
             // Modification !!!
             // ((Lang.AST_Program) root).print();
             System.out.println();
             ((Lang.AST_Program) root).execute();
             pw.flush();

             // Step 5.5: now dump the parse tree
             //           this code can be removed for production systems

             // System.out.println("Dump root");
             // root.PrettyDump();

         } while (true);          // end Language statement loop
     } //end main()
  }  // end Main class

};