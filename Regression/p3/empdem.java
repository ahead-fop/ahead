// the following class is defined by P3 users - it is the
// element type (in demeter set/get format) that is to be stored
// please note that all data members are *protected*
// all data members have accessor and update functions
// there can be user-defined methods as well, but they must use
// the accessor and update functions, and not direct access to protected members

class empdem {

   protected int    empno;   // employee number
   protected int    age;     // employee age
   protected String dept;    // employee department
   protected String name;    // employee name

   // Note:  2 constructors emp(e) is needed for fast transformed constructors

   empdem ( ) { }

   empdem ( int e, int a, String d, String n ) {
      empno = e;
      age   = a;
      dept  = d;
      name  = n;
   }

   empdem( empdem e ) {
      empno = e.empno;
      age   = e.age;
      dept  = e.dept;
      name  = e.name;
   }

   // accessor and update functions

   public int  get_empno()        { return empno; }
   public void set_empno( int x ) { empno = x; }

   public int  get_age()          { return age; }
   public void set_age( int x )   { age = x; }

   public String get_dept()           { return dept; }
   public void   set_dept( String x ) { dept = x; }

   public String get_name()           { return name; }
   public void   set_name( String x ) { name = x; }


   // user-defined functions

   public void birthday() { set_age(get_age()+1); }  // increment age

   public void promote( String newdept ) { set_dept( newdept ); } // assign to new department

   public void print() {
      System.out.print( "empno = " + get_empno() + " age = " + get_age() + 
         " dept = " + get_dept() + " name = " + get_name() + "\n");
   }
}
