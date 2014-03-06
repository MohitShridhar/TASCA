import static org.junit.Assert.*;

import org.junit.Test;


public class InterpreterTest {
    
    @Test
    public void testKeywordsHash() {
	
//	System.out.println(Interpreter.readCommandDatabase().toString());
//	System.out.println(Interpreter.readParameterDatabase().toString());
//	
//	assertEquals("add test", CommandType.ADD, Interpreter.commandKeywords.get("create"));
//	assertEquals("delete test", CommandType.DELETE, Interpreter.commandKeywords.get("delete"));
//	
//	assertEquals("on test", ParameterType.START_TIME, Interpreter.parameterKeywords.get("on"));
//	assertEquals("location test", ParameterType.LOCATION, Interpreter.parameterKeywords.get("loc"));
//	assertEquals("reminder test", ParameterType.REMINDER_TIME, Interpreter.parameterKeywords.get("alert"));
	
	
	Interpreter newInt = new Interpreter();
	
	newInt.processUserInput("add first ever task -on next tuesday -remind monday 8:35pm -loc LT6");
	System.out.println(newInt.getCommandAndPara().getCommandType());
	
    }
    
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
