/**
 * class: JVLabel
 */

package ModelExplorer.MMatrixBrowser;

import javax.swing.*;
import java.awt.*;
import java.text.*;

public class JVLabel extends javax.swing.JLabel {
    public JVLabel(String s) { super(s);}
    public JVLabel(String s, int a) { super(s, a);}
	 public JVLabel(){super();}

    public java.awt.Dimension getPreferredSize() {
        java.awt.Insets ins = getInsets();
        java.awt.FontMetrics fm = getFontMetrics(getFont());
        String text = getText();
        int h = fm.stringWidth(text), descent = fm.getDescent(),
            ascent = fm.getAscent();
        return new java.awt.Dimension(ins.top + ascent + descent + ins.bottom,
            ins.right + h + ins.left);
    }

    public void paint(java.awt.Graphics g) {
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create();

        String text = getText();
        java.awt.Dimension size = getSize();
        java.awt.Insets ins = getInsets();

        java.awt.FontMetrics fm = g2d.getFontMetrics(getFont());
        int h = fm.stringWidth(text), x = ins.right;

        switch(getHorizontalAlignment()) {
          case javax.swing.SwingConstants.CENTER:
            x = (size.height - h + ins.right - ins.left) / 2;
            break;
          case javax.swing.SwingConstants.TOP:
            x = size.height - h - ins.left;
            break;
        }
	
        int descent = fm.getDescent(), ascent = fm.getAscent(),
            y = ins.top + ascent;
        switch(getVerticalAlignment()) {
          case javax.swing.SwingConstants.CENTER:
            y = (size.width + ascent - descent + ins.top - ins.bottom) / 2;
            break;
          case javax.swing.SwingConstants.RIGHT:
            y = size.width - descent - ins.bottom;
            break;
        }
		
        java.awt.geom.AffineTransform trans =
            new java.awt.geom.AffineTransform(0, -1, 1, 0, 0, size.height - 1);
        g2d.transform(trans);
        g2d.setPaintMode();
        if (isOpaque() && (getBackground() != null)) {
            g2d.setColor(getBackground());
            g2d.fillRect(-1, 0, size.height, size.width);
        }
        g2d.setFont(getFont());
        g2d.setColor(getForeground());
        g2d.drawString(text, x - 1, y);
        trans = null;
        g2d = null;
    }
}
