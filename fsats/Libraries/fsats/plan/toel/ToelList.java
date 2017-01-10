package fsats.plan.toel;

import java.util.Vector;

import fsats.db.*;
import fsats.plan.TestPlan;

public class ToelList 
{
   /**
    *  Return true if the toelName exists in the database table.
    */
   public boolean exists(
      String toelName)
   {
      boolean answer = true;

      try
      {
         TestPlan plan = new TestPlan();
         //Database db = plan.getDC();
         //Database db = fsats.db.Database.getCommon();
         Database db = new Database();
         java.sql.Statement statement = db.getConnection().createStatement();
         java.sql.ResultSet result = 
            statement.executeQuery(
               "select count(*) from toel where toel_name='"+toelName+"'");

         result.next();
         answer = (result.getInt(1)==0) ? false : true;

         statement.close();
      }
      catch (java.sql.SQLException e)
      {
         System.err.println(e);
         answer = true;
      }
      finally
      {
         return answer;
      }
   }


   /**
    * Return an array of strings with the toel names.
    */
   public String[] getNames()
   {
      String[] names = new String[0];

      try
      {
         Vector nameVector = new Vector();

         TestPlan plan = new TestPlan();
         //Database db = plan.getDC();
         //Database db = fsats.db.Database.getCommon();
         Database db = new Database();
         java.sql.Statement statement = db.getConnection().createStatement();
         java.sql.ResultSet result =
            statement.executeQuery(
              "select toel_name from toel where check_pnt=0 order by toel_id");

         while (result.next())
         {
            nameVector.addElement(result.getString(1));
         }
         statement.close();

         names = new String[nameVector.size()];
         nameVector.copyInto(names);
      }
      catch (java.sql.SQLException e)
      {
         System.err.println(e);
      }
      finally
      {
         return names;
      }
   }

}
