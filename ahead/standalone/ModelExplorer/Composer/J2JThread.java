package ModelExplorer.Composer;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import ModelExplorer.Main;

public class J2JThread extends Thread
{
	private Main me;
	private ToolsPanel cp;
	private Runnable error ;
   	private Runnable input ;
	private boolean isFile;
	private DefaultMutableTreeNode node;

	public J2JThread(Main me,ToolsPanel cp, boolean isFile, DefaultMutableTreeNode node){
		this.me = me;
		this.cp = cp;
		this.isFile = isFile;
		this.node = node;
		start();
	}


	public J2JThread(Main me,ToolsPanel cp, boolean isFile){
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
		me.status.setText("Status: Running the jak2java........");
		me.status.setForeground(Color.magenta);
		String equationName=(String)cp.eqComboBox.getSelectedItem();
		if (isFile){
			cp.outputArea.setText("");
			invokeJak2java(cp.selectedFile);
		    me.treeBrowser.updateNode(node);
		    me.editorTabbedPane.setSelectedIndex(2);
		}
		else{
			//get all the files from the give dir
			String filePath=me.modelDir+File.separator+equationName.substring(0,equationName.lastIndexOf('.'));
			cp.outputArea.setText("It's extracting the files....\n");
			Object[] files=cp.getFiles(filePath);
			cp.outputArea.append("Finished extracting the files.\n\n");
			if (files!=null){
				for (int i=0; i<files.length; i++){
					filePath=(String)files[i];
					if (filePath.endsWith(File.separator)){
						filePath=filePath.substring(0,filePath.length()-1);
					}
					String ext=filePath.substring(filePath.lastIndexOf('.')+1);
					if (ext.equals("jak")){
						invokeJak2java(filePath);
					}
				}
				me.treeBrowser.updateNode(equationName);
			}
		}

		cp.outputArea.append("Finished running the jak2java process.");
		cp.outputArea.append("\n");
		try{
			cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
		}catch(Exception be){}
		me.status.setText("Status: Finished running the jak2java.");
		me.status.setForeground(me.editorTitle.getForeground());
		cp.setEnabled(true);
	}

	public void invokeJak2java(String filePath){
		try{
			if (filePath.endsWith(File.separator)){
				filePath=filePath.substring(0,filePath.length()-1);
			}
                        /*
                        StringBuffer execStr = new StringBuffer();
			execStr.append("jak2java ");
			execStr.append(filePath);

			cp.outputArea.append("Running output for command: "+execStr.toString()+"\n\n");
			String osName = System.getProperty("os.name" );
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
                        String[] cmd = new String[]{"env", "--", "jak2java", filePath};
                        cp.outputArea.append("Running output:\n\n");
                        Process pr = Runtime.getRuntime().exec(cmd, null, new File(me.modelDir));
			pr.getOutputStream().close () ;

			error = new StreamRunnable (pr.getErrorStream (), me) ;
			new Thread(error).start () ;

			input = new StreamRunnable (pr.getInputStream (), me) ;
			new Thread(input).start () ;

			int status = pr.waitFor () ;
			if (status != 0){
				cp.outputArea.append("Error occured during running the jak2java.\n");
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


