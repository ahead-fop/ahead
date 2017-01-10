//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Mar 14  9:44:00 2000

package fsats.plan;

import java.util.Vector;

/**
 * Provides an abstract interface to the
 * set of OPFACs in the test plan.
 */
public class OpfacDataSet
{
    private static OpfacDataSet nullOpfacDataSet=new NullOpfacDataSet();
    private OpfacData[] opfacs;

    /**
     * Constructs an OpfacDataSet.
     * @param opfacs - an array of OpfacData objects to
     *                 be stored in the OpfacDataSet.
     */
    public OpfacDataSet(OpfacData[] opfacs)
    {
	this.opfacs=opfacs;
    }

    /**
     * Returns the NullOpfacSet.
     */
    public static OpfacDataSet getNull()
    {
	return nullOpfacDataSet;
    }

    /**
     * Returns whether or not the OpfacDataSet is null.
     */
    public boolean isNull()
    {
	return false;
    }

    /**
     * Returns the OpfacData object whose name
     * matches the name passed in as a
     * parameter.
     * If the name passed in as a parameter
     * matches the name of no OpfacData object,
     * then a null OpfacData object is returned.
     */
    public OpfacData getOpfacByName(String opfacName)
    {
	OpfacData opfac=OpfacData.getNull();

	for(int i=0; opfac.isNull() && i<getOpfacCount(); ++i)
	    if(opfacName.equals(opfacs[i].getName()))
		opfac=opfacs[i];

	return opfac;
    }

    /**
     * Returns the OpfacData object whose index
     * matches the index passed in as a
     * parameter.
     */
    public OpfacData getOpfacByIndex(int opfacIndex)
    {
	return opfacs[opfacIndex];
    }

    /**
     * Returns the OpfacData object whose unit
     * number matches the unit number passed
     * in as a parameter.
     * If the unit number passed in as a
     * parameter matches the name of no
     * OpfacData object, then a null OpfacData
     * object is returned.
     */
    public OpfacData getOpfacByUnit(int opfacUnit)
    {
	OpfacData opfac=OpfacData.getNull();

	for(int i=0; opfac.isNull() && i<getOpfacCount(); ++i)
	    if(opfacUnit==opfacs[i].getUnit())
		opfac=opfacs[i];

	return opfac;
    }

    /**
     * Returns the number of OPFACs in
     * this set.
     */
    public int getOpfacCount()
    {
	return opfacs.length;
    }

    /**
     * Returns the names of all OPFACs
     * in this set.
     */
    public String[] getOpfacNames()
    {
	String[] opfacNames=new String[getOpfacCount()];

	for(int i=0; i<opfacNames.length; ++i)
	    opfacNames[i]=opfacs[i].getName();

	return opfacNames;
    }

    /**
     * Returns the unit numbers of all OPFACs
     * in this set.
     */
    public int[] getOpfacUnits()
    {
	int[] opfacUnits=new int[getOpfacCount()];

	for(int i=0; i<opfacUnits.length; ++i)
	    opfacUnits[i]=opfacs[i].getUnit();

	return opfacUnits;
    }

    /**
     * Returns the types of all OPFACs
     * in this set.
     */
    public String[] getTypes()
    {
	Vector typesVector=new Vector();

	for(int i=0; i<getOpfacCount(); ++i)
	    typesVector.addElement(opfacs[i].getType());

	typesVector=eliminateDuplicates(typesVector);

	String[] types=new String[typesVector.size()];
	typesVector.copyInto(types);

	return types;
    }

    /**
     * Returns a String representation of the OpfacSet object.
     */
    public String toString()
    {
	String opfacString="opfacs=(";

	for(int i=0; i<getOpfacCount(); ++i)
	    opfacString+=opfacs[i].toString();

	opfacString+=")";

	return opfacString;
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
	Vector noDuplicates=new Vector();
	boolean isDuplicate;

	noDuplicates.addElement(v.elementAt(0));

	for(int i=1; i<v.size(); ++i)
	{
	    if((String)(v.elementAt(i))==null)
		isDuplicate=true;
	    else
	    {
		isDuplicate=false;

		for(int j=0; j<noDuplicates.size(); ++j)
		    if(((String)(v.elementAt(i))).equals((String)(noDuplicates.elementAt(j))))
			isDuplicate=true;
	    }

	    if(!isDuplicate)
		noDuplicates.addElement(v.elementAt(i));
	}

	return noDuplicates;
    }
}

class NullOpfacDataSet extends OpfacDataSet
{
    /**
     * Constructs a NullOpfacDataSet.
     */
    NullOpfacDataSet()
    {
	super(new OpfacData[0]);
    }

    /**
     * Returns whether or not the NullOpfacDataSet is null.
     */
    public boolean isNull()
    {
	return true;
    }
}
