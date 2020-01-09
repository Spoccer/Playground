package brain;

import actor.BotBrain;
/**
 * @author Spock
 * RandomFarmer chooses a random move each turn.
 */
public class RandomBot extends BotBrain
{        
    public int chooseAction()
    {
        //Choose a random number 0, 90, 180 or 270...
        //These correspond to moving and leaving a Farm.  
        int randomDirection = (int)(Math.random()*4)*90;
        return randomDirection;
    }
}
    

