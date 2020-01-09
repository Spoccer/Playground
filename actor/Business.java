package actor;

import gui.RatBotsColorScheme;
import java.awt.Color;

/**
 * Business status is stored in the second layer of BotWorld in 2020.  
 * @author spockm
 */
public class Business extends GameObject
{
    private int chargeAmount; 
    private int duration;
    
    public Business()
    {
        setColor(RatBotsColorScheme.getNeutralColor());
        chargeAmount = 0;
        duration = 502;
    }
    public Business(int value)
    {
        setColor(RatBotsColorScheme.getNeutralColor());
        chargeAmount = value;
        duration = 502;
    }
    public Business(Color color, int value)
    {
        setColor(color);
        chargeAmount = value;
        duration = 502;
    }
    public Business(Business b)
    {
        setColor(b.getColor());
        chargeAmount = b.getChargeAmount();
    }
    
    public void setChargeAmount(int amt)
    {
        chargeAmount = amt;
    }
    public int getChargeAmount()
    {
        return chargeAmount;
    }
    public void increaseChargeAmount(int amt)
    {
        chargeAmount+=amt;
    }
    
    public void setDuration(int amt)
    {
        duration = amt;
    }
    public void decreaseDuration()
    {
        duration--;
        if(duration <= 0)
        {
            setColor(RatBotsColorScheme.getNeutralColor());
            chargeAmount = 0;            
        }
    }
        
    public String toString()
    {
        if(chargeAmount > 0)
            return " : Business cost = "+chargeAmount;
        else if (chargeAmount == 0)
            return "";
        else
            return " : Job site pay = "+(-chargeAmount);
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Business(this);
        return clone;
    }

}
