package interpreter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.Controller;


/**
 * Interpreter:
 * 
 * @author Mohit Shridhar
 * @Matric A0105912N
 * 
 */


//@author A0105912N

public class Interpreter {
    
    private static final String EXCEPTION_ADDING_REMINDER_TO_FLOATING_TASK = "This is a non-timed task. If you want to add a reminder, please also specify start/end time";
    // Config keys:
    private static final String KEY_QUIT = "quit";
    private static final String KEY_IMPORT = "import";
    private static final String KEY_EXPORT = "export";
    private static final String KEY_DISPLAY_IN_TIME = "display";
    private static final String KEY_DISPLAY_FLOAT = "displayFloat";
    private static final String KEY_DISPLAY_ALL = "displayAll";
    private static final String KEY_REDO = "redo";
    private static final String KEY_UNDO = "undo";
    private static final String KEY_MONTH = "month";
    private static final String KEY_WEEK = "week";
    private static final String KEY_TOMORROW = "tomorrow";
    private static final String KEY_TODAY = "today";
    private static final String KEY_NOW = "now";
    private static final String KEY_SEARACH = "search";
    private static final String KEY_UNMARK = "unmark";
    private static final String KEY_MARK = "mark";
    private static final String KEY_MODIFY = "modify";
    private static final String KEY_CLEAR = "clear";
    private static final String KEY_CLEAR_COMPLETED = "clearCompleted";
    private static final String KEY_DELETE = "delete";
    private static final String KEY_ADD = "add";
    private static final String STRING_DEFAULT = "default";
    private static final String DELIMETER = "-";
    
    private static final String KEY_TASK_ID = "taskID";
    private static final String KEY_FOLDER = "folder";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_REMIND_TIME = "reminderTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_START_TIME = "startTime";
    
    // Exception Messages:
    private static final String INVALID_COMMAND_TYPE = "Make sure that the first word is a valid command";
    private static final String EXCEPTION_EMPTY_ARGUMENT = "\"-%1$s\" is empty, please provide more info";
    private static final String EXCEPTION_DUPLICATE_PARAMETERS = "You have specified \"-%1$s\" more than once; it's confusing";
    private static final String INVALID_START_TIME = "Is the Start Time correct? Eg: tues, 19 May 2015 13:45, 05/19/2015, tonight, 10 min from now etc.";
    private static final String INVALID_END_TIME = "Is the End Time correct? Eg: tues, 19 May 2015 13:45, 05/19/2015, tonight, 10 min from now etc.";
    private static final String INVALID_REMINDER_TIME = "Is the Remind Time correct? Eg: tues, 19 May 2015 13:45, 05/19/2015, tonight, 10 min from now etc.";
    private static final String INVALID_PRIORITY_REF = "Is the priority high OR med OR low OR none (default)?";
    private static final String INVALID_FOLDER_REF = "Make sure that the name of the folder actually exists above";
    private static final String INVALID_TASK_ID = "Can't find Task Number '%1$s', check that it actually exists";
    private static final String INVALID_PARAMETER_TYPE = "What is \"-%1$s\"? Please correct the parameter name";
    private static final String INVALID_COMMAND_ARGUMENT = "You must added a description after \"%1$s\"";
    private static final String ERROR_DATABASE_DUPLICATE_PARA = "Duplicate keywords were found in the 'parameter' database for \"%1$s\"";
    private static final String ERROR_DATABASE_DUPLICATE_COMMAND = "Duplicate keywords were found in the 'command' database for \"%1$s\"";
    private static final String EXCEPTION_EMPTY_LOCATION = "Initiated location parameter cannot have a empty argument";
    private static final String EXCEPTION_EMPTY_DESCRIPTION = "Description cannot be empty";
    private static final String EXCEPTION_EMPTY_KEYWORD_IN_DATABASE = "The database contains empty synonym(s) in \"%1$s\"";
    private static final String EXCEPTION_KEYWORD_MULTIPLE_WORDS = "The synonym(s) for \"%1$s\" have to be single words";
    private static final String EXCEPTION_END_TIME_BEFORE_START_TIME = "Please check that 'start time' occurs chronologically before 'end time'";
    private static final String EXCEPTION_ID_NOT_SPECIFIED = "Please specify the Task Number of the task";
    
    /* DEPRECIATED:
    private static final String EXCEPTION_NO_END_TIME_SPECIFIED = "You must specify 'end time' since you have specified 'start time' OR just specify 'end time'";
    */
    
    // Logging Info:
    private static final String INFO_READ_KEYWORD_DATABASE = "Command and Keyword database intiated properly";
    private static final String INFO_LOADED_KEYWORD_HEADERS = "Command and Keyword headers loaded";
    private static final String INFO_SETTING_PANE_CHECK_NEW_DATABASE = "GUI Settings Pane using interpreter";
    private static final String INFO_SETTINGS_PANE_SAVING_CONFIG = "GUI Settings Pane saving Config file";
    
    // Other exceptions:
    private static final int EXCEPTION_NON_EXISTENT_ID = -1;
    private static int floatingTaskGuiRef;
    public final static Logger logger = Controller.getLogger();
    
    // Hash maps:
    private static Map<String, CommandType> commandKeywords = new HashMap<String, CommandType>();
    private static Map<String, ParameterType> parameterKeywords = new HashMap<String, ParameterType>();
    
    private static Map<CommandType, String> defaultCommandSynonym = new HashMap<CommandType, String>();
    private static Map<ParameterType, String> defaultParameterSynonym = new HashMap<ParameterType, String>();
    
    private static Map<Integer, Integer> guiIdRef = new HashMap<Integer, Integer>();
    private static Config cfg = new Config();
    
    private ArrayList<ParameterType> currentParameters = new ArrayList<ParameterType>(); // For duplicates
    private Command command = new Command();
    
    private String currentFolder = STRING_DEFAULT;
    private static final int FIRST_ARGUMENT = 0;
    
    private static boolean isGuiIdEnabled = false;
    
    /* Keyword Headers: Mapping Config file elements to Command & Parameter types */
    
    private static final Map<String, CommandType> commandHeaders;
    private static final Map<String, ParameterType> parameterHeaders;
    static
    {
        // Main Commands:  
        
        commandHeaders = new HashMap<String, CommandType>();
        
        commandHeaders.put(KEY_ADD, CommandType.ADD);
        commandHeaders.put(KEY_DELETE, CommandType.DELETE);
        commandHeaders.put(KEY_CLEAR_COMPLETED, CommandType.DELETE_ALL_COMPLETED);
        commandHeaders.put(KEY_CLEAR, CommandType.CLEAR);
        commandHeaders.put(KEY_MODIFY, CommandType.MODIFY);
        commandHeaders.put(KEY_MARK, CommandType.MARK);
        commandHeaders.put(KEY_UNMARK, CommandType.UNMARK);
        commandHeaders.put(KEY_SEARACH, CommandType.SEARCH);
        commandHeaders.put(KEY_NOW, CommandType.DISPLAY_NOW);
        commandHeaders.put(KEY_TODAY, CommandType.DISPLAY_TODAY);
        commandHeaders.put(KEY_TOMORROW, CommandType.DISPLAY_TOMORROW);
        commandHeaders.put(KEY_WEEK, CommandType.DISPLAY_WEEK);
        commandHeaders.put(KEY_MONTH, CommandType.DISPLAY_MONTH);
        commandHeaders.put(KEY_UNDO, CommandType.UNDO);
        commandHeaders.put(KEY_REDO, CommandType.REDO);
        commandHeaders.put(KEY_DISPLAY_ALL, CommandType.DISPLAY_ALL);
        commandHeaders.put(KEY_DISPLAY_FLOAT, CommandType.DISPLAY_ALL_FLOAT);
        commandHeaders.put(KEY_DISPLAY_IN_TIME, CommandType.DISPLAY_IN_TIME);
        commandHeaders.put(KEY_EXPORT, CommandType.EXPORT);
        commandHeaders.put(KEY_IMPORT, CommandType.IMPORT);
        commandHeaders.put(KEY_QUIT, CommandType.QUIT);
        
        // Parameter Commands:
        
        parameterHeaders = new HashMap<String, ParameterType>();
        
        parameterHeaders.put(KEY_START_TIME, ParameterType.START_TIME);
        parameterHeaders.put(KEY_END_TIME, ParameterType.END_TIME);
        parameterHeaders.put(KEY_REMIND_TIME, ParameterType.REMINDER_TIME);
        parameterHeaders.put(KEY_PRIORITY,ParameterType.PRIORITY);
        parameterHeaders.put(KEY_LOCATION, ParameterType.LOCATION);
        parameterHeaders.put(KEY_FOLDER, ParameterType.FOLDER);
        parameterHeaders.put(KEY_TASK_ID, ParameterType.TASK_ID);
        
        logger.log(Level.FINE, INFO_LOADED_KEYWORD_HEADERS);
        
	readCommandDatabase();
	readParameterDatabase();
	
	logger.log(Level.FINE, INFO_READ_KEYWORD_DATABASE);
    }
    
    public Interpreter() {
	// Do nothing
    }
  
    
    /*
     * To be used by settings pane for checking validity of keywords:
     */
   
    public Interpreter(Properties props) {
	
	logger.log(Level.FINEST, INFO_SETTING_PANE_CHECK_NEW_DATABASE);
	
	commandKeywords.clear();
	parameterKeywords.clear();
	
	readCommandDatabase(props);
	readParameterDatabase(props);
    }
    
    /*
     * Used when Config file has to be re-read:
     */
    
    public Interpreter(boolean rebuild) {
	
	logger.log(Level.FINEST, INFO_SETTINGS_PANE_SAVING_CONFIG);
	
	if (rebuild) {
	    cfg = new Config();
	    
	    commandKeywords.clear();
	    parameterKeywords.clear();
	    
	    readCommandDatabase();
	    readParameterDatabase();
	}
    }
    
    
    /**
     * Reads keywords from txtfile and saves them in the local memory (hash table)
     * 
     */
    
    private static void readCommandDatabase() throws IllegalArgumentException {
        
	defaultCommandSynonym.clear();
	
        String[] headerKeySet = (String[])( commandHeaders.keySet().toArray( new String[commandHeaders.size()] ) );
        
        for (int i = 0; i < commandHeaders.size(); i++) {

            CommandFeedback feedback = addCommandSynonyms(cfg, headerKeySet[i], commandHeaders.get(headerKeySet[i]));

            checkCommandDatabaseExceptions(headerKeySet, i, feedback);
        }
    }
    
    
    /*
     * Function to be used by settings pane:
     */
    
    private static void readCommandDatabase(Properties props) throws IllegalArgumentException {
        
	defaultCommandSynonym.clear();
	
	String[] headerKeySet = (String[])( commandHeaders.keySet().toArray( new String[commandHeaders.size()] ) );
        
        for (int i = 0; i < commandHeaders.size(); i++) {

            CommandFeedback feedback = addCommandSynonyms(props, headerKeySet[i], commandHeaders.get(headerKeySet[i]));

            checkCommandDatabaseExceptions(headerKeySet, i, feedback);
        }
    }

    public static void checkCommandDatabaseExceptions(String[] headerKeySet, int i,
	    CommandFeedback feedback) {
	if (feedback == CommandFeedback.INVALID_DATABASE_DUPLICATES) {
	    logger.log(Level.WARNING, String.format(ERROR_DATABASE_DUPLICATE_COMMAND, headerKeySet[i].toString()));
	    throw new IllegalArgumentException(String.format(ERROR_DATABASE_DUPLICATE_COMMAND, headerKeySet[i].toString()));
	}

	if (feedback == CommandFeedback.EMPTY_KEYWORD) {
	    logger.log(Level.WARNING, String.format(EXCEPTION_EMPTY_KEYWORD_IN_DATABASE, headerKeySet[i].toString()));
	    throw new IllegalArgumentException(String.format(EXCEPTION_EMPTY_KEYWORD_IN_DATABASE, headerKeySet[i].toString()));
	}

	if (feedback == CommandFeedback.MULTIPLE_WORD_KEYWORD) {
	    logger.log(Level.WARNING, String.format(EXCEPTION_KEYWORD_MULTIPLE_WORDS, headerKeySet[i].toString()));
	    throw new IllegalArgumentException(String.format(EXCEPTION_KEYWORD_MULTIPLE_WORDS, headerKeySet[i].toString()));
	}
    }

    private static CommandFeedback addCommandSynonyms(Config cfg, String type, CommandType commandType) 
    {
        String[] keys = cfg.getSynonyms(type);
        
        for (int i=0; i<keys.length; i++) {
            String key = keys[i].trim();
            
            if (hasMultipleWords(key)) {
        	return CommandFeedback.MULTIPLE_WORD_KEYWORD;
            } else if (key.isEmpty()) {
        	return CommandFeedback.EMPTY_KEYWORD;
            } else if (!commandKeywords.containsKey(key)) {
                commandKeywords.put(key, commandType);
            } else {
                return CommandFeedback.INVALID_DATABASE_DUPLICATES;
            }
        }
        
        defaultCommandSynonym.put(commandType, keys[0]);
        
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    /*
     * Function to be used by settings pane:
     */
    
    private static CommandFeedback addCommandSynonyms(Properties props, String type, CommandType commandType) 
    {
        String[] keys = parseProperties(props, type);
        
        for (int i=0; i<keys.length; i++) {
            String key = keys[i].trim();
            
            if (hasMultipleWords(key)) {
        	return CommandFeedback.MULTIPLE_WORD_KEYWORD;
            } else if (key.isEmpty()) {
        	return CommandFeedback.EMPTY_KEYWORD;
            } else if (!commandKeywords.containsKey(key)) {
                commandKeywords.put(key, commandType);
            } else {
                return CommandFeedback.INVALID_DATABASE_DUPLICATES;
            }
        }
        
        defaultCommandSynonym.put(commandType, keys[0]);
        
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    
    
    private static void readParameterDatabase() throws IllegalArgumentException {
	
	defaultParameterSynonym.clear();
        
        String[] headerKeySet = (String[])( parameterHeaders.keySet().toArray( new String[parameterHeaders.size()] ) );
        
        for (int i = 0; i < parameterHeaders.size(); i++) {

            CommandFeedback feedback = addParameterSynonyms(cfg, headerKeySet[i], parameterHeaders.get(headerKeySet[i]));

            checkParameterDatabaseExceptions(headerKeySet, i, feedback);
        }
    }
    
    /*
     * Function to be used by settings pane:
     */
    
    private static void readParameterDatabase(Properties props) throws IllegalArgumentException {
	
	defaultParameterSynonym.clear();
        
        String[] headerKeySet = (String[])( parameterHeaders.keySet().toArray( new String[parameterHeaders.size()] ) );
        
        for (int i = 0; i < parameterHeaders.size(); i++) {

            CommandFeedback feedback = addParameterSynonyms(props, headerKeySet[i], parameterHeaders.get(headerKeySet[i]));

            checkParameterDatabaseExceptions(headerKeySet, i, feedback);
        }
    }


    public static void checkParameterDatabaseExceptions(String[] headerKeySet,
	    int i, CommandFeedback feedback) {
	if (feedback == CommandFeedback.INVALID_DATABASE_DUPLICATES) {
	    logger.log(Level.WARNING, String.format(ERROR_DATABASE_DUPLICATE_PARA, headerKeySet[i].toString()));
	    throw new IllegalArgumentException(String.format(ERROR_DATABASE_DUPLICATE_PARA, headerKeySet[i].toString()));
	}

	if (feedback == CommandFeedback.EMPTY_KEYWORD) {
	    logger.log(Level.WARNING, String.format(EXCEPTION_EMPTY_KEYWORD_IN_DATABASE, headerKeySet[i].toString()));
	    throw new IllegalArgumentException(String.format(EXCEPTION_EMPTY_KEYWORD_IN_DATABASE, headerKeySet[i].toString()));
	}

	if (feedback == CommandFeedback.MULTIPLE_WORD_KEYWORD) {
	    logger.log(Level.WARNING, String.format(EXCEPTION_KEYWORD_MULTIPLE_WORDS, headerKeySet[i].toString()));
	    throw new IllegalArgumentException(String.format(EXCEPTION_KEYWORD_MULTIPLE_WORDS, headerKeySet[i].toString()));
	}
    }


    private static CommandFeedback addParameterSynonyms(Config cfg, String type, ParameterType parameterType) 
    {
	String[] keys = cfg.getSynonyms(type);

	for (int i=0; i<keys.length; i++) {
	    String key = keys[i].trim();

	    if (hasMultipleWords(key)) {
		return CommandFeedback.MULTIPLE_WORD_KEYWORD;
	    } else if (key.isEmpty()) {
		return CommandFeedback.EMPTY_KEYWORD;
            } else if (!parameterKeywords.containsKey(key)) {
                parameterKeywords.put(key, parameterType);
            } else {
                return CommandFeedback.INVALID_DATABASE_DUPLICATES;
            }
        }
        
        defaultParameterSynonym.put(parameterType, keys[0]);
        
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    /*
     * Function to be used by settings pane:
     */
    
    private static CommandFeedback addParameterSynonyms(Properties props, String type, ParameterType parameterType) 
    {
	String[] keys = parseProperties(props, type);
        
        for (int i=0; i<keys.length; i++) {
            String key = keys[i].trim();
            
            if (hasMultipleWords(key)) {
        	return CommandFeedback.MULTIPLE_WORD_KEYWORD;
            } else if (key.isEmpty()) {
        	return CommandFeedback.EMPTY_KEYWORD;
            } else if (!parameterKeywords.containsKey(key)) {
                parameterKeywords.put(key, parameterType);
            } else {
                return CommandFeedback.INVALID_DATABASE_DUPLICATES;
            }
        }
        
        defaultParameterSynonym.put(parameterType, keys[0]);
        
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }


    public static String[] parseProperties(Properties props, String type) {
	return ((String) props.get((String) type)).trim().toLowerCase().split(",");
    }
    
    private static boolean hasMultipleWords(String input) {
	return input.indexOf(' ') >= 0;
    }
    
    public void processUserInput(String input) {
        CommandType mainCommand = extractMainCommand(input);
        
        checkCommandValidity(mainCommand);
        processCommandArgument(input);
        
        if (hasParameters(mainCommand)) {
            parseAndProcessParameters(input);
        }
        
        command.setCommandType(mainCommand); 
        checkForOtherExceptions();
        
        updateCurrFolderReference();
    }


    public CommandType extractMainCommand(String input) {
	String commandString = getFirstWord(input);
        CommandType mainCommand = interpretCommand(commandString);
	return mainCommand;
    }
    
    private void checkForOtherExceptions() throws IllegalArgumentException {
	
	/* DEPRECIATED: Logic adds default end time
	if (!currentParameters.contains(ParameterType.END_TIME) && currentParameters.contains(ParameterType.START_TIME)) {
	    logger.log(Level.WARNING, EXCEPTION_NO_END_TIME_SPECIFIED);
	    throw new IllegalArgumentException(EXCEPTION_NO_END_TIME_SPECIFIED);
	}
	*/
	
	if (isStartEndNotChronological()) {
	    logger.log(Level.WARNING, EXCEPTION_END_TIME_BEFORE_START_TIME);
	    throw new IllegalArgumentException(EXCEPTION_END_TIME_BEFORE_START_TIME);
	}
	
	
	if (hasNoTaskId()) {
	    logger.log(Level.WARNING, EXCEPTION_ID_NOT_SPECIFIED);
	    throw new IllegalArgumentException(EXCEPTION_ID_NOT_SPECIFIED);
	}
	
	if (addingReminderToFloatingTask()) {
	    
	    logger.log(Level.WARNING, EXCEPTION_ADDING_REMINDER_TO_FLOATING_TASK);
	    throw new IllegalArgumentException(EXCEPTION_ADDING_REMINDER_TO_FLOATING_TASK);
	}
	
    }


    private boolean addingReminderToFloatingTask() {	
	return command.getCommandType() == CommandType.MODIFY && isGuiIdEnabled && isFloatingTask(command.getParameters().getGuiIdRef()) && currentParameters.contains(ParameterType.REMINDER_TIME) && !(currentParameters.contains(ParameterType.START_TIME) || currentParameters.contains(ParameterType.END_TIME));
    }


    private boolean hasNoTaskId() {
	return needsId(command.getCommandType()) && !currentParameters.contains(ParameterType.TASK_ID);
    }


    private boolean isStartEndNotChronological() {
	return (currentParameters.contains(ParameterType.END_TIME) && currentParameters.contains(ParameterType.START_TIME)) && command.getParameters().getStartTime().after(command.getParameters().getEndTime());
    }
    
    private void updateCurrFolderReference() {
	if (command.getParameters().getFolder() == null) {
	    command.getParameters().setFolder(currentFolder);
	}
    }
    
    public boolean isFloatingTask() {
	if (currentParameters.contains(ParameterType.START_TIME) || currentParameters.contains(ParameterType.END_TIME) || currentParameters.contains(ParameterType.REMINDER_TIME)) {
	    return false;
	}
	
	return true;
    }
    
    private void processCommandArgument (String input) throws IllegalArgumentException {
	
	String commandArgument = null;
	
	try {
		commandArgument = getDescription(input);
	} catch (ArrayIndexOutOfBoundsException aE) {
	    return;
	}
		
        CommandFeedback feedback = command.setDescription(commandArgument);
        
        if (needsDescription(interpretCommand(getFirstWord(input))) && feedback == CommandFeedback.EMPTY_DESCRIPTION) {
            logger.log(Level.WARNING, String.format(INVALID_COMMAND_ARGUMENT, getFirstWord(input)));
            throw new IllegalArgumentException(String.format(INVALID_COMMAND_ARGUMENT, getFirstWord(input)));
        }
                
    }


    private String getDescription(String input) {
	return (input.substring(getFirstWord(input).length()).trim()).split(DELIMETER)[FIRST_ARGUMENT].trim();
    }
    
    private boolean needsDescription(CommandType userCommand) {
        
        if (userCommand == CommandType.ADD || userCommand == CommandType.SEARCH || userCommand == CommandType.IMPORT || userCommand == CommandType.EXPORT) {
            return true;
        }
        
        return false;
    }
    
    private boolean needsId(CommandType userCommand) {
	
        if (userCommand == CommandType.MODIFY || userCommand == CommandType.MARK || userCommand == CommandType.UNMARK) {
            return true;
        }
        
	return false;
    }


    private void checkCommandValidity(CommandType mainCommand) throws InvalidParameterException {
        if (mainCommand == CommandType.INVALID) {
            logger.log(Level.WARNING, INVALID_COMMAND_TYPE);
            throw new InvalidParameterException(INVALID_COMMAND_TYPE);
        }
    }
    
    
    private void parseAndProcessParameters(String input) throws IllegalArgumentException {
        String[] inputParameters = input.split(DELIMETER);
        
        currentParameters.clear();
        
        for (int i=1; i<inputParameters.length; i++) { // Ignore the command, focus on the parameter arguments
            String paraTypeString = getFirstWord(inputParameters[i]);
            String paraArgument = isolateArgument(inputParameters, i, paraTypeString);
            
            
            ParameterType parameterType = interpretParameter(paraTypeString);
            checkForParameterExceptions(paraTypeString, paraArgument, parameterType);
            
            CommandFeedback feedback = processParameter(parameterType, paraArgument);
            isParameterArgumentValid(paraArgument, feedback);
            
            checkIfParameterExists(paraTypeString, parameterType); 
        }
        
    }


    public String isolateArgument(String[] inputParameters, int i,
	    String paraTypeString) {
	return inputParameters[i].substring(paraTypeString.length()).trim();
    }


    public void checkForParameterExceptions(String paraTypeString, String paraArgument,
	    ParameterType parameterType) {
	if (parameterType == ParameterType.INVALID) {
	    logger.log(Level.WARNING, String.format(INVALID_PARAMETER_TYPE, paraTypeString));
	    throw new IllegalArgumentException(String.format(INVALID_PARAMETER_TYPE, paraTypeString));
	}
	
	if (paraArgument.isEmpty()) {
	    logger.log(Level.WARNING, String.format(EXCEPTION_EMPTY_ARGUMENT, paraTypeString));
	    throw new IllegalArgumentException(String.format(EXCEPTION_EMPTY_ARGUMENT, paraTypeString));
	}
    }


    public void checkIfParameterExists(String paraTypeString, ParameterType parameterType) throws IllegalArgumentException {
	if (currentParameters.contains(parameterType)) {
	    logger.log(Level.WARNING, String.format(EXCEPTION_DUPLICATE_PARAMETERS, paraTypeString));
            throw new IllegalArgumentException(String.format(EXCEPTION_DUPLICATE_PARAMETERS, paraTypeString));
        }
	
        currentParameters.add(parameterType);
    }
    
    private void isParameterArgumentValid (String paraArgument, CommandFeedback feedback) throws InvalidParameterException {
        switch (feedback) {
        case EMPTY_DESCRIPTION:
            logger.log(Level.WARNING, EXCEPTION_EMPTY_DESCRIPTION);
            throw new InvalidParameterException(EXCEPTION_EMPTY_DESCRIPTION);   
        case EMPTY_LOCATION:
            logger.log(Level.WARNING, EXCEPTION_EMPTY_LOCATION);
            throw new InvalidParameterException(EXCEPTION_EMPTY_LOCATION);          
        case INVALID_START_TIME:
            logger.log(Level.WARNING, INVALID_START_TIME);
            throw new InvalidParameterException(INVALID_START_TIME);
        case INVALID_END_TIME:
            logger.log(Level.WARNING, INVALID_END_TIME);
            throw new InvalidParameterException(INVALID_END_TIME);
        case INVALID_REMIND_TIME:
            logger.log(Level.WARNING, INVALID_REMINDER_TIME);
            throw new InvalidParameterException(INVALID_REMINDER_TIME);
        case INVALID_PRIORITY:
            logger.log(Level.WARNING, INVALID_PRIORITY_REF);
            throw new InvalidParameterException(INVALID_PRIORITY_REF);
        case INVALID_FOLDER_REF:
            logger.log(Level.WARNING, INVALID_FOLDER_REF);
            throw new InvalidParameterException(INVALID_FOLDER_REF);
        case INVALID_TASK_ID:
            logger.log(Level.WARNING, String.format(INVALID_TASK_ID, paraArgument));
            throw new InvalidParameterException(String.format(INVALID_TASK_ID, paraArgument));           
        default:
            return;        
        }
        
    }
    
    public String getFirstWord(String input) {
        return input.trim().split("\\s+")[0].toLowerCase();
    }
 
    
    public boolean hasParameters(CommandType command) {
        if (command == CommandType.ADD || command == CommandType.DELETE || command == CommandType.MARK || command == CommandType.UNMARK || command == CommandType.MODIFY || command == CommandType.SEARCH || command == CommandType.DISPLAY_IN_TIME || command == CommandType.CLEAR || command == CommandType.DELETE_ALL_COMPLETED) {
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
    
    public String getDefaultCommandSyn(CommandType commandType) {
	return defaultCommandSynonym.get(commandType);
    }
    
    public String getDefaultParaSyn(ParameterType parameterType) {
	return defaultParameterSynonym.get(parameterType);
    }
    
    /*
     * For interfacing with GUI:
     */
    
    public static void setIsGuiIdEnabled(boolean isGuiIdEnabled) {
	Interpreter.isGuiIdEnabled = isGuiIdEnabled;
    }
    
    public static boolean checkIsGuiIdEnabled() {
	return isGuiIdEnabled;
    }

    public static void addGuiId(int guiId, int realTaskId){
	if (isGuiIdEnabled) {
	    guiIdRef.put(guiId, realTaskId);
	}

	return;
    }

    public static int getRealId(int guiId) {
	if (isGuiIdEnabled) {

	    if (guiIdRef.get(guiId) == null) {
		return EXCEPTION_NON_EXISTENT_ID;
	    }

	    return guiIdRef.get(guiId);
	}

	return EXCEPTION_NON_EXISTENT_ID;
    }

    public static void clearGuiIdMap() {
	guiIdRef.clear();
    }
    
    
    public static void setFloatingTaskGuiRef(int startGuiIndex) {
	
	if (isGuiIdEnabled) {
	    floatingTaskGuiRef = startGuiIndex;
	}
    }
    
    public static boolean isFloatingTask(int guiId) {
	if (isGuiIdEnabled && guiId >= floatingTaskGuiRef) {
	    return true;
	}
	
	return false;
    }
    
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
    
    public String getCurrentFolder() {
	return currentFolder;
    }
    
    public void setCurrentFolder(String currFolderName) {
	currentFolder = currFolderName;
    }
}