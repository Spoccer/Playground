package actor;

import grid.Grid;
import grid.Location;
import gui.RatBotsArena;
import gui.RatBotsColorScheme;

/**
 *
 * @author spockm
 */
public class Customer extends GameObject implements BlockedLocation
{
    private Location destination;
    private int stepsRemainingOverall; 
    private int stepsRemainingThisDest;
    
    public Customer()
    {
        setColor(RatBotsColorScheme.getCustomerColor());
        destination = new Location(100,100);
        stepsRemainingOverall = 70;
        stepsRemainingThisDest = 0; //get a new dest right away... 
    }
    public Customer(Customer c)
    {
        setColor(RatBotsColorScheme.getCustomerColor());
        destination = c.getDestination();
        stepsRemainingOverall = getStepsRemaining();
        stepsRemainingThisDest = 1;
    }
    
    public Location getDestination() { return destination; }
    public void setDetination(Location dest) 
    {   //This is only used when influenced by advertising
        destination = dest;
//        if(stepsRemainingOverall < 25)
//            stepsRemainingOverall = 25;
        stepsRemainingThisDest = 30;
    }
    public int getStepsRemaining() { return stepsRemainingOverall; }
    
    @Override
    public void act()
    {
        //See if this Customer is done shopping
        stepsRemainingOverall--;
        if(stepsRemainingOverall <= 0)
        {
            this.removeSelfFromGrid();
            return;
        }
                
        //Get a new destination if needed
        stepsRemainingThisDest--;
        if(stepsRemainingThisDest <= 0 || getLocation().equals(destination))
        {
            destination = RatBotsArena.getNewRandomSubwayStation(destination);
            stepsRemainingThisDest = 30;
        }
        
        //Choose to move toward destination (or randomly)
        if(Math.random() > .4)
            setDirection(getLocation().getDirectionToward(destination));
        else
            setDirection((int)(Math.random()*4)*90); //Choose a random direction
        
        //move one step (if possible) 
        move();
        
        setDirection(0); //To make the image orient correctly.  
    }

    public void move()
    {
        //Adapted from Bug.java (GridWorld)
        Grid<GameObject> gr = getGrid();
        if (gr == null)
            return;
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(getDirection());
        //Make sure the destination is valid and unblocked
        if (gr.isValid(next) && gr.get(next) == null)
            moveTo(next);
    }
    
    
    @Override
    public String toString()
    {
        String result = "Customer: "+destination;
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Customer(this);
        return clone;
    }
}
