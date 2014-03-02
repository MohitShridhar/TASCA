import java.util.ArrayList;

/**
 * Skeleton file for the command interpreter
 * 
 * @author Mohit Shridhar
 * @Matric A0105912N
 * @date 27/2/2014
 */


/**
 * Data structure for a new command: Command + Parameters
 *
 */

class Command {
    private CommandType command;
    private Parameters parameters;
    
    public Command() {
	command = null;
	parameters = null;
    }
    
    public Command(CommandType command) {
	command = this.command;
	parameters = null;
    }
    
    public Command(CommandType command, Parameters parameters) {
	command = this.command;
	parameters = this.parameters;
    }
    
    // Mutators:
    public void setCommandType(CommandType command) {
	command = this.command;
    }
    
    public void setParameters(Parameters parameters) {
	parameters = this.parameters;
    }
    
    //Accessors:
    public CommandType getCommandType() {
	return command;
    }
    
    public Parameters getParameters() {
	return parameters;
    }
    
}


public class Interpreter {
   
    
    private static ArrayList<CommandType> currentCommands = new ArrayList<CommandType>();
    
    public Interpreter() {
	
	readKeywordDatabase();
	
    }
    
    /**
     * Reads keywords from txtfile and saves them in the local memory (hash table)
     * 
     */
    
    private static void readKeywordDatabase() {
	
    }
    
    /**
     * Final function that batch processes all the commands input into the command bar
     */
    
    private static void processCommands() {

    }
    
    
    /**
     * High-level function which interprets the command from the user input
     * 
     * @param userInput
     * @return
     */
    
    public static CommandType interpretCommand(String userInput) {
	
	return CommandType.CLEAR; // CHANGE
    }
    
    private static CommandType keywordSearch(String keyword) {
	
	return CommandType.CLEAR; // Change
    }
    
    private static boolean isValidCommand(String userInput) {
	
	return true; // CHANGE
    }
    
    private static boolean commandAlreadyExists(CommandType command) {
	
	return true; // CHANGE
    }
    
    /**
     * Temporarily stores the commands (already interpreted) in a list to be later used for batch processing
     * 
     * @param command
     */
    
    private static void rememberCommand(CommandType command) {
	
    }
    
    private static void forgetCommand(CommandType command) {
	
    }
}
