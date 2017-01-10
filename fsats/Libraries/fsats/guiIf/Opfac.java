package fsats.guiIf;

/** Information about an opfac. */
public interface Opfac
{
    /** Name of Opfac. */
    String getId();

    /** Type of opfac: is FO, FIST, BN_FSE, BDE_FSE, ... */
    String getType();

    /** Location of opfac. */
    Location getLocation();

    /** Missions at opfac. */
    Mission[] getActiveMissions();

    /** Send a target to an opfac. */
    void putTarget(String id, Target target);
}
