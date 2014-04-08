package interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

//@author A0105912N

/* 
 * InterpreterTest.java has 85.4% path coverage of the Interpreter package. 
 * The other 15% mainly deals with GUI interaction and Config IO. 
 * 
 */

public class InterpreterTest {
    
    // Exception Messages:
    private static final String INVALID_COMMAND_TYPE = "Invalid command type";
    private static final String EXCEPTION_EMPTY_ARGUMENT = "Cannot accept empty parameter argument";
    private static final String INVALID_START_TIME = "Invalid start time";
    private static final String INVALID_END_TIME = "Invalid end time";
    private static final String INVALID_REMINDER_TIME = "Invalid remind time";
    private static final String INVALID_PRIORITY_REF = "Invalid priority reference";
    private static final String INVALID_FOLDER_REF = "Invalid folder reference";
    private static final String INVALID_TASK_ID = "Invalid task id reference";
    private static final String INVALID_PARAMETER_TYPE = "Invalid parameter type";
    private static final String INVALID_COMMAND_ARGUMENT = "The description for this command cannot be empty";
    private static final String EXCEPTION_END_TIME_BEFORE_START_TIME = "Please check that 'start time' occurs chronologically before 'end time'";
    private static final String EXCEPTION_ID_NOT_SPECIFIED = "Please specify the ID of the task";
    
    // Initialize interpreter:
    Interpreter interpreter = new Interpreter();
    
    @Test 
    public void testDatabase() {
	
	synonymInterpretations();
	invalidInterpretaions();
	
	checkAllValidKeywords();
	
    }

    private void checkAllValidKeywords() {
	
	// NOTE: the following keyword strings will be invalid if the Config.cfg file is edited
	
	// Commands:
	assertEquals(CommandType.ADD, interpreter.interpretCommand("add"));
	assertEquals(CommandType.DELETE, interpreter.interpretCommand("remove"));
	assertEquals(CommandType.MODIFY, interpreter.interpretCommand("modify"));
	assertEquals(CommandType.UNMARK, interpreter.interpretCommand("unmark"));
	assertEquals(CommandType.DELETE_ALL_COMPLETED, interpreter.interpretCommand("cc"));
	assertEquals(CommandType.CLEAR, interpreter.interpretCommand("clear"));
	assertEquals(CommandType.SEARCH, interpreter.interpretCommand("search"));
	assertEquals(CommandType.DISPLAY_NOW, interpreter.interpretCommand("now"));
	assertEquals(CommandType.DISPLAY_TODAY, interpreter.interpretCommand("today"));
	assertEquals(CommandType.DISPLAY_TOMORROW, interpreter.interpretCommand("tomr"));
	assertEquals(CommandType.DISPLAY_ALL, interpreter.interpretCommand("all"));
	assertEquals(CommandType.DISPLAY_ALL_FLOAT, interpreter.interpretCommand("float"));
	assertEquals(CommandType.DISPLAY_IN_TIME, interpreter.interpretCommand("display"));
	assertEquals(CommandType.DISPLAY_WEEK, interpreter.interpretCommand("week"));
	assertEquals(CommandType.DISPLAY_MONTH, interpreter.interpretCommand("month"));
	assertEquals(CommandType.UNDO, interpreter.interpretCommand("undo"));
	assertEquals(CommandType.REDO, interpreter.interpretCommand("redo"));
	assertEquals(CommandType.EXPORT, interpreter.interpretCommand("export"));
	assertEquals(CommandType.QUIT, interpreter.interpretCommand("quit"));
	
	// Parameters:
	assertEquals(ParameterType.START_TIME, interpreter.interpretParameter("start"));
	assertEquals(ParameterType.END_TIME, interpreter.interpretParameter("end"));
	assertEquals(ParameterType.REMINDER_TIME, interpreter.interpretParameter("remind"));
	assertEquals(ParameterType.LOCATION, interpreter.interpretParameter("location"));
	assertEquals(ParameterType.FOLDER, interpreter.interpretParameter("folder"));
	assertEquals(ParameterType.TASK_ID, interpreter.interpretParameter("id"));
		
    }

    private void invalidInterpretaions() {
	
	// Invalid because string is expected to be trimmed/cleaned-up before using this function:
	assertEquals(CommandType.INVALID, interpreter.interpretCommand("  add  "));
	assertEquals(CommandType.INVALID, interpreter.interpretCommand("  a  "));
	assertEquals(ParameterType.INVALID, interpreter.interpretParameter(" start"));
	assertEquals(ParameterType.INVALID, interpreter.interpretParameter(" on   "));
	
	// Invalid because string is expected to be in lower-case before using this function:
	assertEquals(CommandType.INVALID, interpreter.interpretCommand("Add"));
	assertEquals(CommandType.INVALID, interpreter.interpretCommand("creAte"));
	assertEquals(ParameterType.INVALID, interpreter.interpretParameter("sTarT"));
	assertEquals(ParameterType.INVALID, interpreter.interpretParameter("oN"));
	
	// Invalid synonyms:
	assertEquals(CommandType.INVALID, interpreter.interpretCommand(" addition "));	
	assertEquals(CommandType.INVALID, interpreter.interpretCommand("addition"));	
	assertEquals(ParameterType.INVALID, interpreter.interpretParameter("startTime"));
    }

    private void synonymInterpretations() {
	
	assertEquals(CommandType.ADD, interpreter.interpretCommand("new"));
	assertEquals(CommandType.ADD, interpreter.interpretCommand("create"));
	assertEquals(CommandType.ADD, interpreter.interpretCommand("a"));
	assertEquals(CommandType.ADD, interpreter.interpretCommand("n"));
	
    }
    
    
    /*
     * The Interpreter class will be used across various parts of the application. So it needs to manage multiple 
     * instantiations, while maintaining a static keyword database
     */
    
    @Test
    public void testMultipleInstances() {
	
	Interpreter secondInterpreter = new Interpreter();
	
	interpreter.processUserInput("aDd new task from 1st instance -end 10 dec 2014 13:35:34 -pri none -loc location 1");
	secondInterpreter.processUserInput("seaRch second task from 2nd instance");
	
	checkFirstInstance(interpreter);
	checkSecondInstance(secondInterpreter);
	
    }
    
    private void checkFirstInstance(Interpreter instance) {
	
	assertEquals(CommandType.ADD, instance.getCommandAndPara().getCommandType());
	assertEquals("new task from 1st instance", instance.getCommandAndPara().getParameters().getDescription());
	assertEquals("0", instance.getCommandAndPara().getParameters().getPriority());
	assertEquals("location 1", instance.getCommandAndPara().getParameters().getLocation());
	
	// In UTC time:
	assertEquals("1418189734000", Long.toString(instance.getCommandAndPara().getParameters().getEndTime().getTimeInMillis()));
	
    }
    
    private void checkSecondInstance(Interpreter instance) {
	
	assertEquals(CommandType.SEARCH, instance.getCommandAndPara().getCommandType());
	assertEquals("second task from 2nd instance", instance.getCommandAndPara().getParameters().getDescription());
	
    }
    
    
    @Test
    public void testTokenization() {
	
	validTokenization();
	handleExceptions();
	checkDefaultKeywords();
		
    }

    private void validTokenization() {
	
	// Note: Commands and Parameters are case insensitive
	interpreter.processUserInput("mOdiFy new descriptiOn -iD 12 -staRt 10 july 2014 12:34:23 -eND 15 july 2014 12:34:23 -ReMiNd 12 july 2014 12:34:23 -Pri high -lOc new location -FOLDER default");
	
	// Check all 'Command & Parameter' properties:
	assertEquals(CommandType.MODIFY, interpreter.getCommandAndPara().getCommandType());
	assertEquals("new descriptiOn", interpreter.getCommandAndPara().getParameters().getDescription());
	assertEquals("12", interpreter.getCommandAndPara().getParameters().getTaskId());
	assertEquals("1404966863000", Long.toString(interpreter.getCommandAndPara().getParameters().getStartTime().getTimeInMillis()));
	assertEquals("1405398863000", Long.toString(interpreter.getCommandAndPara().getParameters().getEndTime().getTimeInMillis()));
	assertEquals("1405139663000", Long.toString(interpreter.getCommandAndPara().getParameters().getRemindTime().getTimeInMillis()));
	assertEquals("1", interpreter.getCommandAndPara().getParameters().getPriority());
	assertEquals("new location", interpreter.getCommandAndPara().getParameters().getLocation());
	assertEquals("default", interpreter.getCommandAndPara().getParameters().getFolder());
	assertEquals(false, interpreter.isFloatingTask());
	
    }
    
    private void handleExceptions() {
	
	// Command: utter gibberish
	try {
	    interpreter.processUserInput("bjad akwjdn");
	} catch (Exception e) {
	    assertEquals(INVALID_COMMAND_TYPE, e.getMessage());
	}
	
	testGuiTaskId();
	
	// Invalid start time:
	try {
	    interpreter.processUserInput("add hahaha -start akwdawdakwd awk");
	} catch (Exception e) {
	    assertEquals(INVALID_START_TIME, e.getMessage());
	}
	
	// Invalid end time:
	try {
	    interpreter.processUserInput("add hahaha -end akwdawdakwd awk");
	} catch (Exception e) {
	    assertEquals(INVALID_END_TIME, e.getMessage());
	}
	
	// Invalid start & time not in chronological order:
	try {
	    interpreter.processUserInput("add hahaha -start 15 july 2014 12:34:23 -end 10 july 2014 12:34:23");
	} catch (Exception e) {
	    assertEquals(EXCEPTION_END_TIME_BEFORE_START_TIME, e.getMessage());
	}
	
	// Invalid remind time:
	try {
	    interpreter.processUserInput("add hahaha -pri med -remind akwdawdakwd awk");
	} catch (Exception e) {
	    assertEquals(INVALID_REMINDER_TIME, e.getMessage());
	}
	
	// Invalid location:
	try {
	    interpreter.processUserInput("add hahaha -loc -folder tues");
	} catch (Exception e) {
	    assertEquals(EXCEPTION_EMPTY_ARGUMENT, e.getMessage());
	}
	
	// Invalid priority:
	try {
	    interpreter.processUserInput("add hahaha -pri nones");
	} catch (Exception e) {
	    assertEquals(INVALID_PRIORITY_REF, e.getMessage());
	}
	
	// Invalid folder:
	try {
	    interpreter.processUserInput("add hahaha -folder kjnad");
	} catch (Exception e) {
	    assertEquals(INVALID_FOLDER_REF, e.getMessage());
	}
	
	// Id not specified:
	try {
	    interpreter.processUserInput("mark tues -pri high");
	} catch (Exception e) {
	    assertEquals(EXCEPTION_ID_NOT_SPECIFIED, e.getMessage());
	}
	
	// Invalid parameter type:
	try {
	    interpreter.processUserInput("mark tues -awd awdnkj -ankjd");
	} catch (Exception e) {
	    assertEquals(INVALID_PARAMETER_TYPE, e.getMessage());
	}
	
	// Description cannot be empty for some commands:
	try {
	    interpreter.processUserInput("add -start tues");
	} catch (Exception e) {
	    assertEquals(INVALID_COMMAND_ARGUMENT, e.getMessage());
	}
	
    }

    private void testGuiTaskId() {
	//(when GUI is using interpreter). Note: GUI ID will always be greater than 0
	
	Interpreter.setIsGuiIdEnabled(true);
	
	Interpreter.addGuiId(2, 15); //Map guiId '2' to real task ID '15
	Interpreter.addGuiId(6, 15);

	interpreter.processUserInput("modify task -id 6 -pri low"); // No exception is raised because 6 is mapped to 15 on the GuiId list
	assertEquals("15", interpreter.getCommandAndPara().getParameters().getTaskId());
	
	try {
	    interpreter.processUserInput("modify task -id 5");
	} catch (Exception e) {
	    assertEquals(INVALID_TASK_ID, e.getMessage());
	}
	
	Interpreter.setIsGuiIdEnabled(false);
    }
    
    private void checkDefaultKeywords() {
	
	// NOTE: the following keyword strings will be invalid if the Config.cfg file is edited

	// Commands:
	assertEquals(CommandType.ADD, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.ADD)));
	assertEquals(CommandType.DELETE, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DELETE)));
	assertEquals(CommandType.MODIFY, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.MODIFY)));
	assertEquals(CommandType.UNMARK, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.UNMARK)));
	assertEquals(CommandType.DELETE_ALL_COMPLETED, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DELETE_ALL_COMPLETED)));
	assertEquals(CommandType.CLEAR, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.CLEAR)));
	assertEquals(CommandType.SEARCH, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.SEARCH)));
	assertEquals(CommandType.DISPLAY_NOW, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_NOW)));
	assertEquals(CommandType.DISPLAY_TODAY, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_TODAY)));
	assertEquals(CommandType.DISPLAY_TOMORROW, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_TOMORROW)));
	assertEquals(CommandType.DISPLAY_ALL, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_ALL)));
	assertEquals(CommandType.DISPLAY_ALL_FLOAT, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_ALL_FLOAT)));
	assertEquals(CommandType.DISPLAY_IN_TIME, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_IN_TIME)));
	assertEquals(CommandType.DISPLAY_WEEK, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_WEEK)));
	assertEquals(CommandType.DISPLAY_MONTH, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.DISPLAY_MONTH)));
	assertEquals(CommandType.UNDO, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.UNDO)));
	assertEquals(CommandType.REDO, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.REDO)));
	assertEquals(CommandType.EXPORT, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.EXPORT)));
	assertEquals(CommandType.QUIT, interpreter.interpretCommand(interpreter.getDefaultCommandSyn(CommandType.QUIT)));

	// Parameters:
	assertEquals(ParameterType.START_TIME, interpreter.interpretParameter(interpreter.getDefaultParaSyn(ParameterType.START_TIME)));
	assertEquals(ParameterType.END_TIME, interpreter.interpretParameter(interpreter.getDefaultParaSyn(ParameterType.END_TIME)));
	assertEquals(ParameterType.REMINDER_TIME, interpreter.interpretParameter(interpreter.getDefaultParaSyn(ParameterType.REMINDER_TIME)));
	assertEquals(ParameterType.LOCATION, interpreter.interpretParameter(interpreter.getDefaultParaSyn(ParameterType.LOCATION)));
	assertEquals(ParameterType.FOLDER, interpreter.interpretParameter(interpreter.getDefaultParaSyn(ParameterType.FOLDER)));
	assertEquals(ParameterType.TASK_ID, interpreter.interpretParameter(interpreter.getDefaultParaSyn(ParameterType.TASK_ID)));
	
    }
    
    @Test 
    public void testOtherInterpreterConstructors() {
	
	// Re-build database from given 'Property' file
	interpreter = new Interpreter(new Config().getConfigFile());
	
	// Carry out all the test above (again):
	testDatabase();	
	testMultipleInstances();
	testTokenization();
	
	
	// Re-build database by reading the Config file again:
	interpreter = new Interpreter(true);
	
	// Carry out all the test above (again):
	testDatabase();	
	testMultipleInstances();
	testTokenization();
	
    }
}
