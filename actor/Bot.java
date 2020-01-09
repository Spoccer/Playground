package actor;

import grid.Advertisement;
import grid.BotWorldGrid;
import grid.Location;
import gui.RatBotsArena;
import gui.RatBotsColorAssigner;
import gui.RatBotsColorScheme;
import java.awt.Color;
import java.util.ArrayList;
import world.BotWorld;
/**
 * Bots are the competitors in the BotWorld game.  Every Bot has a BotBrain 
 that acts as its 'brain' by making decisions for its actions.  
 * @author Spock
 */
public class Bot extends GameObject implements BlockedLocation
{       
    /**
     * Each Bot has a BotBrain as it's 'brain'.  The Bot 'tells' the BotBrain 
        what it 'sees' and the BotBrain makes the decision on how to act.  
     */
    private BotBrain botBrain; 
            
    private int score;
    private int roundsWon;
    private int matchesWon;
    private int matchesTied;
    private int matchesLost;
    private int totalScore = 0;
    
    private int mostRecentChoice = 0;
    
    private int mSecUsed = 0;
    
    public final int STARTING_SCORE = 200;
    public final int IMPROVEMENT_AMOUNT = 5;
    public final int NEW_BUSINESS_COST_RATIO = 10;
    public final int MAX_BUSINESS = 350;
    
    /**
     * Constructs a red Rat without a BotBrain.  
     */
    public Bot()
    {
//        botBrain = new BotBrain();
        setColor(Color.RED);
        clearScores();
    }
    /**
     * Constructs a Rat with the given color and given BotBrain for its 'brain'.
     * All rats have a BotBrain that chooses their action each turn.  
     * @param rb the BotBrain that makes decisions for this Rat.
     */
    public Bot(BotBrain rb)
    {
        botBrain = rb;
        setColor(RatBotsColorAssigner.getAssignedColor());
        clearScores();        
    }
    /**
     * Constructs a copy of this Rat (that does not include the BotBrain.)
     * @param in the Rat being copied.
     */
    public Bot(Bot in)
    {
        super(in);
        setLocation(in.getLocation());
        setColor(in.getColor());
        score = in.getScore();
    }
    
    public void setColor(Color in)
    {
        super.setColor(in);
        if(botBrain != null)
            botBrain.setColor(in);
    }
        
    public int getMostRecentChoice() { return mostRecentChoice; }
    
    
    /**
     * Overrides the <code>act</code> method in the <code>GameObject</code> 
        class to act based on what the BotBrain decides.
     */
    @Override
    public void act()
    {
        //Ask the BotBrain what to do. 
        giveDataToBotBrain();
//        System.out.println("starting: "+botBrain.getName()+botBrain.getMoveNumber());
        int choice = BotBrain.REST;
        if(botBrain != null)
            choice = botBrain.chooseAction();
//        System.out.println("done: "+botBrain.getName()+botBrain.getMoveNumber());
        mostRecentChoice = choice;
        
        turn((choice%1000)); //Turn to face the direction of the choice.
        if( (choice%1000)%90 != 0) choice = -1; //An invalid choice.  
        
        if(choice < 0)
        {
            //REST
        }
        else if(choice/1000 == 2) 
        {   
            placeNewBusiness();
        }
        else if(choice/1000 == 3) 
        {   
            advertise(choice-3000);
        }
        else if(canMove() && getScore() >= 0)
        {
            Location previousLoc = getLocation();
            move();
            if(choice/1000 == 1 && RatBotsArena.getPlayMode() >= RatBotsArena.SANDBOX) 
                placeBlockAtLocation(previousLoc); //1000,1090,1180,1270 = MOVE and leave Boulder
        }
        
        //Now pay the cost of the space
        Business b = getGrid().getBusiness(getLocation());
        if(b != null)
        {
            if(b.getColor().equals(getColor()))
            {
                //improve your own business!!! 
                b.increaseChargeAmount(IMPROVEMENT_AMOUNT);
                if(b.getChargeAmount() > MAX_BUSINESS)
                    b.increaseChargeAmount(MAX_BUSINESS-b.getChargeAmount());
            }
            else if(getScore() < 0) 
                addToScore((-RatBotsArena.JOB_WAGE)/2); //slow boost for debt
            else  //Pay to be on space
                addToScore(-b.getChargeAmount());

            //if(getScore() < 0) setScore(0); //can't go negative ????
        }
        
    } //end of act() method
    
    /**
     * Turns the Bot
     * @param newDirection the direction to turn to.   
     */
    public void turn(int newDirection)
    {
        setDirection(newDirection);
    }
    
    private Location nextLocation()
    {
        return getLocation().getAdjacentLocation(getDirection());
    }
    
    private boolean nextLocationValid()
    {
        return getGrid().isValid(nextLocation());
    }
        
    private boolean canMove()
    {
        return canMove(getLocation(), getDirection());
    }
   
    private boolean canMove(Location loc, int dir)
    {
        Location next = loc.getAdjacentLocation(dir);
        if(!getGrid().isValid(next)) 
            return false;
        //Make sure destination is not occupied by a Blocked Location
        //Bots, Boulders, Lakes, Rivers and Trees are Blocked Locations.  
        if(getGrid().get(next) instanceof BlockedLocation )
            return false;
//        //Special for 2020, can't move onto SubwayStation
//        for(Location possibleStation : RatBotsArena.getSubwayStations())
//            if(possibleStation.equals(next))
//                return false;
        
        return true;
    }
    
    /**
     * Moves the Bot forward, putting a Farm into the location it previously
     * occupied.
     */
    public void move()
    {        
        if(canMove())
        {
            Location old = getLocation();
            Location next = old.getAdjacentLocation(getDirection());
                    
            moveTo(next);
        }
    }
    
    private void placeNewBusiness()
    {
        //Leave a Buisness behind
        Location loc = getLocation();
        BotWorldGrid grid = getGrid();
        Business currentB = grid.getBusiness(loc);
        int costToBuild = 0;
        if(currentB != null)
            if(currentB.getColor().equals(RatBotsColorScheme.getNeutralColor()))
            {
                costToBuild = currentB.getChargeAmount()*NEW_BUSINESS_COST_RATIO;
                if(costToBuild < 0)  //Job site!!! (can't build here)
                    costToBuild = 5000000;
            }
            else //already owned by an actual player
                costToBuild = 1000000; 
        Business b = new Business(getColor(),costToBuild/NEW_BUSINESS_COST_RATIO);
        if(this.getScore() >= costToBuild)
        {
            this.setScore(getScore() - costToBuild);
            grid.setBusiness(loc, b);
        }
    }
    
    private void placeBlockAtLocation(Location loc)
    {
        //Leave a Block behind
        BotWorldGrid grid = getGrid();
        Business currentB = grid.getBusiness(loc);
        int costToBuild = 1;
        if(currentB != null)
        {
            costToBuild = currentB.getChargeAmount()*NEW_BUSINESS_COST_RATIO/2;
            if(costToBuild < 0)  //Job site!!! (can't build here)
                costToBuild = 5000000;
            if(costToBuild > MAX_BUSINESS+100) //Subway Station (can't build here)
                costToBuild = 5000000;
        }

        if(this.getScore() >= costToBuild)
        {
            this.setScore(getScore() - costToBuild);
            new Block(getColor()).putSelfInGrid(getGrid(), loc);
        }
            
    }
    
    private void advertise(int amount)
    {
        if(this.getScore() >= amount)
        {
            this.setScore(getScore() - amount);
            //add this to this turn's advertising list... (use 2*sqrt)
            getGrid().addAd(new Advertisement(getLocation(),amount));
//            System.out.println("Added the ad");
        }
//        else
//            System.out.println("Didn't add it");
    }
    

    @Override
    public String toString()
    {
        if(botBrain != null)
            return "Bot: "+botBrain.getName();
        else
            return "Bot!";
    }

    /**
     * Updates the most recent data (location, grid and status)
 information to the BotBrain.  This allows the BotBrain to make a decision
 based on current data every turn.  
     */
    private final void giveDataToBotBrain()
    {
        //score, energy, col, row, myStuff ================
        botBrain.setScore(score);
        botBrain.setLocation(getLocation().getCol(), getLocation().getRow());
        //match stuff: bestScore, roundsWon ===================
        botBrain.setBestScore(this.calculateBestScore());
        botBrain.setRoundsWon(this.getRoundsWon());
        //world stuff: moveNumber, roundNumber ================
        botBrain.setMoveNumber(BotWorld.getMoveNum());
        botBrain.setRoundNumber(BotWorld.getRoundNum());

        //theArena!============================================
        int numRows = getGrid().getNumRows();
        int numCols = getGrid().getNumCols();
        GameObject[][] theArena = new GameObject[numRows][numCols];
        Business[][] businessGrid = new Business[numRows][numCols];
        for(int row=0; row<numRows; row++)
        {
            for(int col=0; col<numCols; col++)
            {
                GameObject a = getGrid().get(new Location(row, col));
                if(a != null)
                    theArena[row][col] = a.getClone();
                Business b = getGrid().getBusiness(new Location(row, col));
                Business clone = (Business)b.getClone();
                    businessGrid[row][col] = (Business)b.getClone();
                //Might need to do each with instanceof here...                
            }
        }
        botBrain.setArena(theArena);
        botBrain.setBusinessGrid(businessGrid);
            
        
    } //end of giveDataToRatBot() -----------------------------
    
//    public int myScore()
//    {
//        int score =0;
//        return score;
//    }
    
    private int calculateBestScore()
    {
        int bestScore = getScore();
        ArrayList<Bot> rats = getGrid().getAllRats();
        for(Bot r : rats)
        {
            if(r.getScore() > bestScore)
            {
                bestScore = r.getScore();
            }
        }  
        return bestScore;
    }
        
    /**
     * Accessor method to get the BotBrain from a Bot.
     * @return the BotBrain 'brain' of this BotBrain.
     */
    public BotBrain getRatBot()
    {
        return botBrain;
    }

    /**
     * Gets the current score (from this round).  
     * @return the score
     */
    public int getScore() { return score; }
    /**
     * Sets the current score of this Bot.
     * @param in the score
     */
    public void setScore(int in) { score = in; }
    /**
     * Adds the given amount to score of this Bot.  
     * @param add the amount to add
     */
    public void addToScore(int add) { score += add; }
    /**
     * Gets the total points scored over all rounds in this match for this Bot.
     * @return the total score
     */
    public int getTotalScore() { return totalScore; }

    /**
     * Gets the number of rounds won by this Bot in the match.
     * @return the rounds won
     */
    public int getRoundsWon() { return roundsWon; }
        
    /**
     * Sets the number of rounds won by this Bot in the match.
     * @param in the rounds won
     */
    public void setRoundsWon(int in) { roundsWon = in; }
    /**
     * Increases the number of rounds won in this match by this Bot by one.
     */
    public void increaseRoundsWon() { roundsWon++; }

    public int getAvgMSecUsed() { return mSecUsed/BotWorld.getMoveNum(); }
    public void increaseMSecUsed(int in) { mSecUsed += in; }
    
    // These methods are used for the RoundRobin tourney.
    public int getMatchesWon() { return matchesWon; }
    public int getMatchesTied() { return matchesTied; }
    public int getMatchesLost() { return matchesLost; }
    public void increaseMatchesWon() { matchesWon++; }
    public void increaseMatchesTied() { matchesTied++; }
    public void increaseMatchesLost() { matchesLost++; }
     /**
     * Initializes this Bot for a new round.  
     */
    public final void initialize()
    {
//        System.out.println("Bot:initialize()");
        score = STARTING_SCORE;
        mSecUsed = 0;
        botBrain.initForRound();
    }
    
    public void addToTotalScore()
    {
        totalScore += score;        
    }
    
    public void clearScores()
    {
        score = 0;
        roundsWon = 0;
        matchesWon = 0;
        matchesTied = 0;
        matchesLost = 0;
        totalScore = 0;
    }

    @Override
    public GameObject getClone()
    {
        GameObject clone = new Bot(this);
        return clone;
    }
    
    
}