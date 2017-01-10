/**
 * class: MMatrixGuiThread
 */

package ModelExplorer.MMatrixBrowser;

import javax.swing.*;
import java.awt.*;
import ModelExplorer.Main;

public class MMatrixGuiThread extends Thread
{
	private Main me;

	public MMatrixGuiThread(Main me){
		this.me = me;
		start();
	}
	public void run(){
		try{
			sleep(100);
		}catch(InterruptedException e){}
		me.mmatrixGui =new MMatrixGui(me);
		//int index=me.browserTabbedPane.getSelectedIndex();
		me.browserTabbedPane.setComponentAt(2,me.mmatrixGui.ContentPane);
		// *DSB* me.browserTabbedPane.setEnabledAt(1,true);
		me.browserTabbedPane.setEnabledAt(2,true);

		me.status.setText("Status: Loading successfully!");
		me.status.setForeground(me.editorTitle.getForeground());
	}
}


