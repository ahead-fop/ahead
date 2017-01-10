/**********************************************************
	class: bcstubgenerator.Main
**********************************************************/

package bcstubgenerator;

import java.io.*;
import java.util.*;
import mmatrix.*;

public class Main {

	public static void main (String[] argv){
			    if (argv.length == 0){
					System.out.println("Using bcstubgenerator <modelname>.");
					System.exit(0);
				}
                String modelDir = argv[0];
                File dir = new File(modelDir);
                if (!dir.isDirectory())
                        System.out.println("invalid model directory!");
                else{
                        //get all the layers inside the model
                        String[] list = dir.list();
                        ArrayList layers = new ArrayList();
                        //System.out.println("Layers: ");
                        for (int i=0; i<list.length; i++){
                                File file = new File(dir,list[i]);
                                if (file.isDirectory() && !list[i].equals("stub") && !list[i].equals("refines")){
                                	layers.add(file);
                                	//System.out.println(file.getPath());
								}
                        }
                        //get all the files in the layers
                        Hashtable units = new Hashtable();
                        ArrayList alist;
                        for (int j=0; j<layers.size(); j++){
								//System.out.println("files in layer"+j);
                                list = ((File)layers.get(j)).list();
                                for (int k=0; k<list.length; k++){
                                        if (list[k].endsWith(".java")){
												//System.out.println(list[k]);
                                                if (units.containsKey(list[k])){
                                                        alist=(ArrayList)units.get(list[k]);
                                                        alist.add(layers.get(j));
                                                }
                                                else{
                                                        alist=new ArrayList();
                                                        alist.add(layers.get(j));
                                                        units.put(list[k],alist);
                                                }
                                        }
                                }
                        }

                        //call the stub genrating process on each entry in the hashtable
                        File stub = new File(dir, "stub");
                        File refine = new File(dir, "refine");
                        if (!stub.isDirectory())
                                stub.mkdir();
                        if (!refine.isDirectory())
                                refine.mkdir();
                        Iterator it = units.keySet().iterator();
                        while (it.hasNext()){
                                String key = (String)it.next();
                                //System.out.println(key + " in layers:");
                                alist = (ArrayList)units.get(key);
                                Vector operands = new Vector();
                                for (int x=0; x<alist.size(); x++){
                                	 //System.out.println("  "+((File)alist.get(x)).getPath());
                                     operands.add(new File((File)alist.get(x), key));
								 }
                                //generating stub process
                                stubProcess(stub, refine, key, operands);
                        }
                }
         }

         public static void stubProcess(File stub, File refine, String key, Vector operands){

                //Hashset to search the repeated definitions

                HashSet definitions = new HashSet();
                HashSet imports = new HashSet();
                HashSet extendClasses = new HashSet();
                HashSet implInterfaces = new HashSet();

                StringBuffer header = new StringBuffer();
                StringBuffer bodyHeader1_stub = new StringBuffer();
                StringBuffer bodyHeader1_refine = new StringBuffer();
                StringBuffer bodyHeader2 = new StringBuffer();
                StringBuffer bodyHeader3 = new StringBuffer();
                StringBuffer fields_stub = new StringBuffer();
                StringBuffer fields_refine = new StringBuffer();
                StringBuffer constructors_stub = new StringBuffer();
                StringBuffer constructors_refine = new StringBuffer();
                StringBuffer methods_stub = new StringBuffer();
                StringBuffer methods_refine = new StringBuffer();
                StringBuffer nested = new StringBuffer();

                boolean hasNoArgCons = false;
                boolean isInterface=true;
                for(int i=0; i < operands.size(); i++) {
                        try{
                                File file = (File)operands.get(i);
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line = br.readLine();
                                boolean isEnd = false;
                                while (line!=null && !isEnd){
                                        //System.out.println(line);
                                        if (line.startsWith("package")||line.equals("")||line.startsWith("import")
                                                ||line.startsWith("//")){
                                                if (!line.startsWith("//") && !line.startsWith("import stub.")){
                                                        if (!imports.contains(line)){
                                                                header.append(line);
                                                                header.append("\n");
                                                                imports.add(line);
                                                        }
                                                }
                                                //System.out.println("here, false");
                                        }
                                        else{
                                                isEnd = true;
                                                //System.out.println("here, true");
                                        }
                                        line = br.readLine();
                                }
                                br.close();

                                MMOutput output = mmatrix.Main.eval( file.getAbsolutePath() );
                                //get the body

                                getClassModifiers(bodyHeader1_stub, output.getModifiers(), true);
                                getClassModifiers(bodyHeader1_refine, output.getModifiers(), false);
                                if (i==0){
									if (output.getType().equals(MMGlobals.Class))
									        isInterface = false;
									else
											isInterface = true;
								}

                                if (i==operands.size()-1){
                                        if (!isInterface){
                                                bodyHeader1_stub.append("class ");
                                                bodyHeader1_refine.append("class ");
                                        }
                                        else{
                                                bodyHeader1_stub.append("interface ");
                                                bodyHeader1_refine.append("interface ");
                                        }
                                        bodyHeader1_stub.append(output.getName());
                                        bodyHeader1_refine.append(output.getName());
                                }

                                //output the list of the super classes
                                Iterator it = output.values().iterator();
                                while (it.hasNext()) {
                                        NamedVector nv = (NamedVector) it.next();
                                        if (!nv.getName().equals("super")){
                                                if (nv.getName().equals(MMGlobals.Classes)){
                                                        Iterator it1 = nv.iterator();
                                                        if (it1.hasNext()){
                                                                String str = (String) it1.next();
                                                                if (!str.startsWith("stub.") && !extendClasses.contains(str)){
                                                                        if (bodyHeader2.length()==0){
                                                                                bodyHeader2.append(" extends ");
                                                                                bodyHeader2.append(str);
                                                                        }
                                                                        else{
                                                                                bodyHeader2.append(", ").append(str);
                                                                        }
                                                                        extendClasses.add(str);

                                                                }
                                                                while (it1.hasNext()){
                                                                        str = (String) it1.next();
                                                                        if (!str.startsWith("stub.") && !extendClasses.contains(str)){
                                                                                bodyHeader2.append(", ").append(str);
                                                                                extendClasses.add(str);
                                                                        }
                                                                }
                                                        }
                                                }
                                                else{
                                                        Iterator it1 = nv.iterator();
                                                        if (it1.hasNext()){
                                                                String str = (String) it1.next();
                                                                if (!str.startsWith("stub.") && !implInterfaces.contains(str)){
                                                                        if (bodyHeader3.length()==0){
                                                                                if (isInterface)
                                                                                        bodyHeader3.append(" extends ");
                                                                                else
                                                                                        bodyHeader3.append(" implements ");
                                                                                bodyHeader3.append(str);
                                                                        }
                                                                        else{
                                                                                bodyHeader3.append(", ").append(str);
                                                                        }
                                                                        implInterfaces.add(str);

                                                                }
                                                                while (it1.hasNext()){
                                                                        str = (String) it1.next();
                                                                        if (!str.startsWith("stub.") && !implInterfaces.contains(str)){
                                                                                bodyHeader3.append(", ").append(str);
                                                                                implInterfaces.add(str);
                                                                        }
                                                                }
                                                        }

                                                }
                                        }
                                }
                                //body.append(" {\n");//finished the headline of the class

                                //get the list of the methods, constructors and fields
                                if (output!=null){
                                        MMHashMap theNested=output.getNested();
                                        if (theNested != null) {
                                                it = theNested.values().iterator();
                                                while (it.hasNext()) {
                                                        MMOutput o = (MMOutput) it.next();
                                                        String modifiers_stub = getModifiers(o.getModifiers(),true);
                                                        String modifiers_refine = getModifiers(o.getModifiers(),false);
                                                        String str = o.getName();
                                                        if (!definitions.contains(str)){
                                                                definitions.add(str);
                                                                if (o.getType().equals(MMGlobals.Field)){
                                                                        fields_stub.append("   ");
                                                                        fields_refine.append("   ");
                                                                        fields_stub.append(modifiers_stub);
                                                                        fields_refine.append(modifiers_refine);
                                                                        fields_stub.append(str.substring(str.lastIndexOf(" ")+1));
                                                                        fields_refine.append(str.substring(str.lastIndexOf(" ")+1));
                                                                        fields_stub.append(" ");
                                                                        fields_refine.append(" ");
                                                                        fields_stub.append(str.substring(0,str.lastIndexOf(" ")));
                                                                        fields_refine.append(str.substring(0,str.lastIndexOf(" ")));
                                                                        if (o.containsKey("finalValue")){
                                                                                NamedVector nv =  (NamedVector)o.get("finalValue");
                                                                                Iterator it0 = nv.iterator();
                                                                                while (it0.hasNext()){
                                                                                        String tmp = (String)it0.next();
                                                                                        fields_stub.append(tmp);
                                                                                        fields_refine.append(tmp);
                                                                                }
                                                                        }
                                                                        else{
                                                                                fields_stub.append(" = ");
                                                                                fields_refine.append(" = ");
                                                                                String s=str.substring(str.lastIndexOf(" ")+1);
                                                                                if (s.equals("boolean")){
                                                                                        fields_stub.append("false");
                                                                                        fields_refine.append("false");
                                                                                }
                                                                                else if (s.equals("int")||s.equals("long")||s.equals("short")||s.equals("byte")||s.equals("char")){
                                                                                        fields_stub.append("0");
                                                                                        fields_refine.append("0");
                                                                                }
                                                                                else if (s.equals("float")||s.equals("double")){
                                                                                        fields_stub.append("0.0");
                                                                                        fields_refine.append("0.0");
                                                                                }
                                                                                else{
                                                                                        fields_stub.append("null");
                                                                                        fields_refine.append("null");
                                                                                }
                                                                        }
                                                                        fields_stub.append(";\n");
                                                                        fields_refine.append(";\n");
                                                                }
                                                                else if (o.getType().equals(MMGlobals.Constructor)){
                                                                        constructors_stub.append("   ");
                                                                        constructors_refine.append("   ");
                                                                        if (modifiers_stub.indexOf("public")==-1 && modifiers_stub.indexOf("private")==-1){
                                                                        	constructors_stub.append("public ").append(modifiers_stub);
																		}
																		else{
																			constructors_stub.append(modifiers_stub);
																		}
																		constructors_refine.append(modifiers_refine);
                                                                        boolean hasArg = addArgs(constructors_stub, constructors_refine,str);
                                                                        if (!hasArg)
                                                                                hasNoArgCons = true;
                                                                        addThrows(o,constructors_stub, constructors_refine);
                                                                        if (isInterface){
                                                                                constructors_stub.append(";\n");
                                                                                constructors_refine.append(";\n");
                                                                        }
                                                                        else{
                                                                                constructors_stub.append("{}\n");
                                                                                constructors_refine.append("{}\n");
                                                                        }
                                                                }
                                                                else if (o.getType().equals(MMGlobals.Method)){
                                                                        methods_stub.append("   ");
                                                                        methods_refine.append("   ");
                                                                        methods_stub.append(modifiers_stub);
                                                                        methods_refine.append(modifiers_refine);
                                                                        methods_stub.append(str.substring(str.lastIndexOf(" ")+1));
                                                                        methods_refine.append(str.substring(str.lastIndexOf(" ")+1));
                                                                        methods_stub.append(" ");
                                                                        methods_refine.append(" ");
                                                                        addArgs(methods_stub, methods_refine, str.substring(0,str.lastIndexOf(" ")));
                                                                        addThrows(o, methods_stub, methods_refine);
                                                                        String s =  str.substring(str.lastIndexOf(" ")+1);
                                                                        if (isInterface || modifiers_stub.indexOf("abstract")!=-1){
                                                                                methods_stub.append(";");
                                                                                methods_refine.append(";");
                                                                        }
                                                                        else if (s.equals("void")){
                                                                                methods_stub.append("{}");
                                                                                methods_refine.append("{}");
                                                                        }
                                                                        else if (s.equals("boolean")){
                                                                                methods_stub.append("{ return false; }");
                                                                                methods_refine.append("{ return false; }");
                                                                        }
                                                                        else if (s.equals("int")||s.equals("long")||s.equals("short")||s.equals("byte")||s.equals("char")){
                                                                                methods_stub.append("{ return 0; }");
                                                                                methods_refine.append("{ return 0; }");
                                                                        }
                                                                        else if (s.equals("float")||s.equals("double")){
                                                                                methods_stub.append("{ return 0.0; }");
                                                                                methods_refine.append("{ return 0.0; }");
                                                                        }
                                                                        else{
                                                                                methods_stub.append("{ return null; }");
                                                                                methods_refine.append("{ return null; }");
                                                                        }
                                                                        methods_stub.append("\n");
                                                                        methods_refine.append("\n");

                                                                }
                                                                else if (o.getType().equals(MMGlobals.Class)){
                                                                        //System.out.println("Nested Class!!!");
                                                                        nestedProcess(o, nested,2);
                                                                }
                                                                else if (o.getType().equals(MMGlobals.Interface)){
                                                                        //System.out.println("Nested Interface!!!");
                                                                        nestedProcess(o, nested,2);
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }catch (Exception e2){
                                System.out.println(e2);
                        }
                }
                bodyHeader3.append("{\n");
                if (!hasNoArgCons && !isInterface){
                        constructors_stub.append("   public ");
                        constructors_refine.append("   public ");
                        constructors_stub.append(key.substring(0, key.lastIndexOf(".")));
                        constructors_refine.append(key.substring(0, key.lastIndexOf(".")));
                        constructors_stub.append("(){}\n");
                        constructors_refine.append("(){}\n");
                }

                try{
                        //write out to the file
                        File stub_file = new File(stub, key);
                        File refine_file = new File(refine, key);
                        BufferedWriter bw1 = new BufferedWriter(new FileWriter(stub_file));
                        BufferedWriter bw2 = new BufferedWriter(new FileWriter(refine_file));
                        bw1.write(header.toString());
                        bw2.write(header.toString());
                        bw1.newLine();
                        bw2.newLine();
                        bw1.write(bodyHeader1_stub.toString());
                        bw2.write(bodyHeader1_refine.toString());
                        bw1.write(bodyHeader2.toString());
                        //build the super class$$
                        String name = key.substring(0, key.lastIndexOf("."));
                        BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File(refine, name+"$$.java")));
                        bw3.write(header.toString());
                        bw3.newLine();
                        if (isInterface)
                                bw3.write("interface ");
                        else
                                bw3.write("abstract class ");
                        bw3.write(name);
                        bw3.write("$$");
                        if (bodyHeader2.indexOf("extends")==-1){
                                bodyHeader2.append(" extends ").append(name).append("$$");
                        }
                        else{
                                bw3.write(" ");
                                bw3.write(bodyHeader2.toString());
                                bodyHeader2.delete(0,bodyHeader2.length());
                                bodyHeader2.append(" extends ").append(name).append("$$");
                        }
                        bw3.write("{}");
                        bw3.flush();
                        bw3.close();
                        //end super class$$
                        bw2.write(bodyHeader2.toString());
                        bw1.write(bodyHeader3.toString());
                        bw2.write(bodyHeader3.toString());
                        bw1.newLine();
                        bw2.newLine();
                        bw1.write(fields_stub.toString());
                        bw2.write(fields_refine.toString());
                        bw1.newLine();
                        bw2.newLine();
                        bw1.write(constructors_stub.toString());
                        bw2.write(constructors_refine.toString());
                        bw1.newLine();
                        bw2.newLine();
                        bw1.write(methods_stub.toString());
                        bw2.write(methods_refine.toString());
                        bw1.newLine();
                        bw2.newLine();
                        bw1.write(nested.toString());
                        bw2.write(nested.toString());
                        bw1.write("}");
                        bw2.write("}");
                        bw1.flush();
                        bw1.close();
                        bw2.flush();
                        bw2.close();
                }catch(Exception e3){
                        System.out.println(e3);
                }
	}

        public static void nestedProcess(MMOutput output, StringBuffer nested, int depth){

                //Hashset to search the repeated definitions

                HashSet definitions = new HashSet();
                //HashSet imports = new HashSet();
                HashSet extendClasses = new HashSet();
                HashSet implInterfaces = new HashSet();

                //StringBuffer header = new StringBuffer();
                StringBuffer bodyHeader1 = new StringBuffer();
                StringBuffer bodyHeader2 = new StringBuffer();
                StringBuffer bodyHeader3 = new StringBuffer();
                StringBuffer fields = new StringBuffer();
                StringBuffer constructors = new StringBuffer();
                StringBuffer methods = new StringBuffer();

                boolean hasNoArgCons = false;
                boolean isInterface=true;

                //get the body
                bodyHeader1.append(getModifiers(output.getModifiers()));
                if (output.getType().equals(MMGlobals.Class)){
                        bodyHeader1.append("class ");
                        isInterface = false;
                }
                else{
                        bodyHeader1.append("interface ");
                        isInterface = true;
                }
                bodyHeader1.append(output.getName());

                //output the list of the super classes
                Iterator it = output.values().iterator();
                while (it.hasNext()) {
                        NamedVector nv = (NamedVector) it.next();
                        if (!nv.getName().equals("super")){
                                if (nv.getName().equals(MMGlobals.Classes)){
                                        Iterator it1 = nv.iterator();
                                        if (it1.hasNext()){
                                                String str = (String) it1.next();
                                                if (!str.startsWith("stub.") && !extendClasses.contains(str)){
                                                        if (bodyHeader2.length()==0){
                                                                bodyHeader2.append(" extends ");
                                                                bodyHeader2.append(str);
                                                        }
                                                        else{
                                                                bodyHeader2.append(", ").append(str);
                                                        }
                                                        extendClasses.add(str);

                                                }
                                                while (it1.hasNext()){
                                                        str = (String) it1.next();
                                                        if (!str.startsWith("stub.") && !extendClasses.contains(str)){
                                                                bodyHeader2.append(", ").append(str);
                                                                extendClasses.add(str);
                                                        }
                                                }
                                        }
                                }
                                else{
                                        Iterator it1 = nv.iterator();
                                        if (it1.hasNext()){
                                                String str = (String) it1.next();
                                                if (!str.startsWith("stub.") && !implInterfaces.contains(str)){
                                                        if (bodyHeader3.length()==0){
                                                                if (isInterface)
                                                                        bodyHeader3.append(" extends ");
                                                                else
                                                                        bodyHeader3.append(" implements ");
                                                                bodyHeader3.append(str);
                                                        }
                                                        else{
                                                                bodyHeader3.append(", ").append(str);
                                                        }
                                                        implInterfaces.add(str);

                                                }
                                                while (it1.hasNext()){
                                                        str = (String) it1.next();
                                                        if (!str.startsWith("stub.") && !implInterfaces.contains(str)){
                                                                bodyHeader3.append(", ").append(str);
                                                                implInterfaces.add(str);
                                                        }
                                                }
                                        }

                                }
                        }
                }
                //body.append(" {\n");//finished the headline of the class

                //get the list of the methods, constructors and fields
                if (output!=null){
                        MMHashMap theNested=output.getNested();
                        if (theNested != null) {
                                it = theNested.values().iterator();
                                while (it.hasNext()) {
                                        MMOutput o = (MMOutput) it.next();
                                        String modifiers = getModifiers(o.getModifiers());
                                        String str = o.getName();
                                        if (!definitions.contains(str)){
                                                definitions.add(str);
                                                if (o.getType().equals(MMGlobals.Field)){
                                                        for (int d=0; d<depth; d++){
                                                                fields.append("   ");
                                                        }
                                                        fields.append(modifiers);
                                                        fields.append(str.substring(str.lastIndexOf(" ")+1));
                                                        fields.append(" ");
                                                        fields.append(str.substring(0,str.lastIndexOf(" ")));
                                                        if (o.containsKey("finalValue")){
                                                                NamedVector nv =  (NamedVector)o.get("finalValue");
                                                                Iterator it0 = nv.iterator();
                                                                while (it0.hasNext()){
                                                                        fields.append((String)it0.next());
                                                                }
                                                        }
                                                        else{
                                                                fields.append(" = ");
                                                                String s=str.substring(str.lastIndexOf(" ")+1);
                                                                if (s.equals("boolean"))
                                                                                fields.append("false");
                                                                else if (s.equals("int")||s.equals("long")||s.equals("short")||s.equals("byte")||s.equals("char"))
                                                                                fields.append("0");
                                                                else if (s.equals("float")||s.equals("double"))
                                                                                fields.append("0.0");
                                                                else
                                                                                fields.append("null");
                                                        }
                                                        fields.append(";\n");
                                                }
                                                else if (o.getType().equals(MMGlobals.Constructor)){
                                                        for (int d=0; d<depth; d++){
                                                                constructors.append("   ");
                                                        }
                                                        constructors.append(modifiers);
                                                        boolean hasArg = addArgs(constructors,str);
                                                        if (!hasArg)
                                                                hasNoArgCons = true;
                                                        addThrows(o,constructors);
                                                        if (isInterface)
                                                                constructors.append(";\n");
                                                        else
                                                                constructors.append("{}\n");
                                                }
                                                else if (o.getType().equals(MMGlobals.Method)){
                                                        for (int d=0; d<depth; d++){
                                                                methods.append("   ");
                                                        }
                                                        methods.append(modifiers);
                                                        methods.append(str.substring(str.lastIndexOf(" ")+1));
                                                        methods.append(" ");
                                                        addArgs(methods, str.substring(0,str.lastIndexOf(" ")));
                                                        addThrows(o, methods);
                                                        String s =  str.substring(str.lastIndexOf(" ")+1);
                                                        if (isInterface || modifiers.indexOf("abstract")!=-1)
                                                                methods.append(";");
                                                        else if (s.equals("void"))
                                                                methods.append("{}");
                                                        else if (s.equals("boolean"))
                                                                methods.append("{ return false; }");
                                                        else if (s.equals("int")||s.equals("long")||s.equals("short")||s.equals("byte")||s.equals("char"))
                                                                methods.append("{ return 0; }");
                                                        else if (s.equals("float")||s.equals("double"))
                                                                methods.append("{ return 0.0; }");
                                                        else
                                                                methods.append("{ return null; }");
                                                        methods.append("\n");

                                                }
                                                else if (o.getType().equals(MMGlobals.Class)){
                                                        //System.out.println("Nested Class!!!");
                                                        nestedProcess(o, nested, depth++);
                                                }
                                                else if (o.getType().equals(MMGlobals.Interface)){
                                                        //System.out.println("Nested Interface!!!");
                                                        nestedProcess(o, nested, depth++);
                                                }
                                        }
                                }
                        }
                }

                bodyHeader3.append("{\n");
                /*if (!hasNoArgCons && !isInterface){
                        constructors.append("   public ");
                        constructors.append(key.substring(0, key.lastIndexOf(".")));
                        constructors.append("(){}\n");
                }*/

                try{
                        //write out to the string buffer nested
                        for (int d=0; d<depth-1; d++){
                                nested.append("   ");
                        }
                        nested.append(bodyHeader1.toString());
                        nested.append(bodyHeader2.toString());
                        nested.append(bodyHeader3.toString());
                        nested.append("\n");
                        nested.append(fields.toString());
                        nested.append("\n");
                        nested.append(constructors.toString());
                        nested.append("\n");
                        nested.append(methods.toString());
                        nested.append("\n");
                        for (int d=0; d<depth-1; d++){
                                nested.append("   ");
                        }
                        nested.append("}\n");
                }catch(Exception e3){
                        System.out.println(e3);
                }
	}


  	//Translate the int modifiers to string representation
        public static void getClassModifiers(StringBuffer sb, int modifiers, boolean isStub){

                if ((modifiers & MMGlobals.ModAbstract)!=0){
                		if (sb.indexOf("abstract")==-1)
                	        	sb.append("abstract ");
				}
                if ((modifiers & MMGlobals.ModFinal)!=0){
					if (sb.indexOf("final")==-1)
                        sb.append("final ");
				}
                if (isStub){
                        if ((modifiers & MMGlobals.ModPublic)!=0){
                        	if (sb.indexOf("public")==-1)
                                sb.append("public ");
						}
                        if ((modifiers & MMGlobals.ModProtected)!=0){
							if (sb.indexOf("protected")==-1)
                                sb.append("protected ");
						}
                        if ((modifiers & MMGlobals.ModPrivate)!=0){
							if (sb.indexOf("private")==-1)
                                sb.append("private ");
						}
                        //if (sb.indexOf("public")==-1 && sb.indexOf("private")==-1 && sb.indexOf("protected")==-1)
                                //sb.append("public ");
                }
                else{
						if (sb.indexOf("public")==-1)
                    	    sb.append("public ");
                }
                if ((modifiers & MMGlobals.ModStatic)!=0){
					if (sb.indexOf("static")==-1)
                        sb.append("static ");
				}
                if ((modifiers & MMGlobals.ModTransient)!=0){
					if (sb.indexOf("transient")==-1)
                        sb.append("transient ");
				}
                if ((modifiers & MMGlobals.ModVolatile)!=0){
					if (sb.indexOf("volatile")==-1)
                        sb.append("volatile ");
				}
                if ((modifiers & MMGlobals.ModNative)!=0){
					if (sb.indexOf("native")==-1)
                        sb.append("native ");
				}
                if ((modifiers & MMGlobals.ModSynchronized)!=0){
					if (sb.indexOf("synchronized")==-1)
                        sb.append("synchronized ");
				}

        }

        //Translate the int modifiers to string representation
        public static String getModifiers(int modifiers, boolean isStub){
                StringBuffer sb = new StringBuffer();
                if ((modifiers & MMGlobals.ModAbstract)!=0)
                	    sb.append("abstract ");
                if ((modifiers & MMGlobals.ModFinal)!=0)
                        sb.append("final ");
                if (isStub){
                        if ((modifiers & MMGlobals.ModPublic)!=0)
                                sb.append("public ");
                        if ((modifiers & MMGlobals.ModProtected)!=0)
                                sb.append("protected ");
                        if ((modifiers & MMGlobals.ModPrivate)!=0)
                                sb.append("private ");
                        //if (sb.indexOf("public")==-1 && sb.indexOf("private")==-1 && sb.indexOf("protected")==-1)
                                //sb.append("public ");
                }
                else{
                    	sb.append("public ");
                }
                if ((modifiers & MMGlobals.ModStatic)!=0)
                        sb.append("static ");
                if ((modifiers & MMGlobals.ModTransient)!=0)
                        sb.append("transient ");
                if ((modifiers & MMGlobals.ModVolatile)!=0)
                        sb.append("volatile ");
                if ((modifiers & MMGlobals.ModNative)!=0)
                        sb.append("native ");
                if ((modifiers & MMGlobals.ModSynchronized)!=0)
                        sb.append("synchronized ");
                return sb.toString();
        }

        //Translate the int modifiers to string representation
        public static String getModifiers(int modifiers){
                StringBuffer sb = new StringBuffer();
                if ((modifiers & MMGlobals.ModAbstract)!=0)
                        sb.append("abstract ");
                if ((modifiers & MMGlobals.ModFinal)!=0)
                        sb.append("final ");
                if ((modifiers & MMGlobals.ModPublic)!=0)
                        sb.append("public ");
                if ((modifiers & MMGlobals.ModProtected)!=0)
                        sb.append("protected ");
                if ((modifiers & MMGlobals.ModPrivate)!=0)
                        sb.append("private ");
                if (sb.indexOf("public")==-1 && sb.indexOf("private")==-1 && sb.indexOf("protected")==-1)
                        sb.append("public ");
                if ((modifiers & MMGlobals.ModStatic)!=0)
                        sb.append("static ");
                if ((modifiers & MMGlobals.ModTransient)!=0)
                        sb.append("transient ");
                if ((modifiers & MMGlobals.ModVolatile)!=0)
                        sb.append("volatile ");
                if ((modifiers & MMGlobals.ModNative)!=0)
                        sb.append("native ");
                if ((modifiers & MMGlobals.ModSynchronized)!=0)
                        sb.append("synchronized ");
                return sb.toString();
        }

        //add in the formal argments
        public static boolean addArgs(StringBuffer sb, String str){
                boolean hasArg = true;
                try{
                        int i1=str.lastIndexOf("(");
                        int i2=str.lastIndexOf(")");
                        if (i1+1==i2){
                                sb.append(str);
                                hasArg = false;
                        }
                        else{
                                hasArg = true;
                                sb.append(str.substring(0,i1+1));
                                StringTokenizer st = new StringTokenizer(
                                        str.substring(i1+1,i2), ",");
                                int idx=0;
                                while (st.hasMoreTokens()) {
                                        if (idx!=0)
                                                sb.append(", ");
                                        sb.append(st.nextToken());
                                        sb.append(" x"+idx);
                                        idx++;
                                }
                                sb.append(")");
                        }
                }catch (Exception e){
                        System.out.println(e);
                }
                return hasArg;
        }

        //add in the formal argments
        public static boolean addArgs(StringBuffer sb_stub, StringBuffer sb_refine, String str){
                boolean hasArg = true;
                try{
                        int i1=str.lastIndexOf("(");
                        int i2=str.lastIndexOf(")");
                        if (i1+1==i2){
                                sb_stub.append(str);
                                sb_refine.append(str);
                                hasArg = false;
                        }
                        else{
                                hasArg = true;
                                sb_stub.append(str.substring(0,i1+1));
                                sb_refine.append(str.substring(0,i1+1));
                                StringTokenizer st = new StringTokenizer(
                                        str.substring(i1+1,i2), ",");
                                int idx=0;
                                while (st.hasMoreTokens()) {
                                        if (idx!=0){
                                                sb_stub.append(", ");
                                                sb_refine.append(", ");
                                        }
                                        String tmp = st.nextToken();
                                        sb_stub.append(tmp);
                                        sb_refine.append(tmp);
                                        sb_stub.append(" x"+idx);
                                        sb_refine.append(" x"+idx);
                                        idx++;
                                }
                                sb_stub.append(")");
                                sb_refine.append(")");
                        }
                }catch (Exception e){
                        System.out.println(e);
                }
                return hasArg;
        }

        //get the throws statement for the constructors or methods
        public static void addThrows(MMOutput output, StringBuffer sb){
                Iterator it = output.values().iterator();
                        while (it.hasNext()) {
                        NamedVector nv = (NamedVector) it.next();
                        if (!nv.getName().equals("super")){
                                if (nv.getName().equals(MMGlobals.Throws)){
                                        sb.append(" throws ");
                                        Iterator it1 = nv.iterator();
                                        while (it1.hasNext()) {
                                                String str = (String) it1.next();
                                                if (it1.hasNext())
                                                	sb.append(str+", ");
                                                else
                                                	sb.append(str+" ");
                                        }
                                }
                        }
                }
        }

        //get the throws statement for the constructors or methods
        public static void addThrows(MMOutput output, StringBuffer sb_stub, StringBuffer sb_refine){
                Iterator it = output.values().iterator();
                while (it.hasNext()) {
                        NamedVector nv = (NamedVector) it.next();
                        if (!nv.getName().equals("super")){
                                if (nv.getName().equals(MMGlobals.Throws)){
                                        sb_stub.append(" throws ");
                                        sb_refine.append(" throws ");
                                        Iterator it1 = nv.iterator();
                                        while (it1.hasNext()) {
                                                String str = (String) it1.next();
                                                if (it1.hasNext()){
                                                	sb_stub.append(str+", ");
                                                	sb_refine.append(str+", ");
												}
												else{
													sb_stub.append(str+" ");
                                                	sb_refine.append(str+" ");
												}
                                        }
                                }
                        }
                }
        }


}//end


