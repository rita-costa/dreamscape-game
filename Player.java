import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

/**
 * This class is part of the "Dreamscape" application. 
 * "Dreamscape" is a simple text based adventure game.  
 * 
 * A "Player" represents a user currently playing the game.
 * 
 * This class contains information about the player's inventory, current location, and methods to 
 * manipulate the player's behaviour.
 *
 * @author Rita Costa (k19005287)
 * @version 2019.11.23
 */
public class Player
{
    private Room currentRoom; //player's current location 
    private Inventory inventory; //player's inventory (stores items player is carrying)
    
    private int strength; //measure of how hungry a player is, decreases with movements

    private ArrayList<Item> goals; //items required for player to win game
    private Room endRoom;          //room player must be in to win the game

    /**
     * Creates a player with an inventory.
     */
    public Player()
    {
        goals = new ArrayList<>();
        inventory = new Inventory(30);
        
        strength = 5; //if player reaches 0 strength they lose
    }
    
    /**
     * Sets the player's goals to a list of items that exist in the game that must be collected.
     * 
     * @param A list of type Item conatining all items that must be collected.
     */
    public void setGoal(ArrayList<Item> goals) 
    {
        this.goals = goals;
    }

    /**
     * Returns the player's current room/location.
     * 
     * Used so Game class can use current room for 'go' and 'back' commands.
     * @return The room the player is currently in.
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Changes current room of player so they are in a different room/location.
     * @param Room player moves to.
     */
    public void setCurrentRoom(Room room) 
    {
        currentRoom = room;
        
        decreaseStrength(1); //as player has moved, their strength decreases
    }

    /**
     * Sets the room player must be in to win the game.
     */
    public void setEndRoom(Room room) 
    {
        endRoom = room;
    }

    /**
     * Returns the Player's inventory.
     */
    public Inventory getInventory() 
    {
        return inventory;
    }
    
    /**
     * Returns the Player's strength.
     */
    public int getStrength() 
    {
        return strength;
    }

    /**
     * For a player to 'eat' an item.
     * If action successful strength of player will change, so strength bar returned too.
     * 
     * @param Name of item player wants to eat.
     * @return String saying if attempt was successful or not and player's strength bar.
     */
    public String eatItem(String itemName) 
    {
        //method in Inventory class returns a string saying if attempt was sucessful or not
        String result = inventory.eatItem(itemName, this);
        return result + "\n" + getStrengthBar();
    }
    
    /**
     * Decreases player's strength value by given number.
     * 
     * @param Int for strength to decrease by.
     */
    public void decreaseStrength(int decrease)
    {
        strength = strength - decrease;
    }
    
    /**
     * Increases strength by given number.
     * 
     * @param Int for strength to increase by.
     */
    public void increaseStrength(int increase)
    {
        strength = strength + increase;
    }
    
    /**
     * Returns the player's strength value as a visual representation:
     * "Strength: [***]" 
     * Each star represents a hunger point.
     * 
     * @return A String displaying player's hunger as a visual bar.
     */
    public String getStrengthBar() 
    {
        int count = strength;
        String strengthBar = "Strength: [";
        
        while(count >= 0) {
            strengthBar += "*";
            count --;
        }
        
        return strengthBar + "]";
    }

    /**
     * When 'drop' command used.
     * 
     * @param Name of item player wants to drop.
     * @return String saying if attempt was successful or not.
     */
    public String dropItem(String itemName) 
    {
        String dropResult = inventory.dropItem(itemName, currentRoom);
        return dropResult;
    }

    /**
     * When 'take' command used.
     * 
     * @param Name of item player wants to pick up.
     * @return String saying if attempt was successful or not.
     */
    public String takeItem(String itemName) 
    {
        String takeResult = inventory.pickUpItem(itemName, currentRoom);
        return takeResult;
    }

    /**
     * Checks if inventory contains all the 'goal' items needed to wake up and if player is in 
     * correct end room. 
     * Return true if both are true, meaning the player has won the game.
     * 
     * @return True if player has fulfilled all requirements to win game.
     */
    public boolean canWin() 
    {
        int targetGoalCount = goals.size();

        //returns true if all goals are found in the inventory
        boolean foundAllGoals = inventory.hasAllGoals(targetGoalCount, goals);

        //if player has all goal items AND is in the appropriate room returns true
        if(foundAllGoals && currentRoom == endRoom) { 
            return true;
        }
        return false;
    }

    /**
     * Called when "use" command only has a second word (supposedly only refers to one item)
     * Checks if player has item in the inventory
     * if so, returns the use message of the itme
     */
    public String useItem(String itemUsedName) 
    {
        if(inventory.hasItemString(itemUsedName)) {
            return inventory.getInventoryItem(itemUsedName).getUseMessage();
        }
        return "You don't have this in your inventory.";
    }
    
    /**
     * Called when "use" command has a third word (supposedly refers to 2 items to be used together)
     * Checks if first item is in inventory and if second item can be used with other items (instance of KeyItem)
     * 
     * @return String of the appropriate result message 
     */
    public String useTwoItems(String itemUsedName, String itemUsedOnName) 
    {
        Item itemUsed = inventory.getInventoryItem(itemUsedName);

        //checks if item to be used is in inventory AND that it is a key item (can be used with other items)
        if(inventory.hasItemString(itemUsedName)) {
                if(itemUsed instanceof KeyItem) {
                //casts item to a KeyItem (as check has proven it is true)
                KeyItem item = (KeyItem) itemUsed; 
                
                //returns result of using item on target
                String useResult = item.useOn(itemUsedOnName, getCurrentRoom());
                return useResult;
            }
            else{
                //item is in inventory but is not a KeyItem
                return "This item cannot be used like this.";
            }
        }
        else {
            //item doesn't exist in inventory
            return "You don't have this item.";
        }
    }
}
