package logic;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Calendar;

import org.junit.Test;

import storage.AllTasks;
import storage.Task;
import controller.SystemMessage;

public class LogicTest {
	AllTasks test ;
	SystemMessage systemMessage;

	//boundry case when index given is negative
	@Test
	public void testDeleteTask() {
		test = new AllTasks();
	    systemMessage = new SystemMessage();
		Calendar taskDate = Calendar.getInstance();
		Task task1 = new Task(0,0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,0,1,taskDate, taskDate, false ,false, false,"Holiday to Australia", "Changi Airport");
		test.addTask(0, task1);
		test.addTask(0, task2);
		
		Logic.initStorage(test);
		Logic.initSystemMessage(systemMessage);
		assertFalse(Logic.deleteTask(-1));

	}
	//boundary case when index is more than all tasks in list
	@Test
	public void testDeleteTask2() {
		test = new AllTasks();
		systemMessage = new SystemMessage();
		Calendar taskDate = Calendar.getInstance();
		Task task1 = new Task(0,0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,0,1,taskDate, taskDate, false ,false, false,"Holiday to Australia", "Changi Airport");
		test.addTask(0, task1);
		test.addTask(0, task2);
		
		Logic.initStorage(test);
		Logic.initSystemMessage(systemMessage);
		assertFalse(Logic.deleteTask(10));

	}

}
