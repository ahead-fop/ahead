// OPFACrmi refines the Remote_OPFAC class to implement stuff necessary
// for RMI.  Refinement is via *delegation*, rather than inheritance.
// Notice that because Java doesn't implement multiple
// inheritance, this causes us some problems.

package F.Libraries.RmiStuff;

import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import FS.Lang;

public class OPFACRmi extends UnicastRemoteObject 
implements Lang.OInterfaceRmi
{
   Lang.OInterface opfac;
   
   public OPFACRmi( Lang.OInterface opfac, String rmiName ) 
       throws RemoteException
   {
      this.opfac = opfac;
      
      // Announce that the opfac object is remotely available
      try {
	 Naming.rebind( rmiName, this );
	 Lang.Log.log(rmiName + " registered on local machine's registry");
      } catch (RemoteException e) {
	 Lang.Log.error("Error: " + e);
      } catch (java.net.MalformedURLException e) {
	 Lang.Log.error("URL Error: " + e);
      }
   }
   
   public void receive( Lang.MInterface message ) {
      opfac.receive(message);
   }

   public boolean isAlive() {
      return opfac.isAlive();
   }
}
