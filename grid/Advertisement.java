package grid;

/**
 * This class is specific to BotWorld 2020
 * @author spockm
 */
public class Advertisement 
{
    private Location loc;
    private int amount;
    
    public Advertisement(Location l, int amt)
    {
        loc = l;
        amount = amt;
    }
    
    public Location getLocation() { return loc; }
    public int getAmount() { return amount; }
    
    public String toString()
    {
        return "AD! "+loc+","+amount;
    }
}
