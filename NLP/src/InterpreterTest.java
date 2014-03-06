import static org.junit.Assert.*;

import org.junit.Test;


public class InterpreterTest {
    
    @Test
    public void testKeywordsHash() {
	
	System.out.println(Interpreter.readKeywordDatabase().toString());
	
	assertEquals("add test", CommandType.ADD, Interpreter.keywords.get("create"));
	assertEquals("delete test", CommandType.DELETE, Interpreter.keywords.get("delete"));
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
