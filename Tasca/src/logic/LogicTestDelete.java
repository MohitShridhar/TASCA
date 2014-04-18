package logic;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import controller.CurrentSystemState;
import storage.AllTasks;

//@author A0094655U
public class LogicTestDelete {

	@Test
	//delete last task
	public void testDeleteTask() {
		Logic.initStorage(new AllTasks());
		Logic.initSystemMessage(new CurrentSystemState());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add2 function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add3 function", "COM1", current);
		
		boolean result = Logic.deleteTask(2);

		System.out.printf("%d", Logic.getStorage().getSize());
		
		assertTrue(result);
	}

	@Test
	//delete first task, index = 0
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
	//deleting invalid task out of range
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
	//deleting a negative task number
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
