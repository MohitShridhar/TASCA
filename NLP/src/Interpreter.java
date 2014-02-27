import java.util.ArrayList;

/**
 * Skeleton file for the command interpreter
 * 
 * @author Mohit Shridhar
 * @Matric A0105912N
 * @date 27/2/2014
 */


/**
 * Classes for time and date values
 * 
 */

class Time {
    private int hour, minute, second;
    private String ampm;
    
    public Time() {
	//Initialize values:
    }
    
    // Accessor and Modifier functions
}

class Date {
    private int day, month, year;
    
    public Date() {
	
	//Initialize values:
    }
    
    // Accessor and Modifier functions:
}



/**
 * Class for storing/accessing different parameters of a command (like add, modify, due)
 * 
 */

class Parameters {
    private String description, location, folder, priority;
    private Date date;
    private Time time;
    
    public Parameters() {
	
	// Initialize values;
    }
    
    // Accessor and Modifier functions
    
}

public class Interpreter {
    
    enum CommandType {
	ADD_DESCRIPTION, ADD_TIME, ADD_DUE_DATE, ADD_LOCATION, ADD_PRIORITY, ADD_TO_FOLDER,
	MODIFY_DESCRIPTION, MODIFY_TIME, MODIFY_DUE_DATE, MODIFY_LOCATION, MODIFY_PRIORITY, MODIFY_FOLDER,
	DISPLAY_TODAY, DISPLAY_TOMORROW, DISPLAY_WEEK, DISPLAY_MONTH,
	
	CONTEXT_TASK, // Must add/modify
	
	DELETE, CLEAR, UNDO, REDO,
	MARK, SEARCH, QUIT
    };
    
    // Extra added. REMOVE Later if not needed:
    enum CommandErrorType {
	INVALID_DESCRIPTION, INVALID_TIME, INVALID_DUE_DATE, INVALID_LOCATION, INVALID_PRIORITY, INVALID_FOLDER_REF,
	
	INVALID_DISPLAY_REF, INVALID_DELETE_REF, CANNOT_UNDO, CANNOT_REDO,
	INVALID_MARK_REF, SEARCH_NOT_FOUND, 
	
	CONTEXT_INVALID, // Must add/modify
		
	INVALID_COMMAND_FORMAT, INVALID_COMMAND, INVALID_ALREADY_EXISTS,
	MISSING_QUOTES, MISSING_INFO, 
	
	INVALID_DATABASE
    }
    
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
