//                              -*- Mode: Java -*-
// Version         : 1.1
// Author          : Bryan D. Hopkins
// Last Modified By: Bryan D. Hopkins
// Last Modified On: Tue Jan  4 11:26:00 2000

package fsats.plan;

/**
 * Wrapper class for String to be used in fsats.plan classes.
 */
public class PlanString
{
  private String value;

  private static NullPlanString nullPlanString=new NullPlanString();

  /**
   * Constructs a PlanString.
   *
   * @param value - the String wrapped by PlanString.
   */
  public PlanString(String value)
  {
    this.value=value;
  }

  /**
   * Returns the NullPlanString.
   */
  public static PlanString getNull()
  {
    return nullPlanString;
  }

  /**
   * Returns whether or not the PlanString is null.
   */
  public boolean isNull()
  {
    return value==null;
  }

  /**
   * Returns the String wrapped by the PlanString object.
   * Returns "" if null.
   */
  public String toString()
  {
    return value==null ? "" : value;
  }
}

/**
 * Represents a null-valued PlanString.
 */
class NullPlanString extends PlanString
{
  /**
   * Constructs a NullPlanString.
   */
  NullPlanString()
  {
    super("");
  }

  /**
   * Returns whether or not the NullPlanString is null.
   */
  public boolean isNull()
  {
    return true;
  }
}
