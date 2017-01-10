//                              -*- Mode: Java -*-
// Version         : 1.7
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Feb 15 15:09:00 2000

package fsats.plan;

import java.io.*;
import java.util.Vector;

import fsats.util.DataContainer;
import fsats.util.FsatsProperties;

/**
 * Provides an abstract interface to the test plan.
 */
public class TestPlan
{
    private MapModCoordinates mapModCoordinates;
    private NetworkDataSet nets;
    private OpfacDataSet opfacs;
    private PortDataSet ports;
    private DataContainer planDataContainer;

    /**
     * Constructs an object through which
     * information on the plan can be accessed.
     */
    public TestPlan()
    {
        readTestPlan (FsatsProperties.get (FsatsProperties.PLAN_FILE) );
    }


    /**
     * Constructs an object through which
     * information on the plan can be accessed.
     *
     * @param fileLocation - The full path name of the file from which
     *                       information on the plan is to be read.
     */
    public TestPlan(String fileLocation)
    {
	readTestPlan(fileLocation);
    }

    /**
     * Reads information on the plan into the TestPlan object.
     *
     * @param fileLocation - The full path name of the file from which
     *                       information on the path is to be read.
     */
    private void readTestPlan(String fileLocation)
    {
	String fieldFromDataContainer;
	PlanInteger lowerLeftAltitude, lowerLeftEasting, lowerLeftGridZone,
	    lowerLeftNorthing, planID, upperRightAltitude, upperRightEasting,
	    upperRightGridZone, upperRightNorthing;

	try
	{
	    InputStream fileIn=new FileInputStream(new File(fileLocation));
	    PushbackReader pushbackReader=new PushbackReader(new InputStreamReader(fileIn));
	    planDataContainer=DataContainer.read(pushbackReader);
	}
	catch(IOException e)
	{
	    planDataContainer=new DataContainer("");
	    System.out.println(e.toString());
	}

	nets=new NetworkDataSet(readNets());
	opfacs=new OpfacDataSet(readOpfacs());
	ports=new PortDataSet(readPorts());

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("lower_left_altitude").getValue();
	lowerLeftAltitude=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("lower_left_easting").getValue();
	lowerLeftEasting=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("lower_left_grid_zone").getValue();
	lowerLeftGridZone=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("lower_left_northing").getValue();
	lowerLeftNorthing=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("plan_id").getValue();
	planID=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("upper_right_altitude").getValue();
	upperRightAltitude=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("upper_right_easting").getValue();
	upperRightEasting=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("upper_right_grid_zone").getValue();
	upperRightGridZone=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	fieldFromDataContainer=planDataContainer.getField("map_mod_coordinates").
	    getField("upper_right_northing").getValue();
	upperRightNorthing=PlanInteger.parsePlanInteger(fieldFromDataContainer);

	mapModCoordinates=new MapModCoordinates(lowerLeftAltitude, lowerLeftEasting,
						lowerLeftGridZone, lowerLeftNorthing,
						planID,
						upperRightAltitude, upperRightEasting,
						upperRightGridZone, upperRightNorthing);
    }

    /**
     * Returns all nets in the
     * plan as a NetworkDataSet.
     */
    public NetworkDataSet getNets()
    {
	return nets;
    }

    /**
     * Returns all OPFACS in the
     * plan as an OpfacDataSet.
     */
    public OpfacDataSet getOpfacs()
    {
	return opfacs;
    }

    /**
     * Returns all ports in the
     * plan as a PortDataSet.
     */
    public PortDataSet getPorts()
    {
	return ports;
    }

    /**
     * Returns the "map_mod_coordinate" information.
     */
    public MapModCoordinates getMapModCoordinates()
    {
	return mapModCoordinates;
    }

    /**
     * Returns the plan ID.
     */
    public int getPlanID()
    {
	return mapModCoordinates.getPlanID();
    }

    /**
     * Returns a String representation of the TestPlan object.
     */
    public String toString()
    {
	return "plan=("+mapModCoordinates.toString()+
	    nets.toString()+" "+opfacs.toString()+
	    " "+ports.toString()+")";
    }

    /**
     * Reads the net information from the test plan dump
     * and puts it in an array of NetworkData objects.
     */
    private NetworkData[] readNets()
    {
	Vector netsVector=new Vector();
	PlanString name;
	PlanString protocol;

	for(int i=1; i<=planDataContainer.getField("nets").getFieldCount(); ++i)
	{
	    name=new PlanString(planDataContainer.getField("nets").getField(i).getLabel());
	    protocol=new PlanString(planDataContainer.getField("nets").getField(i).getField("protocol").getValue());

	    netsVector.addElement(new NetworkData(name, protocol));
	}

	NetworkData[] nets=new NetworkData[netsVector.size()];
	netsVector.copyInto(nets);

	return nets;
    }

    /**
     * Reads the OPFAC information from the test plan dump
     * and puts it in an array of OpfacData objects.
     */
    private OpfacData[] readOpfacs()
    {
	Vector opfacsVector=new Vector();
	Vector opfacNetsVector=new Vector();
	Vector commandersVector=new Vector();
	Vector supporteesVector=new Vector();
	PlanInteger unit;
	PlanString name;
	PlanString type;
	PlanString device;
	PlanString afatdsAlias;
	PlanInteger commander;
	PlanString datum;
	PlanString simulated;
	PlanInteger doeName;
	PlanString doeClass;
	PlanString maxTargetNumber;
	PlanString minTargetNumber;
	PlanInteger supportee;
	PlanInteger locationGridZone;
	PlanInteger locationEasting;
	PlanInteger locationNorthing;
	PlanString locationType;
	PlanInteger locationAltitude;
	PlanFloat range;
	String netName;
	PlanString address;
	OpfacNet[] opfacNets;
    
	for(int i=1; i<=planDataContainer.getField("opfacs").getFieldCount(); ++i)
	{
	    unit=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getLabel());
	    name=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("tacfire_alias").getValue());
	    type=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("role").getValue());
	    device=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("device").getValue());
	    afatdsAlias=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("afatds_alias").getValue());
	    commander=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getField("commander").getValue());
	    datum=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("datum").getValue());
	    simulated=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("simulated").getValue());
	    doeName=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getField("doe_object_name").getValue());
	    doeClass=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("doe_object_class").getValue());
	    maxTargetNumber=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("max_target_number").getValue());
	    minTargetNumber=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("min_target_number").getValue());
	    supportee=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getField("supported").getValue());
	    locationGridZone=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getField("location_grid_zone").getValue());
	    locationEasting=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getField("location_easting").getValue());
	    locationNorthing=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getField("location_northing").getValue());
	    locationType=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("location_type").getValue());
	    locationAltitude=PlanInteger.parsePlanInteger(planDataContainer.getField("opfacs").getField(i).getField("location_altitude").getValue());
	    range=PlanFloat.parsePlanFloat(planDataContainer.getField("opfacs").getField(i).getField("range").getValue());

	    for(int j=1; j<=planDataContainer.getField("opfacs").getField(i).getField("nets").getFieldCount(); ++j)
	    {
		netName=planDataContainer.getField("opfacs").getField(i).getField("nets").getField(j).getLabel();
		address=new PlanString(planDataContainer.getField("opfacs").getField(i).getField("nets").getField(j).getField("address").getValue());

		opfacNetsVector.addElement(new OpfacNet(nets.getNetByName(netName), address));
	    }

	    opfacNets=new OpfacNet[opfacNetsVector.size()];
	    opfacNetsVector.copyInto(opfacNets);
	    opfacNetsVector.removeAllElements();

	    // Note that the commander, subordinates, supportee,
	    // and supporters fields are first set to null and
	    // then set properly once the array of Opfacs is in place.
	    opfacsVector.addElement(new OpfacData(unit, name, type, device,
						  afatdsAlias,
						  OpfacData.getNull(),
						  new OpfacData[0], datum,
						  simulated, doeName, doeClass,
						  maxTargetNumber,
						  minTargetNumber, opfacNets,
						  OpfacData.getNull(),
						  new OpfacData[0],
						  locationGridZone,
						  locationEasting,
						  locationNorthing,
						  locationType,
						  locationAltitude, range));
	    commandersVector.addElement(commander);
	    supporteesVector.addElement(supportee);
	}

	OpfacData[] opfacs=new OpfacData[opfacsVector.size()];
	opfacsVector.copyInto(opfacs);

	// Sets the commander of each OPFAC.
	for(int i=0; i<opfacs.length; ++i)
	    for(int j=0; j<opfacs.length; ++j)
		if(opfacs[j].getUnit()==((PlanInteger)commandersVector.elementAt(i)).intValue())
		    opfacs[i].setCommander(opfacs[j]);

	// Sets the supportee of each OPFAC.
	for(int i=0; i<opfacs.length; ++i)
	    for(int j=0; j<opfacs.length; ++j)
		if(opfacs[j].getUnit()==((PlanInteger)supporteesVector.elementAt(i)).intValue())
		    opfacs[i].setSupportee(opfacs[j]);

	linkOpfacsToSubordinates(opfacs);
	linkOpfacsToSupporters(opfacs);

	return opfacs;
    }

    // Subroutine to set links from each OPFAC to its subordinates.
    private void linkOpfacsToSubordinates(OpfacData[] opfacs)
    {
	for(int i=0; i<opfacs.length; ++i)
	{
	    Vector subordinatesVector=new Vector();

	    for(int j=0; j<opfacs.length; ++j)
		if(opfacs[j].getCommander().getUnit()==opfacs[i].getUnit())
		    subordinatesVector.addElement(opfacs[j]);

	    OpfacData[] subordinates=new OpfacData[subordinatesVector.size()];
	    subordinatesVector.copyInto(subordinates);
	    opfacs[i].setSubordinates(subordinates);
	}
    }

    // Subroutine to set links from each OPFAC to its supporters.
    private void linkOpfacsToSupporters(OpfacData[] opfacs)
    {
	for(int i=0; i<opfacs.length; ++i)
	{
	    Vector supportersVector=new Vector();

	    for(int j=0; j<opfacs.length; ++j)
		if(opfacs[j].getSupportee().getUnit()==opfacs[i].getUnit())
		    supportersVector.addElement(opfacs[j]);

	    OpfacData[] supporters=new OpfacData[supportersVector.size()];
	    supportersVector.copyInto(supporters);
	    opfacs[i].setSupporters(supporters);
	}
    }

    /**
     * Reads the port information from the test plan
     * dump and puts it in an array of Port objects.
     */
    private PortData[] readPorts()
    {
	Vector portsVector=new Vector();
	PlanString name;
	String netName;

	for(int i=1; i<=planDataContainer.getField("ports").getFieldCount(); ++i)
	{
	    name=new PlanString(planDataContainer.getField("ports").getField(i).getLabel());
	    netName=planDataContainer.getField("ports").getField(i).getField("net").getValue();

	    portsVector.addElement(new PortData(name, nets.getNetByName(netName)));
	}

	PortData[] ports=new PortData[portsVector.size()];
	portsVector.copyInto(ports);

	return ports;
    }
}

