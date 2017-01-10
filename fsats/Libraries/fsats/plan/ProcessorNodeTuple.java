package fsats.plan;

import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;

public class ProcessorNodeTuple extends PlanObject 
    implements PlanContainerLabels
{

    private Processor proc;
    private Node node;

    /**
     * @throw IllegalArgumentException if processor or node is null.
     */
    public ProcessorNodeTuple( Processor proc, Node node )
    {
        if ( proc==null || node==null )
            throw new IllegalArgumentException();
        this.node=node;
        this.proc=proc;
    }

    /**
     * @throw IllegalArgumentException if unable to get a valid Processor
     * and Node from the container.
     */
    ProcessorNodeTuple( DataContainer cont, Plan plan )
        throws DataContainerFormatException
    {
        initFromContainer( cont, plan );
        plan.addProcessorNodeTuple( this );
    }

    public Processor getProcessor()
    {
        return proc;
    }

    public Node getNode()
    {
        return node;
    }

    String getName()
    {
        return node.getName() + "_" + proc.getName();
    }

    public boolean isComplete()
    {
        return true;
    }

    public DataContainer toContainer()
    {
        return toContainer( PROCESSOR_NODE_TUPLE );
    }

    public DataContainer toContainer( String label )
    {
        DataContainer cont = new DataContainer( label );
        cont.addField( new DataContainer( PROCESSOR_NAME, proc.getName() ) );
        cont.addField( new DataContainer( PROCESSOR_ID, "" + proc.getID() ) );
        cont.addField( new DataContainer( NODE_NAME, node.getName() ) );
        cont.addField( new DataContainer( NODE_ID, "" + node.getID() ) );
        return cont;
    }

    private void initFromContainer( DataContainer cont, Plan plan )
        throws DataContainerFormatException
    {
        int procID = Integer.parseInt
            ( cont.getField(PROCESSOR_ID).getValue("-1") ) ;
        proc = plan.getProcessor( procID );
        int nodeID = Integer.parseInt( cont.getField(NODE_ID).getValue("-1") );
        node = plan.getNode( nodeID );
        if ( proc == null || node == null )
            throw new IllegalArgumentException();
    }

}
