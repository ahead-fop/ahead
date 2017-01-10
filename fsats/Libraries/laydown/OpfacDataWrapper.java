//                              -*- Mode: Java -*-
// Version         : 1.0
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Wed Jan 26 15:13:00 2000

package laydown;

import fsats.plan.*;

/**
 * Extends the functionality of the fsats.plan.OpfacData class
 * to encapsulate data about the process and site upon which the
 * OPFAC represented is running.
 * Uses the Delegation design pattern in relation to fsats.plan.OpfacData.
 * Plays the role of Delegator in the design pattern.
 * See Mark Grand.  "Patters in Java, Vol. 1."  New York: John Wiley & Sons,
 * 1998.  pp. 53-9.
 */
public class OpfacDataWrapper implements Cloneable, java.io.Serializable
{
    protected static OpfacDataWrapper nullOpfacDataWrapper =
	new NullOpfacDataWrapper();
    protected OpfacData opfacData;
    protected PlanString processId;
    protected PlanString site;

    /**
     * Creates a wrapper to extend the behavior of fsats.plan.OpfacData
     * to encapsulate data about the process and site upon which the
     * OPFAC represented is running.
     * Delegates other behavior to the fsats.plan.OpfacData object that
     * it wraps.
     *
     * @param opfacData - the OpfacData object wrapped by the OpfacDataWrapper.
     * @param processId - the process on which the OPFAC represented is
     *                    running.
     * @param site      - the site on which the OPFAC represented is running.
     */
    public OpfacDataWrapper(OpfacData opfacData,
			    PlanString processId,
			    PlanString site)
    {
	this.opfacData = opfacData;
	this.processId = processId;
	this.site = site;
    }

    /**
     * Creates a wrapper to extend the behavior of fsats.plan.OpfacData
     * to encapsulate data about the process and site upon which the
     * OPFAC represented is running.
     * Delegates other behavior to the fsats.plan.OpfacData object that
     * it wraps.
     *
     * @param opfacData - the OpfacData object wrapped by the OpfacDataWrapper.
     */
    public OpfacDataWrapper(OpfacData opfacData)
    {
	this.opfacData = opfacData;
	processId = PlanString.getNull();
	site = PlanString.getNull();
    }

    /**
     * Returns a null-valued OpfacDataWrapper.
     */
    public static OpfacDataWrapper getNull()
    {
	return nullOpfacDataWrapper;
    }

    /**
     * Returns whether or not an OpfacDataWrapper is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the OPFAC's unit number.
     */
    public int getUnit()
    {
	return opfacData.getUnit();
    }

    /**
     * Returns the OPFAC's TACFIRE alias.
     */
    public String getName()
    {
	return opfacData.getName();
    }

    /**
     * Returns the ID of the process upon which the OPFAC is running.
     */
    public String getProcessId()
    {
	return processId.toString();
    }

    /**
     * Sets the ID of the process upon which the OPFAC is running.
     */
    public void setProcessId(PlanString proId)
    {
	processId = proId;
    }

    /**
     * Returns the site upon which the OPFAC is running.
     */
    public String getSite()
    {
	return site.toString();
    }

    /**
     * Sets the site upon which the OPFAC is running.
     */
    public void setSite(PlanString s)
    {
	site = s;
    }

    /**
     * Returns the OFPAC's type (called "role" in the dump of the plan).
     */
    public String getType()
    {
	return opfacData.getType();
    }

    /**
     * Returns the name of the OPFAC's superior.
     */
    public String getSuperior()
    {
	return opfacData.getCommander().getName();
    }

    /**
     * Returns the name of the OPFAC supported by this OPFAC.
     */
    public String getSupportee()
    {
	return opfacData.getSupportee().getName();
    }

    /**
     * Returns the OPFAC's device.
     */
    public String getDevice()
    {
	return opfacData.getDevice();
    }

    /**
     * Returns the OPFAC's AFATDS alias.
     */
    public String getAfatdsAlias()
    {
	return opfacData.getAfatdsAlias();
    }

    /**
     * Returns the OPFAC's datum.
     */
    public String getDatum()
    {
	return opfacData.getDatum();
    }

    /**
     * Returns whether or not the OPFAC is simulated.
     */
    public String getSimulated()
    {
	return opfacData.getSimulated();
    }

    /**
     * Returns the OPFAC's DOE name.
     * Returns -1 if the field is null.
     */
    public int getDoeName()
    {
	return opfacData.getDoeName();
    }

    /**
     * Returns the OPFAC's DOE class
     * (should always be "OPFAC").
     */
    public String getDoeClass()
    {
	return opfacData.getDoeClass();
    }

    /**
     * Returns the max target number.
     */
    public String getMaxTargetNumber()
    {
	return opfacData.getMaxTargetNumber();
    }

    /**
     * Returns the min target number.
     */
    public String getMinTargetNumber()
    {
	return opfacData.getMinTargetNumber();
    }

    /**
     * Returns the OpfacNet object whose
     * net name matches the name passed
     * in as a parameter.
     * If the name passed in as a parameter
     * matches the net name of no OpfacNet
     * object, then a null OpfacNet object
     * is returned.
     * (OpfacNet is a class representing
     * an association between an OPFAC and
     * a net.  To access the Net object
     * itself that is associated with an
     * OPFAC, one must call OpfacNet.getNet().)
     */
    public OpfacNet getOpfacNetByName(String netName)
    {
	return opfacData.getOpfacNetByName(netName);
    }

    /**
     * Returns the OpfacNet object whose
     * index matches the index passed in
     * as a parameter.
     * (OpfacNet is a class representing
     * an association between an OPFAC and
     * a net.  To access the Net object
     * itself that is associated with an
     * OPFAC, one must call OpfacNet.getNet().)
     */
    public OpfacNet getOpfacNetByIndex(int netIndex)
    {
	return opfacData.getOpfacNetByIndex(netIndex);
    }

    /**
     * Returns the number of nets associated
     * with the OPFAC.
     */
    public int getOpfacNetCount()
    {
	return opfacData.getOpfacNetCount();
    }

    /**
     * Returns the OPFAC's grid zone.
     */
    public int getGridZone()
    {
	return opfacData.getGridZone();
    }

    /**
     * Returns the OPFAC's easting.
     */
    public int getEasting()
    {
	return opfacData.getEasting();
    }

    /**
     * Returns the OPFAC's northing.
     */
    public int getNorthing()
    {
	return opfacData.getNorthing();
    }

    /**
     * Returns the OPFAC's longitude.
     */
    public float getLongitude()
    {
	fsats.measures.Location loc =
	    fsats.measures.GeoUTMConversion.UTMtoLL(
                new fsats.measures.UTMLocation(getEasting(),
					       getNorthing(),
					       getGridZone()));

	return (float)loc.longitude();
    }

    /**
     * Returns the OPFAC's latitude.
     */
    public float getLatitude()
    {
	fsats.measures.Location loc =
	    fsats.measures.GeoUTMConversion.UTMtoLL(
		new fsats.measures.UTMLocation(getEasting(),
					       getNorthing(),
					       getGridZone()));

	return (float)loc.latitude();
    }

    /**
     * Returns the location type of the OPFAC.
     */
    public String getLocationType()
    {
	return opfacData.getLocationType();
    }

    /**
     * Returns the OPFAC's altitude.
     */
    public int getAltitude()
    {
	return opfacData.getAltitude();
    }

    /**
     * Returns the OPFAC's range.
     */
    public float getRange()
    {
	return opfacData.getRange();
    }

    /**
     * Returns a string representation of the OpfacDataWrapper object.
     */
    public String toString()
    {
	StringBuffer s = new StringBuffer();
	s.append("OPFAC " + getName());
	s.append(" type " + getType());
	if( getSuperior() != null)
	    s.append(" with superior " + getSuperior());
	if( getSupportee() != null)
	    s.append(" which supports " + getSupportee());
	s.append("\n");
	s.append(" on process " + getProcessId());
	s.append(" at site " + getSite());

	return s.toString();
    }
}

/**
 * Provides a representation of a null-valued OpfacDataWrapper.
 */
class NullOpfacDataWrapper extends OpfacDataWrapper
{
    /**
     * Constructs a representation of a null-valued OpfacDataWrapper.
     */
    public NullOpfacDataWrapper()
    {
	super(OpfacData.getNull(),
	      PlanString.getNull(),
	      PlanString.getNull());
    }

    /**
     * Returns whether or not a NullOpfacDataWrapper is null.
     */
    public boolean isNull()
    {
	return true;
    }

    /**
     * Returns a string representation of a null-valued OpfacDataWrapper.
     */
    public String toString()
    {
	return "";
    }
}
