import java.util.ArrayList;

/**
 * This class is part of the "Dreamscape" application. 
 * "Dreamscape" is a very simple, text based adventure game.  
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of a list of strings: a command word and various other
 * words (for example, if the command was "take map", then there are two strings: "take" and "map"
 * if the command is "give bread frogbert" there are three strings: "give", "bread", "frogbert").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 * 
 * @author  Michael Kölling, David J. Barnes, and Rita Costa (k19005287)
 * @version 2019.11.28
 */

public class Command
{
    private String commandWord;
    private ArrayList<String> words; //a list of words following the first command word

    /**
     * Create a command object. The command word is set to the first word 
     * of the list given (ie word at index 0).
     * 
     * @param An ArrayList of words that the command consists of.
     */
    public Command(ArrayList<String> words)
    {
        commandWord = words.get(0);
        this.words = words;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @return The command word.
     */
    public String getCommandWord()
    {
        return commandWord;
    }

    /**
     * @param Position of the desired word (second word = 2, third word = 3, etc.)
     * @return The word at the specified position in the list of words making up the command.
     */
    public String getWord(int wordNumber)
    {
        //checks if the size of the list of words is larger or equal to the desired word position. If not, returns null
        if(words.size() >= wordNumber) {
            //returns the word at index that corresponds to the desired word position
            //so eg. if the second word is wanted getWord(2) called but the index must be 1 less than the given position
            //as index 0 is counted
            return words.get(wordNumber - 1); 
        }
        else {
            return null;
        }
    }

    /**
     * @return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (commandWord == null);
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasWord(int wordNumber)
    {
        if(words.size() >= wordNumber) {
            return true;
        }
        else {
            return false;
        }
    }
}

