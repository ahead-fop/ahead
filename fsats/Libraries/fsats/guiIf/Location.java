package fsats.guiIf;

/** 
 * Interface for a position of an opfac or target.
 * Don't care if we use Lat/Long or UTM coordinates.
 * Prefer Lat/Longitude; it was used for illustration.
 */
public interface Location 
{
    float getLatitude();
    float getLongitude();
}
