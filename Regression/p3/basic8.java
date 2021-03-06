/* this java file illustrates the basic organization of classes that will
   be generated by P3.
*/

class basic8 {

   public static void main( String args[] ) {
      empcont1  ec;
      t1        c1;
      t2        c2;
      t3        c3;
      emp       obj;

      emp[] rawdata = {
                         new emp( 10000,60,"Biology","Akers, Mark" ),
                         new emp( 10070,22,"CompSci","Andrews, Kay" ),
                         new emp( 10020,21,"Biology","Alexander, Joe" ),
                         new emp( 10010,40,"Physics","Akin, Monica" ),
                         new emp( 10050,42,"Biology","Akerson, Suzanne" ),
                         new emp( 10040,53,"Astrono","Akerson, Mary" ),
                         new emp( 10060,61,"CompSci","Andrews, John" ),
                         new emp( 10030,23,"Biology","Akerson, Gwyn" )
                      };
      int i;

      ec = new empcont1();
      c1 = new t1( ec );
      c2 = new t2( ec );
      c3 = new t3( ec, "Biology" );

      System.out.println("original employee data\n");

      for (i=0; i<rawdata.length; i++) {
         c1.insert(rawdata[i]);
         c1.obj().print();
      }

      System.out.println("dept() == Biology && name() == Akerson, Gwyn && age > 20");

      for (obj = c1.first(); c1.more(); obj = c1.next()) {
         obj.print();
      }

      System.out.println("read again");
    
      for (c1.first(); c1.more(); c1.next()) {
         c1.obj().print();
      }

      System.out.println("delete name() == Akerson, Gwyn && age <40 ++ delete\n\n");

      for (c2.first(); c2.more(); c2.next()) {
         c2.obj().print();
         c2.remove();
      }

      System.out.println("dept() == Biology && name() == Akerson, Gwyn && age > 20");

      for (obj = c1.first(); c1.more(); obj = c1.next()) {
         obj.print();
      }

      System.out.println("\nretrieve employees from Biology");
      for (c3.first(); c3.more(); c3.next()) {
         c3.obj.print();
      }

      System.out.println("\n\nretrieve employees from CompSci");
      c3.x = "CompSci";
      for (c3.first(); c3.more(); c3.next()) {
         c3.obj.print();
      }

      System.out.println("\n\nDone");
   }
} 
