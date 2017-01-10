//
// UnitType
//
// $Revision: 1.1.1.1 $
//
// $Date: 2002-03-14 22:51:25 $
//
package fsats.plan;

import java.util.*;

/**
 * Enumeration of the Opfac property UnitType.
 */
public class UnitType
{
    protected static TreeMap map = new TreeMap();
	protected static ArrayList list = new ArrayList();

    /**
     * Returns a UnitType such that: UnitType.toString().equals( s ).
     * @throws UnitTypeStringException if string doesn't map to an 
     * UnitType.
     */
    public static UnitType fromString( String s )
           throws UnitTypeStringException
    {
        UnitType type = ( s == null? null: (UnitType) map.get( s ) );
        if ( type == null )
           throw new UnitTypeStringException( s );
        return type;
    }

    /**
     * Returns a iteration of the string equivalents of the enumeration.
     * The class returned is String.
     */
    public static Iterator iterator()
    {
        return list.iterator();
    }

	public final static UnitType AIRDEFENSE_SR = new UnitType
        ( "Air Defense / Short Range" );
	public final static UnitType AIRDEFENSE_MISSILE = new UnitType
        ( "Air Defense / Missile" );
	public final static UnitType AIRDEFENSE_GU = new UnitType
        ( "Air Defense / Gun Unit" );
	public final static UnitType AIRDEFENSE_COMPOSITE = new UnitType
        ( "Air Defense / Composite" );
	public final static UnitType AIRDEFENSE_TU = new UnitType
        ( "Air Defense / Targeting Unit" );
	public final static UnitType AIRDEFENSE_TMDU = new UnitType
        ( "Air Defense / Theater Missile Defense Unit" );
		
	public final static UnitType ARMOR_TRACKED = new UnitType
        ( "Armor / Tracked" );
	public final static UnitType ARMOR_WHEELED = new UnitType
		( "Armor / Wheeled" );
		
	public final static UnitType ANTIARMOR_DISMOUNTED = new UnitType
        ( "Anti Armor / Dismounted" );
	public final static UnitType ANTIARMOR_LIGHT = new UnitType
        ( "Anti Armor / Light" );
	public final static UnitType ANTIARMOR_AIRBORNE = new UnitType
        ( "Anti Armor / Airborne" );
	public final static UnitType ANTIARMOR_AIRASSAULT = new UnitType
        ( "Anti Armor / Air Assault" );
	public final static UnitType ANTIARMOR_MOUNTAIN = new UnitType
        ( "Anti Armor / Mountain" );
	public final static UnitType ANTIARMOR_ARCTIC = new UnitType
        ( "Anti Armor / Arctic" );
	public final static UnitType ANTIARMOR_ARMORED = new UnitType
        ( "Anti Armor / Armored" );
	public final static UnitType ANTIARMOR_MOTORIZED = new UnitType
        ( "Anti Armor / Motorized" );
		
	public final static UnitType AVIATION_FIXEDWING = new UnitType
        ( "Aviation / Fixed Wing" );
	public final static UnitType AVIATION_ROTARYWING = new UnitType
        ( "Aviation / Rotary Wing" );
	public final static UnitType AVIATION_SEARCH_RESCUE = new UnitType
        ( "Aviation / Search and Rescue" );
	public final static UnitType AVIATION_COMPOSITE = new UnitType
        ( "Aviation / Composite" );
	public final static UnitType AVIATION_VSTOL = new UnitType
        ( "Aviation / V/STOL" );
	public final static UnitType AVIATION_UAV = new UnitType
        ( "Aviation / UAV" );
		
	public final static UnitType INFANTRY_MECHANIZED = new UnitType
        ( "Infantry / Mechanized" );
	public final static UnitType INFANTRY_LIGHT = new UnitType
        ( "Infantry / Light" );
	public final static UnitType INFANTRY_MOTORIZED = new UnitType
        ( "Infantry / Motorized" );
	public final static UnitType INFANTRY_MOUNTAIN = new UnitType
        ( "Infantry / Mountain" );
	public final static UnitType INFANTRY_AIRBORNE = new UnitType
        ( "Infantry / Airborne" );
	public final static UnitType INFANTRY_AIRASSAULT = new UnitType
        ( "Infantry / Air Assault" );
	public final static UnitType INFANTRY_NAVAL = new UnitType
        ( "Infantry / Naval" );
	public final static UnitType INFANTRY_IFV = new UnitType
        ( "Infantry / Infantry Fighting Vehicle" );
	public final static UnitType INFANTRY_ARCTIC = new UnitType
        ( "Infantry / Arctic" );

	public final static UnitType ENGINEER_COMBAT = new UnitType
        ( "Engineer / Combat" );
	public final static UnitType ENGINEER_CONSTRUCTION = new UnitType
        ( "Engineer / Construction" );
		
	public final static UnitType FIELDARTILLERY_HOWITZERGUN = new UnitType
        ( "Field Artillery / Howitzer-Gun" );
	public final static UnitType FIELDARTILLERY_ROCKET = new UnitType
        ( "Field Artillery / Rocket" );
	public final static UnitType FIELDARTILLERY_TARGETACQUISITION = new UnitType
        ( "Field Artillery / Target Acquisition" );
	public final static UnitType FIELDARTILLERY_MORTAR = new UnitType
        ( "Field Artillery / Mortar" );
	public final static UnitType FIELDARTILLERY_ARTILLERYSURVEY = new UnitType
        ( "Field Artillery / Artillery Survey" );
	public final static UnitType FIELDARTILLERY_METEOROLOGICAL = new UnitType
        ( "Field Artillery / Meteorological" );
		
	public final static UnitType RECONNAISSANCE_HORSE = new UnitType
        ( "Reconnaissance / Horse" );
	public final static UnitType RECONNAISSANCE_CAVALRY = new UnitType
        ( "Reconnaissance / Cavalry" );
	public final static UnitType RECONNAISSANCE_ARCTIC = new UnitType
        ( "Reconnaissance / Arctic" );
	public final static UnitType RECONNAISSANCE_AIRASSAULT = new UnitType
        ( "Reconnaissance / Air Assault" );
	public final static UnitType RECONNAISSANCE_AIRBORNE = new UnitType
        ( "Reconnaissance / Airborne" );
	public final static UnitType RECONNAISSANCE_MOUNTAIN = new UnitType
        ( "Reconnaissance / Mountain" );
	public final static UnitType RECONNAISSANCE_LIGHT = new UnitType
        ( "Reconnaissance / Light" );
	public final static UnitType RECONNAISSANCE_MARINE = new UnitType
        ( "Reconnaissance / Marine" );
	public final static UnitType RECONNAISSANCE_LRS = new UnitType
        ( "Reconnaissance / Long Range Surveillance" );
		
	public final static UnitType MISSILE_SURFTOSURF = new UnitType
        ( "Missile(surf-surf)" );
		
	public final static UnitType ISF_RIVERINE = new UnitType
        ( "Internal Security Forces / Riverine" );
	public final static UnitType ISF_GROUND = new UnitType
        ( "Internal Security Forces / Ground" );
	public final static UnitType ISF_WM = new UnitType
        ( "Internal Security Forces / Wheeled Mechanized" );
	public final static UnitType ISF_RAILROAD = new UnitType
        ( "Internal Security Forces / Railroad" );
	public final static UnitType ISF_AVIATION = new UnitType
        ( "Internal Security Forces / Aviation" );
		
	public final static UnitType CS_NBC = new UnitType
        ( "Combat Support / NBC" );
	public final static UnitType CS_MI = new UnitType
        ( "Combat Support / Military Intelligence" );
	public final static UnitType CS_LEU = new UnitType
        ( "Combat Support / Law Enforcement Unit" );
	public final static UnitType CS_SU = new UnitType
        ( "Combat Support / Signal Unit" );
	public final static UnitType CS_IWU = new UnitType
        ( "Combat Support / Information Warfare Unit" );
	public final static UnitType CS_LS = new UnitType
        ( "Combat Support / Landing Support" );
	public final static UnitType CS_EOD = new UnitType
        ( "Combat Support / Explosive Ordinance Disposal" );
		
	public final static UnitType CSS_ADMIN = new UnitType
        ( "Combat Service Support / Admin" );
	public final static UnitType CSS_MEDICAL = new UnitType
        ( "Combat Service Support / Medical" );
	public final static UnitType CSS_SUPPLY = new UnitType
        ( "Combat Service Support / Supply" );
	public final static UnitType CSS_TRANSPORTATION = new UnitType
        ( "Combat Service Support / Transportation" );
	public final static UnitType CSS_MAINTENANCE = new UnitType
        ( "Combat Service Support / Maintenance" );
	public final static UnitType CSS_EW = new UnitType
        ( "Combat Service Support / Electronic Warfare" );
		
	public final static UnitType SOF_AVIATION = new UnitType
        ( "Special Operations Forces / Aviation" );
	public final static UnitType SOF_NAVAL = new UnitType
        ( "Special Operations Forces / Naval" );
	public final static UnitType SOF_GROUND = new UnitType
        ( "Special Operations Forces / Ground" );
	public final static UnitType SOF_UNITSUPPORT = new UnitType
        ( "Special Operations Forces / SOF Unit Support" );
		
    private String name;

    protected UnitType( String name )
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
