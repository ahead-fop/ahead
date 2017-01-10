// the following class is defined by P3 users - it is the
// element type (in JBeans set/get format) that is to be stored
// please note that all data members are *protected*
// all data members have accessor and update functions
// there can be user-defined methods as well, but they must use
// the accessor and update functions, and not direct access to protected members

class empJBeans {

   protected int    empno;   // employee number
   protected int    age;     // employee age
   protected String dept;    // employee department
   protected String name;    // employee name

   // Note:  2 constructors emp(e) is needed for fast transformed constructors

   empJBeans ( ) { }

   empJBeans ( int e, int a, String d, String n ) {
      empno = e;
      age   = a;
      dept  = d;
      name  = n;
   }

   empJBeans( empJBeans e ) {
      empno = e.empno;
      age   = e.age;
      dept  = e.dept;
      name  = e.name;
   }

   // accessor and update functions

   public int  getempno()        { return empno; }
   public void setempno( int x ) { empno = x; }

   public int  getage()          { return age; }
   public void setage( int x )   { age = x; }

   public String getdept()           { return dept; }
   public void   setdept( String x ) { dept = x; }

   public String getname()           { return name; }
   public void   setname( String x ) { name = x; }


   // user-defined functions

   public void birthday() { setage(getage()+1); }  // increment age

   public void promote( String newdept ) { setdept( newdept ); } // assign to new department

   public void print() {
      System.out.print( "empno = " + getempno() + " age = " + getage() + 
         " dept = " + getdept() + " name = " + getname() + "\n");
   }
}
