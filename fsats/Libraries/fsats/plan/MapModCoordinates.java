//                              -*- Mode: Java -*-
// Version         :
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Aug 31 15:07:01 1999

package fsats.plan;

import fsats.util.DataContainer;

/**
 * Provides an abstract interface
 * to the "map_mod_coordinates"
 * information in the test plan.
 */
public class MapModCoordinates
{
  private static MapModCoordinates nullMapModCoordinates=new NullMapModCoordinates();
  private PlanInteger lowerLeftAltitude;
  private PlanInteger lowerLeftEasting;
  private PlanInteger lowerLeftGridZone;
  private PlanInteger lowerLeftNorthing;
  private PlanInteger planID;
  private PlanInteger upperRightAltitude;
  private PlanInteger upperRightEasting;
  private PlanInteger upperRightGridZone;
  private PlanInteger upperRightNorthing;

  /**
   * Constructs a MapModCoordinates object.
   *
   * @param lowerLeftAltitude  - the lower left altitude.
   * @param lowerLeftEasting   - the lower left easting.
   * @param lowerLeftGridZone  - the lower left grid zone.
   * @param lowerLeftNorthing  - the lower left northing.
   * @param planID             - the plan ID.
   * @param upperRightAltitude - the upper right altitude.
   * @param upperRightEasting  - the upper right easting.
   * @param upperRightGridZone - the upper right grid zone.
   * @param upperRightNorthing - the upper right northing.
   */
  public MapModCoordinates(PlanInteger lowerLeftAltitude,
			   PlanInteger lowerLeftEasting,
			   PlanInteger lowerLeftGridZone,
			   PlanInteger lowerLeftNorthing,
			   PlanInteger planID,
			   PlanInteger upperRightAltitude,
			   PlanInteger upperRightEasting,
			   PlanInteger upperRightGridZone,
			   PlanInteger upperRightNorthing)
  {
    this.lowerLeftAltitude=lowerLeftAltitude;
    this.lowerLeftEasting=lowerLeftEasting;
    this.lowerLeftGridZone=lowerLeftGridZone;
    this.lowerLeftNorthing=lowerLeftNorthing;
    this.planID=planID;
    this.upperRightAltitude=upperRightAltitude;
    this.upperRightEasting=upperRightEasting;
    this.upperRightGridZone=upperRightGridZone;
    this.upperRightNorthing=upperRightNorthing;
  }

  /**
   * Returns the NullMapModCoordinates object.
   */
  public static MapModCoordinates getNull()
  {
    return nullMapModCoordinates;
  }

  /**
   * Returns whether or not the MapModCoordinates object is null.
   */
  public boolean isNull()
  {
    return false;
  }

  /**
   * Returns the lower left altitude.
   */
  public int getLowerLeftAltitude()
  {
    return lowerLeftAltitude.intValue();
  }

  /**
   * Returns the lower left easting.
   */
  public int getLowerLeftEasting()
  {
    return lowerLeftEasting.intValue();
  }

  /**
   * Returns the lower left grid zone.
   */
  public int getLowerLeftGridZone()
  {
    return lowerLeftGridZone.intValue();
  }

  /**
   * Returns the lower left northing.
   */
  public int getLowerLeftNorthing()
  {
    return lowerLeftNorthing.intValue();
  }

  /**
   * Returns the plan ID.
   * Returns -1 if the field is null
   * (which should never be the case).
   */
  public int getPlanID()
  {
    return planID.intValue();
  }

  /**
   * Returns the upper right altitude.
   */
  public int getUpperRightAltitude()
  {
    return upperRightAltitude.intValue();
  }

  /**
   * Returns the upper right easting.
   */
  public int getUpperRightEasting()
  {
    return upperRightEasting.intValue();
  }

  /**
   * Returns the upper right grid zone.
   */
  public int getUpperRightGridZone()
  {
    return upperRightGridZone.intValue();
  }

  /**
   * Returns the upper right northing.
   */
  public int getUpperRightNorthing()
  {
    return upperRightNorthing.intValue();
  }

  /**
   * Returns a String representation of the MapModCoordinates object.
   */
  public String toString()
  {
    return "map_mod_coordinates=(lower_left_altitude="+
      lowerLeftAltitude.intValue()+" lower_left_easting="+
      lowerLeftEasting.intValue()+" lower_left_grid_zone="+
      lowerLeftGridZone.intValue()+" lower_left_northing="+
      lowerLeftNorthing.intValue()+" plan_id="+planID.intValue()+
      " upper_right_altitude="+upperRightAltitude.intValue()+
      " upper_right_easting="+upperRightEasting.intValue()+
      " upper_right_grid_zone="+upperRightGridZone.intValue()+
      " upper_right_northing="+upperRightNorthing.intValue()+")";
  }
}

class NullMapModCoordinates extends MapModCoordinates
{
  /**
   * Constructs a NullMapModCoordinates object.
   */
  NullMapModCoordinates()
  {
    super(PlanInteger.getNull(), PlanInteger.getNull(),
	  PlanInteger.getNull(), PlanInteger.getNull(),
	  PlanInteger.getNull(),
	  PlanInteger.getNull(), PlanInteger.getNull(),
	  PlanInteger.getNull(), PlanInteger.getNull());
  }

  /**
   * Returns whether or not the NullMapModCoordinates object is null.
   */
  public boolean isNull()
  {
    return true;
  }
}
