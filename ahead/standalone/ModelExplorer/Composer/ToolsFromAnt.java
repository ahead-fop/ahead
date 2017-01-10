/* ******************************************************************
   class      : ToolsFromAnt
   description: Get the tools from ant and create a button for each tool
*********************************************************************/
package ModelExplorer.Composer;

//import ModelExplorer.SwingUtils.*;
import ModelExplorer.Main;
//import ModelExplorer.Util.ModelFilter;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.util.JavaEnvUtils;

//import org.xml.sax.AttributeList ;
//import org.xml.sax.helpers.AttributeListImpl ;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;

public class ToolsFromAnt{

	private Project project;
	final private File buildFile;
	private Vector targets;
	private Vector names;
   private Vector descriptions;
	private int maxLength;
	private AntToolsPanel cp;

	  /** Our current message output status. Follows Project.MSG_XXX. */
    private int msgOutputLevel = Project.MSG_INFO;

    /** Stream to use for logging. */
    //private static PrintStream out = System.out;

    /** Stream that we are using for logging error messages. */
    //private static PrintStream err = System.err;

	 /** Names of classes to add as listeners to project. */
    private Vector listeners = new Vector(5);
	 /**
     * The Ant logger class. There may be only one logger. It will have
     * the right to use the 'out' PrintStream. The class must implements the
     * BuildLogger interface.
     */
    private String loggerClassname = null;

    /**
     * The Ant InputHandler class.  There may be only one input
     * handler.
     */
    private String inputHandlerClassname = null;

    /**
     * Whether or not output to the log is to be unadorned.
     */
    private boolean emacsMode = false;

	public ToolsFromAnt(){
		this (null, new File ("tools.xml")) ;
    }

	public ToolsFromAnt(AntToolsPanel cp, File file){
		this.cp = cp;
		buildFile = file;
		reloadProject () ;
   }

  /** get the main targets, their names and descriptions from the project.
   */
  public void getMainTargets(){
        // find the target with the longest name
        maxLength = 0;
        Enumeration ptargets = project.getTargets().elements();
        String targetName;
        String targetDescription;
        Target currentTarget;
		  targets = new Vector();
		  names = new Vector();
		  descriptions = new Vector();

        // get the main targets depending on the presence of a description
        while (ptargets.hasMoreElements()) {
            currentTarget = (Target) ptargets.nextElement();
            targetName = currentTarget.getName();
            targetDescription = currentTarget.getDescription();
            // maintain a sorted list of targets
            if (targetDescription != null) {
                int pos = findTargetPosition(names, targetName);
                names.insertElementAt(targetName, pos);
                descriptions.insertElementAt(targetDescription, pos);
					 targets.insertElementAt(currentTarget, pos);
                if (targetName.length() > maxLength) {
                    maxLength = targetName.length();
                }
            }
        }
    }

    /**
     * Searches for the correct place to insert a name into a list so as
     * to keep the list sorted alphabetically.
     *
     * @param names The current list of names. Must not be <code>null</code>.
     * @param name  The name to find a place for.
     *              Must not be <code>null</code>.
     *
     * @return the correct place in the list for the given name
     */
    private static int findTargetPosition(Vector names, String name) {
        int res = names.size();
        for (int i = 0; i < names.size() && res == names.size(); i++) {
            if (name.compareTo((String) names.elementAt(i)) < 0) {
                res = i;
            }
        }
        return res;
    }

	 public File getBuildFile(){
		 return buildFile;
	 }

	 public Vector getNames(){
		 return names;
	 }

	 public Vector getDescriptions(){
		 return descriptions;
	 }

	 public Vector getTargets(){
		 return targets;
	 }

	 public Project getProject(){
		 return project;
	 }

         public void setBasedir (String directory) {
	         project.setBasedir (directory) ;
         }

	 public void setProperty(String name, String value){
		 project.setProperty(name, value);
	 }

	 public Vector getTargetArgs(){
		 Vector args = new Vector(targets.size());
		 Property p;
		 HashMap hm;
		 boolean isEmpty;
		 for (int i=0; i<targets.size(); i++){
			 isEmpty = true;
			 Task[] tasks = ((Target)targets.get(i)).getTasks();
			 for (int j=0; j<tasks.length; j++){
				 if (tasks[j].getTaskType().equals("property")){
					 Task t = tasks[j];
					 if (t instanceof UnknownElement) {
					   	((UnknownElement) t).maybeConfigure();
					   	t = ((UnknownElement) t).getTask();
					   	if (t != null) {
							 p = (Property)t;
							 RuntimeConfigurable rc = p.getRuntimeConfigurableWrapper() ;
							 Map list = rc.getAttributeMap() ;
							 String name = (String)list.get ("name") ;
							 String value = (String)list.get ("location") ;
							 if (value==null){
							 	 value = (String)list.get("value");
							 }

							 if (value!=null){
								 if (isEmpty){
									 hm = new HashMap();
									 args.add(hm);
								 }
								 else
									 hm = (HashMap)args.get(i);
								 hm.put(name, value);
								 isEmpty = false;
							 }
						 }
					 }
				 }
			 }
			 if (isEmpty)
				 args.add(null);
		 }
		 return args;

	 }

    public void reloadProject () {
		project = new Project () ;
		try {
		    project.init();
		    project.setUserProperty("ant.file",
					    buildFile.getAbsolutePath());
		    //ProjectHelper.configureProject(project, buildFile);
		    ProjectHelper.getProjectHelper().parse(project,buildFile); 
		    addBuildListeners(project);
		    addInputHandler(project);
		    getMainTargets();
		} catch(Exception e){
		    System.out.println(e.getMessage());
		}
    }

    public void runTarget(String targetName)throws BuildException {
		 Throwable error = null;
		 try {
            PrintStream err = System.err;
            PrintStream out = System.out;

            // use a system manager that prevents from System.exit()
            // only in JDK > 1.1
            SecurityManager oldsm = null;
            if (!JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_0) &&
                !JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_1)){
                oldsm = System.getSecurityManager();

                //SecurityManager can not be installed here for backwards
                //compatability reasons (PD). Needs to be loaded prior to
                //ant class if we are going to implement it.
                //System.setSecurityManager(new NoExitSecurityManager());
            }
            try {
                System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
                System.setErr(new PrintStream(new DemuxOutputStream(project, true)));


                project.fireBuildStarted();
                /*project.init();
                project.setUserProperty("ant.version", getAntVersion());*/

					 project.executeTarget(targetName);
            } finally {
                // put back the original security manager
                //The following will never eval to true. (PD)
                if (oldsm != null){
                    System.setSecurityManager(oldsm);
                }

                System.setOut(out);
                System.setErr(err);
            }
        } catch (RuntimeException exc) {
            error = exc;
            throw exc;
        } catch (Error err) {
            error = err;
            throw err;
        } finally {
            project.fireBuildFinished(error);
        }
    }

	  /**
     * Adds the listeners specified in the command line arguments,
     * along with the default listener, to the specified project.
     *
     * @param project The project to add listeners to.
     *                Must not be <code>null</code>.
     */
    protected void addBuildListeners(Project project) {

        // Add the default listener
        project.addBuildListener(createLogger());

        for (int i = 0; i < listeners.size(); i++) {
            String className = (String) listeners.elementAt(i);
            try {
                BuildListener listener =
                    (BuildListener) Class.forName(className).newInstance();
                project.addBuildListener(listener);
            } catch (Throwable exc) {
                throw new BuildException("Unable to instantiate listener "
                    + className, exc);
            }
        }
    }

	 public void addBuildListener(BuildListener listener){
		 project.addBuildListener(listener);
	 }

    /**
     * Creates the InputHandler and adds it to the project.
     *
     * @exception BuildException if a specified InputHandler
     *                           implementation could not be loaded.
     */
    private void addInputHandler(Project project) {
        InputHandler handler = null;
        if (inputHandlerClassname == null) {
            handler = new DefaultInputHandler();
        } else {
            try {
                handler = (InputHandler)
                    (Class.forName(inputHandlerClassname).newInstance());
            } catch (ClassCastException e) {
                String msg = "The specified input handler class "
                    + inputHandlerClassname
                    + " does not implement the InputHandler interface";
                throw new BuildException(msg);
            }
            catch (Exception e) {
                String msg = "Unable to instantiate specified input handler "
                    + "class " + inputHandlerClassname + " : "
                    + e.getClass().getName();
                throw new BuildException(msg);
            }
        }
        project.setInputHandler(handler);
    }

    // XXX: (Jon Skeet) Any reason for writing a message and then using a bare
    // RuntimeException rather than just using a BuildException here? Is it
    // in case the message could end up being written to no loggers (as the
    // loggers could have failed to be created due to this failure)?
    /**
     * Creates the default build logger for sending build events to the ant
     * log.
     *
     * @return the logger instance for this build.
     */
    private BuildLogger createLogger() {
        BuildLogger logger = null;
        if (loggerClassname != null) {
            try {
                logger = (BuildLogger) (Class.forName(loggerClassname).newInstance());
            } catch (ClassCastException e) {
                System.err.println("The specified logger class "
                    + loggerClassname
                    + " does not implement the BuildLogger interface");
                throw new RuntimeException();
            } catch (Exception e) {
                System.err.println("Unable to instantiate specified logger "
                    + "class " + loggerClassname + " : "
                    + e.getClass().getName());
                throw new RuntimeException();
            }
        } else {
            logger = new MyLogger(cp.outputArea);
        }

        logger.setMessageOutputLevel(msgOutputLevel);
        //logger.setOutputPrintStream(out);
        //logger.setErrorPrintStream(err);
		  //logger.setOutputArea(cp.outputArea);
        logger.setEmacsMode(emacsMode);

        return logger;
    }

}
