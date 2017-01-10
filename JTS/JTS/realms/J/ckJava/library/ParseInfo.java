// package ckJavaLib;

package J.ckJava.library;

public class ParseInfo implements java.io.Serializable {

   public String    name;		// qualified name of class or method
   public boolean   ClassParseInfo;     // am I a ClassParseInfo?
   public int       MccabeCounter;      // for mccabe metric
   public int       LocCounter;         // lines of code
   public MyVector  methodsCalled;	// vector of String identifiers
   public MyVector  classRefs;          // class reference strings
   public MyVector  varDefs;            // set of variable definitions
   public MyVector  varRefs;            // set of variable references
   public int       depth;              // depth in hierarchy

   public void print() {
      if (ClassParseInfo) printClassParseInfo();
      else printMethodParseInfo();
   }

   public boolean equals( Object o ) {
      ParseInfo p = (ParseInfo) o;

      return p.name.equals(name);
   }

   // specific to ParseInfo objects of classes

   public String    superName;		// qualified name of superclass
   
   public MyVector  methods;		// vector of ParseInfo methods
   public int       wordcount;          // count of words inside class def.

   public ParseInfo( String name, ParseInfo superScope, String superName ) {
      if (superScope == null)
         this.name = name;
      else {
         if (superScope.name == null)
            this.name = name;
         else
            this.name = superScope.name + "." + name;
      }
      this.superName = superName;
      methods        = new MyVector();
      methodsCalled  = new MyVector();
      classRefs      = new MyVector();
      varDefs        = new MyVector();
      varRefs        = new MyVector();
      ClassParseInfo = true;
      MccabeCounter  = 0;
      depth          = 0;
      wordcount      = 0;
   }

   public void printClassParseInfo() {
      System.out.println("Class Name = "+name +
                         "   SuperClass = " + superName);
      System.out.println("Mccabe # = "+ MccabeCounter + "  Loc = " + LocCounter +
                         "wordcount = " + wordcount );
      classRefs.print("ClassReferences");
      varDefs.print("Variable Definitions");
      varRefs.print("Variable References");
      System.out.println();
      methods.print("MethodNames");
   }

   // CK METRIC Estimations here

   // merge two ParseInfo vectors on methods

   public static MyVector Merge( MyVector a, MyVector b ) {
      MyVector c = (MyVector) a.clone();
      int i,j;
      ParseInfo bb, cc;
      boolean found;


      for (i=0; i<b.size(); i++) {
        bb = (ParseInfo) b.get(i);
        found = false;
        for (j=0; j<c.size(); j++) {
           cc = (ParseInfo) c.get(j);
           if (cc.equals(bb)) {
              cc.MccabeCounter = cc.MccabeCounter + bb.MccabeCounter;
              cc.LocCounter    = cc.LocCounter + bb.LocCounter;
              cc.methodsCalled = MyVector.returnUnion(cc.methodsCalled,
                                 bb.methodsCalled);
              cc.classRefs     = MyVector.returnUnion(cc.classRefs,
                                 bb.classRefs);
              cc.varDefs       = MyVector.returnUnion(cc.varDefs,
                                 bb.varDefs);
              cc.varRefs       = MyVector.returnUnion(cc.varRefs,
                                 bb.varRefs);
              found = true;
              break;  // we've done our union
           }
        }
        if (!found) c.add(bb);
      }
      return c;
   }
      
   public void propagate( ParseInfo cumulative ) {
      ParseInfo mySuper;

      // collect information from superclasses

      if (superName == null || superName.equals(""))
         return;  // done

      mySuper = (ParseInfo) Utils.readObject(superName + ".ck");
      if (mySuper == null) 
        return;

      cumulative.depth++;
      cumulative.wordcount     = cumulative.wordcount + mySuper.wordcount;
      cumulative.MccabeCounter = cumulative.MccabeCounter  
                                 + mySuper.MccabeCounter;
      cumulative.LocCounter    = cumulative.LocCounter 
                                 + mySuper.LocCounter;
      cumulative.methodsCalled = MyVector.returnUnion( mySuper.methodsCalled,
                                 cumulative.methodsCalled );
      cumulative.classRefs     = MyVector.returnUnion( mySuper.classRefs,
                                 cumulative.classRefs );
      cumulative.varDefs       = MyVector.returnUnion( mySuper.varDefs,
                                 cumulative.varDefs );
      cumulative.varRefs       = MyVector.returnUnion( mySuper.varRefs,
                                 cumulative.varRefs );
      cumulative.methods       = Merge( mySuper.methods, cumulative.methods );
      mySuper.propagate( cumulative );
   }

   public static void usage() {
      System.out.println( "usage: java ParseInfo <filename>");
      System.exit(1);
   }

   public static void main( String args[] ) {
      if (!(args.length == 1 || args.length == 2))
         usage();
      ParseInfo po = (ParseInfo) Utils.readObject( args[0] );

      po.propagate(po); 
      if (args.length == 2)
        po.print();
      output(po);
   }

   public static void output( ParseInfo po ) {
      System.out.print("Class " + po.name + "   ");
      System.out.print("wmc = " + po.wmc() + "   ");
      System.out.print("dit = " + po.dit() + "   ");
      System.out.print("cbo = " + po.cbo() + "   ");
      System.out.print("rfc = " + po.rfc() + "   ");
      System.out.print("lcm = " + po.lcom()+ "   ");
      System.out.print("mcc = " + po.mcc() + "   ");
      System.out.print("loc = " + po.loc() + "   ");
      System.out.print("wc  = " + po.wc()  + "   ");
      System.out.println();
   } 

   public static void main2( String args[] ) {
      if (!(args.length == 1 || args.length == 2))
         usage();
      ParseInfo po = (ParseInfo) Utils.readObject( args[0] );

      if (args.length == 2)
        po.print();
      output(po);
   }


   public int wc() { // word counter
      return wordcount;
   }

   public int mcc() { // mccabe counter
      int sum = MccabeCounter;
      int i;

      for (i=0; i<methods.size(); i++)
         sum = sum + ((ParseInfo) methods.get(i)).MccabeCounter;
      return sum;
   }
       
   public int loc() { // mccabe counter
      int sum = LocCounter;
      int i;

      for (i=0; i<methods.size(); i++)
         sum = sum + ((ParseInfo) methods.get(i)).LocCounter;
      return sum;
   }

   public int dit() {
      return depth;
   }

   public int wmc() { // return number of methods
      return methods.size();
   }

   public int cbo() { // return # of non-inheritance related couplings
      int i, j;
      MyVector v = new MyVector();

      for (i=0; i<classRefs.size(); i++)
         v.union( (String) classRefs.get(i) );

      for (i=0; i<methods.size(); i++)
         v = MyVector.returnUnion(v,((ParseInfo) methods.get(i)).classRefs);
      return v.size();
   }

   public int rfc() { // return # of methods + # of method references
      int sum, i;
      MyVector v = new MyVector();

      for (i=0; i<methods.size(); i++)
         v.union( ((ParseInfo) methods.get(i)).name );

      for (i=0; i<methods.size(); i++) 
         v = MyVector.returnUnion(v,((ParseInfo) methods.get(i)).methodsCalled);
      return v.size();
   }

   public int lcom() {
      int m, empty, nonempty, i, j;
      ParseInfo meth, methi, methj;

      // first compute the set of all instance variables ref'ed by each method
      // (set of class defined ivars ) - (set of var's ref'd by method)

      for (m=0; m<methods.size(); m++) {
          meth = (ParseInfo) methods.get(m);
          meth.IVarRefs = MyVector.returnIntersect( varDefs, meth.varRefs );
      }
    
      // next, for each pair of methods, compute the intersection of their
      // IVarRefs sets.  see if the set is empty or not, and note the result

      empty = nonempty = 0;

      for (i=0; i<methods.size(); i++) {
         methi = (ParseInfo) methods.get(i);
         for (j=i+1; j<methods.size(); j++) {
            methj = (ParseInfo) methods.get(j);
            if (MyVector.returnIntersect(methi.IVarRefs, methj.IVarRefs).size() == 0)
               empty++;
            else
               nonempty++;
         }
      }
      return empty - nonempty;
   }

   // specific to ParseInfo objects of methods

   public MyVector  IVarRefs;          // set of instance vars refed by method

   public ParseInfo( String name, ParseInfo superScope ) {
      this.name = name;
      methodsCalled   = new MyVector();
      classRefs       = new MyVector();
      varDefs         = new MyVector();
      varRefs         = new MyVector();
      ClassParseInfo  = false;
      MccabeCounter   = 0;
      LocCounter      = 0;
      depth           = 0;
      IVarRefs        = null;
   }

   public void printMethodParseInfo() {
      System.out.println("Method Name = "+name);
      System.out.println("Mccabe # = "+ MccabeCounter + " Loc = " + LocCounter);
      System.out.print("Methods Called = ");
      methodsCalled.print();
      classRefs.print("Class References");
      varDefs.print("Variable Definitions");
      varRefs.print("Variable References");
      System.out.println();
   }
}
