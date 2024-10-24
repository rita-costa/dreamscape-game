/**
 * Class Item - an item in the "Dreamscape" adventure game.
 *
 * This class is part of the "Dreamscape" application. 
 * "Dreamscape" is a simple text based adventure game.  
 *
 * An "Item" represents an item found in a Room of the game.
 * It has a weight and description associated with it.
 * The player can interact with an item by picking it up.
 *
 * @Rita Costa (k19005287)
 * @2019/11/13
 */
public class Item
{
    private String name; //name of item user should use for commands
    private String description; //detailed description of the item when user enters room
    private int weight; 
    private int strengthValue;  //value that player's strength increases by if item is eaten
    
    private Room room; //initial room 
    private String useMessage; //massage when item is used

    //various features an item can be, ie. immovable, edible, if it is part of the main goal of the game...
    private boolean isImmovable;
    private boolean isGoal;
    private boolean isEdible;

    /**
     * Creates an object described "description" (this is the long and detailed description seen).
     * Initially sets name 
     */
    public Item(String description, String name, int weight)
    {
        this.description = description + " (" + name + ") ";
        this.name = name;
        this.weight = weight;

        isImmovable = false;
        isGoal = false;
        isEdible = false;
    }

    /**
     * returns the Item's name for commands
     */
    public String getName()
    {
        return name;
    }

    /**
     * sets the Item's room (where it is first placed)
     */
    public void setToRoom(Room room)
    {
        this.room = room;
    }    

    /**
     * returns the Item's room 
     */
    public Room getRoom()
    {
        return room;
    }

    /**
     * returns the Item's description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * returns the Item's weight
     */
    public int getWeight() 
    {
        return weight;
    }
    
    /**
     * returns the Item's strength value (for food)
     */
    public int getStrengthValue() 
    {
        return strengthValue;
    }

    /**
     * sets the message that will be displayed if an item is used
     * in a 'use' command
     */
    public void setUseMessage(String message) 
    {
        useMessage = message;
    }

    /**
     * returns message of consequence of using item in a 'use' command.
     */
    public String getUseMessage() 
    {
        //checks if the useMessage has been set
        if(useMessage != null) {
            return useMessage; //if it has, then return set message
        }

        //if not message was set, nothing happens. Generic message returned
        return "You use the " + name + ". Nothing happens.";
    }    

    /**
     * sets the item to be immovable (player cannot pick it up)
     */
    public void setImmovable() 
    {
        isImmovable = true;
    }

    /**
     * Returns true if item is immovable.
     */
    public boolean getIsImmovable() 
    {
        return isImmovable;
    }

    /**
     * Sets the item as a goal, meaning player must have it in their
     * inventory to win the game
     */
    public void setAsGoal() 
    {
        isGoal = true;
    }

    /**
     * Returns true if item is considered a goal.
     */  
    public boolean getIsGoal() 
    {
        return isGoal;
    }

    /**
     * sets the item as an edible item so 'eat' command works
     * and if given to a character, character becomes happy.
     * 
     * @param The value that player's strength increases by if item is eaten.
     */   
    public void setEdible(int strength) 
    {
        isEdible = true;
        strengthValue = strength;
    }

    /**
     * Returns true if item is edible.
     */
    public boolean getIsEdible() 
    {
        return isEdible;
    }
}
