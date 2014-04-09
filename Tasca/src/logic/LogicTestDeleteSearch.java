package logic;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Scanner;

import org.junit.Test;

import storage.AllTasks;

//@author A0094655U
public class LogicTestDeleteSearch {

	@Test
	public void testDeleteSearch() {
		
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
		
		Scanner userInput = new Scanner("Y");
		
		Logic.deleteSearch("apple", userInput);
		
		assertTrue(storage.getTaskSize() == 3);
		
	}

	@Test
	public void testDeleteSearch2() {
		
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
		
		Scanner userInput = new Scanner("N");
		
		Logic.deleteSearch("apple", userInput);
		
		assertTrue(storage.getTaskSize() == 5);
		
	}
	
}
