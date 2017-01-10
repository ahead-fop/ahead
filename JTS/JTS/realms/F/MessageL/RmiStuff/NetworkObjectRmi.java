package F.Libraries.RmiStuff;

import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import FS.Lang;

/** An RMI-aware Facade for a NetworkObject **/
public class NetworkObjectRmi extends UnicastRemoteObject 
   implements Lang.NOInterfaceRmi
{
   /** The network object that we're serving access to **/
   Lang.NOInterface netObj;

   /** Constructor **/
   public NetworkObjectRmi(Lang.NOInterface netObj, String name)
      throws RemoteException 
   {
      this.netObj = netObj;

      // Announce that netobj is remotely available
      try {
	 Naming.rebind(name, this);
	 Lang.Log.log(name + " registered on local machine's registry");
      } catch (RemoteException e) {
	 Lang.Log.error("Error: " + e);
      } catch (java.net.MalformedURLException e) {
	 Lang.Log.error("URL Error: " + e);
      }
   }

   public boolean register( Lang.OInterface opfac, String opfacName ) {
      return netObj.register(opfac, opfacName); 
   }
     
   public boolean unregister( String name ) {
      return netObj.unregister(name);
   }

   public boolean send( Lang.MInterface message ) {
      return netObj.send(message);
   }
}
