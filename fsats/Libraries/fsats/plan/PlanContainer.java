package fsats.plan;

import java.util.Iterator;
import java.util.StringTokenizer;
import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;
import fsats.util.Log;



class PlanContainer implements PlanContainerLabels
{
    OpfacLoader opfacLoader = new OpfacLoader();
    NetLoader netLoader = new NetLoader();
    OpfacToNetLoader tupleLoader = new OpfacToNetLoader();
    NodeLoader nodeLoader = new NodeLoader();
    ProcessorLoader processorLoader = new ProcessorLoader();
    PortCollectionLoader portLoader = new PortCollectionLoader();
    ProcessorNodeTupleLoader processorToNodeLoader = 
        new ProcessorNodeTupleLoader();

    /**
     * Returns a container representation of plan.
     */
    DataContainer planToContainer( Plan plan )
    {
        DataContainer cont = new DataContainer( PLAN );
        cont.addField( plan.getPlanAttributes().toContainer() );
        cont.addField( toContainer( plan.opfacs().iterator(), OPFACS) );
        cont.addField( toContainer( plan.nets().iterator(), NETS) );
        cont.addField( toContainer( plan.opfacNetTuples().iterator(), 
                                    OPFAC_NET_TUPLES)  );
        cont.addField( toContainer( plan.nodes().iterator(), NODES) );
        cont.addField( toContainer( plan.processors().iterator(), 
                                    PROCESSORS) );
        cont.addField( toContainer( plan.ports().iterator(), PORTS) );
        cont.addField( toContainer( plan.processorNodeTuples().iterator(), 
                                    PROCESSOR_NODE_TUPLES));
        return cont;
    }

    private DataContainer toContainer(Iterator iter, String label )
    {
        DataContainer collectionCont = new DataContainer( label );
        while( iter.hasNext() )
            {
                PlanObject planObject = (PlanObject) iter.next();
                String objLabel = "" + planObject.getID();
                collectionCont.addField( planObject.toContainer( objLabel) );
            }
        return collectionCont;
    }

    /**
     * Loads the contents of a container representation of a plan into a plan.
     */
    void containerToPlan( DataContainer container, Plan plan )
        throws DataContainerFormatException
    {
        DataContainer attrsCont = container.getField( PLAN_ATTRIBUTES );
        PlanAttributes attrs = new PlanAttributes( attrsCont, plan );
        plan.setPlanAttributes( attrs );
        /**
         * the following calls have order dependencies.
         * opfacs and nets must be loaded before opfacNetTuples.
         * nodes must be loaded before processors.
         * processors must be loaded before ports.
         */ 
        opfacLoader.loadContainer( container.getField(OPFACS), plan );
        netLoader.loadContainer( container.getField(NETS), plan );
        tupleLoader.loadContainer( container.getField(OPFAC_NET_TUPLES), plan);
        processorLoader.loadContainer( container.getField(PROCESSORS), plan );
        nodeLoader.loadContainer( container.getField(NODES), plan );
        portLoader.loadContainer( container.getField(PORTS), plan );
        processorToNodeLoader.loadContainer
            ( container.getField(PROCESSOR_NODE_TUPLES), plan );
    }

    /**
     * Template for taking a DataContainer which contains a collection of the
     * same subclass of PlanObjects, creating the objects and adding them
     * to a plan.
     */
    private abstract class CollectionLoader
    {
        /**
         * Adds the container, a collection of plan objects, to the Plan.
         */
        void loadContainer( DataContainer collecCont, Plan plan )
            throws DataContainerFormatException
        {
            DataContainer objectCont = null;
            PlanObject planObject = null;
            int fieldCnt = collecCont.getFieldCount();
            for ( int i = 1; i <= fieldCnt; i++ ) {
                objectCont = collecCont.getField( i );
                try {
                    loadObject( objectCont, plan );
                } catch( Throwable t ) {
                    Log.error( Log.PLAN, t );
                }
            }
        }

        /**
         * Add the container representation of a PlanObject to the plan.
         */
        abstract void loadObject( DataContainer cont, Plan plan )
            throws DataContainerFormatException;
    }
    
    private class OpfacLoader extends CollectionLoader
    {
        void loadContainer( DataContainer collecCont, Plan plan )
            throws DataContainerFormatException
        {
            super.loadContainer( collecCont, plan );
            setCommandRelations( collecCont, plan );            
        }

        /**
         * Set the command relations between the opfacs in this plan to what 
         * is represented in the container.
         */
        void setCommandRelations( DataContainer opfacsCont, Plan plan )
            throws DataContainerFormatException
        {
            int fieldCnt = opfacsCont.getFieldCount();
            for ( int i = 1; i <= fieldCnt; i++ )
                {
                    DataContainer opfacCont = opfacsCont.getField( i );
                    int opfacID = Integer.parseInt
                        ( opfacCont.getField( OPFAC_ID ).getValue("-1"));
                    Opfac opfac = plan.getOpfac( opfacID );
                    if ( opfac != null )
                        opfac.initializeCommanderFromContainer( opfacCont );
                }
        }

        void loadObject( DataContainer cont, Plan plan ) 
            throws DataContainerFormatException
        {
            new Opfac( cont, plan );
        }
    }
    
    private class NetLoader extends CollectionLoader
    {
        void loadObject( DataContainer cont, Plan plan ) 
            throws DataContainerFormatException
        {
            new Net( cont, plan );
        }
    }
    
    private class NodeLoader extends CollectionLoader
    {
        void loadObject( DataContainer cont, Plan plan ) 
            throws DataContainerFormatException
        {
            new Node( cont, plan );
        }
    }
    
    private class ProcessorLoader extends CollectionLoader
    {
        void loadObject( DataContainer cont, Plan plan ) 
            throws DataContainerFormatException
        {
            new Processor( cont, plan );
        }
    }
    
    private class PortCollectionLoader extends CollectionLoader
    {
        void loadObject( DataContainer cont, Plan plan ) 
            throws DataContainerFormatException
        {
            new Port( cont, plan );
        }
    }
    
    private class OpfacToNetLoader extends CollectionLoader
    {
        void loadObject( DataContainer cont, Plan plan ) 
            throws DataContainerFormatException
        {
            new OpfacNetTuple( cont, plan );
        }
    }
    
    private class ProcessorNodeTupleLoader extends CollectionLoader
    {
        void loadObject( DataContainer cont, Plan plan ) 
            throws DataContainerFormatException
        {
            new ProcessorNodeTuple( cont, plan );
        }
    }
    
} 
