package fsats.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import java.lang.System;
import java.lang.String;
import java.lang.Process;
import java.lang.Runtime;
import java.lang.InterruptedException;

import com.oroinc.text.perl.Perl5Util;

/**
 * Java class around the Unix ps command.
 * <p>
 * Note that the variables like ETIME should be used to index into the
 * Vector that is passed into the accept method of classes which implement
 * the PosixProcessFilter interface.
 *
 */
public final class PosixPS
{
   private Perl5Util          _perl;
   private Vector             _psFields = null;
   private Hashtable          _infoTable;
   private PosixProcessFilter _filter = null;

  /*
   * PS command used with the -o option specify the format to be used,
   * and parsed by this class. 
   *
   * NOTE: If the format is changed, you MUST, make the corresponding
   * changes to the static variable below.
   *
   * Also note that args must be the last one specified in the command.
   */ 
   private String _psArgs = "ps -o etime,pid,comm,user,vsz,args";


   public static final int ETIME  = 0;  //elapsed time since the process was started.
   public static final int PID    = 1;  //process ID.
   public static final int COMM   = 2; //argv[0] of the process.
   public static final int USER   = 3; //effective user ID of the process.
   public static final int VSZ    = 4; //size of the process in virtual memory.
   public static final int ARGS   = 5; //The command with all its arguments.

/*************
   These are other fields that are avaiable on POSIX compliant systems via
   the ps command. They are not being used because there seems to be a
   problem with execing this entire command on SCO using JDK 1.1. When
   issuing the following code:
         p = Runtime.getRuntime().exec(command);
         p.waitFor();
   It will hang in the waitFor call. Seems to be a problem with buffering
   all the output that is generated from using the below larger command
   which results in more output.

   private String _psArgs = "ps -o etime,pid,comm,user,vsz,group,nice,pcpu,pgid,ppid,rgroup,ruser,time,tty,args";

   public static final int GROUP  = 6;  //effective group ID  of  the  process.
   public static final int NICE   = 7;  //priority of the process.
   public static final int PCPU   = 8;  //CPU time used.
   public static final int PGID   = 9;  //process group ID.
   public static final int PPID   = 10;  //parent process ID.
   public static final int RGROUP = 11;  //real group ID of the process.
   public static final int RUSER  = 12;  //real user ID of the process.
   public static final int TIME   = 13;  //the cumulative CPU time.
   public static final int TTY    = 14; //controlling terminal of the process.
***********/

  /**
   * Class constructor.
   */
   public PosixPS()
   {
      this(null);
   }

  /**
   * Class sonstructor.
   *
   * @param filter filter object which will be called to determine if
   *               information from a individual process should be accepted.
   *
   * @see PosixProcessFilter
   */
   public PosixPS(PosixProcessFilter filter)
   {
      _perl      = new Perl5Util();
      _infoTable = new Hashtable();
      setFilter(filter);
   }

  /**
   * Set to the specified filter.
   *
   * @see PosixProcessFilter
   */
   public void setFilter(PosixProcessFilter filter)
   {
      _filter = filter;
   }

  /**
   * Return an array of process ids. If a filter was specified in the
   * contructor or <code>setFilter</code> method, then only the id's that
   * are accepted by that filter will be returned. 
   */ 
   public String[] list()
   {
      return( list(_filter) );
   }

  /**
   * Returns a list of process ids that have been accepted by the specified
   * filter. This will also call <code>setFilter</code> with the
   * specified filter.
   *
   * @see PosixProcessFilter
   */
   public String[] list(PosixProcessFilter filter)
   {
      Process  p = null;
      String ps_cmd = _psArgs + " -e";
      String[] command = {"/bin/sh", "-c", ps_cmd};
 
      setFilter(filter);
      
      _infoTable.clear();

      try
      {
         p = Runtime.getRuntime().exec(command);
         p.waitFor();

         if(p.exitValue() == 0)
         {
           /*
            * Should always fall in here, because ps will at least return
            * a header.
            */
            BufferedReader pOut=
              new BufferedReader(
              new InputStreamReader(p.getInputStream()));
            try
            {
               String s = pOut.readLine(); // skip header.
               s = pOut.readLine();

               while( s != null)
               {
                  s = s.trim();  // Get rid of leading and trailing whitespace.
                  _psFields = _perl.split(s); // Split up s on whitespace.

//              for(int i = 0; i < _psFields.size(); i++)
//              System.out.println( "[" + (String)_psFields.elementAt(i) + "]");

                /*
                 * Hash the Vector of Strings. And key with the process id.
                 * Each process will have a vector, which will contain
                 * the information about that process.
                 */
                  if( _filter == null )
                  {
                     _infoTable.put( (String)_psFields.elementAt(PID),
                                     _psFields );
                  }
                  else if( _filter.accept((Vector)_psFields.clone()) )
                  {
                     _infoTable.put( (String)_psFields.elementAt(PID),
                                     _psFields );
                  }
                  s = pOut.readLine();
               }
            }
            catch (IOException e)
            {
               System.err.println("IOException reading process info:" + e);
               _infoTable.clear();
            }
         } // if p.exitValue()
      }
      catch (IOException e) // From Runtime.getRuntime().exec()
      {
         System.err.println("IO error: " + e);
      }
      catch (InterruptedException e1) // From Process.waitFor()
      {
         System.err.println("Exception: " + e1.getMessage());
      }
      catch (IllegalThreadStateException itse) // from Process.exitValue()
      {
          /*
           * This does get thrown occasionally, saying the the process
           * has not finished. Although, waitFor is called before
           * exitValue. Not sure why waitFor does not seem to be doing
           * its job all the time.
           */ 
//         System.err.println("IllegalThreadStateException: " +
//                            itse.getMessage());
      }


     /*
      * Create the array of process ids, to be returned.
      */
      String[] proc_ids = new String[_infoTable.size()];

      int i = 0; 
      for( Enumeration e = _infoTable.elements(); e.hasMoreElements(); i++)
      {
         Vector ps_fields = (Vector)e.nextElement();
         proc_ids[i] = new String( (String)ps_fields.elementAt(PID) );
      }

      return(proc_ids);
   }

  /**
   * Given a process id, see if the process is alive. 
   * This will also update the information that is kept for this process.
   * For example, the memory size and time alive.
   */
   public boolean isProcessAlive(String proc_id)
   {
      if(proc_id == null)
         return(false);

      String ps_cmd = _psArgs + " -p " + proc_id;
      Process  p = null;
      String[] command = {"/bin/sh", "-c", ps_cmd};

      try
      {
         p = Runtime.getRuntime().exec(command);
         p.waitFor();

         if(p.exitValue() == 0)
         {
           /*
            * Should always fall in here, because ps will at least return
            * a header.
            */
            BufferedReader pOut=
              new BufferedReader(
              new InputStreamReader(p.getInputStream()));
            try
            {
               String s = pOut.readLine(); // skip header.
               s = pOut.readLine();
               if( s != null )
               {
                  s = s.trim();  // Get rid of leading and trailing whitespace.
                  _psFields = _perl.split(s); // Split up s on whitespace.
                  _infoTable.put( (String)_psFields.elementAt(PID),
                                  _psFields );
               }
               else
                  return(false); // no process info returned for this id.
            }
            catch (IOException e)
            {
               System.err.println("IOException reading process info:" + e);
            }

            return(true);
         }
         else
            return(false);
      }
      catch (IOException e) // From Runtime.getRuntime().exec()
      {
         System.err.println("IO error: " + e);
      }
      catch (InterruptedException e1) // From Process.waitFor()
      {
         System.err.println("Exception: " + e1.getMessage());
      }
      catch (IllegalThreadStateException itse) // from Process.exitValue()
      {
       /*
        * This does get thrown occasionally, saying the the process
        * has not finished. Although, waitFor is called before
        * exitValue. Not sure why waitFor does not seem to be doing
        * its job all the time.
        */ 
//         System.err.println("IllegalThreadStateException: " +
//                            itse.getMessage());
      }

     /*
      * Return true if we get an exception from exec, waitFor or exitValue.
      */
      return(true);
   }

  /**
   * Return the elapsed time since the process was started.
   */
   public String getEtime(String proc_id)
   {
      return( getPSField(proc_id, ETIME) );
   }

  /**
   * Return the process id. Kindof redundunt.
   */
   public String getPid(String proc_id)
   {
      return( getPSField(proc_id, PID) );
   }

  /**
   * Return the command that was used to start the process.
   */
   public String getComm(String proc_id)
   {
//      return( getPSField(proc_id, COMM) );
      return( getPSField(proc_id, ARGS) );
   }

  /**
   * Return the user id of the process.
   */
   public String getUser(String proc_id)
   {
      return( getPSField(proc_id, USER) );
   }
  
  /**
   * Return the size of the process in virtual memory. 
   */
   public String getVsz(String proc_id)
   {
      return( getPSField(proc_id, VSZ) );
   }

  /**
   * Return command and the arguments used to start this process.
   */ 
   public String getArgs(String proc_id)
   {
      String command   = getPSField(proc_id, ARGS);
      Vector ps_fields = (Vector)_infoTable.get(proc_id);

     /*
      * When executing the ps command, args is the last specifier to the
      * -o option to ps. So, the last elements in _psFields are the
      * command followed by the arguments to the command. And since
      * the arguments are split up with a whitespace, all remaining
      * fields in ps_fields are the individual args to the command.
      */
      if(ps_fields != null && ps_fields.size() >= ARGS+1)
      {
         for(int i = ARGS+1; i < ps_fields.size(); i++)
            command = command + " " + (String)ps_fields.elementAt(i);
         return(command);
      }
      else
         return(null);
   }


/********* 
   public String getGroup(String proc_id)
   {
      return( getPSField(proc_id, GROUP) );
   }

   public String getNice(String proc_id)
   {
      return( getPSField(proc_id, NICE) );
   }

   public String getPcpu(String proc_id)
   {
      return( getPSField(proc_id, PCPU) );
   }

   public String getPgid(String proc_id)
   {
      return( getPSField(proc_id, PGID) );
   }
   
   public String getPpid(String proc_id)
   {
      return( getPSField(proc_id, PPID) );
   }

   public String getRgroup(String proc_id)
   {
      return( getPSField(proc_id, RGROUP) );
   }

   public String getRuser(String proc_id)
   {
      return( getPSField(proc_id, RUSER) );
   }

   public String getTime(String proc_id)
   {
      return( getPSField(proc_id, TIME) );
   }

   public String getTty(String proc_id)
   {
      return( getPSField(proc_id, TTY) );
   }
******/

   private String getPSField(String proc_id, int index)
   {
      Vector ps_fields = (Vector)_infoTable.get(proc_id);
 
      if(ps_fields == null)
      {
        /*
         * This case could happen if they changed filters. For example,
         * got a list of process ids using one filter, then changed filters
         * and got a second list, and then try using an id from the first
         * list. 
         * So, call isProcessAlive(), which will retreive information about
         * that process if it is still alive.
         */
         isProcessAlive(proc_id);
         ps_fields = (Vector)_infoTable.get(proc_id);
      }

      if(ps_fields != null && ps_fields.size() >= index+1)
         return (String)ps_fields.elementAt(index);
      else
         return(null);
   }

   public static void main(String args[])
   {
      PosixPS ps        = new PosixPS();
      String[] proc_ids = ps.list();
      for(int i=0; i < proc_ids.length; i++)
      {
         System.out.println("Name: " + ps.getComm(proc_ids[i]) + " PID: " + ps.getPid(proc_ids[i]) );
      }
      System.out.println("");
      System.out.println("");

/******
      for(int j=0; j < args.length; j++)
      {
         for(int i=0; i < proc_ids.length; i++)
         {
            if( ps.getComm(proc_ids[i]).equals(args[j]) )
            {
               System.out.println("Found: " + ps.getComm(proc_ids[i]) + " PID: " + ps.getPid(proc_ids[i]) + " Time: " + ps.getTime(proc_ids[i]) );
               if(ps.isProcessAlive(proc_ids[i]))
               {
                  System.out.println("Time: " + ps.getTime(proc_ids[i]));
               }
               System.out.println("");
            }
         }
      }
*******/
   }
}
