package logic;

import io.Exporter;
import io.Importer;

import java.text.SimpleDateFormat;
import java.util.*;

import controller.CurrentSystemState;
import storage.*;

public class Logic {

	private static String MESSAGE_TASK_NOT_DELETED = "The task indicated cannot be deleted as it does not exist.";
	private static String MESSAGE_TASK_INDEX_INVALID = "The given task number is invalid.";
	private static String MESSAGE_TASK_DELETED = "The task has been successfully deleted.";
	private static String MESSAGE_TASK_IS_DONE = "has been completed";
	private static String MESSAGE_TASK_FOUND = "%d matches found for string '%s'";
	private static String MESSAGE_DELETE_SEARCH = "Do you want to delete all search results? (Y/N) ";

	private static AllTasks _storage;
	private static CurrentSystemState systemMessage;

	public static void export(String savePath) {
		new Exporter(savePath);
	}

	public static void importFile(String filePath) {
		new Importer(filePath);
	}

	// @author A0094655U
	public static void initStorage(AllTasks alltasks) {
		_storage = alltasks;

		return;
	}

	public static void initSystemMessage(CurrentSystemState sysMsg) {
		systemMessage = sysMsg;
	}

	// @author A0094655U
	public static AllTasks getStorage() {
		return _storage;
	}

	// @author A0094655U
	public static boolean addTask(int folder, int priority, Date start,
			Date end, boolean isThereReminder, boolean isTaskDone,
			boolean isAllDayEvent, String title, String location, Date reminder) {

		boolean isTaskAdded = false;

		Calendar taskStart = Calendar.getInstance();
		taskStart.setTime(start);

		Calendar taskEnd = Calendar.getInstance();
		taskEnd.setTime(end);

		Date current = new Date();

		boolean isValidDuration = start.before(end);
		boolean isEventNotEnded = current.before(end);

		if (isValidDuration && isEventNotEnded) {
			int TaskId = 0;
			int totalNumOfTasks = _storage.getTaskSize();
			int count = 0;
			// taskStart may have a larger time then the i task,
			// thus, increment the i task till taskStart smaller.
			boolean IsTaskNotInCorrectPosition = true;
			while ((count < totalNumOfTasks) && (IsTaskNotInCorrectPosition)) {

				IsTaskNotInCorrectPosition = taskStart.compareTo(_storage
						.getTask(count).getStartTime()) > 0;
				count++;
			}

			if (count != 0) {
				// correct position
				count--;
			}
			if (count == totalNumOfTasks - 1) {
				count++;
			}

			Task task = new Task(folder, TaskId, priority, taskStart, taskEnd,
					isThereReminder, isTaskDone, isAllDayEvent, title, location);

			isTaskAdded = _storage.addTask(count, task);

			TaskWithReminder taskReminder = null;

			if (task.getIsThereReminder()) {

				Calendar reminderTime = Calendar.getInstance();
				reminderTime.setTime(reminder);

				taskReminder = new TaskWithReminder(reminderTime, task);

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

		}
		return isTaskAdded;
	}

	public static boolean deleteTask(int index) {
		boolean isTaskDeleted = false;
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks && index >= 0) {
			if (index < _storage.getTaskSize()) {

				if (_storage.getTask(index).getIsThereReminder()) {
					_storage.deleteReminder(_storage.getTask(index));
				}
				isTaskDeleted = _storage.deleteTask(index);
			} else {
				isTaskDeleted = _storage.deleteFloatingTask(index);
			}
			System.out.printf("%s\n", MESSAGE_TASK_DELETED);

		} else {
			systemMessage.setSystemMessage(MESSAGE_TASK_NOT_DELETED);
		}
		return isTaskDeleted;
	}

	public static void displayTask(int index,
			LinkedList<TaskWithReminder> list, LinkedList<FloatingTask> list2) {

		int totalNumOfTasks = _storage.getSize();
		if (index < _storage.getTaskSize()) {

			if (index < totalNumOfTasks) {
				Task task = _storage.getTask(index);
				list.add(new TaskWithReminder(null, task));
				int indexOfReminder = _storage
						.searchForCorrespondingReminder(task);

				SimpleDateFormat display = new SimpleDateFormat(
						"E yyyy.MM.dd 'at' hh:mm:ss a zzz");
				System.out.println("Folder: " + task.getFolder());
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
					System.out.println("location: NIL");
				}
				try {
					System.out.println("reminder: "
							+ display.format(_storage
									.getReminder(indexOfReminder)
									.getReminderTime().getTime()));
					list.getLast().setReminderTime(
							_storage.getReminder(indexOfReminder)
									.getReminderTime());
				} catch (Exception e2) {
					System.out.println("reminder: NIL");
				}
				System.out.printf("Is Task Done: ");
				System.out.println((task.getIsTaskDone()) ? "YES" : "NO");
				System.out.printf("Is All Day Event: ");
				System.out.println((task.getIsAllDayEvent()) ? "YES" : "NO");
				System.out
						.println("************************************************************************");
			}
		} else {
			if (index >= _storage.getTaskSize()) {

				FloatingTask node = _storage.getFloatingTask(index);
				list2.add(node);

				System.out.println("Folder: " + node.getFolder());
				System.out.println("id: " + node.getTaskID());
				System.out.println("priority: " + node.getPriority());
				System.out.println("title: " + node.getTaskTitle());
				try {
					System.out.println("location: " + node.getLocation());
				} catch (Exception e1) {
					System.out.println("location: NIL");
				}
				System.out.printf("Is Task Done: ");
				System.out.println((node.getIsTaskDone()) ? "YES" : "NO");
				System.out
						.println("=========================================================================");
			} else {
				systemMessage.setSystemMessage(MESSAGE_TASK_INDEX_INVALID);
			}
		}
	}

	// @author A0094655U
	public static void displayAllTasks() {
		int totalNumOfTasks = _storage.getSize();
		LinkedList<TaskWithReminder> list = new LinkedList<TaskWithReminder>();
		LinkedList<FloatingTask> list2 = new LinkedList<FloatingTask>();
		for (int count = 0; count < totalNumOfTasks; count++) {
			displayTask(count, list, list2);
		}
		systemMessage.setTimedList(list);
		systemMessage.setFloatingList(_storage.getFloatingList());
	}

	// @author A0094655U
	public static void displayTasksAtPeriod(Date startDateSpecified,
			Date endDateSpecified) {
		int totalNumOfTasks = _storage.getTaskSize();
		LinkedList<TaskWithReminder> list = new LinkedList<TaskWithReminder>();
		LinkedList<FloatingTask> list2 = new LinkedList<FloatingTask>();

		Date startTime, endTime;
		for (int count = 0; count < totalNumOfTasks; count++) {
			Task task = _storage.getTask(count);

			// convert 'Calendar' to 'Date' to compare
			startTime = task.getStartTime().getTime();
			endTime = task.getEndTime().getTime();

			boolean hasTaskStarted = startTime.before(endDateSpecified)
					&& endTime.after(startDateSpecified);
			boolean hasTaskNotEnded = startTime.before(startDateSpecified)
					&& endTime.after(startDateSpecified);

			if (hasTaskStarted || hasTaskNotEnded) {
				displayTask(count, list, list2);
			}
		}
		systemMessage.setTimedList(list);
		systemMessage.setFloatingList(list2);
	}

	// @author A0094655U
	public static void displayTasksAtDate(Date dateSpecified) {
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(dateSpecified);
		endDate.set(Calendar.HOUR_OF_DAY, 23);
		endDate.set(Calendar.MINUTE, 59);
		displayTasksAtPeriod(dateSpecified, endDate.getTime());
	}

	// @author A0094655U
	public static int searchTask(String searchString) {
		int totalNumOfTasks = _storage.getSize(), count = 0;
		LinkedList<TaskWithReminder> list = new LinkedList<TaskWithReminder>();
		LinkedList<FloatingTask> list2 = new LinkedList<FloatingTask>();
		for (int index = 0; index < totalNumOfTasks; index++) {
			String taskTitle;
			if (index < _storage.getTaskSize()) {
				taskTitle = _storage.getTask(index).getTaskTitle();
				if (isInString(taskTitle, searchString)) {
					count++;
					displayTask(index, list, list2);
				}
			} else {
				taskTitle = _storage.getFloatingTask(index).getTaskTitle();
				if (isInString(taskTitle, searchString)) {
					count++;
					displayTask(index, list, list2);
				}
			}
		}
		systemMessage.setTimedList(list);
		systemMessage.setFloatingList(list2);
		// systemMessage.setSystemMessage(MESSAGE_TASK_FOUND.format(count,searchString));
		return count;
	}
	
	//@author A0070408M
	public static void deleteAlldone() {
		int totalNumOfTasks = _storage.getSize();
		int index = 0;
		int count = 0;
		while (index < totalNumOfTasks) {
			if (index < _storage.getTaskSize()) {
				if (_storage.getTask(index).getIsTaskDone()) {
					deleteTask(index);
					totalNumOfTasks--;
					count++;
				} else {
					index++;
				}
			} else {
				if (_storage.getFloatingTask(index).getIsTaskDone()) {
					deleteTask(index);
					totalNumOfTasks--;
					count++;
				} else {
					index++;
				}

			}

		}
		System.out.printf("%d Tasks have been done and deleted", count);
	}

	// @author A0094655U
	public static String getUserInput(Scanner input) {
		// Scanner input = new Scanner(System.in);
		String result = input.nextLine();
		return result;
	}
	
	//@author A0070408M
	public static void deleteSearch(String searchString, Scanner sc) {
		searchTask(searchString);

		String getResponse = getUserInput(sc);
		// System.out.printf("%s", s);
		if (getResponse.equals("Y")) {
			int totalNumOfTasks = _storage.getSize();
			for (int index = 0; index < totalNumOfTasks; index++) {
				String taskTitle;
				if (index < _storage.getTaskSize()) {
					taskTitle = _storage.getTask(index).getTaskTitle();
				} else {
					taskTitle = _storage.getFloatingTask(index).getTaskTitle();
				}
				if (isInString(taskTitle, searchString)) {
					deleteTask(index);
					totalNumOfTasks--;
				}
			}
			System.out.printf("All search results deleted.");
		}
	}

	/**
	 * @author A0097416X
	 */
	// this method updates a timed task accordingly
	public static boolean updateTask(int folder, String indexString,
			String priority, Calendar start, Calendar end,
			boolean isThereReminder, String title, String location,
			Date reminder) {

		boolean isTaskUpdated = false, isTaskDeleted = false, isTaskAdded = false;
		int priorityInt, index = Integer.parseInt(indexString);
		if (_storage.isValidTaskId(index)) {
			boolean isTaskDone = _storage.getTask(index).getIsTaskDone(), isAllDayEvent = _storage
					.getTask(index).getIsAllDayEvent();

			if (priority == "null") {
				priorityInt = _storage.getTask(index).getPriority();
			} else {
				priorityInt = Integer.parseInt(priority);
			}
			if (start == null) {
				start = Calendar.getInstance();
				start.setTime(_storage.getTask(index).getStartTime().getTime());
			}
			if (end == null) {
				end = Calendar.getInstance();
				end.setTime(_storage.getTask(index).getEndTime().getTime());
			}
			if (title == null) {
				title = _storage.getTask(index).getTaskTitle();
			}
			if (location == null) {
				location = _storage.getTask(index).getLocation();
			}
			if (reminder == null) {
				if (_storage.getTask(index).getIsThereReminder()) {
					int temp = _storage.searchForCorrespondingReminder(_storage
							.getTask(index));
					reminder = _storage.getReminder(temp).getReminderTime()
							.getTime();
					isThereReminder = true;
				}
			}
			if (folder == -1) {
				folder = _storage.getTask(index).getFolder();
			}
			isTaskDeleted = deleteTask(index);
			isTaskAdded = addTask(folder, priorityInt, start.getTime(),
					end.getTime(), isThereReminder, isTaskDone, isAllDayEvent,
					title, location, reminder);
		} else {
			if (index < _storage.getSize()) {
				boolean isTaskDone = false;
				boolean isAllDayEvent = false;
				if (priority == "null") {
					priorityInt = _storage.getFloatingTask(index).getPriority();
				} else {
					priorityInt = Integer.parseInt(priority);
				}
				if (title == null) {
					title = _storage.getFloatingTask(index).getTaskTitle();
				}
				if (location == null) {
					location = _storage.getFloatingTask(index).getLocation();
				}
				if (start == null) {
					start = Calendar.getInstance();
				}
				if (end == null) {
					end.setTime(start.getTime());
					end.add(Calendar.DAY_OF_YEAR, 1);
				}
				if (folder == -1) {
					folder = _storage.getTask(index).getFolder();
				}
				isTaskDeleted = deleteTask(index);
				isTaskAdded = addTask(folder, priorityInt, start.getTime(),
						end.getTime(), isThereReminder, isTaskDone,
						isAllDayEvent, title, location, reminder);
			} else {
				systemMessage.setSystemMessage(MESSAGE_TASK_INDEX_INVALID);
			}
		}
		isTaskUpdated = isTaskDeleted && isTaskAdded;
		return isTaskUpdated;

	}

	/**
	 * @author A0097416X
	 */
	// this method is used to SOLELY add floating tasks
	public static boolean addFloatingTask(int folder, int priority,
			boolean isTaskDone, String title, String location) {
		int dummyID = 0;
		FloatingTask temp = new FloatingTask(folder, dummyID, priority,
				isTaskDone, title, location);
		_storage.addFloatingTask(temp);
		return true;

	}

	/**
	 * @author A0097416X
	 */
	// this method modifies a current floating task accordingly
	public static void updateFloatingTask(int folder, String indexString,
			String priority, String title, String location)
			throws IndexOutOfBoundsException {

		int priorityInt, index = Integer.parseInt(indexString);

		if (index < _storage.getTaskSize()) {
			throw new IndexOutOfBoundsException();
		} else {

			if (index < _storage.getSize() && index >= _storage.getTaskSize()) {
				boolean isTaskDone = _storage.getFloatingTask(index)
						.getIsTaskDone();

				if (priority == "null") {
					priorityInt = _storage.getFloatingTask(index).getPriority();
				} else {
					priorityInt = Integer.parseInt(priority);
				}
				if (title == null) {
					title = _storage.getFloatingTask(index).getTaskTitle();
				}
				if (location == null) {
					location = _storage.getFloatingTask(index).getLocation();
				}
				if (folder == -1) {
					folder = _storage.getFloatingTask(index).getFolder();
				}

				_storage.getFloatingTask(index).setFolder(folder);
				_storage.getFloatingTask(index).setPriority(priorityInt);
				_storage.getFloatingTask(index).setLocation(location);
				_storage.getFloatingTask(index).setTaskTitle(title);
				_storage.getFloatingTask(index).setIsTaskDone(isTaskDone);

				return;

			} else {
				systemMessage.setSystemMessage(MESSAGE_TASK_INDEX_INVALID);
			}
		}
	}

	public static void displayAll() {
		int count = 0;
		LinkedList<TaskWithReminder> list = new LinkedList<TaskWithReminder>();
		LinkedList<FloatingTask> list2 = new LinkedList<FloatingTask>();
		while (count < _storage.getSize()) {
			displayTask(count, list, list2);
			count = count + 1;
		}
		systemMessage.setTimedList(list);
		systemMessage.setFloatingList(_storage.getFloatingList());
		return;
	}

	/**
	 * @author A0097416X
	 */
	// this method displays all the floating tasks only(updates the
	// systemMessage)
	public static void displayAllFloat() {
		int count = _storage.getTaskSize();
		LinkedList<TaskWithReminder> list = new LinkedList<TaskWithReminder>();
		LinkedList<FloatingTask> list2 = new LinkedList<FloatingTask>();
		while (count < _storage.getSize()) {
			displayTask(count, list, list2);
			count = count + 1;
		}
		systemMessage.setTimedList(list);
		systemMessage.setFloatingList(_storage.getFloatingList());
	}

	/**
	 * @author A0097416X
	 */
	// this method clears all timed task and floating task of the folder
	public static void clearFolder(int folder) {
		int count = 0;

		while (count < _storage.getSize()) {
			if (count < _storage.getTaskSize()) {
				if (_storage.getTask(count).getFolder() == folder) {
					deleteTask(count);
				} else {
					count++;
				}
			} else {
				if (_storage.getFloatingTask(count).getFolder() == folder) {
					deleteTask(count);
				} else {
					count++;
				}
			}
		}

	}

	/**
	 * 
	 * 
	 * @author A0097416X
	 */
	// this method displays all tasks for that particular week
	public static void displayWeek() {
		Calendar monday = Calendar.getInstance();
		monday.add(Calendar.DAY_OF_WEEK,
				monday.getFirstDayOfWeek() - monday.get(Calendar.DAY_OF_WEEK));

		Calendar sunday = (Calendar) monday.clone();
		sunday.add(Calendar.DAY_OF_WEEK, 6);

		displayTasksAtPeriod(monday.getTime(), sunday.getTime());
		return;
	}

	public static void displayNow() {
		Calendar start = Calendar.getInstance();
		Calendar end = start;
		end.add(Calendar.MINUTE, 1);
		displayTasksAtPeriod(start.getTime(), end.getTime());
		return;
	}

	// @author A0094655U
	public static void displayToday() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		displayTasksAtDate(today.getTime());
	}

	// @author A0094655U
	public static void displayTomorrow() {
		// long tomorrowInMillis = System.currentTimeMillis()
		// + CONSTANT_MILLISECONDS_IN_A_DAY;

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.set(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH),
				tomorrow.get(Calendar.DATE) + 1, 0, 0);
		// Date tomorrow = new Date(tomorrowInMillis);
		displayTasksAtDate(tomorrow.getTime());
	}

	public static boolean taskIsDone(int index) {
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks && index >= 0) {
			if (index < _storage.getTaskSize() && index >= 0) {
				_storage.getTask(index).setIsTaskDone(true);
			} else {
				_storage.getFloatingTask(index).setIsTaskDone(true);
			}
			return true;
		} else {
			systemMessage.setSystemMessage(MESSAGE_TASK_INDEX_INVALID);
			return false;
		}

	}

	public static boolean taskIsNotDone(int index) {
		int totalNumOfTasks = _storage.getSize();
		if (index < totalNumOfTasks && index >= 0) {
			if (index < _storage.getTaskSize() && index >= 0) {
				_storage.getTask(index).setIsTaskDone(false);
			} else {
				_storage.getFloatingTask(index).setIsTaskDone(false);
			}
			return true;
		} else {
			systemMessage.setSystemMessage(MESSAGE_TASK_INDEX_INVALID);
			return false;
		}
	}

	// @author A0094655U
	private static boolean isInString(String mainString, String subString) {
		if (subString.trim().length() == 0) {
			return false;
		}
		return (mainString.toLowerCase().contains(subString.toLowerCase()));
	}
}
