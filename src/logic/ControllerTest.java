package logic;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import storage.AllTasks;
import storage.Task;

public class ControllerTest {
	AllTasks test ;

	@Test
	public void test() {
		test = new AllTasks();
		Calendar taskDate = Calendar.getInstance();
		Task task1 = new Task(0,0,1,taskDate, taskDate, false ,false, false,"Last day of School", "National University of Singapore");
		Task task2 = new Task(0,0,1,taskDate, taskDate, false ,false, false,"Holiday to Australia", "Changi Airport");
		test.addTask(0, task1);
		test.addTask(0, task2);
		
	}

}
