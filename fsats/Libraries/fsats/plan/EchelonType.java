//
// EchelonType
//
// $Revision: 1.1.1.1 $
//
// $Date: 2002-03-14 22:51:24 $
//
package fsats.plan;

import java.util.*;

/**
 * Enumeration of the Opfac property EchelonType.
 */
public class EchelonType
{
    protected static TreeMap map = new TreeMap();
	protected static ArrayList list = new ArrayList();

    /**
     * Returns a EchelonType such that: EchelonType.toString().equals( s ).
     * @throws EchelonTypeStringException if string doesn't map to an 
     * EchelonType.
     */
    public static EchelonType fromString( String s )
           throws EchelonTypeStringException
    {
        EchelonType type = (s == null?  null: (EchelonType) map.get( s ) );
        if ( type == null )
           throw new EchelonTypeStringException( s );
        return type;
    }

    /**
     * Returns a iteration of the string equivalents of the enumeration.
     * The class returned is String.  Order is guaranteed to be ascending.
     */
    public static Iterator iterator()
    {
        return list.iterator();
    }

	public final static EchelonType TEAM = new EchelonType
		( "Team" );
	public final static EchelonType CREW = new EchelonType
		( "Crew" );
	public final static EchelonType SQUAD = new EchelonType
        ( "Squad" );
	public final static EchelonType SECTION = new EchelonType
        ( "Section" );
	public final static EchelonType PLATOON = new EchelonType
        ( "Platoon" );
	public final static EchelonType DETACHMENT = new EchelonType
        ( "Detachment" );
	public final static EchelonType COMPANY = new EchelonType
        ( "Company" );
	public final static EchelonType BATTERY = new EchelonType
        ( "Battery" );
	public final static EchelonType TROOP = new EchelonType
        ( "Troop" );
	public final static EchelonType BATTALION = new EchelonType
        ( "Battalion" );
	public final static EchelonType SQUADRON = new EchelonType
        ( "Squadron" );
	public final static EchelonType REGIMENT = new EchelonType
		( "Regiment" );
	public final static EchelonType GROUP = new EchelonType
		( "Group" );
	public final static EchelonType BRIGADE = new EchelonType
        ( "Brigade" );
	public final static EchelonType DIVISION = new EchelonType
        ( "Division" );

    private String name;

    protected EchelonType( String name )
    {
        this.name = name;
        map.put( name, this );
		list.add(this);
    }

    public String toString()
    {
        return name;
    }
}
