
		package theParser; import
	Jakarta.util.Util; import
	java.io.ByteArrayOutputStream; import
	java.io.FileOutputStream; import
	java.io.IOException; import
	java.io.PrintWriter; import
        java.io.ObjectOutputStream; import
	java.io.Serializable; import
	java.util.Hashtable; import
	java.util.Stack; import
	java.util.Vector; public class kernel

{

    //**************************************************
    // Class AstList
    //**************************************************
    static public abstract class AstList extends Lang.AstNode {

	public Lang.AstNode last;     // reference to last node on list

	// CheckElemType and ElemType are abstract methods that
	// are implemented by (final) subclasses of AstList

	abstract public boolean CheckElemType( Lang.AstNode l );
	abstract public String ElemType();

	// Here is the constructor as required by JavaCup

// 	public AstList(int n) {
// 	    // initialize all the standard stuff
// 	    super(n);
// 	    arg     = new $TEqn.AstNode[1];
// 	    arg[0]  = null;
// 	    last    = null;
// 	}

	public AstList() {
	    // initialize all the standard stuff
	    arg     = new Lang.AstNode[1];
	    arg[0]  = null;
	    last    = null;
	}

	public Object clone() {
	    Lang.AstList copy;

	    copy = null;
	    try {
		copy = (Lang.AstList) getClass().newInstance();
	    }
	    catch (Exception e) {
		Util.fatalError("Can't clone "+getClass(), e);
	    }
	    super.initClone(copy);
	    initClone(copy);  // last + element up pointers
	    return(copy);
	}

	protected void initClone(Lang.AstList copy) {
	    Lang.AstNode l = copy.arg[0];

	    // Step 1: if the list is empty, exit

	    copy.last = null;
	    if (l==null)  return;  

	    // Step 2: foreach element on the list, set its up pointer
	    //         and find the last element on the list

	    l.up = copy;
	    while (l.right != null) {
		l.up = copy;
		l = l.right;
	    }

	    // Step 3: found last element - set its up pointer and
	    //         set last

	    l.up = copy;
	    copy.last = l;
	}

	// printorder() normally returns a boolean array that indicates how
	// tokens and arguments are to be interlaced when printing.  AstList
	// nodes have a special printing algorithm, and thus printorder should
	// never be called

	public boolean[] printorder() {
	    FatalError err = new FatalError("shouldn't call AstList.printorder()");
	    Util.fatalError(err);
	    return(new boolean[0]);
	}

	// dumpnode() displays the pointer contents (up, left, right, args, last)
	// of a list and its descendents.

	public void dumpnode() {  // dump list head node
	    Lang.AstNode l;

	    super.dumpnode();

	    // 	if (last == null)
	    // 	    ps.println("\tlast:*");
	    // 	else
	    // 	    ps.println("\tlast:"+last.hashCode());

	    if (arg[0] == null)
		return;
	    for (l=arg[0].right; l!=null; l=l.right)
		l.dumpnode();
	}

	// Delete() will delete the list.  Normally, this simply means
	// discarding all elements of the list (by setting the arg[0] and last
	// pointers to null.  However, if the list itself is an element of
	// another list, i.e., the up is an instanceof AstListNode, then up
	// is deleted (i.e., that element is removed from the enclosing list).

	public void Delete( ) {  // delete the list

	    // Step 1: if this list is an element of list, simply delete the
	    //         element

	    if (up instanceof Lang.AstListNode) {
		((Lang.AstListNode) up).Delete();
		return;
	    }

	    // Step 2: else remove all children by setting the arg[0], last
	    //         references to null

	    arg[0] = null;
	    last   = null;
	}

	// add(x) adds AstListNode x to the end of the list.  add is generally
	// called only during parsing, and typically is not called by Jakarta
	// users.

	// add x to end of this list
	public AstList add(Lang.AstListNode x) {
	    if (x == null)
		return(this);

	    // Remove x from old list, if it's on one.
	    if (x.up != null)
		x.Delete();

	    // Insert x into new list
	    if (last == null) {
		// empty list
		last    = x;
		arg[0]  = x;
		x.right = null;
		x.left  = null;
	    } else {
		last.right = x;
		x.left     = last;
		last       = x;
	    }
	    x.up    = this;
	    return(this);
	}

	public AstList add(Lang.AstList newList) {
	    Lang.AstNode n;

	    // Test if new list empty
	    if ((newList == null) || (newList.last == null))
		return(this);

	    // Change parent of inserted list nodes
	    for (n = newList.arg[0]; n != null; n = n.right)
		n.up = this;

	    if (last == null) {
		// original list is empty
		arg[0] = newList.arg[0];
	    }
	    else {
		last.right = newList.arg[0];
		newList.arg[0].left = last;
	    }
	    last = newList.last;

	    // Remove any dangling pointers from old list
	    newList.arg[0] = null;
	    newList.last = null;

	    return(this);
	}

	// Same as add() but adds at the head of the list.
	public AstList addHead(Lang.AstListNode x) {
	    x.left = null;
	    x.right = arg[0];
	    x.up = this;
	    arg[0] = x;
	    if (last == null)
		last = x;
	    return(this);
	}

	public AstList addHead(Lang.AstList newList) {
	    Lang.AstNode n;

	    // Test if new list empty
	    if ((newList == null) || (newList.last == null))
		return(this);

	    // Change parent of inserted list nodes
	    for (n=newList.arg[0]; n != null; n = n.right)
		n.up = this;

	    if (last == null) {
		// original list is empty
		last = newList.last;
	    }
	    else {
		newList.last.right = arg[0];
		arg[0].left = newList.last;
	    }
	    arg[0] = newList.arg[0];

	    // Remove any dangling pointers from old list
	    newList.arg[0] = null;
	    newList.last = null;

	    return(this);
	}

	// SyntaxCheck() examines every element on the list and makes
	// sure that they are of the correct type.  SyntaxCheck is used
	// ensure that the AST is syntactically correct (after being
	// transformed).  ASTs are syntactically correct when created by
	// JavaCup.

	public boolean SyntaxCheck() {
	    Lang.AstNode l;
	    boolean result = true;

	    for (l = arg[0]; l != null; l = l.right)
		if (!CheckElemType(l)) {
		    TypeError("argument of list",ElemType());
		    result = false;
		}
		else result = result && l.SyntaxCheck();
	    return result;
	}

	// print() prints the list.  This is a special method that overrides
	// the generic method of AstNode.  The same thing holds for reduce2ast()
	// and the reduce2java() methods.

	public void print() {
	    Lang.AstNode l;

	    // Step 1: return if the list is empty

	    if (arg[0] == null)
		return;

	    // Step 2: print the first node differently than the rest

	    arg[0].arg[0].print();
	    for (l = arg[0].right; l != null; l = l.right) {
		l.Print_Only_Tokens();
		l.arg[0].print();
	    }
	}

	public void print(Lang.AstProperties props) {
	    Lang.AstNode l;

	    // Step 1: return if the list is empty

	    if (arg[0] == null)
		return;

	    // Step 2: print the first node differently than the rest

	    // Note: the following code assumes that elements of a list
	    //       can reference null constructs - this probably is
	    //       wrong, but we're going with it now.  Probably what
	    //       needs to be done is to normalize the list so that
	    //       union (a,b) (c,d) is (a,b,c,d) rather than (a,b,(c,d)).
	    //       see original version of AstList.java - it probably
	    //       is right given that lists are normalized...

	    for (l = arg[0]; l != null; l = l.right) {
		if (l.arg[0] == null) 
		    continue;
		l.arg[0].print(props);
		l = l.right;
		break;
	    }
	    for ( ; l != null; l = l.right) {
		if ( l.arg[0] == null) 
		    continue;
		l.Print_Only_Tokens(props);
		l.arg[0].print(props);
	    }
	}

	public void reduce2java(Lang.AstProperties props) {
	    Lang.AstNode l;

	    // Step 1: return if the list is empty

	    if (arg[0] == null)
		return;

	    // Step 2: print the first node differently than the rest

	    // Note: the following code assumes that elements of a list
	    //       can reference null constructs - this probably is
	    //       wrong, but we're going with it now.  Probably what
	    //       needs to be done is to normalize the list so that
	    //       union (a,b) (c,d) is (a,b,c,d) rather than (a,b,(c,d)).
	    //       see original version of AstList.java - it probably
	    //       is right given that lists are normalized...

	    for (l = arg[0]; l != null; l = l.right) {
		if (l.arg[0] == null) 
		    continue;
		l.arg[0].reduce2java(props);
		l = l.right;
		break;
	    }
	    for ( ; l != null; l = l.right) {
		if ( l.arg[0] == null) 
		    continue;
		l.Print_Only_Tokens(props);
		l.arg[0].reduce2java(props);
	    }
	}

	public void PDump(String indent) {
	    int     i;
	    Lang.AstNode l;
	    String  id;

	    System.out.println(indent + indent.length() + className());
	    id = indent+" ";
	    for (l = arg[0]; l != null; l = l.right)
		l.PDump(id);
	}


	public Lang.AstNode addComment(String comment) {
	    return(addComment(comment, false));
	}

	public Lang.AstNode addComment(String comment, boolean replace) {
	    Lang.AstNode l;

	    for (l=arg[0]; l != null; l = l.right)
		if (l.arg[0] != null)
		    if (l.arg[0].addComment(comment, replace) != null)
			return(this);
	    return(null);
	}
    }


    //**************************************************
    // Class AstListNode
    //**************************************************
    static public abstract class AstListNode extends Lang.AstNode
    implements Cloneable {

	abstract public boolean CheckArgType();

	// name of arg[0] element type (for syntax checking)
	abstract public String  ArgType();

	// Here are the constructor and setParms methods that
	// are required by JavaCup

// 	public AstListNode(int n) {
// 	    super(n);
// 	}

	public Lang.AstListNode setParms(Lang.AstNode _arg1) {
	    arg     = new Lang.AstNode[1];
	    arg[0]  = _arg1;

	    // if list is empty just return

	    if (_arg1 == null) 
		return((Lang.AstListNode) this);

	    // now update traversal pointers of child

	    arg[0].left = null;
	    arg[0].up   = this;
	    arg[0].right = null;
	    return((Lang.AstListNode) this);
	}

	public Object clone() {
	    Lang.AstListNode copy;

	    copy = null;
	    try {
		copy = (Lang.AstListNode) getClass().newInstance();
	    }
	    catch (Exception e) {
		e.printStackTrace(System.err);
		System.err.println("Can't clone "+getClass());
		System.exit(8394);
	    }
	    super.initClone(copy);	// copy child
	    initClone(copy);	// copy right
	    return(copy);
	}

	protected void initClone(Lang.AstListNode copy) {
	    if (right != null) {
		copy.right = (Lang.AstNode) right.clone();
		copy.right.left = copy;
	    }
	}

	// printorder() normally returns a boolean array that indicates how
	// tokens and arguments are to be interlaced when printing.  AstList
	// nodes have a special printing algorithm, and thus printorder should
	// never be called

	public boolean[] printorder() {
	    Util.fatalError("shouldn't call AstListNode::printorder()");
	    return(new boolean[0]);
	}

	// Delete() the element from the list.  If the list becomes empty, then
	// the list itself is deleted.  Note that list deletion might not
	// entail much unless we are deleting a list that is an element of
	// another list (i.e., list of lists).

	public void Delete() { // physically remove element from list
	    AstList head;

	    // Step 1: fix left and right pointers
	    if (left != null)
		left.right = right;
	    if (right != null)
		right.left = left;

	    // Step 2: fix parent (which is an AstList instance)

	    head = (AstList) up;
	    if (head.last == this)
		head.last = left;
	    if (head.arg[0] == this)
		head.arg[0] = right;

	    // Step 3: now, check to see if the list on which this element
	    //         is empty.  If so, delete the list

	    if (head.arg[0] == null)
		head.Delete();

	    // Clear neighbor pointers
	    left = null;
	    right = null;
	}

	// AddAfter(l) adds (splices) list l immediately after this node
	// if l is an empty list, AddAfter(l) does nothing.  Upon return,
	// l is an empty list.

	public void AddAfter(Lang.AstList tree) {
	    Lang.AstNode nxt, fst, lst, l;
	    Lang.AstList par = (Lang.AstList) up;

	    // Step 1: ignore operation that inserts an empty tree

	    if (tree == null || tree.arg[0] == null) return;

	    // Step 2: remember references to common ptrs: next, first, last

	    nxt = right;
	    fst = tree.arg[0];
	    lst = tree.last;

	    // Step 3: fix the up pointers on the list we are adding

	    for (l = fst; l != null; l = l.right)
		l.up = par;

	    // Step 4: connect links in the right direction

	    lst.right = nxt;
	    right     = fst;

	    // Step 5: connect links in the left direction

	    fst.left = this;
	    if (nxt != null)
		nxt.left = lst;

	    // Step 6: update up.last if necessary

	    if (par.last == this)
		par.last = lst;

	    // Step 7: make sure that the tree is no longer
	    //         accessable

	    tree.up = null;
	    tree.Delete();
	}

	// AddBefore(l) splices list l immediately before the current node
	// otherwise same semantics as AddAfter

	public void AddBefore(Lang.AstList tree) {
	    Lang.AstNode prv, fst, lst, l;
	    Lang.AstList par = (Lang.AstList) up;

	    // Step 1: ignore operation that inserts an empty tree

	    if (tree == null || tree.arg[0] == null) return;

	    // Step 2: remember common pointers: previous, first, last

	    prv = left;
	    fst =  tree.arg[0];
	    lst = tree.last;

	    // Step 3: fix the up pointers on the list we are adding

	    for (l = fst; l != null; l = l.right)
		l.up = par;

	    // Step 4: connect links in the right direction

	    lst.right = this;
	    if (prv != null)
		prv.right = fst;

	    // Step 5: connect links in the left direction

	    fst.left = prv;
	    left = lst;

	    // Step 6: update up.arg[0] if necessary

	    if (par.arg[0] == this)
		par.arg[0] = fst;

	    // Step 7: make sure that the root of tree is no longer
	    //         accessable

	    tree.up = null;
	    tree.Delete();
	}

	// SyntaxCheck() ensures that the current element is of the correct type

	public boolean SyntaxCheck() {
	    boolean result = CheckArgType();

	    if (!result) {
		TypeError("arg[0]",ArgType());
		result = false;
	    }
	    return result;
	}

	// print() and reduce2java() should never be called.  List printing
	// is done by AstList::print() and AstList::reduce2java().

	public void print() {
	    System.out.println("Error - AstListNode::print should not be called");
	}

	public void print(Lang.AstProperties props) {
	    System.out.println("Error - AstListNode::print should not be called");
	}

	public void reduce2java(Lang.AstProperties props) {
	    System.out.println("Error - AstListNode::reduce2java should not be called");
	}

	// reduce2java() generates the Java code that recreates the tree
	// rooted at the current element.

    }


    //**************************************************
    // Class AstToken
    // This is the class to be created and returned by JLex.
    //**************************************************
    static public class AstToken implements Token, Cloneable, Serializable {
	public String white_space;
	public String name;
	private int def_line_num;

	static private boolean print_white_space = true;
	static public int identifier_num = -1;
	static public void printWhitespace(boolean v) { print_white_space = v; }
	static public boolean printWhitespace() { return(print_white_space); }

	public AstToken() {}

	// Remove later
// 	public AstToken(int x) {}

	public AstToken(int num, String _ws, String _sym) {
	    setParms(_ws, _sym, 0);
	}

	public Lang.AstToken setParms(String _ws, String _sym, int lnum) {
	    white_space = _ws;
	    name       = _sym;
	    def_line_num = lnum;
	    return((Lang.AstToken) this);
	}

	// Returns line number (in source file) that this token instance
	// was found in.
	public int lineNum() { return(def_line_num); }

	public boolean Equ(Lang.Token x) {
	    return (name.compareTo(x.tokenName()) == 0);
	}

	public Object clone() {
	    Lang.AstToken copy;

	    copy = new Lang.AstToken().setParms(white_space, name,
						def_line_num);
	    return(copy);
	}

	public void print(Lang.AstProperties props) {
	    PrintWriter ps;

	    ps = (PrintWriter) props.getProperty("output");
	    if (print_white_space)
		ps.print(white_space + name);
	    else
		ps.print(name);
	}

	public void print() {
	    System.out.print(white_space + name);
	}


	public void reduce2java(Lang.AstProperties props) {
	    PrintWriter ps;

	    ps = (PrintWriter) props.getProperty("output");
	    if (print_white_space)
		ps.print(white_space + name);
	    else
		ps.print(name);
	}

	private String replaceChar(int index, String str, String insert) {
	    return(str.substring(0, index) + insert + str.substring(index+1));
	}

	public String makeString(String str) {
	    int i;
	    char ch;

	    for (i=0; i < str.length(); i++) {
		ch = str.charAt(i);
		switch (ch) {
		case '\\':
		    str = replaceChar(i, str, "\\\\");
		    i++;
		    break;
		case '\n':
		    str = replaceChar(i, str, "\\n");
		    i++;
		    break;
		case '\t':
		    str = replaceChar(i, str, "\\t");
		    i++;
		    break;
		case '\b':
		    str = replaceChar(i, str, "\\b");
		    i++;
		    break;
		case '\f':
		    str = replaceChar(i, str, "\\f");
		    i++;
		    break;
		case '\r':
		    str = replaceChar(i, str, "\\r");
		    i++;
		    break;
		case '\"':
		    str = replaceChar(i, str, "\\\"");
		    i++;
		    break;
		}
	    }
	    return(str);
	}

	public void printWhitespaceOnly(Lang.AstProperties props) {
	    PrintWriter pw = (PrintWriter) props.getProperty("output");

	    pw.print(makeString(white_space));
	}

	public String tokenName() {
	    return(name);
	}
    }


    //**************************************************
    // Class AstOptToken
    //**************************************************
    static public class AstOptToken extends Lang.AstNode
	implements Token {

	public AstOptToken() {
// 	    super(0);
	    setParms(null);
	}

// 	public AstOptToken(int n) { super(n); }

	public Lang.AstOptToken setParms(Lang.Token child) {
	    arg = new Lang.AstNode[1];
	    tok = new Lang.Token[1];
	    tok[0]  = child;
	    InitChildren();
	    return((Lang.AstOptToken) this);
	}

	public boolean Equ(Lang.Token x) {
	    return (this.tokenName().compareTo(x.tokenName()) == 0);
	}

	public boolean[] printorder() {
	    Util.fatalError("shouldn't call AstOptToken::printorder()");
	    return null;
	}

	// Delete() deletes the token

	public void Delete() {
	    tok[0] = null;
	}

	// Replace(w) does one of two things: if w is an instanceof AstOptToken
	// then replace the current node (which is an instance of AstOptToken)
	// with w using the generic Replace() method.  Otherwise, replace
	// is undefined.

	public Lang.AstNode Replace(Lang.AstNode withnode) {
	    if (withnode instanceof Lang.AstOptToken)
		return(super.Replace(withnode));
	    System.out.println("AstNode::Replace - shouldn't be called");
	    return(withnode);
	}

	// print(), reduce2java(), and reduce2ast() print/reduce optional nodes

	public void print(Lang.AstProperties props) {
	    if (tok[0] != null)
		tok[0].print(props);
	}

	public void print() {
	    if (tok[0] != null)
		tok[0].print();
	}

	public void reduce2java(Lang.AstProperties props) {
	    if (tok[0] != null) {
		tok[0].reduce2java(props);
	    }
	}

	// SyntaxCheck() shouldn't be called.  It is stubbed now.

	public boolean SyntaxCheck() {
	    Util.fatalError("SyntaxCheck should not be called");
	    return false;
	}

	public String tokenName() {
	    if (tok[0] == null)
		return("");
	    return(tok[0].tokenName());
	}

	public void printWhitespaceOnly(Lang.AstProperties props) {
	    if (tok[0] != null)
		tok[0].printWhitespaceOnly(props);
	}

	//**************************************************
	// This method adds the comment given by the parameter to
	// the AstToken if it exists.
	//**************************************************
	public Lang.AstNode addComment(String comment) {
	    return(addComment(comment, false));
	}

	public Lang.AstNode addComment(String comment, boolean replace) {
	    if (arg[0] == null)
		return(null);
	    return(arg[0].addComment(comment, replace));
	}
    }


    //**************************************************
    // Class AstOptNode
    //**************************************************
    static public class AstOptNode extends Lang.AstNode {
	public AstOptNode() {
// 	    super(0);
	    setParms(null);
	}

// 	public AstOptNode(int n) { super(n); }

	public Lang.AstOptNode setParms(Lang.AstNode child) {
	    arg = new Lang.AstNode[1];
	    arg[0]  = child;
	    InitChildren();
	    return((Lang.AstOptNode) this);
	}

	public boolean[] printorder() {
	    FatalError error = new FatalError();
	    error.printStackTrace();
	    Util.fatalError("shouldn't call AstOptNode::printorder()");
	    return null;
	}

	// Delete() deletes the AST argument of AstOptNode

	public void Delete() {
	    // Step 1: just set arg[0] to null

	    if (arg[0] != null)
		arg[0].up = null;

	    arg[0] = null;
	}

	// Replace(w) does one of two things: if w is an instanceof AstOptNode
	// then replace the current node (which is an instance of AstOptNode)
	// with w using the generic Replace() method.  Otherwise, just replace
	// the argument of the current node with w.  The replaced node is
	// returned as a result

	public Lang.AstNode Replace(Lang.AstNode withnode) {
	    if (withnode instanceof Lang.AstOptNode)
		return(super.Replace(withnode));
	    if (arg[0] != null)
		arg[0].up   = null;
	    arg[0]         = withnode;
	    withnode.left  = null;
	    withnode.right = null;
	    withnode.up    = this;
	    return(withnode);
	}

	// print() and reduce2java() print/reduce optional nodes

	public void print() {
	    if (arg[0] != null) {
		arg[0].print();
	    }
	}

	public void print(Lang.AstProperties props) {
	    if (arg[0] != null) {
		arg[0].print(props);
	    }
	}

	public void reduce2java(Lang.AstProperties props) {
	    if (arg[0] != null) {
		arg[0].reduce2java(props);
	    }
	}

	// SyntaxCheck() shouldn't be called.  Dealing with optional nodes
	// is the responsibility of the NamedRule class generator of Bali -
	// it handles the syntax checking of optional nodes when generating
	// the SyntaxCheck() method for NamedRule classes.

	public boolean SyntaxCheck() {
	    Util.fatalError("SyntaxCheck should not be called");
	    return false;
	}

	public Lang.AstNode addComment(String comment) {
	    return(addComment(comment, false));
	}

	public Lang.AstNode addComment(String comment, boolean replace) {
	    if (arg[0] != null)
		return(arg[0].addComment(comment, replace));
	    return(null);
	}
    }


    //**************************************************
    // Class AstCursor
    //**************************************************
    static public class AstCursor {
	// node references the current node of the cursor
	// in the case of node deletions and replacements,
	// NextIsSet to true, and next is set to the next node
	// to search.  root is the root of the AST to search

	public Lang.AstNode node;       // cursor references this node
	boolean             NextIsSet;  // set true by Sibling, Delete, Replace
	public Lang.AstNode next;       // set by Sibling, Delete, Replace
	Lang.AstNode        root;       // root of the search tree
        Lang.AstNode        CurrElem;   // next element in list (used for
                                        // searching lists only!

	// AstCursor constructor just nullifies all of its data members

	public AstCursor() {
	    node = null;
	    NextIsSet = false;
	    next = null;
	    root = null;
	}

	// note: to scan an AST rooted at t, use the following
	// AstCursor c;
	// for (c.First(t); c.More(); c.PlusPlus()) { ... }

	public Lang.AstNode Root() {
	    return root;
	}

	public void First() {
	    First(root);
	}

	// First(r) finds first nonstructural node (i.e., the first
	// node that isn't an AstListNode or an AstOptNode) in the
	// tree rooted by r.  First also remembers the root of the
	// tree.  If no such node can be found, data member node == null.

	public void First(Lang.AstNode r) {
	    root = r;
	    node = r;
	    NextIsSet = false;
	    next = null;

	    while (node != null &&
		   (node instanceof Lang.AstListNode ||
		    node instanceof Lang.AstOptNode)) {
		GetNext();
	    }
	}

	// More() returns true if there are additional nodes to
	// examine in the AST rooted at root.

	public boolean More() {
	    return node != null;
	}

	// PlusPlus() repositions the cursor on the next nonstructural
	// node of the AST.  If there is no such node, data member node
	// is set to null.

	public void PlusPlus( ) {  // got next printable node
	    GetNext();
	    while (node != null &&
		   (node instanceof Lang.AstListNode ||
		    node instanceof Lang.AstOptNode)) {
		GetNext();
	    }
	}

	//  GetNext() is a method that is internal to the AstCursor class,
	// and should not be exported.  It does the primitive work of
	// advancing a cursor to the next structural or nonstructural
	// node of a tree.  It is an internal method (i.e., one that isn't
	// exported) as it is shared by several exported methods of
	// AstCursor class.

	void GetNext() {

	    // Step 1: check if the next pointer has already been determined
	    //         by the Replace() or Delete() methods.  if so, use it
	    //         and exit

	    if (NextIsSet) {
		NextIsSet = false;
		node = next;
		next = null;
		return;
	    }

	    if (node == null)
		return;

	    // Step 2:  else, if there is a child, return it

	    if (node.arg[0] != null) {
		node = node.arg[0];
		return;
	    }

	    // Step 3: else, skip to next sibling

	    skip();
	}

	// skip() is an internal method (i.e., one that is not exported) by AstClass.
	// it does the work of advancing a cursor to its immediate sibling or relative
	// (but not child).  It is internal to AstClass as it is shared by several
	// exported methods.

	void skip() {

	    // Step 1: if we're already positioned on the next node (virtually)
	    //         then actually position the cursor before performing a skip

	    if (NextIsSet) {
		NextIsSet = false;
		node = next;
		next = null;
	    }

	    if (node == root) {
		node = null;
		return;
	    }

	    // Step 1: if there is a right sibling, return it

	    if (node.right != null) {
		node = node.right;
		return;
	    }

	    // Step 2: otherwise, begin examining ancestors
	    //         if the ancestor is a root (the starting node of
	    //         the search) or the ancestor is null, terminate
	    //         the search. Otherwise, continue to search for
	    //         an ancestor with a nonnull right pointer.

	    while (true) {
		node = node.up;

		// Step 2a: stop if we're at the top of the AST or we're at
		//          the root.

		if (node == null || node == root) {
		    node = null;
		    return;
		}

		// Step 2b: otherwise, proceed to right sibling

		if (node.right != null) {
		    node = node.right;
		    return;
		}

		// Step 2c: else, go up.
	    }
	}

	// ContinueFrom(k) is an internal method to AstCursor.  It tells the cursor
	// advancement methods that the next node to examine in the tree is node k.
	// ContinueFrom(k) is called by Sibling(), Replace(), and Delete();

	void ContinueFrom(Lang.AstNode k) {
	    NextIsSet = true;
	    next      = k;
	    node      = null;
	}

	public void Position(Lang.AstNode x) {
	    Lang.AstNode parentNode;

	    if (x != null) {
		// validate x as a child of r
		parentNode = x;
		while (parentNode != null) {
		    if (parentNode == root)
			break;
		    parentNode = parentNode.up;
		}
		if (parentNode == null) {
		    // x is not a child of root
		    node = null;
		    return;
		}
	    }

	    node = x;
	    NextIsSet = false;
	}

	public void Position(Lang.AstNode r, Lang.AstNode x) {
	    root = r;
	    Position(x);
	}

	// Sibling() allows one to skip the searching of the current node's children
	// and to proceed directly to searching the current node's sibling (or next
	// relative.  A typical use for this would be (a) locate a particular node
	// in an AST, and (knowing that a recurrence of that node will never happen
	// in its children) to continue searching from the node's sibling:
	//
	// example:
	// AstCursor c = new AstCursor();
	//
	// for (c.First(t); c.More(); c.PlusPlus()) {
	//   if (c instanceof $TEqn.AstClass) {
	//       ... found an instance of the Class construct
	//       c.Sibling(); // won't find another instance of Class construct
	//                    // as classes aren't nested in Java/Jakarta
	//   }
	// }

	public void Sibling() {
	    // Step 1: skip to sibling

	    skip();

	    // Step 2: after a skip, we need to remember where to continue
	    //         after the next advancement

	    ContinueFrom(node);
	}

	// Replace(k) swaps the AST rooted at the current node with the tree
	// rooted at k.  An AST search continues from node k.
	// Note: special case if k == root; root is updated.

	public void Replace(Lang.AstNode withnode ) {

	    // Step 1: if we are replacing the root node of the
	    //         search, then update the root variable

	    if (this.node == root)
		root = withnode;

	    // Step 2: now replace this with withnode, and continue
	    //         from withnode

	    ContinueFrom(node.Replace(withnode));
	}

	// Delete() the current node.  Accomplished by repositioning the cursor on its
	// Sibling (as this is where the search will continue), followed by a deletion
	// of the original node.

	public void Delete( ) { // delete node
	    AstNode todelete = node;

	    // Step 1: position cursor after this node
	    Sibling();

	    // Step 2: now delete the node itself
	    todelete.Delete();
	}

	public void AddAfter(Lang.AstList y) {
	    node.AddAfter(y);
	}

	public void AddBefore(Lang.AstList y) {
	    node.AddBefore(y);
	}

	// print() unparses the AST rooted at the current node
	// reduce2java() reduces the Jakarta AST to Java code
	// reduce2ast() reduces the Jakarta AST constructors in Java

	public void print() {
	    node.print();
	}

	public void reduce2java(Lang.AstProperties props) {
	    node.reduce2java(props);
	}

	// SyntaxCheck() returns true if the AST rooted at the current node
	// is syntactically correct

	public boolean SyntaxCheck() {
	    return(node.SyntaxCheck());
	}

	public Lang.AstNode find(String node_type) {
	    Class query_class;

	    try {
		query_class = Class.forName(node_type);
	    }
	    catch (Exception e) {
		return(null);
	    }
	    return(find(query_class));
	}

	public Lang.AstNode find(Class query_class) {
	    Class anc_class;

	    while (node != null) {
		anc_class = node.getClass();
		while (anc_class != null) {
		    if (anc_class == query_class)
			return(node);
		    anc_class = anc_class.getSuperclass();
		}
		PlusPlus();
	    }
	    return(null);
	}


	//**************************************************
	// Unlike the above versions of find, this method takes a String
	// naming an unqualified base type. It scans and gets the class name
	// for each node, converts it to a base type and compares to the
	// type desired.
	//**************************************************
	public Lang.AstNode findBaseType(String baseType) {
	    String csr_base_type;

	    while (node != null) {
		csr_base_type = Util.baseType(node);
		if (baseType.compareTo(csr_base_type) == 0)
		    return(node);
		PlusPlus();
	    }
	    return(null);
	}

        //***************************************************
        // The following methods were added to make list traversals
        // with cursors easier
        //***************************************************

        public void FirstElement( Lang.AstNode e ) {
           if (!(e instanceof Lang.AstList))
              Util.fatalError("Non-list node called with FirstElement()");
           if (e.arg[0]==null) {
              node = null;
              return;
           }
           CurrElem = e.arg[0];
           node = CurrElem.arg[0];
           return;
        }

        public boolean MoreElement() {
           return CurrElem != null;
        }

        public void NextElement() {
           CurrElem = CurrElem.right;
           if (CurrElem == null) 
              node = null;
           else
              node = CurrElem.arg[0];
        }
    }


    //**************************************************
    // Executing the main of Main will perform the following:
    //		1) Initialization.
    //		2) Parse input args and remove switches and their args.
    //		3) Call the driver() method.
    //		4) Call the cleanUp() method.
    //**************************************************
    static public class Main {
	static private int layerID_Counter = 0;
	static Vector switches = new Vector();
	static Vector posArgs = new Vector();
	Lang.AstProperties mainProps = new Lang.AstProperties();

	static protected class Arg {
	    String name;
	    int layerID;
	    boolean optional;
	}

	static protected class Switch extends Arg implements Cloneable {
	    String description;
	    String[] args;	// names or instance bindings

	    public Switch(String _id, String _description, String[] argNames,
			  boolean _optional, int _layer) {
		name = _id;
		description = _description;
		args = argNames;
		optional = _optional;
		layerID = _layer;
	    }

	    public Object clone() throws CloneNotSupportedException {
		return(super.clone());
	    }
	}

	static protected class PositionalArg extends Arg {
	    String binding;

	    public PositionalArg(String _name, int _layer) {
		name = _name;
		layerID = _layer;
		optional = false;
	    }
	}


	//**************************************************
	// ArgList - encapsulates a list of either Switch or
	// PositionalArg objects.
	//**************************************************
	static protected class ArgList extends Vector {
	    // Constants used to constrain find(), first(), and next()
	    static public final int NO_LAYER = -1;
	    static public final Class NO_CLASS = null;

	    // Current filter values
	    int layerFilter = NO_LAYER;
	    Class classFilter = NO_CLASS;

	    // Acts as a cursor for current position.
	    int csrIndex;

	    private Arg locate(int start) {
		Arg arg;

		for (int i=start; i < elementCount; i++) {
		    arg = (Arg) elementData[i];
		    if ((layerFilter != NO_LAYER) &&
			(arg.layerID != layerFilter))
			continue;
		    if ((classFilter != NO_CLASS) &&
			(arg.getClass() != classFilter))
			continue;
		    csrIndex = i;
		    return(arg);
		}
		return(null);
	    }


	    //**************************************************
	    // Return first Arg with class and layerID possible filter
	    // criteria.
	    //**************************************************
	    public Arg first() {
		return(locate(0));
	    }

	    public Arg first(Class cls) {
		classFilter = cls;
		return(locate(0));
	    }

	    public Arg first(int _layer) {
		layerFilter = _layer;
		return(locate(0));
	    }

	    public Arg first(Class cls, int _layer) {
		classFilter = cls;
		layerFilter = _layer;
		return(locate(0));
	    }


	    //**************************************************
	    // Return next Arg with class and layerID possible filter
	    // criteria.
	    //**************************************************
	    public Arg next() {
		return(locate(csrIndex+1));
	    }

	    public Arg next(Class cls) {
		classFilter = cls;
		return(locate(csrIndex+1));
	    }

	    public Arg next(int _layer) {
		layerFilter = _layer;
		return(locate(csrIndex+1));
	    }

	    public Arg next(Class cls, int _layer) {
		classFilter = cls;
		layerFilter = _layer;
		return(locate(csrIndex+1));
	    }

	    
	    //**************************************************
	    // Locate an argument by name with class and layerID possible
	    // filter criteria.
	    //**************************************************
	    public Arg find(String name) {
		Arg arg;

		for (arg = locate(0); arg != null; arg = locate(csrIndex+1)) {
		    if (name.compareTo(arg.name) == 0)
			return(arg);
		}
		return(null);
	    }

	    public Arg find(String name, Class cls) {
		classFilter = cls;
		return(find(name));
	    }

	    public Arg find(String name, int _layer) {
		layerFilter = _layer;
		return(find(name));
	    }

	    public Arg find(String name, Class cls, int _layer) {
		classFilter = cls;
		layerFilter = _layer;
		return(find(name));
	    }
	}

	//**************************************************
	// main
	//**************************************************
	static public void main(String[] args) {
	    Lang.Main instance;
	    ArgList arguments;

	    instance = new Lang.Main();

	    instance.initialize();

	    // Request layers to register their interests in switches
	    // and positional arguments.
	    instance.argInquire(0);

	    // Parse input args. Remove switches and their args.
	    arguments = parseArgs(args);

	    // Call driver()
	    instance.driver(arguments);

	    // Clean up.
	    instance.cleanUp();
	}


	//**************************************************
	// Parse input args. Remove switches and their args.
	//**************************************************
	static private ArgList parseArgs(String[] args) {
	    ArgList argObjects = new ArgList();
	    int j,k;
	    Switch sw;
	    Switch newSwitch;
	    String switchName;
	    PositionalArg parg;

	    for (int i=0; i < args.length; i++) {
		if (args[i].charAt(0) == '-') {
		    // switch
		    switchName = args[i].substring(1);
		    for (j=0; j < switches.size(); j++) {
			sw = (Switch) switches.elementAt(j);
			if (switchName.compareTo(sw.name) == 0) {
			    // Found switch. Clone it.
			    try {
				newSwitch = (Switch) sw.clone();
			    }
			    catch (CloneNotSupportedException e) {
				Util.fatalError(e);
				newSwitch = null;
			    }

			    // Bind args if any
			    if (sw.args != null) {
				// Allocate array to hold args
				newSwitch.args = new String[sw.args.length];

				// Bind args from arg list
				for (k=0; k < sw.args.length; k++) {
				    if (++i == args.length)
					usage();
				    newSwitch.args[k] = args[i];
				}
			    }

			    // Add newly created Switch object to argObjects.
			    argObjects.addElement(newSwitch);

			    break;
			}
		    }	// end of for loop scanning switch list
		}
		else {
		    // non-switch arg
		    if (posArgs.size() == 0)
			usage();
		    parg = (PositionalArg) posArgs.firstElement();
		    posArgs.removeElementAt(0);
		    parg.binding = args[i];

		    // Add existing PositionalArg object to argObjects.
		    argObjects.addElement(parg);
		}
	    }

	    // Since we currently do not allow optional positional arguments,
	    // make sure all required args have been supplied.
	    if (posArgs.size() != 0)
		usage();

	    return(argObjects);
	}


	//**************************************************
	// Print out usage of program.
	//**************************************************
	static private void usage() {
	    int i, j;
	    Switch sw;
	    PositionalArg parg;

	    System.err.print("Usage: $(LanguageName).Main");

	    // List switches
	    for (i=0; i < switches.size(); i++) {
		sw = (Switch) switches.elementAt(i);
		if (sw.optional)
		    System.err.print(" [");
		else
		    System.err.print(" ");
		System.err.print("-" + sw.name);
		if (sw.args != null) {
		    for (j=0; j < sw.args.length; j++)
			System.err.print(" " + sw.args[j]);
		}
		if (sw.optional)
		    System.err.print("]");
	    }

	    // List positional arguments
	    for (i=0; i < posArgs.size(); i++) {
		parg = (PositionalArg) posArgs.elementAt(i);
		System.err.print(" <" + parg.name + ">");
	    }
	    System.err.println();

	    // List switch descriptions
	    for (i=0; i < switches.size(); i++) {
		sw = (Switch) switches.elementAt(i);
		System.err.println("\t-" + sw.name + " : " + sw.description);
	    }

	    // Force exit
	    System.exit(10);
	}

	//**************************************************
	// Initialize state prior any other processing.
	//**************************************************
	public void initialize() {}


	//**************************************************
	// Must be overriden. Each layer makes zero or more calls to
	// switchRegister() and posArgRegister() and then calls
	// super.argInquire(nextLayer()); (See nextLayer() below.)
	//**************************************************
	protected void argInquire(int _layer) {}
	protected final int nextLayer() { return(layerID_Counter++); }

	// Services provided by top level. Cannot be overriden.
	protected final void switchRegister(Switch sw) {
	    switches.addElement(sw);
	}
	protected final void posArgRegister(PositionalArg parg) {
	    posArgs.addElement(parg);
	}


	//**************************************************
	// Can override driver() and call super.driver() in order to
	// do pre or post processing. The default driver simply calls
	// createAST(), then reduceAST(), then outputAST().
	//**************************************************
	protected void driver(ArgList arguments) {
	    Lang.AstNode ast;

	    ast = createAST(arguments);
	    if (ast == null)
		return;
	    ast = reduceAST(arguments, ast);
	    if (ast == null)
		return;
	    outputAST(arguments, ast);
	}


	//**************************************************
	// Methods called by driver().
	//**************************************************
	protected Lang.AstNode createAST(ArgList argObjects) { return(null); }
	protected Lang.AstNode reduceAST(ArgList argObjects,
					 Lang.AstNode ast) {
	    return(ast);
	}
	protected void outputAST(ArgList argObjects, Lang.AstNode ast) {}

	protected void cleanUp() {}
    }	// end of Main class


    //**************************************************
    // class AstNode
    //**************************************************
    static public abstract class AstNode implements Cloneable, Serializable {

	public Lang.Token[]     tok;
	public Lang.AstNode[]   arg;
	public Lang.AstNode     right;
	public Lang.AstNode     left;
	public Lang.AstNode     up;

	public int stackMarker;

	public abstract boolean SyntaxCheck();
	public abstract boolean[] printorder();

	private static Class obj_class;

	static public Stack aliasStack = new Stack();

	static {
	    try {
		obj_class = Class.forName("java.lang.Object");
	    }
	    catch (Exception e) {}
	}

// 	public AstNode(int n) {}

        // the following methods are needed to make
        // writing code templates and layer files easier.
        // the methods themselves will be overridden by
        // the Gscope component.  The problem is that AST
        // generates calls to these methods and it shouldn't

        public Lang.AstNode markStack() { return (Lang.AstNode) this; }
        public Lang.AstNode patch()     { return (Lang.AstNode) this; }

	//**************************************************
	// For conversion to a String
	//**************************************************
	public String toString() {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PrintWriter pw = new PrintWriter(baos);
	    Lang.AstProperties props = new Lang.AstProperties();

	    props.setProperty("output", pw);
	    reduce2java(props);
	    pw.flush();
	    return(baos.toString());
	}

	public Object clone() {
	    Lang.AstNode copy;

	    copy = null;
	    try {
		copy = (Lang.AstNode) getClass().newInstance();
	    }
	    catch (Exception e) {
		Util.fatalError("Can't clone "+getClass(), e);
	    }
	    initClone(copy);
	    return(copy);
	}

	// Copy of an AstNode copies only it's children
	protected void initClone(Lang.AstNode copy) {
	    int i;

	    if (tok != null) {
		copy.tok = new Lang.Token[tok.length];
		for (i=0; i < tok.length; i++)
		    if (tok[i] != null)
			copy.tok[i] = (Lang.Token) tok[i].clone();
	    }
	    if (arg != null) {
		copy.arg = new Lang.AstNode[arg.length];
		for (i=0; i < arg.length; i++) {
		    if (arg[i] != null) {
			copy.arg[i] = (Lang.AstNode) arg[i].clone();
			copy.arg[i].up = copy;
		    }
		}
	    }

	    // initialize children pointers, if this hasn't been done
	    // already.  AstListNodes already have been initialized
	    if (! (copy instanceof Lang.AstList))
		copy.InitChildren();
	}

	//
	// For testing subclasses
	//
	public int firstSubclass() {
	    Util.fatalError("called AstNode.firstSubclass");
	    return(-1);	// stupid compiler
	}

	public int classCode() {
	    Util.fatalError("called AstNode.classCode");
	    return(-1);	// stupid compiler
	}

	public boolean isSubclass(Lang.AstNode sc) {
	    if ((sc.classCode() >= firstSubclass()) &&
		(sc.classCode() < classCode()))
		return(true);
	    else
		return(false);
	}


	// We would like to locate a given ancestor that is an instance of the
	// class specified (classname) or a derived class of that.

	public Lang.AstNode hasAncestor(String classname) {
	    Class ancestorClass;
	    String ancestorBaseName;
	    Lang.AstNode ancestor;

	    ancestor = up;
	    while (ancestor != null) {
		ancestorClass = ancestor.getClass();
		ancestorBaseName = Util.baseType(ancestorClass);
		while (ancestorClass != obj_class) {
		    if (classname.compareTo(ancestorBaseName) == 0)
			return(ancestor);
		    ancestorClass = ancestorClass.getSuperclass();
		}
		ancestor = ancestor.up;
	    }
	    return(null);
	}

// 	public $TEqn.AstNode hasAncestor(String classname) {
// 	    Class query_class;
// 	    Class anc_class;
// 	    $TEqn.AstNode ancestor;

// 	    try {
// 		query_class = Class.forName(classname);
// 	    }
// 	    catch (Exception e) {
// 		return(null);
// 	    }
// 	    ancestor = up;
// 	    while (ancestor != null) {
// 		anc_class = ancestor.getClass();
// 		while (anc_class != obj_class) {
// 		    if (query_class == anc_class)
// 			return(ancestor);
// 		    anc_class = anc_class.getSuperclass();
// 		}
// 		ancestor = ancestor.up;
// 	    }
// 	    return(null);
// 	}

	// InitChildren() initializes the left, right, and up pointers of
	// children nodes.

	public void InitChildren() {
	    int i;
	    Lang.AstNode previous;

	    // Step 1: init node id

	    if (arg[0] == null)
		return;	// no children, therefore nothing to initialize

	    // Step 2: initialize up and left for each child

	    previous = null;
	    for (i=0; i<arg.length; i++) {
		arg[i].up   = (Lang.AstNode) this;
		arg[i].left = previous;
		previous    = arg[i];
	    }

	    // Step 3: initialize right for each child

	    previous = null;
	    for (i=arg.length-1; i>=0; i--) {
		arg[i].right = previous;
		previous = arg[i];
	    }
	}

	public void PrettyDump() {
	    PDump("");
	}

	public void PDump(String indent) {
	    String ind;
	    int i;
       
// 	    System.out.println(indent + indent.length() + className() +
// 			       "\t(" + hashCode() + ")");
	    System.out.println(indent + indent.length() + className());
	    if (arg == null) 
		return;
	    else {
		ind = indent + " ";
		for (i=0; i<arg.length; i++) 
		    if (arg[i] != null) arg[i].PDump(ind);
	    }
	}

	// Replace(k) swaps the current node with node k.  Replace returns
	// k as a result.

	public Lang.AstNode Replace(Lang.AstNode withnode) {

	    // Step 1: adjust right pointer

	    withnode.right = right;
	    if (right != null)
		right.left = withnode;
	    right = null;

	    // Step 2: adjust left pointer

	    withnode.left = left;
	    if (left != null)
		left.right = withnode;
	    left = null;

	    // Step 3: adjust up pointer

	    withnode.up = up;
	    if (up != null)
		up.ReplaceRef((Lang.AstNode) this, withnode);
	    up = null;

	    // Step 4: return withnode

	    return withnode;
	}

	// TypeError(a,t) is used in reporting SyntaxCheck errors.  argument
	// a should have been of type t

	public void TypeError(String argument, String typ) {
	    System.out.println("Type Error: " + argument + " of node " + hashCode()
			       + " should be of type " + typ);
	}

	// ReplaceRef(c,n) replaces reference to child c with Ast node n

	public void ReplaceRef(Lang.AstNode oldnode, Lang.AstNode newnode) {
	    int i;

	    for (i = 0; i < arg.length; i++) {
		if (arg[i] == oldnode) {
		    arg[i] = newnode;
		    return;
		}
	    }
	    System.out.println("\nFatal Error - inconsistent AST reference");
	}


	// print() unparses the contents of the AST rooted at the current node.
	// reduce2java() prints the Java code that is defined by the Jakarta
	//        AST that is rooted at the current node
	// reduce2ast() prints the constructors that are needed to reconstruct
	//        the ASt that is rooted at the current node.

	public void print() {
	    boolean order[];
	    int     t, n, i;

	    order = printorder();
	    t = 0;
	    n = 0;
	    for (i=0; i<order.length; i++) {
		// if order[i] is true; print token else print nonterminal

		if (order[i])
		    tok[t++].print();
		else
		    arg[n++].print();
	    }
	}

	public void print(AstProperties props) {
	    boolean order[];
	    int     t, n, i;

	    order = printorder();
	    t = 0;
	    n = 0;
	    for (i=0; i<order.length; i++) {
		// if order[i] is true; print token else print nonterminal

		if (order[i])
		    tok[t++].print(props);
		else
		    arg[n++].print(props);
	    }
	}

	public void reduce2java(AstProperties props) {
	    boolean order[];
	    int     t, n, i;

	    order = printorder();
	    t = 0;
	    n = 0;
	    for (i=0; i<order.length; i++) {
		// if order[i] is true; print token else print nonterminal

		if (order[i])
		    tok[t++].reduce2java(props);
		else
		    arg[n++].reduce2java(props);
	    }
	}

	// Print_Only_Tokens() prints only the tokens of a given node. This
	// method is used only for the printing of lists.  Similarly,
	// Print_Only_Tokens_Ast() prints the ASTs for tokens of a given node.
	// It too is used only for the reduction of lists.

	public void Print_Only_Tokens(AstProperties props) {
	    int i;

	    if (tok == null)
		return;

	    for (i=0; i<tok.length; i++)
		tok[i].print(props);
	}

	public void Print_Only_Tokens() {
	    int i;

	    if (tok == null)
		return;

	    for (i=0; i<tok.length; i++)
		tok[i].print();
	}

	boolean instanceOf(Class myclass) {
	    Class c;

	    for (c = this.getClass(); c != null; c = c.getSuperclass())
		if (c == myclass)
		    return(true);
	    return(false);
	}

	public boolean instanceOf(Object obj) {
	    Class myclass;

	    myclass = obj.getClass();
	    return(instanceOf(myclass));
	}
    
	public boolean instanceOf(String class_name) {
	    Class myclass;

	    try {
		myclass = Class.forName(class_name);
	    }
	    catch (ClassNotFoundException e) {
		System.err.println("Couldn't find class "+class_name);
		return(false);
	    }
	    return(instanceOf(myclass));
	}

	public String className() {
	    // 	return(this.getClass().getName());
	    return("Lang" + "." + Util.baseType(this));
	}

	public boolean Equ( Lang.AstNode x ) {
	    int i;
   
	    // Step 1: check to see if x is null; if so, return false
   
	    if (x == null) 
		return false;
  
	    // Step 2: must have equal types
 
	    if (getClass() != x.getClass())
		return false;

	    // Step 3: must have equal #s of tokens and equal tokens

	    if (tok != x.tok) {   
		if (tok != null && x.tok != null && tok.length != x.tok.length)
		    return false;
		else 
		    for (i=0; i< tok.length; i++) {
			if (tok[i] != x.tok[i] && tok[i] != null &&
			    !tok[i].Equ((Lang.Token) x.tok[i]))
			    return false;
		    }
	    } 
	    // Step 4: must have equal #s of arguments and equal arguments
  
	    if (arg != null && x.arg != null && arg.length != x.arg.length)
		return false;
	    else
		for (i=0; i< arg.length; i++) {
		    if (arg[i] != x.arg[i] && arg[i] != null &&
			!arg[i].Equ(x.arg[i]))
			return false;
		}
   
	    // Step 6: we got this far, must be equal
   
	    return true;
	}


	// dumpnode() is used for debugging small ASTs.  It prints the id and
	// type of a node, plus its left,right,up, and arg pointers for all
	// nodes in an AST

	public void dumpnode() {
	    int i;
	    AstList l;

	    // Step 1: print the nodeid, class, and up, left, and right pointers

	    System.out.print(hashCode() + " " + className());
	    if (up == null)
		System.out.print("\tup:*");
	    else
		System.out.print("\tup:"+up.hashCode());
	    if (left == null)
		System.out.print("\tleft:*");
	    else
		System.out.print("\tleft:"+left.hashCode());
	    if (right == null)
		System.out.print("\trite:*");
	    else
		System.out.print("\trite:"+right.hashCode());

	    // Step 2: print out last (if it exists)

	    if (this instanceof Lang.AstList) {
		l = (AstList) this;

		if (l.last == null)
		    System.out.print("\tlast:*");
		else
		    System.out.print("\tlast:"+l.last.hashCode());
	    }

	    // Step 2: print out arguments

	    for (i = 0; i < arg.length; i++) {
		if (arg[i] == null)
		    System.out.print("\targ[" + i + "]:*");
		else
		    System.out.print("\targ[" + i + "]:"+arg[i].hashCode());
	    }
	    System.out.println("");

	    // Step 3: validate pointers

	    if (left != null && left.right != this)
		System.out.print("l<->r pointers invalid");

	    if (right != null && right.left != this)
		System.out.print("r<->l pointers invalid");

	    if (arg != null && arg[0] != null && arg[0].up != this)
		System.out.print("d<->u pointers invalid");

	    // Step 4: recurse

	    for (i=0; i < arg.length; i++) {
		if (arg[i] != null)
		    arg[i].dumpnode();
	    }
	}

	// Delete() the current node.  Nodes can be deleted only in the following
	// conditions:
	// the node is an element of a list (i.e., up instanceof AstListNode)
	// the node is optional (i.e., up instanceof AstOptNode)
	// the node is a list (i.e., this instanceof AstList)

	// if node is a list, AstList::Delete overrides, so all we need to
	// do here is handle the first two cases.

	public void Delete() { // delete this node

	    // Step 1: see if parent is an instance of AstList
	    //         if so, remove it instead.

	    if (up instanceof Lang.AstListNode) {
		((Lang.AstListNode) up).Delete();
		return;
	    }

	    // Step 2: else, see if parent is an instance of
	    //         AstOptNode.  if so, remove it instead

	    if (up instanceof Lang.AstOptNode) {
		((Lang.AstOptNode) up).Delete();
		return;
	    }

	    // Step 3: else Fatal error - can only delete nodes
	    //         that are instances of lists

	    Util.fatalError("cannot delete non-list node");
	}

	// AddAfter(y) adds AstList y after the current node.  this is possible
	// if the current node's parent (up) is an instance of AstListNode (that
	// is, the current node is on a list.

	public void AddAfter(Lang.AstList tree) {  // add tree after this node
	    if (up instanceof Lang.AstListNode) {
		((Lang.AstListNode) up).AddAfter(tree);
	    } else
		Util.fatalError("illegal AddAfter");
	}

	// AddBefore(y) adds AstList y before the current node.  this is possible
	// if the current node's parent (up) is an instance of AstListNode (that
	// is, the current node is on a list.

	public void AddBefore(Lang.AstList tree) { // add tree before this node
	    if (up instanceof Lang.AstListNode) {
		((Lang.AstListNode) up).AddBefore(tree);
	    } else
		Util.fatalError("illegal AddAfter");
	}

	//**************************************************
	// This method adds the comment given by the parameter to
	// the first AstToken.
	//**************************************************
	public Lang.AstNode addComment(String comment) {
	    return(addComment(comment, false));
	}

	public Lang.AstNode addComment(String comment, boolean replace) {
	    boolean order[] = printorder();
	    int t, n, i;
	    Lang.AstToken ast_token;

	    t = 0;	// terminal index
	    n = 0;	// non-terminal index
	    for (i=0; i < order.length; i++) {
		// if order[i] is true, a terminal is next; else a non-terminal
		if (order[i]) {
		    // Terminal can be a token or non-token
		    if (tok[t] instanceof AstToken) {
			ast_token = (Lang.AstToken) tok[t];
			if (replace)
			    ast_token.white_space = comment;
			else
			    ast_token.white_space = comment +
				ast_token.white_space;
			return((Lang.AstNode) this);
		    }
		    if (((Lang.AstOptToken) tok[t]).addComment(comment, replace) != null)
			return((Lang.AstNode) this);
		    t++;
		}
		else if (((Lang.AstNode) arg[n++]).addComment(comment, replace) != null)
		    return((Lang.AstNode) this);
	    }
	    return(null);
	}


	// This method gets code generated as the first call to add a comment
	// to an AST. It's main purpose is to determine that the AST is
	// non-null before calling the addComment of the instance.
	public static Lang.AstNode addComment(Lang.AstNode ast,
					      String comment) {
	    return(addComment(ast, comment, false));
	}

	public static Lang.AstNode addComment(Lang.AstNode ast,
					      String comment,
					      boolean replace) {
	    if (ast == null)
		return(null);
	    return(ast.addComment(comment, replace));
	}

        // This method is used to repair ASTs that share subtrees.
        // the idea is to walk the tree looking for nodes that don't
        // point to their parent.  In such cases, the subtree is cloned
        // and the result is that normalized trees don't have shared
        // subtrees

        public Lang.AstNode normalizeTree( Lang.AstNode parnt ) {
           int i;
           if (arg == null)
              return (Lang.AstNode) this;
           for (i=0; i<arg.length; i++)
              if (arg[i] != null)
                 arg[i] = (Lang.AstNode) arg[i].normalizeTree((Lang.AstNode) this);

           // if our up pointer != down pointer parnt, we must
           // clone ourselves

           if (parnt == null)
              return (Lang.AstNode) this;            // at root
           if (up != parnt) {
              return (Lang.AstNode) this.clone();
           }
           else 
              return (Lang.AstNode) this;
        }

        public void normalize() {
           normalizeTree((Lang.AstNode) this);
        }

        // Detach isolates a subtree from its left, up, and right
        // node relations.  Useful for detaching a tree from an
        // existing tree

        public void Detach() {
           up    = null;
           left  = null;
           right = null;
        }

        // writeTree serializes an AST to a file

        public void writeTree( String fileName ) {
           ObjectOutputStream os = null;
           try {
              os = new ObjectOutputStream( new FileOutputStream(fileName + ".ser"));
           }
           catch( Exception e ) {
              Util.fatalError(e.getMessage());
           }

           try {
              os.writeObject(this); 
           }
           catch( Exception e ) {
              Util.fatalError(e.getMessage());
           }

           try {
              os.close();
           }
           catch( Exception e ) {
              Util.fatalError(e.getMessage());
           }
         }
    }



    //**************************************************
    // class AstProperties
    //**************************************************

    static public class AstProperties {
	private Hashtable table;
	private PrintWriter pw = null;
	private ByteArrayOutputStream baos = null;
	static private PrintWriter pwStdOut = null;

	public AstProperties() {
	    table = new Hashtable();
	}

	// All properties should have a string as the key, but the value
	// is an object which must be cast appropriately by the caller.
	public void setProperty(String key, Object value) {
	    table.put(key, value);
	}

	public Object getProperty(String key) {
	    return(table.get(key));
	}

	public Object removeProperty(String key) {
	    return(table.remove(key));
	}

	public boolean containsProperty(String key) {
	    return(table.containsKey(key));
	}

	public void setPw( PrintWriter p ) {
	    pw = p;
	}

	public void setBaos( ByteArrayOutputStream b ) {
	    baos = b;
	}

	public static Lang.AstProperties open( String filename ) {
	    Lang.AstProperties props;
	    PrintWriter   pw;
       
	    props = new Lang.AstProperties();
	    pw = null;
	    if (filename == null) {
		if (pwStdOut == null) {
		    pwStdOut = new PrintWriter(System.out);
		}
		pw = pwStdOut;
	    }
	    else {
		try {
		    pw = new PrintWriter(new FileOutputStream(filename));
		}
		catch (IOException e) {
		    System.err.println("Cannot open " + filename +
				       e.getMessage());
		    System.exit(20);
		}
	    }
	    props.setProperty("output", pw);
	    props.setPw(pw);
	    return props;
	}

	// dump reduction into string array
	public static Lang.AstProperties open() {
	    Lang.AstProperties         props;
	    PrintWriter           pw;
	    ByteArrayOutputStream baos;
       
	    props = new Lang.AstProperties();
	    baos = new ByteArrayOutputStream();
	    pw = new PrintWriter(baos);
	    props.setProperty("output", pw);
	    props.setPw(pw);
	    props.setBaos(baos);
	    return props;
	}

	public String close() {
	    // Step 1: in any case, flush

	    pw.flush();

	    // Step 2: different actions for output to file, StdOut, and bytearrays

	    if (baos != null)
		return baos.toString();   // return string of entire buffer

	    if (pw != pwStdOut) {
		pw.close();               // if file, close it
	    }
	    return null;                 // return null if Stdout or file
	}
    }


    //**************************************************
    // class FatalError
    //**************************************************
    static public class FatalError extends Exception {
	public FatalError() { super(); }
	public FatalError(String s) { super(s); }
    }


    //**************************************************
    // interface Token. This is the type to be created and returned
    // by JLex.
    //**************************************************
    static public interface Token {
	public void print(Lang.AstProperties props);
	public void print();
	public void reduce2java(Lang.AstProperties props);
	public Object clone();
	public String tokenName();
	public boolean Equ(Lang.Token x);
	public void printWhitespaceOnly(Lang.AstProperties props);
    }

    //*****************************************************
    // Utility - this class is a general repository for
    // otherwise class-static methods that must be refined
    // by layers.  Since Java doesn't support the refinement
    // of static methods, we have to resort to adding this
    // class.
    //******************************************************

     static public class Utility { }
};