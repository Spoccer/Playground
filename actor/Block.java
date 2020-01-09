package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Block extends GameObject implements BlockedLocation
{
    
    public Block()
    {
        setColor(Color.DARK_GRAY);
    }
    public Block(Color initialColor)
    {
//        setColor(initialColor);
        setColor(Color.DARK_GRAY);
    }
    public Block(Block b)
    {
//        setLocation(b.getLocation());
        setColor(Color.BLACK);
    }
    
    
    
    @Override
    public String toString()
    {
        String result = "Boulder: ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Block(this);
        return clone;
    }
}
