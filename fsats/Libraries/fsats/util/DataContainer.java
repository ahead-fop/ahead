//                              -*- Mode: Java -*-
// Version         : 1.15
// Author          :
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Fri Feb 11 15:37:00 2000

package fsats.util;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Enumeration;



/**
 * Contains arbitrarily structured data.
 * A container has a label and a value which is either a string or
 * a list of containers, all with different labels.  No ordering of
 * the fields of a container is guaranteed.  The text version
 * of a container will always have the fields in the same order
 * (alphabetical at the moment), in order to support meaningful
 * string comparisons.
 */
public class DataContainer implements Cloneable
{
    static DataContainer nullDC = new NullDataContainer();

    private java.io.PrintWriter stdOutWriter;
    private String label;
    private String value;
    private java.util.Vector fields;
    private DataContainer parent;

    /**
     * Construct an empty container with the specified label.
     */
    public DataContainer(String label)
    {
        this.label=label==null ? "" : label;
    }

    /**
     * Construct a container with the specified label, value.
     */
    public DataContainer(String label, String value)
    {
        this( label );
        setValue( value );
    }


    /**
     * Test for a null container.
     */
    public boolean isNull()
    {
        return false;
    }

    /**
     * Return a null container
     */
    public static DataContainer nullDataContainer()
    {
        return nullDC;
    }

    /**
     * Return the label of this container.
     */
    public String getLabel()
    {
        return label;
    }

    /** 
     * Set the label of this container.
     * If a container exists with the same parent and label that
     * container is removed from its parent.
     */
    public void setLabel(String label)
    {
        DataContainer p=getParent();
        extract();
        this.label=label;
        p.addField(this);
    }

    /**
     * Return the parent container if any.
     */
    public DataContainer getParent()
    {
        return parent==null ? nullDC : parent;
    }

    /**
     * Return the value of this container or null if there is none.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Return the value of this container if it is non-null, otherwise
     * return the specified default.
     */
    public String getValue( String defaultValue )
    {
        return value==null ? defaultValue : value;
    }
    
    /**
     * Set the value of this container.
     * Nothing happens for null containers.
     * If the container had fields they are removed.
     */
    public void setValue(String value)
    {
        while (getFieldCount()>0)
            getField(1).extract();
        fields=null;
        this.value=value;
    }

    /**
     * Return the number of fields in this container.
     */
    public int getFieldCount()
    {
        return fields==null ? 0 : fields.size();
    }

    /**
     * Return the index'th field of this container, sorted by label.
     * The index of the first field is 1.
     * Return a null container if the requested one isn't there.
     */
    public DataContainer getField(int index)
    {
        return 
            fields==null || index<=0 || index>fields.size()
                ? nullDC 
                : (DataContainer)fields.elementAt(index-1);
    }


    /**
     * Return the index of the field with the given label.  If there is
     * no such field return the location where one should be inserted.
     */
    private int locateField(String label)
    {
        int a=1;
        int b=getFieldCount()+1;
        while (a<b)
        {
            int c = (a+b)/2;
            int diff = label.compareTo(getField(c).getLabel());
            if (diff<0)
                b=c;
            else if (diff>0)
                a=c+1;
            else
                a=b=c;
        }
        return a;
    }

    /**
     * Insert item at the given index.
     */
    private void insertField(int index, DataContainer item)
    {
        if (fields==null)
        {
            fields=new java.util.Vector();
            value=null;
        }
        fields.insertElementAt(item, index-1);
        item.parent = this;
    }

    /**
     * Add a new subcontainer to this container.
     * If item was in a container already it is removed.
     * If another child container has the same label it is removed.
     * If item is null nothing happens.
     */
    public void addField(DataContainer item)
    {
        if (!item.isNull())
        {
           String itemLabel = item.getLabel();
           item.extract();
           int i=locateField(itemLabel);
           DataContainer f=getField(i);
           if (f.getLabel().equals(itemLabel))
               f.extract();
           insertField(i, item);
        }
    }

    /**
     * Removes the field at the given index.
     */
    public void removeField(int index)
    {
	fields.removeElementAt(index - 1);
    }

    /**
     * Return the container within this container with the specified label.
     * If no such subcontainer exists, return a null container.
     */
    public DataContainer getField(String label)
    {
        DataContainer f=nullDC;
        if (label!=null)
        {
            f=getField(locateField(label));
            if (!f.getLabel().equals(label))
                f=nullDC;
        }
        return f;
    }

    /**
     * Add a container with the specified label to this container.
     * If such a subcontainer already exists, just return that one.
     */
    public DataContainer addField(String label)
    {
        DataContainer f=nullDC;
        if (label!=null)
        {
            int i=locateField(label);
            f=getField(i);
            if (!f.getLabel().equals(label))
            {
                f=new DataContainer(label);
                insertField(i, f);
            }
        }
        return f;
    }

    /**
     * Extract this container from whatever container it is within.
     */
    public void extract()
    {
        if (parent!=null)
        {
            int i=parent.locateField(label);
            parent.fields.removeElementAt(i-1);
            parent=null;
        }
    }

    /**
     * Removes all empty fields and fields that contain
     * only empty fields from this data container.
     */
    public DataContainer removeEmptyFields()
    {
	for (int i = 1; i < getFieldCount()+1; ++i)
	{
	    if (getField(i).getFieldCount() > 0)
	    {
		getField(i).removeEmptyFields();
		if (getField(i).getFieldCount() == 0)
		{
		    removeField(i);
		    i = 0;
		}
	    }
	    else if (getField(i).getFieldCount() == 0 &&
		     getField(i).getValue() == null)
	    {
		removeField(i);
		i = 0;
	    }
	}

	return this;
    }

    private static boolean mustQuote(char c)
    {
        return
            !( ('A'<=c && c<='Z')
                || ('a'<=c && c<='z')
                || ('0'<=c && c<='9')
                || c=='/'
                || c=='~'
                || c=='_'
                || c=='+'
                || c=='-'
                || c=='.');
    }

    private static boolean mustQuote(String s)
    {
        boolean yes=s.length()==0;
        for (int i=0; i<s.length() && !yes; ++i)
            yes = mustQuote(s.charAt(i));
        return yes;
    }

    private String toHex(char c)
    {
        String h= "0"+Integer.toHexString((int)c);
        return h.substring(h.length()-2);
    }

    private void writeQuotedValue(java.io.Writer out, String value)
    {
        try
        {
            if (value==null)
            {
            }
            else if (!mustQuote(value))
                out.write(value);
            else
            {
                out.write("\"");
                for (int i=0; i<value.length(); ++i)
                {
                    char c=value.charAt(i);
                    if (c=='"' || c=='\\')
                        out.write("\\"+c);
                    else if (Character.isISOControl(c))
                        out.write("\\"+toHex(c));
                    else
                        out.write(c);
                }
                out.write("\"");
            }
        }
        catch (java.io.IOException e)
        {
	    e.printStackTrace();
        }
    }

    /**
     * Write the string for this container to out.
     */
    public void write(java.io.Writer out)
    {
        try
        {
            writeQuotedValue(out, label);
            out.write("=");
            if (value==null)
            {
                out.write("(");
                for (int i=1; i<=getFieldCount(); ++i)
                {
                    if (i>1)
                        out.write(" ");
                    getField(i).write(out);
                }
                out.write(")");
            }
            else
                writeQuotedValue(out, value);
        }
        catch (java.io.IOException e)
        {
	    e.printStackTrace();
        }
    }

    /**
     * Return the string representation of this container.
     */
    public String toString()
    {
        java.io.StringWriter out = new java.io.StringWriter();
        write(out);
        return out.toString();
    }

    private void indent(java.io.PrintWriter out, int level)
    {
        for (int i=0; i<level; ++i)
            out.print("  ");
    }

    private void print(java.io.PrintWriter out, int level)
    {
        indent(out, level);
        writeQuotedValue(out, label);
        out.print("=");
        if (value==null)
        {
            out.println();
            indent(out, level);
            out.println("(");
            for (int i=1; i<=getFieldCount(); ++i)
                getField(i).print(out, level+1);
            indent(out, level);
            out.print(")");
        }
        else
            writeQuotedValue(out, value);
        out.println();
    }

    /**
     * Print a multiline formatted text representation of this container to out.
     */
    public void print(java.io.PrintWriter out)
    {
        print(out, 0);
    }

    /**
     * Convenience method targeted toward debugging.  Calls print(PrintWriter)
     * with a private printWriter tied to standard out.
     */
    public void print()
    {
        if ( stdOutWriter == null ) {
            stdOutWriter = 
                new java.io.PrintWriter(System.out, true);
            print( stdOutWriter );
            stdOutWriter.flush();
        }
    }

    private static void parseSpace(java.io.PushbackReader in)
        throws java.io.IOException
    {
        int i;

        do
        {
            i=in.read();
            // Treat comments as whitespace.
            if ((char)i=='#')
                while (i!=-1 && (char)i!='\n')
                    i=in.read();
        }
        while (i!=-1 && Character.isWhitespace((char)i));

        if (i!=-1)
            in.unread(i);
    }

    private static boolean parseChar(java.io.PushbackReader in, char c)
        throws java.io.IOException
    {
        boolean match = false;
        int i=in.read();
        match = i==(int)c;
        if (!match && i!=-1) in.unread(i);
        return match;
    }

    private static String parseWord(java.io.PushbackReader in)
        throws java.io.IOException
    {
        StringBuffer b = new StringBuffer();
        boolean read_a_char = false;
        int i=in.read();
        char c=' ';
        while (i!=-1 && !mustQuote(c=(char)i))
        {
            b.append(c);
            i=in.read();
            read_a_char = true;
        }
        if (i!=-1)
            in.unread(c);
        // don't use b.toString(), it wastes memory. see sun bug parade.
        String word = b.substring( 0, b.length() );
        return (!read_a_char ? null : word );
    }

    private static char parseRequiredChar(java.io.PushbackReader in)
        throws java.io.IOException
    {
        int i = in.read();
        if (i==-1)
            throw new DataContainerFormatException();
        return (char)i;
    }

    private static String parseQuotedValue(java.io.PushbackReader in)
        throws java.io.IOException
    {
        StringBuffer b = new StringBuffer();
        char c;
        while ( (c=parseRequiredChar(in))!='"' )
        {
            if (c!='\\')
                b.append(c);
            else if ( (c=parseRequiredChar(in))=='\\' || c=='"')
                b.append(c);
            else
                b.append(
                    (char)Integer.parseInt(
                        ""+c+parseRequiredChar(in), 16));
        }
        return b.substring( 0, b.length() );
    }

    private static String parseValue(java.io.PushbackReader in)
        throws java.io.IOException
    {
        return parseChar(in, '"') ? parseQuotedValue(in) : parseWord(in);
    }

    /**
     * Read a container from in.
     * Return a null container if end of input.
     * Throw DataContainerFormatException if the input is not well-formed.
     * Throw java.io.IOException if problem is encountered reading in.
     */
    public static DataContainer read(java.io.PushbackReader in)
        throws java.io.IOException
    {
        DataContainer result = nullDC;
        boolean ok = true;
        parseSpace(in);
        String label = parseValue(in);
        if (label!=null)
        {
            result = new DataContainer(label);
            parseSpace(in);
            if (!parseChar(in, '='))
                throw new DataContainerFormatException();
            parseSpace(in);
            if (parseChar(in, '('))
                for (parseSpace(in); !parseChar(in, ')'); parseSpace(in))
                {
                    DataContainer field = read(in);
                    if (field.isNull())
                        throw new DataContainerFormatException();
                    result.addField(field);
                }
            else
                result.setValue(parseValue(in));
        }
        return result;
    }


    /**
     * Read a container from s, beginning from s(0).
     * This is a convenience method, it is implemented by attaching a
     * PushbackReader to s and calling read(PushbackReader).
     * @see public static DataContainer read(java.io.PushbackReader in)
     */
    public static DataContainer read( String s ) throws java.io.IOException
    {
        java.io.StringReader sr = new java.io.StringReader( s );
        java.io.PushbackReader pr = new java.io.PushbackReader( sr );
        DataContainer container = DataContainer.read( pr );
        return container;
    }


    /**
     * Get the field of this container indicated by the compound label.
     * getMultilevelField("a.b.c") is the same as
     * getField("a").getField("b").getField("c").
     */
    public DataContainer getMultilevelField(String label)
    {
        DataContainer result=this;
        if (label!=null && !label.equals(""))
        {
            int i=label.indexOf('.');
            if (i==-1)
                result = getField(label);
            else
                result = 
                    getField(label.substring(0,i)).
                        getMultilevelField(label.substring(i+1));
        }
        return result;
    }

    /**
     * Add the field of this container indicated by the compound label.
     * addMultilevelField("a.b.c") is the same as
     * addField("a").addField("b").addField("c").
     */
    public DataContainer addMultilevelField(String label)
    {
        DataContainer result=this;
        if (label!=null && !label.equals(""))
        {
            int i=label.indexOf('.');
            if (i==-1)
                result = addField(label);
            else
                result = 
                    addField(label.substring(0,i)).
                        addMultilevelField(label.substring(i+1));
        }
        return result;
    }

    
    /**
     * Return a clone of this DataContainer.  The parent reference is not
     * cloned.  The parent of the clone is null.
     */
    public Object clone()
    {
        DataContainer theClone = new DataContainer( label, value );
        if ( fields != null )
            for ( Enumeration e = fields.elements(); e.hasMoreElements(); )
                {
                    DataContainer field = (DataContainer)e.nextElement();
                    theClone.addField( (DataContainer)field.clone() );
                }
        return theClone;        
    }
    

    
    /**
     * Test jig.
     */
    public static void main(String args[])
    {
        DataContainer c = new DataContainer("empty");
        
        java.io.PushbackReader r = 
            new java.io.PushbackReader(
                new java.io.InputStreamReader(System.in));
        java.io.PrintWriter p = 
            new java.io.PrintWriter(System.out);

        try
        {
            for (c=read(r); !c.isNull(); c=read(r))
                c.print(p);
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        }

        p.close();
    }
}


class NullDataContainer 
    extends DataContainer
{
    NullDataContainer()
    {
        super("");
    }

    public Object clone()
    {
        return this;
    }

    public boolean isNull()
    {
        return true;
    }

    public void setLabel(String label)
    {
    }

    public void setValue(String value)
    {
    }

    public void addField(DataContainer item)
    {
        if (item!=null)
        {
            item.extract();
        }
    }

    public DataContainer addField(String label)
    {
        return this;
    }

    public String toString()
    {
        return "";
    }
    
    public void write(java.io.Writer out)
    {
    }

    public void print(java.io.PrintWriter out)
    {
    }
}

