package composer.unit ;

import composer.Path ;

import composer.algebra.AlgebraException ;
import composer.algebra.Unit ;

import composer.macro.MacroContext ;
import composer.macro.MacroEngine ;

import java.io.BufferedInputStream ;
import java.io.BufferedOutputStream ;
import java.io.BufferedReader ;
import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.InputStreamReader ;
import java.io.IOException ;
import java.io.OutputStreamWriter ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

final class VelocityFileUnitComposeFunction
extends AdjoinFileUnitComposeFunction {

    public VelocityFileUnitComposeFunction
    (Factory factory, List sources, Class klass) {
	super (factory, sources, klass) ;
    }

    private int count = 0 ;

    public void invoke (Unit targetUnit) throws AlgebraException {

	++ count ;
	if (count > 1)
	    throw new IllegalStateException ("entered velocity twice") ;

	super.invoke (targetUnit) ;

	File target = ((FileUnit) targetUnit) . getFile () ;
	try {
	    velocity (target) ;

	} catch (AlgebraException exception) {
	    throw exception ;

	} catch (Exception exception) {
	    throw new AlgebraException (
		fileMessage (target, "failed Velocity expansion"),
		exception
	    ) ;
	}
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    VelocityFileUnitComposeFunction (Factory factory, List sources) {
	this (factory, sources, VelocityFileUnit.class) ;
    }

    final private static void addModelFile (String name, List list) {
	File file = Path.getInstance().resolve (name) ;
	if (file != null)
	    list.add (file) ;
    }

    final private void macroExpand (List sources, File target)
    throws Exception {

	getLogger().info (filesComposed ("velocity", sources, target)) ;

        BufferedReader reader =
            new BufferedReader (
                new InputStreamReader (filesInputStream (sources))
            ) ;

        BufferedWriter writer =
            new BufferedWriter (
                new OutputStreamWriter (new FileOutputStream (target))
            ) ;

	try {
	    MacroContext context = new MacroContext (sources, target) ;
	    MacroEngine engine = MacroEngine.getInstance () ;
	    engine.evaluate (context, writer, target.getName (), reader) ;
	} finally {
	    writer.close () ;
	    reader.close () ;
	}
    }

    final private void velocity (File middle) throws Exception {

	List adjoinSources = new ArrayList () ;

	addModelFile (middle.getName () + ".prefix", adjoinSources) ;
	adjoinSources.add (middle) ;
	addModelFile (middle.getName () + ".suffix", adjoinSources) ;

	String baseName = middle.getName () ;
	int dot = baseName.lastIndexOf ('.') ;

	if (dot > 0) {
	    baseName = baseName.substring (0, dot) ;
	    File target = new File (middle.getParentFile (), baseName) ;
	    backupFile (target) ;
	    macroExpand (adjoinSources, target) ;
	    return ;
	}

	File target =
	    File.createTempFile (baseName, ".tmp", middle.getParentFile ()) ;

	macroExpand (adjoinSources, target) ;
	renameFile (target, middle.getName ()) ;
    }

}
