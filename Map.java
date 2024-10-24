import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is used to create and initalise all the characters (except the player), rooms, and items of a game.
 * 
 * It also provides lists of all the items, rooms, and characters created.
 *
 * @author Rita Costa (k19005287)
 * @version 2019.11.23
 */
public class Map
{
    //rooms, items, and characters
    private Room start, stress, fears, happyPlace, frogRoom, secrets, procrastination, memories, underwater, bedroom;
    private TeleportRoom teleportStation; //child class of Room
    private Item cat, frog, flute, diary, safe, dustBunnies, flower, redDreamStone, blueDreamStone, greenDreamStone;
    private Item cookie, croissant, carrotCake, strawberry, gingerbread, pudding, chocolate;
    private KeyItem key;
    private Character conscience, librarian, birdman, frogbert, sunflowerGirl;

    //lists to store all these
    private ArrayList<Item> items;
    private ArrayList<Room> rooms;
    private ArrayList<Character> characters;
    
    private Room startRoom;
    private Room endRoom;

    /**
     * Creates all rooms, characters and items for a game. 
     */
    public Map()
    {
        //creates lists for all items, rooms and characters
        characters = new ArrayList<>();
        items = new ArrayList<>();
        rooms = new ArrayList<>();

        //creates all items, characters, rooms of game
        createRooms();
        createItems();
        createKeyItems();
        createCharacters();

        //associates items with specified rooms (if applicable)
        assignItems();
        
        //sets rooms player will start and end in
        setStartRoom(start);
        setEndRoom(bedroom);
    }

    /**
     * Create all the rooms and link their exits together.
     * 
     * Each room added to rooms list.
     */
    private void createRooms()
    {
        // create the rooms
        start = new Room("in a room with a bed. It is not yours.");
        rooms.add(start);

        stress = new Room("in your room of STRESS. \n As you walk in the feeling anxiety immediatley washes over you." 
            + "\n There's cold coffee cups strewn everywhere." 
            + "\n A desk stands in a corner with a computer and stacks of paper. \n A person is hunched at the desk...");
        rooms.add(stress);    

        fears = new Room("in a room filled...with bird-humans?! They stare at you...judging. \n" 
            + "You realise you are in the room representing your FEARS. \n"
            + "Specicifically, the fear of being judged. \n");
        rooms.add(fears); 

        happyPlace = new Room("in a greenhouse full of plants! Faint classical music plays in the background. \n"
            + "Cosy chairs with stacks of your favourite books are spread around. \n"
            + "This must be your HAPPY PLACE! \n");
        rooms.add(happyPlace);

        frogRoom = new Room("in a room full of frogs! \n Frogs, frogs everywhere! \n"
            + "You immediately recognise some of your favourite species \n (you'd been studying them before). \n" 
            + "This must be your room of INTERESTS. \n");
        rooms.add(frogRoom);

        teleportStation = new TeleportRoom("in a room with a circle of mages chanting. \n"
            + "The eldest mage - with a white beard down to his toes - turns to you. \n"
            + "He says: 'This is a forbidden area of your mind. \n You will be teleported now.' \n");
        rooms.add(teleportStation);    

        secrets = new Room("in an cold empty concrete room. \n" 
            + "This must be where you keep your DEEPEST DARKEST SECRETS. \n");
        rooms.add(secrets);     

        procrastination = new Room("immedately swimming in a pool of water. \n" 
            + "This room of your mind seems to be a pool. A strangely comfortable one... \n"
            + "There are what seems like mermaids swimming around. They begin to sing... \n"
            + "You don't want to leave...you can stay here for a while longer... \n the task isn't that important... \n"
            + "You realise this must be the PROCRASTINATION room of your mind. \n");
        rooms.add(procrastination);     

        underwater = new Room("underwater *blub* *blub* \n"
            + "There are bubbles everywhere in the turquoise water. \n");        
        rooms.add(underwater);

        memories = new Room("in an old dusty library. There are old books everythere,\nsome are open halfway; \n" 
            + "soft-focus images of your childhood replay on the pages. \n"
            + "It's your MEMORY LIBRARY. \n");
        rooms.add(memories);     

        bedroom = new Room("in your bedroom, but wrong. \n" 
            + "Everything is slightly different. Your sheets a different colour \n"  
            + "you've never seen, your poster on the wall \n"
            + "is from a different band. \n"
            + "It's not really your bedroom at all. \n");
        rooms.add(bedroom); 

        // initialise room exits
        start.setExit("north", stress);
        start.setExit("west", secrets);
        start.setExit("east", memories);

        stress.setExit("south", start);
        stress.setExit("north", procrastination);

        procrastination.setExit("south", stress);
        procrastination.setExit("west", frogRoom);
        procrastination.setExit("down", underwater);

        underwater.setExit("up", procrastination);

        frogRoom.setExit("east", procrastination);
        frogRoom.setExit("south", happyPlace);

        happyPlace.setExit("north", frogRoom);
        happyPlace.setExit("east", teleportStation);
        happyPlace.setExit("south", fears);

        fears.setExit("north", happyPlace);
        fears.setExit("east", secrets);

        secrets.setExit("north", teleportStation);
        secrets.setExit("west", fears);
        secrets.setExit("east", start);

        teleportStation.setExit("west", happyPlace);
        teleportStation.setExit("south", secrets);

        memories.setExit("west", start);
        memories.setExit("north", bedroom);

        bedroom.setExit("south", memories);
    }

    /**
     * Creates items in the game with information: 
     * (description, name used for commands, weight and an int)
     * 
     * Some items set to a room they are first found in (to be described when a player enters this room).
     * Some items are set to have special features (eg, edible or immovable).
     * Goal items set using setAsGoal() method.
     * 
     * Each item added to items list.
     */
    private void createItems()
    {
        // initialises items in rooms
        cat = new Item("A cat with a pointy hat! He looks familiar.... \n   He's Oscar! your old cat!", "cat", 25);
        cat.setToRoom(memories);
        items.add(cat);

        flute = new Item("A shiny tiny flute.", "flute", 10);
        flute.setUseMessage("*floot floot* Hm. Cute tune.");
        flute.setToRoom(fears);
        items.add(flute);

        frog = new Item("A waxy monkey frog sitting on a mossy log. \n   It's your favourite species!", "frog", 15);
        frog.setUseMessage("You pet the froggy. He is damp.");
        frog.setToRoom(frogRoom);
        items.add(frog);

        diary = new Item("Your bejewelled diary from when you were 13 \n   ...oh no.", "diary", 15);
        diary.setUseMessage("'12/03/2010: I broke mum's favourite pot. \nGoing to blame it on Jack and hope for the best'");
        items.add(diary);
 
        safe = new Item("A giant metal safe in the middle of the room. \n   It has a tiny keyhole...", "safe", 0);
        safe.setToRoom(secrets);
        safe.setImmovable();
        items.add(safe);

        redDreamStone = new Item("A glowing red dreamstone!", "red-dreamstone", 10);
        redDreamStone.setUseMessage("It glows in your hands and does...nothing.");
        redDreamStone.setToRoom(underwater);
        redDreamStone.setAsGoal();
        items.add(redDreamStone);

        blueDreamStone = new Item("A glowing blue dreamstone!", "blue-dreamstone", 10);
        blueDreamStone.setUseMessage("It glows in your hands and does...nothing.");
        blueDreamStone.setAsGoal();
        items.add(blueDreamStone);

        greenDreamStone = new Item("A glowing green dreamstone!", "green-dreamstone", 10);
        greenDreamStone.setUseMessage("It glows in your hands and does...nothing.");
        greenDreamStone.setAsGoal();
        items.add(greenDreamStone);

        dustBunnies = new Item("Dust bunnies floating around", "dust", 0);
        dustBunnies.setToRoom(memories);
        dustBunnies.setImmovable();
        items.add(dustBunnies);
        
        strawberry = new Item("A big red strawberry. Kind of \n out of place.", "strawberry", 3);
        strawberry.setToRoom(stress);
        strawberry.setEdible(2);
        items.add(strawberry);
        
        pudding = new Item("A wobbly plate of pudding.", "pudding", 3);
        pudding.setToRoom(frogRoom);
        pudding.setEdible(1);
        items.add(pudding);
        
        chocolate = new Item("A chocolate bar!", "chocolate", 2);
        chocolate.setToRoom(fears);
        chocolate.setEdible(3);
        items.add(chocolate);
        
        cookie = new Item("A somewhat stale cookie sitting on a desk", "cookie", 5);
        cookie.setToRoom(memories);
        cookie.setEdible(2);
        items.add(cookie);

        croissant = new Item("A croissant with butter and jam!", "croissant", 5);
        croissant.setToRoom(frogRoom);
        croissant.setEdible(3); 
        items.add(croissant);
        
        carrotCake = new Item("A carrot cake with icing sitting \n   on a picnic table in the shade", "cake", 5);
        carrotCake.setToRoom(happyPlace);
        carrotCake.setEdible(4);
        items.add(carrotCake);

        flower = new Item("A small daisy!", "daisy", 1);
        items.add(flower);
        
        gingerbread = new Item("A tiny gingerbread man. He has sunflowers for buttons.", "gingerbread", 3);
        strawberry.setEdible(3);
        items.add(gingerbread);
    }
    
     /**
     * Creates key items in the game with same information as the Item class constructor
     * (description, name used for commands, weight and an int)
     * 
     * A seperate method needed so that all non key items are initialised before some
     * being associated with a key item (in setItemsToReveal method) to prevent a null
     * pointer exception error.
     * 
     * Each item added to items list.
     */
    private void createKeyItems()
    {
        key = new KeyItem("A small key on the ground. \n   It has a single pink plasic jewel stuck to it.", "key", 1);
        key.setToRoom(start);
        key.setSuccessMessage("You use the key to open the safe...\n \n");
        key.setItemUsedOn(safe);
        key.setItemsToReveal(new ArrayList(Arrays.asList(diary, greenDreamStone)));
        items.add(key);  
    }

    /**
     * Create all the characters with information in constructor:
     * (dialogue - for 'talk' command, name used in commands, room they first appear in)
     * 
     * Dialogue for when character is happy is also set (different from normal dialogue).
     * 
     * Each character added to characters list.
     */
    private void createCharacters() {
        conscience = new Character("Your conscience, it looks like a sentient glowing orb.", "conscience", start);
        conscience.setChat("'You are asleep. I can't wake you up alone. \n"
            + "You must journey through your mind and find all \nthe dreamstones to wake up... they are: \n " + getGoalNames() + "'");
        conscience.setHappyChat("'Oh why thank you! I think you may find something if \n you swim down in the procrastination room..."
            + "\nRemember, you must collect these items to wake up: \n" + getGoalNames() + "'");
        characters.add(conscience);

        librarian = new Character("A slightly frazzled librarain shuffling books", "librarian", memories);
        librarian.setChat("'Oh...Oh...no time I have to catalogue everything...'");
        librarian.setHappyChat("'Thank you so much! I can't remeber the \nlast time I ate...\n Anyway, here's something for you.'");
        librarian.setReward(blueDreamStone);
        characters.add(librarian);

        birdman = new Character("A bird? man? He is unsettling. He stares at you, and judges.", "birdman", fears);
        birdman.setChat("'...' [he stares]");
        birdman.setHappyChat("'...' [his silence is slightly more pleasant]");
        characters.add(birdman);

        frogbert = new Character("A friendly-looking frog in a waistcoat", "frogbert", frogRoom);
        frogbert.setChat("'G'day! I'm quite happy to see you're out and  \n about exploring your mind!'");
        frogbert.setHappyChat("'Oh you are too kind! Call me Frogbert. \n I can't help you much dear I'm afraid \n" 
            + "But that librarian looks like they're keeping something important...'");
        frogbert.setReward(flower);
        characters.add(frogbert);
        
        sunflowerGirl = new Character("A cheerful girl with a sunflower dress!", "sunflower", happyPlace);
        sunflowerGirl.setChat("'Hellooo... Don't forget to eat something \n to regain your strength.'");
        sunflowerGirl.setHappyChat("'How nice of you! The ideal place to \n wake up is the bedroom, I'd say. \n" 
                                    + "just north of the memory library. Good luck!'");
        sunflowerGirl.setReward(gingerbread);
        characters.add(sunflowerGirl);
    }

    /**
     * Assigns items to their respective rooms (if set to a specific room in createItems method)
     * so that when player enters a room the items associated with it will be described too.
     */
    private void assignItems() {
        //iterates through all items created
        for(Item item : items) {
            if(item.getRoom() != null) { //if item's room field was set so a specific room, item added to that specific Room object
                item.getRoom().addItem(item);
            }
        }
    }

    /**
     * Iterates through all created items in the map and checks if they are considered goals
     * (ie if isGoal is set to true when they were created). 
     * If item is a goal, it is added to the list of goals.
     * 
     * Used in Game class to pass goals onto player.
     * 
     * @return List of type Item with all items that are considered goals.
     */
    public ArrayList<Item> getGoals() {
        ArrayList<Item> goals = new ArrayList<>();
        for(Item item : items) {
            if(item.getIsGoal()) { //checks boolean isGoal of item - usually set to false except when explicityly changed
                goals.add(item);
            }
        }
        return goals;
    }    

    /**
     * Returns a list of names of all goal items. 
     * Used in the dialogue of a character to let player know what they must collect to win game.
     */
    private ArrayList<String> getGoalNames()
    {
        ArrayList<Item> goals = getGoals();
        ArrayList<String> goalNames = new ArrayList<>();

        for(Item goal : goals) {
            goalNames.add(goal.getName());
        }

        return goalNames;
    }

    /**
     * Sets room player will start game in.
     */
    public void setStartRoom(Room room) {
        startRoom = room;
    }
    
    /**
     * Sets room player must be in to win the game.
     */
    public void setEndRoom(Room room) {
        endRoom = room;
    }
    
    /**
     * Returns room player will start game in.
     */
    public Room getStartRoom() {
        return startRoom;
    }
    
    /**
     * Returns room player must be in to win the game.
     */
    public Room getEndRoom() {
        return endRoom;
    }

    /**
     * Returns a list of all characters created.
     */
    public ArrayList<Character> getAllCharacters()
    {
        return characters;
    }

    /**
     * Returns a list of all rooms created.
     */
    public ArrayList<Room> getAllRooms() {
        return rooms;
    }
}
