import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Scanner;
import com.clutch.dates.StringToTime;
import java.util.Calendar;


public class InterpreterTest {
    
    @Test
    public void testMainInterpreter() 
    {
	
	String input = "add FIRST ever task -on next tuesday -remind 12 may 2014 8:30pm -loc LT6 -pri low -id 12 -end sunday 13:12 -folder home";
	
	System.out.println("Simulate user command: ");
	Scanner s = new Scanner(System.in);
	input = s.nextLine();
	
	System.out.println();
	
	/* ##########
	 * API for INTERPRETER
	 * ##########
	 */
	
	// Create an instance in CONTROLLER class:
	
	/* PRECONDITIONS FOR INTERPRETER: (Required inside the Controller Class)
	 * 	import com.clutch.dates.StringToTime; // Note: You need to add "string-to-time.jar" file to the External JAR libraries in your JAVA Build Path - Project Properties
	 *	import java.util.Calendar;
	 * 	
	 * Format: ("-" is used as the delimiter)
	 * 	<Main Command> description -<parameter1> parameter1 arguments -<parameter2> parameter2 arguments.... 
	 */
	
	Interpreter newInt = new Interpreter();
	// USAGE ––– INPUT
	try {
	    newInt.processUserInput(input);
	} catch (IllegalArgumentException eI) { // Check for exceptions
	    System.out.println("Exception - " + eI);
	} 
	
	
	// USAGE --- OUTPUT
	Command commandAndPara = newInt.getCommandAndPara();
	
	System.out.println("Command Type: " + commandAndPara.getCommandType());
	System.out.println();
	
	System.out.println("Parameters: ---------");
	System.out.println("Description: " + commandAndPara.getParameters().getDescription());
	System.out.println("Location: " + commandAndPara.getParameters().getLocation());
	System.out.println("Folder: " + commandAndPara.getParameters().getFolder());
	System.out.println("Priority: " + commandAndPara.getParameters().getPriority());
	System.out.println("Task ID: " + commandAndPara.getParameters().getTaskId());
	System.out.println("Start Time: " + commandAndPara.getParameters().getStartTime());
	System.out.println("End Time: " + commandAndPara.getParameters().getEndTime());
	System.out.println("Reminder Time: " + commandAndPara.getParameters().getRemindTime());
	
	
	// USAGE --- DATE & TIME
	System.out.println();	
	System.out.println("Start_Time Expansion: -------- (StringToTime Class)");
	
	try { // Check if there are any empty parameters
	    Calendar dateTime = commandAndPara.getParameters().getStartTime().getCal();

	    System.out.println("Year: " + dateTime.get(Calendar.YEAR));
	    System.out.println("Month: " + dateTime.get(Calendar.MONTH));
	    System.out.println("Date: " + dateTime.get(Calendar.DATE));
	    System.out.println("Day: " + dateTime.get(Calendar.DAY_OF_WEEK));
	    System.out.println("Hour: " + dateTime.get(Calendar.HOUR));
	    
	} catch (NullPointerException eN) {
	    System.out.println("Some parameters are empty");
	}
	
    }
    
//    private String description, location, folder;
//    private Integer priority, taskId;
//    private StringToTime startTime, endTime, remindTime;
    
    
//    @Test
//    public void testKeywordsHash() {
//	
//	System.out.println(Interpreter.readCommandDatabase().toString());
//	System.out.println(Interpreter.readParameterDatabase().toString());
//	
//	assertEquals("add test", CommandType.ADD, Interpreter.commandKeywords.get("create"));
//	assertEquals("delete test", CommandType.DELETE, Interpreter.commandKeywords.get("delete"));
//	
//	assertEquals("on test", ParameterType.START_TIME, Interpreter.parameterKeywords.get("on"));
//	assertEquals("location test", ParameterType.LOCATION, Interpreter.parameterKeywords.get("loc"));
//	assertEquals("reminder test", ParameterType.REMINDER_TIME, Interpreter.parameterKeywords.get("alert"));
//	
//	
//    }
    
//    @Test
//    public void testReadKeywords() {
//	//Interpreter interpreter = new Interpreter();
//	assertEquals("Hello", "myuserMOHIT", Interpreter.readKeywordDatabase());
//    }
//    	
//    @Test
//    public void testParseInput() {
//	Interpreter interpreter = new Interpreter();
//	
//    	//assertEquals("Random tests", "add", interpreter.parseInput("add   '  adwd' on 'awdawd' at 'ad'"));
//    }
}
