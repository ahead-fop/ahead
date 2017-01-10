//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:24:00 2000

package fsats.plan;

/**
 * Wrapper class for float to be used in fsats.plan classes.
 */
public class PlanFloat
{
    private float value;

    private static NullPlanFloat nullPlanFloat=new NullPlanFloat();

    /**
     * Constructs a PlanFloat.
     *
     * @param value - the float value wrapped by PlanFloat.
     */
    PlanFloat(float value)
    {
	this.value=value;
    }

    /**
     * Returns a null-valued PlanFloat.
     */
    public static PlanFloat getNull()
    {
	return nullPlanFloat;
    }

    /**
     * Returns whether or not the PlanFloat is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the value of the int wrapped by the PlanFloat.
     */
    public float floatValue()
    {
	return value;
    }

    /**
     * Parses the String argument as a PlanFloat.
     */
    public static PlanFloat parsePlanFloat(String s)
    {
	PlanFloat p=PlanFloat.getNull();

	try
	{
	    if(s!=null && s!="")
		p=new PlanFloat(Float.valueOf(s).floatValue());
	}
	catch(NumberFormatException e)
	{
	    // System.out.println(e);
	}

	return p;
    }

    /**
     * Returns a String representation of the PlanFloat.
     */
    public String toString()
    {
	return String.valueOf(value);
    }
}

/**
 * Represents a null-valued PlanFloat.
 */
class NullPlanFloat extends PlanFloat
{
    /**
     * Constructs a NullPlanFloat.
     */
    NullPlanFloat()
    {
	super(-1.0f);
    }

    /**
     * Returns whether or not the NullPlanFloat is null.
     */
    public boolean isNull()
    {
	return true;
    }

    /**
     * Returns a String representation of the NullPlanFloat.
     */
    public String toString()
    {
	return "";
    }
}
