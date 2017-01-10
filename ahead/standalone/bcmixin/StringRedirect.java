
package bcmixin;
import org.apache.bcel.generic.*;
import org.apache.bcel.classfile.*;
import java.util.*;

public class StringRedirect {

	//hashmap to hold the entrys which need to be redirected
	private HashMap stringvalue;
	private HashMap methodrefname;
	private HashMap methodname;
	//hashmap to hold all the conresponding values in the constantpool or classgen
	private HashMap strings;
	private HashMap methodrefs;
	private HashMap methods;

	StringRedirect(ClassGen cgen){
		ConstantPoolGen cp = cgen.getConstantPool();
		Constant[] cs = cp.getConstantPool().getConstantPool();
		Method[] ms = cgen.getMethods();

		stringvalue = new HashMap();
		methodrefname = new HashMap();
		methodname = new HashMap();
		strings = new HashMap();
		methodrefs = new HashMap();

		ConstantPool cpool = cp.getConstantPool();
		int length = cp.getSize();
		for (int i=0; i<length; i++){
				if (cs[i] instanceof ConstantString){
						ConstantString cstring = (ConstantString)cs[i];
						strings.put(cstring.getBytes(cpool), cstring);
				}
				else if (cs[i] instanceof ConstantMethodref){
						ConstantMethodref cmethod = (ConstantMethodref)cs[i];
						String name = ((ConstantNameAndType)cs[cmethod.getNameAndTypeIndex()]).getName(cpool);
						methodrefs.put(name, cmethod);
				}
		}
		methods = new HashMap();
		for (int i=0; i<ms.length; i++){
				methods.put(ms[i].getName(),ms[i]);
		}
	}

	public void findConflicts(String org){
		if (strings.containsKey(org))
			stringvalue.put(strings.get(org),org);
		if (methodrefs.containsKey(org)){
			methodrefname.put(methodrefs.get(org), org);
			//System.out.println(methodrefs.get(org));
			//System.out.println(org);
		}
		if (methods.containsKey(org))
			methodname.put(methods.get(org), org);
	}


	public void redirect(ConstantPoolGen cp){
        //redirect the string value
		Iterator it = stringvalue.keySet().iterator();
		while (it.hasNext()){
            //System.out.println("string redirect!");
			Object key=it.next();
			ConstantString cstring = (ConstantString)key;
			String orgname = (String)stringvalue.get(key);
            //System.out.println("org string: "+orgname);
			int idx = cp.addUtf8("$#$"+orgname);
			ConstantUtf8 u8 = (ConstantUtf8)cp.getConstant(idx);
			//System.out.println(u8.getBytes());
			u8.setBytes( orgname);
            //System.out.println("the new index: "+idx);
            //System.out.println("the old index: "+cstring.getStringIndex());
			cstring.setStringIndex(idx);
		}

		//redirect the method name in the methodref entries
		it = methodrefname.keySet().iterator();
		while (it.hasNext()){
			//System.out.println("method name redirect in the methodref!");
			Object key = it.next();
			ConstantMethodref cmethod = (ConstantMethodref)key;
			//System.out.println(cmethod);
			String orgname = (String)methodrefname.get(key);
			//System.out.println("org method name: "+orgname);
			int idx = cp.addUtf8("#$#"+orgname);//add in a new one
			ConstantUtf8 u8 = (ConstantUtf8)cp.getConstant(idx);
			//System.out.println(u8.getBytes());
			u8.setBytes(orgname);
			//System.out.println("the new index: "+idx);
			ConstantNameAndType cnt = (ConstantNameAndType)cp.getConstantPool().getConstant(cmethod.getNameAndTypeIndex());
			//System.out.println(cnt);
			//System.out.println("the old index: "+cnt.getNameIndex());
			cnt.setNameIndex(idx);
			//System.out.println(cp.getConstant(idx));
		}

		//redirect the method name in the method definition
		it = methodname.keySet().iterator();
		while (it.hasNext()){
			//System.out.println("method name redirect in the method definition!");
			Object key = it.next();
			Method method = (Method)key;
			String orgname = (String)methodname.get(key);
			//System.out.println("org method name: "+orgname);
			int idx = cp.addUtf8("#$$"+orgname);
			ConstantUtf8 u8 = (ConstantUtf8)cp.getConstant(idx);
			//System.out.println(u8.getBytes());
			u8.setBytes(orgname);
			//System.out.println("the new index: "+idx);
			//System.out.println("the old index: "+method.getNameIndex());
			method.setNameIndex(idx);
			//System.out.println(cp.getConstant(idx));
		}
	}

}
