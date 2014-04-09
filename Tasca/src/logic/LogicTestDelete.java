package logic;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import storage.AllTasks;

//@author A0094655U
public class LogicTestDelete {

	@Test
	public void testDeleteTask() {
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add2 function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add3 function", "COM1", current);
		
		boolean result = Logic.deleteTask(2);
		assertTrue(result);
	}

	@Test
	public void testDelete2Task() {
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add2 function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add3 function", "COM1", current);
		
		boolean result = Logic.deleteTask(0);
		assertTrue(result);
	}
	
	@Test
	public void testDelete3Task() {
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add2 function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add3 function", "COM1", current);
		
		boolean result = Logic.deleteTask(5);
		assertFalse(result);
	}
	
	@Test
	public void testDelete4Task() {
		Logic.initStorage(new AllTasks());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add2 function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add3 function", "COM1", current);
		
		boolean result = Logic.deleteTask(-1);
		assertFalse(result);
	}
	
}
