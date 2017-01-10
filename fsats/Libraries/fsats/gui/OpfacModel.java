//                              -*- Mode: Java -*-
// FsatsEnvironment.java ---
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Created On      : Tue Jun 30 12:56:23 1998
// Last Modified By: Bryan Tower
// Last Modified On: April 9 1999

package fsats.gui;

import java.util.*;
import java.awt.*;

class OpfacModel 
    extends OpfacTableModel
{
    private boolean isLocationVisible = true;
    
    private Vector vector = new Vector();
  
    public OpfacModel(Client client) { super(client); }

  private int indexOfName(String name)
  {
      for (int i=0; i<vector.size(); ++i)
      {
          Opfac opfac=(Opfac)vector.elementAt(i);
          if (opfac.getSubscriber().equals(name)) 
              return i;
      }
      return -1;
  }

  public Opfac addOpfac(fsats.guiIf.Opfac simOpfac)
  {
      Opfac opfac = getOpfac(simOpfac.getId());
      if (opfac==null)
      {
          opfac = new Opfac(simOpfac, client);
          int i=vector.size();
          vector.addElement(opfac);
          fireTableRowsInserted(i, i);
      }
      else
          opfac.update(simOpfac);
      return opfac;
  }

    
    /**
     * Remove the specified Opfac.
     */
    public void removeOpfac(Opfac opfac)
    {
      int i=indexOfName(opfac.getSubscriber());
      if (i>=0) 
      {
          vector.removeElementAt(i);
          fireTableRowsDeleted(i, i);
      }
    }
    
    
    /**
     * Remove the Opfac with the specified doeName.
     */
    public void removeOpfac( int doeName )
    {
        int index = indexOf( doeName );
        if ( index > -1 )
            {
                try {
                    
                    vector.removeElementAt( index );
                    fireTableRowsDeleted( index, index );
                    
                } catch( ArrayIndexOutOfBoundsException e ) {
                    //Log.error( Log.UI, "program error", e );
                }
            }
    }
    
    
    /**
     * Return all Opfacs in the model.
     */
    public Enumeration getOpfacs()
    {
        return vector.elements();
    }


    /**
     * find the vector index of the specified opfac
     */
    private int indexOf( int doeName )
    {
        int index = -1;
        int size = vector.size();
        for ( int i = 0; i < size; i++ )
            {
                try {
                    
                    Opfac opfac = (Opfac) vector.elementAt( i );
                    if ( opfac.getName() == doeName )
                        {
                            index = i;
                            break;
                        }
                    
                } catch( ArrayIndexOutOfBoundsException e ) {
                    //Log.error( Log.UI, "program error", e );
                    break;
                }
            }
        return index;
    }
    
    
    /**
     * Return the Opfac with the specified name, 
     * @see getRowCount()
     */
    public Opfac getOpfac(String name) 
    {
        int i=indexOfName(name);
        return i<0 ? null : getOpfacAt(i);
    }

    /**
     * Return the Opfac in the specified row, where row is in table
     * model coordinates (table row numbers begin with zero).
     * @see getRowCount()
     */
    public Opfac getOpfac( int rowNum )  { return getOpfacAt(rowNum); }
    public Opfac getOpfacAt(int row) 
    {
        return (Opfac)vector.elementAt(row);
    }

    public int getRowCount() { return vector.size(); }

    protected Frame newWindow()
    {
        return new ListWindow("All Opfacs", this);
    }
}
