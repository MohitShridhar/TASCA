package logic;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import controller.CurrentSystemState;
import storage.AllTasks;

//@author A0094655U
public class LogicTestClear {

	@Test
	//checks if folder is cleared when added new tasks to a certain folder.
	public void testClearTask() {
		AllTasks storage = new AllTasks();
		Logic.initStorage(storage);
		Logic.initSystemMessage(new CurrentSystemState());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add2 function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add3 function", "COM1", current);
		assertTrue(Logic.getStorage().getSize() == 3);

		Logic.clearFolder(0); 
		assertTrue(Logic.getStorage().getSize() == 0);		
	}
	
	@Test
	//checks if folder is cleared base on other tasks added.
	public void test2ClearTask() {
		AllTasks storage = new AllTasks();
		Logic.initStorage(storage);
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add function", "COM1", current);
		Logic.addTask(1, 0, start, end, false, false, false, "Test logic add2 function", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "Test logic add3 function", "COM1", current);

		assertTrue(Logic.getStorage().getSize() == 3);
		Logic.clearFolder(0);
		assertTrue(Logic.getStorage().getSize() == 1);		
	}

}
