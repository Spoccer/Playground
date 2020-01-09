package brain;

import actor.BotBrain;
/**
 * @author Spock
 * RandomFarmer chooses a random move each turn.
 */
public class BusinessMaker extends BotBrain
{        
    public int chooseAction()
    {
        if(Math.random() > .9)
            return 2000;
        
        //Choose a random number 0, 90, 180 or 270...
        int randomDirection = (int)(Math.random()*4)*90;
        
        return randomDirection;
    }
}
    

