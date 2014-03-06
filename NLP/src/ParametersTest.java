import static org.junit.Assert.*;

import org.junit.Test;


public class ParametersTest {
    
    Parameters parameters = new Parameters();
    
    public String setAndGetDate(String input) {
	parameters.setDateTime(input);
	return parameters.getDateTime().format("dd/MM/yyyy").toString();
    }
    
    @Test
    public void testDateTime() {
	
	/* Date tests: Note Ð You need to change the following dates based on the current date */
	assertEquals("Date interpretation: Next week's date", "12/03/2014", setAndGetDate("next week"));
	assertEquals("Date interpretation: Next month's date", "05/04/2014", setAndGetDate("next month"));
	assertEquals("Date interpretation: Day after tomorrow", "07/03/2014", setAndGetDate("tomorrow +1 day"));
	assertEquals("Date interpretation: 50 days after tomorrow", "25/04/2014", setAndGetDate("tomorrow +50 day"));
	
	/* Failed to parse: dates Ð Add more 
	assertEquals("Date & Time interpretation: Short 'tomorrow'", "05/03/2014", setAndGetDate("tomr"));
	*/
	
	/* Time tests */
	
	
    }

}
