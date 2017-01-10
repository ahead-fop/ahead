//                              -*- Mode: Java -*-
// Version         : 1.3
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:24:00 2000

package fsats.plan;

/**
 * Provides an abstract interface to a
 * net associated with an OPFAC.
 */
public class OpfacNet
{
    private static OpfacNet nullOpfacNet=new NullOpfacNet();
    private NetworkData net;
    private PlanString address;

    /**
     * Constructs an OpfacNet.
     */
    public OpfacNet(NetworkData net, PlanString address)
    {
	this.net=net;
	this.address=address;
    }

    /**
     * Returns a null OpfacNet.
     */
    public static OpfacNet getNull()
    {
	return nullOpfacNet;
    }

    /**
     * Returns whether or not the OpfacNet is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the net associated with
     * an Opfac.
     */
    public NetworkData getNet()
    {
	return net;
    }

    /**
     * Returns the address.
     */
    public String getAddress()
    {
	return address.toString();
    }

    /**
     * Returns a String representation of
     * the OpfacNet object.
     */
    public String toString()
    {
	return net.getName()+"=(address="+address.toString()+")";
    }
}

class NullOpfacNet extends OpfacNet
{
    /**
     * Constructs a NullOpfacNet.
     */
    NullOpfacNet()
    {
	super(NetworkData.getNull(), new PlanString(null));
    }

    /**
     * Returns whether or not the NullOpfacNet is null.
     */
    public boolean isNull()
    {
	return true;
    }

    /**
     * Returns a String representation of
     * the NullOpfacNet object.
     */
    public String toString()
    {
	return "";
    }
}
