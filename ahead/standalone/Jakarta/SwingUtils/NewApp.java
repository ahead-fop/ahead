package Jakarta.SwingUtils;

import java.io.*;
import javax.swing.*;

public class NewApp {

   static void error() {
      System.err.println("Usage: SwingUtils.NewApp -f <name-of-new-swing-app>");
      System.err.println("       <name-of-new-swing-app>.java will be generated");
      System.err.println("");
      System.err.println(" or:   SwingUtils.NewApp -a");
      System.err.println("       SwingApplet.java will be generated");
      System.err.println("");
      System.err.println(" or:   SwingUtils.NewApp -d <name-of-new-swing-dialog>");
      System.err.println("       <name-of-new-swing-dialog>.java will be generated");
      System.err.println("");
      System.err.println(" or:   SwingUtils.NewApp");
      System.err.println("       to present GUI");

      System.exit(1);
   }

   static void generateFile( PrintWriter p, String filename, boolean dialog ) 
          throws Exception {
      p.println("// " + filename + ".java ");
      p.println("");
      p.println("import SwingUtils.*;");
      p.println("import java.awt.*;");
      p.println("import java.awt.event.*;");
      p.println("import javax.swing.*;");
      p.println("");
      p.print("public class " + filename + " extends ");
      if (dialog) 
         p.println(" SwingDialog {");
      else
         p.println(" SwingApp {");
      p.println("");
      p.println("   // initialize constants used in the application");
      p.println("   // REMEMBER -- make constants static!");
      p.println("");
      p.println("   public void initConstants() {");
      p.println("");
      p.println("   }");
      p.println("");
      p.println("   // declare and initialize atomic components here");
      p.println("");
      p.println("   public void initAtoms() {");
      p.println("");
      p.println("   }");
      p.println("");
      p.println("   // declare and initialize layout components here");
      p.println("");
      p.println("   public void initLayout() {");
      p.println("");
      p.println("   }");
      p.println("");
      p.println("   // initialize ContentPane here");
      p.println("");
      p.println("   public void initContentPane() {");
      p.println("      ContentPane = new JPanel();");
      p.println("      ContentPane.setLayout( new GridLayout(1,0) );");
      p.println("      ContentPane.setBorder(BorderFactory.createEtchedBorder());");
      p.println("");
      p.println("   }");
      p.println("");
      p.println("   // initialize listeners here");
      p.println("");
      p.println("   public void initListeners() {");
      p.println("");
      p.println("   }");
      p.println("");
      p.println("   // place in this method any action for exiting application");
      p.println("");
      p.println("   public void applicationExit() {");
      p.println("");
      p.println("   }");
      p.println("");
      if (dialog) {
         p.println("   public " + filename + "(JFrame owner, boolean modal) {");
         p.println("      super(owner, modal);");
         p.println("   } ");
         p.println("");
         p.println("   public " + filename + "(JFrame owner, String AppTitle, boolean modal) {");
         p.println("      super(owner, AppTitle, modal);");
         p.println("   } ");
         p.println("");
         p.println("}");
      }
      else {
         p.println("   public " + filename + "() { super(); } ");
         p.println("");
         p.println("   public " + filename + "(String AppTitle) { super(AppTitle); } ");
         p.println("");
         p.println("   public static void main(String[] args) {");
         p.println("      new " + filename + "(\"" + filename + "\");");
         p.println("   }");
         p.println("}");
      }
      
   }

   static void generateSwingApplet( PrintWriter p ) throws Exception {
      p.println("// SwingApplet.java ");
      p.println("");
      p.println("import java.awt.*;");
      p.println("import java.awt.event.*;");
      p.println("import javax.swing.*;");
      p.println("import java.net.*;");
      p.println("");
      p.println("public class SwingApplet extends JApplet {");
      p.println("");
      p.println("   public JPanel ContentPane;");
      p.println("");
      p.println("   public void initAtoms() {};");
      p.println("   public void initLayout() {};	");
      p.println("   public void initContentPane() {}; ");
      p.println("   public void initListeners() {}; ");
      p.println("");
      p.println("   public void init() {");
      p.println("      initAtoms();			 // initialize atoms");
      p.println("      initLayout();			 // initialize layout");
      p.println("      initContentPane();		 // initialize content pane");
      p.println("      initListeners();                  // initialize listeners");
      p.println("      getContentPane().add(ContentPane);// set content pane of window");
      p.println("   }");
      p.println("");
      p.println("   // a handy method for obtaining URLs of resources");
      p.println("   ");
      p.println("   public Class myClass = null;");
      p.println("");
      p.println("   public URL getResourceURL( String filename ) {");
      p.println("     if (myClass == null)");
      p.println("        myClass = this.getClass();      ");
      p.println("     return myClass.getResource(filename);");
      p.println("   }");
      p.println("");
      p.println("   public SwingApplet( ) { }");
      p.println("}");
   }

   public static void main( String args[] ) {
      String filename = null;
      PrintWriter p = null;

      try {

         if (args.length > 2)
            error();
         else
         if (args.length == 0) {
            new NewAppDialog();
            System.exit(1);
         }
         else
         if (args[0].equals("-f") && args.length == 2) {
            filename = args[1]; 
            p = new PrintWriter( new FileWriter( filename + ".java" ) );
            generateFile( p, filename, false );
            p.close();
         }
         else
         if (args[0].equals("-a") && args.length <= 2) {
            filename = "SwingApplet"; 
            p = new PrintWriter( new FileWriter( filename + ".java" ) );
            generateSwingApplet( p );
            p.close();
         }
         else 
         if (args[0].equals("-d") && args.length == 2) {
            filename = args[1]; 
            p = new PrintWriter( new FileWriter( filename + ".java" ) );
            generateFile( p, filename, true );
            p.close();
         }
         else
            error();
         JOptionPane.showMessageDialog( null, filename + 
                                        ".java successfully generated");
         System.exit(0);
      }
      catch (Exception e) {
         JOptionPane.showMessageDialog(null, 
             filename + ".java not generated correctly. " + e.getMessage(),
             "Error!", JOptionPane.ERROR_MESSAGE);
         System.err.println(filename + ".java not generated correctly");
         System.err.println( e.getMessage() );
         System.err.println( args[0] + " " + args[1] );
         System.exit(1);
      }
   }
}
