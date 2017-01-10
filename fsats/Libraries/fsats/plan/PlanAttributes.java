package fsats.plan;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import fsats.measures.UTMLocation;
import fsats.measures.Datum;
import fsats.util.DataContainer;
import fsats.util.Log;

/**
 * High level attributes of a plan.
 */
public class PlanAttributes extends PlanObject implements PlanContainerLabels
{
    private ChangeEventMulticaster multicaster = new ChangeEventMulticaster();
    private String name = "Unknown";
    private Datum datum = Datum.WGS_84;
    private String description;
    private UTMLocation lowerLeftMapBoundary = new UTMLocation( 0, 0, 0 );
    private UTMLocation upperRightMapBoundary = new UTMLocation( 0, 0, 0 );
    private Date startTime = new Date( 0 );
    private int planID;
    private Plan plan;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat();


    public PlanAttributes( String name, Plan plan )
    {
        this.name = name;
        this.plan = plan;
    }

    public PlanAttributes( DataContainer container, Plan plan )
    {
        initFromContainer( container );
        this.plan = plan;
    }

    void saveToDatabase( java.sql.Connection connection )
    {
    }

    /**
     * Sets the plan name.
     */
    public void setName( String name )
    {
        this.name = name;
        fireChangeEvent();
    }

    public void setPlanID( int planID )
    {
        this.planID = planID;
    }

    /**
     * Sets the plan description.
     */
    public void setDescription( String description )
    {
        this.description = description;
        fireChangeEvent();
    }

    /**
     * Sets the datum.
     */
    public void setDatum( Datum datum )
    {
        this.datum = (datum == null? Datum.WGS_84: datum);
    }


    /**
     * Set the boundaries of the exercise area.
     */
    public void setMapBoundary( UTMLocation lowerLeft, UTMLocation upperRight )
    {
        this.lowerLeftMapBoundary = 
            (lowerLeft == null? new UTMLocation( 0, 0, 0 ) : lowerLeft );
        this.upperRightMapBoundary =
            (upperRight == null? new UTMLocation( 0, 0, 0 ) : upperRight );

        fireChangeEvent();
    }

    public UTMLocation getLowerLeftMapBoundary()
    {
        return lowerLeftMapBoundary;
    }

    public UTMLocation getUpperRightMapBoundary()
    {
        return upperRightMapBoundary;
    }

    /**
     * Set the time when data collection is to begin.
     */
    public void setStartTime( Date startTime )
    {
        this.startTime = (startTime == null? new Date(0) : startTime);
        fireChangeEvent();
    }

    /**
     * Returns the plan name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the plan ID.
     */
    public int getPlanID()
    {
        return planID;
    }

    /**
     * Returns the plan description.
     */
    public String getDescription()
    {
        return description;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    /**
     * Returns this plan's Datum.
     */
    public Datum getDatum()
    {
        return datum;
    }

    /**
     * Implements Verifiable.  Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return true;
    }

    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null || !(obj instanceof PlanAttributes) )
            return false;
        PlanAttributes p = (PlanAttributes) obj;
        return ( (name==null? p.name==null: name.equals( p.name ) )
                 && datum.equals( p.datum )
                 && lowerLeftMapBoundary.equals( p.lowerLeftMapBoundary )
                 && upperRightMapBoundary.equals( p.upperRightMapBoundary )
                 && startTime.equals( p.startTime ) );
    }

    public DataContainer toContainer()
    {
        return toContainer( PLAN_ATTRIBUTES );
    }

    public DataContainer toContainer( String label )
    {

        DataContainer cont = new DataContainer( label );
        cont.addField( new DataContainer( PLAN_NAME, name ) );
        cont.addField( new DataContainer( DESCRIPTION, description ) );
        cont.addField( new DataContainer(  DATUM, datum.toString() ) );
        DataContainer lowerLeftCont = 
            new DataContainer( LOWER_LEFT_MAP_BOUNDARY );
        lowerLeftCont.addField( lowerLeftMapBoundary.toContainer() );
        cont.addField( lowerLeftCont );
        DataContainer upperRightCont = 
            new DataContainer(UPPER_RIGHT_MAP_BOUNDARY);
        upperRightCont.addField( upperRightMapBoundary.toContainer() );
        cont.addField( upperRightCont );
        String startTimeStr = dateFormatter.format( startTime );
        cont.addField( new DataContainer( START_TIME, startTimeStr ) );
        return cont;
    }

    public void initFromContainer( DataContainer container )
    {
        setName( container.getField( PLAN_NAME ).getValue( "Unknown" ) );
        setDescription( container.getField( DESCRIPTION ).getValue() );
        DataContainer utmCont = container.getField( LOWER_LEFT_MAP_BOUNDARY );
        UTMLocation lowerLeft = UTMLocation.create( utmCont.getField( 1 ) );
        utmCont = container.getField( UPPER_RIGHT_MAP_BOUNDARY );
        UTMLocation upperRight = UTMLocation.create( utmCont.getField( 1 ) );
        setMapBoundary( lowerLeft, upperRight );
        String timeStr =  container.getField( START_TIME ).getValue();
        try {
            setStartTime( dateFormatter.parse( timeStr ) );
        } catch( java.text.ParseException e ) {
            Log.error( "PLAN", "Error reading start time.", e );
        }
    }

}
