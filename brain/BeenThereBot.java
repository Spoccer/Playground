package brain;

import actor.BlockedLocation;
import actor.BotBrain;
import grid.Location;

/**
 *
 * @author spockm
 */
public class BeenThereBot extends BotBrain
{
    int[][] timesBeenThere = new int[18][18];
    
    public void initForRound()
    {
        //reset the number of times I think I've been to spots.
        for(int row=0;row<18;row++)
            for(int col=0; col<18; col++)
                timesBeenThere[row][col]=0;
    }

    
    public int chooseAction()
    {
        //Increase the count of times I have been to the spot I'm on.
        timesBeenThere[getRow()][getCol()]++; 
        
        //Choose a random number 0, 90, 180 or 270...
        //These correspond to moving and leaving a Farm.  
        int randomDirection = (int)(Math.random()*4)*90;
           
        int loopCount = 0;
        //If I can't move the direction chosen, choose again! 
        while(!shouldMove(randomDirection) && loopCount < 10)
        {
            randomDirection = (int)(Math.random()*4)*90;
            loopCount++; 
        }    
        
        return randomDirection;
    }

    /**
     * This is like the method canMove from SmartRandom, 
     * except it also avoids spaces it has been to more
     * than 2 times.  (Which isn't super effective.) 
     * @param direction
     * @return true if the Bot should move this direction.
     */
    public boolean shouldMove(int direction)
    {
        Location myLoc = new Location(getRow(), getCol());
        Location next = myLoc.getAdjacentLocation(direction);
        if(!next.isValidLocation())
            return false;
        if(getArena()[next.getRow()][next.getCol()] instanceof BlockedLocation)
            return false;
        if(timesBeenThere[next.getRow()][next.getCol()] > 2)
            return false;
        
        return true;
    }


}
