package Logic;

import Storage.*;
import java.util.Date;
import java.util.Calendar;

//since we use allTasks in all the commands  is it better to use a global variable?

public class Logic {
	
	private static final long CONSTANT_MILLISECONDS_IN_A_DAY = 86400000;
	
	private static String MESSAGE_TASK_NOT_DELETED = "The task indicated cannot be deleted as it does not exist.";
	private static String MESSAGE_TASK_INDEX_INVALID = "The given task number is invalid.";
	
	private static String MESSAGE_TASK_DELETED = "The Task has been successfully deleted.";
	private static String MESSAGE_TASK_IS_DONE = "has been completed";
	
	public static void addTask(AllTasks allTasks, Time startTime, Time endTime, boolean isThereReminder, boolean isTaskDone, boolean isAllDayEvent, 
			String taskTitle, String remarks, Time reminderTime) {
		int counter = 0, dummyTaskID=0;
		boolean check = true;
		
		Task task = new Task(dummyTaskID, startTime, endTime, isThereReminder, isTaskDone, isAllDayEvent, taskTitle, remarks);
		
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getTask(counter).getStartTime().getYear() >= startTime.getYear()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getTask(counter).getStartTime().getMonth() >= startTime.getMonth()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getTask(counter).getStartTime().getDay() >= startTime.getDay()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getTask(counter).getStartTime().getHour() >= startTime.getHour()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getTask(counter).getStartTime().getMinute() >= startTime.getMinute()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		allTasks.addTask(counter, task);
		addReminder(allTasks, reminderTime, task);
		return;

	}
	
	private static void addReminder (AllTasks allTasks, Time reminderTime, Task task) {
		int counter = 0;
		boolean check = true;
		
		Reminder reminder = new Reminder (reminderTime, task);
		
		while (counter < allTasks.getReminderSize() && check) {
			if (allTasks.getReminder(counter).getReminderTime().getYear() >= reminderTime.getYear()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getReminder(counter).getReminderTime().getMonth() >= reminderTime.getMonth()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getReminder(counter).getReminderTime().getDay() >= reminderTime.getDay()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getReminder(counter).getReminderTime().getHour() >= reminderTime.getHour()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.getSize() && check) {
			if (allTasks.getReminder(counter).getReminderTime().getMinute() >= reminderTime.getMinute()) {
				counter= counter - 1;
				check = false;
			}
			counter = counter + 1;
		}
		
		allTasks.addReminder(counter, reminder);
		return;

	}
	
	public static void deleteTask (AllTasks allTasks, int index) {
		if (index >= allTasks.getSize()){
			System.out.printf("%s/n", MESSAGE_TASK_NOT_DELETED);
		} else{
			if (allTasks.getTask(index).getIsThereReminder()) {
				allTasks.deleteReminder(allTasks.getTask(index));
			}
			allTasks.deleteTask(index);
			System.out.printf("%s/n", MESSAGE_TASK_DELETED);
			
		}
		return;
	}
	
	public static void taskIsDone (AllTasks allTasks, int index)  {
		if(allTasks.getSize() <= index) {
			System.out.printf("%s/n", MESSAGE_TASK_INDEX_INVALID);
		} else{
			allTasks.getTask(index).setIsTaskDone(true);
			System.out.printf("Task Number %d %s/n",index , MESSAGE_TASK_IS_DONE);
		}
		return;
	}
	
	public static void displayTask(AllTasks allTasks, int index) {
		
		int totalNumOfTasks = allTasks.getSize();
		
		if (index < totalNumOfTasks) {
		Task task = allTasks.getTask(index);
		int indexOfReminder = allTasks.searchForCorrespondingReminder(task);
		
			System.out.printf("Task Number: %d Start Time: %d:%d %d/%d/%d End Time: %d:%d %d/%d/%d\n"
					+ "Name: %s\nTask is done: %b\nTask is all day event: %b\n"
					+ "Remarks: %s\nReminder Time: %d:%d %d/%d/%d\n", allTasks.getTask(index).getTaskID(), allTasks.getTask(index).getStartTime().getHour(),
					allTasks.getTask(index).getStartTime().getMinute(), allTasks.getTask(index).getStartTime().getDay(), allTasks.getTask(index).getStartTime().getMonth(),
					allTasks.getTask(index).getStartTime().getYear(),allTasks.getTask(index).getEndTime().getHour(), allTasks.getTask(index).getEndTime().getMinute(),
					allTasks.getTask(index).getEndTime().getDay(), allTasks.getTask(index).getEndTime().getMonth(), allTasks.getTask(index).getEndTime().getYear(),
					allTasks.getTask(index).getTaskTitle(),allTasks.getTask(index).getIsTaskDone(), allTasks.getTask(index).getIsAllDayEvent(),
					allTasks.getTask(index).getRemarks(), allTasks.getReminder(indexOfReminder).getReminderTime().getHour(), allTasks.getReminder(indexOfReminder).getReminderTime().getMinute(),
					allTasks.getReminder(indexOfReminder).getReminderTime().getDay(), allTasks.getReminder(indexOfReminder).getReminderTime().getMonth(),
					allTasks.getReminder(indexOfReminder).getReminderTime().getYear());
			
		}
		
	else {
		System.out.printf("%s/n", MESSAGE_TASK_INDEX_INVALID);
	}
	}
	
	
	public static void displayAllTasks(AllTasks allTasks) {
		int totalNumOfTasks = allTasks.getSize();
		for (int count = 0; count < totalNumOfTasks; count++) {
			displayTask(allTasks, count);
		}
	}
	
	public static void displayTasksAtPeriod(AllTasks allTasks, Date startDateSpecified, Date endDateSpecified) {
		int totalNumOfTasks = allTasks.getSize();
		
		Date startTime, endTime;
		for (int count = 0; count < totalNumOfTasks; count++) {
			Task task = allTasks.getTask(count);
			
			//convert 'Calendar' to 'Date' to compare
			startTime = task.getStartTime.getTime();
			endTime = task.getEndTime.getTime();
			
			boolean hasTaskStarted = startTime.after(startDateSpecified);
			boolean hasTaskNotEnded = endTime.before(endDateSpecified);
			
			if (hasTaskStarted && hasTaskNotEnded) {
			displayTask(allTasks, count);
			}
		}	
	}
	
	public static void displayTasksAtDate(AllTasks allTasks, Date dateSpecified) {
	displayTasksAtPeriod(allTasks, dateSpecified, dateSpecified);
	}
	
	public static void displayToday (AllTasks allTasks) {
	Date today = new Date();
	displayTasksAtDate(allTasks, today);
	}

	public static void displayTomorrow (AllTasks allTasks) {
    long tomorrowInMillis = System.currentTimeMillis() + CONSTANT_MILLISECONDS_IN_A_DAY;
	Date tomorrow = new Date(tomorrowInMillis);
	displayTasksAtDate(allTasks, tomorrow);
	}	
	
}