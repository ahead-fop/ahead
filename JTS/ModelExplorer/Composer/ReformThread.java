package ModelExplorer.Composer;

import javax.swing.*;
import java.awt.*;
import javax.swing.tree.*;
import java.io.*;
import ModelExplorer.Main;

public class ReformThread extends Thread
{
	private Main me;
	private ToolsPanel cp;
	private Runnable error ;
    private Runnable input ;
	private boolean isFile;
	private DefaultMutableTreeNode node;

	public ReformThread(Main me,ToolsPanel cp, boolean isFile, DefaultMutableTreeNode node){
		this.me = me;
		this.cp = cp;
		this.isFile = isFile;
		this.node = node;
		start();
	}

	public ReformThread(Main me,ToolsPanel cp, boolean isFile){
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
		me.status.setText("Status: Running the reform........");
		me.status.setForeground(Color.magenta);
		String equationName=(String)cp.eqComboBox.getSelectedItem();
		if (isFile){
			cp.outputArea.setText("");
			invokeReform(cp.selectedFile);
			me.editorTabbedPane.setSelectedIndex(0);
			me.treeBrowser.getTree().setSelectionPath(new TreePath(((DefaultMutableTreeNode)node.getParent()).getPath()));
			me.treeBrowser.getTree().setSelectionPath(new TreePath(node.getPath()));
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
					//if (ext.equals("jak")||ext.equals("layer")||ext.equals("java")){
					if (ext.equals("jak")){
						invokeReform(filePath);
					}
				}
			}
		}
		cp.outputArea.append("Finished running the reform process.");
		cp.outputArea.append("\n");
		try{
			cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
		}catch(Exception be){}
		me.status.setText("Status: Finished running the reform.");
		me.status.setForeground(me.editorTitle.getForeground());
		cp.setEnabled(true);
	}

	public void invokeReform(String filePath){
		try{
			if (filePath.endsWith(File.separator)){
				filePath=filePath.substring(0,filePath.length()-1);
			}
			File tmpFile=new File(filePath+"~");
			if (tmpFile.isFile()){
				tmpFile.delete();
			}
                        /*
                        StringBuffer execStr = new StringBuffer();
			execStr.append("reform ");
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

                        String[] cmd = new String[]{"env", "--", "reform", filePath};
                        cp.outputArea.append("Running output:\n\n");
                        Process pr = Runtime.getRuntime().exec(cmd, null, new File(me.modelDir));
			pr.getOutputStream().close () ;

			error = new StreamRunnable (pr.getErrorStream (), me) ;
			new Thread(error).start () ;

			input = new StreamRunnable (pr.getInputStream (), me) ;
			new Thread(input).start () ;

			int status = pr.waitFor () ;
			if (status != 0){
				cp.outputArea.append("Error occured during running the reform.\n");
				try{
					cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
					tmpFile=new File(filePath+"~");
					if (tmpFile.isFile()){
						tmpFile.delete();
					}
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


