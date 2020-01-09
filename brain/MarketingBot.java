package brain;

import actor.BotBrain;
import actor.Business;
/**
 * @author Spock
 * RandomFarmer chooses a random move each turn.
 */
public class MarketingBot extends BotBrain
{        
    int adTimer = 0;
    
    public int chooseAction()
    {
        //Choose a random number 0, 90, 180 or 270...
        int randomDirection = (int)(Math.random()*4)*90;
        
        Business b = getBusinessGrid()[getRow()][getCol()];
        if(b != null && !b.getColor().equals(getColor()))
        {
            return 2000; //Start a business here
        }
        if(b != null && b.getColor().equals(getColor()))
        {
            if(b.getChargeAmount()<150)
                return REST;
        }
        adTimer++;
        if(adTimer%3 == 0)
        {
//            System.out.println("AD!!");
            return 3450;
        }
        
        
        return randomDirection;
    }
}
    

