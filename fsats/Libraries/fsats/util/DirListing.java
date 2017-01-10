package fsats.util;

import java.io.File;
import java.io.FilenameFilter;

import java.lang.String;

/**
 * Class that can be used to return a list of file names from
 * the specified directory.
 *
 * Also implements the java.io.FilenameFilter interface, which can
 * be used as a filter for a java.io.File object.
 */
public class DirListing implements FilenameFilter
{
   private File   _file;
   private String _dirName;

  /**
   * Class constructor.
   *
   * @param dir_name Name of directory.
   */
   public DirListing(String dir_name)
   {
      _dirName = dir_name;
     /*
      * Make sure that the directory path ends with the path separator.
      */ 
      if( _dirName.endsWith(File.separator) == false )
         _dirName = _dirName.concat(File.separator);

      _file = new File(_dirName);
   }

  /**
   * Returns a list of file names with the absolute path.
   */    
   public String[] getFileNames()
   {
     /*
      * File.list() will only return the file name without the
      * directory prepended to it. So make a new list with the
      * directory path added to the file names.
      */
      String[] list = _file.list(this);
      if(list != null)
      {
         for(int i = 0; i < list.length; i++)
         {
            String file_name = list[i];
            list[i] = new String(_dirName + file_name);
         }
      }
      else
         list = new String[0]; 
      
      return( list );
   }

  /**
   * Return a list File objects for every file that is in the directory.
   */
   public File[] getFileList()
   {
      String[] list      = _file.list(this);
      File[]   file_list = new File[list.length];

      for(int i = 0; i < list.length; i++)
         file_list[i] = new File(_file, list[i]);

      return( file_list );
   }

  /**
   * Implementation of abstract method from FilenameFilter.
   *
   * If the specified file_name in the directory dir is a directory,
   * return false, else return true.
   */ 
   public boolean accept(File dir, String file_name)
   {
//      String path = dir.getAbsolutePath() + File.separator + file_name;
      String path = dir.getAbsolutePath() + file_name;
      if(isDir(path))
         return(false);
      else
         return(true);
   }

   public String toString()
   {
      return( super.toString() + " " + _dirName );
   }

   public static boolean isDir(String dir)
   {
      return( new File(dir).isDirectory() );
   }

   public static void main(String args[])
   {
      DirListing dir = new DirListing("/home/shill/fsats/HealthMon");
      File[] file_list = dir.getFileList();
      for(int i = 0; i < file_list.length; i++)
      {
         System.out.println("Ab path: " + file_list[i].getAbsolutePath());
         System.out.println("path: " + file_list[i].getPath());
         System.out.println("name: " + file_list[i].getName());
         System.out.println("parent: " + file_list[i].getParent());
         System.out.println("");
      }
/*
      String[] list  = dir.getFileNames();
      
      for(int i = 0; i < list.length; i++)
      {
         System.out.println(list[i]);
      }
*/
   }

}
