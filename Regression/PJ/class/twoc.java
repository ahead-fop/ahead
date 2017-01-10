layer y;

refines class junk {
   int d = 4; 

   junk(String x) { this(4); }

   public String toString() { return Super().toString() + " " + d; }

   public static void main( String args[] ) {
      junk j = new junk( "2" );
      System.out.println( j );
   }
}
