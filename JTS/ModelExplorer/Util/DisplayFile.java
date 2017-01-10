// ModelExplorer Project
// Visualizing algebraic transformations in the GenBorg generative software environment.
/**
 *   class      : DisplayFile
 *   description: A utility class which provides a common display function.
 */
package ModelExplorer.Util;

import ModelExplorer.Main;
import mmatrix.*;
import ModelExplorer.Editor.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.URL;
import java.util.Hashtable;
import javax.swing.text.*;

public class DisplayFile {
        
        public static File file=null;
		  public static MMOutput obj=null;
		  
        //Utility function to initilize the JPanel
			public static JPanel initPanel(boolean horizontal) {
			    JPanel p = new JPanel();
			    if (horizontal)
			       p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
			    else
			       p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
			    p.setBorder( BorderFactory.createEmptyBorder(1,2,1,2));
			    return p;
			}

        public static void display(Main me)
        { 
            //File file = new File(filePath);
            String fileName=file.getName();
            if (file.isFile()){
              String str=fileName.substring(fileName.lastIndexOf('.')+1);
              str=str.toLowerCase();
              if (!(str.equals("class")||str.equals("doc")||str.equals("pdf"))){
                 me.editorTitle2.setText(file.getAbsolutePath() );
                 if (str.equals("html")||str.equals("htm")||str.equals("gif")||str.equals("jpg")){
                   if (str.equals("gif")||str.equals("jpg")){
                      String tep="<html><body><img src=file:///"+file.getAbsolutePath()+"></body></html>";
                     // me.editor.setContentType("text/html");
                      me.editor.setText(tep);
                      me.editorScrollPane = new JScrollPane(me.editor);
                      me.editorPanel2 = initPanel(false);
                      me.editorPanel2.add(me.editorTitle2);
                      me.editorPanel2.add(me.editorScrollPane);
                      me.editorTabbedPane.setComponentAt(0,me.editorPanel2);
                      me.editorTabbedPane.setTitleAt(0,"Viewer");
                      me.status.setText("Status: Displaying the selected artifact.");
                                                                        
                   }
                   else{
                      URL myURL;
                      try{ myURL = new URL("file:///"+file.getAbsolutePath()); }
                      catch(Exception e1){
                              me.status.setText("Status: Couldn't create the artifact URL");
                              return;
                      }
                      try{
									me.editor.setPage(myURL);
									me.editorScrollPane = new JScrollPane(me.editor);
									me.editorPanel2 = initPanel(false);
									me.editorPanel2.add(me.editorTitle2);
									me.editorPanel2.add(me.editorScrollPane);
									me.editorTabbedPane.setComponentAt(0,me.editorPanel2);
			  		 		 		me.editorTabbedPane.setTitleAt(0,"Viewer");
                           me.status.setText("Status: Displaying the selected artifact.");
                      }
                      catch (Exception e2){
                           me.status.setText("Status: Couldn't display the artifact");
                      }
                  }
			  		}
			  	}
			 }                   
        }
        
        public static void edit(Main me)
        { 
           String fileName=file.getName();
           if (file.isFile()){
              String str=fileName.substring(fileName.lastIndexOf('.')+1);
              str=str.toLowerCase();
              if (!(str.equals("class")||str.equals("doc")||str.equals("pdf")||str.equals("gif")||str.equals("jpg"))){
					  me.editorTitle.setText(file.getAbsolutePath());
						me.codeEditor.loadFile(file);
						Highlighter hl=me.codeEditor.getEditor().getHighlighter();
						hl.removeAllHighlights();
	 	 				if (obj!=null){
	 	 					int startL=obj.getStartLine()-1;
	 	 					int endL=obj.getLastLine()-1;
	 	 					if (startL>-1){
	 	 						Document doc=me.codeEditor.getEditor().getDocument();
	 	 						DefaultHighlighter.DefaultHighlightPainter dhp =new DefaultHighlighter.DefaultHighlightPainter(Color.lightGray);
	 	 						int startP=doc.getDefaultRootElement().getElement(startL).getStartOffset();
	 	 						int endP=doc.getDefaultRootElement().getElement(endL).getEndOffset();
	 	 						int len=doc.getDefaultRootElement().getElementCount();
	 	 						if ((startL+20)>=len)
	 	 							me.codeEditor.getEditor().goToLine(len-1);
	 	 						else
	 	 							me.codeEditor.getEditor().goToLine(startL+20);
	 	 						try{
	 	 							hl.addHighlight(startP, endP, dhp);
	 	 						}catch(Exception ex){};
	 	 					}
	 	 				}
						//me.editorPanel = initPanel(false);
						//me.editorPanel.add(me.editorTitle);
						//me.editorPanel.add(me.codeEditor.ContentPane);
						me.editorTabbedPane.setComponentAt(0,me.editorPanel);
						if (me.isReadOnly){
							me.editorTabbedPane.setTitleAt(0," Viewer ");
							me.status.setText("Status: Displaying the selected artifact.");
						}
						else{
							me.editorTabbedPane.setTitleAt(0," Editor ");
							me.status.setText("Status: Editing the selected artifact.");
						}
              }
              else{
                 me.status.setText("Status: It's not a text or html artifact, Please use the viewer!");
              }
      }
      else{
         me.status.setText("Status: The selected item is not an artifact!");
      }
   }
}

// end
