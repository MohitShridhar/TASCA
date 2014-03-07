import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Interpreter:
 * Currently batch processes data
 * 
 * @author Mohit Shridhar
 * @Matric A0105912N
 * @date 27/2/2014
 */



public class Interpreter {
   
    // Exceptions:
    private static final String INVALID_COMMAND_TYPE = "Invalid command type";
    private static final String EXCEPTION_EMPTY_ARGUMENT = "Cannot accept empty parameter argument";
    private static final String EXCEPTION_DUPLICATE_PARAMETERS = "Duplicate parameters found";
    private static final String INVALID_START_TIME = "Invalid start time";
    private static final String INVALID_END_TIME = "Invalid end time";
    private static final String INVALID_REMINDER_TIME = "Invalid remind time";
    private static final String INVALID_PRIORITY_REF = "Invalid priority reference";
    private static final String INVALID_FOLDER_REF = "Invalid folder reference";
    private static final String INVALID_PARAMETER_TYPE = "Invalid parameter type";
    
    
    private static Map<String, CommandType> commandKeywords = new HashMap<String, CommandType>();
    private static Map<String, ParameterType> parameterKeywords = new HashMap<String, ParameterType>();
    
    private static ArrayList<ParameterType> currentParameters = new ArrayList<ParameterType>();
    
    private static Command command = new Command();
    
    
    /* Keyword Headers: Mapping Config file elements to Command & Parameter types */
    private static final Map<String, CommandType> commandHeaders;
    private static final Map<String, ParameterType> parameterHeaders;
    static
    {
        // Main Commands:  
	
	commandHeaders = new HashMap<String, CommandType>();
           
        commandHeaders.put("add", CommandType.ADD);
        commandHeaders.put("delete", CommandType.DELETE);
        commandHeaders.put("clear", CommandType.CLEAR);
        commandHeaders.put("modify", CommandType.MODIFY);
        commandHeaders.put("mark", CommandType.MARK);
        commandHeaders.put("search", CommandType.SEARCH);
        commandHeaders.put("today", CommandType.DISPLAY_TODAY);
        commandHeaders.put("tomorrow", CommandType.DISPLAY_TOMORROW);
        commandHeaders.put("week", CommandType.DISPLAY_WEEK);
        commandHeaders.put("undo", CommandType.UNDO);
        commandHeaders.put("redo", CommandType.REDO);
        
        // Parameter Commands:
        
        parameterHeaders = new HashMap<String, ParameterType>();
        
        parameterHeaders.put("startTime", ParameterType.START_TIME);
        parameterHeaders.put("endTime", ParameterType.END_TIME);
        parameterHeaders.put("reminderTime", ParameterType.REMINDER_TIME);
        parameterHeaders.put("priority",ParameterType.PRIORITY);
        parameterHeaders.put("location", ParameterType.LOCATION);
        parameterHeaders.put("folder", ParameterType.FOLDER);
        parameterHeaders.put("taskID", ParameterType.TASK_ID);
        
        
    }
    
    
    public Interpreter() {
	
	/* If you have time: turn the following functions into an abstract class because they are quite similar */
	
	readCommandDatabase();
	readParameterDatabase();

	
    }
    
    
    /**
     * Reads keywords from txtfile and saves them in the local memory (hash table)
     * 
     */
    
    private CommandFeedback readCommandDatabase() {
	
	Config cfg = new Config();
	
	String[] headerKeySet = (String[])( commandHeaders.keySet().toArray( new String[commandHeaders.size()] ) );
	
	for (int i = 0; i < commandHeaders.size(); i++) {

	    CommandFeedback feedback = addCommandSynonyms(cfg, headerKeySet[i], commandHeaders.get(headerKeySet[i]));

	    if (feedback == CommandFeedback.INVALID_DATABASE_DUPLICATES) {
		return CommandFeedback.INVALID_DATABASE_DUPLICATES;
	    }

	}

	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    private CommandFeedback addCommandSynonyms(Config cfg, String type, CommandType commandType) 
    {
	String[] keys = cfg.getSynonyms(type);
	
	for (int i=0; i<keys.length; i++) {
	    String key = keys[i];
	    
	    if (!commandKeywords.containsKey(key)) {
		commandKeywords.put(key, commandType);
	    } else {
		return CommandFeedback.INVALID_DATABASE_DUPLICATES;
	    }
	}
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    
    
    private CommandFeedback readParameterDatabase() {
	Config cfg = new Config();
	
	String[] headerKeySet = (String[])( parameterHeaders.keySet().toArray( new String[parameterHeaders.size()] ) );
	
	for (int i = 0; i < parameterHeaders.size(); i++) {

	    CommandFeedback feedback = addParameterSynonyms(cfg, headerKeySet[i], parameterHeaders.get(headerKeySet[i]));

	    if (feedback == CommandFeedback.INVALID_DATABASE_DUPLICATES) {
		return CommandFeedback.INVALID_DATABASE_DUPLICATES;
	    }

	}

	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    
    private CommandFeedback addParameterSynonyms(Config cfg, String type, ParameterType parameterType) 
    {
	String[] keys = cfg.getSynonyms(type);
	
	for (int i=0; i<keys.length; i++) {
	    String key = keys[i];
	    
	    if (!parameterKeywords.containsKey(key)) {
		parameterKeywords.put(key, parameterType);
	    } else {
		return CommandFeedback.INVALID_DATABASE_DUPLICATES;
	    }
	}
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    
    public void processUserInput(String input) {
	String commandString = getFirstWord(input);
	CommandType mainCommand = interpretCommand(commandString);
	
	checkCommandValidity(mainCommand); // throws invalid error
	processCommandArgument(input);
	
	if (hasParameters(mainCommand)) {
	    parseAndProcessParameters(input);
	}
	
	command.setCommandType(mainCommand);
    }
    
    private void processCommandArgument(String input) {
	String commandArgument = (input.replaceFirst(getFirstWord(input), "").trim()).split("-")[0];
	
	command.setDescription(commandArgument);
    }


    private void checkCommandValidity(CommandType mainCommand) throws InvalidParameterException {
	if (mainCommand == CommandType.INVALID) {
	    throw new InvalidParameterException(INVALID_COMMAND_TYPE);
	}
    }
    
    
    private void parseAndProcessParameters(String input) throws IllegalArgumentException {
	String[] inputParameters = input.split("-");
	
	for (int i=1; i<inputParameters.length; i++) { // Ignore the command, focus on the parameter arguments
	    String paraTypeString = getFirstWord(inputParameters[i]);
	    String paraArgument = inputParameters[i].substring(paraTypeString.length()).trim();
	    
	    if (paraArgument.isEmpty()) {
		throw new IllegalArgumentException(EXCEPTION_EMPTY_ARGUMENT);
	    }
	    
	    ParameterType parameterType = interpretParameter(paraTypeString);
	    if (parameterType == ParameterType.INVALID) {
		throw new IllegalArgumentException(INVALID_PARAMETER_TYPE);
	    }
	    
	    
	    CommandFeedback feedback = processParameter(parameterType, paraArgument);
	    isParameterArgumentValid(feedback);
	    
	    checkIfParameterExists(parameterType); 
	}
	
    }


    private void checkIfParameterExists(ParameterType parameterType) throws IllegalArgumentException {
	if (currentParameters.contains(parameterType)) {
	throw new IllegalArgumentException(EXCEPTION_DUPLICATE_PARAMETERS);
	}
	currentParameters.add(parameterType);
    }
    
    private void isParameterArgumentValid (CommandFeedback feedback) throws InvalidParameterException {
	switch (feedback) {
	case INVALID_START_TIME:
	    throw new InvalidParameterException(INVALID_START_TIME);
	case INVALID_END_TIME:
	    throw new InvalidParameterException(INVALID_END_TIME);
	case INVALID_REMIND_TIME:
	    throw new InvalidParameterException(INVALID_REMINDER_TIME);
	case INVALID_PRIORITY:
	    throw new InvalidParameterException(INVALID_PRIORITY_REF);
	case INVALID_FOLDER_REF:
	    throw new InvalidParameterException(INVALID_FOLDER_REF);
	default:
	    return; // Do nothing	    
	}
	
    }
    
    private String getFirstWord(String input) {
	return input.trim().split("\\s+")[0].toLowerCase();
    }
 
    
    public boolean hasParameters(CommandType command) {
	if (command == CommandType.ADD || command == CommandType.DELETE || command == CommandType.MARK || command == CommandType.MODIFY || command == CommandType.SEARCH) {
	    return true;
	}
	    
	return false;
    }
    
    private CommandFeedback processParameter(ParameterType parameterType, String argument) throws InvalidParameterException
    {	
	switch(parameterType) {
	
	case START_TIME:
	    return command.setStartTime(argument);
	case END_TIME:
	    return command.setEndTime(argument);
	case REMINDER_TIME:
	    return command.setRemindTime(argument);
	case PRIORITY:
	    return command.setPriority(argument);
	case TASK_ID:
	    return command.setTaskId(argument);
	case FOLDER:
	    return command.setFolder(argument);
	case LOCATION:
	    command.setLocation(argument);
	    return CommandFeedback.SUCCESSFUL_OPERATION;	    
	    
	default:
	    throw new InvalidParameterException(INVALID_PARAMETER_TYPE); 
	
	}
	
    }
    
    
    
    // Can be used for color-coding Ð Validate keywords:
    
    public ParameterType interpretParameter(String parameterString) {
	if (!parameterKeywords.containsKey(parameterString)) {
	    return ParameterType.INVALID;
	}
	
	return parameterKeywords.get(parameterString);
    }
    
    public CommandType interpretCommand(String commandString) {
	if (!commandKeywords.containsKey(commandString)) {
	    return CommandType.INVALID;
	}
	
	return commandKeywords.get(commandString);
    }
      
    
    public Command getCommandAndPara() {
	return command;
    }
    
}
