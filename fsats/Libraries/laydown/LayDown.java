//                              -*- Mode: Java -*-
// Version         :
// Author          : Robert S. MacDonald
// Last Modified By: 
// Last Modified On: Tue Jan 09 10:33:35 2001

package laydown;

import fsats.util.DataContainer;
import fsats.plan.*;
import java.io.*;
import java.util.*;
// --- dsb --- import FS.*;
import log.Log;

public class LayDown
    implements java.io.Serializable
{
    /** LayDown is a singleton for now. **/
    private static LayDown _self;

    /** A map of network object names to network object records **/
    public Map _networkRecords = new HashMap();

    /** A map of opfac names to opfac records **/
    public Map _opfacRecords = new HashMap();

    /** A map of opfac name to name of that opfac's superior **/
    public Map _opfacSuperior = new HashMap();

    /** A map of opfac name to a collection of that opfac's subordinates **/
    public Map _opfacSubordinates = new HashMap();

    /** A map of opfac name to name of opfac being supported **/
    public Map _opfacSupportee = new HashMap();

    /** A map of opfac name to a collection of opfacs which support it **/
    public Map _opfacSupporters = new HashMap();

    private TestPlan plan;

    private LayDown(File file)
    {
	DataContainer laydownDc;
	try
	{
	    PushbackReader input = new PushbackReader(new FileReader(file));
	    laydownDc = DataContainer.read(input);
	    input.close();
	}
	catch (Exception e)
	{
	    Log.error("Exception reading laydown.");
	    e.printStackTrace();
	    laydownDc=DataContainer.nullDataContainer();
	}

	PlanString planFileName = 
            new PlanString(laydownDc.getField("plan_file_name").getValue());

	if (planFileName.isNull())
	    plan = new TestPlan();
	else
	    plan = new TestPlan(planFileName.toString());

	OpfacDataSet opfacDataSet = plan.getOpfacs();
	for(int i=0; i<opfacDataSet.getOpfacCount(); ++i)
	    addOpfac(new OpfacDataWrapper(opfacDataSet.getOpfacByIndex(i)));

	NetworkDataSet networkDataSet = plan.getNets();
	for(int i=0; i<networkDataSet.getNetCount(); ++i)
        {
            NetworkData net = networkDataSet.getNetByIndex(i);
	    _networkRecords.put(net.getName(), new NetworkDataWrapper(net));
        }

	setLaydownConfigInfo(laydownDc);
    }

    /**
     * Create a laydown object from a file.
     */
    public static LayDown read(File file) 
        throws IOException, ClassNotFoundException
    {
	if(_self != null)
	{
	    Log.error("Laydown already created!");
	}
	else
	{
	    _self = new LayDown(file);
	    // Debugging
	    System.out.println(_self.toString());
	}

	return _self;
    }

    /**
     * Adds a record for a new OPFAC object.
     */
    public void addOpfac(OpfacDataWrapper record)
    {
	String name = record.getName();
	String superior = record.getSuperior();
	String supportee = record.getSupportee();

	// Add it to the list of opfacs.
	_opfacRecords.put(name, record);

	// Add two-way links to the superior.
	if( superior != null )
	{
	    _opfacSuperior.put(name, superior);
	    Collection set = (Collection) _opfacSubordinates.get(superior);
	    if( set == null)
	    {
		set = new HashSet();
		_opfacSubordinates.put(superior, set);
	    }
	    set.add(name);
	}

	// Add two-way links to supported opfacs.
	if( supportee != null)
	{
	    _opfacSupportee.put(name, supportee);
	    Collection set = (Collection) _opfacSupporters.get(supportee);
	    if( set == null )
	    {
		set = new HashSet();
		_opfacSupporters.put(supportee, set);
	    }
	    set.add(name);
	}
    }

    /**
     * Returns the superior of the specified opfac.
     */
    public OpfacData getSuperior(String opfacName)
    {
	String name = (String) _opfacSuperior.get(opfacName);
	return name==null ? null : (OpfacData)_opfacRecords.get(name);
    }

    /**
     * Returns the names of the subordinates of the specified opfac.
     */
    public Collection getSubordinateNames(String opfacName)
    {
	Collection c = (Collection)_opfacSubordinates.get(opfacName);
	return c==null ? new HashSet() : c;
    }

    /**
     * Returns the opfac that this weapon supports.
     */
    public OpfacData getSupportee(String opfacName)
    {
	String name = (String) _opfacSupportee.get(opfacName);
	return name==null ? null : (OpfacData)_opfacRecords.get(name);
    }

    /**
     * Returns the names of the opfacs supporting this opfac.
     */
    public Collection getSupporterNames(String opfacName)
    {
	Collection c = (Collection)_opfacSupporters.get(opfacName);
	return c==null ? new HashSet() : c;
    }

    /**
     * Returns a network object record by name.
     */
    public NetworkDataWrapper getNetwork(String networkName)
    {
	return (NetworkDataWrapper) _networkRecords.get(networkName);
    }

    /**
     * Returns all network object records.
     */
    public Collection getAllNetworks()
    {
	return _networkRecords.values();
    }

    /**
     * Returns an opfac record by name.
     */
    public OpfacDataWrapper getOpfac(String opfacName)
    {
	return (OpfacDataWrapper) _opfacRecords.get(opfacName);
    }

    /**
     * Returns an opfac record by unit number.
     */
    public OpfacDataWrapper getOpfacByUnit(int opfacUnit)
    {
	OpfacDataWrapper opfac = null;
	Iterator i = getAllOpfacs().iterator();

	while(i.hasNext() && opfac==null)
	{
	    opfac = (OpfacDataWrapper)i.next();
            if (opfac.getUnit() != opfacUnit)
                opfac=null;
	}

	return opfac;
    }

    /**
     * Returns all opfac records.
     */
    public Collection getAllOpfacs()
    {
	return _opfacRecords.values();
    }

    /**
     * Returns a human readable description of this object.
     */
    public String toString() 
    {
	StringBuffer o = new StringBuffer();
	o.append("LayDown:\n");

	// Print network objects
	o.append("\nNetworks:\n");
	Iterator i = _networkRecords.values().iterator();
        while (i.hasNext())
	    o.append(i.next().toString()+"\n");

	// Print opfacs
	o.append("\nOpfacs:\n");
	i = _opfacRecords.values().iterator();
        while (i.hasNext())
	    o.append(i.next().toString()+"\n");

	// Done
	o.append("\nEnd LayDown\n");
	return o.toString();
    }

    /**
     * Returns a reference to the LayDown object.
     */
    public static LayDown getSelf()
    {
	return _self;
    }

    // Sets laydown information found not in the plan but
    // only in the laydown configuration file.
    private void setLaydownConfigInfo(DataContainer laydownDc)
    {
	setOpfacLaydownConfigInfo(laydownDc);
	setNetworkLaydownConfigInfo(laydownDc);
    }

    // Sets laydown information found not in the plan but
    // only in the laydown configuration file for opfacs.
    private void setOpfacLaydownConfigInfo(DataContainer laydownDc)
    {
        DataContainer opfacsDc = laydownDc.getField("opfacs");
	for(int i=1; i<=opfacsDc.getFieldCount(); ++i)
	{
            DataContainer opfacDc = opfacsDc.getField(i);
	    OpfacDataWrapper opf = getOpfac(opfacDc.getLabel());

	    if (opf != null)
	    {
                DataContainer processDc=opfacDc.getField("process");
		opf.setProcessId(
                    new PlanString(processDc.getField("id").getValue()));
		opf.setSite(
                    new PlanString(processDc.getField("site").getValue()));
	    }
	}
    }

    // Sets laydown information found not in the plan but
    // only in the laydown configuration file for networks.
    private void setNetworkLaydownConfigInfo(DataContainer laydownDc)
    {
        DataContainer networksDc = laydownDc.getField("networks");
	for(int i=1; i<=networksDc.getFieldCount(); ++i)
	{
            DataContainer networkDc = networksDc.getField(i);
            NetworkDataWrapper net = getNetwork(networkDc.getLabel());
	    if (net != null)
	    {
                DataContainer processDc = networkDc.getField("process");
		net.setProcessId(
                    new PlanString(processDc.getField("id").getValue()));
		net.setSite(
                    new PlanString(processDc.getField("site").getValue()));
		net.setTransmitDelay(
                    PlanFloat.parsePlanFloat(
                        networkDc.getField("transmit_delay").getValue()));
	    }
	}
    }
}
