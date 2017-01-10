//                              -*- Mode: Java -*-
// Version         :
// Author          :
// Last Modified By: 
// Last Modified On: Tue Jan 09 11:20:24 2001

package fsats.gui;

import fsats.plan.PlanDataAttributes;

public class GUI
    extends javax.swing.JFrame
    implements fsats.guiIf.UIModel
{
    private MainPanel mainPanel;

    public GUI()
    {
        super("Jakarta Fire Support Simulation");
        java.util.Properties p = new java.util.Properties();
        try
        {
            p.load(new java.io.FileInputStream("gui.properties"));

	    /*
	    PlanDataAttributes planDataAttributes = PlanDataAttributes.getInstance();
	    p.setProperty("grid.latitude.min",
			  ""+planDataAttributes.getLowerLeftLatitude());
	    p.setProperty("grid.latitude.max",
			  ""+planDataAttributes.getUpperRightLatitude());
	    p.setProperty("grid.longitude.min",
			  ""+planDataAttributes.getUpperRightLongitude());
	    p.setProperty("grid.longitude.max",
			  ""+planDataAttributes.getLowerLeftLongitude());
	    */
        }
        catch (Exception e)
        {
        }
        mainPanel = new MainPanel(p);
        addWindowListener(
            new java.awt.event.WindowAdapter()
            {
                public void windowClosing(java.awt.event.WindowEvent e) 
                { 
                    mainPanel.exit();
                }
            });
        setJMenuBar(mainPanel.getMenuBar());
        getContentPane().add(mainPanel);
        pack();
        show();
    }

    public synchronized void stateChanged(fsats.guiIf.Opfac state)
    {
        mainPanel.addOpfac(state);
    }

    public synchronized void messageSent(fsats.guiIf.Message message)
    {
        mainPanel.addMessage(message);
    }
}

