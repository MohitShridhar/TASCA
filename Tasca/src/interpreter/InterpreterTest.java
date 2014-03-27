package interpreter;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.antlr.runtime.tree.RewriteEmptyStreamException;
import org.junit.Test;



public class InterpreterTest {
    
    public static Calendar DateToCalendar(Date date){ 
	  Calendar cal = Calendar.getInstance();
	  cal.setTime(date);
	  return cal;
    }
    
    @Test
    public void testMainInterpreter() 
    {
	
	String input = "add FIRST ever task -on next tuesday -remind 12 may 2014 8:30pm -loc LT6 -pri low -id 12 -end sunday 13:12 -folder home";
	
//	System.out.println("Simulate user command: ");
//	Scanner s = new Scanner(System.in);
//	input = s.nextLine();
//	
//	System.out.println();
	
	
	/* New time nlp test  */
	
//	Parser parser = new Parser();
//	List<DateGroup> groups = parser.parse("kanjwd akjndw");
//	
//	if (groups.isEmpty()) {
//	    System.out.println("Empty");
//	}
//
//	for(DateGroup group:groups) {
//	  List<Date> dates = group.getDates();
//	  Date recursUntil = group.getRecursUntil();
//	  
//	    Calendar pTimeNLP = DateToCalendar(group.getDates().get(0));
//	    
//	    
//	    System.out.println(group.getDates().get(0).toString());
//
//	    System.out.println("New 'natty' time nlp: ");
//	    System.out.println("Year: " + pTimeNLP.get(Calendar.YEAR));
//	    System.out.println("Month: " + pTimeNLP.get(Calendar.MONTH));
//	    System.out.println("Date: " + pTimeNLP.get(Calendar.DATE));
//	    System.out.println("Day: " + pTimeNLP.get(Calendar.DAY_OF_WEEK));
//	    System.out.println("Hour: " + pTimeNLP.get(Calendar.HOUR));
//	    
//	    System.out.println();
//	}
//	
	
	/* End time nlp test */
	
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
	

	// USAGE INPUT
//	Interpreter newInt = new Interpreter();
	Interpreter secondInt = new Interpreter();
	
	try {
	    secondInt.processUserInput(input);
	} catch (RewriteEmptyStreamException eI) { // Check for exceptions
	    System.out.println("Exception - " + eI);
	} catch (IllegalArgumentException eIa) {
	    System.out.println("Exception - " + eIa);
	}
	
	
	assertEquals("add interpretation", CommandType.ADD, secondInt.interpretCommand("add")); 
	assertEquals("delete interpretation", CommandType.DELETE, secondInt.interpretCommand("d"));
	assertEquals("modify interpretation", CommandType.INVALID, secondInt.interpretCommand("moDify")); // Must be lower case before interpretation
	
	assertEquals("start time interpretation", ParameterType.START_TIME, secondInt.interpretParameter("on"));

	

	// USAGE --- OUTPUT
	Command commandAndPara = secondInt.getCommandAndPara();
	
	System.out.println("Command Type: " + commandAndPara.getCommandType());
	assertEquals("Command Type: ", CommandType.ADD,commandAndPara.getCommandType());
	
	System.out.println();
	
	System.out.println("Parameters: ---------");
		
	System.out.println("Description: " + commandAndPara.getParameters().getDescription());
	assertEquals("Description: ", "FIRST ever task" ,commandAndPara.getParameters().getDescription());
	
	System.out.println("Location: " + commandAndPara.getParameters().getLocation());
	assertEquals("Location: ", "LT6", commandAndPara.getParameters().getLocation());
	
	System.out.println("Folder: " + commandAndPara.getParameters().getFolder());
	assertEquals("Folder: ", "home", commandAndPara.getParameters().getFolder());
	
	System.out.println("Priority: " + commandAndPara.getParameters().getPriority());
	assertEquals("Priority: ","3", commandAndPara.getParameters().getPriority()); // This is a boundary case. Priority numbers (1: high, 2: med, 3: low, 0: undefined)
	
	System.out.println("Task ID: " + commandAndPara.getParameters().getTaskId());
	assertEquals("Task ID: ", "12", commandAndPara.getParameters().getTaskId()); // All tasks are whole numbers.
	
	System.out.println("Start Time: " + commandAndPara.getParameters().getStartTime().getTime());
	
	System.out.println("End Time: " + commandAndPara.getParameters().getEndTime().getTime());
	
	System.out.println("Reminder Time: " + commandAndPara.getParameters().getRemindTime().getTime());
	
	
	// New function:
	try {
		System.out.println("Recurring End Time:" + commandAndPara.getParameters().getRecurEndTime().getTime());
	}catch (NullPointerException eD) {
	    System.out.println("Some parameters are empty");
	}
	
	// USAGE --- DATE & TIME
	System.out.println();	
	System.out.println("Start_Time Expansion: -------- (StringToTime Class)");
	
	try {
        	Calendar dateTime = commandAndPara.getParameters().getStartTime();
        	
        	
        	System.out.println("Year: " + dateTime.get(Calendar.YEAR));
        	System.out.println("Month: " + dateTime.get(Calendar.MONTH));
        	System.out.println("Date: " + dateTime.get(Calendar.DATE));
        	System.out.println("Day: " + dateTime.get(Calendar.DAY_OF_WEEK));
        	System.out.println("Hour: " + dateTime.get(Calendar.HOUR));
	} catch (NullPointerException eD) {
	    System.out.println("Some parameters are empty");
	}
//    }
    

    
    
    
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
    }
}
