// the following class is defined by P3 users - it is the
// element type that is to be stored
// please note that all data members are *protected*
// all data members have accessor and update functions
// there can be user-defined methods as well, but they must use
// the accessor and update functions, and not direct access to protected members

class emp implements java.io.Serializable {

   protected int    empno;   // employee number
   protected int    age;     // employee age
   protected String dept;    // employee department
   protected String name;    // employee name

   // Note:  2 constructors emp(e) is needed for fast transformed constructors

   emp ( ) { }

   emp ( int e, int a, String d, String n ) {
      empno = e;
      age   = a;
      dept  = d;
      name  = n;
   }

   emp( emp e ) {
      empno = e.empno;
      age   = e.age;
      dept  = e.dept;
      name  = e.name;
   }

   // accessor and update functions

   public int empno()        { return empno; }
   public int empno( int x ) { return empno = x; }

   public int age()          { return age; }
   public int age( int x )   { return age = x; }

   public String dept()           { return dept; }
   public String dept( String x ) { return dept = x; }

   public String name()           { return name; }
   public String name( String x ) { return name = x; }


   // user-defined functions

   public void birthday() { age(age()+1); }  // increment age

   public void promote( String newdept ) { dept( newdept ); } // assign to new department

   public void print() {
      System.out.print( "    " + empno() + "  ,  " + age() + 
         "  ,  " + dept());
      for (int i = 0; i < 18 - dept().length(); i++)
         System.out.print(" ");
      System.out.println(",  " + name());
   }
}
