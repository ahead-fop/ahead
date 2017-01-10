package ModelExplorer.Composer ;

import java.io.BufferedReader ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.io.IOException ;
import java.io.PrintStream ;
import ModelExplorer.Main;

final class StreamRunnable implements Runnable {
	
	 final private BufferedReader input ;
    final private Main me ;
	 final private ToolsPanel cp;

    StreamRunnable (InputStream input, Main me) {
			this.input = new BufferedReader (new InputStreamReader (input)) ;
			this.me = me ;
			this.cp = me.toolsPanel;
    }

    public void run () {
		try {
			for (String line ; (line = input.readLine ()) != null ; ){
				cp.outputArea.append("  ");
				cp.outputArea.append(line + '\n');
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			} 
		} catch (IOException e) {
			cp.outputArea.append("IOException:\n");
			cp.outputArea.append(e.toString());
			cp.outputArea.append("\n");
			try{
				cp.outputArea.setCaretPosition(cp.outputArea.getDocument().getLength());
			}catch(Exception be){}
			me.status.setText("Status: IOException occured when running the composer.");
			me.status.setForeground(me.editorTitle.getForeground());
			cp.setEnabled(true);
		}
		
		try {
		    input.close () ;
		} catch (IOException exception) {
		    /* Close exception deliberately ignored. */
		}
    }

}
