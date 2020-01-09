package gui;

import actor.Block;
import actor.Business;
import actor.Customer;
import actor.GameObject;
import grid.Grid;
import grid.Location;
import grid.BotWorldGrid;
import java.util.ArrayList;
import java.util.Random;
import world.World;

/**
 * The Arena class includes all of the methods needed to setup the arena 
 * according to the rules of the game.  
 * @author Spock
 */
public class RatBotsArena 
{
    /**
     * The size of a side of the central starting room in the arena. 
     */
    private Random randy = new Random();
    
    public static final int CHALLENGE_1 = 1; //Go to work
    public static final int CHALLENGE_2 = 2; //Start a business
    public static final int CHALLENGE_3 = 3; //Get Customers
    public static final int SANDBOX = 4; //SandBox Mode, 1 of everything to start
    public static final int START = 5;
    public static final int NORMAL = 6;
    
    public static final int JOB_WAGE = -20; //negative 'cost' ==> wage
    
    private static int playMode = START; 
    private static ArrayList<Location> subwayStations = new ArrayList<>();
    
    public void setPlayMode(int in) 
    { 
        playMode = in;
    }
    public static int getPlayMode()
    {
        return playMode;
    }
    public static ArrayList<Location> getSubwayStations()
    {
        return subwayStations;
    }
    //This method returns a randomly chosen subway different from currentDest
    public static Location getNewRandomSubwayStation(Location currentDest)
    {
        Location random = new Location(0,0);
        do
        {
            random = subwayStations.get((int)(Math.random()*subwayStations.size()));
        } while(currentDest.equals(random));
        return random;
    }
    
    /**
     * Toggles whether the grid will include Blocks or not.  
     * This is an option in the Arena menu.
     */
//    public void toggleShowBlocks(World world) 
//    { 
//        withBlocks = ! withBlocks; 
//    }

    /**
     * Initializes the Arena based on the selected rules.  
     * @param world the world that the Arena is within
     */
    public void initializeArena(World world)
    {
        BotWorldGrid grid = (BotWorldGrid)world.getGrid();
        grid.initBusinesses();
        if(playMode == NORMAL)
        {
            for(int z=0; z<100; z++)
                addBusinessCluster(world);
            addJobSites(world);
            addSubwayStations(world);
            for(int x=0; x<3;x++)
                new Customer().putSelfInGrid(grid, world.getRandomEmptyLocation());
            for(int x=0; x<3;x++)
                new Block().putSelfInGrid(grid, world.getRandomEmptyLocation());
        }
        else if(playMode == CHALLENGE_1)
        {   //Single Crop to get
            Location start = world.getRandomEmptyLocation();
        }
        else if(playMode == CHALLENGE_2)
        {   //Plant a Seed by a Lake
        }
        else if(playMode == CHALLENGE_3)
        {   //Connect a Lake to a Tree by Canals
            clearHighways(world);
            //have each Bot add a single Tree
        }
        else if(playMode == SANDBOX)
        {
            addOneOfEverything(world);            
        }
        else if(playMode == START)
        {
            addOneOfEverything(world);            
//            playMode = NORMAL;
        }
    }
    
    private void addSubwayStations(World world)
    {
        subwayStations.clear();
        int size = world.getGrid().getNumCols();
        
        int numToSkip = (int)(Math.random()*4)+1;
        
        if(numToSkip != 1)
            addMajorBusinessClose(world, new Location(2,size/2-1));
        if(numToSkip != 2)
            addMajorBusinessClose(world, new Location(size/2-1,size-3));
        if(numToSkip != 3)
            addMajorBusinessClose(world, new Location(size-3,size/2-1));
        if(numToSkip != 4)
            addMajorBusinessClose(world, new Location(size/2-1,2));
    }
    
    private void addMajorBusinessClose(World world, Location loc)
    {
        Location moved = new Location(loc.getRow()+(int)(Math.random()*4)-2,
                                      loc.getCol()+(int)(Math.random()*4)-2);
        BotWorldGrid grid = (BotWorldGrid)world.getGrid();
        if (!grid.isValid(moved))
            moved = loc;  //shouldn't need this...just incase.
        
        subwayStations.add(moved);
        for(int n=0;n<30;n++)
            addBusinessCluster(grid,moved);
        grid.getBusiness(moved).increaseChargeAmount(5000);
    }
    
    private void addBusinessCluster(World world)
    {
        BotWorldGrid grid = (BotWorldGrid)world.getGrid();
        Location center = grid.getRandomValidLocation();
        addBusinessCluster(grid, center);
    }
    
    private void addBusinessCluster(BotWorldGrid grid, Location center)
    {
        int centerValue = (int)(Math.random()*5)+1;  //1-7 size of cluster
        
        for(int row=center.getRow()-centerValue; 
                row<center.getRow()+centerValue; 
                row++)
        {
            for(int col=center.getCol()-centerValue;
                    col<center.getCol()+centerValue;
                    col++)
            {
                Location space = new Location(row,col);
                if(grid.isValid(space))
                {
                    int value = centerValue - center.distanceTo(space);
                    if(value > 0)
                    {
                        if(grid.getBusiness(space) == null)
                            grid.setBusiness(space, new Business());
                        grid.getBusiness(space).increaseChargeAmount(value);
                    }
                }
            }
        }
    }
    
    private void addJobSites(World world)
    {
        BotWorldGrid grid = (BotWorldGrid)world.getGrid();
        grid.setBusiness(new Location(0,0), new Business(JOB_WAGE));
        grid.setBusiness(new Location(0,grid.getNumCols()-1), new Business(JOB_WAGE));
        grid.setBusiness(new Location(grid.getNumRows()-1,0), new Business(JOB_WAGE));
        grid.setBusiness(new Location(grid.getNumRows()-1,grid.getNumCols()-1), new Business(JOB_WAGE));
    }
    
    private void addOneOfEverything(World world)
    {
//        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
//        Seed seed = new Seed();
//        seed.putSelfInGrid(grid, new Location(0,0));
//        Tree tree = new Tree();
//        tree.putSelfInGrid(grid, new Location(0,1));
//        Canal river = new Canal();
//        river.putSelfInGrid(grid, new Location(0,2));
//        DryCanal canal = new DryCanal();
//        canal.putSelfInGrid(grid, new Location(0,3));
//        Crop crop = new Crop();
//        crop.putSelfInGrid(grid, new Location(0,4));
//        Farm farm = new Farm();
//        farm.putSelfInGrid(grid, new Location(0,5));
//        Lake lake = new Lake();
//        lake.putSelfInGrid(grid, new Location(0,6));
//        Block b = new Block();
//        b.putSelfInGrid(grid, new Location(0,7));
    }
    
    private void clearHighways(World world)
    {
        int[] highways = {0,8,9,17};
        Grid gr = world.getGrid();

        for(int z=0; z<18; z++)
        {
            for(int q : highways)
            {
                GameObject g = (GameObject)gr.get(new Location(q,z));
                if (g != null)
                    g.removeSelfFromGrid();
                g = (GameObject)gr.get(new Location(z,q));
                if (g != null)
                    g.removeSelfFromGrid();
            }
        }
    }
    
    private Location getRandomNonHighwayLocation(World world)
    {
        Grid gr = world.getGrid();

        ArrayList<Location> emptyLocs = new ArrayList<Location>();
        //Ignore the ouside rim...
        for (int i = 1; i < 17; i++)
            for (int j = 1; j < 17; j++)
            {
                if(i!=8 && i!=9 && j!=8 && j!=9)
                {
                    Location loc = new Location(i, j);
                    if (gr.isValid(loc) && gr.get(loc) == null)
                        emptyLocs.add(loc);
                }
            }
            if (emptyLocs.size() == 0)
                return null;
            
            int r = randy.nextInt(emptyLocs.size());
            return emptyLocs.get(r);
    }
    
    
}
