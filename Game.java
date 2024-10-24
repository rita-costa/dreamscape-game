import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.Set;

/**
 *  This class is the main class of the "Dreamscape" application. 
 *  "Dreamscape" is a very simple, text based adventure game.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it 
 *  creates the parser, loads the map, creates a player, and starts the game.  
 *  It also evaluates and executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Rita Costa (k19005287)
 * @version 2019.11.12
 */

public class Game 
{
    private Parser parser;
    private Player player;
    private Map map; //contains the rooms, items, and characters for game

    private boolean won;
    private boolean lost;

    //stores rooms player has visited as a stack, so only the most recently added room can be accessed, used for 'back' command
    private Stack<Room> roomHistory;

    //stores all characters that were created
    private HashMap<String, Character> characters;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        won = false;
        lost = false;

        //creates all rooms, items, and characters
        map = new Map();

        parser = new Parser();

        player = new Player(); 
        player.setGoal(map.getGoals()); //sets goal of player to list of items that need to be collected to win game (defined in map)
        player.setCurrentRoom(map.getStartRoom()); //sets current location of player to the 'start' room
        player.setEndRoom(map.getEndRoom());  //sets the room the player needs to be in to end the game

        roomHistory = new Stack<>();

        //sets characters of current game to the ones created in map (so they can be manipulated in this class) 
        loadCharacters(map.getAllCharacters()); 
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is quit or the player has won.

        boolean finished = false;
        while (!finished && !won && !lost) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }

        //if player wins or loses game (ie. didn't quit), additional message printed
        if(won) {
            printWin();
        }
        else if(lost) {
            printLose();
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("Welcome to Dreamscape!");
        System.out.println("Dreamscape is a new, adventure game.");
        System.out.println("Type 'help' if you need help.");
        printRoomDescription();
    }

    /**
     * Print out the end message to player if they win the game.
     */
    private void printWin()
    {
        System.out.println("You collected all the Dreamstones!");
        System.out.println("You lay down on the bed and close your eyes...");
        System.out.println(".");
        System.out.println(".");
        System.out.println(".");
        System.out.println("...and open them to your actual bedroom.");
        System.out.println("You managed to get out of the dream world!");
        System.out.println("\n....................................................................");
        System.out.println("Congratulations you have won Dreasmscape!");
    }
    
    /**
     * Print out the end message to player if they lose the game.
     */
    private void printLose()
    {
        System.out.println("You lose all your strength. ");
        System.out.println("You can't bring yourself to move...");
        System.out.println(".");
        System.out.println("You stay forever trapped in your mind.");
        System.out.println(".");
        System.out.println("Next time you have to eat to replenish your strength!");
    }

    /**
     * Loads characters by saving their name (used in commands) and their Character object to a HashMap.
     * Used to call character's methods to move and interact with player.
     * 
     * @param List of characters returned from 'getAllCharacters' method in map (where characters are initailised).
     */
    private void loadCharacters(ArrayList<Character> characterList) 
    {
        characters = new HashMap<>();

        for(Character character : characterList) {
            //saves character in HashMap in format (name used for commands, Character object)
            characters.put(character.getName(), character);
        }
    }

    /**
     * Print out the description of room player is currently in, including:
     * room description, items in room, characters in room, exit information, player's strength bar.
     */
    private void printRoomDescription() {
        System.out.println(player.getCurrentRoom().getLongDescription());
        System.out.println(player.getStrengthBar());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
       
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look();
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if(commandWord.equals("back")) {
            goBack();
        }
        else if(commandWord.equals("take")) {
            takeItem(command);
        }
        else if(commandWord.equals("drop")) {
            dropItem(command);
        }
        else if(commandWord.equals("inventory")) {
            showInventory();
        }
        else if(commandWord.equals("wake")) {
            wakeUp(command);
        }
        else if(commandWord.equals("talk")) {
            talkTo(command);
        }
        else if(commandWord.equals("give")) {
            giveItem(command);
        }
        else if(commandWord.equals("use")) {
            useItem(command);
        }
        else if(commandWord.equals("wait")) {
            waitCommand();
        }        

        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around your own mind.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasWord(2)) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /** 
     * "Back" was entered. The rest of the command is ignored.
     * Checks if the roomHistory (stack with all previously visited rooms) is empty.
     * If not the player moves into the previous room they were in.
     * 
     * Can be used until player reaches original start location (ie stack empty).
     */
    private void goBack()
    { 
        if(!roomHistory.empty()) { 
            //sets 'previousRoom' to the first room in the room history stack (ie last room to have been visited)
            Room previousRoom = roomHistory.pop(); 

            //changes player's location to that room 
            //without adding room player leaves to stack (ie like retracing steps using string to the initial room) 
            player.setCurrentRoom(previousRoom);
            printRoomDescription();
        }
        else {
            //there are no more rooms to go back to in the history
            System.out.println("You can't go back...");
        }
    }

    /** 
     * "Take" was entered. 
     * Try to find the target item. If not found an error message is printed.
     * If item exists player attempts to add it to inventory by checking if it isn't immovable. 
     */
    private void takeItem(Command command) 
    {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know what to pick up.
            System.out.println("Take what?");
            return;
        }

        String itemName = command.getWord(2); //supposedly the second word will be the name of the item to be picked up (target) 

        //returns string to let user know if takeItem action is sucessful
        String result = player.takeItem(itemName);
        System.out.println(result); //string printed
    }

    /** 
     * "Drop" was entered. 
     * Try to find the target item. If not found an error message is printed.
     * If item exists player attempts to remove it from inventory by checking if it was
     * present in inventory in the first place. 
     */
    private void dropItem(Command command) 
    {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know what to drop.
            System.out.println("Drop what?");
            return;
        } 

        String itemName = command.getWord(2);

        //dropItem method returns a String with a message saying that item has been dropped to then print
        String dropped = player.dropItem(itemName);
        System.out.println(dropped);
    }

    /** 
     * "Go" was entered.
     * Try to go in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If room is teleportation room, player sent to a random room.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getWord(2);

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            moveTo(nextRoom);

            //if the room the player just moved into is a teleportation room, they will be
            //transported to a random other room
            if(nextRoom instanceof TeleportRoom) {
                TeleportRoom room = (TeleportRoom) nextRoom;
                Room roomToTeleportTo = room.teleportTo(map.getAllRooms()); //random room to teleport to from existing rooms chosen

                moveTo(roomToTeleportTo); //player moves again to the randomized room
            }
        }
    }

    /** 
     * Used in goRoom method.
     * Change's players current room, adds last room to history, and moves characters.
     */    
    private void moveTo(Room nextRoom) 
    {
        playerStrengthCheck();
        
        roomHistory.push(player.getCurrentRoom());  //places room player is leaving in room history (for back command)
        player.setCurrentRoom(nextRoom);
        
        moveCharacters();

        printRoomDescription(); //descrption of next room printed
    } 
    
    /** 
     * Checks if player's strength is 0 or higher. If not, player starves and 
     * loses the game (main while loop broken);
     */   
    private void playerStrengthCheck() 
    {
        if(player.getStrength() < 0) {
            lost = true;
        }
    }

    /** 
     * Used in goRoom method.
     * Iterates through all characters in game and changes their current location changed to a random neighbouring room.
     */
    private void moveCharacters() {
        //all characters in the game move to another random room (that was neighbouring their current room)
        Set<String> keys = characters.keySet();
        for(String characterName : keys) {
            characters.get(characterName).move();
        }
    }

    /** 
     * "look" was entered. The rest of the command is ignored.
     * Room description is re-printed.
     * Command useful if inital description has scrolled out of view.
     */
    private void look() 
    {
        printRoomDescription();
    }

    /** 
     * "inventory" was entered. The rest of the command is ignored.
     * A list of all the player's inventory items and their weights printed.
     */
    private void showInventory() {
        System.out.println(player.getInventory().getInventoryString());
    }

    /** 
     * "eat" was entered. 
     * Try to find the target item. If not found an error message is printed.
     * 
     * Checks if existing item is edible, and prints appropriate message.
     */
    private void eat(Command command) 
    {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know where to go...
            System.out.println("Eat what?");
            return;
        }

        String foodName = command.getWord(2);

        String eatResult = player.eatItem(foodName);
        System.out.println(eatResult);
    }

    /** 
     * "wake" was entered. 
     * Checks if "wake up" was typed. 
     * If player has all the goal items and is in correct end room, player wins (won boolean set to true), ending the game
     */
    private void wakeUp(Command command) {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know what to do...
            System.out.println("Wake what?");
            return;
        }

        if((command.getWord(2)).equals("up")) {
            //checks if the player has all the necessary items to win
            //and is in the right room (ie. end room) will return true
            boolean hasWon = player.canWin(); 

            //if player doesn't have all the requirements message saying they haven't won yet printed
            if(!hasWon) {
                System.out.println("You don't have all the goal items or you're not in the right place. \n"
                    + "Remember you must collect all dreamstones and be in the bedroom to wake up.");
            }

            //if player has all requirements to win, boolean won set to true so
            //while loop in play() can be broken and game ends
            won = hasWon;
            return;
        }
        else {
            System.out.println("I don't know what you mean...");
            return;
        }
    }

    /** 
     * "talk" was entered. 
     * Tries to talk to the character specified, if this exists, if not an error message printed.
     */
    private void talkTo(Command command) {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know what to do...
            System.out.println("Talk to who?");
            return;
        }

        //try to find character to talk to (returns null if non existent)
        String characterName = command.getWord(2);
        Character character = characters.get(characterName);

        if(character == null) {
            System.out.println("This doesn't exist.");
        }
        else if(character.isInRoom(player.getCurrentRoom())) { //checks if the character is in the same room as the player
            System.out.println(character.getTalk());           //prints the dialogue of character
            System.out.println(character.giveIfHappy(player)); //if the character is happy they may give a reward to the player
        }
        else {
            //if the character exists but isn't in the room following message is printed
            System.out.println("This character isn't in the room.");
        }
    }

    /** 
     * "give" was entered.
     * The command word order is expected to be: "give [item in inventory] [character to give it to]"
     * Tries to give a character an item from player's inventory.
     * 
     * Once an item is given it cannot be taken back.
     */
    private void giveItem(Command command) {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know what to give...
            System.out.println("Give what to who?");
            return;
        }
        if(!command.hasWord(3)) {
            //if there is no third word we don't know who to give item to
            System.out.println("Give it to who?");
            return;
        }

        //tries to find character (specified in 3rd word of command) to give item to
        String itemName = command.getWord(2);
        String characterName = command.getWord(3);
        Character character = characters.get(characterName);

        if(character == null) {
            System.out.println("This character doesn't exist.");
            return;
        }

        //if character specified is in the room AND player has specified item in inventory, action can be completed
        if(character.isInRoom(player.getCurrentRoom()) && player.getInventory().hasItemString(itemName)) {
            //character recieves item specified (checking if it is edible - if so character is now happy) String with reply returned
            String characterReply = character.recieve(player.getInventory().getInventoryItem(itemName));

            //player removes item from inventory 
            String giveResult = player.getInventory().giveItem(itemName);

            //description of the action - response of character and removal of item - printed
            System.out.println(giveResult);
            System.out.println(characterReply);
        }
        else {
            System.out.println("You can't give this right now.");
        }
    }

    /** 
     * "use" was entered.
     * The command word order is expected to be: "use [item in inventory]" OR "use [item in inventory] [item in room to use with]"
     * Depending on the number of words in the command, try to use the item(s) and print 
     * result returned.
     */
    private void useItem(Command command) 
    {
        if(!command.hasWord(2)) {
            // if there is no second word, we don't know what to use...
            System.out.println("Use what?");
            return;
        }
        
        String itemUsedName = command.getWord(2);
        
        if(!command.hasWord(3)) {
            //if there are only 2 words, the use message of the item (if present) printed
            String useResult = player.useItem(itemUsedName);
            System.out.println(useResult);
            return;
        }
        
        //else, there are 3+ words
        //command expected to be about using two items together
        
        String itemUsedOnName = command.getWord(3);

        String result = player.useTwoItems(itemUsedName, itemUsedOnName);
        System.out.println(result);
    }
    
    /** 
     * "wait" was entered.
     * Characters move but player doesn't. The room description is re-printed.
     * 
     * NOTE: moveTo(player.getCurrentRoom()) method could have been used BUT this would cause player's 
     * strength to deplete, making game very hard to win.
     */
    public void waitCommand()
    {
        //characters move but player doesn't
        moveCharacters();
        printRoomDescription(); //description re-printed so new characters shown if present
    }
}
