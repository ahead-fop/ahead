//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:23:00 2000

package fsats.plan;

/**
 * Provides an abstract interface
 * to the OPFACs in the test plan.
 */
public class OpfacData
{
    private static OpfacData nullOpfacData=new NullOpfacData();
    private PlanInteger unit;
    private PlanString name;
    private PlanString type;
    private PlanString device;
    private PlanString afatdsAlias;
    private OpfacData commander;
    private OpfacData[] subordinates;
    private PlanString datum;
    private PlanString simulated;
    private PlanInteger doeName;
    private PlanString doeClass;
    private PlanString maxTargetNumber;
    private PlanString minTargetNumber;
    private OpfacNet[] opfacNets;
    private OpfacData supportee;
    private OpfacData[] supporters;
    private PlanInteger locationGridZone;
    private PlanInteger locationEasting;
    private PlanInteger locationNorthing;
    private PlanString locationType;
    private PlanInteger locationAltitude;
    private PlanFloat range;

    /**
     * Constructs an OpfacData object.
     */
    public OpfacData(PlanInteger unit, PlanString name, PlanString type,
		     PlanString device, PlanString afatdsAlias,
		     OpfacData commander, OpfacData[] subordinates,
		     PlanString datum, PlanString simulated,
		     PlanInteger doeName, PlanString doeClass,
		     PlanString maxTargetNumber, PlanString minTargetNumber,
		     OpfacNet[] opfacNets,
		     OpfacData supportee, OpfacData[] supporters,
		     PlanInteger locationGridZone, PlanInteger locationEasting,
		     PlanInteger locationNorthing, PlanString locationType,
		     PlanInteger locationAltitude, PlanFloat range)
    {
	this.unit=unit;
	this.name=name;
	this.type=type;
	this.device=device;
	this.afatdsAlias=afatdsAlias;
	this.commander=commander;
	this.subordinates=subordinates;
	this.datum=datum;
	this.simulated=simulated;
	this.doeName=doeName;
	this.doeClass=doeClass;
	this.maxTargetNumber=maxTargetNumber;
	this.minTargetNumber=minTargetNumber;
	this.opfacNets=opfacNets;
	this.supportee=supportee;
	this.supporters=supporters;
	this.locationGridZone=locationGridZone;
	this.locationEasting=locationEasting;
	this.locationNorthing=locationNorthing;
	this.locationType=locationType;
	this.locationAltitude=locationAltitude;
	this.range=range;
    }

    /**
     * Constructs an OpfacData object.
     */
    public OpfacData(PlanInteger unit, PlanString name, PlanString type,
		     PlanString device, PlanString afatdsAlias,
		     OpfacData commander, OpfacData[] subordinates,
		     PlanString datum, PlanString simulated,
		     PlanInteger doeName, PlanString doeClass,
		     PlanString maxTargetNumber, PlanString minTargetNumber,
		     OpfacNet[] opfacNets)
    {
	this(unit, name, type, device, afatdsAlias, commander, subordinates,
	     datum, simulated, doeName, doeClass, maxTargetNumber,
	     minTargetNumber, opfacNets, getNull(), new OpfacData[0],
	     PlanInteger.getNull(), PlanInteger.getNull(),
	     PlanInteger.getNull(), PlanString.getNull(),
	     PlanInteger.getNull(), PlanFloat.getNull());
    }

    /**
     * Returns a reference to a null OpfacData object.
     */
    public static OpfacData getNull()
    {
	return nullOpfacData;
    }

    /**
     * Returns whether or not the OpfacData object is null.
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
	return unit.intValue();
    }

    /**
     * Returns the OPFAC's TACFIRE alias.
     */
    public String getName()
    {
	return name.toString();
    }

    /**
     * Returns the OPFAC's type (called
     * "role" in the dump of the plan).
     */
    public String getType()
    {
	return type.toString();
    }

    /**
     * Returns the OPFAC's device.
     */
    public String getDevice()
    {
	return device.toString();
    }

    /**
     * Returns the OPFAC's AFATDS alias.
     */
    public String getAfatdsAlias()
    {
	return afatdsAlias.toString();
    }

    /**
     * Returns the OPFAC's commander.
     */
    public OpfacData getCommander()
    {
	return commander;
    }

    /**
     * Returns an array of OPFACs that
     * are subordinate to this OPFAC.
     */
    public OpfacData[] getSubordinates()
    {
	return subordinates;
    }

    /**
     * Returns the names of the OPFACs that
     * are subordinate to this OPFAC.
     */
    public String[] getSubordinateNames()
    {
	String[] subordinateNames=new String[subordinates.length];

	for(int i=0; i<subordinates.length; ++i)
	    subordinateNames[i]=subordinates[i].getName();

	return subordinateNames;
    }

    /**
     * Returns the OPFAC's datum.
     */
    public String getDatum()
    {
	return datum.toString();
    }

    /**
     * Returns a String indicating
     * whether or not the OPFAC is
     * simulated.
     */
    public String getSimulated()
    {
	return simulated.toString();
    }

    /**
     * Returns the OPFAC's DOE name.
     * Returns -1 if the field is null.
     */
    public int getDoeName()
    {
	return doeName.intValue();
    }

    /**
     * Returns the OPFAC's DOE class
     * (should always be "OPFAC").
     */
    public String getDoeClass()
    {
	return doeClass.toString();
    }

    /**
     * Returns the max target number.
     */
    public String getMaxTargetNumber()
    {
	return maxTargetNumber.toString();
    }

    /**
     * Returns the min target number.
     */
    public String getMinTargetNumber()
    {
	return minTargetNumber.toString();
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
	OpfacNet opfacNet=OpfacNet.getNull();

	for(int i=0; opfacNet.isNull() && i<getOpfacNetCount(); ++i)
	    if(netName.equals(opfacNets[i].getNet().getName()))
		opfacNet=opfacNets[i];

	return opfacNet;
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
	return opfacNets[netIndex];
    }

    /**
     * Returns the number of nets associated
     * with the OPFAC.
     */
    public int getOpfacNetCount()
    {
	return opfacNets.length;
    }

    /**
     * Returns the OPFAC supported by this OPFAC.
     */
    public OpfacData getSupportee()
    {
	return supportee;
    }

    /**
     * Returns an array of OPFACs that support this OPFAC.
     */
    public OpfacData[] getSupporters()
    {
	return supporters;
    }

    /**
     * Returns the names of the OPFACs that support this OPFAC.
     */
    public String[] getSupporterNames()
    {
	String[] supporterNames=new String[supporters.length];

	for(int i=0; i<supporters.length; ++i)
	    supporterNames[i]=supporters[i].getName();

	return supporterNames;
    }

    /**
     * Returns the grid zone of the OPFAC.
     */
    public int getGridZone()
    {
	return locationGridZone.intValue();
    }

    /**
     * Returns the easting of the OPFAC.
     */
    public int getEasting()
    {
	return locationEasting.intValue();
    }

    /**
     * Returns the northing of the OPFAC.
     */
    public int getNorthing()
    {
	return locationNorthing.intValue();
    }

    /**
     * Returns the location type of the OPFAC.
     */
    public String getLocationType()
    {
	return locationType.toString();
    }

    /**
     * Returns the altitude of the OPFAC.
     */
    public int getAltitude()
    {
	return locationAltitude.intValue();
    }

    /**
     * Returns the range of the OFPAC.
     */
    public float getRange()
    {
	return range.floatValue();
    }

    /**
     * Provides a String representation of the
     * OpfacData object.
     */
    public String toString()
    {
	String opfacString=unit.toString()+"=(afatds_alias="+
	    afatdsAlias.toString()+" commander="+commander.getUnit()+
	    " datum="+datum.toString()+" device="+device.toString()+
	    " doe_object_class="+doeClass.toString()+" doe_object_name="+
	    doeName.toString()+" max_target_number="+
	    maxTargetNumber.toString()+" min_target_number="+
	    minTargetNumber.toString()+" nets=(";

	for(int i=0; i<getOpfacNetCount(); ++i)
	    opfacString+=opfacNets[i].toString();

	opfacString+=") role="+type.toString()+" simulated="+simulated.toString()+
	    " tacfire_alias="+name.toString()+" supported="+
	    supportee.getUnit()+" location_grid_zone="+
	    locationGridZone.toString()+" location_easting="+
	    locationEasting.toString()+" location_northing="+
	    locationNorthing.toString()+" location_type="+
	    locationType.toString()+" locationAltitude="+
	    locationAltitude.toString()+" range="+range.toString()+")";

	return opfacString;
    }

    /**
     * Sets the commander to the Opfac passed in as a parameter.
     */
    void setCommander(OpfacData commander)
    {
	this.commander=commander;
    }

    /**
     * Sets the list of subordinates to the Opfac array passed in
     * as a parameter.
     */
    void setSubordinates(OpfacData[] subordinates)
    {
	this.subordinates=subordinates;
    }

    /**
     * Sets the supportee to the Opfac passed in as a parameter.
     */
    void setSupportee(OpfacData supportee)
    {
	this.supportee=supportee;
    }

    /**
     * Sets the list of supporters to the Opfac array passed in
     * as a parameter.
     */
    void setSupporters(OpfacData[] supporters)
    {
	this.supporters=supporters;
    }
}

class NullOpfacData extends OpfacData
{
    NullOpfacData()
    {
	super(PlanInteger.getNull(), PlanString.getNull(),
	      PlanString.getNull(), PlanString.getNull(),
	      PlanString.getNull(), OpfacData.getNull(),
	      new OpfacData[0], PlanString.getNull(), PlanString.getNull(),
	      PlanInteger.getNull(), PlanString.getNull(),
	      PlanString.getNull(), PlanString.getNull(),
	      new OpfacNet[0]);
    }

    /**
     * Returns whether or not the NullOpfacData object is null.
     */
    public boolean isNull()
    {
	return true;
    }
}
