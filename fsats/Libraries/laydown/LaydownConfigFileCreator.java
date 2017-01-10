//                              -*- Mode: Java -*- 
// LaydownConfigFileCreator.java
// Author          : Bryan D. Hopkins
// Created On      : Tue Jan 09 11:27:40 2001
// Last Modified By: 
// Last Modified On: Wed Jan 10 10:26:04 2001
// Update Count    : 19
// 
// $Log: LaydownConfigFileCreator.java,v $
// Revision 1.1.1.1  2002-03-14 22:51:57  sarvela
// Jack's version of the FSATS prototype.
//
// Revision 1.1.1.1  2001/10/23 16:11:00  dsb
// FSATS Prototype V0.0
//
// 

package laydown;

import fsats.util.*;

import java.io.*;

/**
 * Generates a skeleton laydown_config file from a plan data container file.
 */
public class LaydownConfigFileCreator
{
    private File planFile;

    /**
     * Constructs a LaydownConfigFileCreator object.
     *
     * @param planFileName - The full path name of the plan data container
     *                       file from which the laydown_config file is to be
     *                       generated.
     */
    public LaydownConfigFileCreator(String planFileName)
    {
	planFile = new File(planFileName);
	
	generateLaydownConfigFile(planFile);
    }

    /**
     * Generates a laydown_config file from a plan data container file.
     *
     * @param planFile - The plan data container file from which the
     *                   laydown_config file is to be generated.
     */
    private void generateLaydownConfigFile(File planFile)
    {
	String hostName = FsatsProperties.get("HOSTNAME", "xxx") +
	    ".arlut.utexas.edu";
	
	try
	{
	    DataContainer plan =
		DataContainer.read(new PushbackReader(new FileReader(planFile)));

	    DataContainer laydownConfig = new DataContainer("laydown_config");
	    DataContainer networks = new DataContainer("networks");
	    DataContainer opfacs = new DataContainer("opfacs");
	    
	    DataContainer planNetworks = plan.getField("nets");
	    for (int i = 1; i <= planNetworks.getFieldCount(); ++i)
	    {
		DataContainer planNetwork = planNetworks.getField(i);
		
		DataContainer network =
		    new DataContainer(planNetwork.getLabel());

		DataContainer process = new DataContainer("process");
		process.addField(new DataContainer("id", "p1"));
		process.addField(new DataContainer("site", hostName));
		network.addField(process);

		network.addField(new DataContainer("transmit_delay", "3"));

		networks.addField(network);
	    }

	    DataContainer planOpfacs = plan.getField("opfacs");
	    for (int i = 1; i <= planOpfacs.getFieldCount(); ++i)
	    {
		DataContainer planOpfac = planOpfacs.getField(i);
		String tacfireAlias =
		    planOpfac.getField("tacfire_alias").getValue();

		DataContainer opfac = new DataContainer(tacfireAlias);

		DataContainer process = new DataContainer("process");
		process.addField(new DataContainer("id", "p1"));
		process.addField(new DataContainer("site", hostName));
		opfac.addField(process);

		opfacs.addField(opfac);
	    }

	    laydownConfig.addField(networks);
	    laydownConfig.addField(opfacs);

	    File laydownConfigFile = new File("laydown_config");
	    laydownConfigFile.createNewFile();
	    FileWriter fileWriter = new FileWriter(laydownConfigFile);
	    PrintWriter printWriter = new PrintWriter(fileWriter);

	    laydownConfig.print(printWriter);

	    printWriter.flush();
	    fileWriter.flush();
	    fileWriter.close();
	    printWriter.close();
	}
	catch (IOException ioex)
	{
	    ioex.printStackTrace();
	}
    }

    /**
     * Runs the program.
     */
    public static void main(String args[])
    {
	LaydownConfigFileCreator laydownConfigFileCreator;

	if (args.length != 1)
	{
	    System.out.println("java laydown." +
			       "LaydownConfigFileCreator <plan_file_name>");
	}
	else
	    laydownConfigFileCreator = new LaydownConfigFileCreator(args[0]);
    }
}
