import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class is part of the "Dreamscape" application. 
 * "Dreamscape" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling, David J. Barnes, and Rita Costa (k19005287)
 * @version 2019.11.26
 */
public class Parser 
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() 
    {
        String inputLine;   // will hold the full input line
        String word1 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();
        
        //creates a scanner for the input line
        Scanner tokenizer = new Scanner(inputLine);
        
        //ArrayList created to store all the words of the command/input, so more than 2 words can be stored
        ArrayList<String> words = new ArrayList<>(); 
        
        //adds all individual words to an ArrayList 
        while(tokenizer.hasNext()) {
            words.add(tokenizer.next()); //adds next word in tokeniser to the list
        }

        //returns the first word of the input string
        word1 = words.get(0);
        
        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if(commands.isCommand(word1)) {
            return new Command(words);
        }
        else {
            words.removeAll(words);
            words.add(null);
            
            return new Command(words); 
        }
    }

    /**
     * Gets a list of valid command words from the CommandWords class.
     */
    public String getCommands()
    {
        return commands.getCommandWords();
    }
}
