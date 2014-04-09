package logic;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import storage.AllTasks;

//@Nigel Cheok A0094655U
public class LogicTestSearch {

	//testing for search function
	
	@Test
	public void testSearchTask() {
		
		
		AllTasks storage = new AllTasks();
		
		Logic.initStorage(storage);
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		//should search also search tasks based on location?
		Logic.addTask(0, 0, start, end, false, false, false, "pineapple", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "watermelon", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "green apple", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "papaya", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "honeydew", "COM1", current);

		assertTrue(storage.getTaskSize() == 5);
		
		int numOfSearches = Logic.searchTask("apple");
		assertTrue(numOfSearches == 2);
		
	}
	@Test
	public void testSearch2Task() {
		
		
		AllTasks storage = new AllTasks();
		
		Logic.initStorage(storage);
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "pineapple", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "watermelon", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "green apple", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "papaya", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "honeydew", "COM1", current);

		assertTrue(storage.getTaskSize() == 5);
		
		int numOfSearches = Logic.searchTask("orange");
		assertTrue(numOfSearches == 0);
		
	}

	@Test
	public void testSearch3Task() {
		
		
		AllTasks storage = new AllTasks();
		
		Logic.initStorage(storage);
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "pineapple", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "watermelon", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "green apple", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "papaya", "COM1", current);
		Logic.addTask(0, 0, start, end, false, false, false, "honeydew", "COM1", current);

		assertTrue(storage.getTaskSize() == 5);
		//should not display any search if is a space.
		int numOfSearches = Logic.searchTask(" ");
		assertTrue(numOfSearches == 0);
		
	}
	
}
