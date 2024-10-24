import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

/**
 * This class is part of the "Dreamscape" application. 
 * "Dreamscape" is a simple text based adventure game.  
 * 
 * An "Inventory" represents the current player's inventory in the game - it conatins the items that they
 * are currently holding.
 * 
 * This class conatins methods for commands dealing with picking up or dropping items in rooms and the 
 * exchanges of items during the game. 
 * It does this mainly by manipulating a HashMap that contains the items the player is currently holding.
 * 
 * @author Rita Costa (k19005287)
 * @2019.11.23
 */
public class Inventory
{
    //main HashMap that holds all items a player is currently carrying in form (item cammand name, item object)
    private HashMap<String, Item> inventory;

    private int maxWeight;      //maximum weight limit of inventory
    private int weightCarrying; //current weight of all items in inventory

    /**
     * Creates a new inventory with a specified maximum weight limit.
     */
    public Inventory(int maxWeight)
    {
        this.maxWeight = maxWeight;
        weightCarrying = 0; //initial inventory weight is 0

        inventory = new HashMap<>();
    }

    /**
     * Returns a specific item from the inventory.
     * @parameter Name of item
     * @return The item object.
     */
    public Item getInventoryItem(String itemName)
    {
        return inventory.get(itemName);
    }

    /**
     * Used to add items to player's inventory if 'take' caommand used.
     * First checks if the player can still carry the item (due to weight limitations).
     * 
     * @param Name of item player wants to carry.
     */
    public String pickUpItem(String itemName, Room currentRoom) {
        //target item returned by getting item from the collection of items in player's current room
        Item targetItem = currentRoom.getItem(itemName);

        //if the item isn't there or doesn't exist, returns null
        if(targetItem == null) {
            return "This item doesn't exist...you can't pick it up.";
        }

        //if item exists checks if it can be carried (looking at weight and if it is immovable)
        if(canCarryWeight(targetItem) && !targetItem.getIsImmovable()) {
            addInventoryItem(targetItem); //if it can be carried, added to inventory

            //remove item from room so it now only exists in the player's inventory (only one instance of each item exists)
            currentRoom.removeItem(targetItem); 

            //returns the string telling user they have sucessfully picked up an item
            return inventoryConfirmationMessage("picked up", targetItem);
        }
        else {
            return getFailedTake(targetItem);
        }
    }

    /**
     * Returns a String describing why an item cannot be picked up
     * depending on why it cannot be added to the inventory.
     * 
     * @param Item to be added to inventory.
     */
    private String getFailedTake(Item targetItem) {
        String failMessage = "You cannot carry this item.";

        //checks if item cannot be carried due to weight. If that is the reason, item's weight is shown in message.
        if(!canCarryWeight(targetItem)){
            failMessage += " This " + targetItem.getName() + " is too heavy! \n it weighs: " + targetItem.getWeight();   
        }
        return failMessage;
    }

    /**
     * Checks if item that player wants to add to inventory can be added
     * Checks if it isn't too heavy, considering current inventory weight 
     * and the weight of the new item compared to the weight limit.
     * 
     * @param Item player wants to carry.
     */
    private boolean canCarryWeight(Item newItem)
    {
        int potentialWeight = weightCarrying + newItem.getWeight();

        if(potentialWeight > maxWeight) {
            return false;
        }
        else {
            return true;
        }
    } 

    /**
     * Used when a character gives an item to player as a reward. 
     * Item added to inventory if possible and a String describing the result is returned.
     * 
     * @param Item character gifts player.
     * @return String of result of action (either an error message or a confirmation message).
     */
    public String recieveReward(Item targetItem) 
    {
        //checks if player can carry the reward
        if(canCarryWeight(targetItem)) {
            addInventoryItem(targetItem);
            return "Wow! They gave you a gift for your help. \n" + inventoryConfirmationMessage("recieved", targetItem);
        }
        else {
            return getFailedTake(targetItem);
        }
    }
    
    /**
     * Checks if a gifted item was recieved by checking if the item is present in inventory.
     * Returns true if present, meaning action was successful.
     * 
     * @param Item to be recieved.
     */
    public boolean hasRecieved(Item item)
    {
        boolean recieved = false;
        if(containsItem(item)) {
           recieved = true;
        }
        return recieved;
    }
    
    /**
     * Returns message with current inventory weight after an action has been sucessfuly performed on it.
     */
    private String inventoryConfirmationMessage(String action, Item item) {   
        return "You have " + action + " the " + item.getName() + "! \n"
        + "Your inventory now weighs: " + weightCarrying + "/" + maxWeight;
    }

    /**
     * Returns String with a list of all items in inventory and their weights to be printed
     * when player uses 'inventory' command.
     * 
     * Presented as: "INVENTORY: - a key // Weight = 1"
     */
    public String getInventoryString() {
        String inventoryList = "INVENTORY: ";

        if(inventory.isEmpty()) {
            inventoryList = "You have nothing in your inventory!";
            return inventoryList;
        }

        Set<String> inventoryItems = inventory.keySet(); 
        for(String itemName : inventoryItems) {
            int itemWeight = inventory.get(itemName).getWeight();
            String itemDescription = inventory.get(itemName).getDescription();
            inventoryList += "\n - " + itemName + " // Weight = " + itemWeight;
        }

        inventoryList += "\n \n Total Weight = " + weightCarrying + "/" + maxWeight;
        return inventoryList;
    }

    /**
     * Used to remove items from player's inventory if 'drop' command used.
     * Updates inventory weight and adds the item to the current room (so it can be picked up later if needed)
     * 
     * @param Name of item player wants to drop and player's current room.
     */
    public String dropItem(String itemName, Room currentRoom) {
        //target item returned by getting item from inventory using item's name
        Item targetItem = getInventoryItem(itemName);

        //if the item isn't there or doesn't exist, returns null
        if(targetItem == null) {
            return "You don't have this in your inventory.";
        }

        removeInventoryItem(targetItem);

        //item added to the current room (so it can be taken again if needed)
        currentRoom.addItem(targetItem);
        return inventoryConfirmationMessage("dropped", targetItem);
    }

    /**
     * Used to remove items from player's inventory if 'give' command used.
     * Item removed from inventory BUT unlike dropItem, it is not added to the current room.
     * 
     * @param Name of item player wants to give.
     */
    public String giveItem(String itemName) {
        //target item returned by getting item from inventory using item's name
        Item targetItem = getInventoryItem(itemName);

        removeInventoryItem(targetItem);

        return inventoryConfirmationMessage("given", targetItem); //returns String confirming action to be printed
    }

    /**
     * Removes item from inventory by removing it from inventory HashMap and
     * subtracting the weight of item from the weight being carried.
     */
    private void removeInventoryItem(Item targetItem) {
        //if the item exists in inventory it is removed
        inventory.remove(targetItem.getName());
        weightCarrying -= targetItem.getWeight(); //weight of inventory updated
    }

    /**
     * Adds item to inventory by adding it to inventory HashMap and
     * adding it's weight to weight being carried.
     */
    private void addInventoryItem(Item targetItem)  
    {
            inventory.put(targetItem.getName(), targetItem); //places item in inventory
            weightCarrying += targetItem.getWeight(); //updates weight of inventory by adding additional item weight
    }

    /**
     * For a player to 'eat' an item.
     * Checks if item exists in inventory and is edible.
     * If item is edible, it is removed from the inventory and a sucess message returned.
     * 
     * @param Name of item player wants to eat.
     */
    public String eatItem(String itemName, Player player) {
        Item item = getInventoryItem(itemName);

        //if the item isn't in inventory or doesn't exist, above line returns null
        if(item == null) {
            return "You don't have this in your inventory.";
        }        

        String eatStatusMessage = "You can't eat this!!!!";
        
        //checks if item is set to edible
        if(item.getIsEdible()) {
            removeInventoryItem(item);
            player.increaseStrength(item.getStrengthValue()); //increases player's strength 
            
            return inventoryConfirmationMessage("eaten", item) +  " \nYou are filled with strength!";
        }

        return eatStatusMessage;
    }

    /**
     * Checks if item in parameter is present in inventory HashMap. Returns true if it is.
     */
    public boolean containsItem(Item item) {
        return inventory.containsValue(item);
    }

    /**
     * Checks if the item belonging to the itemName in the parameter is present in inventory HashMap.
     * Returns true if present.
     * 
     * Different from containsItem() because used outside Inventory class and searches for an item based on 
     * it's command name (String) instead of the item object itself.
     */
    public boolean hasItemString(String itemName) {
        return inventory.containsKey(itemName);
    }

    /**
     * Checks if inventory contains all the 'goal' items needed to wake up 
     */
    public boolean hasAllGoals(int targetGoalCount, ArrayList<Item> goals) {
        boolean foundAll = false;
        int goalsInInventory = 0; 

        //iterates through all goal items in game
        for(Item goal : goals) {
            if(containsItem(goal)) { //if inventory contains this goal item, number of goals in inventory increased
                goalsInInventory ++;
            }
        }

        //compare number of goals in inventory to number of goals needed to win game
        //and if room player is in is the end room.
        if(targetGoalCount == goalsInInventory) { 
            foundAll = true;
        }

        return foundAll;
    }
}
