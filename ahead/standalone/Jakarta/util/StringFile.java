package Jakarta.util;

import java.io.*;
import java.util.*;

public class StringFile extends LinkedList {

	public static int        currentClassNum;				// current class mangle number
	public static String     className;						// unmangled name of class to generate
	public static String     nameSpaceName;				// name of namespace to generate
	public static boolean    dotH;							// are we translating dot H files?
   public static HashSet    uniqueImports;
	public static StringFile importStatements;

	public String fileName;												// name of file

	public static void init( String cName, String nName, boolean dH ) {
      currentClassNum = 0;
		className = cName;
		nameSpaceName = nName;
		dotH = dH;
		uniqueImports = new HashSet();
		importStatements = new StringFile("");
	}

	public StringFile( String fileName ) {
		this.fileName = fileName;
	}
  
	// reads file URL and stuffs its contents into a list of strings
	// one string per line
	//
	
   public StringFile readFile() {
		BufferedReader file;

      try {
			file = new BufferedReader( new FileReader( fileName ) );
			while (true) {
				String line = file.readLine();
				if (line == null) 
					break;
				add(line);
			}
			file.close();
      } catch (IOException e) { 
			Util.fatalError( "in reading " + fileName + " : " + e.getMessage());
		}
		return this;
   }

	public void harvestImports() {
		ListIterator li = listIterator(0);
		while (li.hasNext()) {
			String line = (String) li.next();
			if (line.startsWith("import_ ")) {
				String importItem = line.substring(7).trim();
				if (!uniqueImports.contains(importItem)) {
					uniqueImports.add(importItem);
					importStatements.add("#include " + importItem);
				}
			}
		}
	}

	// prints contents of file as is
	public void print( PrintStream ps ) {
		ListIterator li = listIterator(0);
		while (li.hasNext()) 
			ps.println( (String) li.next() );
		return;
	}

	// prints contents of file but additionally
	// adds "//" to the front of any import_ statements
	//
	public void printImports( PrintStream ps ) {
		ListIterator li = listIterator(0);
		while (li.hasNext()) {
			String line = (String) li.next();
         if (line.startsWith("import_"))
				ps.print("//");
			ps.println( line );
		}
		return;
	}


	// for debugging only
	public static void main( String args[] ) {
		StringFile l = new StringFile(args[0]);
		init( args[1], args[2], false );

		PrintStream ps = System.out;
		l.readFile();
      l.harvestImports();
		importStatements.print( ps );
		l.header( ps, true, args[0] );
		l.printImports( ps );
		l.footer( ps );
   }

	public void header( PrintStream ps, boolean last, String fileName ) {
		if (dotH) {
         ps.println("");
			ps.println("class " + className + ";");
		}
      ps.println("");
		ps.println("#ifdef class_");
		ps.println("#undef class_");
		ps.println("#undef super_");
		ps.println("#undef namespace_");
		ps.println("#endif");
		ps.println("");
      ps.println("#define super_ " + className + "__" + currentClassNum++);
		if (last)
         ps.println("#define class_ " + className );
		else
         ps.println("#define class_ " + className + "__" + currentClassNum  );
		if (nameSpaceName != null)
			ps.println("#define namespace_ " + nameSpaceName);
		ps.println("");
		ps.println("//SoUrCe " + fileName );
   }

	public void footer( PrintStream ps ) {
		ps.println("//EnDSoUrCe ");
	}

	public static void printImportStatements( PrintStream ps ) {
		ps.println("#ifndef _import_");
		importStatements.print(ps);
		ps.println("#define _import_");
		ps.println("#endif");
	}
}
