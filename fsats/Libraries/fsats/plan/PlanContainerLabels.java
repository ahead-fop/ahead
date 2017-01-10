

package fsats.plan;

/**
 * Data container labels for class Plan.
 */
interface PlanContainerLabels
{

    /**
     * outer most labels
     */
    public static final String PLAN = "PLAN";
    public static final String PLAN_ATTRIBUTES = "PLAN_ATTRIBUTES";
    public static final String OPFACS = "OPFACS";
    public static final String NETS = "NETS";
    public static final String NODES = "NODES";
    public static final String OPFAC_NET_TUPLES = "OPFAC_NET_TUPLES";
    public static final String PROCESSORS = "PROCESSORS";
    public static final String PORTS = "PORTS";
    public static final String PROCESSOR_NODE_TUPLES = "PROCESSOR_NODE_TUPLES";
    
    /**
     * plan attribute labels
     */
    public static final String PLAN_NAME = "PLAN_NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String LOWER_LEFT_MAP_BOUNDARY = 
        "LOWER_LEFT_MAP_BOUNDARY";
    public static final String UPPER_RIGHT_MAP_BOUNDARY = 
        "UPPER_RIGHT_MAP_BOUNDARY";
    public static final String DATUM = "DATUM";
    public static final String START_TIME = "START_TIME";

    /**
     * opfac labels
     */
    public static final String BUMPER_NUMBER = "BUMPER_NUMBER";
    public static final String COMMANDER_OPFAC_ID = "COMMANDER_OPFAC_ID";
    public static final String COMMANDER_UNIT_NAME = "COMMANDER_UNIT_NAME";
    public static final String DEVICE = "DEVICE";
    public static final String DNS_ROLE_ALIAS = "DNS_ROLE_ALIAS";
    public static final String ECHELON = "ECHELON";
    public static final String OPFAC = "OPFAC";
    public static final String OPFAC_ID = "OPFAC_ID";
    public static final String OR_NAME = "OR_NAME";
    public static final String OBSERVER_ID = "OBSERVER_ID";
    public static final String TACFIRE_ALIAS = "TACFIRE_ALIAS";
    public static final String UNIT_NUMBER = "UNIT_NUMBER";
    public static final String UNIT_REFERENCE_NUMBER = "UNIT_REFERENCE_NUMBER";
    public static final String UNIT_REFERENCE_NUMBER_PKG11 =
        "UNIT_REFERENCE_NUMBER_PKG11";
    public static final String SUBORDINATES = "SUBORDINATES";
    public static final String UNIT_NAME = "UNIT_NAME";
    public static final String UNIT_TYPE = "UNIT_TYPE";
    public static final String MACHINE_NAME = "MACHINE_NAME";
    public static final String ROLE = "ROLE";
    public static final String UIC = "UIC";

    /**
     * net labels
     */
    public static final String NET = "NET";
    public static final String NET_ID = "NET_ID";
    public static final String NET_NAME = "NET_NAME";
    public static final String OPFAC_UNIT_NAME = "OPFAC_UNIT_NAME";
    public static final String TYPE_ID = "TYPE_ID";
    public static final String COMMO = "COMMO";
    public static final String IP_DOMAIN = "IP_DOMAIN";

    /**
     * opfac to net labels
     */
    public static final String OPFAC_NET_TUPLE = "OPFAC_NET_TUPLE";
    public static final String OPFAC_NET_TUPLE_ID = "OPFAC_NET_TUPLE_ID";
    public static final String ADDRESS = "ADDRESS";
    public static final String NAD = "NAD";
    public static final String USE_SERIALIZATION = "USE_SERIALIZATION";
    public static final String HIGH_INITIAL = "HIGH_INITIAL";
    public static final String LOW_INITIAL = "LOW_INITIAL";
    public static final String HIGH_SUBSEQUENT = "HIGH_SUBSEQUENT";
    public static final String LOW_SUBSEQUENT = "LOW_SUBSEQUENT";
    public static final String STATION_RANK = "STATION_RANK";

    /**
     * node labels
     */
    public static final String NODE = "NODE";
    public static final String NODE_ID = "NODE_ID";
    public static final String NODE_NAME = "NODE_NAME";
    public static final String UI_PROCESSOR_ID = "UI_PROCESSOR_ID";
    public static final String UI_PROCESSOR_NAME = "UI_PROCESSOR_NAME";
    public static final String ARCHIVE_PROCESSOR_ID = "ARCHIVE_PROCESSOR_ID";
    public static final String ARCHIVE_PROCESSOR_NAME="ARCHIVE_PROCESSOR_NAME";

    /**
     * processor labels
     */
    public static final String PROCESSOR = "PROCESSOR";
    public static final String PROCESSOR_ID = "PROCESSOR_ID";
    public static final String PROCESSOR_NAME = "PROCESSOR_NAME";

    /**
     * port labels
     */
    public static final String PORT = "PORT";
    public static final String PORT_ID = "PORT_ID";
    public static final String PORT_NAME = "PORT_NAME";
    public static final String IS_LOGGED = "IS_LOGGED";

    /**
     * processor to node labels
     */
    public static final String PROCESSOR_NODE_TUPLE = "PROCESSOR_NODE_TUPLE";

    /**
     * archive device labels
     */
    public static final String ARCHIVE_DEVICE = "ARCHIVE_DEVICE";
    public static final String ARCHIVE_DEVICE_ID = "ARCHIVE_DEVICE_ID";
    public static final String DEVICE_TYPE = "DEVICE_TYPE";
    public static final String PATH = "PATH";

    /**
     * CommoType labels
     */
    public static final String COMMO_TYPE = "COMMO_TYPE";
    public static final String COMMO_TYPE_ID = "COMMO_TYPE_ID";
    public static final String DEVICE_DESCRIPTION = "DEVICE_DESCRIPTION";
    public static final String NET_MEDIUM = "NET_MEDIUM";
    public static final String PROTOCOL = "PROTOCOL";
    public static final String MODULATION = "MODULATION";
    public static final String BIT_RATE = "BIT_RATE";
    public static final String ERROR_CONTROL = "ERROR_CONTROL";
    public static final String PORT_CONFIGURATION = "PORT_CONFIGURATION";

}
