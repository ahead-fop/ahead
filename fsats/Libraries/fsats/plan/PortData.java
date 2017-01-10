//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:26:00 2000

package fsats.plan;

/**
 * Provides an abstract interface to a port.
 */
public class PortData
{
  private static PortData nullPortData=new NullPortData();
  private PlanString name;
  private NetworkData net;

  /**
   * Constructs a PortData.
   *
   * @param name - the port name.
   * @param net - the net associated with the port.
   */
  public PortData(PlanString name, NetworkData net)
  {
    this.name=name;
    this.net=net;
  }

  /**
   * Returns a reference to the NullPortData object.
   */
  public static PortData getNull()
  {
    return nullPortData;
  }

  /**
   * Returns whether or not the PortData object is null.
   */
  public boolean isNull()
  {
    return false;
  }

  /**
   * Returns the port name.
   */
  public String getName()
  {
    return name.toString();
  }

  /**
   * Returns the net associated with the port.
   */
  public NetworkData getNet()
  {
    return net;
  }

  /**
   * Returns a String representation of the PortData object.
   */
  public String toString()
  {
    return name+"=(net="+net.getName();
  }
}

class NullPortData extends PortData
{
  /**
   * Constructs a NullPortData object.
   */
  NullPortData()
  {
    super(PlanString.getNull(), NetworkData.getNull());
  }

  /**
   * Returns whether or not the NullPortData object is null.
   */
  public boolean isNull()
  {
    return true;
  }
}
