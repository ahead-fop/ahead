//                              -*- Mode: Java -*-
// Version         : @(#) $Revision: 1.1.1.1 $
// Author          : Theodore Cox Beckett
// Last Modified By: Theodore Cox Beckett
// Last Modified On: Mon Feb  8 10:35:16 1999

package fsats.gui;

import java.lang.Math;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.Icon;


public class OpfacIcon implements Icon
{
    // Role ID.  these mnemonics came from the old force view code.
    private final static int NULL_ROLE_ID = 0;
    private final static int OP = 1;
    private final static int ARMOR = 2;
    private final static int MECHANIZED = 3;
    private final static int MECHANIZED_MORTAR = 4;
    private final static int MLRS = 5;
    private final static int ARTILLERY = 6;
    private final static int RADAR_INST = 7;
    private final static int SURVEY = 8;
    private final static int METEOR = 9;
    private final static int MECHANIZED_ARTILLERY = 10;
    private final static int RADAR_POST = 11;
    private final static int NGF = 12;
    private final static int CAS = 13;

    // Echelon ID.  these mnemonics came from the old force view code.
    private final static int NULL_ECHELON_ID = 0;
    private final static int SQUAD = 1;
    private final static int SECTION = 2;
    private final static int PLATOON = 3;
    private final static int BATTERY = 4;
    private final static int BATTALION = 5;
    private final static int REGIMENT = 6;
    private final static int BRIGADE = 7;
    private final static int DIVISION = 8;
    private final static int CORPS = 9;

    // dimensions of the main symbol
    private int roleHeight = 25;
    private int roleWidth = (int) ( (4.0/3.0) * roleHeight);

    // height of the little symbol drawn on top of the main symbol
    private int echelonHeight = 7;

    // width of the string drawn at the lower left and lower right corners
    private int unitDesignationWidth = 28;

    // height of the string drawn beneath the main symbol
    private int iconNameHeight = 10;
    private   int iconWidth = roleWidth + (2 * unitDesignationWidth);
    private int iconHeight = roleHeight + echelonHeight + iconNameHeight;

    // see fsats_plan_master.opfac_types.opt_type_name
    private String typeName = "UNKNOWN";
    
    // derived from typeName. see fsats_plan_master.opfac_types.opt_icon_name
    private String iconName = "";
    
    // derived from typeName. see fsats_plan_master.opfac_types.opt_icon_name
    private int roleID = 0;
    
    // derived from typeName. see fsats_plan_master.opfac_types.opt_icon_name
    private int echelonID = 0;

    // string drawn at lower left corner of the main symbol
    private String unitLower = "";

    // string drawn at lower right corner of the main symbol
    private String unitHigher = "";

    // color of symbol interior
    private Color fillColor = Color.yellow;

    // color of symbol perimeter and drawn text
    private static final Color LINE_COLOR = Color.black;

    
    
    public OpfacIcon()
    {
        this( "FIST" );
    }
    
    public static OpfacIcon cloneIcon(OpfacIcon icon)
    {
      try{
        icon = (OpfacIcon)icon.clone();
      }catch(Exception ex){
        //I don't know what to do here!!
      }

      return icon;
    }
    
    public OpfacIcon( String typeName )
    {
        this( typeName, "", "" );
    }
        
    public OpfacIcon( String typeName, String unitLower, String unitHigher )
    {
        if ( typeName != null )
            this.typeName = typeName;
        if ( unitLower != null )            
            this.unitLower = unitLower;
        if ( unitHigher != null )
            this.unitHigher = unitHigher;
        
        setIconHeight( 32 );
        deriveDrawingParameters( typeName );
    }


    /**
     * Setting the icon's width causes the icon's height to be adjusted
     * proportionally.
     */
    public void setIconWidth( int width )
    {
        iconWidth = width;
        iconHeight = (int) ( (double)width / 2.0 );
        deriveSizeParameters();
    }

    /**
     * Setting the icon's height causes the icon's width to be adjusted
     * proportionally.
     */
    public void setIconHeight( int height )
    {
        iconHeight = height;
        iconWidth = 2 * height;
        deriveSizeParameters();
    }
    

    /**
     * Set the interior color of the main symbol.
     */
    public void setFillColor( Color color )
    {
        this.fillColor = color;
    }

    
    public void paintIcon( java.awt.Component c, Graphics g, int x, int y )
    {
        Color origColor = g.getColor();
        drawRole( g, x, y );
        drawEchelon( g, x, y );
        drawStringData( g, x, y );
        g.setColor( origColor );
    }

    public int getIconWidth()
    {
        return iconWidth;
    }

        
    public int getIconHeight()
    {
        return iconHeight;
    }

    public String toString()
    {
        String s = new String( "OpfacIcon[" + typeName + "]" );
        return s;
    }

    
    private void deriveSizeParameters()
    {
       roleHeight = (int) ( (double)iconHeight * 0.64 );
       roleWidth = (int) ( (double)iconWidth * 0.42 );
       echelonHeight = (int) ( (double)iconHeight * 0.15 );
       if ( unitLower.length() == 0 && unitHigher.length() == 0 )
           {
              unitDesignationWidth = 0;
              iconWidth = roleWidth;
           }
       else
           {
               unitDesignationWidth = (int) ( (double)iconWidth * 0.29 );
           }
       iconNameHeight = (int) ( (double)iconHeight * 0.21 );
    }

    private void drawRole( Graphics g, int x, int y )
    {
        x += unitDesignationWidth;
        y += echelonHeight;
        Polygon pg;

        // draw the perimeter of the symbol
        switch ( roleID )
            {
            case NULL_ROLE_ID:
            case ARMOR:
            case MECHANIZED:
            case MECHANIZED_MORTAR:
            case ARTILLERY:
            case MLRS:
            case SURVEY:
            case METEOR:
            case MECHANIZED_ARTILLERY:
            case NGF:
            case CAS:
                g.setColor( fillColor );
                g.fillRect( x, y, roleWidth, roleHeight );
                g.setColor( LINE_COLOR );
                g.drawRect( x, y, roleWidth, roleHeight );
                break;

            case OP:
                Polygon polygon = new Polygon();
                polygon.addPoint( x + (roleWidth / 2), y );
                polygon.addPoint( x, y + roleHeight );
                polygon.addPoint( x + roleWidth, y + roleHeight );
                polygon.addPoint( x + (roleWidth / 2), y );
                g.setColor( fillColor );
                g.fillPolygon( polygon );
                g.setColor( LINE_COLOR );
                g.drawPolygon( polygon );
                break;

            case RADAR_INST:
            case RADAR_POST:
                polygon = new Polygon();
                polygon.addPoint( x, y );
                polygon.addPoint( x + (roleWidth / 2), y + roleHeight );
                polygon.addPoint( x + roleWidth, y );
                polygon.addPoint( x, y );
                g.setColor( fillColor );
                g.fillPolygon( polygon );
                g.setColor( LINE_COLOR );
                g.drawPolygon( polygon );
                break;
		
            default:
                break;
            }
        

        // draw the interior of the symbol
        switch ( roleID )
            {
            case ARMOR:
                drawOval( g, x, y );
                break;

            case MECHANIZED:
                drawOval( g, x, y );
                drawX( g, x, y, roleWidth, roleHeight );
                break;
		
            case MECHANIZED_MORTAR:
                //drawOval( g, x, y );
                //drawX( g, x, y, roleWidth, roleHeight );
                drawArrow( g, x, y );
                break;

            case ARTILLERY:
                drawCannonBall( g, x + (roleWidth / 2) ,
                                y + (roleHeight / 2) );
                break;
		
            case MLRS:
                drawInvertedV( g, x, y + 3);
                drawInvertedV( g, x, y);
                drawCannonBall( g, x + (roleWidth / 2),
                                y + (roleHeight / 2) + 4 );
                break;
		
            case SURVEY:
                drawRoleIDString( g, "SVY", x + (roleWidth / 2),
                                  y + (roleHeight / 2) );
                break;
            case METEOR:
                drawRoleIDString( g, "MET", x + (roleWidth / 2),
                                  y + (roleHeight / 2) );
                break;
            case MECHANIZED_ARTILLERY:
                break;
            case NGF:
                drawRoleIDString( g, "NGF", x + (roleWidth / 2),
                                  y + (roleHeight / 2) );
                break;
            case CAS:
                drawRoleIDString( g, "CAS", x + (roleWidth / 2),
                                  y + (roleHeight / 2) );
                break;
            case OP:
                break;
            case RADAR_INST:
            case RADAR_POST:
                drawRadarSymbol( g, x, y );
                break;
		
            default:
                break;
            }
    }

    
    private void drawEchelon( Graphics g, int x, int y )
    {
        g.setColor( LINE_COLOR );
        x += unitDesignationWidth + (roleWidth / 2);
        y += 0;
        int diameter = echelonHeight;
        int radius = diameter / 2;
        int width = echelonHeight - 1;
        int height = echelonHeight - 1;

        switch ( echelonID )
            {
            case SQUAD:
                g.fillOval( x - radius, y, diameter, diameter );
                break;
            case SECTION:
                g.fillOval( x - diameter - 2, y, diameter, diameter );
                g.fillOval( x + 2, y, diameter, diameter );
                break;
            case PLATOON:
                g.fillOval( x - diameter - radius - 2, y,
                            diameter, diameter );
                g.fillOval( x - radius, y, diameter, diameter );
                g.fillOval( x + radius + 2, y, diameter, diameter );
                break;
            case BATTERY:
                g.drawLine( x, y, x, y + echelonHeight );
                break;
            case BATTALION:
                g.drawLine( x - 2, y, x - 2, y + echelonHeight );
                g.drawLine( x + 2, y, x + 2, y + echelonHeight );
                break;
            case REGIMENT:
                g.drawLine( x - 4, y, x - 4, y + echelonHeight );
                g.drawLine( x, y, x, y + echelonHeight );
                g.drawLine( x + 4, y, x + 4, y + echelonHeight );
                break;
            case BRIGADE:
                drawX( g, x - (width / 2), y, width, height );
                break;
            case DIVISION:
                drawX( g, x - width - 1, y, width, height );
                drawX( g, x + (width / 2) + 1, y, width, height );
                break;
            case CORPS:
                drawX( g, x - (width / 2) - width - 2, y, width, height );
                drawX( g, x - (width / 2), y, width, height );
                drawX( g, x + (width / 2) + 2, y, width, height );
                break;
            default:
                break;
            }
    }


    private final static Font dataFont = new Font("SansSerif", Font.PLAIN, 8);

    
    private void drawStringData( Graphics g, int x, int y )
    {
        g.setColor( LINE_COLOR );
        Font origFont = g.getFont();
        FontMetrics fm = g.getFontMetrics( dataFont );
        int ascent = fm.getAscent();
        int iconNameWidth = 0;
        if (iconName != null)
            iconNameWidth = fm.stringWidth( iconName );
        int unitLowerWidth = 0;
        if (unitLower.length() > 0)
            unitLowerWidth = fm.stringWidth( unitLower );
        int unitHigherWidth = 0;
        if (unitHigher.length() > 0)
            unitHigherWidth = fm.stringWidth( unitHigher );
        int icon_x = x + (iconWidth / 2) - (iconNameWidth / 2);
        int icon_y = y + roleHeight + echelonHeight + ascent;
        int unitLower_x = x + unitDesignationWidth - unitLowerWidth - 1;
        int unitHigher_x = x + unitDesignationWidth + roleWidth + 1;
        int unit_y = y + echelonHeight + roleHeight;

        g.setFont( dataFont );
        if (iconName != null)
            g.drawString( iconName, icon_x, icon_y );
        if (unitLower.length() > 0)
            g.drawString( unitLower, unitLower_x, unit_y );
        if (unitHigher.length() > 0)
            g.drawString( unitHigher, unitHigher_x, unit_y );
        g.setFont( origFont );
    }

    private final static Font roleFont = new Font("SansSerif", Font.PLAIN, 12);

    private void drawRoleIDString( Graphics g, String s, int x, int y )
    {
        Font origFont = g.getFont();
        FontMetrics fm = g.getFontMetrics( roleFont );
        int height = fm.getAscent();
        int width = fm.stringWidth( s );
        int x1 = x - (width / 2);
        int y1 = y + (height / 2);

        g.setFont( roleFont );
        g.drawString( s, x1, y1 );  
        g.setFont( origFont );
    }

    private void drawArrow( Graphics g, int x, int y )
    {
        int x1 = x + (roleWidth / 2);
        int y1 = y + 1;
        int y2 = y + roleHeight - 2;
        int diameter = echelonHeight;
        int radius = diameter / 2;
	
        g.drawLine( x1, y1, x1, y2 );
        g.drawLine( x1 - 4, y1 + 4, x1, y1 );
        g.drawLine( x1 + 4, y1 + 4, x1, y1 );
        g.fillOval( x1 - radius, y2 - diameter + 2, diameter, diameter );
    }
    
    private void drawInvertedV( Graphics g, int x, int y )
    {
        int cannonballRadius = getCannonballDiameter() / 2;
        int x1 = x + (roleWidth / 2) - cannonballRadius - 6;
        int y1 = y + (roleHeight / 2);
        int x2 = x + (roleWidth / 2);
        int y2 = y + (roleHeight / 2) - cannonballRadius - 5;
        int x3 = x + (roleWidth / 2) + cannonballRadius + 6;
        int y3 = y1;

        g.drawLine( x1, y1, x2, y2 );
        g.drawLine( x2, y2, x3, y3 );	
    }
	 
    private int getCannonballDiameter()
    {
        return (int) (roleHeight * .35);
    }
    
    private void drawRadarSymbol( Graphics g, int x, int y )
    {
        int width = (int) (roleWidth * .5);
        int height = (int) (roleHeight * .5);
        int x1 = x + (roleWidth / 3);
        int y1 = y + (int) (roleHeight * .4);

        g.drawArc( x1, y, width, height, 170, 110 );
        g.drawLine( x1 + 2, y1, x1 + 6, y1 - 5 );
        g.drawLine( x1 + 6, y1 - 5, x1 + 7, y1 - 2 );
        g.drawLine( x1 + 7, y1 - 2, x1 + 11, y1 - 7 );
    }

    private void drawCannonBall( Graphics g, int x, int y )
    {
        int diameter = getCannonballDiameter();
        int radius = diameter / 2;
        g.fillOval( x - radius, y - radius, diameter, diameter );
    }

    /**
     * x, y is the location of the perimeter boundary
     */
    private void drawOval ( Graphics g, int x, int y )
    {
        int oval_width = (int) (roleWidth * .8);
        int oval_height = (int) (roleHeight * .55);
        int oval_x = x + (roleWidth - oval_width) / 2;
        int oval_y = y + (roleHeight - oval_height) / 2;
	
        g.drawOval( oval_x, oval_y, oval_width, oval_height );
    }

    /**
     * x, y is the location of the perimeter boundary
     */    
    private void drawX ( Graphics g, int x, int y, int width, int height )
    {
        g.drawLine( x, y, x + width, y + height ); 
        g.drawLine( x, y + height, x + width, y ); 
    }
    

    private void deriveDrawingParameters( String typeName )
    {
        /*********
         * set opfac type related instance variables.  this information
         * is in database table opfac_types.  because we are trying to break the
         * UI's dependency of the database and because this data rarely changes, if ever, 
         * the data in the table is stored in code here.  to generate the code
         * from the data stored in the database, run the query below and
         * paste into the code.
         set pages 1000;
         set lines 300;
         spool javacode;
         select 'else if ( typeName.equalsIgnoreCase("' ||opt_type_name||'") )'
         || chr(12) || '   { iconName="' || opt_icon_name || '"; roleID='
         || opt_role_id || '; echelonID=' || opt_echelon_id || '; }'    
         from opfac_types order by opt_type_name;
         spool off;
         ******/
        if ( typeName.equalsIgnoreCase("ARM_FIST") )
            { iconName="FIST"; roleID=2; echelonID=4; }
        else if ( typeName.equalsIgnoreCase("ATHS") )
            { iconName="ATHS"; roleID=1; echelonID=0; }
        else if ( typeName.equalsIgnoreCase("BDE_FSE") )
            { iconName="FSE"; roleID=3; echelonID=7; }
        else if ( typeName.equalsIgnoreCase("BN_FSE") )
            { iconName="FSE"; roleID=3; echelonID=5; }
        else if ( typeName.equalsIgnoreCase("CAS") )
            { iconName="ALO"; roleID=13; echelonID=2; }
        else if ( typeName.equalsIgnoreCase("COLT") )
            { iconName="COLT"; roleID=1; echelonID=0; }
        else if ( typeName.equalsIgnoreCase("CORPS_ARTY_CF") )
            { iconName="CFO"; roleID=6; echelonID=9; }
        else if ( typeName.equalsIgnoreCase("CORPS_ARTY_CP") )
            { iconName="OPS"; roleID=6; echelonID=9; }
        else if ( typeName.equalsIgnoreCase("CORPS_FSE_MAIN") )
            { iconName="FSE MAIN"; roleID=3; echelonID=9; }
        else if ( typeName.equalsIgnoreCase("CORPS_FSE_REAR") )
            { iconName="FSE REAR"; roleID=3; echelonID=9; }
        else if ( typeName.equalsIgnoreCase("CORPS_FSE_TAC") )
            { iconName="FSE TAC"; roleID=3; echelonID=9; }
        else if ( typeName.equalsIgnoreCase("DIVARTY_CF") )
            { iconName="CFO"; roleID=6; echelonID=8; }
        else if ( typeName.equalsIgnoreCase("DIVARTY_CP") )
            { iconName="OPS"; roleID=6; echelonID=8; }
        else if ( typeName.equalsIgnoreCase("DIV_FSE_MAIN") )
            { iconName="FSE MAIN"; roleID=3; echelonID=8; }
        else if ( typeName.equalsIgnoreCase("DIV_FSE_REAR") )
            { iconName="FSE REAR"; roleID=3; echelonID=8; }
        else if ( typeName.equalsIgnoreCase("DIV_FSE_TAC") )
            { iconName="FSE TAC"; roleID=3; echelonID=8; }
        else if ( typeName.equalsIgnoreCase("DS_BN_CP") )
            { iconName="DS"; roleID=6; echelonID=5; }
        else if ( typeName.equalsIgnoreCase("DS_FA_PLT") )
            { iconName="DS"; roleID=6; echelonID=3; }
        else if ( typeName.equalsIgnoreCase("FA_BDE_CP") )
            { iconName="FA BDE"; roleID=6; echelonID=7; }
        else if ( typeName.equalsIgnoreCase("FA_BTRY") )
            { iconName=" "; roleID=6; echelonID=4; }
        else if ( typeName.equalsIgnoreCase("FA_PLT") )
            { iconName=" "; roleID=6; echelonID=3; }
        else if ( typeName.equalsIgnoreCase("FA_SEC") )
            { iconName=" "; roleID=6; echelonID=2; }
        else if ( typeName.equalsIgnoreCase("FIST") )
            { iconName="FIST"; roleID=2; echelonID=4; }
        else if ( typeName.equalsIgnoreCase("FO") )
            { iconName="FO"; roleID=1; echelonID=0; }
        else if ( typeName.equalsIgnoreCase("GSM") )
            { iconName="GSM"; roleID=7; echelonID=2; }
        else if ( typeName.equalsIgnoreCase("GSR_BN_CP") )
            { iconName="GSR"; roleID=6; echelonID=5; }
        else if ( typeName.equalsIgnoreCase("GSR_FA_PLT") )
            { iconName="GSR"; roleID=6; echelonID=3; }
        else if ( typeName.equalsIgnoreCase("GS_BN_CP") )
            { iconName="GS"; roleID=6; echelonID=5; }
        else if ( typeName.equalsIgnoreCase("GS_FA_PLT") )
            { iconName="GS"; roleID=6; echelonID=3; }
        else if ( typeName.equalsIgnoreCase("JSTARS") )
            { iconName="JSTARS"; roleID=11; echelonID=0; }
        else if ( typeName.equalsIgnoreCase("LANCE_BN") )
            { iconName="LANCE"; roleID=5; echelonID=5; }
        else if ( typeName.equalsIgnoreCase("LANCE_BTRY") )
            { iconName="LANCE"; roleID=5; echelonID=4; }
        else if ( typeName.equalsIgnoreCase("MECH_FIST") )
            { iconName="FIST"; roleID=3; echelonID=4; }
        else if ( typeName.equalsIgnoreCase("MET_SEC") )
            { iconName="MET"; roleID=9; echelonID=2; }
        else if ( typeName.equalsIgnoreCase("MLRS_BN") )
            { iconName="MLRS"; roleID=5; echelonID=5; }
        else if ( typeName.equalsIgnoreCase("MLRS_BTRY") )
            { iconName="MLRS"; roleID=5; echelonID=4; }
        else if ( typeName.equalsIgnoreCase("MLRS_PLT") )
            { iconName="MLRS"; roleID=5; echelonID=3; }
        else if ( typeName.equalsIgnoreCase("MLRS_SEC") )
            { iconName=" "; roleID=5; echelonID=2; }
        else if ( typeName.equalsIgnoreCase("MTR_PLT") )
            { iconName=" "; roleID=4; echelonID=3; }
        else if ( typeName.equalsIgnoreCase("NGF") )
            { iconName="NGLO"; roleID=12; echelonID=2; }
        else if ( typeName.equalsIgnoreCase("Q_36") )
            { iconName="Q36"; roleID=11; echelonID=0; }
        else if ( typeName.equalsIgnoreCase("Q_37") )
            { iconName="Q37"; roleID=11; echelonID=0; }
        else if ( typeName.equalsIgnoreCase("R_BN_CP") )
            { iconName="R"; roleID=6; echelonID=5; }
        else if ( typeName.equalsIgnoreCase("R_FA_PLT") )
            { iconName="R"; roleID=6; echelonID=3; }
        else if ( typeName.equalsIgnoreCase("SURVEY_SEC") )
            { iconName="SVY"; roleID=8; echelonID=2; }
        else if ( typeName.equalsIgnoreCase("UNKNOWN") )
            { iconName="TOP"; roleID=0; echelonID=1000; }
        else
            { iconName=""; roleID=0; echelonID=0; }

    }

}
