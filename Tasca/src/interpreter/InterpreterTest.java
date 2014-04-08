package interpreter;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

//@author A0105912N

/*
 * This file tests the entire interpreter package
 */

public class InterpreterTest {
    
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
	
	interpreter.processUserInput("aDd new task from 1st instance -end 10 dec 2014 13:35:34 -pri high -loc location 1");
	secondInterpreter.processUserInput("seaRch second task from 2nd instance");
	
	checkFirstInstance(interpreter);
	checkSecondInstance(secondInterpreter);
	
    }
    
    private void checkFirstInstance(Interpreter instance) {
	
	assertEquals(CommandType.ADD, instance.getCommandAndPara().getCommandType());
	assertEquals("new task from 1st instance", instance.getCommandAndPara().getParameters().getDescription());
	assertEquals("1", instance.getCommandAndPara().getParameters().getPriority());
	assertEquals("location 1", instance.getCommandAndPara().getParameters().getLocation());
	
	// In UTC time:
	assertEquals("1418189734000", Long.toString(instance.getCommandAndPara().getParameters().getEndTime().getTimeInMillis()));
	
    }
    
    private void checkSecondInstance(Interpreter instance) {
	
	assertEquals(CommandType.SEARCH, instance.getCommandAndPara().getCommandType());
	assertEquals("second task from 2nd instance", instance.getCommandAndPara().getParameters().getDescription());
	
    }
    
}
