//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:27:00 2000

package fsats.plan;

/**
 * Provides an abstract interface to
 * the set of ports in the test plan.
 */
public class PortDataSet
{
  private static PortDataSet nullPortDataSet=new NullPortDataSet();
  private PortData[] ports;

  /**
   * Constructs a PortDataSet.
   */
  public PortDataSet(PortData[] ports)
  {
    this.ports=ports;
  }

  /**
   * Returns the NullPortDataSet object.
   */
  public static PortDataSet getNull()
  {
    return nullPortDataSet;
  }

  /**
   * Returns whether or not the PortDataSet is null.
   */
  public boolean isNull()
  {
    return false;
  }

  /**
   * Returns the PortData object whose name
   * matches the name passed in as a
   * parameter.  If the name passed in
   * as a parameter matches the name of
   * no PortData object, then a null PortData
   * object is returned.
   */
  public PortData getPortByName(String portName)
  {
    PortData port=PortData.getNull();

    for(int i=0; port.isNull() && i<getPortCount(); ++i)
      if(portName.equals(ports[i].getName()))
	port=ports[i];

    return port;
  }

  /**
   * Returns the PortData object whose index
   * matches the index passed in as a
   * parameter.
   */
  public PortData getPortByIndex(int portIndex)
  {
    return ports[portIndex];
  }

  /**
   * Returns the number of ports in this set.
   */
  public int getPortCount()
  {
    return ports.length;
  }

  /**
   * Returns the names of all ports
   * in this set.
   */
  public String[] getPortNames()
  {
    String portNames[]=new String[getPortCount()];

    for(int i=0; i<portNames.length; ++i)
      portNames[i]=ports[i].getName();

    return portNames;
  }

  /**
   * Returns a String representation of the PortSet object.
   */
  public String toString()
  {
    String portString="ports=(";

    for(int i=0; i<getPortCount(); ++i)
      portString+=ports[i].toString();

    portString+=")";

    return portString;
  }
}

class NullPortDataSet extends PortDataSet
{
  /**
   * Constructs a NullPortDataSet.
   */
  NullPortDataSet()
  {
    super(new PortData[0]);
  }

  /**
   * Returns whether or not the NullPortDataSet is null.
   */
  public boolean isNull()
  {
    return true;
  }
}
