
package fsats.util;

public class DataContainerFormat
{

    private DataContainer formats;


    public DataContainerFormat(DataContainer formats)
    {
        this.formats = 
            formats==null ? DataContainer.nullDataContainer() : formats;
    }


    private DataContainer noError()
    {
        return DataContainer.nullDataContainer();
    }


    private DataContainer setError(
        String message)
    {
        DataContainer error = new DataContainer("error");
        error.setValue(message);
        return error;
    }


    private DataContainer addError(
        DataContainer error, String label, DataContainer field)
    {
        if (!field.isNull())
        {
            field.setLabel(label);
            if (error.isNull())
                error = new DataContainer("error");
            error.addField(field);
        }
        return error;
    }


    private DataContainer verifyString(
        DataContainer subject,
        DataContainer format)
    {
        return 
            subject.getValue()!=null 
                ? noError() 
                : setError("Not atomic");
    }


    private DataContainer verifyInteger(
        DataContainer subject,
        DataContainer format)
    {
        DataContainer error = verifyString(subject, format);
        if (error.isNull())
        {
            try
            {
                int i = Integer.parseInt(subject.getValue().trim());
            }
            catch (NumberFormatException e)
            {
                error = setError("Not an integer");
            }
        }
        return error;
    }


    private DataContainer verifyNumber(
        DataContainer subject,
        DataContainer format)
    {
        return verifyString(subject, format);
    }


    private DataContainer verifyEnumerated(
        DataContainer subject,
        DataContainer format)
    {
        DataContainer error = verifyString(subject, format);
        if (error.isNull() && 
                format.getField("values").getField(subject.getValue()).isNull())
            error = setError("Not in enumeration");
        return error;
    }


    private DataContainer verifyAny(
        DataContainer error,
        DataContainer remains,
        DataContainer subject,
        DataContainer format)
    {
        for (int i=remains.getFieldCount(); i>0; --i)
            remains.getField(i).extract();
        return error;
    }


    private DataContainer verifyRecord(
        DataContainer error,
        DataContainer remains,
        DataContainer subject,
        DataContainer format)
    {
        if (subject.getValue()!=null)
            error = setError("Not a record");
        else
        {
            DataContainer fields=format.getField("fields");
            for (int i=fields.getFieldCount(); i>0; --i)
            {
                DataContainer field = fields.getField(i);
                String label = field.getLabel();
                remains.getField(label).extract();
                error = 
                    addError(
                        error, label,
                        verifyStart(subject.getField(label), field));
            }

            DataContainer variant=
                format.getField("cases").getField(
                    subject.getField(
                        format.getField("switch").getValue() ).getValue() );
            if (!variant.isNull())
                error=verifyMore(error, remains, subject, variant);

            DataContainer more = format.getField("more");
            if (!more.isNull())
                error=verifyMore(error, remains, subject, more);
        }
        return error;
    }


    private DataContainer verifyList(
        DataContainer error,
        DataContainer remains,
        DataContainer subject,
        DataContainer format)
    {
        if (subject.getValue()!=null)
            error = setError("Not a list");
        else
        {
            for (int i=subject.getFieldCount(); i>0; --i)
            {
                DataContainer field = subject.getField(i);
                String label = field.getLabel();
                remains.getField(label).extract();
                error = 
                    addError(
                        error, label,
                        verifyStart(field, format.getField("type")));
            }
        }
        return error;
    }


    private DataContainer verifyArray(
        DataContainer error,
        DataContainer remains,
        DataContainer subject,
        DataContainer format)
    {
        if (subject.getValue()!=null)
            error = setError("Not an array");
        else
        {
            boolean hasFirst=false;
            int first=0;
            try 
            { 
                first=Integer.parseInt(format.getField("first").getValue()); 
                hasFirst=true;
            }
            catch (NumberFormatException e) {}

            boolean hasLast=false;
            int last=0;
            try 
            { 
                last=Integer.parseInt(format.getField("last").getValue()); 
                hasLast=true;
            }
            catch (NumberFormatException e) {}

            for (int i=subject.getFieldCount(); i>0; --i)
            {
                DataContainer field = subject.getField(i);
                String label = field.getLabel();
                try
                {
                    int index=Integer.parseInt(label);
                    if ((!hasFirst || index>=first) && (!hasLast || index<=last))
                    {
                        remains.getField(label).extract();
                        error = 
                            addError(
                                error, label,
                                verifyStart(field, format.getField("type")));
                    }
                }
                catch (NumberFormatException e) {}
            }
        }
        return error;
    }


    private DataContainer verifySimpleOrComplex(
        DataContainer error,
        DataContainer remains,
        DataContainer subject,
        DataContainer format)
    {
        String label = subject.getValue()==null ? "complex" : "simple";
        format = format.getField(label);
        return
            format.isNull()
                ? setError("No "+label+" format")
                : verifyMore(error, remains, subject, format);
    }


    private DataContainer verifyOptional(
        DataContainer error,
        DataContainer remains,
        DataContainer subject,
        DataContainer format)
    {
        return
            subject.isNull()
                ? error
                : verifyMore(error, remains, subject, format.getField("type"));
    }


    private DataContainer verifyMore(
        DataContainer error,
        DataContainer remains,
        DataContainer subject,
        DataContainer format)
    {
        String label = format.getValue();
        String kind = format.getField("class").getValue();

        if (format.isNull())
            error = setError("Null format");
        else if (label!=null)
        {
            format = formats.getField(label);
            if (format.isNull())
                error = setError("No format labeled "+label);
            else
                error = verifyMore(error, remains, subject, format);
        }
        else if (kind.equals("OPTIONAL"))
            error = verifyOptional(error, remains, subject, format);
        else if (subject.isNull())
            error = setError("Missing");
        else if (kind.equals("ANY"))
            error = verifyAny(error, remains, subject, format);
        else if (kind.equals("LIST"))
            error = verifyList(error, remains, subject, format);
        else if (kind.equals("ARRAY"))
            error = verifyArray(error, remains, subject, format);
        else if (kind.equals("RECORD"))
            error = verifyRecord(error, remains, subject, format);
        else if (kind.equals("SIMPLE_OR_COMPLEX"))
            error = verifySimpleOrComplex(error, remains, subject, format);
        else if (kind.equals("STRING"))
            error = verifyString(subject, format);
        else if (kind.equals("ENUMERATED"))
            error = verifyEnumerated(subject, format);
        else if (kind.equals("NUMBER"))
            error = verifyNumber(subject, format);
        else if (kind.equals("INTEGER"))
            error = verifyInteger(subject, format);
        else
            error = setError("Unknown class "+kind);

        return error;
    }


    private DataContainer verifyStart(
        DataContainer subject, DataContainer format)
    {
        DataContainer remains=new DataContainer("remains");
        for (int i=subject.getFieldCount(); i>0; --i)
            remains.addField(subject.getField(i).getLabel()).setValue("Extra");

        DataContainer error = DataContainer.nullDataContainer();
        error = verifyMore(error, remains, subject, format);

        if (error.getValue()==null)
            for (int i=remains.getFieldCount(); i>0; --i)
                error.addField(remains.getField(i));

        return error;
    }


    public DataContainer verify(DataContainer subject, String label)
    {
        DataContainer format=formats.getField(label);
        return
            format.isNull() 
                ? setError("No format labeled "+label)
                : verifyStart(subject, format);
    }


    public static void main(String args[])
    {
        java.io.PrintWriter out = 
            new java.io.PrintWriter(System.out);
        java.io.PushbackReader in =
            new java.io.PushbackReader(
                new java.io.InputStreamReader(System.in));

        try
        {
            DataContainerFormat formats = 
                new DataContainerFormat(DataContainer.read(in));

            DataContainer subject = DataContainer.read(in);
            for (int i=1; !subject.isNull(); ++i)
            {
                DataContainer error = formats.verify(subject, args[0]);
                error.setLabel(String.valueOf(i));
                error.print(out);
                subject=DataContainer.read(in);
            }
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }

        out.close();
    }
}

