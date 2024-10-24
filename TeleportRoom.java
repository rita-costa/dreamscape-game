import java.util.ArrayList;
import java.util.Random;

/**
 * Extended from the Room class.
 * This is a room that teleports the player to another random room.
 * 
 * It is a child class because it is useful to check if it is a teleportation room by
 * checking if the room is an instance of a TeleportRoom.
 *
 * @author Rita Costa (k19005287)
 * @version 2019.11.23
 */
public class TeleportRoom extends Room
{
    /**
     * Creates a room similar to one from the Room class
     */
    public TeleportRoom(String description)
    {
        super(description);
    }

    /**
     * Returns a random room in the map that player will be transported to.
     * 
     * @param A list of all the rooms present in the current game.
     */
    public Room teleportTo(ArrayList<Room> allRooms)
    {
        //the teleport room is removed from the list of all rooms so the player cannot teleport into the teleport room
        allRooms.remove(this); 
        
        //randomly generates an integer to act as a random index
        Random randomGen = new Random();
        int randomRoomIndex = randomGen.nextInt(allRooms.size());
        
        //retrieves a random room from the existing ones using the index
        Room randomRoom = allRooms.get(randomRoomIndex);
        return randomRoom;
    }
}
