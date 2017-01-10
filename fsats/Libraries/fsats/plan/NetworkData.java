//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:21:00 2000

package fsats.plan;

/**
 * Provides an abstract interface to a net.
 */
public class NetworkData
{
    private static NetworkData nullNetworkData=new NullNetworkData();
    private PlanString name;
    private PlanString protocol;

    /**
     * Constructs a NetworkData object.
     *
     * @param name     - the net name.
     * @param protocol - the protocol associated with the net.
     */
    public NetworkData(PlanString name, PlanString protocol)
    {
	this.name=name;
	this.protocol=protocol;
    }

    /**
     * Constructs a NetworkData object.
     *
     * @param name - the net name.
     */
    public NetworkData(PlanString name)
    {
	this(name, PlanString.getNull());
    }

    /**
     * Returns a reference to the NullNetworkData object.
     */
    public static NetworkData getNull()
    {
	return nullNetworkData;
    }

    /**
     * Returns whether or not the NetworkData is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the net name.
     */
    public String getName()
    {
	return name.toString();
    }

    /**
     * Returns the protocol associated
     * with the net.
     */
    public String getProtocol()
    {
	return protocol.toString();
    }

    /**
     * Returns a String representation
     * of the NetworkData object.
     */
    public String toString()
    {
	return getName()+"=(protocol="+getProtocol()+")";
    }
}

class NullNetworkData extends NetworkData
{
    /**
     * Constructs a NullNetworkData object.
     */
    NullNetworkData()
    {
	super(PlanString.getNull(), PlanString.getNull());
    }

    /**
     * Returns whether or not the NullNetworkData object is null.
     */
    public boolean isNull()
    {
	return true;
    }
}
