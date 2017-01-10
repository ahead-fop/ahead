//                              -*- Mode: Java -*-
// Version         : 1.0
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Thu Mar 16 15:16:00 2000

package fsats.plan;

import fsats.db.*;
import fsats.measures.*;

import java.sql.*;

public class PlanDataAttributes
{
    public static PlanDataAttributes planDataAttributes = new PlanDataAttributes();

    double lowerLeftEasting, lowerLeftNorthing, lowerLeftAltitude;
    int lowerLeftGridZone;
    double upperRightEasting, upperRightNorthing, upperRightAltitude;
    int upperRightGridZone;

    private PlanDataAttributes()
    {
	String planId = FsatsDefaultDBinfo.getPlanAccount().substring(11);
	String query = "select pd_lower_left_easting, pd_lower_left_northing, "+
	    "pd_lower_left_grid_zone, pd_lower_left_altitude, "+
	    "pd_upper_right_easting, pd_upper_right_northing, "+
	    "pd_upper_right_grid_zone, pd_upper_right_altitude "+
	    "from plan_data_attributes where pd_id="+planId;

	try
	{
	    Database db = new Database();
	    ResultSet result = db.doQuery(query);

	    if(result.next())
	    {
		lowerLeftEasting = result.getDouble(1);
		lowerLeftNorthing = result.getDouble(2);
		lowerLeftGridZone = result.getInt(3);
		lowerLeftAltitude = result.getDouble(4);
		upperRightEasting = result.getDouble(5);
		upperRightNorthing = result.getDouble(6);
		upperRightGridZone = result.getInt(7);
		upperRightAltitude = result.getDouble(8);
	    }

	    result.close();
	    db.close();
	}
	catch(java.sql.SQLException e)
	{
	    System.err.println(e);
	}
    }

    public static PlanDataAttributes getInstance()
    {
	return planDataAttributes;
    }

    public double getLowerLeftEasting()
    {
	return lowerLeftEasting;
    }

    public double getLowerLeftNorthing()
    {
	return lowerLeftNorthing;
    }

    public int getLowerLeftGridZone()
    {
	return lowerLeftGridZone;
    }

    public double getLowerLeftAltitude()
    {
	return lowerLeftAltitude;
    }

    public double getLowerLeftLatitude()
    {
	return GeoUTMConversion.UTMtoLL(new UTMLocation(lowerLeftEasting,
							lowerLeftNorthing,
							lowerLeftGridZone)).latitude();
    }

    public double getLowerLeftLongitude()
    {
	return GeoUTMConversion.UTMtoLL(new UTMLocation(lowerLeftEasting,
							lowerLeftNorthing,
							lowerLeftGridZone)).longitude();
    }

    public double getUpperRightEasting()
    {
	return upperRightEasting;
    }

    public double getUpperRightNorthing()
    {
	return upperRightNorthing;
    }

    public int getUpperRightGridZone()
    {
	return upperRightGridZone;
    }

    public double getUpperRightAltitude()
    {
	return upperRightAltitude;
    }

    public double getUpperRightLatitude()
    {
	return GeoUTMConversion.UTMtoLL(new UTMLocation(upperRightEasting,
							upperRightNorthing,
							upperRightGridZone)).latitude();
    }

    public double getUpperRightLongitude()
    {
	return GeoUTMConversion.UTMtoLL(new UTMLocation(upperRightEasting,
							upperRightNorthing,
							upperRightGridZone)).longitude();
    }

    public static void main(String args[])
    {
	PlanDataAttributes planDataAttributes = PlanDataAttributes.getInstance();

	System.out.println("lower_left_easting = "+
			   planDataAttributes.getLowerLeftEasting());
	System.out.println("lower_left_northing = "+
			   planDataAttributes.getLowerLeftNorthing());
	System.out.println("lower_left_grid_zone = "+
			   planDataAttributes.getLowerLeftGridZone());
	System.out.println("lower_left_altitude = "+
			   planDataAttributes.getLowerLeftAltitude());
	System.out.println("lower_left_latitude = "+
			   planDataAttributes.getLowerLeftLatitude());
	System.out.println("lower_left_longitude = "+
			   planDataAttributes.getLowerLeftLongitude());
	System.out.println("upper_right_easting = "+
			   planDataAttributes.getUpperRightEasting());
	System.out.println("upper_right_northing = "+
			   planDataAttributes.getUpperRightNorthing());
	System.out.println("upper_right_grid_zone = "+
			   planDataAttributes.getUpperRightGridZone());
	System.out.println("upper_right_altitude = "+
			   planDataAttributes.getUpperRightAltitude());
	System.out.println("upper_right_latitude = "+
			   planDataAttributes.getUpperRightLatitude());
	System.out.println("upper_right_longitude = "+
			   planDataAttributes.getUpperRightLongitude());
    }
}
