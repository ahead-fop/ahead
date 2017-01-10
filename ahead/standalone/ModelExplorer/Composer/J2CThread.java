package ModelExplorer.Composer;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import ModelExplorer.Main;

public class J2CThread extends Thread
{
	private Main me;
	private ToolsPanel cp;
	private Runnable error ;
   	private Runnable input ;
	private boolean isFile;
	private DefaultMutableTreeNode node;

	public J2CThread(Main me,ToolsPanel cp, boolean isFile, DefaultMutableTreeNode node){
		this.me = me;
		this.cp = cp;
		this.isFile = isFile;
		this.node = node;
		start();
	}


	public J2CThread(Main me,ToolsPanel cp, boolean isFile){
		this.me = me;
		this.cp = cp;
		this.isFile = isFile;
		this.node = null;
		start();
	}

	public void run(){
		try{
			sleep(100);
		}catch(InterruptedException e){}

		cp.setOutputView();
		cp.setEnabled(false);
		me.status.setForeground(Color.magenta);
		String equationName=(String)cp.eqComboBox.getSelectedItem();
		if (isFile){
			me.status.setText("Status: Compiling java file........");
			cp.outputArea.setText("");
			invokeJavac(cp.selectedFile);
		    me.treeBrowser.updateNode(node);
		    me.editorTabbedPane.setSelectedIndex(2);
		}
		else{
			//run the ant build for the given dir
			me.status.setText("Status: .Building the project created by the equation.......");
			String filePath=me.modelDir+File.separator+equationName.substring(0,equationName.lastIndexOf('.'));
			cp.outputArea.setText("");
			invokeAntBuild(filePath);
			me.treeBrowser.updateNode(equationName);
		}

		cp.outputArea.append("Finished running the build process.");
		cp.outputArea.append("\n");
		try{
			cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
		}catch(Exception be){}
		me.status.setText("Status: Finished running the build.");
		me.status.setForeground(me.editorTitle.getForeground());
		cp.setEnabled(true);
	}

	public void invokeJavac(String filePath){

		if (filePath.endsWith(File.separator)){
			filePath=filePath.substring(0,filePath.length()-1);
		}
		//StringBuffer execStr = new StringBuffer();
                ArrayList execList = new ArrayList();
                execList.add("env");
                execList.add("--");

		String inputString = JOptionPane.showInputDialog("Please give the user class path:",
		                        filePath.substring(0,filePath.lastIndexOf(File.separator)));
		if (inputString!=null){
			//execStr.append("javac -classpath ");
                        execList.add("javac");
                        execList.add("-classpath");
			//execStr.append(inputString);
			//execStr.append(";$CLASSPATH$;. ");
                        execList.add(inputString+";$CLASSPATH$;.");
		}
		else{
			//execStr.append("javac -classpath ");
                        execList.add("javac");
                        execList.add("-classpath");
			//execStr.append("$CLASSPATH$;. ");
                        execList.add("$CLASSPATH$;.");
		}
		//execStr.append(filePath);
                execList.add(filePath);

		try{
			cp.outputArea.append("Running output:\n\n");
			/*String osName = System.getProperty("os.name" );
			//System.out.println(osName);
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
                        for (int i=0; i<execList.size(); i++)
                                cmd[i] = (String)execList.get(i);
                        //run the command using Cygwin's "env" to be portable
                        Process pr=Runtime.getRuntime().exec(cmd,null, new File(me.modelDir));
			pr.getOutputStream().close () ;

			error = new StreamRunnable (pr.getErrorStream (), me) ;
			new Thread(error).start () ;

			input = new StreamRunnable (pr.getInputStream (), me) ;
			new Thread(input).start () ;

			int status = pr.waitFor () ;
			if (status != 0){
				cp.outputArea.append("Error occured during running the Javac.\n");
				try{
					cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
				}catch(Exception be){}
			}
			//cp.setEnabled(true);
		}
		catch(SecurityException se)
		{
			cp.outputArea.append("SecurityException:\n");
			cp.outputArea.append(se.getMessage());
			cp.outputArea.append("\n");
			try{
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			}catch(Exception be){}
		}
		catch(IOException ioe)
		{
			cp.outputArea.append("IOException:\n");
			cp.outputArea.append(ioe.toString());
			cp.outputArea.append("\n");
			try{
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			}catch(Exception be){}
		}
		catch(Exception ex)
		{
			cp.outputArea.append("Exception:\n");
			cp.outputArea.append(ex.toString());
			cp.outputArea.append("\n");
			try{
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			}catch(Exception be){}
		}

	}

	public void invokeAntBuild(String filePath){
			try{
				if (filePath.endsWith(File.separator)){
					filePath=filePath.substring(0,filePath.length()-1);
				}
				//StringBuffer execStr = new StringBuffer();
                                //execStr.append("ant -buildfile ");
				//execStr.append(filePath).append(File.separator).append("build.xml");
                                String[] cmd = new String[]{"env", "--", "ant"};
                        	cp.outputArea.append("Running output:\n\n");
				/*String osName = System.getProperty("os.name" );
				//System.out.println(osName);
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

                                //run the command using Cygwin's "env" to be portable
                                Process pr=Runtime.getRuntime().exec(cmd,null, new File(me.modelDir));
				pr.getOutputStream().close () ;

				error = new StreamRunnable (pr.getErrorStream (), me) ;
				new Thread(error).start () ;

				input = new StreamRunnable (pr.getInputStream (), me) ;
				new Thread(input).start () ;

				int status = pr.waitFor () ;
				if (status != 0){
					cp.outputArea.append("Error occured during running the ant build.\n");
					try{
						cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
					}catch(Exception be){}
				}
				//cp.setEnabled(true);
			}
			catch(SecurityException se)
			{
				cp.outputArea.append("SecurityException:\n");
				cp.outputArea.append(se.getMessage());
				cp.outputArea.append("\n");
				try{
					cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
				}catch(Exception be){}
			}
			catch(IOException ioe)
			{
				cp.outputArea.append("IOException:\n");
				cp.outputArea.append(ioe.toString());
				cp.outputArea.append("\n");
				try{
					cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
				}catch(Exception be){}
			}
			catch(Exception ex)
			{
				cp.outputArea.append("Exception:\n");
				cp.outputArea.append(ex.toString());
				cp.outputArea.append("\n");
				try{
					cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
				}catch(Exception be){}
			}
	}
}


