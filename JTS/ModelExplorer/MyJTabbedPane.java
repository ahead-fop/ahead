package ModelExplorer;

import javax.swing.*;
import javax.swing.event.*;

// note: this class was added because in jdk1.6 
// JTabbedPane.remove() triggered a change event
// which wasn't present in jdk1.5.  This cause
// modelexplorer to fail.  Apparently setComponentAt()
// doesn't trigger a changedState event, so fire()
// is added to generate the event.

public class MyJTabbedPane extends JTabbedPane {
   public void fire() { fireStateChanged(); }
}
