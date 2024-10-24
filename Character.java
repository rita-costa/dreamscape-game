import java.util.ArrayList;
import java.util.Random;

/**
 * This class is part of the "Dreamscape" application. 
 * "Dreamscape" is a simple text based adventure game. 
 * 
 * A "Character" represents a character in the game which can move independently and randomly
 * between neighbouring rooms and can be interacted with by the player. 
 * 
 * This class stores a character's current location, their description information, and methods
 * for different interactions and movements.
 * 
 * Players can interact with characters by talking or giving them items.
 * 
 * Characters also have a (boolean) happy mood. When a player gives a character an edible item,
 * they become happy and give advice or even reward the player with an item.
 * 
 *  
 * @author Rita Costa (k19005287)
 * @version 2019.11.23
 */
public class Character
{
    private Room currentRoom;  //current location of character
    private String name;  //name used for commands
    private String description;  //long description
    private String chat;  //dialogue
    private String happyChat;  //a different dialogue when character is happy
    
    private Item reward; //an item that can be given to a player if the character is happy
    
    private boolean isHappy; 
    private boolean hasGiven;

    /**
     * Creates a character.
     */
    public Character(String description, String name, Room startRoom)
    {
        this.name = name;
        this.description = description + " (" + name + ") ";
        currentRoom = startRoom;
        
        isHappy = false;  //all characters are initially not happy
        hasGiven = false;  //hasn't given any items yet
        chat = "...";
        happyChat = "...:)";

        enterRoom(startRoom); //places character in their initial room at the start of the game
    }
    
    /**
     * Returns a character's name (usually used for commands)
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * Checks if character is in a specific room.
     */
    public boolean isInRoom(Room room) 
    {
        //if the character's current room matches the required room, returns true
        if(currentRoom == room) {
            return true;
        }
        return false;
    }
    
    /**
     * Sets character's normal dialogue.
     */
    public void setChat(String message) 
    {
        chat = message;
    }
    
    /**
     * Sets character's dialogue when they are happy.
     */
    public void setHappyChat(String happyMessage) 
    {
        happyChat = happyMessage;
    }
    
    /**
     * Returns dialogue ofcharacter depending on if they are happy or not.
     */    
    public String getTalk() 
    {
        if(isHappy) {
            return happyChat; //if character is happy a different dialogue is shown
        }
        return chat;
    }
    
    /**
     * Sets the reward (an item object) that character will give to player if they talk with them and they are happy.
     */    
    public void setReward(Item item) 
    {
        reward = item;
    }
    
    /**
     * Returns description of the character as a String.
     */
    public String getDescription()
    {
        //if the character is happy, say so
        if(isHappy) {
            return description + "\n  They seem happy...";
        }
        return description;
    }
    
    /**
     * A reward item is given to player if character is happy and has not given the reward yet.
     * 
     * @param Player character is (potentially) giving item to.
     */    
    public String giveIfHappy(Player player) 
    {   
        //if character is happy AND hasn't giver reward yet AND has a reward item to give - give the reward
        if(isHappy && !hasGiven && reward != null) {
            String result = giveReward(player);
            return result;
        }
        return "";
    }
        
    /**
     * Gives an item to the player as a reward.
     * Checks that the item has been successfully given and returns a message describing action (to be printed).
     * 
     * @param Player that item is being gifted to.
     */
    private String giveReward(Player player) 
    {
        String recieved = player.getInventory().recieveReward(reward);
        
        //if player has successfully added item to their inventory, set isGiven to true
        //so character doesn't give the reward twice
        if(player.getInventory().hasRecieved(reward)) {
            hasGiven = true;
        }
        
        return recieved;
    }

    /**
     * Character recieves an item that player has given them during a successful 'give' command.
     * 
     * Checks if item is edible, as this changes the character's mood.
     * 
     * @param Item that is given.
     * @return String with the character's reply, to be printed.
     */
    public String recieve(Item item) 
    {
        String reply = name + " recieves the gift. They seem satisfied.";
        
        if(item.getIsEdible()) {
            isHappy = true;  //if item is edible, character becomes happy
            reply = "- " + name + " takes the " + item.getName() + " gladly! \n \n They seem in a much better mood. \n" 
                    + "Maybe they will be more willing to help if \nyou talk to them now... \n";
        }
        
        return reply;
    }
    
    /**
     * Changes character's current location to another room.
     * Adds the character to the next room (to appear in the Room description)
     */    
    private void enterRoom(Room nextRoom) 
    {
        currentRoom = nextRoom;
        currentRoom.addCharacter(this);
    }
    
    /**
     * Removes a character from a room.
     * (seperate from enterRoom() as that method is also used to place character in initial room when they are created)
     */     
    private void leaveRoom() 
    {
        currentRoom.removeCharacter(this);
    }
    
    /**
     * Moves the character to a different location.
     * Characters move randomly by choosing a random exit from the room they are currently in (ie moving to a random neighbouring room).
     */  
    public void move() 
    {
        //creates a list of possible exits in the room by retrieving information from Room class
        ArrayList<String> currentExits = currentRoom.getAllExits();
        
        //randomly generates an int to use as an index
        Random randGenerator = new Random(); 
        int randomExitNumber = randGenerator.nextInt(currentExits.size());
        
        //random exit picked from possible exits using the random index int
        String randomExitDirection = currentExits.get(randomExitNumber);
        
        Room nextRoom = currentRoom.getExit(randomExitDirection);
        
        leaveRoom();
        enterRoom(nextRoom); 
    }
}
