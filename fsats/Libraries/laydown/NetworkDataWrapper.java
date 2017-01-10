//                              -*- Mode: Java -*-
// Version         : 1.0
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Wed Jan 26 15:11:00 2000

package laydown;

import fsats.plan.*;

/**
 * Extends the functionality of the fsats.plan.NetworkData class
 * to encapsulate data about the process and site upon which the
 * network represented is functioning and about the network's transmit delay.
 * Uses the Delegation design pattern in relation to fsats.plan.NetworkData.
 * Plays the role of Delegator in the design pattern.
 * See Mark Grand. "Patterns in Java, Vol. 1."  New York: John Wiley & Sons,
 * 1998.  pp. 53-9.
 */
public class NetworkDataWrapper implements java.io.Serializable
{
    protected static NetworkDataWrapper nullNetworkDataWrapper =
	new NullNetworkDataWrapper();
    protected NetworkData networkData;
    protected PlanString processId;
    protected PlanString site;
    protected PlanFloat transmitDelay;

    /**
     * Creates a wrapper to extend the behavior of fsats.plan.NetworkData
     * to encapsulate data about the process and site upon which the
     * network represented is functioning and about the network's
     * transmit delay.
     * Delegates other behavior to the fsats.plan.NetworkData object
     * that it wraps.
     *
     * @param networkData   - the NetworkData object wrapped by
     *                        NetworkDataWrapper.
     * @param processId     - the process on which the network represented
     *                        is running.
     * @param site          - the site on which the network represented is
     *                        running.
     * @param transmitDelay - the transmit delay for the network represented.
     */
    public NetworkDataWrapper(NetworkData networkData,
			      PlanString processId,
			      PlanString site,
			      PlanFloat transmitDelay)
    {
	this.networkData = networkData;
	this.processId = processId;
	this.site = site;
	this.transmitDelay = transmitDelay;
    }

    /**
     * Creates a wrapper to extend the behavior of fsats.plan.NetworkData
     * to encapsulate data about the process and site upon which the
     * network represented is functioning and about the network's
     * transmit delay.
     * Delegates other behavior to the fsats.plan.NetworkData object
     * that it wraps.
     *
     * @param networkData   - the NetworkData object wrapped by
     *                        NetworkDataWrapper.
     */
    public NetworkDataWrapper(NetworkData networkData)
    {
	this.networkData = networkData;
	processId = PlanString.getNull();
	site = PlanString.getNull();
	transmitDelay = PlanFloat.getNull();
    }

    /**
     * Returns a null-valued NetworkDataWrapper.
     */
    public static NetworkDataWrapper getNull()
    {
	return nullNetworkDataWrapper;
    }

    /**
     * Returns whether or not a NetworkDataWrapper is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the network's transmit delay.
     */
    public float getTransmitDelay()
    {
	return transmitDelay.floatValue();
    }

    /**
     * Sets the network's transmit delay.
     */
    public void setTransmitDelay(PlanFloat transDelay)
    {
	transmitDelay = transDelay;
    }

    /**
     * Returns the site upon which the network is running.
     */
    public String getSite()
    {
	return site.toString();
    }

    /**
     * Sets the site upon which the network is running.
     */
    public void setSite(PlanString s)
    {
	site = s;
    }

    /**
     * Returns the ID of the process upon which the network is running.
     */
    public String getProcessId()
    {
	return processId.toString();
    }

    /**
     * Sets the ID of the process upon which the network is running.
     */
    public void setProcessId(PlanString proId)
    {
	processId = proId;
    }

    /**
     * Returns the network's name.
     */
    public String getName()
    {
	return networkData.getName();
    }

    /**
     * Returns the network's protocol.
     */
    public String getProtocol()
    {
	return networkData.getProtocol();
    }

    /**
     * Returns a string representation of the network's data.
     */
    public String toString()
    {
	return "Network "+getName()+
	    " on process "+getProcessId()+
	    " at site "+getSite()+
	    " with protocol "+getProtocol();
    }
}

/**
 * Provides a representation of a null-valued NetworkDataWrapper.
 */
class NullNetworkDataWrapper extends NetworkDataWrapper
{
    /**
     * Constructs a representation of a null-valued NetworkDataWrapper.
     */
    NullNetworkDataWrapper()
    {
	super(NetworkData.getNull(),
	      PlanString.getNull(),
	      PlanString.getNull(),
	      PlanFloat.getNull());
    }

    /**
     * Returns whether or not a NullNetworkDataWrapper is null.
     */
    public boolean isNull()
    {
	return true;
    }

    /**
     * Returns a string representation of a null-valued NetworkDataWrapper.
     */
    public String toString()
    {
	return "";
    }
}
