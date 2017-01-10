package Jakarta.util;

// used for processing fatal errors
//
public class ExitError extends java.lang.Error {
   public ExitError( String msg ) { super(msg); }
   public ExitError( String msg, java.lang.Throwable cause ) {
      super(msg);
      setStackTrace( cause.getStackTrace() );
   }
   public ExitError( java.lang.Throwable cause ) { 
      super(cause.getMessage()); 
		setStackTrace( cause.getStackTrace() );
   }
}

