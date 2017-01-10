package fsats.plan;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A convenience class for creating PlanChangeListener classes.
 * All methods are empty.
 */
public class PlanChangeAdapter implements PlanChangeListener
{

    public void planChanged( PlanChangeEvent e )
    {}

    public void netListChanged( PlanChangeEvent e)
    {}

    public void nodeListChanged( PlanChangeEvent event )
    {}

    public void opfacListChanged( PlanChangeEvent e )
    {}

    public void processorListChanged( PlanChangeEvent e )
    {}

    public void portListChanged( PlanChangeEvent e )
    {}

    public void opfacToNetListChanged( PlanChangeEvent e )
    {}

    public void processorNodeAssociationChanged( PlanChangeEvent e )
    {}

}
