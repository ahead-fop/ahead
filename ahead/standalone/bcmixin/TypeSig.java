
package bcmixin;
import org.apache.bcel.generic.*;
import java.util.*;

public class TypeSig {

	private Type[] t;
	private String sig;

	TypeSig(Type[] t, String sig){
		this.t = t;
		this.sig = sig;
	}

	public String getSig(){
		return sig;
	}

	public Type[] getTypes(){
		return t;
	}
}
