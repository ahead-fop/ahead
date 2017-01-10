package fsats.gui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

import fsats.gui.event.*;

class SimpleCheckBox
    extends JCheckBoxMenuItem
    implements ItemListener
{
    public SimpleCheckBox(String label, boolean initial)
    {
        super(label);
        setSelected(initial);
        addItemListener(this);
    }

    public void itemStateChanged(ItemEvent e)
    {
        whenChanged();
        if (isSelected())
            whenSelected();
        else
            whenDeselected();
    }

    protected void whenChanged() {}
    protected void whenSelected() {}
    protected void whenDeselected() {}
}

public class MainPanel extends JPanel {

  private final static String TARGET_PLACED = "Target";
  private JMenuBar menubar = new JMenuBar();
  private JToolBar toolbar = new JToolBar();
  private GridPane grid;
  private JScrollPane scrollingGrid;
  private boolean linkGridLinesToScale;
  private Client client;
  private final MouseMotionAdapter mousePosition =
      new MouseMotionAdapter() 
      {
          public void mouseMoved (MouseEvent e) 
          {
	      Dimension gs = grid.getGridSize();
	      Point     cp = e.getPoint();
	      Location loc = 
                  grid.pointToCoordinate(
                      new Point(
                          Math.min(cp.x, gs.width), 
                          Math.min(cp.y, gs.height)));
	      currPointerPos.setText(loc.toString());
          }
      };
  private final JTextField currPointerPos = new JTextField(15);
  private boolean displayMousePosition;
  private Properties properties;
  
  /**
   * Constructs a new MainPanel object.
   */
  public MainPanel () {
    this(null);
  }
  
  /**
   * Constructs a new MainPanel object.
   *
   * @param properties initial Properties object
   */
  public MainPanel (Properties properties) {

    this.properties = properties;
    
    // Setup stuff can go here. For now just create the menu,
    // toolbar, and default a grid somehow
    linkGridLinesToScale = true;
    displayMousePosition = false;
    
    // Setup the GridPane
    setupGrid();
    
    // Set up the client and start things up
    setupClient();

    // Create the menu and tool bars
    createBars();

    // Set up the layout
    makeLayout();
  }

  /**
   * Returns the menu bar that can be added to a frame or applet.
   *
   * @return JMenuBar object
   */
  public JMenuBar getMenuBar () {
    return menubar;
  }

  /**
   * Does what is necessary to exit the program.
   */
  public void exit () {
    // just make a quick exit for now
    System.exit(0);
  }
  
  /**
   * Creates the menu and tool bars.
   */
  protected void createBars () 
  {
    Action quitAction = 
        new AbstractAction("Quit")
        {
            public void actionPerformed(ActionEvent e)
            {
                exit();
            }
        };

    SimpleCheckBox displayGridCheckBox =
         new SimpleCheckBox("Display grid lines", grid.isVisibleGridLines())
         {
             protected void whenChanged()
             {
                 grid.setVisibleGridLines(isSelected());
             }
         };

    SimpleCheckBox displayTerrainCheckBox =
         new SimpleCheckBox("Display terrain", grid.isTerrainVisible())
         {
             protected void whenChanged()
             {
                 grid.setTerrainVisible(isSelected());
             }
         };

    SimpleCheckBox displayScalesCheckBox =
      new SimpleCheckBox("Display lat/long scales", true)
      {
          protected void whenSelected()
          {
              scrollingGrid.setColumnHeaderView(grid.getLongitudeHeader());
              scrollingGrid.setRowHeaderView(grid.getLatitudeHeader());
          }
          protected void whenDeselected()
          {
              scrollingGrid.setColumnHeaderView(null);
              scrollingGrid.setRowHeaderView(null);
          }
      };

    SimpleCheckBox displayPointerCoordinatesCheckBox =
      new SimpleCheckBox("Display pointer coordinates", false)
      {
          protected void whenSelected()
          {
              grid.addMouseMotionListener(mousePosition);
              displayMousePosition = true;
          }
          protected void whenDeselected()
          {
              grid.removeMouseMotionListener(mousePosition);
              currPointerPos.setText("See Options");
              displayMousePosition = false;
          }
      };

    SimpleCheckBox displayNewMessagesCheckBox =
      new SimpleCheckBox(
          "Display new messages on map", client.isDisplayMessageArrows())
      {
          protected void whenChanged()
          {
              client.setDisplayMessageArrows(isSelected());
          }
      };
    
    SimpleCheckBox accumulateMessagesCheckBox =
      new SimpleCheckBox(
          "Accumulate messages on map.", 
          client.getAccumulateMessageArrows())
      {
          protected void whenChanged()
          {
              client.setAccumulateMessageArrows(isSelected());
          }
      };

    Action zoomInAction =
      new AbstractAction("Zoom In", new ImageIcon("images/MagnifyPlus.gif")) 
      {
          public void actionPerformed (ActionEvent e) {
            grid.scale(1.5);
          }
      };
    
    Action zoomOutAction =
      new AbstractAction("Zoom Out", new ImageIcon("images/MagnifyMinus.gif")) {
      public void actionPerformed (ActionEvent e) {
	grid.scale(1/1.5);
      }
    };

     Action zoomResetAction =
       new AbstractAction(
           "Reset Zoom", new ImageIcon("images/MagnifyOne.gif")) 
       {
           public void actionPerformed (ActionEvent e) {
             grid.resetScale();
             grid.resetGridLineScale();
           }
       };
     
     Action expandLines =
       new AbstractAction("Expand Grid Lines",
			  new ImageIcon("images/MoreLines.gif")) {
       public void actionPerformed (ActionEvent e) {
	 grid.scaleGridLines(1/1.5);
       }
     };
     
     
     
     Action reduceLines =
       new AbstractAction("Reduce Grid Lines",
			  new ImageIcon("images/LessLines.gif")) {
       public void actionPerformed (ActionEvent e) {
	 grid.scaleGridLines(1.5);
       }
     };
     
     

     // Item used to clear out the arrow list
     Action clearArrows =
         new AbstractAction("Clear Arrows")
         {
             public void actionPerformed(ActionEvent e) { grid.clearArrows(); }
         };


     // Item used to add a target to the grid
     Action addTarget =
         new AbstractAction(
             "Create target", new ImageIcon("images/Crosshairs.gif"))
         {
             public void actionPerformed(ActionEvent e) 
             {
                 grid.placeObject(TARGET_PLACED);
             }
         };
     
     // Menu item that can be used to bring up a dialog that can scale
     // the grid.
     Action freeScale = 
         new AbstractAction("Scale grid from current...")
         {
             public void actionPerformed (ActionEvent e) 
             {
	         String in =
                   JOptionPane.showInputDialog(
                       getTopParent(), 
                       "Enter scale factor (>1.0 to make larger):");
                 if (in != null) 
                     try {
                       double factor = Double.valueOf(in).doubleValue();
                       grid.scale(factor);
                       if (linkGridLinesToScale) {
                         grid.scaleGridLines(1.0/factor);
                       }
                     } catch (Exception ex) {
                       JOptionPane.showMessageDialog(
                           getTopParent(), "Invalid Scaling Factor");
                     }
             }
         };

     // Menu item that can be used to bring up a dialog that can scale
     // the grid.
     Action freeOrig = 
         new AbstractAction("Scale grid from original...")
         {
           public void actionPerformed (ActionEvent e) 
           {
             String in =
               JOptionPane.showInputDialog
                 (getTopParent(), "Enter scale factor (>1.0 to make larger):");
             if (in != null)
                 try {
                   double factor = Double.valueOf(in).doubleValue();
                   grid.resetScale();
                   grid.resetGridLineScale();
                   grid.scale(factor);
                   if (linkGridLinesToScale) {
                     grid.scaleGridLines(1.0/factor);
                   }
                 } catch (Exception ex) {
                   JOptionPane.showMessageDialog(
                       getTopParent(), "Invalid Scaling Factor");
                 }
           }
         };
     

     Action opfacTableMenu =
         new AbstractAction("Opfacs List")                    //ExCIS
         {
             public void actionPerformed(ActionEvent e) 
             {
                 client.manageOpfacListWindow();
             }
         
         };     

     Action missionTableMenu = 
         new AbstractAction("Missions List")                  //ExCIS
         {
             public void actionPerformed (ActionEvent e) 
             {
                 client.manageMissionListWindow();

             }
         };     

     Action messageTableMenu = 
         new AbstractAction("Messages List")                  //ExCIS
         {
             public void actionPerformed (ActionEvent e) 
             {
                 client.manageMessageListWindow();

             }
         };     

     Action messageSequenceChartMenu =                        //ExCIS
         new AbstractAction("Message Sequence Chart")         //ExCIS
	 {                                                    //ExCIS
             public void actionPerformed (ActionEvent e)      //ExCIS 
             {                                                //ExCIS
                 client.manageMessageSequenceChartWindow();   //ExCIS

             }                                                //ExCIS
         };                                                   //ExCIS

     // Create menu bar.

     JMenu menu = new JMenu("File");
     menu.add(quitAction);
     menubar.add(menu);

     menu = new JMenu("Options");
     menu.add(displayGridCheckBox);
     menu.add(displayScalesCheckBox);
     menu.add(displayTerrainCheckBox);
     menu.add(displayPointerCoordinatesCheckBox);
     menu.add(displayNewMessagesCheckBox);
     menu.add(accumulateMessagesCheckBox);
     menubar.add(menu);

     menu = new JMenu("View");
     menu.add(zoomInAction);
     menu.add(zoomOutAction);
     menu.add(zoomResetAction);
     menu.add(expandLines);
     menu.add(reduceLines);
     menu.add(clearArrows);
     menu.add(freeScale);
     menu.add(freeOrig);
     menubar.add(menu);

     menu = new JMenu("Target");
     menu.add(addTarget);
     menubar.add(menu);

     menu = new JMenu("Window");
     menu.add(opfacTableMenu);
     menu.add(missionTableMenu);
     menu.add(messageTableMenu);
     menu.add(messageSequenceChartMenu);           //ExCIS
     menubar.add(menu);

     // Create button bar.

     JButton button = toolbar.add(zoomInAction);
     button.setText("");
     button.setToolTipText("Zoom In  ");

     button = toolbar.add(zoomOutAction);
     button.setText("");
     button.setToolTipText("Zoom Out  ");

     button = toolbar.add(zoomResetAction);
     button.setText("");
     button.setToolTipText("Reset Zoom  ");

     button = toolbar.add(expandLines);
     button.setText("");
     button.setToolTipText("Expand number of grid lines  ");

     button = toolbar.add(reduceLines);
     button.setText("");
     button.setToolTipText("Reduce number of grid lines  ");

     button=toolbar.add(clearArrows);
     //button.setText("");
     button.setToolTipText("Clear message arrows from map");

     button=toolbar.add(addTarget);
     button.setText("");
     button.setToolTipText("Create a target");
  }

  /**
   * Returns the enclosing JApplet or JFrame at the top of the component
   * heirarchy.
   *
   * @return Container reference
   */
  public Container getTopParent () {
    Container c = this.getParent();
    while (c != null &&
	   !((c instanceof JFrame) || (c instanceof JApplet))) {
      c = c.getParent();
    }
    return c;
  }

  /**
   * Code used to add a target to the simulation.
   *
   * @param l Location of target
   */
  protected void addTarget (Location l) {
    // find the top frame
    Container c = this.getParent();
    while (c != null && !(c instanceof Frame)) { c = c.getParent(); }
    
    AddTargetDialog adt = new AddTargetDialog((Frame)c, client, grid, l);
  }
  
  /**
   * Sets up the grid and the scrollpane for the grid.
   */
  protected void setupGrid () {
    // Get some defaults
    CoordinateRect rect = new CoordinateRect(35.0, 70.0, 40.0, 75.0);
    double gridLatDistance = 1.0;
    double gridLongDistance = 1.0;
    Dimension gridSize = new Dimension(500,500);
    Image terrainImage = null;
    
    rect.setLat1(getDoubleProperty("grid.latitude.min", rect.getLat1()));
    rect.setLat2(getDoubleProperty("grid.latitude.max", rect.getLat2()));
    rect.setLong1(getDoubleProperty("grid.longitude.min", rect.getLong1()));
    rect.setLong2(getDoubleProperty("grid.longitude.max", rect.getLong2()));
    gridLatDistance = getDoubleProperty("grid.gridlines.distance.latitude",
					gridLatDistance);
    gridLongDistance = getDoubleProperty("grid.gridlines.distance.longitude",
					 gridLongDistance);
    gridSize.width = getIntProperty("grid.width", gridSize.width);
    gridSize.height = getIntProperty("grid.height", gridSize.height);
    String img = getStringProperty("grid.terrain.image", null);
    if (img != null) {
      terrainImage = Toolkit.getDefaultToolkit().getImage(img);
      MediaTracker mt = new MediaTracker(this);
      mt.addImage(terrainImage, 1);
      try { mt.waitForAll(); } catch (InterruptedException ie) {}
    }
    
    grid = new GridPane(rect,
			gridLatDistance,
			gridLongDistance,
			gridSize,
			terrainImage);

    // Set some more stuff
    grid.setVisibleGridLines(getBooleanProperty("grid.gridlines.visible",
						grid.isVisibleGridLines()));
    grid.setTerrainVisible(getBooleanProperty("grid.terrain.visible",
					      grid.isTerrainVisible()));
    
    
    scrollingGrid = new JScrollPane(grid);
    scrollingGrid.setColumnHeaderView(grid.getLongitudeHeader());
    scrollingGrid.setRowHeaderView(grid.getLatitudeHeader());
    scrollingGrid.setPreferredSize(new Dimension(400,400));
    
    if (displayMousePosition) {
      grid.addMouseMotionListener(mousePosition);
    }

    grid.addObjectPlacedListener(new ObjectPlacedListener () {
      public void objectPlaced (ObjectPlacedEvent e) {
	try {
	  if (TARGET_PLACED.equals((String)e.getPlacedObject())) {
	    addTarget(e.getLocation());
	  }
	} catch (Exception ex) {
	}
      }
    });
  }
  
  /**
   * Sets up the layout.
   */
  protected void makeLayout () {
    this.setLayout(new BorderLayout());
    this.add(toolbar, BorderLayout.NORTH);
    this.add(scrollingGrid, BorderLayout.CENTER);
    
    // Create the lower status stuff
    JPanel infoPanel = new JPanel();
    GridBagLayout gb = new GridBagLayout();
    infoPanel.setLayout(gb);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth  = 1;
    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(5, 0, 5, 5);
    
    JLabel scaleLabel = new JLabel("Current Zoom Factor:");
    gb.setConstraints(scaleLabel, gbc);
    infoPanel.add(scaleLabel);
    
    final JTextField currScale = new JTextField(5);
    currScale.setEditable(false);
    grid.addGridPaneScaledListener(new GridPaneScaledListener () {
      public void gridPaneScaled (GridPaneScaledEvent e) {
	currScale.setText(formatDouble(grid.getCurrentScale(), 2));
      }
    });
    currScale.setText(formatDouble(grid.getCurrentScale(), 2));

    gbc.gridx = 1;
    gb.setConstraints(currScale, gbc);
    infoPanel.add(currScale);

    JLabel posLabel = new JLabel("       Pointer Coordinates: ");
    gbc.gridx = 2;
    gb.setConstraints(posLabel, gbc);
    infoPanel.add(posLabel);

    gbc.gridx = 3;
    currPointerPos.setEditable(false);
    currPointerPos.setText("See Options");
    infoPanel.add(currPointerPos);
    
    gbc.gridx = 4;
    gbc.weightx = 1.0;
    Component glue = Box.createHorizontalGlue();
    gb.setConstraints(glue, gbc);
    infoPanel.add(glue);
    
    this.add(infoPanel, BorderLayout.SOUTH);
  }

  /**
   * Sets up the client adaptor and passes it the grid reference.
   */
  protected void setupClient () {
    client = new Client(grid);
    
    client.setDisplayMessageArrows
      (getBooleanProperty("grid.newmessages.autoshow", true));
    client.setAccumulateMessageArrows(
        getBooleanProperty("grid.newmessages.accumulate", false));
  }

  /**
   * Returns a double from the properties list given the key and
   * a default.
   *
   * @param key String key
   * @param def double default
   * @return value in key or default if not found
   */
  protected double getDoubleProperty (String key, double def) {
    try {
      return Double.valueOf(properties.getProperty(key)).doubleValue();
    } catch (Exception e) {
    }
    return def;
  }
  
  /**
   * Returns an int from the properties list given the key and
   * a default.
   *
   * @param key String key
   * @param def int default
   * @return value in key or default if not found
   */
  protected int getIntProperty (String key, int def) {
    try {
      return Integer.parseInt(properties.getProperty(key));
    } catch (Exception e) {
    }
    return def;
  }

  /**
   * Returns a boolean from the properties list given the key and
   * a default.
   *
   * @param key String key
   * @param def boolean default
   * @return value in key or default if not found
   */
  protected boolean getBooleanProperty (String key, boolean def) {
    try {
      String b = properties.getProperty(key);
      if (b != null) {
	boolean bool = Boolean.valueOf(b).booleanValue();
	return Boolean.valueOf(b).booleanValue();
      }
    } catch (Exception e) {
    }
    return def;
  }

  /**
   * Returns a String from the properties list given the key and
   * a default.
   *
   * @param key String key
   * @param def String default
   * @return value in key or default if not found
   */
  protected String getStringProperty (String key, String def) {
    try {
      String s = properties.getProperty(key);
      return s;
    } catch (Exception e) {
    }
    return def;
  }
  
  /**
   * Formats a double to the number of fraction digit places.
   *
   * @param d double to format
   * @param places int max fraction digits
   * @return String with formatted double
   */
  protected static String formatDouble (double d, int places) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(places);
    return nf.format(d);
  }
  

    void addMessage(fsats.guiIf.Message message) 
    { 
        client.addMessage(message); 
    }

    void addOpfac(fsats.guiIf.Opfac opfac)
    {
        client.addOpfac(opfac);
    }
}  
