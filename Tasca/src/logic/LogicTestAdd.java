package logic;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import storage.AllTasks;

//@author A0094655U
public class LogicTestAdd {

	//testing for add function
	
	@Test
	//checks if both start time == end time.
	public void testAddTask() {
		
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = current;
		Date end = current;
		
		boolean result = Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		assertFalse(result);
	}
	
	@Test
	//if end time is later then start time is true.
	public void test2AddTask() {
		
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		boolean result = Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		assertTrue(result);
	}
    @Test
    //the event has ended already, false.
	public void test3AddTask() {
		
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = new Date(current.getTime() - 120000L);
		Date end = new Date(current.getTime() - 60000L);
		boolean result = Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		assertFalse(result);
	}
	
    @Test
    //end time is earlier than start time.
	public void test4AddTask() {
		
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = new Date(current.getTime() + 120000L);
		Date end = new Date(current.getTime() + 60000L);
		//invalid duration start > end
		boolean result = Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		assertFalse(result);
	}

}
