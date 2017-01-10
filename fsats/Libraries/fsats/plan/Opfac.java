package fsats.plan;

import fsats.measures.UTMLocation;
import fsats.util.ReadOnlyIterator;
import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;
import fsats.util.Log;
import java.util.*;
import java.sql.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Opfac extends PlanObject implements PlanContainerLabels
{
    private HashSet subordinates = new HashSet();

    private Integer observerID;
    private Integer unitNumber;      
    private Integer unitReferenceNumber;      
    private boolean unitReferenceNumberPkg11 = false;
    private String tacfireAlias; 
    private Opfac commander;
    private OpfacDevice device = OpfacDevice.UNKNOWN;
    private String unitName;
    private UnitType unitType;   
    private String machineName;  
    private String bumperNumber;
    private String role;   
    private String uic;     
    private EchelonType echelon;
    private String orName;  
    private String dnsRoleAlias;


    public Opfac( String unitName, String role )
    {
        this.unitName = unitName;
        this.role = role;
    }

    /*
     * Partially constructs an Opfac using the contents of container 
     * for initialization and add the instance to the plan.  
     * The opfac is fully initialized from container with
     * the exception of the commander/subordinate associations.
     * When the commander and subordinates have been added to Plan,
     * completeContainerInitialization() may be called to set the
     * commander/subordinate relations.
     * @see #completeContainerInitialization
     */
    Opfac( DataContainer container, Plan plan ) 
        throws DataContainerFormatException
    {
        setPlan( plan );
        initializeFromContainer( container );
        plan.addOpfac( this );
    }

    public void setDevice( OpfacDevice device )
    {
        this.device = (device==null? OpfacDevice.UNKNOWN: device);
    }

    /**
     * Sets tacfireAlias iff there is no other opfac 
     * with the same tacfireAlias.
     * @see Plan#containsTacfireAlias( String tacfireAlias )
     */
    public void setTacfireAlias( String tacfireAlias )
    {
        if ( tacfireAlias != null && tacfireAlias.equals( this.tacfireAlias) )
            return;
        if ( plan != null && plan.containsTacfireAlias(tacfireAlias) )
            throw new IllegalArgumentException( tacfireAlias );
        this.tacfireAlias = tacfireAlias;
        fireChangeEvent();
    }

    /**
     * Sets unit number iff there is no other opfac with the same unit.
     * @see Plan#containsUnitNumber( String unitNumber )
     */
    public void setUnitNumber( Integer unitNumber )
    {
        if ( unitNumber != null && unitNumber.equals( this.unitNumber) )
            return;
        if ( plan != null && plan.containsUnitNumber( unitNumber ) )
            throw new IllegalArgumentException( unitNumber.toString() );
        if ( unitNumber == null )
            this.unitNumber = unitNumber;
        else
            this.unitNumber = new Integer( unitNumber.intValue() );
        fireChangeEvent();
    }

    /**
     * Sets the unit reference number iff there is no other opfac with 
     * the same URN.
     * @see Plan#containsUnitReferenceNumber( Integer urn )
     */
    public void setUnitReferenceNumber( Integer urn )
    {
        if ( urn != null && urn.equals( unitReferenceNumber) )
            return;
        if ( plan != null && plan.containsUnitReferenceNumber( urn ) )
            throw new IllegalArgumentException( urn.toString() );
        if ( urn == null )
            unitReferenceNumber = urn;
        else
            unitReferenceNumber = new Integer( urn.intValue() );
        fireChangeEvent();
    }

    public void setUnitReferenceNumberPkg11( boolean isPkg11 )
    {
        unitReferenceNumberPkg11 = isPkg11;
    }

    public void setObserverID( Integer observerID )
    {
        this.observerID = observerID;
    }

    /**
     * Sets unit iff there is no other opfac with the same unitName and role.
     * @see Plan#containsOpfac( String unitName, String role )
     */
    public void setUnitNameRole( String unitName, String role )
    {
        if ( unitName != null && unitName.equals( this.unitName )
             && role != null && role.equals( this.role ) )
            return;
        if ( plan != null && plan.containsOpfac( unitName, role ) )
            throw new IllegalArgumentException(unitName +";"+role);
        this.unitName = unitName;
        this.role = role;
        fireChangeEvent();
    }

    public void setUnitType( UnitType unitType )
    {
        this.unitType = unitType;
        fireChangeEvent();
    }

    public void setMachineName( String machineName )
    {
        this.machineName = machineName;
        fireChangeEvent();
    }

    public void setBumperNumber( String bumperNumber )
    {
        this.bumperNumber = bumperNumber;
        fireChangeEvent();
    }

    public void setUIC( String uic )
    {
        this.uic = uic;
        fireChangeEvent();
    }

    public void setEchelon( EchelonType echelon )
    {
        this.echelon = echelon;
        fireChangeEvent();
    }

    public void setOrName( String orName )
    {
        this.orName = orName;
        fireChangeEvent();
    }

    public void setDnsRoleAlias( String dnsRoleAlias )
    {
        this.dnsRoleAlias = dnsRoleAlias;
        fireChangeEvent();
    }

    /**
     * Adds a subordinate iff both this opfac and the subordinate
     * are members of the same plan and subordinate != this opfac.
     */
    public void addSubordinate( Opfac subordinate )
    {
        // note: guard against circular reference because equals will
        // loop indefinitely if an opfac's commander is set to self
        if ( (plan == null) || (subordinate == null) || (subordinate == this)
             || !plan.contains(subordinate) )
            return;
        
        subordinates.add( subordinate );
        
        // setCommander() and addSubordinate() call each other, so 
        // guard against infinite loop
        if ( subordinate.getCommander() != this )
            subordinate.setCommander( this );
        
        fireChangeEvent();
    }

    public void removeSubordinate( Opfac subordinate )
    {
        subordinates.remove( subordinate );        
        subordinate.setCommander( null );
        fireChangeEvent();
    }

    /**
     * Sets the commander iff both this opfac and the commander
     * are members of the same plan and commander != this opfac.
     */
    void setCommander( Opfac commander )
    {
        // note: guard against circular reference because equals will
        // loop indefinitely if an opfac's commander is set to self
        if( (plan == null) || (commander == this) 
            || (commander != null && !plan.contains(commander) ) )
            return;

        this.commander = commander;
        
        // setCommander() and addSubordinate() call each other, so 
        // guard against infinite loop
        if ( commander != null && !commander.subordinates().contains(this) )
            commander.addSubordinate( this );

        fireChangeEvent();
    }

    /**
     * Returns the Opfacs who report to this Opfac in an unmodifiable
     * Collection.
     */
    public Collection subordinates()
    {
        return Collections.unmodifiableCollection( subordinates );
    }

    /**
     * Implements method in PlanObject by returning the unitName.
     */
    public String getName()
    {
        return unitName;
    }

    public Integer getObserverID()
    {
        return observerID;
    }

    /**
     * Returns this opfac's commander.  Returns null when this opfac does
     * not have a commander.
     */
    public Opfac getCommander()
    {
        return commander;
    }

    public OpfacDevice getDevice()
    {
        return device;
    }

    public String getTacfireAlias()
    {
        return tacfireAlias;
    }

    public Integer getUnitNumber()
    {
        return unitNumber;
    }

    public Integer getUnitReferenceNumber()
    {
        return unitReferenceNumber;
    }

    public boolean isUnitReferenceNumberPkg11()
    {
        return unitReferenceNumberPkg11;
    }

    public String getUnitName()
    { 
        return unitName;
    }    

    public UnitType getUnitType()
    {
        return unitType;
    }   
 
    public String getMachineName()
    {
        return machineName;
    }  

    public String getBumperNumber()
    {
        return bumperNumber;
    }

    public String getRole()
    { 
        return role;
    } 
  
    public String getUIC()
    {
        return uic;
    }  
   
    public EchelonType getEchelon()
    {
        return echelon;
    } 

    public String getOrName()
    {
        return orName;
    }  

    public String getDnsRoleAlias()
    {
        return dnsRoleAlias;
    }

    public void saveToDatabase( java.sql.Connection conn )
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return (unitName != null && role != null
                && (commander != null || subordinates.size() > 0) );
    }

    public boolean equals( Object obj )
    {   
        if ( this == obj )
            return true;
        if ( obj == null || !(obj instanceof Opfac) )
            return false;

        /**
         * cannot include both the commander and subordinates.  including
         * both fields in a comparison results in an endless loop.
         */
        Opfac o2 = (Opfac) obj;
        boolean result =
            ((unitNumber == null ? o2.unitNumber == null :
              unitNumber.equals( o2.unitNumber))
            
             && (unitReferenceNumber == null ? o2.unitReferenceNumber == null :
                 unitReferenceNumber.equals( o2.unitReferenceNumber))

             && (unitReferenceNumberPkg11 == o2.unitReferenceNumberPkg11)
            
             && (tacfireAlias == null ? o2.tacfireAlias == null :
                 tacfireAlias.equals( o2.tacfireAlias))
            
             && (commander == null? o2.commander == null :
                 commander.equals( o2.commander))

             && (device == null ? 
                 o2.device == null : device.equals( o2.device))
             
             && (unitName  == null ? 
                 o2.unitName == null : unitName.equals( o2.unitName))  
 
             && (unitType == null ? 
                 o2.unitType == null :unitType.equals(o2.unitType))

             && (machineName == null ? 
                 o2.machineName == null : machineName.equals( o2.machineName)) 
             
             && (bumperNumber == null ? 
                 o2.bumperNumber == null: bumperNumber.equals(o2.bumperNumber))

             && (observerID == null ? 
                 o2.observerID == null: observerID.equals(o2.observerID))

             && (role == null ? o2.role == null : role.equals( o2.role))   

             && (uic == null ? o2.uic == null : uic.equals( o2.uic))   
  
             && (echelon == null ? 
                 o2.echelon == null : echelon.equals( o2.echelon)) 

             && (orName == null ? o2.orName == null : orName.equals(o2.orName))

             && (dnsRoleAlias == null ? 
                 o2.dnsRoleAlias == null: dnsRoleAlias.equals( o2.dnsRoleAlias)));

        return result;
    }

    public String toString()
    {
        return "( unitName=" + unitName + ", role=" + role + ") ";
    }

    public DataContainer toContainer()
    {
        return toContainer( OPFAC );
    }

    public DataContainer toContainer( String label )
    {
        DataContainer cont = new DataContainer( label );
        String cmdrUnitName = (commander==null? "": commander.getUnitName() );
        int cmdrID = (commander==null? 0: commander.getID() ); 
        cont.addField( new DataContainer( OPFAC_ID, "" + getID() ) );
        cont.addField( new DataContainer( TACFIRE_ALIAS, tacfireAlias ) );
        cont.addField( new DataContainer( UNIT_NUMBER, unitNumber == null ?
                                          null : unitNumber.toString() ) );
        cont.addField( new DataContainer
            ( UNIT_REFERENCE_NUMBER, unitReferenceNumber == null ?
              null : unitReferenceNumber.toString() ) );
        cont.addField( new DataContainer
            ( UNIT_REFERENCE_NUMBER_PKG11, "" + unitReferenceNumberPkg11) );
        cont.addField( new DataContainer( DEVICE, device.toString() ) );
        cont.addField( new DataContainer( UNIT_NAME, unitName ) );
        cont.addField( new DataContainer( UNIT_TYPE, unitType == null ? null :
                                          unitType.toString() ) );
        cont.addField( new DataContainer( MACHINE_NAME, machineName ) );
        cont.addField( new DataContainer( BUMPER_NUMBER, bumperNumber ) );
        cont.addField( new DataContainer( OBSERVER_ID, observerID == null ? 
                                          null: observerID.toString() ) );
        cont.addField( new DataContainer( ROLE, role ) );
        cont.addField( new DataContainer( UIC, uic ) );
        cont.addField( new DataContainer( ECHELON, echelon == null ? null : 
                                          echelon.toString() ) );
        cont.addField( new DataContainer( OR_NAME, orName ) );
        cont.addField( new DataContainer( DNS_ROLE_ALIAS, dnsRoleAlias ) );
        cont.addField( new DataContainer( COMMANDER_OPFAC_ID, "" + cmdrID ) );
        cont.addField( new DataContainer( COMMANDER_UNIT_NAME, cmdrUnitName) );

        DataContainer subordinatesCont = new DataContainer( SUBORDINATES );
        Iterator iter = subordinates().iterator();
        int i = 0;
        while( iter.hasNext() )
            {
                Opfac subord = (Opfac) iter.next();
                String subordLabel = subord.getName() +  "____" + i;
                DataContainer subordCont = new DataContainer( subordLabel );
                subordCont.addField
                    ( new DataContainer( OPFAC_ID, "" + subord.getID() ) );
                subordCont.addField
                    ( new DataContainer( UNIT_NAME, subord.getUnitName() ) );
                subordinatesCont.addField( subordCont );
                i++;
            }
        cont.addField( subordinatesCont );
        return cont;
    }


    void initializeFromContainer( DataContainer cont )
        throws DataContainerFormatException
    {
        String idStr = cont.getField( OPFAC_ID ).getValue();
        if ( idStr != null ) {
            try {
                setID( Integer.parseInt( idStr ) );
            } catch( NumberFormatException e ) {
                Log.error( Log.PLAN,  e );
                throw new DataContainerFormatException();
            }
        }

        String unitNumStr = cont.getField( UNIT_NUMBER ).getValue();
        if ( unitNumStr != null ) {
            try {
                setUnitNumber( new Integer( unitNumStr ) );
            } catch( NumberFormatException e ) {
                Log.error( Log.PLAN, e );
            }
        }

        String urnStr = cont.getField( UNIT_REFERENCE_NUMBER ).getValue();
        if ( urnStr != null ) {
            try {
                setUnitReferenceNumber( new Integer( urnStr ) );
            } catch( NumberFormatException e ) {
                Log.error( Log.PLAN, e );
            }
        }

        String urnPkg11Str = 
            cont.getField( UNIT_REFERENCE_NUMBER_PKG11 ).getValue();
        if ( urnPkg11Str != null ) {
             setUnitReferenceNumberPkg11
                 ( urnPkg11Str.equalsIgnoreCase( "T" )
                   || urnPkg11Str.equalsIgnoreCase( "TRUE" ) );
        }
        
        String observerIDStr = cont.getField( OBSERVER_ID ).getValue();
        if ( observerIDStr != null ) {
            try {
                setObserverID( new Integer( observerIDStr ) );
            } catch( NumberFormatException e ) {
                Log.error( Log.PLAN, e );
            }
        }
        
        String deviceStr = cont.getField( DEVICE ).getValue();
        if ( deviceStr != null ) {
            try {
                setDevice( OpfacDevice.fromString( deviceStr ) );
            } catch( OpfacDeviceStringException e ) {
                Log.error( Log.PLAN, e );
                setDevice( OpfacDevice.UNKNOWN );
            }
        }
        
        String unitTypeStr = cont.getField( UNIT_TYPE ).getValue();
        if ( unitTypeStr != null ) {
            try {
                setUnitType( UnitType.fromString( unitTypeStr ) );
            } catch( UnitTypeStringException e ) {
                Log.error( Log.PLAN, e );
            }
        }

        String echelonTypeStr = cont.getField( ECHELON ).getValue();
        if ( echelonTypeStr != null ) {
            try {
                setEchelon( EchelonType.fromString( echelonTypeStr ) );
            } catch( EchelonTypeStringException e ) {
                Log.error( Log.PLAN, "echelonType", e );
            }
        }   
     
        setTacfireAlias( cont.getField( TACFIRE_ALIAS ).getValue() );

        setUnitNameRole( cont.getField( UNIT_NAME ).getValue(),
                         cont.getField( ROLE ).getValue() );

        setMachineName( cont.getField( MACHINE_NAME ).getValue() );

        setBumperNumber( cont.getField( BUMPER_NUMBER ).getValue() );

        setUIC( cont.getField( UIC ).getValue() );

        setOrName( cont.getField( OR_NAME ).getValue() );

        setDnsRoleAlias( cont.getField( DNS_ROLE_ALIAS ).getValue());        
    }

    void initializeCommanderFromContainer( DataContainer cont )
        throws DataContainerFormatException
    {
        int commanderID = 0;
        int subordinateID = 0;
        try {
            commanderID = 
                Integer.parseInt
                ( cont.getField( COMMANDER_OPFAC_ID ).getValue("-1") );
        } catch( NumberFormatException e ) {
            Log.error( Log.PLAN, e );
            throw new DataContainerFormatException();
        }
        setCommander( plan.getOpfac( commanderID ) );
    }

}
