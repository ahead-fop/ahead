
		package theParser; import
	java.util.Vector; import
	Jakarta.util.FixDosOutputStream; import
        Jakarta.util.Util; import
        java.io.*; import
        jtsdoc.*; import 
        com.sun.javadoc.*; import 
        java.util.*; import
        CommentParsing.*; public class layers extends Java 
{
	
  static public class AST_Program extends Java.AST_Program {
   }

   static public class TE_Generator extends Java.TE_Generator {
   }

   static public class TEGen extends Java.TEGen {
   }

   static public class LDecl extends Java.LDecl {
   }

   static public class LayerHeader extends Java.LayerHeader {
   }

   static public class LyrHeader extends Java.LyrHeader {
   }

   /** This is the core class of a Layer. Its rule is 
    * Layer_Decl
	: [ LayerModifiers ] LAYER QName "(" [ AST_ParList ] ")"
	  REALM QName [ SuperLayer ] [ LayerImports ]
	  ClassBody	::LayerDecl
	;
    */
   static public class Layer_Decl extends Java.Layer_Decl {
      public LayerDocJTS _harvest_layer;
	   	   
	   public void execute() 
	   { 
		// All this have to be part of execute
		// The only thing visible from outside is _harvest_layer
		String _harvest_name;
		String comment;
		DOCLETJTS.Lang.ClassDocJTS _harvest_class;

		  // Creates the reference to the layer
	      _harvest_name = ((Lang.AstToken) arg[1].tok[0]).tokenName();
 	      _harvest_layer = new LayerDocJTS(_harvest_name);
		
		   // [LayerModifiers] Optional node
		   Lang.AstNode e0 = arg[0].arg[0];
		   if (e0!=null)
		   { // there are modifiers
			  Lang.AstCursor c1 = new AstCursor();  // c2 to traverse list
              Lang.AstNode   e1 = e0;        // Imports List	
              
			  // Gets the first modifier and reads the comment from it
			  c1.FirstElement(e1);
			  if (c1.node instanceof NonRelMod)
			  { comment = ((Lang.AstToken) c1.node.arg[0].tok[0]).white_space; }
			  else // relative layer
				comment = ((Lang.AstToken) c1.node.tok[0]).white_space;              
              	  
			  for (; c1.MoreElement(); c1.NextElement())
			  {   
				  if (c1.node instanceof NonRelMod)
				  {
					  if (c1.node.arg[0] instanceof ModPublic)
						  _harvest_layer.setPublic(true);
					  if (c1.node.arg[0] instanceof ModAbstract)
						  _harvest_layer.setAbstract(true);
					  if (c1.node.arg[0] instanceof ModFinal)
						  _harvest_layer.setFinal(true);
  					  if (c1.node.arg[0] instanceof ModProtected)
						  _harvest_layer.setProtected(true);
					  if (c1.node.arg[0] instanceof ModPrivate)
						  _harvest_layer.setPrivate(true);
					  if (c1.node.arg[0] instanceof ModStatic)
						  _harvest_layer.setStatic(true);					  
					  if (c1.node.arg[0] instanceof ModTransient)
						  _harvest_layer.setTransient(true);					  
					  if (c1.node.arg[0] instanceof ModVolatile)
						  _harvest_layer.setVolatile(true);					  
					  if (c1.node.arg[0] instanceof ModNative)
						  _harvest_layer.setNative(true);
					  if (c1.node.arg[0] instanceof ModSynchronized)
						  _harvest_layer.setSynchronized(true);					  
				  }				  
				  if (c1.node instanceof ModRelative)
				  {
					_harvest_layer.setRelative(true);  
				  }
			  }			   
		   } // if there are  modifiers
		   else
		   {  // if there are no modifiers
			   comment = ((Lang.AstToken) tok[0]).white_space;
		   }
		   		   
		   // Layer Name arg[1]
           // _harvest_class = new DOCLETJTS.Lang.ClassDocJTS(arg[1].toString());
		   
		   super.execute(); 
		   
		   // [AST_ParList] optional parameter list
		  Lang.AST_ParList kid = (AST_ParList)arg[2].arg[0];
		  if (kid != null) 
		  { // if there are parameters
			  _harvest_layer.setParameters(kid.parametersArray);
		  }
		  else 
		  { // if there are no parameters
			  _harvest_layer.setParameters(new ParameterJTS[0]);
		  }
		  
		  // Realm name arg[3]
		  _harvest_layer.setRealmName(arg[3].toString());
		  
		  // [Superlayer] optional
  		  Lang.AstNode   e1 = arg[4].arg[0]; // checks to see if it is present
          if (e1!=null)
		  { // there is a super layer
			_harvest_layer.setSuperLayer(e1.arg[0].toString());
		  } 
		  
		  // [LayerImports] optional list
		  Lang.AstNode e2 = arg[5].arg[0];
		  if (e2!=null)
		  { // there is a list of Layer imports
			  // LayerImports -> LyrImports -> ImportList
			  // arg[5] OptNode, e2 LyrImports, e3 AstNodeList
			  // Each element of the list is of type ImportPak
			  Lang.AstCursor c2 = new AstCursor();  // c2 to traverse list
              Lang.AstNode   e3 = e2.arg[0];        // Imports List		  
		 	  LinkedList importsList = new LinkedList();		  
			  for (c2.FirstElement(e3); c2.MoreElement(); c2.NextElement())
			  {  
			   if (c2.node instanceof ImportPak)
				 {  
					 String importName = getAST_QualifiedName((Lang.AST_QualifiedName)c2.node.arg[0]);

					 // In case there are .* at the end of the import package clause
					 if (c2.node.arg[1].arg[0] != null) 
						 importName = importName + ".*";
					 
					 importsList.add(importName);					
				 }	
			  } // for all the imports
			  
			  String[] arrayImports = new String[importsList.size()];
			  for (int l=0; l< importsList.size(); l++)
				  arrayImports[l] = (String) importsList.get(l);
			  _harvest_layer.setImportsList(arrayImports);
		  }
		  else
		  { // no imports
			  _harvest_layer.setImportsList(new String[0]);
		  }
		  
		   // Classbody arg[6]
		   // Whatever was harvested in ClassBody get it from there
		   // and set the name of the class to be the layer name.
		  _harvest_class = ((ClassBody)arg[6])._harvest_class;
		  _harvest_class.setName(arg[1].toString());
		  
		   // Sets the classbody
		   _harvest_layer.setClassBody(_harvest_class);
		   
   		   // ************ Comment Processing ************************************
		   System.out.println("Comment to parse " + comment);
		   CommentJTS layerComment = 
				CommentParser.ParserCommentLayer(_harvest_layer, comment);
           _harvest_layer.setComment(layerComment);
		   System.out.println("comment parsed");
		   
		   // *********************************************************************

		   // Here it is where it creates and calls the doclet
		   // to generate the layer file.
		   try {
		     DocletImpl.Lang.DocletJTS.writeALayer(_harvest_layer, OutputDirectory);
		   } catch (IOException e)
		   {
		   }	   
	   } //  end of execute
   }

   static public class LayerDecl extends Java.LayerDecl {
   }

   static public class LayerModifiers extends Java.LayerModifiers {
   }

   static public class LayerModifier extends Java.LayerModifier {
   }

   static public class NonRelMod extends Java.NonRelMod {
   }

   static public class ModRelative extends Java.ModRelative {
   }

   static public class SuperLayer extends Java.SuperLayer {
   }

   static public class SupLayer extends Java.SupLayer {
   }

   static public class LayerImports extends Java.LayerImports {
   }

   static public class LyrImports extends Java.LyrImports {
   }

   static public class ImportList extends Java.ImportList {
   }

   static public class ImportPack extends Java.ImportPack {
   }

   static public class ImportPak extends Java.ImportPak {
   }

};