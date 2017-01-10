package ModelExplorer.Composer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import ModelExplorer.Main;

public class ToolsThread extends Thread
{
	private Main me;
	private AntToolsPanel cp;
	private ToolsFromAnt tools;
	private String target;
	private Runnable error ;
   private Runnable input ;

	public ToolsThread(Main me,AntToolsPanel cp, ToolsFromAnt tools, String target){
		this.me = me;
		this.cp = cp;
		this.tools = tools;
		this.target = target;
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
			me.status.setText("Status: Running the "+target+"........");
			cp.outputArea.setText("");
			me.status.setForeground(Color.magenta);
			//StringBuffer execStr = new StringBuffer();
                        ArrayList execList = new ArrayList();
                        execList.add("env");
                        execList.add("--");
			//String osName = System.getProperty("os.name" );
                        /* Get a path to the "Ant" program (null means not to use Ant):
                        String antPath = System.getProperty ("tool.ant") ;
                        if (antPath == null || antPath.length () < 1)
                                return ;
                        antPath = antPath.trim () ;
                        System.out.println(antPath); */

			String buildFile=tools.getBuildFile().getAbsolutePath();
			//execStr.append("ant") ;
                        execList.add("ant");
			//execStr.append(" -Djts.home=") ;
			//execStr.append(me.ahead_home) ;
                        execList.add("-Djts.home="+me.ahead_home);
			//execStr.append(" -Dbasedir=");
			//execStr.append(me.modelDir);
                        execList.add("-Dbasedir="+me.modelDir);
			//execStr.append(" -Dlayer=");
			//execStr.append(equationName.substring(0,equationName.lastIndexOf('.')));
                        execList.add("-Dlayer="+equationName.substring(0,equationName.lastIndexOf('.')));

			if (cp.getOptions()!=null){
				AntToolOptions options=cp.getOptions();
				int index=getIndex();
				JLabel[] labels=options.getLabels(index);
				Vector fields=options.getFields(index);
				if (labels!=null){
					String name;
					String value;
					Object obj;
					for (int i=0; i<labels.length; i++){
						name=labels[i].getText();
						obj = fields.get(i);
						if (obj instanceof JTextField)
							value=((JTextField)obj).getText();
						else
							value=(String)((JComboBox)obj).getSelectedItem();
						//execStr.append(" -D").append(name).append("=").append(value);
						//System.out.println(value);
						if (value.indexOf("${jts.lib}")==-1){
							//System.out.println("no ${jts.lib}:"+value);
                        	execList.add("-D"+name+"="+value);
						}

					}
				}
			}

			//execStr.append(" -f ").append(buildFile);
			//execStr.append(" ").append(target);
                        execList.add("-buildfile");
                        execList.add(buildFile);
                        execList.add(target);

			/*File tmp=new File(me.modelDir+File.separator+equationName.substring(0,equationName.lastIndexOf('.')));
			cp.outputArea.setText("Running output for command: "+execStr.toString()+"\n\n");
			if (tmp.isDirectory()){
				me.treeBrowser.deleteFile(tmp);
				cp.outputArea.append("Directory "+tmp.getName()+" is deleted.\n\n");
			}*/
			//System.out.println(osName);
			//String[] cmd=new String[3];
			Process pr;
                        /*
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
			} */

                        //get the command array
                        String[] cmd = new String[execList.size()];
                        for (int i=0; i<execList.size(); i++){
                                cmd[i] = (String)execList.get(i);
                        }
                        //run the command using Cygwin's "env" to be portable
                        pr=Runtime.getRuntime().exec(cmd, null, new File(me.modelDir));
			pr.getOutputStream().close () ;

			error = new StreamRunnable (pr.getErrorStream (), me) ;
			new Thread(error).start () ;

			input = new StreamRunnable (pr.getInputStream (), me) ;
			new Thread(input).start () ;

			int status = pr.waitFor () ;
			if (status != 0)
				me.status.setText("Status: "+target+" exited with errors") ;
			else{
				me.status.setText("Status: Finished running the "+target);
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
			me.status.setText("Status: SecurityException occured when running "+target);
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
			me.status.setText("Status: IOException occured when running the "+target);
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
			me.status.setText("Status: Exception occured when running the "+target);
			me.treeBrowser.updateNode(equationName);
			me.status.setForeground(me.editorTitle.getForeground());
			cp.setEnabled(true);
		}
	}

	public int getIndex(){
		Vector names=tools.getNames();
		for (int i=0; i<names.size(); i++){
			if (target.equals((String)names.get(i)))
				return i;
		}
		return -1;
	}

}


