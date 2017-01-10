/*
 * Created on Aug 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ProgramCube.DataStore;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Sahil
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLUtils {

	
	public static String getXMLString(Document doc){

	     XMLOutputter prettyOutput = new XMLOutputter(Format.getPrettyFormat());
	 	
	     StringWriter stw = new StringWriter();
	     try {
	         prettyOutput.output(doc, stw);
	         stw.close();
	     } catch(Exception e) {return "Exception in writing xml to string";}
	     
	     return stw.toString();
	}

	public static String getXMLString(Element elm){
	     XMLOutputter prettyOutput = new XMLOutputter(Format.getPrettyFormat());
	 	
	     StringWriter stw = new StringWriter();
	     try {
	     	
	         prettyOutput.output(elm, stw);
	         
	         stw.close();
	     } catch(Exception e) {return "Exception in writing xml to string";}
	     
	     return stw.toString();
	}
	
	public static String formatXMLStr(String xml){
		
		try {
		      SAXBuilder builder = new SAXBuilder();
		      org.jdom.Document result = builder.build(new StringReader(xml));
		      return getXMLString(result);
		}catch(IOException e) {
			e.printStackTrace();
			
		} catch(JDOMException e) {
		    e.printStackTrace();
		} catch(NullPointerException e) {
		    e.printStackTrace();
		} 
	
		    return null;
	}
	
	public static Document getDocFromString(String xml){
		
		try {
		      SAXBuilder builder = new SAXBuilder();
		      org.jdom.Document result = builder.build(new StringReader(xml));
		      return (result);
		}catch(IOException e) {
			e.printStackTrace();
			
		} catch(JDOMException e) {
		    e.printStackTrace();
		} catch(NullPointerException e) {
		    e.printStackTrace();
		} 
	
		    return null;
	}
}
