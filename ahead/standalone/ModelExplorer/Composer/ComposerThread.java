package ModelExplorer.Composer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import ModelExplorer.Main;

public class ComposerThread extends Thread
{
	private Main me;
	private DefaultToolsPanel cp;
	private Runnable error ;
        private Runnable input ;

	public ComposerThread(Main me,DefaultToolsPanel cp){
		this.me = me;
		this.cp = cp;
		start();
	}

	public void run(){
		try{
			sleep(100);
		}catch(InterruptedException e){}
		String equationName=(String)cp.eqComboBox.getSelectedItem();;
		try{
			cp.setOutputView();
			cp.setEnabled(false);
			me.status.setText("Status: Running the composer........");
			me.status.setForeground(Color.magenta);
			//StringBuffer execStr = new StringBuffer();
                        ArrayList execList = new ArrayList();
                        execList.add("env");
                        execList.add("--");
			//String osName = System.getProperty("os.name" );

			if (cp.getOptions()==null)
				//execStr.append("composer");
                                execList.add("composer");
			else{
				String fu4jak=(String)(cp.getOptions().toolBox.getSelectedItem());
				//execStr.append("composer -Dunit.file.jak=");
				//execStr.append(fu4jak);
                                execList.add("composer");
                                execList.add("-Dunit.file.jak="+fu4jak);
			}
			//execStr.append(" --equation=");
			//execStr.append(equationName.substring(0,equationName.lastIndexOf('.')));
                        execList.add("--equation="+equationName.substring(0,equationName.lastIndexOf('.')));
			//execStr.append(" --logging=");
			//execStr.append("info");
                        execList.add("--logging=info");
			
			File tmp=new File(me.modelDir+File.separator+equationName.substring(0,equationName.lastIndexOf('.')));
			cp.outputArea.setText("Running output:\n\n");
			if (tmp.isDirectory()){
				me.treeBrowser.deleteFile(tmp);
				cp.outputArea.append("Directory "+tmp.getName()+" is deleted.\n\n");
			}
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

			error = new StreamRunnable (pr.getErrorStream (), me) ;
			new Thread(error).start () ;

			input = new StreamRunnable (pr.getInputStream (), me) ;
			new Thread(input).start () ;

			int status = pr.waitFor () ;
			if (status != 0)
				me.status.setText("Status: Composer exited with errors") ;
			else{
				me.status.setText("Status: Finished running the composer.");
			}
			me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			cp.setEnabled(true);
		}
		catch(SecurityException se)
		{
			cp.outputArea.append("SecurityException:\n");
			cp.outputArea.append(se.getMessage());
			cp.outputArea.append("\n");
			try{
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			}catch(Exception be){}
			me.status.setText("Status: SecurityException occured when running the composer.");
			me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			cp.setEnabled(true);
		}
		catch(IOException ioe)
		{
			cp.outputArea.append("IOException:\n");
			cp.outputArea.append(ioe.toString());
			cp.outputArea.append("\n");
			try{
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			}catch(Exception be){}
			me.status.setText("Status: IOException occured when running the composer.");
			me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			cp.setEnabled(true);
		}
		catch(Exception ex)
		{
			cp.outputArea.append("Exception:\n");
			cp.outputArea.append(ex.toString());
			cp.outputArea.append("\n");
			try{
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			}catch(Exception be){}
			me.status.setText("Status: Exception occured when running the composer.");
			me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			cp.setEnabled(true);
		}
	}

}


