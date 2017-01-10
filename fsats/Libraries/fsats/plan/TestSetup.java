package fsats.plan;

import java.io.*;
import fsats.util.DataContainer;
import fsats.util.Log;
import fsats.measures.UTMLocation;

class TestSetup
{

    /**
     * Creates an empty plan.
     */
    static Plan createEmptyPlan( String name )
    {
        Plan plan = new Plan( name );
        return plan;
    }


    /**
     * Creates a plan in a half completed state, i.e. opfacs without
     * commanders or subordinates, nets without opfacs, processors not
     * mapped to nodes, etc.
     */
    static Plan createIncompletePlan( String name )
    {
        Plan plan = new Plan( name );
        initializeIncompletePlan( plan );
        return plan;
    }
    private static void initializeIncompletePlan( Plan plan )
    {
        Opfac opf = new Opfac( "opf1", "role" );
        plan.addOpfac( opf );
        Net net = new Net( "net1" );
        plan.addNet( net );
        Node node = new Node( "node1" );
        plan.addNode( node );
        Processor proc = new Processor( "proc1" );
        plan.addProcessor( proc );
        Port port = new Port( PortName.SIMULATED_01 );
        plan.addPort( port );
    }


    /**
     * Creates a simple complete plan, i.e. the plan objects are interconnected
     * in the normal way, i.e. all opfacs have commanders or subordinates,
     * all processors are mapped to nodes, all ports are
     * mapped to processors, etc.
     *
     * bn_fse4 
     *      fist3
     *          fo1
     *          fo2
     */
    static Plan createStandardPlan( String name )
    {        
        Plan plan = new Plan( name );
        initializeStandardPlan( plan );
        return plan;
    }

    private static void initializeStandardPlan( Plan plan )
    {
        try {
            // add Opfacs
            Opfac fo1 =new Opfac( "opf1", "role1" );
            plan.addOpfac( fo1 );
            initOpfac( fo1, 1 );
            
            Opfac fo2 =new Opfac( "opf2", "role2" );
            plan.addOpfac( fo2 );
            initOpfac( fo2, 2 );

            Opfac fist3 =new Opfac( "opf3", "role3" );
            plan.addOpfac( fist3 );
            initOpfac( fist3, 3 );

            Opfac bn_fse4 =new Opfac( "opf4", "role4" );
            plan.addOpfac( bn_fse4 );
            initOpfac( bn_fse4, 4 );

            bn_fse4.addSubordinate( fist3);
            fist3.addSubordinate( fo1);
            fist3.addSubordinate( fo2);

            // add Nets
            Net net1 = new Net( "net1" );
            plan.addNet( net1 );
            net1.setCommo( CommoType.get( 1 ) );
            net1.setIPDomain( "ipdomain-1" );

            Net net2 = new Net( "net2" );
            plan.addNet( net2 );
            net2.setCommo( CommoType.get( 2 ) );
            net2.setIPDomain( "ipdomain-2" );

            Net net3 = new Net( "net3" );
            plan.addNet( net3 );
            net3.setCommo( CommoType.get( 3 ) );
            net3.setIPDomain( "ipdomain-3" );

            Net net4 = new Net( "orig-name" );
            plan.addNet( net4 );
            net4.setName( "net4" );
            net4.setCommo( CommoType.get( 4 ) );
            net4.setIPDomain( "ipdomain-4" );

            // add Nodes 
            Node node1 = new Node( "node1" );
            plan.addNode( node1 );
            Node node2 = new Node( "node2" );
            plan.addNode( node2 );

            // add Processors 
            Processor p1 = new Processor( "proc1" );
            plan.addProcessor( p1 );
            plan.addProcessorNodeTuple( new ProcessorNodeTuple(p1, node1) );
            Processor p2 = new Processor( "proc2" );
            plan.addProcessor( p2 );
            plan.addProcessorNodeTuple( new ProcessorNodeTuple(p2, node2) );
            Processor p3 = new Processor( "proc3" );
            plan.addProcessor( p3 );
            plan.addProcessorNodeTuple( new ProcessorNodeTuple(p3, node2) );

            // add Ports
            Port port1 = new Port( PortName.DME_1_CH_1 );
            plan.addPort( port1 );
            port1.setNet( net1 );
            port1.setProcessor( p1 );

            Port port2 = new Port( PortName.DME_2_CH_1 );
            plan.addPort( port2 );
            port2.setNet( net2 );
            port2.setProcessor( p2 );

            Port port3 = new Port( PortName.DME_3_CH_1 );
            plan.addPort( port3 );
            port3.setNet( net3 );
            port3.setProcessor( p3 );

            Port port4 = new Port( PortName.DME_4_CH_1 );
            plan.addPort( port4 );
            port4.setNet( net4 );
            port4.setProcessor( p3 );

            // complete node
            node1.setUIProcessor( p1 );
            node1.setArchiveDevice( new ArchiveDevice( "Removeable Filesystem",
                                                       "/datastor", 1 ) );
            node1.setArchiveProcessor( p1 );

            node2.setUIProcessor( p2 );
            node2.setArchiveDevice( new ArchiveDevice( "Removeable Filesystem",
                                                       "/datastor", 1 ) );
            node2.setArchiveProcessor( p2 );
            

            // add OpfacNetTuples
            OpfacNetTuple tuple = 
                new OpfacNetTuple( fo1, net1, "a" );
            plan.addOpfacNetTuple( tuple );
            tuple.setNad( 1 );
            tuple.setLowInitial( 2  );
            tuple.setHighInitial( 3 );
            tuple.setLowSubsequent( 4 );
            tuple.setHighSubsequent( 5 );
            tuple.setUseSerialization( 6 );
            tuple.setStationRank( 6 );
            plan.addOpfacNetTuple( new OpfacNetTuple( fo2, net1, "b" ) );
            plan.addOpfacNetTuple( new OpfacNetTuple( fo2, net2, "c" ) );
            plan.addOpfacNetTuple( new OpfacNetTuple( bn_fse4, net3, "aa" ) );
            plan.addOpfacNetTuple( new OpfacNetTuple( fist3, net3, "bb" ) );
            plan.addOpfacNetTuple( new OpfacNetTuple( bn_fse4, net4, "cc" ) );
        } catch( Throwable t ) {
            Log.error( Log.PLAN, t );
        }
    }
        
    static void initOpfac( Opfac opfac,  int i ) 
    {
        opfac.setTacfireAlias( "tfalias-" + i );
        opfac.setDevice( OpfacDevice.AFATDS );
        opfac.setUnitNumber( new Integer( i ) );
        opfac.setUnitReferenceNumber( new Integer( i ) );
        opfac.setUnitReferenceNumberPkg11( true );
        opfac.setObserverID( new Integer( i + 1 ) );
        opfac.setEchelon( EchelonType.TEAM );
        opfac.setUnitType(UnitType.AIRDEFENSE_SR);
        opfac.setMachineName("machinename");
        opfac.setUIC("uic");
        opfac.setOrName("orname");
        opfac.setDnsRoleAlias("dnsrolealias");
    }
        

}
