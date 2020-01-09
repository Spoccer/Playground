package grid;

/* A RatBotsGrid is adapted from the BoundedGrid class. 
 * 
 * The code for this class is adapted from the BoundedGrid class in the 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2002-2006 College Entrance Examination Board 
 * (http://www.collegeboard.com).
 * @author Alyce Brady
 * @author APCS Development Committee
 * @author Cay Horstmann
 * 
 * adapted by Spock
 */


import actor.Bot;
import actor.Business;
import java.util.ArrayList;
import java.util.Random;

/**
 * A <code>RatBotsGrid</code> is a rectangular grid with a finite number of
 * rows and columns. <br />
 * It contains objects both on the grid as well as 'off-grid' objects.  
 * @param <E> 
 */
public class BotWorldGrid<E> extends AbstractGrid<E>
{
    private Object[][] occupantArray; // the array storing the grid elements
    private Business[][] spaceCost; //for 2020 BotWorld
    private ArrayList<Advertisement> ads; //for 2020 BotWorld
    private ArrayList<ScoringNotification> recentScores;
    private String message;
    

    /**
     * Constructs an empty bounded grid with the given dimensions.
     * (Precondition: <code>rows > 0</code> and <code>cols > 0</code>.)
     * @param rows number of rows in RatBotsGrid
     * @param cols number of columns in RatBotsGrid
     */
    public BotWorldGrid(int rows, int cols)
    {
        if (rows <= 0)
            throw new IllegalArgumentException("rows <= 0");
        if (cols <= 0)
            throw new IllegalArgumentException("cols <= 0");
        occupantArray = new Object[rows][cols];
        recentScores = new ArrayList<ScoringNotification>();
        spaceCost = new Business[rows][cols];
        ads = new ArrayList<>();
//        initBusinesses();
    }
    
    public ArrayList<E> getNeighborsSquare(Location loc)
    {
        ArrayList<E> neighborsSquare = new ArrayList<E>();
        for (int d = Location.NORTH; d < Location.FULL_CIRCLE; d+=Location.RIGHT)
        {
            Location neighborLoc = loc.getAdjacentLocation(d);
            if (isValid(neighborLoc))
                if (get(neighborLoc) != null)
                    neighborsSquare.add(get(neighborLoc));
        }
        return neighborsSquare;
    }

    @Override
    public int getNumRows()
    {
        return occupantArray.length;
    }

    @Override
    public int getNumCols()
    {
        // Note: according to the constructor precondition, numRows() > 0, so
        // theGrid[0] is non-null.
        return occupantArray[0].length;
    }

    @Override
    public boolean isValid(Location loc)
    {
        return 0 <= loc.getRow() && loc.getRow() < getNumRows()
                && 0 <= loc.getCol() && loc.getCol() < getNumCols();
    }

    @Override
    public ArrayList<Location> getOccupiedLocations()
    {
        ArrayList<Location> theLocations = new ArrayList<Location>();

        // Look at all grid locations.
        for (int r = 0; r < getNumRows(); r++)
        {
            for (int c = 0; c < getNumCols(); c++)
            {
                // If there's an object at this location, put it in the array.
                Location loc = new Location(r, c);
                if (get(loc) != null) 
                    theLocations.add(loc);
            }
        }

        return theLocations;
    }

    @Override
    public E get(Location loc)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
                
        return (E) occupantArray[loc.getRow()][loc.getCol()]; // unavoidable warning
    }

    @Override
    public E put(Location loc, E obj)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        if (obj == null)
            throw new NullPointerException("obj == null");

        // Add the object to the grid.
        E oldOccupant = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = obj;
        return oldOccupant;
    }

    @Override
    public E remove(Location loc)
    {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        
        // Remove the object from the grid.
        E r = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = null;
        return r;
    }
    /**
     * Gets all of the Rats that are in the Grid
     * @return an ArrayList filled with all Rats in this grid.
     */
    public ArrayList<Bot> getAllRats()
    {
        ArrayList<Bot> rats = new ArrayList<Bot>();
        
        ArrayList<Location> occupied = getOccupiedLocations();
        
        for(Location loc : occupied)
        {
            if(get(loc) instanceof Bot)
                rats.add((Bot)get(loc));
        }
        
        return rats;
    }
    
    
    
    public boolean isInCorner(Location loc)
    {
        return (loc.getCol() == 0 || loc.getCol() == getNumCols()-1) && 
                (loc.getRow() == 0 || loc.getRow() == getNumRows()-1);
    }
        
    private boolean messageFlag = false;
    public void setMessage(String in)
    {
        messageFlag = true; //turn this on when a new message is waiting.
        message = in;
    }
    public boolean isMessageWaiting()
    {
        return messageFlag;
    }
    public String getMessage()
    {
        messageFlag = false;
        return message;
    }
    
    public void addScoringNotification(ScoringNotification sn)
    {
        recentScores.add(sn);
    }
    
    public ArrayList<ScoringNotification> getScoringNotifications()
    {
        return recentScores;
    }
    
    public void clearScoringNotifications()
    {
        recentScores.clear();
    }
    
    //================================================
    private static Random randy = new Random();
    
    public Business getBusiness(Location loc)
    {
        if(loc.isValidLocation())
            if(spaceCost[loc.getRow()][loc.getCol()] != null)
                return spaceCost[loc.getRow()][loc.getCol()];
        return null;
    }
    public void setBusiness(Location loc, Business b)
    {
        if(loc.isValidLocation())
            spaceCost[loc.getRow()][loc.getCol()] = b;        
    }
    
    public void initBusinesses()
    {
        // Look at all grid locations.
        for (int r = 0; r < getNumRows(); r++)
        {
            for (int c = 0; c < getNumCols(); c++)
            {
                spaceCost[r][c] = new Business();
            }
        }
    }

    
    public Location getRandomValidLocation()
    {
        int row = (int)(Math.random()*occupantArray.length);
        int col = (int)(Math.random()*occupantArray[0].length);
        return new Location(row,col);
    }
       
    
    public ArrayList<Advertisement> getAds()
    {
        sortAds();
//        for(Advertisement a : ads)
//            System.out.println("AD:"+a);
        return ads;
    }
    public void clearAds()
    {
        ads.clear();
    }
    public void addAd(Advertisement ad)
    {
//        System.out.println("Added:"+ad);
        ads.add(ad);
    }
    public void sortAds()
    {
        for(int q=0; q<ads.size(); q++)
        {
            for(int z=0; z<ads.size()-1; z++)
            {
                if(ads.get(z).getAmount() > ads.get(z+1).getAmount())
                {
                    Advertisement temp = ads.get(z);
                    ads.set(z,ads.get(z+1));
                    ads.set(z+1, temp);
                }
            }
        }
    }
//    public boolean isInRoom(Location loc)
//    {
//        for(Location prize : getSuperPrizeLocations())
//        {
//            if(loc.distanceTo(prize) <= 2)
//                return true;
//        }
//        return false;
//    }
}
