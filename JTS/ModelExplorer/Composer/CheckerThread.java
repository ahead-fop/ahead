package ModelExplorer.Composer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import ModelExplorer.Main;

public class CheckerThread extends Thread
{
	private Main me;
	private ToolsPanel cp;
	private Runnable error ;
   private Runnable input ;

	public CheckerThread(Main me,ToolsPanel cp){
		this.me = me;
		this.cp = cp;
		start();
	}

	public void run(){
		try{
			sleep(100);
		}catch(InterruptedException e){}

		if (me.mmatrixGui==null || me.activeEquation==null || !me.activeEquation.equals(cp.getEquation())){
			JOptionPane.showMessageDialog(null,
			        "Please load the corresponding matrix navigotor first!",
			        "alert",
			        JOptionPane.ERROR_MESSAGE);
		}
		else{
			cp.setEnabled(false);
			me.status.setText("Status: Running the error checker........");
			me.status.setForeground(Color.magenta);

			//cp.outputArea.setText("Running error checker...\n");
			me.checker();
			//cp.outputArea.append("Finished running the error checker...\n");

			me.status.setText("Status: Finished running the error checker.");
			me.status.setForeground(me.editorTitle.getForeground());
			cp.setEnabled(true);
		}

	}
}


