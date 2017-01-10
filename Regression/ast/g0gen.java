 
//------------------------------------------
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

// makef is a class that generates a make file for 
// dos and Unix to compile a Bali-generated compiler
// (e.g., Jakarta).

class g0 {

   // reportError just prints out to the command line how to run makef

   public static void reportError() {
      System.err.println("Usage: g0 <projectName-name>");
      System.exit(40);
   }

   // main allows makef to be run from the command line

   public static void main(String argv[]) {
      if (argv.length == 0) g0.reportError();
      if (argv[0].length() == 0) g0.reportError();
      createMakeFile(argv[0]);
   }

   // here's where the real work is done

   public static void createMakeFile(String projectName) {
      File batchfile = null;
      File directory = null;
      Process proc;
      Runtime rt;
      FileOutputStream fos = null;
      PrintWriter ps;
      String filename = projectName + "/makefile";  // used only for printing announcements
      String cmd[] = new String[1];

      // Step 1: initialize the directory

      directory = new File(projectName);
      if (directory.exists()) {
         if (! directory.isDirectory())
            directory.renameTo(new File(projectName +".old"));
         }
      else
         directory.mkdir();


      // Step 1: initialize the makefile

      try {
         batchfile = new File(projectName, "makefile");
      }
      catch (Exception e) {
         System.err.println(e);
         System.err.println("Can't open file " + filename + " for output");
         System.exit(40);
      }

      // Step 2: delete the file if it already exists

      if (batchfile.exists()) {
         System.out.println(filename + "exists and is being deleted");
         batchfile.delete();
      }

      // Step 3: create file output stream

      try {
         fos = new FileOutputStream(batchfile);
       }
        catch (Exception e) {
          System.err.println(e);
          System.err.println("Can't open stream " + filename + " for output");
          System.exit(40);
       };    //<-------- escape here
       
       // Step 4: create print stream; output makefile contents and close

       ps = new PrintWriter(fos);
       ps.println("Prj="+ projectName);
       ps.println("");
       ps.println("all: $(Prj)Sym.class $(Prj).class $(Prj)Scanner.class $(Prj)Parser.class");
       ps.println("");
       ps.println("$(Prj)Sym.class : $(Prj)Sym.java");
       ps.println("	javac -g $(Prj)Sym.java");
       ps.println("");
       ps.println("$(Prj)Scanner.class : $(Prj)Scanner.java");
       ps.println("	javac -g $(Prj)Scanner.java");
       ps.println("");
       ps.println("$(Prj).class : $(Prj).java");
       ps.println("	javac -g $(Prj).java");
       ps.println("");
       ps.println("$(Prj)Parser.java $(Prj)Sym.java : $(Prj).cup");
       ps.println("	java -mx32m java_cup.Main -expect 8 -parser $(Prj)Parser -symbols JakartaSym <Jakarta.cup");
       ps.println("");
       ps.println("$(Prj)Scanner.java : $(Prj).lex");
       ps.println("	java JLex.JLex $(Prj).lex");
       ps.println("");
       ps.println("$(Prj).java $(Prj).lex $(Prj).cup : $(Prj).b");
       ps.println("	java BaliC jakarta.b ");
       ps.println("");
       ps.close();

       // Step 5: announce that the makefile was create

       System.out.println(filename + " was created");
    }
}