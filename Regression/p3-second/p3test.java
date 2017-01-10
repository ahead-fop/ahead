import java.io.*;
import java.util.*;

public class p3test {

   public static void main( String args[] ) {
      if (args.length == 0) {
         System.err.println("ERROR: Please specify the name of the data file.");
         System.exit(1);
      }

      empcont     ec    = new empcont();
      cur_all1    all1  = new cur_all1(ec);
      cur_all2    all2  = new cur_all2(ec);
      cur_all3    all3  = new cur_all3(ec);
      cur_point1  p1    = new cur_point1(ec);
      cur_point2  p2    = new cur_point2(ec, "Chen, Gang");
      cur_point3  p3    = new cur_point3(ec);
      cur_range1  r1    = new cur_range1(ec);
      cur_range2  r2    = new cur_range2(ec, 10600);
      cur_range3  r3    = new cur_range3(ec);
      cur_neq     neq   = new cur_neq(ec);

      /* read rawdata from the data file */

      try {
         String lineBuf;
         StringTokenizer st;
         int num = -1;

         FileInputStream fin = new FileInputStream(args[0]);
         BufferedReader din = new BufferedReader(new InputStreamReader(fin));
         
         for(;;) {
            lineBuf = din.readLine();
            if (lineBuf.length() > 0 && lineBuf.charAt(0) == '|') {
               num = Integer.parseInt((new StringTokenizer(lineBuf, "|")).nextToken());
               break;
            }
         }

         if (num <= 0)
            throw new IOException();

         for (int i = 0; i < num; i++) {
            lineBuf = din.readLine();
            st = new StringTokenizer(lineBuf, "|");

//            all1.insert(new emp( Integer.parseInt(st.nextToken()),
//               Integer.parseInt(st.nextToken()),
//               st.nextToken(), st.nextToken() ));

            ec.insert(new emp( Integer.parseInt(st.nextToken()),
               Integer.parseInt(st.nextToken()),
               st.nextToken(), st.nextToken() ));
         }

         din.close();
         fin.close();
      }
      catch (IOException ioe) {
         System.err.println("Error: " + ioe);
         System.exit(1);
      }

      System.out.println("\n Original employee data (order: empno) \n");
      for (all2.first(); all2.more(); all2.next()) {
         all2.obj().print();
      }

      System.out.println("\n Those age() == 24 (order: -empno) \n");
      for (p1.first(); p1.more(); p1.next()) {
         p1.obj().print();
      }

      System.out.println("\n Increment age of those age() <= 23 (order empno)");
      for (int i = 1; i <= 5; i++) {
         System.out.println("\n Round " + i + "\n");
         for (r3.first(); r3.more(); r3.next()) {
            r3.obj().print();
            r3.obj().birthday();
         }
      }

      System.out.println("\n Again, those age() == 24 (order: -empno) \n");
      for (p1.first(); p1.more(); p1.next()) {
         p1.obj().print();
      }
      
      System.out.println("\n Delete those name() == \"Chen, Gang\" (orderby empno) \n");
      for (p2.first(); p2.more(); p2.next()) {
         p2.obj().print();
         p2.remove();
      }

      System.out.println("\n Delete those name() == \"Batory, Don\" (orderby empno) \n");
      p2.n = "Batory, Don";
      for (p2.first(); p2.more(); p2.next()) {
         p2.obj().print();
         p2.remove();
      }

      System.out.println("\n Those age() > 30 && age() < 40 && name() >= \"H\" (orderby name) \n");
      for (r1.first(); r1.more(); r1.next()) {
         r1.obj().print();
      }

      System.out.println("\n Those dept() == \"Computer Science\" (orderby -name) \n");
      for (p3.first(); p3.more(); p3.next()) {
         p3.obj().print();
      }

      System.out.println("\n Update dept of those empno > 10600 && empno < 10700 (orderby -empno) \n");
      for (r2.first(); r2.more(); r2.next()) {
         r2.obj().print();
         r2.obj().promote("Computer Science");
      }

      System.out.println("\n Update dept of those empno > 10900 && empno < 11000 (orderby -empno) \n");
      r2.x = 10900;
      for (r2.first(); r2.more(); r2.next()) {
         r2.obj().print();
         r2.obj().promote("Computer Science");
      }

      System.out.println("\n Delete those dept() == \"Computer Science\" (orderby -name) \n");
      for (p3.first(); p3.more(); p3.next()) {
         p3.obj().print();
         p3.remove();
      }

      System.out.println("\n Again, those dept() == \"Computer Science\" (orderby -name) \n");
      for (p3.first(); p3.more(); p3.next()) {
         p3.obj().print();
      }

      System.out.println("\n Update dept of all employee (orderby empno) \n");
      for (all2.first(); all2.more(); all2.next()) {
         all2.obj().print();
         all2.obj().promote("Computer Science");
      }

      System.out.println("\n Delete those dept() == \"Computer Science\" (orderby -name) \n");
      for (p3.first(); p3.more(); p3.next()) {
         p3.obj().print();
         p3.remove();
      }

      System.out.println("\n Remaining employee \n");
      for (all1.first(); all1.more(); all1.next()) {
         all1.obj().print();
      }

      System.out.println("\n Set age of all employees to 100 \n");
      for (all3.first(); all3.more(); all3.next()) {
         all3.obj().age(100);
      }

      System.out.println("\n Print those age() != 100 \n");
      for (neq.first(); neq.more(); neq.next()) {
         neq.obj().print();
      }
   }
}
