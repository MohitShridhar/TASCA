package Storage;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import java.io.FileNotFoundException;

public class AllTasksTest {

	@Test
	public void test() {
		AllTasks allTasks = new AllTasks();
		int x=0;
		
		Task task1 = new Task(0,1,Calendar.getInstance(), Calendar.getInstance(), false ,false, false,"go home", "buy eggs");
		Task task2 = new Task(0,1,Calendar.getInstance(), Calendar.getInstance(), false ,false, false,"bye yb", "nothing");
		Task task3 = new Task(0,1,Calendar.getInstance(), Calendar.getInstance(), true ,false, false,"valentines", "buy flowers");
		Reminder reminder = new Reminder(Calendar.getInstance(), task3);
		
		allTasks.addTask(0, task1);
		allTasks.addTask(0, task2);
		allTasks.addTask(1, task3);
		allTasks.addReminder(0, reminder);
		if (allTasks.getReminder(allTasks.searchForCorrespondingReminder(task3)) == reminder) {
			x=1;
		}
		allTasks.deleteReminder(task3);
		allTasks.deleteTask(1);;
		try{
		allTasks.saveData();
		} catch (FileNotFoundException e){
			x=1;
		}
		
		x= allTasks.getSize() + allTasks.getReminderSize() + x;
		assertEquals(3,x);
		
	}

}
