package fsats.db;

public class PlanDatabaseCreationException extends Exception
{

    private Exception e;

    public PlanDatabaseCreationException( String msg )
    {
        super( msg );
    }

    public PlanDatabaseCreationException( Exception e )
    {
        this.e = e;
    }
        
    public PlanDatabaseCreationException( String msg, Exception e )
    {
        super( msg );
        this.e = e;
    }
        
    public void printStackTrace() 
    {
        super.printStackTrace();
        if ( e != null ) {
            System.err.println( "PlanDatabaseCreationException nested "
                                + "exception =" );
            e.printStackTrace();            
        }
    }

    public String toString()
    {
        if ( e != null )
            return super.toString() + ": nested exception = " + e.toString();
        else
            return super.toString();
    }

}
