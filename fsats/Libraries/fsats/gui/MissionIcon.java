package fsats.gui;

import java.lang.Math;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.Icon;

public class MissionIcon implements Icon
{
  private int targetID = 0;    
  private String targetNumber = "";
  private boolean isActive = false;
  
  // color of symbol interior
  private Color fillColor = Color.yellow;

    public MissionIcon() {}

    /**
     * @param targetNumber readable target identifier.
     * @param targetID unique identifier of target.
     */
    public MissionIcon( String targetNumber, int targetID, boolean isActive )
    {
        this.targetNumber = targetNumber==null ? "" : targetNumber;
        this.targetID = targetID;
        this.isActive = isActive;
    }
    
    /**
     * Setting the icon's height causes the icon's width to be adjusted
     * proportionally.
     */
    public void setIconHeight(int height) { setIconWidth(height); }

    public void setIconWidth(int width)
    {
        iconSize = width<minSize ? minSize : width;
    }
   
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(iconSize, iconSize);
    }
        
    public String toString()
    {
      String s = new String( "TargetSymbol[" + targetID + ',' +
                             targetNumber + ',' + isActive + ']' );
      return s;
    }

    
    public int getTargetID()
    {
        return targetID;
    }

    
    public String getTargetNumber()
    {
        return targetNumber;
    }

    public boolean getIsActive()
    {
        return isActive;
    }

    /**
     * Set the interior color of the main symbol.
     */
    public void setFillColor( Color color )
    {
        this.fillColor = color;
    }

    
    private final static Font useFont = new Font("SansSerif", Font.PLAIN, 10);
    private static boolean sizeAdjusted = false;
    private static int iconSize=32;
    private static int minSize=32;

    private static void adjustSize(Graphics g)
    {
        if (!sizeAdjusted)
            synchronized (useFont)
            {
                FontMetrics fm=g.getFontMetrics(g.getFont());
                minSize = fm.stringWidth("MM9999");
                if (iconSize<minSize) iconSize=minSize;
                sizeAdjusted=true;
            }
    }

    public void paintIcon(java.awt.Component c, Graphics g, int x, int y )
    {
      // Save original settings.
      Font originalFont = g.getFont();
      Color originalColor = g.getColor();

      // Set font for target number and resize if needed.
      g.setFont(useFont);
      adjustSize(g);
      int numberWidth = g.getFontMetrics(useFont).stringWidth(targetNumber);
      int half=iconSize/2;

      // Draw cross.
      g.setColor(Color.red);
      g.drawLine(x, y+half, x+iconSize, y+half);
      g.drawLine(x+half, y, x+half, y+iconSize);

      // Draw target number at bottom.
      g.setColor(Color.black);
      g.drawString(targetNumber, x+half-numberWidth/2, y+iconSize);

      // restore original settings.
      g.setColor(originalColor);
      g.setFont(originalFont);
    }

    public int getIconWidth() { return iconSize; }

    public int getIconHeight() { return iconSize; }
}
