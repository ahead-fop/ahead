/* ******************************************************************
   class      : Pair
*********************************************************************/

package ModelExplorer.Browser;

import java.io.*;
import java.util.*;

//To create a pair of column# and value, which is used by the select method.
class Pair{
	private int column;
	private String value;
	
	public Pair(int column, String value){
		this.column=column;
		this.value=value;
	}
	
	public int getColumn(){
		return column;
	}
	
	public String getValue(){
		return value;
	}
	
	public String toString(){
		return column+"\t"+value;
	}
}

