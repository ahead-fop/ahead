// package ckJavaLib;

package J.ckJava.library;

import java.util.Vector;

public class MyVector extends Vector {

   public MyVector() { super(); }

   public void unionNE( String s ) {
      if (s.equals("")) return;
      s = Utils.truncate(s);
      union(s);
   }

   public void union( Object p ) {
      if (!contains(p))
         add(p);
   }

   public void union( MyVector v ) {
      int i;

      for (i=0; i<size(); i++)
        this.union((ParseInfo) get(i));
   }

   public static MyVector returnUnion( MyVector a, MyVector b ) {
      MyVector v = (MyVector) a.clone();
      int i;

      for (i=0; i<b.size(); i++)
         v.union(b.get(i));
      return v;
   }

   public static MyVector returnDifference( MyVector a, MyVector b ) {
      MyVector v = (MyVector) a.clone();
      int i;

      for (i=0; i<b.size(); i++)
         v.remove(b.get(i));
      return v;
   }

   public static MyVector returnIntersect( MyVector a, MyVector b) {
      int i;
      Object o;
      MyVector v = new MyVector();

      for (i=0; i<a.size(); i++) {
         o = a.get(i);
         if (b.contains(o))
            v.union(o);
      }
      return v;
   }

   public void print(String title) {
      System.out.print(title + " : ");
      print();
   }

   public void print() {
      int i;
      Object o;

      for (i=0; i<size(); i++) {
         o = get(i);
         if (o instanceof String) {
            System.out.print((String) o + ", ");
            continue;
         }
         if (o instanceof ParseInfo) {
            ((ParseInfo) o).print();
            continue;
         }
         System.out.println("Don't know how to print " + o.getClass().getName());
      }
      System.out.println();
   }
}

