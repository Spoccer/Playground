/*
 * 
 */

package gui;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class RatBotsColorScheme 
{
    private static final int NUM_SCHEMES = 4;
    
    private static Color[] backgroundColor = new Color[NUM_SCHEMES];
    private static Color[] neutralColor = new Color[NUM_SCHEMES];
    private Color[] jobSiteColor = new Color[NUM_SCHEMES];
    private Color[] blockColor= new Color[NUM_SCHEMES];
    private Color[] watermarkColor = new Color[NUM_SCHEMES];
    private static Color[] customerColor = new Color[NUM_SCHEMES];
    private Color[] gridLineColor = new Color[NUM_SCHEMES];
    private Color[] ratAliveColor = new Color[NUM_SCHEMES];
    private Color[] ratDeadColor = new Color[NUM_SCHEMES];
    
    private static int scheme;
    
    
    public RatBotsColorScheme()
    {
        scheme = 0;
        init();
    }
    
    public void changeScheme()
    {
        scheme++;
        if(scheme == NUM_SCHEMES)
            scheme = 0;
    }
    
    public static Color getBackgroundColor() { return backgroundColor[scheme]; }
    public static Color getNeutralColor() { return neutralColor[scheme]; }
    public Color getJobSiteColor() { return jobSiteColor[scheme]; }
    public Color getBlockColor() { return blockColor[scheme]; }
    public Color getDestinationHighlight() { return watermarkColor[scheme]; }
    public static Color getCustomerColor() { return customerColor[scheme]; }
    public Color getGridLineColor() { return gridLineColor[scheme]; }
    public Color getRatAliveColor() { return ratAliveColor[scheme]; }
    public Color getRatDeadColor() { return ratDeadColor[scheme]; }
    
    private void init()
    {
        //The default color scheme for 2015
        backgroundColor[0] = new Color(150,150,150);
        neutralColor[0] = Color.LIGHT_GRAY;
        jobSiteColor[0] = Color.YELLOW;
        blockColor[0] = Color.BLUE;
        watermarkColor[0] = new Color(100,50,255);
        customerColor[0] = Color.WHITE;
        gridLineColor[0] = Color.BLACK;
        ratAliveColor[0] = Color.BLACK;
        ratDeadColor[0] = Color.LIGHT_GRAY;
                
        //Alternate scheme
        backgroundColor[1] = Color.BLACK;
        neutralColor[1] = Color.YELLOW;
        jobSiteColor[1] = Color.BLUE;
        blockColor[1] = Color.GREEN;
        watermarkColor[1] = Color.BLUE;
        customerColor[1] = Color.LIGHT_GRAY;
        gridLineColor[1] = Color.DARK_GRAY;
        ratAliveColor[1] = Color.BLACK;
        ratDeadColor[1] = Color.LIGHT_GRAY;
                
        //Alternate (Inverse)
        backgroundColor[2] = Color.LIGHT_GRAY;
        neutralColor[2] = Color.BLACK;
        jobSiteColor[2] = Color.DARK_GRAY;
        blockColor[2] = Color.BLACK;
        watermarkColor[2] = Color.BLUE;
        customerColor[2] = Color.DARK_GRAY;
        gridLineColor[2] = Color.DARK_GRAY;
        ratAliveColor[2] = Color.BLACK;
        ratDeadColor[2] = Color.WHITE;
                
        //Alternate (Inverse)
        int n = 3;
        backgroundColor[n] = Color.WHITE;
        neutralColor[n] = Color.BLACK;
        jobSiteColor[n] = Color.DARK_GRAY;
        blockColor[n] = Color.BLACK;
        watermarkColor[n] = Color.BLUE;
        customerColor[n] = Color.LIGHT_GRAY;
        gridLineColor[n] = Color.DARK_GRAY;
        ratAliveColor[n] = Color.BLACK;
        ratDeadColor[n] = Color.WHITE;
        
    }
}
