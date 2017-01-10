//
// StringManager
//
// $Revision: 1.1.1.1 $
//
// $Date: 2002-03-14 22:51:28 $
//
package fsats.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import fsats.util.FsatsProperties;

/**
 * StringManager provides strings to the system based on a given
 * reference. This allows the on-screen strings to be defined outside
 * of the code itself and in a central location, which should make
 * it easier to change the look of the program. This is a singleton
 * class.
 */
public class StringManager
{
    /**
     * Value returned when the requested string key is not found
     * in the lookup table.
     */
    public static final String UNKNOWN_STRING_VALUE = 
        "<UNKOWN STRING VALUE>";
    
    private static final StringManager singleton = new StringManager();
    
    private File file;
    private Properties strings = new Properties();
    private FileInputStream strings_in_file = null;
    
    /**
     * Instantiates a new StringManager. This is private and should
     * only be called by StringManager itself to ensure this class
     * follows the singleton pattern.
     */
    private StringManager()
    {
        // FIXME should probably load in the initial strings properties
        // file here
        
        // - set the File to the file containting the strings
        // - load in the strings with the load method of Properties
        setTextFile("ace-strings.cfg");
    }
    
    /**
     * Returns the singleton instance of the StringManager class.
     *
     * @return StringManager reference
     */
    public static StringManager instance()
    {
        return singleton;
    }
    
    /**
     * Returns the requested string after finding it in the string
     * lookup table. If the key is not found in the table, the following
     * UNKNOWN_STRING_VALUE string will be returned.
     *
     * // FIXME - add assertions and preconditions
     *
     * @param key reference into the string table
     * @return the String associated with the key or UNKNOWN_STRING_VALUE
     */
    public String format(String key)
    {
        // FIXME
        // - look for the key in the strings properties object
        // - if found, return that value, otherwise return the
        //   UNKNOWN_STRING_VALUE constant
        String key_answer = strings.getProperty(key);
        
        if(key_answer == null)
        {
            return UNKNOWN_STRING_VALUE;
        }
        else
        {
            return key_answer;
        }
    }

    /**
     * Returns the requested string referenced by the given key
     * and after processing it via MessageFormat.format method -
     * the key should return a pattern that will be used with the
     * object passed in to form a complete string.
     *
     * // FIXME - add assertions and preconditions
     *
     * @param key reference into the string table
     * @param args array of Objects used as arguments to format the string
     * @return the requested formatted string or UNKNOWN_STRING_VALUE if
     *         the key could not be found
     */
    public String format(String key, Object[] args)
    {
        // FIXME
        // - look for the key in the strings properies object
        // - if found, run the returned value and the passed in args
        //   through the MessageFormat.format method and return the
        //   resulting string
        // - if not found, return UNKNOWN_STRING_VALUE
        String key_answer = strings.getProperty(key);
        
        if(key_answer == null)
        {
            return UNKNOWN_STRING_VALUE;
        }
        else
        {
            String messageFormat_answer = MessageFormat.format(key_answer, args);
            return messageFormat_answer;
        }
    }
    
    /**
     * Returns the character given the lookup key. If the value associated
     * with the key is a String of more than 1 character, only the first
     * character will be returned. If the key or value is not found, null
     * will be returned.
     *
     * @param key reference in the string table
     * @return Character found or null if not found
     */
    public Character getCharacter(String key)
    {
        String value = strings.getProperty(key);
        
        if (value != null && value.length() > 0)
        {
            return new Character(value.charAt(0));
        }
        
        return null;
    }
    
    /**
     * Returns the character given the lookup key. If the value associated
     * with the key is a String of more than 1 character, only the first
     * character will be returned. If the key or value is not found,
     * the default character that is passed in will be returned.
     *
     * @param key reference in the string table
     * @param def character to return if not found in table
     * @return char found or default if not found
     */
    public char getChar(String key, char def)
    {
        String value = strings.getProperty(key);
        
        if (value != null && value.length() > 0)
        {
            return value.charAt(0);
        }
        
        return def;
    }

    /**
     * Returns the character given the lookup key. If the value associated
     * with the key is a String of more than 1 character, only the first
     * character will be returned. If the key or value is not found,
     * (char)Character.UNASSIGNED will be returned.
     *
     * @param key reference in the string table
     * @param def character to return if not found in table
     * @return char found or default if not found
     */
    public char getChar(String key)
    {
        return getChar(key, (char)Character.UNASSIGNED);
    }
    
    /**
     * Sets the string lookup file to a new file.
     *
     * // FIXME - add assertions and preconditions
     *
     * @param file new string properties file
     */
    public void setTextFile(String file)
    {
        // NOTE - currently looking in the UI_CONFIG_DIR for the file - this
        // may be changed in the future depending on what uses StringManager.
        // - make sure the file exists and can be read
        // - if so, create a file input stream
        try
        {
            File f = new File(FsatsProperties.get("UI_CONFIG_DIR") +
                              File.separator + file);
            strings_in_file = new FileInputStream(f);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File Not Found.");
        }
        
        // - have the strings Properties load in the new file
        try
        {
            if(strings_in_file != null)
            {
                strings.load(strings_in_file);
                strings_in_file.close();
            }
        }
        catch(IOException e)
        {
            System.out.println("Load did not work.");
        }
    }
}
