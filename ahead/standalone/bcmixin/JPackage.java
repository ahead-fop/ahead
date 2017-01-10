/** A class for changing the additional classes' package added into the
 *  composed directory.
 */
package bcmixin;

import java.util.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Repository;
import java.io.*;

public class JPackage{
        static HashSet ctable = new HashSet();
        static HashSet rtable = new HashSet();

        public static void main(String[] args){
                String fileName1 = null;
                String fileName2 = null;
                String pname = null;
                if (args.length==2){
                        fileName1 = args[0];
                        pname = args[1];
                }
                else{
                        System.out.println("Please specify the file and the package name.");
                        System.exit(1);
                }

                try{
                        BufferedReader br = new BufferedReader(new FileReader(new File(fileName1)));
                        String line = br.readLine();
                        while (line!=null){
                                ctable.add(line);
                                line = br.readLine();
                        }

                        /*br = new BufferedReader(new FileReader(new File(fileName2)));
                        line = br.readLine();
                        while (line!=null){
                                rtable.put(line, line);
                                line = br.readLine();
                        }*/

                        rtable.add("BaliParser");
                        rtable.add("BaliParser$JJCalls");
                        rtable.add("BaliParserConstants");
                        rtable.add("BaliParserTokenManager");
                        rtable.add("JavaCharStream");
                        rtable.add("ParseException");
                        rtable.add("Token");
                        rtable.add("TokenMgrError");
                }catch(IOException ioe){
                        System.out.println(ioe);
                        System.exit(1);
                }

                /**Iterator it0 = rtable.keySet().iterator();
                //System.out.println(rtable.size());
                while (it0.hasNext()){
                        String s = (String)it0.next();
                        if (s!=null){
                                System.out.println(s);
                        }
                }*/

                //rename the classes from CTable
                Iterator it = ctable.iterator();
                //get the original top pacakage name
                String topname=null;
                //iterator all the class files to rename
                while (it.hasNext()){
                        rename((String)it.next(),topname,pname);
                }
        }

  	public static void rename(String fileName, String topname, String pname){

		try{

			JavaClass jc = new ClassParser (fileName+".class") . parse () ;
			ClassGen cgen = new ClassGen(jc);
			ConstantPoolGen cp = cgen.getConstantPool();
			Constant[] cs = cp.getConstantPool().getConstantPool();
			StringRedirect sr = new StringRedirect(cgen);
			//System.out.println("pool length:" +cs.length);
			int length = cp.getSize();

			//get all the types from the constant pool
			/**1. rename*/
			for (int i=0; i<length; i++){
				if (cs[i] instanceof ConstantUtf8){
					ConstantUtf8 u8 = (ConstantUtf8)cs[i];
					String str = u8.getBytes();

					//Analyze the str to get the types & rename it by generating a new string
					//case 1: exactly class name
					if (rtable.contains(str)){
						sr.findConflicts(str);
						u8.setBytes(pname+"/"+str);
					}
					//case 2: start with class$
					else if (str.startsWith("class$") && str.length()>6){
						if (rtable.contains(str.substring(6))){
							str="class$"+pname+"$"+str.substring(7);
							u8.setBytes(str);
							//hs.add(str);
						}
					}
					//case 3: need to parse out the types in the string
					else if (str.indexOf(";")!=-1){
						StringTokenizer st = new StringTokenizer(str, ";");
						int num = st.countTokens();
						if (!str.endsWith(";"))
							num--;
						StringBuffer sb = new StringBuffer();
						String token;
						for (int j=0; j<num; j++){
							 token = st.nextToken();
							 int idx = token.indexOf("L");
							 if (idx==-1){
								 //no object type occured
								 sb.append(token).append(";");
							 }
							 else{
								 //get object name
								 //System.out.println(token);
								 String name = token.substring(idx+1);
								 if (rtable.contains(name)){
									 sb.append(token.substring(0,idx+1));
									 sb.append(pname).append("/").append(name).append(";");

								 }
								 else{
									 sb.append(token).append(";");
								 }
							 }
						 }
						 if (st.hasMoreTokens())
							sb.append(st.nextToken());

						 u8.setBytes(sb.toString());
					 }

				  }
			  }


			sr.redirect(cp);
			cgen.getJavaClass().dump(fileName+".class");

		}catch(java.io.IOException e) {
			System.err.println(e);
        }

	}
}
