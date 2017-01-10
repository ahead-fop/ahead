package composer.unit;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import java.io.File ;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;

import org.onekin.xak.*;
class Xak2FileUnitComposeFunction extends FileUnitComposeFunction {

    public Xak2FileUnitComposeFunction (Factory fact, List src, Class klass) {
    	super (fact, src, klass) ;
    }

    public void invoke (Unit targetUnit) throws AlgebraException {

    	File target = ((FileUnit) targetUnit) . getFile () ;
    	writeEquation (target) ;

    	List args = new ArrayList (4 + getSources().size ()) ;

		args.add ("-xak2") ;

		args.add ("-c") ;

		for (Iterator p = getSources().iterator () ; p.hasNext() ; )
			args.add (((FileUnit) p.next ()) . getFile() . getPath ()) ;

		args.add ("-o") ;
		args.add (target.getPath ()) ;
	
		StringBuffer buffer = new StringBuffer ("xak") ;
		for (Iterator p = args.iterator () ; p.hasNext () ; )
			 buffer.append(' ').append (p.next ()) ;
		getLogger().info (buffer.toString ()) ;

		try	{
		 backupFile (target) ;
		 String[] argArray = new String [args.size()] ;
		 // xak.Main.main (  ) ;
		 Xak composer = new Xak();		
		 composer.executeCommand((String[]) args.toArray (argArray));		 
		}
		catch (Exception except) {
		 throw new AlgebraException ("failed xak " + args,	except) ;
		}
    }
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    Xak2FileUnitComposeFunction (Factory factory, List sources) {
    	this (factory, sources, Xak2FileUnit.class) ;
    }

}