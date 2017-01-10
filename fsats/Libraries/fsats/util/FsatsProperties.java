package fsats.util;

import java.io.*;

/**
 * This class implements a specialized property set.  It is statically
 * initialized with the standard java system properties and
 * properties defined in files environment.properties and fsats.properties
 * located in $FSATS_HOME/bin.  If the same property is defined in
 * the three property sets loaded at initialization, then
 * fsats.properties takes highest precedence, fsats_env next
 * and system properties last.
 */
public class FsatsProperties
{
    /**
     * the names of fsats env_vars/properties accessed by java code
     */
    public static final String LOG_ALL_DOE_MSG = "LOG_ALL_DOE_MSG";
    public static final String DOE_CHANNEL = "DOE_CHANNEL";
    public static final String ERROR_LOG = "ERROR_LOG";
    public static final String FSATS_HOME = "fsats.home";
    public static final String LOG_DIR = "LOG_DIR";
    public static final String NODE_ID = "NODE_ID";
    public static final String ORACLE_HOST_URL = "ORACLE_HOST_URL";    
    public static final String TWO_TASK = "TWO_TASK";
    public static final String FSATS_UTC_OFFSET = "FSATS_UTC_OFFSET";
    public static final String PLAN_NAME = "PLAN_NAME";
    public static final String NODE_NAME = "NODE_NAME";
    public static final String SQL_USER = "fsats.sql.user";
    public static final String SQL_PASSWORD = "fsats.sql.password";
    public static final String PLAN_FILE = "fsats.sql.plan.dc";


    
    private static String fsatsPropertiesFile = "";

    private static java.util.Properties properties = null;

    static
    {
        initialize();
    }

    private static boolean load(String filename)
    {
        properties = new java.util.Properties(properties);
        boolean loaded = false;
        try
        {
            java.io.InputStream in = new java.io.FileInputStream(filename);
            properties.load(in);
            loaded = true;
        }
        catch (java.io.FileNotFoundException e)
        {
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
        return loaded;
    }

    private static String fsatsPath()
    {
        return 
            properties.getProperty(
                FSATS_HOME, properties.getProperty("user.home")) 
                   + File.separator + "bin" + File.separator;
    }

    private static void initialize()
    {
        if (properties==null)
        {
            properties=System.getProperties();

            boolean loaded =
                load("environment.properties")
                    || load(fsatsPath()+"environment.properties");
            if (!loaded)
                System.out.println("Warning: file environment.properties not found.");

            loaded =
                load(fsatsPropertiesFile="fsats.properties")
                    || load(fsatsPropertiesFile=fsatsPath()+"fsats.properties");
        }
    }

    public static String get( String name )
    {
        return get(name, null);
    }

    public static String get( String name, String defaultValue )
    {        
        //initialize();
        return properties.getProperty(name, defaultValue);
    }

    /**
     * Add the specified property to property set accessed by this class.
     * Also add the specified property to the "fsats.properties" file.
     */
    public static void update( String name, String value )
    {
        //initialize();
        properties.put( name, value );
        try
        {
            // -- dsb -- properties.save(new FileOutputStream(fsatsPropertiesFile), "");
            properties.store(new FileOutputStream(fsatsPropertiesFile), "");
        } 
        catch( Exception e ) 
        {
            e.printStackTrace();
        }
    }
    
        
    /**
     * Test driver.  
     * Parameters of form a=b sets a to b in fsats.properties and gets a.
     * Parameters of form axb test get of axb.
     * No parameters lists all loaded environment variables.
     */
    public static void main( String[] args )
    {
        if (args.length==0)
        {
            //initialize();
            properties.list(System.out);
        }
        else
        {
            for (int i=0; i<args.length; ++i)
            {
                String label=args[i];
                int j=args[i].indexOf('=');
                if (j>=0)
                {
                    label = args[i].substring(0, j);
                    update(label, args[i].substring(j+1));
                }
                System.out.println(label+"="+get(label));
            }
        }
    }
    

    
}

