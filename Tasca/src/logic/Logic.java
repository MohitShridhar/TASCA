package logic;


import io.Exporter;
import io.Importer;

import java.text.SimpleDateFormat;
import java.util.*;

import storage.*;

public class Logic {

	private static String MESSAGE_TASK_NOT_DELETED = "The task indicated cannot be deleted as it does not exist.";
	private static String MESSAGE_TASK_INDEX_INVALID = "The given task number is invalid.";
	private static String MESSAGE_TASK_DELETED = "The Task has been successfully deleted.";
	private static String MESSAGE_TASK_IS_DONE = "has been completed";
	private static String MESSAGE_DELETE_SEARCH = "Do you want to delete all search results? (Y/N) ";

	private static AllTasks _storage;


	// private static Scanner sc = new Scanner(System.in);

	public static void initStorage(AllTasks alltasks) {
		_storage = alltasks;
		return;
	}

	public static void addTask(int priority, Date start, Date end,
			boolean isThereReminder, boolean isTaskDone, boolean isAllDayEvent,
			String title, String remarks, Date reminder) {

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
			boolean IsTaskNotInCorrectPosition = true;
			while ((count < totalNumOfTasks) && (IsTaskNotInCorrectPosition)) {

				IsTaskNotInCorrectPosition = taskStart.compareTo(_storage
						.getTask(count).getStartTime()) > 0;
				count++;
			}
			if (count != 0) { // correct position
				count--;
			}
			if(count == totalNumOfTasks -1){
				count++;
			}


			Task task = new Task(TaskId, priority, taskStart, taskEnd,
					isThereReminder, isTaskDone, isAllDayEvent, title, remarks);

			_storage.addTask(count, task);
			
			Reminder taskReminder = null;

			if (task.getIsThereReminder()) {

				Calendar reminderTime = Calendar.getInstance();
				reminderTime.setTime(reminder);

				taskReminder = new Reminder(reminderTime, task);

				count = 0;

				boolean IsReminderNotInCorrectPosition = true;
				int totalNumOfReminders = _storage.getReminderSize();

				while ((count < totalNumOfReminders)
						&& (IsReminderNotInCorrectPosition)) {
					IsReminderNotInCorrectPosition = reminderTime
							.compareTo(_storage.getReminder(count)
									.getReminderTime()) > 0;
					count++;
				}
				if (count != 0) { // correct position
					count--;
				}

				_storage.addReminder(count, taskReminder);
			}
			System.out.printf("Task Added\n");
			
		}
	}
	
	
	
	
	
	public static void deleteTaskUndoRedo (int index) {
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks) {

			if (_storage.getTask(index).getIsThereReminder()) {
				_storage.deleteReminder(_storage.getTask(index));
			}
			_storage.deleteTask(index);
			System.out.printf("%s\n", MESSAGE_TASK_DELETED);

		} else {
			System.out.printf("%s\n", MESSAGE_TASK_NOT_DELETED);
		}
	}

	public static void deleteTask(int index) {
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks) {

			if (_storage.getTask(index).getIsThereReminder()) {
				_storage.deleteReminder(_storage.getTask(index));
			}
			_storage.deleteTask(index);
			System.out.printf("%s\n", MESSAGE_TASK_DELETED);

		} else {
			System.out.printf("%s\n", MESSAGE_TASK_NOT_DELETED);
		}
	}
	
	
	public static void taskIsDone(int index) {
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks) {
			_storage.getTask(index).setIsTaskDone(true);
			System.out.printf("Task Number %d %s\n", index,
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

			SimpleDateFormat display = new SimpleDateFormat(
					"E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("id: " + task.getTaskID());
			System.out.println("priority: " + task.getPriority());
			System.out.println("start: "
					+ display.format(task.getStartTime().getTime()));
			System.out.println("end: "
					+ display.format(task.getEndTime().getTime()));
			System.out.println("title: " + task.getTaskTitle());
			try {
				System.out.println("location: " + task.getLocation());
			} catch (Exception e1) {
				System.out.println("location: none");
			}
			try {
				System.out.println("reminder: "
						+ display.format(_storage.getReminder(indexOfReminder)
								.getReminderTime().getTime()));
			} catch (Exception e2) {
				System.out.println("reminder: nil");
			}
			System.out.printf("Is Task Done: ");
			System.out.println((task.getIsTaskDone()) ? "YES" : "NO");
			System.out.printf("Is All Day Event: ");
			System.out.println((task.getIsAllDayEvent()) ? "YES" : "NO");
			System.out
					.println("************************************************************************");
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
			startTime = task.getStartTime().getTime();
			endTime = task.getEndTime().getTime();

			boolean hasTaskStarted = startTime.before(endDateSpecified)&&  endTime.after(startDateSpecified);
			boolean hasTaskNotEnded = startTime.before(startDateSpecified) && endTime.after(startDateSpecified);

			if (hasTaskStarted || hasTaskNotEnded) {
				displayTask(count);
			}
		}
	}
	
	public static void export(String savePath) {
	    new Exporter(savePath); // TODO: implement singleton
	}
	
	public static void importFile(String filePath) {
	    new Importer().importIcs(filePath); //TODO: implement singleton + be consistent with exporter
	}
	
	public static void displayTasksAtDate(Date dateSpecified) {
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(dateSpecified);
		endDate.set(Calendar.HOUR_OF_DAY, 23);
		endDate.set(Calendar.MINUTE, 59);
		displayTasksAtPeriod(dateSpecified, endDate.getTime());
	}

	public static void displayToday() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		displayTasksAtDate(today.getTime());
	}

	public static void displayTomorrow() {
		// long tomorrowInMillis = System.currentTimeMillis()
		// + CONSTANT_MILLISECONDS_IN_A_DAY;

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.set(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH),
				tomorrow.get(Calendar.DATE) + 1, 0, 0);
		// Date tomorrow = new Date(tomorrowInMillis);
		displayTasksAtDate(tomorrow.getTime());
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

	public static void deleteAlldone() {
		int totalNumOfTasks = _storage.getSize();
		int index = 0;
		int count = 0;
		while (index < totalNumOfTasks) {
			if (_storage.getTask(index).getIsTaskDone()) {
				deleteTask(index);
				totalNumOfTasks--;
				count++;
			}
			index++;
		}
		System.out.printf("%d Tasks have been done and deleted", count);
	}

	public static void deleteSearch(String searchString, Scanner sc) {
		searchTask(searchString);
		System.out.printf("%s", MESSAGE_DELETE_SEARCH);

		String s = sc.nextLine();
		System.out.printf("%s", s);
		if (s.equals("Y")) {
			int totalNumOfTasks = _storage.getSize();
			for (int index = 0; index < totalNumOfTasks; index++) {
				String taskTitle = _storage.getTask(index).getTaskTitle();
				if (isInString(taskTitle, searchString)) {
					deleteTask(index);
					totalNumOfTasks--;
				}
			}
			System.out.printf("All search results deleted.");
		}
	}

	public static void updateTask(String indexString, String priority, Calendar start,
			Calendar end, boolean isThereReminder, String title, String location,
			Date reminder) {
		int priorityInt, index = Integer.parseInt(indexString);
		boolean isTaskDone = _storage.getTask(index).getIsTaskDone(), isAllDayEvent = _storage
				.getTask(index).getIsAllDayEvent();
		if (priority == "null"){
			priorityInt = _storage.getTask(index).getPriority();
		}else {
			priorityInt = _storage.getTask(index).getPriority();
		}
		if (start == null){
			start = Calendar.getInstance();
			start.setTime( _storage.getTask(index).getStartTime().getTime());
		}
		if(end == null) {
			end = Calendar.getInstance();
			end.setTime(_storage.getTask(index).getEndTime().getTime());
		}
		if (title == null){
			title =  _storage.getTask(index).getTaskTitle();
		}
		if (location == null) {
			location =  _storage.getTask(index).getLocation();
		}
		deleteTask(index);
		addTask(priorityInt, start.getTime(), end.getTime(), isThereReminder, isTaskDone,
				isAllDayEvent, title, location, reminder);
		return;

	}


	public static void displayAll() {
		int count = 0;
		while (count < _storage.getSize()) {
			displayTask(count);
			count = count + 1;
		}
		return;
	}
	
	public static void clearAll() {
		_storage = new AllTasks();
		return;
				
	}
}
