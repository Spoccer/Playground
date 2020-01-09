package brain;

import actor.BlockedLocation;
import actor.BotBrain;
import grid.Location;

/**
 *
 * @author spockm
 */
public class GoalFinder extends BotBrain
{
    //From BeenThereBot
    int[][] timesBeenThere = new int[18][18];
    Location goal = new Location(17,17);
    
    public void initForRound()
    {
        //reset the number of times I think I've been to spots.
        for(int row=0;row<18;row++)
            for(int col=0; col<18; col++)
                timesBeenThere[row][col]=0;
    }
    
    /**
     * The chooseAction method calculates the proper move
     * for this Bot to make in the current turn
     * @return the move to be made (as an integer) 
     */
    public int chooseAction()
    {
        setName("R:"+goal.getRow()+", C:"+goal.getCol());
        timesBeenThere[getRow()][getCol()]++;
        
        if(getLocation().equals(goal))
            goal = chooseGoal();
        
        //Here's the movement section -------------------
        int[] values = new int[4];
        //Calculate a point value for each of the four directions.  
        for(int z=0; z<4; z++)
        {
            int direction = z*90;
            Location neighbor = getLocation().getAdjacentLocation(direction); 
            values[z] = neighbor.distanceTo(goal);
            //find the distance from space to goal - add it to value.  
            if(!neighbor.isValidLocation())
                values[z] += 1000;
            else if(getArena()[neighbor.getRow()][neighbor.getCol()] instanceof BlockedLocation)
                values[z] += 1000;
            else    //penalize for how many times you've been to the space by adding to value.
                values[z]+=timesBeenThere[neighbor.getRow()][neighbor.getCol()];
        }
        //Find the lowest scoring of these values
        int lowestDir = 0;
        for(int z=0; z<4; z++)
        {
            if(values[z] < values[lowestDir])
                lowestDir = z;
        }
        return lowestDir*90; 
    }
    
    public Location getLocation()
    {
        return new Location(getRow(), getCol());
    }

    public Location chooseGoal()
    {
        //This is VERY weak, but just for a demo.  
        
        Location newGoal = new Location(3,3);
        //Choose new goal! 
        int loopCount=0;
        while(getArena()[newGoal.getRow()][newGoal.getCol()] != null && loopCount<20)
        {
            loopCount++;
            int goalRow = (int)(Math.random()*18);
            int goalCol = (int)(Math.random()*18);
            newGoal = new Location(goalRow, goalCol);
        }
        return newGoal;
    }
}
