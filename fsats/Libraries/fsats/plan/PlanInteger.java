//                              -*- Mode: Java -*-
// Version         : 1.2
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:25:00 2000

package fsats.plan;

/**
 * Wrapper class for int to be used in fsats.plan classes.
 */
public class PlanInteger
{
    private int value;

    private static NullPlanInteger nullPlanInteger=new NullPlanInteger();

    /**
     * Constructs a PlanInteger.
     *
     * @param value   - the int value wrapped by PlanInteger.
     */
    PlanInteger(int value)
    {
	this.value=value;
    }

    /**
     * Returns a null-valued PlanInteger.
     */
    public static PlanInteger getNull()
    {
	return nullPlanInteger;
    }

    /**
     * Returns whether or not the PlanInteger is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the value of the int wrapped by the PlanInteger.
     */
    public int intValue()
    {
	return value;
    }

    /**
     * Parses the String argument as a PlanInteger.
     */
    public static PlanInteger parsePlanInteger(String s)
    {
	PlanInteger p=PlanInteger.getNull();

	try
	{
	    if(s!=null && s!="")
		p=new PlanInteger(Integer.parseInt(s));
	}
	catch(NumberFormatException e)
	{
	    // System.out.println(e);
	}

	return p;
    }

    /**
     * Returns a String representation of the PlanInteger.
     */
    public String toString()
    {
	return String.valueOf(value);
    }
}

/**
 * Represents a null-valued PlanInteger.
 */
class NullPlanInteger extends PlanInteger
{
    /**
     * Constructs a NullPlanInteger.
     */
    NullPlanInteger()
    {
	super(-1);
    }

    /**
     * Returns whether or not the NullPlanInteger is null.
     */
    public boolean isNull()
    {
	return true;
    }

    /**
     * Returns a String representation of the NullPlanInteger.
     */
    public String toString()
    {
	return "";
    }
}
