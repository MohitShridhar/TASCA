import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Interpreter:
 * Currently batch processes data
 * 
 * @author Mohit Shridhar
 * @Matric A0105912N
 * @date 27/2/2014
 */



public class Interpreter {
   
    //change to private:
    public static Map<String, CommandType> keywords = new HashMap<String, CommandType>();
    
    private static CommandType mainCommand;
    private static Parameters parameters = new Parameters();
    
    
    /* Keyword Headers: Mapping Config file elements to Command types */
    private static final Map<String, CommandType> keywordHeaders;
    static
    {
        keywordHeaders = new HashMap<String, CommandType>();
        
        // Main Commands:        
        keywordHeaders.put("add", CommandType.ADD);
        keywordHeaders.put("delete", CommandType.DELETE);
        keywordHeaders.put("clear", CommandType.CLEAR);
        keywordHeaders.put("modify", CommandType.MODIFY);
        keywordHeaders.put("mark", CommandType.MARK);
        keywordHeaders.put("search", CommandType.SEARCH);
        keywordHeaders.put("today", CommandType.DISPLAY_TODAY);
        keywordHeaders.put("tomorrow", CommandType.DISPLAY_TOMORROW);
        keywordHeaders.put("week", CommandType.DISPLAY_WEEK);
        keywordHeaders.put("undo", CommandType.UNDO);
        keywordHeaders.put("redo", CommandType.REDO);
        
        // Complementary Commands:
        
    }
    
    
    public Interpreter() {
	
	readKeywordDatabase();
	
    }
    
    
    /**
     * Reads keywords from txtfile and saves them in the local memory (hash table)
     * 
     */
    
    public static CommandFeedback readKeywordDatabase() {
	
	Config cfg = new Config();
	
	String[] headerKeySet = (String[])( keywordHeaders.keySet().toArray( new String[keywordHeaders.size()] ) );
	
	for (int i = 0; i < keywordHeaders.size(); i++) {

	    CommandFeedback feedback = addSynonyms(cfg, headerKeySet[i], keywordHeaders.get(headerKeySet[i]));

	    if (feedback == CommandFeedback.INVALID_DATABASE_DUPLICATES) {
		return CommandFeedback.INVALID_DATABASE_DUPLICATES;
	    }

	}

	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    
    private static CommandFeedback addSynonyms(Config cfg, String type, CommandType commandType) 
    {
	String[] keys = cfg.getSynonyms(type);
	
	for (int i=0; i<keys.length; i++) {
	    String key = keys[i];
	    
	    if (!keywords.containsKey(key)) {
		keywords.put(key, commandType);
	    } else {
		return CommandFeedback.INVALID_DATABASE_DUPLICATES;
	    }
	}
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    
    
    // UnderTEST ... CLEANUP!!!! private
    private static void parseInput(String input) {
	String mainCommandString = getFirstWord(input);
	mainCommand = interpretCommand(mainCommandString);
	
	
    }
    
    
    private static String getFirstWord(String input) {
	return input.split("'")[0].trim();
    }
 
    
    private static boolean hasOtherParameters(CommandType command) {
	
	return true;
    }
    
    /**
     * Final function that batch processes all the commands input into the command bar
     */
    
    private static CommandFeedback interpretAllParameters() {
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }    
    
    /**
     * High-level function which interprets the command from the user input
     * 
     * @param userInput
     * @return
     */
    
    public static CommandType interpretParameter(String userInput) {
	
	return CommandType.CLEAR; // CHANGE
    }
    
    private static CommandType interpretCommand(String commandString) {
	
	return CommandType.INVALID;
    }
    
    private static CommandType keywordSearch(String keyword) {
	
	return CommandType.CLEAR; // Change
    }
    
    private static boolean isValidPara(String userInput) {
	
	return true; // CHANGE
    }
    
    private static boolean paraAlreadyExists(String command) {

	return false; // CHANGE
    }
    
    public static Command getCommandAndPara() {
	
	return new Command();
    }
    
}
