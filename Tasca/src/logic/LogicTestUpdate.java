package logic;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import controller.CurrentSystemState;
import storage.AllTasks;

//@author A0094655U
public class LogicTestUpdate {

	@Test
	//test if update is implemented correctly start time < end time.
	public void testUpdateTask() {
		Logic.initStorage(new AllTasks());
		Logic.initSystemMessage(new CurrentSystemState());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);

		start.setTime(current.getTime() + 60000L);
		end.setTime(current.getTime() + 120000L);
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(start);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(end);
		
		boolean result;
		
		result = Logic.updateTask(0, "0", "1", startDate, endDate, false, "Test logic modify function", "ICUBE", current);
				
		assertTrue(result);
	}
	
	@Test
	//test if update is implemented correctly end time < stars time yields in correct result.
	public void test2UpdateTask() {
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);

		start.setTime(current.getTime() + 60000L);
		end.setTime(current.getTime() + 120000L);
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(start);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(end);
		
		boolean result;
		
		result = Logic.updateTask(0, "0", "1", endDate, startDate, false, "Test logic modify function", "ICUBE", current);
				
		assertFalse(result);
	}

}
