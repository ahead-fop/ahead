package Begin;

import java.io.*;

// all elements that can be stored in a program text object
// (which is CH's internal representation of an HTML method)
// must implement this interface.  StringWrappers and calls
// are the current examples.

interface Action { 
                                  // inline into t the current action
                                  // using extension file f
   void inline( ProgramText t, HTMLfile f ); 
   String toString();             // convert into string
   void write( PrintWriter pw );  // print to pw
}
