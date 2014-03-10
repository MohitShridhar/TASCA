package Logic;

import Storage.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Logic {

	private static final long CONSTANT_MILLISECONDS_IN_A_DAY = 86400000;

	private static String MESSAGE_TASK_NOT_DELETED = "The task indicated cannot be deleted as it does not exist.";
	private static String MESSAGE_TASK_INDEX_INVALID = "The given task number is invalid.";

	private static String MESSAGE_TASK_DELETED = "The Task has been successfully deleted.";
	private static String MESSAGE_TASK_IS_DONE = "has been completed";

	private static AllTasks _storage;

	public static void initStorage(AllTasks alltasks) {
		AllTasks _storage = alltasks;
	}

	public static void addTask(Date start, Date end, boolean isThereReminder,
			boolean isTaskDone, boolean isAllDayEvent, String title,
			String remarks, Date reminder) {

		Calendar taskStart = Calendar.getInstance();
		taskStart.setTime(start);

		Calendar taskEnd = Calendar.getInstance();
		taskEnd.setTime(end);

		Date current = new Date();

		boolean isValidDuration = start.before(end);
		boolean isEventNotEnded = current.before(end);

		if (isValidDuration && isEventNotEnded) {
			int TaskId = 0;
			int totalNumOfTasks = _storage.getSize();
			int count = 0;
			// taskStart will have a larger time then the i task, incremental i
			// task till taskStart smaller.
			boolean IsTaskNotInCorrectPosition = taskStart.compareTo(_storage
					.getTask(count).startTime()) > 0;
			while ((count < totalNumOfTasks) && (IsTaskNotInCorrectPosition)) {
				count++;
				IsTaskNotInCorrectPosition = taskStart.compareTo(_storage
						.getTask(count).startTime()) > 0;
			}
			if (count != 0) { // correct position
				count--;
			}

			Task task = new Task(TaskId, taskStart, taskEnd, isThereReminder,
					isTaskDone, isAllDayEvent, title, remarks);

			_storage.addTask(count, task);

			Calendar reminderTime = Calendar.getInstance();
			reminderTime.setTime(reminder);

			Reminder taskReminder = new Reminder(reminderTime, task);

			count = 0;

			boolean IsReminderNotInCorrectPosition = reminderTime
					.compareTo(_storage.getReminder(count).getReminderTime()) > 0;
			int totalNumOfReminders = _storage.getReminderSize();

			while ((count < totalNumOfReminders)
					&& (IsReminderNotInCorrectPosition)) {
				count++;
				IsReminderNotInCorrectPosition = reminderTime
						.compareTo(_storage.getReminder(count)
								.getReminderTime()) > 0;
			}
			if (count != 0) { // correct position
				count--;
			}

			_storage.addReminder(count, taskReminder);
		}
	}

	public static void deleteTask(int index) {
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks) {

			if (_storage.getTask(index).getIsThereReminder()) {
				_storage.deleteReminder(_storage.getTask(index));
			}
			_storage.deleteTask(index);
			System.out.printf("%s/n", MESSAGE_TASK_DELETED);

		} else {
			System.out.printf("%s/n", MESSAGE_TASK_NOT_DELETED);
		}
	}

	public static void taskIsDone(int index) {
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks) {
			_storage.getTask(index).setIsTaskDone(true);
			System.out.printf("Task Number %d %s/n", index,
					MESSAGE_TASK_IS_DONE);
		} else {
			System.out.printf("%s/n", MESSAGE_TASK_INDEX_INVALID);
		}
		return;
	}

	public static void displayTask(int index) {

		int totalNumOfTasks = _storage.getSize();

		if (index < totalNumOfTasks) {
			Task task = _storage.getTask(index);
			int indexOfReminder = _storage.searchForCorrespondingReminder(task);

			Date startTime = task.getStartTime().getTime();
			Date endTime = task.getEndTime().getTime();

			SimpleDateFormat display = new SimpleDateFormat(
					"E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("id: " + task.getTaskID());
			System.out.println("start: "
					+ display.format(task.getStartTime().getTime()));
			System.out.println("end: "
					+ display.format(task.getEndTime().getTime()));
			System.out.println("title: " + task.getTaskTitle());
			System.out.println("remarks: " + task.getRemarks());
			System.out.println("reminder: "
					+ display.format(task.getReminder(indexOfReminder)
							.getReminderTime().getTime()));
			System.out.println((task.getIsTaskDone()) ? "YES" : "NO");
			System.out.println((task.getIsAllDayEvent()) ? "YES" : "NO");
		}

		else {
			System.out.printf("%s/n", MESSAGE_TASK_INDEX_INVALID);
		}
	}

	public static void displayAllTasks() {
		int totalNumOfTasks = _storage.getSize();
		for (int count = 0; count < totalNumOfTasks; count++) {
			displayTask(count);
		}
	}

	public static void displayTasksAtPeriod(Date startDateSpecified,
			Date endDateSpecified) {
		int totalNumOfTasks = _storage.getSize();

		Date startTime, endTime;
		for (int count = 0; count < totalNumOfTasks; count++) {
			Task task = _storage.getTask(count);

			// convert 'Calendar' to 'Date' to compare
			startTime = task.getStartTime.getTime();
			endTime = task.getEndTime.getTime();

			boolean hasTaskStarted = startTime.after(startDateSpecified);
			boolean hasTaskNotEnded = endTime.before(endDateSpecified);

			if (hasTaskStarted && hasTaskNotEnded) {
				displayTask(count);
			}
		}
	}

	public static void displayTasksAtDate(Date dateSpecified) {
		displayTasksAtPeriod(dateSpecified, dateSpecified);
	}

	public static void displayToday(AllTasks allTasks) {
		Date today = new Date();
		displayTasksAtDate(today);
	}

	public static void displayTomorrow(AllTasks allTasks) {
		long tomorrowInMillis = System.currentTimeMillis()
				+ CONSTANT_MILLISECONDS_IN_A_DAY;
		Date tomorrow = new Date(tomorrowInMillis);
		displayTasksAtDate(tomorrow);
	}

	public static void searchTask(String searchString) {
		int totalNumOfTasks = _storage.getSize();
		for (int index = 0; index < totalNumOfTasks; index++) {
			String taskTitle = _storage.getTask(index).getTaskTitle();
			if (isInString(taskTitle, searchString)) {
				displayTask(index);
			}
		}
	}

	private static boolean isInString(String mainString, String subString) {
		return (mainString.toLowerCase().contains(subString.toLowerCase()));
	}
   
  public static void deletealldone(){
			int totalNumOfTasks= _storage.getSize();
			int index=0; count = 0;
			while (index < totalNumOfTasks)
			{
				if (_storage.getTask(index).getIsTaskDone())
				{	deleteTask(index);
					count++;
				}
				index++;
			}
			System.out.printf("%d Tasks have been done and deleted", count);
		} 
}