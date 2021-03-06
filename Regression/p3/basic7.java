/* this java file illustrates the basic organization of classes that will
   be generated by P3.
*/

class basic7 {

   public static void main( String args[] ) {
      empcont1  ec = new empcont1();
      t0        c0 = new t0(ec);
      t1        c1 = new t1(ec);
      t2        c2 = new t2(ec);
      t3        c3 = new t3(ec);
      t4        c4 = new t4(ec);
      t5        c5 = new t5(ec);
      t6        c6 = new t6(ec);
      t7        c7 = new t7(ec);

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

      for (i=0; i<rawdata.length; i++) {
         c0.insert(rawdata[i]);
      }

      System.out.println("t0 - print all");
      for (c0.first(); c0.more(); c0.next()) {
         c0.obj().print();
      }

      System.out.println("dept == biology");
      for (c1.first(); c1.more(); c1.next()) {
         c1.obj().print();
      }

      System.out.println("age > 20");
      for (c2.first(); c2.more(); c2.next()) {
         c2.obj().print();
      }

      System.out.println("age > 20  && dept == biology");
      for (c3.first(); c3.more(); c3.next()) {
         c3.obj().print();
      }

      System.out.println("age < 40");
      for (c4.first(); c4.more(); c4.next()) {
         c4.obj().print();
      }

      System.out.println("age < 40 && dept == biology");
      for (c5.first(); c5.more(); c5.next()) {
         c5.obj().print();
      }

      System.out.println("20 < age < 40 && dept == biology");
      for (c6.first(); c6.more(); c6.next()) {
         c6.obj().print();
      }

      System.out.println("20 < age < 40");
      for (c7.first(); c7.more(); c7.next()) {
         c7.obj().print();
      }

      System.out.println("\n\nDone");
   }
} 
