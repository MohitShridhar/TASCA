package logic;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Scanner;

import org.junit.Test;

import controller.CurrentSystemState;
import storage.AllTasks;

//@author A0094655U
public class LogicTestDeleteSearch {

	@Test
	//deleting all task related that is a substring.
	public void testDeleteSearch() {
		
		AllTasks storage = new AllTasks();
		
		Logic.initStorage(storage);
		Logic.initSystemMessage(new CurrentSystemState());
		
		Date current = new Date();
		Date start = current;
		Date end = new Date(current.getTime() + 60000L);
		
		Logic.addTask(0, 0, start, end, false, false, false, "guava", "fruit stall", current);
		Logic.addTask(0, 0, start, end, false, false, false, "watermelon", "fruit stall", current);
		Logic.addTask(0, 0, start, end, false, false, false, "green apple", "fruit stall", current);
		Logic.addTask(0, 0, start, end, false, false, false, "papaya", "fruit stall", current);
		Logic.addTask(0, 0, start, end, false, false, false, "honeydew", "fruit stall", current);

		assertTrue(storage.getTaskSize() == 5);
		
		Scanner userInput = new Scanner("Y");
		
		Logic.deleteSearch("melon", userInput);
		
		assertTrue(storage.getTaskSize() == 4);
		
	}
	
	@Test
	//test if negative usercommmand is issued, task is not deleted.
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
