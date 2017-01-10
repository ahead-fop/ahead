//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:21:00 2000

package fsats.plan;

import java.util.Vector;

/**
 * Provides an abstract interface to
 * the set of nets in the test plan.
 */
public class NetworkDataSet
{
    private static NetworkDataSet nullNetworkDataSet=new NullNetworkDataSet();
    private NetworkData[] nets;

    /**
     * Constructs a NetworkDataSet.
     *
     * @param nets - an array of NetworkData objects to
     *               be stored in the NetworkDataSet.
     */
    public NetworkDataSet(NetworkData[] nets)
    {
	this.nets=nets;
    }

    /**
     * Returns the NullNetworkDataSet.
     */
    public static NetworkDataSet getNull()
    {
	return nullNetworkDataSet;
    }

    /**
     * Returns whether or not the NetworkDataSet is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the NetworkData object whose name
     * matches the name passed in as a parameter.
     * If the name passed in as a parameter matches
     * the name of no NetworkData object, then a
     * null NetworkData object is returned.
     */
    public NetworkData getNetByName(String netName)
    {
	NetworkData net=NetworkData.getNull();

	for(int i=0; net.isNull() && i<getNetCount(); ++i)
	    if(netName.equals(nets[i].getName()))
		net=nets[i];

	return net;
    }

    /**
     * Returns all network object records.
     */
    public NetworkData[] getAllNetworks()
    {
	return nets;
    }

    /**
     * Returns the NetworkData object whose index
     * matches the index passed in as a parameter.
     */
    public NetworkData getNetByIndex(int netIndex)
    {
	return nets[netIndex];
    }

    /**
     * Returns the number of nets in the
     * this set.
     */
    public int getNetCount()
    {
	return nets.length;
    }

    /**
     * Returns the names of all nets
     * in this set.
     */
    public String[] getNetNames()
    {
	String[] netNames=new String[getNetCount()];

	for(int i=0; i<netNames.length; ++i)
	    netNames[i]=nets[i].getName();

	return netNames;
    }

    /**
     * Returns all protocols in this set.
     */
    public String[] getProtocols()
    {
	Vector protocolsVector=new Vector();

	for(int i=0; i<getNetCount(); ++i)
	    protocolsVector.addElement(nets[i].getProtocol());

	protocolsVector=eliminateDuplicates(protocolsVector);

	String[] protocols=new String[protocolsVector.size()];
	protocolsVector.copyInto(protocols);

	return protocols;
    }

    /**
     * Returns a String representation of the NetworkDataSet object.
     */
    public String toString()
    {
	String netSetString="nets=(";

	for(int i=0; i<getNetCount(); ++i)
	    netSetString+=nets[i].toString();

	netSetString+=")";

	return netSetString;
    }

    /**
     * Eliminates duplicate elements in a Vector passed
     * in as a parameter.
     * The Vector passed in as a parameter is not changed.
     * Instead, the method returns a Vector with duplicates
     * eliminated.
     */
    private Vector eliminateDuplicates(Vector v)
    {
	Vector unique=new Vector();
        for (int i=0; i<v.size(); ++i)
        {
            String s = (String)v.elementAt(i);
            boolean isDuplicate=(s==null);
            for (int j=0; !isDuplicate && j<unique.size(); ++j)
            {
                isDuplicate=s.equals((String)unique.elementAt(j));
            }
            if (!isDuplicate) unique.addElement(s);
        }
        return unique;
    }
}

class NullNetworkDataSet extends NetworkDataSet
{
    /**
     * Constructs a NullNetworkDataSet.
     */
    NullNetworkDataSet()
    {
	super(new NetworkData[0]);
    }

    /**
     * Returns whether or not the NullNetworkDataSet object is null.
     */
    public boolean isNull()
    {
	return true;
    }
}
