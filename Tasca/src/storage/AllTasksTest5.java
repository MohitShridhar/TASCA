package storage;

import static org.junit.Assert.*;


import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class AllTasksTest5 {
	private AllTasks test;

	@Test
	public void test() {
		test = new AllTasks();
		Calendar taskDate = setCalFromMilli(1397696400000L);
		Task task1 = new Task(0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,1,taskDate, taskDate, false ,false, false,"Sleeping", "Home");
		Task task3 = new Task(0,1,taskDate, taskDate, false ,false, false,"Running", "Park");
		Task task4 = new Task(0,1,taskDate, taskDate, false ,false, false,"Fly to Japan", "Airport");
		test.addTask(0, task1);
		test.addTask(1, task2);
		test.addTask(0, task3);
		test.addTask(2, task4);
	
		boolean check;
		check = test.isValidTaskId(3);
		assertTrue(check);
	}
	
    @Test
	public void test2() {
		test = new AllTasks();
		Calendar taskDate = setCalFromMilli(1397696400000L);
		Task task1 = new Task(0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,1,taskDate, taskDate, false ,false, false,"Sleeping", "Home");
		Task task3 = new Task(0,1,taskDate, taskDate, false ,false, false,"Running", "Park");
		Task task4 = new Task(0,1,taskDate, taskDate, false ,false, false,"Fly to Japan", "Airport");
		test.addTask(0, task1);
		test.addTask(1, task2);
		test.addTask(0, task3);
		test.addTask(2, task4);
	
		boolean check;
		check = test.isValidTaskId(4);
		assertFalse(check);
	}
    
	//defend against negative values?
    @Test
	public void test3() {
		test = new AllTasks();
		Calendar taskDate = setCalFromMilli(1397696400000L);
		Task task1 = new Task(0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,1,taskDate, taskDate, false ,false, false,"Sleeping", "Home");
		Task task3 = new Task(0,1,taskDate, taskDate, false ,false, false,"Running", "Park");
		Task task4 = new Task(0,1,taskDate, taskDate, false ,false, false,"Fly to Japan", "Airport");
		test.addTask(0, task1);
		test.addTask(1, task2);
		test.addTask(0, task3);
		test.addTask(2, task4);
	
		boolean check;
		check = test.isValidTaskId(-1);
		assertFalse(check);
	}
    
	// methods to help on testing

	public Calendar setCalFromMilli(long millisec) {
		Calendar taskDate = Calendar.getInstance();
		Date taskTime = new Date(millisec);
		taskDate.setTime(taskTime);
		return taskDate;
	}

}
