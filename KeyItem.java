import java.util.ArrayList;

/**
 * The KeyItem class is an extension of the Item class, used in the 
 * "Dreamscape" project. 
 * This item can be used with another item in the room to 'unlock' 
 * something and reveal hidden items. (such as opening a safe with a key)
 *
 * @author Rita Costa (k19005287)
 * @version 2019/11/27
 */
public class KeyItem extends Item
{
    private String itemUsedOnName;
    private ArrayList<Item> itemsToReveal;
    private String successMessage;

    private boolean hasRevealed;

    /**
     * Constructor for objects of class UsableItem
     */
    public KeyItem(String description, String name, int weight)
    {
        super(description, name, weight); 
        hasRevealed = false;
    }

    /**
     * Sets item that this key item is successfully used on
     */
    public void setItemUsedOn(Item item) 
    {
        itemUsedOnName = item.getName();
    }

    /**
     * Sets a success message if use command successful.
     */
    public void setSuccessMessage(String message) 
    {
        successMessage = message;
    }

    /**
     * Sets a list of items to be items that are revealed if use command successful
     */
    public void setItemsToReveal(ArrayList<Item> items) 
    {      
        itemsToReveal = items;
    }

    /**
     * Adds items to be revealed to the current room if this hasn't been done yet.
     *
     * @param  Current room.
     */
    private void revealItems(Room room)
    {
        //only reveals the items if they haven't been revealed already
        if(!hasRevealed) {
            for(Item item : itemsToReveal) {
                room.addItem(item);
            }
            hasRevealed = true;
        }
    }

    /**
     * Called when "use" command supposedly involves using 2 items together
     * Checks if this is possible and if so, reveals previously hidden items to player
     *
     * @param  Name of item to be used on.
     * @param  Current room.
     */
    public String useOn(String targetItemName, Room room)
    {
        //check if the target item is the same as item it is supposed to be used with 
        //and that the traget item is in the current room 
        if(targetItemName.equals(itemUsedOnName) && room.getItem(targetItemName) != null) {
            revealItems(room); 
            return successMessage + room.getItemString(); //returns a success message and updated list of items in the room
        }

        return "You can't use this item like this.";
    }
}
