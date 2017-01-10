package ModelExplorer.Guidsl;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import ModelExplorer.Main;

public class GuidslThread extends Thread
{
	private Main me;
	private GuidslPanel gp;

	public GuidslThread(Main me,GuidslPanel gp){
		this.me = me;
		this.gp = gp;
		start();
	}

	public void run(){
		try{
			sleep(100);
		}catch(InterruptedException e){}
		String modelName=(String)gp.modelfileComboBox.getSelectedItem();
		try{
			gp.setEnabled(false);
			me.status.setText("Status: Running the GuiDsl........");
			me.status.setForeground(Color.magenta);
			//StringBuffer execStr = new StringBuffer();
			ArrayList execList = new ArrayList();
			execList.add("env");
			execList.add("--");
			//String osName = System.getProperty("os.name" );

            execList.add("guidsl");

            //if (gp.debugbox.isSelected())
            //	execList.add("-d");
            //if (gp.printinputfilebox.isSelected())
            //	execList.add("-p");
			if (gp.equationfileformatbox.isSelected())
				execList.add("-e");

			execList.add(modelName);

			/*System.out.println(osName);
			String[] cmd=new String[3];
			Process pr;
			if( osName.equals( "Windows NT" )|| osName.equals("Windows 2000")|| osName.equals("Windows XP"))
			{
			   cmd[0] = "cmd.exe" ;
				cmd[1] = "/C" ;
				cmd[2] = execStr.toString();
				pr=Runtime.getRuntime().exec(cmd,null,new File(me.modelDir));
			}
			else if( osName.equals( "Windows 95" ) )
			{
				cmd[0] = "command.com" ;
				cmd[1] = "/C" ;
				cmd[2] = execStr.toString();
				pr=Runtime.getRuntime().exec(cmd,null,new File(me.modelDir));
			}
			else{
				//System.out.println(execStr.toString());
				pr=Runtime.getRuntime().exec(execStr.toString(),null,new File(me.modelDir));
			}*/

			//get the command array
			String[] cmd = new String[execList.size()];
			for (int i=0; i<execList.size(); i++){
					cmd[i] = (String)execList.get(i);
			}
			//run the command using Cygwin's "env" to be portable
			Process pr = Runtime.getRuntime().exec(cmd,null, new File(me.modelDir));
			pr.getOutputStream().close () ;

			int status = pr.waitFor () ;
			if (status != 0)
				me.status.setText("Status: GuiDsl exited with errors") ;
			else{
				me.status.setText("Status: Finished running the GuiDsl.");
			}
			//me.treeBrowser.updateNode(EquationName);
			me.status.setForeground(me.editorTitle.getForeground());
			gp.setEnabled(true);
		}
		catch(SecurityException se)
		{
			//cp.outputArea.append("SecurityException:\n");
			//cp.outputArea.append(se.getMessage());
			//cp.outputArea.append("\n");
			//try{
			//	cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			//}catch(Exception be){}
			me.status.setText("Status: SecurityException occured when running the GuiDsl.");
			//me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			gp.setEnabled(true);
		}
		catch(IOException ioe)
		{
			//cp.outputArea.append("IOException:\n");
			//cp.outputArea.append(ioe.toString());
			//cp.outputArea.append("\n");
			//try{
			//	cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			//}catch(Exception be){}
			me.status.setText("Status: IOException occured when running the GuiDsl.");
			//me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			gp.setEnabled(true);
		}
		catch(Exception ex)
		{
			//cp.outputArea.append("Exception:\n");
			//cp.outputArea.append(ex.toString());
			//cp.outputArea.append("\n");
			//try{
			//	cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			//}catch(Exception be){}
			me.status.setText("Status: Exception occured when running the GuiDsl.");
			//me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			gp.setEnabled(true);
		}
	}

}


