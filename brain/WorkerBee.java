package brain;

import actor.BotBrain;
/**
 * @author Spock
 * WorkerBee goes to the job site at 0,0
 */
public class WorkerBee extends BotBrain
{        
    public int chooseAction()
    {
        //Moves up and over to 0,0 and stays there.  
        if(this.getRow()>0) return NORTH;
        if(this.getCol()>0) return WEST;
        return REST;
    }
}
    

