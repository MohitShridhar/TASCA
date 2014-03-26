package logic;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Calendar;

import org.junit.Test;

import storage.AllTasks;
import storage.Task;

public class LogicTest {
	AllTasks test ;

	//boundry case when index given is negative
	@Test
	public void test() {
		test = new AllTasks();
		Calendar taskDate = Calendar.getInstance();
		Task task1 = new Task(0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,1,taskDate, taskDate, false ,false, false,"Holiday to Australia", "Changi Airport");
		test.addTask(0, task1);
		test.addTask(0, task2);
		
		Logic.initStorage(test);
		assertFalse(Logic.deleteTask(-1));

	}
	//boundary case when index is more than all tasks in list
	@Test
	public void test2() {
		test = new AllTasks();
		Calendar taskDate = Calendar.getInstance();
		Task task1 = new Task(0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,1,taskDate, taskDate, false ,false, false,"Holiday to Australia", "Changi Airport");
		test.addTask(0, task1);
		test.addTask(0, task2);
		
		Logic.initStorage(test);
		assertFalse(Logic.deleteTask(10));

	}

}
