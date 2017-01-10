package fsats.plan;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The interface for observing a Plan.
 * @see PlanChangeAdapter
 */
public interface PlanChangeListener
{
    /**
     * Called on any change to the plan.
     * e.getSource() is the plan instance.
     * e.getType() is always PlanChangeEvent.MODIFIED.
     */
    public void planChanged( PlanChangeEvent e );

    /**
     * Called when nets are added, removed or modified.
     * e.getSource() is the Net that was added, removed or modified.
     */
    public void netListChanged( PlanChangeEvent e);

    /**
     * Called when a node is added, removed or modified.
     * e.getSource() is the Node that was added, removed or modified.
     */
    public void nodeListChanged( PlanChangeEvent e );

    /**
     * Called when a opfac is added, removed or modified.
     * e.getSource() is the Opfac that was added, removed or modified.
     */
    public void opfacListChanged( PlanChangeEvent e );

    /**
     * Called when a processor is added, removed or modified.
     * e.getSource() is the processor that was added, removed or modified.
     */
    public void processorListChanged( PlanChangeEvent e );

    /**
     * Called when a port is added, removed or modified.
     * e.getSource() is the port that was added, removed or modified.
     */
    public void portListChanged( PlanChangeEvent e );

    /**
     * Called when an Opfac is added to or removed from a net.
     * e.getSource() is the OpfacNetTuple that was added, removed or modified.
     */
    public void opfacToNetListChanged( PlanChangeEvent e );

    /**
     * Called when a Processor-Node association is added or removed.
     * e.getSource() is the ProcessorNodeTuple that was added or removed.
     */
    public void processorNodeAssociationChanged( PlanChangeEvent e );

}
