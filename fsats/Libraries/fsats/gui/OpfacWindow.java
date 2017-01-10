package fsats.gui;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import javax.swing.table.*;
import java.awt.Color;

import fsats.gui.event.*;

/** Window for displaying an opfac with its list of missions.
 */
class OpfacWindow 
    extends ListWindow
{
    OpfacWindow(Opfac opfac)
    {
        super("Opfac "+opfac.getSubscriber(), opfac);
        update(opfac);
        setVisible(true);
    }

    void update(Opfac opfac)
    {
        top.removeAll();
        top.add(new JLabel("Opfac ID: "+opfac.getSubscriber()));
        top.add(new JLabel("Type: "+opfac.getType()));
        Location l = opfac.getLocation();
        top.add(new JLabel("Latitude: "+ l.getLatitudeDMS()));
        top.add(new JLabel("Longitude: "+ l.getLongitudeDMS()));
        repaint();
    }
}


