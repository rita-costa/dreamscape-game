import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "Dreamscape" application. 
 * "Dreamscape" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling, David J. Barnes, and Rita Costa (k19005287)
 * @version 2019.11.23
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<String, Item> items;        //stores items that are in the room.
    private HashMap<String, Character> characters;  //stores characters that are in the room.

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
        characters = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Associates a created item (from Item class) to the room by adding it to a HashMap of items.
     * This HashMap contains the item's key name (used for commands) and the item object iteslf.
     * @param item The item to be placed in the room.
     */
    public void addItem(Item item) 
    {
        items.put(item.getName(), item);
    }

    /**
     * Removes an item from the collection of items found in a room.
     * This is used when an item is picked up by the player and placed in their inventory.
     * 
     * By removing the item from the room, the player can move rooms while 'carrying' item
     * and when they return to the room there won't be another copy of the same item picked up.
     * @param item The item to be removed from room.
     */
    public void removeItem(Item item) 
    {
        items.remove(item.getName());
    }

    /**
     * Associates a character to the room by adding it to a HashMap of characters currently in the room.
     * This HashMap contains the character's command name (used for commands) and the character object iteslf.
     * 
     * Used when a character moves into a room so it's description is added to the long description.
     * 
     * @param item The item to be placed in the room.
     */
    public void addCharacter(Character character) 
    {
        characters.put(character.getName(), character);
    }

    /**
     * Removes a character from the collection of characters found in a room.
     * This is used when a character moves from one room to another.
     * 
     * @param The character leaving the room.
     */
    public void removeCharacter(Character character) 
    {
        characters.remove(character.getName());
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     You See: - a key 
     *     There are some beings in the room: * a frog (frogbert)
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "\n--------------------------------------------- \n" 
        + "You are " + description + "\n \n" + getItemString() + "\n \n" + getCharacterString() + "\n \n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits: [ ";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += exit + ".  ";
        }

        returnString += "] ";
        return returnString;
    }

    /**
     * Return a string describing the room's items, for example
     * "You See: - a key".
     * If the room has no items it will not print anything.
     * 
     * @return Details of items in room.
     */
    public String getItemString()
    {
        //check if there are any items in room by checking items HashMap. If not, doesn't print anything.
        if(items.isEmpty()) {
            return " ";
        }

        String returnString = "You See: ";
        Set<String> keys = items.keySet();
        for(String item : keys) {
            returnString += "\n- " + items.get(item).getDescription();
        }
        return returnString;
    }

    /**
     * Return a string describing the room's characters and their command names, for example
     * "You See: * a frog (frogbert)".
     * If the room has no characters it will not print anything.
     * 
     * @return Details of characters in the room.
     */
    private String getCharacterString() 
    {
        //check if there are any items in room by checking items HashMap. If not, doesn't print anything.
        if(characters.isEmpty()) {
            return " ";
        }

        String returnString = "There are some people here: ";
        Set<String> keys = characters.keySet();
        for(String characterName : keys) {
            returnString += "\n* " + characters.get(characterName).getDescription();
        }
        return returnString;
    }

    /**
     * @parameter The name (used in commands) of an item.
     * @return The item in the room that matches the name.
     * 
     * If this item isn't found in the room or doesn't exist, returns null.
     */
    public Item getItem(String itemName) 
    {
        return items.get(itemName);
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Returns a list of all the possible exits of the room.
     */
    public ArrayList<String> getAllExits() 
    {
        ArrayList<String> allExits = new ArrayList<>();

        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            allExits.add(exit);
        }
        return allExits;
    }
}

