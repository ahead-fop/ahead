package Begin;

// an HTML object is (or HTML file is parsed into)
// a sequence of objects of type Decl
// each Decl object is expressable as a ProgramText object

import java.io.*;

public interface Decl {
   void write( PrintWriter pw );
   ProgramText inline( HTMLfile ext );  // perform composition with ext
}
