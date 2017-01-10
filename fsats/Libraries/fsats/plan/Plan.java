package fsats.plan;

import java.io.*;
import java.util.*;
import java.sql.SQLException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import fsats.db.PlanUtils;
import fsats.util.FsatsProperties;
import fsats.util.Log;
import fsats.util.ManyToOneMap;
import fsats.util.ReadOnlyIterator;
import fsats.util.DataContainer;
import fsats.util.DataContainerFormatException;


/**
 * The data elements which comprise an instrumented exercise/test.
 */
public class Plan 
{
    private PlanContainer planContainer = new PlanContainer();
    private List planChangeListeners = new ArrayList();
    private PlanAttributes planAttributes = 
        new PlanAttributes( "unknown" + new Date().getTime(), this );
    private NetCollection netCollec = new NetCollection();
    private NodeCollection nodeCollec = new NodeCollection();
    private OpfacCollection opfacCollec = new OpfacCollection();
    private ProcessorCollection processorCollec = new ProcessorCollection();
    private PortCollection portCollec = new PortCollection();
    private OpfacToNetCollection opfacToNetCollec = new OpfacToNetCollection();
    private ProcessorToNodeCollection procToNodeCollec = 
        new ProcessorToNodeCollection();

    private ChangeListener attributeListener = 
        new ChangeListener() {
                public void stateChanged( ChangeEvent e )
                {
                Plan.this.firePlanChanged();
                }
            };
    
    
    /**
     * Creates a new plan with the specified name.
     */
    public Plan( String name ) 
    {
        planAttributes.setName( name );
    }

    /**
     * Creates a new plan and initializes it from the DataContainer.
     */
    public Plan( DataContainer container ) throws DataContainerFormatException
    {
        planContainer.containerToPlan( container, this );
    }

    /**
     * Writes this plan as a DataContainer to file $FSATS_HOME/plans/planName
     * where planName == this.getPlanAttributes().getName()
     */
    public void saveToFile() throws IOException
    {
        String fileName = FsatsProperties.get( FsatsProperties.FSATS_HOME,
                                               "/usr/fsats" );
        fileName = fileName + "/plans/" + planAttributes.getName();
        saveToFile( fileName );
    }

    /**
     * Writes this plan as a DataContainer to the specified file.
     */
    public void saveToFile( String fileName ) throws IOException
    {
        FileWriter fwriter = new FileWriter( fileName );
        PrintWriter pwriter = new PrintWriter( fwriter );
        DataContainer container = toContainer();
        container.print( pwriter );
        pwriter.close();
    }


    void setPlanAttributes( PlanAttributes planAttributes )
    {
        if ( planAttributes == null )
            return;
        this.planAttributes.removeChangeListener( attributeListener );
        this.planAttributes = planAttributes;
        this.planAttributes.addChangeListener( attributeListener );
    }

    /**
     * Returns planAttributes for this plan.
     */
    public PlanAttributes getPlanAttributes()
    {
        return planAttributes;
    }


    //////////////////////////////////////////////////////////////////////////


    /**
     * Adds the node to the plan iff there is no other node with the
     * same name.  NodeListChanged() is called on registered 
     * PlanChangeListeners.
     * @return true if the node was added, false otherwise.
     */
    public boolean addNode( Node node )
    {
        if ( node == null || containsNode( node.getName() ) )
            return false;
        nodeCollec.add( node );
        return true;
    }

    /**
     * Removes the specified node from the collection.
     * Call nodeListChanged() in registered PlanChangeListeners.
     */
    public void removeNode( Node node )
    {
        if ( node == null || !nodeCollec.contains( node ) )
            return;
        removeProcessorNodeTuples( node );
        nodeCollec.remove( node );
    }

    public Node getNode( String name )
    {
        return (Node) nodeCollec.getNode( name );
    }

    Node getNode( int id )
    {
        return (Node) nodeCollec.get( id );
    }

    /**
     * Tests if this plan contains the specified node.
     */
    public boolean contains( Node node )
    {
        return nodeCollec.contains( node );
    }

    /**
     * Tests for the presence of a node with the specified name.
     */
    public boolean containsNode( String name )
    {
        return nodeCollec.containsNodeWithName( name );
    }

    /**
     * Returns a read-only collection of the nodes.
     */
    public Collection nodes()
    {
        return nodeCollec.collection();
    }


    //-------------------------------------------------------------------


    /**
     * Adds a new Net to the plan iff there is no other net with the same
     * name. NetListChanged() is called on registered PlanChangeListeners.
     * @return true if the net was added, false otherwise.
     */
    public boolean addNet( Net net )
    {
        if ( net == null || containsNet(net.getName()) )
            return false;
        netCollec.add( net );
        return true;
    }    

    /**
     * Removes the specified net from the plan.
     * Detachs all opfacs from the net and removes the net from
     * any associated ports.
     * NetListChanged() called in registered PlanChangeListeners.
     */
    public void removeNet( Net net )
    {
        if ( !netCollec.contains( net ) )
            return;
        portCollec.clearReferencesToNet( net );
        removeOpfacNetTuples( net );
        netCollec.remove( net );
    }

    public Net getNet( String netName )
    {
        return netCollec.getNet( netName );
    }

    Net getNet( int id )
    {
        return (Net) netCollec.get( id );
    }

    /**
     * Tests if this plan contains the specified net.
     */
    public boolean contains( Net net )
    {
        return netCollec.contains( net );
    }

    /**
     * Tests for the presence of a net with the specified name.
     */
    public boolean containsNet( String name )
    {
        return netCollec.containsNetWithName( name );
    }

    /**
     * Returns a read-only collection of the nets.
     */
    public Collection nets()
    {
        return netCollec.collection();
    }


    //-------------------------------------------------------------------


    /**
     * Adds a new opfac to the plan iff no other opfac has the same 
     * unitName and role, the same unitNumber or the same tacfireAlias. 
     * OpfacListChanged() is called in registered PlanChangeListeners.
     * @return true if the opfac added, false otherwise.
     */
    public boolean addOpfac( Opfac opfac )
    {
        if ( opfac == null 
             || containsOpfac( opfac.getUnitName(), opfac.getRole() )
             || containsUnitNumber( opfac.getUnitNumber() )
             || containsTacfireAlias( opfac.getTacfireAlias() ) )
            return false;
        opfacCollec.add( opfac );
        return true;
    }

    /**
     * Removes the specified opfac and all subordinates.  Also removes 
     * all OpfacNetTuples containing the opfac.  Calls PlanChangeEvent.
     */
    public void removeOpfac( Opfac opfac )
    {
        if ( !opfacCollec.contains( opfac ) )
            return;
        Opfac commander = opfac.getCommander();
        if ( commander != null )
            commander.removeSubordinate( opfac );
        Iterator iter = opfac.subordinates().iterator();
        while( iter.hasNext() )
            {
                Opfac subordinate = (Opfac) iter.next();
                removeOpfac( subordinate );
            }
        removeOpfacNetTuples( opfac );
        opfacCollec.remove( opfac );
    }

    /**
     * Test for the presence of an opfac with the specified unitName and role.
     */
    public boolean containsOpfac( String unitName, String role )
    {        
        return opfacCollec.containsOpfac( unitName, role );
    }

    /**
     * Test for the presence of an opfac with the specified unitNumber.
     */
    public boolean containsUnitNumber( Integer unitNumber )
    {        
        return opfacCollec.containsUnitNumber( unitNumber );
    }

    /**
     * Test for the presence of an opfac with the specified 
     * Unit Reference Number.
     */
    public boolean containsUnitReferenceNumber( Integer urn )
    {        
        return opfacCollec.containsUnitReferenceNumber( urn );
    }

    /**
     * Test for the presence of an opfac with the specified tacfireAlias.
     */
    public boolean containsTacfireAlias( String tacfireAlias )
    {      
        return opfacCollec.containsTacfireAlias( tacfireAlias );
    }

    /**
     * Tests if this plan contains the specified opfac.
     */
    public boolean contains( Opfac opfac )
    {
        return opfacCollec.contains( opfac );
    }

    /**
     * Returns the Opfac with the specified unitName and role.
     */    
    public Opfac getOpfac( String unitName, String role )
    {
        return opfacCollec.getOpfac( unitName, role );
    }

    public Opfac getOpfac( int id )
    {
        return (Opfac) opfacCollec.get( id );
    }

    /**
     * Returns a read-only collection of the opfacs.
     */
    public Collection opfacs()
    {
        return opfacCollec.collection();
    }


    /**
     * Returns in a read-only colleciton, those units which don't have 
     * a commander.
     */
    public Collection getTopLevelUnits()
    {
        List topLevelUnits = new ArrayList();
        for (Iterator i = opfacCollec.collection().iterator(); i.hasNext(); )
        {
            Opfac o = (Opfac) i.next();
            if (o.getCommander() == null || o.getCommander() == o)
                topLevelUnits.add(o);
        }
        return Collections.unmodifiableCollection( topLevelUnits );
    }

    //-------------------------------------------------------------------


    /**
     * Adds the processor to this plan iff there is no other processor with
     * the same name.
     */
    public boolean addProcessor( Processor proc )
    {  
        if ( proc == null || containsProcessor( proc.getName() ) )
            return false;
        processorCollec.add( proc );
        return true;
    }

    /**
     * Removes the processor and all ports associated with the processor.
     */
    public void removeProcessor( Processor processor )
    {
        portCollec.removePortsContainingProcessor( processor );
        removeProcessorNodeTuple( processor );
        processorCollec.remove( processor );
    }

    /**
     * Tests if this plan contains the specified processor.
     */
    public boolean contains( Processor processor )
    {
        return processorCollec.contains( processor );
    }

    /**
     * Tests for the presence of a processor with the specified name.
     */
    public boolean containsProcessor( String name )
    {
        return ( getProcessor( name ) != null );
    }

    /**
     * Returns the procesor with the specified name, null if not found.
     */
    public Processor getProcessor( String name )
    {
        return processorCollec.getProcessor( name );
    }

    Processor getProcessor( int id )
    {
        return (Processor) processorCollec.get( id );
    }

    /**
     * Returns a read-only collection of the processors.
     */
    public Collection processors()
    {
        return processorCollec.collection();
    }


    //-------------------------------------------------------------------


    /**
     * Adds the port to this plan iff there is no other port with the
     * same processor and portName.
     * @return true if the port added, false otherwise.
     */
    public boolean addPort( Port port )
    {
        if ( port == null
             || containsPort( port.getProcessor(), port.getPortName() ) )
            return false;
        portCollec.add( port );
        return true;
    }

    /**
     * Removes the port.
     */
    public void removePort( Port port )
    {
        portCollec.remove( port );
    }

    public boolean containsPort( Processor processor, PortName portName )
    {
        return portCollec.containsPort( processor, portName );
    }

    public boolean contains( Port port )
    {
        return portCollec.contains( port );
    }

    /**
     * Returns a read-only collection of the ports.
     */
    public Collection ports()
    {
        return portCollec.collection();
    }

    /**
     * Returns a read-only collection of those ports which have the net.
     */
    public Collection ports( Net net )
    {
        return portCollec.portsContainingNet( net );
    }

    /**
     * Returns a read-only collection of those ports which have the processor.
     */
    public Collection ports( Processor p )
    {
        return portCollec.portsContainingProcessor( p );
    }


    //-------------------------------------------------------------------


    /**
     * Associates an opfac with a net iff the opfac and net are both in
     * the plan, the opfac is not allready on the net and the net-address 
     * is not allready in use.
     */
    public boolean addOpfacNetTuple( OpfacNetTuple tuple ) 
    {
        if ( tuple == null )
            return false;
        Opfac opfac = tuple.getOpfac();
        Net net = tuple.getNet();
        String address = tuple.getAddress();
        if ( !contains(opfac) || !contains(net)
             || isOpfacOnNet( opfac, net ) || isAddressInUse( net, address ) )
            return false;
        opfacToNetCollec.add( tuple );
        return true;
    }

    /**
     * Tests if this plan contains the specified tuple.
     */
    public boolean contains( OpfacNetTuple tuple )
    {
        return opfacToNetCollec.contains( tuple );
    }

    /**
     * Returns the OpfacNetTuple with the opfac and net, null if not present.
     */
    public OpfacNetTuple getOpfacNetTuple( Opfac opfac, Net net )
    {
        return opfacToNetCollec.getTuple( opfac, net );
    }

    /**
     * Returns true if the opfac is associated with the net, false otherwise.
     */
    public boolean isOpfacOnNet( Opfac opfac, Net net )
    {
        return opfacToNetCollec.containsTupleWithOpfacNet( opfac, net );
    }

    /**
     * Returns true if an opfac is on the net with the specified address.
     */
    public boolean isAddressInUse( Net net, String address )
    {
        if ( net == null || address == null )
            return false;
        Iterator iterator = opfacNetTuples( net ).iterator();
        while ( iterator.hasNext() )
            {
                OpfacNetTuple tuple = (OpfacNetTuple) iterator.next();
                if ( address.equals( tuple.getAddress() ) )
                    return true;
            }
        return false;        
    }
    
    /**
     * Removes an association between an opfac and a net.
     */
    public void removeOpfacNetTuple( Opfac opfac, Net net )
    {
        opfacToNetCollec.removeTuple( opfac, net );
    }
    
    /**
     * Removes all net connections for the specified opfac.
     * Returns true if one or more tuples were removed, false otherwise.
     */
    void removeOpfacNetTuples( Opfac opfac )
    {
        opfacToNetCollec.removeTuplesContainingOpfac( opfac );
    }

    /**
     * Removes all net connections for the specified net.
     * Returns true if one or more tuples were removed, false otherwise.
     */
    void removeOpfacNetTuples( Net net )
    {
        opfacToNetCollec.removeTuplesContainingNet( net );
    }

    /**
     * Returns a read only collection of OpfacNetTuples.
     */
    public Collection opfacNetTuples()
    {
        return opfacToNetCollec.collection();
    }

    /**
     * Returns an unmodifiable collection of the OpfacNetTuples containing
     * the specified Opfac.
     */
    public Collection opfacNetTuples( Opfac opfac )
    {
        return opfacToNetCollec.tuplesContainingOpfac( opfac );
    }

    /**
     * Returns an unmodifiable collection of the OpfacNetTuples containing
     * the specified Net.
     */
    public Collection opfacNetTuples( Net net )
    {
        return opfacToNetCollec.tuplesContainingNet( net );
    }


    //-------------------------------------------------------------------


    /**
     * Associates a processor with a node iff the processor and node are
     * in the plan.  If the processor is associated with another node, 
     * that association is removed.
     */
    public boolean addProcessorNodeTuple( ProcessorNodeTuple tuple )
    {
        if ( tuple == null )
            return false;
        Processor proc = tuple.getProcessor();
        Node node = tuple.getNode();
        if ( proc==null || node==null || !contains(proc) || !contains(node) )
            return false;        
        if ( processorHasAssociation( proc ) )
             removeProcessorNodeTuple( proc );
        procToNodeCollec.add( tuple );
        return true;
    }

    /**
     * Removes the processor-node association containing the processor.
     */
    public void removeProcessorNodeTuple( Processor Processor )
    {
        procToNodeCollec.removeEntryWithProcessor( Processor );
    }

    /**
     * Removes all processor-node associations containing the node.
     */
    public void removeProcessorNodeTuples( Node node )
    {
        procToNodeCollec.removeEntriesWithNode( node );
    }

    /**
     * Returns a read only collection of the processors associated
     * with the node.
     */
    public Collection processorsAssociatedWith( Node node )
    {
        return procToNodeCollec.processorsWithNode( node );
    }

    /**
     * Returns the node associated with the processor.
     */
    public Node nodeAssociatedWith( Processor processor )
    {
        return procToNodeCollec.getNodeWithProcessor( processor );
    }

    /**
     * Returns the node associated with the processor.
     */
    public boolean processorHasAssociation( Processor processor )
    {
        return (procToNodeCollec.getNodeWithProcessor( processor ) != null);
    }

    /**
     * Returns a read-only collection of processor node assocations.
     * The collection contains ProcessorNodeTuples.
     */
    Collection processorNodeTuples()
    {
        return procToNodeCollec.collection();
    }

    //-------------------------------------------------------------------


    /**
     * Register for PlanChangeEvents.
     * @see PlanChangeAdapter
     */
    public void addPlanChangeListener( PlanChangeListener listener )
    {
        planChangeListeners.add( listener );
    }

    /**
     * Deregister for PlanChangeEvents.
     */
    public void removePlanChangeListener( PlanChangeListener listener )
    {
        planChangeListeners.remove( listener );
    }

    /**
     * Tests if all required attributes have been set.
     */
    public boolean isComplete()
    {
        return false;
    }

    /**
     * Two plans are equal iff they have the same name.
     */
    public boolean equals( Object obj )
    {
        if ( obj == null || !(obj instanceof Plan ) )
            return false;
        Plan plan2 = (Plan) obj;
        boolean result =
            ( planAttributes.equals( plan2.planAttributes )
              && netCollec.equals( plan2.netCollec )
              && nodeCollec.equals( plan2.nodeCollec )
              && opfacCollec.equals( plan2.opfacCollec )
              && processorCollec.equals( plan2.processorCollec )
              && portCollec.equals( plan2.portCollec )
              && opfacToNetCollec.equals( plan2.opfacToNetCollec ) );
        return result;
    }
    
    public String toString()
    {
        return "Plan[ " + planAttributes.getName() + " ]";
    }

    public DataContainer toContainer()
    {
        return planContainer.planToContainer( this );
    }

    void firePlanChanged()
    {
        PlanChangeEvent event = 
            new PlanChangeEvent( PlanChangeEvent.MODIFIED, this );
        Iterator iter = planChangeListeners.iterator();
        while( iter.hasNext() ) 
            {
                PlanChangeListener listener = (PlanChangeListener) iter.next();
                listener.planChanged( event );
            }
    }




    private abstract class PlanObjectCollection implements ChangeListener
    {
        protected List list = new ArrayList();

        public void stateChanged( ChangeEvent e )
        {
            sendListChanged( PlanChangeEvent.MODIFIED, e.getSource() );
        }

        void add( PlanObject obj )
        {
            if ( obj.getID() <= 0 )
                obj.setID( maxID() + 1 );
            obj.setPlan( Plan.this );
            orderedInsert( obj );
            obj.addChangeListener( this );
            sendListChanged( PlanChangeEvent.ADDED, obj );
            Plan.this.firePlanChanged();
        }
        private int maxID()
        {
            int id = 0;
            Iterator iter=list.iterator();
            while( iter.hasNext() )
            {
                PlanObject obj = (PlanObject) iter.next();
                id = Math.max( id, obj.getID() );
            }
            return id;
        }
        private void orderedInsert( PlanObject obj )
        {
            String name = obj.getName();
            if ( name==null )
                name = "";
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String name2 = ( (PlanObject)list.get(i) ).getName();
                if ( name2 == null )
                    name2 = "";
                if ( name.compareTo( name2 ) < 0 ) {
                    list.add( i, obj );
                    return;
                }
            }
            list.add( obj );            
        }


        void remove( PlanObject obj )
        {
            obj.removeChangeListener( this );
            list.remove( obj );
            sendListChanged( PlanChangeEvent.REMOVED, obj );
            Plan.this.firePlanChanged();
        }

        PlanObject get( int id )
        {
            Iterator iter = list.iterator();
            while( iter.hasNext() ) {
                PlanObject obj = (PlanObject) iter.next();
                if ( obj.getID() == id )
                    return obj;                
            }
            return null;
        }

        boolean contains( Object obj )
        {
            return list.contains( obj );
        }

        Collection collection()
        {
            return Collections.unmodifiableCollection( list );
        }

        private void sendListChanged( String changeType, Object source )
        {
            PlanChangeEvent e = new PlanChangeEvent( changeType, source );
            Iterator iter = Plan.this.planChangeListeners.iterator();
            while( iter.hasNext() )
                {
                    PlanChangeListener listener = 
                        (PlanChangeListener) iter.next();
                    callListChanged( listener, e );
                }        
        }

        /**
         * Implementation should call the appropriate listChanged method 
         * on the listener.
         */
        abstract void callListChanged( PlanChangeListener listener, 
                                       PlanChangeEvent e );

        public boolean equals( Object obj )
        {
            if ( obj == null || !(obj instanceof PlanObjectCollection) )
                return false;
            PlanObjectCollection planObjectCollection2 = 
                (PlanObjectCollection) obj;
            return list.equals( planObjectCollection2.list );
        }

        void print()
        {
            Iterator iter = list.iterator();
            while( iter.hasNext() ) {
                PlanObject obj = (PlanObject) iter.next();
                obj.toContainer().print();
            }
        }
    }



    private class NodeCollection extends PlanObjectCollection
    {
        void callListChanged( PlanChangeListener listener, PlanChangeEvent e )
        {
            listener.nodeListChanged( e );
        } 

        Node getNode( String name )
        {
            if ( name == null )
                return null;
            Iterator iter = list.iterator();
            while( iter.hasNext() ) {
                Node node = (Node) iter.next();
                if ( name.equals( node.getName() ) )
                    return node;
            }
            return null;
        }

        boolean containsNodeWithName( String name )
        {
            return (getNode(name) != null);
        }
    }



    private class NetCollection extends PlanObjectCollection
    {
        void callListChanged( PlanChangeListener listener, PlanChangeEvent e )
        {
            listener.netListChanged( e );
        } 

        Net getNet( String name )
        {
            if ( name == null )
                return null;
            Iterator iter = list.iterator();
            while( iter.hasNext() ) {
                Net net = (Net) iter.next();
                if ( name.equals( net.getName() ) )
                    return net;
            }
            return null;
        }

        boolean containsNetWithName( String name )
        {
            return (getNet(name) != null);
        }
    }



    private class OpfacCollection extends PlanObjectCollection
    {
        void callListChanged( PlanChangeListener listener, PlanChangeEvent e )
        {
            listener.opfacListChanged( e );
        }

        Opfac getOpfac( String unitName, String role )
        {
            if ( unitName == null || role == null )
                return null;
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    Opfac opfac = (Opfac) iter.next();                
                    if ( unitName.equals( opfac.getUnitName() ) &&
                         role.equals( opfac.getRole() ) )
                        return opfac;
                }
            return null;
        }

        boolean containsOpfac( String unitName, String role )
        {
            return (getOpfac(unitName, role) != null);
        }

        boolean containsTacfireAlias( String tacfireAlias )
        {
            if ( tacfireAlias == null )
                return false;
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    Opfac opfac = (Opfac) iter.next();                
                    if ( tacfireAlias.equals( opfac.getTacfireAlias() ) )
                        return true;
                }
            return false;
        }

        boolean containsUnitNumber( Integer unitNumber )
        {
            if ( unitNumber == null )
                return false;
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    Opfac opfac = (Opfac) iter.next();                
                    if ( unitNumber.equals( opfac.getUnitNumber() ) )
                        return true;
                }
            return false;
        }

        boolean containsUnitReferenceNumber( Integer urn )
        {
            if ( urn == null )
                return false;
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    Opfac opfac = (Opfac) iter.next();                
                    if ( urn.equals( opfac.getUnitReferenceNumber() ) )
                        return true;
                }
            return false;
        }

    }


    private class ProcessorCollection extends PlanObjectCollection
    {
        void callListChanged( PlanChangeListener listener, PlanChangeEvent e )
        {
            listener.processorListChanged( e );
        } 

        Processor getProcessor( String name )
        {
            if ( name == null )
                return null;
            Iterator iter = list.iterator();
            while( iter.hasNext() ) {
                Processor processor = (Processor) iter.next();
                if ( name.equals( processor.getName() ) )
                    return processor;
            }
            return null;
        }

        boolean containsProcessor( String name )
        {
            return (getProcessor(name) != null);
        }
    }
    


    private class PortCollection extends PlanObjectCollection
    {
        void callListChanged( PlanChangeListener listener, PlanChangeEvent e )
        {
            listener.portListChanged( e );
        } 

        Port getPort( Processor processor, PortName portName )
        {
            if ( processor == null || portName == null )
                return null;
            Iterator iter = list.iterator();
            while( iter.hasNext() ) 
                {
                    Port port = (Port) iter.next();
                    if ( processor == port.getProcessor()
                         && portName.equals( port.getPortName() ) )
                        return port;
                }
            return null;
        }

        boolean containsPort( Processor processor, PortName portName )
        {
            return (getPort(processor, portName) != null);
        }

        void removePortsContainingProcessor( Processor processor )
        {
            Object[] arr = list.toArray();
            for ( int i=0; i<arr.length; i++ ) {
                Port port = (Port) arr[i];
                if ( processor == port.getProcessor() )
                    remove( port );
            }
        }
        
        void clearReferencesToNet( Net net )
        {
            if ( net == null )
                return;
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    Port port = (Port) iter.next();
                    if ( net == port.getNet() )
                        port.setNet( null );
                }
        }

        Collection portsContainingNet( Net net )
        {
            List portList = new ArrayList();
            if ( net == null )
                return Collections.unmodifiableCollection( portList );
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    Port port = (Port) iter.next();
                    if ( net == port.getNet() )
                        portList.add( port );
                }
            return Collections.unmodifiableCollection( portList );
        }

        Collection portsContainingProcessor( Processor proc )
        {
            List portList = new ArrayList();
            if ( proc == null )
                return Collections.unmodifiableCollection( portList );
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    Port port = (Port) iter.next();
                    if ( proc == port.getProcessor() )
                        portList.add( port );
                }
            return Collections.unmodifiableCollection( portList );
        }

    }



    private class OpfacToNetCollection extends PlanObjectCollection
    {
        void callListChanged( PlanChangeListener listener, PlanChangeEvent e )
        {
            listener.opfacToNetListChanged( e );
        } 

        OpfacNetTuple getTuple( Opfac opfac, Net net )
        {
            if ( opfac == null || net == null )
                return null;
            Iterator iter = list.iterator();
            while( iter.hasNext() ) {
                OpfacNetTuple tuple = (OpfacNetTuple) iter.next();
                if ( opfac == tuple.getOpfac()
                     && net == tuple.getNet() )
                    return tuple;
            }
            return null;
        }

        void removeTuple( Opfac opfac, Net net )
        {
            if ( opfac == null || net == null )
                return;
            Object[] arr = list.toArray();
            for ( int i=0; i<arr.length; i++ ) {
                OpfacNetTuple tuple = (OpfacNetTuple) arr[i];
                if ( opfac == tuple.getOpfac()
                     && net == tuple.getNet() )
                    remove( tuple );
            }
        }
        
        void removeTuplesContainingOpfac( Opfac opfac )
        {
            if ( opfac == null )
                return;            
            Object[] arr = tuplesContainingOpfac( opfac ).toArray();
            for ( int i=0; i<arr.length; i++ ) {
                OpfacNetTuple tuple = (OpfacNetTuple) arr[i];
                remove( tuple );
            }
        }
        
        void removeTuplesContainingNet( Net net )
        {
            if ( net == null )
                return;
            Object[] arr = tuplesContainingNet( net ).toArray();
            for ( int i=0; i<arr.length; i++ ) {
                OpfacNetTuple tuple = (OpfacNetTuple) arr[i];
                remove( tuple );
            }
        }
        
        boolean containsTupleWithOpfacNet( Opfac opfac, Net net )
        {
            return (getTuple(opfac, net) != null);
        }

        Collection tuplesContainingOpfac( Opfac opfac )
        {
            List tupleList = new ArrayList();
            if ( opfac == null )
                return Collections.unmodifiableCollection( tupleList );
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    OpfacNetTuple tuple = (OpfacNetTuple) iter.next();
                    if ( opfac == tuple.getOpfac() )
                        tupleList.add( tuple );
                }
            return Collections.unmodifiableCollection( tupleList );
        }

        Collection tuplesContainingNet( Net net )
        {
            List tupleList = new ArrayList(20);
            if ( net == null )
                return Collections.unmodifiableCollection( tupleList );
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    OpfacNetTuple tuple = (OpfacNetTuple) iter.next();
                    if ( net == tuple.getNet() )
                        tupleList.add( tuple );
                }
            return Collections.unmodifiableCollection( tupleList );
        }
     
    }




    private class ProcessorToNodeCollection extends PlanObjectCollection
    {
        void callListChanged( PlanChangeListener listener, PlanChangeEvent e )
        {
            listener.processorNodeAssociationChanged( e );
        } 

        Node getNodeWithProcessor( Processor processor )
        {
            if ( processor == null )
                return null;
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    ProcessorNodeTuple tuple = (ProcessorNodeTuple)iter.next();
                    if ( processor == tuple.getProcessor() )
                        return tuple.getNode();
                }
            return null;
        }  
        
        void removeEntryWithProcessor( Processor processor )
        {
            if ( processor == null )
                return;
            Iterator iter = list.iterator();
            while (iter.hasNext() )
                {
                    ProcessorNodeTuple tuple = (ProcessorNodeTuple)iter.next();
                    if ( processor == tuple.getProcessor() )
                        {
                            remove( tuple );
                            // iterator is now invalid
                            removeEntryWithProcessor( processor );
                            return;
                        }
                }
        }
        
        void removeEntriesWithNode( Node node )
        {
            if ( node == null )
                return;
            Iterator iter = list.iterator();
            while (iter.hasNext() )
                {
                    ProcessorNodeTuple tuple = (ProcessorNodeTuple)iter.next();
                    if ( node == tuple.getNode() )
                        {
                            remove( tuple );
                            // iterator is now invalid
                            removeEntriesWithNode( node );
                            return;
                        }
                }
        }
        
        Collection processorsWithNode( Node node )
        {
            Collection coll = new ArrayList();
            if ( node == null )
                return coll;
            Iterator iter = list.iterator();
            while( iter.hasNext() )
                {
                    ProcessorNodeTuple tuple = (ProcessorNodeTuple)iter.next();
                    if ( node == tuple.getNode() )
                        coll.add( tuple.getProcessor() );
                }
            return Collections.unmodifiableCollection( coll );
        }

    }

    public static void main( String[] args )
    {
        try {
            PushbackReader in = new PushbackReader( new FileReader( args[0] ));
            DataContainer dc = DataContainer.read( in );
            Plan plan = new Plan( dc );
            plan.toContainer().print();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }


}    
